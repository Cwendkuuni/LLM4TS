import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

  private CheckGlobalThis checkGlobalThis;

  @Before
  public void setUp() {
    checkGlobalThis = new CheckGlobalThis();
  }

  // Test case for shouldTraverse method when the node is a function without any annotations
  @Test
  public void testShouldTraverseFunctionWithoutAnnotations() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertTrue(result);
  }

  // Test case for shouldTraverse method when the node is a constructor function
  @Test
  public void testShouldTraverseConstructorFunction() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    n.setType(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    n.setJSDocInfo(jsDoc);
    Node parent = new Node();

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  // Test case for shouldTraverse method when the node is a function with @this annotation
  @Test
  public void testShouldTraverseFunctionWithThisAnnotation() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    n.setType(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThisType("MyType");
    n.setJSDocInfo(jsDoc);
    Node parent = new Node();

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  // Test case for shouldTraverse method when the node is a function with @override annotation
  @Test
  public void testShouldTraverseFunctionWithOverrideAnnotation() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    n.setType(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setOverride(true);
    n.setJSDocInfo(jsDoc);
    Node parent = new Node();

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  // Test case for shouldTraverse method when the node is not a global context
  @Test
  public void testShouldTraverseNonGlobalContext() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    n.setType(Token.FUNCTION);
    Node parent = new Node();
    parent.setType(Token.NAME);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  // Test case for shouldTraverse method when the node is a function assigned to a prototype property
  @Test
  public void testShouldTraverseFunctionAssignedToPrototypeProperty() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    n.setType(Token.FUNCTION);
    Node parent = new Node();
    parent.setType(Token.GETPROP);
    Node grandparent = new Node();
    grandparent.setJSDocInfo(new JSDocInfo());
    grandparent.getJSDocInfo().setLendsName("Something.prototype");
    parent.setParent(grandparent);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  // Test case for visit method when a global this is encountered
  @Test
  public void testVisitGlobalThis() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node();
    DiagnosticType expected = DiagnosticType.warning("JSC_USED_GLOBAL_THIS",
        "dangerous use of the global 'this' object");

    checkGlobalThis.visit(t, n, parent);

    DiagnosticType actual = t.getErrors().get(0).getType();
    assertEquals(expected, actual);
  }

  // Test case for visit method when a global this is encountered as the left side of an assignment
  @Test
  public void testVisitGlobalThisAsLeftSideOfAssignment() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    checkGlobalThis.assignLhsChild = new Node(Token.THIS);
    DiagnosticType expected = DiagnosticType.warning("JSC_USED_GLOBAL_THIS",
        "dangerous use of the global 'this' object");

    checkGlobalThis.visit(t, n, parent);

    DiagnosticType actual = t.getErrors().get(0).getType();
    assertEquals(expected, actual);
    assertNull(checkGlobalThis.assignLhsChild);
  }

  // Test case for visit method when a global this is encountered but shouldReportThis returns false
  @Test
  public void testVisitGlobalThisShouldNotReportThis() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);

    checkGlobalThis.visit(t, n, parent);

    assertEquals(0, t.getErrors().size());
  }

  // Test case for shouldReportThis method when assignLhsChild is not null
  @Test
  public void testShouldReportThisWithAssignLhsChild() {
    Node n = new Node(Token.THIS);
    checkGlobalThis.assignLhsChild = new Node(Token.THIS);

    boolean result = checkGlobalThis.shouldReportThis(n);

    assertTrue(result);
  }

  // Test case for shouldReportThis method when parent is a property access
  @Test
  public void testShouldReportThisWithPropertyAccess() {
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);

    boolean result = checkGlobalThis.shouldReportThis(n);

    assertTrue(result);
  }

  // Test case for shouldReportThis method when parent is not a property access
  @Test
  public void testShouldReportThisNotAPropertyAccess() {
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.NOT);

    boolean result = checkGlobalThis.shouldReportThis(n);

    assertFalse(result);
  }

  // Test case for getFunctionJsDocInfo method when the node has JSDoc information
  @Test
  public void testGetFunctionJsDocInfoWithJSDoc() {
    Node n = new Node(Token.FUNCTION);
    JSDocInfo expected = new JSDocInfo();
    n.setJSDocInfo(expected);

    JSDocInfo actual = checkGlobalThis.getFunctionJsDocInfo(n);

    assertEquals(expected, actual);
  }

  // Test case for getFunctionJsDocInfo method when the node does not have JSDoc information
  // but the parent is a NAME node
  @Test
  public void testGetFunctionJsDocInfoWithParentNameNode() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    parent.setJSDocInfo(new JSDocInfo());
    n.setParent(parent);
    JSDocInfo expected = parent.getJSDocInfo();

    JSDocInfo actual = checkGlobalThis.getFunctionJsDocInfo(n);

    assertEquals(expected, actual);
  }

  // Test case for getFunctionJsDocInfo method when the node does not have JSDoc information
  // but the parent is an ASSIGN node and the grandparent is a VAR node
  @Test
  public void testGetFunctionJsDocInfoWithParentAssignAndGrandparentVarNode() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    parent.setJSDocInfo(new JSDocInfo());
    Node grandparent = new Node(Token.VAR);
    grandparent.setParent(new Node());
    grandparent.setJSDocInfo(new JSDocInfo());
    parent.setParent(grandparent);
    n.setParent(parent);
    JSDocInfo expected = grandparent.getJSDocInfo();

    JSDocInfo actual = checkGlobalThis.getFunctionJsDocInfo(n);

    assertEquals(expected, actual);
  }
}