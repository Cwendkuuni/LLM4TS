import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    String code = "function evil() { this.useful = undefined; }";

    AbstractCompiler compiler = new AbstractCompiler();
    Checker checker = new Checker(new DiagnosticType[0], compiler);
    Node root = Parser.parse(code, null, null);

    CheckGlobalThis check = new CheckGlobalThis(compiler, WARNING);
    new NodeTraversal(check, root).traverse();

    assertTrue(check.compiler.getDiagnostics().size() == 1);
    assertTrue(check.compiler.getDiagnostics().get(0).getMessage().getType() == DiagnosticType.WARNING);
    assertTrue(check.compiler.getDiagnostics().get(0).getMessage().getLineNumber() > 0);
  }

  @Test
  public void testCheckGlobalThisWithConstructor() {
    String code = "function EvilConstructor() { this.useful = undefined; }";

    AbstractCompiler compiler = new AbstractCompiler();
    Checker checker = new Checker(new DiagnosticType[0], compiler);
    Node root = Parser.parse(code, null, null);

    CheckGlobalThis check = new CheckGlobalThis(compiler, WARNING);
    new NodeTraversal(check, root).traverse();

    assertTrue(check.compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testCheckGlobalThisWithPrototypeMethod() {
    String code = "var MyObject = function() {}; MyObject.prototype.evil = function() { this.useful = undefined; }";

    AbstractCompiler compiler = new AbstractCompiler();
    Checker checker = new Checker(new DiagnosticType[0], compiler);
    Node root = Parser.parse(code, null, null);

    CheckGlobalThis check = new CheckGlobalThis(compiler, WARNING);
    new NodeTraversal(check, root).traverse();

    assertTrue(check.compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testCheckGlobalThisWithAnnotatedFunction() {
    String code = "/** @this MyObject */ function MyFunction() { this.useful = undefined; }";

    AbstractCompiler compiler = new AbstractCompiler();
    Checker checker = new Checker(new DiagnosticType[0], compiler);
    Node root = Parser.parse(code, null, null);

    CheckGlobalThis check = new CheckGlobalThis(compiler, WARNING);
    new NodeTraversal(check, root).traverse();

    assertTrue(check.compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testCheckGlobalThisWithNoAnnotations() {
    String code = "function evil() { this.useful = undefined; }";

    AbstractCompiler compiler = new AbstractCompiler();
    Checker checker = new Checker(new DiagnosticType[0], compiler);
    Node root = Parser.parse(code, null, null);

    CheckGlobalThis check = new CheckGlobalThis(compiler, WARNING);
    new NodeTraversal(check, root).traverse();

    assertTrue(check.compiler.getDiagnostics().size() == 1);
    assertTrue(check.compiler.getDiagnostics().get(0).getMessage().getType() == DiagnosticType.WARNING);
    assertTrue(check.compiler.getDiagnostics().get(0).getMessage().getLineNumber() > 0);
  }

  @Test
  public void testCheckGlobalThisWithAssignment() {
    String code = "var a = function() { this.useful = undefined; }";

    AbstractCompiler compiler = new AbstractCompiler();
    Checker checker = new Checker(new DiagnosticType[0], compiler);
    Node root = Parser.parse(code, null, null);

    CheckGlobalThis check = new CheckGlobalThis(compiler, WARNING);
    new NodeTraversal(check, root).traverse();

    assertTrue(check.compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testCheckGlobalThisWithNestedAssignments() {
    String code = "var a = { b: function() { this.useful = undefined; } }; a.b = c;";

    AbstractCompiler compiler = new AbstractCompiler();
    Checker checker = new Checker(new DiagnosticType[0], compiler);
    Node root = Parser.parse(code, null, null);

    CheckGlobalThis check = new CheckGlobalThis(compiler, WARNING);
    new NodeTraversal(check, root).traverse();

    assertTrue(check.compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testCheckGlobalThisWithFunctionCall() {
    String code = "function evil() { this.useful = undefined; } evil();";

    AbstractCompiler compiler = new AbstractCompiler();
    Checker checker = new Checker(new DiagnosticType[0], compiler);
    Node root = Parser.parse(code, null, null);

    CheckGlobalThis check = new CheckGlobalThis(compiler, WARNING);
    new NodeTraversal(check, root).traverse();

    assertTrue(check.compiler.getDiagnostics().size() == 1);
    assertTrue(check.compiler.getDiagnostics().get(0).getMessage().getType() == DiagnosticType.WARNING);
    assertTrue(check.compiler.getDiagnostics().get(0).getMessage().getLineNumber() > 0);
  }

  @Test
  public void testCheckGlobalThisWithVariableDeclaration() {
    String code = "var evil = function() { this.useful = undefined; }";

    AbstractCompiler compiler = new AbstractCompiler();
    Checker checker = new Checker(new DiagnosticType[0], compiler);
    Node root = Parser.parse(code, null, null);

    CheckGlobalThis check = new CheckGlobalThis(compiler, WARNING);
    new NodeTraversal(check, root).traverse();

    assertTrue(check.compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testCheckGlobalThisWithVariableAssignment() {
    String code = "var a = { use: 'global' }; a.evil = function() { this.useful = undefined; }";

    AbstractCompiler compiler = new AbstractCompiler();
    Checker checker = new Checker(new DiagnosticType[0], compiler);
    Node root = Parser.parse(code, null, null);

    CheckGlobalThis check = new CheckGlobalThis(compiler, WARNING);
    new NodeTraversal(check, root).traverse();

    assertTrue(check.compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testCheckGlobalThisWithComplexObjectExpression() {
    String code = "var a = { b: { c: function() { this.useful = undefined; } } }";

    AbstractCompiler compiler = new AbstractCompiler();
    Checker checker = new Checker(new DiagnosticType[0], compiler);
    Node root = Parser.parse(code, null, null);

    CheckGlobalThis check = new CheckGlobalThis(compiler, WARNING);
    new NodeTraversal(check, root).traverse();

    assertTrue(check.compiler.getDiagnostics().isEmpty());
  }
}