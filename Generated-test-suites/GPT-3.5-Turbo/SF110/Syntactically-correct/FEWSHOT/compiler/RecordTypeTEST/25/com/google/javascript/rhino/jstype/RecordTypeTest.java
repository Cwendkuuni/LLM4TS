package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordTypeBuilder.RecordProperty;

import java.util.HashMap;
import java.util.Map;

public class RecordTypeTest {
    @Test
    public void testRecordType() {
        JSTypeRegistry registry = new JSTypeRegistry(null);
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("property1", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), new Node()));
        properties.put("property2", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), new Node()));
        RecordType recordType = new RecordType(registry, properties);

        assertTrue(recordType.isSynthetic());
        assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), recordType.getImplicitPrototype());
        assertTrue(recordType.checkRecordEquivalenceHelper(recordType, EquivalenceMethod.EQUALS));
        assertTrue(recordType.checkRecordEquivalenceHelper(recordType, EquivalenceMethod.IDENTITY_EQUALS));
        assertTrue(recordType.isSubtype(recordType));
    }

    @Test
    public void testIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry(null);
        Map<String, RecordProperty> properties1 = new HashMap<>();
        properties1.put("property1", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), new Node()));
        properties1.put("property2", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), new Node()));
        RecordType recordType1 = new RecordType(registry, properties1);

        Map<String, RecordProperty> properties2 = new HashMap<>();
        properties2.put("property1", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), new Node()));
        properties2.put("property2", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), new Node()));
        properties2.put("property3", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), new Node()));
        RecordType recordType2 = new RecordType(registry, properties2);

        assertTrue(RecordType.isSubtype(recordType1, recordType1));
        assertTrue(RecordType.isSubtype(recordType2, recordType1));
        assertFalse(RecordType.isSubtype(recordType1, recordType2));
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        JSTypeRegistry registry = new JSTypeRegistry(null);
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("property1", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), new Node()));
        RecordType recordType = new RecordType(registry, properties);
        ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

        assertEquals(objectType.getGreatestSubtype(objectType), recordType.getGreatestSubtypeHelper(objectType));
        assertEquals(objectType.getGreatestSubtype(objectType), recordType.getGreatestSubtypeHelper(recordType));
    }

    @Test
    public void testDefineProperty() {
        JSTypeRegistry registry = new JSTypeRegistry(null);
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("property1", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), new Node()));
        RecordType recordType = new RecordType(registry, properties);

        assertFalse(recordType.defineProperty("property1", registry.getNativeType(JSTypeNative.NUMBER_TYPE), false, new Node()));
    }
}