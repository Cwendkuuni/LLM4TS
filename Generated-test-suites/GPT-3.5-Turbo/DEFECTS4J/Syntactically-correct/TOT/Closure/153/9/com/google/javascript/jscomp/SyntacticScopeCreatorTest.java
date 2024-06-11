import org.junit.Assert;
import org.junit.Test;

public class SyntacticScopeCreatorTest {
  
  @Test
  public void testCreateScope_FunctionDeclaration() {
    AbstractCompiler compiler = new AbstractCompiler();
    ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
    
    Node functionNode = createFunctionNode("myFunction", null, null);
    Scope scope = scopeCreator.createScope(functionNode, null);
    
    Assert.assertNotNull(scope);
    Assert.assertEquals(functionNode, scope.getRootNode());
    Assert.assertEquals(null, scope.getParent());
    Assert.assertEquals("myFunction", scope.getVar("myFunction").getName());
  }
  
  @Test
  public void testCreateScope_FunctionExpression() {
    AbstractCompiler compiler = new AbstractCompiler();
    ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
    
    Node functionNode = createFunctionNode("", null, null);
    Scope scope = scopeCreator.createScope(functionNode, null);
    
    Assert.assertNotNull(scope);
    Assert.assertEquals(functionNode, scope.getRootNode());
    Assert.assertEquals(null, scope.getParent());
    Assert.assertEquals("", scope.getVar("").getName());
  }
  
  @Test
  public void testCreateScope_GlobalBlock() {
    AbstractCompiler compiler = new AbstractCompiler();
    ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
    
    Node globalBlock = createGlobalBlockNode();
    Scope scope = scopeCreator.createScope(globalBlock, null);
    
    Assert.assertNotNull(scope);
    Assert.assertEquals(globalBlock, scope.getRootNode());
    Assert.assertEquals(null, scope.getParent());
  }
  
  @Test
  public void testScanVars_VariableDeclaration() {
    AbstractCompiler compiler = new AbstractCompiler();
    ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
    
    Node varNode = createVarNode("var x = 1, y, z;");
    SyntacticScopeCreator.scanVars(varNode, null);
    
    Scope scope = scopeCreator.getScope();
    Assert.assertNotNull(scope);
    Assert.assertEquals(null, scope.getParent());
    Assert.assertEquals(3, scope.getVarCount());
    Assert.assertEquals("x", scope.getVar("x").getName());
    Assert.assertEquals("y", scope.getVar("y").getName());
    Assert.assertEquals("z", scope.getVar("z").getName());
  }
  
  @Test
  public void testScanVars_FunctionDeclaration() {
    AbstractCompiler compiler = new AbstractCompiler();
    ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
    
    Node functionNode = createFunctionNode("myFunction", null, null);
    SyntacticScopeCreator.scanVars(functionNode, null);
    
    Scope scope = scopeCreator.getScope();
    Assert.assertNotNull(scope);
    Assert.assertEquals(null, scope.getParent());
    Assert.assertEquals(1, scope.getVarCount());
    Assert.assertEquals("myFunction", scope.getVar("myFunction").getName());
  }
  
  @Test
  public void testScanVars_FunctionExpression() {
    AbstractCompiler compiler = new AbstractCompiler();
    ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
    
    Node functionNode = createFunctionNode("", null, null);
    SyntacticScopeCreator.scanVars(functionNode, null);
    
    Scope scope = scopeCreator.getScope();
    Assert.assertNotNull(scope);
    Assert.assertEquals(null, scope.getParent());
    Assert.assertEquals(1, scope.getVarCount());
    Assert.assertEquals("", scope.getVar("").getName());
  }
  
  @Test
  public void testScanVars_CatchBlock() {
    AbstractCompiler compiler = new AbstractCompiler();
    ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
    
    Node catchNode = createCatchNode("catch (e) { console.log(e); }");
    SyntacticScopeCreator.scanVars(catchNode, null);
    
    Scope scope = scopeCreator.getScope();
    Assert.assertNotNull(scope);
    Assert.assertEquals(null, scope.getParent());
    Assert.assertEquals(1, scope.getVarCount());
    Assert.assertEquals("e", scope.getVar("e").getName());
  }
  
  @Test
  public void testScanVars_Script() {
    AbstractCompiler compiler = new AbstractCompiler();
    ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
    
    Node scriptNode = createScriptNode("");
    SyntacticScopeCreator.scanVars(scriptNode, null);
    
    Scope scope = scopeCreator.getScope();
    Assert.assertNotNull(scope);
    Assert.assertEquals(null, scope.getParent());
  }
  
  private Node createFunctionNode(String functionName, Node args, Node body) {
    Node functionNode = new Node(Token.FUNCTION);
    Node nameNode = new Node(Token.NAME, functionName);
    functionNode.addChildToBack(nameNode);
    if (args != null) {
      functionNode.addChildToBack(args);
    }
    if (body != null) {
      functionNode.addChildToBack(body);
    }
    return functionNode;
  }
  
  private Node createVarNode(String varDeclaration) {
    Node varNode = new Node(Token.VAR);
    Node assignmentNode = new Node(Token.ASSIGN);
    Node nameNode = new Node(Token.NAME);
    nameNode.setString(varDeclaration);
    assignmentNode.addChildToBack(nameNode);
    varNode.addChildToBack(assignmentNode);
    return varNode;
  }
  
  private Node createCatchNode(String catchBlock) {
    Node catchNode = new Node(Token.CATCH);
    Node varNode = new Node(Token.NAME, "e");
    catchNode.addChildToBack(varNode);
    Node block = new Node(Token.BLOCK);
    block.addChildToBack(new Node(Token.SCRIPT));
    catchNode.addChildToBack(block);
    return catchNode;
  }
  
  private Node createGlobalBlockNode() {
    return new Node(Token.SCRIPT);
  }
  
  private Node createScriptNode(String code) {
    Node scriptNode = new Node(Token.SCRIPT);
    scriptNode.putProp(Node.SOURCE_NAME_PROP, "test.js");
    Node childNode = new Node(Token.SCRIPT);
    childNode.setString(code);
    scriptNode.addChildToBack(childNode);
    return scriptNode;
  }
}