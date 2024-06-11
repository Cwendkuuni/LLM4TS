package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import org.junit.Test;

public class RecordTypeTest {
  @Test
  public void testCheckRecordEquivalenceHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType1 = new RecordType(registry, Maps.newHashMap());
    RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
    RecordType recordType3 = new RecordType(registry, Maps.newHashMap());

    // Check equivalence of identical records.
    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType1, false));

    // Check equivalence of records with same properties but in different order.
    Map<String, RecordProperty> properties = Maps.newLinkedHashMap();
    properties.put("b", new RecordProperty("b", registry.getIntType()));
    properties.put("a", new RecordProperty("a", registry.getIntType()));
    recordType1 = new RecordType(registry, properties, true);
    properties = Maps.newLinkedHashMap();
    properties.put("a", new RecordProperty("a", registry.getIntType()));
    properties.put("b", new RecordProperty("b", registry.getIntType()));
    recordType2 = new RecordType(registry, properties, true);
    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, false));

    // Check inequivalence of records with different properties.
    properties = Maps.newLinkedHashMap();
    properties.put("a", new RecordProperty("a", registry.getIntType()));
    properties.put("b", new RecordProperty("b", registry.getIntType()));
    recordType1 = new RecordType(registry, properties, true);
    properties = Maps.newLinkedHashMap();
    properties.put("c", new RecordProperty("c", registry.getIntType()));
    recordType2 = new RecordType(registry, properties, true);
    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, false));

    // Check inequivalence of records with conflicting properties.
    properties = Maps.newLinkedHashMap();
    properties.put("a", new RecordProperty("a", registry.getIntType()));
    properties.put("b", new RecordProperty("b", registry.getBooleanType()));
    recordType1 = new RecordType(registry, properties, true);
    properties = Maps.newLinkedHashMap();
    properties.put("a", new RecordProperty("a", registry.getBooleanType()));
    recordType2 = new RecordType(registry, properties, true);
    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, false));
  }

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    // ObjectType is a subtype of every record type.
    RecordType recordType1 = new RecordType(registry, Maps.newHashMap());
    assertTrue(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)
        .isSubtype(recordType1));

    // A record type is a subtype of itself.
    assertTrue(recordType1.isSubtype(recordType1));

    // A record type with no properties is a subtype of every record type.
    RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
    assertTrue(recordType1.isSubtype(recordType2));

    // A record type with properties is a subtype of a record type
    // with the same properties and types, in any order.
    Map<String, RecordProperty> properties = Maps.newLinkedHashMap();
    properties.put("a", new RecordProperty("a", registry.getIntType()));
    properties.put("b", new RecordProperty("b", registry.getIntType()));
    recordType1 = new RecordType(registry, properties, true);
    properties = Maps.newLinkedHashMap();
    properties.put("b", new RecordProperty("b", registry.getIntType()));
    properties.put("a", new RecordProperty("a", registry.getIntType()));
    recordType2 = new RecordType(registry, properties, true);
    assertTrue(recordType1.isSubtype(recordType2));

    // A record type with properties is a subtype of a record type
    // with the same properties and types, but inferred.
    properties = Maps.newLinkedHashMap();
    properties.put("a", new RecordProperty("a", registry.getIntType()));
    properties.put("b", new RecordProperty("b", registry.getIntType()));
    recordType1 = new RecordType(registry, properties, false);
    properties = Maps.newLinkedHashMap();
    properties.put("b", new RecordProperty("b", registry.getIntType()));
    properties.put("a", new RecordProperty("a", registry.getIntType()));
    recordType2 = new RecordType(registry, properties, false);
    assertTrue(recordType1.isSubtype(recordType2));

    // A record type with properties is not a subtype of a record type
    // with a different property.
    properties = Maps.newLinkedHashMap();
    properties.put("a", new RecordProperty("a", registry.getIntType()));
    properties.put("b", new RecordProperty("b", registry.getIntType()));
    recordType1 = new RecordType(registry, properties, true);
    properties = Maps.newLinkedHashMap();
    properties.put("c", new RecordProperty("c", registry.getIntType()));
    recordType2 = new RecordType(registry, properties, true);
    assertFalse(recordType1.isSubtype(recordType2));

    // A record type is a subtype of a record type with a supertype property.
    properties = Maps.newLinkedHashMap();
    properties.put("a", new RecordProperty("a", registry.getIntType()));
    properties.put("b", new RecordProperty("b", registry.getObjectType()));
    recordType1 = new RecordType(registry, properties, true);
    properties = Maps.newLinkedHashMap();
    properties.put("a", new RecordProperty("a", registry.getIntType()));
    properties.put("b", new RecordProperty("b", registry.getIntType()));
    recordType2 = new RecordType(registry, properties, true);
    assertTrue(recordType1.isSubtype(recordType2));

    // A record type is not a subtype of a record type with a subtype property.
    properties = Maps.newLinkedHashMap();
    properties.put("a", new RecordProperty("a", registry.getIntType()));
    properties.put("b", new RecordProperty("b", registry.getObjectType()));
    recordType1 = new RecordType(registry, properties, true);
    properties = Maps.newLinkedHashMap();
    properties.put("a", new RecordProperty("a", registry.getIntType()));
    properties.put("b", new RecordProperty("b", registry.getNativeObjectType(
        JSTypeNative.NUMBER_TYPE).getGreatestSubtype(registry.getIntType())));
    recordType2 = new RecordType(registry, properties, true);
    assertFalse(recordType1.isSubtype(recordType2));
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();

    // The greatest subtype of a record type and a null type is the record type.
    RecordType recordType1 = new RecordType(registry, Maps.newHashMap());
    JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(null);
    assertTrue(recordType1.isEquivalentTo(greatestSubtype));

    // The greatest subtype of a record type and itself is the record type.
    greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType1);
    assertTrue(recordType1.isEquivalentTo(greatestSubtype));

    // The greatest subtype of two record types with the same properties is the record type.
    Map<String, RecordProperty> properties = Maps.newLinkedHashMap();
    properties.put("a", new RecordProperty("a", registry.getIntType()));
    properties.put("b", new RecordProperty("b", registry.getIntType()));
    recordType1 = new RecordType(registry, properties, true);
    properties = Maps.newLinkedHashMap();
    properties.put("a", new RecordProperty("a", registry.getIntType()));
    properties.put("b", new RecordProperty("b", registry.getIntType()));
    RecordType recordType2 = new RecordType(registry, properties, true);
    greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
    assertTrue(recordType1.isEquivalentTo(greatestSubtype));

    // The greatest subtype of two record types with different properties is the empty record type.
    properties = Maps.newLinkedHashMap();
    properties.put("a", new RecordProperty("a", registry.getIntType()));
    properties.put("b", new RecordProperty("b", registry.getIntType()));
    recordType1 = new RecordType(registry, properties, true);
    properties = Maps.newLinkedHashMap();
    properties.put("c", new RecordProperty("c", registry.getIntType()));
    recordType2 = new RecordType(registry, properties, true);
    greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
    assertTrue(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)
        .isEquivalentTo(greatestSubtype));

    // The greatest subtype of a record type and a primitive type is the primitive type, if the property is of that type.
    RecordType recordType3 = new RecordType(registry, Maps.newHashMap());
    properties = Maps.newLinkedHashMap();
    properties.put("a", new RecordProperty("a", registry.getIntType()));
    recordType1 = new RecordType(registry, properties, true);
    greatestSubtype = recordType1.getGreatestSubtypeHelper(registry.getIntType());
    assertTrue(registry.getIntType().isEquivalentTo(greatestSubtype));

    // The greatest subtype of a record type and a primitive type is the record type, if the property is of a different type.
    properties = Maps.newLinkedHashMap();
    properties.put("a", new RecordProperty("a", registry.getBooleanType()));
    recordType1 = new RecordType(registry, properties, true);
    greatestSubtype = recordType1.getGreatestSubtypeHelper(registry.getIntType());
    assertTrue(recordType1.isEquivalentTo(greatestSubtype));

    // The greatest subtype of a record type and a union type is the record type that includes all alternatives that match the record's properties.
    properties = Maps.newLinkedHashMap();
    properties.put("a", new RecordProperty("a", registry.getIntType()));
    UnionType unionType = new UnionType(registry, new Object[] {
        registry.getIntType(), registry.getStringType()
    });
    RecordType recordType4 = new RecordType(registry, properties, true);
    greatestSubtype = recordType1.getGreatestSubtypeHelper(unionType);
    Map<String, JSType> propertiesUnion = unionType.getTypes();
    RecordType recordType5 = new RecordType(registry, propertiesUnion, false);
    assertTrue(recordType4.isSubtype(recordType5));
    assertTrue(recordType5.isSubtype(recordType4));
    assertTrue(recordType1.isSubtype(recordType5));
  }
}