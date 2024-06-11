package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class RecordTypeTest {

    @Test
    public void testIsEquivalentTo() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties1 = new HashMap<>();
        properties1.put("a", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
        properties1.put("b", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
        RecordType record1 = new RecordType(registry, properties1);

        Map<String, RecordProperty> properties2 = new HashMap<>();
        properties2.put("a", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
        properties2.put("b", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
        RecordType record2 = new RecordType(registry, properties2);

        assertTrue(record1.isEquivalentTo(record2));
    }

    @Test
    public void testGetImplicitPrototype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordType record = new RecordType(registry, new HashMap<>());
        ObjectType implicitPrototype = record.getImplicitPrototype();

        assertEquals(JSTypeNative.OBJECT_TYPE, implicitPrototype.getDisplayName());
    }

    @Test
    public void testIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties1 = new HashMap<>();
        properties1.put("a", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
        properties1.put("b", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
        RecordType record1 = new RecordType(registry, properties1);

        Map<String, RecordProperty> properties2 = new HashMap<>();
        properties2.put("a", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
        properties2.put("b", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
        RecordType record2 = new RecordType(registry, properties2);

        assertTrue(record1.isSubtype(record2));
    }

    @Test
    public void testDefineProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordType record = new RecordType(registry, new HashMap<>());

        assertTrue(record.defineProperty("a", registry.getNativeType(JSTypeNative.STRING_TYPE), false, null));
        assertTrue(record.defineProperty("b", registry.getNativeType(JSTypeNative.NUMBER_TYPE), false, null));
        assertFalse(record.defineProperty("a", registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), false, null));
    }

    @Test
    public void testToMaybeRecordType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordType record = new RecordType(registry, new HashMap<>());

        assertEquals(record, record.toMaybeRecordType());
    }

    @Test
    public void testResolveInternal() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("a", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
        properties.put("b", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
        RecordType record = new RecordType(registry, properties);

        assertEquals(record, record.resolveInternal(null, null));
    }
}