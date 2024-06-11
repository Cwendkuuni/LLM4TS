package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap;
import com.google.javascript.rhino.Node;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    // Test case for an object literal that is only assigned and never used in full.
    String source = "function foo() {\n" +
        "  var obj = {\n" +
        "    a: 1,\n" +
        "    b: 2,\n" +
        "    c: 3\n" +
        "  };\n" +
        "  var x = obj.a;\n" +
        "  var y = obj.b;\n" +
        "}\n";

    Node root = AbstractCompiler.parse(source, null);
    InlineObjectLiterals pass = new InlineObjectLiterals(
        new CompilerPassAdapter(), NodeUtil::newNameIdSupplier);
    pass.process(null, root);

    // Check if the object literal has been split into individual variables.
    Iterator<Node> it = NodeTraversal.depthFirst(root);
    Node objVar = null;
    Node varA = null;
    Node varB = null;
    Node varC = null;
    while (it.hasNext()) {
      Node node = it.next();
      if (node.isVar()) {
        if (node.getName().equals("obj")) {
          objVar = node;
        } else if (node.getName().equals("a")) {
          varA = node;
        } else if (node.getName().equals("b")) {
          varB = node;
        } else if (node.getName().equals("c")) {
          varC = node;
        }
      }
    }
    assertTrue("obj variable should exist", objVar != null);
    assertTrue("a variable should exist", varA != null);
    assertTrue("b variable should exist", varB != null);
    assertTrue("c variable should exist", varC != null);

    // Check if the object literal has been removed.
    assertNull("obj variable should be removed", NodeTraversal.findNode(root, "obj"));

    // Test case for a nested object literal that is only assigned and never used in full.
    source = "function foo() {\n" +
        "  var obj = {\n" +
        "    a: {\n" +
        "      b: 1,\n" +
        "      c: 2\n" +
        "    },\n" +
        "    d: 3\n" +
        "  };\n" +
        "  var x = obj.a.b;\n" +
        "  var y = obj.a.c;\n" +
        "}\n";

    root = AbstractCompiler.parse(source, null);
    pass.process(null, root);

    // Check if the nested object literal has been split into individual variables.
    it = NodeTraversal.depthFirst(root);
    Node objVarNested = null;
    Node varBBasedOnObj = null;
    Node varBC = null;
    while (it.hasNext()) {
      Node node = it.next();
      if (node.isVar()) {
        if (node.getName().equals("obj")) {
          objVarNested = node;
        } else if (node.getName().startsWith("JSCompiler_object_inline_")) {
          String name = node.getName().substring(22);
          if (name.equals("a_b")) {
            varBBasedOnObj = node;
          } else if (name.equals("a_c")) {
            varBC = node;
          }
        }
      }
    }
    assertTrue("obj variable should exist", objVarNested != null);
    assertTrue("a_b variable should exist", varBBasedOnObj != null);
    assertTrue("a_c variable should exist", varBC != null);

    // Check if the nested object literal has been removed.
    assertNull("obj variable should be removed", NodeTraversal.findNode(root, "obj"));

    // Test case for an object literal that is used in full.
    source = "function foo() {\n" +
        "  var obj = {\n" +
        "    a: 1,\n" +
        "    b: {\n" +
        "      c: 1,\n" +
        "      d: 2\n" +
        "    }\n" +
        "  };\n" +
        "  function bar() {\n" +
        "    return obj;\n" +
        "  }\n" +
        "}\n";

    root = AbstractCompiler.parse(source, null);    pass.process(null, root);

    // Check if the object literal has not been split into individual variables.
    assertNull("obj variable should not be removed", NodeTraversal.findNode(root, "obj"));
  }

  @Test
  public void testInlineObjectLiteralsWithSelfReference() {
    // Test case for an object literal with self-reference.
    String source = "function foo() {\n" +
        "  var obj = {\n" +
        "    a: {\n" +
        "      b: obj\n" +
        "    }\n" +
        "  };\n" +
        "}\n";

    Node root = AbstractCompiler.parse(source, null);
    InlineObjectLiterals pass = new InlineObjectLiterals(
        new CompilerPassAdapter(), NodeUtil::newNameIdSupplier);
    pass.process(null, root);

    // Object literals with self-references cannot be inlined.
    Node obj = NodeTraversal.findNode(root, "obj");
    assertTrue("obj should still exist", obj != null);
  }

  @Test
  public void testInlineObjectLiteralsWithNonObjectLiteralReference() {
    // Test case for an object literal with a non-object literal reference.
    String source = "function foo() {\n" +
        "  var obj = {\n" +
        "    a: 1,\n" +
        "    b: 2\n" +
        "  };\n" +
        "  var x = 3;\n" +
        "  var y = obj.x;\n" +
        "}\n";

    Node root = AbstractCompiler.parse(source, null);
    InlineObjectLiterals pass = new InlineObjectLiterals(
        new CompilerPassAdapter(), NodeUtil::newNameIdSupplier);
    pass.process(null, root);

    // Object literals cannot be inlined when they have non-object literal references.
    Node obj = NodeTraversal.findNode(root, "obj");
    assertTrue("obj should still exist", obj != null);
  }
}