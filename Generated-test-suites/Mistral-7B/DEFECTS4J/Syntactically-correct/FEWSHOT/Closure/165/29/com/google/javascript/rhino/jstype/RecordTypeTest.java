package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import com.google.javascript.rhino.jstype.types.ObjectType;
import com.google.javascript.rhino.jstype.types.UnionType;
import java.util.Map;
import java.util.SortedMap;
import org.junit.Test;

public class RecordTypeTest {
  @Test
  public void testCreateRecordType() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    RecordType recordType = new RecordType(registry, recordProperties);
    assertNotNull(recordType);
    assertEquals(registry, recordType.getTypeRegistry());
    assertEquals(Maps.newTreeMap(), recordType.properties);
    assertTrue(recordType.isFrozen);
  }

  @Test
  public void testIsEquivalentTo() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    Map<String, RecordProperty> record1Properties = Maps.newHashMap();
    Map<String, RecordProperty> record2Properties = Maps.newHashMap();
    RecordType recordType1 = new RecordType(registry, record1Properties);
    RecordType recordType2 = new RecordType(registry, record2Properties);

    // Test self-equivalence
    assertTrue(recordType1.isEquivalentTo(recordType1));

    // Test non-equivalence with different record types
    assertFalse(recordType1.isEquivalentTo(recordType2));

    // Test record type equivalence with same properties
    record2Properties.putAll(record1Properties);
    RecordType recordType3 = new RecordType(registry, record2Properties);
    assertTrue(recordType1.isEquivalentTo(recordType3));

    // Test record type equivalence with different order of properties
    Map<String, RecordProperty> record3Properties = Maps.newHashMap();
    record3Properties.putAll(record1Properties);
    RecordType recordType4 = new RecordType(registry, record3Properties);
    assertTrue(recordType1.isEquivalentTo(recordType4));
  }

  @Test
  public void testGetImplicitPrototype() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    when(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)).thenReturn(
        mock(ObjectType.class));

    RecordType recordType = new RecordType(registry, Maps.newHashMap());
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE),
        recordType.getImplicitPrototype());
  }

  @Test
  public void testDefineProperty() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    RecordType recordType = new RecordType(registry, recordProperties);

    String propertyName = "testProperty";
    JSType propertyType = mock(JSType.class);
    Node propertyNode = mock(Node.class);
    RecordProperty recordProperty = new RecordProperty(propertyName, propertyType, propertyNode);
    recordProperties.put(propertyName, recordProperty);

    boolean result = recordType.defineProperty(
        propertyName, propertyType, false, propertyNode);
    assertTrue(result);
    assertTrue(recordType.hasProperty(propertyName));
    assertEquals(propertyType, recordType.getPropertyType(propertyName));
  }

  @Test
  public void testDefinePropertyFrozen() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    RecordType recordType = new RecordType(registry, recordProperties);

    String propertyName = "testProperty";
    JSType propertyType = mock(JSType.class);
    Node propertyNode = mock(Node.class);
    RecordProperty recordProperty = new RecordProperty(propertyName, propertyType, propertyNode);
    recordProperties.put(propertyName, recordProperty);

    recordType.isFrozen = true;
    boolean result = recordType.defineProperty(
        propertyName, propertyType, false, propertyNode);
    assertFalse(result);
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    UnionType emptyUnion = mock(UnionType.class);
    when(registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE)).thenReturn(emptyUnion);

    ObjectType objectType = mock(ObjectType.class);
    when(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)).thenReturn(objectType);

    RecordType recordType1 = new RecordType(registry, Maps.newHashMap());
    RecordProperty recordProperty1 = new RecordProperty("property1", mock(JSType.class), null);
    recordType1.properties.put("property1", recordProperty1);

    RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
    RecordProperty recordProperty2 = new RecordProperty("property1", mock(JSType.class), null);
    RecordProperty recordProperty3 = new RecordProperty("property2", mock(JSType.class), null);
    recordType2.properties.put("property1", recordProperty1);
    recordType2.properties.put("property2", recordProperty3);

    UnionType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
    assertEquals(emptyUnion, greatestSubtype);

    RecordType recordType3 = new RecordType(registry, Maps.newHashMap());
    recordType3.properties.put("property1", recordProperty1);

    greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType3);
    assertEquals(recordType1, greatestSubtype);

    RecordType recordType4 = new RecordType(registry, Maps.newHashMap());
    recordType4.properties.put("property1", recordProperty1);
    recordType4.properties.put("property2", recordProperty3);

    greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType4);
    assertEquals(recordType1, greatestSubtype);

    JSType recordType5 = mock(JSType.class);
    greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType5);
    assertEquals(emptyUnion, greatestSubtype);
  }

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    ObjectType objectType = mock(ObjectType.class);
    UnionType emptyUnion = mock(UnionType.class);
    when(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)).thenReturn(objectType);
    when(registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE)).thenReturn(emptyUnion);

    RecordType recordType1 = new RecordType(registry, Maps.newHashMap());
    RecordProperty recordProperty1 = new RecordProperty("property1", mock(JSType.class), null);
    recordType1.properties.put("property1", recordProperty1);

    RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
    RecordProperty recordProperty2 = new RecordProperty("property2", mock(JSType.class), null);
    recordType2.properties.put("property2", recordProperty2);

    RecordType recordType3 = new RecordType(registry, Maps.newHashMap());
    recordType3.properties.put("property1", recordProperty1);
    recordType3.properties.put("property2", recordProperty2);

    RecordType recordType4 = new RecordType(registry, Maps.newHashMap());
    recordType4.properties.put("property2", recordProperty2);

    RecordType recordType5 = new RecordType(registry, Maps.newHashMap());
    RecordProperty recordProperty3 = new RecordProperty("property3", mock(JSType.class), null);
    recordType5.properties.put("property3", recordProperty3);

    assertTrue(recordType1.isSubtype(recordType1));
    assertTrue(recordType1.isSubtype(objectType));
    assertTrue(objectType.isSubtype(recordType1));
    assertTrue(recordType1.isSubtype(recordType3));
    assertTrue(recordType3.isSubtype(recordType1));
    assertFalse(recordType1.isSubtype(recordType4));
    assertFalse(recordType4.isSubtype(recordType1));
    assertFalse(recordType1.isSubtype(recordType5));
    assertFalse(recordType5.isSubtype(recordType1));
  }

  @Test
  public void testResolveInternal() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    ErrorReporter errorReporter = mock(ErrorReporter.class);
    StaticScope<JSType> scope = mock(StaticScope.class);

    RecordType recordType = new RecordType(registry, Maps.newHashMap());
    RecordProperty recordProperty = new RecordProperty("property1", mock(JSType.class), null);
    recordType.properties.put("property1", recordProperty);

    JSType propertyType = mock(JSType.class);
    when(recordProperty.getType()).thenReturn(propertyType);

    JSType resolvedType = recordType.resolveInternal(errorReporter, scope);
    assertEquals(recordType, resolvedType);

    when(propertyType.resolve(errorReporter, scope)).thenReturn(mock(JSType.class));
    recordType.properties.clear();
    recordType.properties.put("property1", recordProperty);

    resolvedType = recordType.resolveInternal(errorReporter, scope);
    assertEquals(recordType, resolvedType);
  }
}