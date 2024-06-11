import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.isAssignmentOp;
import static com.google.javascript.jscomp.NodeUtil.isForIn;
import static com.google.javascript.jscomp.NodeUtil.isFunction;
import static com.google.javascript.jscomp.NodeUtil.isName;
import static com.google.javascript.jscomp.NodeUtil.isReturn;
import static com.google.javascript.jscomp.NodeUtil.isVariable;
import static com.google.javascript.jscomp.NodeUtil.matchNode;
import static com.google.javascript.jscomp.NodeUtil.matchPredicate;
import static com.google.javascript.rhino.Token.*;

import org.junit.Test;

import com.google.common.base.Predicate;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.GraphBuilder;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.TestingCompiler;
import com.google.javascript.rhino.NodeFactory;

public class DeadAssignmentsEliminationTest {

  @Test
  public void testDeadAssignmentsElimination() {
    CompilerOptions options = new CompilerOptions();
    options.setMaxInstancesPerFunction(Integer.MAX_VALUE);
    testingCompiler = new TestingCompiler(new GraphBuilder(), options);
    AbstractCompiler compiler = testingCompiler.newCompiler();

    // Test the case: dead_x = dead_y = 1;
    String input = "function foo() { var x, y; x = y = 1; }";
    String expectedOutput = "function foo() { }";

    compileAndTest(compiler, input, expectedOutput);

    // Test the case: y = 1, x = 1, print(x)
    input = "function foo() { var x, y; y = 1; x = 1; print(x); }";
    expectedOutput = "function foo() { y = 1; print(y); }";

    compileAndTest(compiler, input, expectedOutput);

    // Test the case: y = 1, x = 1, x = 2, print(x)
    input = "function foo() { var x, y; y = 1; x = 1; x = 2; print(x); }";
    expectedOutput = "function foo() { y = 1; x = 2; print(x); }";

    compileAndTest(compiler, input, expectedOutput);

    // Test the case: y = 1, x = 1, x = 2, print(x) with a different order of statements
    input = "function foo() { var x, y; x = 1; y = 1; x = 2; print(x); }";
    expectedOutput = "function foo() { y = 1; x = 2; print(x); }";

    compileAndTest(compiler, input, expectedOutput);

    // Test the case: if (a = C && X = a) {..} ; .......; a = S;
    input = "function foo() { var a, X; if (a = C && X = a) { var b; } a = S; }";
    expectedOutput = "function foo() { if (C = C) { var b; } a = S; }";

    compileAndTest(compiler, input, expectedOutput);

    // Test the case: if (a = C && X = a) {..} ; .......; a = S; with
    // a different order of statements in the if statement
    input = "function foo() { var a, X; if (X = a && a = C) { var b; } a = S; }";
    expectedOutput = "function foo() { if (C = C) { var b; } a = S; }";

    compileAndTest(compiler, input, expectedOutput);

    // Test the case: for (var i = 0; i < 10; i++) { i = i + 1; }
    input = "function foo() { for (var i = 0; i < 10; i = i + 1) {} }";
    expectedOutput = "function foo() { for (var i = 0; i < 10; ) {} }";

    compileAndTest(compiler, input, expectedOutput);

    // Test the case: for (var i = 0; i < 10; i++) { i = i + 1; } with
    // different initialization value
    input = "function foo() { for (var i = 10; i < 20; i = i + 1) {} }";
    expectedOutput = "function foo() { for (var i = 10; i < 20; ) {} }";

    compileAndTest(compiler, input, expectedOutput);
  }

  private void compileAndTest(AbstractCompiler compiler, String input,
      String expectedOutput) {
    compiler.addPasses(new CompilerPassManager()
        .addPass(new DeadAssignmentsElimination(compiler)));

    Node root = compiler.compile(input, null, null).getRootNode();
    String output = NodePrinter.printTree(root);

    assertEquals(expectedOutput, output);
  }

  private AbstractCompiler testingCompiler;
  private static Predicate<Node> isAssignmentOrIncrementDecrement =
      new Predicate<Node>() {
        @Override
        public boolean apply(Node n) {
          return isAssignmentOp(n) || (n.getType() == INC || n.getType() == DEC);
        }
      };

  private static Predicate<Node> isAnyFunction = new Predicate<Node>() {
        @Override
        public boolean apply(Node n) {
          return isFunction(n);
        }
      };

  private static Predicate<Node> isVariableOrAssignment =
      new Predicate<Node>() {
        @Override
        public boolean apply(Node n) {
          return (isVariable(n) || isAssignmentOrIncrementDecrement.apply(n));
        }
      };

  private static Predicate<Node> isAssignToVariable =
      new Predicate<Node>() {
        @Override
        public boolean apply(Node n) {
          return isVariable(n.getFirstChild()) && isAssignmentOrIncrementDecrement.apply(n);
        }
      };

  private static Node matchAssignToVariable(Node n) {
    return matchNode(n, isAssignToVariable);
  }
}