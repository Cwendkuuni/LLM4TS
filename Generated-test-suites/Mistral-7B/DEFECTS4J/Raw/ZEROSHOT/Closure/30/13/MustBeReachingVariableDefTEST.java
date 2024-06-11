import static org.junit.Assert.*;
import static com.google.javascript.jscomp.MustBeReachingVariableDef.*;

import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.GraphNode;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class MustBeReachingVariableDefTest {

  @Test
  public void testMustBeReachingVariableDef() {
    Scope scope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();

    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, scope, compiler);

    Node node1 = Node.newBlock(new Token(Token.BLOCK, 0, 0));
    Node node2 = Node.newAssign("x", Node.newName("y"), Node.newNumber(1));
    Node node3 = Node.newAssign("x", Node.newName("z"), Node.newNumber(2));
    Node node4 = Node.newAssign("y", Node.newName("x"));
    Node node5 = Node.newReturn(Node.newName("x"));

    Node entryNode = Node.newBlock(new Token(Token.BLOCK, 0, 0));
    entryNode.addChild(node1);
    entryNode.addChild(node2);
    entryNode.addChild(node3);
    entryNode.addChild(node4);
    entryNode.addChild(node5);

    cfg.addEntryNode(entryNode);

    Map<Node, GraphNode<Node, Branch>> nodeMap = cfg.getNodesMap();
    Node useNode = nodeMap.get(node5);

    MustDef expected = new MustDef();
    expected.reachingDef.put(scope.getVar("x"), new Definition(node3));
    expected.reachingDef.put(scope.getVar("y"), new Definition(node2));

    MustDef actual = analyzer.flowThrough(node5, analyzer.createEntryLattice());

    assertEquals(expected, actual);
  }

  @Test
  public void testMustBeReachingVariableDef_dependsOnOuterScopeVars() {
    Scope scope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();

    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, scope, compiler);

    Node node1 = Node.newBlock(new Token(Token.BLOCK, 0, 0));
    Node node2 = Node.newAssign("x", Node.newName("outerScopeVar"), Node.newNumber(1));
    Node node3 = Node.newAssign("x", Node.newName("y"), Node.newNumber(1));
    Node node4 = Node.newAssign("y", Node.newName("x"));
    Node node5 = Node.newReturn(Node.newName("x"));

    Node entryNode = Node.newBlock(new Token(Token.BLOCK, 0, 0));
    entryNode.addChild(node1);
    entryNode.addChild(node2);
    entryNode.addChild(node3);
    entryNode.addChild(node4);
    entryNode.addChild(node5);

    cfg.addEntryNode(entryNode);

    Map<Node, GraphNode<Node, Branch>> nodeMap = cfg.getNodesMap();
    Node useNode = nodeMap.get(node5);

    assertFalse(analyzer.dependsOnOuterScopeVars("x", useNode));

    Node outerScopeNode = Node.newVar("outerScopeVar");
    cfg.addNode(outerScopeNode, null);
    analyzer = new MustBeReachingVariableDef(cfg, scope, compiler);

    MustDef actual = analyzer.flowThrough(node5, analyzer.createEntryLattice());

    assertTrue(actual.reachingDef.containsKey(scope.getVar("x")));
    assertTrue(actual.reachingDef.get(scope.getVar("x")).depends.isEmpty());
  }

  @Test
  public void testMustBeReachingVariableDef_getDef() {
    Scope scope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();

    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, scope, compiler);

    Node node1 = Node.newBlock(new Token(Token.BLOCK, 0, 0));
    Node node2 = Node.newAssign("x", Node.newName("y"), Node.newNumber(1));
    Node node3 = Node.newAssign("x", Node.newName("z"), Node.newNumber(2));
    Node node4 = Node.newAssign("y", Node.newName("x"));
    Node node5 = Node.newAssign("y", Node.newName("x"));
    Node node6 = Node.newReturn(Node.newName("x"));

    Node entryNode = Node.newBlock(new Token(Token.BLOCK, 0, 0));
    entryNode.addChild(node1);
    entryNode.addChild(node2);
    entryNode.addChild(node3);
    entryNode.addChild(node4);
    entryNode.addChild(node5);
    entryNode.addChild(node6);

    cfg.addEntryNode(entryNode);

    Map<Node, GraphNode<Node, Branch>> nodeMap = cfg.getNodesMap();
    Node useNode = nodeMap.get(node6);

    MustDef expected = new MustDef();
    expected.reachingDef.put(scope.getVar("x"), new Definition(node3));
    expected.reachingDef.put(scope.getVar("y"), new Definition(node3));

    analyzer.compute(useNode, null);

    Node defNodeX = analyzer.getDef("x", useNode);
    Node defNodeY = analyzer.getDef("y", useNode);

    assertEquals(node3, defNodeX);
    assertEquals(node3, defNodeY);
  }
}