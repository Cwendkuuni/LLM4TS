import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;

public class ScopeTest {

  private Scope globalScope;
  private Scope localScope;
  
  @Before
  public void setUp() {
    globalScope = new Scope(null, rootNode);
    localScope = new Scope(parentScope, rootNode);
  }

  @Test
  public void testDeclareVariableInGlobalScope() {
    Var var = globalScope.declare("testVar", new Node(Token.NAME), JSType.UNKNOWN, null);
    assertNotNull(var);
    assertEquals("testVar", var.getName());
    assertEquals(JSType.UNKNOWN, var.getType());
    assertNull(var.getInputName());
  }

  @Test
  public void testDeclareVariableInLocalScope() {
    Var var = localScope.declare("testVar", new Node(Token.NAME), JSType.OBJECT, null);
    assertNotNull(var);
    assertEquals("testVar", var.getName());
    assertEquals(JSType.OBJECT, var.getType());
    assertNull(var.getInputName());
  }

  @Test
  public void testUndeclareVariable() {
    Var var = localScope.declare("testVar", new Node(Token.NAME), JSType.UNKNOWN, null);
    assertNotNull(var);
    localScope.undeclare(var);
    assertNull(localScope.getVar("testVar"));
  }

  @Test
  public void testGetVarInGlobalScope() {
    Var var = globalScope.declare("testVar", new Node(Token.NAME), JSType.BOOLEAN, null);
    Var retrievedVar = globalScope.getVar("testVar");
    assertNotNull(retrievedVar);
    assertEquals(var, retrievedVar);
  }

  @Test
  public void testGetVarInLocalScope() {
    Var var = localScope.declare("testVar", new Node(Token.NAME), JSType.STRING, null);
    Var retrievedVar = localScope.getVar("testVar");
    assertNotNull(retrievedVar);
    assertEquals(var, retrievedVar);
  }

  @Test
  public void testIsDeclaredInScope() {
    localScope.declare("testVar", new Node(Token.NAME), JSType.NUMBER, null);
    assertTrue(localScope.isDeclared("testVar", true));
  }

}