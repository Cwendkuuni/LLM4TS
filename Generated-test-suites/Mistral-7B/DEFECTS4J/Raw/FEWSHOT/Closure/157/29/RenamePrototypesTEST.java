package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerPassTestUtils;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.VariableMap;
import com.google.javascript.rhino.TokenStream;
import com.google.javascript.rhino.Token;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link RenamePrototypes} class.
 */
public class RenamePrototypesTest {

  private Compiler compiler;
  private CompilerPassManager passManager;
  private RenamePrototypes renamePrototypes;

  @Before
  public void setUp() throws Exception {
    compiler = new Compiler();
    passManager = new CompilerPassManager(compiler);
    renamePrototypes = new RenamePrototypes(compiler, false, null, null);
  }

  @Test
  public void testRenameProperties() throws Exception {
    String code = "function Foo() {}\n" +
                  "Foo.prototype.foo = 1;\n" +
                  "Foo.prototype.bar = 2;\n" +
                  "var x = new Foo();\n" +
                  "x.foo = 3;\n";

    TokenStream input = new TokenStream(code);
    Node externs = Node.root();
    Node root = Node.parse(input);

    passManager.addPass(CompilerPass.SOURCE_FILES, renamePrototypes);
    passManager.addPass(CompilerPass.EXterns, new ProcessExterns(compiler));
    passManager.runPasses();

    Node result = compiler.getOutput();

    Node expectedResult = Node.parse(
        "function Foo() {}\n" +
        "Foo.prototype.foo__ = 1;\n" +
        "Foo.prototype.bar__ = 2;\n" +
        "var x = new Foo();\n" +
        "x.foo__ = 3;\n");

    CompilerPassTestUtils.assertNodeEquals(expectedResult, result);

    Node x = Node.getById(result, "x");
    Node foo = Node.getById(x, "foo__");
    assertEquals(3, foo.getInt());
  }

  @Test
  public void testDontRenameExternedProperties() throws Exception {
    String code = "function Foo() {}\n" +
                  "Foo.prototype.foo = 1;\n" +
                  "foo = 2;\n"; // foo is defined externally

    TokenStream input = new TokenStream(code);
    Node externs = Node.root();
    Node root = Node.parse(input);

    passManager.addPass(CompilerPass.SOURCE_FILES, renamePrototypes);
    passManager.addPass(CompilerPass.EXterns, new ProcessExterns(compiler));
    passManager.runPasses();

    Node result = compiler.getOutput();

    Node expectedResult = Node.parse(
        "function Foo() {}\n" +
        "Foo.prototype.foo = 1;\n" +
        "foo = 2;\n");

    CompilerPassTestUtils.assertNodeEquals(expectedResult, result);

    Node foo = Node.getById(result, "foo");
    assertEquals(2, foo.getInt());
  }

  @Test
  public void testDontRenamePrivateProperties() throws Exception {
    String code = "function Foo() {}\n" +
                  "Foo.prototype.__foo = 1;\n";

    TokenStream input = new TokenStream(code);
    Node externs = Node.root();
    Node root = Node.parse(input);

    passManager.addPass(CompilerPass.SOURCE_FILES, renamePrototypes);
    passManager.addPass(CompilerPass.EXterns, new ProcessExterns(compiler));
    passManager.runPasses();

    Node result = compiler.getOutput();

    Node expectedResult = Node.parse(
        "function Foo() {}\n" +
        "Foo.prototype.__foo = 1;\n");

    CompilerPassTestUtils.assertNodeEquals(expectedResult, result);

    Node foo = Node.getById(result, "__foo");
    assertEquals(1, foo.getInt());
  }

  @Test
  public void testDontRenameAggressivelyRenamedProperties() throws Exception {
    String code = "function Foo() {}\n" +
                  "Foo.prototype.fooUpper = 1;\n";

    TokenStream input = new TokenStream(code);
    Node externs = Node.root();
    Node root = Node.parse(input);

    passManager.addPass(CompilerPass.SOURCE_FILES, renamePrototypes);
    passManager.addPass(CompilerPass.EXterns, new ProcessExterns(compiler));
    passManager.runPasses();

    Node result = compiler.getOutput();

    Node expectedResult = Node.parse(
        "function Foo() {}\n" +
        "Foo.prototype.fooUpper__ = 1;\n");

    CompilerPassTestUtils.assertNodeEquals(expectedResult, result);

    Node fooUpper = Node.getById(result, "fooUpper__");
    assertEquals(1, fooUpper.getInt());
  }

  @Test
  public void testRenamePropertiesWithSameNameButDifferentContexts() throws Exception {
    String code = "function Foo() {}\n" +
                  "Foo.prototype.foo = 1;\n" +
                  "function Bar() {}\n" +
                  "Bar.prototype.foo = 2;\n" +
                  "var x = new Foo();\n" +
                  "x.foo = 3;\n" +
                  "var y = new Bar();\n" +
                  "y.foo = 4;\n";

    TokenStream input = new TokenStream(code);
    Node externs = Node.root();
    Node root = Node.parse(input);

    passManager.addPass(CompilerPass.SOURCE_FILES, renamePrototypes);
    passManager.addPass(CompilerPass.EXterns, new ProcessExterns(compiler));
    passManager.runPasses();

    Node result = compiler.getOutput();

    Node expectedResult = Node.parse(
        "function Foo() {}\n" +
        "Foo.prototype.foo__ = 1;\n" +
        "function Bar() {}\n" +
        "Bar.prototype.foo__ = 2;\n" +
        "var x = new Foo();\n" +
        "x.foo__ = 3;\n" +
        "var y = new Bar();\n" +
        "y.foo__ = 4;\n");

    CompilerPassTestUtils.assertNodeEquals(expectedResult, result);

    Node x = Node.getById(result, "x");
    Node foo = Node.getById(x, "foo__");
    assertEquals(3, foo.getInt());

    Node y = Node.getById(result, "y");
    Node fooY = Node.getById(y, "foo__");
    assertEquals(4, fooY.getInt());
  }

  @Test
  public void testRenamePrototypeByHeuristic() throws Exception {
    String code = "function Foo() {}\n" +
                  "Foo.prototype.fooBar = 1;\n";

    TokenStream input = new TokenStream(code);
    Node externs = Node.root();
    Node root = Node.parse(input);

    passManager.addPass(CompilerPass.SOURCE_FILES, renamePrototypes);
    passManager.addPass(CompilerPass.EXterns, new ProcessExterns(compiler));
    passManager.runPasses();

    Node result = compiler.getOutput();

    Node expectedResult = Node.parse(
        "function Foo() {}\n" +
        "Foo.prototype.fooBar__ = 1;\n");

    CompilerPassTestUtils.assertNodeEquals(expectedResult, result);

    Node fooBar = Node.getById(result, "fooBar__");
    assertEquals(1, fooBar.getInt());
  }

  @Test
  public void testDontRenamePrototypeByHeuristic() throws Exception {
    String code = "function Foo() {}\n" +
                  "Foo.prototype.toString = 1;\n";

    TokenStream input = new TokenStream(code);
    Node externs = Node.root();
    Node root = Node.parse(input);

    passManager.addPass(CompilerPass.SOURCE_FILES, renamePrototypes);
    passManager.addPass(CompilerPass.EXterns, new ProcessExterns(compiler));
    passManager.runPasses();

    Node result = compiler.getOutput();

    Node expectedResult = Node.parse(
        "function Foo() {}\n" +
        "Foo.prototype.toString = 1;\n");

    CompilerPassTestUtils.assertNodeEquals(expectedResult, result);

    Node toString = Node.getById(result, "toString");
    assertEquals(1, toString.getInt());
  }

  @Test
  public void testRenameObjLitPropertiesByHeuristic() throws Exception {
    String code = "var x = { fooBar: 1 };\n";

    TokenStream input = new TokenStream(code);
    Node externs = Node.root();
    Node root = Node.parse(input);

    passManager.addPass(CompilerPass.SOURCE_FILES, renamePrototypes);
    passManager.addPass(CompilerPass.EXterns, new ProcessExterns(compiler));
    passManager.runPasses();

    Node result = compiler.getOutput();

    Node expectedResult = Node.parse(
        "var x = { fooBar__: 1 };\n");

    CompilerPassTestUtils.assertNodeEquals(expectedResult, result);

    Node fooBar = Node.getById(result, "x.fooBar__");
    assertEquals(1, fooBar.getInt());
  }

  @Test
  public void testDontRenameObjLitPropertiesByHeuristic() throws Exception {
    String code = "var x = { toString: 1 };\n";

    TokenStream input = new TokenStream(code);
    Node externs = Node.root();
    Node root = Node.parse(input);

    passManager.addPass(CompilerPass.SOURCE_FILES, renamePrototypes);
    passManager.addPass(CompilerPass.EXterns, new ProcessExterns(compiler));
    passManager.runPasses();

    Node result = compiler.getOutput();

    Node expectedResult = Node.parse(
        "var x = { toString: 1 };\n");

    CompilerPassTestUtils.assertNodeEquals(expectedResult, result);

    Node toString = Node.getById(result, "toString");
    assertEquals(1, toString.getInt());
  }

  @Test
  public void testRenamePrototypeWithSameNameButDifferentContexts() throws Exception {
    String code = "function Foo() {}\n" +
                  "Foo.prototype.foo = 1;\n" +
                  "function Bar() {}\n" +
                  "Bar.prototype.foo = 2;\n" +
                  "var x = new Foo();\n" +
                  "x.foo = 3;\n" +
                  "var y = new Bar();\n" +
                  "y.foo = 4;\n";

    TokenStream input = new TokenStream(code);
    Node externs = Node.root();
    Node root = Node.parse(input);

    passManager.addPass(CompilerPass.SOURCE_FILES, renamePrototypes);
    passManager.addPass(CompilerPass.EXterns, new ProcessExterns(compiler));
    passManager.runPasses();

    Node result = compiler.getOutput();

    Node expectedResult = Node.parse(
        "function Foo() {}\n" +
        "Foo.prototype.foo__ = 1;\n" +
        "function Bar() {}\n" +
        "Bar.prototype.foo__ = 2;\n" +
        "var x = new Foo();\n" +
        "x.foo__ = 3;\n" +
        "var y = new Bar();\n" +
        "y.foo__ = 4;\n");

    CompilerPassTestUtils.assertNodeEquals(expectedResult, result);

    Node x = Node.getById(result, "x");
    Node foo = Node.getById(x, "foo__");
    assertEquals(3, foo.getInt());

    Node y = Node.getById(result, "y");
    Node fooY = Node.getById(y, "foo__");
    assertEquals(4, fooY.getInt());
  }

  /**
   * Base class for test cases that modify the input code.
   */
  private static class ProcessExterns extends CompilerPass {

    private final Compiler compiler;

    ProcessExterns(Compiler compiler) {
      this.compiler = compiler;
    }

    @Override
    public void process(Node externs, Node root) {
      Preconditions.checkNotNull(externs);
      Preconditions.checkNotNull(root);

      TokenStream input = compiler.getInput();
      TokenStream externsInput = new TokenStream(externs);

      // Merge the two input streams into one.
      TokenStream mergedInput = new TokenStream();
      mergedInput.write(input.read());
      mergedInput.write(externsInput.readAll());

      // Parse the merged input and replace the original input with it.
      Node mergedRoot = Node.parse(mergedInput);
      compiler.setInput(mergedRoot);

      // Reset the compiler state to avoid side effects.
      compiler.reset();

      super.process(externs, root);
    }
  }
}