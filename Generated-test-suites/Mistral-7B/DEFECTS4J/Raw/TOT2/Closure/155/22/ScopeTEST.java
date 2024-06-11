import static org.junit.Assert.*;
import static com.google.javascript.jscomp.Scope.Var.*;

import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.ErrorReporter;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.NodeUtil;

import java.util.Iterator;
import java.util.Map;

public class ScopeTest {

  private static void testScope(Scope scope, CompilerInput input) {
    ErrorReporter errorReporter = new ErrorReporter();

    // Basic functionality test
    Var variable = scope.declare("testVariable", null, JSType.STRING, input);
    assertEquals("testVariable", variable.getName());
    assertEquals(JSType.STRING, variable.getType());

    // Test getParentScope
    assertEquals(scope.getParent(), scope.getParentScope());

    // Test getRootNode
    assertEquals(scope.getRootNode(), scope.getParent().getParent().getRootNode());

    // Test getTypeOfThis
    assertEquals(JSType.getNativeObjectType(GLOBAL_THIS), scope.getTypeOfThis());

    // Test isLocal and isGlobal
    assertFalse(scope.isLocal());
    assertTrue(scope.isGlobal());
    scope = new Scope(null, Node.newFunction(Node.NEW, Node.newIdentifier("testFunction"), null, null));
    assertFalse(scope.isGlobal());
    assertTrue(scope.isLocal());

    // Test declare and getVar
    Var variable2 = scope.declare("testVariable2", null, JSType.NUMBER, input);
    Var declaredVariable = scope.getVar("testVariable2");
    assertEquals(variable2, declaredVariable);

    // Test undeclare
    scope.undeclare(variable2);
    assertNull(scope.getVar("testVariable2"));

    // Test getVars
    Iterator<Var> vars = scope.getVars();
    assertTrue(vars.hasNext());
    Var var = vars.next();
    assertEquals("testVariable", var.getName());
    assertTrue(vars.hasNext());
    Var var2 = vars.next();
    assertEquals("testVariable2", var2.getName());
    assertFalse(vars.hasNext());

    // Test getVarCount
    assertEquals(2, scope.getVarCount());

    // Test isDeclared
    assertTrue(scope.isDeclared("testVariable", true));
    assertFalse(scope.isDeclared("testVariable3", true));
    assertTrue(scope.isDeclared("testVariable3", false));

    // Edge cases and exception handling tests

    // Test declare duplicate variable
    try {
      scope.declare("testVariable", null, JSType.STRING, input);
      scope.declare("testVariable", null, JSType.STRING, input);
      fail("Should have thrown an exception");
    } catch (IllegalStateException e) {
      // Expected
    }

    // Test getVar with non-existent variable
    assertNull(scope.getVar("testVariable4"));

    // Test getVar with non-existent parent scope
    Scope parentScope = new Scope(null, Node.newBlock(Node.NEW));
    Scope childScope = new Scope(parentScope, Node.newFunction(Node.NEW, Node.newIdentifier("testFunction"), null, null));
    assertNull(childScope.getVar("testVariable4"));

    // Test getVar with variable in parent scope
    Var parentVariable = parentScope.declare("testVariable4", null, JSType.STRING, input);
    assertEquals(parentVariable, childScope.getVar("testVariable4"));

    // Test getVar with bleeding function variable
    Node bleedingFunction = Node.newFunctionExpression(Node.NEW, Node.newIdentifier("testBleedingFunction"), null, null);
    Scope bleedingFunctionScope = new Scope(scope, bleedingFunction);
    Node bleedingFunctionVariable = bleedingFunction.getLastChild();
    assertEquals(bleedingFunctionVariable, bleedingFunctionScope.getVar("arguments")
            .getInitialValue());

    // Test getVar with variable in grandparent scope
    Scope grandparentScope = new Scope(null, Node.newBlock(Node.NEW));
    Scope parentScope2 = new Scope(grandparentScope, Node.newFunction(Node.NEW, Node.newIdentifier("testFunction2"), null, null));
    Scope childScope2 = new Scope(parentScope2, Node.newFunction(Node.NEW, Node.newIdentifier("testFunction3"), null, null));
    Var grandparentVariable = grandparentScope.declare("testVariable5", null, JSType.STRING, input);
    assertEquals(grandparentVariable, childScope2.getVar("testVariable5"));

    // Test join
    Scope scope1 = new Scope(null, Node.newBlock(Node.NEW));
    Var variable1 = scope1.declare("testVariable1", null, JSType.STRING, input);
    Scope scope2 = new Scope(scope1, Node.newBlock(Node.NEW));
    Var variable2 = scope2.declare("testVariable1", null, JSType.NUMBER, input);
    assertEquals(variable1.getType().join(variable2.getType()), variable1.getType());

    // Test resolveType
    Var variableWithInferredType = scope.declare("testVariableWithInferredType", null, null, input);
    variableWithInferredType.resolveType(errorReporter);
    assertEquals(JSType.STRING, variableWithInferredType.getType());

    // Test equals and hashCode
    Var var1 = new Var("testVariable1", null, JSType.STRING, null, null, 0, null, false, null);
    Var var2 = new Var("testVariable1", null, JSType.STRING, null, null, 0, null, false, null);
    assertEquals(var1, var2);
    assertEquals(var1.hashCode(), var2.hashCode());

    // Test toString
    assertEquals("Scope.Var testVariable1", var1.toString());
  }

  @org.junit.Test
  public void testScope() {
    CompilerInput input = new CompilerInput("test.js", null);
    Scope globalScope = new Scope(null, Node.newBlock(Node.NEW));
    testScope(globalScope, input);

    Node rootNode = Node.newFunction(Node.NEW, Node.newIdentifier("testFunction"), null, Node.newBlock(Node.NEW));
    Scope functionScope = new Scope(globalScope, rootNode);
    testScope(functionScope, input);

    rootNode = Node.newBlock(Node.NEW);
    Scope blockScope = new Scope(functionScope, rootNode);
    testScope(blockScope, input);
  }
}