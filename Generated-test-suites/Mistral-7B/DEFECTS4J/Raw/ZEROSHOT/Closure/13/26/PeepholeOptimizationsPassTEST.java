import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodePattern.*;
import static com.google.javascript.jscomp.NodeType.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPeepholeOptimization;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerTestCase;
import com.google.javascript.rhino.Node;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Test file for PeepholeOptimizationsPass class.
 */
public class PeepholeOptimizationsPassTest {

  private static AbstractCompiler createCompiler() {
    CompilerOptions options = new CompilerOptions(Collections.emptyMap());
    return new AbstractCompiler(options) {
      @Override
      public CompilerPass getPass(String passName) {
        if (PASS_ID_PEEPHOLE_OPTIMIZATIONS.equals(passName)) {
          return new PeepholeOptimizationsPass(this,
                  new AbstractPeepholeOptimization[0]) {
            @Override
            public void visit(Node n) {
              // No-op implementation of visit method.
            }
          };
        }
        return null;
      }
    };
  }

  @org.junit.Test
  public void testPeepholeOptimizationsPassEmpty() {
    AbstractCompiler compiler = createCompiler();
    CompilerPass pass = compiler.getPass(PASS_ID_PEEPHOLE_OPTIMIZATIONS);
    assertNotNull(pass);

    Node root = createNode("function foo() {}");
    pass.process(null, root);
    assertEquals(0, root.getChildCount());
  }

  @org.junit.Test
  public void testPeepholeOptimizationsPassSingleNode() {
    AbstractCompiler compiler = createCompiler();
    AbstractPeepholeOptimization constantFolding = new ConstantFolding();
    PeepholeOptimizationsPass pass =
        new PeepholeOptimizationsPass(compiler, constantFolding);

    Node root = createNode("function foo(x) { return 2 * x; }");
    pass.process(null, root);

    Node expected = createNode("function foo(x) { return 4; }");
    Node actual = root;
    assertNodeEquals(expected, actual);
  }

  @org.junit.Test
  public void testPeepholeOptimizationsPassMultipleNodes() {
    AbstractCompiler compiler = createCompiler();
    AbstractPeepholeOptimization constantFolding = new ConstantFolding();
    AbstractPeepholeOptimization deadCodeElimination = new DeadCodeElimination();
    PeepholeOptimizationsPass pass =
        new PeepholeOptimizationsPass(compiler, constantFolding, deadCodeElimination);

    Node root = createNode("function foo(x) {"
        + " if (x > 0 && x < 10) {"
        + " return x * 2;"
        + " } else {"
        + " return 1;"
        + " }"
        + "}");

    pass.process(null, root);

    Node expected = createNode("function foo(x) {"
        + " if (x > 0 && x < 10) {"
        + " return x < 5 ? x * 2 : 1;"
        + " } else {"
        + " return 1;"
        + " }"
        + "}");
    Node actual = root;
    assertNodeEquals(expected, actual);
  }

  @org.junit.Test
  public void testPeepholeOptimizationsPassSkipsFunctionCalls() {
    AbstractCompiler compiler = createCompiler();
    AbstractPeepholeOptimization constantFolding = new ConstantFolding();
    PeepholeOptimizationsPass pass =
        new PeepholeOptimizationsPass(compiler, constantFolding);

    Node root = createNode("function foo(x) {"
        + " return Math.pow(x, 2);"
        + "}");

    pass.process(null, root);

    Node expected = root;
    Node actual = root;
    assertNodeEquals(expected, actual);
  }

  @org.junit.Test
  public void testPeepholeOptimizationsPassSkipsScripts() {
    AbstractCompiler compiler = createCompiler();
    AbstractPeepholeOptimization constantFolding = new ConstantFolding();
    PeepholeOptimizationsPass pass =
        new PeepholeOptimizationsPass(compiler, constantFolding);

    Node root = createNode("(function foo() {})()");
    pass.process(null, root);

    Node expected = root;
    Node actual = root;
    assertNodeEquals(expected, actual);
  }

  @org.junit.Test
  public void testPeepholeOptimizationsPassSkipsSkippedNodes() {
    AbstractCompiler compiler = createCompiler();
    AbstractPeepholeOptimization constantFolding = new ConstantFolding();
    PeepholeOptimizationsPass pass =
        new PeepholeOptimizationsPass(compiler, constantFolding);

    Node root = createNode("function foo() {"
        + " if (false) {"
        + " // skipped"
        + " return 1;"
        + " } else {"
        + " return 2;"
        + " }"
        + "}");

    pass.process(null, root);

    Node expected = root;
    Node actual = root;
    assertNodeEquals(expected, actual);
  }

  @org.junit.Test
  public void testPeepholeOptimizationsPassSkipsTraversedNodes() {
    AbstractCompiler compiler = createCompiler();
    AbstractPeepholeOptimization constantFolding = new ConstantFolding();
    AbstractPeepholeOptimization deadCodeElimination = new DeadCodeElimination();
    PeepholeOptimizationsPass pass =
        new PeepholeOptimizationsPass(compiler, constantFolding, deadCodeElimination);

    Node root = createNode("function foo() {"
        + " if (true) {"
        + " return 1;"
        + " } else {"
        + " return 2;"
        + " }"
        + "}");

    pass.process(null, root);

    // The second traversal should not be performed
    // because the function has been optimized already.
    pass.process(null, root);

    Node expected = root;
    Node actual = root;
    assertNodeEquals(expected, actual);
  }

  @org.junit.Test
  public void testPeepholeOptimizationsPassWithNestedFunctions() {
    AbstractCompiler compiler = createCompiler();
    AbstractPeepholeOptimization constantFolding = new ConstantFolding();
    AbstractPeepholeOptimization deadCodeElimination = new DeadCodeElimination();
    PeepholeOptimizationsPass pass =
        new PeepholeOptimizationsPass(compiler, constantFolding, deadCodeElimination);

    Node root = createNode("function foo() {"
        + " function bar() {"
        + " return 1 + 1;"
        + " }"
        + " return bar();"
        + "}");

    pass.process(null, root);

    Node expected = createNode("function foo() {"
        + " return 2;"
        + "}");
    Node actual = root;
    assertNodeEquals(expected, actual);
  }

  @org.junit.Test
  public void testPeepholeOptimizationsPassWithSelfInvokingFunction() {
    AbstractCompiler compiler = createCompiler();
    AbstractPeepholeOptimization constantFolding = new ConstantFolding();
    AbstractPeepholeOptimization deadCodeElimination = new DeadCodeElimination();
    PeepholeOptimizationsPass pass =
        new PeepholeOptimizationsPass(compiler, constantFolding, deadCodeElimination);

    Node root = createNode("(function foo() {"
        + " return foo();"
        + "})()");

    pass.process(null, root);

    Node expected = createNode("(function foo() {"
        + " var that = this;"
        + " return that();"
        + "})()");
    Node actual = root;
    assertNodeEquals(expected, actual);
  }

  private void assertNodeEquals(Node expected, Node actual) {
    assertEquals("Expected and actual nodes are different.", expected, actual);
    assertEquals("Expected and actual nodes have different types.",
        expected.getType(), actual.getType());
  }

  private Node createNode(String code) {
    CompilerOptions options = new CompilerOptions(Collections.emptyMap());
    return new AbstractCompiler(options).parseTree(code, null);
  }
}