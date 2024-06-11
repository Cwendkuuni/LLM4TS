package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.RuntimeTypeCheck;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.Token;

import javax.annotation.Nullable;

@RunWith(JUnit4.class)
public class RuntimeTypeCheckTest {

  @Test
  public void testAddMarkers() {
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(null, null);

    // Test for a simple interface
    String interfaceName = "MyInterface";
    String className = "MyClassImplementingMyInterface";

    Node interfaceNode = NodeUtil.newQualifiedNameNode(interfaceName, -1, -1);
    Node classNode = NodeUtil.newQualifiedNameNode(className, -1, -1);

    Node functionNode = NodeUtil.newFunctionDeclaration(
        Token.FUNCTION, null, null, ImmutableList.of(), null, null);

    Node functionParent = Node.newBlock(functionNode);

    Node markersParent = Node.newBlock();
    markersParent.addChildToBack(functionParent);

    Node expectedMarker = Node.newString("instance_of__" + className);
    Node marker = runtimeTypeCheck.addMarker(
        NodeUtil.getFunctionType(functionNode),
        markersParent,
        null);

    assertEquals(expectedMarker, marker);

    // Test for a class implementing multiple interfaces
    String interfaceName2 = "MyOtherInterface";

    Node interfaceNode2 = NodeUtil.newQualifiedNameNode(interfaceName2, -1, -1);
    Node expectedMarker2 = Node.newString("implements__" + interfaceName2);

    marker = runtimeTypeCheck.addMarker(
        NodeUtil.getFunctionType(functionNode),
        markersParent,
        NodeUtil.getObjectType(interfaceNode2));

    assertEquals(expectedMarker2, marker);
  }

  @Test
  public void testAddChecks() {
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(null, null);

    String className = "MyClass";
    String interfaceName = "MyInterface";

    Node functionType = NodeUtil.getFunctionType(
        NodeUtil.newFunctionDeclaration(
            Token.FUNCTION, null, null, ImmutableList.of(), null, null));

    Node classNode = NodeUtil.newQualifiedNameNode(className, -1, -1);

    Node functionParent = NodeUtil.newBlock();
    Node markersParent = NodeUtil.newBlock();

    // Add markers for the class and the interface
    Node marker = runtimeTypeCheck.addMarker(
        functionType, markersParent, NodeUtil.getObjectType(classNode));
    marker = runtimeTypeCheck.addMarker(
        functionType, markersParent, NodeUtil.getObjectType(
            NodeUtil.newQualifiedNameNode(interfaceName, -1, -1)));

    // Insert checks for the class and the interface
    Node checkTypeCallNode = runtimeTypeCheck.createCheckTypeCallNode(
        NodeUtil.getObjectType(classNode), NodeUtil.newIdentifier("param"));

    Node checkTypeCallNode2 = runtimeTypeCheck.createCheckTypeCallNode(
        NodeUtil.getObjectType(NodeUtil.newQualifiedNameNode(interfaceName, -1, -1)),
        NodeUtil.newIdentifier("param"));

    markersParent.addChildToBack(checkTypeCallNode);
    markersParent.addChildToBack(checkTypeCallNode2);

    Node function = NodeUtil.getFunctionDeclaration(functionType, markersParent);
    Node expectedFunction = NodeUtil.getFunctionDeclaration(
        Token.FUNCTION, null, null, ImmutableList.of(), null, null);
    expectedFunction.addChildToBack(markersParent);

    assertEquals(expectedFunction, function);
  }

  @Test
  public void testCreateCheckerNode() {
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(null, null);

    // Test for a null type
    Node nullCheckerNode = runtimeTypeCheck.createCheckerNode(Node.NULL_TYPE);
    assertNull(nullCheckerNode);

    // Test for a boolean value type
    Node booleanCheckerNode = runtimeTypeCheck.createCheckerNode(
        Node.BOOLEAN_VALUE_TYPE);
    Node expectedBooleanCheckerNode = NodeUtil.newCall(
        NodeUtil.newQualifiedNameNode("valueChecker", -1, -1),
        Node.newString("Boolean"));
    assertEquals(expectedBooleanCheckerNode, booleanCheckerNode);

    // Test for a number value type
    Node numberCheckerNode = runtimeTypeCheck.createCheckerNode(
        Node.NUMBER_VALUE_TYPE);
    Node expectedNumberCheckerNode = NodeUtil.newCall(
        NodeUtil.newQualifiedNameNode("valueChecker", -1, -1),
        Node.newString("Number"));
    assertEquals(expectedNumberCheckerNode, numberCheckerNode);

    // Test for a string value type
    Node stringCheckerNode = runtimeTypeCheck.createCheckerNode(
        Node.STRING_VALUE_TYPE);
    Node expectedStringCheckerNode = NodeUtil.newCall(
        NodeUtil.newQualifiedNameNode("valueChecker", -1, -1),
        Node.newString("String"));
    assertEquals(expectedStringCheckerNode, stringCheckerNode);

    // Test for an instance type
    String className = "MyClass";
    Node classNode = NodeUtil.newQualifiedNameNode(className, -1, -1);
    Node classCheckerNode = runtimeTypeCheck.createCheckerNode(
        NodeUtil.getObjectType(classNode));
    Node expectedClassCheckerNode = NodeUtil.newCall(
        NodeUtil.newQualifiedNameNode("classChecker", -1, -1),
        Node.newString(className));
    assertEquals(expectedClassCheckerNode, classCheckerNode);

    // Test for an interface type
    String interfaceName = "MyInterface";
    Node interfaceNode = NodeUtil.newQualifiedNameNode(interfaceName, -1, -1);
    Node interfaceCheckerNode = runtimeTypeCheck.createCheckerNode(
        NodeUtil.getObjectType(interfaceNode));
    Node expectedInterfaceCheckerNode = NodeUtil.newCall(
        NodeUtil.newQualifiedNameNode("interfaceChecker", -1, -1),
        Node.newString(interfaceName));
    assertEquals(expectedInterfaceCheckerNode, interfaceCheckerNode);

    // Test for a void type
    Node voidCheckerNode = runtimeTypeCheck.createCheckerNode(Node.VOID_TYPE);
    assertNull(voidCheckerNode);

    // Test for an unknown type
    Node unknownCheckerNode = runtimeTypeCheck.createCheckerNode(
        Node.UNKNOWN_TYPE);
    assertNull(unknownCheckerNode);

    // Test for an all type
    Node allCheckerNode = runtimeTypeCheck.createCheckerNode(Node.All_TYPE);
    assertNull(allCheckerNode);

    // Test for a union type
    String className2 = "MyOtherClass";
    Node classNode2 = NodeUtil.newQualifiedNameNode(className2, -1, -1);
    Node interfaceName2 = "MyOtherInterface";
    Node interfaceNode2 = NodeUtil.newQualifiedNameNode(interfaceName2, -1, -1);
    Node unionType = new Node(
        Token.UNIONTYPE, NodeUtil.getObjectType(classNode),
        NodeUtil.getObjectType(interfaceNode2));
    Node unionCheckerNode = runtimeTypeCheck.createCheckerNode(unionType);
    Node expectedUnionCheckerNode = NodeUtil.newCall(
        NodeUtil.newQualifiedNameNode("unionChecker", -1, -1),
        Node.newArray(
            NodeUtil.newCall(
                NodeUtil.newQualifiedNameNode("valueChecker", -1, -1),
                Node.newString(className2)),
            NodeUtil.newCall(
                NodeUtil.newQualifiedNameNode("interfaceChecker", -1, -1),
                Node.newString(interfaceName2))));
    assertEquals(expectedUnionCheckerNode, unionCheckerNode);
  }

  @Test
  public void testCreateCheckTypeCallNode() {
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(null, null);

    String className = "MyClass";
    Node classNode = NodeUtil.newQualifiedNameNode(className, -1, -1);

    Node classNameNode = Node.newString(className);
    Node stringNode = NodeUtil.newString("string");

    // Test for a simple instance type
    Node checkTypeCallNode = runtimeTypeCheck.createCheckTypeCallNode(
        NodeUtil.getObjectType(classNode), classNameNode);
    Node expectedCheckTypeCallNode = NodeUtil.newCall(
        NodeUtil.newQualifiedNameNode("checkType", -1, -1),
        classNameNode,
        Node.newArray(NodeUtil.newCall(
            NodeUtil.newQualifiedNameNode("classChecker", -1, -1), classNameNode)));
    assertEquals(expectedCheckTypeCallNode, checkTypeCallNode);

    // Test for a union type
    String className2 = "MyOtherClass";
    Node classNode2 = NodeUtil.newQualifiedNameNode(className2, -1, -1);
    Node interfaceName = "MyInterface";
    Node interfaceNode = NodeUtil.newQualifiedNameNode(interfaceName, -1, -1);
    Node unionType = new Node(
        Token.UNIONTYPE, NodeUtil.getObjectType(classNode),
        NodeUtil.getObjectType(interfaceNode));
    Node checkTypeCallNode2 = runtimeTypeCheck.createCheckTypeCallNode(
        unionType, classNameNode);
    Node expectedCheckTypeCallNode2 = NodeUtil.newCall(
        NodeUtil.newQualifiedNameNode("checkType", -1, -1),
        classNameNode,
        Node.newArray(
            NodeUtil.newCall(
                NodeUtil.newQualifiedNameNode("classChecker", -1, -1), classNameNode),
            NodeUtil.newCall(
                NodeUtil.newQualifiedNameNode("interfaceChecker", -1, -1),
                interfaceName)));
    assertEquals(expectedCheckTypeCallNode2, checkTypeCallNode2);

    // Test for a value type
    Node checkTypeCallNode3 = runtimeTypeCheck.createCheckTypeCallNode(
        Node.STRING_VALUE_TYPE, stringNode);
    Node expectedCheckTypeCallNode3 = NodeUtil.newCall(
        NodeUtil.newQualifiedNameNode("checkType", -1, -1),
        stringNode,
        Node.newArray(NodeUtil.newCall(
            NodeUtil.newQualifiedNameNode("valueChecker", -1, -1),
            Node.newString("String"))));
    assertEquals(expectedCheckTypeCallNode3, checkTypeCallNode3);

    // Test for a null type
    Node checkTypeCallNode4 = runtimeTypeCheck.createCheckTypeCallNode(
        Node.NULL_TYPE, classNameNode);
    assertNull(checkTypeCallNode4);

    // Test for a void type
    Node checkTypeCallNode5 = runtimeTypeCheck.createCheckTypeCallNode(
        Node.VOID_TYPE, classNameNode);
    assertNull(checkTypeCallNode5);

    // Test for an unknown type
    Node checkTypeCallNode6 = runtimeTypeCheck.createCheckTypeCallNode(
        Node.UNKNOWN_TYPE, classNameNode);
    assertNull(checkTypeCallNode6);

    // Test for an all type
    Node checkTypeCallNode7 = runtimeTypeCheck.createCheckTypeCallNode(
        Node.All_TYPE, classNameNode);
    assertNull(checkTypeCallNode7);
  }
}