import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ScopeTest {

  // Mocking the global scope for testing
  private Scope globalScope;

  @Before
  public void setUp() {
    Node rootNode = new Node(Token.BLOCK);
    globalScope = new Scope(rootNode, (ObjectType) null);
  }

  @Test
  public void testDeclareWithInferredType() {
    String name = "x";
    Node nameNode = new Node(Token.NAME, name);
    JSType type = null;
    CompilerInput input = null;
    Var var = globalScope.declare(name, nameNode, type, input);

    assertNotNull(var);
    assertEquals(name, var.getName());
    assertNull(var.getType());
    assertNull(var.getInputName());
    assertEquals(globalScope, var.getScope());
  }

  @Test
  public void testDeclareWithDeclaredType() {
    String name = "x";
    Node nameNode = new Node(Token.NAME, name);
    JSType type = JSType.UNKNOWN;
    CompilerInput input = null;
    Var var = globalScope.declare(name, nameNode, type, input, false);

    assertNotNull(var);
    assertEquals(name, var.getName());
    assertEquals(type, var.getType());
    assertNull(var.getInputName());
    assertEquals(globalScope, var.getScope());
  }

  @Test
  public void testGetVar() {
    String name = "x";
    Node nameNode = new Node(Token.NAME, name);
    JSType type = JSType.UNKNOWN;
    CompilerInput input = null;
    Var var = globalScope.declare(name, nameNode, type, input, false);

    Var retrievedVar = globalScope.getVar(name);

    assertNotNull(retrievedVar);
    assertEquals(var, retrievedVar);
  }

  @Test
  public void testGetVarNotDeclared() {
    String name = "x";

    Var var = globalScope.getVar(name);

    assertNull(var);
  }

  @Test
  public void testIsDeclared() {
    String name = "x";
    Node nameNode = new Node(Token.NAME, name);
    JSType type = JSType.UNKNOWN;
    CompilerInput input = null;
    globalScope.declare(name, nameNode, type, input, false);

    boolean isDeclared = globalScope.isDeclared(name, false);

    assertTrue(isDeclared);
  }

  @Test
  public void testIsDeclaredInParentScope() {
    Scope parentScope = new Scope(globalScope, null);
    String name = "x";
    Node nameNode = new Node(Token.NAME, name);
    JSType type = JSType.UNKNOWN;
    CompilerInput input = null;
    parentScope.declare(name, nameNode, type, input, false);

    boolean isDeclared = globalScope.isDeclared(name, true);

    assertTrue(isDeclared);
  }

  @Test
  public void testIsNotDeclared() {
    String name = "x";

    boolean isDeclared = globalScope.isDeclared(name, false);

    assertFalse(isDeclared);
  }

  // Other test cases for remaining methods in the Scope class can be added similarly

}