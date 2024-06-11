import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

  private CheckGlobalThis checkGlobalThis;

  @Before
  public void setUp() {
    checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
  }

  @Test
  public void testShouldTraverse_FunctionWithConstructorAnnotation_ShouldNotTraverse() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    parent.setJSDocInfo(jsDoc);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionWithThisAnnotation_ShouldNotTraverse() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setThisType(new JSTypeExpression());
    parent.setJSDocInfo(jsDoc);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionWithOverrideAnnotation_ShouldNotTraverse() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setOverride(true);
    parent.setJSDocInfo(jsDoc);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionInGlobalScope_ShouldTraverse() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_FunctionInBlockScope_ShouldTraverse() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_FunctionInAssignmentScope_ShouldTraverse() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testVisit_ThisNode_ShouldReportError() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);

    // Act
    checkGlobalThis.visit(t, n, parent);
    DiagnosticType error = t.getError();

    // Assert
    assertNotNull(error);
    assertEquals(DiagnosticType.WARNING, error);
    assertEquals("dangerous use of the global 'this' object", error.getMessage());
  }

  @Test
  public void testVisit_ThisNodeOnLeftSideOfAssign_ShouldReportError() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToFront(n);

    // Act
    checkGlobalThis.visit(t, n, parent);
    DiagnosticType error = t.getError();

    // Assert
    assertNotNull(error);
    assertEquals(DiagnosticType.WARNING, error);
    assertEquals("dangerous use of the global 'this' object", error.getMessage());
  }

  @Test
  public void testVisit_ThisNodeOnRightSideOfAssign_NoErrorReported() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToBack(n);

    // Act
    checkGlobalThis.visit(t, n, parent);
    DiagnosticType error = t.getError();

    // Assert
    assertNull(error);
  }

  @Test
  public void testVisit_ThisNodeOnRightSideOfAssignToPrototypeProperty_NoErrorReported() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node assign = new Node(Token.ASSIGN);
    Node prototype = new Node(Token.GETPROP);
    prototype.addChildToFront(new Node(Token.NAME, "prototype"));
    assign.addChildToFront(prototype);
    parent.addChildToFront(new Node(Token.NAME, "property"));
    assign.addChildToBack(n);

    // Act
    checkGlobalThis.visit(t, n, assign);
    DiagnosticType error = t.getError();

    // Assert
    assertNull(error);
  }

  @Test
  public void testVisit_ThisNodeOnRightSideOfAssignToNonPrototypeProperty_ShouldReportError() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node assign = new Node(Token.ASSIGN);
    Node property = new Node(Token.NAME, "property");
    assign.addChildToFront(property);
    assign.addChildToBack(n);

    // Act
    checkGlobalThis.visit(t, n, assign);
    DiagnosticType error = t.getError();

    // Assert
    assertNotNull(error);
    assertEquals(DiagnosticType.WARNING, error);
    assertEquals("dangerous use of the global 'this' object", error.getMessage());
  }

  @Test
  public void testGetFunctionJsDocInfo_FunctionHasJsDocInfo_ReturnsJsDocInfo() {
    // Arrange
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    n.setJSDocInfo(jsDoc);

    // Act
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

    // Assert
    assertEquals(jsDoc, result);
  }

  @Test
  public void testGetFunctionJsDocInfo_ParentIsNameNode_HasJsDocInfo_ReturnsJsDocInfoOfParent() {
    // Arrange
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    JSDocInfo jsDoc = new JSDocInfo();
    parent.setJSDocInfo(jsDoc);
    parent.addChildToFront(n);

    // Act
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

    // Assert
    assertEquals(jsDoc, result);
  }

  @Test
  public void testGetFunctionJsDocInfo_ParentIsNameNode_NoJsDocInfo_ReturnsJsDocInfoOfGrandparent() {
    // Arrange
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    JSDocInfo jsDoc = new JSDocInfo();
    Node grandparent = new Node(Token.VAR);
    grandparent.setJSDocInfo(jsDoc);
    grandparent.addChildToFront(parent);
    parent.addChildToFront(n);

    // Act
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

    // Assert
    assertEquals(jsDoc, result);
  }

  @Test
  public void testGetFunctionJsDocInfo_NoJsDocInfo_ReturnsNull() {
    // Arrange
    Node n = new Node(Token.FUNCTION);

    // Act
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

    // Assert
    assertNull(result);
  }

}