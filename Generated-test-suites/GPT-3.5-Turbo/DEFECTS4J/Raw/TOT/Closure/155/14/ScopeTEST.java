import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;

public class ScopeTest {

  private Scope globalScope;
  
  @Before
  public void setUp() {
    globalScope = new Scope(null, null);
  }
  
  @Test
  public void testDeclare() {
    Var var = globalScope.declare("x", null, null, null);
    assertEquals("x", var.getName());
    assertNull(var.getNameNode());
    assertNull(var.getType());
    assertFalse(var.isDefine());
    assertTrue(var.isTypeInferred());
  }
  
  @Test
  public void testUndeclare() {
    Var var = globalScope.declare("x", null, null, null);
    globalScope.undeclare(var);
    assertNull(globalScope.getVar("x"));
  }
  
  @Test
  public void testGetVar_Local() {
    Scope localScope = new Scope(globalScope, null);
    Var globalVar = globalScope.declare("x", null, null, null);
    Var localVar = localScope.declare("x", null, null, null);
    assertEquals(localVar, localScope.getVar("x"));
    assertEquals(globalVar, globalScope.getVar("x"));
  }

  @Test
  public void testGetVar_Global() {
    Var var = globalScope.declare("x", null, null, null);
    assertEquals(var, globalScope.getVar("x"));
  }

  @Test
  public void testIsDeclared() {
    globalScope.declare("x", null, null, null);
    assertTrue(globalScope.isDeclared("x", true));
    assertFalse(globalScope.isDeclared("y", true));
  }
  
}