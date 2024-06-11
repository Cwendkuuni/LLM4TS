import static org.junit.Assert.*;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceName;

import java.util.Map;

public class ScopeTest {

  @org.junit.Test
  public void testCreateScope() {
    Node rootNode = Node.newFunctionNode(null, null, new SourceName("test.js"), 0);
    Scope scope = new Scope(null, rootNode);
    assertNull(scope.getTypeOfThis());
    assertEquals(0, scope.getDepth());
    assertTrue(scope.isBottom());
  }

  @org.junit.Test
  public void testCreateFunctionScope() {
    Node rootNode = Node.newFunctionNode(null, null, new SourceName("test.js"), 0);
    Scope parentScope = new Scope(null, rootNode);
    Scope scope = new Scope(parentScope, rootNode);
    assertNotNull(scope.getTypeOfThis());
    assertEquals(parentScope.getDepth() + 1, scope.getDepth());
    assertFalse(scope.isBottom());
  }

  @org.junit.Test
  public void testDeclareVariable() {
    Node rootNode = Node.newFunctionNode(null, null, new SourceName("test.js"), 0);
    Scope parentScope = new Scope(null, rootNode);
    Scope scope = new Scope(parentScope, rootNode);

    String variableName = "testVariable";
    Var variable = scope.declare(variableName, Node.newNameNode("testVariable"), Node.INT_TYPE, null);
    assertNotNull(variable);
    assertEquals(variableName, variable.getName());
    assertEquals(scope, variable.getScope());
    assertEquals(variableName, variable.getInputName());
    assertEquals(0, variable.getIndex());
    assertEquals(Node.INT_TYPE, variable.getType());
  }

  @org.junit.Test
  public void testDeclareVariableWithJSDocInfo() {
    Node rootNode = Node.newFunctionNode(null, null, new SourceName("test.js"), 0);
    Scope parentScope = new Scope(null, rootNode);
    Scope scope = new Scope(parentScope, rootNode);

    String variableName = "testVariable";
    JSDocInfo info = NodeUtil.createJSDocInfo("@define testVariable");
    Var variable = scope.declare(variableName, Node.newNameNode("testVariable"), Node.INT_TYPE, null, info);
    assertNotNull(variable);
    assertEquals(variableName, variable.getName());
    assertEquals(scope, variable.getScope());
    assertEquals(variableName, variable.getInputName());
    assertEquals(0, variable.getIndex());
    assertEquals(Node.INT_TYPE, variable.getType());
    assertTrue(variable.isDefine());
  }

  @org.junit.Test
  public void testGetVar() {
    Node rootNode = Node.newFunctionNode(null, null, new SourceName("test.js"), 0);
    Scope parentScope = new Scope(null, rootNode);
    Scope scope = new Scope(parentScope, rootNode);

    String variableName1 = "testVariable1";
    Var variable1 = scope.declare(variableName1, Node.newNameNode(variableName1), Node.INT_TYPE, null);

    String variableName2 = "testVariable2";
    Var variable2 = scope.declare(variableName2, Node.newNameNode(variableName2), Node.STRING_TYPE, null);

    Var variable1Result = scope.getVar(variableName1);
    assertEquals(variable1, variable1Result);

    Var variable2Result = scope.getVar(variableName2);
    assertEquals(variable2, variable2Result);

    Var nullResult = scope.getVar("nonExistentVariable");
    assertNull(nullResult);
  }

  @org.junit.Test
  public void testIsDeclared() {
    Node rootNode = Node.newFunctionNode(null, null, new SourceName("test.js"), 0);
    Scope parentScope = new Scope(null, rootNode);
    Scope scope = new Scope(parentScope, rootNode);

    String variableName1 = "testVariable1";
    Var variable1 = scope.declare(variableName1, Node.newNameNode(variableName1), Node.INT_TYPE, null);

    String variableName2 = "testVariable2";
    Var variable2 = scope.declare(variableName2, Node.newNameNode(variableName2), Node.STRING_TYPE, null);

    assertTrue(scope.isDeclared(variableName1, true));
    assertTrue(scope.isDeclared(variableName1, false));
    assertTrue(scope.isDeclared(variableName2, true));
    assertTrue(scope.isDeclared(variableName2, false));
    assertFalse(scope.isDeclared("nonExistentVariable", true));
    assertFalse(scope.isDeclared("nonExistentVariable", false));

    assertTrue(parentScope.isDeclared(variableName1, false));
    assertFalse(parentScope.isDeclared(variableName1, true));
    assertFalse(parentScope.isDeclared(variableName2, true));
    assertFalse(parentScope.isDeclared(variableName2, false));
  }

  @org.junit.Test
  public void testGetVars() {
    Node rootNode = Node.newFunctionNode(null, null, new SourceName("test.js"), 0);
    Scope parentScope = new Scope(null, rootNode);
    Scope scope = new Scope(parentScope, rootNode);

    String variableName1 = "testVariable1";
    Var variable1 = scope.declare(variableName1, Node.newNameNode(variableName1), Node.INT_TYPE, null);

    String variableName2 = "testVariable2";
    Var variable2 = scope.declare(variableName2, Node.newNameNode(variableName2), Node.STRING_TYPE, null);

    Map<String, Var> vars = scope.getVars();
    assertEquals(2, vars.size());
    Var variable1Result = vars.get(variableName1);
    assertEquals(variable1, variable1Result);
    Var variable2Result = vars.get(variableName2);
    assertEquals(variable2, variable2Result);
  }

  @org.junit.Test
  public void testGetVarCount() {
    Node rootNode = Node.newFunctionNode(null, null, new SourceName("test.js"), 0);
    Scope parentScope = new Scope(null, rootNode);
    Scope scope = new Scope(parentScope, rootNode);

    String variableName1 = "testVariable1";
    Var variable1 = scope.declare(variableName1, Node.newNameNode(variableName1), Node.INT_TYPE, null);

    String variableName2 = "testVariable2";
    Var variable2 = scope.declare(variableName2, Node.newNameNode(variableName2), Node.STRING_TYPE, null);

    assertEquals(2, scope.getVarCount());
  }
}