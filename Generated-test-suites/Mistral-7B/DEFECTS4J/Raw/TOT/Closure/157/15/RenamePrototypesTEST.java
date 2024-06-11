import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenamePrototypes.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerResult;
import com.google.javascript.jscomp.CompilerResult.Error;
import com.google.javascript.jscomp.CompilerResult.Warning;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.TokenStream;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

public class RenamePrototypesTest {

  @org.junit.Test
  public void testSuccess() throws Exception {
    String input = "function Foo() {}\n" +
                   "Foo.prototype.bar = function() {}\n" +
                   "var x = new Foo();\n" +
                   "Foo.prototype.baz = 42;\n" +
                   "x.bar();";

    CompilerOptions options = new CompilerOptions();
    options.setOutputFile("out.js");

    Compiler compiler = new Compiler();
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);
    compiler.addPass(pass);

    Node root = createNodeFromString(input);
    TokenStream inputStream = new TokenStream(new StringBuilder(input));
    CompilerResult result = compiler.compile(inputStream, root, options);

    assertEquals(1, result.getWarnings().size());
    List<Warning> warnings = result.getWarnings();
    assertEquals("Renaming prototype property: 'baz'. Old name: 'baz'. New name: '_baz_'.",
                 warnings.get(0).getMessage());

    assertEquals(0, result.getErrors().size());

    Node expected = createNodeFromString("function Foo() {}\n" +
                                         "Foo.prototype._baz_ = 42;\n" +
                                         "Foo.prototype.bar = function() {}\n" +
                                         "var x = new Foo();\n" +
                                         "x.bar();");

    assertEqualsNodes(expected, result.getOutput());
  }

  @org.junit.Test
  public void testAgreesiveRenaming() throws Exception {
    String input = "function Foo() {}\n" +
                   "Foo.prototype.baz = function() {}\n" +
                   "var x = new Foo();\n" +
                   "Foo.prototype.bar = 42;\n" +
                   "x.baz();";

    CompilerOptions options = new CompilerOptions();
    options.setOutputFile("out.js");

    Compiler compiler = new Compiler();
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);
    compiler.addPass(pass);

    Node root = createNodeFromString(input);
    TokenStream inputStream = new TokenStream(new StringBuilder(input));
    CompilerResult result = compiler.compile(inputStream, root, options);

    assertEquals(0, result.getWarnings().size());
    List<Warning> warnings = result.getWarnings();

    assertEquals(0, result.getErrors().size());

    Node expected = createNodeFromString("function Foo() {}\n" +
                                         "Foo.prototype._baz__ = function() {}\n" +
                                         "var x = new Foo();\n" +
                                         "Foo.prototype._baz__ = 42;\n" +
                                         "x._baz__();");

    assertEqualsNodes(expected, result.getOutput());
  }

  @org.junit.Test
  public void testNoRenamingForPrivate() throws Exception {
    String input = "function Foo() {}\n" +
                   "var _fooProto = Foo.prototype;\n" +
                   "function _Foo() {}\n" +
                   "_Foo.prototype._bar = function() {}\n" +
                   "Foo.prototype._bar = _Foo.prototype._bar;\n" +
                   "var x = new Foo();\n" +
                   "x._bar();";

    CompilerOptions options = new CompilerOptions();
    options.setOutputFile("out.js");

    Compiler compiler = new Compiler();
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);
    compiler.addPass(pass);

    Node root = createNodeFromString(input);
    TokenStream inputStream = new TokenStream(new StringBuilder(input));
    CompilerResult result = compiler.compile(inputStream, root, options);

    assertEquals(0, result.getWarnings().size());
    List<Warning> warnings = result.getWarnings();

    assertEquals(0, result.getErrors().size());

    Node expected = createNodeFromString(input);
    assertEqualsNodes(expected, result.getOutput());
  }

  @org.junit.Test
  public void testNoRenamingForExported() throws Exception {
    String input = "function Foo() {}\n" +
                   "Foo.prototype.IndexOf = Array.prototype.indexOf;\n" +
                   "var x = new Foo();\n" +
                   "x.Indexof('');";

    CompilerOptions options = new CompilerOptions();
    options.setOutputFile("out.js");

    Compiler compiler = new Compiler();
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);
    compiler.addPass(pass);

    Node root = createNodeFromString(input);
    TokenStream inputStream = new TokenStream(new StringBuilder(input));
    CompilerResult result = compiler.compile(inputStream, root, options);

    assertEquals(0, result.getWarnings().size());
    List<Warning> warnings = result.getWarnings();

    assertEquals(0, result.getErrors().size());

    Node expected = createNodeFromString(input);
    assertEqualsNodes(expected, result.getOutput());
  }

  @org.junit.Test
  public void testErrorWhenRenamingConflicts() throws Exception {
    String input = "function Foo() {}\n" +
                   "Foo.prototype.bar = function() {}\n" +
                   "Foo.prototype.prototype = { getBar: function() {}};\n" +
                   "var x = new Foo();\n" +
                   "x.bar();";

    CompilerOptions options = new CompilerOptions();
    options.setOutputFile("out.js");

    Compiler compiler = new Compiler();
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);
    compiler.addPass(pass);

    Node root = createNodeFromString(input);
    TokenStream inputStream = new TokenStream(new StringBuilder(input));
    CompilerResult result = compiler.compile(inputStream, root, options);

    assertEquals(1, result.getErrors().size());
    List<Error> errors = result.getErrors();
    assertEquals("Error: Renaming conflict for property 'prototype'. Old name: 'prototype'. New name: '_prototype_'.",
                 errors.get(0).getMessage());

    assertEquals(0, result.getWarnings().size());
  }

  @org.junit.Test
  public void testErrorWhenRenamingBuiltIn() throws Exception {
    String input = "function Foo() {}\n" +
                   "Foo.prototype.toString = String.prototype.toString;\n" +
                   "var x = new Foo();\n" +
                   "x.toString();";

    CompilerOptions options = new CompilerOptions();
    options.setOutputFile("out.js");

    Compiler compiler = new Compiler();
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);
    compiler.addPass(pass);

    Node root = createNodeFromString(input);
    TokenStream inputStream = new TokenStream(new StringBuilder(input));
    CompilerResult result = compiler.compile(inputStream, root, options);

    assertEquals(1, result.getErrors().size());
    List<Error> errors = result.getErrors();
    assertEquals("Error: Renaming conflict for property 'toString'. Old name: 'toString'. New name: '_toString_'.",
                 errors.get(0).getMessage());

    assertEquals(0, result.getWarnings().size());
  }

  @org.junit.Test
  public void testErrorWhenRenamingNonString() throws Exception {
    String input = "function Foo() {}\n" +
                   "Foo.prototype.three = 3;\n" +
                   "var x = new Foo();";

    CompilerOptions options = new CompilerOptions();
    options.setOutputFile("out.js");

    Compiler compiler = new Compiler();
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);
    compiler.addPass(pass);

    Node root = createNodeFromString(input);
    TokenStream inputStream = new TokenStream(new StringBuilder(input));
    CompilerResult result = compiler.compile(inputStream, root, options);

    assertEquals(1, result.getErrors().size());
    List<Error> errors = result.getErrors();
    assertEquals("Error: Property 'three' is not a string.", errors.get(0).getMessage());

    assertEquals(0, result.getWarnings().size());
  }

  private Node createNodeFromString(String input) {
    NodeFactory factory = new NodeFactory();
    TokenStream inputStream = new TokenStream(new StringBuilder(input));
    return factory.buildParseTree(inputStream);
  }

  private void assertEqualsNodes(Node expected, Node actual) {
    PrintStream out = System.out;
    PrintStream err = System.err;
    System.setOut(new PrintStream(new StringBuilder()));
    System.setErr(new PrintStream(new StringBuilder()));

    NodeTraversal.traverse(new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n == null || expected == null) {
          return;
        }

        if (expected.getType() != n.getType()) {
          fail("Unexpected type: expected=" + expected.getType() + ", actual=" + n.getType());
          return;
        }

        if (expected.getFirstChild() != null && n.getFirstChild() != null) {
          assertEqualsNodes(expected.getFirstChild(), n.getFirstChild());
        }

        if (expected.getLastChild() != null && n.getLastChild() != null) {
          assertEqualsNodes(expected.getLastChild(), n.getLastChild());
        }
      }

      @Override
      public void postVisit(NodeTraversal t, Node n, Node parent) {
        if (n != null && expected != null) {
          String expectedString = NodePrinter.print(expected);
          String actualString = NodePrinter.print(n);
          fail("Unexpected node content: expected=" + expectedString + ", actual=" + actualString);
        }
      }
    }, actual, null);

    System.setOut(out);
    System.setErr(err);
  }
}