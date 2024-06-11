package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.NodeVisitor;
import com.google.javascript.jscomp.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.ReferenceMap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.Node;

@RunWith(JUnit4.class)
public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    // Create a simple object literal and test if it is inlined
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setPrintIntermediateFileNames(false);
    options.setWarningLevel("QUIET");

    String input = "function foo() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: 2," +
        "    c: {d: 3}" +
        "  };" +
        "  var x = obj.a;" +
        "  var y = obj.b;" +
        "  var z = obj.c.d;" +
        "  return x + y + z;" +
        "}";

    Node root = NodePrinter.parse(input);

    CompilerPass pass = new InlineObjectLiterals(compiler, new SimpleNameIdSupplier());
    pass.process(null, root);

    Traversal traversal = new Traversal(root);
    NodeVisitor visitor = new NodeVisitor() {
      @Override
      public void visit(Node node) {
        if (node.isName()) {
          String name = node.getString();
          if (name.startsWith("JSCompiler_object_inline_")) {
            System.out.println("Found inlined variable: " + name);
            assertNotNull(traversal.getScope().getVar(name));
          }
        }
      }
    };

    NodeUtil.visitPostOrder(root, visitor);

    Scope scope = traversal.getScope();
    ReferenceCollection refColl = new ReferenceCollectingCallback(compiler, null).process(null, root);

    Map<String, Node> expectedVars = Maps.newLinkedHashMap();
    expectedVars.put("a", NodeUtil.newNumberNode(1));
    expectedVars.put("b", NodeUtil.newNumberNode(2));
    expectedVars.put("d", NodeUtil.newNumberNode(3));
    expectedVars.put("JSCompiler_object_inline_a", NodeUtil.newVarNode("a", Lists.newArrayList(NodeUtil.newNumberNode(1))));
    expectedVars.put("JSCompiler_object_inline_b", NodeUtil.newVarNode("b", Lists.newArrayList(NodeUtil.newNumberNode(2))));
    expectedVars.put("JSCompiler_object_inline_d", NodeUtil.newVarNode("d", Lists.newArrayList(NodeUtil.newNumberNode(3))));

    for (Reference ref : refColl.getReferences(scope.getVar("obj"))) {
      Node refNode = ref.getNode();
      String name = refNode.getString();
      Node varNode = expectedVars.get(name);
      if (varNode == null) {
        throw new AssertionError("Unexpected reference to variable: " + name);
      }
      NodeUtil.assertNodesEqual(varNode, refNode);
    }

    assertEquals("function foo() {" +
        "  var a = 1;" +
        "  var b = 2;" +
        "  var d = 3;" +
        "  return a + b + d;" +
        "}", NodePrinter.printNode(root));
  }
}

class SimpleNameIdSupplier implements Supplier<String> {
  private int counter = 0;

  @Override
  public String get() {
    return "SimpleNameId_" + counter++;
  }
}