package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.CompilationTestHelper;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Symbol;
import org.junit.Test;

public class CrossModuleMethodMotionTest {

  @Test
  public void testMoveMethods() throws Exception {
    Compiler compiler = new Compiler();
    CompilerPass pass = new CrossModuleMethodMotion(compiler, new CrossModuleMethodMotion.IdGenerator(), true);

    String sourceCode =
        "/**\n" +
        " * @constructor\n" +
        " */\n" +
        "function Foo() {\n" +
        "}\n" +
        "\n" +
        "Foo.prototype.bar = function() {\n" +
        "  return \"Foo.bar() called.\";\n" +
        "};\n" +
        "\n" +
        "function A() {\n" +
        "}\n" +
        "\n" +
        "A.prototype.qux = function() {\n" +
        "  return this.bar();\n" +
        "};\n" +
        "\n" +
        "function B() {\n" +
        "}\n" +
        "\n" +
        "B.prototype.qux = function() {\n" +
        "  return \"B.qux() called.\";\n" +
        "};\n";

    Node root = CompilationTestHelper.compile(compiler, sourceCode).getRoot();
    assertFalse(root.isError());

    pass.process(null, root);
    compiler.process();

    Node expectedRoot = CompilationTestHelper.compile(compiler,
        "/**\n" +
        " * @constructor\n" +
        " */\n" +
        "function Foo() {\n" +
        "}\n" +
        "\n" +
        "Foo.prototype.bar = function JSCompiler_stubMethod(id) {\n" +
        "  return function() {\n" +
        "    return Foo.prototype.bar.apply(this, arguments);\n" +
        "  };\n" +
        "};\n" +
        "\n" +
        "function A() {\n" +
        "}\n" +
        "\n" +
        "A.prototype.qux = function() {\n" +
        "  return A.prototype.qux.call(this).apply(this, arguments);\n" +
        "};\n" +
        "\n" +
        "function B() {\n" +
        "  B.prototype.qux = JSCompiler_unstubMethod(0, function() {\n" +
        "    return \"B.qux() called.\";\n" +
        "  });\n" +
        "}\n").getRoot();

    Node actualRoot = compiler.getRootNode();
    assertTrue(actualRoot.isTopLevel());
    assertSame(expectedRoot, actualRoot);
  }

  @Test
  public void testMoveMethodsWithMissingDeepestCommonModule() {
    Compiler compiler = new Compiler();
    CompilerPass pass = new CrossModuleMethodMotion(compiler, new CrossModuleMethodMotion.IdGenerator(), true);

    String sourceCode =
        "/**\n" +
        " * @constructor\n" +
        " */\n" +
        "function Foo() {\n" +
        "}\n" +
        "\n" +
        "Foo.prototype.bar = function() {\n" +
        "  return \"Foo.bar() called.\";\n" +
        "};\n" +
        "\n" +
        "function A() {\n" +
        "}\n" +
        "\n" +
        "A.prototype.qux = function() {\n" +
        "  return this.bar();\n" +
        "};\n" +
        "\n" +
        "// Missing B module\n";

    Node root = CompilationTestHelper.compile(compiler, sourceCode).getRoot();
    assertFalse(root.isError());

    pass.process(null, root);
    compiler.process();

    Node actualRoot = compiler.getRootNode();
    assertTrue(actualRoot.isError());
    assertSame(DiagnosticType.error(JSError.make(CrossModuleMethodMotion.NULL_COMMON_MODULE_ERROR)), actualRoot.getFirstChild());
  }

  @Test
  public void testMoveMethodsWithMissingPrototype() {
    Compiler compiler = new Compiler();
    CompilerPass pass = new CrossModuleMethodMotion(compiler, new CrossModuleMethodMotion.IdGenerator(), true);

    String sourceCode =
        "/**\n" +
        " * @constructor\n" +
        " */\n" +
        "function Foo() {\n" +
        "}\n" +
        "\n" +
        "Foo.prototype.bar = function() {\n" +
        "  return \"Foo.bar() called.\";\n" +
        "};\n" +
        "\n" +
        "function A() {\n" +
        "}\n" +
        "\n" +
        "A.prototype.qux = function() {\n" +
        "  return this.baz(); // Missing prototype property\n" +
        "};\n";

    Node root = CompilationTestHelper.compile(compiler, sourceCode).getRoot();
    assertFalse(root.isError());

    pass.process(null, root);
    compiler.process();

    Node actualRoot = compiler.getRootNode();
    assertTrue(actualRoot.isError());
    assertNull(actualRoot.getFirstChild());
  }

  @Test
  public void testMoveMethodsWithUnreferencedNameInfo() {
    Compiler compiler = new Compiler();
    CompilerPass pass = new CrossModuleMethodMotion(compiler, new CrossModuleMethodMotion.IdGenerator(), true);

    String sourceCode =
        "/**\n" +
        " * @constructor\n" +
        " */\n" +
        "function Foo() {\n" +
        "}\n" +
        "\n" +
        "Foo.prototype.bar = function() {\n" +
        "  return \"Foo.bar() called.\";\n" +
        "};\n" +
        "\n" +
        "function A() {\n" +
        "}\n" +
        "\n" +
        "// Unreferenced name info\n" +
        "function X() {\n" +
        "}\n" +
        "\n" +
        "X.prototype.qux = function() {\n" +
        "  return this.bar();\n" +
        "};\n";

    Node root = CompilationTestHelper.compile(compiler, sourceCode).getRoot();
    assertFalse(root.isError());

    pass.process(null, root);
    compiler.process();

    Node actualRoot = compiler.getRootNode();
    assertTrue(actualRoot.isError());
    assertNull(actualRoot.getFirstChild());
  }

  @Test
  public void testMoveMethodsWithGetOrSet() {
    Compiler compiler = new Compiler();
    CompilerPass pass = new CrossModuleMethodMotion(compiler, new CrossModuleMethodMotion.IdGenerator(), true);

    String sourceCode =
        "/**\n" +
        " * @constructor\n" +
        " */\n" +
        "function Foo() {\n" +
        "}\n" +
        "\n" +
        "Object.defineProperty(Foo.prototype, \"bar\", {\n" +
        "  get: function() {\n" +
        "    return \"Foo.bar getter called.\";\n" +
        "  },\n" +
        "  set: function(value) {\n" +
        "    this.baz = value;\n" +
        "  }\n" +
        "});\n" +
        "\n" +
        "function A() {\n" +
        "}\n" +
        "\n" +
        "A.prototype.qux = function() {\n" +
        "  return this.bar;\n" +
        "};\n";

    Node root = CompilationTestHelper.compile(compiler, sourceCode).getRoot();
    assertFalse(root.isError());

    pass.process(null, root);
    compiler.process();

    Node actualRoot = compiler.getRootNode();
    assertTrue(actualRoot.isError());
    assertNull(actualRoot.getFirstChild());
  }

  @Test
  public void testMoveMethodsWithNullDeepestCommonModuleRef() {
    Compiler compiler = new Compiler();
    CompilerPass pass = new CrossModuleMethodMotion(compiler, new CrossModuleMethodMotion.IdGenerator(), true);

    String sourceCode =
        "/**\n" +
        " * @constructor\n" +
        " */\n" +
        "function Foo() {\n" +
        "}\n" +
        "\n" +
        "Foo.prototype.bar = function() {\n" +
        "  return \"Foo.bar() called.\";\n" +
        "};\n" +
        "\n" +
        "function A() {\n" +
        "}\n" +
        "\n" +
        "A.prototype.qux = function() {\n" +
        "  return null.bar(); // Null deepest common module reference\n" +
        "};\n";

    Node root = CompilationTestHelper.compile(compiler, sourceCode).getRoot();
    assertFalse(root.isError());

    pass.process(null, root);
    compiler.process();

    Node actualRoot = compiler.getRootNode();
    assertTrue(actualRoot.isError());
    assertSame(DiagnosticType.error(JSError.make(CrossModuleMethodMotion.NULL_COMMON_MODULE_ERROR)), actualRoot.getFirstChild());
  }
}