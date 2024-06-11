import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Reference;
import com.google.javascript.jscomp.ReferenceCollection;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Token;

public class InlineObjectLiteralsTest {

  private Compiler compiler;
  private InlineObjectLiterals inlineObjectLiterals;
  private AbstractCompiler abstractCompiler;
  private Supplier<String> safeNameIdSupplier;

  @Before
  public void setup() {
    compiler = new Compiler();
    abstractCompiler = new AbstractCompiler(compiler) {};
    safeNameIdSupplier = () -> "test";

    inlineObjectLiterals = new InlineObjectLiterals(
        abstractCompiler, safeNameIdSupplier);
  }

  @Test
  public void testInlineSimpleObjectLiterals() {
    String code = "var obj = { a: 1, b: 2 };";
    Node root = NodePrinter.print(NodePrinter.parse(code));
    inlineObjectLiterals.process(NodeUtil.newRootNode(compiler, code), root);

    Node expected = NodePrinter.print(
        NodePrinter.parse("var a = 1, b = 2;\n" +
            "var obj = { a: a, b: b };\n" +
            "return obj;"));
    assertEquals(expected, NodeUtil.getChild(root, 0));
  }

  @Test
  public void testInlineObjectLiteralsWithFunctionProperty() {
    String code = "function f() {\n" +
        "  var obj = { f: function() {\n" +
        "    return this.a;\n" +
        "  }, a: 1 };\n" +
        "  return obj.f();\n" +
        "}";
    Node root = NodeUtil.newRootNode(compiler, code);
    inlineObjectLiterals.process(root, root);

    Node expected = NodePrinter.print(
        NodePrinter.parse("function f() {\n" +
            "  var a = 1;\n" +
            "  var obj = { f: function() {\n" +
            "    return a;\n" +
            "  }, a: a };\n" +
            "  return obj.f();\n" +
            "}"));
    assertEquals(expected, NodeUtil.getChild(root, 0));
  }

  @Test
  public void testInlineObjectLiteralsWithNestedObjectLiterals() {
    String code = "var obj = { a: { b: 1 } };";
    Node root = NodeUtil.newRootNode(compiler, code);
    inlineObjectLiterals.process(root, root);

    Node expected = NodePrinter.print(
        NodePrinter.parse("var a = {};\n" +
            "var b = 1;\n" +
            "a.b = b;\n" +
            "var obj = { a: a };\n" +
            "return obj;"));
    Node actual = NodeUtil.getChild(root, 0);

    ListMultimap<String, Node> actualVars = ArrayListMultimap.create();
    NodeUtil.visit(actual, new NodeUtil.Visitor<Void>() {
      @Override
      public Void visit(Node node) {
        if (node.getType() == Token.VAR) {
          actualVars.put(node.getString(), node);
        }
        return null;
      }
    });

    Map<String, Node> expectedVars = Maps.newHashMap();
    NodeUtil.visit(expected, new NodeUtil.Visitor<Void>() {
      @Override
      public Void visit(Node node) {
        if (node.getType() == Token.VAR) {
          expectedVars.put(node.getString(), node);
        }
        return null;
      }
    });

    for (Map.Entry<String, Node> entry : expectedVars.entrySet()) {
      Node expectedVar = entry.getValue();
      Node actualVar = actualVars.get(entry.getKey());
      assertEquals(expectedVar.getFirstChild().getString(),
          actualVar.getFirstChild().getString());
    }
    assertEquals(expected, actual);
  }

  @Test
  public void testDontInlineObjectLiteralsWithSelfReference() {
    String code = "var obj = { a: obj };";
    Node root = NodeUtil.newRootNode(compiler, code);
    inlineObjectLiterals.process(root, root);

    Node expected = NodePrinter.print(NodePrinter.parse(code));
    assertEquals(expected, NodeUtil.getChild(root, 0));
  }

  @Test
  public void testInlineObjectLiteralsWithFunctionDeclaration() {
    String code = "function f() {\n" +
        "  var obj = {\n" +
        "    foo: function() {\n" +
        "      return this.bar;\n" +
        "    },\n" +
        "    bar: 1,\n" +
        "  };\n" +
        "  return obj.foo();\n" +
        "}";
    Node root = NodeUtil.newRootNode(compiler, code);
    inlineObjectLiterals.process(root, root);

    Node expected = NodePrinter.print(
        NodePrinter.parse("function f() {\n" +
            "  var bar = 1;\n" +
            "  var obj = {\n" +
            "    foo: function() {\n" +
            "      return bar;\n" +
            "    },\n" +
            "    bar: bar\n" +
            "  };\n" +
            "  return obj.foo();\n" +
            "}"));
    Node actual = NodeUtil.getChild(root, 0);

    ListMultimap<String, Node> actualVars = ArrayListMultimap.create();
    NodeUtil.visit(actual, new NodeUtil.Visitor<Void>() {
      @Override
      public Void visit(Node node) {
        if (node.getType() == Token.VAR) {
          actualVars.put(node.getString(), node);
        }
        return null;
      }
    });

    Map<String, Node> expectedVars = Maps.newHashMap();
    NodeUtil.visit(expected, new NodeUtil.Visitor<Void>() {
      @Override
      public Void visit(Node node) {
        if (node.getType() == Token.VAR) {
          expectedVars.put(node.getString(), node);
        }
        return null;
      }
    });

    for (Map.Entry<String, Node> entry : expectedVars.entrySet()) {
      Node expectedVar = entry.getValue();
      Node actualVar = actualVars.get(entry.getKey());
      assertEquals(expectedVar.getFirstChild().getString(),
          actualVar.getFirstChild().getString());
    }
    assertEquals(expected, actual);
  }

  @Test
  public void testInlineObjectLiteralsWithInitObjectLiterals() {
    String code = "var obj = { a: 1, b: 2 };\n" +
        "function f() {\n" +
        "  var x = obj;\n" +
        "  var y = x.b;\n" +
        "  return y;\n" +
        "}";
    Node root = NodeUtil.newRootNode(compiler, code);
    inlineObjectLiterals.process(root, root);

    Node expected = NodePrinter.print(
        NodePrinter.parse("var a = 1,\n" +
            "               b = 2;\n" +
            "var obj = {\n" +
            "  a: a,\n" +
            "  b: b\n" +
            "};\n" +
            "function f() {\n" +
            "  var y = b;\n" +
            "  return y;\n" +
            "}"));
    Node actual = NodeUtil.getChild(root, 0);

    ListMultimap<String, Node> actualVars = ArrayListMultimap.create();
    NodeUtil.visit(actual, new NodeUtil.Visitor<Void>() {
      @Override
      public Void visit(Node node) {
        if (node.getType() == Token.VAR) {
          actualVars.put(node.getString(), node);
        }
        return null;
      }
    });

    Map<String, Node> expectedVars = Maps.newHashMap();
    NodeUtil.visit(expected, new NodeUtil.Visitor<Void>() {
      @Override
      public Void visit(Node node) {
        if (node.getType() == Token.VAR) {
          expectedVars.put(node.getString(), node);
        }
        return null;
      }
    });

    for (Map.Entry<String, Node> entry : expectedVars.entrySet()) {
      Node expectedVar = entry.getValue();
      Node actualVar = actualVars.get(entry.getKey());
      assertEquals(expectedVar.getFirstChild().getString(),
          actualVar.getFirstChild().getString());
    }
    assertEquals(expected, actual);
  }

  @Test
  public void testInlineObjectLiteralsWithCyclicReferences() {
    String code = "var obj1 = { a: obj2 };\n" +
        "var obj2 = { a: obj1 };";
    Node root = NodeUtil.newRootNode(compiler, code);
    inlineObjectLiterals.process(root, root);

    Node expected = NodePrinter.print(
        NodePrinter.parse("var obj1 = {}, obj2 = {};\n" +
            "obj1.a = obj2;\n" +
            "obj2.a = obj1;"));
    Node actual = NodeUtil.getChild(root, 0);

    ListMultimap<String, Node> actualVars = ArrayListMultimap.create();
    NodeUtil.visit(actual, new NodeUtil.Visitor<Void>() {
      @Override
      public Void visit(Node node) {
        if (node.getType() == Token.VAR) {
          actualVars.put(node.getString(), node);
        }
        return null;
      }
    });

    Map<String, Node> expectedVars = Maps.newHashMap();
    NodeUtil.visit(expected, new NodeUtil.Visitor<Void>() {
      @Override
      public Void visit(Node node) {
        if (node.getType() == Token.VAR) {
          expectedVars.put(node.getString(), node);
        }
        return null;
      }
    });

    for (Map.Entry<String, Node> entry : expectedVars.entrySet()) {
      Node expectedVar = entry.getValue();
      Node actualVar = actualVars.get(entry.getKey());
      assertEquals(expectedVar.getFirstChild().getString(),
          actualVar.getFirstChild().getString());
    }
    assertEquals(expected, actual);
  }
}