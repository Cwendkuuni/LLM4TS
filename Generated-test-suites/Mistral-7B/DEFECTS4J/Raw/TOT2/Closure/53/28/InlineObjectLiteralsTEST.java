import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.InlineObjectLiterals;
import com.google.javascript.rhino.Node;

import java.util.Map;

import static com.google.common.truth.Truth.assertThat;

public class InlineObjectLiteralsTest {

  private static final Compiler COMPILER = new Compiler();
  private static final CompilerPass PASS = new InlineObjectLiterals(COMPILER, () -> "");

  @org.junit.Test
  public void testInlineObjectLiteral() {
    String input = "var obj = {a: 1, b: 2};";
    Node externs = Node.newText("function main() {}");
    Node root = Node.newText(input);

    PASS.process(externs, root);

    Node expected = Node.newText("var a = 1;\n" +
        "var b = 2;\n" +
        "var obj = {\n" +
        "  a: a,\n" +
        "  b: b\n" +
        "};\n" +
        "function main() {}\n");

    assertThat(COMPILER.getTreeToString(root)).isEqualTo(expected.toString());
  }

  @org.junit.Test
  public void testInlineObjectLiteralWithFunction() {
    String input = "var obj = {a: 1, fn: function() {}};";
    Node externs = Node.newText("function main() {}");
    Node root = Node.newText(input);

    PASS.process(externs, root);

    Node expected = Node.newText("var a = 1;\n" +
        "var fn = function() {}\n" +
        "var obj = {\n" +
        "  a: a,\n" +
        "  fn: fn\n" +
        "};\n" +
        "function main() {}\n");

    assertThat(COMPILER.getTreeToString(root)).isEqualTo(expected.toString());
  }

  @org.junit.Test
  public void testInlineObjectLiteralWithCycle() {
    String input = "var obj1 = {a: obj2};\n" +
        "var obj2 = {a: obj1};";
    Node externs = Node.newText("function main() {}");
    Node root = Node.newText(input);

    // This test should fail due to a cycle in object literals

    // Adding try-catch block to test the error scenario
    try {
      PASS.process(externs, root);
      fail("Expected an error due to a cyclic object literal");
    } catch (RuntimeException e) {
      // excpected
    }
  }

  @org.junit.Test
  public void testInlineObjectLiteralWithExportedVariable() {
    String input = "export var obj = {a: 1};";
    Node externs = Node.newText("export var obj;");
    Node root = Node.newText(input);

    // This test should fail as the pass does not inline exported variables

    try {
      PASS.process(externs, root);
      fail("Expected an error as exported variables should not be inlined");
    } catch (RuntimeException e) {
      // expected
    }
  }

  @org.junit.Test
  public void testInlineObjectLiteralWithGlobalVariable() {
    String input = "var obj = {a: window.x};";
    Node externs = Node.newText("var x = 1;");
    Node root = Node.newText(input);

    PASS.process(externs, root);

    Node expected = Node.newText("var a = window.x;\n" +
        "var obj = {\n" +
        "  a: a\n" +
        "};\n");

    assertThat(COMPILER.getTreeToString(root)).isEqualTo(expected.toString());
  }

  @org.junit.Test
  public void testInlineObjectLiteralWithSelfReference() {
    String input = "var obj = {a: function() { return this.a; }};";
    Node externs = Node.newText("function main() {}");
    Node root = Node.newText(input);

    // This test should fail due to self-reference in object literal

    try {
      PASS.process(externs, root);
      fail("Expected an error due to self-reference in object literal");
    } catch (RuntimeException e) {
      // expected
    }
  }

  @org.junit.Test
  public void testInlineObjectLiteralWithNestedObject() {
    String input = "var obj = {a: {b: 1}};";
    Node externs = Node.newText("function main() {}");
    Node root = Node.newText(input);

    PASS.process(externs, root);

    Node expected = Node.newText("var a = {\n" +
        "  b: 1\n" +
        "};\n" +
        "var obj = {\n" +
        "  a: a\n" +
        "};\n");

    assertThat(COMPILER.getTreeToString(root)).isEqualTo(expected.toString());
  }

  @org.junit.Test
  public void testInlineObjectLiteralWithEmptyObject() {
    String input = "var obj = {};";
    Node externs = Node.newText("function main() {}");
    Node root = Node.newText(input);

    PASS.process(externs, root);

    // Empty object should not be inlined
    Node expected = Node.newText("var obj = {};\n");

    assertThat(COMPILER.getTreeToString(root)).isEqualTo(expected.toString());
  }

  @org.junit.Test
  public void testInlineObjectLiteralWithArray() {
    String input = "var obj = {a: [1, 2]};";
    Node externs = Node.newText("function main() {}");
    Node root = Node.newText(input);

    // This test should fail as the pass does not inline arrays

    try {
      PASS.process(externs, root);
      fail("Expected an error as arrays should not be inlined");
    } catch (RuntimeException e) {
      // expected
    }
  }

  @org.junit.Test
  public void testInlineObjectLiteralWithComplexInput() {
    String input = "var obj = {\n" +
        "  a: {b: 1, c: {d: [1, 2], e: [3, 4]}}\n" +
        "};\n";
    Node externs = Node.newText("function main() {}");
    Node root = Node.newText(input);

    PASS.process(externs, root);

    Node expected = Node.newText("var a = {\n" +
        "  b: 1,\n" +
        "  c: {\n" +
        "    d: [1, 2],\n" +
        "    e: [3, 4]\n" +
        "  }\n" +
        "};\n" +
        "var obj = {\n" +
        "  a: a\n" +
        "};\n");

    assertThat(COMPILER.getTreeToString(root)).isEqualTo(expected.toString());
  }

  @org.junit.Test
  public void testInlineObjectLiteralWithNestedFunctions() {
    String input = "var obj = {\n" +
        "  a: function() {\n" +
        "    return function() {\n" +
        "      return 1;\n" +
        "    }();\n" +
        "  },\n" +
        "  b: function() {\n" +
        "    return 1;\n" +
        "  }\n" +
        "};\n";
    Node externs = Node.newText("function main() {}");
    Node root = Node.newText(input);

    PASS.process(externs, root);

    Node expected = Node.newText("var a = function() {\n" +
        "  return (function() {\n" +
        "      return 1;\n" +
        "    }());\n" +
        "};\n" +
        "var b = function() {\n" +
        "  return 1;\n" +
        "};\n" +
        "var obj = {\n" +
        "  a: a,\n" +
        "  b: b\n" +
        "};\n");

    assertThat(COMPILER.getTreeToString(root)).isEqualTo(expected.toString());
  }

  @org.junit.Test
  public void testInlineObjectLiteralWithInlineFunction() {
    String input = "var obj = {\n" +
        "  a: function() {\n" +
        "    return function() {\n" +
        "      return this.a;\n" +
        "    }.bind(this);\n" +
        "  },\n" +
        "  b: 1\n" +
        "};\n";
    Node externs = Node.newText("function main() {}");
    Node root = Node.newText(input);

    // This test should fail as the bind() method does not work with inline functions

    try {
      PASS.process(externs, root);
      fail("Expected an error as bind() method does not work with inline functions");
    } catch (RuntimeException e) {
      // expected
    }
  }

  @org.junit.Test
  public void testInlineObjectLiteralWithExportedFunction() {
    String input = "export function main() {\n" +
        "  var obj = {\n" +
        "    a: function() {\n" +
        "      return 1;\n" +
        "    },\n" +
        "    b: 1\n" +
        "  };\n" +
        "}\n";
    Node externs = Node.newText(input);
    Node root = Node.newText("");

    PASS.process(externs, root);

    Node expected = Node.newText("export function main() {\n" +
        "  var a = function() {\n" +
        "      return 1;\n" +
        "  };\n" +
        "  var obj = {\n" +
        "    a: a,\n" +
        "    b: 1\n" +
        "  };\n" +
        "}\n");

    assertThat(COMPILER.getTreeToString(root)).isEqualTo(expected.toString());
  }
}