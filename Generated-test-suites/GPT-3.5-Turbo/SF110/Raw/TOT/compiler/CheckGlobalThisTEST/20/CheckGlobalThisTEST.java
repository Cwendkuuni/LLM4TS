import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {

  private CheckGlobalThis checker;
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    checker = new CheckGlobalThis(compiler);
  }

  @Test
  public void testShouldTraverse_FunctionWithAssignmentToThis_DoesNotTraverse() {
    NodeTraversal t = new NodeTraversal();
    Node n = createFunctionNode();
    Node parent = createAssignmentNode(createThisNode(), createValueNode());
    boolean actual = checker.shouldTraverse(t, n, parent);
    assertFalse(actual);
  }

  @Test
  public void testShouldTraverse_FunctionWithAnnotation_DoesNotTraverse() {
    NodeTraversal t = new NodeTraversal();
    Node n = createFunctionNodeWithAnnotation();
    Node parent = createValueNode();
    boolean actual = checker.shouldTraverse(t, n, parent);
    assertFalse(actual);
  }

  @Test
  public void testShouldTraverse_FunctionWithNoLogicalPlaceForAnnotation_DoesNotTraverse() {
    NodeTraversal t = new NodeTraversal();
    Node n = createFunctionNode();
    Node parent = createObjectLiteralNode();
    boolean actual = checker.shouldTraverse(t, n, parent);
    assertFalse(actual);
  }

  @Test
  public void testShouldTraverse_FunctionLentToPrototype_DoesNotTraverse() {
    NodeTraversal t = new NodeTraversal();
    Node n = createFunctionNode();
    Node parent = createObjectLiteralKeyNode();
    Node grandparent = createObjectLiteralNode();
    grandparent.putProp("JSDocInfo", createJSDocInfoNode());
    parent.setParent(grandparent);
    boolean actual = checker.shouldTraverse(t, n, parent);
    assertFalse(actual);
  }

  @Test
  public void testShouldTraverse_LeftSideOfAssignment_Traverses() {
    NodeTraversal t = new NodeTraversal();
    Node n = createFunctionNode();
    Node parent = createAssignmentNode(createThisNode(), createValueNode());
    boolean actual = checker.shouldTraverse(t, n, parent);
    assertTrue(actual);
  }

  @Test
  public void testShouldTraverse_RightSideOfAssignment_PrototypeProperty_Traverses() {
    NodeTraversal t = new NodeTraversal();
    Node n = createFunctionNode();
    Node parent = createAssignmentNode(createGetPropNode(createGetPropNode(createThisNode(), "prototype"), "property"), createValueNode());
    boolean actual = checker.shouldTraverse(t, n, parent);
    assertTrue(actual);
  }

  @Test
  public void testVisit_ThisOnLeftSideOfAssignment_DiagnosticReported() {
    NodeTraversal t = new NodeTraversal();
    Node n = createThisNode();
    Node parent = createAssignmentNode(n, createValueNode());
    checker.visit(t, n, parent);
    DiagnosticType expected = compiler.report(t.makeError(n, CheckGlobalThis.GLOBAL_THIS));
    assertEquals(expected, CheckGlobalThis.GLOBAL_THIS);
  }

  @Test
  public void testVisit_ThisOnRightSideOfAssignment_PrototypeProperty_DiagnosticNotReported() {
    NodeTraversal t = new NodeTraversal();
    Node n = createThisNode();
    Node parent = createAssignmentNode(createGetPropNode(createGetPropNode(createThisNode(), "prototype"), "property"), n);
    checker.visit(t, n, parent);
    assertNull(compiler.report(t.makeError(n, CheckGlobalThis.GLOBAL_THIS)));
  }

  @Test
  public void testShouldReportThis_ThisOnLeftSideOfAssignment_ReturnsTrue() {
    Node n = createThisNode();
    boolean actual = checker.shouldReportThis(n);
    assertTrue(actual);
  }

  @Test
  public void testShouldReportThis_ThisWithPropertyAccess_ReturnsTrue() {
    Node n = createThisNode();
    Node parent = createGetPropNode(n, "property");
    boolean actual = checker.shouldReportThis(parent);
    assertTrue(actual);
  }

  @Test
  public void testShouldReportThis_ThisWithoutPropertyAccess_ReturnsFalse() {
    Node n = createThisNode();
    Node parent = createAssignmentNode(n, createValueNode());
    boolean actual = checker.shouldReportThis(parent);
    assertFalse(actual);
  }

  private Node createFunctionNode() {
    return createNodeWithTokenType(Token.FUNCTION);
  }

  private Node createThisNode() {
    return createNodeWithTokenType(Token.THIS);
  }
  
  private Node createValueNode() {
    return createNodeWithTokenType(Token.VALUE);
  }

  private Node createAssignmentNode(Node lhs, Node rhs) {
    Node assignNode = createNodeWithTokenType(Token.ASSIGN);
    assignNode.addChildToBack(lhs);
    assignNode.addChildToBack(rhs);
    return assignNode;
  }

  private Node createGetPropNode(Node object, String property) {
    Node getPropNode = createNodeWithTokenType(Token.GETPROP);
    Node stringNode = createNodeWithTokenType(Token.STRING);
    stringNode.setString(property);
    getPropNode.addChildToBack(object);
    getPropNode.addChildToBack(stringNode);
    return getPropNode;
  }

  private Node createObjectLiteralNode() {
    return createNodeWithTokenType(Token.OBJECTLIT);
  }

  private Node createObjectLiteralKeyNode() {
    Node objectLiteralKey = createNodeWithTokenType(Token.STRING_KEY);
    objectLiteralKey.putProp("JSDocInfo", createJSDocInfoNode());
    return objectLiteralKey;
  }

  private Node createFunctionNodeWithAnnotation() {
    Node functionNode = createFunctionNode();
    functionNode.putProp("JSDocInfo", createJSDocInfoNode());
    return functionNode;
  }

  private Node createJSDocInfoNode() {
    return createNodeWithTokenType(Token.JSDOC);
  }

  private Node createNodeWithTokenType(int tokenType) {
    Node node = new Node(tokenType);
    return node;
  }

}