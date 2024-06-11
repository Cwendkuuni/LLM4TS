package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.RenameVars;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.VariableMap;
import com.google.javascript.rhino.Token;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class RenameVarsTest {

  private AbstractCompiler compiler;
  private RenameVars renameVars;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler() {};
    renameVars = new RenameVars(compiler, null, false, false, false, null, null, null);
  }

  @Test
  public void testProcess() {
    String code = "function Foo(a, b) {\n" +
        "  var c = function(d, e) { return a; }\n" +
        "}\n";

    Node root = NodeUtil.parse(code);
    renameVars.process(NodeUtil.getExterns(root), root);
    NodeTraversal.traverse(compiler, root, new NodePrintingCallback());
    String result = compiler.getSource();

    // Expected output:
    // function x(a,b) {
    //   var y = function(d,e) { return a; }
    // }
    String expected = "function x(a,b) {\n" +
        "  var y = function(d,e) { return a; }\n" +
        "}";
    assertEquals(expected, result);
  }

  @Test
  public void testProcessWithPrefix() {
    String code = "function Foo(a, b) {\n" +
        "  var c = function(d, e) { return a; }\n" +
        "}";

    renameVars = new RenameVars(compiler, "foo_", false, false, false, null, null, null);
    Node root = NodeUtil.parse(code);
    renameVars.process(NodeUtil.getExterns(root), root);
    NodeTraversal.traverse(compiler, root, new NodePrintingCallback());
    String result = compiler.getSource();

    // Expected output:
    // function foo_Foo(a,b) {
    //   var foo_c = function(d,e) { return a; }
    // }
    String expected = "function foo_Foo(a,b) {\n" +
        "  var foo_c = function(d,e) { return a; }\n" +
        "}";
    assertEquals(expected, result);
  }

  @Test
  public void testProcessWithLocalRenamingOnly() {
    String code = "function Foo(a, b) {\n" +
        "  var c = function(d, e) { return a; }\n" +
        "  var x = 42;\n" +
        "}\n";

    renameVars = new RenameVars(compiler, null, true, false, false, null, null, null);
    Node root = NodeUtil.parse(code);
    renameVars.process(NodeUtil.getExterns(root), root);
    NodeTraversal.traverse(compiler, root, new NodePrintingCallback());
    String result = compiler.getSource();

    // Expected output:
    // function Foo(a,b) {
    //   var L0 = function(d,e) { return a; };
    //   var L1 = 42;
    // }
    String expected = "function Foo(a,b) {\n" +
        "  var L0 = function(d,e) { return a; };\n" +
        "  var L1 = 42;\n" +
        "}";
    assertEquals(expected, result);
  }

  @Test
  public void testProcessWithPreserveAnonymousFunctionNames() {
    String code = "function Foo(a, b) {\n" +
        "  var c = function(d, e) {\n" +
        "    return a;\n" +
        "  };\n" +
        "}\n";

    renameVars = new RenameVars(compiler, null, false, true, false, null, null, null);
    Node root = NodeUtil.parse(code);
    renameVars.process(NodeUtil.getExterns(root), root);
    NodeTraversal.traverse(compiler, root, new NodePrintingCallback());
    String result = compiler.getSource();

    // Expected output:
    // function Foo(a,b) {
    //   var anonymous_function = function(d,e) { return a; };
    // }
    String expected = "function Foo(a,b) {\n" +
        "  var anonymous_function = function(d,e) { return a; };\n" +
        "}";
    assertEquals(expected, result);
  }

  @Test
  public void testProcessWithGeneratePseudoNames() {
    String code = "function Foo(a, b) {\n" +
        "  var c = a;\n" +
        "  var d = b;\n" +
        "}";

    renameVars = new RenameVars(compiler, null, false, false, true, null, null, null);
    Node root = NodeUtil.parse(code);
    renameVars.process(NodeUtil.getExterns(root), root);
    NodeTraversal.traverse(compiler, root, new NodePrintingCallback());
    String result = compiler.getSource();

    // Expected output:
    // function Foo(a,b) {
    //   var $0 = a;
    //   var $1 = b;
    // }
    String expected = "function Foo(a,b) {\n" +
        "  var $0 = a;\n" +
        "  var $1 = b;\n" +
        "}";
    assertEquals(expected, result);
  }

  @Test
  public void testProcessWithReservedCharacters() {
    String code = "function Foo(a$b) {\n" +
        "  var c = function(d$e) { return a$b; }\n" +
        "}";

    char[] reservedCharacters = {'$'};
    renameVars = new RenameVars(compiler, null, false, false, false, null, reservedCharacters, null);
    Node root = NodeUtil.parse(code);
    renameVars.process(NodeUtil.getExterns(root), root);
    NodeTraversal.traverse(compiler, root, new NodePrintingCallback());
    String result = compiler.getSource();

    // Expected output:
    // function Foo(a_b) {
    //   var L0 = function(d_e) { return a_b; }
    // }
    String expected = "function Foo(a_b) {\n" +
        "  var L0 = function(d_e) { return a_b; }\n" +
        "}";
    assertEquals(expected, result);
  }

  @Test
  public void testProcessWithReservedNames() {
    String code = "function foo(bar) {\n" +
        "  var qux = 42;\n" +
        "}";

    Set<String> reservedNames = Sets.newHashSet("qux");
    renameVars = new RenameVars(compiler, null, false, false, false, null, null, reservedNames);
    Node root = NodeUtil.parse(code);
    renameVars.process(NodeUtil.getExterns(root), root);
    NodeTraversal.traverse(compiler, root, new NodePrintingCallback());
    String result = compiler.getSource();

    // Expected output:
    // function foo(bar) {
    //   var L0 = 42;
    // }
    String expected = "function foo(bar) {\n" +
        "  var L0 = 42;\n" +
        "}";
    assertEquals(expected, result);
  }

  @Test
  public void testProcessWithExterns() {
    String code = "function Foo(a, b) {\n" +
        "  var c = a;\n" +
        "}";

    Node externs = NodeUtil.parse("var a = 42, b = 43;");
    renameVars = new RenameVars(compiler, null, false, false, false, null, null, null);
    renameVars.process(externs, NodeUtil.parse(code));
    NodeTraversal.traverse(compiler, NodeUtil.parse(code), new NodePrintingCallback());
    String result = compiler.getSource();

    // Expected output:
    // function Foo(a,b) {
    //   var L0 = a;
    // }
    String expected = "function Foo(a,b) {\n" +
        "  var L0 = a;\n" +
        "}";
    assertEquals(expected, result);
  }

  @Test
  public void testProcessWithPrevUsedRenameMap() {
    String code1 = "function Foo(a, b) {\n" +
        "  var c = function(d, e) { return a; }\n" +
        "}\n";

    String code2 = "function Foo(a, b) {\n" +
        "  var d = 42;\n" +
        "}\n";

    Map<String, String> prevUsedRenameMap = Maps.newHashMap();
    prevUsedRenameMap.putAll(Maps.newHashMap(renameVars.getVariableMap().asMap()));

    Node root1 = NodeUtil.parse(code1);
    Node root2 = NodeUtil.parse(code2);
    renameVars = new RenameVars(compiler, null, false, false, false, prevUsedRenameMap, null, null);
    renameVars.process(NodeUtil.getExterns(root1), root1);
    renameVars.process(NodeUtil.getExterns(root2), root2);

    // Expected output for root1:
    // function Foo(a,b) {
    //   var L0 = function(d,e) { return a; }
    // }
    String expectedRoot1 = "function Foo(a,b) {\n" +
        "  var L0 = function(d,e) { return a; }\n" +
        "}";

    // Expected output for root2:
    // function Foo(a,b) {
    //   var L1 = 42;
    // }
    String expectedRoot2 = "function Foo(a,b) {\n" +
        "  var L1 = 42;\n" +
        "}";

    NodeTraversal.traverse(compiler, root1, new NodePrintingCallback());
    String resultRoot1 = compiler.getSource();
    assertEquals(expectedRoot1, resultRoot1);

    NodeTraversal.traverse(compiler, root2, new NodePrintingCallback());
    String resultRoot2 = compiler.getSource();
    assertEquals(expectedRoot2, resultRoot2);

    Map<String, String> newRenameMap = renameVars.getVariableMap();
    assertEquals(2, newRenameMap.size());
    assertEquals("L0", newRenameMap.get("c"));
    assertEquals("L1", newRenameMap.get("d"));
  }
}

class NodePrintingCallback extends AbstractPostOrderCallback {
  @Override
  public void visit(NodeTraversal t, Node n, Node parent) {
    if (n.getType() != Token.NAME && n.getType() != Token.BLOCK) {
      return;
    }

    if (parent == null) {
      System.out.print("function " + n.getString() + "(");
    } else {
      System.out.print(n.getString());
    }

    if (n.getType() != Token.NAME) {
      return;
    }

    List<Node> children = NodeUtil.getChildren(n);
    if (children.size() > 0 && children.get(children.size() - 1).getType() != Token.PUNCTUATION) {
      System.out.print(", ");
    }

    if (n.getType() == Token.BLOCK) {
      System.out.println("{\n");
      NodeTraversal.traverse(compiler, n, this);
      System.out.println("}");
    }
  }
}