package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {
    private JSTypeRegistry registry;
    private RecordType recordType;
    
    @Before
    public void setup() {
        registry = new JSTypeRegistry();
        recordType = new RecordType(registry, null);
    }
    
    @Test
    public void testIsSynthetic() {
        assertFalse(recordType.isSynthetic());
    }
    
    @Test
    public void testCheckRecordEquivalenceHelper() {
        RecordType otherRecord = new RecordType(registry, null);
        assertTrue(recordType.checkRecordEquivalenceHelper(otherRecord, null));
    }
    
    @Test
    public void testGetPropertyType() {
        assertNull(recordType.getPropertyType("property"));
    }
    
    @Test
    public void testDefineProperty() {
        assertFalse(recordType.defineProperty("property", null, false, null));
    }
     @Test
    public void testGetGreatestSubtypeHelper() {
        JSType that = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        assertEquals(registry.getNativeObjectType(JSTypeNative.NO_TYPE), recordType.getGreatestSubtypeHelper(that));
    }
    
    @Test
    public void testToMaybeRecordType() {
        assertEquals(recordType, recordType.toMaybeRecordType());
    }
    
    @Test
    public void testIsSubtype() {
        JSType that = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        assertFalse(recordType.isSubtype(that));
    }
    
    @Test
    public void testIsSubtype_TwoRecordTypes() {
        RecordType typeA = new RecordType(registry, null);
        RecordType typeB = new RecordType(registry, null);
        assertTrue(RecordType.isSubtype(typeA, typeB));
    }
}