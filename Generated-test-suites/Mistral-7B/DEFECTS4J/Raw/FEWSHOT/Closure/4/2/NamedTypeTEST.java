package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.StylizedNames;
import com.google.javascript.rhino.testing.PolymerTest;
import com.google.javascript.rhino.testing.TypeEqualityTester;

import java.util.List;

public class NamedTypeTest extends PolymerTest {

  private static final String NAMED_TYPE_REFERENCE = "NamedType";
  private static final String REFERENCED_TYPE_NAME = "ReferencedType";
  private static final String SOURCE_NAME = "NamedTypeTest";

  private static class TestNamedType extends NamedType {
    TestNamedType(JSTypeRegistry registry, String reference) {
      super(registry, reference, SOURCE_NAME, 1, 1);
    }
  }

  private static class TestFunctionType extends FunctionType {
    TestFunctionType(JSTypeRegistry registry, Node funcNode) {
      super(registry, funcNode, null, null);
    }
  }

  private static class TestObjectType extends ObjectType {
    TestObjectType(JSTypeRegistry registry, String name) {
      super(registry, name);
    }
  }

  private static class TestEnumType extends EnumType {
    TestEnumType(JSTypeRegistry registry, String name) {
      super(registry, name, Lists.newArrayList(), null, null);
    }
  }

  private static class TestEnumElementType extends EnumElementType {
    TestEnumElementType(JSTypeRegistry registry, String name, JSType primitiveType) {
      super(registry, name, primitiveType);
    }
  }

  private static class TestPropertyContinuation {
    private final String propertyName;
    private final JSType type;
    private final boolean inferred;
    private final Node propertyNode;

    TestPropertyContinuation(
        String propertyName,
        JSType type,
        boolean inferred,
        Node propertyNode) {
      this.propertyName = propertyName;
      this.type = type;
      this.inferred = inferred;
      this.propertyNode = propertyNode;
    }
  }

  @Test
  public void testIsNamedType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new TestNamedType(registry, NAMED_TYPE_REFERENCE);
    assertTrue(namedType.isNamedType());
  }

  @Test
  public void testIsNominalType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new TestNamedType(registry, NAMED_TYPE_REFERENCE);
    assertTrue(namedType.isNominalType());
  }

  @Test
  public void testHashCode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType1 = new TestNamedType(registry, NAMED_TYPE_REFERENCE);
    NamedType namedType2 = new TestNamedType(registry, NAMED_TYPE_REFERENCE);
    assertEquals(namedType1.hashCode(), namedType2.hashCode());
  }

  @Test
  public void testToStringHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new TestNamedType(registry, NAMED_TYPE_REFERENCE);
    assertEquals(NAMED_TYPE_REFERENCE, namedType.toStringHelper(false));
  }

  @Test
  public void testGetReferenceName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new TestNamedType(registry, NAMED_TYPE_REFERENCE);
    assertEquals(NAMED_TYPE_REFERENCE, namedType.getReferenceName());
  }

  @Test
  public void testDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new TestNamedType(registry, NAMED_TYPE_REFERENCE);
    TestObjectType referencedObjectType = new TestObjectType(registry, REFERENCED_TYPE_NAME);

    TestPropertyContinuation propertyContinuation = new TestPropertyContinuation(
        "property",
        referencedObjectType,
        false,
        new Node());

    namedType.defineProperty("property", referencedObjectType, false, new Node());

    TestNamedType testNamedType = new TestNamedType(registry, NAMED_TYPE_REFERENCE);
    assertTrue(testNamedType.hasProperty("property"));
    assertEquals(referencedObjectType, testNamedType.getProperty("property"));
  }

  @Test
  public void testFinishPropertyContinuations() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new TestNamedType(registry, NAMED_TYPE_REFERENCE);

    TestObjectType referencedObjectType = new TestObjectType(registry, REFERENCED_TYPE_NAME);
    TestPropertyContinuation propertyContinuation1 = new TestPropertyContinuation(
        "property1",
        referencedObjectType,
        false,
        new Node());
    TestPropertyContinuation propertyContinuation2 = new TestPropertyContinuation(
        "property2",
        referencedObjectType,
        false,
        new Node());

    namedType.propertyContinuations = Lists.newArrayList(propertyContinuation1, propertyContinuation2);

    namedType.finishPropertyContinuations();

    TestNamedType testNamedType = new TestNamedType(registry, NAMED_TYPE_REFERENCE);
    assertTrue(testNamedType.hasProperty("property1"));
    assertTrue(testNamedType.hasProperty("property2"));
    assertEquals(referencedObjectType, testNamedType.getProperty("property1"));
    assertEquals(referencedObjectType, testNamedType.getProperty("property2"));
  }

  @Test
  public void testGetReferencedTypeInternal() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new TestNamedType(registry, NAMED_TYPE_REFERENCE);
    assertNull(namedType.getReferencedTypeInternal());
  }

  @Test
  public void testGetReferencedObjTypeInternal() {
    JSTypeRegistry registry = new JSTypeRegistry();
    TestObjectType referencedObjectType = new TestObjectType(registry, REFERENCED_TYPE_NAME);
    NamedType namedType = new TestNamedType(registry, REFERENCED_TYPE_NAME);
    assertNull(namedType.getReferencedObjTypeInternal());

    namedType.setReferencedObjTypeInternal(referencedObjectType);
    assertEquals(referencedObjectType, namedType.getReferencedObjTypeInternal());
  }

  @Test
  public void testResolveViaRegistry() {
    JSTypeRegistry registry = new JSTypeRegistry();
    TestObjectType referencedObjectType = new TestObjectType(registry, REFERENCED_TYPE_NAME);
    NamedType namedType = new TestNamedType(registry, REFERENCED_TYPE_NAME);

    registry.registerType(REFERENCED_TYPE_NAME, referencedObjectType);

    JSType resolvedType = namedType.resolveInternal(context.errorReporter(), context
        .getEnclosingStaticScope());

    TestNamedType testNamedType = new TestNamedType(registry, REFERENCED_TYPE_NAME);
    assertEquals(referencedObjectType, testNamedType.getReferencedType());
    assertEquals(referencedObjectType, resolvedType);
  }

  @Test
  public void testResolveViaProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    TestObjectType parentClass = new TestObjectType(registry, "ParentClass");
    TestObjectType referencedObjectType = new TestObjectType(registry, REFERENCED_TYPE_NAME);
    NamedType namedType = new TestNamedType(registry, "ParentClass." + REFERENCED_TYPE_NAME);

    parentClass.defineProperty(
        StylizedNames.get("property"),
        referencedObjectType,
        false,
        new Node());

    JSType resolvedType = namedType.resolveInternal(context.errorReporter(), context
        .getEnclosingStaticScope());

    TestNamedType testNamedType = new TestNamedType(registry, "ParentClass." + REFERENCED_TYPE_NAME);
    assertEquals(referencedObjectType, testNamedType.getReferencedType());
    assertEquals(referencedObjectType, resolvedType);
  }

  @Test
  public void testSetReferencedAndResolvedType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    TestObjectType referencedObjectType = new TestObjectType(registry, REFERENCED_TYPE_NAME);
    NamedType namedType = new TestNamedType(registry, REFERENCED_TYPE_NAME);

    namedType.setReferencedAndResolvedType(referencedObjectType, context.errorReporter(), context
        .getEnclosingStaticScope());

    TestNamedType testNamedType = new TestNamedType(registry, REFERENCED_TYPE_NAME);
    assertEquals(referencedObjectType, testNamedType.getReferencedType());
  }

  @Test
  public void testHandleTypeCycle() {
    JSTypeRegistry registry = new JSTypeRegistry();
    TestObjectType objectType = new TestObjectType(registry, "ObjectType");
    NamedType namedType = new TestNamedType(registry, "ObjectType");

    namedType.setReferencedType(objectType);

    try {
      namedType.resolveInternal(context.errorReporter(), context.getEnclosingStaticScope());
      fail();
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  @Test
  public void testCheckEnumElementCycle() {
    JSTypeRegistry registry = new JSTypeRegistry();
    TestEnumType enumType = new TestEnumType(registry, REFERENCED_TYPE_NAME);
    TestEnumElementType enumElementType = new TestEnumElementType(
        registry, "Element", namedType);
    NamedType namedType = new TestNamedType(registry, REFERENCED_TYPE_NAME);

    namedType.setReferencedAndResolvedType(enumType, context.errorReporter(), context
        .getEnclosingStaticScope());

    assertEquals(enumType, namedType.getReferencedType());

    namedType.checkEnumElementCycle(context.errorReporter());

    TestNamedType testNamedType = new TestNamedType(registry, REFERENCED_TYPE_NAME);
    assertEquals(enumType, testNamedType.getReferencedType());
  }

  @Test
  public void testHandleUnresolvedType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    TestObjectType referencedObjectType = new TestObjectType(registry, REFERENCED_TYPE_NAME);
    NamedType namedType = new TestNamedType(registry, REFERENCED_TYPE_NAME);

    namedType.handleUnresolvedType(context.errorReporter(), false);

    TestNamedType testNamedType = new TestNamedType(registry, REFERENCED_TYPE_NAME);
    assertNull(testNamedType.getReferencedType());
  }

  @Test
  public void testHandleUnresolvedTypeWithForwardReference() {
    JSTypeRegistry registry = new JSTypeRegistry();
    TestObjectType referencedObjectType = new TestObjectType(registry, REFERENCED_TYPE_NAME);
    registry.registerForwardDeclaration(REFERENCED_TYPE_NAME);
    NamedType namedType = new TestNamedType(registry, REFERENCED_TYPE_NAME);

    namedType.handleUnresolvedType(context.errorReporter(), false);

    TestNamedType testNamedType = new TestNamedType(registry, REFERENCED_TYPE_NAME);
    assertNull(testNamedType.getReferencedType());
  }

  @Test
  public void testSetValidator() {
    JSTypeRegistry registry = new JSTypeRegistry();
    TestObjectType referencedObjectType = new TestObjectType(registry, REFERENCED_TYPE_NAME);
    Predicate<JSType> validator = new Predicate<JSType>() {
      @Override
      public boolean apply(JSType input) {
        return input instanceof TestObjectType;
      }
    };
    NamedType namedType = new TestNamedType(registry, REFERENCED_TYPE_NAME);

    namedType.setValidator(validator);

    TestNamedType testNamedType = new TestNamedType(registry, REFERENCED_TYPE_NAME);
    JSType referencedType = testNamedType.getReferencedType();
    assertTrue(validator.apply(referencedType));
  }

  @Test
  public void testFinishPropertyContinuationsWithoutReferencedType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new TestNamedType(registry, NAMED_TYPE_REFERENCE);

    TestPropertyContinuation propertyContinuation = new TestPropertyContinuation(
        "property",
        new TestFunctionType(registry, null),
        false,
        new Node());

    namedType.propertyContinuations = Lists.newArrayList(propertyContinuation);

    try {
      namedType.finishPropertyContinuations();
      fail();
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  @Test
  public void testFinishPropertyContinuationsWithReferencedType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    TestObjectType referencedObjectType = new TestObjectType(registry, REFERENCED_TYPE_NAME);
    NamedType namedType = new TestNamedType(registry, NAMED_TYPE_REFERENCE);

    TestPropertyContinuation propertyContinuation = new TestPropertyContinuation(
        "property",
        referencedObjectType,
        false,
        new Node());

    namedType.propertyContinuations = Lists.newArrayList(propertyContinuation);

    namedType.finishPropertyContinuations();

    TestNamedType testNamedType = new TestNamedType(registry, NAMED_TYPE_REFERENCE);
    assertTrue(testNamedType.hasProperty("property"));
    assertEquals(referencedObjectType, testNamedType.getProperty("property"));
  }

  @Test
  public void testFinishPropertyContinuationsWithReferencedTypeAndInheritedProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    TestObjectType parentClass = new TestObjectType(registry, "ParentClass");
    TestObjectType referencedObjectType = new TestObjectType(registry, REFERENCED_TYPE_NAME);
    NamedType namedType = new TestNamedType(registry, "ParentClass." + REFERENCED_TYPE_NAME);

    parentClass.defineProperty(
        StylizedNames.get("property"),
        referencedObjectType,
        false,
        new Node());

    TestPropertyContinuation propertyContinuation = new TestPropertyContinuation(
        "property",
        referencedObjectType,
        false,
        new Node());

    namedType.propertyContinuations = Lists.newArrayList(propertyContinuation);

    namedType.finishPropertyContinuations();

    TestNamedType testNamedType = new TestNamedType(registry, "ParentClass." + REFERENCED_TYPE_NAME);
    assertTrue(testNamedType.hasProperty("property"));
    assertEquals(referencedObjectType, testNamedType.getProperty("property"));
  }

  @Test
  public void testToString() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new TestNamedType(registry, NAMED_TYPE_REFERENCE);
    String actualToString = namedType.toString();
    String expectedToString = "NamedType";
    new TypeEqualityTester().assertEquals(expectedToString, actualToString);
  }
}