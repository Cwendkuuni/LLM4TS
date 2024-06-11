import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenameLabels.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenameLabels;
import com.google.javascript.jscomp.test.TestCompiler;
import com.google.javascript.rhino.Token;

import java.util.HashMap;
import java.util.Map;

public class RenameLabelsTest {

  private static final String TEST_CODE =
      "function foo() {\n"
          + "  a: {\n"
          + "    b: break a;\n"
          + "  }\n"
          + "  c: {\n"
          + "    d: break c;\n"
          + "    e: break a;\n"
          + "  }\n"
          + "  f: {\n"
          + "    g: break d;\n"
          + "    h: continue d;\n"
          + "  }\n"
          + "}\n";

  @Test
  public void testRenameLabels() {
    TestCompiler compiler = new TestCompiler();
    Compiler passCompiler = new Compiler();
    RenameLabels pass = new RenameLabels(passCompiler);

    Node root = compiler.parse(TEST_CODE, null);
    pass.process(compiler.externs(), root);

    NodeTraversal nodeTraversal = new NodeTraversal(root);
    new ProcessLabels().traverse(nodeTraversal, root);

    String expectedCode =
        "function foo() {\n"
            + "  a: {\n"
            + "    b: break a;\n"
            + "  }\n"
            + "  c: {\n"
            + "    d: break c;\n"
            + "  }\n"
            + "  a: {\n"
            + "    e: continue d;\n"
            + "  }\n"
            + "  f: {\n"
            + "    g: break d;\n"
            + "  }\n"
            + "}\n";

    String actualCode = NodePrinter.printToSource(root);
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testRemoveUnusedLabels() {
    TestCompiler compiler = new TestCompiler();
    Compiler passCompiler = new Compiler();
    RenameLabels pass = new RenameLabels(passCompiler, new NoNameSupplier(), false);

    Node root = compiler.parse(TEST_CODE, null);
    pass.process(compiler.externs(), root);

    NodeTraversal nodeTraversal = new NodeTraversal(root);
    new ProcessLabels().traverse(nodeTraversal, root);

    String expectedCode =
        "function foo() {\n"
            + "  c: {\n"
            + "    d: break c;\n"
            + "  }\n"
            + "  a: {\n"
            + "    e: continue d;\n"
            + "  }\n"
            + "  f: {\n"
            + "    g: break d;\n"
            + "  }\n"
            + "}\n";

    String actualCode = NodePrinter.printToSource(root);
    assertEquals(expectedCode, actualCode);
  }

  @Test(expected = AssertionError.class)
  public void testLabelNameCollision() {
    TestCompiler compiler = new TestCompiler();
    Compiler passCompiler = new Compiler();
    RenameLabels pass = new RenameLabels(passCompiler);

    String testCode =
        "function foo() {\n"
            + "  a: {\n"
            + "    var a = 1;\n"
            + "  }\n"
            + "}";

    Node root = compiler.parse(testCode, null);
    pass.process(compiler.externs(), root);
  }

  @Test(expected = AssertionError.class)
  public void testKeywordLabel() {
    TestCompiler compiler = new TestCompiler();
    Compiler passCompiler = new Compiler();
    RenameLabels pass = new RenameLabels(passCompiler);

    String testCode =
        "function foo() {\n"
            + "  if (true) {\n"
            + "    break if;\n"
            + "  }\n"
            + "}";

    Node root = compiler.parse(testCode, null);
    pass.process(compiler.externs(), root);
  }

  @Test(expected = AssertionError.class)
  public void testLabelNestingDepthLimit() {
    TestCompiler compiler = new TestCompiler();
    Compiler passCompiler = new Compiler();
    RenameLabels pass = new RenameLabels(passCompiler);

    String testCode = "";
    for (int i = 0; i < 1000; ++i) {
      testCode += "function foo" + i + "() {\n";
      testCode += "  a: {\n";
      testCode += "    b: break a;\n";
      testCode += "  }\n";
      testCode += "}";
      testCode += "\n";
    }

    Node root = compiler.parse(testCode, null);
    pass.process(compiler.externs(), root);
  }

  @Test
  public void testLabelDepthLimit() {
    TestCompiler compiler = new TestCompiler();
    Compiler passCompiler = new Compiler();
    RenameLabels pass = new RenameLabels(passCompiler);

    String testCode = "";
    for (int i = 0; i < 10; ++i) {
      testCode += "function foo" + i + "() {\n";
      testCode += "  a: {\n";
      testCode += "    b: break a;\n";
      testCode += "  }\n";
      testCode += "}";
      testCode += "\n";
    }

    Node root = compiler.parse(testCode, null);
    pass.process(compiler.externs(), root);

    String expectedCode = "";
    for (int i = 0; i < 10; ++i) {
      expectedCode += "function foo" + i + "() {\n";
      expectedCode += "  a" + i + ": {\n";
      expectedCode += "    b" + i + ": break a" + i + ";\n";
      expectedCode += "  }\n";
      expectedCode += "}";
      expectedCode += "\n";
    }

    String actualCode = NodePrinter.printToSource(root);
    assertEquals(expectedCode, actualCode);
  }

  /** A dummy name supplier that doesn't return any names. */
  private static class NoNameSupplier implements Supplier<String> {
    @Override
    public String get() {
      throw new UnsupportedOperationException();
    }
  }

}