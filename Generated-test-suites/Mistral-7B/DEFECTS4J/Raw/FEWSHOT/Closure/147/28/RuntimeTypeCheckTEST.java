package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.ImmutableList;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.UnionType;

@RunWith(JUnit4.class)
public class RuntimeTypeCheckTest {

  @Test
  public void testAddMarkers() {
    // Test adding markers for user-defined class
    Node root = NodeUtil.parseCompilationUnit(
        "class MyClass {}", null, null, null);
    RuntimeTypeCheck typeCheck = new RuntimeTypeCheck(null, null);
    typeCheck.process(null, root);
    Node myClassNode = NodeUtil.getRootNode(root).getFirstChild();
    Node marker = findMarker(myClassNode, "instance_of__MyClass");
    assertNotNull(marker);

    // Test adding markers for interface implementation
    Node interfaceNode = NodeUtil.parseCompilationUnit(
        "interface MyInterface {}", null, null, null).getFirstChild();
    Node myClassWithInterfaceNode = NodeUtil.parseCompilationUnit(
        "class MyClass implements MyInterface {}", null, null, null)
        .getFirstChild();
    typeCheck.process(null, myClassWithInterfaceNode);
    marker = findMarker(myClassWithInterfaceNode, "implements__MyInterface");
    assertNotNull(marker);
  }

  @Test
  public void testAddChecks() {
    // Test adding checks for function parameters
    Node functionNode = NodeUtil.parseCompilationUnit(
        "function testFunction(param) { return param; }", null, null, null)
        .getFirstChild();
    RuntimeTypeCheck typeCheck = new RuntimeTypeCheck(null, null);
    typeCheck.process(null, functionNode);
    Node paramNode = NodeUtil.getFunctionParameters(functionNode).getFirstChild();
    Node checkNode = findCheckNode(functionNode, 0);
    assertNotNull(checkNode);

    // Test adding checks for function return value
    Node returnNode = functionNode.getLastChild();
    checkNode = findCheckNode(functionNode, 1);
    assertNotNull(checkNode);
  }

  @Test
  public void testCreateCheckerNode() {
    // Test creating checker node for null type
    Node nullCheckerNode = createCheckerNode(null);
    assertNotNull(nullCheckerNode);

    // Test creating checker node for value types
    FunctionType numberType = (FunctionType)
        NodeUtil.parseCompilationUnit("function testNumber() { return 1; }", null, null, null)
        .getFirstChild()
        .getJSType();
    Node valueCheckerNode = createCheckerNode(numberType);
    assertNotNull(valueCheckerNode);

    // Test creating checker node for class types
    ObjectType myClassType = new ObjectType("MyClass");
    Node classCheckerNode = createCheckerNode(myClassType);
    assertNotNull(classCheckerNode);

    // Test creating checker node for interface types
    ObjectType myInterfaceType = new ObjectType("MyInterface");
    Node interfaceCheckerNode = createCheckerNode(myInterfaceType);
    assertNotNull(interfaceCheckerNode);
  }

  private Node findMarker(Node node, String markerName) {
    for (Node propertyNode = node.getFirstChild(); propertyNode != null;
         propertyNode = propertyNode.getNext()) {
      if (propertyNode.getType() == Token.GETPROP &&
          propertyNode.getFirstChild().getNodeType() == Token.STRING &&
          propertyNode.getFirstChild().getStringValue().equals(markerName)) {
        return propertyNode.getNext();
      }
    }
    return null;
  }

  private Node findCheckNode(Node node, int index) {
    for (int i = 0; i < node.getChildCount(); i++) {
      Node child = node.getChild(i);
      if (child.getType() == Token.CALL &&
          child.getFirstChild().getNodeType() == Token.IDENT &&
          child.getFirstChild().getStringValue().equals("checkType")) {
        if (i == index && child.getLastChild().getType() == Token.ARRAYLIT) {
          for (int j = 0; j < child.getLastChild().getChildCount(); j++) {
            Node checkerNode = child.getLastChild().getChild(j);
            if (checkerNode.getNodeType() == Token.CALL) {
              if (checkerNode.getFirstChild().getNodeType() == Token.IDENT &&
                  checkerNode.getFirstChild().getStringValue().equals(
                      "valueChecker")) {
                return checkerNode;
              }
              if (checkerNode.getFirstChild().getNodeType() == Token.IDENT &&
                  checkerNode.getFirstChild().getStringValue().startsWith(
                      "classChecker")) {
                return checkerNode;
              }
              if (checkerNode.getFirstChild().getNodeType() == Token.IDENT &&
                  checkerNode.getFirstChild().getStringValue().startsWith(
                      "interfaceChecker")) {
                return checkerNode;
              }
            }
          }
        }
      }
    }
    return null;
  }

  private Node createCheckerNode(JSType type) {
    return null;
  }
}