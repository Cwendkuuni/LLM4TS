package com.google.javascript.jscomp;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.GraphBuilder;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InlineObjectLiteralsTest extends TestCase {

  public void testInlineObjectLiterals() {
    String jsCode = "function f(o) {\n" +
            "  var obj = {\n" +
            "    a: 1,\n" +
            "    b: function() {\n" +
            "      return 'hello';\n" +
            "    },\n" +
            "    c: o\n" +
            "  };\n" +
            "  var x = obj.a;\n" +
            "  var y = obj.b();\n" +
            "  var z = obj.c;\n" +
            "  return [x, y, z];\n" +
            "}";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();

    // Add the test class as the source file
    List<String> sources = new ArrayList<>();
    sources.add("InlineObjectLiteralsTest.java");
    sources.add(jsCode);

    CompilerPassManager passManager = new CompilerPassManager(compiler, options);
    passManager.addPass(new InlineObjectLiterals(compiler, () -> "JSCompiler_inline_object_"));
    passManager.addPass(GraphBuilder.class);

    Node externalRoot = compiler.parseAndCompileForward(sources, null);
    assertNotNull(externalRoot);

    Node root = externalRoot.getFirstChild();
    Node function = NodeUtil.findFunction(root, "f");
    assertNotNull(function);

    // Test the original code
    Node functionResult = NodeUtil.runFunction(compiler, root, "f", null, null);
    assertNotNull(functionResult);
    List<Node> functionResultList = NodeUtil.asList(functionResult);
    assertEquals(3, functionResultList.size());

    // Test the inlined code
    CompilerPassManager passManagerForTest = new CompilerPassManager(compiler, options);
    passManagerForTest.addPass(new InlineObjectLiterals(compiler, () -> "JSCompiler_inline_object_test_"));
    passManagerForTest.addPass(GraphBuilder.class);

    Node externalRootForTest = compiler.parseAndCompileForward(sources, null);
    assertNotNull(externalRootForTest);

    Node rootForTest = externalRootForTest.getFirstChild();
    Node functionForTest = NodeUtil.findFunction(rootForTest, "f");
    assertNotNull(functionForTest);

    Node functionResultForTest = NodeUtil.runFunction(compiler, rootForTest, "f", null, null);
    assertNotNull(functionResultForTest);
    List<Node> functionResultListForTest = NodeUtil.asList(functionResultForTest);
    assertEquals(3, functionResultListForTest.size());

    // Test that the inlined variables are correctly named and used
    for (int i = 0; i < 3; i++) {
      Node node = functionResultListForTest.get(i);
      if (i == 0) {
        assertEquals("JSCompiler_inline_object_test_a", node.getString());
      } else if (i == 1) {
        assertEquals("JSCompiler_inline_object_test_b", node.getString());
        assertEquals("hello", NodeUtil.runFunction(compiler, node, null, null).getString());
      } else {
        // The last value should be the original 'o' parameter
        assertEquals("o", node.getString());
      }
    }
  }

  private static NodeUtil nodeUtil
      = new NodeUtil();
  private static Function<Node, Node> runFunction = new Function<Node, Node>() {
    @Override
    public Node apply(Node node) {
      return NodeUtil.runFunction(compiler, node, null, null);
    }
  };

  private static Function<Node, String> asList = new Function<Node, List<Node>>() {
    @Override
    public List<Node> apply(Node node) {
      return NodeUtil.asList(node);
    }
  };
}

class NodeUtil {
  static Node findFunction(Node node, String name) {
    if (node.isFunction()) {
      Node f = node;
      if (f.getName().getString().equals(name)) {
        return f;
      }
    }
    for (Node child = node.getFirstChild(); child != null;
         child = child.getNext()) {
      Node result = findFunction(child, name);
      if (result != null) {
        return result;
      }
    }
    return null;
  }

  static Node runFunction(Compiler compiler, Node node, String functionName,
                         Object parameters, Object context) {
    Node function = NodeUtil.findFunction(node, functionName);
    return compiler.callFunction(function, NodeUtil.asList(parameters), context);
  }

  static List<Node> asList(Node node) {
    if (node.isList()) {
      return node.getChildren();
    }
    List<Node> list = new ArrayList<>();
    list.add(node);
    return list;
  }

  static Node newVarNode(String name, Node value) {
    return new Node(Token.VAR, null, null, null, null, null, name, null, null, null, value);
  }

  static Node newUndefinedNode(Node parent) {
    return new Node(Token.UNDEFINED, null, null, null, null, null, null, parent);
  }

  static Node newExpr(Node node) {
    return new Node(Token.EXPR_STATEMENT, null, node);
  }

  static Node newStmt(Node node) {
    return new Node(Token.STATEMENT, null, node);
  }
}