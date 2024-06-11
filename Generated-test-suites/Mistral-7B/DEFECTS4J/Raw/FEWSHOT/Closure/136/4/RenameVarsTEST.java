package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.*;

public class RenameVarsTest {

  @Test
  public void testRenameVars() {
    String prefix = "test_";
    RenameVars renameVars = new RenameVars(null, prefix, false, false, false, null, null, null);

    // Test empty code
    Node root = Node.NULL;
    renameVars.process(Node.NULL, root);
    assertEquals(0, renameVars.assignmentLog.length());

    // Test local variables
    String code = "function foo() { var a = 1, b = 2, c = 3; }";
    Node node = RhinoEvaluator.compileToNode(code);
    renameVars.process(Node.NULL, node);
    List<String> expectedLocalNames = Lists.newArrayList("a", "b", "c");
    List<String> actualLocalNames = extractVariableNames(node);
    assertEquals(expectedLocalNames, actualLocalNames);

    // Test global variables
    code = "var a = 1, b = 2, c = 3;";
    node = RhinoEvaluator.compileToNode(code);
    renameVars.process(Node.NULL, node);
    List<String> expectedGlobalNames = Lists.newArrayList("a", "b", "c");
    actualLocalNames = extractVariableNames(node.getFirstChild());
    assertEquals(expectedGlobalNames, actualLocalNames);

    // Test global and local variables
    code = "function foo() { var a = 1, b = 2; } var c = 3;";
    node = RhinoEvaluator.compileToNode(code);
    renameVars.process(Node.NULL, node.getFirstChild());
    actualLocalNames = extractVariableNames(node.getFirstChild().getLastChild());
    List<String> expectedLocalNames2 = Lists.newArrayList("a", "b");
    assertEquals(expectedLocalNames2, actualLocalNames);
    actualLocalNames = extractVariableNames(node);
    expectedGlobalNames = Lists.newArrayList("c");
    assertEquals(expectedGlobalNames, actualLocalNames);

    // Test anonymous functions
    code = "function foo() { var a = function() { return 1; }; }";
    node = RhinoEvaluator.compileToNode(code);
    renameVars.process(Node.NULL, node);
    actualLocalNames = extractVariableNames(node);
    expectedLocalNames2 = Lists.newArrayList("a");
    assertEquals(expectedLocalNames2, actualLocalNames);

    // Test anonymous functions with preserved names
    renameVars = new RenameVars(null, prefix, false, true, false, null, null, null);
    node = RhinoEvaluator.compileToNode(code);
    renameVars.process(Node.NULL, node);
    actualLocalNames = extractVariableNames(node);
    String expectedAnonymousFunctionName = "a$" + prefix + "_0";
    assertEquals(expectedAnonymousFunctionName, actualLocalNames.get(0));

    // Test reserved names
    renameVars = new RenameVars(null, prefix, false, false, false, null, null, Sets.newHashSet("foo", "bar"));
    code = "var foo = 1, bar = 2;";
    node = RhinoEvaluator.compileToNode(code);
    renameVars.process(Node.NULL, node);
    actualLocalNames = extractVariableNames(node);
    assertEquals(2, actualLocalNames.size());
    assertTrue(actualLocalNames.containsAll(Sets.newHashSet("bar")));
    assertTrue(actualLocalNames.get(0).startsWith("$"));
    assertEquals("bar", actualLocalNames.get(1));

    // Test prefixes
    renameVars = new RenameVars(null, prefix, false, false, false, null, null, null);
    code = "function foo() { var a = 1, b = 2; } var c = 3;";
    node = RhinoEvaluator.compileToNode(code);
    renameVars.process(Node.NULL, node.getFirstChild());
    actualLocalNames = extractVariableNames(node.getFirstChild().getLastChild());
    expectedLocalNames2 = Lists.newArrayList("a", "b");
    assertEquals(expectedLocalNames2, actualLocalNames);
    actualLocalNames = extractVariableNames(node);
    expectedGlobalNames = Lists.newArrayList("c");
    assertEquals(expectedGlobalNames, actualLocalNames);
    String expectedPrefix = prefix + "_";
    for (String name : actualLocalNames) {
      assertTrue(name.startsWith(expectedPrefix));
    }
  }

  private List<String> extractVariableNames(Node node) {
    List<String> variableNames = new ArrayList<>();
    if (node.getType() == Token.BLOCK) {
      Node child = node.getFirstChild();
      while (child != null) {
        if (child.getType() == Token.VAR) {
          variableNames.add(child.getString().substring(1));
        }
        child = child.getNextSibling();
      }
    }
    return variableNames;
  }
}