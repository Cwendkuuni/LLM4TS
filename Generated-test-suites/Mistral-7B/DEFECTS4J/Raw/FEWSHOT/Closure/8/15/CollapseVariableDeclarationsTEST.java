package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.Set;

/**
 * Test cases for CollapseVariableDeclarations class.
 */
@RunWith(JUnit4.class)
public class CollapseVariableDeclarationsTest {

  private static Compiler compiler;
  private static NodeFactory nodeFactory;

  static {
    compiler = new Compiler();
    compiler.setOptions(new CompilerOptions());
    nodeFactory = compiler.getFactory();
  }

  @Test
  public void testCollapseSingleVariableDeclarations() {
    Scope scope = new Scope(nodeFactory, null);
    Node root = createNode("var a; var b = 1; var c = 2;");
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    List<Node> variables = Lists.newArrayList(root.getChild(0));
    assertEquals(1, variables.size());
    assertEquals("var a = 1, c = 2;", variables.get(0).toString());
  }

  @Test
  public void testCollapseMultipleVariableDeclarations() {
    Scope scope = new Scope(nodeFactory, null);
    Node root = createNode("var a; var b = 1; var c = 2; var d = 3;");
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    List<Node> variables = Lists.newArrayList(root.getChild(0));
    assertEquals(1, variables.size());
    assertEquals("var a = 1, b = 3, c = 2;", variables.get(0).toString());
  }

  @Test
  public void testCollapseVariableAssignments() {
    Scope scope = new Scope(nodeFactory, null);
    Node root = createNode("var a; a = true; b = true; var c = true;");
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    List<Node> variables = Lists.newArrayList(root.getChild(0));
    assertEquals(1, variables.size());
    assertEquals("var a = true, c = true;", variables.get(0).toString());
  }

  @Test
  public void testCollapseVariableDeclarationsWithinFunction() {
    Scope scope = new Scope(nodeFactory, null);
    Node root = createNode("function foo() {\n" +
                          "  var a;\n" +
                          "  var b = 1;\n" +
                          "  var c = 2;\n" +
                          "  return a + b + c;\n" +
                          "}");
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    List<Node> variables = Lists.newArrayList(root.getChild(0));
    assertEquals(1, variables.size());
    assertEquals("function foo() {\n" +
                 "  var a, b = 1, c = 2;\n" +
                 "  return a + b + c;\n" +
                 "}", variables.get(0).toString());
  }

  @Test
  public void testCollapseVariableDeclarationsWithinConditional() {
    Scope scope = new Scope(nodeFactory, null);
    Node root = createNode("if (true) {\n" +
                          "  var a;\n" +
                          "  var b = 1;\n" +
                          "  var c = 2;\n" +
                          "} else {\n" +
                          "  var d = 1;\n" +
                          "}");
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    List<Node> variablesA = Lists.newArrayList(root.getChild(1));
    List<Node> variablesB = Lists.newArrayList(root.getChild(3));

    assertEquals(1, variablesA.size());
    assertEquals("if (true) {\n" +
                 "  var a, b = 1, c = 2;\n" +
                 "}", variablesA.get(0).toString());

    assertEquals(1, variablesB.size());
    assertEquals("else {\n" +
                 "  var d = 1;\n" +
                 "}", variablesB.get(0).toString());
  }

  @Test
  public void testCollapseEmptyDeclarationBlock() {
    Scope scope = new Scope(nodeFactory, null);
    Node root = createNode("var a; var b;");
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    List<Node> variables = Lists.newArrayList(root.getChild(0));
    assertEquals(1, variables.size());
    assertNull(variables.get(0).toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCollapseVariableDeclarationsDuringNormalization() {
    Scope scope = new Scope(nodeFactory, null);
    Node root = createNode("var a; var b;");
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);
    compiler.getLifeCycleStage().normalize(); // This should throw an exception
  }

  private Node createNode(String code) {
    return compiler.parseTree(code, null, null, null).getRootNode();
  }
}