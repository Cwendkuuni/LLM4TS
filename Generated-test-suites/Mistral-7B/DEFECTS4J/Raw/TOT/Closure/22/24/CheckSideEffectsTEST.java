import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerResult;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParserPhase;

public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects() {
    CompilerOptions options = new CompilerOptions(ParserPhase.SYNTAX_ONLY);
    options.setWarningLevel(CheckLevel.QUIRKS);

    AbstractCompiler compiler = new Compiler();
    CompilerInput input = new CompilerInput(
        "function foo() { return 42; }",
        "function main() { var x = 1; x = foo(); }",
        "function bar() { x = 1; }",
        "function baz() { x = 1; }",
        "function quux() { x = foo(); }");

    CompilerResult result = compiler.compile(input, options);

    Node root = result.getTree();

    CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.QUIRKS, false);
    NodeTraversal.traverse(checker, root, null);

    assertNotNull(checker.problemNodes);
    assertEquals(3, checker.problemNodes.size());

    Node main = findNodeWithName(root, "main");
    assertSame(main, checker.problemNodes.get(0));

    Node bar = findNodeWithName(root, "bar");
    assertSame(bar, checker.problemNodes.get(1));

    Node baz = findNodeWithName(root, "baz");
    assertSame(baz, checker.problemNodes.get(2));

    checker.protectSideEffects();
    assertEquals(4, root.getChildCount());
    assertNotNull(findNodeWithName(root, "JSCOMPILER_PRESERVE"));

    // Test that the protected nodes are not removed by other passes
    new StripProtection(compiler).process(input, root);
    assertEquals(4, root.getChildCount());
    assertNotNull(findNodeWithName(root, "JSCOMPILER_PRESERVE"));
  }

  @Test
  public void testCheckSideEffectsProtectSideEffectFreeCode() {
    CompilerOptions options = new CompilerOptions(ParserPhase.SYNTAX_ONLY);
    options.setWarningLevel(CheckLevel.QUIRKS);

    AbstractCompiler compiler = new Compiler();
    CompilerInput input = new CompilerInput(
        "function foo() { return 42; }",
        "function main() { var x = 1; x = foo(); }",
        "function bar() { x = 1; }",
        "function baz() { x = 1; }",
        "function quux() { x = foo(); }",
        "function quuxProtected() { JSCOMPILER_PRESERVE(x = foo()); }");

    CompilerResult result = compiler.compile(input, options);

    Node root = result.getTree();

    CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.QUIRKS, true);
    NodeTraversal.traverse(checker, root, null);

    assertNotNull(checker.problemNodes);
    assertTrue(checker.problemNodes.isEmpty());
  }

  @Test
  public void testCheckSideEffectsIgnoreEval() {
    CompilerOptions options = new CompilerOptions(ParserPhase.SYNTAX_ONLY);
    options.setWarningLevel(CheckLevel.QUIRKS);

    AbstractCompiler compiler = new Compiler();
    CompilerInput input = new CompilerInput(
        "function foo() { return 42; }",
        "function main() { eval('var x = 1; x = foo();'); }");

    CompilerResult result = compiler.compile(input, options);

    Node root = result.getTree();

    CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.QUIRKS, false);
    NodeTraversal.traverse(checker, root, null);

    assertNotNull(checker.problemNodes);
    assertTrue(checker.problemNodes.isEmpty());
  }

  private Node findNodeWithName(Node node, String name) {
    if (node.isName() && node.getString().equals(name)) {
      return node;
    }

    for (int i = 0; i < node.getChildCount(); i++) {
      Node child = node.getChild(i);
      Node result = findNodeWithName(child, name);
      if (result != null) {
        return result;
      }
    }

    return null;
  }
}