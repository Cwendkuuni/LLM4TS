import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.*;

import static org.junit.Assert.*;

public class CheckGlobalThisTest {

  private CheckGlobalThis checkGlobalThis;

  @Before
  public void setUp() {
    checkGlobalThis = new CheckGlobalThis(new AbstractCompiler());
  }

  @Test
  public void testShouldTraverse_NonFunction_ReturnsTrue() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.EMPTY);
    Node parent = new Node(Token.EMPTY);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_FunctionWithConstructorAnnotation_ReturnsFalse() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.EMPTY);
    JSDocInfo jsDocInfo = new JSDocInfo.Builder().markConstructor().build();
    n.setJSDocInfo(jsDocInfo);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionWithThisAnnotation_ReturnsFalse() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.EMPTY);
    JSDocInfo jsDocInfo = new JSDocInfo.Builder().markThis().build();
    n.setJSDocInfo(jsDocInfo);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionWithNoLogicalPlaceForThisAnnotation_ReturnsFalse() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.EXPR_RESULT);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionInsideBlock_ReturnsTrue() {
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
  public void testShouldTraverse_FunctionInsideScript_ReturnsTrue() {
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
  public void testShouldTraverse_FunctionInsideName_ReturnsTrue() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_FunctionInsideAssign_ReturnsTrue() {
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
  public void testShouldTraverse_FunctionInsideStringKey_ReturnsTrue() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.STRING_KEY);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_FunctionInsideMethod_ReturnsFalse() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.METHOD);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldReportThis_AssignLhsChildNotNull_ReturnsTrue() {
    // Arrange
    checkGlobalThis.assignLhsChild = new Node(Token.EMPTY);
    Node n = new Node(Token.THIS);

    // Act
    boolean result = checkGlobalThis.shouldReportThis(n);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testShouldReportThis_ParentIsNull_ReturnsFalse() {
    // Arrange
    Node n = new Node(Token.THIS);

    // Act
    boolean result = checkGlobalThis.shouldReportThis(n);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldReportThis_ParentIsNotGet_ReturnsFalse() {
    // Arrange
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.EMPTY);

    // Act
    boolean result = checkGlobalThis.shouldReportThis(n);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldReportThis_ParentIsGet_ReturnsTrue() {
    // Arrange
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);

    // Act
    boolean result = checkGlobalThis.shouldReportThis(n);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testVisit_ThisNode_ShouldReportError() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.EMPTY);
    DiagnosticType expectedError = CheckGlobalThis.GLOBAL_THIS;
    boolean errorReported = false;

    // Act
    checkGlobalThis.visit(t, n, parent);
    DiagnosticType[] errors = t.getErrors();
    for (DiagnosticType error : errors) {
      if (error.equals(expectedError)) {
        errorReported = true;
        break;
      }
    }

    // Assert
    assertTrue(errorReported);
  }

  @Test
  public void testVisit_NotAssignLhsChild_ShouldNotResetAssignLhsChild() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.EMPTY);
    Node parent = new Node(Token.EMPTY);
    checkGlobalThis.assignLhsChild = new Node(Token.NAME);

    // Act
    checkGlobalThis.visit(t, n, parent);

    // Assert
    assertNotNull(checkGlobalThis.assignLhsChild);
  }

  @Test
  public void testVisit_AssignLhsChild_ShouldResetAssignLhsChild() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.EMPTY);
    Node parent = new Node(Token.EMPTY);
    checkGlobalThis.assignLhsChild = n;

    // Act
    checkGlobalThis.visit(t, n, parent);

    // Assert
    assertNull(checkGlobalThis.assignLhsChild);
  }

  @Test
  public void testGetFunctionJsDocInfo_FunctionHasJsDoc_ReturnsJsDocInfo() {
    // Arrange
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDocInfo = new JSDocInfo.Builder().markConstructor().build();
    n.setJSDocInfo(jsDocInfo);

    // Act
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

    // Assert
    assertEquals(jsDocInfo, result);
  }

  @Test
  public void testGetFunctionJsDocInfo_ParentIsAssignableName_ReturnsJsDocInfo() {
    // Arrange
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    JSDocInfo jsDocInfo = new JSDocInfo.Builder().markConstructor().build();
    parent.setJSDocInfo(jsDocInfo);
    n.setParent(parent);

    // Act
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

    // Assert
    assertEquals(jsDocInfo, result);
  }

  @Test
  public void testGetFunctionJsDocInfo_ParentIsAssign_SetsJSDocInfo() {
    // Arrange
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    JSDocInfo jsDocInfo = new JSDocInfo.Builder().markConstructor().build();
    parent.setJSDocInfo(jsDocInfo);
    parent.setFirstChild(n);

    // Act
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

    // Assert
    assertEquals(jsDocInfo, result);
  }

  @Test
  public void testGetFunctionJsDocInfo_ParentIsVar_SetsJSDocInfo() {
    // Arrange
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.VAR);
    JSDocInfo jsDocInfo = new JSDocInfo.Builder().markConstructor().build();
    parent.setJSDocInfo(jsDocInfo);
    parent.addChildToFront(n);

    // Act
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

    // Assert
    assertEquals(jsDocInfo, result);
  }
}