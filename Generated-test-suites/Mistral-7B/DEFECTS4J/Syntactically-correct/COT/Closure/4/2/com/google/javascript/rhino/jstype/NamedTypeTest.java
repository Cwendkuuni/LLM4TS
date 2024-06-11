package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;

import com.google.common.base.Predicates;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.StaticSlot;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.NamedType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.PropertyContinuation;
import com.google.javascript.rhino.jstype.PropertyType;
import com.google.javascript.rhino.jstype.StaticSlotType;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link NamedType} class.
 */
public class NamedTypeTest {

  private static final String NAMED_TYPE_REFERENCE = "MyNamedType";
  private static final String SOURCE_NAME = "MySourceFile.js";
  private static final int LINE_NUMBER = 10;
  private static final int CHAR_NUMBER = 20;
  private static final ErrorReporter errorReporter = new ErrorReporter();
  private static final StaticScope<JSType> ENCLOSING_SCOPE = new StaticScope<>();

  @Before
  public void setUp() {
    errorReporter.reset();
  }

  @Test
  public void testNamedTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, NAMED_TYPE_REFERENCE, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
    assertEquals(NAMED_TYPE_REFERENCE, namedType.getReferenceName());
    assertEquals(JSTypeNative.UNKNOWN_TYPE, namedType.getBaseType());
    assertFalse(namedType.isResolved());
  }

  @Test
  public void testDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, NAMED_TYPE_REFERENCE, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
    PropertyContinuation propertyContinuation = new PropertyContinuation(
        "propertyName",
        JSType.BOOLEAN,
        true,
        Node.newLiteral(true));
    namedType.defineProperty("propertyName", JSType.BOOLEAN, true, Node.newLiteral(true));
    assertTrue(namedType.hasProperty("propertyName"));
    assertEquals(PropertyType.BOOLEAN, namedType.getPropertyType("propertyName"));
    assertEquals(JSType.BOOLEAN, namedType.getReferencedType().getPropertyType("propertyName"));
    assertEquals(1, namedType.getPropertyContinuations().size());
    assertEquals(propertyContinuation, namedType.getPropertyContinuations().get(0));
  }

  @Test
  public void testResolveViaRegistry() {
    JSTypeRegistry registry = new JSTypeRegistry();
    StaticScope<JSType> enclosing = new StaticScope<>();
    JSType referencedType = new ObjectType("ReferencedType");
    registry.putType(NAMED_TYPE_REFERENCE, referencedType);
    NamedType namedType = new NamedType(registry, NAMED_TYPE_REFERENCE, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
    assertEquals(referencedType, namedType.resolveInternal(errorReporter, ENCLOSING_SCOPE));
  }

  @Test
  public void testResolveViaProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    StaticScope<JSType> parentScope = new StaticScope<>();
    StaticScope<JSType> enclosing = new StaticScope<>();
    ObjectType parentObjectType = new ObjectType("ParentType");
    parentObjectType.defineProperty("childProperty", JSTypeNative.ANY, true, null);
    parentScope.putSlot(
        "ParentType",
        new StaticSlot<>(
            "ParentType",
            new StaticSlotType<JSType>(JSTypeNative.OBJECT_TYPE)));
    enclosing.setParent(parentScope);
    JSType childType = new NamedType(registry, "ParentType.childProperty", SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
    assertEquals(JSTypeNative.ANY, childType.resolveInternal(errorReporter, enclosing).getBaseType());
  }

  @Test
  public void testResolveUnknownType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    StaticScope<JSType> enclosing = new StaticScope<>();
    NamedType namedType = new NamedType(registry, NAMED_TYPE_REFERENCE, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
    namedType.setResolvedTypeInternal(new ObjectType("ResolvedType"));
    assertEquals(new ObjectType("ResolvedType"), namedType.resolveInternal(errorReporter, enclosing));
  }

  @Test
  public void testResolveCycle() {
    JSTypeRegistry registry = new JSTypeRegistry();
    StaticScope<JSType> enclosing = new StaticScope<>();
    ObjectType cycleType = new ObjectType("CycleType");
    cycleType.setPropertyType("childProperty", cycleType);
    registry.putType(NAMED_TYPE_REFERENCE, cycleType);
    NamedType namedType = new NamedType(registry, NAMED_TYPE_REFERENCE, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
    try {
      namedType.resolveInternal(errorReporter, enclosing);
      fail("Expected a cycle detection warning");
    } catch (AssertionError e) {
      String warningMessage = errorReporter.getTopErrorMessage();
      assertTrue(warningMessage.contains("Cycle detected in inheritance chain of type " + NAMED_TYPE_REFERENCE));
      assertEquals(JSTypeNative.UNKNOWN_TYPE, namedType.getReferencedType());
    }
  }

  @Test
  public void testResolveEnumElementCycle() {
    JSTypeRegistry registry = new JSTypeRegistry();
    StaticScope<JSType> enclosing = new StaticScope<>();

    ObjectType enumType = new ObjectType("EnumType");
    EnumElementType enumElementType1 = new EnumElementType("EnumElement1", enumType);
    EnumElementType enumElementType2 = new EnumElementType("EnumElement2", enumType);
    enumElementType1.setPrimitiveType(enumType);
    enumElementType2.setPrimitiveType(enumType);
    registry.putType(NAMED_TYPE_REFERENCE + ".EnumElement1", enumElementType1);
    registry.putType(NAMED_TYPE_REFERENCE + ".EnumElement2", enumElementType2);

    NamedType namedType = new NamedType(registry, NAMED_TYPE_REFERENCE, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
    try {
      namedType.resolveInternal(errorReporter, enclosing);
      fail("Expected an enum element cycle warning");
    } catch (AssertionError e) {
      String warningMessage = errorReporter.getTopErrorMessage();
      assertTrue(warningMessage.contains("Cycle detected in inheritance chain of type " + NAMED_TYPE_REFERENCE));
      assertEquals(JSTypeNative.UNKNOWN_TYPE, namedType.getReferencedType());
    }
  }

  @Test
  public void testResolveFunctionType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    StaticScope<JSType> enclosing = new StaticScope<>();
    FunctionType functionType = new FunctionType(
        new ObjectType("FunctionType"),
        null,
        null,
        null,
        null,
        null,
        null,
        JSTypeNative.VOID_TYPE);
    registry.putType(NAMED_TYPE_REFERENCE, functionType.getInstanceType());
    NamedType namedType = new NamedType(registry, NAMED_TYPE_REFERENCE, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
    assertEquals(functionType.getInstanceType(), namedType.resolveInternal(errorReporter, enclosing));
  }

  @Test
  public void testResolveEnumType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    StaticScope<JSType> enclosing = new StaticScope<>();
    EnumType enumType = new EnumType(Name.JAVASCRIPT, "MyEnumType");
    registry.putType(NAMED_TYPE_REFERENCE, enumType.getElementsType());
    NamedType namedType = new NamedType(registry, NAMED_TYPE_REFERENCE, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
    assertEquals(enumType.getElementsType(), namedType.resolveInternal(errorReporter, enclosing));
  }

  @Test
  public void testResolveNoObjectType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    StaticScope<JSType> enclosing = new StaticScope<>();
    NamedType namedType = new NamedType(registry, NAMED_TYPE_REFERENCE, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
    namedType.setReferencedAndResolvedType(JSTypeNative.NO_OBJECT_TYPE);
    assertEquals(JSTypeNative.NO_OBJECT_TYPE, namedType.resolveInternal(errorReporter, enclosing));
  }

  @Test
  public void testResolverUnknownTypeWhenLastGeneration() {
    JSTypeRegistry registry = new JSTypeRegistry();
    StaticScope<JSType> enclosing = new StaticScope<>();
    NamedType namedType = new NamedType(registry, NAMED_TYPE_REFERENCE, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
    namedType.setReferencedAndResolvedType(new ObjectType("UnknownType"));
    assertFalse(namedType.isResolved());
    assertEquals(
        JSTypeNative.NO_RESOLVED_TYPE,
        namedType.getReferencedType());
  }

  @Test
  public void testSetValidator() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Predicate<JSType> validator = Predicates.instanceOf(ObjectType.class);
    NamedType namedType = new NamedType(registry, NAMED_TYPE_REFERENCE, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
    namedType.setValidator(validator);
    JSType referencedType = new ObjectType("ObjectType");
    namedType.resolveInternal(errorReporter, ENCLOSING_SCOPE);
    assertTrue(validator.apply(referencedType));
  }
}