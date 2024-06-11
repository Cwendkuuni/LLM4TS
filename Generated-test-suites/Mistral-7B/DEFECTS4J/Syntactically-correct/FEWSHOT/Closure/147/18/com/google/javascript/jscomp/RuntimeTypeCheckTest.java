package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.UnionType;
import org.junit.Test;

public class RuntimeTypeCheckTest {

  @Test
  public void testAddMarkers() {
    Node rootNode = new Node(Token.SCRIPT, null);

    // Test class with no interfaces
    String className1 = "MyClass";
    FunctionType classType1 = new FunctionType(className1, ImmutableList.of());
    Node classNode1 = NodeUtil.newQualifiedNameNode(className1, -1, -1);
    Node markerNode1 = new Node(Token.GETELEM, classNode1, new Node(Token.STRING, "prototype"));
    markerNode1.addChild(new Node(Token.STRING, "instance_of__MyClass"));
    Node markerNode1Parent = NodeUtil.findEnclosingFunction(classNode1, rootNode);
    markerNode1Parent.addChildAfter(markerNode1, markerNode1Parent.getLastChild());

    // Test class implementing an interface
    String className2 = "MyClassImpl";
    FunctionType classType2 = new FunctionType(className2, ImmutableList.of("MyInterface"));
    Node classNode2 = NodeUtil.newQualifiedNameNode(className2, -1, -1);
    Node markerNode2 = new Node(Token.GETELEM, classNode2, new Node(Token.STRING, "prototype"));
    markerNode2.addChild(new Node(Token.STRING, "implements__MyInterface"));
    Node markerNode2Parent = NodeUtil.findEnclosingFunction(classNode2, rootNode);
    markerNode2Parent.addChildAfter(markerNode2, markerNode2Parent.getLastChild());

    // Test constructor call with markers
    String constructorName = "MyConstructor";
    Node constructorNode = NodeUtil.newQualifiedNameNode(className1, -1, -1);
    Node constructorCallNode = NodeUtil.newFunctionCall(constructorName, null);
    Node markerNode3 = NodeUtil.findMarkerNode(constructorCallNode, "instance_of__MyClass");
    assertNotNull(markerNode3);
    Node markerNode4 = NodeUtil.findMarkerNode(constructorCallNode, "implements__MyInterface");
    assertNull(markerNode4);

    // Test method call with markers
    String methodName = "myMethod";
    FunctionType methodType = new FunctionType(methodName, ImmutableList.of("number"));
    Node methodNode = NodeUtil.newFunctionDeclaration(methodName, methodType, null);
    Node markerNode5 = NodeUtil.findMarkerNode(methodNode, "instance_of__MyClass");
    assertNull(markerNode5);
    Node markerNode6 = NodeUtil.findMarkerNode(methodNode, "implements__MyInterface");
    assertNull(markerNode6);

    // Test method call with markers on interface
    String interfaceName = "MyInterface";
    FunctionType interfaceType = new FunctionType(interfaceName, ImmutableList.of());
    ObjectType interfaceObjectType = new ObjectType(interfaceName, null, interfaceType, null);
    Node interfaceNode = NodeUtil.newQualifiedNameNode(interfaceName, -1, -1);
    Node markerNode7 = NodeUtil.findMarkerNode(interfaceNode, "implements__MyInterface");
    assertNotNull(markerNode7);
  }

  @Test
  public void testAddChecks() {
    Node rootNode = new Node(Token.SCRIPT, null);

    // Test constructor check
    String constructorName = "MyConstructor";
    FunctionType constructorType = new FunctionType(constructorName, ImmutableList.of("string"));
    Node constructorNode = NodeUtil.newFunctionDeclaration(constructorName, constructorType, null);
    Node constructorCallNode = NodeUtil.newFunctionCall(constructorName, new Node(Token.STRING, "hello"));
    Node checkTypeCallNode = NodeUtil.findCheckTypeCallNode(constructorCallNode);
    assertNotNull(checkTypeCallNode);
    FunctionType checkerType = (FunctionType) checkTypeCallNode.getLastChild();
    assertFalse(checkerType.isConstructor());

    // Test function parameter check
    String functionName = "myFunction";
    FunctionType functionType = new FunctionType(functionName, ImmutableList.of("number", "boolean"), null);
    Node functionNode = NodeUtil.newFunctionDeclaration(functionName, functionType, null);
    Node paramNode1 = functionNode.getLastChild().getLastChild();
    Node checkTypeCallNode1 = NodeUtil.findCheckTypeCallNode(paramNode1);
    assertNotNull(checkTypeCallNode1);

    // Test function return value check
    Node returnNode = new Node(Token.RETURN, null, null);
    Node returnCallNode = new Node(Token.CALL, new Node(Token.GETPROP, returnNode, new Node(Token.STRING, "toString")), null);
    Node checkTypeCallNode2 = NodeUtil.findCheckTypeCallNode(returnCallNode);
    assertNotNull(checkTypeCallNode2);

    // Test union type check
    FunctionType unionTypeFunction = new FunctionType("myFunction", ImmutableList.of("number", "string"), null);
    Node unionTypeFunctionNode = NodeUtil.newFunctionDeclaration(unionTypeFunction.getName(), unionTypeFunction, null);
    Node unionTypeCheckNode = NodeUtil.findCheckTypeCallNode(unionTypeFunctionNode.getLastChild());
    assertNotNull(unionTypeCheckNode);
    UnionType unionType = (UnionType) unionTypeCheckNode.getLastChild();
    assertFalse(unionType.getAlternates().isEmpty());
  }

  private static Node findMarkerNode(Node node, String markerName) {
    for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
      if (child.getType() == Token.GETELEM && markerName.equals(child.getLastChild().getValue())) {
        return child;
      }
      Node found = findMarkerNode(child, markerName);
      if (found != null) {
        return found;
      }
    }
    return null;
  }

  private static Node findCheckTypeCallNode(Node node) {
    for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
      if (child.getType() == Token.CALL && "checkType".equals(child.getFirstChild().getValue())) {
        return child;
      }
      Node found = findCheckTypeCallNode(child);
      if (found != null) {
        return found;
      }
    }
    return null;
  }
}