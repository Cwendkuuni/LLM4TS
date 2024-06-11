import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Token;

@RunWith(JUnit4.class)
public class CheckSideEffectsTest {

  private AbstractCompiler compiler;
  private CheckSideEffects checkSideEffects;

  @Before
  public void setUp() {
    compiler = new Compiler();
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
  }

  @Test
  public void testSimpleOperator() {
    String source = "function foo() {"
        + "var x = 1 + 1;"
        + "}";

    CompilerInput input = new CompilerInput(source, "Test.java", null);
    checkSideEffects.process(input, null);
    assertTrue(input.getDiagnostics().isEmpty());
  }

  @Test
  public void testUselessCode() {
    String source = "function foo() {"
        + "var s = \"this string is \""
        + "         \"continued on the next line but you forgot the +\";"
        + "x == foo();"
        + "foo();;"
        + "}";

    CompilerInput input = new CompilerInput(source, "Test.java", null);
    checkSideEffects.process(input, null);
    List<Node> problemNodes = checkSideEffects.getProblemNodes();
    assertTrue(problemNodes.size() == 3);
    assertFalse(NodeUtil.isStatement(problemNodes.get(0)));
    assertFalse(NodeUtil.isStatement(problemNodes.get(1)));
    assertFalse(NodeUtil.isStatement(problemNodes.get(2)));
  }

  @Test
  public void testSimpleVariableAssignment() {
    String source = "function foo() {"
        + "var s = \"this string is continued on the next line\";"
        + "}";

    CompilerInput input = new CompilerInput(source, "Test.java", null);
    checkSideEffects.process(input, null);
    assertTrue(input.getDiagnostics().isEmpty());
  }

  @Test
  public void testFunctionCall() {
    String source = "function foo() {"
        + "var x = foo();"
        + "}";

    CompilerInput input = new CompilerInput(source, "Test.java", null);
    checkSideEffects.process(input, null);
    List<Node> problemNodes = checkSideEffects.getProblemNodes();
    assertTrue(problemNodes.isEmpty());
  }

  @Test(expected = AssertionError.class)
  public void testNullParent() {
    Node root = new Node(Token.BLOCK);
    checkSideEffects.visit(NodeTraversal.EMPTY, root, null);
  }

  @Test
  public void testCommaOperator() {
    String source = "function foo() {"
        + "var x = 1,"
        + "     y = 2;"
        + "}";

    CompilerInput input = new CompilerInput(source, "Test.java", null);
    checkSideEffects.process(input, null);
    assertTrue(input.getDiagnostics().isEmpty());
  }

  @Test
  public void testForLoop() {
    String source = "function foo() {"
        + "for (var i = 0; i < 3; i++) {"
        + "  var x = i + 1;"
        + "}"
        + "}";

    CompilerInput input = new CompilerInput(source, "Test.java", null);
    checkSideEffects.process(input, null);
    assertTrue(input.getDiagnostics().isEmpty());
  }

  @Test
  public void testProtectSideEffectFreeCode() {
    String source = "function foo() {"
        + "var x = 1;"
        + "JSCOMPILER_PRESERVE(x);"
        + "}";

    CheckSideEffects protectSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    CompilerInput input = new CompilerInput(source, "Test.java", null);
    protectSideEffects.process(input, null);
    List<Node> problemNodes = protectSideEffects.getProblemNodes();
    assertTrue(problemNodes.isEmpty());
  }

  @Test
  public void testProtectSideEffectFreeCodeWithDeadCode() {
    String source = "function foo() {"
        + "var x = 1;"
        + "JSCOMPILER_PRESERVE(x);"
        + "// dead code"
        + "}";

    CheckSideEffects protectSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    CompilerInput input = new CompilerInput(source, "Test.java", null);
    protectSideEffects.process(input, null);
    List<Node> problemNodes = protectSideEffects.getProblemNodes();
    assertTrue(problemNodes.isEmpty());
  }

  @Test
  public void testProtectSideEffectFreeCodeWithSideEffect() {
    String source = "function foo() {"
        + "var x = 1;"
        + "JSCOMPILER_PRESERVE(x);"
        + "x = 2;"
        + "}";

    CheckSideEffects protectSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    CompilerInput input = new CompilerInput(source, "Test.java", null);
    protectSideEffects.process(input, null);
    List<Node> problemNodes = protectSideEffects.getProblemNodes();
    assertTrue(problemNodes.size() == 1);
    assertTrue(NodeUtil.isStatement(problemNodes.get(0)));
  }

  @Test
  public void testProtectSideEffectFreeCodeWithSideEffectInComments() {
    String source = "function foo() {"
        + "var x = 1;"
        + "// JSCOMPILER_PRESERVE(x);"
        + "x = 2;"
        + "}";

    CheckSideEffects protectSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    CompilerInput input = new CompilerInput(source, "Test.java", null);
    protectSideEffects.process(input, null);
    List<Node> problemNodes = protectSideEffects.getProblemNodes();
    assertTrue(problemNodes.isEmpty());
  }

  @Test
  public void testProtectSideEffectFreeCodeWithSideEffectInFunctionCall() {
    String source = "function foo() {"
        + "var x = 1;"
        + "JSCOMPILER_PRESERVE(x);"
        + "foo(x);"
        + "}";

    CheckSideEffects protectSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    CompilerInput input = new CompilerInput(source, "Test.java", null);
    protectSideEffects.process(input, null);
    List<Node> problemNodes = protectSideEffects.getProblemNodes();
    assertTrue(problemNodes.isEmpty());
  }

  @Test
  public void testProtectSideEffectFreeCodeWithSideEffectInFunctionCallWithDeadCode() {
    String source = "function foo() {"
        + "var x = 1;"
        + "JSCOMPILER_PRESERVE(x);"
        + "foo(x);"
        + "// dead code"
        + "}";

    CheckSideEffects protectSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    CompilerInput input = new CompilerInput(source, "Test.java", null);
    protectSideEffects.process(input, null);
    List<Node> problemNodes = protectSideEffects.getProblemNodes();
    assertTrue(problemNodes.isEmpty());
  }

  @Test
  public void testProtectSideEffectFreeCodeWithSideEffectInFunctionCallWithSideEffect() {
    String source = "function foo() {"
        + "var x = 1;"
        + "JSCOMPILER_PRESERVE(x);"
        + "foo(x);"
        + "x = 2;"
        + "}";

    CheckSideEffects protectSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    CompilerInput input = new CompilerInput(source, "Test.java", null);
    protectSideEffects.process(input, null);
    List<Node> problemNodes = protectSideEffects.getProblemNodes();
    assertTrue(problemNodes.size() == 1);
    assertFalse(NodeUtil.isStatement(problemNodes.get(0)));
  }

  @Test
  public void testProtectSideEffectFreeCodeWithSideEffectInReturnStatement() {
    String source = "function foo() {"
        + "return JSCOMPILER_PRESERVE(1);"
        + "}";

    CheckSideEffects protectSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    CompilerInput input = new CompilerInput(source, "Test.java", null);
    protectSideEffects.process(input, null);
    List<Node> problemNodes = protectSideEffects.getProblemNodes();
    assertTrue(problemNodes.isEmpty());
  }

  @Test
  public void testProtectSideEffectFreeCodeWithSideEffectInReturnStatementWithDeadCode() {
    String source = "function foo() {"
        + "return JSCOMPILER_PRESERVE(1);"
        + "// dead code"
        + "}";

    CheckSideEffects protectSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    CompilerInput input = new CompilerInput(source, "Test.java", null);
    protectSideEffects.process(input, null);
    List<Node> problemNodes = protectSideEffects.getProblemNodes();
    assertTrue(problemNodes.isEmpty());
  }

  @Test
  public void testProtectSideEffectFreeCodeWithSideEffectInReturnStatementWithSideEffect() {
    String source = "function foo() {"
        + "return JSCOMPILER_PRESERVE(1);"
        + "return 2;"
        + "}";

    CheckSideEffects protectSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    CompilerInput input = new CompilerInput(source, "Test.java", null);
    protectSideEffects.process(input, null);
    List<Node> problemNodes = protectSideEffects.getProblemNodes();
    assertTrue(problemNodes.size() == 1);
    assertFalse(NodeUtil.isStatement(problemNodes.get(0)));
  }
}