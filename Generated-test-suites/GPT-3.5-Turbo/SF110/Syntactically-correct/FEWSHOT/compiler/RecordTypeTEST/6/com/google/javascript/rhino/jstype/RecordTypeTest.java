package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordTypeBuilder.RecordProperty;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {
    private JSTypeRegistry registry;
    private Map<String, RecordProperty> properties;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        properties = new HashMap<>();
    }

    @Test
    public void testConstructor() {
        RecordType record = new RecordType(registry, properties);
        assertNotNull(record);
    }

    @Test
    public void testIsSynthetic() {
        RecordType record = new RecordType(registry, properties);
        assertFalse(record.isSynthetic());
    }

    @Test
    public void testCheckRecordEquivalenceHelper() {
        RecordType recordA = new RecordType(registry, properties);
        RecordType recordB = new RecordType(registry, properties);
        assertTrue(recordA.checkRecordEquivalenceHelper(recordB, null));
    }

    @Test
    public void testGetImplicitPrototype() {
        RecordType record = new RecordType(registry, properties);
        ObjectType prototype = record.getImplicitPrototype();
        assertNotNull(prototype);
    }

    @Test
    public void testDefineProperty() {
        RecordType record = new RecordType(registry, properties);
        assertTrue(record.defineProperty("prop", registry.getNativeType(JSTypeNative.NUMBER_TYPE), true, null));
        assertFalse(record.defineProperty("prop", registry.getNativeType(JSTypeNative.NUMBER_TYPE), true, null));
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        RecordType recordA = new RecordType(registry, properties);
        recordA.defineProperty("propA", registry.getNativeType(JSTypeNative.STRING_TYPE), true, null);
        RecordProperty propB = new RecordProperty(registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), null);
        properties.put("propB", propB);
        RecordType recordB = new RecordType(registry, properties);
        JSType subtype = recordA.getGreatestSubtypeHelper(recordB);
        assertNotNull(subtype);
    }

    @Test
    public void testToMaybeRecordType() {
        RecordType record = new RecordType(registry, properties);
        assertEquals(record, record.toMaybeRecordType());
    }

    @Test
    public void testIsSubtype() {
        RecordType recordA = new RecordType(registry, properties);
        RecordType recordB = new RecordType(registry, properties);
        assertTrue(recordA.isSubtype(recordB));
    }

    @Test
    public void testIsSubtype_ObjectType() {
        RecordType record = new RecordType(registry, properties);
        assertTrue(record.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
    }
}