package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class RecordTypeTest {

    @Test
    public void testRecordType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        
        Map<String, RecordProperty> properties = new HashMap<>();
        RecordType recordType = new RecordType(registry, properties);
        
        assertTrue(recordType.isSynthetic());
        assertFalse(recordType.isSubtype(registry.getNativeType(JSTypeNative.NULL_TYPE)));
    }

    @Test
    public void testRecordTypeDefinedProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        
        Map<String, RecordProperty> properties = new HashMap<>();
        RecordType recordType = new RecordType(registry, properties, true);
        
        assertFalse(recordType.isSynthetic());
        assertFalse(recordType.isSubtype(registry.getNativeType(JSTypeNative.NULL_TYPE)));
        recordType.defineProperty("property", registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), true, null);
        assertTrue(recordType.hasProperty("property"));
        assertEquals(registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), recordType.getPropertyType("property"));
    }
    
    @Test
    public void testCheckRecordEquivalenceHelper() {
        JSTypeRegistry registry = new JSTypeRegistry();
        
        Map<String, RecordProperty> properties1 = new HashMap<>();
        RecordType recordType1 = new RecordType(registry, properties1);
        
        Map<String, RecordProperty> properties2 = new HashMap<>();
        RecordType recordType2 = new RecordType(registry, properties2);
        
        assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, true));
    }
    
    @Test
    public void testGetGreatestSubtypeHelper() {
        JSTypeRegistry registry = new JSTypeRegistry();
        
        Map<String, RecordProperty> properties1 = new HashMap<>();
        RecordType recordType1 = new RecordType(registry, properties1);
        
        Map<String, RecordProperty> properties2 = new HashMap<>();
        RecordType recordType2 = new RecordType(registry, properties2);
        
        assertEquals(registry.getNativeObjectType(JSTypeNative.NO_TYPE), recordType1.getGreatestSubtypeHelper(recordType2));
    }
    
    @Test
    public void testIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        
        Map<String, RecordProperty> properties = new HashMap<>();
        RecordType recordType = new RecordType(registry, properties);
        
        assertFalse(recordType.isSubtype(registry.getNativeType(JSTypeNative.NULL_TYPE)));
    }
    
    @Test
    public void testSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        
        Map<String, RecordProperty> properties1 = new HashMap<>();
        RecordType recordType1 = new RecordType(registry, properties1);
        
        Map<String, RecordProperty> properties2 = new HashMap<>();
        RecordType recordType2 = new RecordType(registry, properties2);
        
        assertTrue(RecordType.isSubtype(recordType1.toMaybeObjectType(), recordType2));
    }
    
    @Test
    public void testDict() {
        JSTypeRegistry registry = new JSTypeRegistry();
        
        Map<String, RecordProperty> properties = new HashMap<>();
        RecordType recordType = new RecordType(registry, properties);
        
        ObjectType objectType = recordType;
        ObjectType dictType = registry.getNativeObjectType(JSTypeNative.DICT_TYPE);
        
        assertTrue(dictType.isSubtype(objectType));
    }
}