import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

@RunWith(MockitoJUnitRunner.class)
public class CheckGlobalThisTest {

  @Mock
  private AbstractCompiler compiler;
  @Mock
  private NodeTraversal nodeTraversal;
  @Mock
  private Node node;
  @Mock
  private Node parentNode;
  @Mock
  private Node assignLhsChild;
  @Mock
  private Node functionNode;
  @Mock
  private Node getPropNode;
  @Mock
  private Node stringNode;
  @Mock
  private Node numberNode;
  @Mock
  private Node blockNode;
  @Mock
  private Node scriptNode;
  @Mock
  private Node nameNode;

  private CheckGlobalThis checkGlobalThis;

  @Before
  public void setUp() {
    checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
  }

  @Test
  public void shouldTraverse_function() {
    Mockito.when(node.getType()).thenReturn(Token.FUNCTION);
    Mockito.when(node.getParent()).thenReturn(parentNode);
    Mockito.when(parentNode.getType()).thenReturn(Token.SCRIPT);

    assertTrue(checkGlobalThis.shouldTraverse(nodeTraversal, node, parentNode));
  }

  @Test
  public void shouldTraverse_constructor() {
    Mockito.when(node.getType()).thenReturn(Token.FUNCTION);
    Mockito.when(node.getJSDocInfo()).thenReturn(Mockito.mock(JSDocInfo.class));
    Mockito.when(node.getJSDocInfo().isConstructor()).thenReturn(true);

    assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, node, parentNode));
  }

  @Test
  public void shouldTraverse_functionWithAnnotation() {
    Mockito.when(node.getType()).thenReturn(Token.FUNCTION);
    Mockito.when(node.getJSDocInfo()).thenReturn(Mockito.mock(JSDocInfo.class));
    Mockito.when(node.getJSDocInfo().hasThisType()).thenReturn(true);

    assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, node, parentNode));
  }

  @Test
  public void shouldTraverse_prototypeMethod() {
    Mockito.when(node.getType()).thenReturn(Token.FUNCTION);
    Mockito.when(parentNode.getType()).thenReturn(Token.GETPROP);
    Mockito.when(parentNode.getLastChild().getString()).thenReturn("prototype");

    assertTrue(checkGlobalThis.shouldTraverse(nodeTraversal, node, parentNode));
  }

  @Test
  public void shouldNotTraverse_objectLiteralKey() {
    Mockito.when(node.getType()).thenReturn(Token.STRING);
    Mockito.when(parentNode.getType()).thenReturn(Token.ASSIGN);
    Mockito.when(parentNode.getFirstChild().getType()).thenReturn(Token.GETPROP);
    Mockito.when(parentNode.getFirstChild().getLastChild().getString()).thenReturn("prototype");

    assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, node, parentNode));
  }

  @Test
  public void shouldNotTraverse_objectLiteralKeyDeep() {
    Mockito.when(node.getType()).thenReturn(Token.NUMBER);
    Mockito.when(parentNode.getType()).thenReturn(Token.ASSIGN);
    Mockito.when(parentNode.getFirstChild().getType()).thenReturn(Token.GETPROP);
    Mockito.when(parentNode.getFirstChild().getLastChild().getString()).thenReturn("prototype");
    Mockito.when(parentNode.getNext().getType()).thenReturn(Token.GETPROP);
    Mockito.when(parentNode.getNext().getLastChild().getString()).thenReturn("prototype");

    assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, node, parentNode));
  }

  @Test
  public void visit_shouldReportThisOnLeftSideOfAssignment() {
    Mockito.when(node.getType()).thenReturn(Token.THIS);
    Mockito.when(parentNode.getType()).thenReturn(Token.ASSIGN);
    Mockito.when(assignLhsChild.getType()).thenReturn(Token.IDENT);
    Mockito.when(assignLhsChild.getParent()).thenReturn(parentNode);

    checkGlobalThis.visit(nodeTraversal, node, parentNode);
    assertNotNull(compiler.reportCallCount);
    assertTrue(compiler.reportCallCount > 0);
  }

  @Test
  public void visit_shouldNotReportThisOnRightSideOfAssignment() {
    Mockito.when(node.getType()).thenReturn(Token.THIS);
    Mockito.when(parentNode.getType()).thenReturn(Token.ASSIGN);
    Mockito.when(assignLhsChild.getType()).thenReturn(Token.IDENT);
    Mockito.when(assignLhsChild.getParent()).thenReturn(parentNode);
    Mockito.when(parentNode.getFirstChild().getType()).thenReturn(Token.IDENT);

    checkGlobalThis.visit(nodeTraversal, node, parentNode);
    assertNotNull(compiler.reportCallCount);
    assertEquals(0, compiler.reportCallCount);
  }

  @Test
  public void visit_shouldReportThisOnPropertyAccess() {
    Mockito.when(node.getType()).thenReturn(Token.THIS);
    Mockito.when(parentNode.getType()).thenReturn(Token.GETPROP);

    checkGlobalThis.visit(nodeTraversal, node, parentNode);
    assertNotNull(compiler.reportCallCount);
    assertTrue(compiler.reportCallCount > 0);
  }

  @Test
  public void visit_shouldNotReportThisOnPropertyAccessOfPrototype() {
    Mockito.when(node.getType()).thenReturn(Token.THIS);
    Mockito.when(parentNode.getType()).thenReturn(Token.GETPROP);
    Mockito.when(parentNode.getLastChild().getString()).thenReturn("prototype");

    checkGlobalThis.visit(nodeTraversal, node, parentNode);
    assertNotNull(compiler.reportCallCount);
    assertEquals(0, compiler.reportCallCount);
  }

  @Test
  public void getFunctionJsDocInfo_shouldReturnJsDocInfo() {
    Mockito.when(node.getType()).thenReturn(Token.FUNCTION);

    JSDocInfo jsDocInfo = checkGlobalThis.getFunctionJsDocInfo(node);
    assertNotNull(jsDocInfo);
  }

  @Test
  public void getFunctionJsDocInfo_shouldReturnNullIfNoJsDocInfo() {
    Mockito.when(node.getType()).thenReturn(Token.FUNCTION);

    JSDocInfo jsDocInfo = checkGlobalThis.getFunctionJsDocInfo(node);
    assertNull(jsDocInfo);
  }

  @Test
  public void shouldReportThis_onLeftSideOfAssignment() {
    Mockito.when(node.getType()).thenReturn(Token.THIS);
    Mockito.when(parentNode.getType()).thenReturn(Token.ASSIGN);
    Mockito.when(assignLhsChild.getType()).thenReturn(Token.IDENT);
    Mockito.when(assignLhsChild.getParent()).thenReturn(parentNode);

    assertTrue(checkGlobalThis.shouldReportThis(node, parentNode));
  }

  @Test
  public void shouldNotReportThis_onRightSideOfAssignment() {
    Mockito.when(node.getType()).thenReturn(Token.THIS);
    Mockito.when(parentNode.getType()).thenReturn(Token.ASSIGN);
    Mockito.when(parentNode.getFirstChild().getType()).thenReturn(Token.IDENT);

    assertFalse(checkGlobalThis.shouldReportThis(node, parentNode));
  }

  @Test
  public void shouldNotReportThis_onPropertyAccess() {
    Mockito.when(node.getType()).thenReturn(Token.THIS);
    Mockito.when(parentNode.getType()).thenReturn(Token.GETPROP);

    assertFalse(checkGlobalThis.shouldReportThis(node, parentNode));
  }

  @Test
  public void shouldReportThis_onPropertyAccessOfPrototype() {
    Mockito.when(node.getType()).thenReturn(Token.THIS);
    Mockito.when(parentNode.getType()).thenReturn(Token.GETPROP);
    Mockito.when(parentNode.getLastChild().getString()).thenReturn("prototype");

    assertFalse(checkGlobalThis.shouldReportThis(node, parentNode));
  }
}