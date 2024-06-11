import static org.junit.Assert.*;
import static com.google.javascript.jscomp.InlineObjectLiterals.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Reference;
import com.google.javascript.jscomp.ReferenceCollection;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.IR;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class InlineObjectLiteralsTest {

  private AbstractCompiler compiler;
  private Supplier<String> safeNameIdSupplier;

  @org.junit.Before
  public void setUp() {
    compiler = new Compiler();
    safeNameIdSupplier = () -> "test_id";
  }

  @org.junit.Test
  public void testInlineObjectLiterals() {
    CompilerPass inlineObjectLiterals =
        new InlineObjectLiterals(compiler, safeNameIdSupplier);

    String input =
        "var x = {a: 1, b: function() {}};\n" +
        "x.b();\n" +
        "var y = x;\n" +
        "y.a = 2;\n" +
        "console.log(x.a);";

    Node root = Node.parse(input);

    inlineObjectLiterals.process(null, root);

    Node expected =
        "var a = 1;\n" +
        "var b = function() {}\n" +
        "var x = {\n" +
        "  a: a,\n" +
        "  b: b\n" +
        "};\n" +
        "x.b();\n" +
        "var y = x;\n" +
        "a = 2;\n" +
        "console.log(a);";

    assertEquals(expected.toString(), root.toString());

    Scope scope = root.getFirstChild().getFirstChild().getFirstChild();
    Iterator<Scope.Var> varIt = scope.getVars().iterator();

    Var xVar = varIt.next();
    assertEquals("JSCompiler_object_inline_a_test_id", xVar.name);
    Var aVar = varIt.next();
    assertEquals("JSCompiler_object_inline_b_test_id", aVar.name);
  }

  @org.junit.Test
  public void testInlineObjectLiteralsWithFunctionProperties() {
    CompilerPass inlineObjectLiterals =
        new InlineObjectLiterals(compiler, safeNameIdSupplier);

    String input =
        "function foo() {\n" +
        "  var x = {foo: function() {}};\n" +
        "  x.foo();\n" +
        "  var y = x;\n" +
        "  y.foo();\n" +
        "}\n" +
        "foo();";

    Node root = Node.parse(input);

    inlineObjectLiterals.process(null, root);

    Node expected =
        "function foo() {\n" +
        "  var foo = function() {}\n" +
        "  var x = {\n" +
        "    foo: foo\n" +
        "  };\n" +
        "  x.foo();\n" +
        "  var y = x;\n" +
        "  foo = function() {}\n" +
        "  y.foo();\n" +
        "}\n" +
        "foo();";

    assertEquals(expected.toString(), root.toString());
  }

  @org.junit.Test
  public void testInlineObjectLiteralsWithNestedProperties() {
    CompilerPass inlineObjectLiterals =
        new InlineObjectLiterals(compiler, safeNameIdSupplier);

    String input =
        "function foo() {\n" +
        "  var x = {a: {b: 1}};\n" +
        "  x.a.b = 2;\n" +
        "  var y = x;\n" +
        "  y.a.b = 3;\n" +
        "  console.log(x.a.b, y.a.b);\n" +
        "}\n" +
        "foo();";

    Node root = Node.parse(input);

    inlineObjectLiterals.process(null, root);

    Node expected =
        "function foo() {\n" +
        "  var a = {\n" +
        "    b: 1\n" +
        "  };\n" +
        "  var x = {\n" +
        "    a: a\n" +
        "  };\n" +
        "  a.b = 2;\n" +
        "  var y = x;\n" +
        "  a = {\n" +
        "    b: 3\n" +
        "  };\n" +
        "  x = {\n" +
        "    a: a\n" +
        "  };\n" +
        "  console.log(a.b, x.a.b);\n" +
        "}\n" +
        "foo();";

    assertEquals(expected.toString(), root.toString());
  }

  @org.junit.Test
  public void testInlineObjectLiteralsWithSelfReference() {
    CompilerPass inlineObjectLiterals =
        new InlineObjectLiterals(compiler, safeNameIdSupplier);

    String input =
        "function foo() {\n" +
        "  var x = {\n" +
        "    a: function() { return x; },\n" +
        "    b: 1\n" +
        "  };\n" +
        "  console.log(x.a().a() === x);\n" +
        "}\n" +
        "foo();";

    Node root = Node.parse(input);

    inlineObjectLiterals.process(null, root);

    Node expected =
        "function foo() {\n" +
        "  var a = function() { return this };\n" +
        "  var x = {\n" +
        "    a: a,\n" +
        "    b: 1\n" +
        "  };\n" +
        "  console.log(a.call(x).a() === x);\n" +
        "}\n" +
        "foo();";

    assertEquals(expected.toString(), root.toString());
  }

  @org.junit.Test
  public void testInlineObjectLiteralsWithSelfReferenceInInitializer() {
    CompilerPass inlineObjectLiterals =
        new InlineObjectLiterals(compiler, safeNameIdSupplier);

    String input =
        "function foo() {\n" +
        "  var x = {\n" +
        "    a: function() { return this },\n" +
        "    b: 1\n" +
        "  };\n" +
        "  x.a = function() { return x.a; };\n" +
        "  console.log(x.a() === x);\n" +
        "}\n" +
        "foo();";

    Node root = Node.parse(input);

    inlineObjectLiterals.process(null, root);

    Node expected =
        "function foo() {\n" +
        "  var a = function() { return this };\n" +
        "  var x = {\n" +
        "    a: a,\n" +
        "    b: 1\n" +
        "  };\n" +
        "  a = function() { return x.a };\n" +
        "  console.log(a.call(x) === x);\n" +
        "}\n" +
        "foo();";

    assertEquals(expected.toString(), root.toString());
  }

  @org.junit.Test
  public void testInlineObjectLiteralsWithSelfReferenceInGetter() {
    CompilerPass inlineObjectLiterals =
        new InlineObjectLiterals(compiler, safeNameIdSupplier);

    String input =
        "function foo() {\n" +
        "  var x = {\n" +
        "    get a() { return this; },\n" +
        "    b: 1\n" +
        "  };\n" +
        "  console.log(x.a === x);\n" +
        "}\n" +
        "foo();";

    Node root = Node.parse(input);

    inlineObjectLiterals.process(null, root);

    Node expected =
        "function foo() {\n" +
        "  var getter = function() { return this };\n" +
        "  var x = {\n" +
        "    get a() { return getter },\n" +
        "    b: 1\n" +
        "  };\n" +
        "  console.log(getter.call(x) === x);\n" +
        "}\n" +
        "foo();";

    assertEquals(expected.toString(), root.toString());
  }

  @org.junit.Test
  public void testInlineObjectLiteralsWithCyclicReference() {
    CompilerPass inlineObjectLiterals =
        new InlineObjectLiterals(compiler, safeNameIdSupplier);

    String input =
        "function foo() {\n" +
        "  var x = {\n" +
        "    a: 1,\n" +
        "    b: x\n" +
        "  };\n" +
        "  console.log(x.b === x);\n" +
        "}\n" +
        "foo();";

    Node root = Node.parse(input);

    inlineObjectLiterals.process(null, root);

    Node expected =
        "function foo() {\n" +
        "  var a = 1;\n" +
        "  var x = {\n" +
        "    a: a,\n" +
        "    b: a\n" +
        "  };\n" +
        "  console.log(a === x);\n" +
        "}\n" +
        "foo();";

    assertEquals(expected.toString(), root.toString());
  }

  @org.junit.Test
  public void testInlineObjectLiteralsWithCyclicReferenceInGetter() {
    CompilerPass inlineObjectLiterals =
        new InlineObjectLiterals(compiler, safeNameIdSupplier);

    String input =
        "function foo() {\n" +
        "  var x = {\n" +
        "    get a() { return this.b },\n" +
        "    get b() { return this.a }\n" +
        "  };\n" +
        "  console.log(x.a === x.b);\n" +
        "}\n" +
        "foo();";

    Node root = Node.parse(input);

    inlineObjectLiterals.process(null, root);

    Node expected =
        "function foo() {\n" +
        "  var a = null;\n" +
        "  var x = {\n" +
        "    get a() { return a },\n" +
        "    get b() { return a }\n" +
        "  };\n" +
        "  a = x;\n" +
        "  console.log(x.a === x.b);\n" +
        "}\n" +
        "foo();";

    assertEquals(expected.toString(), root.toString());
  }

  @org.junit.Test
  public void testInlineObjectLiteralsWithCyclicReferenceInSetter() {
    CompilerPass inlineObjectLiterals =
        new InlineObjectLiterals(compiler, safeNameIdSupplier);

    String input =
        "function foo() {\n" +
        "  var x = {\n" +
        "    set a(value) { this.b = value },\n" +
        "    get b() { return this.a }\n" +
        "  };\n" +
        "  x.a = 1;\n" +
        "  console.log(x.a === x.b);\n" +
        "}\n" +
        "foo();";

    Node root = Node.parse(input);

    inlineObjectLiterals.process(null, root);

    Node expected =
        "function foo() {\n" +
        "  var a = 1;\n" +
        "  var x = {\n" +
        "    set a(value) { this.b = value },\n" +
        "    get b() { return a }\n" +
        "  };\n" +
        "  console.log(a === x.b);\n" +
        "}\n" +
        "foo();";

    assertEquals(expected.toString(), root.toString());
  }
}