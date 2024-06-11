import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.TestSourceFile;
import com.google.javascript.rhino.JSDocInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.JUnit4;

import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public class SyntacticScopeCreatorTest {

  @Mock
  private AbstractCompiler compiler;

  @Mock
  private ScopeCreator scopeCreator;

  @Mock
  private Scope parentScope;

  @Mock
  private Node rootNode;

  @Mock
  private Node functionNode;

  @Mock
  private Node varNode;

  @Mock
  private Node argsNode;

  @Mock
  private Node bodyNode;

  @Mock
  private Node functionExpressionNode;

  @Mock
  private Node catchNode;

  @Captor
  private ArgumentCaptor<String> nameCaptor;

  @Captor
  private ArgumentCaptor<Node> nodeCaptor;

  @Captor
  private ArgumentCaptor<JSType> declaredTypeCaptor;

  @Captor
  private ArgumentCaptor<TestSourceFile> inputCaptor;

  @Before
  public void setUp() {
    when(compiler.createScopeCreator(any(AbstractCompiler.class)))
        .thenReturn(scopeCreator);
  }

  @Test
  public void testCreateScopeForRootNode() {
    when(rootNode.getType()).thenReturn(Token.SCRIPT);

    Scope scope = scopeCreator.createScope(rootNode, null);

    verify(scopeCreator).scanRoot(rootNode, null);
    verifyNoMoreInteractions(scopeCreator);

    assertEquals("global", scope.getName());
    assertNull(scope.getParent());
    assertTrue(scope.isGlobal());
  }

  @Test
  public void testCreateScopeForFunctionNode() {
    when(rootNode.getType()).thenReturn(Token.FUNCTION);
    when(functionNode.getType()).thenReturn(Token.FUNCTION);

    when(functionNode.getFirstChild().getString()).thenReturn("foo");

    Scope scope = scopeCreator.createScope(rootNode, null);

    verify(scopeCreator).scanRoot(rootNode, null);
    verify(scopeCreator).declareVar("foo", functionNode.getFirstChild(),
                                   functionNode, null, null, functionNode);

    ArgumentCaptor<Scope> parentScopeCaptor = ArgumentCaptor.forClass(Scope.class);
    verify(scopeCreator).declareVar("foo", functionNode.getFirstChild(),
                                   functionNode, parentScopeCaptor.capture(),
                                   null, functionNode);
    assertEquals(scope, parentScopeCaptor.getValue());

    verifyNoMoreInteractions(scopeCreator);
  }

  @Test
  public void testCreateScopeForFunctionExpressionNode() {
    when(rootNode.getType()).thenReturn(Token.FUNCTION);
    when(functionNode.getType()).thenReturn(Token.FUNCTION_EXPRESSION);

    when(functionNode.getFirstChild().getString()).thenReturn("foo");

    Scope scope = scopeCreator.createScope(rootNode, null);

    verify(scopeCreator).scanRoot(rootNode, null);
    verify(scopeCreator, never()).declareVar("foo", functionNode.getFirstChild(),
                                            functionNode, null, null, functionNode);

    verifyNoMoreInteractions(scopeCreator);
  }

  @Test
  public void testCreateScopeForVariableDeclarationNode() {
    when(rootNode.getType()).thenReturn(Token.VAR);
    when(varNode.getType()).thenReturn(Token.NAME);
    when(varNode.getString()).thenReturn("foo");

    Scope scope = scopeCreator.createScope(rootNode, null);

    verify(scopeCreator).scanRoot(rootNode, null);
    verify(scopeCreator).declareVar("foo", varNode, rootNode, null, null, rootNode);

    verifyNoMoreInteractions(scopeCreator);
  }

  @Test
  public void testCreateScopeForFunctionWithArguments() {
    when(rootNode.getType()).thenReturn(Token.FUNCTION);
    when(functionNode.getType()).thenReturn(Token.FUNCTION);

    when(functionNode.getFirstChild().getString()).thenReturn("foo");

    Node argsNode = mock(Node.class);
    when(argsNode.getType()).thenReturn(Token.LP);

    Node argNode = mock(Node.class);
    when(argNode.getType()).thenReturn(Token.NAME);
    when(argNode.getString()).thenReturn("arg");

    when(functionNode.getFirstChild().getNext()).thenReturn(argsNode);
    when(argsNode.getFirstChild()).thenReturn(argNode);
    when(argsNode.getNext()).thenReturn(bodyNode);

    Scope scope = scopeCreator.createScope(rootNode, null);

    verify(scopeCreator).scanRoot(rootNode, null);
    verify(scopeCreator).declareVar("foo", functionNode.getFirstChild(),
                                   functionNode, null, null, functionNode);
    verify(scopeCreator).declareVar("arg", argNode, argsNode, functionNode, null, rootNode);

    verifyNoMoreInteractions(scopeCreator);
  }

  @Test
  public void testCreateScopeForCatchBlock() {
    when(rootNode.getType()).thenReturn(Token.SCRIPT);
    when(parentScope.isGlobal()).thenReturn(false);

    Node catchNode = mock(Node.class);
    when(catchNode.getType()).thenReturn(Token.CATCH);
    when(catchNode.getFirstChild()).thenReturn(varNode);
    when(varNode.getType()).thenReturn(Token.NAME);
    when(varNode.getString()).thenReturn("varName");
    when(varNode.getNext()).thenReturn(bodyNode);

    Scope scope = scopeCreator.createScope(rootNode, parentScope);

    verify(scopeCreator).scanRoot(rootNode, parentScope);
    verify(scopeCreator).declareVar("varName", varNode, parentScope, null, null, rootNode);

    verifyNoMoreInteractions(scopeCreator);
  }

  @Test
  public void testOnRedeclarationForGlobalScope() {
    when(rootNode.getType()).thenReturn(Token.SCRIPT);
    when(scope.isGlobal()).thenReturn(true);

    Node varNode1 = mock(Node.class);
    when(varNode1.getString()).thenReturn("var1");

    Node varNode2 = mock(Node.class);
    when(varNode2.getString()).thenReturn("var1");

    when(scope.getVar("var1", false)).thenReturn(null);

    scopeCreator.declareVar("var1", varNode1, rootNode, null, null, rootNode);
    scopeCreator.declareVar("var1", varNode2, rootNode, null, null, rootNode);

    verify(compiler).report(any(JSError.class));
  }

  @Test
  public void testOnRedeclarationForLocalScope() {
    when(rootNode.getType()).thenReturn(Token.SCRIPT);
    when(scope.isGlobal()).thenReturn(false);

    Node varNode1 = mock(Node.class);
    when(varNode1.getString()).thenReturn("var1");

    Node varNode2 = mock(Node.class);
    when(varNode2.getString()).thenReturn("var1");

    Scope parentScope = mock(Scope.class);

    when(scope.getVar("var1", false)).thenReturn(null);

    scopeCreator.declareVar("var1", varNode1, rootNode, parentScope, null, rootNode);
    scopeCreator.declareVar("var1", varNode2, rootNode, parentScope, null, rootNode);

    verifyNoMoreInteractions(compiler);
  }

  @Test
  public void testOnRedeclarationForArgumentsShadowing() {
    when(rootNode.getType()).thenReturn(Token.SCRIPT);
    when(scope.isGlobal()).thenReturn(false);

    Node functionNode = mock(Node.class);
    when(functionNode.getType()).thenReturn(Token.FUNCTION);

    Node argumentsNode = mock(Node.class);
    when(argumentsNode.getType()).thenReturn(Token.LP);

    Node argNode = mock(Node.class);
    when(argNode.getType()).thenReturn(Token.NAME);
    when(argNode.getString()).thenReturn("arg");

    when(functionNode.getFirstChild()).thenReturn(argumentsNode);
    when(argumentsNode.getFirstChild()).thenReturn(argNode);
    when(argumentsNode.getNext()).thenReturn(bodyNode);

    Node varNode = mock(Node.class);
    when(varNode.getString()).thenReturn("arguments");

    when(scope.getVar("arguments", false)).thenReturn(null);

    scopeCreator.declareVar("arguments", varNode, rootNode, null, null, rootNode);

    verify(compiler).report(eq(VAR_ARGUMENTS_SHADOWED_ERROR));
  }
}