import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParserOptions;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class VarCheckTest {

  private Compiler compiler;
  private CompilerOptions options;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() {
    options = new CompilerOptions();
    options.setAssumeFunctionCallsAreValid(true);
    options.setAssumeFunctionDeclarationsAreValid(true);
    options.setAssumeNoTypeErrors(true);
    options.setAssumeNoModules(true);

    nodeFactory = new NodeFactory();
    compiler = new Compiler(options);
  }

  @After
  public void tearDown() {
    compiler = null;
  }

  @Test
  public void testValidateEmptyFile() {
    String code = "";
    Node root = parser(code).getAstRoot(compiler);
    VarCheck pass = new VarCheck(compiler);
    pass.process(null, root);
    assertFalse("Empty file should not produce any errors", pass.getNumErrors() > 0);
  }

  @Test
  public void testValidateVariableDeclaration() {
    String code = "var x = 5;";
    Node root = parser(code).getAstRoot(compiler);
    VarCheck pass = new VarCheck(compiler);
    pass.process(null, root);
    assertFalse("Variable declaration should not produce any errors", pass.getNumErrors() > 0);
  }

  @Test
  public void testValidateUndefinedVariable() {
    String code = "x = 5;";
    Node root = parser(code).getAstRoot(compiler);
    VarCheck pass = new VarCheck(compiler);
    pass.process(null, root);
    assertTrue("Undefined variable should produce an error", pass.getNumErrors() > 0);
  }

  @Test
  public void testValidateVariableDeclarationInFunction() {
    String code = "function foo() {\n  var x = 5;\n}";
    Node root = parser(code).getAstRoot(compiler);
    VarCheck pass = new VarCheck(compiler);
    pass.process(null, root);
    assertFalse("Variable declaration inside a function should not produce any errors", pass.getNumErrors() > 0);
  }

  @Test
  public void testValidateUndefinedVariableInFunction() {
    String code = "function foo() {\n  x = 5;\n}";
    Node root = parser(code).getAstRoot(compiler);
    VarCheck pass = new VarCheck(compiler);
    pass.process(null, root);
    assertTrue("Undefined variable inside a function should produce an error", pass.getNumErrors() > 0);
  }

  @Test
  public void testValidateVariableDeclarationInModule() {
    String code = "var x = 5;\nmodule foo {\n  function bar() {}\n}";
    Node root = parser(code).getAstRoot(compiler);
    VarCheck pass = new VarCheck(compiler);
    pass.process(null, root);
    assertFalse("Variable declaration inside a module should not produce any errors", pass.getNumErrors() > 0);
  }

  @Test
  public void testValidateUndefinedVariableInModule() {
    String code = "module foo {\n  function bar() {x = 5;}\n}";
    Node root = parser(code).getAstRoot(compiler);
    VarCheck pass = new VarCheck(compiler);
    pass.process(null, root);
    assertTrue("Undefined variable inside a module should produce an error", pass.getNumErrors() > 0);
  }

  @Test
  public void testValidateModuleDependency() {
    String codeA = "module a {\n  var x = 5;\n}";
    String codeB = "module b {\n  function bar() {\n    x = 5;\n  }\n}";
    Node rootA = parser(codeA).getAstRoot(compiler);
    Node rootB = parser(codeB).getAstRoot(compiler);
    VarCheck pass = new VarCheck(compiler);
    pass.process(parser(codeA).getExterns(), rootA);
    pass.process(parser(codeB).getExterns(), rootB);
    assertFalse("Module dependencies should not produce any errors", pass.getNumErrors() > 0);
  }

  @Test
  public void testValidateMissingModuleDependency() {
    String codeA = "module a {\n  var x = 5;\n}";
    String codeB = "module b {\n  function bar() {\n    x = 5;\n  }\n  import 'a';\n}";
    Node rootA = parser(codeA).getAstRoot(compiler);
    Node rootB = parser(codeB).getAstRoot(compiler);
    VarCheck pass = new VarCheck(compiler);
    pass.process(parser(codeA).getExterns(), rootA);
    pass.process(parser(codeB).getExterns(), rootB);
    assertTrue("Missing module dependency should produce a warning", pass.getNumWarnings() > 0);
  }

  @Test
  public void testValidateStrictModuleDependency() {
    String codeA = "module a {\n  export var x = 5;\n}";
    String codeB = "module b {\n  function bar() {\n    x = 5;\n  }\n  import 'a';\n}";
    Node rootA = parser(codeA).getAstRoot(compiler);
    Node rootB = parser(codeB).getAstRoot(compiler);
    VarCheck pass = new VarCheck(compiler);
    pass.process(parser(codeA).getExterns(), rootA);
    pass.process(parser(codeB).getExterns(), rootB);
    assertFalse("Strict module dependency should not produce any errors", pass.getNumErrors() > 0);
  }

  private ParserOptions createParserOptions() {
    ParserOptions options = new ParserOptions();
    options.setAllowAllTypes(true);
    options.setAllowReserved(true);
    return options;
  }

  private Node parser(String code) {
    ParserOptions parserOptions = createParserOptions();
    Node root = compiler.parse(code, parserOptions).getAstRoot(compiler);
    return root;
  }

  private void assertErrors(VarCheck pass, int expectedErrors) {
    int actualErrors = pass.getNumErrors();
    if (expectedErrors != actualErrors) {
      fail("Expected " + expectedErrors + " errors but got " + actualErrors);
    }
  }

  private void assertWarnings(VarCheck pass, int expectedWarnings) {
    int actualWarnings = pass.getNumWarnings();
    if (expectedWarnings != actualWarnings) {
      fail("Expected " + expectedWarnings + " warnings but got " + actualWarnings);
    }
  }

  private void assertNoErrors(VarCheck pass) {
    assertFalse("Expected no errors but got " + pass.getNumErrors() + " errors", pass.hasErrors());
  }

  private void assertNoWarnings(VarCheck pass) {
    assertFalse("Expected no warnings but got " + pass.getNumWarnings() + " warnings", pass.hasWarnings());
  }

  private void assertError(VarCheck pass, JSError expectedError) {
    JSError actualError = pass.getDiagnostics().getErrors().get(0);
    assertEquals(expectedError.getCode(), actualError.getCode());
    assertEquals(expectedError.getMessage(), actualError.getMessage());
  }

  private void assertWarning(VarCheck pass, JSError expectedWarning) {
    JSError actualWarning = pass.getDiagnostics().getWarnings().get(0);
    assertEquals(expectedWarning.getCode(), actualWarning.getCode());
    assertEquals(expectedWarning.getMessage(), actualWarning.getMessage());
  }

  private void assertEquals(Object expected, Object actual) {
    if (expected instanceof JSError && actual instanceof JSError) {
      assertEquals(((JSError) expected).getMessage(),
          ((JSError) actual).getMessage());
      assertEquals(((JSError) expected).getCode(),
          ((JSError) actual).getCode());
    } else if (expected != null) {
      assertEquals(expected.toString(), actual.toString());
    } else {
      assertEquals(expected, actual);
    }
  }
}