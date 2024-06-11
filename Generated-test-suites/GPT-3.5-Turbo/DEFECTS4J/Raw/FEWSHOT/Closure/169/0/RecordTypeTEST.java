package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RecordTypeTest {
    private JSTypeRegistry registry = new JSTypeRegistry();

    @Test
    public void testIsSynthetic() {
        RecordType recordType = new RecordType(registry, new RecordProperty(), true);
        assertFalse(recordType.isSynthetic());

        recordType = new RecordType(registry, new RecordProperty(), false);
        assertTrue(recordType.isSynthetic());
    }

    @Test
    public void testCheckRecordEquivalenceHelper() {
        RecordType recordType1 = new RecordType(registry, new RecordProperty(), true);
        RecordType recordType2 = new RecordType(registry, new RecordProperty(), true);
        assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, true));

        RecordType recordType3 = new RecordType(registry, new RecordProperty(), true);
        recordType3.defineProperty("prop1", registry.getNativeType(JSTypeNative.NUMBER_TYPE), false, null);
        assertFalse(recordType1.checkRecordEquivalenceHelper(recordType3, true));
    }
    
    @Test
    public void testDefineProperty() {
        RecordType recordType = new RecordType(registry, new RecordProperty(), true);
        recordType.defineProperty("prop1", registry.getNativeType(JSTypeNative.STRING_TYPE), false, null);
        assertEquals(registry.getNativeType(JSTypeNative.STRING_TYPE), recordType.getPropertyType("prop1"));
        assertNull(recordType.getPropertyType("prop2"));

        recordType.defineProperty("prop1", registry.getNativeType(JSTypeNative.NUMBER_TYPE), true, null);
        assertEquals(registry.getNativeType(JSTypeNative.NUMBER_TYPE), recordType.getPropertyType("prop1"));
        assertNull(recordType.getPropertyType("prop2"));
    }

    @Test
    public void testIsSubtype() {
        RecordType recordType1 = new RecordType(registry, new RecordProperty(), true);
        RecordType recordType2 = new RecordType(registry, new RecordProperty(), true);
        assertTrue(recordType1.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
        assertTrue(recordType1.isSubtype(recordType2));

        recordType1.defineProperty("prop1", registry.getNativeType(JSTypeNative.STRING_TYPE), false, null);
        assertTrue(recordType1.isSubtype(recordType2));
        assertFalse(recordType2.isSubtype(recordType1));
    }
}