package com.google.javascript.rhino.jstype;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;

public class RecordTypeTest {
  private ErrorReporter errorReporter;
  private RecordTypeBuilder builder;
  private RecordType recordType;
  private RecordType recordTypeWithExtraProperty;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    errorReporter = mock(ErrorReporter.class);
    builder = new RecordTypeBuilder(new JSTypeRegistry()) {
      @Override
      protected ErrorReporter errorReporter() {
        return errorReporter;
      }
    };
    recordType = builder.buildRecordType("RecordType", Maps.newTreeMap());
    recordTypeWithExtraProperty = builder.buildRecordType("RecordTypeWithExtraProperty", Maps.newTreeMap());
    recordTypeWithExtraProperty.defineProperty("extraProperty", createStringType(), false, null);
  }

  @Test
  public void testIsEquivalentToSelf() {
    assertThat(recordType.isEquivalentTo(recordType)).isTrue();
  }

  @Test
  public void testIsEquivalentToSameRecordType() {
    RecordType sameRecordType = new RecordType(recordType.registry(), recordType.properties);
    assertThat(recordType.isEquivalentTo(sameRecordType)).isTrue();
  }

  @Test
  public void testIsEquivalentToDifferentRecordTypesWithSameProperties() {
    Map<String, RecordProperty> differentRecordProperties = new TreeMap<>();
    differentRecordProperties.putAll(recordType.properties);
    differentRecordProperties.remove("property1");
    differentRecordProperties.put("property1", createStringType().asFinal());

    RecordType differentRecordType = builder.buildRecordType("DifferentRecordType", differentRecordProperties);
    assertThat(recordType.isEquivalentTo(differentRecordType)).isTrue();
  }

  @Test
  public void testIsEquivalentToDifferentRecordTypesWithDifferentProperties() {
    RecordType differentRecordType = builder.buildRecordType("DifferentRecordType", recordType.properties);
    differentRecordType.defineProperty("property1", createNumberType(), false, null);
    assertThat(recordType.isEquivalentTo(differentRecordType)).isFalse();
  }

  @Test
  public void testIsEquivalentToRecordTypeWithLessProperties() {
    RecordType recordTypeWithLessProperty = new RecordType(recordType.registry(), recordType.properties) {
      @Override
      public boolean defineProperty(String propertyName, JSType type, boolean inferred, Node propertyNode) {
        return super.defineProperty(propertyName, type, true, propertyNode);
      }
    };
    recordTypeWithLessProperty.removeProperty("property1");
    assertThat(recordType.isEquivalentTo(recordTypeWithLessProperty)).isTrue();
  }

  @Test
  public void testIsEquivalentToRecordTypeWithMoreProperties() {
    RecordType recordTypeWithMoreProperties = new RecordType(recordType.registry(), Maps.newTreeMap(recordType.properties)) {
      @Override
      public boolean defineProperty(String propertyName, JSType type, boolean inferred, Node propertyNode) {
        return super.defineProperty(propertyName, type, true, propertyNode);
      }
    };
    recordTypeWithMoreProperties.defineProperty("extraProperty", createNumberType(), false, null);
    assertThat(recordType.isEquivalentTo(recordTypeWithMoreProperties)).isFalse();
  }

  @Test
  public void testIsEquivalentToRecordTypeWithPropertyTypeMismatch() {
    RecordType recordTypeWithMismatchedProperty = new RecordType(recordType.registry(), recordType.properties) {
      @Override
      public boolean defineProperty(String propertyName, JSType type, boolean inferred, Node propertyNode) {
        return super.defineProperty(propertyName, createNumberType(), true, propertyNode);
      }
    };
    assertThat(recordType.isEquivalentTo(recordTypeWithMismatchedProperty)).isFalse();
  }

  @Test
  public void testIsEquivalentToEmptyRecordType() {
    RecordType emptyRecordType = new RecordType(recordType.registry(), Maps.newTreeMap());
    assertThat(recordType.isEquivalentTo(emptyRecordType)).isTrue();
  }

  @Test
  public void testIsEquivalentToNativeObjectType() {
    assertThat(recordType.isEquivalentTo(recordType.registry().getNativeObjectType(JSTypeNative.OBJECT_TYPE))).isFalse();
  }

  @Test
  public void testIsSubtype() {
    // RecordType should be a subtype of itself
    assertThat(recordType.isSubtype(recordType)).isTrue();

    // RecordType should be a subtype of any record type with the same properties
    RecordType samePropertiesRecordType = new RecordType(recordType.registry(), recordType.properties);
    assertThat(recordType.isSubtype(samePropertiesRecordType)).isTrue();

    // RecordType should be a subtype of any record type that's a subtype of it
    RecordType subtypeRecordType = new RecordType(recordType.registry(), recordType.properties);
    subtypeRecordType.defineProperty("extraProperty", createStringType(), false, null);
    assertThat(recordType.isSubtype(subtypeRecordType)).isTrue();

    // RecordType should not be a subtype of a record type with fewer properties
    RecordType lessPropertiesRecordType = new RecordType(recordType.registry(), recordType.properties) {
      @Override
      public boolean defineProperty(String propertyName, JSType type, boolean inferred, Node propertyNode) {
        return super.defineProperty(propertyName, type, true, propertyNode);
      }
    };
    lessPropertiesRecordType.removeProperty("property1");
    assertThat(recordType.isSubtype(lessPropertiesRecordType)).isFalse();

    // RecordType should not be a subtype of a record type with an extra property
    assertThat(recordType.isSubtype(recordTypeWithExtraProperty)).isFalse();

    // RecordType should not be a subtype of a type that's not a record type
    JSType notARecordType = createObjectType();
    assertThat(recordType.isSubtype(notARecordType)).isFalse();
  }

  @Test
  public void testGetImplicitPrototype() {
    JSType implicitPrototype = recordType.getImplicitPrototype();
    JSType expectedImplicitPrototype = recordType.registry().getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    assertThat(implicitPrototype).isEqualTo(expectedImplicitPrototype);
  }

  @Test
  public void testDefineProperty() {
    String propertyName = "property1";
    JSType propertyType = createStringType();

    // RecordType should allow defining a property when not frozen
    recordType.defineProperty(propertyName, propertyType, false, null);
    JSType property = recordType.getPropertyType(propertyName);
    assertThat(property).isEqualTo(propertyType);

    // RecordType should not allow defining a property when frozen
    recordType.isFrozen = true;
    try {
      recordType.defineProperty(propertyName, propertyType, false, null);
      fail();
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    // RecordType should have no greatest subtype with itself
    JSType greatestSubtype = recordType.getGreatestSubtypeHelper(recordType);
    assertThat(recordType.registry().getNativeObjectType(JSTypeNative.NO_OBJECT_TYPE)).isEqualTo(greatestSubtype);

    // RecordType should have itself as the greatest subtype when comparing with an empty record type
    greatestSubtype = recordType.getGreatestSubtypeHelper(new RecordType(recordType.registry(), Maps.newTreeMap()));
    assertThat(recordType).isEqualTo(greatestSubtype);

    // RecordType should have itself as the greatest subtype when comparing with an object type
    greatestSubtype = recordType.getGreatestSubtypeHelper(recordType.registry().getNativeObjectType(JSTypeNative.OBJECT_TYPE));
    assertThat(recordType).isEqualTo(greatestSubtype);

    // RecordType and a record type with the same properties should have each other as their greatest subtypes
    RecordType samePropertiesRecordType = new RecordType(recordType.registry(), recordType.properties);
    greatestSubtype = recordType.getGreatestSubtypeHelper(samePropertiesRecordType);
    assertThat(samePropertiesRecordType).isEqualTo(greatestSubtype);

    // RecordType should have a record type with all its properties and their types as its greatest subtype
    Map<String, RecordProperty> newProperties = new TreeMap<>(recordType.properties);
    newProperties.put("newProperty", createNumberType().asFinal());
    RecordType newRecordType = new RecordType(recordType.registry(), newProperties);
    greatestSubtype = recordType.getGreatestSubtypeHelper(newRecordType);
    JSType newRecordTypeGreatestSubtype = newRecordType.getGreatestSubtypeHelper(recordType);
    assertThat(newRecordTypeGreatestSubtype).isEqualTo(greatestSubtype);

    // RecordType should not have a record type with fewer properties as its greatest subtype
    RecordType lessPropertiesRecordType = new RecordType(recordType.registry(), recordType.properties) {
      @Override
      public boolean defineProperty(String propertyName, JSType type, boolean inferred, Node propertyNode) {
        return super.defineProperty(propertyName, type, true, propertyNode);
      }
    };
    leastPropertiesRecordType.removeProperty("property1");
    greatestSubtype = recordType.getGreatestSubtypeHelper(lessPropertiesRecordType);
    assertThat(recordType.registry().getNativeObjectType(JSTypeNative.NO_OBJECT_TYPE)).isEqualTo(greatestSubtype);

    // RecordType should not have a record type with an extra property as its greatest subtype
    greatestSubtype = recordType.getGreatestSubtypeHelper(recordTypeWithExtraProperty);
    assertThat(recordType.registry().getNativeObjectType(JSTypeNative.NO_OBJECT_TYPE)).isEqualTo(greatestSubtype);
  }

  @Test
  public void testResolveInternal() {
    // Test record type properties resolution
    Map<String, JSType> expectedProperties = new TreeMap<>();
    expectedProperties.put("property1", createStringType());
    expectedProperties.put("property2", createNumberType());

    when(recordType.getPropertyType("property1")).thenReturn(createStringType());
    when(recordType.getPropertyType("property2")).thenReturn(createNumberType());

    StaticScope<JSType> scope = mock(StaticScope.class);
    when(scope.getValue("property1", ArgumentMatchers.any())).thenReturn("value1");
    when(scope.getValue("property2", ArgumentMatchers.any())).thenReturn(1);

    recordType.resolveInternal(errorReporter, scope);

    Map<String, JSType> actualProperties = recordType.properties;
    for (Map.Entry<String, JSType> entry : actualProperties.entrySet()) {
      JSType propertyType = entry.getValue();
      JSType expectedPropertyType;
      if (entry.getKey().equals("property1")) {
        expectedPropertyType = createStringType();
      } else {
        expectedPropertyType = createNumberType();
      }

      assertThat(propertyType).isEqualTo(expectedPropertyType);
    }

    // Test object type properties resolution
    SortedMap<String, JSType> objectTypeProperties = recordType.registry().getNativeObjectType(JSTypeNative.OBJECT_TYPE).getProperties();
    for (Map.Entry<String, JSType> entry : objectTypeProperties.entrySet()) {
      JSType propertyType = entry.getValue();
      JSType expectedPropertyType;
      if (entry.getKey().equals("constructor")) {
        expectedPropertyType = createFunctionType(new FunctionType("FunctionType", createContextType(), createArrayType(createContextType(), new Type[]{}), new Type[]{}));
      } else if (entry.getKey().equals("hasOwnProperty")) {
        expectedPropertyType = createFunctionType(new FunctionType("FunctionType", createContextType(), createArrayType(new Type[]{createContextType(), createStringType()}, new Type[]{createBooleanType()}), new Type[]{}));
      } else {
        expectedPropertyType = createUndefinedType();
      }

      JSType resolvedPropertyType = recordType.resolveInternal(errorReporter, scope).getPropertyType(entry.getKey());
      assertThat(resolvedPropertyType).isEqualTo(expectedPropertyType);
    }
  }

  private JSType createStringType() {
    return registry.getNativeType(JSTypeNative.STRING_TYPE);
  }

  private JSType createNumberType() {
    return registry.getNativeType(JSTypeNative.NUMBER_TYPE);
  }

  private JSType createBooleanType() {
    return registry.getNativeType(JSTypeNative.BOOLEAN_TYPE);
  }

  private JSType createObjectType() {
    return registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
  }

  private JSType createContextType() {
    return registry.getNativeType(JSTypeNative.CONTEXT_TYPE);
  }

  private JSType createFunctionType(FunctionType functionType) {
    return registry.getFactory().makeFunctionType(functionType);
  }

  private JSType createArrayType(JSType elementType, Type[] dimensions) {
    return registry.getFactory().makeArrayType(elementType, dimensions);
  }

  private JSType createUndefinedType() {
    return registry.getNativeType(JSTypeNative.UNDEFINED_TYPE);
  }
}