package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import org.junit.Test;

public class RuntimeTypeCheckTest {

  @Test
  public void testAddMarkers() {
    // Test adding markers for a simple class
    String className1 = "MySimpleClass";
    Node classNode1 = Node.newQualifiedNameNode(className1, -1, -1);

    FunctionType functionType1 = new FunctionType(
        new ObjectType(className1), ImmutableList.of(), null);

    Node nodeToInsertAfter1 = new Node(Token.SCRIPT, null);
    Node marker1 = new Node(Token.GETPROP, classNode1, Node.newString("prototype"));
    marker1.setProperty("instance_of__" + className1, Node.newBoolean(true));

    Node assign1 = new Node(Token.ASSIGN, marker1, Node.newBoolean(true));
    nodeToInsertAfter1.addChildAfter(assign1, null);

    Node markersToAdd1 = new Node(Token.ARRAYLIT);
    markersToAdd1.addChildToBack(marker1);

    Node addMarkers1 = new Node(Token.CALL, jsCode("addMarkers"), nodeToInsertAfter1, markersToAdd1);

    assertEquals(addMarkers1, processAddMarkers(className1, functionType1));

    // Test adding markers for a class with interfaces
    String className2 = "MyClassWithInterfaces";
    Node classNode2 = Node.newQualifiedNameNode(className2, -1, -1);

    FunctionType functionType2 = new FunctionType(
        new ObjectType(className2), ImmutableList.of(), null);

    ObjectType interfaceType1 = new ObjectType("MyInterface1");
    ObjectType interfaceType2 = new ObjectType("MyInterface2");
    functionType2.addImplementedInterface(interfaceType1);
    functionType2.addImplementedInterface(interfaceType2);

    Node marker1Interface1 = new Node(Token.GETPROP, classNode2, Node.newString("prototype"));
    marker1Interface1.setProperty("implements__" + interfaceType1.getReferenceName(), Node.newBoolean(true));

    Node marker1Interface2 = new Node(Token.GETPROP, classNode2, Node.newString("prototype"));
    marker1Interface2.setProperty("implements__" + interfaceType2.getReferenceName(), Node.newBoolean(true));

    Node markersToAdd2 = new Node(Token.ARRAYLIT);
    markersToAdd2.addChildToBack(marker1Interface1);
    markersToAdd2.addChildToBack(marker1Interface2);

    Node addMarkers2 = new Node(Token.CALL, jsCode("addMarkers"), nodeToInsertAfter1, markersToAdd2);

    assertEquals(addMarkers2, processAddMarkers(className2, functionType2));
  }

  @Test
  public void testAddChecks() {
    // Test adding checks for a simple class
    String className1 = "MySimpleClass";
    Node classNode1 = Node.newQualifiedNameNode(className1, -1, -1);

    FunctionType functionType1 = new FunctionType(
        new ObjectType(className1), ImmutableList.of(), null);

    Node checkNode1 = jsCode("checkType");
    Node checkerNode1 = new Node(Token.CALL, jsCode("valueChecker"), Node.newString("MySimpleClass"));
    Node arrayNode1 = new Node(Token.ARRAYLIT);
    arrayNode1.addChildToBack(checkerNode1);

    Node addChecks1 = new Node(Token.CALL, checkNode1, Node.newString(className1), arrayNode1);

    assertEquals(addChecks1, processAddChecks(className1, functionType1));

    // Test adding checks for a class with interfaces
    String className2 = "MyClassWithInterfaces";
    Node classNode2 = Node.newQualifiedNameNode(className2, -1, -1);

    FunctionType functionType2 = new FunctionType(
        new ObjectType(className2), ImmutableList.of(), null);

    ObjectType interfaceType1 = new ObjectType("MyInterface1");
    ObjectType interfaceType2 = new ObjectType("MyInterface2");
    functionType2.addImplementedInterface(interfaceType1);
    functionType2.addImplementedInterface(interfaceType2);

    Node checkerNode1Interface1 = new Node(Token.CALL, jsCode("interfaceChecker"), Node.newString(interfaceType1.getReferenceName()));
    Node checkerNode1Interface2 = new Node(Token.CALL, jsCode("interfaceChecker"), Node.newString(interfaceType2.getReferenceName()));
    Node arrayNode2 = new Node(Token.ARRAYLIT);
    arrayNode2.addChildToBack(checkerNode1Interface1);
    arrayNode2.addChildToBack(checkerNode1Interface2);

    Node addChecks2 = new Node(Token.CALL, checkNode1, Node.newString(className2), arrayNode2);

    assertEquals(addChecks2, processAddChecks(className2, functionType2));
  }

  private Node processAddMarkers(String className, FunctionType functionType) {
    Node nodeToInsertAfter = Node.newQualifiedNameNode("constructor", -1, -1);
    AbstractCompiler compiler = new Compiler();
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    Node root = new Node(Token.SCRIPT, null);
    Node function = new Node(Token.FUNCTION, Node.newString(className), null, null);
    function.setJSType(functionType);
    root.addChildToBack(function);
    runtimeTypeCheck.process(null, root);
    return nodeToInsertAfter;
  }

  private Node processAddChecks(String className, FunctionType functionType) {
    Node nodeToInsertAfter = Node.newQualifiedNameNode("constructor", -1, -1);
    AbstractCompiler compiler = new Compiler();
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    Node root = new Node(Token.SCRIPT, null);
    Node function = new Node(Token.FUNCTION, Node.newString(className), null, null);
    function.setJSType(functionType);
    root.addChildToBack(function);
    runtimeTypeCheck.process(null, root);
    return nodeToInsertAfter;
  }

  private static Node jsCode(String prop) {
    return NodeUtil.newQualifiedNameNode("jscomp.typecheck." + prop, -1, -1);
  }
}