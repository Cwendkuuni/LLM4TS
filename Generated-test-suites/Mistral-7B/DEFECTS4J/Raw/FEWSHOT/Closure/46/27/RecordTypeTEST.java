package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.Map;
import java.util.SortedMap;

public class RecordTypeTest {

    @Test
    public void testCreateRecordType() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("x", new RecordProperty("x", registry.getNumberType()));
        properties.put("y", new RecordProperty("y", registry.getBooleanType()));

        RecordType recordType = new RecordType(registry, properties);
        assertEquals("RecordType[x: Number, y: Boolean]", recordType.toString());
    }

    @Test
    public void testIsEquivalentTo() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> properties1 = Maps.newHashMap();
        properties1.put("x", new RecordProperty("x", registry.getNumberType()));
        properties1.put("y", new RecordProperty("y", registry.getBooleanType()));

        Map<String, RecordProperty> properties2 = Maps.newHashMap();
        properties2.put("x", new RecordProperty("x", registry.getNumberType()));
        properties2.put("y", new RecordProperty("y", registry.getBooleanType()));

        RecordType recordType1 = new RecordType(registry, properties1);
        RecordType recordType2 = new RecordType(registry, properties2);

        assertTrue(recordType1.isEquivalentTo(recordType2));
    }

    @Test
    public void testIsEquivalentToDifferentOrder() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> properties1 = Maps.newHashMap();
        properties1.put("x", new RecordProperty("x", registry.getNumberType()));
        properties1.put("y", new RecordProperty("y", registry.getBooleanType()));

        Map<String, RecordProperty> properties2 = Maps.newHashMap();
        properties2.put("y", new RecordProperty("y", registry.getBooleanType()));
        properties2.put("x", new RecordProperty("x", registry.getNumberType()));

        RecordType recordType1 = new RecordType(registry, properties1);
        RecordType recordType2 = new RecordType(registry, properties2);

        assertTrue(recordType1.isEquivalentTo(recordType2));
    }

    @Test
    public void testIsEquivalentToDifferentProperties() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> properties1 = Maps.newHashMap();
        properties1.put("x", new RecordProperty("x", registry.getNumberType()));
        properties1.put("y", new RecordProperty("y", registry.getBooleanType()));

        Map<String, RecordProperty> properties2 = Maps.newHashMap();
        properties2.put("x", new RecordProperty("x", registry.getBooleanType()));
        properties2.put("y", new RecordProperty("y", registry.getNumberType()));

        RecordType recordType1 = new RecordType(registry, properties1);
        RecordType recordType2 = new RecordType(registry, properties2);

        assertFalse(recordType1.isEquivalentTo(recordType2));
    }

    @Test
    public void testIsEquivalentToNull() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("x", new RecordProperty("x", registry.getNumberType()));

        RecordType recordType = new RecordType(registry, properties);

        assertFalse(recordType.isEquivalentTo(null));
    }

    @Test
    public void testIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> properties1 = Maps.newHashMap();
        properties1.put("x", new RecordProperty("x", registry.getNumberType()));
        properties1.put("y", new RecordProperty("y", registry.getBooleanType()));

        Map<String, RecordProperty> properties2 = Maps.newHashMap();
        properties2.put("x", new RecordProperty("x", registry.getNumberType()));
        properties2.put("y", new RecordProperty("y", registry.getBooleanType()));

        RecordType recordType1 = new RecordType(registry, properties1);
        RecordType recordType2 = new RecordType(registry, properties2);

        assertTrue(recordType1.isSubtype(recordType2));
        assertTrue(recordType2.isSubtype(recordType1));
    }

    @Test
    public void testIsSubtypeDifferentOrder() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> properties1 = Maps.newHashMap();
        properties1.put("x", new RecordProperty("x", registry.getNumberType()));
        properties1.put("y", new RecordProperty("y", registry.getBooleanType()));

        Map<String, RecordProperty> properties2 = Maps.newHashMap();
        properties2.put("y", new RecordProperty("y", registry.getBooleanType()));
        properties2.put("x", new RecordProperty("x", registry.getNumberType()));

        RecordType recordType1 = new RecordType(registry, properties1);
        RecordType recordType2 = new RecordType(registry, properties2);

        assertTrue(recordType1.isSubtype(recordType2));
        assertTrue(recordType2.isSubtype(recordType1));
    }

    @Test
    public void testIsSubtypeDifferentProperties() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> properties1 = Maps.newHashMap();
        properties1.put("x", new RecordProperty("x", registry.getNumberType()));
        properties1.put("y", new RecordProperty("y", registry.getBooleanType()));

        Map<String, RecordProperty> properties2 = Maps.newHashMap();
        properties2.put("y", new RecordProperty("y", registry.getBooleanType()));
        properties2.put("z", new RecordProperty("z", registry.getNumberType()));

        RecordType recordType1 = new RecordType(registry, properties1);
        RecordType recordType2 = new RecordType(registry, properties2);

        assertFalse(recordType1.isSubtype(recordType2));
    }

    @Test
    public void testIsSubtypeNull() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("x", new RecordProperty("x", registry.getNumberType()));

        RecordType recordType = new RecordType(registry, properties);

        assertFalse(recordType.isSubtype(null));
    }

    @Test
    public void testIsSubtypeSelf() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("x", new RecordProperty("x", registry.getNumberType()));

        RecordType recordType = new RecordType(registry, properties);

        assertTrue(recordType.isSubtype(recordType));
    }

    @Test
    public void testGetLeastSupertype() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> properties1 = Maps.newHashMap();
        properties1.put("x", new RecordProperty("x", registry.getNumberType()));
        properties1.put("y", new RecordProperty("y", registry.getBooleanType()));

        Map<String, RecordProperty> properties2 = Maps.newHashMap();
        properties2.put("x", new RecordProperty("x", registry.getNumberType()));
        properties2.put("y", new RecordProperty("y", registry.getBooleanType()));
        properties2.put("z", new RecordProperty("z", registry.getBooleanType()));

        Map<String, RecordProperty> properties3 = Maps.newHashMap();
        properties3.put("x", new RecordProperty("x", registry.getNumberType()));

        RecordType recordType1 = new RecordType(registry, properties1);
        RecordType recordType2 = new RecordType(registry, properties2);
        RecordType recordType3 = new RecordType(registry, properties3);

        RecordType leastSupertype = recordType1.getLeastSupertype(recordType2);
        assertEquals("RecordType[x: Number, y: Boolean]", leastSupertype.toString());

        leastSupertype = recordType1.getLeastSupertype(recordType3);
        assertEquals("RecordType[x: Number]", leastSupertype.toString());
    }

    @Test
    public void testGetLeastSupertypeEmptyRecord() {
        JSTypeRegistry registry = new JSTypeRegistry();

        RecordType emptyRecord = registry.getNativeObjectType(
                JSTypeNative.OBJECT_TYPE).getRecordType();

        RecordType recordType = new RecordType(registry, Maps.newHashMap());

        RecordType leastSupertype = recordType.getLeastSupertype(emptyRecord);
        assertEquals("RecordType[]", leastSupertype.toString());
    }

    @Test
    public void testGetLeastSupertypeNativeObject() {
        JSTypeRegistry registry = new JSTypeRegistry();

        RecordType recordType = new RecordType(registry, Maps.newHashMap());

        RecordType leastSupertype = recordType.getLeastSupertype(
                registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
        assertEquals("RecordType[]", leastSupertype.toString());
    }

    @Test
    public void testGetGreatestSubtypeHelperNull() {
        JSTypeRegistry registry = new JSTypeRegistry();

        RecordType recordType = new RecordType(registry, Maps.newHashMap());

        assertNull(recordType.getGreatestSubtypeHelper(null));
    }

    @Test
    public void testGetGreatestSubtypeHelperNativeObject() {
        JSTypeRegistry registry = new JSTypeRegistry();

        RecordType recordType = new RecordType(registry, Maps.newHashMap());

        JSType nativeObjectType = registry.getNativeType(
                JSTypeNative.OBJECT_TYPE);

        assertNull(recordType.getGreatestSubtypeHelper(nativeObjectType));
    }

    @Test
    public void testGetGreatestSubtypeHelperEmptyRecord() {
        JSTypeRegistry registry = new JSTypeRegistry();

        RecordType recordType = new RecordType(registry, Maps.newHashMap());

        RecordType emptyRecord = registry.getNativeObjectType(
                JSTypeNative.OBJECT_TYPE).getRecordType();

        RecordType greatestSubtype = recordType.getGreatestSubtypeHelper(emptyRecord);
        assertEquals("RecordType[]", greatestSubtype.toString());
    }

    @Test
    public void testGetGreatestSubtypeHelperSelf() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("x", new RecordProperty("x", registry.getNumberType()));

        RecordType recordType = new RecordType(registry, properties);

        RecordType greatestSubtype = recordType.getGreatestSubtypeHelper(recordType);
        assertEquals("RecordType[x: Number]", greatestSubtype.toString());
    }

    @Test
    public void testGetGreatestSubtypeHelperSameProperties() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> properties1 = Maps.newHashMap();
        properties1.put("x", new RecordProperty("x", registry.getNumberType()));
        properties1.put("y", new RecordProperty("y", registry.getBooleanType()));

        Map<String, RecordProperty> properties2 = Maps.newHashMap();
        properties2.put("x", new RecordProperty("x", registry.getNumberType()));
        properties2.put("y", new RecordProperty("y", registry.getBooleanType()));

        RecordType recordType1 = new RecordType(registry, properties1);
        RecordType recordType2 = new RecordType(registry, properties2);

        RecordType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
        assertEquals("RecordType[x: Number, y: Boolean]", greatestSubtype.toString());
    }

    @Test
    public void testGetGreatestSubtypeHelperDifferentProperties() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> properties1 = Maps.newHashMap();
        properties1.put("x", new RecordProperty("x", registry.getNumberType()));
        properties1.put("y", new RecordProperty("y", registry.getBooleanType()));

        Map<String, RecordProperty> properties2 = Maps.newHashMap();
        properties2.put("x", new RecordProperty("x", registry.getNumberType()));
        properties2.put("z", new RecordProperty("z", registry.getBooleanType()));

        RecordType recordType1 = new RecordType(registry, properties1);
        RecordType recordType2 = new RecordType(registry, properties2);

        RecordType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
        assertEquals("RecordType[x: Number]", greatestSubtype.toString());
    }

    @Test
    public void testDefinePropertyFrozen() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("x", new RecordProperty("x", registry.getNumberType()));

        RecordType recordType = new RecordType(registry, properties);
        recordType.isFrozen = true;

        assertFalse(recordType.defineProperty("y", registry.getBooleanType(), false, null));
    }

    @Test
    public void testDefinePropertyUnfrozen() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("x", new RecordProperty("x", registry.getNumberType()));

        RecordType recordType = new RecordType(registry, properties);

        assertTrue(recordType.defineProperty("y", registry.getBooleanType(), false, null));
    }
}