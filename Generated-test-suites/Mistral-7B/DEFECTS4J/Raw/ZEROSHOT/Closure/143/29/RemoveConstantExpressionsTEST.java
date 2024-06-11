import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.List;

public class RemoveConstantExpressionsTest {

  private Compiler compiler;

  @org.junit.Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions();
    compiler = new Compiler(options);
  }

  @org.junit.Test
  public void testRemoveConstantExpressions() {
    String sourceCode = "function foo() { return 1; } function bar() { return 2; } "
        + "function baz() { return 3; } "
        + "function qux() { return 4; } "
        + "1 + foo() + bar() + baz() + qux();";

    Node root = NodeFactory.createProgram(NodeFactory.createScript(sourceCode));

    RemoveConstantExpressions removeConstantExpressions =
        new RemoveConstantExpressions(compiler);
    removeConstantExpressions.process(null, root);

    Node expectedOutput = NodeFactory.createProgram(
        NodeFactory.createScript(
            "function foo() { return 1; } function bar() { return 2; } "
                + "function baz() { return 3; } function qux() { return 4; } "
                + "foo();bar();baz();qux();"));

    List<Node> differences = NodeUtil.findDifferences(root, expectedOutput);

    assertFalse("Unexpected code changes.", differences.isEmpty());

    for (Node difference : differences) {
      Node parent = difference.getParent();
      int index = List.indexOf(parent.getChildrenList(), difference);

      Node expectedChild = expectedOutput.getChild(index);

      Node actualChild = root.getChild(index);

      NodeUtil.assertEqualNodes("Unexpected difference", expectedChild, actualChild);
    }
  }

  @org.junit.Test
  public void testRemoveConstantExpressionsWithSideEffects() {
    String sourceCode = "function foo() { console.log('foo'); return 1; } "
        + "function bar() { console.log('bar'); return 2; } "
        + "1 + foo() + bar();";

    Node root = NodeFactory.createProgram(NodeFactory.createScript(sourceCode));

    RemoveConstantExpressions removeConstantExpressions =
        new RemoveConstantExpressions(compiler);
    removeConstantExpressions.process(null, root);

    Node expectedOutput = NodeFactory.createProgram(
        NodeFactory.createScript(
            "function foo() { console.log('foo'); } function bar() { console.log('bar'); } "
                + "foo();bar();"));

    List<Node> differences = NodeUtil.findDifferences(root, expectedOutput);

    assertFalse("Unexpected code changes.", differences.isEmpty());

    for (Node difference : differences) {
      Node parent = difference.getParent();
      int index = List.indexOf(parent.getChildrenList(), difference);

      Node expectedChild = expectedOutput.getChild(index);

      Node actualChild = root.getChild(index);

      NodeUtil.assertEqualNodes("Unexpected difference", expectedChild, actualChild);
    }
  }

  @org.junit.Test
  public void testRemoveConstantExpressionsWithComplexExpression() {
    String sourceCode = "function foo() { return 1 + 2 * 3; } "
        + "1 + foo() + foo();";

    Node root = NodeFactory.createProgram(NodeFactory.createScript(sourceCode));

    RemoveConstantExpressions removeConstantExpressions =
        new RemoveConstantExpressions(compiler);
    removeConstantExpressions.process(null, root);

    Node expectedOutput = NodeFactory.createProgram(
        NodeFactory.createScript(
            "function foo() { return 1 + 2 * 3; } function foo() { return 1 + 2 * 3; } "
                + "foo();foo();"));

    List<Node> differences = NodeUtil.findDifferences(root, expectedOutput);

    assertFalse("Unexpected code changes.", differences.isEmpty());

    for (Node difference : differences) {
      Node parent = difference.getParent();
      int index = List.indexOf(parent.getChildrenList(), difference);

      Node expectedChild = expectedOutput.getChild(index);

      Node actualChild = root.getChild(index);

      NodeUtil.assertEqualNodes("Unexpected difference", expectedChild, actualChild);
    }
  }

  @org.junit.Test
  public void testRemoveConstantExpressionsWithAndExpression() {
    String sourceCode = "function foo() { return 1; } "
        + "1 && foo();";

    Node root = NodeFactory.createProgram(NodeFactory.createScript(sourceCode));

    RemoveConstantExpressions removeConstantExpressions =
        new RemoveConstantExpressions(compiler);
    removeConstantExpressions.process(null, root);

    Node expectedOutput = NodeFactory.createProgram(
        NodeFactory.createScript("function foo() { return 1; } foo();"));

    List<Node> differences = NodeUtil.findDifferences(root, expectedOutput);

    assertFalse("Unexpected code changes.", differences.isEmpty());

    for (Node difference : differences) {
      Node parent = difference.getParent();
      int index = List.indexOf(parent.getChildrenList(), difference);

      Node expectedChild = expectedOutput.getChild(index);

      Node actualChild = root.getChild(index);

      NodeUtil.assertEqualNodes("Unexpected difference", expectedChild, actualChild);
    }
  }

  @org.junit.Test
  public void testRemoveConstantExpressionsWithOrExpression() {
    String sourceCode = "function foo() { return 1; } "
        + "1 || foo();";

    Node root = NodeFactory.createProgram(NodeFactory.createScript(sourceCode));

    RemoveConstantExpressions removeConstantExpressions =
        new RemoveConstantExpressions(compiler);
    removeConstantExpressions.process(null, root);

    Node expectedOutput = NodeFactory.createProgram(
        NodeFactory.createScript("1;function foo() { return 1; } "));

    List<Node> differences = NodeUtil.findDifferences(root, expectedOutput);

    assertFalse("Unexpected code changes.", differences.isEmpty());

    for (Node difference : differences) {
      Node parent = difference.getParent();
      int index = List.indexOf(parent.getChildrenList(), difference);

      Node expectedChild = expectedOutput.getChild(index);

      Node actualChild = root.getChild(index);

      NodeUtil.assertEqualNodes("Unexpected difference", expectedChild, actualChild);
    }
  }
}