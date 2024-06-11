import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckLevel level;
  private CheckGlobalThis checkGlobalThis;
  private NodeTraversal.Callback callback;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    level = CheckLevel.WARNING;
    checkGlobalThis = new CheckGlobalThis(compiler, level);
    callback = checkGlobalThis;
  }

  @Test
  public void shouldTraverse_withFunctionNodeParent_shouldReturnFalse() {
    // Arrange
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);
    when(parent.getType()).thenReturn(Token.FUNCTION);

    // Act
    boolean result = callback.shouldTraverse(t, n, parent);

    // Assert
    assertEquals(false, result);
  }

  @Test
  public void shouldTraverse_withFunctionNodeParentAndConstructorJSDoc_shouldReturnFalse() {
    // Arrange
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);
    when(parent.getType()).thenReturn(Token.FUNCTION);
    JSDocInfo jsDoc = mock(JSDocInfo.class);
    when(jsDoc.isConstructor()).thenReturn(true);
    when(checkGlobalThis.getFunctionJsDocInfo(n)).thenReturn(jsDoc);

    // Act
    boolean result = callback.shouldTraverse(t, n, parent);

    // Assert
    assertEquals(false, result);
  }

  @Test
  public void shouldTraverse_withFunctionNodeParentAndThisTypeJSDoc_shouldReturnFalse() {
    // Arrange
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);
    when(parent.getType()).thenReturn(Token.FUNCTION);
    JSDocInfo jsDoc = mock(JSDocInfo.class);
    when(jsDoc.isConstructor()).thenReturn(false);
    when(jsDoc.hasThisType()).thenReturn(true);
    when(checkGlobalThis.getFunctionJsDocInfo(n)).thenReturn(jsDoc);

    // Act
    boolean result = callback.shouldTraverse(t, n, parent);

    // Assert
    assertEquals(false, result);
  }

  @Test
  public void shouldTraverse_withFunctionNodeParentAndNestedAssign_shouldReturnFalse() {
    // Arrange
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);
    when(parent.getType()).thenReturn(Token.FUNCTION);
    JSDocInfo jsDoc = mock(JSDocInfo.class);
    when(jsDoc.isConstructor()).thenReturn(false);
    when(jsDoc.hasThisType()).thenReturn(false);
    when(checkGlobalThis.getFunctionJsDocInfo(n)).thenReturn(jsDoc);
    Node assignLhsChild = mock(Node.class);
    checkGlobalThis.assignLhsChild = assignLhsChild;

    // Act
    boolean result = callback.shouldTraverse(t, n, parent);

    // Assert
    assertEquals(false, result);
  }

  @Test
  public void shouldTraverse_withAssignParentAndThisNodeIsLhs_shouldSetAssignLhsChild() {
    // Arrange
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);
    Node lhs = mock(Node.class);
    when(parent.getType()).thenReturn(Token.ASSIGN);
    when(parent.getFirstChild()).thenReturn(lhs);
    when(lhs.getNext()).thenReturn(n);

    // Act
    boolean result = callback.shouldTraverse(t, n, parent);

    // Assert
    assertEquals(true, result);
    assertEquals(lhs, checkGlobalThis.assignLhsChild);
  }

  @Test
  public void shouldTraverse_withAssignParentAndPropertyAccessInLhsAndPrototype_shouldReturnFalse() {
    // Arrange
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);
    Node lhs = mock(Node.class);
    Node propName = mock(Node.class);
    when(parent.getType()).thenReturn(Token.ASSIGN);
    when(parent.getFirstChild()).thenReturn(lhs);
    when(lhs.getNext()).thenReturn(n);
    when(lhs.getType()).thenReturn(Token.GETPROP);
    when(lhs.getLastChild()).thenReturn(propName);
    when(propName.getString()).thenReturn("prototype");

    // Act
    boolean result = callback.shouldTraverse(t, n, parent);

    // Assert
    assertEquals(false, result);
  }

  @Test
  public void shouldTraverse_withAssignParentAndPropertyAccessInLhsAndPrototypeProperty_shouldReturnFalse() {
    // Arrange
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);
    Node lhs = mock(Node.class);
    Node propName = mock(Node.class);
    when(parent.getType()).thenReturn(Token.ASSIGN);
    when(parent.getFirstChild()).thenReturn(lhs);
    when(lhs.getNext()).thenReturn(n);
    when(lhs.getType()).thenReturn(Token.GETPROP);
    when(lhs.getLastChild()).thenReturn(propName);
    when(propName.getString()).thenReturn("some.prototype.property");

    // Act
    boolean result = callback.shouldTraverse(t, n, parent);

    // Assert
    assertEquals(false, result);
  }

  @Test
  public void shouldTraverse_withAssignParentAndThisNodeIsNotLhsAndPropertyAccessInRhs_shouldReturnFalse() {
    // Arrange
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);
    Node lhs = mock(Node.class);
    Node rhs = mock(Node.class);
    Node propName = mock(Node.class);
    when(parent.getType()).thenReturn(Token.ASSIGN);
    when(parent.getFirstChild()).thenReturn(lhs);
    when(lhs.getNext()).thenReturn(rhs);
    when(rhs.getType()).thenReturn(Token.GETPROP);
    when(rhs.getLastChild()).thenReturn(propName);
    when(propName.getString()).thenReturn("some.property");

    // Act
    boolean result = callback.shouldTraverse(t, n, parent);

    // Assert
    assertEquals(true, result);
  }

  @Test
  public void shouldTraverse_withAssignParentAndThisNodeIsNotLhsAndPropertyAccessInRhsWithPrototype_shouldReturnFalse() {
    // Arrange
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);
    Node lhs = mock(Node.class);
    Node rhs = mock(Node.class);
    Node propName = mock(Node.class);
    when(parent.getType()).thenReturn(Token.ASSIGN);
    when(parent.getFirstChild()).thenReturn(lhs);
    when(lhs.getNext()).thenReturn(rhs);
    when(rhs.getType()).thenReturn(Token.GETPROP);
    when(rhs.getLastChild()).thenReturn(propName);
    when(propName.getString()).thenReturn("prototype.some.property");

    // Act
    boolean result = callback.shouldTraverse(t, n, parent);

    // Assert
    assertEquals(true, result);
  }

  @Test
  public void visit_withThisNodeAndShouldReportThis_shouldReportError() {
    // Arrange
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);
    when(n.getType()).thenReturn(Token.THIS);
    when(checkGlobalThis.shouldReportThis(n, parent)).thenReturn(true);

    // Act
    callback.visit(t, n, parent);

    // Assert
    verify(compiler).report(t.makeError(n, level, CheckGlobalThis.GLOBAL_THIS));
  }

  @Test
  public void visit_withThisNodeAndNotShouldReportThis_shouldNotReportError() {
    // Arrange
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);
    when(n.getType()).thenReturn(Token.THIS);
    when(checkGlobalThis.shouldReportThis(n, parent)).thenReturn(false);

    // Act
    callback.visit(t, n, parent);

    // Assert
    verify(compiler, never()).report(t.makeError(n, level, CheckGlobalThis.GLOBAL_THIS));
  }

  @Test
  public void visit_withAssignLhsChild_shouldResetAssignLhsChild() {
    // Arrange
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);
    checkGlobalThis.assignLhsChild = n;

    // Act
    callback.visit(t, n, parent);

    // Assert
    assertEquals(null, checkGlobalThis.assignLhsChild);
  }

  @Test
  public void getFunctionJsDocInfo_withNoJsDocInfoOnFunction_shouldReturnParentJsDocInfo() {
    // Arrange
    Node n = mock(Node.class);
    Node parent = mock(Node.class);
    JSDocInfo parentJsDoc = mock(JSDocInfo.class);
    when(n.getParent()).thenReturn(parent);
    when(parent.getJSDocInfo()).thenReturn(parentJsDoc);

    // Act
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

    // Assert
    assertEquals(parentJsDoc, result);
  }

  @Test
  public void getFunctionJsDocInfo_withNoJsDocInfoOnFunctionAndParentIsName_shouldReturnGrandparentJsDocInfo() {
    // Arrange
    Node n = mock(Node.class);
    Node parent = mock(Node.class);
    Node grandparent = mock(Node.class);
    JSDocInfo grandparentJsDoc = mock(JSDocInfo.class);
    when(n.getParent()).thenReturn(parent);
    when(n.getJSDocInfo()).thenReturn(null);
    when(parent.getJSDocInfo()).thenReturn(null);
    when(parent.getParent()).thenReturn(grandparent);
    when(grandparent.getType()).thenReturn(Token.VAR);
    when(grandparent.getJSDocInfo()).thenReturn(grandparentJsDoc);

    // Act
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

    // Assert
    assertEquals(grandparentJsDoc, result);
  }

}