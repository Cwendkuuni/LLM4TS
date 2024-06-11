import org.junit.Test;
import static org.junit.Assert.assertTrue;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.rhino.Node;

public class CheckSideEffectsTest {
  @Test
  public void testCheckSideEffects() {
    CheckLevel level = CheckLevel.WARNING;
    Compiler compiler = new Compiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, false);

    String sourceCode = "function test() {\n" +
            "  var a = 5;\n" +
            "  var b = 10;\n" +
            "  var c = a + b;\n" +
            "  return c;\n" +
            "}";

    Node root = AbstractCompiler.parseTreeFromString(sourceCode, null);
    checkSideEffects.process(null, root);

    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testCheckSideEffectsWithUselessCode() {
    CheckLevel level = CheckLevel.WARNING;
    Compiler compiler = new Compiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, false);

    String sourceCode = "function test() {\n" +
            "  var a = 5;\n" +
            "  var b = 10;\n" +
            "  var c = a + b;\n" +
            "  ; // empty statement\n" +
            "  return c;\n" +
            "}";

    Node root = AbstractCompiler.parseTreeFromString(sourceCode, null);
    checkSideEffects.process(null, root);

    assertTrue(checkSideEffects.problemNodes.size() > 0);
  }

  @Test(expected = AssertionError.class)
  public void testCheckSideEffectsWithProtectedCode() {
    CheckLevel level = CheckLevel.WARNING;
    Compiler compiler = new Compiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, true);

    String sourceCode = "function test() {\n" +
            "  JSCOMPILER_PRESERVE\n" +
            "  var a = 5;\n" +
            "  var b = 10;\n" +
            "  var c = a + b;\n" +
            "  return c;\n" +
            "}";

    Node root = AbstractCompiler.parseTreeFromString(sourceCode, null);
    checkSideEffects.process(null, root);

    throw new AssertionError(); // test should not reach this point
  }
}