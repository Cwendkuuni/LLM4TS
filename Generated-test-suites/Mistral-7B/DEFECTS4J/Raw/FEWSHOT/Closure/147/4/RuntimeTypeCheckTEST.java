package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import com.google.common.collect.ImmutableList;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class RuntimeTypeCheckTest {

  @Test
  public void testAddMarkers() {
    // Test case for a simple class with no implemented interfaces
    String className1 = "TestClass1";
    // Create a tree for the test class
    Node rootNode1 = createTestClassTree(className1);
    // Create an instance of RuntimeTypeCheck
    RuntimeTypeCheck checker1 = new RuntimeTypeCheck(null, null);
    // Call the process method
    checker1.process(null, rootNode1);
    // Check the marker properties have been added to the class prototype
    Node prototypeNode1 = getPrototypeNode(rootNode1, className1);
    assertNotNull(prototypeNode1);
    Node instanceOfMarker1 = getInstanceOfMarkerNode(prototypeNode1, className1);
    assertNotNull(instanceOfMarker1);

    // Test case for a class with implemented interfaces
    String className2 = "TestClass2";
    String interfaceName1 = "TestInterface1";
    String interfaceName2 = "TestInterface2";
    // Create a tree for the test class and its interfaces
    Node rootNode2 = createTestClassTreeWithInterfaces(className2, interfaceName1, interfaceName2);
    // Create an instance of RuntimeTypeCheck
    RuntimeTypeCheck checker2 = new RuntimeTypeCheck(null, null);
    // Call the process method
    checker2.process(null, rootNode2);
    // Check the marker properties have been added to the class prototype and its interfaces
    Node prototypeNode2 = getPrototypeNode(rootNode2, className2);
    assertNotNull(prototypeNode2);
    Node instanceOfMarker2 = getInstanceOfMarkerNode(prototypeNode2, className2);
    assertNotNull(instanceOfMarker2);
    Node implementsMarker1 = getImplementsMarkerNode(prototypeNode2, interfaceName1);
    assertNotNull(implementsMarker1);
    Node implementsMarker2 = getImplementsMarkerNode(prototypeNode2, interfaceName2);
    assertNotNull(implementsMarker2);
  }

  @Test
  public void testAddChecks() {
    // Test case for a function with a single parameter of a known type
    String className = "TestClass";
    String functionName = "testFunction";
    String parameterType = "number";
    // Create a tree for the test class and its function
    Node rootNode = createTestClassWithFunctionTree(className, functionName, parameterType);
    // Create an instance of RuntimeTypeCheck
    RuntimeTypeCheck checker = new RuntimeTypeCheck(null, null);
    // Call the process method
    checker.process(null, rootNode);
    // Check the type checking call has been added to the function body
    Node functionNode = getFunctionNode(rootNode, className, functionName);
    assertNotNull(functionNode);
    Node checkTypeCallNode = getCheckTypeCallNode(functionNode);
    assertNotNull(checkTypeCallNode);

    // Test case for a function with a single parameter of an unknown type
    String unknownParameterType = "unknownType";
    // Create a tree for the test class and its function
    Node rootNodeUnknown = createTestClassWithFunctionTree(className, functionName, unknownParameterType);
    // Create an instance of RuntimeTypeCheck
    RuntimeTypeCheck checkerUnknown = new RuntimeTypeCheck(null, null);
    // Call the process method
    checkerUnknown.process(null, rootNodeUnknown);
    // Check the type checking call has not been added to the function body
    Node functionNodeUnknown = getFunctionNode(rootNodeUnknown, className, functionName);
    assertNotNull(functionNodeUnknown);
    Node checkTypeCallNodeUnknown = getCheckTypeCallNode(functionNodeUnknown);
    assertNull(checkTypeCallNodeUnknown);

    // Test case for a function with multiple parameters of known types
    String className2 = "TestClass2";
    String functionName2 = "testFunction2";
    String parameterType1 = "string";
    String parameterType2 = "number";
    // Create a tree for the test class and its function
    Node rootNode2 = createTestClassWithFunctionTree(className2, functionName2, parameterType1, parameterType2);
    // Create an instance of RuntimeTypeCheck
    RuntimeTypeCheck checker2 = new RuntimeTypeCheck(null, null);
    // Call the process method
    checker2.process(null, rootNode2);
    // Check the type checking calls have been added to the function body
    Node functionNode2 = getFunctionNode(rootNode2, className2, functionName2);
    assertNotNull(functionNode2);
    ImmutableList<Node> checkTypeCallNodes = getCheckTypeCallNodes(functionNode2);
    assertEquals(2, checkTypeCallNodes.size());
  }

  private Node createTestClassTree(String className) {
    return NodeUtil.newQualifiedNameNode(className, -1, -1);
  }

  private Node createTestClassTreeWithInterfaces(String className, String... interfaceNames) {
    Node classNode = NodeUtil.newQualifiedNameNode(className, -1, -1);
    for (String interfaceName : interfaceNames) {
      Node interfaceNode = NodeUtil.newQualifiedNameNode(interfaceName, -1, -1);
      Node extendsKeywordNode = new Node(Token.EXTENDS, null, interfaceNode);
      classNode.addChildToBack(extendsKeywordNode);
    }
    return classNode;
  }

  private Node createTestClassTreeWithFunction(String className, String functionName, String... parameterTypes) {
    Node classNode = NodeUtil.newQualifiedNameNode(className, -1, -1);
    Node functionNode = new Node(Token.FUNCTION, null, null);
    functionNode.setJSType(new FunctionType(null, ImmutableList.of(), new FunctionType.ReturnType(null, null, null)));
    Node functionDeclarationNode = new Node(Token.VAR, null, functionNode);
    classNode.addChildToBack(functionDeclarationNode);
    Node bodyNode = new Node(Token.BLOCK, null, null);
    functionNode.setBody(bodyNode);
    Node parameterNodes = new Node(Token.ARRAYLIT);
    for (String parameterType : parameterTypes) {
      Node parameterNode = new Node(Token.VAR, null, null, new Node(Token.IDENT, null, parameterType));
      bodyNode.addChildToBack(parameterNode);
      parameterNodes.addChildToBack(parameterNode);
    }
    functionNode.setParameters(parameterNodes);
    return classNode;
  }

  private Node createTestClassTreeWithFunctionTree(String className, String functionName, String... parameterTypes) {
    Node classNode = NodeUtil.newQualifiedNameNode(className, -1, -1);
    Node functionNode = createTestClassTreeWithFunction(className, functionName, parameterTypes);
    classNode.addChildToBack(functionNode);
    return classNode;
  }

  private Node getPrototypeNode(Node rootNode, String className) {
    for (Node childNode = rootNode.getFirstChild(); childNode != null; childNode = childNode.getNext()) {
      if (childNode.getType() == Token.BLOCK) {
        for (Node grandChildNode = childNode.getFirstChild(); grandChildNode != null; grandChildNode = grandChildNode.getNext()) {
          if (grandChildNode.getType() == Token.GETPROP && grandChildNode.getFirstChild().getStringValue().equals("prototype")) {
            Node prototypeNode = grandChildNode.getNext();
            for (Node prototypePropertyNode = prototypeNode.getFirstChild(); prototypePropertyNode != null; prototypePropertyNode = prototypePropertyNode.getNext()) {
              if (prototypePropertyNode.getType() == Token.GETPROP && prototypePropertyNode.getFirstChild().getStringValue().equals(className)) {
                return prototypePropertyNode.getNext();
              }
            }
          }
        }
      }
    }
    return null;
  }

  private Node getInstanceOfMarkerNode(Node prototypeNode, String className) {
    for (Node childNode = prototypeNode.getFirstChild(); childNode != null; childNode = childNode.getNext()) {
      if (childNode.getType() == Token.GETPROP && childNode.getFirstChild().getStringValue().equals("instance_of__" + className)) {
        return childNode.getNext();
      }
    }
    return null;
  }

  private Node getImplementsMarkerNode(Node prototypeNode, String interfaceName) {
    for (Node childNode = prototypeNode.getFirstChild(); childNode != null; childNode = childNode.getNext()) {
      if (childNode.getType() == Token.GETPROP && childNode.getFirstChild().getStringValue().equals("implements__" + interfaceName)) {
        return childNode.getNext();
      }
    }
    return null;
  }

  private Node getFunctionNode(Node rootNode, String className, String functionName) {
    for (Node childNode = rootNode.getFirstChild(); childNode != null; childNode = childNode.getNext()) {
      if (childNode.getType() == Token.BLOCK) {
        for (Node grandChildNode = childNode.getFirstChild(); grandChildNode != null; grandChildNode = grandChildNode.getNext()) {
          if (grandChildNode.getType() == Token.VAR && grandChildNode.getFirstChild().getType() == Token.IDENT && grandChildNode.getFirstChild().getStringValue().equals(className)
              && grandChildNode.getNext() != null && grandChildNode.getNext().getType() == Token.FUNCTION) {
            Node functionNode = grandChildNode.getNext();
            if (functionNode.getLastChild().getType() == Token.IDENT && functionNode.getLastChild().getStringValue().equals(functionName)) {
              return functionNode;
            }
          }
        }
      }
    }
    return null;
  }

  private Node getCheckTypeCallNode(Node functionNode) {
    for (Node childNode = functionNode.getFirstChild(); childNode != null; childNode = childNode.getNext()) {
      if (childNode.getType() == Token.CALL) {
        Node checkTypeFunctionNode = childNode.getFirstChild();
        if (checkTypeFunctionNode.getFirstChild().getStringValue().equals("checkType")) {
          return childNode;
        }
      }
    }
    return null;
  }

  private ImmutableList<Node> getCheckTypeCallNodes(Node functionNode) {
    ImmutableList.Builder<Node> checkTypeCallNodesBuilder = ImmutableList.builder();
    for (Node childNode = functionNode.getFirstChild(); childNode != null; childNode = childNode.getNext()) {
      if (childNode.getType() == Token.CALL) {
        Node checkTypeFunctionNode = childNode.getFirstChild();
        if (checkTypeFunctionNode.getFirstChild().getStringValue().equals("checkType")) {
          checkTypeCallNodesBuilder.add(childNode);
        }
      }
    }
    return checkTypeCallNodesBuilder.build();
  }
}