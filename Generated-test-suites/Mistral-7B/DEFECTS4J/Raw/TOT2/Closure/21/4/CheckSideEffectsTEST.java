import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import org.junit.Test;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceFile;

public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, WARNING, false);

    String sourceCode = "function foo() {} var x = 1; x = foo();";
    SourceFile sourceFile = new SourceFile("Test.js", sourceCode);

    Node root = sourceFile.getAst();
    checkSideEffects.process(null, root);

    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testCheckSideEffectsWithUselessCodeWarning() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, WARNING, false);

    String sourceCode = "function foo() {} var x = 1; x = foo(); // This comment is useless";
    SourceFile sourceFile = new SourceFile("Test.js", sourceCode);

    Node root = sourceFile.getAst();
    checkSideEffects.process(null, root);

    assertTrue(checkSideEffects.problemNodes.size() == 1);
  }

  @Test(expected = AssertionError.class)
  public void testCheckSideEffectsWithProtectedCode() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, WARNING, true);

    String sourceCode = "function JSCOMPILER_PROTECTED_CODE() {}";
    SourceFile sourceFile = new SourceFile("Test.js", sourceCode);

    Node root = sourceFile.getAst();
    checkSideEffects.process(null, root);

    // This test should throw an AssertionError since protected code should not be touched.
    checkSideEffects.protectSideEffects();
  }
}