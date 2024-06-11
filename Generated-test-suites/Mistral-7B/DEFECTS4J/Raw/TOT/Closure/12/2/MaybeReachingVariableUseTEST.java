import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.jscomp.graph.GraphNode;
import com.google.javascript.jscomp.graph.LatticeElement;
import com.google.javascript.rhino.Node;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MaybeReachingVariableUseTest {

  private static Scope createScope() {
    // TODO: Create a test scope with some variables and functions.
    return new Scope();
  }

  @org.junit.Test
  public void testEmpty() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = createScope();
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);

    ReachingUses output = analysis.getEntryValue();
    assertNotNull(output);
    assertTrue(output.mayUseMap.isEmpty());
  }

  @org.junit.Test
  public void testSimpleAssignment() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = createScope();
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);

    // Assignment node
    Node defNode = Node.newAssignment(Token.ASSIGN, new Node.Name("x"), new Node.Number(1));
    GraphNode<Node, Branch> def = cfg.addNode(defNode);

    ReachingUses output = analysis.computeResult(def, null, null);
    assertNotNull(output);
    Set<Node> uses = new HashSet<>();
    uses.add(defNode);
    assertUsesEqual(uses, output.mayUseMap);
  }

  @org.junit.Test
  public void testSimpleAssignmentWithCondition() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = createScope();
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);

    // Assignment node
    Node defNode = Node.newAssignment(Token.ASSIGN, new Node.Name("x"), new Node.Number(1));
    Node condNode = Node.newIf(Token.IF, new Node.BooleanLiteral(true), new Node.Block(Token.BLOCK));
    GraphNode<Node, Branch> def = cfg.addNode(new Node.Sequence(Token.SEQUENCE, defNode, condNode));

    ReachingUses output = analysis.computeResult(def, null, null);
    assertNotNull(output);
    Set<Node> uses = new HashSet<>();
    uses.add(defNode);
    assertUsesEqual(uses, output.mayUseMap);
  }

  @org.junit.Test
  public void testComplexAssignmentWithConditionalAndLoop() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = createScope();
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);

    // Variable declaration
    Node varNode = Node.newVar(Token.VAR, new Node.Name("x"), new Node.Number(0), new Node.Name("x"), null);
    GraphNode<Node, Branch> var = cfg.addNode(varNode);

    // Assignment node
    Node defNode = Node.newAssignment(Token.ASSIGN, new Node.Name("x"), Node.newNumber(1));
    GraphNode<Node, Branch> def = cfg.addNode(defNode);
    cfg.addEdge(var, def, DiGraphEdge.CONTROL_FLOW);

    // Conditional node
    Node condNode = Node.newIf(Token.IF, new Node.BooleanLiteral(true), new Node.Block(Token.BLOCK));
    GraphNode<Node, Branch> cond = cfg.addNode(condNode);

    // Loop node
    Node loopNode = Node.newWhile(Token.WHILE, Node.newName("b"), new Node.Block(Token.BLOCK));
    GraphNode<Node, Branch> loop = cfg.addNode(loopNode);

    // Add edge to loop from condition
    cfg.addEdge(cond, loop, DiGraphEdge.CONTROL_FLOW);

    // Assignment inside the loop
    Node defNodeInsideLoop = Node.newAssignment(Token.ASSIGN, new Node.Name("x"), Node.newNumber(2));
    GraphNode<Node, Branch> defInsideLoop = cfg.addNode(defNodeInsideLoop);
    cfg.addEdge(loop, defInsideLoop, DiGraphEdge.CONTROL_FLOW);

    ReachingUses output = analysis.computeResult(def, null, null);
    assertNotNull(output);
    Set<Node> uses = new HashSet<>();
    uses.add(defNode);
    uses.addAll(analysis.getUses("x", defNode));
    assertUsesEqual(uses, output.mayUseMap);
  }

  @org.junit.Test
  public void testComplexAssignmentWithConditionalAndLoopAndEscape() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = createScope();
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);

    // Variable declaration
    Node varNode = Node.newVar(Token.VAR, new Node.Name("x"), new Node.Number(0), new Node.Name("x"), null);
    GraphNode<Node, Branch> var = cfg.addNode(varNode);

    // Assignment node
    Node defNode = Node.newAssignment(Token.ASSIGN, new Node.Name("x"), Node.newNumber(1));
    GraphNode<Node, Branch> def = cfg.addNode(defNode);
    cfg.addEdge(var, def, DiGraphEdge.CONTROL_FLOW);

    // Escaped variable
    Var escapedVar = jsScope.declareVar("escaped", Node.TYPE_ANY, null, null);
    cfg.addAnnotation(def, new Node.Assignment(Token.ASSIGN, Node.newName("escaped"), Node.newName("x")));

    // Conditional node
    Node condNode = Node.newIf(Token.IF, new Node.BooleanLiteral(true), new Node.Block(Token.BLOCK));
    GraphNode<Node, Branch> cond = cfg.addNode(condNode);

    // Loop node
    Node loopNode = Node.newWhile(Token.WHILE, Node.newName("b"), new Node.Block(Token.BLOCK));
    GraphNode<Node, Branch> loop = cfg.addNode(loopNode);

    // Add edge to loop from condition
    cfg.addEdge(cond, loop, DiGraphEdge.CONTROL_FLOW);

    // Assignment inside the loop
    Node defNodeInsideLoop = Node.newAssignment(Token.ASSIGN, new Node.Name("x"), Node.newNumber(2));
    GraphNode<Node, Branch> defInsideLoop = cfg.addNode(defNodeInsideLoop);
    cfg.addEdge(loop, defInsideLoop, DiGraphEdge.CONTROL_FLOW);

    ReachingUses output = analysis.computeResult(def, null, null);
    assertNotNull(output);
    Set<Node> uses = new HashSet<>();
    uses.add(defNode);
    uses.addAll(analysis.getUses("x", defNode));
    uses.removeAll(analysis.getUses("escaped", defNode));
    assertUsesEqual(uses, output.mayUseMap);
  }

  @org.junit.Test
  public void testComplexAssignmentWithConditionalAndLoopAndEscapeWithFunction() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = createScope();
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);

    // Variable declaration
    Node varNode = Node.newVar(Token.VAR, new Node.Name("x"), new Node.Number(0), new Node.Name("x"), null);
    GraphNode<Node, Branch> var = cfg.addNode(varNode);

    // Function declaration
    Node funcNode = Node.newFunction(Token.FUNCTION, Node.newName("func"), Node.newArray(), new Node.Block(Token.BLOCK));
    GraphNode<Node, Branch> func = cfg.addNode(funcNode);

    // Assignment node
    Node defNode = Node.newAssignment(Token.ASSIGN, new Node.Name("x"), Node.newNumber(1));
    GraphNode<Node, Branch> def = cfg.addNode(defNode);
    cfg.addEdge(var, def, DiGraphEdge.CONTROL_FLOW);
    cfg.addEdge(def, func, DiGraphEdge.CONTROL_FLOW);

    // Escaped variable
    Var escapedVar = jsScope.declareVar("escaped", Node.TYPE_ANY, null, null);
    cfg.addAnnotation(def, new Node.Assignment(Token.ASSIGN, Node.newName("escaped"), Node.newName("x")));

    // Conditional node
    Node condNode = Node.newIf(Token.IF, new Node.BooleanLiteral(true), new Node.Block(Token.BLOCK));
    GraphNode<Node, Branch> cond = cfg.addNode(condNode);

    // Loop node
    Node loopNode = Node.newWhile(Token.WHILE, Node.newName("b"), new Node.Block(Token.BLOCK));
    GraphNode<Node, Branch> loop = cfg.addNode(loopNode);

    // Add edge to loop from condition
    cfg.addEdge(cond, loop, DiGraphEdge.CONTROL_FLOW);

    // Assignment inside the loop
    Node defNodeInsideLoop = Node.newAssignment(Token.ASSIGN, new Node.Name("x"), Node.newNumber(2));
    GraphNode<Node, Branch> defInsideLoop = cfg.addNode(defNodeInsideLoop);
    cfg.addEdge(loop, defInsideLoop, DiGraphEdge.CONTROL_FLOW);

    ReachingUses output = analysis.computeResult(def, null, null);
    assertNotNull(output);
    Set<Node> uses = new HashSet<>();
    uses.add(defNode);
    uses.addAll(analysis.getUses("x", defNode));
    uses.removeAll(analysis.getUses("escaped", defNode));
    uses.removeAll(analysis.getUses("func", defNode));
    assertUsesEqual(uses, output.mayUseMap);
  }

  private void assertUsesEqual(Set<Node> expectedUses, Multimap<Var, Node> actualUses) {
    for (Node use : expectedUses) {
      assertTrue(actualUses.containsKey(jsScope.getVar(use.getString())));
      assertTrue(actualUses.get(jsScope.getVar(use.getString())).contains(use));
    }
  }
}