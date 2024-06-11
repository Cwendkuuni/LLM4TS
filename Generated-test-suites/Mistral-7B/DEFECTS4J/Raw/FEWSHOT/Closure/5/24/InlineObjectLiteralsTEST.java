package com.google.javascript.jscomp;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.test.NodeMatcher;
import com.google.javascript.jscomp.test.TestCompiler;
import com.google.javascript.rhino.Token;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() throws Exception {
    String jsSource =
        "function foo() {" +
        "  var obj = {" +
        "    x: 1," +
        "    y: function() {" +
        "      return 2;" +
        "    }," +
        "    z: [3, 4]," +
        "  };" +
        "  var a = obj.x;" +
        "  var b = obj.y();" +
        "  var c = obj.z[0];" +
        "  var d = obj.z[1];" +
        "}" +
        "function bar() {" +
        "  var obj = {" +
        "    x: 1," +
        "    y: 1," +
        "    z: {" +
        "      a: 1," +
        "      b: 2" +
        "    }," +
        "  };" +
        "  var a = obj.x;" +
        "  var b = obj.y;" +
        "  var c = obj.z.a;" +
        "  var d = obj.z.b;" +
        "}" +
        "function baz() {" +
        "  var obj = {" +
        "    x: 1," +
        "    y: 1," +
        "  };" +
        "  var a = obj.x;" +
        "  var b = obj.y;" +
        "}" +
        "function qux() {" +
        "  var obj = {" +
        "    x: 1," +
        "  };" +
        "  var a = obj.x;" +
        "}" +
        "function quux() {" +
        "  var obj = {};" +
        "  var a = obj.x;" +
        "}";

    TestCompiler compiler = new TestCompiler();
    compiler.addPass(new InlineObjectLiterals(compiler, new DefaultNameIdFunction()));
    Node root = compiler.compile(jsSource);

    Scope globalScope = root.getFirstChild();

    assertEquals(4, globalScope.size());

    Node foo = NodeMatcher.findFunctionDeclaration(root, "foo");
    Node bar = NodeMatcher.findFunctionDeclaration(root, "bar");
    Node baz = NodeMatcher.findFunctionDeclaration(root, "baz");
    Node qux = NodeMatcher.findFunctionDeclaration(root, "qux");
    Node quuz = NodeMatcher.findFunctionDeclaration(root, "quuz"); // not tested

    // foo
    Node fooObj = NodeMatcher.findVariableDeclaration(foo, "obj").getInitializer();
    assertEquals(3, fooObj.getChildCount());
    Node fooX = NodeMatcher.findVariableDeclaration(foo, "a").getInitializer();
    Node fooY = NodeMatcher.findCall(foo, "y");
    Node fooYCall = fooY.getFirstChild();
    Node fooZ = NodeMatcher.findVariableDeclaration(foo, "c").getInitializer();
    Node fooZFirst = NodeMatcher.findVariableDeclaration(foo, "d").getInitializer();

    Map<String, Node> expectedFoo = ImmutableMap.of(
        "obj.x", fooX,
        "obj.y.call(obj)", fooYCall,
        "obj.z[0]", fooZ,
        "obj.z[1]", fooZFirst
    );

    Map<String, Node> actualFoo = Maps.newLinkedHashMap();
    for (Node child = fooObj.getFirstChild(); child != null; child = child.getNext()) {
      String key = child.getString();
      if (key.endsWith(":")) {
        Node value = child.removeFirstChild();
        actualFoo.put(key, value);
      }
    }

    assertMapEquals(expectedFoo, actualFoo);

    // bar
    Node barObj = NodeMatcher.findVariableDeclaration(bar, "obj").getInitializer();
    assertEquals(4, barObj.getChildCount());
    Node barX = NodeMatcher.findVariableDeclaration(bar, "a").getInitializer();
    Node barY = NodeMatcher.findVariableDeclaration(bar, "b").getInitializer();
    Node barZ = NodeMatcher.findVariableDeclaration(bar, "c").getInitializer();
    Node barZA = NodeMatcher.findVariableDeclaration(bar, "d").getInitializer();

    Map<String, Node> expectedBar = ImmutableMap.of(
        "obj.x", barX,
        "obj.y", barY,
        "obj.z.a", barZA
    );

    Map<String, Node> actualBar = Maps.newLinkedHashMap();
    for (Node child = barObj.getFirstChild(); child != null; child = child.getNext()) {
      String key = child.getString();
      if (key.endsWith(":")) {
        Node value = child.removeFirstChild();
        actualBar.put(key, value);
      }
    }

    assertMapEquals(expectedBar, actualBar);

    // baz
    Node bazObj = NodeMatcher.findVariableDeclaration(baz, "obj").getInitializer();
    assertEquals(2, bazObj.getChildCount());
    Node bazX = NodeMatcher.findVariableDeclaration(baz, "a").getInitializer();
    Node bazY = NodeMatcher.findVariableDeclaration(baz, "b").getInitializer();

    Map<String, Node> expectedBaz = ImmutableMap.of(
        "obj.x", bazX,
        "obj.y", bazY
    );

    Map<String, Node> actualBaz = Maps.newLinkedHashMap();
    for (Node child = bazObj.getFirstChild(); child != null; child = child.getNext()) {
      String key = child.getString();
      if (key.endsWith(":")) {
        Node value = child.removeFirstChild();
        actualBaz.put(key, value);
      }
    }

    assertMapEquals(expectedBaz, actualBaz);

    // qux
    Node quxObj = NodeMatcher.findVariableDeclaration(qux, "obj").getInitializer();
    assertEquals(1, quxObj.getChildCount());
    Node quxX = NodeMatcher.findVariableDeclaration(qux, "a").getInitializer();

    Map<String, Node> expectedQux = ImmutableMap.of(
        "obj.x", quxX
    );

    Map<String, Node> actualQux = Maps.newLinkedHashMap();
    for (Node child = quxObj.getFirstChild(); child != null; child = child.getNext()) {
      String key = child.getString();
      if (key.endsWith(":")) {
        Node value = child.removeFirstChild();
        actualQux.put(key, value);
      }
    }

    assertMapEquals(expectedQux, actualQux);

    // quuz
    // not tested

    // nothing should have been added to the global scope
    assertFalse(NodeMatcher.findVariableDeclaration(globalScope, "JSCompiler_object_inline_x"));
    assertFalse(NodeMatcher.findVariableDeclaration(globalScope, "JSCompiler_object_inline_y"));
    assertFalse(NodeMatcher.findVariableDeclaration(globalScope, "JSCompiler_object_inline_z"));
    assertFalse(NodeMatcher.findVariableDeclaration(globalScope, "JSCompiler_object_inline_a"));
    assertFalse(NodeMatcher.findVariableDeclaration(globalScope, "JSCompiler_object_inline_b"));
  }

  private static void assertMapEquals(Map<String, Node> expected, Map<String, Node> actual) {
    assertEquals(expected.size(), actual.size());
    for (Map.Entry<String, Node> e : expected.entrySet()) {
      String key = e.getKey();
      Node expectedNode = e.getValue();
      Node actualNode = actual.get(key);
      assertEquals(expectedNode, actualNode);
    }
  }
}