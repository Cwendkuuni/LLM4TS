import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.NodeFactory;

@RunWith(JUnit4.class)
public class CheckSideEffectsTest {

  private Compiler compiler;
  private NodeFactory nodeFactory;
  private CheckSideEffects checkSideEffects;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions();
    compiler = new Compiler(options);
    nodeFactory = compiler.getNodeFactory();
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
  }

  @After
  public void tearDown() {
    compiler = null;
  }

  @Test
  public void testBasicFunctionality() {
    String code = "function foo() { return 42; }";
    Node root = Parser.parse(code, null, nodeFactory);
    NodeTraversal.traverse(compiler, root, checkSideEffects);
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testEmptyStatement() {
    String code = "var x = 42;\n;";
    Node root = Parser.parse(code, null, nodeFactory);
    NodeTraversal.traverse(compiler, root, checkSideEffects);
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testMissingAssignOperator() {
    String code = "var s = 'this string is ' 'continued on the next line but you forgot the +';";
    Node root = Parser.parse(code, null, nodeFactory);
    NodeTraversal.traverse(compiler, root, checkSideEffects);
    assertFalse(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testFunctionCallWithoutParentheses() {
    String code = "x == foo";
    Node root = Parser.parse(code, null, nodeFactory);
    NodeTraversal.traverse(compiler, root, checkSideEffects);
    assertFalse(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testEmptyFunctionCall() {
    String code = "foo();;";
    Node root = Parser.parse(code, null, nodeFactory);
    NodeTraversal.traverse(compiler, root, checkSideEffects);
    assertFalse(checkSideEffects.problemNodes.isEmpty());
  }

  @Test(expected = AssertionError.class)
  public void testProtectedCode() {
    String code = "function foo() { JSCOMPILER_PRESERVE JSCOMPILER_PRESERVE return 42; }";
    Node root = Parser.parse(code, null, nodeFactory);
    NodeTraversal.traverse(compiler, root, checkSideEffects);
    checkSideEffects.protectSideEffects();
  }

  @Test
  public void testForStatement() {
    String code = "for (var i = 0; i < 10; i++) { i++; }";
    Node root = Parser.parse(code, null, nodeFactory);
    NodeTraversal.traverse(compiler, root, checkSideEffects);
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }
}

class CheckSideEffectsTest_ProtectSideEffects extends CheckSideEffects {

  CheckSideEffectsTest_ProtectSideEffects(AbstractCompiler compiler, CheckLevel level,
      boolean protectSideEffectFreeCode) {
    super(compiler, level, protectSideEffectFreeCode);
  }

  @Override
  public void protectSideEffects() {
    // Disabled for testing
  }
}

class CheckSideEffectsTest_StripProtection extends StripProtection {

  CheckSideEffectsTest_StripProtection(AbstractCompiler compiler) {
    super(compiler);
  }

  @Override
  public void visit(NodeTraversal t, Node n, Node parent) {
    // Disabled for testing
    super.visit(t, n, parent);
  }
}