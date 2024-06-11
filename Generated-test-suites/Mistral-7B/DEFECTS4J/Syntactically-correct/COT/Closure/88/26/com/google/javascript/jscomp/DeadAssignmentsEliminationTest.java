package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.NullWriter;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ScopedCallback;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Test;

/**
 * Unit tests for DeadAssignmentsElimination class.
 */
public class DeadAssignmentsEliminationTest {

  @Test
  public void testDeadAssignmentsElimination() {
    // Test case 1: Simple dead assignment elimination
    String input = "function foo() {" +
        "  var x = 1;" +
        "  x = 2;" +
        "  return x;" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler(new NullWriter());
    Node root = NodeFactory.compile(input, null, compiler);
    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(null, root);

    NodeTraversal traversal = new NodeTraversal(root, null);
    eliminator.enterScope(traversal);
    assertEquals("function foo() {" +
        "  var x = 1;" +
        "  return x;" +
        "}", NodeUtil.toString(root));
    eliminator.exitScope(traversal);

    // Test case 2: Dead assignment in a condition expression
    input = "function foo() {" +
        "  var x = 1;" +
        "  if (x = 2) {" +
        "    return 1;" +
        "  }" +
        "  return 2;" +
        "}";

    root = NodeFactory.compile(input, null, compiler);
    eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(null, root);

    traversal = new NodeTraversal(root, null);
    eliminator.enterScope(traversal);
    assertEquals("function foo() {" +
        "  var x = 1;" +
        "  if (x) {" +
        "    return 1;" +
        "  }" +
        "  return 2;" +
        "}", NodeUtil.toString(root));
    eliminator.exitScope(traversal);

    // Test case 3: Dead assignment in a loop
    input = "function foo() {" +
        "  var x = 1;" +
        "  for (var i = 0; i = x; i++) {" +
        "    return 1;" +
        "  }" +
        "  return 2;" +
        "}";

    root = NodeFactory.compile(input, null, compiler);
    eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(null, root);

    traversal = new NodeTraversal(root, null);
    eliminator.enterScope(traversal);
    assertEquals("function foo() {" +
        "  var x = 1;" +
        "  for (var i = 0; i < x; i++) {" +
        "    return 1;" +
        "  }" +
        "  return 2;" +
        "}", NodeUtil.toString(root));
    eliminator.exitScope(traversal);

    // Test case 4: Dead assignment in a function call expression
    input = "function foo() {" +
        "  function bar() {" +
        "    var x = 1;" +
        "    x = 2;" +
        "  }" +
        "  bar();" +
        "  return 1;" +
        "}";

    root = NodeFactory.compile(input, null, compiler);
    eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(null, root);

    traversal = new NodeTraversal(root, null);
    eliminator.enterScope(traversal);
    assertEquals("function foo() {" +
        "  function bar() {}" +
        "  }" +
        "  bar();" +
        "  return 1;" +
        "}", NodeUtil.toString(root));
    eliminator.exitScope(traversal);

    // Test case 5: Dead assignments in a try-catch block
    input = "function foo() {" +
        "  try {" +
        "    var x = 1;" +
        "    x = 2;" +
        "    throw new Error();" +
        "  } catch (e) {" +
        "    return 1;" +
        "  }" +
        "  return 2;" +
        "}";

    root = NodeFactory.compile(input, null, compiler);
    eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(null, root);

    traversal = new NodeTraversal(root, null);
    eliminator.enterScope(traversal);
    assertEquals("function foo() {" +
        "  try {" +
        "    var x = 1;" +
        "    throw new Error();" +
        "  } catch (e) {", NodeUtil.toString(root));
    eliminator.exitScope(traversal);
  }
}