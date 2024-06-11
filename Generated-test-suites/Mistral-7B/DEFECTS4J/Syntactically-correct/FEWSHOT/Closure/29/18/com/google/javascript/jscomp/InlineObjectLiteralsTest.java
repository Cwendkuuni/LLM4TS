package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    // Create a simple object literal and test if it can be inlined
    Node objectNode = new Node(Token.OBJECT_LITERAL, null);
    objectNode.addChildToFront(new Node(Token.KEYWORD, "a"));
    objectNode.addChildToFront(new Node(Token.COLON, null));
    objectNode.addChildToFront(new Node(Token.NUMBER, "1"));

    Node rootNode = new Node(Token.PROGRAM, null);
    rootNode.addChildToFront(objectNode);

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(
        new AbstractCompiler(null), new Supplier<String>() {
          private int counter = 0;

          @Override
          public String get() {
            return "inlineVar_" + ++counter;
          }
        });

    inlineObjectLiterals.process(null, rootNode);

    Var a = findVariableByName("a", rootNode);
    assertNotNull(a);
    assertEquals("inlineVar_1", a.name);

    // Test if multiple object literals can be inlined in the same script
    Node complexObjectNode = new Node(Token.OBJECT_LITERAL, null);
    complexObjectNode.addChildToFront(new Node(Token.KEYWORD, "a"));
    complexObjectNode.addChildToFront(new Node(Token.COLON, null));
    complexObjectNode.addChildToFront(new Node(Token.OBJECT_LITERAL, null));
    complexObjectNode.getLastChild().addChildToFront(new Node(Token.KEYWORD, "b"));
    complexObjectNode.getLastChild().addChildToFront(new Node(Token.COLON, null));
    complexObjectNode.getLastChild().addChildToFront(new Node(Token.NUMBER, "2"));

    rootNode.getLastChild().setRightChild(complexObjectNode);

    inlineObjectLiterals.process(null, rootNode);

    Var aVar = findVariableByName("a", rootNode);
    assertEquals("inlineVar_1", aVar.name);

    Var bVar = findVariableByName("b", rootNode);
    assertNotNull(bVar);
    assertEquals("inlineVar_2", bVar.name);

    // Test if an object literal with properties referencing each other
    // can be inlined
    Node cyclicObjectNode = new Node(Token.OBJECT_LITERAL, null);
    cyclicObjectNode.addChildToFront(new Node(Token.KEYWORD, "a"));
    cyclicObjectNode.addChildToFront(new Node(Token.COLON, null));
    cyclicObjectNode.addChildToFront(new Node(Token.KEYWORD, "b"));
    cyclicObjectNode.addChildToFront(new Node(Token.COLON, null));
    cyclicObjectNode.addChildToFront(cyclicObjectNode);

    rootNode.setRightChild(cyclicObjectNode);

    inlineObjectLiterals.process(null, rootNode);

    Var aVarCyclic = findVariableByName("a", rootNode);
    Var bVarCyclic = findVariableByName("b", rootNode);

    assertNotNull(aVarCyclic);
    assertEquals("inlineVar_3", aVarCyclic.name);
    assertNotNull(bVarCyclic);
    assertEquals("inlineVar_4", bVarCyclic.name);

    // Test if an object literal containing an array can be inlined
    Node objectWithArrayNode = new Node(Token.OBJECT_LITERAL, null);
    objectWithArrayNode.addChildToFront(new Node(Token.KEYWORD, "a"));
    objectWithArrayNode.addChildToFront(new Node(Token.COLON, null));
    objectWithArrayNode.addChildToFront(new Node(Token.ARRAY_LITERAL, null));
    objectWithArrayNode.getLastChild().addChildToFront(new Node(Token.NUMBER, "1"));

    rootNode.setRightChild(objectWithArrayNode);

    inlineObjectLiterals.process(null, rootNode);

    Var aVarWithArray = findVariableByName("a", rootNode);
    assertNotNull(aVarWithArray);
    assertEquals("inlineVar_5", aVarWithArray.name);
  }

  private Var findVariableByName(String name, Node root) {
    for (Iterator<Var> it = root.getScope().getVars().iterator(); it
        .hasNext();) {
      Var var = it.next();
      if (var.name.equals(name)) {
        return var;
      }
    }
    return null;
  }
}