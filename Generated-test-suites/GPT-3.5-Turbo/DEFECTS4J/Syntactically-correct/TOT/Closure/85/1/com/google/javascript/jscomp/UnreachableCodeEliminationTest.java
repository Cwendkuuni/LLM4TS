import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Test;

public class UnreachableCodeEliminationTest {

  private static final DiagnosticType ERROR_TYPE = new DiagnosticType() {};

  @Test
  public void testRemoveDeadCode() throws Exception {
    AbstractCompiler compiler = createCompiler();
    Node root = parse("function foo() { return; alert('unreachable'); }");
    NodeTraversal.traverse(compiler, root, new UnreachableCodeElimination(compiler, true));
    Node expected = parse("function foo() { return; }");
    assertNodeEqual(expected, root);
  }

  @Test
  public void testRemoveUselessExpression() throws Exception {
    AbstractCompiler compiler = createCompiler();
    Node root = parse("function foo() { var x = 1; x; }");
    NodeTraversal.traverse(compiler, root, new UnreachableCodeElimination(compiler, true));
    Node expected = parse("function foo() { var x = 1; }");
    assertNodeEqual(expected, root);
  }

  @Test
  public void testRemoveConditionalBranch() throws Exception {
    AbstractCompiler compiler = createCompiler();
    Node root = parse("function foo() { if (true) { return; } else { alert('unreachable'); } }");
    NodeTraversal.traverse(compiler, root, new UnreachableCodeElimination(compiler, true));
    Node expected = parse("function foo() { if (true) { return; } }");
    assertNodeEqual(expected, root);
  }

  @Test
  public void testRemoveNestedUnconditionalBranch() throws Exception {
    AbstractCompiler compiler = createCompiler();
    Node root = parse("function foo() { if (true) { return; } if (false) { return; } alert('unreachable'); }");
    NodeTraversal.traverse(compiler, root, new UnreachableCodeElimination(compiler, true));
    Node expected = parse("function foo() { if (true) { return; } }");
    assertNodeEqual(expected, root);
  }

  @Test
  public void testKeepNestedUnconditionalBranchIfFirstBranchRemoved() throws Exception {
    AbstractCompiler compiler = createCompiler();
    Node root = parse("function foo() { if (true) { if (false) { return; } alert('unreachable'); } }");
    NodeTraversal.traverse(compiler, root, new UnreachableCodeElimination(compiler, true));
    Node expected = parse("function foo() { if (true) { if (false) { return; } alert('unreachable'); } }");
    assertNodeEqual(expected, root);
  }

  @Test
  public void testKeepNestedUnconditionalBranchIfSecondBranchRemoved() throws Exception {
    AbstractCompiler compiler = createCompiler();
    Node root = parse("function foo() { if (true) { return; } if (false) { if (true) { return; } alert('unreachable'); } }");
    NodeTraversal.traverse(compiler, root, new UnreachableCodeElimination(compiler, true));
    Node expected = parse("function foo() { if (true) { return; } if (false) { if (true) { return; } alert('unreachable'); } }");
    assertNodeEqual(expected, root);
  }

  @Test
  public void testKeepUsefulUnconditionalBranch() throws Exception {
    AbstractCompiler compiler = createCompiler();
    Node root = parse("function foo() { if (true) { return; } var x = 1; alert(x); }");
    NodeTraversal.traverse(compiler, root, new UnreachableCodeElimination(compiler, true));
    Node expected = parse("function foo() { if (true) { return; } var x = 1; alert(x); }");
    assertNodeEqual(expected, root);
  }

  @Test
  public void testRemoveUnreachableWhileLoop() throws Exception {
    AbstractCompiler compiler = createCompiler();
    Node root = parse("function foo() { while (false) { alert('unreachable'); } }");
    NodeTraversal.traverse(compiler, root, new UnreachableCodeElimination(compiler, true));
    Node expected = parse("function foo() {}");
    assertNodeEqual(expected, root);
  }

  @Test
  public void testKeepReachableWhileLoop() throws Exception {
    AbstractCompiler compiler = createCompiler();
    Node root = parse("function foo() { while (true) { return; alert('unreachable'); } }");
    NodeTraversal.traverse(compiler, root, new UnreachableCodeElimination(compiler, true));
    Node expected = parse("function foo() { while (true) { return; } }");
    assertNodeEqual(expected, root);
  }

  @Test
  public void testRemoveUnreachableDoWhileLoop() throws Exception {
    AbstractCompiler compiler = createCompiler();
    Node root = parse("function foo() { do { alert('unreachable'); } while (false); }");
    NodeTraversal.traverse(compiler, root, new UnreachableCodeElimination(compiler, true));
    Node expected = parse("function foo() {}");
    assertNodeEqual(expected, root);
  }

  @Test
  public void testKeepReachableDoWhileLoop() throws Exception {
    AbstractCompiler compiler = createCompiler();
    Node root = parse("function foo() { do { return; alert('unreachable'); } while (true); }");
    NodeTraversal.traverse(compiler, root, new UnreachableCodeElimination(compiler, true));
    Node expected = parse("function foo() { do { return; } while (true); }");
    assertNodeEqual(expected, root);
  }

  @Test
  public void testKeepVarDeclarationInReachableCode() throws Exception {
    AbstractCompiler compiler = createCompiler();
    Node root = parse("function foo() { if (true) { var x = 1; return; } alert(x); }");
    NodeTraversal.traverse(compiler, root, new UnreachableCodeElimination(compiler, true));
    Node expected = parse("function foo() { if (true) { var x = 1; return; } alert(x); }");
    assertNodeEqual(expected, root);
  }

  @Test
  public void testKeepCatchesInReachableCode() throws Exception {
    AbstractCompiler compiler = createCompiler();
    Node root = parse("function foo() { try { return; } catch (e) { alert('unreachable'); } }");
    NodeTraversal.traverse(compiler, root, new UnreachableCodeElimination(compiler, true));
    Node expected = parse("function foo() { try { return; } catch (e) { alert('unreachable'); } }");
    assertNodeEqual(expected, root);
  }

  private AbstractCompiler createCompiler() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setLanguageIn(CompilerOptions.LanguageMode.ECMASCRIPT6);
    options.setLanguageOut(CompilerOptions.LanguageMode.ECMASCRIPT5);
    options.setWarningLevel(ERROR_TYPE, CheckLevel.ERROR);
    compiler.initOptions(options);
    return compiler;
  }

  private Node parse(String source) {
    Compiler compiler = createCompiler();
    Node root = compiler.parse(SourceFile.fromCode("/testcode", source));
    assertFalse(compiler.hasErrors());
    return root;
  }

  private void assertNodeEqual(Node expected, Node actual) {
    assertTrue(expected.isEquivalentTo(actual));
  }
}