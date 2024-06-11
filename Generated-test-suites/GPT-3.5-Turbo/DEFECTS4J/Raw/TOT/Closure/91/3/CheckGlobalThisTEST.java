import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;

public class CheckGlobalThisTest {

  private CheckGlobalThis checkGlobalThis;
  private AbstractCompiler mockCompiler;
  private NodeTraversal.Callback mockCallback;
  private Node mockNode;
  private NodeTraversal mockTraversal;
  private Node mockParent;

  @Before
  public void setUp() {
    mockCompiler = mock(AbstractCompiler.class);
    mockCallback = mock(NodeTraversal.Callback.class);
    mockNode = mock(Node.class);
    mockTraversal = mock(NodeTraversal.class);
    mockParent = mock(Node.class);

    checkGlobalThis = new CheckGlobalThis(mockCompiler, CheckLevel.WARNING);
  }

  @Test
  public void testShouldTraverse_FunctionWithConstructorAnnotation_ReturnFalse() {
    JSDocInfo mockJsDocInfo = mockConstructorJsDocInfo();
    when(mockNode.getType()).thenReturn(Token.FUNCTION);
    when(mockNode.getJSDocInfo()).thenReturn(mockJsDocInfo);
    when(mockNode.getParent()).thenReturn(mockParent);

    boolean result = checkGlobalThis.shouldTraverse(mockTraversal, mockNode, mockParent);

    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionWithThisTypeAnnotation_ReturnFalse() {
    JSDocInfo mockJsDocInfo = mockThisTypeJsDocInfo();
    when(mockNode.getType()).thenReturn(Token.FUNCTION);
    when(mockNode.getJSDocInfo()).thenReturn(mockJsDocInfo);
    when(mockNode.getParent()).thenReturn(mockParent);

    boolean result = checkGlobalThis.shouldTraverse(mockTraversal, mockNode, mockParent);

    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionWithOverrideAnnotation_ReturnFalse() {
    JSDocInfo mockJsDocInfo = mockOverrideJsDocInfo();
    when(mockNode.getType()).thenReturn(Token.FUNCTION);
    when(mockNode.getJSDocInfo()).thenReturn(mockJsDocInfo);
    when(mockNode.getParent()).thenReturn(mockParent);

    boolean result = checkGlobalThis.shouldTraverse(mockTraversal, mockNode, mockParent);

    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionInNonGlobalContext_ReturnFalse() {
    when(mockNode.getType()).thenReturn(Token.FUNCTION);
    when(mockNode.getParent()).thenReturn(mockParent);
    when(mockParent.getType()).thenReturn(Token.ADD);

    boolean result = checkGlobalThis.shouldTraverse(mockTraversal, mockNode, mockParent);

    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionInGlobalContext_ReturnTrue() {
    when(mockNode.getType()).thenReturn(Token.FUNCTION);
    when(mockNode.getParent()).thenReturn(mockParent);
    when(mockParent.getType()).thenReturn(Token.SCRIPT);

    boolean result = checkGlobalThis.shouldTraverse(mockTraversal, mockNode, mockParent);

    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_FunctionInGlobalObjectContext_ReturnTrue() {
    when(mockNode.getType()).thenReturn(Token.FUNCTION);
    when(mockNode.getParent()).thenReturn(mockParent);
    when(mockParent.getType()).thenReturn(Token.GETPROP);

    boolean result = checkGlobalThis.shouldTraverse(mockTraversal, mockNode, mockParent);

    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_LeftSideOfAssignment_ReturnTrue() {
    when(mockNode.getType()).thenReturn(Token.NAME);
    when(mockNode.getParent()).thenReturn(mockParent);
    when(mockParent.getType()).thenReturn(Token.ASSIGN);
    when(mockNode.getNext()).thenReturn(mockNode);

    boolean result = checkGlobalThis.shouldTraverse(mockTraversal, mockNode, mockParent);

    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_RightSideOfAssignmentToPrototype_ReturnFalse() {
    Node mockLhs = mock(Node.class);
    when(mockNode.getType()).thenReturn(Token.GETPROP);
    when(mockNode.getParent()).thenReturn(mockParent);
    when(mockParent.getType()).thenReturn(Token.ASSIGN);
    when(mockParent.getFirstChild()).thenReturn(mockLhs);
    when(mockLhs.getType()).thenReturn(Token.GETPROP);
    when(mockLhs.getLastChild()).thenReturn(mockNode);
    when(mockNode.getString()).thenReturn("prototype");

    boolean result = checkGlobalThis.shouldTraverse(mockTraversal, mockNode, mockParent);

    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_RightSideOfAssignmentToNestedPrototype_ReturnFalse() {
    Node mockLhs = mock(Node.class);
    Node mockLlhs = mock(Node.class);
    when(mockNode.getType()).thenReturn(Token.GETPROP);
    when(mockNode.getParent()).thenReturn(mockParent);
    when(mockParent.getType()).thenReturn(Token.ASSIGN);
    when(mockParent.getFirstChild()).thenReturn(mockLhs);
    when(mockLhs.getType()).thenReturn(Token.STRING);
    when(mockLhs.getNext()).thenReturn(mockLlhs);
    when(mockLlhs.getType()).thenReturn(Token.GETPROP);
    when(mockLlhs.getLastChild()).thenReturn(mockNode);
    when(mockNode.getString()).thenReturn("prototype");

    boolean result = checkGlobalThis.shouldTraverse(mockTraversal, mockNode, mockParent);

    assertFalse(result);
  }

  @Test
  public void testVisit_ThisNodeWithoutParent_ReturnsNoErrorReported() {
    when(mockNode.getType()).thenReturn(Token.THIS);
    when(mockTraversal.makeError(eq(mockNode), any(CheckLevel.class), any(DiagnosticType.class)))
        .thenReturn(mock(JSError.class));

    checkGlobalThis.visit(mockTraversal, mockNode, null);

    verify(mockCompiler, never()).report(any(JSError.class));
  }

  @Test
  public void testVisit_ThisNodeWithParent_ReturnsErrorReported() {
    Node mockError = mock(Node.class);
    when(mockNode.getType()).thenReturn(Token.THIS);
    when(mockTraversal.makeError(eq(mockNode), any(CheckLevel.class), any(DiagnosticType.class)))
        .thenReturn(mockError);

    checkGlobalThis.visit(mockTraversal, mockNode, mockParent);

    verify(mockCompiler).report(mockError);
  }

  @Test
  public void testVisit_NotAssignLhsChild_ReturnsNoErrorReported() {
    when(mockNode.getType()).thenReturn(Token.NOT);

    checkGlobalThis.visit(mockTraversal, mockNode, mockParent);

    verify(mockCompiler, never()).report(any(JSError.class));
  }

  @Test
  public void testVisit_AssignLhsChild_ReturnsErrorReported() {
    Node mockError = mock(Node.class);
    when(mockNode.getType()).thenReturn(Token.NOT);
    when(mockNode == mockNode).thenReturn(true);
    when(mockTraversal.makeError(eq(mockNode), any(CheckLevel.class), any(DiagnosticType.class)))
        .thenReturn(mockError);

    checkGlobalThis.visit(mockTraversal, mockNode, mockParent);

    verify(mockCompiler).report(mockError);
  }

  private JSDocInfo mockConstructorJsDocInfo() {
    JSDocInfo mockJsDocInfo = mock(JSDocInfo.class);
    when(mockJsDocInfo.isConstructor()).thenReturn(true);
    return mockJsDocInfo;
  }

  private JSDocInfo mockThisTypeJsDocInfo() {
    JSDocInfo mockJsDocInfo = mock(JSDocInfo.class);
    when(mockJsDocInfo.hasThisType()).thenReturn(true);
    return mockJsDocInfo;
  }

  private JSDocInfo mockOverrideJsDocInfo() {
    JSDocInfo mockJsDocInfo = mock(JSDocInfo.class);
    when(mockJsDocInfo.isOverride()).thenReturn(true);
    return mockJsDocInfo;
  }
}