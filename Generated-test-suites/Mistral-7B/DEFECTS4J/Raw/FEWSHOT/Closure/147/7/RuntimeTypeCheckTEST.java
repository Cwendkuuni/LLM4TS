package com.google.javascript.jscomp;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RuntimeTypeCheck;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.UnionType;

import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

@RunWith(JUnit4.class)
public class RuntimeTypeCheckTest {

  private Node prepareNodeForTesting() {
    NodeFactory factory = new NodeFactory();
    Node root = factory.parseTree(
        "function Foo() {} function Bar() {}",
        "Foo.prototype.test = function() { return 'foo'; };",
        "Bar.prototype.test = function() { return 'bar'; };",
        "function check() { return Foo.prototype.test(); }");

    return root;
  }

  @Test
  public void testAddMarkers() {
    RuntimeTypeCheck checker = new RuntimeTypeCheck(null, null);
    Node root = prepareNodeForTesting();
    checker.process(null, root);

    TreeSet<String> expected = new TreeSet<String>(ALPHA);
    expected.addAll(ImmutableList.of(
        "Foo",
        "Foo.prototype"));

    TreeSet<String> actual = new TreeSet<String>();

    NodeTraversal.traverse(null, root, new NodeTraversal.AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.GETPROP && n.getFirstChild().getFirstChild().getFirstChild().getStringValue().equals("prototype")) {
          String className = NodeUtil.getFunctionName(n.getParent().getJSType().getSource());
          actual.add(className);
        }
      }
    });

    assertTrue(expected.containsAll(actual));
  }

  @Test
  public void testAddChecks() {
    RuntimeTypeCheck checker = new RuntimeTypeCheck(null, null);
    Node root = prepareNodeForTesting();
    checker.process(null, root);

    Node functionNode = NodeUtil.getFunctionDeclarationNode(root, "Foo");
    Node checkNode = NodeUtil.getChildByFunctionName(functionNode, "checkType");

    FunctionType functionType = (FunctionType) functionNode.getJSType();
    JSType returnType = functionType.getReturnType();

    TreeSet<String> expected = new TreeSet<String>(ALPHA);
    expected.addAll(ImmutableList.of(
        "null",
        "string",
        "Foo"));

    TreeSet<String> actual = new TreeSet<String>();

    NodeTraversal.traverse(null, checkNode, new NodeTraversal.AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.CALL && n.getFirstChild().getStringValue().equals("checkType")) {
          Node checkerNode = n.getLastChild();
          if (checkerNode.getType() == Token.ARRAYLIT) {
            for (Node checkerChild : checkerNode.getChildren()) {
              if (checkerChild.getType() == Token.CALL) {
                String checkerTypeName = checkerChild.getFirstChild().getStringValue();
                actual.add(checkerTypeName);
              }
            }
          }
        }
      }
    });

    assertTrue(expected.containsAll(actual));
  }

  @Test
  public void testAddBoilerplateCode() {
    RuntimeTypeCheck checker = new RuntimeTypeCheck(null, null);
    Node root = prepareNodeForTesting();
    checker.process(null, root);

    Node jsCodeNode = NodeUtil.getChildByFunctionName(root, "jscomp_runtimeTypeCheck_");
    assertTrue(jsCodeNode != null);
    assertTrue(jsCodeNode.getLastChild().getLastChild().getLastChild().getLastChild().getLastChild().getLastChild().getStringValue().contains("checkType"));
  }
}