import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.JSType;

public class ScopeTest {

  @org.junit.Test
  public void testCreateGlobalScope() {
    Scope globalScope = new Scope(null, null);
    assertEquals(JSType.getObjectType(), globalScope.getTypeOfThis());
    assertNull(globalScope.getParent());
    assertTrue(globalScope.isGlobal());
  }

  @org.junit.Test
  public void testCreateLocalScope() {
    Node rootNode = Node.newFunctionNode();
    Scope localScope = new Scope(null, rootNode);
    assertEquals(JSType.getObjectType(), localScope.getTypeOfThis());
    assertNull(localScope.getParent());
    assertFalse(localScope.isGlobal());

    Node variableNode = Node.newNameNode("x");
    localScope.declare("x", variableNode, JSType.INT, null);

    Var x = localScope.getVar("x");
    assertSame(x, localScope.getOwnSlot("x").getSlotValue());
    assertEquals(JSType.INT, x.getType());
  }

  @org.junit.Test
  public void testCreateLocalScopeWithFunction() {
    Node rootNode = Node.newFunctionNode();
    Node functionNode = Node.newFunctionNode();
    Scope localScope = new Scope(null, functionNode);
    Scope parentScope = new Scope(null, rootNode);
    parentScope.setChildScope(localScope);

    assertEquals(JSType.getObjectType(), localScope.getTypeOfThis());
    assertSame(parentScope, localScope.getParent());
    assertFalse(localScope.isGlobal());

    Node variableNode = Node.newNameNode("x");
    localScope.declare("x", variableNode, JSType.STRING, null);

    Var x = localScope.getVar("x");
    assertSame(x, localScope.getOwnSlot("x").getSlotValue());
    assertEquals(JSType.STRING, x.getType());
  }

  @org.junit.Test
  public void testDeclareVariableWithType() {
    Node rootNode = Node.newFunctionNode();
    Scope localScope = new Scope(null, rootNode);

    Node variableNode = Node.newNameNode("x");
    JSType type = JSType.INT;
    localScope.declare("x", variableNode, type, null);

    Var x = localScope.getVar("x");
    assertSame(x, localScope.getOwnSlot("x").getSlotValue());
    assertEquals(type, x.getType());
  }

  @org.junit.Test
  public void testDeclareVariableWithTypeAndInfo() {
    Node rootNode = Node.newFunctionNode();
    Scope localScope = new Scope(null, rootNode);

    Node variableNode = Node.newNameNode("x");
    JSType type = JSType.INT;
    JSDocInfo info = new JSDocInfo();
    localScope.declare("x", variableNode, type, null, info);

    Var x = localScope.getVar("x");
    assertSame(x, localScope.getOwnSlot("x").getSlotValue());
    assertEquals(type, x.getType());
    assertEquals(info, x.getJSDocInfo());
  }

  @org.junit.Test
  public void testUndeclareVariable() {
    Node rootNode = Node.newFunctionNode();
    Scope localScope = new Scope(null, rootNode);

    Node variableNode = Node.newNameNode("x");
    Var x = localScope.declare("x", variableNode, JSType.INT, null);
    localScope.undeclare(x);

    assertNull(localScope.getVar("x"));
  }

  @org.junit.Test
  public void testGetVarInParent() {
    Node rootNode = Node.newFunctionNode();
    Scope parentScope = new Scope(null, rootNode);
    Scope localScope = new Scope(parentScope, Node.newFunctionNode());

    Node variableNode = Node.newNameNode("x");
    Var x = parentScope.declare("x", variableNode, JSType.INT, null);

    assertNull(localScope.getVar("x"));
    assertEquals(x, parentScope.getVar("x"));
  }

  @org.junit.Test
  public void testGetVarWithSameNameButDifferentNode() {
    Node rootNode = Node.newFunctionNode();
    Scope parentScope = new Scope(null, rootNode);
    Scope localScope = new Scope(parentScope, Node.newFunctionNode());

    Node variableNode1 = Node.newNameNode("x");
    Var x1 = parentScope.declare("x", variableNode1, JSType.INT, null);

    Node variableNode2 = Node.newNameNode("x");
    Var x2 = localScope.declare("x", variableNode2, JSType.STRING, null);

    assertSame(x1, parentScope.getVar("x"));
    assertSame(x2, localScope.getVar("x"));
  }

  @org.junit.Test
  public void testGetSlotValue() {
    Node rootNode = Node.newFunctionNode();
    Scope localScope = new Scope(null, rootNode);

    Node variableNode = Node.newNameNode("x");
    Var x = localScope.declare("x", variableNode, JSType.INT, null);

    assertEquals(x, localScope.getSlotValue("x"));
  }

  @org.junit.Test
  public void testGetSlotValueNull() {
    Node rootNode = Node.newFunctionNode();
    Scope localScope = new Scope(null, rootNode);

    assertEquals(null, localScope.getSlotValue("x"));
  }

  @org.junit.Test
  public void testGetSlotValueInParent() {
    Node rootNode = Node.newFunctionNode();
    Scope parentScope = new Scope(null, rootNode);
    Scope localScope = new Scope(parentScope, Node.newFunctionNode());

    Node variableNode = Node.newNameNode("x");
    Var x = parentScope.declare("x", variableNode, JSType.INT, null);

    assertEquals(x, parentScope.getSlotValue("x"));
    assertEquals(null, localScope.getSlotValue("x"));
  }

  @org.junit.Test
  public void testSetType() {
    Node rootNode = Node.newFunctionNode();
    Scope localScope = new Scope(null, rootNode);

    Node variableNode = Node.newNameNode("x");
    Var x = localScope.declare("x", variableNode, null, null);

    x.setType(JSType.STRING);
    assertEquals(JSType.STRING, x.getType());
  }

  @org.junit.Test(expected = IllegalStateException.class)
  public void testSetTypeOnDeclaredVariable() {
    Node rootNode = Node.newFunctionNode();
    Scope localScope = new Scope(null, rootNode);

    Node variableNode = Node.newNameNode("x");
    Var x = localScope.declare("x", variableNode, JSType.INT, null);

    x.setType(JSType.STRING);
  }

  @org.junit.Test
  public void testResolveType() {
    Node rootNode = Node.newFunctionNode();
    Scope localScope = new Scope(null, rootNode);

    Node variableNode = Node.newNameNode("x");
    Var x = localScope.declare("x", variableNode, null, null);

    x.resolveType(null);
    assertEquals(JSType.BOTTOM, x.getType());
  }

  @org.junit.Test
  public void testResolveTypeWithType() {
    Node rootNode = Node.newFunctionNode();
    Node typeNode = Node.newNameNode("MyType");
    Scope localScope = new Scope(null, rootNode);

    Node variableNode = Node.newNameNode("x");
    Var x = localScope.declare("x", variableNode, null, null);

    x.setTypeInferred(false);
    x.resolveType(null, JSType.getNamedType("MyType", null));
    assertEquals(JSType.getNamedType("MyType", null), x.getType());
  }

  @org.junit.Test
  public void testIsTypeInferred() {
    Node rootNode = Node.newFunctionNode();
    Scope localScope = new Scope(null, rootNode);

    Node variableNode = Node.newNameNode("x");
    Var x = localScope.declare("x", variableNode, null, null);

    assertFalse(x.isTypeInferred());

    x.setTypeInferred(true);
    assertTrue(x.isTypeInferred());
  }

  @org.junit.Test
  public void testGetInputName() {
    Node rootNode = Node.newFunctionNode();
    Scope localScope = new Scope(null, rootNode);

    CompilerInput input = new CompilerInput("test.js");
    Node variableNode = Node.newNameNode("x");
    Var x = localScope.declare("x", variableNode, null, input);

    assertEquals("test.js", x.getInputName());
  }

  @org.junit.Test
  public void testGetInputNameNull() {
    Node rootNode = Node.newFunctionNode();
    Scope localScope = new Scope(null, rootNode);

    Node variableNode = Node.newNameNode("x");
    Var x = localScope.declare("x", variableNode, null, null);

    assertEquals(null, x.getInputName());
  }

  @org.junit.Test
  public void testIsDefine() {
    Node rootNode = Node.newFunctionNode();
    Scope localScope = new Scope(null, rootNode);

    Node variableNode = Node.newNameNode("x");
    Var x = localScope.declare("x", variableNode, null, null);

    assertFalse(x.isDefine());

    x.setIsDefine(true);
    assertTrue(x.isDefine());
  }

  @org.junit.Test
  public void testIsDefineFalse() {
    Node rootNode = Node.newFunctionNode();
    Scope localScope = new Scope(null, rootNode);

    Node variableNode = Node.newNameNode("x");
    Var x = localScope.declare("x", variableNode, null, null);

    assertFalse(x.isDefine());

    x.setIsDefine(false);
    assertFalse(x.isDefine());
  }

  @org.junit.Test
  public void testIsConst() {
    Node rootNode = Node.newFunctionNode();
    Scope localScope = new Scope(null, rootNode);

    Node variableNode = Node.newNameNode("x");
    Var x = localScope.declare("x", variableNode, null, null);

    assertFalse(x.isConst());

    x.setIsConst(true);
    assertTrue(x.isConst());
  }

  @org.junit.Test
  public void testIsConstFalse() {
    Node rootNode = Node.newFunctionNode();
    Scope localScope = new Scope(null, rootNode);

    Node variableNode = Node.newNameNode("x");
    Var x = localScope.declare("x", variableNode, null, null);

    assertFalse(x.isConst());

    x.setIsConst(false);
    assertFalse(x.isConst());
  }

  @org.junit.Test
  public void testIsInGlobalScope() {
    Node rootNode = Node.newFunctionNode();
    Scope globalScope = new Scope(null, rootNode);
    Scope localScope = new Scope(globalScope, Node.newFunctionNode());

    assertEquals(globalScope, localScope.getGlobalScope());
    assertTrue(globalScope.isGlobal());
    assertFalse(localScope.isGlobal());
  }

  @org.junit.Test
  public void testGetParent() {
    Node rootNode = Node.newFunctionNode();
    Scope parentScope = new Scope(null, rootNode);
    Scope localScope = new Scope(parentScope, Node.newFunctionNode());

    assertEquals(parentScope, localScope.getParent());
  }

  @org.junit.Test
  public void testGetRootNode() {
    Node rootNode = Node.newFunctionNode();
    Scope localScope = new Scope(null, rootNode);

    assertEquals(rootNode, localScope.getRootNode());
  }

  @org.junit.Test
  public void testJoin() {
    Map<String, JSType> map1 = new LinkedHashMap<>();
    map1.put("x", JSType.INT);
    map1.put("y", JSType.STRING);

    Map<String, JSType> map2 = new LinkedHashMap<>();
    map2.put("x", JSType.STRING);
    map2.put("z", JSType.OBJECT);

    Map<String, JSType> mapResult = Scope.join(map1, map2);

    Map<String, JSType> expected = new LinkedHashMap<>();
    expected.put("x", JSType.getLeastSupertype(JSType.INT, JSType.STRING));
    expected.put("y", JSType.STRING);
    expected.put("z", JSType.OBJECT);

    assertEquals(expected, mapResult);
  }
}