package com.google.javascript.jscomp;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.google.javascript.rhino.Nodeops.assertThrows;
import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class InlineObjectLiteralsTest {

  @Test
  public void testInlineSimpleObjectLiteral() {
    String code = "var obj = {a: 1, b: 2};";
    Compiler pass = new InlineObjectLiterals(new TestCompiler(), () -> "");
    AbstractCompiler compiler = new Compiler();
    Node root = compiler.parseTree(code);
    pass.process(compiler.getExterns(), root);
    String result = compiler.printTree(root);

    ImmutableMap<String, String> expected =
        ImmutableMap.of("JSCompiler_object_inline_a_", "1",
                        "JSCompiler_object_inline_b_", "2");
    ImmutableMap<String, String> actual = parseVarMap(result);

    assertEquals(expected, actual);
  }

  @Test
  public void testInlineNestedObjectLiteral() {
    String code = "var obj = {a: {b: 1, c: 2}, d: 3};";
    Compiler pass = new InlineObjectLiterals(new TestCompiler(), () -> "");
    AbstractCompiler compiler = new Compiler();
    Node root = compiler.parseTree(code);
    pass.process(compiler.getExterns(), root);
    String result = compiler.printTree(root);

    ImmutableMap<String, String> expected = ImmutableMap.<String, String>builder()
        .put("JSCompiler_object_inline_a_b_", "1")
        .put("JSCompiler_object_inline_a_c_", "2")
        .put("JSCompiler_object_inline_a_", "{}")
        .put("JSCompiler_object_inline_d_", "3")
        .build();
    ImmutableMap<String, String> actual = parseVarMap(result);

    assertEquals(expected, actual);
  }

  @Test
  public void testInlineEmptyObjectLiteral() {
    String code = "var obj = {};";
    Compiler pass = new InlineObjectLiterals(new TestCompiler(), () -> "");
    AbstractCompiler compiler = new Compiler();
    Node root = compiler.parseTree(code);
    pass.process(compiler.getExterns(), root);
    String result = compiler.printTree(root);

    ImmutableMap<String, String> expected = ImmutableMap.of();
    ImmutableMap<String, String> actual = parseVarMap(result);

    assertEquals(expected, actual);
  }

  @Test
  public void testInlineObjectLiteralWithUndefinedProperty() {
    String code = "var obj = {a: 1, undefined: 2};";
    Compiler pass = new InlineObjectLiterals(new TestCompiler(), () -> "");
    AbstractCompiler compiler = new Compiler();
    Node root = compiler.parseTree(code);
    pass.process(compiler.getExterns(), root);
    String result = compiler.printTree(root);

    ImmutableMap<String, String> expected = ImmutableMap.of("JSCompiler_object_inline_a_", "1");
    ImmutableMap<String, String> actual = parseVarMap(result);

    assertEquals(expected, actual);
  }

  @Test
  public void testInlineObjectLiteralWithCycle() {
    String code = "var obj1 = {a: obj2}; var obj2 = {b: obj1};";
    Compiler pass = new InlineObjectLiterals(new TestCompiler(), () -> "");
    AbstractCompiler compiler = new Compiler();
    Node root = compiler.parseTree(code);
    pass.process(compiler.getExterns(), root);

    assertThrows(IllegalStateException.class, () -> compiler.printTree(root));
  }

  @Test
  public void testInlineObjectLiteralWithFunctionProperty() {
    String code = "var obj = {get foo() {}};";
    Compiler pass = new InlineObjectLiterals(new TestCompiler(), () -> "");
    AbstractCompiler compiler = new Compiler();
    Node root = compiler.parseTree(code);
    pass.process(compiler.getExterns(), root);
    String result = compiler.printTree(root);

    ImmutableMap<String, String> expected = ImmutableMap.of();
    ImmutableMap<String, String> actual = parseVarMap(result);

    assertEquals(expected, actual);
  }

  @Test
  public void testInlineObjectLiteralWithGetterSetterProperty() {
    String code = "var obj = {get prop() {return 1;}, set prop(x) {}}; obj.prop = 2;";
    Compiler pass = new InlineObjectLiterals(new TestCompiler(), () -> "");
    AbstractCompiler compiler = new Compiler();
    Node root = compiler.parseTree(code);
    pass.process(compiler.getExterns(), root);
    String result = compiler.printTree(root);

    ImmutableMap<String, String> expected = ImmutableMap.of();
    ImmutableMap<String, String> actual = parseVarMap(result);

    assertEquals(expected, actual);
  }

  @Test
  public void testInlineObjectLiteralWithConstantProperty() {
    String code = "var obj = {const PROP: 1};";
    Compiler pass = new InlineObjectLiterals(new TestCompiler(), () -> "");
    AbstractCompiler compiler = new Compiler();
    Node root = compiler.parseTree(code);
    pass.process(compiler.getExterns(), root);
    String result = compiler.printTree(root);

    ImmutableMap<String, String> expected = ImmutableMap.of();
    ImmutableMap<String, String> actual = parseVarMap(result);

    assertEquals(expected, actual);
  }

  private ImmutableMap<String, String> parseVarMap(String code) {
    Compiler compiler = new TestCompiler();
    Node root = compiler.parseTree(code);
    return InlineObjectLiterals.parseVarMap(root);
  }
}