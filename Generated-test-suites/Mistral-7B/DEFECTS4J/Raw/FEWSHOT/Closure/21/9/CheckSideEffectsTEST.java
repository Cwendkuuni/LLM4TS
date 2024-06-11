package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.CheckLevel.WARNING;
import static com.google.javascript.jscomp.Checker.checkRhino;
import static com.google.javascript.rhino.NodeType.CALL_EXPR;
import static com.google.javascript.rhino.NodeType.NAME;
import static com.google.javascript.rhino.NodeType.VOID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.NodeOperations;
import com.google.javascript.rhino.Token;

import java.util.List;

/**
 * Test for {@code CheckSideEffects} class.
 */
public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects() {
    AbstractCompiler compiler = new CompilerOptions().getCompiler();
    CompilerInput input = new CompilerInput(new SourceFile("test.js"));

    String sourceCode = "var s = \"this string is \";\n" +
        "var x = foo();\n" +
        "// comment out the next line\n" +
        "foo();;\n" +
        "function foo() {\n" +
        "  return 1;\n" +
        "}\n";

    checkRhino(compiler, new CheckLevel(WARNING), input, sourceCode);

    List<Node> problemNodes = Lists.newArrayList();
    CheckSideEffects checker = new CheckSideEffects(compiler, new CheckLevel(WARNING), false);
    NodeTraversal.traverse(compiler, root(input), checker);

    checkSideEffects(checker, problemNodes);

    assertEquals(3, problemNodes.size());

    Node xAssignNode = findNode(input, "x =");
    assertNull(xAssignNodes(xAssignNode).get(0));
    Node fooCallNode1 = findNode(input, "foo()");
    assertNull(fooCallNodes(fooCallNode1).get(0));
    Node fooCallNode2 = findNode(input, "foo();");
    assertNull(fooCallNodes(fooCallNode2).get(0));
  }

  @Test
  public void testProtectSideEffectFreeCode() {
    AbstractCompiler compiler = new CompilerOptions().getCompiler();
    CompilerInput input = new CompilerInput(new SourceFile("test.js"));

    String sourceCode = "var s = \"this string is \";\n" +
        "var x = foo();\n" +
        "// comment out the next line\n" +
        "foo();;\n" +
        "function foo() {\n" +
        "  return 1;\n" +
        "}\n";

    CheckSideEffects checker = new CheckSideEffects(compiler, new CheckLevel(WARNING), true);
    NodeTraversal.traverse(compiler, root(input), checker);

    checkSideEffects(checker, problemNodes());

    assertEquals(0, problemNodes().size());

    Node sAssignNode = findNode(input, "s =");
    Node jscompProtectorFn = findNodeWithName(input, "JSCOMPILER_PRESERVE");
    Node sAssignReplacementNode = findNodeWithName(input, "JSCOMPILER_PRESERVE")
        .getFirstChild()
        .getLastChild();
    assertEquals(sAssignNode, sAssignReplacementNode.getNext());
  }

  @Test
  public void testRemoveProtection() {
    AbstractCompiler compiler = new CompilerOptions().getCompiler();
    CompilerInput input = new CompilerInput(new SourceFile("test.js"));

    String sourceCode = "var s = \"this string is \";\n" +
        "var x = foo();\n" +
        "// comment out the next line\n" +
        "foo();;\n" +
        "function foo() {\n" +
        "  return 1;\n" +
        "}\n";

    CheckSideEffects checker = new CheckSideEffects(compiler, new CheckLevel(WARNING), true);
    NodeTraversal.traverse(compiler, root(input), checker);

    problemNodes(checker).forEach(this::removeProtection);

    String expectedSourceCode = "var s = \"this string is \";\n" +
        "var x = foo();\n" +
        "// comment out the next line\n" +
        "foo();;\n" +
        "function foo() {\n" +
        "  return 1;\n" +
        "}\n";

    assertEquals(expectedSourceCode, getSourceCode(input));
  }

  private void checkSideEffects(CheckSideEffects checker, List<Node> problemNodes) {
    problemNodes.clear();
    checker.process(null, null);
    checker.protectSideEffects();
  }

  private List<Node> problemNodes() {
    return problemNodes(null);
  }

  private List<Node> problemNodes(CheckSideEffects checker) {
    if (checker == null) {
      checker = new CheckSideEffects(new CompilerOptions().getCompiler(), new CheckLevel(WARNING), true);
    }
    return checker.problemNodes;
  }

  private void removeProtection(Node node) {
    Node parent = node.getParent();
    if (parent != null) {
      Node replacement = NodeOperations.cloneTree(node);
      parent.replaceChild(node, replacement);
      replacement.addChildToBack(node);
      node.detach();
    }
  }

  private Node findNode(CompilerInput input, String value) {
    return findNode(input.getAstRoot(input.getCompiler()), value);
  }

  private Node findNode(Node node, String value) {
    if (node.isString() && node.getString().equals(value)) {
      return node;
    }
    for (Node child : node.getChildren()) {
      Node result = findNode(child, value);
      if (result != null) {
        return result;
      }
    }
    return null;
  }

  private List<Node> findNodeWithName(CompilerInput input, String name) {
    return findNodeWithName(input.getAstRoot(input.getCompiler()), name);
  }

  private List<Node> findNodeWithName(Node node, String name) {
    if (node.isName() && node.getString().equals(name)) {
      return Lists.newArrayList(node);
    }
    for (Node child : node.getChildren()) {
      List<Node> result = findNodeWithName(child, name);
      if (!result.isEmpty()) {
        return result;
      }
    }
    return Lists.newArrayList();
  }

  private List<Node> fooCallNodes(Node node) {
    return NodeUtil.findDescendantsOfType(node, CALL_EXPR);
  }

  private List<Node> xAssignNodes(Node node) {
    return NodeUtil.findDescendantsOfType(node, NAME, "x =");
  }

  private Node root(CompilerInput input) {
    return input.getAstRoot(input.getCompiler());
  }

  private String getSourceCode(CompilerInput input) {
    return input.getSourceCode();
  }
}