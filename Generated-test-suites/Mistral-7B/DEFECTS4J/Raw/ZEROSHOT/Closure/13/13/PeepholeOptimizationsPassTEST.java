import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerPasses.*;
import static com.google.javascript.jscomp.CompilerTests.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.AbstractNodeVisitor;
import com.google.javascript.rhino.Node;

public class PeepholeOptimizationsPassTest {
  private Compiler compiler;
  private CompilerOptions options;

  @Before
  public void setUp() {
    options = new CompilerOptions();
    options.setAssumeFunctionTypesForTopLevelDeclarations(true);
    compiler = newCompiler(options);
  }

  @Test
  public void testBasicPeepholeOptimizations() {
    String input = "function f() { return (1 + 1) * 3; }";
    Node node = parseAndCompile(compiler, input);
    assertEquals(10, getValue(node));

    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new ConstantFolding(), new SimpleVariableRenaming(), new UselessCodeElimination());
    pass.process(node, null);
    assertEquals(10, getValue(node));

    String expectedOutput = "function f() { return 3; }";
    assertEquals(expectedOutput, getCompiledSource(node));
  }

  @Test
  public void testComplexPeepholeOptimizations() {
    String input = "function f() { var x = 1; return (1 + 1) * x; }";
    Node node = parseAndCompile(compiler, input);
    assertEquals(3, getValue(node));

    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new ConstantFolding(), new SimpleVariableRenaming(), new UselessCodeElimination());
    pass.process(node, null);
    assertEquals(3, getValue(node));

    String expectedOutput = "function f() { return 3; }";
    assertEquals(expectedOutput, getCompiledSource(node));
  }

  @Test
  public void testNoOptimizationWhenDisabled() {
    CompilerOptions options = new CompilerOptions();
    options.setPeepholeOptimizations(false);
    Compiler compiler = newCompiler(options);

    String input = "function f() { return (1 + 1) * 3; }";
    Node node = parseAndCompile(compiler, input);
    assertEquals(3 * (1 + 1), getValue(node));

    String expectedOutput = "function f() { return (1 + 1) * 3; }";
    assertEquals(expectedOutput, getCompiledSource(node));
  }

  @Test
  public void testNoOptimizationWhenOptimizationFailed() {
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new ThrowingOptimization());

    String input = "function f() { throw new Error('Test'); }";
    Node node = parseAndCompile(compiler, input);

    pass.process(node, null);
    assertEquals(getValue(node), getValue(getNodeByPath(node, "throw new Error('Test')")));

    String expectedOutput = "function f() { throw new Error('Test'); }";
    assertEquals(expectedOutput, getCompiledSource(node));
  }

  @Test
  public void testTraversalOrder() {
    String input = "function f() { return g() * h(); } function g() { return 1; } function h() { return 2; }";
    Node node = parseAndCompile(compiler, input);

    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new ConstantFolding(), new SimpleVariableRenaming());
    pass.process(node, null);

    AbstractNodeVisitor visitor = new AbstractNodeVisitor() {
      int count = 0;

      @Override
      public void visit(NodeTraversal nt) {
        count++;
      }
    };

    NodeTraversal.traverse(node, visitor);
    assertEquals(4, count);
  }
}