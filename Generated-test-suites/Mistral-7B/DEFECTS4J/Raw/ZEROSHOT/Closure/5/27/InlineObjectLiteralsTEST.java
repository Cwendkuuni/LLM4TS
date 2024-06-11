package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPipeline;
import com.google.javascript.jscomp.CompilerPipeline.CompilationResult;
import com.google.javascript.jscomp.CompilerTestBase;
import com.google.javascript.rhino.Node;

public class InlineObjectLiteralsTest extends CompilerTestBase {

  public void testInlineObjectLiterals() throws Exception {
    AbstractCompiler compiler = newAbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    options.setWarn(false);

    CompilerPass pass = new InlineObjectLiterals(compiler, () -> getUniqueNameGenerator().getNextName());
    CompilerPipeline pipeline = new CompilerPipeline(compiler, options);
    pipeline.addPass(pass);

    String input = "function Foo() {\n" +
        "  var obj1 = {\n" +
        "    a: 1,\n" +
        "    b: 2,\n" +
        "    c: function() {}\n" +
        "  };\n" +
        "  var obj2 = obj1;\n" +
        "  var obj3 = {\n" +
        "    d: obj1,\n" +
        "    e: 3\n" +
        "  };\n" +
        "  var obj4 = obj3;\n" +
        "  return obj1;\n" +
        "}";

    CompilationResult result = pipeline.compile(input);

    Node actual = result.getCompiledSource();

    ImmutableMap<String, String> expected = ImmutableMap.of(
        "a", "JSCompiler_object_inline_a_",
        "b", "JSCompiler_object_inline_b_",
        "c", "JSCompiler_object_inline_c_",
        "d", "JSCompiler_object_inline_d_"
    );

    // Objects have been inlined as variables, so the return statement
    // should just return one of those variables.
    String expectedCode = "function Foo() {\n" +
        "  var a = JSCompiler_object_inline_a__;\n" +
        "  var b = JSCompiler_object_inline_b__;\n" +
        "  var c = JSCompiler_object_inline_c__;\n" +
        "  var d = JSCompiler_object_inline_d__;\n" +
        "  return a;\n" +
        "}";

    assertSourceCodeEquals(expectedCode, actual);

    // Check that all references to the inlined variables have been updated.
    NodeTraversal traversal = new NodeTraversal(actual);
    for (Node node : traversal.fromTop(NodeUtil.MATCH_ALL)) {
      if (node.isGetProp()) {
        Node parent = node.getParent();
        Node key = node.getLastChild();
        String varName = key.getString();
        Node expectedVar = NodeUtil.findVariable(expected, varName);
        assertEquals(expectedVar, parent);
      }
    }
  }

  private AbstractCompiler newAbstractCompiler() {
    return new MockAbstractCompiler();
  }

  private static class MockAbstractCompiler extends AbstractCompiler {
    @Override
    public Scope createTopLevelScope() {
      throw new UnsupportedOperationException();
    }
  }
}