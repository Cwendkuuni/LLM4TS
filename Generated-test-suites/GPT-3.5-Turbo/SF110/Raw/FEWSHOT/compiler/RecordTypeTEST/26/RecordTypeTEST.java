package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class RecordTypeTest {
    private JSTypeRegistry registry;
    private Map<String, RecordProperty> properties;
    private RecordType recordType;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        properties = new HashMap<>();
        recordType = new RecordType(registry, properties);
    }

    @Test
    public void testIsSynthetic() {
        assertFalse(recordType.isSynthetic());
    }

    @Test
    public void testCheckRecordEquivalenceHelper() {
        RecordType otherRecord = new RecordType(registry, properties);

        assertTrue(recordType.checkRecordEquivalenceHelper(otherRecord, EquivalenceMethod.EQUALITY));
    }

    @Test
    public void testDefineProperty() {
        assertFalse(recordType.defineProperty("propertyName", registry.getNativeType(JSTypeNative.STRING_TYPE), true, null));
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        JSType that = registry.getNativeType(JSTypeNative.OBJECT_TYPE);
        JSType greatestSubtype = registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE);

        assertEquals(greatestSubtype, recordType.getGreatestSubtypeHelper(that));
    }

    @Test
    public void testToMaybeRecordType() {
        assertEquals(recordType, recordType.toMaybeRecordType());
    }

    @Test
    public void testIsSubtype() {
        assertFalse(recordType.isSubtype(registry.getNativeType(JSTypeNative.STRING_TYPE)));
        assertTrue(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
        assertTrue(recordType.isSubtype(recordType));
    }

    @Test
    public void testIsSubtype_static() {
        ObjectType typeA = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        RecordType typeB = new RecordType(registry, properties);
        properties.put("property", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));

        assertFalse(RecordType.isSubtype(typeA, typeB));
    }
}