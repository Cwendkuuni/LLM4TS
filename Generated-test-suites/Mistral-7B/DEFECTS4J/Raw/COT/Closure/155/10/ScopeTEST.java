import static org.junit.Assert.*;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;

public class ScopeTest {

  @Test
  public void testScopeConstructor() {
    Scope parent = new Scope(null, null);
    Scope scope = new Scope(parent, null);

    assertEquals(parent, scope.getParent());
  }

  @Test
  public void testGlobalScopeConstructor() {
    Scope scope = new Scope(null, null);
    assertNull(scope.getParent());
  }

  @Test
  public void testEmptyScopeConstructor() {
    Scope scope = new Scope(null, null);

    assertTrue(scope.isBottom());
  }

  @Test
  public void testDeclareVariable() {
    Scope parent = new Scope(null, null);
    Scope scope = new Scope(parent, null);

    String name = "testVariable";
    Node nameNode = new Node(Token.NAME, "testVariable");
    JSType type = new ObjectType("Object");
    CompilerInput input = new CompilerInput("testFile.js");

    Var var = scope.declare(name, nameNode, type, input);

    Var expectedVar = new Var(true, name, nameNode, type, scope, 0, input, false, null);

    assertEquals(expectedVar, var);
    assertEquals(scope.getVars().size(), 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDeclareVariableNullParent() {
    Node rootNode = new Node(Token.BLOCK, null);
    Scope scope = new Scope(null, rootNode);

    String name = "testVariable";
    Node nameNode = new Node(Token.NAME, "testVariable");
    JSType type = new ObjectType("Object");
    CompilerInput input = new CompilerInput("testFile.js");

    scope.declare(name, nameNode, type, input);
  }

  @Test
  public void testGetVar() {
    Scope parent = new Scope(null, null);
    Scope scope = new Scope(parent, null);

    String name = "testVariable";
    Node nameNode = new Node(Token.NAME, "testVariable");
    JSType type = new ObjectType("Object");
    CompilerInput input = new CompilerInput("testFile.js");

    Var var = scope.declare(name, nameNode, type, input);

    Var expectedVar = new Var(true, name, nameNode, type, scope, 0, input, false, null);

    assertEquals(expectedVar, scope.getVar(name));
  }

  @Test
  public void testGetVarNull() {
    Scope scope = new Scope(null, null);

    assertEquals(null, scope.getVar("testVariable"));
  }

  @Test
  public void testIsDeclared() {
    Scope parent = new Scope(null, null);
    Scope scope = new Scope(parent, null);

    String name = "testVariable";
    Node nameNode = new Node(Token.NAME, "testVariable");
    JSType type = new ObjectType("Object");
    CompilerInput input = new CompilerInput("testFile.js");

    scope.declare(name, nameNode, type, input);

    assertTrue(scope.isDeclared(name, true));
    assertFalse(scope.isDeclared("otherVariable", true));
    assertFalse(scope.isDeclared(name, false));
  }

  @Test
  public void testGetVars() {
    Scope parent = new Scope(null, null);
    Scope scope = new Scope(parent, null);

    String name1 = "testVariable1";
    Node nameNode1 = new Node(Token.NAME, "testVariable1");
    JSType type1 = new ObjectType("Object");
    CompilerInput input1 = new CompilerInput("testFile1.js");

    String name2 = "testVariable2";
    Node nameNode2 = new Node(Token.NAME, "testVariable2");
    JSType type2 = new ObjectType("Object");
    CompilerInput input2 = new CompilerInput("testFile2.js");

    scope.declare(name1, nameNode1, type1, input1);
    scope.declare(name2, nameNode2, type2, input2);

    int expectedSize = 2;

    Iterator<Var> varsIterator = scope.getVars();

    int counter = 0;
    while (varsIterator.hasNext()) {
      Var var = varsIterator.next();
      assertEquals(expectedSize, ++counter);
    }
  }

  @Test
  public void testGetVarCount() {
    Scope parent = new Scope(null, null);
    Scope scope = new Scope(parent, null);

    String name1 = "testVariable1";
    Node nameNode1 = new Node(Token.NAME, "testVariable1");
    JSType type1 = new ObjectType("Object");
    CompilerInput input1 = new CompilerInput("testFile1.js");

    String name2 = "testVariable2";
    Node nameNode2 = new Node(Token.NAME, "testVariable2");
    JSType type2 = new ObjectType("Object");
    CompilerInput input2 = new CompilerInput("testFile2.js");

    scope.declare(name1, nameNode1, type1, input1);
    scope.declare(name2, nameNode2, type2, input2);

    int expectedSize = 2;

    assertEquals(expectedSize, scope.getVarCount());
  }

  @Test
  public void testIsLocal() {
    Scope globalScope = new Scope(null, null);
    Scope localScope = new Scope(globalScope, null);

    assertTrue(globalScope.isGlobal());
    assertFalse(localScope.isGlobal());
  }

  @Test
  public void testIsBottom() {
    Scope emptyScope = new Scope(null, null);

    assertTrue(emptyScope.isBottom());
  }

  @Test
  public void testGetRootNode() {
    Scope parent = new Scope(null, null);
    Scope scope = new Scope(parent, new Node(Token.BLOCK, null));

    Node expectedRootNode = new Node(Token.BLOCK, null);

    assertEquals(expectedRootNode, scope.getRootNode());
  }

  @Test
  public void testGetParent() {
    Scope parent = new Scope(null, null);
    Scope scope = new Scope(parent, null);

    assertEquals(parent, scope.getParent());
  }

  @Test
  public void testGetParentScope() {
    Scope parent = new Scope(null, null);
    Scope scope = new Scope(parent, null);

    assertEquals(parent, scope.getParentScope());
  }

  @Test
  public void testgetTypeOfThis() {
    Scope parent = new Scope(null, null);
    Scope scope = new Scope(parent, new Node(Token.FUNCTION, null));

    ObjectType expectedThisType = new ObjectType("Object");

    assertEquals(expectedThisType, scope.getTypeOfThis());
  }

  @Test
  public void testJoin() {
    Scope parent = new Scope(null, null);
    Scope child1 = new Scope(parent, null);
    Scope child2 = new Scope(child1, null);

    Map<String, Var> parentVars = new LinkedHashMap<>();
    parentVars.put("parentVar1", new Var(true, "parentVar1", null, new ObjectType("Object"), parent, 0, null, false, null));

    Map<String, Var> child1Vars = new LinkedHashMap<>();
    child1Vars.put("childVar1", new Var(true, "childVar1", null, new ObjectType("Object"), child1, 0, null, false, null));

    Map<String, Var> child2Vars = new LinkedHashMap<>();
    child2Vars.put("childVar2", new Var(true, "childVar2", null, new ObjectType("Object"), child2, 0, null, false, null));

    parent.vars = parentVars;
    child1.vars = child1Vars;
    child2.vars = child2Vars;

    Map<String, Var> expectedVars = new LinkedHashMap<>();
    expectedVars.put("parentVar1", new Var(true, "parentVar1", null, new ObjectType("Object"), child2, 0, null, false, null));
    expectedVars.put("childVar1", new Var(true, "childVar1", null, new ObjectType("Object"), child2, 0, null, false, null));
    expectedVars.put("childVar2", new Var(true, "childVar2", null, new ObjectType("Object"), child2, 0, null, false, null));

    Map<String, Var> joinedVars = child2.join(child1.vars);

    assertEquals(expectedVars, joinedVars);
  }

  @Test
  public void testJoinWithBottomScope() {
    Scope parent = new Scope(null, null);
    Scope bottomScope = new Scope(null, null);
    Scope child = new Scope(parent, null);

    Map<String, Var> parentVars = new LinkedHashMap<>();
    parentVars.put("parentVar1", new Var(true, "parentVar1", null, new ObjectType("Object"), parent, 0, null, false, null));

    Map<String, Var> bottomVars = new LinkedHashMap<>();

    parent.vars = parentVars;
    bottomScope.vars = bottomVars;
    child.vars = bottomVars;

    Map<String, Var> expectedVars = new LinkedHashMap<>();
    expectedVars.put("parentVar1", new Var(true, "parentVar1", null, new ObjectType("Object"), child, 0, null, false, null));

    Map<String, Var> joinedVars = child.join(parent.vars);

    assertEquals(expectedVars, joinedVars);
  }

  @Test
  public void testJoinWithDifferentTypes() {
    Scope parent = new Scope(null, null);
    Scope child1 = new Scope(parent, null);
    Scope child2 = new Scope(child1, null);

    Map<String, Var> parentVars = new LinkedHashMap<>();
    parentVars.put("parentVar1", new Var(true, "parentVar1", null, new ObjectType("Object"), parent, 0, null, false, null));
    parentVars.put("parentVar2", new Var(true, "parentVar2", null, new ObjectType("Number"), parent, 0, null, false, null));

    Map<String, Var> child1Vars = new LinkedHashMap<>();
    child1Vars.put("childVar1", new Var(true, "childVar1", null, new ObjectType("String"), child1, 0, null, false, null));

    Map<String, Var> child2Vars = new LinkedHashMap<>();
    child2Vars.put("childVar2", new Var(true, "childVar2", null, new ObjectType("Number"), child2, 0, null, false, null));

    parent.vars = parentVars;
    child1.vars = child1Vars;
    child2.vars.putAll(child1Vars);
    child2.vars.put("childVar2", new Var(true, "childVar2", null, new ObjectType("Number"), child2, 0, null, false, null));

    Map<String, Var> expectedVars = new LinkedHashMap<>();
    expectedVars.put("parentVar1", new Var(true, "parentVar1", null, new ObjectType("Object"), child2, 0, null, false, null));
    expectedVars.put("parentVar2", new Var(true, "parentVar2", null, new ObjectType("Number"), child2, 0, null, false, null));
    expectedVars.put("childVar1", new Var(true, "childVar1", null, new ObjectType("String"), child2, 0, null, false, null));
    expectedVars.put("childVar2", new Var(true, "childVar2", null, new ObjectType("Number"), child2, 0, null, false, null));

    Map<String, Var> joinedVars = child2.join(child1.vars);

    assertEquals(expectedVars, joinedVars);
  }

  @Test
  public void testJoinWithSameTypes() {
    Scope parent = new Scope(null, null);
    Scope child1 = new Scope(parent, null);
    Scope child2 = new Scope(child1, null);

    Map<String, Var> parentVars = new LinkedHashMap<>();
    parentVars.put("parentVar1", new Var(true, "parentVar1", null, new ObjectType("Object"), parent, 0, null, false, null));
    parentVars.put("parentVar2", new Var(true, "parentVar2", null, new ObjectType("Object"), parent, 0, null, false, null));

    Map<String, Var> child1Vars = new LinkedHashMap<>();
    child1Vars.put("childVar1", new Var(true, "childVar1", null, new ObjectType("Object"), child1, 0, null, false, null));

    Map<String, Var> child2Vars = new LinkedHashMap<>();
    child2Vars.putAll(child1Vars);

    parent.vars = parentVars;
    child1.vars = child1Vars;
    child2.vars = child2Vars;

    Map<String, Var> expectedVars = new LinkedHashMap<>();
    expectedVars.put("parentVar1", new Var(true, "parentVar1", null, new ObjectType("Object"), child2, 0, null, false, null));
    expectedVars.put("parentVar2", new Var(true, "parentVar2", null, new ObjectType("Object"), child2, 0, null, false, null));
    expectedVars.put("childVar1", new Var(true, "childVar1", null, new ObjectType("Object"), child2, 0, null, false, null));

    Map<String, Var> joinedVars = child2.join(child1.vars);

    assertEquals(expectedVars, joinedVars);
  }
}