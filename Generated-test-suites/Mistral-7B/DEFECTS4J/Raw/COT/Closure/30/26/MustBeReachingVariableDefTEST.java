package com.google.javascript.jscomp;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.google.common.collect.Sets;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MustBeReachingVariableDefTest {

  @Test
  public void testEmpty() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);
    MustDef expected = new MustDef();

    MustDef result = analysis.compute(cfg.getEntryNode(), expected);

    assertSame(expected, result);
  }

  @Test
  public void testSimpleAssignment() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();

    Node assignment = Node.newAssignment(Token.ASSIGN, Node.newName("x"),
        Node.newName("y"));
    cfg.addNode(assignment);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);
    MustDef input = new MustDef();
    MustDef expected = new MustDef();
    expected.reachingDef.put(jsScope.getVar("x"),
        new Definition(assignment));

    MustDef result = analysis.compute(assignment, input);

    assertSame(expected, result);
  }

  @Test
  public void testMultipleAssignments() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();

    Node assignment1 = Node.newAssignment(Token.ASSIGN, Node.newName("x"),
        Node.newName("y"));
    Node assignment2 = Node.newAssignment(Token.ASSIGN, Node.newName("x"),
        Node.newName("z"));
    cfg.addNode(assignment1);
    cfg.addNode(assignment2);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);
    MustDef input = new MustDef();
    MustDef expected = new MustDef();
    expected.reachingDef.put(jsScope.getVar("x"),
        new Definition(assignment2)); // last definition wins

    MustDef result = analysis.compute(assignment2, input);

    assertSame(expected, result);
  }

  @Test
  public void testMultipleDefinitions() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();

    Node assignment1 = Node.newAssignment(Token.ASSIGN, Node.newName("x"),
        Node.newName("y"));
    Node assignment2 = Node.newAssignment(Token.ASSIGN, Node.newName("x"),
        Node.newName("z"));
    cfg.addNode(assignment1);
    cfg.addNode(assignment2);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);
    MustDef input = new MustDef();
    MustDef expected = new MustDef();
    expected.reachingDef.put(jsScope.getVar("x"), new Definition(assignment1));

    MustDef result = analysis.compute(assignment1, input);

    assertSame(expected, result);
  }

  @Test
  public void testComplexControlFlow() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();

    Node ifNode = Node.newIf(Token.IF, Node.newBool(true));
    Node assignment1 = Node.newAssignment(Token.ASSIGN, Node.newName("x"),
        Node.newName("y"));
    Node assignment2 = Node.newAssignment(Token.ASSIGN, Node.newName("x"),
        Node.newName("z"));
    Node elseBranch = Node.newBlock();
    cfg.addNode(ifNode);
    cfg.addNode(assignment1);
    cfg.addNode(Node.newGoto(Token.GOTO, elseBranch));
    cfg.addNode(Node.newAssignment(Token.ASSIGN, Node.newName("x"),
        Node.newNumber(1)));
    cfg.addNode(elseBranch);
    cfg.addNode(assignment2);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);
    MustDef input = new MustDef();
    MustDef expected = new MustDef();
    expected.reachingDef.put(jsScope.getVar("x"), new Definition(assignment2));

    MustDef result = analysis.compute(assignment2, input);

    assertSame(expected, result);
  }

  @Test
  public void testEscapedVariables() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();

    Node function = Node.newFunction(Token.FUNCTION, null, null, null,
        Node.newBlock());
    Node variableDeclaration = Node.newVariable("x");
    Node variableUse = Node.newName("x");
    Node assignment = Node.newAssignment(Token.ASSIGN, variableUse,
        Node.newName("y"));
    cfg.addNode(function);
    cfg.addNode(variableDeclaration);
    cfg.addNode(Node.newGoto(Token.GOTO, Node.newName("label1")));
    cfg.addNode(Node.newBlock(new Node[0]));
    cfg.addNode(Node.newLabel("label1"));
    cfg.addNode(assignment);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);
    analysis.escaped.add(jsScope.getVar("x"));

    MustDef input = new MustDef();
    MustDef expected = new MustDef();
    assertNull(expected.reachingDef.get(jsScope.getVar("x")));

    MustDef result = analysis.compute(function, input);

    assertSame(expected, result);
  }

  @Test
  public void testDefinesOnOuterScopeVariable() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();

    Node function = Node.newFunction(Token.FUNCTION, null, null, null,
        Node.newBlock());
    Node variableDeclaration = Node.newVariable("outer");
    Node variableUse = Node.newName("outer");
    Node assignment = Node.newAssignment(Token.ASSIGN, variableUse,
        Node.newName("y"));
    Node use = Node.newName("outer");
    cfg.addNode(function);
    cfg.addNode(variableDeclaration);
    cfg.addNode(Node.newGoto(Token.GOTO, Node.newName("label1")));
    cfg.addNode(Node.newBlock(new Node[0]));
    cfg.addNode(Node.newLabel("label1"));
    cfg.addNode(assignment);
    cfg.addNode(use);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);

    MustDef input = new MustDef();
    MustDef expected = new MustDef();
    assertNull(expected.reachingDef.get(jsScope.getVar("outer")));

    MustDef result = analysis.compute(function, input);

    MustDef output = analysis.compute(use, result);

    assertSame(input, output);
  }

  @Test
  public void testMultipleUses() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();

    Node function = Node.newFunction(Token.FUNCTION, null, null, null,
        Node.newBlock());
    Node variableDeclaration = Node.newVariable("x");
    Node use1 = Node.newName("x");
    Node use2 = Node.newName("x");
    Node assignment = Node.newAssignment(Token.ASSIGN, Node.newName("x"),
        Node.newName("y"));
    cfg.addNode(function);
    cfg.addNode(variableDeclaration);
    cfg.addNode(use1);
    cfg.addNode(use2);
    cfg.addNode(Node.newGoto(Token.GOTO, Node.newName("label1")));
    cfg.addNode(Node.newBlock(new Node[0]));
    cfg.addNode(Node.newLabel("label1"));
    cfg.addNode(assignment);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);

    MustDef input = new MustDef();
    MustDef expected = new MustDef();
    expected.reachingDef.put(jsScope.getVar("x"), new Definition(assignment));

    MustDef result = analysis.compute(assignment, input);

    MustDef output1 = analysis.compute(use1, result);
    MustDef output2 = analysis.compute(use2, result);

    assertSame(expected, output1);
    assertSame(expected, output2);
  }

  @Test
  public void testMultipleUsesInDifferentBlocks() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();

    Node function = Node.newFunction(Token.FUNCTION, null, null, null,
        Node.newBlock());
    Node variableDeclaration = Node.newVariable("x");
    Node use1 = Node.newName("x");
    Node use2 = Node.newName("x");
    Node assignment1 = Node.newAssignment(Token.ASSIGN, Node.newName("x"),
        Node.newName("y"));
    Node assignment2 = Node.newAssignment(Token.ASSIGN, Node.newName("x"),
        Node.newName("z"));
    cfg.addNode(function);
    cfg.addNode(variableDeclaration);
    cfg.addNode(use1);
    cfg.addNode(Node.newBlock(new Node[0]));
    cfg.addNode(use2);
    cfg.addNode(Node.newGoto(Token.GOTO, Node.newName("label1")));
    cfg.addNode(Node.newBlock(new Node[0]));
    cfg.addNode(Node.newLabel("label1"));
    cfg.addNode(assignment1);
    cfg.addNode(Node.newGoto(Token.GOTO, Node.newName("label2")));
    cfg.addNode(Node.newLabel("label2"));
    cfg.addNode(assignment2);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);

    MustDef input = new MustDef();
    MustDef expected = new MustDef();
    expected.reachingDef.put(jsScope.getVar("x"), new Definition(assignment2));

    MustDef result = analysis.compute(assignment2, input);

    MustDef output1 = analysis.compute(use1, result);
    MustDef output2 = analysis.compute(use2, result);

    assertSame(expected, output1);
    assertSame(expected, output2);
  }

  @Test
  public void testComplexControlFlowWithEscapes() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();

    Node function = Node.newFunction(Token.FUNCTION, null, null, null,
        Node.newBlock());
    Node variableDeclaration = Node.newVariable("outer");
    Node variableDeclaration2 = Node.newVariable("inner");
    Node useOuter = Node.newName("outer");
    Node useInner = Node.newName("inner");
    Node assignmentOuter = Node.newAssignment(Token.ASSIGN, useOuter,
        Node.newName("y"));
    Node assignmentInner = Node.newAssignment(Token.ASSIGN, useInner,
        Node.newName("z"));
    Node escape = Node.newName("outer");
    Node conditional = Node.newIf(Token.IF, Node.newBool(true));
    Node gotoLabel1 = Node.newGoto(Token.GOTO, Node.newName("label1"));
    Node label1 = Node.newLabel("label1");
    Node gotoLabel2 = Node.newGoto(Token.GOTO, Node.newName("label2"));
    Node label2 = Node.newLabel("label2");
    Node ifBranch = Node.newBlock(new Node[0]);
    Node elseBranch = Node.newBlock(new Node[0]);
    cfg.addNode(function);
    cfg.addNode(variableDeclaration);
    cfg.addNode(variableDeclaration2);
    cfg.addNode(useOuter);
    cfg.addNode(useInner);
    cfg.addNode(assignmentOuter);
    cfg.addNode(escape);
    cfg.addNode(conditional);
    cfg.addNode(gotoLabel1);
    cfg.addNode(label1);
    cfg.addNode(assignmentInner);
    cfg.addNode(gotoLabel2);
    cfg.addNode(label2);
    cfg.addNode(ifBranch);
    cfg.addNode(elseBranch);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);
    analysis.escaped.add(jsScope.getVar("outer"));

    MustDef input = new MustDef();
    MustDef expected = new MustDef();
    assertNull(expected.reachingDef.get(jsScope.getVar("outer")));

    MustDef result = analysis.compute(function, input);

    MustDef outputUseOuter = analysis.compute(useOuter, result);
    MustDef outputUseInner = analysis.compute(useInner, result);

    assertSame(input, outputUseOuter);
    assertSame(input, outputUseInner);
  }
}