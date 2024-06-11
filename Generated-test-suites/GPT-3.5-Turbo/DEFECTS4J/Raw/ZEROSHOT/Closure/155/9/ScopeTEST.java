import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.JSDocInfo;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.StaticScope;
import com.google.javascript.rhino.jstype.StaticSlot;

public class ScopeTest {

  private Scope parent;
  private Node rootNode;
  private ObjectType thisType;
  private boolean isBottom;
  private int depth;

  @Before
  public void setUp() {
    rootNode = new Node(Token.NAME);
    thisType = new ObjectType();
    isBottom = false;
    depth = 1;
    parent = new Scope(null, rootNode);
  }

  @Test
  public void testDeclare() {
    Node nameNode = new Node(Token.NAME);
    JSType type = new JSType();
    Var var = parent.declare("name", nameNode, type, null);
    
    assertEquals("name", var.getName());
    assertEquals(nameNode, var.getNameNode());
    assertEquals(type, var.getType());
    
    assertFalse(var.isConst());
    assertFalse(var.isDefine());
    assertNull(var.getInitialValue());
  }

  @Test
  public void testUndeclare() {
    Node nameNode = new Node(Token.NAME);
    JSType type = new JSType();
    Var var = parent.declare("name", nameNode, type, null);

    parent.undeclare(var);

    assertNull(parent.getVar("name"));
  }

  @Test
  public void testGetVar() {
    Node nameNode = new Node(Token.NAME);
    JSType type = new JSType();
    Var var1 = parent.declare("name1", nameNode, type, null);
    Var var2 = parent.declare("name2", nameNode, type, null);

    assertEquals(var1, parent.getVar("name1"));
    assertEquals(var2, parent.getVar("name2"));
    assertNull(parent.getVar("name3"));
  }

  @Test
  public void testIsDeclared() {
    Node nameNode = new Node(Token.NAME);
    JSType type = new JSType();
    Var var1 = parent.declare("name1", nameNode, type, null);

    assertTrue(parent.isDeclared("name1", true));  
    assertTrue(parent.isDeclared("name1", false));  
    assertFalse(parent.isDeclared("name2", true));  
    assertFalse(parent.isDeclared("name2", false));  
  }

  @Test
  public void testIsGlobal() {
    assertTrue(parent.isGlobal());
  }

  @Test
  public void testIsLocal() {
    assertFalse(parent.isLocal());
  }
}