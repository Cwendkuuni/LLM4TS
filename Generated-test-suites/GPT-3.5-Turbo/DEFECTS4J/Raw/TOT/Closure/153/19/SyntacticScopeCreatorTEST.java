import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.Node;

public class SyntacticScopeCreatorTest {

  private SyntacticScopeCreator scopeCreator;
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    scopeCreator = new SyntacticScopeCreator(compiler);
  }

  @Test
  public void testCreateScope_GlobalBlock_ShouldReturnGlobalScope() {
    Node n = new Node(Token.SCRIPT);
    Scope parent = null;

    Scope scope = scopeCreator.createScope(n, parent);
    Scope expectedScope = new Scope(n, compiler);

    assertEquals(expectedScope, scope);
  }

  @Test
  public void testCreateScope_FunctionBlockWithArgsAndBody_ShouldReturnFunctionScope() {
    Node n = new Node(Token.FUNCTION);
    n.putProp(Node.SOURCENAME_PROP, "test.js");
    Node fnNameNode = new Node(Token.NAME, "testFn");
    Node args = new Node(Token.LP);
    Node body = new Node(Token.BLOCK);
    n.addChildToFront(fnNameNode);
    n.addChildToBack(args);
    n.addChildToBack(body);
    Scope parent = new Scope(null, n);

    Scope scope = scopeCreator.createScope(n, parent);
    Scope expectedScope = new Scope(parent, n);

    // Declare function name and arguments variables
    expectedScope.declare("testFn", fnNameNode, null, compiler.getInput("test.js"));
    expectedScope.declare("arguments", args, null, compiler.getInput("test.js"));

    assertEquals(expectedScope, scope);
  }

  @Test
  public void testCreateScope_FunctionBlockWithoutArgsAndBody_ShouldReturnFunctionScope() {
    Node n = new Node(Token.FUNCTION);
    n.putProp(Node.SOURCENAME_PROP, "test.js");
    Node fnNameNode = new Node(Token.NAME, "testFn");
    Node body = new Node(Token.BLOCK);
    n.addChildToFront(fnNameNode);
    n.addChildToBack(body);
    Scope parent = new Scope(null, n);

    Scope scope = scopeCreator.createScope(n, parent);
    Scope expectedScope = new Scope(parent, n);

    // Declare function name
    expectedScope.declare("testFn", fnNameNode, null, compiler.getInput("test.js"));

    assertEquals(expectedScope, scope);
  }

  @Test
  public void testCreateScope_FunctionBlockWithEmptyFnNameAndArgs_ShouldReturnFunctionScope() {
    Node n = new Node(Token.FUNCTION);
    n.putProp(Node.SOURCENAME_PROP, "test.js");
    Node fnNameNode = new Node(Token.NAME, "");
    Node args = new Node(Token.LP);
    Node body = new Node(Token.BLOCK);
    n.addChildToFront(fnNameNode);
    n.addChildToBack(args);
    n.addChildToBack(body);
    Scope parent = new Scope(null, n);

    Scope scope = scopeCreator.createScope(n, parent);
    Scope expectedScope = new Scope(parent, n);

    assertEquals(expectedScope, scope);
  }

  @Test
  public void testCreateScope_CatchBlock_ShouldReturnCatchScope() {
    Node n = new Node(Token.CATCH);
    n.putProp(Node.SOURCENAME_PROP, "test.js");
    Node var = new Node(Token.NAME, "error");
    Node block = new Node(Token.BLOCK);
    n.addChildToFront(var);
    n.addChildToBack(block);
    Scope parent = new Scope(null, n);

    Scope scope = scopeCreator.createScope(n, parent);
    Scope expectedScope = new Scope(parent, n);

    // Declare catch variable
    expectedScope.declare("error", var, null, compiler.getInput("test.js"));

    assertEquals(expectedScope, scope);
  }

  @Test
  public void testCreateScope_ScriptBlock_ShouldReturnScriptScope() {
    Node n = new Node(Token.SCRIPT);
    n.putProp(Node.SOURCENAME_PROP, "test.js");
    Scope parent = null;

    Scope scope = scopeCreator.createScope(n, parent);
    Scope expectedScope = new Scope(n, compiler);

    assertEquals(expectedScope, scope);
  }

  @Test
  public void testCreateScope_FunctionBlockWithDuplicateVarDeclarationInGlobalScope_ShouldReportError() {
    Node n = new Node(Token.FUNCTION);
    n.putProp(Node.SOURCENAME_PROP, "test.js");
    Node var1 = new Node(Token.NAME, "x");
    Node var2 = new Node(Token.NAME, "x");
    Node body = new Node(Token.BLOCK);
    n.addChildToFront(var1);
    n.addChildToBack(body);
    body.addChildToFront(var2);
    Scope parent = new Scope(null, n);

    Scope scope = scopeCreator.createScope(n, parent);

    // Error should be reported for duplicate var declaration
    assertTrue(compiler.getErrorManager().getErrors().hasNext());
  }

  @Test
  public void testCreateScope_FunctionBlockWithDuplicateVarDeclarationInNonGlobalScope_ShouldNotReportError() {
    Node n = new Node(Token.FUNCTION);
    n.putProp(Node.SOURCENAME_PROP, "test.js");
    Node var1 = new Node(Token.NAME, "x");
    Node var2 = new Node(Token.NAME, "x");
    Node body = new Node(Token.BLOCK);
    n.addChildToFront(var1);
    n.addChildToBack(body);
    body.addChildToFront(var2);
    Scope parent = new Scope(null, n);
    parent.declare("x", var1, null, compiler.getInput("test.js"));

    Scope scope = scopeCreator.createScope(n, parent);

    // No error should be reported for duplicate var declaration
    assertFalse(compiler.getErrorManager().getErrors().hasNext());
  }

  @Test
  public void testCreateScope_FunctionBlockWithArgumentsVar_ShouldReportError() {
    Node n = new Node(Token.FUNCTION);
    n.putProp(Node.SOURCENAME_PROP, "test.js");
    Node args = new Node(Token.NAME, "arguments");
    Node body = new Node(Token.BLOCK);
    n.addChildToFront(args);
    n.addChildToBack(body);
    Scope parent = new Scope(null, n);

    Scope scope = scopeCreator.createScope(n, parent);

    // Error should be reported for shadowing "arguments" variable
    assertTrue(compiler.getErrorManager().getErrors().hasNext());
  }

  @Test
  public void testCreateScope_FunctionBlockWithArgumentsVarInsideVarDeclaration_ShouldReportError() {
    Node n = new Node(Token.FUNCTION);
    n.putProp(Node.SOURCENAME_PROP, "test.js");
    Node var = new Node(Token.VAR);
    Node args = new Node(Token.NAME, "arguments");
    Node body = new Node(Token.BLOCK);
    n.addChildToFront(var);
    var.addChildToFront(args);
    n.addChildToBack(body);
    Scope parent = new Scope(null, n);

    Scope scope = scopeCreator.createScope(n, parent);

    // Error should be reported for shadowing "arguments" variable
    assertTrue(compiler.getErrorManager().getErrors().hasNext());
  }

}