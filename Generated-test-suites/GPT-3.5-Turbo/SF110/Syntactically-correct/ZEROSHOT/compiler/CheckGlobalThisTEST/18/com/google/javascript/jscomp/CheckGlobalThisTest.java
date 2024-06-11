import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;

  @Test
  public void testShouldTraverse_FunctionWithThisAnnotation_ShouldReturnFalse() {
    // Arrange
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setThisType(new Node(Token.THIS));
    n.setJSDocInfo(jsDoc);
    Node parent = new Node(Token.ROOT);

    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionWithConstructorAnnotation_ShouldReturnFalse() {
    // Arrange
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    n.setJSDocInfo(jsDoc);
    Node parent = new Node(Token.ROOT);

    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionWithInterfaceAnnotation_ShouldReturnFalse() {
    // Arrange
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setInterface(true);
    n.setJSDocInfo(jsDoc);
    Node parent = new Node(Token.ROOT);

    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionWithOverrideAnnotation_ShouldReturnFalse() {
    // Arrange
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setOverride(true);
    n.setJSDocInfo(jsDoc);
    Node parent = new Node(Token.ROOT);

    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_NonFunctionParent_ShouldReturnFalse() {
    // Arrange
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = null;
    n.setJSDocInfo(jsDoc);
    Node parent = new Node(Token.ROOT);
    parent.setType(Token.BLOCK);

    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_GetPropAssignmentToPrototypeProperty_ShouldReturnFalse() {
    // Arrange
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.STRING);
    JSDocInfo jsDoc = null;
    n.setJSDocInfo(jsDoc);
    Node parent = new Node(Token.GETPROP);
    parent.setType(Token.GETPROP);
    parent.getLastChild().setString("prototype");

    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testVisit_ThisNodeWithGlobalThis_ShouldReportError() {
    // Arrange
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ROOT);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    // Act
    checkGlobalThis.visit(t, n, parent);

    // Assert
    // Check that the error was reported
    assertNotNull(compiler.getError());
  }

  @Test
  public void testVisit_AssignLhsChildNode_ShouldResetAssignLhsChild() {
    // Arrange
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.NAME);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    // Act
    checkGlobalThis.visit(t, n, parent);

    // Assert
    assertNull(checkGlobalThis.assignLhsChild);
  }

  @Test
  public void testShouldReportThis_ThisNodeWithAssignLhsChild_ShouldReturnTrue() {
    // Arrange
    Node n = new Node(Token.THIS);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    checkGlobalThis.assignLhsChild = new Node(Token.NAME);

    // Act
    boolean result = checkGlobalThis.shouldReportThis(n);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testShouldReportThis_ThisNodeWithGetParent_ReturnsTrue() {
    // Arrange
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    // Act
    boolean result = checkGlobalThis.shouldReportThis(n);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testGetFunctionJsDocInfo_FunctionWithJSDocInfo_ReturnsJSDocInfo() {
    // Arrange
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    n.setJSDocInfo(jsDoc);
    Node parent = new Node(Token.ROOT);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    // Act
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

    // Assert
    assertEquals(jsDoc, result);
  }

  @Test
  public void testGetFunctionJsDocInfo_FunctionWithParentName_ReturnsParentJSDocInfo() {
    // Arrange
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = null;
    n.setJSDocInfo(jsDoc);
    Node parent = new Node(Token.NAME);
    parent.setJSDocInfo(new JSDocInfo());
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    // Act
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

    // Assert
    assertEquals(parent.getJSDocInfo(), result);
  }

  @Test
  public void testGetFunctionJsDocInfo_FunctionWithParentAssign_ReturnsGrandparentJSDocInfo() {
    // Arrange
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = null;
    n.setJSDocInfo(jsDoc);
    Node parent = new Node(Token.NAME);
    parent.setType(Token.ASSIGN);
    parent.setJSDocInfo(null);
    Node grandparent = new Node(Token.VAR);
    grandparent.setJSDocInfo(new JSDocInfo());
    grandparent.addChildToBack(parent);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    // Act
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

    // Assert
    assertEquals(grandparent.getJSDocInfo(), result);
  }
}