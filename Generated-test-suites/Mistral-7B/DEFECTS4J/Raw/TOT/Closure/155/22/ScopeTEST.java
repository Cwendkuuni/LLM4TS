import static org.junit.Assert.*;
import static com.google.javascript.jscomp.Scope.Var.*;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.FunctionScope;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;

import java.util.Iterator;
import java.util.Map;

/**
 * Test for Scope class.
 */
public class ScopeTest {

  private static Scope createFunctionScope(Node rootNode, ErrorReporter errorReporter) {
    JSType rootNodeType = rootNode.getJSType();
    ObjectType thisType = new ObjectType("Object");
    return new FunctionScope(null, rootNode, thisType, errorReporter);
  }

  private static Scope createGlobalScope(Node rootNode, ErrorReporter errorReporter) {
    ObjectType thisType = new ObjectType("Object");
    return new Scope(rootNode, null, thisType, errorReporter);
  }

  @org.junit.Test
  public void testDeclareVariable() {
    ErrorReporter errorReporter = new ErrorReporter();
    Node rootNode = NodeUtil.createBlock("function test() {}");
    Scope scope = createFunctionScope(rootNode, errorReporter);

    String variableName = "testVariable";
    JSType variableType = JSType.BOOLEAN;
    CompilerInput input = new CompilerInput("test.js", null);

    Var variable = scope.declare(variableName, null, variableType, input, true);
    assertEquals(variableName, variable.getName());
    assertEquals(variableType, variable.getType());
    assertFalse(variable.isTypeInferred());
  }

  @org.junit.Test
  public void testUndelecareVariable() {
    ErrorReporter errorReporter = new ErrorReporter();
    Node rootNode = NodeUtil.createBlock("function test() {}");
    Scope scope = createFunctionScope(rootNode, errorReporter);

    String variableName = "testVariable";
    Var variable = scope.declare(variableName, null, JSType.BOOLEAN, null, true);
    scope.undeclare(variable);
    assertNull(scope.getVar(variableName));
  }

  @org.junit.Test
  public void testGetSlot() {
    ErrorReporter errorReporter = new ErrorReporter();
    Node rootNode = NodeUtil.createBlock("function test() {}");
    Scope scope = createFunctionScope(rootNode, errorReporter);

    String variableName = "testVariable";
    JSType variableType = JSType.BOOLEAN;
    CompilerInput input = new CompilerInput("test.js", null);

    Var variable = scope.declare(variableName, null, variableType, input, true);
    assertEquals(variable, scope.getSlot(variableName));
  }

  @org.junit.Test
  public void testGetVar() {
    ErrorReporter errorReporter = new ErrorReporter();
    Node rootNode = NodeUtil.createBlock("function test() {}");
    Scope scope = createFunctionScope(rootNode, errorReporter);

    String variableName = "testVariable";
    JSType variableType = JSType.BOOLEAN;
    CompilerInput input = new CompilerInput("test.js", null);

    Var variable = scope.declare(variableName, null, variableType, input, true);
    assertEquals(variable, scope.getVar(variableName));
  }

  @org.junit.Test
  public void testGetVarInGlobalScope() {
    ErrorReporter errorReporter = new ErrorReporter();
    Node rootNode = NodeUtil.createBlock("function test() {}");
    Scope globalScope = createGlobalScope(rootNode, errorReporter);

    String variableName = "globalVariable";
    JSType variableType = JSType.BOOLEAN;
    CompilerInput input = new CompilerInput("test.js", null);

    Var variable = globalScope.declare(variableName, null, variableType, input, true);
    assertNull(globalScope.getParent().getVar(variableName));
  }

  @org.junit.Test
  public void testGetVarInParentScope() {
    ErrorReporter errorReporter = new ErrorReporter();
    Node rootNode = NodeUtil.createBlock("function parentFunction() {" +
        "function childFunction() {}" +
        "var childVariable = 1; }");
    Scope parentScope = createFunctionScope(NodeUtil.getChild(rootNode, 0), errorReporter);
    Scope childScope = createFunctionScope(NodeUtil.getChild(rootNode, 1), errorReporter);

    String childVariableName = "childVariable";
    JSType childVariableType = JSType.NUMBER;
    CompilerInput input = new CompilerInput("test.js", null);

    Var childVariable = childScope.declare(childVariableName, null, childVariableType, input, true);
    assertEquals(childVariable.getScope(), childScope);
    assertEquals(childVariable, parentScope.getVar(childVariableName));
  }

  @org.junit.Test
  public void testIsDeclared() {
    ErrorReporter errorReporter = new ErrorReporter();
    Node rootNode = NodeUtil.createBlock("function test() {" +
        "var testVariable = 1;" +
        "function childFunction() {}" +
        "var childVariable = 1;" +
        "}");
    Scope scope = createFunctionScope(rootNode, errorReporter);

    assertTrue(scope.isDeclared("testVariable", true));
    assertTrue(scope.isDeclared("testVariable", false));
    assertTrue(scope.isDeclared("childVariable", true));
    assertTrue(scope.isDeclared("childVariable", false));
    assertFalse(scope.isDeclared("undefinedVariable", true));
    assertFalse(scope.isDeclared("undefinedVariable", false));
  }

  @org.junit.Test
  public void testGetVars() {
    ErrorReporter errorReporter = new ErrorReporter();
    Node rootNode = NodeUtil.createBlock("function test() {" +
        "var testVariable1 = 1;" +
        "var testVariable2 = 2;" +
        "var testVariable3 = 3;" +
        "}");
    Scope scope = createFunctionScope(rootNode, errorReporter);

    Map<String, Var> expectedVars = new ImmutableMap.Builder<String, Var>()
        .put("testVariable1", new Var("", null, null, JSType.NUMBER, scope, 0, null, false, null))
        .put("testVariable2", new Var("", null, null, JSType.NUMBER, scope, 1, null, false, null))
        .put("testVariable3", new Var("", null, null, JSType.NUMBER, scope, 2, null, false, null))
        .build();

    Iterator<Var> vars = scope.getVars().iterator();
    for (Map.Entry<String, Var> entry : expectedVars.entrySet()) {
      Var var = vars.next();
      assertEquals(entry.getValue().getName(), var.getName());
      assertEquals(entry.getValue().getType(), var.getType());
    }
  }
}