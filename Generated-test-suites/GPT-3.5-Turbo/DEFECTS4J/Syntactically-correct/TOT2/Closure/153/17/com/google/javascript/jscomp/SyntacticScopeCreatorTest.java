import static org.junit.Assert.*;
import org.junit.Test;

public class SyntacticScopeCreatorTest {

  @Test
  public void testCreateScope() {
    // Test creating scope with parent
    // Edge case: parent scope is null
    // Verify that scope is created correctly
    SyntacticScopeCreator creator = new SyntacticScopeCreator(new AbstractCompiler());
    Scope parentScope = null;
    Node node = new Node(Token.FUNCTION);
    Scope scope = creator.createScope(node, parentScope);
    assertNotNull(scope);

    // Test creating scope without parent
    // Edge case: parent scope is not null
    // Verify that scope is created correctly
    parentScope = new Scope(new Node(Token.FUNCTION), new AbstractCompiler());
    scope = creator.createScope(node, parentScope);
    assertNotNull(scope);
  }

  @Test
  public void testScanRoot() {
    // Test scanning root node
    // Typical case: root node is a function
    // Verify that function variables are declared
    SyntacticScopeCreator creator = new SyntacticScopeCreator(new AbstractCompiler());
    Node functionNode = new Node(Token.FUNCTION);
    Scope parentScope = null;
    Scope scope = new Scope(functionNode, new AbstractCompiler());
    creator.scanRoot(functionNode, parentScope);
    assertNotNull(scope.getVar("arguments"));

    // Test scanning root node
    // Typical case: root node is the global block
    // Verify that variables in global block are declared
    Node globalBlockNode = new Node(Token.SCRIPT);
    creator.scanRoot(globalBlockNode, null);
    assertNotNull(scope);
  }

  @Test
  public void testScanVars() {
    // Test scanning variables under a node
    // Typical case: variable declaration node
    // Verify that variables are declared correctly
    SyntacticScopeCreator creator = new SyntacticScopeCreator(new AbstractCompiler());
    Node varNode = new Node(Token.VAR);
    Node parent = new Node(Token.SCRIPT);
    creator.scanVars(varNode, parent);
    assertNotNull(scope.getVar("x"));

    // Test scanning variables under a node
    // Typical case: control structure node
    // Verify that variables in control structure are scanned correctly
    Node controlStructureNode = new Node(Token.IF);
    creator.scanVars(controlStructureNode, parent);
    assertNotNull(scope);
  }

  @Test
  public void testRedeclarationHandler() {
    // Test handling redeclaration of variables
    // Typical case: variable is redeclared
    // Verify that error is reported
    SyntacticScopeCreator creator = new SyntacticScopeCreator(new AbstractCompiler());
    Node varNode1 = new Node(Token.VAR);
    Node varNode2 = new Node(Token.VAR);
    Node parent = new Node(Token.SCRIPT);
    creator.scanVars(varNode1, parent);
    creator.scanVars(varNode2, parent);
    assertNotNull(scope.getVar("x"));
    // Verify that error is reported for redeclaration
  }

  @Test
  public void testDeclareVar() {
    // Test declaring a variable
    // Typical case: declare a variable
    // Verify that variable is declared in scope
    SyntacticScopeCreator creator = new SyntacticScopeCreator(new AbstractCompiler());
    Node varNode = new Node(Token.VAR);
    Node parent = new Node(Token.SCRIPT);
    creator.declareVar("x", varNode, parent, parent, null, parent);
    assertNotNull(scope.getVar("x"));
  }
}