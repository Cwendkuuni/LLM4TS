package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class RecordTypeTest {
    @Test
    public void testDefineProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordType recordType = new RecordType(registry, new HashMap<String, RecordProperty>());

        assertTrue(recordType.defineProperty("a", registry.getNativeType(JSTypeNative.UNKNOWN_TYPE), false, null));
        assertFalse(recordType.defineProperty("a", registry.getNativeType(JSTypeNative.UNKNOWN_TYPE), false, null));
    }

    @Test
    public void testDefineDeclaredProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordType recordType = new RecordType(registry, new HashMap<String, RecordProperty>());

        assertTrue(recordType.defineDeclaredProperty("a", registry.getNativeType(JSTypeNative.UNKNOWN_TYPE), null));
        assertFalse(recordType.defineDeclaredProperty("a", registry.getNativeType(JSTypeNative.UNKNOWN_TYPE), null));
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordType recordType = new RecordType(registry, new HashMap<String, RecordProperty>());
        JSType type = recordType.getGreatestSubtypeHelper(registry.getNativeType(JSTypeNative.UNKNOWN_TYPE));

        assertEquals(registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE), type);
    }

    @Test
    public void testIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordType recordType1 = new RecordType(registry, new HashMap<String, RecordProperty>());
        RecordType recordType2 = new RecordType(registry, new HashMap<String, RecordProperty>());
        boolean result = recordType1.isSubtype(recordType2);

        assertTrue(result);
    }

    @Test
    public void testResolveInternal() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordType recordType = new RecordType(registry, new HashMap<String, RecordProperty>());
        JSType type = recordType.resolveInternal(null, null);

        assertEquals(registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE), type);
    }
}