import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.ErrorReporter;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.SyntacticScopeCreator;
import com.google.javascript.jscomp.TestSourceFile;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class SyntacticScopeCreatorTest {

  private AbstractCompiler compiler;
  private ErrorReporter errorReporter;
  private SyntacticScopeCreator scopeCreator;
  private TestSourceFile input;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    errorReporter = mock(ErrorReporter.class);
    scopeCreator = new SyntacticScopeCreator(compiler);
    input = new TestSourceFile("test.js");
  }

  @Test
  public void testCreateScope_empty() {
    Scope parent = null;
    Scope actual = scopeCreator.createScope(createNode(Token.SCRIPT), parent);
    assertSame(Scope.GLOBAL, actual);
  }

  @Test
  public void testCreateScope_function() {
    Scope parent = null;
    Scope actual = scopeCreator.createScope(createNode(Token.FUNCTION, createNode("test"), createNode("{}")), parent);
    assertSame(Scope.FUNCTION, actual);
  }

  @Test
  public void testCreateScope_function_arguments() {
    Scope parent = null;
    Scope actual = scopeCreator.createScope(createNode(Token.FUNCTION, createNode("test"), createNode("{}"), null, null, createNode("arguments")), parent);
    assertSame(Scope.FUNCTION, actual);
    assertSame(actual.getVar("arguments"), actual.getVar("arguments"));
  }

  @Test
  public void testCreateScope_function_arguments_redeclaration() {
    Scope parent = null;
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler, new DuplicateHandler());
    Scope actual = creator.createScope(createNode(Token.FUNCTION, createNode("test"), createNode("{}"), null, null, createNode("arguments")), parent);
    assertSame(Scope.FUNCTION, actual);
    Scope.Var argumentsVar = actual.getVar("arguments");
    assertSame(argumentsVar, actual.getVar("arguments"));
    creator.redeclarationHandler.onRedeclaration(
        actual, "arguments",
        createNode("arguments"), null, null, null);
    assertNull(actual.getVar("arguments"));
  }

  @Test
  public void testCreateScope_function_local_variables() {
    Scope parent = new Scope(null, input);
    Scope actual = scopeCreator.createScope(createNode(Token.FUNCTION, createNode("test"), createNode("{}")), parent);

    Node varNode1 = createNode(Token.VAR, createNode("x"), null);
    Node varNode2 = createNode(Token.VAR, createNode("y"), null);
    Node varNode3 = createNode(Token.VAR, createNode("z"), null);

    Node statements = createNode(Token.BLOCK, varNode1, varNode2, varNode3);
    Node body = createNode(Token.BLOCK, statements);

    scopeCreator.scanVars(body, createNode(Token.FUNCTION, createNode("test"), null));

    Node x = actual.getVar("x");
    Node y = actual.getVar("y");
    Node z = actual.getVar("z");

    assertSame(varNode1, x.getParentNode());
    assertSame(x, y.getPrevVar());
    assertSame(y, z.getPrevVar());
  }

  @Test
  public void testCreateScope_function_redeclared_variable() {
    Scope parent = new Scope(null, input);
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler, new DuplicateHandler());
    Scope actual = creator.createScope(createNode(Token.FUNCTION, createNode("test"), null), parent);

    Node varNode1 = createNode(Token.VAR, createNode("x"), null);
    Node varNode2 = createNode(Token.VAR, createNode("x"), null);

    Node statements = createNode(Token.BLOCK, varNode1, varNode2);
    Node body = createNode(Token.BLOCK, statements);

    creator.scanVars(body, createNode(Token.FUNCTION, createNode("test"), null));

    assertNull(actual.getVar("x"));
  }

  @Test
  public void testCreateScope_function_global_variables() {
    Scope parent = null;
    Scope actual = scopeCreator.createScope(createNode(Token.FUNCTION, createNode("test"), null), parent);

    Node varNode1 = createNode(Token.VAR, createNode("x"), null);
    Node varNode2 = createNode(Token.VAR, createNode("y"), null);

    Node statements = createNode(Token.BLOCK, varNode1, varNode2);
    Node body = createNode(Token.BLOCK, statements);

    scopeCreator.scanVars(body, null);

    Node x = actual.getVar("x");
    Node y = actual.getVar("y");

    assertSame(Scope.GLOBAL, x.getParentScope());
    assertSame(Scope.GLOBAL, y.getParentScope());
  }

  @Test
  public void testCreateScope_function_global_redeclared_variable() {
    Scope parent = null;
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler, new DuplicateHandler());
    Scope actual = creator.createScope(createNode(Token.FUNCTION, createNode("test"), null), parent);

    Node varNode1 = createNode(Token.VAR, createNode("x"), null);
    Node varNode2 = createNode(Token.VAR, createNode("x"), null);

    Node statements = createNode(Token.BLOCK, varNode1, varNode2);
    Node body = createNode(Token.BLOCK, statements);

    creator.scanVars(body, null);

    assertNull(actual.getVar("x"));
  }

  @Test
  public void testCreateScope_function_function_variables() {
    Scope parent = new Scope(null, input);
    Scope actual = scopeCreator.createScope(createNode(Token.FUNCTION, createNode("test"), null), parent);

    Node functionNode = createNode(Token.FUNCTION, createNode("test2"), null);
    Node functionBody = createNode(Token.BLOCK);

    scopeCreator.scanRoot(functionNode, null);

    Node x = actual.getVar("x");
    Node y = actual.getVar("y");
    Node test2 = actual.getVar("test2");

    assertSame(Scope.FUNCTION, x.getParentScope());
    assertSame(Scope.FUNCTION, y.getParentScope());
    assertSame(functionNode, test2.getParentNode());
  }

  @Test
  public void testCreateScope_function_function_variables_redeclaration() {
    Scope parent = new Scope(null, input);
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler, new DuplicateHandler());
    Scope actual = creator.createScope(createNode(Token.FUNCTION, createNode("test"), null), parent);

    Node functionNode = createNode(Token.FUNCTION, createNode("test2"), null);
    Node functionBody = createNode(Token.BLOCK);

    creator.scanRoot(functionNode, null);

    Node x = actual.getVar("x");
    Node y = actual.getVar("y");
    Node test2 = actual.getVar("test2");

    assertSame(Scope.FUNCTION, x.getParentScope());
    assertSame(Scope.FUNCTION, y.getParentScope());
    assertNull(test2);
  }

  @Test
  public void testCreateScope_function_function_variable_with_jsdoc() {
    Scope parent = new Scope(null, input);
    Scope actual = scopeCreator.createScope(createNode(Token.FUNCTION, createNode("test"), null), parent);

    Node functionNode = createNode(Token.FUNCTION, createNode("test2"), null);
    Node functionBody = createNode(Token.BLOCK, createNode(Token.VAR, createNode("x"), null), createNode("{}"));
    JSDocInfo jsdocInfo = createNode(Token.COMMENT, createNode("/** * @type {number} */"), null);
    Node xNode = createNode(Token.VAR, createNode("x"), null);

    functionNode.setFirstChild(jsdocInfo);
    functionBody.setFirstChild(xNode);

    creator.scanRoot(functionNode, null);

    Node x = actual.getVar("x");
    Node test2 = actual.getVar("test2");

    assertSame(JSType.NUMBER, x.getType());
    assertSame(Scope.FUNCTION, x.getParentScope());
    assertSame(Scope.GLOBAL, test2.getParentScope());
  }

  @Test
  public void testCreateScope_catch() {
    Scope parent = null;
    Scope actual = scopeCreator.createScope(createNode(Token.TRY, null), parent);
    Node catchNode = createNode(Token.CATCH, createNode("test"), createNode("{}"));

    scopeCreator.scanVars(catchNode, null);

    Node test = actual.getVar("test");
    assertSame(catchNode, test.getParentNode());
  }

  @Test
  public void testCreateScope_script() {
    Scope parent = null;
    Scope actual = scopeCreator.createScope(createNode(Token.SCRIPT), parent);
    assertSame(Scope.GLOBAL, actual);
  }

  private Node createNode(int... tokenTypes) {
    Node node = new Node();
    for (int tokenType : tokenTypes) {
      node.addChild(new Node(tokenType));
    }
    return node;
  }

  private static class DuplicateHandler implements SyntacticScopeCreator.RedeclarationHandler {
    @Override
    public void onRedeclaration(Scope s, String name, Node n, Node parent, Node gramps, Node nodeWithLineNumber) {
      throw new RuntimeException("Should not be called here");
    }
  }
}