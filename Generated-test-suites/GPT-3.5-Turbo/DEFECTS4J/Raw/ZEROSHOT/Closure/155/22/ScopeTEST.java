import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

public class ScopeTest {
  
  private Scope globalScope;
  private Scope localScope;
  private Node rootNode;
  
  @Before
  public void setUp() {
    rootNode = new Node(Node.SCRIPT);
    globalScope = new Scope(rootNode, null);
    localScope = new Scope(globalScope, rootNode);
  }
  
  @Test
  public void testGetDepth() {
    assertEquals(0, globalScope.getDepth());
    assertEquals(1, localScope.getDepth());
  }
  
  @Test
  public void testIsBottom() {
    assertFalse(globalScope.isBottom());
    assertFalse(localScope.isBottom());
  }
  
  @Test
  public void testGetRootNode() {
    assertEquals(rootNode, globalScope.getRootNode());
    assertEquals(rootNode, localScope.getRootNode());
  }
  
  @Test
  public void testGetParent() {
    assertNull(globalScope.getParent());
    assertEquals(globalScope, localScope.getParent());
  }
  
  @Test
  public void testGetTypeOfThis() {
    assertNull(globalScope.getTypeOfThis());
    assertNull(localScope.getTypeOfThis());
  }
  
  @Test
  public void testDeclare() {
    Scope.Var var = globalScope.declare("testVar", null, null, null);
    assertNotNull(var);
    assertEquals("testVar", var.getName());
    assertNull(var.getNameNode());
    assertNull(var.getType());
    assertNull(var.getJSDocInfo());

    var = localScope.declare("testVar2", null, null, null);
    assertNotNull(var);
    assertEquals("testVar2", var.getName());
    assertNull(var.getNameNode());
    assertNull(var.getType());
    assertNull(var.getJSDocInfo());
  }
  
  @Test
  public void testUndeclare() {
    Scope.Var var = globalScope.declare("testVar", null, null, null);
    globalScope.undeclare(var);
    assertNull(globalScope.getVar("testVar"));
  }
  
  @Test
  public void testGetVar() {
    Scope.Var var = globalScope.declare("testVar", null, null, null);
    assertEquals(var, globalScope.getVar("testVar"));
    assertEquals(var, localScope.getVar("testVar"));
    
    var = localScope.declare("testVar", null, null, null);
    assertEquals(var, localScope.getVar("testVar"));
  }
  
  @Test
  public void testIsDeclared() {
    globalScope.declare("testVar", null, null, null);
    assertTrue(globalScope.isDeclared("testVar", false));
    assertTrue(globalScope.isDeclared("testVar", true));
    assertFalse(globalScope.isDeclared("nonexistentVar", false));
    assertFalse(globalScope.isDeclared("nonexistentVar", true));
    
    localScope.declare("testVar", null, null, null);
    assertFalse(localScope.isDeclared("testVar", false));
    assertTrue(localScope.isDeclared("testVar", true));
    assertFalse(localScope.isDeclared("nonexistentVar", false));
    assertFalse(localScope.isDeclared("nonexistentVar", true));
  }
  
  @Test
  public void testGetVars() {
    globalScope.declare("var1", null, null, null);
    globalScope.declare("var2", null, null, null);
    assertEquals(2, globalScope.getVarCount());
    
    localScope.declare("var3", null, null, null);
    assertEquals(1, localScope.getVarCount());
  }
  
  @Test
  public void testIsGlobal() {
    assertTrue(globalScope.isGlobal());
    assertFalse(localScope.isGlobal());
  }
  
  @Test
  public void testIsLocal() {
    assertFalse(globalScope.isLocal());
    assertTrue(localScope.isLocal());
  }
}