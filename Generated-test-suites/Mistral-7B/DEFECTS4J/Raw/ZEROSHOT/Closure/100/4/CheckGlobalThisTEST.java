import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static com.google.javascript.rhino.NodeTree.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testNoGlobalThis() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    Node node = parseJs("function foo() {}");

    CheckGlobalThisVisitor visitor = new CheckGlobalThisVisitor(checker);
    node.accept(visitor);

    assertThat(checker.getNumDiagnostics(), is(0));
  }

  @Test
  public void testGlobalThisInFunction() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    Node node = parseJs("function foo() { this.x = 1; }");

    CheckGlobalThisVisitor visitor = new CheckGlobalThisVisitor(checker);
    node.accept(visitor);

    DiagnosticInfo diagnostic = checker.getDiagnostic(0);
    assertThat(diagnostic.getCode(), is(DiagnosticCode.JSC_USED_GLOBAL_THIS));
    assertThat(diagnostic.getMessage(), is("dangerous use of the global 'this' object"));
  }

  @Test
  public void testGlobalThisInAssignment() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    Node node = parseJs("a = this;");
    node = node.getFirstChild(); // get rid of the assignment 'a ='

    CheckGlobalThisVisitor visitor = new CheckGlobalThisVisitor(checker);
    node.accept(visitor);

    DiagnosticInfo diagnostic = checker.getDiagnostic(0);
    assertThat(diagnostic.getCode(), is(DiagnosticCode.JSC_USED_GLOBAL_THIS));
    assertThat(diagnostic.getMessage(), is("dangerous use of the global 'this' object"));
  }

  @Test
  public void testPrototypePropertyAccess() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    Node node = parseJs("var obj = { x: this.y };");

    CheckGlobalThisVisitor visitor = new CheckGlobalThisVisitor(checker);
    node.accept(visitor);

    assertThat(checker.getNumDiagnostics(), is(0));
  }

  @Test
  public void testFunctionWithJsDoc() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    Node node = parseJs("/** @constructor */ function Foo() { this.x = 1; }");

    CheckGlobalThisVisitor visitor = new CheckGlobalThisVisitor(checker);
    node.accept(visitor);

    assertThat(checker.getNumDiagnostics(), is(0));
  }

  @Test
  public void testFunctionWithJsDocAndThisAnnotation() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    Node node = parseJs("/** @constructor @this {x: number} */ function Foo() { this.x = 1; }");

    CheckGlobalThisVisitor visitor = new CheckGlobalThisVisitor(checker);
    node.accept(visitor);

    assertThat(checker.getNumDiagnostics(), is(0));
  }

  @Test
  public void testFunctionWithJsDocAndPrototypePropertyAccess() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    Node node = parseJs("/** @constructor */ function Foo() { this.x.y = 1; }");

    CheckGlobalThisVisitor visitor = new CheckGlobalThisVisitor(checker);
    node.accept(visitor);

    DiagnosticInfo diagnostic = checker.getDiagnostic(0);
    assertThat(diagnostic.getCode(), is(DiagnosticCode.JSC_USED_GLOBAL_THIS));
    assertThat(diagnostic.getMessage(), is("dangerous use of the global 'this' object"));
  }
}

class CheckGlobalThisVisitor extends AbstractNodeVisitor {

  private CheckGlobalThis checker;

  CheckGlobalThisVisitor(CheckGlobalThis checker) {
    this.checker = checker;
  }

  @Override
  public void visit(NodeTraversal t, Node n, Node parent) {
    super.visit(t, n, parent);
    checker.visit(t, n, parent);
  }
}