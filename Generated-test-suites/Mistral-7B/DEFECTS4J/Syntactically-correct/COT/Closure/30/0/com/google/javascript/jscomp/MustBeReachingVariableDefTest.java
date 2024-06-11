import static org.junit.Assert.*;

import com.google.javascript.jscomp.MustBeReachingVariableDef;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

public class MustBeReachingVariableDefTest {

  private Scope jsScope;
  private AbstractCompiler compiler;
  private MustBeReachingVariableDef mustBeReachingVariableDef;

  @Before
  public void setup() {
    jsScope = new Scope();
    compiler = new AbstractCompiler();
    mustBeReachingVariableDef = new MustBeReachingVariableDef(
        null, jsScope, compiler);
  }

  @Test
  public void testBasicFunctionality() {
    Node entryNode = Node.newBlock(Token.BLOCK);
    Node assignmentNode = Node.newAssign("x", Node.newNumber(5));
    Node useNode = Node.newName("x");

    Node cfgEntryNode = Node.newBlock(Token.BLOCK);
    Node cfgAssignmentNode = Node.newAssign(
        Node.newName("x"), Node.newNumber(3));
    Node cfgUseNode = Node.newName("x");

    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(
        "MustBeReachingVariableDefTest", new Node[]{cfgEntryNode,
            cfgAssignmentNode, cfgUseNode}, new Branch[]{
            new Branch(cfgEntryNode, cfgAssignmentNode),
            new Branch(cfgAssignmentNode, cfgUseNode),
            new Branch(cfgAssignmentNode, cfgEntryNode),
            new Branch(cfgUseNode, cfgEntryNode)});

    mustBeReachingVariableDef = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node result = mustBeReachingVariableDef.getDef("x", cfgUseNode);
    assertNotNull(result);
    assertEquals(cfgAssignmentNode, result);

    boolean dependsOnOuterScope = mustBeReachingVariableDef.dependsOnOuterScopeVars(
        "x", cfgUseNode);
    assertFalse(dependsOnOuterScope);
  }

  @Test
  public void testNullArguments() {
    // Test with null entry node and null use node
    ControlFlowGraph<Node> cfg = null;
    Node useNode = null;

    try {
      Node result = mustBeReachingVariableDef.getDef("x", useNode);
      fail("Expected IllegalArgumentException but got null result");
    } catch (IllegalArgumentException e) {
      // Expected
    }

    try {
      boolean dependsOnOuterScope = mustBeReachingVariableDef.dependsOnOuterScopeVars(
          "x", useNode);
      fail("Expected IllegalArgumentException but got null result");
    } catch (IllegalArgumentException e) {
      // Expected
    }

    // Test with null control flow graph
    mustBeReachingVariableDef = new MustBeReachingVariableDef(null, jsScope, compiler);

    try {
      Node result = mustBeReachingVariableDef.getDef("x", cfgUseNode);
      fail("Expected IllegalArgumentException but got null result");
    } catch (IllegalArgumentException e) {
      // Expected
    }

    try {
      boolean dependsOnOuterScope = mustBeReachingVariableDef.dependsOnOuterScopeVars(
          "x", cfgUseNode);
      fail("Expected IllegalArgumentException but got null result");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testNonExistentVariable() {
    Node entryNode = Node.newBlock(Token.BLOCK);
    Node assignmentNode = Node.newAssign("x", Node.newNumber(5));
    Node useNode = Node.newName("y");

    Node cfgEntryNode = Node.newBlock(Token.BLOCK);
    Node cfgAssignmentNode = Node.newAssign(
        Node.newName("x"), Node.newNumber(3));
    Node cfgUseNode = Node.newName("x");

    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(
        "MustBeReachingVariableDefTest", new Node[]{cfgEntryNode,
            cfgAssignmentNode, cfgUseNode}, new Branch[]{
            new Branch(cfgEntryNode, cfgAssignmentNode),
            new Branch(cfgAssignmentNode, cfgUseNode),
            new Branch(cfgAssignmentNode, cfgEntryNode),
            new Branch(cfgUseNode, cfgEntryNode)});

    mustBeReachingVariableDef = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node result = mustBeReachingVariableDef.getDef("y", cfgUseNode);
    assertNull(result);

    boolean dependsOnOuterScope = mustBeReachingVariableDef.dependsOnOuterScopeVars(
        "y", cfgUseNode);
    assertFalse(dependsOnOuterScope);
  }

  @Test
  public void testEscapeVariable() {
    Scope escapedScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    MustBeReachingVariableDef mustBeReachingVariableDef = new MustBeReachingVariableDef(
        null, escapedScope, compiler);

    Node entryNode = Node.newBlock(Token.BLOCK);
    Node assignmentNode = Node.newAssign("x", Node.newName("y"));
    Node useNode = Node.newName("x");

    Node cfgEntryNode = Node.newBlock(Token.BLOCK);
    Node cfgAssignmentNode = Node.newAssign(
        Node.newName("x"), Node.newNumber(3));
    Node cfgUseNode = Node.newName("x");

    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(
        "MustBeReachingVariableDefTest", new Node[]{cfgEntryNode,
            cfgAssignmentNode, cfgUseNode}, new Branch[]{
            new Branch(cfgEntryNode, cfgAssignmentNode),
            new Branch(cfgAssignmentNode, cfgUseNode),
            new Branch(cfgAssignmentNode, cfgEntryNode),
            new Branch(cfgUseNode, cfgEntryNode)});

    mustBeReachingVariableDef.jsScope.setVar("y", new Var(Node.newName("y"), 0));
    mustBeReachingVariableDef.escaped.add(mustBeReachingVariableDef.jsScope.getVar("y"));

    Node result = mustBeReachingVariableDef.getDef("x", cfgUseNode);
    assertNull(result);

    boolean dependsOnOuterScope = mustBeReachingVariableDef.dependsOnOuterScopeVars(
        "x", cfgUseNode);
    assertFalse(dependsOnOuterScope);
  }
}