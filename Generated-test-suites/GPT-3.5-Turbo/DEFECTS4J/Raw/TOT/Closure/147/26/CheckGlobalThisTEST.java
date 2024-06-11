import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_FunctionWithConstructorAnnotation_ShouldReturnFalse() {
    // Arrange
    AbstractCompiler mockCompiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(mockCompiler, level);

    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);

    when(n.getType()).thenReturn(Token.FUNCTION);
    when(checkGlobalThis.getFunctionJsDocInfo(n)).thenReturn(createConstructorAnnotationJSDocInfo());

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionWithThisAnnotation_ShouldReturnFalse() {
    // Arrange
    AbstractCompiler mockCompiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(mockCompiler, level);

    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);

    when(n.getType()).thenReturn(Token.FUNCTION);
    when(checkGlobalThis.getFunctionJsDocInfo(n)).thenReturn(createThisAnnotationJSDocInfo());

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionInBlock_ShouldReturnFalse() {
    // Arrange
    AbstractCompiler mockCompiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(mockCompiler, level);

    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);

    when(n.getType()).thenReturn(Token.FUNCTION);
    when(checkGlobalThis.getFunctionJsDocInfo(n)).thenReturn(null);
    when(parent.getType()).thenReturn(Token.BLOCK);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionInScript_ShouldReturnFalse() {
    // Arrange
    AbstractCompiler mockCompiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(mockCompiler, level);

    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);

    when(n.getType()).thenReturn(Token.FUNCTION);
    when(checkGlobalThis.getFunctionJsDocInfo(n)).thenReturn(null);
    when(parent.getType()).thenReturn(Token.SCRIPT);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionAssignedToName_ShouldReturnFalse() {
    // Arrange
    AbstractCompiler mockCompiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(mockCompiler, level);

    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);

    when(n.getType()).thenReturn(Token.FUNCTION);
    when(checkGlobalThis.getFunctionJsDocInfo(n)).thenReturn(null);
    when(parent.getType()).thenReturn(Token.NAME);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionAssignedToAssign_ShouldReturnFalse() {
    // Arrange
    AbstractCompiler mockCompiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(mockCompiler, level);

    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);

    when(n.getType()).thenReturn(Token.FUNCTION);
    when(checkGlobalThis.getFunctionJsDocInfo(n)).thenReturn(null);
    when(parent.getType()).thenReturn(Token.ASSIGN);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_LeftSideOfAssignment_ShouldReturnTrue() {
    // Arrange
    AbstractCompiler mockCompiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(mockCompiler, level);

    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);
    Node lhs = mock(Node.class);
    Node rhs = mock(Node.class);

    when(n.getType()).thenReturn(Token.FUNCTION);
    when(checkGlobalThis.getFunctionJsDocInfo(n)).thenReturn(null);
    when(parent.getType()).thenReturn(Token.ASSIGN);
    when(parent.getFirstChild()).thenReturn(lhs);
    when(lhs.getNext()).thenReturn(rhs);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_NotLeftSideOfAssignmentWithPropertyAccess_ShouldReturnTrue() {
    // Arrange
    AbstractCompiler mockCompiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(mockCompiler, level);

    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);
    Node lhs = mock(Node.class);
    Node rhs = mock(Node.class);
    Node llhs = mock(Node.class);
    Node lllhs = mock(Node.class);

    when(n.getType()).thenReturn(Token.FUNCTION);
    when(checkGlobalThis.getFunctionJsDocInfo(n)).thenReturn(null);
    when(parent.getType()).thenReturn(Token.ASSIGN);
    when(parent.getFirstChild()).thenReturn(lhs);
    when(lhs.getNext()).thenReturn(rhs);
    when(NodeUtil.isGet(lhs)).thenReturn(true);
    when(lhs.getType()).thenReturn(Token.GETPROP);
    when(lhs.getLastChild().getString()).thenReturn("prototype");
    when(lhs.getFirstChild()).thenReturn(llhs);
    when(llhs.getType()).thenReturn(Token.GETPROP);
    when(llhs.getLastChild().getString()).thenReturn("prototype");
    when(lllhs.getType()).thenReturn(Token.VAR);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testVisit_ThisToken_ShouldReportError() {
    // Arrange
    AbstractCompiler mockCompiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(mockCompiler, level);

    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);

    when(n.getType()).thenReturn(Token.THIS);
    when(checkGlobalThis.shouldReportThis(n, parent)).thenReturn(true);

    // Act
    checkGlobalThis.visit(t, n, parent);

    // Assert
    verify(mockCompiler).report(any());
  }

  @Test
  public void testVisit_NotThisToken_ShouldNotReportError() {
    // Arrange
    AbstractCompiler mockCompiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(mockCompiler, level);

    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);

    when(n.getType()).thenReturn(Token.NAME);
    when(checkGlobalThis.shouldReportThis(n, parent)).thenReturn(false);

    // Act
    checkGlobalThis.visit(t, n, parent);

    // Assert
    verify(mockCompiler, never()).report(any());
  }

  private JSDocInfo createConstructorAnnotationJSDocInfo() {
    JSDocInfo mockJSDoc = mock(JSDocInfo.class);
    when(mockJSDoc.isConstructor()).thenReturn(true);
    return mockJSDoc;
  }

  private JSDocInfo createThisAnnotationJSDocInfo() {
    JSDocInfo mockJSDoc = mock(JSDocInfo.class);
    when(mockJSDoc.hasThisType()).thenReturn(true);
    return mockJSDoc;
  }

}