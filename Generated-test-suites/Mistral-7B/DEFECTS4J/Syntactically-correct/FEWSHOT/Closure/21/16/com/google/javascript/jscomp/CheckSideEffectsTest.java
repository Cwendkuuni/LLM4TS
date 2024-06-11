package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.SimpleCompiler;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceFile;

import java.util.List;

/**
 * Test cases for {@link CheckSideEffects} class.
 */
@RunWith(JUnit4.class)
public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects() {
    SourceFile source = new SourceFile("CheckSideEffectsTest.js", "use strict;\n" +
        "function foo() {}\n" +
        "function main() {\n" +
        "  checkSideEffects();\n" +
        "}\n" +
        "function checkSideEffects() {\n" +
        "  var s = 'this string is continued on the next line but you forgot the +\'\n" +
        "  x = foo();\n" +
        "  foo();;\n" +
        "}\n");

    Compiler compiler = new SimpleCompiler();
    Checker checker = new Checker(Checker.Type.SYNCHRONOUS);
    checker.check(source, compiler.getOptions());
    List<Node> errors = Lists.newArrayList(compiler.getErrors());

    Node checkSideEffectsCall = compiler.getRoot();
    for (Node child : checkSideEffectsCall.getChildren()) {
      if (child.isIdentifier() && child.getString().equals("checkSideEffects")) {
        Node checkSideEffects = child;
        break;
      }
    }

    Node TraversalRoot = new NodeFactory().createProgram();
    TraversalRoot.setSourceName("CheckSideEffectsTest.js");
    NodeTraversal.traverse(compiler, TraversalRoot, new CheckSideEffects(compiler, CheckLevel.WARNING, false));

    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);

    NodeUtil.accept(checkSideEffects, checkSideEffectsCall.getFirstChild(), null);

    assertFalse("No warnings expected for the following code", errors.isEmpty());
    assertTrue("Unexpected number of warnings", errors.size() == 3);
    checkWarnings(errors, 0, "Is there a missing '+' on the previous line?");
    checkWarnings(errors, 1, "The result of the '=' operator is not being used.");
    checkWarnings(errors, 2, "The result of the '=' operator is not being used.");
  }

  @Test
  public void testCheckSideEffectsProtectSideEffects() {
    SourceFile source = new SourceFile("CheckSideEffectsProtectSideEffectsTest.js", "use strict;\n" +
        "function foo() {}\n" +
        "function main() {\n" +
        "  checkSideEffects();\n" +
        "}\n" +
        "function checkSideEffects() {\n" +
        "  var s = 'this string is continued on the next line but you forgot the +\'\n" +
        "  x = foo();\n" +
        "  foo();;\n" +
        "  var x = 1;\n" +
        "  JSCOMPILER_PRESERVE(s); // protects side-effect free code\n" +
        "}\n");

    Compiler compiler = new SimpleCompiler();
    Checker checker = new Checker(Checker.Type.SYNCHRONOUS);
    checker.check(source, compiler.getOptions());
    List<Node> errors = Lists.newArrayList(compiler.getErrors());

    Node checkSideEffectsCall = compiler.getRoot();
    for (Node child : checkSideEffectsCall.getChildren()) {
      if (child.isIdentifier() && child.getString().equals("checkSideEffects")) {
        Node checkSideEffects = child;
        break;
      }
    }

    Node TraversalRoot = new NodeFactory().createProgram();
    TraversalRoot.setSourceName("CheckSideEffectsProtectSideEffectsTest.js");
    NodeTraversal.traverse(compiler, TraversalRoot, new CheckSideEffects(compiler, CheckLevel.WARNING, true));

    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

    NodeUtil.accept(checkSideEffects, checkSideEffectsCall.getFirstChild(), null);

    assertFalse("No warnings expected for the following code", errors.isEmpty());
    assertTrue("Unexpected number of warnings", errors.size() == 2);
    checkWarnings(errors, 0, "This code lacks side-effects. Is there a bug?");
    checkWarnings(errors, 1, "This code lacks side-effects. Is there a bug?");
  }

  private void checkWarnings(List<Node> errors, int index, String message) {
    Node warning = errors.get(index);
    assertEquals("Expected warning message: " + message,
        message, warning.getFirstChild().getString());
    assertEquals("Expected warning level: WARNING",
        CheckLevel.WARNING.name(), warning.getJSDocInfo().getTagName());
  }
}