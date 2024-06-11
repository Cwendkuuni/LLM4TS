package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import com.google.common.collect.Sets;
import org.junit.Test;

import java.util.Set;

public class MustBeReachingVariableDefTest {

  @Test
  public void testBasicDefinition() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();

    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node root = new Node(Token.BLOCK);
    root.setLineNumber(1);
    Node defNode = new Node(Token.ASSIGN);
    defNode.setLineNumber(1);
    defNode.setNext(new Node(Token.NAME, "x"));
    defNode.setNext(new Node(Token.ASSIGN, new Node(Token.NUM, 5)));

    Node useNode = new Node(Token.NAME, "x");
    useNode.setLineNumber(2);

    root.setFirstChild(defNode);
    root.setNext(useNode);

    MustDef output = analyzer.analyze(root);

    Definition expectedDef = new Definition(defNode);
    assertEquals(expectedDef, output.reachingDef.get(jsScope.getVar("x")));
  }

  @Test
  public void testMultipleDefinitions() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();

    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node root = new Node(Token.BLOCK);
    root.setLineNumber(1);
    Node defNode1 = new Node(Token.ASSIGN);
    defNode1.setLineNumber(1);
    defNode1.setNext(new Node(Token.NAME, "x"));
    defNode1.setNext(new Node(Token.ASSIGN, new Node(Token.NUM, 5)));

    Node defNode2 = new Node(Token.ASSIGN);
    defNode2.setLineNumber(3);
    defNode2.setNext(new Node(Token.NAME, "x"));
    defNode2.setNext(new Node(Token.ASSIGN, new Node(Token.NUM, 10)));

    Node useNode = new Node(Token.NAME, "x");
    useNode.setLineNumber(4);

    root.setFirstChild(defNode1);
    root.setNext(defNode2);
    root.setNext(useNode);

    MustDef output = analyzer.analyze(root);

    Definition expectedDef1 = new Definition(defNode1);
    Definition expectedDef2 = new Definition(defNode2);
    assertNull(output.reachingDef.get(jsScope.getVar("x")));
    assertEquals(expectedDef1, output.reachingDef.get(jsScope.getVar("x")).get(0));
    assertEquals(expectedDef2, output.reachingDef.get(jsScope.getVar("x")).get(1));
  }

  @Test
  public void testMultipleUses() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();

    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node root = new Node(Token.BLOCK);
    root.setLineNumber(1);

    Node defNode = new Node(Token.ASSIGN);
    defNode.setLineNumber(1);
    defNode.setNext(new Node(Token.NAME, "x"));
    defNode.setNext(new Node(Token.ASSIGN, new Node(Token.NUM, 5)));

    Node useNode1 = new Node(Token.NAME, "x");
    useNode1.setLineNumber(2);

    Node useNode2 = new Node(Token.NAME, "x");
    useNode2.setLineNumber(3);

    root.setFirstChild(defNode);
    root.setNext(useNode1);
    root.setNext(useNode2);

    MustDef output = analyzer.analyze(root);

    Definition expectedDef = new Definition(defNode);
    assertEquals(expectedDef, output.reachingDef.get(jsScope.getVar("x")));
  }

  @Test
  public void testEscapedVariable() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();

    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Set<Var> escaped = Sets.newHashSet();
    jsScope.put("globalVar", new Var(new Node(Token.NAME, "globalVar"), null, jsScope));
    escaped.add(jsScope.getVar("globalVar"));
    analyzer.computeEscaped(jsScope, escaped, compiler);

    Node root = new Node(Token.BLOCK);
    root.setLineNumber(1);

    Node defNode1 = new Node(Token.ASSIGN);
    defNode1.setLineNumber(1);
    defNode1.setNext(new Node(Token.NAME, "x"));
    defNode1.setNext(new Node(Token.ASSIGN, new Node(Token.NUM, 5)));

    Node useNode1 = new Node(Token.NAME, "x");
    useNode1.setLineNumber(2);

    Node assignGlobal = new Node(Token.ASSIGN);
    assignGlobal.setLineNumber(3);
    assignGlobal.setNext(new Node(Token.NAME, "globalVar"));
    assignGlobal.setNext(new Node(Token.ASSIGN, useNode1));

    root.setFirstChild(defNode1);
    root.setNext(assignGlobal);
    root.setNext(useNode1);

    MustDef output = analyzer.analyze(root);

    assertNull(output.reachingDef.get(jsScope.getVar("x")));
  }

  @Test
  public void testDependence() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();

    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node root = new Node(Token.BLOCK);
    root.setLineNumber(1);

    Node defNode = new Node(Token.ASSIGN);
    defNode.setLineNumber(1);
    defNode.setNext(new Node(Token.NAME, "x"));
    defNode.setNext(new Node(Token.ASSIGN, new Node(Token.NUM, 5)));

    Node rValueNode = new Node(Token.NAME, "y");
    rValueNode.setLineNumber(2);

    Node useNode = new Node(Token.NAME, "x");
    useNode.setLineNumber(3);

    root.setFirstChild(defNode);
    root.setNext(rValueNode);
    root.setNext(useNode);

    analyzer.computeMustDef(defNode, defNode, new MustDef(), false);

    Definition expectedDef = new Definition(defNode);
    Definition def = analyzer.reachingDef.get(jsScope.getVar("x"));
    assertEquals(expectedDef, def);
    assertEquals(Sets.newHashSet(), def.depends);

    MustDef output = analyzer.analyze(root);

    Definition outputDef = output.reachingDef.get(jsScope.getVar("x"));
    assertEquals(expectedDef, outputDef);

    assertFalse(outputDef.depends.isEmpty());
    assertTrue(outputDef.depends.contains(jsScope.getVar("y")));
  }
}