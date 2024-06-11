package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeFactory;

public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(CompilerOptions.WarningLevel.QUIRKS, CheckLevel.WARNING);
    AbstractCompiler compiler = new Compiler(options);

    String input = "function foo() { return 1; } var x = 0; x = foo();";

    CompilerInput ci = new CompilerInput(NodeFactory.createScriptNodeFromString(input), "test.js");
    Node root = ci.getAstRoot(compiler);

    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    NodeTraversal.traverse(compiler, root, checkSideEffects);

    assertEquals(0, checkSideEffects.problemNodes.size());

    ci = new CompilerInput(NodeFactory.createScriptNodeFromString("var s = \"this string is \";"), "test.js");
    root = ci.getAstRoot(compiler);
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    NodeTraversal.traverse(compiler, root, checkSideEffects);

    assertEquals(1, checkSideEffects.problemNodes.size());
    Node problemNode = checkSideEffects.problemNodes.get(0);
    assertEquals(Node.TYPE_STRING_LITERAL, problemNode.getType());
    assertEquals("Is there a missing '+' on the previous line?", problemNode.getAnnotation(CheckSideEffects.USELESS_CODE_ERROR).getMessage());
  }

  @Test
  public void testProtectSideEffectFreeCode() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(CompilerOptions.WarningLevel.QUIRKS, CheckLevel.ERROR);
    AbstractCompiler compiler = new Compiler(options);

    String input = "function foo() { return 1; } var x = 0; x = foo();";

    CompilerInput ci = new CompilerInput(NodeFactory.createScriptNodeFromString(input), "test.js");
    Node root = ci.getAstRoot(compiler);

    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.ERROR, true);
    NodeTraversal.traverse(compiler, root, checkSideEffects);

    checkSideEffects.protectSideEffects();

    ci = new CompilerInput(NodeFactory.createScriptNodeFromString(input), "test.js");
    root = ci.getAstRoot(compiler);

    StripProtection stripProtection = new StripProtection(compiler);
    NodeTraversal.traverse(compiler, root, stripProtection);

    CheckSideEffects checkSideEffects2 = new CheckSideEffects(compiler, CheckLevel.ERROR, false);
    NodeTraversal.traverse(compiler, root, checkSideEffects2);

    assertEquals(0, checkSideEffects2.problemNodes.size());
  }
}

class Compiler extends AbstractCompiler {

  public Compiler(CompilerOptions options) {
    super(options);
  }
}