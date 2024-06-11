import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParseTree;
import com.google.javascript.rhino.SourceFile;

import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testSafeUsage() {
    SourceFile source = new SourceFile("Test.js");
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    CheckGlobalThis check = new CheckGlobalThis(compiler, WARNING);

    String code = "function Foo() {};\n" +
                  "Foo.prototype.bar = function() {};";
    ParseTree tree = source.parse(code, null, null);

    Node root = tree.getRootNode();
    NodeTraversal traversal = new NodeTraversal(root, check);
    traversal.traverseAllChildren();

    assertEquals(0, check.getNumDiagnostics());
  }

  @Test
  public void testUnsafeAssignment() {
    SourceFile source = new SourceFile("Test.js");
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    CheckGlobalThis check = new CheckGlobalThis(compiler, WARNING);

    String code = "var a = this;";
    ParseTree tree = source.parse(code, null, null);

    Node root = tree.getRootNode();
    NodeTraversal traversal = new NodeTraversal(root, check);
    traversal.traverseAllChildren();

    assertEquals(1, check.getNumDiagnostics());
    assertEquals("Test.js:1:7: JSC_USED_GLOBAL_THIS", check.getDiagnostics()[0].getMessage());
  }

  @Test
  public void testUnsafePropertyAccess() {
    SourceFile source = new SourceFile("Test.js");
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    CheckGlobalThis check = new CheckGlobalThis(compiler, WARNING);

    String code = "this.foo = 42;";
    ParseTree tree = source.parse(code, null, null);

    Node root = tree.getRootNode();
    NodeTraversal traversal = new NodeTraversal(root, check);
    traversal.traverseAllChildren();

    assertEquals(1, check.getNumDiagnostics());
    assertEquals("Test.js:1:6: JSC_USED_GLOBAL_THIS", check.getDiagnostics()[0].getMessage());
  }

  @Test
  public void testUnsafeFunctionCall() {
    SourceFile source = new SourceFile("Test.js");
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    CheckGlobalThis check = new CheckGlobalThis(compiler, WARNING);

    String code = "function foo() {};\n" +
                  "foo();";
    ParseTree tree = source.parse(code, null, null);

    Node root = tree.getRootNode();
    NodeTraversal traversal = new NodeTraversal(root, check);
    traversal.traverseAllChildren();

    assertEquals(1, check.getNumDiagnostics());
    assertEquals("Test.js:4:4: JSC_USED_GLOBAL_THIS", check.getDiagnostics()[0].getMessage());
  }

  @Test
  public void testConstructor() {
    SourceFile source = new SourceFile("Test.js");
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    CheckGlobalThis check = new CheckGlobalThis(compiler, WARNING);

    String code = "function Foo() { this.foo = 42; }";
    ParseTree tree = source.parse(code, null, null);

    Node root = tree.getRootNode();
    NodeTraversal traversal = new NodeTraversal(root, check);
    traversal.traverseAllChildren();

    assertEquals(0, check.getNumDiagnostics());
  }

  @Test
  public void testMethodAnnotatedWithThis() {
    SourceFile source = new SourceFile("Test.js");
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    CheckGlobalThis check = new CheckGlobalThis(compiler, WARNING);

    String code = "/** @this */ function Foo() { this.foo = 42; }";
    ParseTree tree = source.parse(code, null, null);

    Node root = tree.getRootNode();
    NodeTraversal traversal = new NodeTraversal(root, check);
    traversal.traverseAllChildren();

    assertEquals(0, check.getNumDiagnostics());
  }

  @Test
  public void testFunctionAnnotatedWithConstructor() {
    SourceFile source = new SourceFile("Test.js");   
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    CheckGlobalThis check = new CheckGlobalThis(compiler, WARNING);

    String code = "/** @constructor */ function Foo() { this.foo = 42; }";
    ParseTree tree = source.parse(code, null, null);

    Node root = tree.getRootNode();
    NodeTraversal traversal = new NodeTraversal(root, check);
    traversal.traverseAllChildren();

    assertEquals(0, check.getNumDiagnostics());
  }

  @Test
  public void testPrototypeProperty() {
    SourceFile source = new SourceFile("Test.js");
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    CheckGlobalThis check = new CheckGlobalThis(compiler, WARNING);

    String code = "function Foo() {} \n" +
                  "Foo.prototype.bar = function() { this.baz = 42; }";
    ParseTree tree = source.parse(code, null, null);

    Node root = tree.getRootNode();
    NodeTraversal traversal = new NodeTraversal(root, check);
    traversal.traverseAllChildren();

    assertEquals(0, check.getNumDiagnostics());
  }

  @Test
  public void testPrototypeMethod() {
    SourceFile source = new SourceFile("Test.js");
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    CheckGlobalThis check = new CheckGlobalThis(compiler, WARNING);

    String code = "function Foo() {} \n" +
                  "Foo.prototype.bar = function() { this.baz = 42; }";
    ParseTree tree = source.parse(code, null, null);

    Node root = tree.getRootNode();
    NodeTraversal traversal = new NodeTraversal(root, check);
    traversal.traverseAllChildren();

    assertEquals(0, check.getNumDiagnostics());
  }

  @Test
  public void testFunctionWithNoThisAnnotation() {
    SourceFile source = new SourceFile("Test.js");
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    CheckGlobalThis check = new CheckGlobalThis(compiler, WARNING);

    String code = "function Foo() { this.foo = 42; }";
    ParseTree tree = source.parse(code, null, null);

    Node root = tree.getRootNode();
    NodeTraversal traversal = new NodeTraversal(root, check);
    traversal.traverseAllChildren();

    assertEquals(1, check.getNumDiagnostics());
    assertEquals("Test.js:1:7: JSC_USED_GLOBAL_THIS", check.getDiagnostics()[0].getMessage());
  }
}