import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ScopeTest {

  private Scope scope;
  
  @Before
  public void setUp() {
    scope = new Scope(null, null);
  }
  
  @Test
  public void testDeclare() {
    Var var = scope.declare("foo", null, null, null);
    assertNotNull(var);
    assertEquals("foo", var.getName());
    assertNull(var.getNameNode());
    assertNull(var.getInputName());
  }
  
  @Test
  public void testUndeclare() {
    Var var = scope.declare("foo", null, null, null);
    assertNotNull(var);
    scope.undeclare(var);
    Var undeclaredVar = scope.getVar("foo");
    assertNull(undeclaredVar);
  }
  
  @Test
  public void testIsDeclared() {
    scope.declare("foo", null, null, null);
    assertTrue(scope.isDeclared("foo", false));
    assertFalse(scope.isDeclared("bar", false));
  }
  
  @Test
  public void testGetVars() {
    scope.declare("foo", null, null, null);
    scope.declare("bar", null, null, null);
    Iterator<Var> vars = scope.getVars();
    List<String> varNames = new ArrayList<>();
    while (vars.hasNext()) {
      varNames.add(vars.next().getName());
    }
    assertEquals(2, varNames.size());
    assertTrue(varNames.contains("foo"));
    assertTrue(varNames.contains("bar"));
  }
  
  @Test
  public void testIsGlobal() {
    assertTrue(scope.isGlobal());
    Scope childScope = new Scope(scope, null);
    assertFalse(childScope.isGlobal());
  }
  
  @Test
  public void testIsLocal() {
    assertFalse(scope.isLocal());
    Scope childScope = new Scope(scope, null);
    assertTrue(childScope.isLocal());
  }
  
  @Test
  public void testGetRootNode() {
    Node rootNode = new Node(Token.BLOCK);
    scope = new Scope(rootNode, null);
    assertEquals(rootNode, scope.getRootNode());
  }
  
  @Test
  public void testGetParent() {
    Scope parentScope = new Scope(null, null);
    scope = new Scope(parentScope, null);
    assertEquals(parentScope, scope.getParent());
  }
  
  @Test
  public void testGetGlobalScope() {
    Scope globalScope = scope.getGlobalScope();
    assertEquals(null, globalScope.getParent());
  }
  
  @Test
  public void testGetDepth() {
    assertEquals(0, scope.getDepth());
    Scope childScope = new Scope(scope, null);
    assertEquals(1, childScope.getDepth());
  }
  
  @Test
  public void testGetTypeOfThis() {
    ObjectType thisType = new ObjectType();
    scope = new Scope(null, thisType);
    assertEquals(thisType, scope.getTypeOfThis());
  }
  
  @Test
  public void testIsBottom() {
    assertFalse(scope.isBottom());
    Scope bottomScope = new Scope(null, null);
    assertTrue(bottomScope.isBottom());
  }
}