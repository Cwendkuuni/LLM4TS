import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.Token;
import com.google.javascript.rhino.JSDocInfo;

import org.junit.Before;
import org.junit.Test;

public class SyntacticScopeCreatorTest {

  private AbstractCompiler compiler;
  private SyntacticScopeCreator scopeCreator;
  private Scope parentScope;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    parentScope = mock(Scope.class);
    scopeCreator = new SyntacticScopeCreator(compiler);
  }

  @Test
  public void testCreateScope() {
    // Test empty scope creation
    Scope returnedScope = scopeCreator.createScope(null, null);
    assertNotNull(returnedScope);
    assertSame(scopeCreator, returnedScope.getScopeCreator());

    // Test non-empty scope creation
    Node rootNode = new Node(Token.SCRIPT, null);
    Scope createdScope = scopeCreator.createScope(rootNode, null);
    assertNotNull(createdScope);
    assertSame(scopeCreator, createdScope.getScopeCreator());
  }

  @Test
  public void testScanRootFunction() {
    Node functionNode = new Node(Token.FUNCTION, null);
    when(functionNode.getFirstChild()).thenReturn(new Node(Token.NAME, "foo"));
    Node argsNode = new Node(Token.LP, null);
    Node bodyNode = new Node(Token.BLOCK, null);

    scopeCreator.scanRoot(functionNode, parentScope);

    Var fooVar = parentScope.getVar("foo");
    assertNotNull(fooVar);
  }

  @Test
  public void testScanRootFunctionArgs() {
    Node functionNode = new Node(Token.FUNCTION, null);
    Node fnNameNode = new Node(Token.NAME, "foo");
    Node argsNode = new Node(Token.LP, null);
    Node argANode = new Node(Token.NAME, "a");
    Node argBNode = new Node(Token.NAME, "b");
    Node bodyNode = new Node(Token.BLOCK, null);

    when(argsNode.getFirstChild()).thenReturn(argANode);
    when(argsNode.getNext().getFirstChild()).thenReturn(argBNode);

    scopeCreator.scanRoot(functionNode, parentScope);

    Var fooVar = parentScope.getVar("foo");
    assertNotNull(fooVar);

    Var aVar = parentScope.getVar("a");
    assertNotNull(aVar);

    Var bVar = parentScope.getVar("b");
    assertNotNull(bVar);
  }

  @Test
  public void testScanRootFunctionBody() {
    Node functionNode = new Node(Token.FUNCTION, null);
    Node fnNameNode = new Node(Token.NAME, "foo");
    Node argsNode = new Node(Token.LP, null);
    Node bodyNode = new Node(Token.BLOCK, null);

    when(functionNode.getFirstChild()).thenReturn(fnNameNode);
    when(functionNode.getNext()).thenReturn(argsNode);

    scopeCreator.scanRoot(functionNode, parentScope);

    Var fooVar = parentScope.getVar("foo");
    assertNotNull(fooVar);

    Node variableA = new Node(Token.VAR, null);
    Node variableANameNode = new Node(Token.NAME, "a");
    Node variableB = new Node(Token.VAR, null);
    Node variableBNameNode = new Node(Token.NAME, "b");
    bodyNode.setFirstChild(variableA);
    bodyNode.setNext(variableB);

    when(variableANameNode.getString()).thenReturn("a");
    when(variableBNameNode.getString()).thenReturn("b");

    scopeCreator.scanVars(bodyNode, functionNode);

    Var aVar = parentScope.getVar("a");
    assertNotNull(aVar);

    Var bVar = parentScope.getVar("b");
    assertNotNull(bVar);
  }

  @Test
  public void testScanRootScript() {
    Node scriptNode = new Node(Token.SCRIPT, null);

    scopeCreator.scanRoot(scriptNode, null);
  }

  @Test(expected = AssertionError.class)
  public void testCreateScopeWithError() {
    Node rootNode = new Node(Token.FUNCTION, null);
    when(rootNode.getFirstChild()).thenReturn(new Node(Token.NAME, "foo"));
    Node argsNode = new Node(Token.LP, null);
    Node argANode = new Node(Token.NAME, "a");
    Node argBNode = new Node(Token.NAME, "b");
    Node bodyNode = new Node(Token.BLOCK, null);

    when(argsNode.getFirstChild()).thenReturn(argANode);
    when(argsNode.getNext().getFirstChild()).thenReturn(argBNode);
    when(bodyNode.getFirstChild()).thenReturn(new Node(Token.VAR, null));

    Scope createdScope = scopeCreator.createScope(rootNode, parentScope);
  }

  @Test
  public void testOnRedeclarationDefaultHandler() {
    DiagnosticType errorType = VAR_MULTIPLY_DECLARED_ERROR;

    String name = "foo";
    Node n = new Node(Token.NAME, name);
    Node parent = new Node(Token.NAME, "parent");
    Node gramps = new Node(Token.NAME, "grandparent");
    Node nodeWithLineNumber = new Node(Token.NUMBER_LITERAL, 1);

    scopeCreator.getDefaultRedeclarationHandler().onRedeclaration(
        parentScope, name, n, parent, gramps, nodeWithLineNumber);

    assertNotNull(parentScope.getDiagnostic(errorType));
  }

  @Test
  public void testOnRedeclarationCustomHandler() {
    DiagnosticType errorType = VAR_MULTIPLY_DECLARED_ERROR;

    String name = "foo";
    Node n = new Node(Token.NAME, name);
    Node parent = new Node(Token.NAME, "parent");
    Node gramps = new Node(Token.NAME, "grandparent");
    Node nodeWithLineNumber = new Node(Token.NUMBER_LITERAL, 1);

    when(compiler.report(anyString(), any(Node.class), any(DiagnosticType.class),
        anyString(), any(Object.class))).thenReturn(new Node(Token.SKIP, null));

    SyntacticScopeCreator.RedeclarationHandler customHandler =
        new SyntacticScopeCreator.RedeclarationHandler() {
          @Override
          public void onRedeclaration(
              Scope s, String name, Node n, Node parent, Node gramps,
              Node nodeWithLineNumber) {
            s.report(errorType, n, name, name, null);
          }
        };

    scopeCreator = new SyntacticScopeCreator(compiler, customHandler);
    scopeCreator.getDefaultRedeclarationHandler().onRedeclaration(
        parentScope, name, n, parent, gramps, nodeWithLineNumber);

    assertNotNull(parentScope.getDiagnostic(errorType));
  }

  @Test
  public void testDeclareVar() {
    String name = "foo";
    Node n = new Node(Token.NAME, name);
    Node parent = new Node(Token.NAME, "parent");
    Node gramps = new Node(Token.NAME, "grandparent");
    Node nodeWithLineNumber = new Node(Token.NUMBER_LITERAL, 1);

    JSType declaredType = JSType.BOOLEAN;

    scopeCreator.declareVar(name, n, parent, gramps, declaredType, nodeWithLineNumber);

    Var fooVar = parentScope.getVar(name);
    assertNotNull(fooVar);
    assertSame(declaredType, fooVar.getType());
  }
}