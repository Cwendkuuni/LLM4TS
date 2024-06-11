package com.google.javascript.rhino.jstype;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

/**
 * Test cases for RecordType class.
 */
public class RecordTypeTest {
  private static RecordType createRecordType(Map<String, RecordProperty> properties) {
    JSTypeRegistry registry = new JSTypeRegistry();
    return new RecordType(registry, properties, true);
  }

  @Test
  public void testBasicRecordType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", Integer.TYPE));
    properties.put("b", new RecordProperty("b", String.class));

    RecordType recordType = createRecordType(properties);

    // Check properties.
    SortedMap<String, JSType> recordedProperties = recordType.properties;
    assertThat(recordedProperties.size()).isEqualTo(2);
    assertThat(recordedProperties.get("a")).isInstanceOf(Integer.TYPE);
    assertThat(recordedProperties.get("b")).isInstanceOf(String.class);

    // Check equivalence.
    JSType aType = new IntegerType(registry, false);
    JSType bType = new StringType(registry, false);
    RecordType anotherRecordType = createRecordType(Maps.newHashMap("a", new RecordProperty("a", aType))
        .put("b", new RecordProperty("b", bType)));
    assertThat(recordType.checkRecordEquivalenceHelper(anotherRecordType, true)).isTrue();

    // Check getImplicitPrototype()
    assertThat(recordType.getImplicitPrototype()).isEqualTo(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));

    // Check defineProperty()
    String propertyName = "c";
    JSType propertyType = new ObjectType(registry, null, null);
    recordType.defineProperty(propertyName, propertyType, false, null);
    assertNull(recordType.getPropertyType(propertyName));

    // Check getGreatestSubtypeHelper()
    RecordType anotherRecordType2 = createRecordType(Maps.newHashMap("a", new RecordProperty("a", aType))
        .put("b", new RecordProperty("b", String.class))
        .put("c", new RecordProperty("c", Integer.TYPE)));
    JSType greatestSubtype = recordType.getGreatestSubtypeHelper(anotherRecordType2);
    assertThat(recordType.isSubtype(anotherRecordType2)).isTrue();
    assertThat(recordType.isSubtype(anotherRecordType2.getGreatestSubtypeHelper(recordType))).isTrue();
    assertThat(grearestSubtype).isInstanceOf(recordType.getClass());

    // Check isSubtype()
    RecordType anotherRecordType3 = createRecordType(Maps.newHashMap("a", new RecordProperty("a", aType))
        .put("b", new RecordProperty("b", String.class))
        .put("c", new RecordProperty("c", Boolean.TYPE)));
    RecordType anotherRecordType4 = createRecordType(Maps.newHashMap("c", new RecordProperty("c", Boolean.TYPE)));
    assertThat(recordType.isSubtype(anotherRecordType3)).isFalse();
    assertThat(recordType.isSubtype(anotherRecordType4)).isTrue();

    // Check resolveInternal()
    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<>(registry);
    scope.put("Integer", new IntegerType(registry, false));
    scope.put("String", new StringType(registry, false));
    JSType resolvedType = recordType.resolveInternal(errorReporter, scope);
    assertThat(resolvedType).isInstanceOf(recordType.getClass());
  }

  @Test
  public void testInheritance() {
    Map<String, RecordProperty> baseProperties = Maps.newHashMap();
    baseProperties.put("a", new RecordProperty("a", Integer.TYPE));

    // Create base record type.
    RecordType baseRecordType = createRecordType(baseProperties);

    // Create derived record type with an additional property.
    Map<String, RecordProperty> derivedProperties = new HashMap<>(baseProperties);
    derivedProperties.put("b", new RecordProperty("b", String.class));
    RecordType derivedRecordType = createRecordType(derivedProperties);

    // Check inheritance.
    JSType baseType = baseRecordType.getImplicitPrototype();
    JSType derivedType = derivedRecordType.getImplicitPrototype();
    JSType greatestSubtype = baseRecordType.getGreatestSubtypeHelper(derivedRecordType);
    assertThat(baseType).isNotNull();
    assertThat(derivedType).isNotNull();
    assertThat(grearestSubtype).isNotNull();
    assertThat(grearestSubtype).isInstanceOf(derivedRecordType.getClass());
    assertThat(baseType).isSubtypeOf(grearestSubtype);
    assertThat(derivedType).isSubtypeOf(grearestSubtype);
  }

  @Test
  public void testGetPropertyTypeWithConflictingProperty() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", Integer.TYPE));
    properties.put("b", new RecordProperty("b", String.class));
    RecordType recordType = createRecordType(properties);

    // Check getPropertyType() with conflicting property.
    String conflictingProperty = "a";
    JSType conflictingTypeA = new IntegerType(registry, false);
    JSType conflictingTypeB = new StringType(registry, false);
    RecordType conflictingRecordType = createRecordType(Maps.newHashMap("a", new RecordProperty("a", conflictingTypeB)));
    recordType.defineProperty(conflictingProperty, conflictingTypeA, false, null);
    JSType propertyType = recordType.getPropertyType(conflictingProperty);
    UnionTypeBuilder builder = new UnionTypeBuilder(registry);
    builder.addAlternate(conflictingTypeA);
    builder.addAlternate(conflictingTypeB);
    JSType expectedUnionType = builder.build();
    assertThat(propertyType).isInstanceOf(expectedUnionType.getClass());
  }
}