import org.junit.Before;
import org.junit.Test;

public class SyntacticScopeCreatorTest {

  private SyntacticScopeCreator scopeCreator;

  // Initialize the scope creator before each test
  @Before
  public void setUp() {
    scopeCreator = new SyntacticScopeCreator(new AbstractCompiler());
  }

  // Test createScope method with a non-null parent scope
  @Test
  public void testCreateScopeWithNonNullParent() {
    Node n = new Node(Token.VAR);
    Scope parent = new Scope(new Node(Token.ROOT), new AbstractCompiler());
    Scope scope = scopeCreator.createScope(n, parent);

    // Assert that the created scope has the correct parent and node
    assertEquals(parent, scope.getParent());
    assertEquals(n, scope.getRootNode());
  }

  // Test createScope method with a null parent scope
  @Test
  public void testCreateScopeWithNullParent() {
    Node n = new Node(Token.SCRIPT);
    Scope scope = scopeCreator.createScope(n, null);

    // Assert that the created scope has no parent and the correct node
    assertNull(scope.getParent());
    assertEquals(n, scope.getRootNode());
  }

  // Test declareVar method with a duplicate variable declaration
  @Test(expected = JSError.class)
  public void testDeclareVarDuplicate() {
    String name = "x";
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.VAR);
    Node gramps = new Node(Token.SCRIPT);
    Node nodeWithLineNumber = new Node(Token.VAR);
    scopeCreator.declareVar(name, n, parent, gramps, null, nodeWithLineNumber);
  }

  // Test declareVar method with a valid variable declaration
  @Test
  public void testDeclareVarValid() {
    String name = "x";
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.VAR);
    Node gramps = new Node(Token.SCRIPT);
    Node nodeWithLineNumber = new Node(Token.VAR);
    scopeCreator.declareVar(name, n, parent, gramps, null, nodeWithLineNumber);

    // Assert that the variable was declared in the scope
    assertNotNull(scopeCreator.getScope().getVar(name));
  }
}