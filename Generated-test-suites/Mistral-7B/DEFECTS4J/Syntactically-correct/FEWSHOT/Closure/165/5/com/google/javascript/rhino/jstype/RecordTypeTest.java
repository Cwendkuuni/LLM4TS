package com.google.javascript.rhino.jstype; // import statements go here if needed

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class RecordTypeTest {

    @Test
    public void testRecordTypeCreation() {
        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("name", new RecordProperty("name", String.class));
        properties.put("age", new RecordProperty("age", Integer.class));

        JSTypeRegistry registry = new JSTypeRegistry();
        RecordType recordType = new RecordType(registry, properties);

        // Basic checks
        assertEquals("RecordType", recordType.getClass().getSimpleName());
        assertEquals(2, recordType.getPropertyCount());
        assertFalse(recordType.isFrozen());

        // Property checks
        assertEquals("name", recordType.getPropertyNames().get(0));
        assertEquals(String.class, recordType.getPropertyType("name"));
        assertEquals("age", recordType.getPropertyNames().get(1));
        assertEquals(Integer.class, recordType.getPropertyType("age"));

        // Freeze check
        recordType.freeze();
        assertTrue(recordType.isFrozen());
    }

    @Test
    public void testIsEquivalentTo() {
        Map<String, RecordProperty> properties1 = Maps.newHashMap();
        properties1.put("name", new RecordProperty("name", String.class));
        properties1.put("age", new RecordProperty("age", Integer.class));

        Map<String, RecordProperty> properties2 = Maps.newHashMap();
        properties2.put("name", new RecordProperty("name", String.class));
        properties2.put("age", new RecordProperty("age", Integer.class));
        properties2.put("gender", new RecordProperty("gender", String.class));

        JSTypeRegistry registry = new JSTypeRegistry();
        RecordType recordType1 = new RecordType(registry, properties1);
        RecordType recordType2 = new RecordType(registry, properties2);

        RecordType recordType3 = new RecordType(registry, properties1);

        // Same object test
        assertTrue(recordType1.isEquivalentTo(recordType1));

        // Equal records test
        assertTrue(recordType1.isEquivalentTo(recordType3));

        // Different records test
        assertFalse(recordType1.isEquivalentTo(recordType2));

        // Different classes test
        JSType otherType = new Int32Type();
        assertFalse(recordType1.isEquivalentTo(otherType));
    }

    @Test
    public void testGetImplicitPrototype() {
        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("name", new RecordProperty("name", String.class));
        properties.put("age", new RecordProperty("age", Integer.class));

        JSTypeRegistry registry = new JSTypeRegistry();
        RecordType recordType = new RecordType(registry, properties);

        ObjectType prototypeObjectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        ObjectType expectedPrototypeObjectType = new ObjectType(registry, null, null);
        assertEquals(expectedPrototypeObjectType, recordType.getImplicitPrototype());
    }

    @Test
    public void testDefineProperty() {
        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("name", new RecordProperty("name", String.class));
        properties.put("age", new RecordProperty("age", Integer.class));

        JSTypeRegistry registry = new JSTypeRegistry();
        RecordType recordType = new RecordType(registry, properties);

        String newPropertyName = "gender";
        JSType newPropertyType = String.class;

        // Define property
        recordType.defineProperty(newPropertyName, newPropertyType, false, null);

        // Check property is defined
        assertTrue(recordType.hasProperty(newPropertyName));
        assertEquals(newPropertyType, recordType.getPropertyType(newPropertyName));

        // Freeze and check property cannot be defined again
        recordType.freeze();
        assertFalse(recordType.defineProperty(newPropertyName, newPropertyType, false, null));
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        Map<String, RecordProperty> properties1 = Maps.newHashMap();
        properties1.put("name", new RecordProperty("name", String.class));
        properties1.put("age", new RecordProperty("age", Integer.class));

        Map<String, RecordProperty> properties2 = Maps.newHashMap();
        properties2.put("name", new RecordProperty("name", String.class));
        properties2.put("age", new RecordProperty("age", Integer.class));
        properties2.put("gender", new RecordProperty("gender", String.class));

        JSTypeRegistry registry = new JSTypeRegistry();
        RecordType recordType1 = new RecordType(registry, properties1);
        RecordType recordType2 = new RecordType(registry, properties2);

        UnionTypeBuilder unionTypeBuilder = new UnionTypeBuilder(registry);

        // Check property intersection
        unionTypeBuilder.addAlternate(recordType1);
        unionTypeBuilder.addAlternate(registry.getNativeType(JSTypeNative.OBJECT_TYPE)
                .getRecordType(Maps.newHashMap()));
        JSType greatestSubtype1 = unionTypeBuilder.build();

        unionTypeBuilder = new UnionTypeBuilder(registry);
        unionTypeBuilder.addAlternate(recordType2);
        unionTypeBuilder.addAlternate(registry.getNativeType(JSTypeNative.OBJECT_TYPE)
                .getRecordType(Maps.newHashMap()));
        JSType greatestSubtype2 = unionTypeBuilder.build();

        JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);

        // Check greatest subtype is correct
        assertEquals(greatestSubtype1, greatestSubtype);
        assertEquals(greatestSubtype2, greatestSubtype.getGreatestSubtype(recordType1));
    }

    @Test
    public void testIsSubtype() {
        Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
        recordProperties1.put("name", new RecordProperty("name", String.class));
        recordProperties1.put("age", new RecordProperty("age", Integer.class));

        Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
        recordProperties2.put("name", new RecordProperty("name", String.class));
        recordProperties2.put("age", new RecordProperty("age", Integer.class));
        recordProperties2.put("gender", new RecordProperty("gender", String.class));

        Map<String, RecordProperty> recordProperties3 = Maps.newHashMap();
        recordProperties3.put("name", new RecordProperty("name", String.class));
        recordProperties3.put("age", new RecordProperty("age", Integer.class));
        recordProperties3.put("gender", new RecordProperty("gender", Number.class));

        JSTypeRegistry registry = new JSTypeRegistry();
        RecordType recordType1 = new RecordType(registry, recordProperties1);
        RecordType recordType2 = new RecordType(registry, recordProperties2);
        RecordType recordType3 = new RecordType(registry, recordProperties3);

        ObjectType objectType = registry.getNativeType(JSTypeNative.OBJECT_TYPE);
        RecordType recordTypeWithExtraProperty = registry.getNativeType(JSTypeNative.OBJECT_TYPE)
                .getRecordType(Maps.newHashMap());

        // Test types are equal case
        assertTrue(recordType1.isSubtype(recordType1));

        // Test recordType1 is a subtype of recordType1 with extra property
        assertTrue(recordType1.isSubtype(recordTypeWithExtraProperty));

        // Test recordType1 is a subtype of recordType2
        assertTrue(recordType1.isSubtype(recordType2));

        // Test recordType1 is not a subtype of objectType
        assertFalse(recordType1.isSubtype(objectType));

        // Test recordType2 is a subtype of recordType1
        assertTrue(recordType2.isSubtype(recordType1));

        // Test recordType1 is a subtype of recordType3
        assertFalse(recordType1.isSubtype(recordType3));

        // Test recordType3 is a subtype of recordType1
        assertFalse(recordType3.isSubtype(recordType1));
    }

    @Test
    public void testResolveInternal() {
        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("name", new RecordProperty("name", String.class));
        properties.put("age", new RecordProperty("age", Integer.class));

        JSTypeRegistry registry = new JSTypeRegistry();
        RecordType recordType = new RecordType(registry, properties);

        String propertyName = "name";
        JSType propertyType = String.class;

        // Check resolveInternal resolves properties
        recordType.resolveInternal(null, null);
        assertTrue(recordType.hasProperty(propertyName));
        assertEquals(propertyType, recordType.getPropertyType(propertyName));

        // Freeze and check properties cannot be resolved again
        recordType.freeze();
        assertFalse(recordType.resolveInternal(null, null).hasProperty(propertyName));
    }
}