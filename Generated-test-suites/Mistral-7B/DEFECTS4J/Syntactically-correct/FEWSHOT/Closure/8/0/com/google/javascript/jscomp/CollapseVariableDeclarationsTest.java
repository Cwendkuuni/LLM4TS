package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.GraphBuilder;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RunWith(JUnit4.class)
public class CollapseVariableDeclarationsTest {

  @Test
  public void testCollapseVariableDeclarations() {
    Compiler compiler = new Compiler();
    GraphBuilder graphBuilder = new GraphBuilder(compiler);
    Node root = NodeFactory.parse("function foo() {"
        + "var a;"
        + "var b = 1;"
        + "var c = 2;"
        + "var d = 3;"
        + "var e;"
        + "var f = 4;"
        + "var g = 5;"
        + "var h;"
        + "var i = 6;"
        + "var j = 7;"
        + "var k = 8;"
        + "var l;"
        + "var m = 9;"
        + "var n = 10;"
        + "var o = 11;"
        + "var p;"
        + "var q = 12;"
        + "var r = 13;"
        + "var s;"
        + "var t = 14;"
        + "var u = 15;"
        + "var v = 16;"
        + "var w = 17;"
        + "var x;"
        + "var y = 18;"
        + "var z = 19;"
        + "}");

    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(graphBuilder.getExterns(), root);

    Node fooFuncNode = graphBuilder.findFunctionByName("foo");
    List<Node> fooFuncBodyNodes = new ArrayList<>();
    Node currentNode = fooFuncNode;
    while (currentNode != null) {
      fooFuncBodyNodes.add(currentNode);
      currentNode = currentNode.getNext();
    }

    int index = 0;
    for (Node node : fooFuncBodyNodes) {
      if (node.isVar()) {
        Var var = compiler.getScope().getVar(node.getString());
        int expectedIndex = index;
        if (++index > 1) {
          Var previousVar = compiler.getScope().getVar(fooFuncBodyNodes.get(index - 1).getString());
          expectedIndex = previousVar.getIndex() + 1;
        }
        Node collapsedNode = node.getParent();
        Var collapsedVar = compiler.getScope().getVar(collapsedNode.getString());
        assertThat(collapsedVar.getIndex()).isEqualTo(expectedIndex);
      }
      index++;
    }
  }

  @Test
  public void testCollapseAssignments() {
    Compiler compiler = new Compiler();
    GraphBuilder graphBuilder = new GraphBuilder(compiler);
    Node root = NodeFactory.parse("function foo() {"
        + "var a = true;"
        + "var b = true;"
        + "var c = true;"
        + "}");

    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(graphBuilder.getExterns(), root);

    Node fooFuncNode = graphBuilder.findFunctionByName("foo");
    Node aAssignment = fooFuncNode.getChildAtIndex(0);
    Node bAssignment = fooFuncNode.getChildAtIndex(1);
    Node cAssignment = fooFuncNode.getChildAtIndex(2);

    Node collapsedNode = aAssignment.getParent();
    Node collapsedNodeB = bAssignment.getParent();
    Node collapsedNodeC = cAssignment.getParent();

    assertThat(collapsedNode).isEqualTo(collapsedNodeB);
    assertThat(collapsedNode).isEqualTo(collapsedNodeC);

    int aIndex = collapsedNode.getIndex();
    int bIndex = aAssignment.getIndex();
    int cIndex = cAssignment.getIndex();

    assertThat(bIndex).isLessThan(aIndex);
    assertThat(cIndex).isLessThan(aIndex);

    Var aVar = compiler.getScope().getVar(NodeFactory.stringLiteral("a"));
    Var bVar = compiler.getScope().getVar(NodeFactory.stringLiteral("b"));
    Var cVar = compiler.getScope().getVar(NodeFactory.stringLiteral("c"));

    assertThat(aVar.getIndex()).isEqualTo(aIndex);
    assertThat(bVar.getIndex()).isEqualTo(bIndex);
    assertThat(cVar.getIndex()).isEqualTo(cIndex);
  }

  @Test
  public void testNullInput() {
    Compiler compiler = new Compiler();
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    assertNull(pass.process(null, null));
  }

  @Test
  public void testEmptyInput() {
    Compiler compiler = new Compiler();
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    assertNull(pass.process(NodeFactory.nullNode(), NodeFactory.nullNode()));
  }
}