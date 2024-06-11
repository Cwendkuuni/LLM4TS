package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

/**
 * Test cases for the CheckSideEffects class
 */
public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects() {
    CompilerInput input = NodeFactory.createRoot(
        Token.BLOCK,
        Lists.newArrayList(
            Token.VAR, Token.IDENT("x"), Token.EQUALS, Token.NAME("foo"), Token.SEMICOLON));

    CompilerOptions options = new CompilerOptions();
    options.setCheckSideEffects(CheckLevel.WARNING);

    AbstractCompiler compiler = new Compiler();
    compiler.compile(input, options, null);

    Node root = input.getAstRoot(compiler);
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    checkSideEffects.process(input, root);

    // Basic functionality test
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    // Test for missing '+' operator
    input = NodeFactory.createRoot(
        Token.BLOCK,
        Lists.newArrayList(
            Token.VAR, Token.IDENT("s"), Token.STRING_LITERAL,
            "[this string is ", Token.STRING_LITERAL,
            "continued on the next line but you forgot the +", Token.SEMICOLON));

    compiler.compile(input, options, null);
    root = input.getAstRoot(compiler);
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    checkSideEffects.process(input, root);

    assertTrue(checkSideEffects.problemNodes.size() > 0);

    // Test for empty statement
    input = NodeFactory.createRoot(Token.BLOCK, Lists.<Node>newArrayList());

    compiler.compile(input, options, null);
    root = input.getAstRoot(compiler);
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    checkSideEffects.process(input, root);

    assertTrue(checkSideEffects.problemNodes.isEmpty());

    // Test for stray semicolon
    input = NodeFactory.createRoot(
        Token.BLOCK,
        Lists.newArrayList(Token.VAR, Token.IDENT("x"), Token.EQUALS, Token.NAME("foo"), Token.SEMICOLON, Token.SEMICOLON));

    compiler.compile(input, options, null);
    root = input.getAstRoot(compiler);
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    checkSideEffects.process(input, root);

    assertTrue(checkSideEffects.problemNodes.isEmpty());

    // Test for side-effect free code protection
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    checkSideEffects.process(input, root);

    assertTrue(checkSideEffects.problemNodes.isEmpty());

    // Test for side-effect free code protection with problem nodes
    input = NodeFactory.createRoot(
        Token.BLOCK,
        Lists.newArrayList(
            Token.VAR, Token.IDENT("s"), Token.STRING_LITERAL,
            "[this string is ", Token.STRING_LITERAL,
            "continued on the next line but you forgot the +", Token.SEMICOLON,
            Token.IDENT("x"), Token.EQUALS, Token.NAME("foo"), Token.SEMICOLON));

    compiler.compile(input, options, null);
    root = input.getAstRoot(compiler);
    checkSideEffects.process(input, root);

    assertTrue(checkSideEffects.problemNodes.size() > 0);

    // Test for side-effect free code protection after processing
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    checkSideEffects.protectSideEffects();

    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  @Test(expected = AssertionError.class)
  public void testCheckSideEffectsThrowsException() {
    CompilerInput input = NodeFactory.createRoot(Token.BLOCK, Lists.<Node>newArrayList());
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new Compiler();
    compiler.compile(input, options, null);
    Node root = input.getAstRoot(compiler);

    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.ERROR, false);
    checkSideEffects.process(input, root);

    assertFalse(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testStripProtection() {
    CompilerInput input = NodeFactory.createRoot(
        Token.BLOCK,
        Lists.<Node>newArrayList(
            Token.EXPR_STATEMENT,
            Token.CALL,
            Token.IDENT("JSCOMPILER_PRESERVE"),
            Token.L_PAREN,
            Token.NAME("x"),
            Token.R_PAREN));

    CompilerOptions options = new CompilerOptions();

    AbstractCompiler compiler = new Compiler();
    compiler.compile(input, options, null);
    Node root = input.getAstRoot(compiler);

    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    checkSideEffects.process(input, root);

    CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
    stripProtection.process(input, root);

    Node xNode = NodeUtil.findSingleChildOfType(root, Token.VAR, "x");
    assertFalse(xNode.isCall());
  }
}