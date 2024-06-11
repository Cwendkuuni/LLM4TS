
package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {
    private RecordType recordType;
    private JSTypeRegistry registry;
    private PrototypeObjectType prototype;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        prototype = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        recordType = new RecordType(registry, new HashMap<String, RecordTypeBuilder.RecordProperty>());
    }

    @Test
    public void testIsSynthetic() {
        assertFalse(recordType.isSynthetic());
    }

    @Test
    public void testCheckRecordEquivalenceHelper_sameRecordType_true() {
        RecordType otherRecord = new RecordType(registry, new HashMap<String, RecordTypeBuilder.RecordProperty>());
        assertTrue(recordType.checkRecordEquivalenceHelper(otherRecord, EquivalenceMethod.JSTYPE_EQUALS));
    }

    @Test
    public void testCheckRecordEquivalenceHelper_differentRecordType_false() {
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<String, RecordTypeBuilder.RecordProperty>();
        properties.put("foo", new RecordTypeBuilder.RecordProperty(new JSTypePrimitive(registry, JSTypeNative.NO_TYPE), null));
        RecordType otherRecord = new RecordType(registry, properties);
        assertFalse(recordType.checkRecordEquivalenceHelper(otherRecord, EquivalenceMethod.JSTYPE_EQUALS));
    }

    @Test
    public void testDefineProperty_frozen_true() {
        recordType.defineProperty("foo", new JSTypePrimitive(registry, JSTypeNative.NO_TYPE), true, null);
        recordType.freeze();

        assertFalse(recordType.defineProperty("bar", new JSTypePrimitive(registry, JSTypeNative.NO_TYPE), true, null));
    }

    @Test
    public void testGetGreatestSubtypeHelper_recordType_true() {
        RecordType that = new RecordType(registry, new HashMap<String, RecordTypeBuilder.RecordProperty>());
        assertTrue(recordType.getGreatestSubtypeHelper(that).isNativeType(JSTypeNative.NO_OBJECT_TYPE));
    }

    @Test
    public void testGetGreatestSubtypeHelper_nonRecordType_true() {
        JSType that = registry.getNativeType(JSTypeNative.OBJECT_TYPE);
        assertTrue(recordType.getGreatestSubtypeHelper(that).isNativeType(JSTypeNative.NO_OBJECT_TYPE));
    }

    @Test
    public void testGetImplicitPrototype() {
        assertEquals(prototype, recordType.getImplicitPrototype());
    }

    @Test
    public void testIsSubtype_subtype_true() {
        RecordType that = new RecordType(registry, new HashMap<String, RecordTypeBuilder.RecordProperty>());
        assertTrue(recordType.isSubtype(that));
    }

    @Test
    public void testIsSubtype_superType_false() {
        ObjectType superType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        assertFalse(recordType.isSubtype(superType));
    }

    @Test
    public void testIsSubtype_nonRecordType_false() {
        JSType nonRecordType = registry.getNativeType(JSTypeNative.BIGINT_TYPE);
        assertFalse(recordType.isSubtype(nonRecordType));
    }

    @Test
    public void testIsSubtype_sameRecordType_true() {
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<String, RecordTypeBuilder.RecordProperty>();
        properties.put("foo", new RecordTypeBuilder.RecordProperty(new JSTypePrimitive(registry, JSTypeNative.NO_TYPE), null));
        RecordType sameRecordType = new RecordType(registry, properties);
        assertTrue(recordType.isSubtype(sameRecordType));
    }

    @Test
    public void testIsSubtype_differentPropertyType_false() {
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<String, RecordTypeBuilder.RecordProperty>();
        properties.put("foo", new RecordTypeBuilder.RecordProperty(new JSTypePrimitive(registry, JSTypeNative.BOOLEAN_TYPE), null));
        RecordType differentPropertyType = new RecordType(registry, properties);
        assertFalse(recordType.isSubtype(differentPropertyType));
    }

    @Test
    public void testIsSubtype_missingProperty_false() {
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<String, RecordTypeBuilder.RecordProperty>();
        properties.put("foo", new RecordTypeBuilder.RecordProperty(new JSTypePrimitive(registry, JSTypeNative.NO_TYPE), null));
        properties.put("bar", new RecordTypeBuilder.RecordProperty(new JSTypePrimitive(registry, JSTypeNative.NO_TYPE), null));
        RecordType missingProperty = new RecordType(registry, properties);
        assertFalse(recordType.isSubtype(missingProperty));
    }

    @Test
    public void testIsSubtype_covariantProperty_false() {
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<String, RecordTypeBuilder.RecordProperty>();
        properties.put("foo", new RecordTypeBuilder.RecordProperty(new JSTypePrimitive(registry, JSTypeNative.NO_TYPE), null));
        RecordType covariantProperty = new RecordType(registry, properties);
        assertFalse(recordType.isSubtype(covariantProperty));
    }

    @Test
    public void testToMaybeRecordType() {
        assertEquals(recordType, recordType.toMaybeRecordType());
    }

    @Test
    public void testIsSynthetic_true() {
        assertTrue(recordType.isSynthetic());
    }

    @Test
    public void testCheckRecordEquivalenceHelper_sameProperties_true() {
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<String, RecordTypeBuilder.RecordProperty>();
        properties.put("foo", new RecordTypeBuilder.RecordProperty(new JSTypePrimitive(registry, JSTypeNative.NO_TYPE), null));
        RecordType otherRecord = new RecordType(registry, properties);
        assertTrue(recordType.checkRecordEquivalenceHelper(otherRecord, EquivalenceMethod.JSTYPE_EQUALS));
    }

    @Test
    public void testCheckRecordEquivalenceHelper_differentProperties_false() {
        RecordType otherRecord = new RecordType(registry, new HashMap<String, RecordTypeBuilder.RecordProperty>());
        assertFalse(recordType.checkRecordEquivalenceHelper(otherRecord, EquivalenceMethod.JSTYPE_EQUALS));
    }

    @Test
    public void testDefineProperty_notFrozen_true() {
        assertTrue(recordType.defineProperty("foo", new JSTypePrimitive(registry, JSTypeNative.NO_TYPE), true, null));
    }

    @Test
    public void testGetGreatestSubtypeHelper_noGreatestSupertype_sameProperties() {
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<String, RecordTypeBuilder.RecordProperty>();
        properties.put("foo", new RecordTypeBuilder.RecordProperty(new JSTypePrimitive(registry, JSTypeNative.NO_OBJECT_TYPE), null));
        RecordType sameProperties = new RecordType(registry, properties);
        assertTrue(recordType.getGreatestSubtypeHelper(sameProperties).isNativeType(JSTypeNative.NO_OBJECT_TYPE));
    }

    @Test
    public void testGetGreatestSubtypeHelper_noGreatestSupertype_noCommonProperties() {
        ObjectType type = registry.getNativeObjectType(JSTypeNative.NULL_TYPE);
        assertTrue(recordType.getGreatestSubtypeHelper(type).isNativeType(JSTypeNative.NO_OBJECT_TYPE));
    }

    @Test
    public void testGetImplicitPrototype_samePrototype_true() {
        assertEquals(prototype, recordType.getImplicitPrototype());
    }

    @Test
    public void testIsSubtype_sameRecordType_true() {
        assertTrue(recordType.isSubtype(recordType));
    }

    @Test
    public void testIsSubtype_superType_true() {
        assertTrue(prototype.isSubtype(recordType));
    }

    @Test
    public void testIsSubtype_nonRecordType_false() {
        assertFalse(recordType.isSubtype(registry.getNativeType(JSTypeNative.NULL_TYPE)));
    }

    @Test
    public void testIsSubtype_differentPropertyType_false() {
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<String, RecordTypeBuilder.RecordProperty>();
        properties.put("foo", new RecordTypeBuilder.RecordProperty(new JSTypePrimitive(registry, JSTypeNative.INT_TYPE), null));
        RecordType differentPropertyType = new RecordType(registry, properties);
        assertFalse(recordType.isSubtype(differentPropertyType));
    }

    @Test
    public void testIsSubtype_missingProperty_false() {
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<String, RecordTypeBuilder.RecordProperty>();
        properties.put("foo", new RecordTypeBuilder.RecordProperty(new JSTypePrimitive(registry, JSTypeNative.NO_TYPE), null));
        properties.put("bar", new RecordTypeBuilder.RecordProperty(new JSTypePrimitive(registry, JSTypeNative.NO_TYPE), null));
        RecordType missingProperty = new RecordType(registry, properties);
        assertFalse(recordType.isSubtype(missingProperty));
    }

    @Test
    public void testIsSubtype_covariantProperty_false() {
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<String, RecordTypeBuilder.RecordProperty>();
        properties.put("foo", new RecordTypeBuilder.RecordProperty(new JSTypePrimitive(registry, JSTypeNative.NO_TYPE), null));
        RecordType covariantProperty = new RecordType(registry, properties);
        assertFalse(recordType.isSubtype(covariantProperty));
    }

    @Test
    public void testToMaybeRecordType_sameRecordType_true() {
        assertEquals(recordType, recordType.toMaybeRecordType());
    }
}
