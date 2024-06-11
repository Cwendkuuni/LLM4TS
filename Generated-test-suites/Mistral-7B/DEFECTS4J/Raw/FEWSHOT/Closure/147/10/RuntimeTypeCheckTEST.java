package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.google.common.io.CharStreams;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.UnionType;
import org.junit.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

public class RuntimeTypeCheckTest {

  @Test
  public void testAddMarkers() throws IOException {
    String sourceCode = "class MyClass {}\n" +
        "function myFunction(param) {}\n";

    AbstractCompiler compiler = new Compiler();
    Node externs = NodeUtil.parseCode(sourceCode);
    Node root = NodeUtil.parseCode("class MyClass {\n" +
        "  constructor() {\n" +
        "  }\n" +
        "}\n" +
        "function myFunction(param) {\n" +
        "  return param;\n" +
        "}\n");

    RuntimeTypeCheck typeCheck = new RuntimeTypeCheck(compiler, null);
    typeCheck.process(externs, root);

    Node myClass = Iterables.getLast(NodeUtil.getChildren(root));
    assertEquals(2, myClass.getChildCount());

    Node instanceOfMarker = myClass.getChild(0);
    assertNotNull(instanceOfMarker);
    assertEquals("instance_of__MyClass", instanceOfMarker.toString());

    Node implementsMarkers = myClass.getChild(1);
    assertNotNull(implementsMarkers);
    assertEquals("implements__", implementsMarkers.toString());

    TreeSet<Node> interfaceMarkers = new TreeSet<>(ALPHA);
    for (Node markerNode : Iterables.getChildrenAssoc(implementsMarkers)) {
      ObjectType interfaceType =
          ((ObjectType) NodeUtil.getJSType(markerNode.getParent()));
      interfaceMarkers.add(markerNode);
      assertEquals(interfaceType.getReferenceName(), markerNode.toString());
    }
    assertEquals(ImmutableList.of("IFace1", "IFace2"), interfaceMarkers);
  }

  @Test
  public void testAddChecks() throws IOException {
    String sourceCode = "class MyClass {\n" +
        "  constructor(param) {\n" +
        "  }\n" +
        "}\n" +
        "function myFunction(param) {\n" +
        "  return new MyClass(param);\n" +
        "}\n";

    AbstractCompiler compiler = new Compiler();
    Node externs = NodeUtil.parseCode(sourceCode);
    Node root = NodeUtil.parseCode(sourceCode);

    RuntimeTypeCheck typeCheck = new RuntimeTypeCheck(compiler, null);
    typeCheck.process(externs, root);

    Node myFunction = Iterables.find(NodeUtil.getChildren(root),
        node -> NodeUtil.isFunction(node) &&
        "myFunction".equals(NodeUtil.getFunctionName(node)));
    assertEquals(1, myFunction.getChildCount());

    Node checkNode = myFunction.getChild(0);
    assertNotNull(checkNode);
    assertEquals("checkType(param, [valueChecker('MyClass')])", checkNode.toString());
  }

  @Test
  public void testAddChecksForInterface() throws IOException {
    String sourceCode = "interface IFace {\n" +
        "}\n" +
        "class MyClass implements IFace {\n" +
        "  constructor() {\n" +
        "  }\n" +
        "}\n" +
        "function myFunction(param) {\n" +
        "  return new MyClass();\n" +
        "}\n";

    AbstractCompiler compiler = new Compiler();
    Node externs = NodeUtil.parseCode(sourceCode);
    Node root = NodeUtil.parseCode(sourceCode);

    RuntimeTypeCheck typeCheck = new RuntimeTypeCheck(compiler, null);
    typeCheck.process(externs, root);

    Node myFunction = Iterables.find(NodeUtil.getChildren(root),
        node -> NodeUtil.isFunction(node) &&
        "myFunction".equals(NodeUtil.getFunctionName(node)));
    assertEquals(1, myFunction.getChildCount());

    Node checkNode = myFunction.getChild(0);
    assertNotNull(checkNode);
    assertEquals("checkType(param, [interfaceChecker('IFace')])", checkNode.toString());
  }

  @Test
  public void testAddChecksForUnionType() throws IOException {
    String sourceCode = "function myFunction(param) {\n" +
        "  return param;\n" +
        "}\n" +
        "class MyClass {\n" +
        "}\n" +
        "class AnotherClass {\n" +
        "}\n";

    AbstractCompiler compiler = new Compiler();
    Node externs = NodeUtil.parseCode(sourceCode);
    Node root = NodeUtil.parseCode(sourceCode);

    RuntimeTypeCheck typeCheck = new RuntimeTypeCheck(compiler, null);
    typeCheck.process(externs, root);

    Node myFunction = Iterables.find(NodeUtil.getChildren(root),
        node -> NodeUtil.isFunction(node) &&
        "myFunction".equals(NodeUtil.getFunctionName(node)));
    assertEquals(1, myFunction.getChildCount());

    Node checkNode = myFunction.getChild(0);
    assertNotNull(checkNode);
    Node arrayNode = (Node) checkNode.getLastChild();
    assertEquals(2, arrayNode.getChildCount());

    Node stringCheckerNode = arrayNode.getChild(0);
    Node classCheckerNode = arrayNode.getChild(1);

    assertEquals("valueChecker('string')", stringCheckerNode.toString());
    assertEquals("classChecker('MyClass')", classCheckerNode.toString());
  }

  @Test
  public void testAddChecksForNullType() throws IOException {
    String sourceCode = "function myFunction(param) {\n" +
        "  return param;\n" +
        "}\n";

    AbstractCompiler compiler = new Compiler();
    Node externs = NodeUtil.parseCode(sourceCode);
    Node root = NodeUtil.parseCode(sourceCode);

    RuntimeTypeCheck typeCheck = new RuntimeTypeCheck(compiler, null);
    typeCheck.process(externs, root);

    Node myFunction = Iterables.find(NodeUtil.getChildren(root),
        node -> NodeUtil.isFunction(node) &&
        "myFunction".equals(NodeUtil.getFunctionName(node)));
    assertEquals(1, myFunction.getChildCount());

    Node checkNode = myFunction.getChild(0);
    assertNotNull(checkNode);
    assertEquals("checkType(param, [nullChecker()])", checkNode.toString());
  }

  @Test
  public void testAddChecksForBooleanValueType() throws IOException {
    String sourceCode = "function myFunction(param) {\n" +
        "  return param;\n" +
        "}\n";

    AbstractCompiler compiler = new Compiler();
    Node externs = NodeUtil.parseCode(sourceCode);
    Node root = NodeUtil.parseCode(sourceCode);

    RuntimeTypeCheck typeCheck = new RuntimeTypeCheck(compiler, null);
    typeCheck.process(externs, root);

    Node myFunction = Iterables.find(NodeUtil.getChildren(root),
        node -> NodeUtil.isFunction(node) &&
        "myFunction".equals(NodeUtil.getFunctionName(node)));
    assertEquals(1, myFunction.getChildCount());

    Node checkNode = myFunction.getChild(0);
    assertNotNull(checkNode);
    assertEquals("checkType(param, [valueChecker('boolean')])", checkNode.toString());
  }

  @Test
  public void testAddChecksForNumberValueType() throws IOException {
    String sourceCode = "function myFunction(param) {\n" +
        "  return param;\n" +
        "}\n";

    AbstractCompiler compiler = new Compiler();
    Node externs = NodeUtil.parseCode(sourceCode);
    Node root = NodeUtil.parseCode(sourceCode);

    RuntimeTypeCheck typeCheck = new RuntimeTypeCheck(compiler, null);
    typeCheck.process(externs, root);

    Node myFunction = Iterables.find(NodeUtil.getChildren(root),
        node -> NodeUtil.isFunction(node) &&
        "myFunction".equals(NodeUtil.getFunctionName(node)));
    assertEquals(1, myFunction.getChildCount());

    Node checkNode = myFunction.getChild(0);
    assertNotNull(checkNode);
    assertEquals("checkType(param, [valueChecker('number')])", checkNode.toString());
  }

  @Test
  public void testAddChecksForStringValueType() throws IOException {
    String sourceCode = "function myFunction(param) {\n" +
        "  return param;\n" +
        "}\n";

    AbstractCompiler compiler = new Compiler();
    Node externs = NodeUtil.parseCode(sourceCode);
    Node root = NodeUtil.parseCode(sourceCode);

    RuntimeTypeCheck typeCheck = new RuntimeTypeCheck(compiler, null);
    typeCheck.process(externs, root);

    Node myFunction = Iterables.find(NodeUtil.getChildren(root),
        node -> NodeUtil.isFunction(node) &&
        "myFunction".equals(NodeUtil.getFunctionName(node)));
    assertEquals(1, myFunction.getChildCount());

    Node checkNode = myFunction.getChild(0);
    assertNotNull(checkNode);
    assertEquals("checkType(param, [valueChecker('string')])", checkNode.toString());
  }

  @Test
  public void testAddChecksForVoidType() throws IOException {
    String sourceCode = "function myFunction() {\n" +
        "  return;\n" +
        "}\n";

    AbstractCompiler compiler = new Compiler();
    Node externs = NodeUtil.parseCode(sourceCode);
    Node root = NodeUtil.parseCode(sourceCode);

    RuntimeTypeCheck typeCheck = new RuntimeTypeCheck(compiler, null);
    typeCheck.process(externs, root);

    Node myFunction = Iterables.find(NodeUtil.getChildren(root),
        node -> NodeUtil.isFunction(node) &&
        "myFunction".equals(NodeUtil.getFunctionName(node)));
    assertEquals(1, myFunction.getChildCount());

    Node checkNode = myFunction.getChild(0);
    assertNotNull(checkNode);
    assertEquals("checkType(null, [])", checkNode.toString());
  }
}