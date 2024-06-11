package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

public class RecordTypeTest {

    @Test
    public void testRecordTypeCreation() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> propertiesMap = Maps.newHashMap();
        propertiesMap.put("name", new RecordProperty("name", String.class));
        RecordType recordType = new RecordType(registry, propertiesMap);
        assertEquals("RecordType[name: String]", recordType.toString());
    }

    @Test
    public void testIsEquivalentTo() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> propertiesMap1 = Maps.newHashMap();
        propertiesMap1.put("name", new RecordProperty("name", String.class));
        RecordType recordType1 = new RecordType(registry, propertiesMap1);

        Map<String, RecordProperty> propertiesMap2 = Maps.newHashMap();
        propertiesMap2.put("name", new RecordProperty("name", String.class));
        propertiesMap2.put("age", new RecordProperty("age", Number.class));
        RecordType recordType2 = new RecordType(registry, propertiesMap2);

        Map<String, RecordProperty> propertiesMap3 = Maps.newHashMap();
        propertiesMap3.put("name", new RecordProperty("name", Number.class));
        RecordType recordType3 = new RecordType(registry, propertiesMap3);

        Map<String, RecordProperty> propertiesMap4 = Maps.newHashMap();
        propertiesMap4.put("age", new RecordProperty("age", Number.class));
        RecordType recordType4 = new RecordType(registry, propertiesMap4);

        Map<String, RecordProperty> propertiesMap5 = Maps.newHashMap();
        propertiesMap5.put("name", new RecordProperty("name", String.class));
        propertiesMap5.put("age", new RecordProperty("age", Number.class));
        RecordType recordType5 = new RecordType(registry, propertiesMap5);

        assertTrue(recordType1.isEquivalentTo(recordType1));
        assertTrue(recordType1.isEquivalentTo(recordType5));
        assertFalse(recordType1.isEquivalentTo(recordType2));
        assertFalse(recordType1.isEquivalentTo(recordType3));
        assertFalse(recordType1.isEquivalentTo(recordType4));
    }

    @Test
    public void testGetImplicitPrototype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> propertiesMap = Maps.newHashMap();
        propertiesMap.put("name", new RecordProperty("name", String.class));
        RecordType recordType = new RecordType(registry, propertiesMap);
        assertEquals("Object", recordType.getImplicitPrototype().getName());
    }

    @Test
    public void testDefineProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> propertiesMap = Maps.newHashMap();
        propertiesMap.put("name", new RecordProperty("name", String.class));
        RecordType recordType = new RecordType(registry, propertiesMap);

        JSType type = Integer.TYPE;
        boolean result = recordType.defineProperty("age", type, false, null);
        assertTrue(result);
    }

    @Test(expected = IllegalStateException.class)
    public void testDefinePropertyWithNullRecordProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> propertiesMap = Maps.newHashMap();
        propertiesMap.put(null, new RecordProperty("name", String.class));
        RecordType recordType = new RecordType(registry, propertiesMap);
    }

    @Test
    public void testGetLeastSupertype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> propertiesMap1 = Maps.newHashMap();
        propertiesMap1.put("name", new RecordProperty("name", String.class));
        RecordType recordType1 = new RecordType(registry, propertiesMap1);

        Map<String, RecordProperty> propertiesMap2 = Maps.newHashMap();
        propertiesMap2.put("name", new RecordProperty("name", String.class));
        propertiesMap2.put("age", new RecordProperty("age", Number.class));
        RecordType recordType2 = new RecordType(registry, propertiesMap2);

        RecordType result = recordType1.getLeastSupertype(recordType2);
        assertEquals("RecordType[name: String]", result.toString());
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> propertiesMap1 = Maps.newHashMap();
        propertiesMap1.put("name", new RecordProperty("name", String.class));
        RecordType recordType1 = new RecordType(registry, propertiesMap1);

        Map<String, RecordProperty> propertiesMap2 = Maps.newHashMap();
        propertiesMap2.put("name", new RecordProperty("name", String.class));
        propertiesMap2.put("age", new RecordProperty("age", Number.class));
        RecordType recordType2 = new RecordType(registry, propertiesMap2);

        Map<String, RecordProperty> propertiesMap3 = Maps.newHashMap();
        propertiesMap3.put("name", new RecordProperty("name", String.class));
        propertiesMap3.put("age", new RecordProperty("age", Number.class));
        propertiesMap3.put("gender", new RecordProperty("gender", String.class));
        RecordType recordType3 = new RecordType(registry, propertiesMap3);

        Map<String, RecordProperty> propertiesMap4 = Maps.newHashMap();
        propertiesMap4.put("name", new RecordProperty("name", String.class));
        propertiesMap4.put("age", new RecordProperty("age", Number.class));
        propertiesMap4.put("gender", new RecordProperty("gender", Number.class));
        RecordType recordType4 = new RecordType(registry, propertiesMap4);

        RecordType result = recordType1.getGreatestSubtypeHelper(recordType3);
        assertEquals("RecordType[name: String, age: Number, gender: String]", result.toString());
    }

    @Test
    public void testIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> propertiesMap1 = Maps.newHashMap();
        propertiesMap1.put("name", new RecordProperty("name", String.class));
        RecordType recordType1 = new RecordType(registry, propertiesMap1);

        Map<String, RecordProperty> propertiesMap2 = Maps.newHashMap();
        propertiesMap2.put("name", new RecordProperty("name", String.class));
        propertiesMap2.put("age", new RecordProperty("age", Number.class));
        RecordType recordType2 = new RecordType(registry, propertiesMap2);

        Map<String, RecordProperty> propertiesMap3 = Maps.newHashMap();
        propertiesMap3.put("age", new RecordProperty("age", Number.class));
        RecordType recordType3 = new RecordType(registry, propertiesMap3);

        assertTrue(recordType1.isSubtype(recordType1));
        assertTrue(recordType1.isSubtype(recordType2));
        assertFalse(recordType1.isSubtype(recordType3));
    }

    @Test
    public void testToMaybeRecordType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> propertiesMap = Maps.newHashMap();
        propertiesMap.put("name", new RecordProperty("name", String.class));
        RecordType recordType = new RecordType(registry, propertiesMap);
        assertEquals(recordType, recordType.toMaybeRecordType());
    }

    @Test
    public void testResolveInternal() {
        ErrorReporter errorReporter = new ErrorReporter() {};
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> propertiesMap = Maps.newHashMap();
        propertiesMap.put("name", new RecordProperty("name", String.class));
        RecordType recordType = new RecordType(registry, propertiesMap);

        JSType stringType = String.class;
        recordType.resolveInternal(errorReporter, null);

        for (Map.Entry<String, JSType> entry : recordType.properties.entrySet()) {
            JSType type = entry.getValue();
            assertEquals(type, type.resolve(errorReporter, null));
        }
    }
}