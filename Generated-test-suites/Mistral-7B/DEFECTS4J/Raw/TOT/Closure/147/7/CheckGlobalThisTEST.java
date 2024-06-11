import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.Node;

public class CheckGlobalThisTest {

  private CheckGlobalThis checker;

  @org.junit.Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions(true, null, null);
    AbstractCompiler compiler = new AbstractCompiler(options);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
  }

  @org.junit.Test
  public void testGlobalThisInFunction() {
    SourceFile source = new SourceFile("Test.js", "function test() { this.prop = 5; }");
    Node ast = Parser.parse(source, null, null);
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();
    assertTrue(checker.compiler.getDiagnostics().isEmpty());

    source = new SourceFile("Test.js", "function test() { this.prop = this.prop; }");
    ast = Parser.parse(source, null, null);
    traversal = new NodeTraversal(ast, checker);
    traversal.traverse();
    assertFalse(checker.compiler.getDiagnostics().isEmpty());
    DiagnosticType diagnostic = checker.compiler.getDiagnostics().get(0);
    assertNotNull(diagnostic);
    assertTrue(diagnostic.getCode() == CheckGlobalThis.GLOBAL_THIS.getCode());
    assertTrue(diagnostic.getMessage().equals("dangerous use of the global 'this' object"));
  }

  @org.junit.Test
  public void testThisInPrototypeMethod() {
    SourceFile source = new SourceFile("Test.js", "function Test() {} Test.prototype.test = function() { this.prop = 5; }");
    Node ast = Parser.parse(source, null, null);
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();
    assertTrue(checker.compiler.getDiagnostics().isEmpty());
  }

  @org.junit.Test
  public void testThisInConstructor() {
    SourceFile source = new SourceFile("Test.js", "function Test() { this.prop = 5; }");
    Node ast = Parser.parse(source, null, null);
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();
    assertTrue(checker.compiler.getDiagnostics().isEmpty());
  }

  @org.junit.Test
  public void testThisInFunctionWithJsDoc() {
    SourceFile source = new SourceFile("Test.js", "@constructor\nfunction Test() { this.prop = 5; }");
    Node ast = Parser.parse(source, null, null);
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();
    assertTrue(checker.compiler.getDiagnostics().isEmpty());
  }

  @org.junit.Test
  public void testThisInFunctionWithThisAnnotation() {
    SourceFile source = new SourceFile("Test.js", "function Test() { @this\nthis.prop = 5; }");
    Node ast = Parser.parse(source, null, null);
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();
    assertTrue(checker.compiler.getDiagnostics().isEmpty());
  }

  @org.junit.Test
  public void testThisInFunctionWithNoAnnotation() {
    SourceFile source = new SourceFile("Test.js", "function Test() { this.prop = 5; }");
    Node ast = Parser.parse(source, null, null);
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();
    assertTrue(checker.compiler.getDiagnostics().isEmpty());
  }

  @org.junit.Test
  public void testThisInAssignment() {
    SourceFile source = new SourceFile("Test.js", "var test = this;");
    Node ast = Parser.parse(source, null, null);
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();
    assertTrue(checker.compiler.getDiagnostics().isEmpty());
  }

  @org.junit.Test
  public void testThisInPropertyAccess() {
    SourceFile source = new SourceFile("Test.js", "var test = { prop: this };");
    Node ast = Parser.parse(source, null, null);
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();
    assertTrue(checker.compiler.getDiagnostics().isEmpty());
  }

  @org.junit.Test
  public void testThisInPrototypePropertyAccess() {
    SourceFile source = new SourceFile("Test.js", "function Test() {} Test.prototype.test = function() { this.prop.prop = 5; }");
    Node ast = Parser.parse(source, null, null);
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();
    assertTrue(checker.compiler.getDiagnostics().isEmpty());
  }
}