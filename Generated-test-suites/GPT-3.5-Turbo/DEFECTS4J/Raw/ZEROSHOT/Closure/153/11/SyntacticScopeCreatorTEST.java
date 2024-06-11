import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class SyntacticScopeCreatorTest {

  private SyntacticScopeCreator scopeCreator;

  @Before
  public void setUp() {
    scopeCreator = new SyntacticScopeCreator();
  }
  
  @Test
  public void testCreateScope_nullNode_returnsNull() {
    // Arrange
    Node n = null;
    
    // Act
    Scope scope = scopeCreator.createScope(n, null);
    
    // Assert
    assertNull(scope);
  }
  
  @Test
  public void testCreateScope_globalNode_returnsGlobalScope() {
    // Arrange
    Node n = new Node(Token.SCRIPT);
    
    // Act
    Scope scope = scopeCreator.createScope(n, null);
    
    // Assert
    assertNotNull(scope);
    assertTrue(scope.isGlobal());
  }
  
  @Test
  public void testCreateScope_functionNode_returnsFunctionScope() {
    // Arrange
    Node n = new Node(Token.FUNCTION);
    Scope parent = new Scope(new Node(Token.SCRIPT));
    
    // Act
    Scope scope = scopeCreator.createScope(n, parent);
    
    // Assert
    assertNotNull(scope);
    assertFalse(scope.isGlobal());
    assertEquals(parent, scope.getParent());
  }
  
  @Test
  public void testDeclareVar_newVariable_declaresVariable() {
    // Arrange
    Scope scope = new Scope(new Node(Token.SCRIPT));
    Node n = new Node(Token.NAME);
    String name = "testVar";
    
    // Act
    scopeCreator.declareVar(name, n, null, null, null, null);
    
    // Assert
    assertTrue(scope.isDeclared(name, false));
    assertEquals(n, scope.getVar(name).getNode());
  }
  
  @Test
  public void testDeclareVar_redeclaredVariable_callsRedeclarationHandler() {
    // Arrange
    Scope scope = new Scope(null);
    scope.declare("testVar", new Node(Token.NAME), null, null);
    Node n = new Node(Token.NAME);
    String name = "testVar";
    
    // Act
    boolean handlerCalled = false;
    scopeCreator.setRedeclarationHandler(new SyntacticScopeCreator.RedeclarationHandler() {
      @Override
      public void onRedeclaration(
          Scope s, String name,
          Node n, Node parent, Node gramps, Node nodeWithLineNumber) {
        handlerCalled = true;
      }
    });
    scopeCreator.declareVar(name, n, null, null, null, null);
    
    // Assert
    assertTrue(handlerCalled);
  }
  
  @Test
  public void testDeclareVar_redeclaredVariable_notCallsRedeclarationHandler() {
    // Arrange
    Scope scope = new Scope(null);
    scope.declare("testVar", new Node(Token.NAME), null, null);
    Node n = new Node(Token.NAME);
    String name = "testVar";
    
    // Act
    boolean handlerCalled = false;
    scopeCreator.setRedeclarationHandler(new SyntacticScopeCreator.RedeclarationHandler() {
      @Override
      public void onRedeclaration(
          Scope s, String name,
          Node n, Node parent, Node gramps, Node nodeWithLineNumber) {
        handlerCalled = true;
      }
    });
    scopeCreator.declareVar(name, n, null, null, null, null);
    
    // Assert
    assertFalse(handlerCalled);
  }
}