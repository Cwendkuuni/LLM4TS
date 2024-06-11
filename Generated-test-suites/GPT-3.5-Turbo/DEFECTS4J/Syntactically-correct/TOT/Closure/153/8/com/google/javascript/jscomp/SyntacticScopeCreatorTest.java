import org.junit.Test;

public class SyntacticScopeCreatorTest {

  @Test
  public void testCreateScope_FunctionDeclaration() {
    // Testing function declaration case
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
    Node node = new Node(Token.FUNCTION);
    Scope parentScope = null;
    Scope scope = creator.createScope(node, parentScope);

    // Assertions
    assertNotNull(scope);
    assertNull(scope.getParent());
    assertEquals(node, scope.getRootNode());
    assertFalse(scope.isGlobal());
  }

  @Test
  public void testCreateScope_GlobalBlock() {
    // Testing global block case
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
    Node node = new Node(Token.SCRIPT);
    Scope parentScope = null;
    Scope scope = creator.createScope(node, parentScope);

    // Assertions
    assertNotNull(scope);
    assertNull(scope.getParent());
    assertEquals(node, scope.getRootNode());
    assertTrue(scope.isGlobal());
  }

  @Test
  public void testCreateScope_VarDeclaration() {
    // Testing var declaration case
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
    Node node = new Node(Token.VAR);
    Scope parentScope = new Scope(null, null);
    Scope scope = creator.createScope(node, parentScope);

    // Assertions
    assertNotNull(scope);
    assertEquals(parentScope, scope.getParent());
    assertEquals(node, scope.getRootNode());
    assertFalse(scope.isGlobal());
  }

  @Test
  public void testScanVars_VarDeclaration() {
    // Testing scanVars with var declaration case
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
    Node node = new Node(Token.VAR);
    Node child1 = new Node(Token.NAME, new Node(Token.STRING, "x"));
    Node child2 = new Node(Token.NAME, new Node(Token.STRING, "y"));
    Node child3 = new Node(Token.NAME, new Node(Token.STRING, "z"));
    node.addChildToBack(child1);
    node.addChildToBack(child2);
    node.addChildToBack(child3);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(node);
    Scope scope = new Scope(parent, node);

    creator.scanVars(node, parent);

    // Assertions
    assertNotNull(scope.getVar("x"));
    assertNotNull(scope.getVar("y"));
    assertNotNull(scope.getVar("z"));
  }

  @Test
  public void testScanVars_FunctionDeclaration() {
    // Testing scanVars with function declaration case
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
    Node node = new Node(Token.FUNCTION);
    Node child1 = new Node(Token.NAME, new Node(Token.STRING, "fnName"));
    Node child2 = new Node(Token.LP);
    Node child3 = new Node(Token.STRING, "x");
    Node child4 = new Node(Token.BLOCK);
    node.addChildToBack(child1);
    node.addChildToBack(child2);
    node.addChildToBack(child3);
    node.addChildToBack(child4);
    node.putProp(Node.SOURCENAME_PROP, "sourceName");
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(node);
    Scope scope = new Scope(parent, node);

    creator.scanVars(node, parent);

    // Assertions
    assertNotNull(scope.getVar("fnName"));
    assertNotNull(scope.getVar("x"));
  }

  @Test
  public void testScanVars_CatchBlock() {
    // Testing scanVars with catch block case
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
    Node node = new Node(Token.CATCH);
    Node child1 = new Node(Token.NAME, new Node(Token.STRING, "exception"));
    Node child2 = new Node(Token.BLOCK);
    node.addChildToBack(child1);
    node.addChildToBack(child2);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(node);
    Scope scope = new Scope(parent, node);

    creator.scanVars(node, parent);

    // Assertions
    assertNotNull(scope.getVar("exception"));
  }

  @Test
  public void testDeclareVar() {
    // Testing declareVar case
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
    Node node = new Node(Token.VAR);
    Node child1 = new Node(Token.NAME, new Node(Token.STRING, "x"));
    node.addChildToBack(child1);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(node);
    Scope scope = new Scope(parent, node);

    creator.declareVar("x", child1, node, parent, null, node);

    // Assertions
    assertNotNull(scope.getVar("x"));
  }

  @Test
  public void testDefaultRedeclarationHandler_OnRedeclaration() {
    // Testing DefaultRedeclarationHandler onRedeclaration case
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
    SyntacticScopeCreator.DefaultRedeclarationHandler handler =
        creator.new DefaultRedeclarationHandler();
    Node node = new Node(Token.VAR);
    Node child1 = new Node(Token.NAME, new Node(Token.STRING, "x"));
    node.addChildToBack(child1);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(node);
    Scope scope = new Scope(parent, node);
    String name = "x";

    handler.onRedeclaration(scope, name, child1, node, parent, node);

    // Assertions
    assertNotNull(scope.getVar("x"));
  }

}