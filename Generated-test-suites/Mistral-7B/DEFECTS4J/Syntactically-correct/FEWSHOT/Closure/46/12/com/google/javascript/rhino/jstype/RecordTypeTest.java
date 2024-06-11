package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.Map;
import java.util.SortedMap;

public class RecordTypeTest {

    @Test
    public void testRecordTypeCreation() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("name", new RecordProperty("name", String.class));
        properties.put("age", new RecordProperty("age", Number.class));

        RecordType recordType = new RecordType(registry, properties);
        assertEquals("RecordType[name: String, age: Number]", recordType.toString());
    }

    @Test
    public void testRecordTypeIsEquivalentToItself() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("name", new RecordProperty("name", String.class));
        properties.put("age", new RecordProperty("age", Number.class));

        RecordType recordType1 = new RecordType(registry, properties);
        RecordType recordType2 = new RecordType(registry, properties);

        assertTrue(recordType1.isEquivalentTo(recordType2));
    }

    @Test
    public void testRecordTypeIsNotEquivalentToDifferentProperties() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties1 = Maps.newHashMap();
        properties1.put("name", new RecordProperty("name", String.class));
        properties1.put("age", new RecordProperty("age", Number.class));

        Map<String, RecordProperty> properties2 = Maps.newHashMap();
        properties2.put("name", new RecordProperty("name", String.class));
        properties2.put("age", new RecordProperty("age", Boolean.class));

        RecordType recordType1 = new RecordType(registry, properties1);
        RecordType recordType2 = new RecordType(registry, properties2);

        assertFalse(recordType1.isEquivalentTo(recordType2));
    }

    @Test
    public void testRecordTypeIsSubtypeOfEmptyObjectType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordType recordType = new RecordType(registry, Maps.newTreeMap());

        assertTrue(recordType.isEquivalentTo(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
    }

    @Test
    public void testRecordTypeIsSubtypeOfRecordTypeWithSameProperties() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties1 = Maps.newHashMap();
        properties1.put("name", new RecordProperty("name", String.class));
        properties1.put("age", new RecordProperty("age", Number.class));

        Map<String, RecordProperty> properties2 = Maps.newHashMap();
        properties2.put("name", new RecordProperty("name", String.class));
        properties2.put("age", new RecordProperty("age", Number.class));

        RecordType recordType1 = new RecordType(registry, properties1);
        RecordType recordType2 = new RecordType(registry, properties2);

        assertTrue(recordType1.isSubtype(recordType2));
    }

    @Test
    public void testRecordTypeIsNotSubtypeOfRecordTypeWithDifferentProperties() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties1 = Maps.newHashMap();
        properties1.put("name", new RecordProperty("name", String.class));
        properties1.put("age", new RecordProperty("age", Number.class));

        Map<String, RecordProperty> properties2 = Maps.newHashMap();
        properties2.put("name", new RecordProperty("name", String.class));
        properties2.put("age", new RecordProperty("age", Boolean.class));

        RecordType recordType1 = new RecordType(registry, properties1);
        RecordType recordType2 = new RecordType(registry, properties2);

        assertFalse(recordType1.isSubtype(recordType2));
    }

    @Test
    public void testRecordTypeIsSubtypeOfRecordTypeWithFewerProperties() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties1 = Maps.newHashMap();
        properties1.put("name", new RecordProperty("name", String.class));
        properties1.put("age", new RecordProperty("age", Number.class));

        Map<String, RecordProperty> properties2 = Maps.newHashMap();
        properties2.put("name", new RecordProperty("name", String.class));

        RecordType recordType1 = new RecordType(registry, properties1);
        RecordType recordType2 = new RecordType(registry, properties2);

        assertTrue(recordType1.isSubtype(recordType2));
    }

    @Test
    public void testRecordTypeIsNotSubtypeOfRecordTypeWithMoreProperties() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties1 = Maps.newHashMap();
        properties1.put("name", new RecordProperty("name", String.class));
        properties1.put("age", new RecordProperty("age", Number.class));

        Map<String, RecordProperty> properties2 = Maps.newHashMap();
        properties2.put("name", new RecordProperty("name", String.class));
        properties2.put("age", new RecordProperty("age", Number.class));
        properties2.put("city", new RecordProperty("city", String.class));

        RecordType recordType1 = new RecordType(registry, properties1);
        RecordType recordType2 = new RecordType(registry, properties2);

        assertFalse(recordType1.isSubtype(recordType2));
    }

    @Test
    public void testGetLeastSupertype() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> properties1 = Maps.newHashMap();
        properties1.put("name", new RecordProperty("name", String.class));
        properties1.put("age", new RecordProperty("age", Number.class));

        Map<String, RecordProperty> properties2 = Maps.newHashMap();
        properties2.put("name", new RecordProperty("name", String.class));
        properties2.put("age", new RecordProperty("age", Number.class));
        properties2.put("city", new RecordProperty("city", String.class));

        RecordType recordType1 = new RecordType(registry, properties1);
        RecordType recordType2 = new RecordType(registry, properties2);

        RecordType leastSupertype = recordType1.getLeastSupertype(recordType2);
        assertEquals("RecordType[name: String, age: Number]", leastSupertype.toString());
    }

    @Test
    public void testGetLeastSupertypeWithNullProperties() {
        JSTypeRegistry registry = new JSTypeRegistry();

        RecordType leastSupertype = new RecordType(registry, null).getLeastSupertype(null);
        assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), leastSupertype);
    }

    @Test
    public void testGetLeastSupertypeWithEmptyProperties() {
        JSTypeRegistry registry = new JSTypeRegistry();

        RecordType leastSupertype = new RecordType(registry, Maps.newTreeMap()).getLeastSupertype(new RecordType(registry, Maps.newTreeMap()));
        assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), leastSupertype);
    }

    @Test
    public void testGetGreatestSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> properties1 = Maps.newHashMap();
        properties1.put("name", new RecordProperty("name", String.class));
        properties1.put("age", new RecordProperty("age", Number.class));

        Map<String, RecordProperty> properties2 = Maps.newHashMap();
        properties2.put("name", new RecordProperty("name", String.class));
        properties2.put("age", new RecordProperty("age", Number.class));
        properties2.put("city", new RecordProperty("city", String.class));

        RecordType recordType1 = new RecordType(registry, properties1);
        RecordType recordType2 = new RecordType(registry, properties2);

        RecordType greatestSubtype = recordType1.getGreatestSubtype(recordType2);
        assertEquals("RecordType[name: String, age: Number, city: String]", greatestSubtype.toString());
    }

    @Test
    public void testGetGreatestSubtypeWithNullProperties() {
        JSTypeRegistry registry = new JSTypeRegistry();

        RecordType greatestSubtype = new RecordType(registry, null).getGreatestSubtype(null);
        assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), greatestSubtype);
    }

    @Test
    public void testGetGreatestSubtypeWithEmptyProperties() {
        JSTypeRegistry registry = new JSTypeRegistry();

        RecordType greatestSubtype = new RecordType(registry, Maps.newTreeMap()).getGreatestSubtype(new RecordType(registry, Maps.newTreeMap()));
        assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), greatestSubtype);
    }

    @Test
    public void testIsSubtypeWithSameProperties() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> properties1 = Maps.newHashMap();
        properties1.put("name", new RecordProperty("name", String.class));
        properties1.put("age", new RecordProperty("age", Number.class));

        Map<String, RecordProperty> properties2 = Maps.newHashMap();
        properties2.put("name", new RecordProperty("name", String.class));
        properties2.put("age", new RecordProperty("age", Number.class));

        RecordType recordType1 = new RecordType(registry, properties1);
        RecordType recordType2 = new RecordType(registry, properties2);

        assertTrue(RecordType.isSubtype(recordType1, recordType2));
        assertTrue(RecordType.isSubtype(recordType2, recordType1));
    }

    @Test
    public void testIsSubtypeWithMoreProperties() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> properties1 = Maps.newHashMap();
        properties1.put("name", new RecordProperty("name", String.class));
        properties1.put("age", new RecordProperty("age", Number.class));

        Map<String, RecordProperty> properties2 = Maps.newHashMap();
        properties2.put("name", new RecordProperty("name", String.class));
        properties2.put("age", new RecordProperty("age", Number.class));
        properties2.put("city", new RecordProperty("city", String.class));

        RecordType recordType1 = new RecordType(registry, properties1);
        RecordType recordType2 = new RecordType(registry, properties2);

        assertFalse(RecordType.isSubtype(recordType1, recordType2));
    }

    @Test
    public void testIsSubtypeWithFewerProperties() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> properties1 = Maps.newHashMap();
        properties1.put("name", new RecordProperty("name", String.class));
        properties1.put("age", new RecordProperty("age", Number.class));

        Map<String, RecordProperty> properties2 = Maps.newHashMap();
        properties2.put("name", new RecordProperty("name", String.class));

        RecordType recordType1 = new RecordType(registry, properties1);
        RecordType recordType2 = new RecordType(registry, properties2);

        assertTrue(RecordType.isSubtype(recordType1, recordType2));
    }

    @Test
    public void testIsSubtypeWithDifferentPropertyTypes() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> properties1 = Maps.newHashMap();
        properties1.put("name", new RecordProperty("name", String.class));
        properties1.put("age", new RecordProperty("age", Number.class));

        Map<String, RecordProperty> properties2 = Maps.newHashMap();
        properties2.put("name", new RecordProperty("name", Boolean.class));
        properties2.put("age", new RecordProperty("age", Number.class));

        RecordType recordType1 = new RecordType(registry, properties1);
        RecordType recordType2 = new RecordType(registry, properties2);

        assertFalse(RecordType.isSubtype(recordType1, recordType2));
    }

    @Test
    public void testIsSubtypeWithDifferentPropertyNames() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> properties1 = Maps.newHashMap();
        properties1.put("name", new RecordProperty("name", String.class));
        properties1.put("age", new RecordProperty("age", Number.class));

        Map<String, RecordProperty> properties2 = Maps.newHashMap();
        properties2.put("age", new RecordProperty("age", Number.class));
        properties2.put("city", new RecordProperty("city", String.class));

        RecordType recordType1 = new RecordType(registry, properties1);
        RecordType recordType2 = new RecordType(registry, properties2);

        assertFalse(RecordType.isSubtype(recordType1, recordType2));
    }

    @Test
    public void testGetPropertyType() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("name", new RecordProperty("name", String.class));
        properties.put("age", new RecordProperty("age", Number.class));

        RecordType recordType = new RecordType(registry, properties);

        assertEquals(String.class, recordType.getPropertyType("name"));
        assertEquals(Number.class, recordType.getPropertyType("age"));
        assertNull(recordType.getPropertyType("unknown"));
    }

    @Test
    public void testHasProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("name", new RecordProperty("name", String.class));
        properties.put("age", new RecordProperty("age", Number.class));

        RecordType recordType = new RecordType(registry, properties);

        assertTrue(recordType.hasProperty("name"));
        assertTrue(recordType.hasProperty("age"));
        assertFalse(recordType.hasProperty("unknown"));
    }

    @Test
    public void testDefineProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("name", new RecordProperty("name", String.class));
        properties.put("age", new RecordProperty("age", Number.class));

        RecordType recordType = new RecordType(registry, properties);

        recordType.defineProperty("city", String.class, false, null);

        assertEquals(String.class, recordType.getPropertyType("city"));
        assertTrue(recordType.hasProperty("city"));
    }

    @Test(expected = IllegalStateException.class)
    public void testDefinePropertyWithNullProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("name", new RecordProperty("name", String.class));
        properties.put("age", new RecordProperty("age", Number.class));

        RecordType recordType = new RecordType(registry, properties);

        recordType.defineProperty(null, String.class, false, null);
    }
}