package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import java.util.TreeMap;

public class RecordTypeTest {
  @Test
  public void testCreateRecordType() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    Map<String, RecordProperty> properties = Maps.newHashMap();

    properties.put("x", new RecordProperty("x", int.class));
    properties.put("y", new RecordProperty("y", String.class));

    RecordType recordType = new RecordType(registry, properties);

    // Basic checks
    assertEquals(JSType.Record, recordType.getTypeFlags());
    assertEquals(properties, recordType.getProperties());
    assertFalse(recordType.isFrozen());

    // Test getImplicitPrototype
    JSType prototype = recordType.getImplicitPrototype();
    assertEquals(JSType.Object, prototype.getTypeFlags());

    // Test isEquivalentTo
    JSType recordType1 = new RecordType(registry, properties);
    JSType recordType2 = new RecordType(registry, properties);
    assertTrue(recordType1.isEquivalentTo(recordType2));

    // Test defineProperty
    RecordProperty xProp = new RecordProperty("x", int.class);
    RecordProperty yProp = new RecordProperty("y", String.class);

    TreeMap<String, JSType> newProperties = new TreeMap<>();
    newProperties.put("x", xProp.getType());
    newProperties.put("y", yProp.getType());

    RecordType frozenRecord = new RecordType(registry, newProperties);
    assertFalse(frozenRecord.defineProperty("x", int.class, false, null));
    assertTrue(frozenRecord.defineProperty("y", String.class, false, null));

    // Test getGreatestSubtypeHelper
    JSType otherType = new ObjectType(registry, null, null);
    JSType intType = int.class.getType(registry);
    JSType recordType3 = new RecordType(registry, newProperties());
    JSType stringType = String.class.getType(registry);

    JSType greatestSubtype = recordType.getGreatestSubtypeHelper(otherType);
    assertSame(otherType, greatestSubtype);

    greatestSubtype = recordType.getGreatestSubtypeHelper(intType);
    assertSame(intType, greatestSubtype);

    greatestSubtype = recordType.getGreatestSubtypeHelper(recordType3);
    assertEquals(recordType, greatestSubtype);

    greatestSubtype = recordType.getGreatestSubtypeHelper(intType);
    assertEquals(intType, greatestSubtype);

    greatestSubtype = recordType.getGreatestSubtypeHelper(stringType);
    assertEquals(recordType, greatestSubtype);

    // Test isSubtype
    ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    RecordType recordType4 = new RecordType(registry, newProperties());

    assertTrue(recordType.isSubtype(recordType));
    assertTrue(recordType.isSubtype(recordType4));
    assertTrue(recordType.isSubtype(objectType));
    assertFalse(objectType.isSubtype(recordType));

    // Test isSubtype (2)
    JSType recordType5 = new RecordType(registry, newProperties());
    JSType recordType6 = new RecordType(registry, newProperties());

    RecordProperty xProp2 = new RecordProperty("x", int.class);
    RecordProperty yProp2 = new RecordProperty("y", String.class);

    TreeMap<String, JSType> properties2 = new TreeMap<>();
    properties2.put("x", xProp2.getType());
    properties2.put("z", int.class.getType(registry));

    RecordType recordType7 = new RecordType(registry, properties2);
    RecordType recordType8 = new RecordType(registry, properties2);

    when(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).getPropertyType("x"))
        .thenReturn(xProp.getType());
    when(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).getPropertyType("y"))
        .thenReturn(yProp.getType());

    when(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).getPropertyType("z"))
        .thenReturn(intType);

    when(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).hasProperty("z"))
        .thenReturn(true);

    assertTrue(recordType.isSubtype(recordType5));
    assertTrue(recordType.isSubtype(recordType6));
    assertTrue(recordType.isSubtype(recordType7));
    assertTrue(recordType.isSubtype(recordType8));

    // Test isSubtype (3)
    ObjectType objectType2 = new ObjectType(registry, null, null);

    UnionTypeBuilder builder = new UnionTypeBuilder(registry);
    builder.addAlternate(objectType);

    JSType unionType = builder.build();
    RecordType recordType9 = new RecordType(registry, newProperties());
    RecordType recordType10 = new RecordType(registry, newProperties());
    RecordProperty zProp = new RecordProperty("z", int.class);

    TreeMap<String, JSType> properties3 = new TreeMap<>();
    properties3.put("z", zProp.getType());

    RecordType recordType11 = new RecordType(registry, properties3);

    when(registry.getEachReferenceTypeWithProperty("z"))
        .thenReturn(new Object[] { objectType2, recordType11 });

    assertTrue(recordType.isSubtype(recordType9));
    assertTrue(recordType.isSubtype(recordType10));

    // Test isSubtype (4)
    RecordType recordType12 = new RecordType(registry, newProperties());
    RecordType recordType13 = new RecordType(registry, newProperties());

    RecordProperty xProp3 = new RecordProperty("x", int.class);
    RecordProperty xProp4 = new RecordProperty("x", int.class);
    RecordProperty zProp2 = new RecordProperty("z", int.class);

    TreeMap<String, JSType> properties4 = new TreeMap<>();
    properties4.put("x", xProp3.getType());
    properties4.put("z", zProp2.getType());

    RecordType recordType14 = new RecordType(registry, properties4);

    when(registry.getEachReferenceTypeWithProperty("x"))
        .thenReturn(new Object[] { recordType12, recordType13 });

    assertTrue(recordType.isSubtype(recordType14));

    // Test defineProperty (1)
    RecordType recordType15 = new RecordType(registry, properties);

    RecordProperty newProp = new RecordProperty("z", double.class);

    recordType15.defineProperty("z", double.class, false, null);

    Map<String, JSType> newProperties2 = new HashMap<>(properties);
    newProperties2.put("z", double.class);

    RecordType newRecordType = new RecordType(registry, newProperties2);

    assertEquals(newRecordType, recordType15);

    // Test defineProperty (2)
    RecordType recordType16 = new RecordType(registry, properties);

    RecordProperty existingProp = properties.get("x");

    recordType16.defineProperty("x", int.class, false, null);

    assertEquals(recordType, recordType16);

    // Test defineProperty (3)
    RecordType recordType17 = new RecordType(registry, properties);

    recordType17.defineProperty("x", int.class, false, null);

    RecordType frozenRecord2 = new RecordType(registry, properties);

    assertFalse(frozenRecord2.defineProperty("x", int.class, false, null));

    // Test resolveInternal
    ErrorReporter errorReporter = mock(ErrorReporter.class);
    StaticScope<JSType> scope = mock(StaticScope.class);

    RecordType recordType18 = new RecordType(registry, properties);

    JSType resolvedType = recordType18.resolveInternal(errorReporter, scope);

    assertEquals(recordType, resolvedType);
  }

  @Test
  public void testRecordTypeWithFrozenProperties() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    Map<String, RecordProperty> properties = Maps.newHashMap();

    properties.put("x", new RecordProperty("x", int.class));
    properties.put("y", new RecordProperty("y", String.class));

    RecordType recordType = new RecordType(registry, properties);
    recordType.freeze();

    RecordType frozenRecord = new RecordType(registry, properties);

    // Basic checks
    assertTrue(recordType.isFrozen());
    assertFalse(frozenRecord.isFrozen());

    // Test defineProperty
    RecordProperty xProp = new RecordProperty("x", int.class);
    RecordProperty yProp = new RecordProperty("y", String.class);

    TreeMap<String, JSType> newProperties = new TreeMap<>();
    newProperties.put("x", xProp.getType());
    newProperties.put("y", yProp.getType());

    RecordType unfrozenRecord = new RecordType(registry, newProperties);

    assertFalse(recordType.defineProperty("x", int.class, false, null));
    assertFalse(recordType.defineProperty("y", String.class, false, null));

    // Test getGreatestSubtypeHelper
    JSType otherType = new ObjectType(registry, null, null);
    JSType intType = int.class.getType(registry);
    JSType recordType3 = new RecordType(registry, newProperties());
    JSType stringType = String.class.getType(registry);

    JSType greatestSubtype = recordType.getGreatestSubtypeHelper(otherType);
    assertSame(otherType, greatestSubtype);

    greatestSubtype = recordType.getGreatestSubtypeHelper(intType);
    assertSame(intType, greatestSubtype);

    greatestSubtype = recordType.getGreatestSubtypeHelper(recordType3);
    assertEquals(recordType, greatestSubtype);

    greatestSubtype = recordType.getGreatestSubtypeHelper(intType);
    assertEquals(intType, greatestSubtype);

    greatestSubtype = recordType.getGreatestSubtypeHelper(stringType);
    assertEquals(recordType, greatestSubtype);

    // Test isSubtype
    ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    RecordType recordType4 = new RecordType(registry, newProperties());

    assertTrue(recordType.isSubtype(recordType));
    assertTrue(recordType.isSubtype(recordType4));
    assertTrue(recordType.isSubtype(objectType));
    assertFalse(objectType.isSubtype(recordType));

    // Test isSubtype (2)
    RecordType recordType5 = new RecordType(registry, newProperties());
    JSType intType2 = int.class.getType(registry);
    RecordType recordType6 = new RecordType(registry, newProperties());
    RecordProperty zProp = new RecordProperty("z", int.class);

    TreeMap<String, JSType> properties2 = new TreeMap<>();
    properties2.put("z", zProp.getType());

    RecordType recordType7 = new RecordType(registry, properties2);
    RecordType recordType8 = new RecordType(registry, properties2);

    when(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).getPropertyType("z"))
        .thenReturn(zProp.getType());

    when(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).hasProperty("z"))
        .thenReturn(true);

    assertTrue(recordType.isSubtype(recordType5));
    assertTrue(recordType.isSubtype(recordType6));
    assertTrue(recordType.isSubtype(recordType7));
    assertTrue(recordType.isSubtype(recordType8));

    // Test isSubtype (3)
    ObjectType objectType2 = new ObjectType(registry, null, null);

    UnionTypeBuilder builder = new UnionTypeBuilder(registry);
    builder.addAlternate(objectType);

    JSType unionType = builder.build();
    RecordType recordType9 = new RecordType(registry, newProperties());
    RecordType recordType10 = new RecordType(registry, newProperties());
    RecordProperty zProp2 = new RecordProperty("z", int.class);

    TreeMap<String, JSType> properties3 = new TreeMap<>();
    properties3.put("z", zProp2.getType());

    RecordType recordType11 = new RecordType(registry, properties3);

    when(registry.getEachReferenceTypeWithProperty("z"))
        .thenReturn(new Object[] { objectType2, recordType11 });

    assertTrue(recordType.isSubtype(recordType9));
    assertTrue(recordType.isSubtype(recordType10));

    // Test isSubtype (4)
    RecordType recordType12 = new RecordType(registry, newProperties());
    RecordType recordType13 = new RecordType(registry, newProperties());

    RecordProperty xProp3 = new RecordProperty("x", int.class);
    RecordProperty xProp4 = new RecordProperty("x", int.class);
    RecordProperty zProp3 = new RecordProperty("z", int.class);

    TreeMap<String, JSType> properties4 = new TreeMap<>();
    properties4.put("x", xProp3.getType());
    properties4.put("z", zProp3.getType());

    RecordType recordType14 = new RecordType(registry, properties4);

    when(registry.getEachReferenceTypeWithProperty("x"))
        .thenReturn(new Object[] { recordType12, recordType13 });

    assertTrue(recordType.isSubtype(recordType14));
  }
}