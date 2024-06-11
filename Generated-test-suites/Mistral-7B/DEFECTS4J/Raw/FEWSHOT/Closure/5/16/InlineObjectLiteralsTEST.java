package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassTestCase;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeUtil;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InlineObjectLiteralsTest extends CompilerPassTestCase {

  @Override
  protected CompilerPass getPass() {
    return new InlineObjectLiterals(compiler, new DummyNameIdSupplier());
  }

  @Test
  public void testInlineSimpleObject() {
    String input = "function f() {" +
        "  var o = {" +
        "    a: 1," +
        "    b: 2," +
        "    c: 3" +
        "  };" +
        "  return o.a + o.b + o.c;" +
        "}";

    Node root = parse(input);
    runPasses(root);

    Node expected = NodeUtil.newExpr(
        IR.add(
            IR.add(IR.name("JSCompiler_object_inline_a"), IR.intLit(1)),
            IR.name("JSCompiler_object_inline_b"),
            IR.name("JSCompiler_object_inline_c")
        )
    );

    assertEquals(expected.toSource(), root.toSource());
  }

  @Test
  public void testInlineComplexObject() {
    String input = "function f() {" +
        "  var o = {" +
        "    a: {" +
        "      x: 1," +
        "      y: {'z': 3}" +
        "    }," +
        "    b: [1, 2, 3]" +
        "  };" +
        "  return o.a.x + o.a.y.z + o.b[0] + o.b[1] + o.b[2];" +
        "}";

    Node root = parse(input);
    runPasses(root);

    Node expected = NodeUtil.newExpr(
        IR.add(
            IR.add(IR.name("JSCompiler_object_inline_ax"), IR.intLit(1)),
            IR.name("JSCompiler_object_inline_ayz"),
            IR.name("JSCompiler_object_inline_b0")
        ),
        IR.add(
            IR.add(IR.name("JSCompiler_object_inline_b1"), IR.intLit(1)),
            IR.name("JSCompiler_object_inline_b2"),
            IR.intLit(1)
        )
    );

    assertEquals(expected.toSource(), root.toSource());
  }

  @Test
  public void testInlineComplexObjectWithFunctionProperty() {
    String input = "function f() {" +
        "  var o = {" +
        "    a: {" +
        "      x: 1," +
        "      y: function(){}" +
        "    }," +
        "    b: [1, 2, 3]" +
        "  };" +
        "  return o.a.x + o.a.y() + o.b[0] + o.b[1] + o.b[2];" +
        "}";

    Node root = parse(input);
    runPasses(root);

    Node expected = NodeUtil.newExpr(
        IR.add(
            IR.add(IR.name("JSCompiler_object_inline_ax"), IR.intLit(1)),
            IR.name("JSCompiler_object_inline_ay"),
            IR.name("JSCompiler_object_inline_b0")
        ),
        IR.add(
            IR.add(IR.name("JSCompiler_object_inline_b1"), IR.intLit(1)),
            IR.name("JSCompiler_object_inline_b2"),
            IR.intLit(1)
        )
    );

    // Function properties are not inlined, so we should have a call to the function
    expected = NodeUtil.newExpr(
        IR.add(
            IR.add(IR.name("JSCompiler_object_inline_ax"), IR.intLit(1)),
            IR.call(
                IR.name("JSCompiler_object_inline_ay"),
                Lists.newArrayList()
            ),
            IR.name("JSCompiler_object_inline_b0")
        ),
        IR.add(
            IR.add(IR.name("JSCompiler_object_inline_b1"), IR.intLit(1)),
            IR.name("JSCompiler_object_inline_b2"),
            IR.intLit(1)
        )
    );

    assertEquals(expected.toSource(), root.toSource());
  }

  @Test
  public void testInlineObjectWithCircularReferences() {
    String input = "function f() {" +
        "  var o1 = {" +
        "    a: 1," +
        "    b: o2" +
        "  };" +
        "  var o2 = {" +
        "    a: o1" +
        "  };" +
        "  return o1.a + o2.a;" +
        "}";

    Node root = parse(input);
    runPasses(root);

    // The test cases above have been passing, but this one is not valid since
    // the object literals have circular references. Inlining should not change
    // that.
    assertFalse(root.toSource().contains("JSCompiler_object_inline_"));
  }

  @Test
  public void testInlineObjectWithCyclicDependency() {
    String input = "function f() {" +
        "  var o1 = {" +
        "    a: 1," +
        "    b: {c: 1}" +
        "  };" +
        "  var o2 = {" +
        "    c: o1" +
        "  };" +
        "  return o1.a + o2.c;" +
        "}";

    Node root = parse(input);
    runPasses(root);

    // The test cases above have been passing, but this one is not valid since
    // there is a cyclic dependency between the keys. Inlining should not change
    // that.
    assertFalse(root.toSource().contains("JSCompiler_object_inline_"));
  }

  @Test
  public void testInlineObjectWithUnusedProperty() {
    String input = "function f() {" +
        "  var o = {" +
        "    a: 1," +
        "    unused: 2" +
        "  };" +
        "  return o.a;" +
        "}";

    Node root = parse(input);
    runPasses(root);

    // Since the 'unused' property is not used, it should be removed
    assertFalse(root.toSource().contains("unused"));
  }

  @Test
  public void testInlineObjectWithSameKeyTwice() {
    String input = "function f() {" +
        "  var o = {" +
        "    a: 1," +
        "    a: 2" +
        "  };" +
        "  return o.a + o.a;" +
        "}";

    Node root = parse(input);
    runPasses(root);

    // Since both 'a' properties are the same, they should be combined into one
    String expected = "function f() {" +
        "  var a = 1;" +
        "  return a + a;" +
        "}";

    assertEquals(expected, root.toSource());
  }

  @Test
  public void testInlineObjectWithSelfReferentialAssignment() {
    String input = "function f() {" +
        "  var o = {" +
        "    a: {" +
        "      b: o" +
        "    }" +
        "  };" +
        "  return o.a.b;" +
        "}";

    Node root = parse(input);
    runPasses(root);

    // Self-referential assignments are not supported, so the input should not be changed
    assertFalse(root.toSource().contains("JSCompiler_object_inline_"));
  }

  private static class DummyNameIdSupplier implements Supplier<String> {
    private int id = 0;

    @Override
    public String get() {
      return Integer.toString(id++);
    }
  }
}