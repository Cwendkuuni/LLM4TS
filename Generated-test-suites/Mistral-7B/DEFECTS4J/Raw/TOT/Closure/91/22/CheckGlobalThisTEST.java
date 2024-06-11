import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.TestingCompiler;
import com.google.javascript.rhino.JSDocInfo;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
  private AbstractCompiler compiler;
  private Checker checker;
  private CompilerOptions options;

  @Before
  public void setUp() throws Exception {
    options = new CompilerOptions(true, null);
    compiler = new TestingCompiler(options);
    checker = compiler.getChecker();
  }

  @Test
  public void testSafeUsageOfThis() throws Exception {
    String code = "function Foo() { this.x = 1; }";
    String expected = "";
    Node node = Parser.parse(code, null, null, null);
    checker.process(node, null);
    assertTrue(checker.getDiagnostics().isEmpty());
  }

  @Test
  public void testUnsafeAssignment() throws Exception {
    String code = "var a = this; a.x = 1;";
    String expected = "2:12  warning JSC_USED_GLOBAL_THIS: dangerous use of the global 'this' object\n";
    Node node = Parser.parse(code, null, null, null);
    compiler.compile(node, null, null);
    assertEquals(expected, compiler.getDiagnostics().getMessages().get(0).message);
  }

  @Test
  public void testUnsafePropertyAccess() throws Exception {
    String code = "console.log(this.x);";
    String expected = "1:5  warning JSC_USED_GLOBAL_THIS: dangerous use of the global 'this' object\n";
    Node node = Parser.parse(code, null, null, null);
    compiler.compile(node, null, null);
    assertEquals(expected, compiler.getDiagnostics().getMessages().get(0).message);
  }

  @Test
  public void testConstructorFunction() throws Exception {
    String code = "function Foo() { this.x = 1; }";
    String expected = "";
    Node node = Parser.parse(code, null, null, null);
    checker.process(node, null);
    assertTrue(checker.getDiagnostics().isEmpty());
  }

  @Test
  public void testPrototypeFunction() throws Exception {
    String code = "function Foo() {}\nFoo.prototype.x = function() { this.y = 1; }";
    String expected = "";
    Node node = Parser.parse(code, null, null, null);
    checker.process(node, null);
    assertTrue(checker.getDiagnostics().isEmpty());
  }

  @Test
  public void testFunctionAnnotatedWithThis() throws Exception {
    String code = "/**@this {type: 'MyType'} */ function Foo() { this.x = 1; }";
    String expected = "";
    Node node = Parser.parse(code, null, null, null);
    checker.process(node, null);
    assertTrue(checker.getDiagnostics().isEmpty());
  }

  @Test
  public void testFunctionWhereNoThisAnnotation() throws Exception {
    String code = "function Foo() { this.x = 1; }";
    String expected = "2:12  warning JSC_USED_GLOBAL_THIS: dangerous use of the global 'this' object\n";
    Node node = Parser.parse(code, null, null, null);
    compiler.compile(node, null, null);
    assertEquals(expected, compiler.getDiagnostics().getMessages().get(0).message);
  }

  private void assertEquals(String expected, String actual) {
    if (!expected.equals(actual)) {
      throw new AssertionError("Expected: " + expected + " but was: " + actual);
    }
  }
}