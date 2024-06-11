import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.*;
import static com.google.javascript.rhino.NodeType.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.TestSourceFile;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    CheckGlobalThis checker = new CheckGlobalThis(null, CheckLevel.WARNING);

    // Test empty script
    String script = "function evil() {}";
    TestSourceFile sourceFile = new TestSourceFile(CheckGlobalThis.class, script);
    AbstractCompiler compiler = new AbstractCompiler();
    checker.traverse(new NodeTraversal(compiler, sourceFile.ast(), null));
    assertEquals(0, compiler.getDiagnostics().size());

    // Test unsafe usage of this in a function
    script = "function evil() { this.useful = undefined; }";
    sourceFile = new TestSourceFile(CheckGlobalThis.class, script);
    compiler = new AbstractCompiler();
    checker.traversalStart(compiler);
    checker.visit(new NodeTraversal(compiler, sourceFile.ast(), null));
    checker.traversalDone(compiler);
    assertEquals(1, compiler.getDiagnostics().size());
    DiagnosticType diagnostic = compiler.getDiagnostics().get(0);
    assertEquals(CheckGlobalThis.GLOBAL_THIS, diagnostic.getType());
    assertEquals("dangerous use of the global 'this' object", diagnostic.getMessage());

    // Test safe usage of this in a constructor
    script = "function MyClass() {}";
    sourceFile = new TestSourceFile(CheckGlobalThis.class, script);
    compiler = new AbstractCompiler();
    checker.traverse(new NodeTraversal(compiler, sourceFile.ast(), null));
    assertEquals(0, compiler.getDiagnostics().size());

    // Test safe usage of this in a prototype method
    script = "function MyClass() {} MyClass.prototype.safe = function() {};";
    sourceFile = new TestSourceFile(CheckGlobalThis.class, script);
    compiler = new AbstractCompiler();
    checker.traverse(new NodeTraversal(compiler, sourceFile.ast(), null));
    assertEquals(0, compiler.getDiagnostics().size());

    // Test safe usage of this in a function with a @this annotation
    script = "/** @this {myType} */ function safe(x) { return this.x + x; }";
    sourceFile = new TestSourceFile(CheckGlobalThis.class, script);
    compiler = new AbstractCompiler();
    checker.traverse(new NodeTraversal(compiler, sourceFile.ast(), null));
    assertEquals(0, compiler.getDiagnostics().size());

    // Test safe usage of this in a function with no logical place to put a @this annotation
    script = "function safe(x) { return this.x + x; }";
    sourceFile = new TestSourceFile(CheckGlobalThis.class, script);
    compiler = new AbstractCompiler();
    checker.traverse(new NodeTraversal(compiler, sourceFile.ast(), null));
    assertEquals(0, compiler.getDiagnostics().size());

    // Test safe usage of this in a function with multiple assignments
    script = "function safe() { var x = this; this.y = 1; }";
    sourceFile = new TestSourceFile(CheckGlobalThis.class, script);
    compiler = new AbstractCompiler();
    checker.traverse(new NodeTraversal(compiler, sourceFile.ast(), null));
    assertEquals(0, compiler.getDiagnostics().size());

    // Test safe usage of this in a function with nested assignments
    script = "(function() { var x = this; this.y = {z: function() { this.w = 1; }}; })();";
    sourceFile = new TestSourceFile(CheckGlobalThis.class, script);
    compiler = new AbstractCompiler();
    checker.traverse(new NodeTraversal(compiler, sourceFile.ast(), null));
    assertEquals(0, compiler.getDiagnostics().size());

    // Test safe usage of this in a function where it is assigned to a variable
    script = "function safe() { var a = this; }";
    sourceFile = new TestSourceFile(CheckGlobalThis.class, script);
    compiler = new AbstractCompiler();
    checker.traverse(new NodeTraversal(compiler, sourceFile.ast(), null));
    assertEquals(0, compiler.getDiagnostics().size());

    // Test safe usage of this in a getter method
    script = "function MyClass() {} MyClass.prototype.getX = function() { return this.x; }";
    sourceFile = new TestSourceFile(CheckGlobalThis.class, script);
    compiler = new AbstractCompiler();
    checker.traverse(new NodeTraversal(compiler, sourceFile.ast(), null));
    assertEquals(0, compiler.getDiagnostics().size());

    // Test safe usage of this in a setter method
    script = "function MyClass() {} MyClass.prototype.setX = function(value) { this.x = value; }";
    sourceFile = new TestSourceFile(CheckGlobalThis.class, script);
    compiler = new AbstractCompiler();
    checker.traverse(new NodeTraversal(compiler, sourceFile.ast(), null));
    assertEquals(0, compiler.getDiagnostics().size());

    // Test safe usage of this in a function with a property access
    script = "function safe() { var x = this.y; }";
    sourceFile = new TestSourceFile(CheckGlobalThis.class, script);
    compiler = new AbstractCompiler();
    checker.traverse(new NodeTraversal(compiler, sourceFile.ast(), null));
    assertEquals(0, compiler.getDiagnostics().size());

    // Test safe usage of this in a function with a nested property access
    script = "function safe() { var x = this.y.z; }";
    sourceFile = new TestSourceFile(CheckGlobalThis.class, script);
    compiler = new AbstractCompiler();
    checker.traverse(new NodeTraversal(compiler, sourceFile.ast(), null));
    assertEquals(0, compiler.getDiagnostics().size());

    // Test safe usage of this in a function with a multiple property access
    script = "function safe() { var x = this.y.z.w; }";
    sourceFile = new TestSourceFile(CheckGlobalThis.class, script);
    compiler = new AbstractCompiler();
    checker.traverse(new NodeTraversal(compiler, sourceFile.ast(), null));
    assertEquals(0, compiler.getDiagnostics().size());

    // Test safe usage of this in a function with a method call
    script = "function safe() { var x = someMethod.call(this); }";
    sourceFile = new TestSourceFile(CheckGlobalThis.class, script);
    compiler = new AbstractCompiler();
    checker.traverse(new NodeTraversal(compiler, sourceFile.ast(), null));
    assertEquals(0, compiler.getDiagnostics().size());

    // Test safe usage of this in a function with a constructor call
    script = "function safe() { var x = new MyClass(); }";
    sourceFile = new TestSourceFile(CheckGlobalThis.class, script);
    compiler = new AbstractCompiler();
    checker.traverse(new NodeTraversal(compiler, sourceFile.ast(), null));
    assertEquals(0, compiler.getDiagnostics().size());

    // Test safe usage of this in a function with an array index
    script = "function safe() { var x = this[0]; }";
    sourceFile = new TestSourceFile(CheckGlobalThis.class, script);
    compiler = new AbstractCompiler();
    checker.traverse(new NodeTraversal(compiler, sourceFile.ast(), null));
    assertEquals(0, compiler.getDiagnostics().size());

    // Test safe usage of this in a function with a for loop
    script = "function safe() { for (var i = 0; i < this.length; ++i) {} }";
    sourceFile = new TestSourceFile(CheckGlobalThis.class, script);
    compiler = new AbstractCompiler();
    checker.traverse(new NodeTraversal(compiler, sourceFile.ast(), null));
    assertEquals(0, compiler.getDiagnostics().size());

    // Test safe usage of this in a function with a for in loop
    script = "function safe() { for (var key in this) {} }";
    sourceFile = new TestSourceFile(CheckGlobalThis.class, script);
    compiler = new AbstractCompiler();
    checker.traverse(new NodeTraversal(compiler, sourceFile.ast(), null));
    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testCheckGlobalThisWithJsDoc() {
    CheckGlobalThis checker = new CheckGlobalThis(null, CheckLevel.WARNING);

    // Test safe usage of this in a constructor with JSDoc
    String script = "/** @constructs MyClass */ function MyClass() {}";
    TestSourceFile sourceFile = new TestSourceFile(CheckGlobalThis.class, script);
    AbstractCompiler compiler = new AbstractCompiler();
    checker.traverse(new NodeTraversal(compiler, sourceFile.ast(), null));
    assertEquals(0, compiler.getDiagnostics().size());

    // Test safe usage of this in a function with JSDoc
    script = "/** @this {MyType} */ function safe() {}";
    sourceFile = new TestSourceFile(CheckGlobalThis.class, script);
    compiler = new AbstractCompiler();
    checker.traverse(new NodeTraversal(compiler, sourceFile.ast(), null));
    assertEquals(0, compiler.getDiagnostics().size());

    // Test safe usage of this in a function with multiple JSDocs
    script = "/** @this {MyType} */ function safe() {} /** @this {AnotherType} */ function safe() {}";
    sourceFile = new TestSourceFile(CheckGlobalThis.class, script);
    compiler = new AbstractCompiler();
    checker.traverse(new NodeTraversal(compiler, sourceFile.ast(), null));
    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testCheckGlobalThisWithException() {
    CheckGlobalThis checker = new CheckGlobalThis(null, CheckLevel.ERROR);

    // Test error reporting for unsafe usage of this in a function
    String script = "function evil() { this.useful = undefined; }";
    TestSourceFile sourceFile = new TestSourceFile(CheckGlobalThis.class, script);
    AbstractCompiler compiler = new AbstractCompiler();
    checker.traverse(new NodeTraversal(compiler, sourceFile.ast(), null));
    assertEquals(1, compiler.getDiagnostics().size());
    DiagnosticType diagnostic = compiler.getDiagnostics().get(0);
    assertEquals(CheckGlobalThis.GLOBAL_THIS, diagnostic.getType());
    assertEquals("dangerous use of the global 'this' object", diagnostic.getMessage());

    // Test error reporting for unsafe usage of this in a function with a @this annotation
    script = "/** @this {MyType} */ function evil() { this.useful = undefined; }";
    sourceFile = new TestSourceFile(CheckGlobalThis.class, script);
    compiler = new AbstractCompiler();
    checker.traverse(new NodeTraversal(compiler, sourceFile.ast(), null));
    assertEquals(1, compiler.getDiagnostics().size());
    diagnostic = compiler.getDiagnostics().get(0);
    assertEquals(CheckGlobalThis.GLOBAL_THIS, diagnostic.getType());
    assertEquals("dangerous use of the global 'this' object", diagnostic.getMessage());

    // Test error reporting for unsafe usage of this in a function with a @constructor annotation
    script = "/** @constructor */ function MyClass() { this.useful = undefined; }";
    sourceFile = new TestSourceFile(CheckGlobalThis.class, script);
    compiler = new AbstractCompiler();
    checker.traverse(new NodeTraversal(compiler, sourceFile.ast(), null));
    assertEquals(0, compiler.getDiagnostics().size());

    // Test error reporting for unsafe usage of this in a prototype method
    script = "function MyClass() {} MyClass.prototype.evil = function() { this.useful = undefined; }";
    sourceFile = new TestSourceFile(CheckGlobalThis.class, script);
    compiler = new AbstractCompiler();
    checker.traverse(new NodeTraversal(compiler, sourceFile.ast(), null));
    assertEquals(1, compiler.getDiagnostics().size());
    diagnostic = compiler.getDiagnostics().get(0);
    assertEquals(CheckGlobalThis.GLOBAL_THIS, diagnostic.getType());
    assertEquals("dangerous use of the global 'this' object", diagnostic.getMessage());
  }
}