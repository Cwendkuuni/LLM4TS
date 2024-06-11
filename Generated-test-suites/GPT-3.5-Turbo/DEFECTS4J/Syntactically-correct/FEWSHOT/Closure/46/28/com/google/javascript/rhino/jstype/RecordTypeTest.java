package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class RecordTypeTest {

    private JSTypeRegistry registry;
    private RecordType recordType;
    private Map<String, RecordTypeBuilder.RecordProperty> properties;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        properties = new HashMap<>();
        properties.put("a", new RecordTypeBuilder.RecordProperty(null, registry.getNativeType(JSTypeNative.OBJECT_TYPE)));
        properties.put("b", new RecordTypeBuilder.RecordProperty(null, registry.getNativeType(JSTypeNative.NUMBER_TYPE)));
        recordType = new RecordType(registry, properties);
    }

    @Test
    public void testIsEquivalentTo() {
        assertTrue(recordType.isEquivalentTo(recordType));
        assertFalse(recordType.isEquivalentTo(null));

        Map<String, RecordTypeBuilder.RecordProperty> otherProperties = new HashMap<>();
        otherProperties.put("a", new RecordTypeBuilder.RecordProperty(null, registry.getNativeType(JSTypeNative.OBJECT_TYPE)));
        otherProperties.put("b", new RecordTypeBuilder.RecordProperty(null, registry.getNativeType(JSTypeNative.NUMBER_TYPE)));
        RecordType otherRecordType = new RecordType(registry, otherProperties);
        assertTrue(recordType.isEquivalentTo(otherRecordType));

        otherProperties.put("c", new RecordTypeBuilder.RecordProperty(null, registry.getNativeType(JSTypeNative.STRING_TYPE)));
        RecordType differentRecordType = new RecordType(registry, otherProperties);
        assertFalse(recordType.isEquivalentTo(differentRecordType));
    }

    @Test
    public void testGetImplicitPrototype() {
        ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        assertEquals(objectType, recordType.getImplicitPrototype());
    }

    @Test
    public void testDefineProperty() {
        assertTrue(recordType.defineProperty("c", registry.getNativeType(JSTypeNative.STRING_TYPE), false, null));
        assertFalse(recordType.defineProperty("a", registry.getNativeType(JSTypeNative.STRING_TYPE), false, null));
        assertFalse(recordType.defineProperty("c", registry.getNativeType(JSTypeNative.STRING_TYPE), true, null));
    }

    @Test
    public void testGetLeastSupertype() {
        JSType otherType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        assertEquals(registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE), recordType.getLeastSupertype(otherType));

        otherType = recordType;
        assertEquals(recordType, recordType.getLeastSupertype(otherType));

        Map<String, RecordTypeBuilder.RecordProperty> otherProperties = new HashMap<>();
        otherProperties.put("a", new RecordTypeBuilder.RecordProperty(null, registry.getNativeType(JSTypeNative.OBJECT_TYPE)));
        otherProperties.put("b", new RecordTypeBuilder.RecordProperty(null, registry.getNativeType(JSTypeNative.NUMBER_TYPE)));
        otherProperties.put("c", new RecordTypeBuilder.RecordProperty(null, registry.getNativeType(JSTypeNative.STRING_TYPE)));
        RecordType otherRecordType = new RecordType(registry, otherProperties);
        assertEquals(otherRecordType, recordType.getLeastSupertype(otherRecordType));
    }

    @Test
    public void testIsSubtype() {
        ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        assertTrue(recordType.isSubtype(objectType));

        Map<String, RecordTypeBuilder.RecordProperty> otherProperties = new HashMap<>();
        otherProperties.put("a", new RecordTypeBuilder.RecordProperty(null, registry.getNativeType(JSTypeNative.OBJECT_TYPE)));
        otherProperties.put("b", new RecordTypeBuilder.RecordProperty(null, registry.getNativeType(JSTypeNative.NUMBER_TYPE)));
        RecordType otherRecordType = new RecordType(registry, otherProperties);
        assertTrue(recordType.isSubtype(otherRecordType));

        otherProperties.put("c", new RecordTypeBuilder.RecordProperty(null, registry.getNativeType(JSTypeNative.STRING_TYPE)));
        RecordType differentRecordType = new RecordType(registry, otherProperties);
        assertFalse(recordType.isSubtype(differentRecordType));
    }

    @Test
    public void testIsSubtypeHelper() {
        ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        assertTrue(RecordType.isSubtype(objectType, recordType));

        Map<String, RecordTypeBuilder.RecordProperty> otherProperties = new HashMap<>();
        otherProperties.put("a", new RecordTypeBuilder.RecordProperty(null, registry.getNativeType(JSTypeNative.OBJECT_TYPE)));
        otherProperties.put("b", new RecordTypeBuilder.RecordProperty(null, registry.getNativeType(JSTypeNative.NUMBER_TYPE)));
        RecordType otherRecordType = new RecordType(registry, otherProperties);
        assertTrue(RecordType.isSubtype(recordType, otherRecordType));

        otherProperties.put("c", new RecordTypeBuilder.RecordProperty(null, registry.getNativeType(JSTypeNative.STRING_TYPE)));
        RecordType differentRecordType = new RecordType(registry, otherProperties);
        assertFalse(RecordType.isSubtype(recordType, differentRecordType));
    }

    @Test
    public void testResolveInternal() {
        JSType stringType = registry.getNativeType(JSTypeNative.STRING_TYPE);
        recordType.defineProperty("c", stringType, true, null);
        assertEquals(stringType, recordType.getPropertyType("c"));

        JSType objectType = registry.getNativeType(JSTypeNative.OBJECT_TYPE);
        StaticScope<JSType> scope = new StaticScope<>(null);
        scope.declare("c", objectType);
        recordType.resolveInternal(null, scope);
        assertEquals(objectType, recordType.getPropertyType("c"));
    }
}