import static org.junit.Assert.*;
import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Supplier;
import com.google.javascript.rhino.Node;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class InlineObjectLiteralsTest {

  private static abstract class TestCompiler extends AbstractCompiler {
    private final TestInlineObjectLiterals testPass;

    TestCompiler() {
      testPass = new TestInlineObjectLiterals(this, () -> "");
    }

    TestInlineObjectLiterals getTestPass() {
      return testPass;
    }

    void testInlineObjectLiterals(String input, String expectedOutput) {
      Node root = parseJs(input);
      getTestPass().process(null, root);
      String output = printPretty(root);
      assertEquals(expectedOutput, output);
    }
  }

  public static class TestInlineObjectLiterals extends InlineObjectLiterals {
    TestInlineObjectLiterals(AbstractCompiler compiler, Supplier<String> nameIdSupplier) {
      super(compiler, nameIdSupplier);
    }

    @Override
    private class InliningBehavior extends InliningBehavior {
      /**
       * A list of variables that should not be inlined, because their
       * reference information is out of sync with the state of the AST.
       */
      @Override
      Set<Var> staleVars = Sets.newLinkedHashSet();

      @Override
      public void afterExitScope(NodeTraversal t, ReferenceMap referenceMap) {
        super.afterExitScope(t, referenceMap);
        for (Var v : staleVars) {
          fail("Variable " + v.name + " was inlined, but should not have been.");
        }
      }
    }
  }

  @Test
  public void testSimpleInlineObject() throws Exception {
    TestCompiler compiler = new TestCompiler();
    compiler.testInlineObjectLiterals(
        "var x = {a: 1};",
        "var a_1_;\n" +
        "var x = a_1_;\n" +
        "a_1_ = {\n" +
        "  a: 1\n" +
        "};");
  }

  @Test
  public void testInlineObjectWithMultipleSameProperty() throws Exception {
    TestCompiler compiler = new TestCompiler();
    compiler.testInlineObjectLiterals(
        "var x = {a: 1, a: 2};",
        "var a_1_;\n" +
        "var a_2_;\n" +
        "var x = {\n" +
        "  a: a_1_,\n" +
        "  a: a_2_,\n" +
        "};\n" +
        "a_1_ = 1;\n" +
        "a_2_ = 2;");
  }

  @Test
  public void testInlineObjectWithMultipleDifferentProperties() throws Exception {
    TestCompiler compiler = new TestCompiler();
    compiler.testInlineObjectLiterals(
        "var x = {a: 1, b: 2};",
        "var a_1_;\n" +
        "var b_2_;\n" +
        "var x = {\n" +
        "  a: a_1_,\n" +
        "  b: b_2_,\n" +
        "};\n" +
        "a_1_ = 1;\n" +
        "b_2_ = 2;");
  }

  @Test
  public void testInlineObjectWithSinglePropertyOnLeftHS() throws Exception {
    TestCompiler compiler = new TestCompiler();
    compiler.testInlineObjectLiterals(
        "var x = {a: {b: 1}}; var y = x.a;",
        "var a_1_;\n" +
        "var b_1_;\n" +
        "var x = {\n" +
        "  a: {\n" +
        "    b: a_1_,\n" +
        "  }\n" +
        "};\n" +
        "a_1_ = {\n" +
        "  b: b_1_,\n" +
        "};\n" +
        "b_1_ = 1;\n" +
        "var y = a_1_.b;");
  }

  @Test
  public void testInlineObjectWithSinglePropertyOnRightHS() throws Exception {
    TestCompiler compiler = new TestCompiler();
    compiler.testInlineObjectLiterals(
        "var x = {a: 1}; var y = {a: x};",
        "var a_1_;\n" +
        "var x = a_1_;\n" +
        "a_1_ = {\n" +
        "  a: 1\n" +
        "};\n" +
        "var y = {\n" +
        "  a: x\n" +
        "};");
  }

  @Test
  public void testInlineObjectWithEmptyObject() throws Exception {
    TestCompiler compiler = new TestCompiler();
    compiler.testInlineObjectLiterals(
        "var x = {};",
        "var x = {};");
  }

  @Test
  public void testInlineObjectWithArrayProperty() throws Exception {
    TestCompiler compiler = new TestCompiler();
    compiler.testInlineObjectLiterals(
        "var x = {a: [1, 2]};",
        "var a_1_;\n" +
        "var x = {\n" +
        "  a: a_1_,\n" +
        "};\n" +
        "a_1_ = [1, 2];");
  }

  @Test
  public void testInlineObjectWithNestedObject() throws Exception {
    TestCompiler compiler = new TestCompiler();
    compiler.testInlineObjectLiterals(
        "var x = {a: {b: 1}};",
        "var b_1_;\n" +
        "var a_1_;\n" +
        "var x = {\n" +
        "  a: {\n" +
        "    b: b_1_,\n" +
        "  }\n" +
        "};\n" +
        "b_1_ = 1;\n");
  }

  @Test
  public void testInlineObjectWithSelfReference() throws Exception {
    TestCompiler compiler = new TestCompiler();
    compiler.testInlineObjectLiterals(
        "var x = {a: {b: x}};",
        "fail('InlineObjectLiterals cannot inline self-referential objects.');");
  }

  @Test
  public void testInlineObjectWithMultipleReferencesToSameObject() throws Exception {
    TestCompiler compiler = new TestCompiler();
    compiler.testInlineObjectLiterals(
        "var x = {a: 1}; var y = x; var z = x; var a = x;",
        "fail('InlineObjectLiterals cannot inline objects that are referenced multiple times.');");
  }

  @Test
  public void testInlineObjectWithFunctionReference() throws Exception {
    TestCompiler compiler = new TestCompiler();
    compiler.testInlineObjectLiterals(
        "function f() { var x = {a: f}; }",
        "fail('InlineObjectLiterals cannot inline objects with function references.');");
  }

  @Test
  public void testInlineObjectWithPropertyReference() throws Exception {
    TestCompiler compiler = new TestCompiler();
    compiler.testInlineObjectLiterals(
        "var x = {a: {b: x.a}};",
        "fail('InlineObjectLiterals cannot inline objects with property references.');");
  }
}