package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeUtil;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RunWith(JUnit4.class)
public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    Compiler compiler = new Compiler();
    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(compiler, Node::newName);
    Node rootNode = new Node(Token.SOURCE_START);
    Node externsNode = new Node(Token.SOURCE_START);

    inlineObjectLiterals.process(externsNode, rootNode);

    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(compiler, new InliningBehavior());
    callback.process(externsNode, rootNode);

    Scope scope = rootNode.getLastChild().getFirstChild().getFirstChild();

    // Test that the object literal has been inlined and replaced with individual variables
    Node objLiteral = NodeUtil.findFirstChildOfType(scope, Node::isObjectLit);
    assertNotNull(objLiteral);
    assertNull(objLiteral.getParent().getFirstChild());

    List<Node> variables = Lists.newArrayList();
    NodeUtil.visitPreOrder(objLiteral, new NodeUtil.Visitor<Node>() {
      @Override
      public void visit(Node node) {
        if (node.isVar()) {
          variables.add(node);
        }
      }
    });

    Map<String, Node> expectedVariables = Maps.newHashMap();
    expectedVariables.put("key1", new Node(Token.STRING, "key1"));
    expectedVariables.put("key2", new Node(Token.STRING, "key2"));
    expectedVariables.put("value1", new Node(Token.NUMBER, 1));
    expectedVariables.put("value2", new Node(Token.NUMBER, 2));

    for (Node variable : variables) {
      String variableName = variable.getString();
      Node variableValue = variable.getFirstChild();
      assertEquals(expectedVariables.get(variableName), variableValue);
    }

    // Test that the object literal's keys have been replaced with the new variable names in usages
    Node usage1 = NodeUtil.findFirstChildOfType(scope, node -> node.getParent().isGetProp()
        && node.getString().equals("obj.key1"));
    assertNotNull(usage1);
    assertEquals(expectedVariables.get("key1"), usage1.getParent().getLastChild());

    Node usage2 = NodeUtil.findFirstChildOfType(scope, node -> node.getParent().isGetProp()
        && node.getString().equals("obj.key2"));
    assertNotNull(usage2);
    assertEquals(expectedVariables.get("key2"), usage2.getParent().getLastChild());

    // Test that the object literal's values have been replaced with the new variables in usages
    Node usageValue1 = NodeUtil.findFirstChildOfType(scope, node -> node.getParent().isCall()
        && node.getParent().getLastChild().isName()
        && node.getParent().getLastChild().getString().equals("setValue1"));
    assertNotNull(usageValue1);
    assertEquals(expectedVariables.get("value1"), usageValue1.getFirstChild());

    Node usageValue2 = NodeUtil.findFirstChildOfType(scope, node -> node.getParent().isCall()
        && node.getParent().getLastChild().isName()
        && node.getParent().getLastChild().getString().equals("setValue2"));
    assertNotNull(usageValue2);
    assertEquals(expectedVariables.get("value2"), usageValue2.getFirstChild());

    // Test that the original object literal is not present in the AST after the pass
    assertNull(NodeUtil.findFirstChildOfType(scope, Node::isObjectLit));
  }
}

class InliningBehavior extends Behavior {
  // Copy the code from the original InliningBehavior class, except for the splitObject method
  // ...
}