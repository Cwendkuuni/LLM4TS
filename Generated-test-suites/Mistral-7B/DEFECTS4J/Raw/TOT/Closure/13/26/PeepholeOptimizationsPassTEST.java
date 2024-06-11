import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPeepholeOptimization;
import com.google.javascript.jscomp.CompilationTestHelper;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PeepholeChangeHandler;
import com.google.javascript.jscomp.PeepholeOptimizationsPass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PeepholeOptimizationsPassTest {

  @Test
  public void testPeepholeOptimizationsPass() {
    AbstractCompiler compiler = new CompilationTestHelper() {
      @Override
      protected void addAdditionalPasses(CompilerPassManager passes) {
        passes.addPass(new PeepholeOptimizationsPass(getAbstractCompiler(),
            new ConstantFolding(), new DeadCodeElimination()));
      }
    }.createCompiler();

    String sourceCode = "function foo() { var x = 1 + 1; return x; }";
    Node rootNode = compiler.parseTree(sourceCode);

    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new ConstantFolding(), new DeadCodeElimination());
    pass.process(null, rootNode);

    Node optimizedNode = pass.getCompiler().getRootNode();
    assertEquals("function foo() { return 2; }", optimizedNode.toSource(compiler));
  }

  @Test(expected = AssertionError.class)
  public void testErrorScenario() {
    AbstractCompiler compiler = new CompilationTestHelper() {
      @Override
      protected void addAdditionalPasses(CompilerPassManager passes) {
        passes.addPass(new PeepholeOptimizationsPass(getAbstractCompiler(), new InvalidOptimization()));
      }
    }.createCompiler();

    String sourceCode = "function foo() { return 1; }";
    Node rootNode = compiler.parseTree(sourceCode);

    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new InvalidOptimization());
    pass.process(null, rootNode);
  }

  static class ConstantFolding implements AbstractPeepholeOptimization {
    @Override
    public Node optimizeSubtree(Node root) {
      if (root.isBinaryOperator("+") && root.getLeft().isNumberLiteral()
          && root.getRight().isNumberLiteral()) {
        Node left = root.getLeft();
        Node right = root.getRight();
        int sum = left.getIntValue() + right.getIntValue();
        return new NumberLiteral(sum);
      }
      return root;
    }

    @Override
    public void beginTraversal(AbstractCompiler compiler) {
      // No action
    }

    @Override
    public void endTraversal(AbstractCompiler compiler) {
      // No action
    }
  }

  static class DeadCodeElimination implements AbstractPeepholeOptimization {
    @Override
    public Node optimizeSubtree(Node root) {
      if (root.isFunction()) {
        for (Node child : root.children()) {
          if (child.getNextSibling() != null) {
            if (child.isStatement() && child.getExpression() == null) {
              child.getParent().removeChild(child);
              return null;
            }
          }
          Node optimizedChild = optimizeSubtree(child);
          if (optimizedChild != child) {
            child = optimizedChild;
          }
        }
      }
      return root;
    }

    @Override
    public void beginTraversal(AbstractCompiler compiler) {
      // No action
    }

    @Override
    public void endTraversal(AbstractCompiler compiler) {
      // No action
    }
  }

  static class InvalidOptimization implements AbstractPeepholeOptimization {
    @Override
    public Node optimizeSubtree(Node root) {
      throw new RuntimeException("Invalid optimization");
    }

    @Override
    public void beginTraversal(AbstractCompiler compiler) {
      // No action
    }

    @Override
    public void endTraversal(AbstractCompiler compiler) {
      // No action
    }
  }
}