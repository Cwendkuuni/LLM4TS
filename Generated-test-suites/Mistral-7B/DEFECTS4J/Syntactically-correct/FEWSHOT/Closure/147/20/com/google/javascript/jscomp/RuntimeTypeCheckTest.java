package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeTraversal;

import java.util.Comparator;
import java.util.TreeSet;

/**
 * Test class for {@link com.google.javascript.jscomp.RuntimeTypeCheck}
 */
public class RuntimeTypeCheckTest {

  @Test
  public void testAddMarkers() {
    // Given
    Node root = NodeUtil.parseJsString("" +
        "function Foo() {}\n" +
        "function Bar() {}\n" +
        "function FooBar() {\n" +
        "  this.foo = new Foo();\n" +
        "}\n");

    // When
    NodeTraversal.traverse(
        new AbstractCompiler(new MockCompilerInput("")), root,
        new RuntimeTypeCheck(
            new AbstractCompiler(new MockCompilerInput("")), null));

    // Then
    TreeSet<String> markerNames = new TreeSet<>(Comparator.naturalOrder());
    Node currentNode = root;
    while (currentNode != null) {
      if (currentNode.getType() != Token.SCRIPT &&
          currentNode.getType() != Token.BLOCK) {
        Node prototype = currentNode.getFirstChild();
        if (prototype != null) {
          Node markerNode = prototype.getFirstChild();
          while (markerNode != null) {
            String markerName = markerNode.toString();
            markerNames.add(markerName);
            markerNode = markerNode.getNext();
          }
        }
      }
      currentNode = currentNode.getNext();
    }

    ImmutableList<String> expectedMarkerNames = ImmutableList.of(
        "instance_of__Foo", "implements__Function",
        "instance_of__Foo", "implements__Function");
    assertEquals(expectedMarkerNames, markerNames);
  }

  @Test
  public void testAddChecks() {
    // Given
    Node root = NodeUtil.parseJsString("" +
        "function Foo(x) { return x; }\n" +
        "function Bar(x) { return x * 2; }\n" +
        "function FooBar(x) { return Foo(x) + Bar(x); }");

    // When
    NodeTraversal.traverse(
        new AbstractCompiler(new MockCompilerInput("")), root,
        new RuntimeTypeCheck(
            new AbstractCompiler(new MockCompilerInput("")), null));

    // Then
    Node functionNode = NodeUtil.findNodeByFunctionName(root, "FooBar");
    assertEquals(3, functionNode.getLastChild().getNumChildren());

    Node checkNode1 = functionNode.getLastChild().getChild(0);
    assertEquals("number", checkNode1.getFirstChild().toString());
    assertEquals("checkType", checkNode1.getNext().getType());
    assertEquals("x", checkNode1.getNext().getNext().toString());
    assertEquals("[valueChecker('number')]",
        checkNode1.getNext().getNext().getNext().toString());

    Node checkNode2 = functionNode.getLastChild().getChild(1);
    assertEquals("number", checkNode2.getFirstChild().toString());
    assertEquals("checkType", checkNode2.getNext().getType());
    assertEquals("Foo(x)", checkNode2.getNext().toString());
    assertEquals("[valueChecker('number')]",
        checkNode2.getNext().getNext().toString());

    Node checkNode3 = functionNode.getLastChild().getChild(2);
    assertEquals("number", checkNode3.getFirstChild().toString());
    assertEquals("checkType", checkNode3.getNext().getType());
    assertEquals("Bar(x)", checkNode3.getNext().toString());
    assertEquals("[valueChecker('number')]",
        checkNode3.getNext().getNext().toString());
  }

  @Test
  public void testAddBoilerplateCode() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler(new MockCompilerInput(""));

    // When
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.addBoilerplateCode();

    // Then
    String boilerplateCode = compiler.getNodeForCodeInsertion(null)
        .getLastChild().getLastChild().toString();
    assertTrue(boilerplateCode.contains("checkType"));
    assertTrue(boilerplateCode.contains("valueChecker"));
    assertTrue(boilerplateCode.contains("interfaceChecker"));
    assertTrue(boilerplateCode.contains("classChecker"));
    assertTrue(boilerplateCode.contains("nullChecker"));
  }
}

class MockCompilerInput implements CompilerInput {
  private final String name;

  MockCompilerInput(String name) {
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public InputStream getInputStream() {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isExtern() {
    return false;
  }
}