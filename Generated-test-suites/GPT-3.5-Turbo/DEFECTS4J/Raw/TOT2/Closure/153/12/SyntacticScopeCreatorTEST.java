import static org.junit.Assert.*;
import org.junit.Test;

public class SyntacticScopeCreatorTest {

  @Test
  public void testCreateScope() {
    // Test creating a scope with a parent
    SyntacticScopeCreator creator = new SyntacticScopeCreator(new AbstractCompiler());
    Node node = new Node(Token.FUNCTION);
    Scope parentScope = new Scope(node, new AbstractCompiler());
    Scope result = creator.createScope(node, parentScope);
    
    assertNotNull(result);
    assertEquals(parentScope, result.getParent());
  }

  @Test
  public void testScanRoot() {
    // Test scanning root with a function node
    SyntacticScopeCreator creator = new SyntacticScopeCreator(new AbstractCompiler());
    Node node = new Node(Token.FUNCTION);
    Scope parentScope = new Scope(node, new AbstractCompiler());
    creator.createScope(node, parentScope);

    // Verify that root is scanned correctly
    assertEquals("SourceName", creator.getSourceName());
  }

  @Test
  public void testScanVars() {
    // Test scanning variables under a node
    SyntacticScopeCreator creator = new SyntacticScopeCreator(new AbstractCompiler());
    Node node = new Node(Token.VAR);
    Node child1 = new Node(Token.NAME, new Node(Token.EMPTY));
    child1.setString("x");
    Node child2 = new Node(Token.NAME, new Node(Token.EMPTY));
    child2.setString("y");
    node.addChildToBack(child1);
    node.addChildToBack(child2);

    creator.scanVars(node, null);

    // Verify that variables are scanned correctly
    Scope scope = creator.getScope();
    assertNotNull(scope.getVar("x"));
    assertNotNull(scope.getVar("y"));
  }

  @Test
  public void testDeclareVar() {
    // Test declaring a variable
    SyntacticScopeCreator creator = new SyntacticScopeCreator(new AbstractCompiler());
    Node n = new Node(Token.NAME);
    n.setString("x");
    Node parent = new Node(Token.VAR, n);
    Node nodeWithLineNumber = new Node(Token.EMPTY);
    creator.declareVar("x", n, parent, null, null, nodeWithLineNumber);

    // Verify that variable is declared correctly
    Scope scope = creator.getScope();
    assertNotNull(scope.getVar("x"));
  }
}