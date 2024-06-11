package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    // Given a valid input
    Node externs = Node.object();
    Node root = Node.object();

    AbstractCompiler compiler = new AbstractCompiler() {};
    Supplier<String> safeNameIdSupplier = () -> "test";
    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);

    inlineObjectLiterals.process(externs, root);

    // Then the output should have the expected structure
    assertNotNull(root);
    assertEquals(1, root.getNumChildren());
    Node obj = root.getFirstChild();
    assertEquals(4, obj.getNumChildren());

    // And the variables should have been inlined
    Map<String, Node> expectedVariables = Maps.newLinkedHashMap();
    expectedVariables.put("a", Node.number(1));
    expectedVariables.put("b", Node.string("foo"));
    expectedVariables.put("c", Node.string("bar"));

    Map<String, Node> variables = Maps.newHashMap();
    for (Node child = obj.getFirstChild(); child != null; child = child.getNext()) {
      String key = child.getString();
      Node variable = child.getFirstChild();
      variables.put(key, variable);
    }

    assertEquals(3, variables.size());
    for (Map.Entry<String, Node> entry : expectedVariables.entrySet()) {
      Node variable = variables.get(entry.getKey());
      assertEquals(entry.getValue(), variable);
    }
  }

  @Test
  public void testInlineObjectLiteralsWithSelfReference() {
    // Given an input with a self-referential object literal
    Node externs = Node.object();
    Node root = Node.object();

    AbstractCompiler compiler = new AbstractCompiler() {};
    Supplier<String> safeNameIdSupplier = () -> "test";
    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);

    root.setName("selfRef");
    root.setType(Token.OBJECTLIT);
    root.addChild(Node.string("a").setName("a"));
    root.addChild(Node.object());
    Node obj = root.getLastChild();
    obj.setName("obj");
    obj.setType(Token.OBJECTLIT);
    obj.addChild(Node.string("b").setName("b"));
    obj.addChild(Node.getPropAccess(Node.object(), "a", null, null));

    inlineObjectLiterals.process(externs, root);

    // Then the output should not have the expected structure, as the
    // self-referential object literal cannot be inlined
    assertNotNull(root);
    assertEquals(1, root.getNumChildren());
    Node objInOutput = root.getFirstChild();
    assertEquals(2, objInOutput.getNumChildren());

    Map<String, Node> variables = Maps.newHashMap();
    for (Node child = objInOutput.getFirstChild(); child != null; child = child.getNext()) {
      String key = child.getString();
      Node variable = child.getFirstChild();
      variables.put(key, variable);
    }

    assertEquals(2, variables.size());
    assertNotNull(variables.get("a"));
    assertNull(variables.get("b"));
  }

  @Test
  public void testInlineObjectLiteralsWithNestedObjectLiterals() {
    // Given an input with nested object literals
    Node externs = Node.object();
    Node root = Node.object();

    AbstractCompiler compiler = new AbstractCompiler() {};
    Supplier<String> safeNameIdSupplier = () -> "test";
    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);

    root.setName("root");
    root.setType(Token.OBJECTLIT);
    root.addChild(Node.object());
    Node nestedObj = root.getFirstChild().getFirstChild();
    nestedObj.setName("nestedObj");
    nestedObj.setType(Token.OBJECTLIT);
    nestedObj.addChild(Node.string("a").setName("a"));
    nestedObj.addChild(Node.object());
    Node nestedObj2 = nestedObj.getLastChild();
    nestedObj2.setName("nestedObj2");
    nestedObj2.setType(Token.OBJECTLIT);
    nestedObj2.addChild(Node.string("b").setName("b"));

    inlineObjectLiterals.process(externs, root);

    // Then the output should have the expected structure, with the outer
    // object literal inlined and the inner one left as-is
    assertNotNull(root);
    assertEquals(1, root.getNumChildren());
    Node objInOutput = root.getFirstChild();
    assertEquals(3, objInOutput.getNumChildren());

    Map<String, Node> variables = Maps.newHashMap();
    for (Node child = objInOutput.getFirstChild(); child != null; child = child.getNext()) {
      String key = child.getString();
      Node variable = child.getFirstChild();
      variables.put(key, variable);
    }

    assertEquals(2, variables.size());
    assertNotNull(variables.get("a"));
    assertNull(variables.get("b"));

    // And the nested object literal should still be present and unchanged
    assertNotNull(nestedObj2);
    assertEquals(1, nestedObj2.getNumChildren());
    Node b = nestedObj2.getFirstChild();
    assertEquals("b", b.getString());
  }
}