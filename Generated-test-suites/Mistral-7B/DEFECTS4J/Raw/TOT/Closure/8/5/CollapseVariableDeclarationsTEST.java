package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CollapseVariableDeclarations;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;

public class CollapseVariableDeclarationsTest {

  private NodeFactory nodeFactory;
  private CompilerOptions options;

  @Before
  public void setUp() {
    nodeFactory = new NodeFactory();
    options = new CompilerOptions();
  }

  @Test
  public void testSimpleCollapsing() {
    AbstractCompiler compiler = new AbstractCompiler(options);
    CompilerPassManager passManager = new CompilerPassManager(compiler);
    Node root = createNode("var a; var b = 1; var c = 2;");
    passManager.addPass(new CollapseVariableDeclarations(compiler));
    Node result = passManager.process(root, null);
    Node expected = createNode("var a, b = 1, c = 2;");
    NodeTraversal traversal = new NodeTraversal(compiler, result, null);
    Node actual = traversal.getRoot();
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void testCollapsingVarAndAssign() {
    AbstractCompiler compiler = new AbstractCompiler(options);
    CompilerPassManager passManager = new CompilerPassManager(compiler);
    Node root = createNode("var a; a = 1; var b = 2; b = 3;");
    passManager.addPass(new CollapseVariableDeclarations(compiler));
    Node result = passManager.process(root, null);
    Node expected = createNode("var a, b = 2; b = 3; a = 1;");
    NodeTraversal traversal = new NodeTraversal(compiler, result, null);
    Node actual = traversal.getRoot();
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void testCollapsingWithFunctions() {
    AbstractCompiler compiler = new AbstractCompiler(options);
    CompilerPassManager passManager = new CompilerPassManager(compiler);
    Node root = createNode("function foo() {\n" +
        "  var a; var b = 1;\n" +
        "  var c = 2;\n" +
        "}");
    passManager.addPass(new CollapseVariableDeclarations(compiler));
    Node result = passManager.process(root, null);
    Node expected = createNode("function foo() {\n" +
        "  var a, b = 1, c = 2;\n" +
        "}");
    NodeTraversal traversal = new NodeTraversal(compiler, result, null);
    Node actual = traversal.getRoot();
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void testNoCollapsing() {
    AbstractCompiler compiler = new AbstractCompiler(options);
    CompilerPassManager passManager = new CompilerPassManager(compiler);
    Node root = createNode("var a; var b = 1; var c = 2; var d = 3;");
    passManager.addPass(new CollapseVariableDeclarations(compiler));
    Node result = passManager.process(root, null);
    Node expected = createNode("var a; var b = 1; var c = 2; var d = 3;");
    NodeTraversal traversal = new NodeTraversal(compiler, result, null);
    Node actual = traversal.getRoot();
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void testCollapsingWithLetStatement() {
    AbstractCompiler compiler = new AbstractCompiler(options);
    CompilerPassManager passManager = new CompilerPassManager(compiler);
    Node root = createNode("let a; let b = 1; let c = 2;");
    passManager.addPass(new CollapseVariableDeclarations(compiler));
    Node result = passManager.process(root, null);
    Node expected = createNode("let a, b = 1, c = 2;");
    NodeTraversal traversal = new NodeTraversal(compiler, result, null);
    Node actual = traversal.getRoot();
    assertThat(actual).isEqualTo(expected);
  }

  @Test(expected = AssertionError.class)
  public void testCollapsingWithAssignedVariable() {
    AbstractCompiler compiler = new AbstractCompiler(options);
    CompilerPassManager passManager = new CompilerPassManager(compiler);
    Node root = createNode("var a = 1; var b = a;");
    passManager.addPass(new CollapseVariableDeclarations(compiler));
    Node result = passManager.process(root, null);
    NodeTraversal traversal = new NodeTraversal(compiler, result, null);
    Node actual = traversal.getRoot();
    Node expected = createNode("var a = 1, b = a;");
    assertThat(actual).isNotEqualTo(expected);
  }

  @Test(expected = AssertionError.class)
  public void testCollapsingWithBlockScopeVariable() {
    AbstractCompiler compiler = new AbstractCompiler(options);
    CompilerPassManager passManager = new CompilerPassManager(compiler);
    Node root = createNode("{ var a; var b = a; }");
    passManager.addPass(new CollapseVariableDeclarations(compiler));
    Node result = passManager.process(root, null);
    NodeTraversal traversal = new NodeTraversal(compiler, result, null);
    Node actual = traversal.getRoot();
    Node expected = createNode("{ var a; var b = a; }");
    assertThat(actual).isNotEqualTo(expected);
  }

  private Node createNode(String sourceCode) {
    return nodeFactory.parseTree(sourceCode, null, null, null);
  }
}