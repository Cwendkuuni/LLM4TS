package org.apache.commons.cli;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    // Given a simple object literal assignment
    String sourceCode = "function test() {\n" +
        "  var obj = {\n" +
        "    a: 1,\n" +
        "    b: 2\n" +
        "  };\n" +
        "  return obj.a + obj.b;\n" +
        "}";

    // When the InlineObjectLiterals pass is applied
    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(
        new AbstractCompiler() {},
        () -> "test_var_" + System.nanoTime());
    inlineObjectLiterals.process(null, AbstractCompiler.parse(sourceCode, null));

    // Then the object literal should be inlined into separate variable assignments
    String expectedCode = "function test() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "  return a + b;\n" +
        "}";

    assertEquals(expectedCode, AbstractCompiler.printTree(
        inlineObjectLiterals.compiler.getRootNode()));
  }

  @Test
  public void testInlineObjectLiteralsWithFunctionProperties() {
    // Given an object literal with a function property
    String sourceCode = "function test() {\n" +
        "  var obj = {\n" +
        "    a: 1,\n" +
        "    foo: function() {\n" +
        "      return 42;\n" +
        "    },\n" +
        "    b: 2\n" +
        "  };\n" +
        "  return obj.a + obj.foo() + obj.b;\n" +
        "}";

    // When the InlineObjectLiterals pass is applied
    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(
        new AbstractCompiler() {},
        () -> "test_var_" + System.nanoTime());
    inlineObjectLiterals.process(null, AbstractCompiler.parse(sourceCode, null));

    // Then the object literal should be inlined into separate variable assignments
    String expectedCode = "function test() {\n" +
        "  var a = 1;\n" +
        "  var foo = function() {\n" +
        "    return 42;\n" +
        "  };\n" +
        "  var b = 2;\n" +
        "  return a + foo() + b;\n" +
        "}";

    assertEquals(expectedCode, AbstractCompiler.printTree(
        inlineObjectLiterals.compiler.getRootNode()));
  }

  @Test
  public void testInlineObjectLiteralsWithNestedObjectLiterals() {
    // Given an object literal with nested object literals
    String sourceCode = "function test() {\n" +
        "  var obj = {\n" +
        "    a: {\n" +
        "      b: 1,\n" +
        "      c: 2\n" +
        "    },\n" +
        "    d: 3\n" +
        "  };\n" +
        "  return obj.a.b + obj.a.c + obj.d;\n" +
        "}";

    // When the InlineObjectLiterals pass is applied
    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(
        new AbstractCompiler() {},
        () -> "test_var_" + System.nanoTime());
    inlineObjectLiterals.process(null, AbstractCompiler.parse(sourceCode, null));

    // Then the object literal should be inlined into separate variable assignments
    String expectedCode = "function test() {\n" +
        "  var a_b = 1;\n" +
        "  var a_c = 2;\n" +
        "  var d = 3;\n" +
        "  return a_b + a_c + d;\n" +
        "}";

    assertEquals(expectedCode, AbstractCompiler.printTree(
        inlineObjectLiterals.compiler.getRootNode()));
  }

  @Test
  public void testInlineObjectLiteralsWithUndefinedProperties() {
    // Given an object literal with undefined properties
    String sourceCode = "function test() {\n" +
        "  var obj = {\n" +
        "    a: 1,\n" +
        "    b: 2,\n" +
        "    c: undefined\n" +
        "  };\n" +
        "  return obj.a + obj.b + obj.c;\n" +
        "}";

    // When the InlineObjectLiterals pass is applied
    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(
        new AbstractCompiler() {},
        () -> "test_var_" + System.nanoTime());
    inlineObjectLiterals.process(null, AbstractCompiler.parse(sourceCode, null));

    // Then the object literal should be inlined into separate variable assignments
    String expectedCode = "function test() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "  var c = undefined;\n" +
        "  return a + b + c;\n" +
        "}";

    assertEquals(expectedCode, AbstractCompiler.printTree(
        inlineObjectLiterals.compiler.getRootNode()));
  }

  @Test
  public void testInlineObjectLiteralsWithCycle() {
    // Given an object literal with a cycle
    String sourceCode = "function test() {\n" +
        "  var obj1 = {\n" +
        "    a: 1,\n" +
        "    obj2: obj2\n" +
        "  };\n" +
        "  var obj2 = {\n" +
        "    b: 2,\n" +
        "    obj1: obj1\n" +
        "  };\n" +
        "  return obj1.a + obj2.b;\n" +
        "}";

    // When the InlineObjectLiterals pass is applied
    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(
        new AbstractCompiler() {},
        () -> "test_var_" + System.nanoTime());

    // Then an exception should be thrown because of the cycle
    assertFalse(new ThrowsExceptionTest(InlineObjectLiterals.class) {
      @Override
      public void testExceptionThrownByMethod(Throwable e) {
        inlineObjectLiterals.process(null, AbstractCompiler.parse(sourceCode, null));
      }
    }.run());
  }

  @Test
  public void testInlineObjectLiteralsWithSelfReference() {
    // Given an object literal with a self-reference
    String sourceCode = "function test() {\n" +
        "  var obj = {\n" +
        "    a: 1,\n" +
        "    b: function() {\n" +
        "      return obj.a;\n" +
        "    }\n" +
        "  };\n" +
        "  return obj.b();\n" +
        "}";

    // When the InlineObjectLiterals pass is applied
    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(
        new AbstractCompiler() {},
        () -> "test_var_" + System.nanoTime());
    inlineObjectLiterals.process(null, AbstractCompiler.parse(sourceCode, null));

    // Then the object literal should not be inlined, as it has a self-reference
    String expectedCode = "function test() {\n" +
        "  var obj = {\n" +
        "    a: 1,\n" +
        "    b: function() {\n" +
        "      return this.a;\n" +
        "    }\n" +
        "  };\n" +
        "  return obj.b();\n" +
        "}";

    assertEquals(expectedCode, AbstractCompiler.printTree(
        inlineObjectLiterals.compiler.getRootNode()));
  }
}