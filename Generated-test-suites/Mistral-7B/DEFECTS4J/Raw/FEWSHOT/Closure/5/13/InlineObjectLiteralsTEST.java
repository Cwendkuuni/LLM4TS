package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Supplier;

@RunWith(JUnit4.class)
public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public CompilerPass getPass(String passName) {
        if ("InlineObjectLiterals".equals(passName)) {
          return new InlineObjectLiterals(this, newSupplier());
        }
        return null;
      }
    };

    String input = "function foo() {" +
        "  var x = {a: 1, b: 2};" +
        "  x.a = 3;" +
        "  var y = x;" +
        "  x.b = y;" +
        "  return x;" +
        "}";

    Node externs = Node.root();
    Node root = Node.parse(input);

    InlineObjectLiterals pass = (InlineObjectLiterals) compiler.getPass("InlineObjectLiterals");
    pass.process(externs, root);

    Node expected = Node.parse("function foo() {" +
        "  var a = 1;" +
        "  var b = 2;" +
        "  var x = {" +
        "    a: a," +
        "    b: b" +
        "  };" +
        "  x.a = 3;" +
        "  var y = x;" +
        "  x.b = y;" +
        "  return x;" +
        "}");

    assertTrue(NodeUtil.isTreeEquivalent(root, expected));
  }

  @Test
  public void testInlineObjectLiteralsWithInitialValues() {
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public CompilerPass getPass(String passName) {
        if ("InlineObjectLiterals".equals(passName)) {
          return new InlineObjectLiterals(this, newSupplier());
        }
        return null;
      }
    };

    String input = "function foo() {" +
        "  var x = {a: 1, b: 2};" +
        "  x.a = 3;" +
        "  return x;" +
        "}";

    Node externs = Node.root();
    Node root = Node.parse(input);

    InlineObjectLiterals pass = (InlineObjectLiterals) compiler.getPass("InlineObjectLiterals");
    pass.process(externs, root);

    Node expected = Node.parse("function foo() {" +
        "  var a = 1;" +
        "  var b = 2;" +
        "  var x = {" +
        "    a: a," +
        "    b: b" +
        "  }" +
        "  return x;" +
        "}");

    // Ensure initial values are inlined
    Node x = root.getFirstChild().getFirstChild();
    List<Node> xChildren = Lists.newArrayList(x.getChildren());
    assertNotNull(xChildren.get(0).getString().equals("a"));
    assertNotNull(xChildren.get(1).getString().equals("b"));

    assertTrue(NodeUtil.isTreeEquivalent(root, expected));
  }

  @Test
  public void testInlineObjectLiteralsWithSelfReferentialAssignment() {
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public CompilerPass getPass(String passName) {
        if ("InlineObjectLiterals".equals(passName)) {
          return new InlineObjectLiterals(this, newSupplier());
        }
        return null;
      }
    };

    String input = "function foo() {" +
        "  var x = {" +
        "    a: x.b," +
        "    b: 2" +
        "  };" +
        "  return x;" +
        "}";

    Node externs = Node.root();
    Node root = Node.parse(input);

    InlineObjectLiterals pass = (InlineObjectLiterals) compiler.getPass("InlineObjectLiterals");
    pass.process(externs, root);

    Node expected = Node.parse("function foo() {" +
        "  var a = 2;" +
        "  var x = {" +
        "    a: a," +
        "    b: a" +
        "  }" +
        "  return x;" +
        "}");

    assertTrue(NodeUtil.isTreeEquivalent(root, expected));
  }

  @Test
  public void testInlineObjectLiteralsWithNestedObjectLiterals() {
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public CompilerPass getPass(String passName) {
        if ("InlineObjectLiterals".equals(passName)) {
          return new InlineObjectLiterals(this, newSupplier());
        }
        return null;
      }
    };

    String input = "function foo() {" +
        "  var x = {" +
        "    a: 1," +
        "    b: {" +
        "      c: 2," +
        "      d: 3" +
        "    }" +
        "  };" +
        "  return x;" +
        "}";

    Node externs = Node.root();
    Node root = Node.parse(input);

    InlineObjectLiterals pass = (InlineObjectLiterals) compiler.getPass("InlineObjectLiterals");
    pass.process(externs, root);

    Node expected = Node.parse("function foo() {" +
        "  var a = 1;" +
        "  var b = {" +
        "    c: 2," +
        "    d: 3" +
        "  }" +
        "  var x = {" +
        "    a: a," +
        "    b: b" +
        "  }" +
        "  return x;" +
        "}");

    assertTrue(NodeUtil.isTreeEquivalent(root, expected));
  }

  @Test
  public void testInlineObjectLiteralsWithPropertiesNotInObjectLiteral() {
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public CompilerPass getPass(String passName) {
        if ("InlineObjectLiterals".equals(passName)) {
          return new InlineObjectLiterals(this, newSupplier());
        }
        return null;
      }
    };

    String input = "function foo() {" +
        "  var x = {" +
        "    a: 1," +
        "    b: 2" +
        "  };" +
        "  x.c = 3;" +
        "  return x;" +
        "}";

    Node externs = Node.root();
    Node root = Node.parse(input);

    InlineObjectLiterals pass = (InlineObjectLiterals) compiler.getPass("InlineObjectLiterals");
    pass.process(externs, root);

    Node expected = Node.parse("function foo() {" +
        "  var a = 1;" +
        "  var b = 2;" +
        "  var x = {" +
        "    a: a," +
        "    b: b" +
        "  }" +
        "  x.c = 3;" +
        "  return x;" +
        "}");

    assertTrue(NodeUtil.isTreeEquivalent(root, expected));
  }

  @Test
  public void testInlineObjectLiteralsWithGlobalVariable() {
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public CompilerPass getPass(String passName) {
        if ("InlineObjectLiterals".equals(passName)) {
          return new InlineObjectLiterals(this, newSupplier());
        }
        return null;
      }
    };

    String input = "var x = {a: 1, b: 2};" +
        "function foo() {" +
        "  x.a = 3;" +
        "  return x;" +
        "}";

    Node externs = Node.root();
    Node root = Node.parse(input);

    InlineObjectLiterals pass = (InlineObjectLiterals) compiler.getPass("InlineObjectLiterals");
    pass.process(externs, root);

    Node expected = Node.parse("var a = 1;" +
        "var b = 2;" +
        "var x = {" +
        "  a: a," +
        "  b: b" +
        "}" +
        "function foo() {" +
        "  x.a = 3;" +
        "  return x;" +
        "}");

    assertTrue(NodeUtil.isTreeEquivalent(root, expected));
  }

  @Test
  public void testInlineObjectLiteralsWithExportedVariable() {
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public CompilerPass getPass(String passName) {
        if ("InlineObjectLiterals".equals(passName)) {
          return new InlineObjectLiterals(this, newSupplier());
        }
        return null;
      }
    };

    String input = "export let x = {a: 1, b: 2};" +
        "function foo() {" +
        "  x.a = 3;" +
        "  return x;" +
        "}";

    Node externs = Node.root();
    externs.addChild(Node.parse("export let x;"));
    Node root = Node.parse(input);

    InlineObjectLiterals pass = (InlineObjectLiterals) compiler.getPass("InlineObjectLiterals");
    pass.process(externs, root);

    Node expected = Node.parse("export let a = 1;" +
        "export let b = 2;" +
        "var x = {" +
        "  a: a," +
        "  b: b" +
        "}" +
        "function foo() {" +
        "  x.a = 3;" +
        "  return x;" +
        "}");

    assertTrue(NodeUtil.isTreeEquivalent(root, expected));
  }

  @Test
  public void testInlineObjectLiteralsWithForbiddenVariable() {
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public CompilerPass getPass(String passName) {
        if ("InlineObjectLiterals".equals(passName)) {
          return new InlineObjectLiterals(this, newSupplier());
        }
        return null;
      }
    };

    String input = "function foo() {" +
        "  var x = {a: 1, RENAME_PROPERTY_FUNCTION_NAME: 2};" +
        "}";

    Node externs = Node.root();
    Node root = Node.parse(input);

    InlineObjectLiterals pass = (InlineObjectLiterals) compiler.getPass("InlineObjectLiterals");
    pass.process(externs, root);

    assertTrue(NodeUtil.isTreeEquivalent(root, Node.parse(input)));
  }

  @Test
  public void testInlineObjectLiteralsWithStaleVariables() {
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public CompilerPass getPass(String passName) {
        if ("InlineObjectLiterals".equals(passName)) {
          return new InlineObjectLiterals(this, newSupplier());
        }
        return null;
      }
    };

    String input = "function foo() {" +
        "  var x = {a: 1, b: 2};" +
        "  x.a = 3;" +
        "  var y = x;" +
        "  x.b = y;" +
        "  return x;" +
        "}" +
        "function bar() {" +
        "  var z = {a: 1, b: x};" +
        "  return z;" +
        "}";

    Node externs = Node.root();
    Node root = Node.parse(input);

    InlineObjectLiterals pass = (InlineObjectLiterals) compiler.getPass("InlineObjectLiterals");
    pass.process(externs, root);

    Node expected = Node.parse("function foo() {" +
        "  var a = 1;" +
        "  var b = 2;" +
        "  var x = {" +
        "    a: a," +
        "    b: b" +
        "  }" +
        "  x.a = 3;" +
        "  var y = x;" +
        "  x.b = y;" +
        "  return x;" +
        "}" +
        "function bar() {" +
        "  var a = 1;" +
        "  var x = {" +
        "    a: a" +
        "  }" +
        "  return x;" +
        "}");

    assertTrue(NodeUtil.isTreeEquivalent(root, expected));
  }

  private static Supplier<String> newSupplier() {
    return new Supplier<String>() {
      private int counter = 0;

      @Override
      public String get() {
        return "JSCompiler_object_inline_" + (++counter);
      }
    };
  }
}