package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.traverse.AbstractPostOrderCallback;
import com.google.javascript.rhino.NodeTraversal;

import java.util.List;
import java.util.Set;

/**
 * Test cases for CollapseVariableDeclarations class.
 */
public class CollapseVariableDeclarationsTest {

  @Test
  public void testCollapsingVariableDeclarations() {
    AbstractCompiler compilerMock = mock(AbstractCompiler.class);
    Node root = createTestNode("var a; var b = 1; var c = 2;");
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compilerMock);

    pass.process(null, root);

    Node expected = createTestNode("var a, b = 1, c = 2;");
    verify(compilerMock, times(1)).reportCodeChange();
    assertEquals(expected.toString(), root.toString());
  }

  @Test
  public void testCollapsingVariableDeclarationsWithAssignments() {
    AbstractCompiler compilerMock = mock(AbstractCompiler.class);
    Node root = createTestNode("var a; a = true; var b = true;");
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compilerMock);

    pass.process(null, root);

    Node expected = createTestNode("var a, b = true;");
    verify(compilerMock, times(1)).reportCodeChange();
    assertEquals(expected.toString(), root.toString());
  }

  @Test
  public void testCollapsingVariableDeclarationsWithinFunction() {
    AbstractCompiler compilerMock = mock(AbstractCompiler.class);
    Node root = createTestNode("function test() {\n" +
        "  var a;\n" +
        "  var b = 1;\n" +
        "  var c = 2;\n" +
        "}");
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compilerMock);

    pass.process(null, root);

    Node expected = createTestNode("function test() {\n" +
        "  var a, b = 1, c = 2;\n" +
        "}");
    verify(compilerMock, times(1)).reportCodeChange();
    assertEquals(expected.toString(), root.toString());
  }

  @Test
  public void testCollapsingVariableDeclarationsWithinConditionalStatement() {
    AbstractCompiler compilerMock = mock(AbstractCompiler.class);
    Node root = createTestNode("if (true) {\n" +
        "  var a;\n" +
        "  var b = 1;\n" +
        "} else {\n" +
        "  var c;\n" +
        "  var d = 2;\n" +
        "}");
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compilerMock);

    pass.process(null, root);

    Node expected = createTestNode("if (true) {\n" +
        "  var a, b = 1;\n" +
        "} else {\n" +
        "  var c, d = 2;\n" +
        "}");
    verify(compilerMock, times(1)).reportCodeChange();
    assertEquals(expected.toString(), root.toString());
  }

  @Test
  public void testCollapsingVariableDeclarationsWithinLoop() {
    AbstractCompiler compilerMock = mock(AbstractCompiler.class);
    Node root = createTestNode("for (var i = 0; i < 10; i++) {\n" +
        "  var a;\n" +
        "  var b = 1;\n" +
        "}");
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compilerMock);

    pass.process(null, root);

    Node expected = createTestNode("for (var i = 0; i < 10; i++) {\n" +
        "  var a, b = 1;\n" +
        "}");
    verify(compilerMock, times(1)).reportCodeChange();
    assertEquals(expected.toString(), root.toString());
  }

  @Test
  public void testCollapsingVariableDeclarationsWithinSwitchStatement() {
    AbstractCompiler compilerMock = mock(AbstractCompiler.class);
    Node root = createTestNode("switch (1) {\n" +
        "  case 1:\n" +
        "    var a;\n" +
        "    var b = 1;\n" +
        "    break;\n" +
        "  case 2:\n" +
        "    var c;\n" +
        "    var d = 2;\n" +
        "    break;\n" +
        "}");
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compilerMock);

    pass.process(null, root);

    Node expected = createTestNode("switch (1) {\n" +
        "  case 1:\n" +
        "    var a, b = 1;\n" +
        "    break;\n" +
        "  case 2:\n" +
        "    var c, d = 2;\n" +
        "    break;\n" +
        "}");
    verify(compilerMock, times(1)).reportCodeChange();
    assertEquals(expected.toString(), root.toString());
  }

  private Node createTestNode(String code) {
    Node jsRoot = new Node(Token.BLOCK, 0);
    Node jsCode = new Node(Token.BLOCK, 1);
    jsCode.setLineNumber(0);

    for (String line : code.split("\n")) {
      Node jsLine = parseLine(line);
      jsCode.addChildToBack(jsLine);
    }

    jsRoot.addChildToBack(jsCode);
    return jsRoot;
  }

  private Node parseLine(String line) {
    NodeTraversal nodeTraversal = new NodeTraversal(new AbstractPostOrderCallback() {});
    Node parsedCode = nodeTraversal.parse(new StringBuilder(line), 0, line.length(), null, null);
    return parsedCode;
  }
}