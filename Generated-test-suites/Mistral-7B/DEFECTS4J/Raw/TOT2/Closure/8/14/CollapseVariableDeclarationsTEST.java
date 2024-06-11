import com.google.javascript.jscomp.CollapseVariableDeclarations;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;

@RunWith(JUnit4.class)
public class CollapseVariableDeclarationsTest {

  @Test
  public void testCollapseVariableDeclarations() {
    Compiler compiler = new Compiler();
    CompilerPass collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);

    String sourceCode = "function test() {\n" +
            "  var a;\n" +
            "  var b = 1;\n" +
            "  var c = 2;\n" +
            "  var d = 3;\n" +
            "  var e = f;\n" +
            "  var f = 4;\n" +
            "  var g = h;\n" +
            "  var h = 5;\n" +
            "  var i = j;\n" +
            "  var j = 6;\n" +
            "  var k = l;\n" +
            "  var l = m;\n" +
            "  var m = 7;\n" +
            "  var n = o;\n" +
            "  var o = 8;\n" +
            "  var p = q;\n" +
            "  var q = r;\n" +
            "  var r = 9;\n" +
            "  var s = t;\n" +
            "  var t = u;\n" +
            "  var u = 10;\n" +
            "  var v = w;\n" +
            "  var w = x;\n" +
            "  var x = 11;\n" +
            "  var y = z;\n" +
            "  var z = 12;\n" +
            "}";

    Node root = SourceFile.getOrCreate(sourceCode);
    collapseVariableDeclarations.process(null, root);

    String expectedCode = "function test() {\n" +
            "  var a, b = 1, c = 2, d = 3, e, f = 4, g = 5, h = 5, i = 6, j = 6, k = 7,\n" +
            "      l = 7, m = 7, n = 8, o = 8, p, q = 9, r = 9, s, t = 10, u = 10, v = 11,\n" +
            "      w = 11, x = 11, y, z = 12;\n" +
            "}";

    String actualCode = compiler.getSource().getCode();
    assertThat(expectedCode).isEqualTo(actualCode);
  }

  @Test
  public void testCollapseSingleDeclaration() {
    Compiler compiler = new Compiler();
    CompilerPass collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);

    String sourceCode = "function test() {\n" +
            "  var a;\n" +
            "  var a = 1;\n" +
            "}";

    Node root = SourceFile.getOrCreate(sourceCode);
    collapseVariableDeclarations.process(null, root);

    String expectedCode = "function test() {\n" +
            "  var a = 1;\n" +
            "}";

    String actualCode = compiler.getSource().getCode();
    assertThat(expectedCode).isEqualTo(actualCode);
  }

  @Test
  public void testCollapseMultipleAssignments() {
    Compiler compiler = new Compiler();
    CompilerPass collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);

    String sourceCode = "function test() {\n" +
            "  var a = 1;\n" +
            "  var b = 1,\n" +
            "      b = 2;\n" +
            "  var c = 1,\n" +
            "      c = 2,\n" +
            "      c = 3;\n" +
            "}";

    Node root = SourceFile.getOrCreate(sourceCode);
    collapseVariableDeclarations.process(null, root);

    String expectedCode = "function test() {\n" +
            "  var a = 1,\n" +
            "      a = 1;\n" +
            "  var b = 1,\n" +
            "      b = 1,\n" +
            "      b = 2,\n" +
            "      b = 2;\n" +
            "  var c = 1,\n" +
            "      c = 1,\n" +
            "      c = 1,\n" +
            "      c = 2,\n" +
            "      c = 2,\n" +
            "      c = 3,\n" +
            "      c = 3;\n" +
            "}";

    String actualCode = compiler.getSource().getCode();
    assertThat(expectedCode).isEqualTo(actualCode);
  }

  @Test(expected = AssertionError.class)
  public void testCollapseNonVariableDeclaration() {
    Compiler compiler = new Compiler();
    CompilerPass collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);

    String sourceCode = "function test() {\n" +
            "  var a = 1;\n" +
            "  a = 2;\n" +
            "}";

    Node root = SourceFile.getOrCreate(sourceCode);
    collapseVariableDeclarations.process(null, root);
  }

  @Test
  public void testCollapseFunctionDeclarations() {
    Compiler compiler = new Compiler();
    CompilerPass collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);

    String sourceCode = "function test() {\n" +
            "  function a() {} \n" +
            "  function b() {} \n" +
            "  function c() {} \n" +
            "}";

    Node root = SourceFile.getOrCreate(sourceCode);
    collapseVariableDeclarations.process(null, root);

    String expectedCode = "function test() {\n" +
            "  function a(), b(), c() {} \n" +
            "}";

    String actualCode = compiler.getSource().getCode();
    assertThat(expectedCode).isEqualTo(actualCode);
  }

  @Test
  public void testCollapseVariableDeclarationsInsideLoop() {
    Compiler compiler = new Compiler();
    CompilerPass collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);

    String sourceCode = "function test() {\n" +
            "  for (var i = 0; i < 10; i++) {\n" +
            "    var x = i;\n" +
            "    var y = i;\n" +
            "  } \n" +
            "}";

    Node root = SourceFile.getOrCreate(sourceCode);
    collapseVariableDeclarations.process(null, root);

    String expectedCode = "function test() {\n" +
            "  for (var i = 0, x = i, y = i; i < 10; i++) {\n" +
            "  } \n" +
            "}";

    String actualCode = compiler.getSource().getCode();
    assertThat(expectedCode).isEqualTo(actualCode);
  }

  @Test
  public void testCollapseVariableDeclarationsWithIfStatement() {
    Compiler compiler = new Compiler();
    CompilerPass collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);

    String sourceCode = "function test() {\n" +
            "  var a = 1;\n" +
            "  if (true) {\n" +
            "    var a = 2;\n" +
            "  } \n" +
            "}";

    Node root = SourceFile.getOrCreate(sourceCode);
    collapseVariableDeclarations.process(null, root);

    String expectedCode = "function test() {\n" +
            "  var a = 1;\n" +
            "  if (true) {\n" +
            "    var a = 2;\n" +
            "  } \n" +
            "  var a = 1;\n" +
            "}";

    String actualCode = compiler.getSource().getCode();
    assertThat(expectedCode).isEqualTo(actualCode);
  }

  @Test
  public void testCollapseVariableDeclarationsInsideFunction() {
    Compiler compiler = new Compiler();
    CompilerPass collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);

    String sourceCode = "function test() {\n" +
            "  function innerFunction() {\n" +
            "    var a = 1;\n" +
            "    var b = 1;\n" +
            "  }\n" +
            "}";

    Node root = SourceFile.getOrCreate(sourceCode);
    collapseVariableDeclarations.process(null, root);

    String expectedCode = "function test() {\n" +
            "  function innerFunction() {\n" +
            "    var a = 1, b = 1;\n" +
            "  }\n" +
            "}";

    String actualCode = compiler.getSource().getCode();
    assertThat(expectedCode).isEqualTo(actualCode);
  }
}