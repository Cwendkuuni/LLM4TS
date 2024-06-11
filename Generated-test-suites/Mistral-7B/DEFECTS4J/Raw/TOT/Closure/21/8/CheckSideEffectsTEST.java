import static com.google.javascript.jscomp.CheckLevel.WARNING;
import static com.google.javascript.jscomp.JSCodingConvention.ALLOW_EXTRA_SEMICOLONS;
import static org.junit.Assert.assertEquals;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.CompilerFlags;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParserTree;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

public class CheckSideEffectsTest {

  private AbstractCompiler compiler;
  private CompilerOptions options;

  @Before
  public void setup() {
    options = new CompilerOptions(null, null, null);
    options.setCompilerPasses(Lists.newArrayList(
        new CompilerPass() {
          @Override
          public void process(Node externs, Node root) {
            // No-op
          }
        }));
    options.setCodingConvention(ALLOW_EXTRA_SEMICOLONS);
    compiler = new Compiler();
    compiler.setOptions(options);
  }

  @Test
  public void testNoSideEffect() {
    String input = "function foo() { return 1; }";
    ParserTree tree = new ParserTree(input);
    Node root = tree.getRootNode();

    CheckSideEffects checker = new CheckSideEffects(compiler, WARNING, false);
    NodeTraversal.traverse(compiler, root, checker);
    checker.protectSideEffects();
    assertEquals(0, checker.problemNodes.size());
    compiler.compileSingleFile(tree, null, null);
  }

  @Test
  public void testEmptyStatement() {
    String input = "function foo() { }";
    ParserTree tree = new ParserTree(input);
    Node root = tree.getRootNode();

    CheckSideEffects checker = new CheckSideEffects(compiler, WARNING, false);
    NodeTraversal.traverse(compiler, root, checker);
    checker.protectSideEffects();
    assertEquals(0, checker.problemNodes.size());
    compiler.compileSingleFile(tree, null, null);
  }

  @Test
  public void testSimpleOperator() {
    String input = "function foo() { return 1 + 1; }";
    ParserTree tree = new ParserTree(input);
    Node root = tree.getRootNode();

    CheckSideEffects checker = new CheckSideEffects(compiler, WARNING, false);
    NodeTraversal.traverse(compiler, root, checker);
    checker.protectSideEffects();
    assertEquals(0, checker.problemNodes.size());
    compiler.compileSingleFile(tree, null, null);
  }

  @Test
  public void testStatementWithSideEffect() {
    String input = "function foo() { document.body.style.backgroundColor = 'red'; }";
    ParserTree tree = new ParserTree(input);
    Node root = tree.getRootNode();

    CheckSideEffects checker = new CheckSideEffects(compiler, WARNING, false);
    NodeTraversal.traverse(compiler, root, checker);
    checker.protectSideEffects();
    assertEquals(1, checker.problemNodes.size());
    compiler.compileSingleFile(tree, null, null);
    compiler.dispose(); // Dispose to reset compiler state

    checker = new CheckSideEffects(compiler, WARNING, false);
    NodeTraversal.traverse(compiler, root, checker);
    checker.protectSideEffects();
    assertEquals(0, checker.problemNodes.size());
    compiler.compileSingleFile(tree, null, null);
  }

  @Test
  public void testStatementWithSideEffectProtected() {
    String input = "function foo() { JSCOMPILER_PRESERVE document.body.style.backgroundColor = 'red'; }";
    ParserTree tree = new ParserTree(input);
    Node root = tree.getRootNode();

    CheckSideEffects checker = new CheckSideEffects(compiler, WARNING, true);
    NodeTraversal.traverse(compiler, root, checker);
    checker.protectSideEffects();
    assertEquals(0, checker.problemNodes.size());
    compiler.compileSingleFile(tree, null, null);
  }

  @Test
  public void testStatementWithSideEffectRemoved() {
    String input = "function foo() { document.body.style.backgroundColor = 'red'; }";
    ParserTree tree = new ParserTree(input);
    Node root = tree.getRootNode();

    CompilerPass stripProtection = new StripProtection(compiler);
    NodeTraversal.traverse(compiler, root, stripProtection);

    CheckSideEffects checker = new CheckSideEffects(compiler, WARNING, false);
    NodeTraversal.traverse(compiler, root, checker);
    checker.protectSideEffects(); // This won't be called because side-effects are already removed
    assertEquals(0, checker.problemNodes.size());
    compiler.compileSingleFile(tree, null, null);
  }
}

class StripProtection implements CompilerPass {
  // Implementation omitted for brevity
}