package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.TestCompiler;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects() {
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = true;
    AbstractCompiler compiler = new TestCompiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);

    // Test empty program
    Node root = NodeFactory.createProgram();
    NodeTraversal.traverse(compiler, root, checkSideEffects);
    assertTrue("No warnings expected for empty program", compiler.getDiagnostics().isEmpty());

    // Test simple assignment
    Node assignmentNode = NodeFactory.createAssign(Token.NAME, NodeFactory.createName("x"), NodeFactory.createNumber(1));
    Node parent = NodeFactory.createBlock();
    parent.addChild(assignmentNode);
    Node program = NodeFactory.createProgram();
    program.addChild(parent);
    NodeTraversal.traverse(compiler, program, checkSideEffects);
    List<String> warnings = compiler.getDiagnostics();
    assertTrue("One warning expected for simple assignment", warnings.size() == 1);
    assertTrue(warnings.get(0).contains("Suspicious code. This code lacks side-effects."));

    // Test void statement
    Node voidNode = NodeFactory.createStatement(Token.VOID);
    parent = NodeFactory.createBlock();
    parent.addChild(voidNode);
    program = NodeFactory.createProgram();
    program.addChild(parent);
    NodeTraversal.traverse(compiler, program, checkSideEffects);
    warnings = compiler.getDiagnostics();
    assertTrue("No warnings expected for void statement", warnings.isEmpty());

    // Test simple operator
    Node opNode = NodeFactory.createBinaryOp(Token.PLUS, NodeFactory.createNumber(1), NodeFactory.createNumber(1));
    parent = NodeFactory.createBlock();
    parent.addChild(opNode);
    program = NodeFactory.createProgram();
    program.addChild(parent);
    NodeTraversal.traverse(compiler, program, checkSideEffects);
    warnings = compiler.getDiagnostics();
    assertTrue("No warnings expected for simple operator", warnings.isEmpty());

    // Test side effect free expression
    Node sideEffectFree = NodeFactory.createBinaryOp(Token.PLUS, NodeFactory.createName("msg"), NodeFactory.createString(" warning"));
    Node sideEffectFreeExpr = NodeFactory.createExpressionStatement(sideEffectFree);
    parent = NodeFactory.createBlock();
    parent.addChild(sideEffectFreeExpr);
    program = NodeFactory.createProgram();
    program.addChild(parent);
    NodeTraversal.traverse(compiler, program, checkSideEffects);
    warnings = compiler.getDiagnostics();
    assertTrue("No warnings expected for side effect free expression", warnings.isEmpty());

    // Test side effect statement
    Node sideEffect = NodeFactory.createAssign(Token.NAME, NodeFactory.createName("x"), NodeFactory.createCall(NodeFactory.createName("foo"), null));
    parent = NodeFactory.createBlock();
    parent.addChild(sideEffect);
    program = NodeFactory.createProgram();
    program.addChild(parent);
    NodeTraversal.traverse(compiler, program, checkSideEffects);
    warnings = compiler.getDiagnostics();
    assertTrue("One warning expected for side effect statement", warnings.size() == 1);
    assertTrue(warnings.get(0).contains("Suspicious code. This code lacks side-effects."));

    // Test protectSideEffects
    CheckSideEffects protectSideEffects = new CheckSideEffects(compiler, level, false);
    Node protectedNode = NodeFactory.createAssign(Token.NAME, NodeFactory.createName("x"), NodeFactory.createNumber(1));
    parent = NodeFactory.createBlock();
    parent.addChild(protectedNode);
    NodeTraversal.traverse(compiler, parent, protectSideEffects);
    protectSideEffects.protectSideEffects();
    NodeTraversal.traverse(compiler, parent, checkSideEffects);
    warnings = compiler.getDiagnostics();
    assertTrue("No warnings expected for protectedNode", warnings.isEmpty());

    // Test removeProtection
    CompilerPass stripProtection = new CheckSideEffects.StripProtection(compiler);
    NodeTraversal.traverse(compiler, program, stripProtection);
    NodeTraversal.traverse(compiler, program, checkSideEffects);
    warnings = compiler.getDiagnostics();
    assertTrue("No warnings expected after removeProtection", warnings.isEmpty());
  }

  @Test(expected = AssertionError.class)
  public void testCheckSideEffectsException() {
    CheckLevel level = CheckLevel.ERROR;
    boolean protectSideEffectFreeCode = true;
    AbstractCompiler compiler = new TestCompiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);

    // Test null root
    Node root = null;
    NodeTraversal.traverse(compiler, root, checkSideEffects);
  }

  @Test(expected = AssertionError.class)
  public void testCheckSideEffectsNullParent() {
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = true;
    AbstractCompiler compiler = new TestCompiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);

    // Test null parent
    Node parent = null;
    Node node = NodeFactory.createNumber(1);
    Node program = NodeFactory.createProgram();
    program.addChild(NodeFactory.createBlock().addChild(node));
    NodeTraversal.traverse(compiler, program, checkSideEffects);
  }

  @Test(expected = AssertionError.class)
  public void testCheckSideEffectsNullChild() {
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = true;
    AbstractCompiler compiler = new TestCompiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);

    // Test null child
    Node parent = NodeFactory.createBlock();
    Node node = null;
    Node program = NodeFactory.createProgram();
    program.addChild(parent);
    NodeTraversal.traverse(compiler, program, checkSideEffects);
  }

  @Test(expected = AssertionError.class)
  public void testCheckSideEffectsInvalidParentType() {
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = true;
    AbstractCompiler compiler = new TestCompiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);

    // Test invalid parent type
    Node parent = NodeFactory.createNumber(1);
    Node node = NodeFactory.createNumber(2);
    Node program = NodeFactory.createProgram();
    program.addChild(parent);
    NodeTraversal.traverse(compiler, program, checkSideEffects);
  }

  @Test(expected = AssertionError.class)
  public void testCheckSideEffectsInvalidChildType() {
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = true;
    AbstractCompiler compiler = new TestCompiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);

    // Test invalid child type
    Node parent = NodeFactory.createBlock();
    Node node = NodeFactory.createProgram();
    Node program = NodeFactory.createProgram();
    program.addChild(parent);
    NodeTraversal.traverse(compiler, program, checkSideEffects);
  }

  @Test(expected = AssertionError.class)
  public void testCheckSideEffectsInvalidParentTypeWithChildren() {
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = true;
    AbstractCompiler compiler = new TestCompiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);

    // Test invalid parent type with children
    Node parent = NodeFactory.createNumber(1);
    Node child1 = NodeFactory.createNumber(2);
    Node child2 = NodeFactory.createNumber(3);
    Node program = NodeFactory.createProgram();
    program.addChild(parent);
    parent.addChild(child1);
    parent.addChild(child2);
    NodeTraversal.traverse(compiler, program, checkSideEffects);
  }

  @Test(expected = AssertionError.class)
  public void testCheckSideEffectsInvalidChildTypeWithParent() {
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = true;
    AbstractCompiler compiler = new TestCompiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);

    // Test invalid child type with parent
    Node parent = NodeFactory.createBlock();
    Node node = NodeFactory.createProgram();
    Node program = NodeFactory.createProgram();
    program.addChild(parent);
    parent.setParent(node);
    NodeTraversal.traverse(compiler, program, checkSideEffects);
  }
}