package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class InlineObjectLiteralsTest {

  private Compiler compiler;
  private InlineObjectLiterals inlineObjectLiterals;
  private Supplier<String> safeNameIdSupplier;

  @Before
  public void setup() {
    compiler = new Compiler();
    inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> "test_var");
  }

  @Test
  public void testSimpleObjectLiteralInline() {
    Node externs = Node.objectLiteral();
    Node root = Node.script(Lists.newArrayList(
        Node.var("obj", Node.objectLiteral()),
        Node.assign(Node.name("obj.a"), Node.number(1))
    ));

    inlineObjectLiterals.process(externs, root);
    Scope scope = root.getScope();

    // Check the generated variable declarations
    Iterator<Var> varIt = scope.getVars();
    Var objVar = varIt.next();
    Var aVar = varIt.next();

    assertEquals("JSCompiler_object_inline_a_test_var", aVar.name);
    assertEquals("JSCompiler_object_inline_obj_test_var", objVar.name);

    // Check the inlined object literal
    Node objNode = scope.getVar("obj").getValue();
    Node aNode = objNode.getFirstChild();
    assertEquals("a", aNode.getString());
    assertEquals(Node.number(1), aNode.getNext());

    // Check that the original object literal reference was replaced
    Node objRef = root.getFirstChild();
    assertNull(objRef.getFirstChild());
  }

  @Test
  public void testComplexObjectLiteralInline() {
    Node externs = Node.objectLiteral();
    Node root = Node.script(Lists.newArrayList(
        Node.var("obj", Node.objectLiteral()),
        Node.assign(Node.name("obj.a.b.c"), Node.number(1))
    ));

    inlineObjectLiterals.process(externs, root);
    Scope scope = root.getScope();

    // Check the generated variable declarations
    Iterator<Var> varIt = scope.getVars();
    Var objVar = varIt.next();
    Var aVar = varIt.next();
    Var bVar = varIt.next();
    Var cVar = varIt.next();

    assertEquals("JSCompiler_object_inline_c_test_var", cVar.name);
    assertEquals("JSCompiler_object_inline_b_test_var", bVar.name);
    assertEquals("JSCompiler_object_inline_a_test_var", aVar.name);
    assertEquals("JSCompiler_object_inline_obj_test_var", objVar.name);

    // Check the inlined object literal
    Node objNode = scope.getVar("obj").getValue();
    Node aNode = objNode.getFirstChild();
    Node bNode = aNode.getNext();
    Node cNode = bNode.getNext();

    assertEquals("b", bNode.getString());
    assertEquals("c", cNode.getString());
    assertEquals(Node.number(1), cNode.getNext());

    // Check that the original object literal reference was replaced
    Node objRef = root.getFirstChild();
    assertNull(objRef.getFirstChild());
  }

  @Test(expected = AssertionError.class)
  public void testSelfReferentialObjectLiteral() {
    Node externs = Node.objectLiteral();
    Node root = Node.script(Lists.newArrayList(
        Node.var("obj", Node.objectLiteral()),
        Node.assign(Node.name("obj.a.obj.b"), Node.number(1))
    ));

    inlineObjectLiterals.process(externs, root);
  }

  @Test
  public void testObjectLiteralWithGetter() {
    Node externs = Node.objectLiteral();
    Node root = Node.script(Lists.newArrayList(
        Node.var("obj", Node.objectLiteral()),
        Node.assign(Node.name("obj.getter"), Node.objectLiteral()),
        Node.assign(Node.name("obj.getter.a"), Node.number(1))
    ));

    inlineObjectLiterals.process(externs, root);
    Scope scope = root.getScope();

    // Check the generated variable declarations
    Iterator<Var> varIt = scope.getVars();
    Var objVar = varIt.next();
    Var getterVar = varIt.next();
    Var aVar = varIt.next();

    assertEquals("JSCompiler_object_inline_a_test_var", aVar.name);
    assertEquals("JSCompiler_object_inline_getter_test_var", getterVar.name);
    assertEquals("JSCompiler_object_inline_obj_test_var", objVar.name);

    // Check the inlined object literal
    Node objNode = scope.getVar("obj").getValue();
    Node getterNode = objNode.getFirstChild();
    Node aNode = getterNode.getNext();

    assertEquals("a", aNode.getString());
    assertEquals(Node.number(1), aNode.getNext());

    // Check that the original object literal reference was replaced
    Node objRef = root.getFirstChild();
    assertNull(objRef.getFirstChild());

    // Check that the getter property was replaced with a regular property
    Node getterProp = getterNode.getFirstChild();
    assertEquals("getter", getterProp.getString());
    Node aProp = getterProp.getNext();
    assertEquals("a", aProp.getString());
  }
}