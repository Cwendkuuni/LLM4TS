import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.compiler.Compiler;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

@RunWith(MockitoJUnitRunner.class)
public class CheckGlobalThisTest {

  @Mock
  private AbstractCompiler compiler;

  @Mock
  private Compiler compilerMock;

  @Mock
  private Node node;

  @Mock
  private Node parentNode;

  @Mock
  private Node assignParentNode;

  @Mock
  private Node assignLhsNode;

  @Mock
  private Node lhsNode;

  @Mock
  private Node getPropNode;

  @InjectMocks
  private CheckGlobalThis checkGlobalThis;

  @Before
  public void setUp() {
    when(compilerMock.getCurrentLevel()).thenReturn(CheckLevel.WARNING);
    when(compiler).thenReturn(compilerMock);
    when(node.getType()).thenAnswer(invocation -> invocation.getArgument(0));
    when(parentNode.getType()).thenReturn(Token.FUNCTION);
    when(assignParentNode.getType()).thenReturn(Token.ASSIGN);
    when(assignLhsNode.getType()).thenReturn(Token.NAME);
    when(lhsNode.getType()).thenReturn(Token.GETPROP);
    when(getPropNode.getString()).thenReturn("prototype");
    when(getPropNode.getLastChild()).thenReturn(getPropNode);
  }

  @Test
  public void shouldNotTraverseNonGlobalContexts() {
    when(parentNode.getParent()).thenReturn(node);

    assertFalse(checkGlobalThis.shouldTraverse(mock(NodeTraversal.class), node, parentNode));
  }

  @Test
  public void shouldTraverseConstructors() {
    JSDocInfo jsDoc = mock(JSDocInfo.class);
    when(jsDoc.isConstructor()).thenReturn(true);

    when(node.getJSDocInfo()).thenReturn(jsDoc);

    when(parentNode.getParent()).thenReturn(node);

    assertFalse(checkGlobalThis.shouldTraverse(mock(NodeTraversal.class), node, parentNode));
  }

  @Test
  public void shouldTraverseFunctionsAnnotatedWithAtthis() {
    JSDocInfo jsDoc = mock(JSDocInfo.class);
    when(jsDoc.hasThisType()).thenReturn(true);

    when(node.getJSDocInfo()).thenReturn(jsDoc);

    when(parentNode.getParent()).thenReturn(node);

    assertFalse(checkGlobalThis.shouldTraverse(mock(NodeTraversal.class), node, parentNode));
  }

  @Test
  public void shouldTraverseFunctionsAnnotatedWithAtconstructor() {
    JSDocInfo jsDoc = mock(JSDocInfo.class);
    when(jsDoc.isConstructor()).thenReturn(true);

    when(node.getJSDocInfo()).thenReturn(jsDoc);

    when(parentNode.getParent()).thenReturn(node);

    assertTrue(checkGlobalThis.shouldTraverse(mock(NodeTraversal.class), node, parentNode));
  }

  @Test
  public void shouldTraverseFunctionsInScriptContext() {
    when(parentNode.getType()).thenReturn(Token.SCRIPT);

    assertTrue(checkGlobalThis.shouldTraverse(mock(NodeTraversal.class), node, parentNode));
  }

  @Test
  public void shouldTraverseFunctionsInBlockContext() {
    when(parentNode.getType()).thenReturn(Token.BLOCK);

    assertTrue(checkGlobalThis.shouldTraverse(mock(NodeTraversal.class), node, parentNode));
  }

  @Test
  public void shouldTraverseFunctionsWithNoLogicalPlaceForThisAnnotation() {
    when(parentNode.getType()).thenReturn(Token.NAME);
    when(parentNode.getLastChild().getString()).thenReturn("a");

    assertTrue(checkGlobalThis.shouldTraverse(mock(NodeTraversal.class), node, parentNode));
  }

  @Test
  public void shouldTraverseFunctionsLentToPrototype() {
    when(parentNode.getType()).thenReturn(Token.ASSIGN);
    when(assignParentNode.getType()).thenReturn(Token.NAME);
    when(assignParentNode.getLastChild().getType()).thenReturn(Token.GETPROP);
    when(assignParentNode.getLastChild().getLastChild().getString()).thenReturn("prototype");

    assertFalse(checkGlobalThis.shouldTraverse(mock(NodeTraversal.class), node, assignParentNode));
  }

  @Test
  public void shouldTraverseFunctionsOnLeftSideOfAssignment() {
    when(parentNode.getType()).thenReturn(Token.ASSIGN);
    when(parentNode.getFirstChild().getType()).thenReturn(Token.NAME);

    assertTrue(checkGlobalThis.shouldTraverse(mock(NodeTraversal.class), node, parentNode));
  }

  @Test
  public void shouldReportGlobalThisKeyword() {
    when(parentNode.getType()).thenReturn(Token.ASSIGN);
    when(parentNode.getFirstChild().getType()).thenReturn(Token.THIS);

    checkGlobalThis.visit(mock(NodeTraversal.class), node, parentNode);

    verify(compiler).report(argThat(diagnostic -> diagnostic.getMessage()
        .equals("dangerous use of the global 'this' object")));
  }

  @Test
  public void shouldNotReportGlobalThisKeywordInsidePrototypeMethod() {
    JSDocInfo jsDoc = mock(JSDocInfo.class);
    when(jsDoc.isConstructor()).thenReturn(false);
    when(jsDoc.isInterface()).thenReturn(false);

    when(node.getParent().getType()).thenReturn(Token.FUNCTION);
    when(node.getParent().getJSDocInfo()).thenReturn(jsDoc);

    when(parentNode.getType()).thenReturn(Token.ASSIGN);
    when(parentNode.getFirstChild().getType()).thenReturn(Token.THIS);

    checkGlobalThis.visit(mock(NodeTraversal.class), node, parentNode);

    verifyNoMoreInteractions(compiler);
  }

  @Test
  public void shouldReportGlobalThisKeywordInsideFunctionAnnotatedWithAtthis() {
    JSDocInfo jsDoc = mock(JSDocInfo.class);
    when(jsDoc.hasThisType()).thenReturn(true);

    when(node.getParent().getType()).thenReturn(Token.FUNCTION);
    when(node.getParent().getJSDocInfo()).thenReturn(jsDoc);

    when(parentNode.getType()).thenReturn(Token.ASSIGN);
    when(parentNode.getFirstChild().getType()).thenReturn(Token.THIS);

    checkGlobalThis.visit(mock(NodeTraversal.class), node, parentNode);

    verify(compiler).report(argThat(diagnostic -> diagnostic.getMessage()
        .equals("dangerous use of the global 'this' object")));
  }

  @Test
  public void shouldReportGlobalThisKeywordInsideFunctionAnnotatedWithAtconstructor() {
    JSDocInfo jsDoc = mock(JSDocInfo.class);
    when(jsDoc.isConstructor()).thenReturn(true);

    when(node.getParent().getType()).thenReturn(Token.FUNCTION);
    when(node.getParent().getJSDocInfo()).thenReturn(jsDoc);

    when(parentNode.getType()).thenReturn(Token.ASSIGN);
    when(parentNode.getFirstChild().getType()).thenReturn(Token.THIS);

    checkGlobalThis.visit(mock(NodeTraversal.class), node, parentNode);

    verifyNoMoreInteractions(compiler);
  }

  @Test
  public void shouldReportGlobalThisKeywordInsideFunction() {
    when(parentNode.getType()).thenReturn(Token.ASSIGN);
    when(parentNode.getFirstChild().getType()).thenReturn(Token.NAME);
    when(parentNode.getFirstChild().getLastChild().getType()).thenReturn(Token.THIS);

    checkGlobalThis.visit(mock(NodeTraversal.class), node, parentNode);

    verify(compiler).report(argThat(diagnostic -> diagnostic.getMessage()
        .equals("dangerous use of the global 'this' object")));
  }

  @Test
  public void shouldReportGlobalThisKeywordInsidePropertyAccess() {
    when(parentNode.getType()).thenReturn(Token.ASSIGN);
    when(parentNode.getFirstChild().getType()).thenReturn(Token.NAME);
    when(parentNode.getFirstChild().getLastChild().getType()).thenReturn(Token.GETPROP);
    when(parentNode.getFirstChild().getFirstChild().getType()).thenReturn(Token.THIS);

    checkGlobalThis.visit(mock(NodeTraversal.class), node, parentNode);

    verify(compiler).report(argThat(diagnostic -> diagnostic.getMessage()
        .equals("dangerous use of the global 'this' object")));
  }

  @Test
  public void shouldNotReportGlobalThisKeywordInsideObjectLiteralKey() {
    when(parentNode.getType()).thenReturn(Token.ASSIGN);
    when(parentNode.getFirstChild().getType()).thenReturn(Token.NAME);
    when(parentNode.getFirstChild().getLastChild().getType()).thenReturn(Token.GETPROP);
    when(parentNode.getFirstChild().getFirstChild().getType()).thenReturn(Token.STRING);

    checkGlobalThis.visit(mock(NodeTraversal.class), node, parentNode);

    verifyNoMoreInteractions(compiler);
  }

  @Test
  public void shouldReportGlobalThisKeywordInsideObjectLiteralKeyChain() {
    when(parentNode.getType()).thenReturn(Token.ASSIGN);
    when(parentNode.getFirstChild().getType()).thenReturn(Token.NAME);
    when(parentNode.getFirstChild().getLastChild().getType()).thenReturn(Token.GETPROP);
    when(parentNode.getFirstChild().getFirstChild().getLastChild().getType())
        .thenReturn(Token.GETPROP);
    when(parentNode.getFirstChild().getFirstChild().getFirstChild().getType())
        .thenReturn(Token.THIS);

    checkGlobalThis.visit(mock(NodeTraversal.class), node, parentNode);

    verify(compiler).report(argThat(diagnostic -> diagnostic.getMessage()
        .equals("dangerous use of the global 'this' object")));
  }

  @Test
  public void shouldNotReportGlobalThisKeywordOnLeftSideOfAssignment() {
    when(parentNode.getType()).thenReturn(Token.ASSIGN);
    when(parentNode.getFirstChild().getType()).thenReturn(Token.THIS);

    assertNotNull(checkGlobalThis.assignLhsChild);

    checkGlobalThis.visit(mock(NodeTraversal.class), node, parentNode);

    verifyNoMoreInteractions(compiler);
  }
}