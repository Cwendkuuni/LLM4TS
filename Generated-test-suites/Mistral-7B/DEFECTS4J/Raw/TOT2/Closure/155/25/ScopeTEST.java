import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createName;
import static com.google.javascript.rhino.NodeFactory.createScope;
import static com.google.javascript.rhino.NodeFactory.createFunction;
import static com.google.javascript.rhino.NodeFactory.createObject;
import static com.google.javascript.rhino.NodeFactory.createStringLiteral;
import static com.google.javascript.rhino.NodeFactory.createIntLiteral;
import static com.google.javascript.rhino.NodeFactory.createGlobalScope;
import static com.google.javascript.rhino.NodeFactory.createVariable;
import static com.google.javascript.rhino.NodeFactory.createIdentifier;
import static com.google.javascript.rhino.NodeFactory.createProperty;
import static com.google.javascript.rhino.NodeFactory.createThis;
import static com.google.javascript.rhino.NodeFactory.createFunctionType;
import static com.google.javascript.rhino.NodeFactory.createObjectType;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;

public class ScopeTest {

  private Scope scope;
  private Scope parentScope;
  private Node rootNode;

  @Before
  public void setUp() {
    parentScope = createGlobalScope();
    rootNode = createFunction(createObject(), null, new Object[] {});
    scope = new Scope(parentScope, rootNode);
  }

  @Test
  public void testDeclareVariable() {
    String name = "x";
    Scope.Var variable = scope.declare(name, createName(name), createIntLiteral(5), null);
    assertNotNull(variable);
    assertEquals(name, variable.getName());
    assertNotNull(variable.getType());
    assertEquals(createIntLiteral(5).getType(), variable.getType().getType());
    assertEquals(scope, variable.getScope());
    assertFalse(parentScope.isDeclared(name, false));
    assertTrue(scope.isDeclared(name, false));
  }

  @Test
  public void testDeclareVariableWithJSDocInfo() {
    String name = "x";
    String jsDocDescription = "Description of variable x.";
    Scope.Var variable = scope.declare(name, createName(name), createIntLiteral(5), null, null, null, null, true,
        createObject(createProperty("name", createStringLiteral(name)), createProperty("description", createStringLiteral(jsDocDescription))));
    assertNotNull(variable);
    assertEquals(name, variable.getName());
    assertNotNull(variable.getType());
    assertEquals(createIntLiteral(5).getType(), variable.getType().getType());
    assertEquals(scope, variable.getScope());
    assertFalse(parentScope.isDeclared(name, false));
    assertTrue(scope.isDeclared(name, false));
    assertNotNull(variable.getJSDocInfo());
    assertEquals(jsDocDescription, variable.getJSDocInfo().getDescription());
  }

  @Test
  public void testDeclareVariableWithParentScope() {
    String name = "x";
    Scope childScope = new Scope(scope, createFunction(createObject(), null, new Object[] {}));
    Scope.Var variable = childScope.declare(name, createName(name), createIntLiteral(5), null);
    assertNotNull(variable);
    assertEquals(name, variable.getName());
    assertNotNull(variable.getType());
    assertEquals(createIntLiteral(5).getType(), variable.getType().getType());
    assertEquals(childScope, variable.getScope());
    assertFalse(scope.isDeclared(name, false));
    assertTrue(childScope.isDeclared(name, false));
    assertFalse(parentScope.isDeclared(name, false));
  }

  @Test(expected = IllegalStateException.class)
  public void testDeclareVariableTwice() {
    String name = "x";
    scope.declare(name, createName(name), createIntLiteral(5), null);
    scope.declare(name, createName(name), createIntLiteral(5), null);
  }

  @Test
  public void testGetVariable() {
    String name = "x";
    Scope.Var variable = scope.declare(name, createName(name), createIntLiteral(5), null);
    Scope.Var getVariable = scope.getVar(name);
    assertNotNull(getVariable);
    assertEquals(variable, getVariable);
  }

  @Test
  public void testGetVariableParentScope() {
    String name = "x";
    Scope childScope = new Scope(scope, createFunction(createObject(), null, new Object[] {}));
    childScope.declare(name, createName(name), createIntLiteral(5), null);
    Scope.Var getVariable = scope.getVar(name);
    assertNull(getVariable);
    Scope.Var getVariableChildScope = childScope.getVar(name);
    assertNotNull(getVariableChildScope);
  }

  @Test
  public void testGetVariables() {
    String name1 = "x";
    String name2 = "y";
    scope.declare(name1, createName(name1), createIntLiteral(5), null);
    scope.declare(name2, createName(name2), createIntLiteral(10), null);
    Iterator<Var> variables = scope.getVars();
    int count = 0;
    while (variables.hasNext()) {
      count++;
      variables.next();
    }
    assertEquals(2, count);
  }

  @Test
  public void testGetVarCount() {
    String name1 = "x";
    String name2 = "y";
    scope.declare(name1, createName(name1), createIntLiteral(5), null);
    scope.declare(name2, createName(name2), createIntLiteral(10), null);
    assertEquals(2, scope.getVarCount());
  }

  @Test
  public void testIsDeclared() {
    String name1 = "x";
    String name2 = "y";
    scope.declare(name1, createName(name1), createIntLiteral(5), null);
    scope.declare(name2, createName(name2), createIntLiteral(10), null);
    assertFalse(parentScope.isDeclared(name1, false));
    assertTrue(scope.isDeclared(name1, false));
    assertFalse(scope.isDeclared(name2, false));
    assertFalse(parentScope.isDeclared(name2, false));
  }

  @Test
  public void testUndelecareVariable() {
    String name = "x";
    Scope.Var variable = scope.declare(name, createName(name), createIntLiteral(5), null);
    scope.undeclare(variable);
    Scope.Var getVariable = scope.getVar(name);
    assertNull(getVariable);
  }

  @Test(expected = IllegalStateException.class)
  public void testUndeclareNonExistentVariable() {
    String name = "x";
    scope.undeclare(null);
  }

  @Test
  public void testGetTypeOfThis() {
    ObjectType thisType = createObjectType(createProperty("foo", createStringLiteral("bar")));
    Scope scope = new Scope(null, createRootNode(), thisType);
    assertEquals(thisType, scope.getTypeOfThis());
  }

  @Test
  public void testGetParentScope() {
    Scope childScope = new Scope(scope, createFunction(createObject(), null, new Object[] {}));
    assertEquals(scope, childScope.getParentScope());
  }

  @Test
  public void testDeclareVariableGlobal() {
    String name = "x";
    Scope globalScope = createGlobalScope();
    Scope.Var variable = globalScope.declare(name, createName(name), createIntLiteral(5), null);
    assertNotNull(variable);
    assertEquals(name, variable.getName());
    assertNotNull(variable.getType());
    assertEquals(createIntLiteral(5).getType(), variable.getType().getType());
    assertEquals(globalScope, variable.getScope());
    assertTrue(globalScope.isDeclared(name, false));
    assertFalse(scope.isDeclared(name, false));
  }

  @Test
  public void testIsLocalScope() {
    assertTrue(scope.isLocal());
    assertFalse(parentScope.isLocal());
  }

  @Test
  public void testIsGlobalScope() {
    assertFalse(scope.isGlobal());
    assertTrue(parentScope.isGlobal());
  }

  @Test
  public void testJoinScopes() {
    String name1 = "x";
    String name2 = "y";
    Map<String, Var> map1 = new LinkedHashMap<String, Var>();
    Map<String, Var> map2 = new LinkedHashMap<String, Var>();
    map1.put(name1, new Var(true, name1, createName(name1), createIntLiteral(5), null, 0, null, false, null));
    map2.put(name2, new Var(true, name2, createName(name2), createIntLiteral(10), null, 0, null, false, null));
    Scope scope1 = new Scope(null, null, null) {
      @Override
      protected Map<String, Var> getVars() {
        return map1;
      }
    };
    Scope scope2 = new Scope(null, null, null) {
      @Override
      protected Map<String, Var> getVars() {
        return map2;
      }
    };
    Scope joinedScope = scope1.join(scope2);
    Iterator<Var> variables = joinedScope.getVars();
    int count = 0;
    while (variables.hasNext()) {
      count++;
      variables.next();
    }
    assertEquals(2, count);
    Scope.Var x = joinedScope.getVar(name1);
    assertNotNull(x);
    assertEquals(name1, x.getName());
    assertEquals(5, x.getIndex());

    Scope.Var y = joinedScope.getVar(name2);
    assertNotNull(y);
    assertEquals(name2, y.getName());
    assertEquals(1, y.getIndex());
  }
}