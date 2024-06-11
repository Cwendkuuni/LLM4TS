import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.*;

public class SyntacticScopeCreatorTest {

  private AbstractCompiler compiler;
  private SyntacticScopeCreator scopeCreator;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    scopeCreator = new SyntacticScopeCreator(compiler);
  }

  @Test
  public void testCreateScope_FunctionNode() {
    Node n = new Node(Token.FUNCTION);
    Scope parent = new Scope(new Node(Token.SCRIPT), compiler);

    Scope scope = scopeCreator.createScope(n, parent);

    assertNotNull(scope);
    assertEquals(n, scope.getRootNode());
    assertNull(scope.getParent());
  }

  @Test
  public void testCreateScope_GlobalBlockNode() {
    Node n = new Node(Token.SCRIPT);
    Scope parent = null;

    Scope scope = scopeCreator.createScope(n, parent);

    assertNotNull(scope);
    assertEquals(n, scope.getRootNode());
    assertNull(scope.getParent());
  }

  @Test
  public void testScanRoot_FunctionNode() {
    Node n = new Node(Token.FUNCTION);
    Scope parent = new Scope(new Node(Token.SCRIPT), compiler);

    scopeCreator.scanRoot(n, parent);

    assertNotNull(scopeCreator.getScope());
    assertEquals(n, scopeCreator.getScope().getRootNode());
    assertNull(scopeCreator.getScope().getParent());
  }

  @Test
  public void testScanRoot_GlobalBlockNode() {
    Node n = new Node(Token.SCRIPT);
    Scope parent = null;

    scopeCreator.scanRoot(n, parent);

    assertNotNull(scopeCreator.getScope());
    assertEquals(n, scopeCreator.getScope().getRootNode());
    assertNull(scopeCreator.getScope().getParent());
  }

  @Test
  public void testScanVars_VarNode() {
    Node n = new Node(Token.VAR);
    Node parent = new Node(Token.SCRIPT);

    scopeCreator.scanVars(n, parent);

    // TODO: Add assertions for variable declarations
  }

  @Test
  public void testScanVars_FunctionNode_FunctionExpression() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);

    scopeCreator.scanVars(n, parent);

    // TODO: Add assertions for variable declarations
  }

  @Test
  public void testScanVars_FunctionNode_FunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    n.putProp(Node.SOURCENAME_PROP, "test.js");

    scopeCreator.scanVars(n, parent);

    // TODO: Add assertions for variable declarations
  }

  @Test
  public void testScanVars_CatchNode() {
    Node n = new Node(Token.CATCH);
    Node parent = new Node(Token.SCRIPT);

    scopeCreator.scanVars(n, parent);

    // TODO: Add assertions for variable declarations
  }

  @Test
  public void testScanVars_ScriptNode() {
    Node n = new Node(Token.SCRIPT);
    Node parent = null;

    scopeCreator.scanVars(n, parent);

    // TODO: Add assertions for variable declarations
  }
}