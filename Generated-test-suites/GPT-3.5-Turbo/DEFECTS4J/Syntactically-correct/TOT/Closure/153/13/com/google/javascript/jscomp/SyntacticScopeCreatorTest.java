import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.Node;

public class SyntacticScopeCreatorTest {
  
  @Test
  public void testCreateScope_FunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    n.putProp(Node.SOURCENAME_PROP, "test.js");
    Node fnNameNode = new Node(Token.NAME, "testFunc");
    n.addChildToFront(fnNameNode);
    Node args = new Node(Token.LP);
    Node arg1 = new Node(Token.NAME, "arg1");
    args.addChildToFront(arg1);
    n.addChildToBack(args);
    Node body = new Node(Token.BLOCK);
    Node statement = new Node(Token.VAR);
    Node varName = new Node(Token.NAME, "var1");
    Node varValue = new Node(Token.NUMBER, "5");
    statement.addChildToBack(varName);
    statement.addChildToBack(varValue);
    body.addChildToFront(statement);
    n.addChildToBack(body);
    
    SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(null);
    Scope scope = scopeCreator.createScope(n, null);
    
    assertNotNull(scope);
    assertEquals("test.js", scope.getSourceName());
    
    Scope.Var var = scope.getVar("testFunc");
    assertNotNull(var);
    assertNull(var.getParentNode());
    assertEquals(n, var.getDeclaringNode());
    assertNull(var.getType());
    assertNull(var.getInput());
    
    var = scope.getVar("arg1");
    assertNotNull(var);
    assertEquals(args, var.getParentNode());
    assertEquals(n, var.getDeclaringNode());
    assertNull(var.getType());
    assertNull(var.getInput());
    
    var = scope.getVar("var1");
    assertNotNull(var);
    assertEquals(statement, var.getParentNode());
    assertEquals(n, var.getDeclaringNode());
    assertNull(var.getType());
    assertNull(var.getInput());
  }
  
  @Test
  public void testCreateScope_GlobalBlock() {
    Node n = new Node(Token.SCRIPT);
    n.putProp(Node.SOURCENAME_PROP, "test.js");
    Node statement = new Node(Token.VAR);
    Node varName = new Node(Token.NAME, "var1");
    Node varValue = new Node(Token.NUMBER, "5");
    statement.addChildToBack(varName);
    statement.addChildToBack(varValue);
    n.addChildToFront(statement);
    
    SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(null);
    Scope scope = scopeCreator.createScope(n, null);
    
    assertNotNull(scope);
    assertEquals("test.js", scope.getSourceName());
    
    Scope.Var var = scope.getVar("var1");
    assertNotNull(var);
    assertEquals(statement, var.getParentNode());
    assertNull(var.getDeclaringNode());
    assertNull(var.getType());
    assertNull(var.getInput());
  }
  
  @Test
  public void testDeclareVar_ValidDeclaration() {
    Node n = new Node(Token.SCRIPT);
    n.putProp(Node.SOURCENAME_PROP, "test.js");
    
    SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(null);
    Scope scope = new Scope(n, null);
    scopeCreator.declareVar("var1", n, null, null, null, n);
    
    Scope.Var var = scope.getVar("var1");
    assertNotNull(var);
    assertNull(var.getParentNode());
    assertNull(var.getDeclaringNode());
    assertNull(var.getType());
    assertNull(var.getInput());
  }
  
  @Test
  public void testDeclareVar_DuplicateDeclaration_GlobalScope() {
    Node n = new Node(Token.SCRIPT);
    n.putProp(Node.SOURCENAME_PROP, "test.js");
    
    SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(null);
    Scope scope = new Scope(n, null);
    scope.declare("var1", n, null, null);
    scopeCreator.declareVar("var1", n, null, null, null, n);
    
    Scope.Var var = scope.getVar("var1");
    assertNotNull(var);
    assertNull(var.getParentNode());
    assertNull(var.getDeclaringNode());
    assertNull(var.getType());
    assertNull(var.getInput());
  }
  
  @Test
  public void testDeclareVar_DuplicateDeclaration_CatchScope() {
    Node n = new Node(Token.CATCH);
    Node var = new Node(Token.NAME, "exception");
    n.addChildToFront(var);
    Node block = new Node(Token.BLOCK);
    n.addChildToBack(block);
    n.putProp(Node.SOURCENAME_PROP, "test.js");
    
    SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(null);
    Scope parentScope = new Scope(new Node(Token.SCRIPT), null);
    Scope scope = new Scope(parentScope, n);
    parentScope.declare("exception", var, null, null);
    scopeCreator.declareVar("exception", var, n, null, null, n);
    
    Scope.Var scopeVar = scope.getVar("exception");
    assertNotNull(scopeVar);
    assertEquals(var, scopeVar.getParentNode());
    assertEquals(n, scopeVar.getDeclaringNode());
    assertNull(scopeVar.getType());
    assertNull(scopeVar.getInput());
    
    Scope.Var parentScopeVar = parentScope.getVar("exception");
    assertNotNull(parentScopeVar);
    assertNull(parentScopeVar.getParentNode());
    assertNull(parentScopeVar.getDeclaringNode());
    assertNull(parentScopeVar.getType());
    assertNull(parentScopeVar.getInput());
  }
  
  @Test
  public void testDeclareVar_DuplicateDeclaration_CatchScope_Error() {
    Node n = new Node(Token.CATCH);
    Node var = new Node(Token.NAME, "exception");
    n.addChildToFront(var);
    Node block = new Node(Token.BLOCK);
    n.addChildToBack(block);
    n.putProp(Node.SOURCENAME_PROP, "test.js");
    
    Compiler compiler = new Compiler();
    SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
    Scope parentScope = new Scope(new Node(Token.SCRIPT), null);
    Scope scope = new Scope(parentScope, n);
    parentScope.declare("exception", var, null, null);
    scopeCreator.declareVar("exception", var, n, null, null, n);
    
    Scope.Var scopeVar = scope.getVar("exception");
    assertNotNull(scopeVar);
    assertEquals(var, scopeVar.getParentNode());
    assertEquals(n, scopeVar.getDeclaringNode());
    assertNull(scopeVar.getType());
    assertNull(scopeVar.getInput());
    
    Scope.Var parentScopeVar = parentScope.getVar("exception");
    assertNotNull(parentScopeVar);
    assertNull(parentScopeVar.getParentNode());
    assertNull(parentScopeVar.getDeclaringNode());
    assertNull(parentScopeVar.getType());
    assertNull(parentScopeVar.getInput());
    
    // Check if the correct error is reported
    JSError[] reportedErrors = compiler.getErrors();
    assertEquals(1, reportedErrors.length);
    assertEquals("test.js", reportedErrors[0].sourceName);
    assertEquals("JSC_VAR_MULTIPLY_DECLARED_ERROR", reportedErrors[0].getType().key);
    assertEquals("exception", reportedErrors[0].description);
    assertEquals("??", reportedErrors[0].args[1]);
  }
  
  @Test
  public void testDeclareVar_ShadowingArguments_Error() {
    Node n = new Node(Token.SCRIPT);
    n.putProp(Node.SOURCENAME_PROP, "test.js");
    Node args = new Node(Token.NAME, "arguments");
    args.putBooleanProp(Node.IS_DECLARED_VAR, true);
    Node statement = new Node(Token.VAR, args);
    n.addChildToFront(statement);
    
    Compiler compiler = new Compiler();
    SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
    Scope scope = new Scope(n, null);
    scopeCreator.declareVar("arguments", args, null, null, null, n);
    
    Scope.Var var = scope.getVar("arguments");
    assertNotNull(var);
    assertEquals(statement, var.getParentNode());
    assertNull(var.getDeclaringNode());
    assertNull(var.getType());
    assertNull(var.getInput());
    
    // Check if the correct error is reported
    JSError[] reportedErrors = compiler.getErrors();
    assertEquals(1, reportedErrors.length);
    assertEquals("test.js", reportedErrors[0].sourceName);
    assertEquals("JSC_VAR_ARGUMENTS_SHADOWED_ERROR", reportedErrors[0].getType().key);
  }
  
}