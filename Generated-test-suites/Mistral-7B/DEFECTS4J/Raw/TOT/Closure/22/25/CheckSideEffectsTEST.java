import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.TestCompiler;
import com.google.javascript.rhino.NodeFactory;

import org.junit.Before;
import org.junit.Test;

public class CheckSideEffectsTest {

  private AbstractCompiler compiler;
  private CompilerInput input;
  private NodeFactory nodeFactory;

  @Before
  public void setup() {
    compiler = new TestCompiler();
    nodeFactory = new NodeFactory(compiler.getOptions().getLanguageVersion());
  }

  @Test
  public void testNoSideEffect() {
    compiler.setOption(CompilerOptions.Warning.USELESS_CODE, WARNING);
    String code = "function foo() {}";
    input = new CompilerInput(code, "Test.java", null);
    Node root = nodeFactory.parse(input);
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, WARNING, false);
    NodeTraversal.traverse(compiler, root, checkSideEffects);
    checkSideEffects.protectSideEffects();
    compiler.compile(input, null);
    List<String> errors = compiler.getErrors();
    assertTrue(errors.isEmpty());
  }

  @Test
  public void testSideEffect() {
    compiler.setOption(CompilerOptions.Warning.USELESS_CODE, WARNING);
    String code = "function foo() { document.write('Hello') }";
    input = new CompilerInput(code, "Test.java", null);
    Node root = nodeFactory.parse(input);
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, WARNING, false);
    NodeTraversal.traverse(compiler, root, checkSideEffects);
    checkSideEffects.protectSideEffects();
    compiler.compile(input, null);
    List<String> errors = compiler.getErrors();
    assertFalse(errors.isEmpty());
    assertEquals("Suspicious code. document.write('Hello')", errors.get(0).getMessage());
  }

  @Test
  public void testProtectSideEffectFreeCode() {
    compiler.setOption(CompilerOptions.Warning.USELESS_CODE, WARNING);
    compiler.setOption(CompilerOptions.Warning.UNDEFINED, COMPILER_PASS_WARNING);
    String code = "function foo() { document.write('Hello') }";
    input = new CompilerInput(code, "Test.java", null);
    Node root = nodeFactory.parse(input);
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, WARNING, true);
    NodeTraversal.traverse(compiler, root, checkSideEffects);
    checkSideEffects.protectSideEffects();
    compiler.compile(input, null);
    List<String> errors = compiler.getErrors();
    assertTrue(errors.isEmpty());
    String protectedCode = compiler.getIntermediateRepresentation().toString();
    assertTrue(protectedCode.contains("JSCOMPILER_PRESERVE(function foo() { document.write('Hello') })"));

    String strippedCode = new StripProtection(compiler).process(null, root, null).toString();
    assertEquals(code, strippedCode);
  }

  @Test
  public void testProtectSideEffectFreeCodeEmptyCode() {
    compiler.setOption(CompilerOptions.Warning.USELESS_CODE, WARNING);
    String code = "";
    input = new CompilerInput(code, "Test.java", null);
    Node root = nodeFactory.parse(input);
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, WARNING, true);
    NodeTraversal.traverse(compiler, root, checkSideEffects);
    checkSideEffects.protectSideEffects();
    compiler.compile(input, null);
    List<String> errors = compiler.getErrors();
    assertTrue(errors.isEmpty());
    String protectedCode = compiler.getIntermediateRepresentation().toString();
    assertTrue(protectedCode.contains("JSCOMPILER_PRESERVE(function() {})"));
  }

  @Test
  public void testProtectSideEffectFreeCodeSingleStatement() {
    compiler.setOption(CompilerOptions.Warning.USELESS_CODE, WARNING);
    String code = "function foo() { return 42 }";
    input = new CompilerInput(code, "Test.java", null);
    Node root = nodeFactory.parse(input);
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, WARNING, true);
    NodeTraversal.traverse(compiler, root, checkSideEffects);
    checkSideEffects.protectSideEffects();
    compiler.compile(input, null);
    List<String> errors = compiler.getErrors();
    assertTrue(errors.isEmpty());
    String protectedCode = compiler.getIntermediateRepresentation().toString();
    assertTrue(protectedCode.contains("JSCOMPILER_PRESERVE(function foo() { return 42 })"));
  }
}