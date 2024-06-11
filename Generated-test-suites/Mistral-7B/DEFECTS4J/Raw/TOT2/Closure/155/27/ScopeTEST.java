import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSDocInfo.createJSDocInfo;
import static com.google.javascript.rhino.NodeFactory.createName;
import static com.google.javascript.rhino.NodeFactory.createNumberLiteral;
import static com.google.javascript.rhino.NodeFactory.createProperty;
import static com.google.javascript.rhino.NodeFactory.createScope;
import static com.google.javascript.rhino.NodeFactory.createStringLiteral;
import static com.google.javascript.rhino.NodeFactory.createThis;
import static com.google.javascript.rhino.Token.*;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;

import org.junit.Before;
import org.junit.Test;

public class ScopeTest {

  private Scope globalScope;
  private Scope localScope;
  private CompilerInput input;

  @Before
  public void setUp() {
    Node rootNode = NodeFactory.createBlock(null);
    this.globalScope = new Scope(rootNode, null);
    this.input = new CompilerInput("test.js", "test");

    Node rootNode2 = NodeFactory.createFunction(
        "test", FUNCTION, null, null, null, null);
    this.localScope = new Scope(rootNode2, globalScope);
  }

  @Test
  public void testDeclareVariable() {
    // Declare a local variable with a name "x" and a type "number".
    Var x = localScope.declare("x", createName("x"), JSType.getNumberType(),
        input);

    // Check that the variable was correctly added to the scope.
    assertEquals("x", x.getName());
    assertEquals(localScope, x.getScope());
    assertEquals(0, x.getIndex());
    assertNull(x.getParentNode());
    assertFalse(x.isBleedingFunction());
    assertEquals(JSType.getNumberType(), x.getType());
    assertNull(x.getNameNode());
    assertFalse(x.isDefine());
    assertNull(x.getJSDocInfo());
    assertFalse(x.isInferred());
    assertNull(x.getInputName());
    assertFalse(x.isNoShadow());

    // Test getting the variable from the scope.
    Var declaredX = localScope.getVar("x");
    assertEquals(x, declaredX);

    // Test getting an undefined variable from the scope.
    assertNull(localScope.getVar("y"));
  }

  @Test
  public void testDeclareVariableWithJSDocInfo() {
    // Declare a local variable with a name "x" and a type "number",
    // with a JSDocInfo annotation "@param {number} x".
    JSDocInfo jsdocInfo = createJSDocInfo(createProperty(
        createStringLiteral("x"), createStringLiteral("x"), null),
        createStringLiteral("number"), null);
    Var x = localScope.declare("x", createName("x"), JSType.getNumberType(),
        input, false, jsdocInfo);

    // Check that the variable was correctly added to the scope and that
    // the JSDocInfo was correctly stored.
    assertEquals("x", x.getName());
    assertEquals(localScope, x.getScope());
    assertEquals(0, x.getIndex());
    assertNull(x.getParentNode());
    assertFalse(x.isBleedingFunction());
    assertEquals(JSType.getNumberType(), x.getType());
    assertNull(x.getNameNode());
    assertTrue(x.isDefine());
    assertEquals(jsdocInfo, x.getJSDocInfo());
    assertFalse(x.isInferred());
    assertNull(x.getInputName());
    assertFalse(x.isNoShadow());

    // Test getting the variable from the scope.
    Var declaredX = localScope.getVar("x");
    assertEquals(x, declaredX);

    // Test getting an undefined variable from the scope.
    assertNull(localScope.getVar("y"));
  }

  @Test
  public void testDeclareVariableWithInput() {
    // Declare a local variable with a name "x" and a type "number",
    // with an input "input.js".
    Var x = localScope.declare("x", createName("x"), JSType.getNumberType(),
        input, true);

    // Check that the variable was correctly added to the scope.
    assertEquals("x", x.getName());
    assertEquals(localScope, x.getScope());
    assertEquals(0, x.getIndex());
    assertNull(x.getParentNode());
    assertFalse(x.isBleedingFunction());
    assertEquals(JSType.getNumberType(), x.getType());
    assertNull(x.getNameNode());
    assertFalse(x.isDefine());
    assertNull(x.getJSDocInfo());
    assertFalse(x.isInferred());
    assertEquals("input.js", x.getInputName());
    assertFalse(x.isNoShadow());

    // Test getting the variable from the scope.
    Var declaredX = localScope.getVar("x");
    assertEquals(x, declaredX);

    // Test getting an undefined variable from the scope.
    assertNull(localScope.getVar("y"));
  }

  @Test
  public void testDeclareVariableWithMultipleInputs() {
    // Declare a local variable with a name "x" and a type "number",
    // with two inputs "input1.js" and "input2.js".
    Var x = localScope.declare("x", createName("x"), JSType.getNumberType(),
        new CompilerInput("input1.js", "input1"),
        new CompilerInput("input2.js", "input2"));

    // Check that the variable was correctly added to the scope.
    assertEquals("x", x.getName());
    assertEquals(localScope, x.getScope());
    assertEquals(0, x.getIndex());
    assertNull(x.getParentNode());
    assertFalse(x.isBleedingFunction());
    assertEquals(JSType.getNumberType(), x.getType());
    assertNull(x.getNameNode());
    assertFalse(x.isDefine());
    assertNull(x.getJSDocInfo());
    assertFalse(x.isInferred());
    assertEquals("input1.js, input2.js", x.getInputName());
    assertFalse(x.isNoShadow());

    // Test getting the variable from the scope.
    Var declaredX = localScope.getVar("x");
    assertEquals(x, declaredX);

    // Test getting an undefined variable from the scope.
    assertNull(localScope.getVar("y"));
  }

  @Test
  public void testGetTypeOfThis() {
    // Check that the global scope's thisType is the native ObjectType.
    assertEquals(JSType.getObjectType(), globalScope.getTypeOfThis());

    // Check that the local scope's thisType is the FunctionType that was
    // created for the test function.
    FunctionType functionType = (FunctionType) localScope.getTypeOfThis();
    assertEquals("test", functionType.getName());
  }

  @Test
  public void testUndeclareVariable() {
    // Declare a local variable with a name "x" and a type "number".
    Var x = localScope.declare("x", createName("x"), JSType.getNumberType(),
        input);

    // Check that the variable was correctly added to the scope.
    assertEquals("x", x.getName());
    assertEquals(localScope, x.getScope());
    assertEquals(0, x.getIndex());
    assertNull(x.getParentNode());
    assertFalse(x.isBleedingFunction());
    assertEquals(JSType.getNumberType(), x.getType());
    assertNull(x.getNameNode());
    assertFalse(x.isDefine());
    assertNull(x.getJSDocInfo());
    assertFalse(x.isInferred());
    assertNull(x.getInputName());
    assertFalse(x.isNoShadow());

    // Undeclare the variable from the scope.
    localScope.undeclare(x);

    // Test getting an undefined variable from the scope.
    assertNull(localScope.getVar("x"));
  }

  @Test
  public void testGetVar() {
    // Declare two variables with the names "x" and "y" in the local scope.
    Var x = localScope.declare("x", createName("x"), JSType.getNumberType(),
        input);
    Var y = localScope.declare("y", createName("y"), JSType.getNumberType(),
        input);

    // Check that the variables were correctly added to the scope.
    assertEquals("x", x.getName());
    assertEquals("y", y.getName());
    assertEquals(localScope, x.getScope());
    assertEquals(localScope, y.getScope());
    assertFalse(x.equals(y));

    // Test getting the variables from the scope.
    Var getX = localScope.getVar("x");
    Var getY = localScope.getVar("y");

    // Check that the variables returned from the scope are the same as the
    // variables that were declared.
    assertEquals(x, getX);
    assertEquals(y, getY);

    // Test getting an undefined variable from the scope.
    assertNull(localScope.getVar("z"));
  }

  @Test
  public void testGetVars() {
    // Declare two variables with the names "x" and "y" in the local scope.
    Var x = localScope.declare("x", createName("x"), JSType.getNumberType(),
        input);
    Var y = localScope.declare("y", createName("y"), JSType.getNumberType(),
        input);

    // Check that the variables were correctly added to the scope.
    assertEquals(2, localScope.getVarCount());

    // Test iterating over the variables in the scope.
    Multimap<String, Var> vars = LinkedHashMultimap.create();
    for (Var var : localScope.getVars()) {
      vars.put(var.getName(), var);
    }
    assertEquals(x, vars.get("x").get(0));
    assertEquals(y, vars.get("y").get(0));
  }

  @Test
  public void testIsDeclared() {
    // Declare a variable with the name "x" in the local scope.
    Var x = localScope.declare("x", createName("x"), JSType.getNumberType(),
        input);

    // Test checking whether variables are declared in the local scope.
    assertTrue(localScope.isDeclared("x", false));
    assertTrue(localScope.isDeclared("x", true));

    // Test checking whether variables are declared in the parent scope.
    assertTrue(globalScope.isDeclared("globalX", false));
    assertTrue(globalScope.isDeclared("globalX", true));

    // Test checking whether an undefined variable is declared.
    assertFalse(localScope.isDeclared("y", false));
    assertFalse(localScope.isDeclared("y", true));
  }

  @Test
  public void testGetParentScope() {
    // Declare a variable with the name "x" in the local scope.
    Var x = localScope.declare("x", createName("x"), JSType.getNumberType(),
        input);

    // Check that the local scope's parent is the global scope.
    assertEquals(globalScope, localScope.getParentScope());

    // Check that the global scope has no parent scope.
    assertNull(globalScope.getParentScope());
  }

  @Test
  public void testToString() {
    // Declare a variable with the name "x" in the local scope.
    Var x = localScope.declare("x", createName("x"), JSType.getNumberType(),
        input);

    // Test converting the scope to a string.
    assertEquals("Scope{" + System.lineSeparator()
        + "  vars=" + System.lineSeparator()
        + "    {" + System.lineSeparator()
        + "      name='x'" + System.lineSeparator()
        + "      type=" + JSType.getNumberType() + System.lineSeparator()
        + "    }" + System.lineSeparator()
        + "  }" + System.lineSeparator()
        + "}", localScope.toString());

    // Test converting the variable to a string.
    assertEquals("Scope.Var x", x.toString());
  }
}