package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

@RunWith(JUnit4.class)
public class RuntimeTypeCheckTest {

  @Test
  public void testAddMarkers() {
    NodeFactory nodeFactory = new NodeFactory();
    Node root = nodeFactory.parse("function Foo() {}");

    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(null, null);
    runtimeTypeCheck.process(null, root);

    Node fooPrototype = findPrototype(root, "Foo");
    assertNotNull(findMarker(fooPrototype, "instance_of__Foo"));
  }

  @Test
  public void testAddMarkersForInterface() {
    NodeFactory nodeFactory = new NodeFactory();
    Node root = nodeFactory.parse("interface Bar {} function Foo() implements Bar {}");

    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(null, null);
    runtimeTypeCheck.process(null, root);

    Node fooPrototype = findPrototype(root, "Foo");
    assertNotNull(findMarker(fooPrototype, "implements__Bar"));
  }

  @Test
  public void testAddChecksForFunction() {
    NodeFactory nodeFactory = new NodeFactory();
    Node root = nodeFactory.parse("function Foo(a: number, b: string) {}");

    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(null, null);
    runtimeTypeCheck.process(null, root);

    Node foo = findPrototype(root, "Foo");
    Node block = foo.getLastChild();

    Node aChecker = findFirstChecker(block, "number");
    assertNotNull(aChecker);

    Node bChecker = findFirstChecker(block, "string");
    assertNotNull(bChecker);
  }

  @Test
  public void testAddChecksForReturnValue() {
    NodeFactory nodeFactory = new NodeFactory();
    Node root = nodeFactory.parse("function Foo() { return 42; }");

    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(null, null);
    runtimeTypeCheck.process(null, root);

    Node foo = findPrototype(root, "Foo");
    Node returnNode = findReturnNode(foo);

    Node checkNode = findChecker(returnNode, "number");
    assertNotNull(checkNode);
  }

  private Node findPrototype(Node root, String className) {
    Node prototypeNode = null;
    for (Node child = root.getFirstChild(); child != null;
         child = child.getNext()) {
      if (NodeUtil.isFunction(child) && NodeUtil.getFunctionName(child)
          .equals(className)) {
        prototypeNode = child;
        break;
      }
    }
    return prototypeNode;
  }

  private Node findMarker(Node node, String markerName) {
    for (Node child = node.getFirstChild(); child != null;
         child = child.getNext()) {
      if (child.getType() != Token.GETELEM ||
          !child.getFirstChild().getType()
              .equals(Token.GETPROP) ||
          !child.getFirstChild().getFirstChild()
              .toString().equals("prototype")) {
        continue;
      }

      Node marker = child.getNext();
      if (marker.getType() != Token.IDENT ||
          !marker.toString().equals(markerName)) {
        continue;
      }

      return marker;
    }
    return null;
  }

  private Node findFirstChecker(Node block, String typeName) {
    for (Node child = block.getFirstChild(); child != null;
         child = child.getNext()) {
      if (child.getType() == Token.CALL &&
          child.getFirstChild().toString().equals("checkType")) {
        Node arrayNode = child.getLastChild();
        for (Node checkerNode = arrayNode.getFirstChild();
             checkerNode != null;
             checkerNode = checkerNode.getNext()) {
          if (checkerNode.getType() == Token.CALL &&
              checkerNode.getFirstChild().toString().equals("valueChecker") &&
              checkerNode.getLastChild() != null &&
              checkerNode.getLastChild().getType() == Token.STRING &&
              checkerNode.getLastChild().toString().equals(typeName)) {
            return checkerNode;
          }
        }
      }
    }
    return null;
  }

  private Node findReturnNode(Node node) {
    if (node.getType() == Token.RETURN) {
      return node;
    }
    for (Node child = node.getFirstChild(); child != null;
         child = child.getNext()) {
      if (child.getType() == Token.RETURN) {
        return child;
      }
    }
    return null;
  }

  private Node findChecker(Node node, String typeName) {
    for (Node child = node.getFirstChild(); child != null;
         child = child.getNext()) {
      if (child.getType() == Token.CALL &&
          child.getFirstChild().toString().equals("checkType")) {
        Node arrayNode = child.getLastChild();
        for (Node checkerNode = arrayNode.getFirstChild();
             checkerNode != null;
             checkerNode = checkerNode.getNext()) {
          if (checkerNode.getType() == Token.CALL &&
              checkerNode.getFirstChild().toString().equals("classChecker") &&
              checkerNode.getLastChild() != null &&
              checkerNode.getLastChild().getType() == Token.STRING &&
              checkerNode.getLastChild().toString().equals(typeName)) {
            return checkerNode;
          }
          if (checkerNode.getType() == Token.CALL &&
              checkerNode.getFirstChild().toString().equals("interfaceChecker") &&
              checkerNode.getLastChild() != null &&
              checkerNode.getLastChild().getType() == Token.STRING &&
              checkerNode.getLastChild().toString().equals(typeName)) {
            return checkerNode;
          }
          if (checkerNode.getType() == Token.CALL &&
              checkerNode.getFirstChild().toString().equals("valueChecker") &&
              checkerNode.getLastChild() != null &&
              checkerNode.getLastChild().getType() == Token.STRING &&
              checkerNode.getLastChild().toString().equals(typeName)) {
            return checkerNode;
          }
        }
      }
    }
    return null;
  }
}