import static org.junit.Assert.*;
import static com.google.javascript.jscomp.Scope.*;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

public class ScopeTest {

  @Test
  public void testScopeCreation() {
    Node rootNode = Node.createIntLiteral(1); // Simulate a root node
    Scope scope = new Scope(null, rootNode);
    assertEquals(0, scope.getDepth());
    assertNull(scope.getParent());
    assertEquals("<global>", scope.getClass().getSimpleName());
  }

  @Test
  public void testDeclareVariable() {
    Node rootNode = Node.createFunction("myFunction", new String[]{}, null,
        Arrays.asList(Node.createVariable("x", INT_TYPE, null)));
    Scope parentScope = new Scope(null, rootNode);
    Scope scope = new Scope(parentScope, rootNode);

    String varName = "x";
    Var expectedVar = new Var(true, varName, null, INT_TYPE, scope, 0, null,
        false, null);

    Var actualVar = scope.declare(varName, null, INT_TYPE, null);
    assertEquals(expectedVar, actualVar);
    assertEquals(1, scope.getVarCount());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDeclareVariableWithExistingName() {
    Node rootNode = Node.createFunction("myFunction", new String[]{}, null,
        Arrays.asList(Node.createVariable("x", INT_TYPE, null)));
    Scope parentScope = new Scope(null, rootNode);
    Scope scope = new Scope(parentScope, rootNode);

    String varName = "x";
    scope.declare(varName, null, INT_TYPE, null); // Declare first time
    scope.declare(varName, null, INT_TYPE, null); // Try to declare second time
  }

  @Test
  public void testDeclareVariableWithExistingNameInParentScope() {
    Node rootNode = Node.createFunction("myFunction", new String[]{}, null,
        Arrays.asList(Node.createVariable("x", INT_TYPE, null)));
    Scope parentScope = new Scope(null, rootNode);
    Scope scope = new Scope(parentScope, rootNode);

    String varName = "x";
    parentScope.declare(varName, null, INT_TYPE, null); // Declare first time
    Var actualVar = scope.declare(varName, null, INT_TYPE, null);
    Var expectedVar = new Var(true, varName, null, INT_TYPE, scope, 1, null, false, null);
    assertEquals(expectedVar, actualVar);
    assertEquals(2, parentScope.getVarCount());
  }

  @Test
  public void testGetVariable() {
    Node rootNode = Node.createFunction("myFunction", new String[]{}, null,
        Arrays.asList(Node.createVariable("x", INT_TYPE, null),
            Node.createVariable("y", INT_TYPE, null)));
    Scope parentScope = new Scope(null, rootNode);
    Scope scope = new Scope(parentScope, rootNode);

    String xVarName = "x";
    String yVarName = "y";
    scope.declare(xVarName, null, INT_TYPE, null);
    scope.declare(yVarName, null, INT_TYPE, null);

    Var xVar = scope.getVar(xVarName);
    assertEquals("Scope.Var x", xVar.toString());
    assertEquals("x", xVar.getName());

    Var yVar = scope.getVar(yVarName);
    assertEquals("Scope.Var y", yVar.toString());
    assertEquals("y", yVar.getName());

    Var xVarInParent = parentScope.getVar(xVarName);
    assertNull(xVarInParent);
  }

  @Test
  public void testIsDeclared() {
    Node rootNode = Node.createFunction("myFunction", new String[]{}, null,
        Arrays.asList(Node.createVariable("x", INT_TYPE, null),
            Node.createVariable("y", INT_TYPE, null)));
    Scope parentScope = new Scope(null, rootNode);
    Scope scope = new Scope(parentScope, rootNode);

    scope.declare("x", null, INT_TYPE, null);
    scope.declare("y", null, INT_TYPE, null);

    assertTrue(scope.isDeclared("x", false));
    assertTrue(scope.isDeclared("x", true));
    assertTrue(scope.isDeclared("y", false));
    assertTrue(scope.isDeclared("y", true));
    assertFalse(scope.isDeclared("z", false));
    assertFalse(scope.isDeclared("z", true));
  }

  @Test
  public void testUndeclareVariable() {
    Node rootNode = Node.createFunction("myFunction", new String[]{}, null,
        Arrays.asList(Node.createVariable("x", INT_TYPE, null),
            Node.createVariable("y", INT_TYPE, null)));
    Scope parentScope = new Scope(null, rootNode);
    Scope scope = new Scope(parentScope, rootNode);

    String xVarName = "x";
    String yVarName = "y";
    scope.declare(xVarName, null, INT_TYPE, null);
    scope.declare(yVarName, null, INT_TYPE, null);

    Var xVar = scope.getVar(xVarName);
    assertEquals("Scope.Var x", xVar.toString());
    assertEquals(1, scope.getVarCount());

    scope.undeclare(xVar);
    Var xVarRemoved = scope.getVar(xVarName);
    assertNull(xVarRemoved);
    assertEquals(1, scope.getVarCount());

    Var yVar = scope.getVar(yVarName);
    assertEquals("Scope.Var y", yVar.toString());
    assertEquals(1, scope.getVarCount());
  }

  @Test
  public void testGetParentScope() {
    Node rootNode = Node.createFunction("myFunction", new String[]{}, null,
        Arrays.asList(Node.createVariable("x", INT_TYPE, null),
            Node.createVariable("y", INT_TYPE, null)));
    Scope parentScope = new Scope(null, rootNode);
    Scope scope = new Scope(parentScope, rootNode);

    String xVarName = "x";
    String yVarName = "y";
    scope.declare(xVarName, null, INT_TYPE, null);
    scope.declare(yVarName, null, INT_TYPE, null);

    Var xVar = scope.getVar(xVarName);
    assertEquals("Scope.Var x", xVar.toString());
    assertEquals(1, scope.getVarCount());

    Scope parent = scope.getParent();
    assertEquals(parentScope, parent);
    assertEquals(2, parentScope.getVarCount());
  }

  @Test
  public void testGetParentScopeIsRootNode() {
    Node rootNode = Node.createFunction("myFunction", new String[]{}, null,
        Arrays.asList(Node.createVariable("x", INT_TYPE, null),
            Node.createVariable("y", INT_TYPE, null)));
    Scope scope = new Scope(null, rootNode);

    assertEquals(null, scope.getParent());
    assertEquals(0, scope.getVarCount());
  }
}