import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.ProcessingEnvironment;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.JSDocInfo;

public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects() {
    String sourceCode = "function foo() {" +
        "  var x = 1;" +
        "  var y = 2;" +
        "  x = foo(); // should that be '='?" +
        "  foo();; // probably just a stray-semicolon. Doesn't hurt to check though" +
        "  return x + y;" +
        "}";

    ProcessingEnvironment processingEnv = new ProcessingEnvironment();
    Parser parser = new Parser(processingEnv);
    SourceFile input = new SourceFile("Test.js", sourceCode);
    Node rootNode = parser.parse(input, null, null);

    CompilerOptions options = new CompilerOptions(processingEnv);
    Compiler compiler = new Compiler(options, processingEnv, null);
    CheckerFactory checkerFactory = compiler.getCheckerFactory();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);

    compiler.process(input, rootNode);

    NodeTraversal.traverse(checkSideEffects, rootNode, null);

    List<DiagnosticPosition> diagnostics = checkerFactory.getDiagnostics();
    assertEquals(1, diagnostics.size());

    DiagnosticPosition diagnostic = diagnostics.get(0);
    assertEquals("JSC_USELESS_CODE", diagnostic.getType().getName());
    assertEquals("Suspicious code. This code lacks side-effects. Is there a bug?", diagnostic.getMessage());
    assertEquals(3, diagnostic.getSourceFile().getLineNumber());
    assertEquals(8, diagnostic.getSourcePosition().getCharacterOffset());
  }

  @Test
  public void testCheckSideEffects_JSDOC() {
    String sourceCode = "/**\n" +
        " * This function does nothing but return a value.\n" +
        " * @return {number} A random number.\n" +
        " */\n" +
        "function foo() {" +
        "  return Math.random();" +
        "}" +
        "function bar() {" +
        "  foo(); // This call should not trigger a warning, even if foo has side-effects in other contexts." +
        "}" +
        "function baz() {" +
        "  var x = foo(); // This assignment should trigger a warning, even if foo has no side-effects in this context." +
        "}" +
        "bar();" +
        "baz();";

    ProcessingEnvironment processingEnv = new ProcessingEnvironment();
    Parser parser = new Parser(processingEnv);
    SourceFile input = new SourceFile("Test.js", sourceCode);
    Node rootNode = parser.parse(input, null, null);

    CompilerOptions options = new CompilerOptions(processingEnv);
    Compiler compiler = new Compiler(options, processingEnv, null);
    CheckerFactory checkerFactory = compiler.getCheckerFactory();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.ERROR, false);

    compiler.process(input, rootNode);

    NodeTraversal.traverse(checkSideEffects, rootNode, null);

    List<DiagnosticPosition> diagnostics = checkerFactory.getDiagnostics();
    assertEquals(1, diagnostics.size());

    DiagnosticPosition diagnostic = diagnostics.get(0);
    assertEquals("JSC_USELESS_CODE", diagnostic.getType().getName());
    assertEquals("Suspicious code. This code lacks side-effects. Is there a bug?", diagnostic.getMessage());
    assertEquals(5, diagnostic.getSourceFile().getLineNumber());
    assertEquals(33, diagnostic.getSourcePosition().getCharacterOffset());
  }

  @Test
  public void testCheckSideEffects_ProtectSideEffectFreeCode() {
    String sourceCode = "function foo() {" +
        "  // Some side-effect free code." +
        "}" +
        "function bar() {" +
        "  foo(); // This call should not trigger a warning." +
        "}" +
        "bar();";

    ProcessingEnvironment processingEnv = new ProcessingEnvironment();
    Parser parser = new Parser(processingEnv);
    SourceFile input = new SourceFile("Test.js", sourceCode);
    Node rootNode = parser.parse(input, null, null);

    CompilerOptions options = new CompilerOptions(processingEnv);
    Compiler compiler = new Compiler(options, processingEnv, null);
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

    compiler.process(input, rootNode);

    NodeTraversal.traverse(checkSideEffects, rootNode, null);

    List<DiagnosticPosition> diagnostics = checkerFactory.getDiagnostics();
    assertTrue(diagnostics.isEmpty());
  }

  @Test
  public void testCheckSideEffects_ProtectSideEffectFreeCode_Removal() {
    String sourceCode = "function foo() {" +
        "  // Some side-effect free code." +
        "}" +
        "function bar() {" +
        "  foo(); // This call should not trigger a warning." +
        "}" +
        "bar();";

    ProcessingEnvironment processingEnv = new ProcessingEnvironment();
    Parser parser = new Parser(processingEnv);
    SourceFile input = new SourceFile("Test.js", sourceCode);
    Node rootNode = parser.parse(input, null, null);

    CompilerOptions options = new CompilerOptions(processingEnv);
    Compiler compiler = new Compiler(options, processingEnv, null);
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    StripProtection stripProtection = new StripProtection(compiler);

    compiler.process(input, rootNode);

    NodeTraversal.traverse(checkSideEffects, rootNode, null);
    NodeTraversal.traverse(stripProtection, rootNode, null);

    List<DiagnosticPosition> diagnostics = checkerFactory.getDiagnostics();
    assertTrue(diagnostics.isEmpty());
  }
}