import static org.junit.Assert.*;
import static com.google.javascript.jscomp.Scope.*;
import static com.google.javascript.rhino.NodeFactory.createName;
import static com.google.javascript.rhino.NodeFactory.createVar;
import static com.google.javascript.rhino.NodeFactory.createFunction;
import static com.google.javascript.rhino.NodeFactory.createBlock;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.NodeUtil;

import org.junit.Test;

public class ScopeTest {

  @Test
  public void testCreateScope() {
    Node rootNode = createFunction(null, null, createBlock(null), null);
    Scope scope = new Scope(null, rootNode);
    assertEquals(new ObjectType(JSTypeNative.FUNCTION), scope.getTypeOfThis());
    assertFalse(scope.isBottom());
    assertEquals(0, scope.getDepth());
  }

  @Test
  public void testCreateGlobalScope() {
    Node rootNode = createBlock(null);
    Scope scope = new Scope(rootNode, null);
    assertEquals(new ObjectType(JSTypeNative.GLOBAL_THIS), scope.getTypeOfThis());
    assertTrue(scope.isBottom());
    assertEquals(0, scope.getDepth());
  }

  @Test
  public void testCreateEmptyScope() {
    Node rootNode = createBlock(null);
    Scope scope = new Scope(rootNode, null);
    assertTrue(scope.isBottom());
    assertEquals(0, scope.getDepth());
  }

  @Test
  public void testDeclareVariable() {
    Node rootNode = createFunction(null, null, createBlock(null), null);
    Scope parentScope = new Scope(null, rootNode);
    Scope scope = new Scope(parentScope, createVar("myVar", null, null));

    Var variable = scope.declare("myVar", createName("myVar"), JSType.NUMBER, null);
    assertEquals("myVar", variable.getName());
    assertEquals(JSType.NUMBER, variable.getType());
    assertEquals(NodeUtil.createIntLit(0), variable.getInitialValue());
  }

  @Test
  public void testDeclareVariableWithJSDocInfo() {
    Node rootNode = createFunction(null, null, createBlock(null), null);
    Scope parentScope = new Scope(null, rootNode);
    Scope scope = new Scope(parentScope, createVar("myVar", null, null));

    JSDocInfo info = NodeUtil.createJSDocComment("@param {number} myVar");
    Var variable = scope.declare("myVar", createName("myVar"), JSType.NUMBER, null, info);
    assertEquals("myVar", variable.getName());
    assertEquals(JSType.NUMBER, variable.getType());
    assertEquals(NodeUtil.createIntLit(0), variable.getInitialValue());
    assertEquals(info, variable.getJSDocInfo());
  }

  @Test
  public void testDeclareFunction() {
    Node rootNode = createFunction(null, null, createBlock(null), null);
    Scope parentScope = new Scope(null, rootNode);
    Scope scope = new Scope(parentScope, createVar("myFunction", null, null));

    Node functionNode = createFunction(null, null, createBlock(null), null);
    Var functionVariable = scope.declare("myFunction", createName("myFunction"), FunctionType.get(JSType.VOID, new JSType[0]), null);
    assertEquals("myFunction", functionVariable.getName());
    assertEquals(FunctionType.get(JSType.VOID, new JSType[0]), functionVariable.getType());
    assertEquals(functionNode, functionVariable.getNameNode());
  }

  @Test
  public void testDeclareVariableWithExternInput() {
    Node rootNode = createFunction(null, null, createBlock(null), null);
    Scope parentScope = new Scope(null, rootNode);
    Scope scope = new Scope(parentScope, createVar("myVar", null, null));

    CompilerInput input = NodeUtil.createCompilerInput("test.js", null);
    Var variable = scope.declare("myVar", createName("myVar"), JSType.NUMBER, input, false);
    assertTrue(variable.isExtern());
  }

  @Test
  public void testDeclareVariableTwice() {
    Node rootNode = createFunction(null, null, createBlock(null), null);
    Scope parentScope = new Scope(null, rootNode);
    Scope scope = new Scope(parentScope, createVar("myVar", null, null));

    Var variable1 = scope.declare("myVar", createName("myVar"), JSType.NUMBER, null);
    assertNotNull(variable1);

    try {
      scope.declare("myVar", createName("myVar"), JSType.STRING, null);
      fail("Expected an IllegalStateException");
    } catch (IllegalStateException e) {
      // expected
    }
  }

  @Test
  public void testGetSlot() {
    Node rootNode = createFunction(null, null, createBlock(null), null);
    Scope parentScope = new Scope(null, rootNode);
    Scope scope = new Scope(parentScope, createVar("myVar", null, null));

    Var variable = scope.declare("myVar", createName("myVar"), JSType.NUMBER, null);
    StaticSlot<JSType> slot = scope.getSlot("myVar");
    assertEquals(variable, slot);
    assertEquals(JSType.NUMBER, slot.getValue(null));
  }

  @Test
  public void testGetSlotNotFound() {
    Node rootNode = createFunction(null, null, createBlock(null), null);
    Scope parentScope = new Scope(null, rootNode);
    Scope scope = new Scope(parentScope, createVar("myVar", null, null));

    Var variable = scope.declare("myVar", createName("myVar"), JSType.NUMBER, null);
    StaticSlot<JSType> slot = scope.getSlot("myOtherVar");
    assertNull(slot);
  }

  @Test
  public void testGetOwnSlot() {
    Node rootNode = createFunction(null, null, createBlock(null), null);
    Scope parentScope = new Scope(null, rootNode);
    Scope scope = new Scope(parentScope, createVar("myVar", null, null));

    Var variable = scope.declare("myVar", createName("myVar"), JSType.NUMBER, null);
    StaticSlot<JSType> slot = scope.getOwnSlot("myVar");
    assertEquals(variable, slot);
  }

  @Test
  public void testGetVar() {
    Node rootNode = createFunction(null, null, createBlock(null), null);
    Scope parentScope = new Scope(null, rootNode);
    Scope scope = new Scope(parentScope, createVar("myVar", null, null));

    Var variable = scope.declare("myVar", createName("myVar"), JSType.NUMBER, null);
    Var getVar = scope.getVar("myVar");
    assertEquals(variable, getVar);
  }

  @Test
  public void testGetVarNotFound() {
    Node rootNode = createFunction(null, null, createBlock(null), null);
    Scope parentScope = new Scope(null, rootNode);
    Scope scope = new Scope(parentScope, createVar("myVar", null, null));

    Var variable = scope.declare("myVar", createName("myVar"), JSType.NUMBER, null);
    Var getVar = scope.getVar("myOtherVar");
    assertNull(getVar);
  }

  @Test
  public void testGetVarInParentScope() {
    Node rootNode = createFunction(null, null, createBlock(null), null);
    Scope parentScope = new Scope(null, rootNode);
    Scope parentScope2 = new Scope(parentScope, createVar("myVar", null, null));
    Scope scope = new Scope(parentScope2, createVar("myVar", null, null));

    Var variable = parentScope.declare("myVar", createName("myVar"), JSType.NUMBER, null);
    Var getVar = scope.getVar("myVar");
    assertEquals(variable, getVar);
  }

  @Test
  public void testIsDeclared() {
    Node rootNode = createFunction(null, null, createBlock(null), null);
    Scope parentScope = new Scope(null, rootNode);
    Scope scope = new Scope(parentScope, createVar("myVar", null, null));

    assertTrue(scope.isDeclared("myVar", false));
    assertFalse(scope.isDeclared("myOtherVar", false));
    assertTrue(scope.isDeclared("myVar", true));
    assertTrue(parentScope.isDeclared("myVar", true));
  }

  @Test
  public void testIsDefine() {
    Node rootNode = createFunction(null, null, createBlock(null), null);
    Scope parentScope = new Scope(null, rootNode);
    Scope scope = new Scope(parentScope, createVar("myVar", null, null));

    Var variable = scope.declare("myVar", createName("myVar"), JSType.NUMBER, null);
    assertTrue(variable.isDefine());

    Var variableWithDocInfo = scope.declare("myVar", createName("myVar"), JSType.NUMBER, null, NodeUtil.createJSDocComment("@define myVar"));
    assertTrue(variableWithDocInfo.isDefine());
  }

  @Test
  public void testIsConst() {
    Node rootNode = createFunction(null, null, createBlock(null), null);
    Scope parentScope = new Scope(null, rootNode);
    Scope scope = new Scope(parentScope, createVar("myConst", null, null, NodeUtil.createConstName("myConst")));

    Var variable = scope.declare("myConst", createName("myConst"), JSType.NUMBER, null);
    assertTrue(variable.isConst());
  }

  @Test
  public void testIsBleedingFunction() {
    Node rootNode = createFunction(null, null, createVar("myFunction", null, null), createBlock(null));
    Scope parentScope = new Scope(null, rootNode);
    Scope scope = new Scope(parentScope, createVar("myVar", null, null, createVar("myFunction", null, null)));

    Var functionVariable = scope.declare("myFunction", createName("myFunction"), FunctionType.get(JSType.VOID, new JSType[0]), null);
    assertTrue(functionVariable.isBleedingFunction());
  }

  @Test
  public void testGetInitialValue() {
    Node rootNode = createFunction(null, null, createBlock(null), null);
    Scope parentScope = new Scope(null, rootNode);
    Scope scope = new Scope(parentScope, createVar("myVar", null, null));

    Node initialValueNode = createIntLit(10);
    scope.declare("myVar", createName("myVar"), JSType.NUMBER, null, null, null, null, false, null, initialValueNode);

    Node getValue = scope.getVar("myVar").getInitialValue();
    assertEquals(initialValueNode, getValue);
  }

  @Test
  public void testGetTypeOfThisInFunctionScope() {
    Node rootNode = createFunction(null, null, createBlock(null), null);
    Scope globalScope = new Scope(null, rootNode);
    Scope functionScope = new Scope(globalScope, createVar("myFunction", null, null));

    assertEquals(FunctionType.get(JSType.VOID, new JSType[0]), functionScope.getTypeOfThis());
  }

  @Test
  public void testGetTypeOfThisInGlobalScope() {
    Node rootNode = createBlock(null);
    Scope globalScope = new Scope(rootNode, null);

    assertEquals(JSTypeNative.GLOBAL_THIS, globalScope.getTypeOfThis());
  }

  @Test
  public void testJoinMaps() {
    Map<String, JSType> map1 = Maps.newHashMap();
    map1.put("a", JSType.NUMBER);
    map1.put("b", JSType.STRING);

    Map<String, JSType> map2 = Maps.newHashMap();
    map2.put("b", JSType.NUMBER);

    Map<String, JSType> joinedMap = map1.join(map2);

    assertEquals(JSType.NUMBER, joinedMap.get("b"));
  }
}