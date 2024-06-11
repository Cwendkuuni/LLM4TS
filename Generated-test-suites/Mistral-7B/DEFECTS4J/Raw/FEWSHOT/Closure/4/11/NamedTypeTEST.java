package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.StaticSlot;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.ObjectType;
import com.google.javascript.rhino.PropertyType;
import com.google.javascript.rhino.ScriptContext;
import com.google.javascript.rhino.TypeFactory;

import java.util.List;

public class NamedTypeTest {
  private ErrorReporter errorReporter;
  private StaticScope<JSType> enclosing;
  private TypeFactory typeFactory;
  private ScriptContext ctx;

  @Before
  public void setUp() {
    errorReporter = new ErrorReporter();
    enclosing = createStaticScope();
    typeFactory = new TypeFactory(errorReporter, enclosing);
    ctx = new ScriptContext(typeFactory, errorReporter);
  }

  @Test
  public void testResolveViaRegistry() {
    JSTypeRegistry registry = TypeFactory.getJSTypeRegistry(ctx);

    // Test resolving a simple type
    NamedType namedType = new NamedType(registry, "String", null, 0, 0);
    JSType referencedType = namedType.resolveInternal(errorReporter, enclosing);
    assertTrue(referencedType instanceof JSTypeNative.StringType);

    // Test resolving a type that does not exist in the registry
    namedType = new NamedType(registry, "UnknownType", null, 0, 0);
    assertFalse(namedType.isResolved());
    namedType.resolveInternal(errorReporter, enclosing);
    assertTrue(namedType.isResolved());
    assertSame(namedType.getReferencedType(),
               registry.getNativeObjectType(JSTypeNative.NO_RESOLVED_TYPE));

    // Test resolving a type that exists in the registry but has not been
    // defined yet
    registry.registerType("UnknownType", JSTypeNative.STRING_TYPE);
    namedType = new NamedType(registry, "UnknownType", null, 0, 0);
    namedType.resolveInternal(errorReporter, enclosing);
    assertTrue(namedType.isResolved());
    assertTrue(namedType.getReferencedType() instanceof JSTypeNative.StringType);

    // Test resolving a type that has a validator
    registry.registerType("ValidatedType", new MyTypeValidator());
    namedType = new NamedType(registry, "ValidatedType", null, 0, 0);
    namedType.setValidator(new MyTypeValidator());
    namedType.resolveInternal(errorReporter, enclosing);
    assertTrue(namedType.isResolved());
    assertTrue(namedType.getReferencedType() instanceof JSTypeNative.StringType);
  }

  @Test
  public void testResolveViaProperties() {
    JSTypeRegistry registry = TypeFactory.getJSTypeRegistry(ctx);
    ObjectType objectType1 = createObjectType("Object1");
    ObjectType objectType2 = createObjectType("Object2");

    // Test resolving a type that is a direct property of the enclosing scope
    NamedType namedType1 = new NamedType(registry, "Property1", null, 0, 0);
    StaticSlot<JSType> slot1 = enclosing.put(createIdent("Property1"), objectType1);
    namedType1.resolveInternal(errorReporter, enclosing);
    assertTrue(namedType1.isResolved());
    assertTrue(namedType1.getReferencedType().equals(objectType1));

    // Test resolving a type that is a property of an object type
    ObjectType objectType3 = createObjectType("Object3");
    objectType3.setPropertyType("Property1", objectType1);
    StaticSlot<JSType> slot2 = enclosing.put(createIdent("Object3"), objectType3);
    NamedType namedType2 = new NamedType(registry, "Property1", null, 0, 0);
    slot2.setType(objectType3);
    namedType2.resolveInternal(errorReporter, enclosing);
    assertTrue(namedType2.isResolved());
    assertTrue(namedType2.getReferencedType().equals(objectType1));

    // Test resolving a type that is a nested property of an object type
    ObjectType objectType4 = createObjectType("Object4");
    objectType4.setPropertyType("Property2", createObjectType("Object5"));
    ObjectType objectType5 = createObjectType("Object5");
    objectType5.setPropertyType("Property1", objectType1);
    StaticSlot<JSType> slot3 = enclosing.put(createIdent("Object4"), objectType4);
    slot3.setType(objectType4);
    NamedType nestedNamedType = new NamedType(registry, "Property1.Property1", null, 0, 0);
    slot3.setType(objectType4);
    nestedNamedType.resolveInternal(errorReporter, enclosing);
    assertTrue(nestedNamedType.isResolved());
    assertTrue(nestedNamedType.getReferencedType().equals(objectType1));
  }

  @Test
  public void testHandleUnresolvedType() {
    NamedType namedType = new NamedType(null, "UnresolvedType", null, 0, 0);
    namedType.resolveInternal(errorReporter, enclosing);
    assertTrue(namedType.isResolved());
    assertNull(namedType.getReferencedType());
  }

  @Test
  public void testHandleTypeCycle() {
    ObjectType objectType = createObjectType("ObjectWithCycle");
    objectType.setPropertyType("Property1", objectType);
    NamedType namedType = new NamedType(null, "ObjectWithCycle.Property1", null, 0, 0);
    namedType.resolveInternal(errorReporter, enclosing);
    assertTrue(namedType.isResolved());
    assertNull(namedType.getReferencedType());
    assertTrue(errorReporter.getLog().length() > 0);
  }

  @Test
  public void testCheckEnumElementCycle() {
    ObjectType objectType = createObjectType("ObjectWithCycle");
    ObjectType enumType = createEnumType("EnumType");
    EnumElementType enumElementType1 = createEnumElementType("EnumElement1", enumType);
    EnumElementType enumElementType2 = createEnumElementType("EnumElement2", enumType);
    enumElementType1.setEnumElementType(enumElementType2);
    enumElementType2.setEnumElementType(enumElementType1);
    ObjectType objectTypeWithEnum = createObjectType("ObjectWithEnum");
    objectTypeWithEnum.setPropertyType("EnumProperty", enumType);
    StaticSlot<JSType> slot = enclosing.put(createIdent("ObjectWithEnum"), objectTypeWithEnum);
    slot.setType(objectTypeWithEnum);
    NamedType namedType = new NamedType(null, "EnumProperty.EnumElement1", null, 0, 0);
    namedType.resolveInternal(errorReporter, enclosing);
    assertTrue(namedType.isResolved());
    assertNull(namedType.getReferencedType());
    assertTrue(errorReporter.getLog().length() > 0);
  }

  @Test
  public void testSetValidator() {
    ObjectType objectType = createObjectType("ObjectType");
    JSTypeValidator validator = new ObjectTypeValidator();
    NamedType namedType = new NamedType(null, "ObjectType", null, 0, 0);
    namedType.setValidator(validator);
    namedType.resolveInternal(errorReporter, enclosing);
    assertTrue(namedType.isResolved());
    assertTrue(validator.isValid(namedType.getReferencedType()));
  }

  @Test
  public void testPropertyContinuations() {
    ObjectType objectType = createObjectType("ObjectType");
    String propertyName = "PropertyName";
    JSType propertyType = createObjectType("PropertyType");
    Node propertyNode = createPropertyNode("PropertyName", propertyType);

    NamedType namedType = new NamedType(null, "NamedType", null, 0, 0);
    namedType.defineProperty(propertyName, propertyType, false, propertyNode);
    namedType.resolveInternal(errorReporter, enclosing);
    assertTrue(namedType.isResolved());
    assertNull(namedType.getReferencedType());

    // Test that property continuations are defined and committed correctly
    namedType = new NamedType(null, "NamedType", null, 0, 0);
    List<PropertyContinuation> propertyContinuations = Lists.newArrayList();
    namedType.propertyContinuations = propertyContinuations;
    PropertyContinuation propertyContinuation = new PropertyContinuation(
        propertyName, propertyType, false, propertyNode);
    propertyContinuations.add(propertyContinuation);
    namedType.resolveInternal(errorReporter, enclosing);
    assertTrue(namedType.isResolved());
    assertTrue(namedType.getReferencedType().equals(objectType));
    assertEquals(1, namedType.propertyContinuations.size());
    assertEquals(propertyContinuation, namedType.propertyContinuations.get(0));

    // Test that property continuations are committed when the referenced type
    // is resolved
    ObjectType referencedObjectType = createObjectType("ReferencedObjectType");
    objectType.setPropertyType(propertyName, referencedObjectType);
    StaticSlot<JSType> slot = enclosing.put(createIdent("ReferencedObjectType"), referencedObjectType);
    slot.setType(referencedObjectType);
    NamedType resolvedNamedType = new NamedType(null, "NamedType", null, 0, 0);
    namedType.propertyContinuations = propertyContinuations;
    namedType.resolveInternal(errorReporter, enclosing);
    assertTrue(namedType.isResolved());
    assertTrue(namedType.getReferencedType().equals(objectType));
    assertEquals(1, namedType.propertyContinuations.size());
    assertEquals(propertyContinuation, namedType.propertyContinuations.get(0));
    PropertyContinuation committedPropertyContinuation = propertyContinuation;
    committedPropertyContinuation.commit(objectType);
    assertEquals(0, namedType.propertyContinuations.size());
    assertEquals(null, namedType.getProperty(propertyName));
    assertSame(committedPropertyContinuation.type, objectType.getPropertyType(propertyName));
  }

  private static ObjectType createObjectType(String name) {
    return new ObjectType(TypeFactory.getJSTypeRegistry(ctx), name);
  }

  private static EnumType createEnumType(String name) {
    return new EnumType(TypeFactory.getJSTypeRegistry(ctx), name);
  }

  private static EnumElementType createEnumElementType(String name, EnumType enumType) {
    return new EnumElementType(TypeFactory.getJSTypeRegistry(ctx), name, enumType);
  }

  private static Node createPropertyNode(String name, JSType propertyType) {
    return new PropertyNode(null, null, null, name, propertyType, null, null, null, null, null, null, null);
  }

  private static StaticScope<JSType> createStaticScope() {
    return new StaticScope<>(TypeFactory.getJSTypeRegistry(ctx), null);
  }

  private static String createIdent(String name) {
    return new IdentNode(null, null, name, null, 0, 0, false, null);
  }

  private static class ObjectTypeValidator implements Predicate<JSType> {
    @Override
    public boolean apply(JSType input) {
      return input instanceof ObjectType;
    }
  }

  private static class MyTypeValidator implements Predicate<JSType> {
    @Override
    public boolean apply(JSType input) {
      return input instanceof JSTypeNative.StringType;
    }
  }

  private static class PropertyContinuation {
    private final String propertyName;
    private final JSType type;
    private final boolean inferred;
    private final Node propertyNode;

    private PropertyContinuation(
        String propertyName,
        JSType type,
        boolean inferred,
        Node propertyNode) {
      this.propertyName = propertyName;
      this.type = type;
      this.inferred = inferred;
      this.propertyNode = propertyNode;
    }

    void commit(ObjectType target) {
      target.defineProperty(
          propertyName, type, inferred, propertyNode);
    }
  }
}