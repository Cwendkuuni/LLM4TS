package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.TestCompiler;
import com.google.javascript.rhino.NodeOperations;
import com.google.javascript.rhino.Token;

/**
 * Test cases for CheckSideEffects class
 */
public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects() {
    AbstractCompiler compiler = new TestCompiler();
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevelCode(CheckLevel.WARNING, "JSC_USELESS_CODE");

    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

    String sourceCode = "function foo() {}\n" +
        "function bar() {\n" +
        "  var s = \"this string is \";\n" +
        "  var t = \"continued on the next line but you forgot the +\";\n" +
        "  x = foo();\n" +
        "  foo();;\n" +
        "}\n";

    CompilerInput input = new CompilerInput(sourceCode, "test.js", null);
    checkSideEffects.process(input, null);

    Node root = input.getAstRoot(compiler);
    NodeTraversal.traverse(compiler, root, new NodeTraversal.RootVisitor() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isFunction()) {
          String functionName = n.getName().getName();
          if (functionName.equals("bar")) {
            int warningCount = 0;
            for (Node warning : checkSideEffects.problemNodes) {
              if (warning.getParent().equals(n)) {
                warningCount++;
              }
            }
            assertTrue("Expected at least one warning for function 'bar'", warningCount > 0);
          }
        }
      }
    });
  }

  @Test
  public void testProtectSideEffects() {
    AbstractCompiler compiler = new TestCompiler();
    CompilerOptions options = new CompilerOptions();

    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

    String sourceCode = "function foo() {\n" +
        "  var x = 5;\n" +
        "}\n";

    CompilerInput input = new CompilerInput(sourceCode, "test.js", null);
    checkSideEffects.process(input, null);

    Node root = input.getAstRoot(compiler);

    checkSideEffects.protectSideEffects();

    NodeTraversal.traverse(compiler, root, new StripProtection(compiler));

    root = input.getAstRoot(compiler);

    Node expectedNode = NodeOperations.newAssign(NodeOperations.newVar("JSCOMPILER_PROTECTOR_FN"), NodeOperations.newCall(NodeOperations.newName("JSCOMPILER_PROTECTOR_FN"), null));
    Node actualNode = NodeUtil.findSingleNode(root, NodeUtil.isExpressionStatement(NodeUtil.isAssign(NodeUtil.isName("JSCOMPILER_PROTECTOR_FN"))));

    assertTrue("Expected 'JSCOMPILER_PROTECTOR_FN' function to be defined", actualNode != null);
    assertTrue("Expected 'JSCOMPILER_PROTECTOR_FN' function body to be 'foo()'", NodeUtil.isExpressionStatement(actualNode) && NodeUtil.isFunctionCall(actualNode.getFirstChild()));
    assertTrue("Expected 'foo' function to be unchanged", NodeUtil.isFunction(root.getFirstChild()) && NodeUtil.isFunction(expectedNode.getFirstChild()));
  }

  @Test(expected = AssertionError.class)
  public void testNoWarningsForSideEffectingCode() {
    AbstractCompiler compiler = new TestCompiler();
    CompilerOptions options = new CompilerOptions();

    String sourceCode = "function foo() {\n" +
        "  var x = 5;\n" +
        "  x++;\n" +
        "  return x;\n" +
        "}\n";

    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

    CompilerInput input = new CompilerInput(sourceCode, "test.js", null);
    checkSideEffects.process(input, null);

    Node root = input.getAstRoot(compiler);

    NodeTraversal.traverse(compiler, root, new NodeTraversal.RootVisitor() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isFunction()) {
          String functionName = n.getName().getName();
          if (functionName.equals("foo")) {
            int warningCount = 0;
            for (Node warning : checkSideEffects.problemNodes) {
              if (warning.getParent().equals(n)) {
                warningCount++;
              }
            }
            assertTrue("Expected at least one warning for function 'foo'", warningCount > 0);
          }
        }
      }
    });
  }

  @Test(expected = AssertionError.class)
  public void testNoWarningsForBasicArithmeticOperations() {
    AbstractCompiler compiler = new TestCompiler();
    CompilerOptions options = new CompilerOptions();

    String sourceCode = "function foo() {\n" +
        "  var x = 5;\n" +
        "  var y = 10;\n" +
        "  var sum = x + y;\n" +
        "  var difference = x - y;\n" +
        "  var product = x * y;\n" +
        "  var quotient = x / y;\n" +
        "  var remainder = x % y;\n" +
        "  return sum + difference + product + quotient + remainder;\n" +
        "}\n";

    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

    CompilerInput input = new CompilerInput(sourceCode, "test.js", null);
    checkSideEffects.process(input, null);

    Node root = input.getAstRoot(compiler);

    NodeTraversal.traverse(compiler, root, new NodeTraversal.RootVisitor() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isFunction()) {
          String functionName = n.getName().getName();
          if (functionName.equals("foo")) {
            int warningCount = 0;
            for (Node warning : checkSideEffects.problemNodes) {
              if (warning.getParent().equals(n)) {
                warningCount++;
              }
            }
            assertTrue("Expected no warnings for function 'foo'", warningCount == 0);
          }
        }
      }
    });
  }

  @Test(expected = AssertionError.class)
  public void testNoWarningsForComplexCode() {
    AbstractCompiler compiler = new TestCompiler();
    CompilerOptions options = new CompilerOptions();

    String sourceCode = "function foo() {\n" +
        "  var x = 5;\n" +
        "  var y = 10;\n" +
        "  var z = 15;\n" +
        "  var sum = x + y;\n" +
        "  var difference = x - y;\n" +
        "  if (sum > 15) {\n" +
        "    x = x + 1;\n" +
        "  } else {\n" +
        "    y = y + 1;\n" +
        "  }\n" +
        "  var product = x * y * z;\n" +
        "  var quotient = x / (y + 1);\n" +
        "  var remainder = x % (y + 1);\n" +
        "  var result = sum + difference + product + quotient + remainder;\n" +
        "  return result;\n" +
        "}\n";

    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

    CompilerInput input = new CompilerInput(sourceCode, "test.js", null);
    checkSideEffects.process(input, null);

    Node root = input.getAstRoot(compiler);

    NodeTraversal.traverse(compiler, root, new NodeTraversal.RootVisitor() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isFunction()) {
          String functionName = n.getName().getName();
          if (functionName.equals("foo")) {
            int warningCount = 0;
            for (Node warning : checkSideEffects.problemNodes) {
              if (warning.getParent().equals(n)) {
                warningCount++;
              }
            }
            assertTrue("Expected no warnings for function 'foo'", warningCount == 0);
          }
        }
      }
    });
  }
}