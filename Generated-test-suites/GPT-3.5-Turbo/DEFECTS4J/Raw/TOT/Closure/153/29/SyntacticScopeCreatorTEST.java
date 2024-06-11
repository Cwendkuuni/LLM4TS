import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SyntacticScopeCreatorTest {

  @Test
  public void createScope_functionDeclaration_ValidInput_ShouldCreateScope() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
    Node n = new Node(Token.FUNCTION);
    Scope parent = new Scope(null, null);
    
    // Act
    Scope scope = scopeCreator.createScope(n, parent);
    
    // Assert
    assertNotNull(scope);
    assertEquals(compiler, scopeCreator.compiler);
    assertNull(scopeCreator.sourceName);
    assertEquals(parent, scope.getParent());
    assertEquals(n, scope.getRootNode());
    assertNull(scope.getParentVar());
    assertNull(scope.getRootVar());
  }
  
  @Test
  public void createScope_globalBlock_ValidInput_ShouldCreateScope() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
    Node n = new Node(Token.SCRIPT);
    Scope parent = null;
    
    // Act
    Scope scope = scopeCreator.createScope(n, parent);
    
    // Assert
    assertNotNull(scope);
    assertEquals(compiler, scopeCreator.compiler);
    assertNull(scopeCreator.sourceName);
    assertNull(scope.getParent());
    assertEquals(n, scope.getRootNode());
    assertNull(scope.getParentVar());
    assertNull(scope.getRootVar());
  }
  
  @Test
  public void declareVar_VariableNotDeclared_ShouldDeclareVariable() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.SCRIPT);
    Node gramps = null;
    JSType declaredType = null;
    Node nodeWithLineNumber = new Node(Token.NAME, "test", 1);
    String name = "test";
    
    // Act
    scopeCreator.declareVar(name, n, parent, gramps, declaredType, nodeWithLineNumber);
    Scope scope = scopeCreator.scope;
    
    // Assert
    assertNotNull(scope);
    assertTrue(scope.isDeclared(name, false));
    assertFalse(scope.isLocal());
  }
  
  @Test
  public void declareVar_VariableAlreadyDeclared_ShouldCallRedeclarationHandler() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    MockRedeclarationHandler redeclarationHandler = new MockRedeclarationHandler();
    SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler, redeclarationHandler);
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.SCRIPT);
    Node gramps = null;
    JSType declaredType = null;
    Node nodeWithLineNumber = new Node(Token.NAME, "test", 1);
    String name = "test";
    scopeCreator.scope = new Scope(parent, null);
    
    // Act
    scopeCreator.declareVar(name, n, parent, gramps, declaredType, nodeWithLineNumber);
    
    // Assert
    assertTrue(redeclarationHandler.onRedeclarationCalled);
  }
  
  @Test
  public void declareVar_VariableArgumentsAlreadyDeclaredInLocalScope_ShouldCallRedeclarationHandler() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    MockRedeclarationHandler redeclarationHandler = new MockRedeclarationHandler();
    SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler, redeclarationHandler);
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.CATCH);
    Node gramps = null;
    JSType declaredType = null;
    Node nodeWithLineNumber = new Node(Token.NAME, "test", 1);
    String name = "arguments";
    scopeCreator.scope = new Scope(parent, null);
    
    // Act
    scopeCreator.declareVar(name, n, parent, gramps, declaredType, nodeWithLineNumber);
    
    // Assert
    assertTrue(redeclarationHandler.onRedeclarationCalled);
  }
  
  @Test
  public void declareVar_VariableArgumentsAlreadyDeclaredInGlobalScope_ShouldCallRedeclarationHandler() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    MockRedeclarationHandler redeclarationHandler = new MockRedeclarationHandler();
    SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler, redeclarationHandler);
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.SCRIPT);
    Node gramps = null;
    JSType declaredType = null;
    Node nodeWithLineNumber = new Node(Token.NAME, "test", 1);
    String name = "arguments";
    scopeCreator.scope = new Scope(null, compiler.getInput("test"));
    
    // Act
    scopeCreator.declareVar(name, n, parent, gramps, declaredType, nodeWithLineNumber);
    
    // Assert
    assertTrue(redeclarationHandler.onRedeclarationCalled);
  }
  
  @Test
  public void scanVars_VarDeclaration_ValidInput_ShouldDeclareVariables() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
    Node n = new Node(Token.VAR);
    Node parent = new Node(Token.SCRIPT);
    
    // Act
    scopeCreator.scanVars(n, parent);
    Scope scope = scopeCreator.scope;
    
    // Assert
    assertNotNull(scope);
    assertTrue(scope.isDeclared("x", false));
    assertTrue(scope.isDeclared("y", false));
    assertTrue(scope.isDeclared("z", false));
    assertFalse(scope.isLocal());
  }
  
  @Test
  public void scanVars_FunctionDeclaration_ValidInput_ShouldDeclareVariable() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    
    // Act
    scopeCreator.scanVars(n, parent);
    Scope scope = scopeCreator.scope;
    
    // Assert
    assertNotNull(scope);
    assertTrue(scope.isDeclared("fnName", false));
    assertFalse(scope.isLocal());
  }
  
  @Test
  public void scanVars_CatchBlock_ValidInput_ShouldDeclareVariable() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
    Node n = new Node(Token.CATCH);
    Node parent = new Node(Token.SCRIPT);
    
    // Act
    scopeCreator.scanVars(n, parent);
    Scope scope = scopeCreator.scope;
    
    // Assert
    assertNotNull(scope);
    assertTrue(scope.isDeclared("var", false));
    assertFalse(scope.isLocal());
  }
  
  @Test
  public void scanVars_Script_ValidInput_ShouldSetSourceName() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
    Node n = new Node(Token.SCRIPT);
    Node parent = new Node(Token.SCRIPT);
    
    // Act
    scopeCreator.scanVars(n, parent);
    
    // Assert
    assertNotNull(scopeCreator.sourceName);
  }

  private class MockRedeclarationHandler implements SyntacticScopeCreator.RedeclarationHandler {
    public boolean onRedeclarationCalled = false;
    
    public void onRedeclaration(
        Scope s, String name,
        Node n, Node parent, Node gramps, Node nodeWithLineNumber) {
      onRedeclarationCalled = true;
    }
  }
}