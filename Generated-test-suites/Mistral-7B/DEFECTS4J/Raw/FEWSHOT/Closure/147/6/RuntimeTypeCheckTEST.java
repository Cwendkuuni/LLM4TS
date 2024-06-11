package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.UnionType;

import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

@RunWith(JUnit4.class)
public class RuntimeTypeCheckTest {
  @Test
  public void testAddMarkers() throws IOException {
    // Given a node with a FunctionType
    String className = "MyClass";
    Node classNode = Node.newQualifiedNameNode(className, -1, -1);
    Node constructorNode = Node.newFunction(Token.FUNCTION, null, null, null);
    FunctionType constructorType = new FunctionType(constructorNode, null);

    // When adding markers for the constructor
    Node rootNode = new Node(Token.SCRIPT, null);
    RuntimeTypeCheck pass = new RuntimeTypeCheck(null, null);
    NodeTraversal.traverse(pass, rootNode, new AddMarkers(pass));

    // Then the marker properties should be present in the constructor node
    Node marker = rootNode.getFirstChild().getLastChild();
    assertEquals(className, marker.getFirstChild().getString());
    assertEquals("instance_of__" + className, marker.getLastChild().getString());

    // Given a node with an interface type
    className = "MyInterface";
    ObjectType interfaceType = new ObjectType(className, null);

    // When adding markers for the constructor and the interface
    marker = null;
    rootNode = new Node(Token.SCRIPT, null);
    pass = new RuntimeTypeCheck(null, null);
    NodeTraversal.traverse(pass, rootNode, new AddMarkers(pass));

    // Then the marker properties should be present in the constructor node
    marker = rootNode.getFirstChild().getLastChild();
    assertEquals(className, marker.getFirstChild().getString());
    assertEquals("implements__" + interfaceType.getReferenceName(),
        marker.getLastChild().getString());
  }

  @Test
  public void testAddChecks() throws IOException {
    // Given a function node with a FunctionType
    String functionName = "MyFunction";
    String className = "MyClass";
    Node functionNode = Node.newFunction(Token.FUNCTION, null, null, null);
    FunctionType functionType = new FunctionType(functionNode, null);

    // When adding checks for the parameters
    Node rootNode = new Node(Token.SCRIPT, null);
    RuntimeTypeCheck pass = new RuntimeTypeCheck(null, null);
    NodeTraversal.traverse(pass, rootNode, new AddChecks(pass));

    // Then the checkType calls should be present in the function node
    Node checkNode = null;
    Node block = functionNode.getLastChild();
    for (Node paramType : functionType.getParameters()) {
      Node paramNode = block.getFirstChild();
      if (paramNode == null) {
        continue;
      }

      checkNode = createCheckTypeCallNode(
          paramType.getJSType(), paramNode.cloneTree());

      if (checkNode == null) {
        paramNode = paramNode.getNext();
        continue;
      }

      assertNotNull(checkNode);
      block.addChildAfter(checkNode, paramNode);
      paramNode = paramNode.getNext();
    }

    // Given a return node
    Node returnNode = new Node(Token.RETURN, null, null);
    functionNode.getLastChild().replaceChild(
        functionNode.getLastChild(), returnNode);

    // When adding checks for the return value
    NodeTraversal.traverse(pass, rootNode, new AddChecks(pass));

    // Then the checkType call should be present in the return node
    checkNode = null;
    block = functionNode.getLastChild();
    checkNode = createCheckTypeCallNode(
        functionType.getReturnType(), returnNode.cloneTree());

    assertNotNull(checkNode);
    returnNode.replaceChild(returnNode, checkNode);
  }

  private Node createCheckTypeCallNode(JSType type, Node expr) {
    if (type == null) {
      return null;
    }

    if (type.isUnionType()) {
      Collection<JSType> alternates = Sets.newTreeSet(ALPHA);
      alternates.addAll(((UnionType) type).getAlternates());
      for (JSType alternate : alternates) {
        Node checkerNode = createCheckerNode(alternate);
        if (checkerNode == null) {
          return null;
        }
        if (expr != null) {
          checkerNode = new Node(Token.CALL, checkerNode, expr);
        }
        return checkerNode;
      }
    }

    return createCheckerNode(type);
  }

  private Node createCheckerNode(JSType type) {
    if (type.isNullType()) {
      return Node.newCall(jsCode("valueChecker"), Node.newString("null"));

    } else if (type.isBooleanValueType()
        || type.isNumberValueType()
        || type.isStringValueType()
        || type.isVoidType()) {
      return new Node(Token.CALL, jsCode("valueChecker"),
          Node.newString(type.toString()));

    } else if (type.isInstanceType()) {
      ObjectType objType = (ObjectType) type;
      String refName = objType.getReferenceName();
      String sourceName = NodeUtil.getSourceName(objType.getConstructor().getSource());
      CompilerInput sourceInput = new CompilerInput(sourceName, null, null);
      if (sourceInput.isExtern()) {
        return new Node(Token.CALL, jsCode("externClassChecker"),
            Node.newString(refName));
      }

      return new Node(Token.CALL, jsCode("classChecker"),
          Node.newString(refName));

    } else {
      // We don't check this type (e.g. unknown & all types).
      return null;
    }
  }

  private Node jsCode(String prop) {
    return new Node(Token.GETELEM, jsCode("jscomp"), Node.newString(prop));
  }

  @Test
  public void testClassChecker() {
    // Given a class node with a constructor node
    String className = "MyClass";
    Node classNode = Node.newQualifiedNameNode(className, -1, -1);
    Node constructorNode = Node.newFunction(Token.FUNCTION, null, null, null);
    FunctionType constructorType = new FunctionType(constructorNode, null);

    // When calling the classChecker
    Node checkNode = createCheckerNode(new ObjectType(className, null));
    assertNotNull(checkNode);

    // Then the checkType call should not contain any checker nodes
    TreeSet<JSType> types = new TreeSet<>(ALPHA);
    types.addAll(ImmutableList.of(constructorType));
    for (JSType type : types) {
      Node checkerNode = NodeUtil.getChildByType(checkNode, JSType.class);
      assertNull(checkerNode);
    }
  }

  @Test
  public void testInterfaceChecker() {
    // Given a class node with a constructor node and an interface type
    String className = "MyClass";
    String interfaceName = "MyInterface";
    Node classNode = Node.newQualifiedNameNode(className, -1, -1);
    Node constructorNode = Node.newFunction(Token.FUNCTION, null, null, null);
    FunctionType constructorType = new FunctionType(constructorNode, null);
    ObjectType interfaceType = new ObjectType(interfaceName, null);

    // When calling the interfaceChecker
    Node checkNode = createCheckerNode(interfaceType);
    assertNotNull(checkNode);

    // Then the checkType call should contain one checker node
    TreeSet<JSType> types = new TreeSet<>(ALPHA);
    types.addAll(ImmutableList.of(constructorType, interfaceType));
    for (JSType type : types) {
      Node checkerNode = NodeUtil.getChildByType(checkNode, JSType.class);
      if (type.isInstanceType()) {
        assertEquals(interfaceType, checkerNode.getFirstChild());
        break;
      }
    }
  }
}