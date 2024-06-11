import org.junit.Before;
import org.junit.Test;

public class SyntacticScopeCreatorTest {
  
  private SyntacticScopeCreator scopeCreator;
  private AbstractCompiler compiler;
  
  @Before
  public void setup() {
    compiler = new AbstractCompiler();
    scopeCreator = new SyntacticScopeCreator(compiler);
  }
  
  @Test
  public void testCreateScope_withNullParent_shouldCreateNewScope() {
    // Arrange
    Node n = new Node(TokenType.FUNCTION);
    Scope parent = null;
    
    // Act
    Scope scope = scopeCreator.createScope(n, parent);
    
    // Assert
    assertNotNull(scope);
    assertNull(scope.getParent());
    assertEquals(n, scope.getRootNode());
  }
  
  @Test
  public void testCreateScope_withNonNullParent_shouldCreateNewScope() {
    // Arrange
    Node n = new Node(TokenType.VAR);
    Scope parent = new Scope(n, compiler);
    
    // Act
    Scope scope = scopeCreator.createScope(n, parent);
    
    // Assert
    assertNotNull(scope);
    assertEquals(parent, scope.getParent());
    assertEquals(n, scope.getRootNode());
  }
  
  @Test
  public void testCreateScope_withFunctionNode_shouldCreateScopeWithFunctionVariables() {
    // Arrange
    Node n = new Node(TokenType.FUNCTION);
    Scope parent = null;
    n.putProp(Node.SOURCENAME_PROP, "test.js");
    Node functionNameNode = new Node(TokenType.NAME, "testFunction");
    Node argsNode = new Node(TokenType.LP);
    Node bodyNode = new Node(TokenType.BLOCK);
    n.addChildToBack(functionNameNode);
    n.addChildToBack(argsNode);
    n.addChildToBack(bodyNode);
    
    // Act
    Scope scope = scopeCreator.createScope(n, parent);
    
    // Assert
    assertNotNull(scope);
    assertTrue(scope.isDeclared("testFunction", false));
    assertEquals(n, scope.getRootNode());
  }
  
  @Test
  public void testCreateScope_withVarNode_shouldDeclareVariables() {
    // Arrange
    Node n = new Node(TokenType.VAR);
    Scope parent = new Scope(n, compiler);
    Node child1 = new Node(TokenType.NAME, "x");
    Node child2 = new Node(TokenType.NAME, "y");
    n.addChildToBack(child1);
    n.addChildToBack(child2);
    
    // Act
    Scope scope = scopeCreator.createScope(n, parent);
    
    // Assert
    assertNotNull(scope);
    assertTrue(scope.isDeclared("x", false));
    assertTrue(scope.isDeclared("y", false));
    assertEquals(n, scope.getRootNode());
  }
  
  @Test
  public void testCreateScope_withCatchNode_shouldDeclareCatchVariable() {
    // Arrange
    Node n = new Node(TokenType.CATCH);
    Scope parent = new Scope(n, compiler);
    Node catchVar = new Node(TokenType.NAME, "e");
    Node block = new Node(TokenType.BLOCK);
    n.addChildToBack(catchVar);
    n.addChildToBack(block);
    
    // Act
    Scope scope = scopeCreator.createScope(n, parent);
    
    // Assert
    assertNotNull(scope);
    assertTrue(scope.isDeclared("e", false));
    assertEquals(n, scope.getRootNode());
  }
  
  @Test
  public void testCreateScope_withScriptNode_shouldSetSourceName() {
    // Arrange
    Node n = new Node(TokenType.SCRIPT);
    n.putProp(Node.SOURCENAME_PROP, "test.js");
    Scope parent = null;
    
    // Act
    Scope scope = scopeCreator.createScope(n, parent);
    
    // Assert
    assertNotNull(scope);
    assertEquals("test.js", scopeCreator.sourceName);
  }
  
  @Test
  public void testScanVars_withVarsNode_shouldDeclareVariables() {
    // Arrange
    Node n = new Node(TokenType.VAR);
    Scope parent = new Scope(n, compiler);
    Node child1 = new Node(TokenType.NAME, "x");
    Node child2 = new Node(TokenType.NAME, "y");
    n.addChildToBack(child1);
    n.addChildToBack(child2);
    
    // Act
    scopeCreator.scanVars(n, null);
    
    // Assert
    assertTrue(parent.isDeclared("x", false));
    assertTrue(parent.isDeclared("y", false));
  }
  
  @Test
  public void testScanVars_withFunctionNode_shouldDeclareFunctionVariable() {
    // Arrange
    Node n = new Node(TokenType.FUNCTION);
    Scope parent = new Scope(n, compiler);
    Node functionNameNode = new Node(TokenType.NAME, "testFunction");
    Node argsNode = new Node(TokenType.LP);
    Node bodyNode = new Node(TokenType.BLOCK);
    n.addChildToBack(functionNameNode);
    n.addChildToBack(argsNode);
    n.addChildToBack(bodyNode);
    
    // Act
    scopeCreator.scanVars(n, parent);
    
    // Assert
    assertTrue(parent.isDeclared("testFunction", false));
  }
  
  @Test
  public void testScanVars_withCatchNode_shouldDeclareCatchVariable() {
    // Arrange
    Node n = new Node(TokenType.CATCH);
    Scope parent = new Scope(n, compiler);
    Node catchVar = new Node(TokenType.NAME, "e");
    Node block = new Node(TokenType.BLOCK);
    n.addChildToBack(catchVar);
    n.addChildToBack(block);
    
    // Act
    scopeCreator.scanVars(n, parent);
    
    // Assert
    assertTrue(parent.isDeclared("e", false));
  }
}