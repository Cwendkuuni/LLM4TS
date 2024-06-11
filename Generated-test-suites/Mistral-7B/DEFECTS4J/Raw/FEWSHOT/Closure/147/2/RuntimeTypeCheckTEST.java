package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import org.junit.Test;

public class RuntimeTypeCheckTest {

  @Test
  public void testAddMarkers() {
    // Test adding markers for a user-defined class
    Node root = createTestNode(
        "function MyClass() {}",
        "function MyClassMethod() {}",
        "MyClass.prototype.MyClassMethod = MyClassMethod;");

    RuntimeTypeCheck typeChecker = new RuntimeTypeCheck(null, null);
    typeChecker.process(null, root);

    Node myClassNode = NodeUtil.newQualifiedNameNode("MyClass", -1, -1);
    Node myClassMarker = findMarker(myClassNode, "instance_of__MyClass");
    assertNotNull(myClassMarker);

    // Test adding markers for an interface implemented by a user-defined class
    root = createTestNode(
        "interface MyInterface {}",
        "function MyClass() implements MyInterface {}",
        "MyClass.prototype.MyClassMethod = function() {};");

    typeChecker = new RuntimeTypeCheck(null, null);
    typeChecker.process(null, root);

    myClassNode = NodeUtil.newQualifiedNameNode("MyClass", -1, -1);
    myClassMarker = findMarker(myClassNode, "implements__MyInterface");
    assertNotNull(myClassMarker);
  }

  @Test
  public void testAddChecks() {
    // Test adding checks for a user-defined class method parameter
    Node root = createTestNode(
        "function MyClass(x) {}",
        "function MyClassMethod(y) {}",
        "MyClass.prototype.MyClassMethod = function(z) { return x + y + z; };");

    RuntimeTypeCheck typeChecker = new RuntimeTypeCheck(null, null);
    typeChecker.process(null, root);

    Node myClassNode = NodeUtil.newQualifiedNameNode("MyClass", -1, -1);
    Node myClassMethodNode = NodeUtil.newQualifiedNameNode(
        "MyClass.prototype.MyClassMethod", -1, -1);

    Node xChecker = findCheckerNode(myClassNode, "x");
    assertNotNull(xChecker);

    Node yChecker = findCheckerNode(myClassMethodNode, "y");
    assertNotNull(yChecker);

    Node zChecker = findCheckerNode(myClassMethodNode, "z");
    assertNotNull(zChecker);

    // Test adding checks for a user-defined class return value
    root = createTestNode(
        "function MyClass() {}",
        "function MyClassMethod() { return MyClass.x; }",
        "MyClass.x = 42;");

    typeChecker = new RuntimeTypeCheck(null, null);
    typeChecker.process(null, root);

    myClassNode = NodeUtil.newQualifiedNameNode("MyClass", -1, -1);
    Node myClassMethodNode = NodeUtil.newQualifiedNameNode(
        "MyClass.prototype.MyClassMethod", -1, -1);

    Node returnChecker = findCheckerNode(myClassMethodNode, null);
    assertNotNull(returnChecker);
  }

  @Test
  public void testAddChecksForUnionTypes() {
    // Test adding checks for a union type parameter
    Node root = createTestNode(
        "function MyClass(x) {}",
        "function MyClassMethod(y) {}",
        "MyClass.prototype.MyClassMethod = function(z) { return x + y + z; };",
        "function Foo() {}",
        "function Bar() {}",
        "function MyClassWithUnionType(x) {}",
        "MyClassWithUnionType.prototype.MyClassMethod = function(y) { return x + y; };",
        "MyClassWithUnionType.prototype.canBeFoo = function() { return true; };",
        "MyClassWithUnionType.prototype.canBeBar = function() { return true; };");

    RuntimeTypeCheck typeChecker = new RuntimeTypeCheck(null, null);
    typeChecker.process(null, root);

    Node myClassWithUnionTypeNode = NodeUtil.newQualifiedNameNode(
        "MyClassWithUnionType", -1, -1);

    Node xChecker = findCheckerNode(myClassWithUnionTypeNode, "x");
    assertNotNull(xChecker);

    Node fooChecker = findCheckerNode(myClassWithUnionTypeNode, "canBeFoo");
    assertNotNull(fooChecker);

    Node barChecker = findCheckerNode(myClassWithUnionTypeNode, "canBeBar");
    assertNotNull(barChecker);
  }

  @Test
  public void testAddChecksForExternedTypes() {
    // Test adding checks for an externed class parameter
    Node root = createTestNode(
        "function MyFunction(myObj) {}",
        "extern MyObj {}",
        "MyFunction.prototype.MyFunctionMethod = function() { return myObj.foo; };");

    RuntimeTypeCheck typeChecker = new RuntimeTypeCheck(null, null);
    typeChecker.process(null, root);

    Node myFunctionNode = NodeUtil.newQualifiedNameNode("MyFunction", -1, -1);

    Node myObjChecker = findCheckerNode(myFunctionNode, "myObj");
    assertNotNull(myObjChecker);
  }

  @Test
  public void testAddChecksForInterfaceTypes() {
    // Test adding checks for an interface implemented by a user-defined class
    Node root = createTestNode(
        "interface MyInterface {}",
        "function MyClass() implements MyInterface {}",
        "MyClass.prototype.MyClassMethod = function() {};");

    RuntimeTypeCheck typeChecker = new RuntimeTypeCheck(null, null);
    typeChecker.process(null, root);

    Node myClassNode = NodeUtil.newQualifiedNameNode("MyClass", -1, -1);
    Node myClassMarker = findMarker(myClassNode, "implements__MyInterface");
    assertNotNull(myClassMarker);

    Node myClassMethodNode = NodeUtil.newQualifiedNameNode(
        "MyClass.prototype.MyClassMethod", -1, -1);
    Node myClassMethodMarker = findMarker(myClassMethodNode, "implements__MyInterface");
    assertNotNull(myClassMethodMarker);
  }

  @Test
  public void testAddChecksForNullType() {
    // Test adding checks for a null type parameter
    Node root = createTestNode(
        "function MyClass(x) {}",
        "function MyClassMethod(y) {}",
        "MyClass.prototype.MyClassMethod = function(z) { return x + y + z; };",
        "function MyClassWithNullType(x) {}",
        "MyClassWithNullType.prototype.MyClassMethod = function(y) { return x + y; };",
        "MyClassWithNullType.prototype.canBeNull = function() { return true; };");

    RuntimeTypeCheck typeChecker = new RuntimeTypeCheck(null, null);
    typeChecker.process(null, root);

    Node myClassWithNullTypeNode = NodeUtil.newQualifiedNameNode(
        "MyClassWithNullType", -1, -1);

    Node xChecker = findCheckerNode(myClassWithNullTypeNode, "x");
    assertNotNull(xChecker);

    Node nullChecker = findCheckerNode(myClassWithNullTypeNode, "canBeNull");
    assertNotNull(nullChecker);
  }

  @Test
  public void testAddChecksForVoidType() {
    // Test adding checks for a void type return value
    Node root = createTestNode(
        "function MyClass() {}",
        "function MyClassMethod() { return; }",
        "MyClass.prototype.MyClassMethod = function() {};");

    RuntimeTypeCheck typeChecker = new RuntimeTypeCheck(null, null);
    typeChecker.process(null, root);

    Node myClassNode = NodeUtil.newQualifiedNameNode("MyClass", -1, -1);
    Node myClassMethodNode = NodeUtil.newQualifiedNameNode(
        "MyClass.prototype.MyClassMethod", -1, -1);

    Node returnChecker = findCheckerNode(myClassMethodNode, null);
    assertNull(returnChecker);
  }

  private Node createTestNode(String... nodes) {
    Node root = NodeUtil.compile(nodes);
    return root;
  }

  private Node findMarker(Node node, String markerName) {
    while (node != null) {
      if (node.getType() == Token.GETPROP) {
        if (node.getFirstChild().toString().equals(markerName)) {
          return node;
        }
      }

      node = node.getParent();
    }

    return null;
  }

  private Node findCheckerNode(Node node, @Nullable String propertyName) {
    while (node != null) {
      if (node.getType() == Token.CALL) {
        if (node.getFirstChild().toString().equals("checkType")) {
          Node checkersNode = node.getLastChild();
          for (int i = 0; i < checkersNode.getNumberOfChildren(); i++) {
            Node checkerNode = checkersNode.getChild(i);
            if (propertyName == null) {
              if (checkerNode.getType() == Token.GETPROP) {
                if (checkerNode.getFirstChild().toString().equals("valueChecker")) {
                  if (node.getChild(1).getChild(0).toString().equals(
                      node.getParent().getChild(0).toString())) {
                    return checkerNode;
                  }
                }
              }
            } else {
              if (checkerNode.getType() == Token.GETPROP &&
                  checkerNode.getFirstChild().toString().equals(propertyName)) {
                return checkerNode;
              }
            }
          }
        }
      }

      node = node.getParent();
    }

    return null;
  }
}