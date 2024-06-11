package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Map;
import java.util.HashMap;

public class RecordTypeTest {
    @Test
    public void testConstructor() {
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        RecordType recordType = new RecordType(new JSTypeRegistry(), properties);
    
        assertTrue(recordType.isSynthetic());
    }
    
    @Test
    public void testIsSynthetic() {
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        RecordType recordType = new RecordType(new JSTypeRegistry(), properties);
        
        assertTrue(recordType.isSynthetic());
    }
    
    @Test
    public void testCheckRecordEquivalenceHelper() {
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        RecordType recordType1 = new RecordType(new JSTypeRegistry(), properties);
        RecordType recordType2 = new RecordType(new JSTypeRegistry(), properties);
        
        assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, false));
        assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, true));
    }
    
    @Test
    public void testDefineProperty() {
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        RecordType recordType = new RecordType(new JSTypeRegistry(), properties);
        
        assertTrue(recordType.defineProperty("property", new JSType(new JSTypeRegistry()), true, null));
        assertFalse(recordType.defineProperty("property", new JSType(new JSTypeRegistry()), true, null));
    }
    
    @Test
    public void testGetGreatestSubtypeHelper() {
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        RecordType recordType = new RecordType(new JSTypeRegistry(), properties);
        
        assertNull(recordType.getGreatestSubtypeHelper(new JSType(new JSTypeRegistry())));
    }
    
    @Test
    public void testIsSubtype() {
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        RecordType recordType1 = new RecordType(new JSTypeRegistry(), properties);
        RecordType recordType2 = new RecordType(new JSTypeRegistry(), properties);
        
        assertTrue(recordType1.isSubtype(recordType2));
        assertTrue(recordType1.isSubtype(recordType1));
        assertFalse(recordType1.isSubtype(new JSType(new JSTypeRegistry())));
    }
    
    @Test
    public void testResolveInternal() {
        ErrorReporter errorReporter = new ErrorReporter(new JSTypeRegistry());
        StaticScope<JSType> scope = new StaticScope<>();
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        RecordType recordType = new RecordType(new JSTypeRegistry(), properties);
        
        assertEquals(recordType, recordType.resolveInternal(errorReporter, scope));
    }
}