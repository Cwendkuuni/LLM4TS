package com.google.javascript.jscomp;

import static com.google.javascript.jscomp.CheckLevel.WARNING;
import static com.google.javascript.jscomp.Diagnostics.error;
import static com.google.javascript.jscomp.Diagnostics.warning;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParserTestBase;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceFile;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest extends ParserTestBase {

  private CheckGlobalThis checker;
  private NodeTraversal traversal;

  @Override
  protected SourceFile createSourceFile(String src) throws Exception {
    return sourceFile("Test.js", src);
  }

  @Override
  protected CompilerOptions getOptions() {
    return CompilerOptions.defaultOptions();
  }

  @Override
  protected AbstractCompiler getCompiler() throws Exception {
    AbstractCompiler compiler = super.getCompiler();
    checker = new CheckGlobalThis(compiler, WARNING);
    return compiler;
  }

  @Override
  protected void processSourceFile(Node externalSources, SourceFile src) throws Exception {
    super.processSourceFile(externalSources, src);
    traversal = new NodeTraversal(src.getRoot(), checker);
    src.getRoot().accept(traversal);
  }

  @Test
  public void testNoGlobalThis() {
    test("function foo() {}");
    test("function foo(x) {}");
    test("function foo(x) { return x; }");
    test("var a = function foo() {}");
    test("var a = function foo(x) {}");
    test("var a = function foo(x) { return x; }");
  }

  @Test
  public void testGlobalThisInFunction() {
    test("function foo() { var x = this; }");
    test("function foo() { return this; }");
    test("function foo() { return this.x; }");
    test("function foo() { this.x = 1; }");
  }

  @Test
  public void testGlobalThisInConstructor() {
    test("function Foo() { this.x = 1; }");
    test("function Foo() { this.prototype.x = 1; }");
  }

  @Test
  public void testGlobalThisInMethod() {
    test("function Foo() { this.prototype.m = function() { return this; }; }");
    test("function Foo() { this.prototype.m = function m() { return this; }; }");
  }

  @Test
  public void testGlobalThisWithAssignment() {
    test("(a = this).x = 1;");
    test("(a = this).prototype.x = 1;");
  }

  @Test
  public void testGlobalThisWithGetProperty() {
    test("(a.prototype = this).x = 1;");
    test("(a.prototype = this).prototype.x = 1;");
  }

  @Test
  public void testNoReportingForPrototypeAccess() {
    test("var a = { get x() { return this; } }; function foo() { a.x = 1; }");
  }

  @Test
  public void testNoReportingForPrototypeMethod() {
    test("function Foo() { this.prototype.m = function() { return this; }; }");
    test("function Foo() { this.prototype.m = function m() { return this; }; }");
  }

  @Test
  public void testNoReportingForAnnotatedFunction() {
    test("/** @constructor */ function Foo() {}");
  }

  @Test
  public void testNoReportingForAnnotatedMethod() {
    test("function Foo() { this.prototype.m = /** @constructor */ function m() {} };");
  }

  @Test
  public void testNoReportingForAnnotatedProperty() {
    test("function Foo() { this.prototype.m = { get x() { return /** @this {type: 'Foo'} */ this; } }; }");
  }

  @Test
  public void testNoReportingForAnnotatedVariable() {
    test("function Foo() { var /** @this {type: 'Foo'} */ x = this; }");
  }

  @Test
  public void testNoReportingForAnnotatedParameter() {
    test("function Foo(/** @this {type: 'Foo'} */ x) {}");
  }

  @Test
  public void testCheckGlobalThisOnLeftSideOfAssignment() {
    testWithDiagnostics("function foo() { this = {}; }",
        warning(GLOBAL_THIS, 1, 1, "dangerous use of the global 'this' object"));
  }

  @Test
  public void testCheckGlobalThisOnLeftSideOfPropertyAccess() {
    testWithDiagnostics("function foo() { x = this.x; }",
        warning(GLOBAL_THIS, 1, 1, "dangerous use of the global 'this' object"));
  }

  @Test
  public void testCheckGlobalThisOnLeftSideOfPrototypePropertyAccess() {
    testWithDiagnostics("function foo() { this.prototype.x = 1; }",
        warning(GLOBAL_THIS, 1, 1, "dangerous use of the global 'this' object"));
  }

  @Test
  public void testCheckGlobalThisOnLeftSideOfPrototypeMethodCall() {
    testWithDiagnostics("function foo() { this.prototype.m(); }",
        warning(GLOBAL_THIS, 1, 1, "dangerous use of the global 'this' object"));
  }

  @Test
  public void testCheckGlobalThisOnLeftSideOfPrototypeGetterCall() {
    testWithDiagnostics("function foo() { this.prototype.x = 1; }",
        warning(GLOBAL_THIS, 1, 1, "dangerous use of the global 'this' object"),
        warning(GLOBAL_THIS, 1, 3, "dangerous use of the global 'this' object"));
  }

  private void test(String src) throws Exception {
    testWithDiagnostics(src);
  }

  private void testWithDiagnostics(String src, DiagnosticType... expected) {
    testWithDiagnostics(src, null, expected);
  }

  private void testWithDiagnostics(String src, String[] expectedMessages, DiagnosticType... expectedTypes) {
    test(src, null, expectedTypes, expectedMessages);
  }

  private void testWithDiagnostics(String src, Node node, DiagnosticType... expectedTypes) {
    testWithDiagnostics(src, node, expectedTypes, null);
  }

  private void testWithDiagnostics(String src, Node node, DiagnosticType[] expectedTypes, String[] expectedMessages) {
    test(src);
    Diagnostics diagnostics = compiler.getDiagnostics();
    int length = diagnostics.length();
    for (int i = 0; i < length; ++i) {
      Diagnostic diagnostic = diagnostics.get(i);
      if (expectedTypes != null && !isMatch(expectedTypes, diagnostic.getType())) {
        continue;
      }
      if (expectedMessages != null && !isMatch(expectedMessages, diagnostic.getMessage())) {
        continue;
      }
      if (node != null && !isMatch(node, diagnostic.getSourceNode())) {
        continue;
      }
      assertEquals("Unexpected diagnostic", diagnostic, error(diagnostic.getMessage(), diagnostic.getSourceNode(), diagnostic.getStartPosition(), diagnostic.getEndPosition()));
      break;
    }
    assertNull("Missing diagnostic", error(src, 0, 0, null, GLOBAL_THIS));
  }

  private static boolean isMatch(Node expectedNode, Node node) {
    if (expectedNode == null && node == null) {
      return true;
    }
    if (expectedNode == null || node == null) {
      return false;
    }
    return expectedNode.isIdenticalTo(node);
  }

  private static boolean isMatch(DiagnosticType[] expectedTypes, DiagnosticType type) {
    if (expectedTypes == null) {
      return true;
    }
    for (DiagnosticType expectedType : expectedTypes) {
      if (expectedType.equals(type)) {
        return true;
      }
    }
    return false;
  }

  private static boolean isMatch(String[] expectedMessages, String message) {
    if (expectedMessages == null) {
      return true;
    }
    for (String expectedMessage : expectedMessages) {
      if (expectedMessage.equals(message)) {
        return true;
      }
    }
    return false;
  }

  private static NodeFactory nodeFactory;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    nodeFactory = compiler.getFactory();
  }

  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
    checker = null;
    traversal = null;
    nodeFactory = null;
  }
}