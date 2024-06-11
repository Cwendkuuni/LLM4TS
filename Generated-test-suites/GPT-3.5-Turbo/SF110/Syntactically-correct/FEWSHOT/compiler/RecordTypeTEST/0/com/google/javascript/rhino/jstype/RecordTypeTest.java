package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RecordTypeTest {
    @Test
    public void testRecordTypeConstructor() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
        RecordType recordType = builder.build();
        assertTrue(recordType.isSynthetic());
    }

    @Test
    public void testRecordProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
        builder.addProperty("prop1", registry.getNativeType(JSTypeNative.STRING_TYPE), null);
        builder.addProperty("prop2", registry.getNativeType(JSTypeNative.NUMBER_TYPE), null);
        RecordType recordType = builder.build();
        assertTrue(recordType.hasProperty("prop1"));
        assertTrue(recordType.hasProperty("prop2"));
        assertFalse(recordType.hasProperty("prop3"));
        assertEquals(registry.getNativeType(JSTypeNative.STRING_TYPE), recordType.getPropertyType("prop1"));
        assertEquals(registry.getNativeType(JSTypeNative.NUMBER_TYPE), recordType.getPropertyType("prop2"));
        assertNull(recordType.getPropertyType("prop3"));
    }

    @Test
    public void testRecordEquivalenceHelper() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder1 = new RecordTypeBuilder(registry);
        builder1.addProperty("prop1", registry.getNativeType(JSTypeNative.STRING_TYPE), null);
        RecordType recordType1 = builder1.build();

        RecordTypeBuilder builder2 = new RecordTypeBuilder(registry);
        builder2.addProperty("prop1", registry.getNativeType(JSTypeNative.STRING_TYPE), null);
        RecordType recordType2 = builder2.build();

        assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.SAME_VALUE));
    }

    @Test
    public void testDefineProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
        builder.addProperty("prop1", registry.getNativeType(JSTypeNative.STRING_TYPE), null);
        RecordType recordType = builder.build();

        assertFalse(recordType.defineProperty("prop1", registry.getNativeType(JSTypeNative.NUMBER_TYPE), false, null));
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder1 = new RecordTypeBuilder(registry);
        builder1.addProperty("prop1", registry.getNativeType(JSTypeNative.STRING_TYPE), null);
        RecordType recordType1 = builder1.build();

        RecordTypeBuilder builder2 = new RecordTypeBuilder(registry);
        builder2.addProperty("prop2", registry.getNativeType(JSTypeNative.NUMBER_TYPE), null);
        RecordType recordType2 = builder2.build();

        assertEquals(registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE), recordType1.getGreatestSubtypeHelper(recordType2));
    }

    @Test
    public void testIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder1 = new RecordTypeBuilder(registry);
        builder1.addProperty("prop1", registry.getNativeType(JSTypeNative.STRING_TYPE), null);
        RecordType recordType1 = builder1.build();

        RecordTypeBuilder builder2 = new RecordTypeBuilder(registry);
        builder2.addProperty("prop1", registry.getNativeType(JSTypeNative.STRING_TYPE), null);
        RecordType recordType2 = builder2.build();

        assertTrue(RecordType.isSubtype(recordType1, recordType2));
    }
}