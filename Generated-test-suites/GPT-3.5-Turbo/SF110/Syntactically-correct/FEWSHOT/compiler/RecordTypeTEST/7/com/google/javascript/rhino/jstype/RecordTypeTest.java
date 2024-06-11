package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class RecordTypeTest {
    
    @Test
    public void testIsSynthetic() {
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        properties.put("prop1", new RecordTypeBuilder.RecordProperty(null));
        properties.put("prop2", new RecordTypeBuilder.RecordProperty(null));
        RecordType recordType = new RecordType(null, properties);
        
        assertFalse(recordType.isSynthetic());
    }
    
    @Test
    public void testCheckRecordEquivalenceHelper_SameKeys() {
        Map<String, RecordTypeBuilder.RecordProperty> properties1 = new HashMap<>();
        properties1.put("prop1", new RecordTypeBuilder.RecordProperty(null));
        properties1.put("prop2", new RecordTypeBuilder.RecordProperty(null));
        RecordType recordType1 = new RecordType(null, properties1);
        
        Map<String, RecordTypeBuilder.RecordProperty> properties2 = new HashMap<>();
        properties2.put("prop1", new RecordTypeBuilder.RecordProperty(null));
        properties2.put("prop2", new RecordTypeBuilder.RecordProperty(null));
        RecordType recordType2 = new RecordType(null, properties2);
        
        assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, null));
    }
    
    @Test
    public void testCheckRecordEquivalenceHelper_DifferentKeys() {
        Map<String, RecordTypeBuilder.RecordProperty> properties1 = new HashMap<>();
        properties1.put("prop1", new RecordTypeBuilder.RecordProperty(null));
        properties1.put("prop2", new RecordTypeBuilder.RecordProperty(null));
        RecordType recordType1 = new RecordType(null, properties1);
        
        Map<String, RecordTypeBuilder.RecordProperty> properties2 = new HashMap<>();
        properties2.put("prop1", new RecordTypeBuilder.RecordProperty(null));
        properties2.put("prop3", new RecordTypeBuilder.RecordProperty(null));
        RecordType recordType2 = new RecordType(null, properties2);
        
        assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, null));
    }
    
    @Test
    public void testGetPropertyType() {
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        properties.put("prop1", new RecordTypeBuilder.RecordProperty(null));
        RecordType recordType = new RecordType(null, properties);
        
        assertNotNull(recordType.getPropertyType("prop1"));
    }
    
    @Test
    public void testGetPropertyType_PropertyNotFound() {
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        properties.put("prop1", new RecordTypeBuilder.RecordProperty(null));
        RecordType recordType = new RecordType(null, properties);
        
        assertNull(recordType.getPropertyType("prop2"));
    }
    
    @Test
    public void testIsSubtype_Subtype() {
        Map<String, RecordTypeBuilder.RecordProperty> properties1 = new HashMap<>();
        properties1.put("prop1", new RecordTypeBuilder.RecordProperty(null));
        properties1.put("prop2", new RecordTypeBuilder.RecordProperty(null));
        RecordType recordType1 = new RecordType(null, properties1);
        
        Map<String, RecordTypeBuilder.RecordProperty> properties2 = new HashMap<>();
        properties2.put("prop1", new RecordTypeBuilder.RecordProperty(null));
        RecordType recordType2 = new RecordType(null, properties2);
        
        assertTrue(recordType1.isSubtype(recordType2));
    }
    
    @Test
    public void testIsSubtype_NotSubtype() {
        Map<String, RecordTypeBuilder.RecordProperty> properties1 = new HashMap<>();
        properties1.put("prop1", new RecordTypeBuilder.RecordProperty(null));
        RecordType recordType1 = new RecordType(null, properties1);
        
        Map<String, RecordTypeBuilder.RecordProperty> properties2 = new HashMap<>();
        properties2.put("prop1", new RecordTypeBuilder.RecordProperty(null));
        properties2.put("prop2", new RecordTypeBuilder.RecordProperty(null));
        RecordType recordType2 = new RecordType(null, properties2);
        
        assertFalse(recordType1.isSubtype(recordType2));
    }
}