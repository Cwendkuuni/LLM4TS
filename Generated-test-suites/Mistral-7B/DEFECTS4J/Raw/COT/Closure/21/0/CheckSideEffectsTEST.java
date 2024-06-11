package com.google.javascript.jscomp.tests;

import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.TestCompiler;
import com.google.javascript.rhino.NodeOperations;

import org.junit.Test;

import java.util.List;

public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(CompilerOptions.WarningLevel.WARNINGS, CheckLevel.WARNING);
    Compiler compiler = new TestCompiler(options);

    String sourceCode = "function foo() {\n" +
        "  var s = \"this string is \";\n" +
        "  var s2 = \"continued on the next line but you forgot the +\";\n" +
        "  x = foo();\n" +
        "  foo();;\n" +
        "}\n";

    Node root = NodeOperations.parseJsSource(sourceCode);
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    NodeTraversal.traverse(compiler, root, checkSideEffects);

    List<Node> problemNodes = checkSideEffects.problemNodes;
    assertTrue(problemNodes.isEmpty());

    sourceCode = "function foo() {\n" +
        "  var s = \"this string is \";\n" +
        "  var s2 = \"continued on the next line but you forgot the +\";\n" +
        "  x == foo();\n" +
        "  foo();;\n" +
        "}\n";

    root = NodeOperations.parseJsSource(sourceCode);
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    NodeTraversal.traverse(compiler, root, checkSideEffects);

    problemNodes = checkSideEffects.problemNodes;
    assertTrue(problemNodes.size() == 3);
    for (Node node : problemNodes) {
      assertTrue(node.getType() == Token.ASSIGN || node.getType() == Token.SEMICOLON);
    }

    sourceCode = "function foo() {\n" +
        "  var s = \"this string is \";\n" +
        "  var s2 = \"continued on the next line but you forgot the +\";\n" +
        "  x = foo();\n" +
        "  return foo();;\n" +
        "}\n";

    root = NodeOperations.parseJsSource(sourceCode);
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    NodeTraversal.traverse(compiler, root, checkSideEffects);

    problemNodes = checkSideEffects.problemNodes;
    assertTrue(problemNodes.isEmpty());

    sourceCode = "function foo() {\n" +
        "  var s = \"this string is \";\n" +
        "  var s2 = \"continued on the next line but you forgot the +\";\n" +
        "  x = foo();\n" +
        "  return foo();;\n" +
        "  x = 5;\n" +
        "}\n";

    root = NodeOperations.parseJsSource(sourceCode);
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    NodeTraversal.traverse(compiler, root, checkSideEffects);

    problemNodes = checkSideEffects.problemNodes;
    assertTrue(problemNodes.size() == 4);
    for (int i = 0; i < problemNodes.size(); i++) {
      Node node = problemNodes.get(i);
      if (i < 3) {
        assertTrue(node.getType() == Token.ASSIGN || node.getType() == Token.SEMICOLON);
      } else {
        assertTrue(node.isNumberLiteral());
      }
    }
  }

  @Test(expected = AssertionError.class)
  public void testCheckSideEffectsException() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(CompilerOptions.WarningLevel.ERRORS, CheckLevel.ERROR);
    Compiler compiler = new TestCompiler(options);

    String sourceCode = "function foo() {\n" +
        "  throw new Error(\"Something went wrong\");\n" +
        "  x = bar();\n" +
        "}\n";

    Node root = NodeOperations.parseJsSource(sourceCode);
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.ERROR, true);
    NodeTraversal.traverse(compiler, root, checkSideEffects);
  }

  @Test
  public void testProtectSideEffects() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(CompilerOptions.WarningLevel.WARNINGS, CheckLevel.WARNING);
    Compiler compiler = new TestCompiler(options);

    String sourceCode = "function foo() {\n" +
        "  x = 5;\n" +
        "}\n";

    Node root = NodeOperations.parseJsSource(sourceCode);
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    NodeTraversal.traverse(compiler, root, checkSideEffects);

    List<Node> problemNodes = checkSideEffects.problemNodes;
    assertTrue(problemNodes.isEmpty());

    sourceCode = "function foo() {\n" +
        "  x = 5;\n" +
        "  return x;\n" +
        "}\n";

    root = NodeOperations.parseJsSource(sourceCode);
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    NodeTraversal.traverse(compiler, root, checkSideEffects);

    problemNodes = checkSideEffects.problemNodes;
    assertTrue(problemNodes.isEmpty());

    sourceCode = "function foo() {\n" +
        "  x = 5;\n" +
        "  return x + 1;\n" +
        "}\n";

    root = NodeOperations.parseJsSource(sourceCode);
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    NodeTraversal.traverse(compiler, root, checkSideEffects);

    problemNodes = checkSideEffects.problemNodes;
    assertTrue(problemNodes.isEmpty());

    sourceCode = "function foo() {\n" +
        "  x = 5;\n" +
        "  return x + y;\n" +
        "}\n";

    root = NodeOperations.parseJsSource(sourceCode);
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    NodeTraversal.traverse(compiler, root, checkSideEffects);

    problemNodes = checkSideEffects.problemNodes;
    assertTrue(problemNodes.size() == 1);
    assertTrue(problemNodes.get(0).isIdentifier("y"));

    sourceCode = "function foo() {\n" +
        "  if (true) {\n" +
        "    x = 5;\n" +
        "  } else {\n" +
        "    return x + y;\n" +
        "  }\n" +
        "}\n";

    root = NodeOperations.parseJsSource(sourceCode);
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    NodeTraversal.traverse(compiler, root, checkSideEffects);

    problemNodes = checkSideEffects.problemNodes;
    assertTrue(problemNodes.isEmpty());

    sourceCode = "function foo() {\n" +
        "  if (true) {\n" +
        "    x = 5;\n" +
        "  } else {\n" +
        "    return x + y;\n" +
        "  }\n" +
        "  return x + z;\n" +
        "}\n";

    root = NodeOperations.parseJsSource(sourceCode);
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    NodeTraversal.traverse(compiler, root, checkSideEffects);

    problemNodes = checkSideEffects.problemNodes;
    assertTrue(problemNodes.size() == 1);
    assertTrue(problemNodes.get(0).isIdentifier("y"));

    sourceCode = "function foo() {\n" +
        "  if (true) {\n" +
        "    x = 5;\n" +
        "  } else {\n" +
        "    return x + y;\n" +
        "  }\n" +
        "  return x + z;\n" +
        "  x = 10;\n" +
        "}\n";

    root = NodeOperations.parseJsSource(sourceCode);
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    NodeTraversal.traverse(compiler, root, checkSideEffects);

    problemNodes = checkSideEffects.problemNodes;
    assertTrue(problemNodes.size() == 2);
    assertTrue(problemNodes.get(0).isIdentifier("y"));
    assertTrue(problemNodes.get(1).isIdentifier("z"));
  }

  @Test
  public void testRemoveProtection() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(CompilerOptions.WarningLevel.WARNINGS, CheckLevel.WARNING);
    Compiler compiler = new TestCompiler(options);

    String sourceCode = "function foo() {\n" +
        "  JSCOMPILER_PRESERVE(x = 5);\n" +
        "  return x;\n" +
        "}\n";

    Node root = NodeOperations.parseJsSource(sourceCode);
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    NodeTraversal.traverse(compiler, root, checkSideEffects);

    List<Node> problemNodes = checkSideEffects.problemNodes;
    assertTrue(problemNodes.isEmpty());

    sourceCode = "function foo() {\n" +
        "  JSCOMPILER_PRESERVE(x = 5);\n" +
        "  return x;\n" +
        "  JSCOMPILER_PRESERVE(x = 10);\n" +
        "}\n";

    root = NodeOperations.parseJsSource(sourceCode);
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    NodeTraversal.traverse(compiler, root, checkSideEffects);

    problemNodes = checkSideEffects.problemNodes;
    assertTrue(problemNodes.size() == 2);
    assertTrue(problemNodes.get(0).isIdentifier("x"));
    assertTrue(problemNodes.get(1).isIdentifier("x"));

    sourceCode = "function foo() {\n" +
        "  JSCOMPILER_PRESERVE(x = 5);\n" +
        "  return x;\n" +
        "  JSCOMPILER_PRESERVE(x = 10);\n" +
        "  JSCOMPILER_PRESERVE(x = 15);\n" +
        "}\n";

    root = NodeOperations.parseJsSource(sourceCode);
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    NodeTraversal.traverse(compiler, root, checkSideEffects);

    problemNodes = checkSideEffects.problemNodes;
    assertTrue(problemNodes.size() == 3);
    assertTrue(problemNodes.get(0).isIdentifier("x"));
    assertTrue(problemNodes.get(1).isIdentifier("x"));
    assertTrue(problemNodes.get(2).isIdentifier("x"));
  }
}