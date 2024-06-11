import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_NonGlobalFunctionWithConstructorAnnotation_ShouldReturnFalse() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordConstructor();
    n.setJSDocInfo(jsDoc);

    // Act
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_NonGlobalFunctionWithThisAnnotation_ShouldReturnFalse() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThisType();
    n.setJSDocInfo(jsDoc);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_GlobalFunctionWithConstructorAnnotation_ShouldReturnTrue() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordConstructor();
    n.setJSDocInfo(jsDoc);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, null);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_GlobalFunctionWithoutAnnotations_ShouldReturnTrue() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, null);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_LeftSideOfAssignmentInFunctionWithPrototypeMethod_ShouldReturnFalse() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.GETPROP);
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.NAME);
    Node rhs = new Node(Token.GETPROP);
    lhs.setString("prototype");
    rhs.setString("method");
    n.addChildToBack(lhs);
    n.addChildToBack(rhs);
    parent.addChildToBack(lhs);
    parent.addChildToBack(rhs);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_LeftSideOfAssignmentInFunctionWithPrototypeProperty_ShouldReturnFalse() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.GETPROP);
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.NAME);
    Node rhs = new Node(Token.GETPROP);
    lhs.setString("prototype");
    rhs.setString("property");
    n.addChildToBack(lhs);
    n.addChildToBack(rhs);
    parent.addChildToBack(lhs);
    parent.addChildToBack(rhs);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_LeftSideOfAssignmentInFunctionWithNoPrototypeProperty_ShouldReturnTrue() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.GETPROP);
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.NAME);
    Node rhs = new Node(Token.GETPROP);
    lhs.setString("prototype");
    rhs.setString("property");
    n.addChildToBack(lhs);
    n.addChildToBack(rhs);
    parent.addChildToBack(lhs);
    parent.addChildToBack(rhs);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_RightSideOfAssignmentInFunctionWithPrototypeProperty_ShouldReturnFalse() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.GETPROP);
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.NAME);
    Node rhs = new Node(Token.GETPROP);
    lhs.setString("object");
    rhs.setString("prototype.property");
    n.addChildToBack(lhs);
    n.addChildToBack(rhs);
    parent.addChildToBack(lhs);
    parent.addChildToBack(rhs);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldReportThis_LeftSideOfAssignmentNode_ShouldReturnTrue() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    checkGlobalThis.setAssignLhsChild(n);

    // Act
    boolean result = checkGlobalThis.shouldReportThis(n, parent);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testShouldReportThis_RightSideOfAssignmentNode_ShouldReturnFalse() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    checkGlobalThis.setAssignLhsChild(null);

    // Act
    boolean result = checkGlobalThis.shouldReportThis(n, parent);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testVisit_ThisNode_ShouldReportThis() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);

    // Act
    checkGlobalThis.visit(t, n, parent);

    // Assert
    assertEquals(1, compiler.getReportedErrors().size());
    // Verify specific error message if needed
  }

  @Test
  public void testVisit_AssignLhsChildNode_ShouldNotReportThis() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.ASSIGN);
    checkGlobalThis.setAssignLhsChild(n);

    // Act
    checkGlobalThis.visit(t, n, parent);

    // Assert
    assertTrue(compiler.getReportedErrors().isEmpty());
  }

  @Test
  public void testVisit_NonThisNode_ShouldNotReportThis() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.ASSIGN);

    // Act
    checkGlobalThis.visit(t, n, parent);

    // Assert
    assertTrue(compiler.getReportedErrors().isEmpty());
  }

  @Test
  public void testGetFunctionJsDocInfo_FunctionHasJSDoc_ShouldReturnJSDocInfo() {
    // Arrange
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    n.setJSDocInfo(jsDoc);

    // Act
    JSDocInfo result = getFunctionJsDocInfo(n);

    // Assert
    assertEquals(jsDoc, result);
  }

  @Test
  public void testGetFunctionJsDocInfo_FunctionHasParentWithJSDoc_ShouldReturnParentJSDocInfo() {
    // Arrange
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    JSDocInfo jsDoc = new JSDocInfo();
    parent.setJSDocInfo(jsDoc);
    n.setParent(parent);

    // Act
    JSDocInfo result = getFunctionJsDocInfo(n);

    // Assert
    assertEquals(jsDoc, result);
  }

  @Test
  public void testGetFunctionJsDocInfo_FunctionHasParentWithVarDeclarationAndJSDoc_ShouldReturnParentJSDocInfo() {
    // Arrange
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    Node gramps = new Node(Token.VAR);
    JSDocInfo jsDoc = new JSDocInfo();
    gramps.setJSDocInfo(jsDoc);
    parent.setParent(gramps);
    n.setParent(parent);

    // Act
    JSDocInfo result = getFunctionJsDocInfo(n);

    // Assert
    assertEquals(jsDoc, result);
  }

  @Test
  public void testGetFunctionJsDocInfo_FunctionHasParentWithVarDeclarationAndNoJSDoc_ShouldReturnNull() {
    // Arrange
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    Node gramps = new Node(Token.VAR);
    parent.setParent(gramps);
    n.setParent(parent);

    // Act
    JSDocInfo result = getFunctionJsDocInfo(n);

    // Assert
    assertNull(result);
  }

  @Test
  public void testGetFunctionJsDocInfo_FunctionHasNoJSDocAndParentWithoutJSDoc_ShouldReturnNull() {
    // Arrange
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    n.setParent(parent);

    // Act
    JSDocInfo result = getFunctionJsDocInfo(n);

    // Assert
    assertNull(result);
  }

}