import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.jscomp.FunctionType;
import com.google.javascript.jscomp.JSTypeNative;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ObjectType;
import com.google.javascript.jscomp.SubclassRelationship;
import com.google.javascript.jscomp.SubclassType;

public class ClosureCodingConventionTest {

  private ClosureCodingConvention convention = new ClosureCodingConvention();

  @org.junit.Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = new FunctionType("ParentCtor", ImmutableList.of(),
        new ObjectType("Object"), null);
    FunctionType childCtor = new FunctionType("ChildCtor", ImmutableList.of(),
        new ObjectType("Object"), null);

    ConventionManager.instance().registerCodingConvention(
        new DefaultCodingConvention());
    ConventionManager.instance().registerCodingConvention(convention);

    convention.applySubclassRelationship(parentCtor, childCtor, SubclassType.INHERITS);
    assertEquals(parentCtor, childCtor.getPrototype());
    assertEquals(childCtor.getPrototype(), childCtor.getPrototype().getPrototype());
    assertEquals(parentCtor, childCtor.getPrototype().getPrototype());
    assertSame(parentCtor.getSource(), childCtor.getPrototype().getSource());
    assertEquals(childCtor, childCtor.getPrototype().getDeclaredPropertyNames().get(0));
    assertEquals("constructor", childCtor.getPrototype().getDeclaredPropertyNames().get(1));
    assertEquals("superClass_", childCtor.getPrototype().getDeclaredPropertyNames().get(2));

    convention.applySubclassRelationship(parentCtor, childCtor, SubclassType.MIXIN);
    assertEquals(parentCtor.getPrototype(), childCtor.getPrototype());
    assertEquals(parentCtor, childCtor.getPrototype().getPrototype());
    assertSame(parentCtor.getSource(), childCtor.getPrototype().getSource());
    assertNull(childCtor.getPrototype().getDeclaredPropertyNames().get(0));
    assertNull(childCtor.getPrototype().getDeclaredPropertyNames().get(1));
    assertNull(childCtor.getPrototype().getDeclaredPropertyNames().get(2));
  }

  @org.junit.Test
  public void testExtractClassNameIfRequire() {
    Node node = Node.createCall("goog.require",
        Node.createQualifiedName("X"), null, null, null);
    String className = convention.extractClassNameIfRequire(node, null);
    assertEquals("X", className);
  }

  @org.junit.Test
  public void testExtractClassNameIfProvide() {
    Node node = Node.createCall("goog.provide",
        Node.createQualifiedName("X"), null, null, null);
    String className = convention.extractClassNameIfProvide(node, null);
    assertEquals("X", className);
  }

  @org.junit.Test
  public void testSingletonGetter() {
    FunctionType singletonGetter = new FunctionType("SingletonGetter",
        ImmutableList.of(), new ObjectType("Object"), null);
    singletonGetter.defineDeclaredProperty("instance_", new ObjectType("Object"),
        singletonGetter.getSource());
    singletonGetter.defineDeclaredProperty("getInstance", singletonGetter,
        singletonGetter.getSource());

    FunctionType getter = new FunctionType("Getter", ImmutableList.of(),
        new ObjectType("Object"), null);
    SubclassRelationship relationship = new SubclassRelationship(SubclassType.INHERITS,
        Node.createQualifiedName("SubClass"), Node.createQualifiedName("SingletonGetter"));
    getter.applySubclassRelationship(singletonGetter, getter, relationship);

    Node getterInstance = Node.createCall(getter, null, null, null, null);
    ObjectType instance = Node.createObjectLiteral(ImmutableList.of(), null);
    instance.defineProperty("foo", Node.createString("bar"), null);
    getterInstance.setContextProperty("instance_", instance);

    ObjectType result = (ObjectType) NodeTraversal.scan(getNode(getNode(getterInstance, 0)), singletonGetter);
    assertEquals("instance_", result.getDeclaredPropertyNames().get(0).getName());
    assertEquals("getInstance", result.getDeclaredPropertyNames().get(1).getName());
    assertEquals("Object", result.getType().getClassName());
  }

  @org.junit.Test
  public void testPropertyTestFunctions() {
    FunctionType propertyTestFunction = new FunctionType("isNumber",
        ImmutableList.of(JSTypeNative.OBJECT_TYPE),
        JSTypeNative.BOOLEAN_TYPE, null);
    propertyTestFunction.setBody(Node.createCall(
        Node.createQualifiedName("goog.isNumber"), Node.createNull(), null, null, null));

    Node call = Node.createCall(propertyTestFunction, Node.createObjectLiteral(ImmutableList.of(), null), null, null, null);
    assertTrue(call.getBoolean());

    propertyTestFunction.setBody(Node.createCall(
        Node.createQualifiedName("goog.isDef"), Node.createNull(), null, null, null));
    assertFalse(call.getBoolean());

    propertyTestFunction.setBody(Node.createCall(
        Node.createQualifiedName("goog.isDefAndNotNull"), Node.createNull(), null, null, null));
    assertFalse(call.getBoolean());

    ObjectType object = new ObjectType("Object", null);
    object.defineProperty("foo", Node.createString("bar"), null);
    Node objectNode = Node.createObjectLiteral(ImmutableList.of("foo"), object);
    call = Node.createCall(propertyTestFunction, objectNode, null, null, null);
    assertTrue(call.getBoolean());

    call = Node.createCall(propertyTestFunction, Node.createNull(), null, null, null);
    assertFalse(call.getBoolean());
  }

  @org.junit.Test
  public void testObjectLiteralCast() {
    ObjectType objectType = new ObjectType("Object", null);
    FunctionType functionType = new FunctionType("FunctionType", ImmutableList.of(),
        objectType, null);

    Node objectLiteralNode = Node.createObjectLiteral(ImmutableList.of(), null);
    Node objectLiteralCast = convention.getObjectLiteralCast(NodeTraversal.getTraverser(),
        Node.createCall(Node.createQualifiedName("goog.reflect.object"), "cast",
            Node.createArrayLiteral(Node.createString("ObjectType"), objectLiteralNode), null, null));

    assertEquals("ObjectType", objectLiteralCast.getQualifiedName());
    assertEquals(objectLiteralNode, objectLiteralCast.getSecond());

    Node functionNode = Node.createCall(functionType, null, null, null, null);
    Node castNode = Node.createCall(Node.createQualifiedName("goog.reflect.object"), "cast",
        Node.createArrayLiteral(Node.createString("FunctionType"), functionNode), null, null);
    ObjectType castResultType = (ObjectType) NodeTraversal.scan(castNode, functionType);
    assertEquals("FunctionType", castResultType.getType().getClassName());
  }

  @org.junit.Test
  public void testIsSuperClassReference() {
    FunctionType superClass = new FunctionType("SuperClass", ImmutableList.of(),
        new ObjectType("Object"), null);
    superClass.defineDeclaredProperty("superClass_", new ObjectType("Object"),
        superClass.getSource());
    FunctionType subClass = new FunctionType("SubClass", ImmutableList.of(),
        new ObjectType("Object"), null);

    Node node = Node.createCall(Node.createQualifiedName("isSuperClassReference"),
        Node.createQualifiedName("superClass_"), null, null, null);
    assertTrue(convention.isSuperClassReference(node.getString()));
  }

  @org.junit.Test
  public void testIsPrivate() {
    FunctionType functionType = new FunctionType("FunctionType", ImmutableList.of(),
        new ObjectType("Object"), null);
    assertFalse(convention.isPrivate("foo"));

    functionType.definePrivateProperty("foo", Node.createNumber(42), null);
    assertTrue(convention.isPrivate("foo"));
  }

  @org.junit.Test
  public void testIdentifyTypeDeclarationCall() {
    String code = "goog.addDependency('X', ['Y']);";
    Node node = Node.process(code, null, null);
    List<String> result = convention.identifyTypeDeclarationCall(node);
    assertEquals(ImmutableList.of("X"), result);
  }

  private Node getNode(Node node, int index) {
    if (node == null) {
      return null;
    }
    for (int i = 0; i < index; i++) {
      node = node.getNext();
    }
    return node;
  }
}