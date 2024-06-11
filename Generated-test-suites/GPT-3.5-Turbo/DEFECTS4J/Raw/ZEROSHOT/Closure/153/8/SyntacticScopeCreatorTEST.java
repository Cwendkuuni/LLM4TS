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
  public void testCreateScope_withFunctionNode_shouldCreateScope() {
    Node n = new Node(Token.FUNCTION);
    Scope parent = new Scope(n, new AbstractCompiler());

    Scope scope = scopeCreator.createScope(n, parent);

    assertNotNull(scope);
    assertNull(scope.getParent());
    assertEquals(n, scope.getRootNode());
  }

  @Test
  public void testCreateScope_withoutParent_shouldCreateGlobalScope() {
    Node n = new Node(Token.SCRIPT);

    Scope scope = scopeCreator.createScope(n, null);

    assertNotNull(scope);
    assertNull(scope.getParent());
    assertEquals(n, scope.getRootNode());
  }

  @Test
  public void testCreateScope_withVarNode_shouldIgnore() {
    Node n = new Node(Token.VAR);

    Scope parent = new Scope(n, new AbstractCompiler());
    Scope scope = scopeCreator.createScope(n, parent);

    assertNull(scope);
  }

  @Test
  public void testCreateScope_withFunctionExpression_shouldDeclareVar() {
    Node n = new Node(Token.FUNCTION);
    Node nameNode = new Node(Token.NAME, "foo");
    n.addChildToFront(nameNode);

    Scope parent = new Scope(n, new AbstractCompiler());
    Scope scope = scopeCreator.createScope(n, parent);

    assertNotNull(scope);
    Scope.Var var = scope.getVar("foo");
    assertNotNull(var);
    assertEquals(nameNode, var.getNameNode());
  }

  @Test
  public void testCreateScope_withFunctionNode_shouldDeclareVar() {
    Node n = new Node(Token.FUNCTION);
    Node nameNode = new Node(Token.NAME, "foo");
    n.addChildToFront(nameNode);

    Scope parent = new Scope(n, new AbstractCompiler());
    Scope scope = scopeCreator.createScope(n, parent);

    assertNotNull(scope);
    Scope.Var var = scope.getVar("foo");
    assertNotNull(var);
    assertEquals(nameNode, var.getNameNode());
  }

  @Test
  public void testCreateScope_withCatchNode_shouldDeclareVar() {
    Node n = new Node(Token.CATCH);
    Node nameNode = new Node(Token.NAME, "error");
    n.addChildToFront(nameNode);

    Scope parent = new Scope(n, new AbstractCompiler());
    Scope scope = scopeCreator.createScope(n, parent);

    assertNotNull(scope);
    Scope.Var var = scope.getVar("error");
    assertNotNull(var);
    assertEquals(nameNode, var.getNameNode());
  }

  @Test
  public void testCreateScope_withScriptNode_shouldSetSourceName() {
    Node n = new Node(Token.SCRIPT);
    n.putProp(Node.SOURCENAME_PROP, "test.js");

    Scope scope = scopeCreator.createScope(n, null);

    assertNotNull(scope);
    assertEquals("test.js", scope.getSourceName());
  }
}