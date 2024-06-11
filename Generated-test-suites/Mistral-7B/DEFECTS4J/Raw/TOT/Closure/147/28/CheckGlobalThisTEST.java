import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.CheckGlobalThis;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CompilerOptions options;

  @Before
  public void setup() {
    options = new CompilerOptions(null);
    compiler = new AbstractCompiler(options);
  }

  @Test
  public void testCheckGlobalThis_SafeUses() {
    String code = "function foo() { return this.bar; }";
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    Node tree = AbstractCompiler.parseTree(code);
    NodeTraversal traversal = new NodeTraversal(tree, checker);
    traversal.traverse();
    assertFalse(compiler.getDiagnostics().hasError());
  }

  @Test
  public void testCheckGlobalThis_UnsafeUses_PropertyAccess() {
    String code = "function foo() { this.bar = 42; }";
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    Node tree = AbstractCompiler.parseTree(code);
    NodeTraversal traversal = new NodeTraversal(tree, checker);
    traversal.traverse();
    assertTrue(compiler.getDiagnostics().hasError());
  }

  @Test
  public void testCheckGlobalThis_UnsafeUses_Assignment() {
    String code = "this.bar = 42;";
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    Node tree = AbstractCompiler.parseTree(code);
    NodeTraversal traversal = new NodeTraversal(tree, checker);
    traversal.traverse();
    assertTrue(compiler.getDiagnostics().hasError());
  }

  @Test
  public void testCheckGlobalThis_SafeUses_Constructor() {
    String code = "function Foo() { this.bar = 42; }";
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    Node tree = AbstractCompiler.parseTree(code);
    NodeTraversal traversal = new NodeTraversal(tree, checker);
    traversal.traverse();
    assertFalse(compiler.getDiagnostics().hasError());
  }

  @Test
  public void testCheckGlobalThis_SafeUses_PrototypeMethod() {
    String code = "function Foo() { this.bar = function() { return this.baz; }; }";
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    Node tree = AbstractCompiler.parseTree(code);
    NodeTraversal traversal = new NodeTraversal(tree, checker);
    traversal.traverse();
    assertFalse(compiler.getDiagnostics().hasError());
  }

  @Test
  public void testCheckGlobalThis_SafeUses_FunctionAnnotation() {
    String code = "function @constructor Foo() { this.bar = 42; }";
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    Node tree = AbstractCompiler.parseTree(code);
    NodeTraversal traversal = new NodeTraversal(tree, checker);
    traversal.traverse();
    assertFalse(compiler.getDiagnostics().hasError());
  }

  @Test
  public void testCheckGlobalThis_SafeUses_AnonymousFunction() {
    String code = "var foo = function() { this.bar = 42; };";
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    Node tree = AbstractCompiler.parseTree(code);
    NodeTraversal traversal = new NodeTraversal(tree, checker);
    traversal.traverse();
    assertFalse(compiler.getDiagnostics().hasError());
  }

  @Test
  public void testCheckGlobalThis_SafeUses_ObjectLiteral() {
    String code = "var foo = { bar: function() { return this.baz; } };";
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    Node tree = AbstractCompiler.parseTree(code);
    NodeTraversal traversal = new NodeTraversal(tree, checker);
    traversal.traverse();
    assertFalse(compiler.getDiagnostics().hasError());
  }
}