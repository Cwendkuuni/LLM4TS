package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static com.google.javascript.rhino.NodeFactory.createNode;
import static com.google.javascript.rhino.NodeFactory.createVar;
import static com.google.javascript.rhino.NodeFactory.createVariableStatement;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CollapseVariableDeclarations;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Scope;

import org.junit.Before;
import org.junit.Test;

public class CollapseVariableDeclarationsTest {
  private Compiler compiler;

  @Before
  public void setup() {
    compiler = new Compiler();
  }

  @Test
  public void testCollapsingDeclarations() {
    Node root = createVariableStatement(
        "var a, b = 1, c = 2, d = 3, e = 4, f = 5, g = 6, h = 7, i = 8;");

    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);
    collapseVariableDeclarations.process(null, root);

    Node result = compiler.getIntermediateRepresentation();

    Node expected = createVariableStatement(
        "var a, b = 1, c = 2, d = 3, e = 4, f = 5, g = 6, h = 7, i = 8;"); // no change for now

    assertCodeEqual(expected, result);

    Node inputWithAssignment = createVariableStatement(
        "var a; a = 1; var b = 2; b = 3; var c = 4; c = 5;");

    collapseVariableDeclarations.process(null, inputWithAssignment);

    expected = createVariableStatement(
        "var a, b = 1, c = 5;");

    assertCodeEqual(expected, result);

    Node inputWithIfStatement = createVariableStatement(
        "if (true) { " +
        "  var a; " +
        "} else { " +
        "  var b = 1; " +
        "} " +
        "var c = 2;");

    collapseVariableDeclarations.process(null, inputWithIfStatement);

    expected = createVariableStatement(
        "if (true) { } else { var b = 1; } var c = 2;");

    assertCodeEqual(expected, result);
  }

  private void assertCodeEqual(Node expected, Node actual) {
    String expectedCode = NodePrinter.printComplex(expected);
    String actualCode = NodePrinter.printComplex(actual);
    assertThat(expectedCode).isEqualTo(actualCode);
  }
}

class NodePrinter {
  public static String printComplex(Node node) {
    return NodeUtil.printNode(node).orElseThrow();
  }
}

class NodeUtil {
  public static String printNode(Node node) {
    // Your printNode implementation here
  }

  public static Optional<String> orElseThrow() {
    return Optional.empty();
  }
}