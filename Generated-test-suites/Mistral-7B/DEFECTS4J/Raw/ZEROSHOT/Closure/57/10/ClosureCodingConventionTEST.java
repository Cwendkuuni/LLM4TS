package com.example.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SubclassType;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;

import java.util.Collection;
import java.util.List;

public class ClosureCodingConventionTest {

  private static final NodeFactory NODE_FACTORY = new NodeFactory();
  private static final NodeTraversal TRAVERSAL = new NodeTraversal(NODE_FACTORY);

  private static FunctionType createFunctionType(String name) {
    ObjectType prototype = new ObjectType(NODE_FACTORY, name + "_prototype");
    return new FunctionType(NODE_FACTORY, name, ImmutableList.of(), prototype);
  }

  @org.junit.Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = createFunctionType("ParentClass");
    FunctionType childCtor = createFunctionType("ChildClass");
    SubclassType type = SubclassType.INHERITS;

    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.applySubclassRelationship(parentCtor, childCtor, type);

    ObjectType childPrototype = childCtor.getPrototype();
    assertEquals("ParentClass", childPrototype.getName());
    FunctionType childConstructor = childPrototype.getDeclaredProperties().get("constructor");
    assertEquals("ChildClass", childConstructor.getName());
    FunctionType superClass_ = childPrototype.getDeclaredProperties().get("superClass_");
    assertEquals("ParentClass", superClass_.getName());
  }

  @org.junit.Test
  public void testGetclasssDefinedByCall() {
    String parentClassName = "ParentClass";
    String childClassName = "ChildClass";

    Node parentNode = makeNode("goog.inherits", NODE_FACTORY, ImmutableList.of(
        makeNode("ChildClass", NODE_FACTORY),
        makeNode(parentClassName, NODE_FACTORY)
    ));

    Node subclassRelationship = new ClosureCodingConvention().getclasssDefinedByCall(parentNode, null);
    assertEquals(SubclassType.INHERITS, subclassRelationship.getType());
    assertSame(makeNode(childClassName, NODE_FACTORY), subclassRelationship.getSubclass());
    assertSame(makeNode(parentClassName, NODE_FACTORY), subclassRelationship.getSuperclass());

    parentNode = makeNode("goog.mixin", NODE_FACTORY, ImmutableList.of(
        makeNode("ChildClass$prototype", NODE_FACTORY),
        makeNode("ParentClass$prototype", NODE_FACTORY)
    ));
    subclassRelationship = new ClosureCodingConvention().getclasssDefinedByCall(parentNode, null);
    assertEquals(SubclassType.MIXIN, subclassRelationship.getType());
    assertNull(subclassRelationship.getSubclass());
    assertSame(makeNode("ParentClass$prototype", NODE_FACTORY), subclassRelationship.getSuperclass());
  }

  @org.junit.Test
  public void testExtractClassNameIfProvideAndRequire() {
    String className = "MyClassName";

    Node provideNode = makeNode("goog.provide", NODE_FACTORY, ImmutableList.of(makeNode(className, NODE_FACTORY)));
    assertEquals(className, new ClosureCodingConvention().extractClassNameIfProvide(provideNode, null));

    Node requireNode = makeNode("goog.require", NODE_FACTORY, ImmutableList.of(makeNode(className, NODE_FACTORY)));
    assertEquals(className, new ClosureCodingConvention().extractClassNameIfRequire(requireNode, null));
  }

  @org.junit.Test
  public void testIsSuperClassReference() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    assertTrue(convention.isSuperClassReference("superClass_"));
    assertFalse(convention.isSuperClassReference("otherProperty"));
  }

  @org.junit.Test
  public void testEndsWithPrototype() {
    Node qualifiedNameNode1 = makeNode("a.b.c", NODE_FACTORY);
    assertFalse(new ClosureCodingConvention().endsWithPrototype(qualifiedNameNode1));

    Node qualifiedNameNode2 = makeNode("a.b.c.prototype", NODE_FACTORY);
    assertTrue(new ClosureCodingConvention().endsWithPrototype(qualifiedNameNode2));
  }

  @org.junit.Test
  public void testExtractClassNameIfGoog() {
    Node node = makeNode("goog.addSingletonGetter", NODE_FACTORY, ImmutableList.of(
        makeNode("MySingletonGetter", NODE_FACTORY)
    ));
    assertEquals("MySingletonGetter", new ClosureCodingConvention().extractClassNameIfSingletonGetter(node, null));
  }

  @org.junit.Test
  public void testGetExportPropertyFunction() {
    assertEquals("goog.exportProperty", new ClosureCodingConvention().getExportPropertyFunction());
  }

  @org.junit.Test
  public void testGetSingletonGetterClassName() {
    Node node = makeNode("goog.addSingletonGetter", NODE_FACTORY, ImmutableList.of(
        makeNode("MySingletonGetter", NODE_FACTORY),
        makeNode("MyClass", NODE_FACTORY)
    ));
    assertEquals("MyClass", new ClosureCodingConvention().getSingletonGetterClassName(node, null));
  }

  @org.junit.Test
  public void testApplySingletonGetter() {
    FunctionType functionType = createFunctionType("MyClass");
    FunctionType getterType = createFunctionType("MyClass$getter");
    ObjectType objectType = new ObjectType(NODE_FACTORY, "MyClass");

    new ClosureCodingConvention().applySingletonGetter(functionType, getterType, objectType);

    assertEquals("MyClass$getInstance", functionType.getDeclaredProperties().get("getInstance").getName());
    assertEquals("MyClass", functionType.getDeclaredProperties().get("instance_").getType().getName());
  }

  @org.junit.Test
  public void testGetGlobalObject() {
    assertEquals("goog.global", new ClosureCodingConvention().getGlobalObject());
  }

  @org.junit.Test
  public void testIsPropertyTestFunction() {
    Node node = makeNode("goog.isDef", NODE_FACTORY, ImmutableList.of(makeNode("x", NODE_FACTORY)));
    assertTrue(new ClosureCodingConvention().isPropertyTestFunction(node));
  }

  @org.junit.Test
  public void testGetObjectLiteralCast() {
    Node callNode = makeNode("goog.reflect.object.cast", NODE_FACTORY, ImmutableList.of(
        makeNode("x", NODE_FACTORY),
        makeNode("MyType", NODE_FACTORY)
    ));
    ObjectLiteralCast expected = new ObjectLiteralCast("MyType", makeNode("{}", NODE_FACTORY));
    ObjectLiteralCast actual = new ClosureCodingConvention().getObjectLiteralCast(TRAVERSAL, callNode);
    assertSame(expected, actual);
  }

  private static Node makeNode(String name, NodeFactory nodeFactory, List<Node> children) {
    Node node = nodeFactory.makeName(name);
    if (children != null) {
      for (Node child : children) {
        node.addChild(child);
      }
    }
    return node;
  }

  private static void assertSame(Node expected, Node actual) {
    if (!expected.equals(actual)) {
      throw new AssertionError("Expected " + expected + " but got " + actual);
    }
  }

  private static void assertEquals(SubclassType expected, SubclassType actual) {
    assertEquals(expected.name(), expected.toString());
    assertEquals(expected.getType(), actual.getType());
    assertEquals(expected.getSubclass(), actual.getSubclass());
    assertEquals(expected.getSuperclass(), actual.getSuperclass());
  }

  private static enum SubclassType {
    INHERITS("inherits"),
    MIXIN("mixin");

    private final String name;
    private final SubclassType type;
    private final Node subclass;
    private final Node superclass;

    SubclassType(String name) {
      this.name = name;
      this.type = this;
      this.subclass = null;
      this.superclass = null;
    }

    SubclassType(String name, Node subclass, Node superclass) {
      this.name = name;
      this.type = INHERITS;
      this.subclass = subclass;
      this.superclass = superclass;
    }

    public String getName() {
      return name;
    }

    public SubclassType getType() {
      return type;
    }

    public Node getSubclass() {
      return subclass;
    }

    public Node getSuperclass() {
      return superclass;
    }

    @Override
    public String toString() {
      if (type == INHERITS) {
        return "SubclassType.INHERITS(" + subclass.getString() + ", " + superclass.getString() + ")";
      } else {
        return "SubclassType.MIXIN(" + superclass.getString() + ")";
      }
    }
  }

  private static class AssertionFunctionSpec {
    private final String name;
    private final JSTypeNative returnType;

    AssertionFunctionSpec(String name, JSTypeNative returnType) {
      this.name = name;
      this.returnType = returnType;
    }

    public String getName() {
      return name;
    }

    public JSTypeNative getReturnType() {
      return returnType;
    }
  }

  private static boolean equals(Object a, Object b) {
    if (a == b) {
      return true;
    }
    if (a == null || b == null) {
      return false;
    }
    return a.equals(b);
  }

  private static boolean assertFalse(boolean condition) {
    if (condition) {
      throw new AssertionError("Expected false but got true");
    }
  }

  private static boolean assertTrue(boolean condition) {
    if (!condition) {
      throw new AssertionError("Expected true but got false");
    }
  }
}