import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

@RunWith(JUnit4.class)
public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(
        new Compiler(null, null, null, null, null, null, null, null, null),
        WARNING, false);

    String code = "function foo() { }\n" +
                  "var s = \"this string is \";\n" +
                  "var x;\n" +
                  "x = foo();\n" +
                  "foo();;\n";

    Node root = AbstractCompiler.parseTree(code);
    checkSideEffects.process(null, root);

    assertFalse("Unexpected warnings for valid code", checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testCheckSideEffectsWithNoSideEffects() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(
        new Compiler(null, null, null, null, null, null, null, null, null),
        WARNING, false);

    String code = "function foo() { return 42; }\n" +
                  "var x = foo();\n";

    Node root = AbstractCompiler.parseTree(code);
    checkSideEffects.process(null, root);

    assertTrue("Unexpected warnings for valid code", checkSideEffects.problemNodes.isEmpty());
  }

  @Test(expected = AssertionError.class)
  public void testCheckSideEffectsWithException() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(
        new Compiler(null, null, null, null, null, null, null, null, null),
        WARNING, false);

    String code = "function foo() { throw new Error(); }\n" +
                  "foo();;\n";

    Node root = AbstractCompiler.parseTree(code);
    checkSideEffects.process(null, root);
  }

  @Test
  public void testCheckSideEffectsWithProtectedCode() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(
        new Compiler(null, null, null, null, null, null, null, null, null),
        WARNING, true);

    String code = "function foo() { }\n" +
                  "var s = \"this string is \";\n" +
                  "var x;\n" +
                  "x = foo();\n" +
                  "foo();;\n";

    Node root = AbstractCompiler.parseTree(code);
    checkSideEffects.process(null, root);

    // Protected code should not generate warnings
    assertTrue("Unexpected warnings for protected code", checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testProtectSideEffects() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(
        new Compiler(null, null, null, null, null, null, null, null, null),
        WARNING, true);

    String code = "function foo() { }\n" +
                  "var s = \"this string is \";\n" +
                  "var x;\n" +
                  "x = foo();\n" +
                  "foo();;\n";

    checkSideEffects.process(null, AbstractCompiler.parseTree(code));
    checkSideEffects.protectSideEffects();

    String protectedCode = "function JSCOMPILER_PROTECTED_foo() { }\n" +
                           "var JSCOMPILER_PROTECTED_x;\n" +
                           "JSCOMPILER_PROTECTED_x = JSCOMPILER_PROTECTED_foo();\n" +
                           "JSCOMPILER_PROTECTED_foo();;\n";

    Node protectedRoot = AbstractCompiler.parseTree(protectedCode);
    Node protectorCall = protectedRoot.getFirstChild();
    Node protectedXAssign = protectorCall.getLastChild().getLastChild();

    assertTrue("Unexpected number of children in protector call", protectorCall.getChildCount() > 0);
    assertTrue("Unexpected number of children in protected X assignment", protectedXAssign.getChildCount() > 0);

    String expectedProtectedCode = "JSCOMPILER_PROTECTED_foo.call(this);\n" +
                                   "JSCOMPILER_PROTECTED_x = JSCOMPILER_PROTECTED_foo();\n" +
                                   "JSCOMPILER_PROTECTED_foo.call(this);\n";

    String actualProtectedCode = IR.toString(protectedRoot);

    assertEquals("Unexpected protected code", expectedProtectedCode, actualProtectedCode);
  }

  @Test
  public void testStripProtection() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(
        new Compiler(null, null, null, null, null, null, null, null, null),
        WARNING, false);
    checkSideEffects.process(null, AbstractCompiler.parseTree("function JSCOMPILER_PROTECTED_foo() { }\n" +
                                                              "JSCOMPILER_PROTECTED_foo.call(this);"));
    new StripProtection(checkSideEffects.compiler).process(null, checkSideEffects.problemNodes.get(0).getParent());

    String expectedCode = "function foo() { }\n" +
                          "foo.call(this);";

    String actualCode = IR.toString(checkSideEffects.problemNodes.get(0).getParent());

    assertEquals("Unexpected protected code after stripping", expectedCode, actualCode);
  }
}