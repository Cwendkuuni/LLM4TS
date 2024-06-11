import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SyntacticScopeCreatorTest {

  private AbstractCompiler compiler;
  private SyntacticScopeCreator scopeCreator;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    scopeCreator = new SyntacticScopeCreator(compiler);
  }

  @Test
  public void testCreateScope_FunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    n.putProp(Node.SOURCENAME_PROP, "test.js");

    Node fnNameNode = new Node(Token.NAME, "testFunc");
    n.addChildToFront(fnNameNode);

    Node args = new Node(Token.LP);
    n.addChildToBack(args);

    Node arg1 = new Node(Token.NAME, "arg1");
    args.addChildToBack(arg1);

    Node body = new Node(Token.BLOCK);
    n.addChildToBack(body);

    Scope scope = scopeCreator.createScope(n, null);

    assertEquals("test.js", scope.getScopeRoot().getProp(Node.SOURCENAME_PROP));
    assertEquals("testFunc", scope.getVar("testFunc").getName());
    assertNotNull(scope.getVar("arg1"));
  }

  @Test
  public void testCreateScope_FunctionExpression() {
    Node n = new Node(Token.FUNCTION);
    n.putProp(Node.SOURCENAME_PROP, "test.js");

    Node fnNameNode = new Node(Token.NAME, "");
    n.addChildToFront(fnNameNode);

    Node args = new Node(Token.LP);
    n.addChildToBack(args);
    
    Node arg1 = new Node(Token.NAME, "arg1");
    args.addChildToBack(arg1);

    Node body = new Node(Token.BLOCK);
    n.addChildToBack(body);

    Scope scope = scopeCreator.createScope(n, null);

    assertEquals("test.js", scope.getScopeRoot().getProp(Node.SOURCENAME_PROP));
    assertNull(scope.getVar("")); // function expression shouldn't have a name in scope
    assertNotNull(scope.getVar("arg1"));
  }

  @Test
  public void testCreateScope_VariableDeclaration() {
    Node n = new Node(Token.VAR);
    n.putProp(Node.SOURCENAME_PROP, "test.js");

    Node var1 = new Node(Token.NAME, "x");
    n.addChildToBack(var1);

    Node var2 = new Node(Token.NAME, "y");
    n.addChildToBack(var2);

    Node var3 = new Node(Token.NAME, "z");
    n.addChildToBack(var3);

    Scope scope = scopeCreator.createScope(n, null);

    assertEquals("test.js", scope.getScopeRoot().getProp(Node.SOURCENAME_PROP));
    assertNotNull(scope.getVar("x"));
    assertNotNull(scope.getVar("y"));
    assertNotNull(scope.getVar("z"));
  }

  @Test
  public void testCreateScope_GlobalBlock() {
    Node n = new Node(Token.SCRIPT);
    n.putProp(Node.SOURCENAME_PROP, "test.js");

    Scope scope = scopeCreator.createScope(n, null);

    assertEquals("test.js", scope.getScopeRoot().getProp(Node.SOURCENAME_PROP));
    assertTrue(scope.isGlobal());
  }

  @Test
  public void testCreateScope_CatchBlock() {
    Node n = new Node(Token.CATCH);
    n.putProp(Node.SOURCENAME_PROP, "test.js");

    Node catchVar = new Node(Token.NAME, "error");
    n.addChildToFront(catchVar);

    Node catchBlock = new Node(Token.BLOCK);
    n.addChildToBack(catchBlock);

    Scope parentScope = scopeCreator.createScope(new Node(Token.SCRIPT), null);
    Scope scope = scopeCreator.createScope(n, parentScope);

    assertEquals("test.js", scope.getScopeRoot().getProp(Node.SOURCENAME_PROP));
    assertNotNull(scope.getVar("error"));
  }

  @Test
  public void testCreateScope_FunctionDeclarationDuplicateDeclaration() {
    Node n = new Node(Token.FUNCTION);
    n.putProp(Node.SOURCENAME_PROP, "test.js");

    Node fnNameNode = new Node(Token.NAME, "testFunc");
    n.addChildToFront(fnNameNode);

    Node args = new Node(Token.LP);
    n.addChildToBack(args);

    Node arg1 = new Node(Token.NAME, "arg1");
    args.addChildToBack(arg1);

    Node body = new Node(Token.BLOCK);
    n.addChildToBack(body);

    Scope parentScope = scopeCreator.createScope(new Node(Token.SCRIPT), null);
    parentScope.declare("testFunc", fnNameNode, null, compiler.getInput("test.js"));

    Scope scope = scopeCreator.createScope(n, parentScope);

    assertEquals("test.js", scope.getScopeRoot().getProp(Node.SOURCENAME_PROP));
    assertNull(scope.getVar("testFunc"));
    assertNotNull(scope.getVar("arg1"));
  }

  @Test
  public void testCreateScope_FunctionDeclarationShadowArguments() {
    Node n = new Node(Token.FUNCTION);
    n.putProp(Node.SOURCENAME_PROP, "test.js");

    Node fnNameNode = new Node(Token.NAME, "testFunc");
    n.addChildToFront(fnNameNode);

    Node args = new Node(Token.LP);
    n.addChildToBack(args);

    Node arg1 = new Node(Token.NAME, "arguments");
    args.addChildToBack(arg1);

    Node body = new Node(Token.BLOCK);
    n.addChildToBack(body);

    Scope parentScope = scopeCreator.createScope(new Node(Token.SCRIPT), null);

    Scope scope = scopeCreator.createScope(n, parentScope);

    assertEquals("test.js", scope.getScopeRoot().getProp(Node.SOURCENAME_PROP));
    assertNull(scope.getVar("testFunc"));
    assertNull(scope.getVar("arguments"));
  }

}