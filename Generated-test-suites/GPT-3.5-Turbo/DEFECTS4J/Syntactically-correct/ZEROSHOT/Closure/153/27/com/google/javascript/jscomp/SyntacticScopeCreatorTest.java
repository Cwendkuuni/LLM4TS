import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SyntacticScopeCreatorTest {

  private SyntacticScopeCreator scopeCreator;

  @Before
  public void setUp() {
    scopeCreator = new SyntacticScopeCreator(new AbstractCompiler());
  }

  @Test
  public void testCreateScope_withFunctionNode_createsScopeWithFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Scope parent = new Scope(n, new AbstractCompiler());
    
    Scope scope = scopeCreator.createScope(n, parent);
    
    assertNotNull(scope);
    assertEquals(n, scope.getRootNode());
  }

  @Test
  public void testCreateScope_withGlobalBlock_createsScopeWithGlobalBlock() {
    Node n = new Node(Token.SCRIPT);
    Scope parent = null;
    
    Scope scope = scopeCreator.createScope(n, parent);
    
    assertNotNull(scope);
    assertEquals(n, scope.getRootNode());
  }

  @Test
  public void testScanVars_withVarNode_addsVariablesToScope() {
    Node n = new Node(Token.VAR);
    Node child1 = new Node(Token.NAME, "x");
    Node child2 = new Node(Token.NAME, "y");
    Node child3 = new Node(Token.NAME, "z");
    n.addChildToBack(child1);
    n.addChildToBack(child2);
    n.addChildToBack(child3);
    Node parent = new Node(Token.SCRIPT);
    
    scopeCreator.scanVars(n, parent);
    
    assertNotNull(scopeCreator.scope);
    assertTrue(scopeCreator.scope.isDeclared("x", false));
    assertTrue(scopeCreator.scope.isDeclared("y", false));
    assertTrue(scopeCreator.scope.isDeclared("z", false));
  }

  @Test
  public void testScanVars_withFunctionNodeAndFunctionExpression_doesNotAddFunctionToScope() {
    Node n = new Node(Token.FUNCTION);
    Node fnNameNode = new Node(Token.NAME, "foo");
    Node args = new Node(Token.LP);
    Node body = new Node(Token.BLOCK);
    n.addChildToFront(fnNameNode);
    n.addChildToBack(args);
    n.addChildToBack(body);
    Node parent = new Node(Token.SCRIPT);
    
    scopeCreator.scanVars(n, parent);
    
    assertNotNull(scopeCreator.scope);
    assertFalse(scopeCreator.scope.isDeclared("foo", false));
  }

  @Test
  public void testScanVars_withFunctionNodeAndNamedParentFunction_addsFunctionNameToScope() {
    Node n = new Node(Token.FUNCTION);
    Node fnNameNode = new Node(Token.NAME, "foo");
    Node args = new Node(Token.LP);
    Node body = new Node(Token.BLOCK);
    n.addChildToFront(fnNameNode);
    n.addChildToBack(args);
    n.addChildToBack(body);
    Node parent = new Node(Token.FUNCTION);
    parent.addChildToFront(new Node(Token.NAME, "parentFunctionName"));
    
    scopeCreator.scanVars(n, parent);
    
    assertNotNull(scopeCreator.scope);
    assertTrue(scopeCreator.scope.isDeclared("foo", false));
  }

  @Test
  public void testScanVars_withFunctionNodeAndUnnamedParentFunction_doesNotAddFunctionNameToScope() {
    Node n = new Node(Token.FUNCTION);
    Node fnNameNode = new Node(Token.NAME, "");
    Node args = new Node(Token.LP);
    Node body = new Node(Token.BLOCK);
    n.addChildToFront(fnNameNode);
    n.addChildToBack(args);
    n.addChildToBack(body);
    Node parent = new Node(Token.FUNCTION);
    parent.addChildToFront(new Node(Token.NAME, "parentFunctionName"));
    
    scopeCreator.scanVars(n, parent);
    
    assertNotNull(scopeCreator.scope);
    assertFalse(scopeCreator.scope.isDeclared("parentFunctionName", false));
  }

  @Test
  public void testScanVars_withCatchNode_addsCatchVarToScope() {
    Node n = new Node(Token.CATCH);
    Node var = new Node(Token.NAME, "e");
    Node block = new Node(Token.BLOCK);
    n.addChildToFront(var);
    n.addChildToBack(block);
    Node parent = new Node(Token.SCRIPT);
    
    scopeCreator.scanVars(n, parent);
    
    assertNotNull(scopeCreator.scope);
    assertTrue(scopeCreator.scope.isDeclared("e", false));
  }

  @Test
  public void testScanVars_withScriptNode_setsSourceName() {
    Node n = new Node(Token.SCRIPT);
    n.putProp(Node.SOURCENAME_PROP, "test.js");
    
    scopeCreator.scanVars(n, null);
    
    assertEquals("test.js", scopeCreator.sourceName);
  }
  
  // Create more test cases for the remaining methods in the SyntacticScopeCreator class...

}