package com.google.javascript.jscomp.tests;

import static com.google.javascript.jscomp.CheckLevel.WARNING;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.JSTestCase;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParserTestCase;
import com.google.javascript.rhino.Node;

/**
 * Tests for {@link CheckGlobalThis}.
 */
public class CheckGlobalThisTest extends JSTestCase {

  public void testCheckGlobalThis() throws Exception {
    AbstractCompiler compiler = newCompiler();
    CompilerOptions options = new CompilerOptions(false, null, null, null);
    options.setWarningLevel(CheckLevel.WARNING, DiagnosticType.JSC_USED_GLOBAL_THIS);
    compiler.setOptions(options);

    String sourceCode =
        "function evil() {\n" +
        "  this.useful = undefined;\n" +
        "}\n" +
        "\n" +
        "function safe() {\n" +
        "  this.useful = undefined;\n" +
        "}\n" +
        "\n" +
        "function safeProto() {\n" +
        "  this.useful = undefined;\n" +
        "}\n" +
        "\n" +
        "function safeConstructor() {\n" +
        "  this.useful = undefined;\n" +
        "}\n" +
        "\n" +
        "function safePrototypeConstructor() {\n" +
        "  this.useful = undefined;\n" +
        "}\n" +
        "\n" +
        "function unsafeWithPrototypeAccess() {\n" +
        "  var a = {\n" +
        "    prototype: {\n" +
        "      unsafe: function() {\n" +
        "        this.useful = undefined;\n" +
        "      }\n" +
        "    }\n" +
        "  };\n" +
        "  a.prototype.unsafe.useful = undefined;\n" +
        "}\n" +
        "\n" +
        "function unsafeWithQualifiedName() {\n" +
        "  var a = {\n" +
        "    x: {\n" +
        "      prototype: {\n" +
        "        unsafe: function() {\n" +
        "          this.useful = undefined;\n" +
        "        }\n" +
        "      }\n" +
        "    }\n" +
        "  };\n" +
        "  a.x.prototype.unsafe.useful = undefined;\n" +
        "}\n";

    Node ast = parseAndCompile(compiler, sourceCode);

    NodeTraversal traversal = new NodeTraversal(ast, null);
    new CheckGlobalThis(compiler, WARNING).traverse(traversal, ast, null);

    ImmutableList<Node> diagnostics = traversal.getDiagnostics();

    // Test that no warnings are emitted for safe code.
    assertDiagnosticsEmpty(diagnostics);

    // Test that a warning is emitted for unsafe code.
    sourceCode = "function evil() { this.useful = undefined; }";
    ast = parseAndCompile(compiler, sourceCode);
    traversal = new NodeTraversal(ast, null);
    new CheckGlobalThis(compiler, WARNING).traverse(traversal, ast, null);
    assertEquals(1, diagnostics.size());
    assertTrue(diagnostics.get(0).getMessage().contains("dangerous use of the global 'this' object"));

    // Test that a warning is emitted for property accesses on the prototype.
    sourceCode = "var a = {\n" +
            "  prototype: {\n" +
            "    unsafe: function() {\n" +
            "      this.useful = undefined;\n" +
            "    }\n" +
            "  }\n" +
            "};\n" +
            "a.prototype.unsafe.useful = undefined;";
    ast = parseAndCompile(compiler, sourceCode);
    traversal = new NodeTraversal(ast, null);
    new CheckGlobalThis(compiler, WARNING).traverse(traversal, ast, null);
    assertEquals(1, diagnostics.size());
    assertTrue(diagnostics.get(0).getMessage().contains("dangerous use of the global 'this' object"));

    // Test that a warning is emitted for qualified name accesses on the prototype.
    sourceCode = "var a = {\n" +
            "  x: {\n" +
            "    prototype: {\n" +
            "      unsafe: function() {\n" +
            "        this.useful = undefined;\n" +
            "      }\n" +
            "    }\n" +
            "  };\n" +
            "  a.x.prototype.unsafe.useful = undefined;";
    ast = parseAndCompile(compiler, sourceCode);
    traversal = new NodeTraversal(ast, null);
    new CheckGlobalThis(compiler, WARNING).traverse(traversal, ast, null);
    assertEquals(1, diagnostics.size());
    assertTrue(diagnostics.get(0).getMessage().contains("dangerous use of the global 'this' object"));
  }

  @Override
  protected ParserTestCase getParserTestCases() {
    return ParserTestCase.EMPTY;
  }
}