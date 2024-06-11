import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.rhino.Node;

public class CheckGlobalThisTest {

  @org.junit.Test
  public void testSafeUseOfThis() {
    CompilerOptions options = new CompilerOptions(new AbstractCompiler());
    CheckGlobalThis checker = new CheckGlobalThis(options.getAbstractCompiler(), WARNING);

    Node code = new Node("function foo() { this.x = 5; }");
    NodeTraversal traversal = new NodeTraversal(code, checker);
    traversal.traverse();
    assertEquals(0, checker.compiler.getDiagnostics().size());
  }

  @org.junit.Test
  public void testUnsafeUseOfThisAsProperty() {
    CompilerOptions options = new CompilerOptions(new AbstractCompiler());
    CheckGlobalThis checker = new CheckGlobalThis(options.getAbstractCompiler(), WARNING);

    Node code = new Node("function foo() { this.x = 5; } function bar() { foo.x = 10; }");
    NodeTraversal traversal = new NodeTraversal(code, checker);
    traversal.traverse();
    assertEquals(1, checker.compiler.getDiagnostics().size());
    assertEquals(DiagnosticType.WARNING, checker.compiler.getDiagnostics().get(0).getType());
  }

  @org.junit.Test
  public void testUnsafeUseOfThisAsFunction() {
    CompilerOptions options = new CompilerOptions(new AbstractCompiler());
    CheckGlobalThis checker = new CheckGlobalThis(options.getAbstractCompiler(), WARNING);

    Node code = new Node("function foo() { this.x = 5; } function bar() { this(); }");
    NodeTraversal traversal = new NodeTraversal(code, checker);
    traversal.traverse();
    assertEquals(1, checker.compiler.getDiagnostics().size());
    assertEquals(DiagnosticType.WARNING, checker.compiler.getDiagnostics().get(0).getType());
  }

  @org.junit.Test
  public void testUnsafeUseOfThisInPrototypeFunction() {
    CompilerOptions options = new CompilerOptions(new AbstractCompiler());
    CheckGlobalThis checker = new CheckGlobalThis(options.getAbstractCompiler(), WARNING);

    Node code = new Node("function Foo() {this.x = 5;} Foo.prototype.bar = function() { this.x = 10; }");
    NodeTraversal traversal = new NodeTraversal(code, checker);
    traversal.traverse();
    assertEquals(1, checker.compiler.getDiagnostics().size());
    assertEquals(DiagnosticType.WARNING, checker.compiler.getDiagnostics().get(0).getType());
  }

  @org.junit.Test
  public void testSafeUseOfThisInFunctionAnnotatedWithConstructor() {
    CompilerOptions options = new CompilerOptions(new AbstractCompiler());
    CheckGlobalThis checker = new CheckGlobalThis(options.getAbstractCompiler(), WARNING);

    Node code = new Node("/** @constructor */ function Foo() { this.x = 5; }");
    NodeTraversal traversal = new NodeTraversal(code, checker);
    traversal.traverse();
    assertEquals(0, checker.compiler.getDiagnostics().size());
  }

  @org.junit.Test
  public void testSafeUseOfThisInFunctionAnnotatedWithThis() {
    CompilerOptions options = new CompilerOptions(new AbstractCompiler());
    CheckGlobalThis checker = new CheckGlobalThis(options.getAbstractCompiler(), WARNING);

    Node code = new Node("function foo(@this {x: number}) { this.x = this.x + 1; }");
    NodeTraversal traversal = new NodeTraversal(code, checker);
    traversal.traverse();
    assertEquals(0, checker.compiler.getDiagnostics().size());
  }
}