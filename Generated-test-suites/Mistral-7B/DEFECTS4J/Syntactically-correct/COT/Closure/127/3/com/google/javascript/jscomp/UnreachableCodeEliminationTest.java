package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.UnreachableCodeElimination;

/**
 * Tests for the UnreachableCodeElimination class.
 */
@RunWith(JUnit4.class)
public class UnreachableCodeEliminationTest {
  // 

  @Test
  public void testUnreachableCodeElimination() {
    String jsCode = "function main() {" +
        "  if (true) {" +
        "    return;" +
        "  } else {" +
        "    var x = 1;" +
        "  }" +
        "  alert('unreachable');" +
        "}" +
        "main();";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node root = null;

    try {
      root = compiler.parseTree(jsCode, "test.js");
    } catch (Exception e) {
      fail("Failed to parse input code: " + e.getMessage());
    }

    UnreachableCodeElimination elimination = new UnreachableCodeElimination(
        compiler, true);
    elimination.process(null, root);

    Node expectedRoot = new Node(Token.PROGRAM, null);
    expectedRoot.setLine(1);
    expectedRoot.setSourceFile("test.js");
    expectedRoot.appendChild(new Node(Token.FUNCTION, "main", 0, null, false));
    expectedRoot.getLastChild().appendChild(new Node(Token.IF, 0, null, null, null, 1));
    expectedRoot.getLastChild().getLastChild().setLine(3);
    expectedRoot.getLastChild().getLastChild().setSourceFile("test.js");
    expectedRoot.getLastChild().getLastChild().getLastChild()
        .setNodeType(Token.RETURN);

    Node actualRoot = root;
    while (actualRoot.isFunction()) {
      actualRoot = actualRoot.getLastChild();
    }

    assertEquals(expectedRoot.toString(), actualRoot.toString());
  }

  @Test
  public void testUnreachableCodeWithConditionalStatement() {
    String jsCode = "function main() {" +
        "  if (false) {" +
        "    return;" +
        "  } else {" +
        "    var x = 1;" +
        "  }" +
        "  alert('unreachable');" +
        "}" +
        "main();";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node root = null;

    try {
      root = compiler.parseTree(jsCode, "test.js");
    } catch (Exception e) {
      fail("Failed to parse input code: " + e.getMessage());
    }

    UnreachableCodeElimination elimination = new UnreachableCodeElimination(
        compiler, true);
    elimination.process(null, root);

    Node expectedRoot = new Node(Token.PROGRAM, null);
    expectedRoot.setLine(1);
    expectedRoot.setSourceFile("test.js");
    expectedRoot.appendChild(new Node(Token.FUNCTION, "main", 0, null, false));
    expectedRoot.getLastChild().appendChild(new Node(Token.IF, 0, null, null, null, 1));
    expectedRoot.getLastChild().getLastChild().setNodeType(Token.ELSE);
    expectedRoot.getLastChild().getLastChild().getLastChild().appendChild(
        new Node(Token.VAR, "x", 0, null, Node.NO_INIT, 0));
    expectedRoot.getLastChild().getLastChild().getLastChild().getLastChild()
        .setLine(3);
    expectedRoot.getLastChild().getLastChild().getLastChild().getLastChild()
        .setSourceFile("test.js");

    Node actualRoot = root;
    while (actualRoot.isFunction()) {
      actualRoot = actualRoot.getLastChild();
    }

    assertEquals(expectedRoot.toString(), actualRoot.toString());
  }

  @Test
  public void testRemoveNoOpStatements() {
    String jsCode = "function main() {" +
        "  var x = 1;" +
        "  alert('');" +
        "}" +
        "main();";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node root = null;

    try {
      root = compiler.parseTree(jsCode, "test.js");
    } catch (Exception e) {
      fail("Failed to parse input code: " + e.getMessage());
    }

    UnreachableCodeElimination elimination = new UnreachableCodeElimination(
        compiler, true);
    elimination.process(null, root);

    Node expectedRoot = new Node(Token.PROGRAM, null);
    expectedRoot.setLine(1);
    expectedRoot.setSourceFile("test.js");
    expectedRoot.appendChild(new Node(Token.FUNCTION, "main", 0, null, false));
    expectedRoot.getLastChild().appendChild(new Node(Token.VAR, "x", 0, null, Node.NO_INIT, 0));

    Node actualRoot = root;
    while (actualRoot.isFunction()) {
      actualRoot = actualRoot.getLastChild();
    }

    assertEquals(expectedRoot.toString(), actualRoot.toString());
  }

  @Test(expected = AssertionError.class)
  public void testUnreachableCodeWithFunctionCall() {
    String jsCode = "function main() {" +
        "  if (true) {" +
        "    return;" +
        "  } else {" +
        "    foo();" +
        "  }" +
        "  alert('unreachable');" +
        "}" +
        "function foo() {}" +
        "main();";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node root = null;

    try {
      root = compiler.parseTree(jsCode, "test.js");
    } catch (Exception e) {
      fail("Failed to parse input code: " + e.getMessage());
    }

    UnreachableCodeElimination elimination = new UnreachableCodeElimination(
        compiler, true);
    elimination.process(null, root);

    fail("Expected an AssertionError but no exception was thrown.");
  }

  @Test(expected = AssertionError.class)
  public void testUnreachableCodeWithReturnInTryCatchBlock() {
    String jsCode = "function main() {" +
        "  try {" +
        "    return;" +
        "  } catch (e) {" +
        "    alert('unreachable');" +
        "  }" +
        "}" +
        "main();";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node root = null;

    try {
      root = compiler.parseTree(jsCode, "test.js");
    } catch (Exception e) {
      fail("Failed to parse input code: " + e.getMessage());
    }

    UnreachableCodeElimination elimination = new UnreachableCodeElimination(
        compiler, true);
    elimination.process(null, root);

    fail("Expected an AssertionError but no exception was thrown.");
  }

  // 
}