package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import junit.sourcefiles.TestUtilities;

@RunWith(JUnit4.class)
public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() throws Exception {
    String input = "function foo() {\n" +
        "  var obj = {x: 1, y: 2};\n" +
        "  var z = obj.x;\n" +
        "  obj.x = 3;\n" +
        "  var a = obj;\n" +
        "  var b = {\n" +
        "    x: obj.x,\n" +
        "    y: obj.y\n" +
        "  };\n" +
        "  obj = null;\n" +
        "  assert(z === 3);\n" +
        "  assert(a.x === 3);\n" +
        "  assert(b.x === 3);\n" +
        "  assert(b.y === 2);\n" +
        "}";

    String expectedOutput = "function foo() {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "  var obj = {\n" +
        "    x: x,\n" +
        "    y: y\n" +
        "  };\n" +
        "  var z = obj.x;\n" +
        "  x = 3;\n" +
        "  var a = obj;\n" +
        "  var b = {\n" +
        "    x: x,\n" +
        "    y: y\n" +
        "  };\n" +
        "  obj = null;\n" +
        "  assert(z === 3);\n" +
        "  assert(a.x === 3);\n" +
        "  assert(b.x === 3);\n" +
        "  assert(b.y === 2);\n" +
        "}";

    ImmutableMap<String, String> options = ImmutableMap.<String, String>builder()
        .put("--jscomp_warning_level", "VERBOSE")
        .build();

    Node root = TestUtilities.parseJs(input);
    Compiler compiler = newCompiler();
    compiler.compile(root, options);
    String output = TestUtilities.prettyPrint(root);

    assertEquals(expectedOutput, output);
  }

  private AbstractCompiler newCompiler() {
    return new CompilerOptions()
        .addPass(new InlineObjectLiterals(
            new AbstractCompiler(), new NameIdSupplier()))
        .toCompiler();
  }
}

class NameIdSupplier implements Supplier<String> {
  private int counter = 0;

  @Override
  public String get() {
    return "name" + counter++;
  }
}