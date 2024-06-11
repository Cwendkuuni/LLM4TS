package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static com.google.javascript.jscomp.NodeUtil.getLoops;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.MinimizeExitPoints;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.JSTestingFramework;

import junit.framework.Test;
import junit.framework.TestCase;

/**
 * Tests for the MinimizeExitPoints compiler pass.
 *
 * @author johnlenz@google.com (John Lenz)
 */
public class MinimizeExitPointsTest extends TestCase {

  public static void main(String[] args) {
    JunitModeCompilerRunner runner = new JunitModeCompilerRunner();
    runner.runCompilerTests(MinimizeExitPointsTest.class);
  }

  /**
   * Tests the MinimizeExitPoints compiler pass.
   *
   * @author johnlenz@google.com (John Lenz)
   */
  public static class MinimizeExitPointsTestCompiler extends TestCase {

    private Compiler compiler;

    @Override
    protected void setUp() throws Exception {
      CompilerOptions options = new CompilerOptions();
      options.setAssumptions(Assumings.all());
      compiler = new Compiler();
      compiler.setOptions(options);
    }

    public void testMinimizeExitPoints() throws Exception {
      String jsCode = "function foo() {"
          + "for (var i = 0; i < 10; i++) {"
          + " if (i % 2 == 0) {"
          + " break;"
          + " } else {"
          + " continue;"
          + " }"
          + "}"
          + "}";

      Node sourceNode = IR.parseSnippet(jsCode, null).getTree();
      NodeTraversal traversal = new NodeTraversal(compiler);
      Function<Node> minExitPoints = new MinimizeExitPoints(compiler);
      traversal.traverse(sourceNode, minExitPoints);
      Node transformedNode = traversal.getOutput();

      // Check the number of loops.
      int expectedLoops = 1;
      int actualLoops = getLoops(sourceNode).size();
      assertThat(actualLoops).isEqualTo(expectedLoops);

      // Check the number of exits.
      int expectedExits = 0;
      int actualExits = getExits(sourceNode).size();
      assertThat(actualExits).isEqualTo(expectedExits);

      // Check the transformed node.
      String transformedJsCode = IR.toString(transformedNode);
      String expectedJsCode = "function foo() {"
          + "for (var i = 0; i < 10; i++) {"
          + " if (i % 2 != 0) {"
          + " continue;"
          + " }"
          + " if (i % 2 == 0) {"
          + " ;"
          + " }"
          + "}"
          + "}";
      assertThat(transformedJsCode).isEqualTo(expectedJsCode);
    }

    private Iterable<Node> getLoops(Node node) {
      return Iterables.filter(getAllNodes(node), new LoopPredicate());
    }

    private Iterable<Node> getExits(Node node) {
      return Iterables.filter(getAllNodes(node), new ExitPredicate());
    }

    private Iterable<Node> getAllNodes(Node node) {
      NodeTraversal traversal = new NodeTraversal(compiler);
      Function<Node, Void> visitor = new NodeVisitor();
      traversal.traverse(node, visitor);
      return visitor.nodes;
    }

    private static class LoopPredicate implements Predicate<Node> {
      @Override
      public boolean apply(Node node) {
        return node.isFor() || node.isWhile() || node.isDo();
      }
    }

    private static class ExitPredicate implements Predicate<Node> {
      @Override
      public boolean apply(Node node) {
        return node.isBreak() || node.isContinue() || node.isReturn();
      }
    }

    private static class NodeVisitor implements Function<Node, Void> {
      Iterable<Node> nodes = Iterables.asList(this);
      @Override
      public Void apply(Node node) {
        return null;
      }
    }
  }

  /**
   * A simple test case to ensure that the compiler will correctly report an
   * error when no compiler pass is provided.
   */
  public static class NoCompilerPassTest extends MinimizeExitPointsTestCompiler {
    public void testErrorOnNoCompilerPass() throws Exception {
      String jsCode = "function foo() {}";
      Node sourceNode = IR.parseSnippet(jsCode, null).getTree();
      NodeTraversal traversal = new NodeTraversal(compiler);
      Function<Node> emptyPass = new Function<Node>() {};
      assertThrows(IllegalArgumentException.class, () -> {
        traversal.traverse(sourceNode, emptyPass);
      });
    }
  }

  public static Test suite() {
    return new JUnit4TestRunner().suite(MinimizeExitPointsTest.class);
  }
}