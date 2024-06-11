import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.List;

public class CheckSideEffectsTest {

  private AbstractCompiler compiler;
  private CheckSideEffects checkSideEffects;

  @Before
  public void setUp() {
    compiler = new Compiler();
    checkSideEffects = new CheckSideEffects(compiler, WARNING, false);
  }

  @Test
  public void testProcessEmptyCode() {
    CompilerInput input = CompilerInput.fromCode("", null);
    checkSideEffects.process(input, null);
    assertTrue(input.getDiagnostics().isEmpty());
  }

  @Test
  public void testCheckSideEffectsSimpleStatement() {
    CompilerInput input = CompilerInput.fromCode("var x = 1;", null);
    checkSideEffects.process(input, null);
    List<Node> diagnostics = input.getDiagnostics();
    assertEquals(0, diagnostics.size());
  }

  @Test
  public void testCheckSideEffectsVoidStatement() {
    CompilerInput input = CompilerInput.fromCode(";", null);
    checkSideEffects.process(input, null);
    List<Node> diagnostics = input.getDiagnostics();
    assertEquals(0, diagnostics.size());
  }

  @Test
  public void testCheckSideEffectsSimpleExpression() {
    CompilerInput input = CompilerInput.fromCode("1 + 1;", null);
    checkSideEffects.process(input, null);
    List<Node> diagnostics = input.getDiagnostics();
    assertEquals(0, diagnostics.size());
  }

  @Test
  public void testCheckSideEffectsSimpleOperator() {
    CompilerInput input = CompilerInput.fromCode("1 + 1", null);
    checkSideEffects.process(input, null);
    List<Node> diagnostics = input.getDiagnostics();
    assertEquals(1, diagnostics.size());
    Node diagnostic = diagnostics.get(0);
    assertEquals(USELESS_CODE_ERROR, diagnostic.getType());
    String message = diagnostic.getJSDocInfo().getSummary();
    assertEquals("The result of the '+' operator is not being used.", message);
  }

  @Test
  public void testCheckSideEffectsStringConcat() {
    CompilerInput input = CompilerInput.fromCode("var s = \"This is a string\";", null);
    checkSideEffects.process(input, null);
    List<Node> diagnostics = input.getDiagnostics();
    assertEquals(0, diagnostics.size());
  }

  @Test
  public void testCheckSideEffectsCallStatement() {
    CompilerInput input = CompilerInput.fromCode("func();;", null);
    checkSideEffects.process(input, null);
    List<Node> diagnostics = input.getDiagnostics();
    assertEquals(1, diagnostics.size());
    Node diagnostic = diagnostics.get(0);
    assertEquals(USELESS_CODE_ERROR, diagnostic.getType());
    String message = diagnostic.getJSDocInfo().getSummary();
    assertEquals("func() probably does not return a value.", message);
  }

  @Test
  public void testCheckSideEffectsForLoop() {
    CompilerInput input = CompilerInput.fromCode("for (var i = 0; i < 10; i++) {;\n}", null);
    checkSideEffects.process(input, null);
    List<Node> diagnostics = input.getDiagnostics();
    assertEquals(0, diagnostics.size());
  }

  @Test
  public void testCheckSideEffectsStatementInsideExpression() {
    CompilerInput input = CompilerInput.fromCode("1 + (var x = 1);", null);
    checkSideEffects.process(input, null);
    List<Node> diagnostics = input.getDiagnostics();
    assertEquals(1, diagnostics.size());
    Node diagnostic = diagnostics.get(0);
    assertEquals(USELESS_CODE_ERROR, diagnostic.getType());
    String message = diagnostic.getJSDocInfo().getSummary();
    assertEquals("The result of the statement 'var x = 1;' is not being used.", message);
  }

  @Test
  public void testCheckSideEffectsStatementInsideExpressionWithSideEffect() {
    CompilerInput input = CompilerInput.fromCode("1 + (document.write('Hello'));", null);
    checkSideEffects.process(input, null);
    List<Node> diagnostics = input.getDiagnostics();
    assertEquals(1, diagnostics.size());
    Node diagnostic = diagnostics.get(0);
    assertEquals(USELESS_CODE_ERROR, diagnostic.getType());
    String message = diagnostic.getJSDocInfo().getSummary();
    assertEquals("The statement 'document.write('Hello')' has a side-effect.", message);
  }

  @Test
  public void testCheckSideEffectsProtectedCode() {
    CompilerInput input = CompilerInput.fromCode("JSCOMPILER_PRESERVEjscomp_protectSideEffects();", null);
    checkSideEffects.process(input, null);
    List<Node> diagnostics = input.getDiagnostics();
    assertEquals(0, diagnostics.size());
  }

  @Test(expected = AssertionError.class)
  public void testCheckSideEffectsProtectedCodeWithUnprotectedCall() {
    CompilerInput input = CompilerInput.fromCode("JSCOMPILER_PRESERVEjscomp_protectSideEffects();jscomp_protectSideEffects();", null);
    checkSideEffects.process(input, null);
  }

  @Test
  public void testCheckSideEffectsProtectSideEffects() {
    CompilerInput input = CompilerInput.fromCode("function jscomp_protectSideEffects() {;\n}", null);
    checkSideEffects = new CheckSideEffects(compiler, WARNING, true);
    checkSideEffects.process(input, null);
    List<Node> diagnostics = input.getDiagnostics();
    assertEquals(0, diagnostics.size());
  }

  @Test
  public void testCheckSideEffectsStripProtection() {
    CompilerInput input = CompilerInput.fromCode("JSCOMPILER_PRESERVEfunction jscomp_protectSideEffects() { JSCOMPILER_PRESERVEJSCOMPILER_PRESERVEJSCOMPILER_PRESERVE};", null);
    checkSideEffects = new CheckSideEffects(compiler, WARNING, false);
    checkSideEffects.process(input, null);
    List<Node> diagnostics = input.getDiagnostics();
    assertEquals(1, diagnostics.size());
    Node diagnostic = diagnostics.get(0);
    assertEquals(USELESS_CODE_ERROR, diagnostic.getType());
    String message = diagnostic.getJSDocInfo().getSummary();
    assertEquals("The code contains a call to 'jscomp_protectSideEffects', which is no longer needed.", message);
  }

  @Test
  public void testCheckSideEffectsProtectSideEffectsAndStripProtection() {
    CompilerInput input = CompilerInput.fromCode("JSCOMPILER_PRESERVEfunction jscomp_protectSideEffects() { JSCOMPILER_PRESERVEJSCOMPILER_PRESERVEJSCOMPILER_PRESERVE}; JSCOMPILER_PRESERVEfunction jscomp_protectSideEffects2() { JSCOMPILER_PRESERVEJSCOMPILER_PRESERVEJSCOMPILER_PRESERVE};", null);
    checkSideEffects = new CheckSideEffects(compiler, WARNING, true);
    checkSideEffects.process(input, null);
    List<Node> diagnostics = input.getDiagnostics();
    assertEquals(0, diagnostics.size());

    checkSideEffects = new CheckSideEffects(compiler, WARNING, false);
    checkSideEffects.process(input, null);
    diagnostics = input.getDiagnostics();
    assertEquals(2, diagnostics.size());
    for (Node diagnostic : diagnostics) {
      String message = diagnostic.getJSDocInfo().getSummary();
      if (message.equals("The code contains a call to 'jscomp_protectSideEffects', which is no longer needed.")) {
        continue;
      }
      assertEquals(USELESS_CODE_ERROR, diagnostic.getType());
      assertEquals("The statement 'JSCOMPILER_PRESERVEJSCOMPILER_PRESERVEJSCOMPILER_PRESERVE' has no side-effects.", message);
    }
  }
}