package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {

    private JSTypeRegistry registry;
    private RecordType recordType;
    
    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        properties.put("property1", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
        properties.put("property2", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
        recordType = new RecordType(registry, properties);
    }
    
    @Test
    public void testConstructor_withDeclared() {
        assertTrue(recordType.isDeclared());
        assertFalse(recordType.isSynthetic());
    }
    
    @Test
    public void testConstructor_withoutDeclared() {
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        properties.put("property1", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
        properties.put("property2", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
        RecordType synthRecordType = new RecordType(registry, properties, false);
        assertFalse(synthRecordType.isDeclared());
        assertTrue(synthRecordType.isSynthetic());
    }
    
    @Test
    public void testGetPropertyType() {
        assertEquals(registry.getNativeType(JSTypeNative.STRING_TYPE), recordType.getPropertyType("property1"));
        assertEquals(registry.getNativeType(JSTypeNative.NUMBER_TYPE), recordType.getPropertyType("property2"));
        assertNull(recordType.getPropertyType("nonexistantProperty"));
    }
    
    @Test
    public void testGetOwnPropertyNames() {
        assertEquals(2, recordType.getOwnPropertyNames().size());
        assertTrue(recordType.getOwnPropertyNames().contains("property1"));
        assertTrue(recordType.getOwnPropertyNames().contains("property2"));
        assertFalse(recordType.getOwnPropertyNames().contains("nonexistantProperty"));
    }
    
    @Test
    public void testIsSubtype() {
        ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        assertTrue(recordType.isSubtype(objectType));
        
        Map<String, RecordTypeBuilder.RecordProperty> subProperties = new HashMap<>();
        subProperties.put("property1", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
        RecordType subtype = new RecordType(registry, subProperties);
        assertTrue(subtype.isSubtype(recordType));
        
        Map<String, RecordTypeBuilder.RecordProperty> mismatchProperties = new HashMap<>();
        mismatchProperties.put("property1", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.OBJECT_TYPE), null));
        RecordType mismatch = new RecordType(registry, mismatchProperties);
        assertFalse(mismatch.isSubtype(recordType));
    }
    
    @Test
    public void testCheckRecordEquivalenceHelper() {
        Map<String, RecordTypeBuilder.RecordProperty> equivalentProperties = new HashMap<>();
        equivalentProperties.put("property1", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
        equivalentProperties.put("property2", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
        RecordType equivalentRecordType = new RecordType(registry, equivalentProperties);
        assertTrue(recordType.checkRecordEquivalenceHelper(equivalentRecordType, null));
        
        Map<String, RecordTypeBuilder.RecordProperty> differentProperties = new HashMap<>();
        differentProperties.put("property1", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.OBJECT_TYPE), null));
        differentProperties.put("property2", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), null));
        RecordType differentRecordType = new RecordType(registry, differentProperties);
        assertFalse(recordType.checkRecordEquivalenceHelper(differentRecordType, null));
    }

    @Test
    public void testIsFrozen() {
        assertTrue(recordType.isFrozen());
    }
    
    @Test
    public void testDefineProperty() {
        assertFalse(recordType.defineProperty("property3", registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), false, null));
        
        RecordType mutableRecordType = new RecordType(registry, new HashMap<>());
        assertTrue(mutableRecordType.defineProperty("property3", registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), false, null));
    }
    
    @Test
    public void testIsInvariant() {
        assertTrue(recordType.isInvariant(registry.getNativeType(JSTypeNative.BOOLEAN_TYPE)));
        assertFalse(recordType.isInvariant(registry.getNativeType(JSTypeNative.STRING_TYPE)));
    }
    
    @Test
    public void testGetGreatestSubtypeHelper() {
        assertEquals(registry.getNativeObjectType(JSTypeNative.NO_TYPE), recordType.getGreatestSubtypeHelper(registry.getNativeObjectType(JSTypeNative.NO_TYPE)));
        assertEquals(registry.getNativeObjectType(JSTypeNative.NO_OBJECT_TYPE), recordType.getGreatestSubtypeHelper(registry.getNativeObjectType(JSTypeNative.BOOLEAN_TYPE)));
    }
    
    @Test
    public void testToMaybeRecordType() {
        assertEquals(recordType, recordType.toMaybeRecordType());
    }
}