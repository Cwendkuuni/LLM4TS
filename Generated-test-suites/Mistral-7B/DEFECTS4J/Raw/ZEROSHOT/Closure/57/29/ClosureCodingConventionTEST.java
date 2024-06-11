import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.jscomp.SubclassRelationship;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class ClosureCodingConventionTest {

  @Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = createMockFunctionType("ParentCtor");
    FunctionType childCtor = createMockFunctionType("ChildCtor");
    ClosureCodingConvention convention = new ClosureCodingConvention();

    convention.applySubclassRelationship(parentCtor, childCtor, SubclassType.INHERITS);

    assertNotNull(childCtor.getPrototype().getDeclaredProperties());
    assertEquals("superClass_", childCtor.getPrototype().getDeclaredProperties().get("superClass_").getName());
    assertEquals(parentCtor, childCtor.getPrototype().getDeclaredProperties().get("superClass_").getValue());

    assertEquals("constructor", childCtor.getPrototype().getDeclaredProperties().get("constructor").getName());
    assertEquals(childCtor, childCtor.getPrototype().getDeclaredProperties().get("constructor").getValue());
  }

  @Test
  public void testGetclasssDefinedByCall() {
    Node node = createMockNode("goog.inherits(ChildClass, ParentClass)");

    ClosureCodingConvention convention = new ClosureCodingConvention();
    SubclassRelationship relationship = convention.getclasssDefinedByCall(node);

    assertNotNull(relationship);
    assertEquals(SubclassType.INHERITS, relationship.getType());
    assertEquals("ChildClass", relationship.getSubclass().getQualifiedName());
    assertEquals("ParentClass", relationship.getSuperclass().getQualifiedName());
  }

  @Test
  public void testExtractClassNameIfRequire() {
    Node node = createMockNode("goog.require('Foo')");

    ClosureCodingConvention convention = new ClosureCodingConvention();
    String className = convention.extractClassNameIfRequire(node, null);

    assertEquals("Foo", className);
  }

  @Test
  public void testExtractClassNameIfProvide() {
    Node node = createMockNode("goog.provide('Foo')");

    ClosureCodingConvention convention = new ClosureCodingConvention();
    String className = convention.extractClassNameIfProvide(node, null);

    assertEquals("Foo", className);
  }

  @Test
  public void testIsSuperClassReference() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    assertTrue(convention.isSuperClassReference("superClass_"));
    assertFalse(convention.isSuperClassReference("otherProperty"));
  }

  @Test
  public void testEndsWithPrototype() {
    Node node = createMockNode("a.b.c.prototype");

    ClosureCodingConvention convention = new ClosureCodingConvention();
    assertTrue(convention.endsWithPrototype(node));

    node = createMockNode("a.b.c");
    assertFalse(convention.endsWithPrototype(node));
  }

  @Test
  public void testIdentifyTypeDeclarationCall() {
    Node node = createMockNode("goog.addDependency('Foo', [Bar, Baz])");

    ClosureCodingConvention convention = new ClosureCodingConvention();
    List<String> typeNames = convention.identifyTypeDeclarationCall(node);

    assertEquals(2, typeNames.size());
    assertEquals("Bar", typeNames.get(0));
    assertEquals("Baz", typeNames.get(1));
  }

  @Test
  public void testGetAbstractMethodName() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    assertEquals("goog.abstractMethod", convention.getAbstractMethodName());
  }

  @Test
  public void testSingletonGetterClassName() {
    Node node = createMockNode("goog.addSingletonGetter(Foo.prototype, 'bar')");

    ClosureCodingConvention convention = new ClosureCodingConvention();
    String className = convention.getSingletonGetterClassName(node);

    assertEquals("Foo", className);
  }

  @Test
  public void testApplySingletonGetter() {
    FunctionType functionType = createMockFunctionType("Foo");
    FunctionType getterType = createMockFunctionType("getter");
    ObjectType objectType = createMockObjectType("Object");
    ClosureCodingConvention convention = new ClosureCodingConvention();

    convention.applySingletonGetter(functionType, getterType, objectType);

    assertEquals(2, functionType.getDeclaredProperties().size());
    assertEquals("getInstance", functionType.getDeclaredProperties().get(0).getName());
    assertEquals(getterType, functionType.getDeclaredProperties().get(0).getValue());

    assertEquals("instance_", functionType.getDeclaredProperties().get(1).getName());
    assertEquals(objectType, functionType.getDeclaredProperties().get(1).getValue());
  }

  @Test
  public void testIsPropertyTestFunction() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    Node node = createMockNode("goog.isString(foo)");
    assertTrue(convention.isPropertyTestFunction(node));

    node = createMockNode("goog.isFunction(foo)");
    assertTrue(convention.isPropertyTestFunction(node));
  }

  @Test
  public void testObjectLiteralCast() {
    NodeTraversal traversal = createMockNodeTraversal();
    Node callNode = createMockNode("goog.reflect.object(foo) as { bar: string }");

    ClosureCodingConvention convention = new ClosureCodingConvention();
    ObjectLiteralCast objectLiteralCast = convention.getObjectLiteralCast(traversal, callNode);

    assertEquals("foo", objectLiteralCast.getNode().getQualifiedName());
    assertEquals("{ bar: string }", objectLiteralCast.getType());
  }

  @Test
  public void testOptionalParameter() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    assertFalse(convention.isOptionalParameter(createMockNode("function foo(bar: number) {}")));
  }

  @Test
  public void testVarArgsParameter() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    assertFalse(convention.isVarArgsParameter(createMockNode("function foo(bar: number, ...args: number[]) {}")));
  }

  @Test
  public void testIsPrivate() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    assertFalse(convention.isPrivate("foo"));
  }

  @Test
  public void testAssertionFunctions() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    List<AssertionFunctionSpec> functions = convention.getAssertionFunctions();

    assertEquals(7, functions.size());
    assertTrue(functions.get(0).isFunction("goog.asserts.assert"));
    assertTrue(functions.get(1).isFunction("goog.asserts.assertNumber", JSTypeNative.NUMBER_TYPE));
    assertTrue(functions.get(2).isFunction("goog.asserts.assertString", JSTypeNative.STRING_TYPE));
    assertTrue(functions.get(3).isFunction("goog.asserts.assertFunction", JSTypeNative.FUNCTION_INSTANCE_TYPE));
    assertTrue(functions.get(4).isFunction("goog.asserts.assertObject", JSTypeNative.OBJECT_TYPE));
    assertTrue(functions.get(5).isFunction("goog.asserts.assertArray", JSTypeNative.ARRAY_TYPE));
    assertTrue(functions.get(6).isFunction("goog.asserts.assertInstanceof", JSTypeNative.OBJECT_TYPE));
  }

  @Test
  public void testFunctionBind() {
    Node node = createMockNode("goog.bind(foo, this, [arg1, arg2])");

    ClosureCodingConvention convention = new ClosureCodingConvention();
    Bind bind = convention.describeFunctionBind(node);

    assertEquals("foo", bind.getFunction().getQualifiedName());
    assertEquals(this, bind.getThisValue());
    assertEquals("arg1, arg2", bind.getParameters());
  }

  private FunctionType createMockFunctionType(String name) {
    ObjectType prototype = createMockObjectType("Prototype");
    ObjectType instanceType = new ObjectType(name, prototype);
    instanceType.setPrototype(prototype);
    return new FunctionType(null, instanceType);
  }

  private ObjectType createMockObjectType(String name) {
    return new ObjectType(name, null);
  }

  private Node createMockNode(String nodeStr) {
    Node node = new Node();
    NodeTraversal traversal = new NodeTraversal(null, null);
    Node parent = new Node();
    node.setParent(parent);
    node.setSourcePosition(0, 0);
    node.setSourceName("Test.js");
    node.setType(Token.CALL);
    node.setFirstChild(NodeUtil.createStringNode(nodeStr));
    traversal.setCurrentPosition(0, 0);
    traversal.setCurrentNode(node);
    NodeUtil.setParentNodes(node, traversal);
    return node;
  }

  private NodeTraversal createMockNodeTraversal() {
    return new NodeTraversal(null, null);
  }

  private static class NodeTraversal {
    Node currentNode;
    Node currentPosition;

    NodeTraversal(Node parent, Node position) {
      this.currentNode = parent;
      this.currentPosition = position;
    }

    void setParentNodes(Node node) {
      Node parent = this.currentNode;
      Node position = this.currentPosition;
      node.setParent(parent);
      node.setSourcePosition(position.getLine(), position.getColumn());
      node.setSourceName(parent.getSourceName());
      NodeUtil.setParentNodes(node, this);
    }
  }

  private static class NodeUtil {
    static Node createStringNode(String nodeStr) {
      Node node = new Node();
      node.setType(Token.STRING);
      node.setString(nodeStr);
      return node;
    }

    static Node createQualifiedNameNode(String nodeStr) {
      Node node = new Node();
      node.setType(Token.GETPROP);
      Node name = NodeUtil.createNameNode(nodeStr.substring(0, nodeStr.lastIndexOf(".") + 1));
      node.setFirstChild(name);
      return node;
    }

    static Node createNameNode(String nodeStr) {
      Node node = new Node();
      node.setType(Token.NAME);
      node.setString(nodeStr);
      return node;
    }

    static Node createArrayLiteralNode(List<Node> nodes) {
      Node node = new Node();
      node.setType(Token.ARRAYLIT);
      for (Node child : nodes) {
        node.addChild(child);
      }
      return node;
    }

    static Node createObjectLiteralNode(Map<String, Node> properties) {
      Node node = new Node();
      node.setType(Token.OBJECTLIT);
      for (Map.Entry<String, Node> entry : properties.entrySet()) {
        node.addChild(createObjectPropertyNode(entry.getKey(), entry.getValue()));
      }
      return node;
    }

    static Node createObjectPropertyNode(String name, Node value) {
      Node node = new Node();
      node.setType(Token.GETPROP);
      Node propName = NodeUtil.createNameNode(name);
      node.setFirstChild(propName);
      node.setLastChild(value);
      return node;
    }
  }
}