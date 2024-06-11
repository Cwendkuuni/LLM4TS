import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.dataflow.DataFlowAnalysis;
import com.google.javascript.jscomp.dataflow.LiveVariablesAnalysis;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.rhino.Node;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LiveVariablesAnalysisTest {

  private static Node createSimpleAssignmentNode(int index) {
    Node n = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.NAME, String.valueOf(index));
    Node rhs = new Node(Token.INT_LITERAL, 42);
    n.addChildrenToEnd(lhs, rhs);
    return n;
  }

  private static Node createSimpleStatementNode(int index) {
    Node n = new Node(Token.VAR, String.valueOf(index));
    Node value = new Node(Token.INT_LITERAL, 42);
    n.addChildrenToEnd(value);
    return n;
  }

  private static Node createWhileNode(int index) {
    Node n = new Node(Token.WHILE);
    Node condition = new Node(Token.INT_LITERAL, 1);
    Node body = new Node(Token.ASSIGN, new Node(Token.NAME, String.valueOf(index)),
        createSimpleAssignmentNode(index));
    n.addChildrenToEnd(condition, body);
    return n;
  }

  private static Node createFuncDeclarationNode(String name, int index) {
    Node n = new Node(Token.FUNCTION);
    Node nameNode = new Node(Token.NAME, name);
    n.addChildrenToEnd(nameNode);
    Node body = new Node(Token.BLOCK);
    Node assignment = createSimpleAssignmentNode(index);
    body.addChildrenToEnd(assignment);
    n.addChildrenToEnd(body);
    return n;
  }

  private static Node createFuncCallNode(String name, int index) {
    Node n = new Node(Token.NAME, name);
    Node arguments = new Node(Token.ARRAY_LITERAL, Arrays.asList(new Node(Token.INT_LITERAL, index)));
    n.addChildrenToEnd(arguments);
    return n;
  }

  private static Node createForInNode(int index) {
    Node n = new Node(Token.FOR);
    Node init = new Node(Token.VAR, String.valueOf("i"));
    init.addChildrenToEnd(new Node(Token.NAME, "i"),
        new Node(Token.ASSIGN, new Node(Token.NAME, "i"), new Node(Token.INT_LITERAL, 0)));
    Node condition = new Node(Token.INT_LITERAL, 1);
    Node increment = new Node(Token.ASSIGN, new Node(Token.NAME, "i"),
        new Node(Token.PLUS, new Node(Token.NAME, "i"), new Node(Token.INT_LITERAL, 1)));
    Node body = new Node(Token.ASSIGN, new Node(Token.NAME, String.valueOf(index)),
        new Node(Token.NAME, String.valueOf("i")));
    n.addChildrenToEnd(init, condition, increment, body);
    return n;
  }

  private static Node createBlockNode(int... indices) {
    Node n = new Node(Token.BLOCK);
    for (int index : indices) {
      Node statement = createSimpleStatementNode(index);
      n.addChildrenToEnd(statement);
    }
    return n;
  }

  private static Node createScriptNode() {
    Node n = new Node(Token.SCRIPT);
    Node chain = new Node(Token.BLOCK);
    n.addChildrenToEnd(chain);
    return n;
  }

  private static ControlFlowGraph<Node> createControlFlowGraph(Node root) {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    cfg.build(root, Collections.emptyList());
    return cfg;
  }

  private static LiveVariablesAnalysis createLiveVariablesAnalysis(ControlFlowGraph<Node> cfg, Scope scope) {
    return new LiveVariablesAnalysis(cfg, scope, null);
  }

  @org.junit.Test
  public void testSimpleAssignment() {
    Scope scope = new Scope(createScriptNode());
    Node root = createSimpleAssignmentNode(0);
    ControlFlowGraph<Node> cfg = createControlFlowGraph(root);
    LiveVariablesAnalysis liveVariablesAnalysis = createLiveVariablesAnalysis(cfg, scope);
    LiveVariablesAnalysis.LiveVariableLattice result = liveVariablesAnalysis.computeResult(createEntryLattice(), root);
    LiveVariablesAnalysis.LiveVariableLattice expected = new LiveVariablesAnalysis.LiveVariableLattice(1);
    expected.liveSet.set(0);
    assertEquals(expected, result);
  }

  @org.junit.Test
  public void testSimpleStatement() {
    Scope scope = new Scope(createScriptNode());
    Node root = createSimpleStatementNode(0);
    ControlFlowGraph<Node> cfg = createControlFlowGraph(root);
    LiveVariablesAnalysis liveVariablesAnalysis = createLiveVariablesAnalysis(cfg, scope);
    LiveVariablesAnalysis.LiveVariableLattice result = liveVariablesAnalysis.computeResult(createEntryLattice(), root);
    LiveVariablesAnalysis.LiveVariableLattice expected = new LiveVariablesAnalysis.LiveVariableLattice(1);
    expected.liveSet.set(0);
    assertEquals(expected, result);
  }

  @org.junit.Test
  public void testWhile() {
    Scope scope = new Scope(createScriptNode());
    Node root = createWhileNode(0);
    ControlFlowGraph<Node> cfg = createControlFlowGraph(root);
    LiveVariablesAnalysis liveVariablesAnalysis = createLiveVariablesAnalysis(cfg, scope);
    LiveVariablesAnalysis.LiveVariableLattice result = liveVariablesAnalysis.computeResult(createEntryLattice(), root);
    LiveVariablesAnalysis.LiveVariableLattice expected = new LiveVariablesAnalysis.LiveVariableLattice(1);
    expected.liveSet.set(0);
    assertEquals(expected, result);
  }

  @org.junit.Test
  public void testFunctionDeclaration() {
    Scope scope = new Scope(createScriptNode());
    Node root = createFuncDeclarationNode("foo", 0);
    ControlFlowGraph<Node> cfg = createControlFlowGraph(root);
    LiveVariablesAnalysis liveVariablesAnalysis = createLiveVariablesAnalysis(cfg, scope);
    LiveVariablesAnalysis.LiveVariableLattice result = liveVariablesAnalysis.computeResult(createEntryLattice(), root);
    LiveVariablesAnalysis.LiveVariableLattice expected = new LiveVariablesAnalysis.LiveVariableLattice(1);
    expected.liveSet.set(0);
    assertEquals(expected, result);
  }

  @org.junit.Test
  public void testFunctionCall() {
    Scope scope = new Scope(createScriptNode());
    Node root = createFuncCallNode("foo", 0);
    ControlFlowGraph<Node> cfg = createControlFlowGraph(root);
    LiveVariablesAnalysis liveVariablesAnalysis = createLiveVariablesAnalysis(cfg, scope);
    LiveVariablesAnalysis.LiveVariableLattice result = liveVariablesAnalysis.computeResult(createEntryLattice(), root);
    LiveVariablesAnalysis.LiveVariableLattice expected = new LiveVariablesAnalysis.LiveVariableLattice(1);
    expected.liveSet.set(0);
    assertEquals(expected, result);
  }

  @org.junit.Test
  public void testForIn() {
    Scope scope = new Scope(createScriptNode());
    Node root = createForInNode(0);
    ControlFlowGraph<Node> cfg = createControlFlowGraph(root);
    LiveVariablesAnalysis liveVariablesAnalysis = createLiveVariablesAnalysis(cfg, scope);
    LiveVariablesAnalysis.LiveVariableLattice result = liveVariablesAnalysis.computeResult(createEntryLattice(), root);
    LiveVariablesAnalysis.LiveVariableLattice expected = new LiveVariablesAnalysis.LiveVariableLattice(1);
    expected.liveSet.set(0);
    assertEquals(expected, result);
  }

  @org.junit.Test
  public void testBlock() {
    Scope scope = new Scope(createScriptNode());
    Node root = createBlockNode(0);
    ControlFlowGraph<Node> cfg = createControlFlowGraph(root);
    LiveVariablesAnalysis liveVariablesAnalysis = createLiveVariablesAnalysis(cfg, scope);
    LiveVariablesAnalysis.LiveVariableLattice result = liveVariablesAnalysis.computeResult(createEntryLattice(), root);
    LiveVariablesAnalysis.LiveVariableLattice expected = new LiveVariablesAnalysis.LiveVariableLattice(1);
    expected.liveSet.set(0);
    assertEquals(expected, result);
  }

  @org.junit.Test
  public void testEscapedVariables() {
    Scope scope = new Scope(createScriptNode());
    Node root = createFuncDeclarationNode("foo", 0);
    ControlFlowGraph<Node> cfg = createControlFlowGraph(root);
    LiveVariablesAnalysis liveVariablesAnalysis = createLiveVariablesAnalysis(cfg, scope);
    Set<Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();
    assertTrue(escapedLocals.isEmpty());

    scope = new Scope(createScriptNode());
    Node root2 = createFuncDeclarationNode("foo", 0);
    Node arguments = root2.getFirstChild();
    Node argsNode = arguments.getFirstChild();
    argsNode.setString("arguments");
    ControlFlowGraph<Node> cfg2 = createControlFlowGraph(root2);
    LiveVariablesAnalysis liveVariablesAnalysis2 = createLiveVariablesAnalysis(cfg2, scope);
    Set<Var> escapedLocals2 = liveVariablesAnalysis2.getEscapedLocals();
    assertEquals(1, escapedLocals2.size());
  }
}