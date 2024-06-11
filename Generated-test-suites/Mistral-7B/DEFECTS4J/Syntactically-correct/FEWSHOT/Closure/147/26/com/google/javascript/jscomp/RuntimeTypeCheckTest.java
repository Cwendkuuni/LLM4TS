package com.google.javascript.jscomp;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Function;
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
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

/**
 * Test cases for {@link RuntimeTypeCheck} class.
 */
public class RuntimeTypeCheckTest {

  private AbstractCompiler compiler;
  private Node rootNode;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    rootNode = Node.newBlock(1);
  }

  @Test
  public void testAddMarkers() {
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(null, rootNode);

    Node functionNode = NodeUtil.newQualifiedNameNode("MyFunction", -1, -1);
    FunctionType functionType = FunctionType.create(new ObjectType[]{}, new ObjectType[]{});
    Node constructorNode = NodeUtil.newFunctionDeclaration(functionNode, functionType);
    rootNode.addChildToFront(constructorNode);

    runtimeTypeCheck.process(null, rootNode);

    Node prototypeNode = findPrototypeNode(functionNode);
    FunctionType prototypeFunctionType = (FunctionType) prototypeNode.getJSType();

    assertCheckerNodesPresent(prototypeFunctionType, "instance_of__MyFunction");
    assertCheckerNodesPresent(prototypeFunctionType, "implements__I1");
    assertCheckerNodesPresent(prototypeFunctionType, "implements__I2");
  }

  @Test
  public void testAddChecks() {
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    rootNode.addChildToFront(NodeUtil.newFunctionDeclaration(
        NodeUtil.newQualifiedNameNode("MyFunction", -1, -1),
        FunctionType.create(new ObjectType[]{}, new ObjectType[]{})).cloneTree());

    runtimeTypeCheck.process(null, rootNode);

    Node functionNode = NodeUtil.newQualifiedNameNode("MyFunction", -1, -1);
    FunctionType functionType = FunctionType.create(new ObjectType[]{}, new ObjectType[]{});
    Node constructorNode = NodeUtil.newFunctionDeclaration(functionNode, functionType);
    Node blockNode = constructorNode.getLastChild();
    Node firstParameterNode = constructorNode.getFirstChild().getFirstChild();

    runtimeTypeCheck.process(null, rootNode);

    Node checkNode = findCheckerNode(functionType.getParameters()[0].getJSType(), firstParameterNode);
    assertNotNull(checkNode);
  }

  @Test
  public void testAddBoilerplateCode() {
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(null, rootNode);

    Node boilerplateCodeNode = findBoilerplateCodeNode(rootNode);
    assertNotNull(boilerplateCodeNode);
  }

  private Node findPrototypeNode(Node functionNode) {
    Node prototypeNode = null;
    for (Node child = functionNode.getFirstChild(); child != null;
         child = child.getNext()) {
      if (child.getType() == Token.GETPROP &&
          "prototype".equals(child.getFirstChild().getLeafValue())) {
        prototypeNode = child;
        break;
      }
    }
    return prototypeNode;
  }

  private Node findCheckerNode(JSType type, Node node) {
    Node markerNode = null;
    for (Node child = node.getFirstChild(); child != null;
         child = child.getNext()) {
      if (child.getType() == Token.GETELEM &&
          "instance_of__".equals(child.getFirstChild().getLeafValue()) &&
          type.toString().equals(child.getNext().getLeafValue())) {
        markerNode = child;
        break;
      }
    }
    return markerNode;
  }

  private Node findBoilerplateCodeNode(Node rootNode) {
    for (Node child = rootNode.getFirstChild(); child != null;
         child = child.getNext()) {
      if (child.getType() == Token.SCRIPT) {
        return child;
      }
    }
    return null;
  }

  private void assertCheckerNodesPresent(FunctionType functionType, String... markerNames) {
    TreeSet<String> markerNamesSet = Sets.newTreeSet();
    Iterables.addAll(markerNamesSet, markerNames);

    TreeSet<String> markerNamesInFunctionSet = new TreeSet<>();
    for (Node child = functionType.getPrototype().getFirstChild();
         child != null; child = child.getNext()) {
      if (child.getType() == Token.GETELEM &&
          (child.getFirstChild().getLeafValue().startsWith("instance_of__") ||
              child.getFirstChild().getLeafValue().startsWith("implements__"))) {
        markerNamesInFunctionSet.add(child.getFirstChild().getLeafValue().substring(11));
      }
    }

    Comparator<String> comparator = new Comparator<String>() {
      @Override
      public int compare(String s1, String s2) {
        return s1.compareTo(s2);
      }
    };

    assertSame(comparator.compare(markerNamesSet, markerNamesInFunctionSet), 0);
  }

  private static NodeUtil nodeUtil = new NodeUtil();

  private static class NodeUtil {

    Node newQualifiedNameNode(String name, int startPosition, int endPosition) {
      Node nameNode = new Node(Token.NEW, new Node(Token.IDENT, name), null);
      return new Node(Token.QUALIFIED, Node.newIntLiteral(startPosition),
          Node.newIntLiteral(endPosition), nameNode);
    }

    Node newFunctionDeclaration(Node nameNode, FunctionType functionType) {
      Node functionDeclarationNode = new Node(Token.FUNCTION, null, null, null);
      Node functionNode = new Node(Token.NAME, functionDeclarationNode, null);
      functionNode.addAttribute(new Node(Token.ATTRIBUTE, "name", null, nameNode));
      functionDeclarationNode.setJSType(functionType);
      return functionDeclarationNode;
    }

    Node getFnParameters(Node functionDeclarationNode) {
      Node argumentsNode = functionDeclarationNode.getLastChild();
      Node parametersNode = argumentsNode.getFirstChild();
      return parametersNode;
    }

    Node getFunctionName(Node functionDeclarationNode) {
      Node nameNode = functionDeclarationNode.getFirstChild();
      return nameNode;
    }

    Node getSourceName(Node constructorNode) {
      Node sourceNameNode = constructorNode.getFirstChild()
          .getFirstChild();
      return sourceNameNode;
    }

    Node cloneTree(Node node) {
      return Node.cloneTree(node, compiler);
    }

    Node findEnclosingConstructorDeclaration(Node n) {
      while (n.getParent().getType() != Token.SCRIPT &&
             n.getParent().getType() != Token.BLOCK) {
        n = n.getParent();
      }
      return n;
    }

    Node findNodeToInsertAfter(Node n) {
      Node nodeToInsertAfter = findEnclosingConstructorDeclaration(n);
      Node next = nodeToInsertAfter.getNext();
      while (next != null && isClassDefiningCall(next)) {
        nodeToInsertAfter = next;
        next = nodeToInsertAfter.getNext();
      }
      return nodeToInsertAfter;
    }

    boolean isClassDefiningCall(Node next) {
      return NodeUtil.isExprCall(next) &&
          compiler.getCodingConvention().getclasssDefinedByCall(
              next.getFirstChild()) != null;
    }
  }
}