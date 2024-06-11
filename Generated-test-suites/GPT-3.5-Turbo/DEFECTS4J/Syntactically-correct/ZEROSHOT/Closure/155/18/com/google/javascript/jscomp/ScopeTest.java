import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.StaticScope;
import com.google.javascript.rhino.jstype.StaticSlot;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ScopeTest {

  Scope scope;
  Var var;

  @Before
  public void setUp() {
    scope = new Scope(null, new Node(Token.FUNCTION));
    var = scope.declare("testVar", new Node(Token.NAME), null, null);
  }

  @Test
  public void testDeclare() {
    assertEquals("testVar", var.getName());
    assertEquals(new Node(Token.NAME), var.getNameNode());
  }

  @Test
  public void testIsDeclared() {
    assertTrue(scope.isDeclared("testVar", false));
    assertTrue(scope.isDeclared("testVar", true));
    assertFalse(scope.isDeclared("notDeclared", false));
    assertFalse(scope.isDeclared("notDeclared", true));
  }

  @Test
  public void testGetVar() {
    assertEquals(var, scope.getVar("testVar"));
    assertNull(scope.getVar("notDeclared"));
  }

  @Test
  public void testGetGlobalScope() {
    Scope globalScope = new Scope(new Node(Token.BLOCK), null);
    scope.setParent(globalScope);
    assertEquals(globalScope, scope.getGlobalScope());
  }

  @Test
  public void testIsGlobal() {
    assertTrue(scope.isGlobal());
    assertFalse(scope.isLocal());
  }

  @Test
  public void testGetRootNode() {
    Node rootNode = new Node(Token.BLOCK);
    Scope scope = new Scope(null, rootNode);
    assertEquals(rootNode, scope.getRootNode());
  }
}