import static org.junit.Assert.*;
import static com.google.javascript.jscomp.Scope.*;
import com.google.common.collect.Maps;
import java.util.Map;

/**
 * Tests for {@link Scope} class
 */
public class ScopeTest {

  /**
   * Test creating a new scope and accessing variables.
   */
  @org.junit.Test
  public void testCreateScopeAndAccessVariables() {
    Scope parentScope = new Scope(null, new Node(Token.BLOCK));
    Scope childScope = new Scope(parentScope, new Node(Token.FUNCTION, null, new Node(Token.IDENT, "foo"), null));

    // Declare and access variables
    childScope.declare("x", new Node(Token.VAR, "x"), null, new CompilerInput("TestFile.js"));
    childScope.declare("y", new Node(Token.VAR, "y"), null, new CompilerInput("TestFile.js"));

    Var x = childScope.getVar("x");
    Var y = childScope.getVar("y");

    assertNotNull(x);
    assertNotNull(y);
    assertNull(parentScope.getVar("x"));
    assertNull(parentScope.getVar("y"));

    // Access variables
    Object typeX = x.getType();
    Object typeY = y.getType();

    assertEquals(JSType.UNDEFINED, typeX);
    assertEquals(JSType.UNDEFINED, typeY);

    // Set variable types
    x.setType(JSType.STRING);
    y.setType(JSType.NUMBER);

    typeX = x.getType();
    typeY = y.getType();

    assertEquals(JSType.STRING, typeX);
    assertEquals(JSType.NUMBER, typeY);

    // Verify parent scope has no access to child scope variables
    assertNull(parentScope.getVar("x"));
    assertNull(parentScope.getVar("y"));
  }

  /**
   * Test creating global scope and accessing global variables.
   */
  @org.junit.Test
  public void testCreateGlobalScopeAndAccessVariables() {
    Scope globalScope = new Scope(null, new Node(Token.SCRIPT), null, new ObjectType(GLOBAL_THIS));

    // Declare and access global variables
    globalScope.declare("x", new Node(Token.VAR, "x"), null, new CompilerInput("TestFile.js"));
    globalScope.declare("y", new Node(Token.VAR, "y"), null, new CompilerInput("TestFile.js"));

    Var x = globalScope.getVar("x");
    Var y = globalScope.getVar("y");

    assertNotNull(x);
    assertNotNull(y);

    // Access variables
    Object typeX = x.getType();
    Object typeY = y.getType();

    assertEquals(JSType.UNDEFINED, typeX);
    assertEquals(JSType.UNDEFINED, typeY);

    // Set variable types
    x.setType(JSType.STRING);
    y.setType(JSType.NUMBER);

    typeX = x.getType();
    typeY = y.getType();

    assertEquals(JSType.STRING, typeX);
    assertEquals(JSType.NUMBER, typeY);

    // Verify global scope has access to global variables
    assertEquals(x, globalScope.getVar("x"));
    assertEquals(y, globalScope.getVar("y"));
  }

  /**
   * Test creating a bottom scope and accessing variables.
   */
  @org.junit.Test
  public void testCreateBottomScopeAndAccessVariables() {
    Node rootNode = new Node(Token.FUNCTION, null, new Node(Token.IDENT, "foo"), null);
    Scope bottomScope = new Scope(null, rootNode, new ObjectType(GLOBAL_THIS));

    // Access variables
    assertNull(bottomScope.getVar("x"));
    assertNull(bottomScope.getVar("y"));
  }

  /**
   * Test declaring and accessing variables with the same name in different scopes.
   */
  @org.junit.Test
  public void testDeclareAndAccessVariablesWithSameNameInDifferentScopes() {
    Scope parentScope = new Scope(null, new Node(Token.BLOCK));
    Scope childScope1 = new Scope(parentScope, new Node(Token.FUNCTION, null, new Node(Token.IDENT, "foo"), null));
    Scope childScope2 = new Scope(parentScope, new Node(Token.FUNCTION, null, new Node(Token.IDENT, "foo"), null));

    childScope1.declare("x", new Node(Token.VAR, "x"), null, new CompilerInput("TestFile1.js"));
    childScope2.declare("x", new Node(Token.VAR, "x"), null, new CompilerInput("TestFile2.js"));

    Var x1 = childScope1.getVar("x");
    Var x2 = childScope2.getVar("x");

    assertNotNull(x1);
    assertNotNull(x2);

    // Access variables
    Object typeX1 = x1.getType();
    Object typeX2 = x2.getType();

    assertEquals(JSType.UNDEFINED, typeX1);
    assertEquals(JSType.UNDEFINED, typeX2);

    // Set variable types
    x1.setType(JSType.STRING);
    x2.setType(JSType.NUMBER);

    typeX1 = x1.getType();
    typeX2 = x2.getType();

    assertEquals(JSType.STRING, typeX1);
    assertEquals(JSType.NUMBER, typeX2);

    // Verify parent scope has no access to child scope variables
    assertNull(parentScope.getVar("x"));
  }

  /**
   * Test getting variables by their name, even when they're not present.
   */
  @org.junit.Test
  public void testGetVariableByNameNull() {
    Scope scope = new Scope(null, new Node(Token.BLOCK));

    Var var = scope.getVar("x");

    assertNull(var);
  }

  /**
   * Test getting variables by their name, when they're present in the current
   * scope or one of its parents.
   */
  @org.junit.Test
  public void testGetVariableByNamePresent() {
    Scope parentScope = new Scope(null, new Node(Token.BLOCK));
    Scope childScope = new Scope(parentScope, new Node(Token.FUNCTION, null, new Node(Token.IDENT, "foo"), null));

    childScope.declare("x", new Node(Token.VAR, "x"), null, new CompilerInput("TestFile.js"));

    Var var = childScope.getVar("x");

    assertNotNull(var);

    // Verify parent scope has no access to child scope variables
    assertNull(parentScope.getVar("x"));
  }

  /**
   * Test getting variables by their name, when they're defined in the parent
   * scope and have the same name as a variable in the current scope.
   */
  @org.junit.Test
  public void testGetVariableByNameShadowed() {
    Scope parentScope = new Scope(null, new Node(Token.BLOCK));
    Scope childScope = new Scope(parentScope, new Node(Token.FUNCTION, null, new Node(Token.IDENT, "foo"), null));

    parentScope.declare("x", new Node(Token.VAR, "x"), null, new CompilerInput("ParentFile.js"));
    childScope.declare("x", new Node(Token.VAR, "x"), null, new CompilerInput("ChildFile.js"));

    Var parentX = parentScope.getVar("x");
    Var childX = childScope.getVar("x");

    assertNotNull(parentX);
    assertNotNull(childX);

    // Access variables
    Object typeParentX = parentX.getType();
    Object typeChildX = childX.getType();

    assertEquals(JSType.UNDEFINED, typeParentX);
    assertEquals(JSType.UNDEFINED, typeChildX);

    // Set variable types
    parentX.setType(JSType.STRING);
    childX.setType(JSType.NUMBER);

    typeParentX = parentX.getType();
    typeChildX = childX.getType();

    assertEquals(JSType.STRING, typeParentX);
    assertEquals(JSType.NUMBER, typeChildX);

    // Verify that the current scope uses the variable from itself, not the parent
    assertNotNull(childScope.getVar("x"));
    assertNull(parentScope.getVar("x"));
  }

  /**
   * Test getting variables by their name, when they're defined in the parent
   * scope and have a different name than the current scope.
   */
  @org.junit.Test
  public void testGetVariableByNameDifferentName() {
    Scope parentScope = new Scope(null, new Node(Token.BLOCK));
    Scope childScope = new Scope(parentScope, new Node(Token.FUNCTION, null, new Node(Token.IDENT, "foo"), null));

    parentScope.declare("x", new Node(Token.VAR, "x"), null, new CompilerInput("ParentFile.js"));
    childScope.declare("y", new Node(Token.VAR, "y"), null, new CompilerInput("ChildFile.js"));

    Var parentX = parentScope.getVar("x");
    Var childY = childScope.getVar("y");

    assertNotNull(parentX);
    assertNotNull(childY);

    // Access variables
    Object typeParentX = parentX.getType();
    Object typeChildY = childY.getType();

    assertEquals(JSType.UNDEFINED, typeParentX);
    assertEquals(JSType.UNDEFINED, typeChildY);

    // Set variable types
    parentX.setType(JSType.STRING);
    childY.setType(JSType.NUMBER);

    typeParentX = parentX.getType();
    typeChildY = childY.getType();

    assertEquals(JSType.STRING, typeParentX);
    assertEquals(JSType.NUMBER, typeChildY);

    // Verify that the current scope has no access to the parent scope variable
    assertNull(childScope.getVar("x"));
  }

  /**
   * Test getting variables by their name, when they don't exist in any scope.
   */
  @org.junit.Test
  public void testGetVariableByNameNonexistent() {
    Scope scope = new Scope(null, new Node(Token.BLOCK));

    Var var = scope.getVar("x");

    assertNull(var);
  }
}