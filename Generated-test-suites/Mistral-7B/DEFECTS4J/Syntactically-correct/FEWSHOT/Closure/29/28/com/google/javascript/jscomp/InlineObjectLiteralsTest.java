package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InlineObjectLiteralsTest {

  private final AbstractCompiler compiler = new AbstractCompiler() {};
  private final Supplier<String> safeNameIdSupplier = () -> "test_id";

  @Test
  public void testInlineObjectLiterals() {
    Node root = Node.createJSArray();
    Var v = NodeUtil.newLocal("obj", root);
    Node obj = Node.newObjectLiteral();
    Node prop = Node.newName("prop");
    Node val = Node.newNumberLiteral(1);
    obj.addChildToEnd(NodeUtil.newPropertyName(prop));
    obj.addChildToEnd(val);

    Node assign = Node.newAssign(v, obj);
    root.addChildToEnd(assign);

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);
    inlineObjectLiterals.process(null, root);

    assertTrue(obj.isRemoved());
    assertFalse(val.isRemoved());

    Iterator<Var> it = v.getScope().getVars();
    Var newVar = null;
    while (it.hasNext()) {
      Var next = it.next();
      if (next.name.equals("JSCompiler_object_inline_prop_0")) {
        newVar = next;
        break;
      }
    }

    Preconditions.checkState(newVar != null);
    Node newVal = newVar.getInitialValue();
    assertEquals(Node.newNumberLiteral(1), newVal);
  }

  @Test
  public void testInlineObjectLiteralsWithInitialValues() {
    Node root = Node.createJSArray();
    Var v = NodeUtil.newLocal("obj", root);
    Node obj = Node.newObjectLiteral();
    Node prop = Node.newName("prop");
    Node val = Node.newNumberLiteral(1);
    Node initVal = Node.newNumberLiteral(2);

    obj.addChildToEnd(NodeUtil.newPropertyName(prop));
    obj.addChildToEnd(val);
    Node init = Node.newObjectInitializer(obj);
    obj.setInitialValue(init);

    Node assign = Node.newAssign(v, obj);
    root.addChildToEnd(assign);

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);
    inlineObjectLiterals.process(null, root);

    assertTrue(obj.isRemoved());
    assertFalse(val.isRemoved());
    assertFalse(init.isRemoved());

    Iterator<Var> it = v.getScope().getVars();
    Var newVar = null;
    Var newInitVar = null;
    while (it.hasNext()) {
      Var next = it.next();
      if (next.name.equals("JSCompiler_object_inline_prop_0")) {
        newVar = next;
      } else if (next.name.startsWith("JSCompiler_object_inline_init_")) {
        newInitVar = next;
      }
    }

    Preconditions.checkState(newVar != null);
    Node newVal = newVar.getInitialValue();
    Preconditions.checkState(newInitVar != null);
    Node newInitVal = newInitVar.getInitialValue();

    assertEquals(Node.newNumberLiteral(1), newVal);
    assertEquals(Node.newNumberLiteral(2), newInitVal);
  }

  @Test
  public void testInlineObjectLiteralsWithSelfReference() {
    Node root = Node.createJSFunction();

    Var obj = NodeUtil.newLocal("obj", root);
    Node objAssign = Node.newAssign(obj, Node.newObjectLiteral());
    root.addChildToEnd(objAssign);

    Var prop = NodeUtil.newLocal("prop", root);
    Node propAssign = Node.newAssign(NodeUtil.newName(obj.getName() + ".prop"), Node.newName(prop.getName()));
    root.addChildToEnd(propAssign);

    Var ref = NodeUtil.newLocal("ref", root);
    Node refAssign = Node.newAssign(ref, NodeUtil.newName(prop.getName()));
    root.addChildToEnd(refAssign);

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);
    inlineObjectLiterals.process(null, root);

    assertTrue(obj.isRemoved());
    assertFalse(prop.isRemoved());
    assertFalse(ref.isRemoved());

    Node objLiteral = NodeUtil.getFirstChildOfType(obj, Node.OBJECT_LITERAL);
    Node propNode = NodeUtil.getFirstChildOfType(prop, Node.GET_PROP);
    Node refNode = NodeUtil.getFirstChildOfType(ref, Node.NAME);

    assertEquals(objLiteral, propNode.getFirstChild());
    assertEquals(refNode, propNode.getLastChild());
  }

  @Test
  public void testInlineObjectLiteralsWithCyclicReference() {
    Node root = Node.createJSFunction();

    Var obj1 = NodeUtil.newLocal("obj1", root);
    Var obj2 = NodeUtil.newLocal("obj2", root);
    Node obj1Assign = Node.newAssign(obj1, Node.newObjectLiteral());
    root.addChildToEnd(obj1Assign);

    Node obj2Assign = Node.newAssign(obj2, Node.newObjectLiteral());
    Node obj1Prop = NodeUtil.newPropertyName("obj2");
    Node obj2Prop = NodeUtil.newPropertyName("obj1");
    Node obj1Val = Node.newName(obj2.getName());
    Node obj2Val = Node.newName(obj1.getName());
    Node obj1Obj = Node.newObjectLiteral();
    Node obj1ObjProp = NodeUtil.newPropertyName(obj2Prop);
    Node obj1ObjVal = obj1Val;
    Node obj2Obj = Node.newObjectLiteral();
    Node obj2ObjProp = NodeUtil.newPropertyName(obj1Prop);
    Node obj2ObjVal = obj2Val;

    obj1Obj.addChildToEnd(obj1ObjProp);
    obj1Obj.addChildToEnd(obj1ObjVal);
    obj2Obj.addChildToEnd(obj2ObjProp);
    obj2Obj.addChildToEnd(obj2ObjVal);

    obj1ObjVal.setParent(obj1ObjProp);
    obj2ObjVal.setParent(obj2ObjProp);

    obj1Assign.addChildToEnd(obj1Obj);
    obj2Assign.addChildToEnd(obj2Obj);

    Node prop1 = NodeUtil.newLocal("prop1", root);
    Node prop1Assign = Node.newAssign(NodeUtil.newName(obj1.getName() + ".obj2.obj1.prop1"), Node.newName(prop1.getName()));
    root.addChildToEnd(prop1Assign);

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);
    inlineObjectLiterals.process(null, root);

    assertTrue(obj1.isRemoved());
    assertTrue(obj2.isRemoved());
    assertFalse(prop1.isRemoved());

    Node obj1Literal = NodeUtil.getFirstChildOfType(obj1, Node.OBJECT_LITERAL);
    Node obj2Literal = NodeUtil.getFirstChildOfType(obj2, Node.OBJECT_LITERAL);
    Node obj1Obj = NodeUtil.getFirstChildOfType(obj1Literal, Node.OBJECT);
    Node obj2Obj = NodeUtil.getFirstChildOfType(obj2Literal, Node.OBJECT);

    assertNull(obj1Obj);
    assertNull(obj2Obj);
  }

  @Test
  public void testInlineObjectLiteralsWithMultipleAssignments() {
    Node root = Node.createJSFunction();

    Var obj = NodeUtil.newLocal("obj", root);
    Node objAssign = Node.newAssign(obj, Node.newObjectLiteral());
    root.addChildToEnd(objAssign);

    Var prop1 = NodeUtil.newLocal("prop1", root);
    Node prop1Assign1 = Node.newAssign(NodeUtil.newName(obj.getName() + ".prop1"), Node.newNumberLiteral(1));
    root.addChildToEnd(prop1Assign1);

    Var prop2 = NodeUtil.newLocal("prop2", root);
    Node prop1Assign2 = Node.newAssign(NodeUtil.newName(obj.getName() + ".prop1"), Node.newNumberLiteral(2));
    root.addChildToEnd(prop1Assign2);

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);
    inlineObjectLiterals.process(null, root);

    assertTrue(obj.isRemoved());

    Node objLiteral = NodeUtil.getFirstChildOfType(obj, Node.OBJECT_LITERAL);
    Node prop1Node1 = NodeUtil.getFirstChildOfType(prop1, Node.GET_PROP);
    Node prop1Node2 = NodeUtil.getFirstChildOfType(prop2, Node.GET_PROP);

    assertEquals(objLiteral, prop1Node1.getFirstChild());
    assertEquals(objLiteral, prop1Node2.getFirstChild());

    Node prop1Obj1 = NodeUtil.getFirstChildOfType(prop1Node1, Node.OBJECT);
    Node prop1Obj2 = NodeUtil.getFirstChildOfType(prop1Node2, Node.OBJECT);

    assertNull(prop1Obj1);
    assertNull(prop1Obj2);
  }

  @Test
  public void testInlineObjectLiteralsWithNestedObjectLiterals() {
    Node root = Node.createJSFunction();

    Var obj = NodeUtil.newLocal("obj", root);
    Node objAssign = Node.newAssign(obj, Node.newObjectLiteral());
    root.addChildToEnd(objAssign);

    Var nestedObj = NodeUtil.newLocal("nestedObj", root);
    Node nestedObjAssign = Node.newAssign(NodeUtil.newName(obj.getName() + ".nestedObj"), Node.newObjectLiteral());
    root.addChildToEnd(nestedObjAssign);

    Var nestedProp = NodeUtil.newLocal("nestedProp", root);
    Node nestedPropAssign = Node.newAssign(NodeUtil.newName(obj.getName() + ".nestedObj.nestedProp"), Node.newNumberLiteral(1));
    root.addChildToEnd(nestedPropAssign);

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);
    inlineObjectLiterals.process(null, root);

    assertTrue(obj.isRemoved());

    Node objLiteral = NodeUtil.getFirstChildOfType(obj, Node.OBJECT_LITERAL);
    Node nestedObjLiteral = NodeUtil.getFirstChildOfType(nestedObj, Node.OBJECT_LITERAL);
    Node nestedPropNode = NodeUtil.getFirstChildOfType(nestedProp, Node.GET_PROP);

    assertEquals(objLiteral, nestedObjLiteral.getFirstChild());

    Node nestedObjObj = NodeUtil.getFirstChildOfType(nestedObjLiteral, Node.OBJECT);
    Node nestedPropObj = NodeUtil.getFirstChildOfType(nestedPropNode, Node.OBJECT);

    assertNull(nestedObjObj);
    assertNull(nestedPropObj);
  }

  @Test
  public void testInlineObjectLiteralsWithNestedArrays() {
    Node root = Node.createJSFunction();

    Var obj = NodeUtil.newLocal("obj", root);
    Node objAssign = Node.newAssign(obj, Node.newObjectLiteral());
    root.addChildToEnd(objAssign);

    Var nestedArray = NodeUtil.newLocal("nestedArray", root);
    Node nestedArrayAssign = Node.newAssign(NodeUtil.newName(obj.getName() + ".nestedArray"), Node.newJSArray());
    root.addChildToEnd(nestedArrayAssign);

    Var nestedArrayItem = NodeUtil.newLocal("nestedArrayItem", root);
    Node nestedArrayItemAssign = Node.newAssign(NodeUtil.newName(NodeUtil.getFirstChildOfType(nestedArray, Node.JSARRAY).getLastChild()), Node.newNumberLiteral(1));
    root.addChildToEnd(nestedArrayItemAssign);

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);
    inlineObjectLiterals.process(null, root);

    assertTrue(obj.isRemoved());

    Node objLiteral = NodeUtil.getFirstChildOfType(obj, Node.OBJECT_LITERAL);
    Node nestedArrayLiteral = NodeUtil.getFirstChildOfType(nestedArray, Node.JSARRAY);
    Node nestedArrayItemNode = NodeUtil.getFirstChildOfType(nestedArrayItem, Node.ASSIGN);

    assertEquals(objLiteral, nestedArrayLiteral.getFirstChild());

    Node nestedArrayObj = NodeUtil.getFirstChildOfType(NodeUtil.getFirstChildOfType(nestedArrayLiteral, Node.JSARRAY), Node.OBJECT);
    Node nestedArrayItemObj = NodeUtil.getFirstChildOfType(nestedArrayItemNode, Node.OBJECT);

    assertNull(nestedArrayObj);
    assertNull(nestedArrayItemObj);
  }

  private void assertEquals(Node expected, Node actual) {
    String expectedText = IR.toString(expected, null);
    String actualText = IR.toString(actual, null);

    assertEquals(expectedText, actualText);
  }
}