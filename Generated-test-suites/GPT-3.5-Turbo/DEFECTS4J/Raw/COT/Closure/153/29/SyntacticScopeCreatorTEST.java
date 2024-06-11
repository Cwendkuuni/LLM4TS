
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;

public class SyntacticScopeCreatorTest {

  private SyntacticScopeCreator scopeCreator;
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    scopeCreator = new SyntacticScopeCreator(compiler);
  }

  @Test
  public void testCreateScope_GlobalBlock() {
    Node n = new Node(Token.SCRIPT);
    n.putProp(Node.SOURCENAME_PROP, "test.js");

    Scope scope = scopeCreator.createScope(n, null);

    assertTrue(scope.isGlobal());
    assertNull(scope.getParent());
    assertEquals("test.js", scope.getRootNode().getProp(Node.SOURCENAME_PROP));
  }

  @Test
  public void testCreateScope_FunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    n.putProp(Node.SOURCENAME_PROP, "test.js");

    Scope parent = new Scope(null, null);

    Scope scope = scopeCreator.createScope(n, parent);

    assertFalse(scope.isGlobal());
    assertEquals(parent, scope.getParent());
    assertEquals(n, scope.getRootNode());
    assertEquals("test.js", scope.getRootNode().getProp(Node.SOURCENAME_PROP));
  }

  @Test
  public void testCreateScope_FunctionExpression() {
    Node n = new Node(Token.FUNCTION);
    n.putProp(Node.SOURCENAME_PROP, "test.js");

    Scope parent = new Scope(null, null);

    Scope scope = scopeCreator.createScope(n, parent);

    assertFalse(scope.isGlobal());
    assertEquals(parent, scope.getParent());
    assertEquals(n, scope.getRootNode());
    assertEquals("test.js", scope.getRootNode().getProp(Node.SOURCENAME_PROP));
  }

  @Test
  public void testCreateScope_CatchBlock() {
    Node n = new Node(Token.CATCH);
    n.putProp(Node.SOURCENAME_PROP, "test.js");

    Scope parent = new Scope(null, null);

    Scope scope = scopeCreator.createScope(n, parent);

    assertFalse(scope.isGlobal());
    assertEquals(parent, scope.getParent());
    assertEquals(n, scope.getRootNode());
    assertEquals("test.js", scope.getRootNode().getProp(Node.SOURCENAME_PROP));
  }

  @Test
  public void testDeclareVar() {
    Node n = new Node(Token.NAME);
    n.setString("x");

    Node parent = new Node(Token.SCRIPT);

    Node gramps = new Node(Token.ASSIGN);
    parent.addChildToFront(gramps);

    JSDocInfo info = new JSDocInfo();
    info.suppressionTypes("duplicate");

    Node nodeWithLineNumber = new Node(Token.NAME);
    nodeWithLineNumber.setLineno(1);

    scopeCreator.declareVar("x", n, parent, gramps, null, nodeWithLineNumber);

    Scope scope = new Scope(null, null);
    scope.declare("x", n, null, null);

    assertTrue(scope.isDeclared("x", false));
  }

  @Test(expected = VarMultiplyDeclaredError.class)
  public void testDeclareVar_MultipleDeclaration_Error() {
    Node n = new Node(Token.NAME);
    n.setString("x");

    Node parent = new Node(Token.SCRIPT);

    Node gramps = new Node(Token.ASSIGN);
    parent.addChildToFront(gramps);

    Node nodeWithLineNumber = new Node(Token.NAME);
    nodeWithLineNumber.setLineno(1);

    scopeCreator.declareVar("x", n, parent, gramps, null, nodeWithLineNumber);
    scopeCreator.declareVar("x", n, parent, gramps, null, nodeWithLineNumber);
  }

  @Test(expected = VarArgumentsShadowedError.class)
  public void testDeclareVar_ArgumentsShadowed_Error() {
    Node n = new Node(Token.NAME);
    n.setString("arguments");

    Node parent = new Node(Token.SCRIPT);

    Node gramps = new Node(Token.ASSIGN);
    parent.addChildToFront(gramps);

    Node nodeWithLineNumber = new Node(Token.NAME);
    nodeWithLineNumber.setLineno(1);

    scopeCreator.declareVar("arguments", n, parent, gramps, null, nodeWithLineNumber);
  }
}