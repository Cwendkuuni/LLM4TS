import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
  private CheckGlobalThis checker;

  @Before
  public void setup() {
    this.checker = new CheckGlobalThis(new Compiler(), WARNING);
  }

  @Test
  public void testSafeFunction() {
    Node node = NodeFactory.createFunction("safeFunction");
    assertTrue(checker.shouldTraverse(NodeTraversal.EMPTY, node, null));
  }

  @Test
  public void testConstructorFunction() {
    Node node = NodeFactory.createFunction("constructorFunction", new Node("constructor") {});
    assertFalse(checker.shouldTraverse(NodeTraversal.EMPTY, node, null));
  }

  @Test
  public void testFunctionWithThisAnnotation() {
    Node node = NodeFactory.createFunction("functionWithThisAnnotation", new Node("this") {});
    assertFalse(checker.shouldTraverse(NodeTraversal.EMPTY, node, null));
  }

  @Test
  public void testFunctionWithNoParent() {
    Node node = NodeFactory.createFunction("functionWithNoParent");
    assertFalse(checker.shouldTraverse(NodeTraversal.EMPTY, node, null));
  }

  @Test
  public void testFunctionWithParentBlock() {
    Node parent = NodeFactory.createBlock();
    Node functionNode = NodeFactory.createFunction("functionWithParentBlock", NodeFactory.createName("functionName"));
    parent.addChildToEnd(functionNode);
    assertTrue(checker.shouldTraverse(NodeTraversal.EMPTY, functionNode, parent));
  }

  @Test
  public void testFunctionWithParentName() {
    Node parent = NodeFactory.createName("parentFunctionName");
    Node functionNode = NodeFactory.createFunction("functionWithParentName", NodeFactory.createName("functionName"));
    parent.addChildToEnd(functionNode);
    assertTrue(checker.shouldTraverse(NodeTraversal.EMPTY, functionNode, parent));
  }

  @Test
  public void testFunctionWithParentAssign() {
    Node parentAssign = NodeFactory.createAssign(NodeFactory.createName("parentVariableName"), NodeFactory.createFunction("functionWithParentAssign", NodeFactory.createName("functionName")));
    Node functionNode = NodeFactory.createFunction("functionWithParentAssign", NodeFactory.createName("functionName"));
    parentAssign.addChildToEnd(functionNode);
    assertTrue(checker.shouldTraverse(NodeTraversal.EMPTY, functionNode, parentAssign));
  }

  @Test
  public void testAssignmentLeftSideWithThis() {
    Node assign = NodeFactory.createAssign(NodeFactory.createName("variableName"), NodeFactory.createThis());
    assertTrue(checker.shouldTraverse(NodeTraversal.EMPTY, NodeFactory.createThis(), assign));
  }

  @Test
  public void testAssignmentRightSideWithThis() {
    Node assign = NodeFactory.createAssign(NodeFactory.createName("variableName"), NodeFactory.createThis());
    Node rhs = NodeFactory.createThis();
    Node lhs = NodeFactory.createName("variableName");
    assign.setParent(NodeFactory.createAssign(lhs, rhs));
    assertFalse(checker.shouldTraverse(NodeTraversal.EMPTY, rhs, assign));
  }

  @Test
  public void testAssignmentWithPrototypeProperty() {
    Node assign = NodeFactory.createAssign(
        NodeFactory.createGetProp(NodeFactory.createName("prototypeProperty"), NodeFactory.createName("objectName")),
        NodeFactory.createThis());
    Node lhs = NodeFactory.createGetProp(NodeFactory.createName("prototypeProperty"), NodeFactory.createName("objectName"));
    assign.setParent(NodeFactory.createAssign(lhs, NodeFactory.createThis()));
    assertFalse(checker.shouldTraverse(NodeTraversal.EMPTY, NodeFactory.createThis(), assign));
  }

  @Test
  public void testAssignmentWithPrototypeSubProperty() {
    Node assign = NodeFactory.createAssign(
        NodeFactory.createGetProp(NodeFactory.createName("prototypeProperty"), NodeFactory.createName("objectName")),
        NodeFactory.createGetProp(NodeFactory.createName("subProperty"), NodeFactory.createThis()));
    Node lhs = NodeFactory.createGetProp(NodeFactory.createName("prototypeProperty"), NodeFactory.createName("objectName"));
    assign.setParent(NodeFactory.createAssign(lhs, NodeFactory.createGetProp(NodeFactory.createName("subProperty"), NodeFactory.createThis())));
    assertFalse(checker.shouldTraverse(NodeTraversal.EMPTY, NodeFactory.createThis(), assign));
  }
}