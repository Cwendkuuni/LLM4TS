import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.jscomp.NodeVisitScope;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CheckGlobalThisTest {

  @InjectMocks
  private CheckGlobalThis checkGlobalThis;

  @Mock
  private AbstractCompiler compiler;

  @Mock
  private NodeTraversal traversal;

  @Mock
  private Node node;

  @Mock
  private Node parentNode;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
  }

  @Test
  public void testShouldTraverseFunction() {
    when(node.getType()).thenReturn(Token.FUNCTION);
    when(node.getParent()).thenReturn(null);

    assertTrue(checkGlobalThis.shouldTraverse(traversal, node, parentNode));
  }

  @Test
  public void testShouldNotTraverseConstructorFunction() {
    JSDocInfo jsDocInfo = mock(JSDocInfo.class);
    when(jsDocInfo.isConstructor()).thenReturn(true);
    when(node.getJSDocInfo()).thenReturn(jsDocInfo);

    when(node.getType()).thenReturn(Token.FUNCTION);

    assertFalse(checkGlobalThis.shouldTraverse(traversal, node, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    JSDocInfo jsDocInfo = mock(JSDocInfo.class);
    when(jsDocInfo.hasThisType()).thenReturn(true);
    when(node.getJSDocInfo()).thenReturn(jsDocInfo);

    when(node.getType()).thenReturn(Token.FUNCTION);

    assertFalse(checkGlobalThis.shouldTraverse(traversal, node, parentNode));
  }

  @Test
  public void testShouldTraverseFunctionWithoutJsDocInfo() {
    when(node.getType()).thenReturn(Token.FUNCTION);
    when(node.getParent()).thenReturn(null);

    assertTrue(checkGlobalThis.shouldTraverse(traversal, node, parentNode));
  }

  @Test
  public void testShouldNotTraverseAssignmentLeftSide() {
    Node leftSide = mock(Node.class);
    Node rightSide = mock(Node.class);
    Node assignNode = mock(Node.class);

    when(leftSide.getType()).thenReturn(Token.NAME);
    when(leftSide.getParent()).thenReturn(assignNode);
    when(rightSide.getType()).thenReturn(Token.THIS);
    when(assignNode.getType()).thenReturn(Token.ASSIGN);
    when(assignNode.getFirstChild()).thenReturn(leftSide);
    when(assignNode.getLastChild()).thenReturn(rightSide);

    assertFalse(checkGlobalThis.shouldTraverse(traversal, node, parentNode));
  }

  @Test
  public void testShouldNotTraversePrototypePropertyAssignment() {
    Node leftSide = mock(Node.class);
    Node assignNode = mock(Node.class);

    when(leftSide.getType()).thenReturn(Token.GETPROP);
    when(leftSide.getLastChild().getString()).thenReturn("prototype");
    when(assignNode.getType()).thenReturn(Token.ASSIGN);
    when(assignNode.getFirstChild()).thenReturn(leftSide);

    assertFalse(checkGlobalThis.shouldTraverse(traversal, node, parentNode));
  }

  @Test
  public void testShouldNotTraverseQualifiedPrototypePropertyAssignment() {
    Node leftSide = mock(Node.class);
    Node assignNode = mock(Node.class);

    when(leftSide.getType()).thenReturn(Token.GETPROP);
    String leftName = "someObject.prototype";
    when(leftSide.getQualifiedName()).thenReturn(leftName);
    when(assignNode.getType()).thenReturn(Token.ASSIGN);
    when(assignNode.getFirstChild()).thenReturn(leftSide);

    assertFalse(checkGlobalThis.shouldTraverse(traversal, node, parentNode));
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    Node leftSide = mock(Node.class);
    Node assignNode = mock(Node.class);

    when(leftSide.getType()).thenReturn(Token.ASSIGN);
    when(leftSide.getLastChild()).thenReturn(node);

    when(assignNode.getType()).thenReturn(Token.ASSIGN);
    when(assignNode.getFirstChild()).thenReturn(leftSide);

    when(shouldReportThis(node, parentNode)).thenReturn(true);

    checkGlobalThis.visit(traversal, node, parentNode);

    verify(compiler).report(eq(traversal.makeError(node, equals(CheckLevel.WARNING), eq(GLOBAL_THIS))));
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfAssignment() {
    Node leftSide = mock(Node.class);
    Node assignNode = mock(Node.class);

    when(leftSide.getType()).thenReturn(Token.ASSIGN);
    when(leftSide.getLastChild()).thenReturn(parentNode);

    when(assignNode.getType()).thenReturn(Token.ASSIGN);
    when(assignNode.getFirstChild()).thenReturn(node);

    when(shouldReportThis(node, parentNode)).thenReturn(false);

    checkGlobalThis.visit(traversal, node, parentNode);

    verifyZeroInteractions(compiler);
  }

  @Test
  public void testShouldNotReportThisOnLeftSideOfFunctionCall() {
    Node leftSide = mock(Node.class);
    Node functionCall = mock(Node.class);

    when(leftSide.getType()).thenReturn(Token.THIS);
    when(functionCall.getType()).thenReturn(Token.CALL);
    when(functionCall.getFirstChild()).thenReturn(node);

    when(shouldReportThis(node, parentNode)).thenReturn(false);

    checkGlobalThis.visit(traversal, node, parentNode);

    verifyZeroInteractions(compiler);
  }

  private boolean shouldReportThis(Node node, Node parentNode) {
    return checkGlobalThis.shouldReportThis(node, parentNode);
  }
}