import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;
import static com.google.javascript.rhino.Token.*;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.TestConstants;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

@RunWith(Parameterized.class)
public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckLevel level;

  public CheckGlobalThisTest(AbstractCompiler compiler, CheckLevel level) {
    this.compiler = compiler;
    this.level = level;
  }

  @Parameterized.Parameters(name = "{index}: testCase_{index}")
  public static Object[] data() {
    return new Object[] {
        new Object(new CheckGlobalThis(TestConstants.getCompiler(), OFF),
            TestCases.CASE_1),
        new Object(new CheckGlobalThis(TestConstants.getCompiler(), WARN),
            TestCases.CASE_1),
        new Object(new CheckGlobalThis(TestConstants.getCompiler(), QUIET),
            TestCases.CASE_1),
        new Object(new CheckGlobalThis(TestConstants.getCompiler(), ERROR),
            TestCases.CASE_1),

        new Object(new CheckGlobalThis(TestConstants.getCompiler(), OFF),
            TestCases.CASE_2),
        new Object(new CheckGlobalThis(TestConstants.getCompiler(), WARN),
            TestCases.CASE_2),
        new Object(new CheckGlobalThis(TestConstants.getCompiler(), QUIET),
            TestCases.CASE_2),
        new Object(new CheckGlobalThis(TestConstants.getCompiler(), ERROR),
            TestCases.CASE_2),

        new Object(new CheckGlobalThis(TestConstants.getCompiler(), OFF),
            TestCases.CASE_3),
        new Object(new CheckGlobalThis(TestConstants.getCompiler(), WARN),
            TestCases.CASE_3),
        new Object(new CheckGlobalThis(TestConstants.getCompiler(), QUIET),
            TestCases.CASE_3),
        new Object(new CheckGlobalThis(TestConstants.getCompiler(), ERROR),
            TestCases.CASE_3),

        new Object(new CheckGlobalThis(TestConstants.getCompiler(), OFF),
            TestCases.CASE_4),
        new Object(new CheckGlobalThis(TestConstants.getCompiler(), WARN),
            TestCases.CASE_4),
        new Object(new CheckGlobalThis(TestConstants.getCompiler(), QUIET),
            TestCases.CASE_4),
        new Object(new CheckGlobalThis(TestConstants.getCompiler(), ERROR),
            TestCases.CASE_4),

        new Object(new CheckGlobalThis(TestConstants.getCompiler(), OFF),
            TestCases.CASE_5),
        new Object(new CheckGlobalThis(TestConstants.getCompiler(), WARN),
            TestCases.CASE_5),
        new Object(new CheckGlobalThis(TestConstants.getCompiler(), QUIET),
            TestCases.CASE_5),
        new Object(new CheckGlobalThis(TestConstants.getCompiler(), ERROR),
            TestCases.CASE_5)
    };
  }

  private Object testCase;

  @Test
  public void test() {
    Node node = TestCases.getNodeFromString(testCase.toString());
    NodeTraversal traversal = new NodeTraversal(compiler, node);
    CheckGlobalThis checker = (CheckGlobalThis) traversal.getCallback();
    checker.visit(traversal, node, null);
    assertEquals(TestCases.expectedDiagnostics(testCase),
        checker.compiler.getDiagnostics());
  }

  static class TestCases {
    static Node CASE_1 = TestConstants.parseJsString(
        "function evil() { this.useful = 1; }");
    static Node CASE_2 = TestConstants.parseJsString(
        "var a = function evil() { this.useful = 1; }");
    static Node CASE_3 = TestConstants.parseJsString(
        "function a() { this.useful = 1; }");
    static Node CASE_4 = TestConstants.parseJsString(
        "function evil() { this.prototype.useful = 1; }");
    static Node CASE_5 = TestConstants.parseJsString(
        "function a() { this.prototype.useful = 1; }");

    static String expectedDiagnostics(Object testCase) {
      if (testCase.toString().equals(CASE_1.toString())) {
        return "1:1: " + GLOBAL_THIS.getName();
      }
      if (testCase.toString().equals(CASE_2.toString())) {
        return "1:1: " + GLOBAL_THIS.getName() + "\n" +
            "1:1: Cannot assign to 'a' because it is a function.";
      }
      if (testCase.toString().equals(CASE_3.toString())) {
        return "1:1: " + GLOBAL_THIS.getName() + "\n" +
            "1:1: Cannot assign to 'a' because it is a function.";
      }
      if (testCase.toString().equals(CASE_4.toString())) {
        return "1:1: " + GLOBAL_THIS.getName() + "\n" +
            "1:1: Cannot assign to 'prototype'.";
      }
      if (testCase.toString().equals(CASE_5.toString())) {
        return "1:1: " + GLOBAL_THIS.getName() + "\n" +
            "1:1: Cannot assign to 'prototype'.";
      }
      throw new RuntimeException("Unexpected test case: " + testCase);
    }
  }

  static class GLOBAL_THIS {
    static final DiagnosticType NAME = new DiagnosticType(
        "JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object");
  }
}