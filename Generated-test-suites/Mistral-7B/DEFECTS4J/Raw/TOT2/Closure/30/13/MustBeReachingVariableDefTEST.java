import com.google.javascript.jscomp.MustBeReachingVariableDef;
import com.google.javascript.jscomp.Scope;
import org.junit.Test;
import static org.junit.Assert.*;

public class MustBeReachingVariableDefTest {

  @Test
  public void testSimpleAssignment() {
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node rootNode = jsScope.getRootNode();
    Node assignmentNode = Node.newAssignment(rootNode, rootNode.getChild(0),
        Node.newNumberLiteral(rootNode, 10));

    analysis.compute();
    Node expectedDef = rootNode.getChild(0);
    Node actualDef = analysis.getDef("a", assignmentNode);
    assertEquals("Expected definition 'a' to be 'a' but was: " + actualDef,
        expectedDef, actualDef);
  }

  @Test
  public void testAssignmentWithDependencies() {
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node rootNode = jsScope.getRootNode();
    Node aNode = Node.newVar(rootNode, "a");
    Node bNode = Node.newVar(rootNode, "b");
    Node cNode = Node.newVar(rootNode, "c");
    Node assignmentNode = Node.newAssignment(rootNode, aNode, Node.newName(rootNode, "b"));
    Node useNode = Node.newName(rootNode, "a");

    analysis.compute();
    Node expectedDef = bNode;
    Node actualDef = analysis.getDef("a", useNode);
    assertEquals("Expected definition 'a' to be 'b' but was: " + actualDef,
        expectedDef, actualDef);
  }

  @Test
  public void testMultipleAssignments() {
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node rootNode = jsScope.getRootNode();
    Node aNode = Node.newVar(rootNode, "a");
    Node bNode = Node.newVar(rootNode, "b");
    Node cNode = Node.newVar(rootNode, "c");
    Node assignmentNode1 = Node.newAssignment(rootNode, aNode, Node.newName(rootNode, "b"));
    Node assignmentNode2 = Node.newAssignment(rootNode, bNode, Node.newName(rootNode, "c"));
    Node useNode = Node.newName(rootNode, "a");

    analysis.compute();
    Node expectedDef = cNode;
    Node actualDef = analysis.getDef("a", useNode);
    assertEquals("Expected definition 'a' to be 'c' but was: " + actualDef,
        expectedDef, actualDef);
  }

  @Test(expected = AssertionError.class)
  public void testVariableNotFound() {
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node rootNode = jsScope.getRootNode();
    Node useNode = Node.newName(rootNode, "z");

    analysis.compute();
    Node actualDef = analysis.getDef("z", useNode);
    assertNull("Expected 'z' to not have a definition but it was: " + actualDef, actualDef);
  }

  @Test
  public void testAssignmentToParameter() {
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node rootNode = jsScope.getRootNode();
    Node parameterNode = Node.newParam(rootNode, "x");
    Node assignmentNode = Node.newAssignment(rootNode, parameterNode, Node.newName(rootNode, "a"));
    Node useNode = Node.newName(rootNode, "x");

    analysis.compute();
    Node expectedDef = null; // No definition since it's a parameter.
    Node actualDef = analysis.getDef("x", useNode);
    assertEquals("Expected definition 'x' to be null but was: " + actualDef, expectedDef, actualDef);
  }

  @Test
  public void testAssignmentToEscapedVariable() {
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    // Set up an escaped variable.
    Var escapedVar = jsScope.getVar("escaped");
    escapedVar.escapes = true;
    Node escapedNode = Node.newVar(jsScope.getRootNode(), "escaped");

    Node rootNode = jsScope.getRootNode();
    Node assignmentNode = Node.newAssignment(rootNode, escapedNode, Node.newName(rootNode, "a"));
    Node useNode = Node.newName(rootNode, "escaped");

    analysis.compute();
    Node actualDef = analysis.getDef("escaped", useNode);
    assertNull("Expected 'escaped' to not have a definition but it was: " + actualDef, actualDef);
  }

  @Test
  public void testAssignmentToVariableInOuterScope() {
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    // Define a variable in an outer scope.
    Scope outerScope = new Scope();
    outerScope.setParent(jsScope);
    outerScope.setVars(Sets.newHashSet(NodeUtil.newVar(outerScope, "outer")));

    Node rootNode = jsScope.getRootNode();
    Node outerNode = Node.newVar(rootNode, "outer");
    Node assignmentNode = Node.newAssignment(rootNode, outerNode, Node.newName(rootNode, "a"));
    Node useNode = Node.newName(rootNode, "outer");

    analysis.compute();
    Node expectedDef = null; // No definition since 'outer' is in an outer scope.
    Node actualDef = analysis.getDef("outer", useNode);
    assertEquals("Expected definition 'outer' to be null but it was: " + actualDef, expectedDef, actualDef);
  }

  @Test
  public void testUseOfVariableBeforeAssignment() {
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node rootNode = jsScope.getRootNode();
    Node aNode = Node.newVar(rootNode, "a");
    Node useNode = Node.newName(rootNode, "a");
    Node assignmentNode = Node.newAssignment(rootNode, aNode, Node.newName(rootNode, "b"));

    analysis.compute();
    assertNull("Expected definition 'a' to be null before assignment but it was: " + analysis.getDef("a", useNode), analysis.getDef("a", useNode));

    Node expectedDef = aNode;
    Node actualDef = analysis.getDef("a", assignmentNode);
    assertEquals("Expected definition 'a' to be 'a' after assignment but was: " + actualDef, expectedDef, actualDef);
  }

  @Test
  public void testUseOfVariableAfterAssignment() {
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node rootNode = jsScope.getRootNode();
    Node aNode = Node.newVar(rootNode, "a");
    Node assignmentNode = Node.newAssignment(rootNode, aNode, Node.newName(rootNode, "b"));
    Node useNode = Node.newName(rootNode, "a");

    analysis.compute();
    Node expectedDef = Node.newName(rootNode, "b"); // The last definition before use.
    Node actualDef = analysis.getDef("a", useNode);
    assertEquals("Expected definition 'a' to be 'b' after assignment but was: " + actualDef, expectedDef, actualDef);
  }

  @Test
  public void testUseOfVariableWithMultipleDefinitions() {
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node rootNode = jsScope.getRootNode();
    Node aNode = Node.newVar(rootNode, "a");
    Node bNode = Node.newVar(rootNode, "b");
    Node useNode = Node.newName(rootNode, "a");

    Node assignmentNode1 = Node.newAssignment(rootNode, aNode, Node.newName(rootNode, "b"));
    Node assignmentNode2 = Node.newAssignment(rootNode, aNode, Node.newNumberLiteral(rootNode, 10));

    analysis.compute();
    Node expectedDef = null; // There are multiple possible definitions.
    Node actualDef = analysis.getDef("a", useNode);
    assertNull("Expected multiple definitions for 'a' but it was: " + actualDef, actualDef);
  }

  @Test
  public void testUseOfVariableWithDependencies() {
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node rootNode = jsScope.getRootNode();
    Node aNode = Node.newVar(rootNode, "a");
    Node bNode = Node.newVar(rootNode, "b");
    Node cNode = Node.newVar(rootNode, "c");
    Node useNode = Node.newName(rootNode, "a");

    Node assignmentNode1 = Node.newAssignment(rootNode, aNode, Node.newName(rootNode, "b"));
    Node assignmentNode2 = Node.newAssignment(rootNode, bNode, Node.newName(rootNode, "c"));

    analysis.compute();
    Node expectedDef = cNode;
    Node actualDef = analysis.getDef("a", useNode);
    assertEquals("Expected definition 'a' to be 'c' but was: " + actualDef, expectedDef, actualDef);
  }

  @Test
  public void testUseOfVariableWithLoop() {
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node rootNode = jsScope.getRootNode();
    Node aNode = Node.newVar(rootNode, "a");
    Node useNode = Node.newName(rootNode, "a");

    Node loopNode = Node.newWhile(rootNode, Node.newName(rootNode, "i"), Node.newNumberLiteral(rootNode, 0), Node.newNumberLiteral(rootNode, 5));
    Node assignmentNode = Node.newAssignment(rootNode, aNode, Node.newName(rootNode, "a"));

    for (int i = 0; i < 5; i++) {
      Node loopBodyNode = Node.newBlock(loopNode);
      Node loopBodyAssignmentNode = Node.newAssignment(loopBodyNode, aNode, Node.newNumberLiteral(loopBodyNode, i + 1));
      Node loopBodyUseNode = Node.newName(loopBodyNode, "a");
      analysis.traverse(loopBodyNode);
    }

    analysis.compute();
    Node expectedDef = Node.newNumberLiteral(rootNode, 5);
    Node actualDef = analysis.getDef("a", useNode);
    assertEquals("Expected definition 'a' to be 5 but was: " + actualDef, expectedDef, actualDef);
  }

  @Test
  public void testUseOfVariableWithException() {
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node rootNode = jsScope.getRootNode();
    Node aNode = Node.newVar(rootNode, "a");
    Node useNode = Node.newName(rootNode, "a");

    Node tryNode = Node.newTryBlock(rootNode);
    Node assignmentNode = Node.newAssignment(tryNode, aNode, Node.newName(rootNode, "b"));
    Node throwNode = Node.newThrow(tryNode, Node.newStringLiteral(rootNode, "ExceptionThrown"));

    Node catchNode = Node.newCatchBlock(rootNode, Node.newParam(rootNode, "e"), Node.newBlock(rootNode));
    Node useNodeInCatch = Node.newName(catchNode, "a");

    analysis.traverse(tryNode);
    analysis.traverse(catchNode);

    analysis.compute();
    Node expectedDef = Node.newName(tryNode, "b"); // The last definition before use.
    Node actualDef = analysis.getDef("a", useNodeInCatch);
    assertEquals("Expected definition 'a' to be 'b' but was: " + actualDef, expectedDef, actualDef);
  }
}