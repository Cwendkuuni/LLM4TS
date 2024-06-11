import static org.junit.Assert.*;
import static com.google.javascript.jscomp.MustBeReachingVariableDef.*;

import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

public class MustBeReachingVariableDefTest {

  private ControlFlowGraph<Node> cfg;
  private Scope jsScope;
  private AbstractCompiler compiler;

  @Before
  public void setup() {
    // Create a simple control flow graph for testing purposes.
    Node rootNode = Node.newBlock("root");
    Node entryNode = Node.newBlock("entry");
    Node assignmentNode = Node.newAssign("x", Node.newName("y"), null);
    Node ifNode = Node.newIf(Node.newBinaryOp(Token.OP_EQ, Node.newName("x"), Node.newInt(1)), null, null);
    Node assignmentNodeIfTrue = Node.newAssign("z", Node.newName("x"), null);
    Node ifNodeElse = Node.newBlock("else");
    Node assignmentNodeIfElse = Node.newAssign("w", Node.newInt(42), null);
    Node exitNode = Node.newBlock("exit");

    cfg = new ControlFlowGraph<>(rootNode);
    cfg.addEdge(entryNode, assignmentNode);
    cfg.addEdge(assignmentNode, ifNode);
    cfg.addEdge(ifNode, assignmentNodeIfTrue, Branch.BRANCH_TRUE);
    cfg.addEdge(ifNode, ifNodeElse, Branch.BRANCH_FALSE);
    cfg.addEdge(assignmentNodeIfTrue, exitNode);
    cfg.addEdge(ifNodeElse, assignmentNodeIfElse);
    cfg.addEdge(assignmentNodeIfElse, exitNode);

    // Create a simple scope for testing purposes.
    jsScope = new Scope();
    jsScope.setRootNode(rootNode);

    // Create a simple abstract compiler for testing purposes.
    compiler = new AbstractCompiler() {};
  }

  @Test
  public void mustBeReachingVariableDef_BasicTest() {
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    Node useNode = Node.newGetProp(Node.newName("x"), "toString");

    MustDef expected = new MustDef();
    expected.reachingDef.put(jsScope.getVar("x"), new Definition(assignmentNode));
    expected.reachingDef.put(jsScope.getVar("y"), new Definition(assignmentNode));

    MustDef actual = analyzer.flowThrough(cfg.getEntryNode(), expected);

    assertEquals(expected, actual);
  }

  @Test
  public void mustBeReachingVariableDef_ConditionalTest() {
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    Node useNodeIfTrue = Node.newGetProp(Node.newName("z"), "toString");
    Node useNodeIfFalse = Node.newGetProp(Node.newName("w"), "toString");

    MustDef expectedIfTrue = new MustDef();
    expectedIfTrue.reachingDef.put(jsScope.getVar("x"), new Definition(assignmentNode));
    expectedIfTrue.reachingDef.put(jsScope.getVar("y"), new Definition(assignmentNode));
    expectedIfTrue.reachingDef.put(jsScope.getVar("z"), new Definition(assignmentNodeIfTrue));

    MustDef expectedIfFalse = new MustDef();
    expectedIfFalse.reachingDef.put(jsScope.getVar("x"), new Definition(assignmentNode));
    expectedIfFalse.reachingDef.put(jsScope.getVar("y"), new Definition(assignmentNode));
    expectedIfFalse.reachingDef.put(jsScope.getVar("w"), new Definition(assignmentNodeIfElse));

    MustDef actualIfTrue = analyzer.flowThrough(cfg.getNode(ifNode), expectedIfTrue);
    MustDef actualIfFalse = analyzer.flowThrough(cfg.getNode(ifNodeElse), expectedIfFalse);

    assertEquals(expectedIfTrue, actualIfTrue);
    assertEquals(expectedIfFalse, actualIfFalse);
  }

  @Test
  public void mustBeReachingVariableDef_EdgeCasesTest() {
    // Test for variables that are defined but not used.
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    Node unusedNode = Node.newAssign("unused", Node.newInt(42), null);
    MustDef expected = new MustDef();
    MustDef actual = analyzer.flowThrough(unusedNode, expected);
    assertTrue(expected.reachingDef.isEmpty());
    assertTrue(actual.reachingDef.isEmpty());

    // Test for variables that are defined and used multiple times.
    MustDef expectedMultipleUse = new MustDef();
    expectedMultipleUse.reachingDef.put(jsScope.getVar("x"), new Definition(assignmentNode));
    MustDef actualMultipleUse =
        analyzer.flowThrough(cfg.getEntryNode(), expectedMultipleUse);
    assertEquals(expectedMultipleUse, actualMultipleUse);

    // Test for variables that are defined and used outside of their scope.
    MustBeReachingVariableDef analyzerOutOfScope =
        new MustBeReachingVariableDef(cfg, jsScope, compiler);
    Node useNodeOutOfScope = Node.newGetProp(Node.newName("x"), "toString");
    MustDef expectedOutOfScope = new MustDef();
    expectedOutOfScope.reachingDef.put(jsScope.getVar("x"), new Definition(assignmentNode));
    MustDef actualOutOfScope = analyzerOutOfScope.flowThrough(useNodeOutOfScope, expectedOutOfScope);
    assertNull(actualOutOfScope);

    // Test for variables that are defined conditionally and used unconditionally.
    MustDef expectedConditionalUse = new MustDef();
    expectedConditionalUse.reachingDef.put(jsScope.getVar("x"), new Definition(assignmentNode));
    MustDef actualConditionalUse = analyzer.flowThrough(cfg.getNode(ifNode), expectedConditionalUse);
    assertEquals(expectedConditionalUse, actualConditionalUse);

    // Test for variables that are defined conditionally and used conditionally.
    MustDef expectedAndExpectedConditionalUse = new MustDef();
    expectedAndExpectedConditionalUse.reachingDef.put(jsScope.getVar("x"), new Definition(assignmentNode));
    MustDef actualAndExpectedConditionalUse = analyzer.flowThrough(cfg.getNode(ifNode), expectedAndExpectedConditionalUse);
    assertEquals(expectedAndExpectedConditionalUse, actualAndExpectedConditionalUse);

    // Test for variables that are defined unconditionally and used conditionally.
    MustDef expectedUnconditionalConditionalUse = new MustDef();
    expectedUnconditionalConditionalUse.reachingDef.put(jsScope.getVar("x"), new Definition(assignmentNode));
    MustDef actualUnconditionalConditionalUse = analyzer.flowThrough(ifNode, expectedUnconditionalConditionalUse);
    assertNotNull(actualUnconditionalConditionalUse);
    assertTrue(actualUnconditionalConditionalUse.reachingDef.isEmpty());
  }

  @Test
  public void mustBeReachingVariableDef_ExceptionHandlingTest() {
    // Create a simple control flow graph for exception handling testing purposes.
    Node rootNode = Node.newBlock("root");
    Node entryNode = Node.newBlock("entry");
    Node tryNode = Node.newTry();
    Node assignmentNode = Node.newAssign("x", Node.newName("y"), null);
    Node throwNode = Node.newThrow(Node.newName("Error"));
    Node catchNode = Node.newCatch(Node.newName("Error"), null);
    Node assignmentNodeInCatch = Node.newAssign("z", Node.newName("x"), null);
    Node exitNode = Node.newBlock("exit");

    cfg = new ControlFlowGraph<>(rootNode);
    cfg.addEdge(entryNode, tryNode);
    cfg.addEdge(tryNode, assignmentNode);
    cfg.addEdge(assignmentNode, throwNode);
    cfg.addEdge(throwNode, catchNode);
    cfg.addEdge(assignmentNodeInCatch, exitNode);
    cfg.addEdge(catchNode, exitNode);

    // Create a simple scope for exception handling testing purposes.
    jsScope = new Scope();
    jsScope.setRootNode(rootNode);

    // Create a simple abstract compiler for exception handling testing purposes.
    compiler = new AbstractCompiler() {};

    // Test for variables that are defined and used within a try block.
    MustBeReachingVariableDef analyzerTry = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    Node useNodeTry = Node.newGetProp(Node.newName("x"), "toString");
    MustDef expectedTry = new MustDef();
    expectedTry.reachingDef.put(jsScope.getVar("x"), new Definition(assignmentNode));
    MustDef actualTry = analyzerTry.flowThrough(cfg.getNode(tryNode), expectedTry);
    assertEquals(expectedTry, actualTry);

    // Test for variables that are defined and used within a catch block.
    MustBeReachingVariableDef analyzerCatch = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    Node useNodeCatch = Node.newGetProp(Node.newName("z"), "toString");
    MustDef expectedCatch = new MustDef();
    expectedCatch.reachingDef.put(jsScope.getVar("x"), new Definition(assignmentNode));
    expectedCatch.reachingDef.put(jsScope.getVar("z"), new Definition(assignmentNodeInCatch));
    MustDef actualCatch = analyzerCatch.flowThrough(cfg.getNode(catchNode), expectedCatch);
    assertEquals(expectedCatch, actualCatch);
  }
}