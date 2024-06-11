package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {
    
    private JSTypeRegistry registry;
    private Map<String, RecordTypeBuilder.RecordProperty> properties;
    
    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        properties = new HashMap<>();
        properties.put("prop1", new RecordTypeBuilder.RecordProperty(
                registry.getNativeType(JSTypeNative.STRING_TYPE), null));
        properties.put("prop2", new RecordTypeBuilder.RecordProperty(
                registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
    }
    
    @Test
    public void testConstructor() {
        // create a record type with properties
        RecordType recordType = new RecordType(registry, properties);
        
        // check if properties are defined correctly
        assertEquals(registry.getNativeType(JSTypeNative.STRING_TYPE),
                recordType.getPropertyType("prop1"));
        assertEquals(registry.getNativeType(JSTypeNative.NUMBER_TYPE),
                recordType.getPropertyType("prop2"));
        
        // check if all properties are defined
        assertEquals(2, recordType.getOwnPropertyNames().size());
    }
    
    @Test
    public void testConstructorDeclared() {
        // create a record type with properties and set declared to false
        RecordType recordType = new RecordType(registry, properties, false);
        
        // check if properties are defined correctly
        assertEquals(registry.getNativeType(JSTypeNative.STRING_TYPE),
                recordType.getPropertyType("prop1"));
        assertEquals(registry.getNativeType(JSTypeNative.NUMBER_TYPE),
                recordType.getPropertyType("prop2"));
        
        // check if all properties are defined
        assertEquals(2, recordType.getOwnPropertyNames().size());
        
        // check if the record type is synthetic
        assertEquals(true, recordType.isSynthetic());
    }
    
    @Test
    public void testCheckRecordEquivalenceHelper() {
        // create two record types with the same properties
        RecordType recordType1 = new RecordType(registry, properties);
        RecordType recordType2 = new RecordType(registry, properties);
        
        // check if the two record types are equivalent
        assertEquals(true, recordType1.checkRecordEquivalenceHelper(recordType2, null));
    }
    
    @Test
    public void testGetImplicitPrototype() {
        // create a record type
        RecordType recordType = new RecordType(registry, properties);
        
        // check if the implicit prototype is an object type
        assertEquals(JSTypeNative.OBJECT_TYPE,
                recordType.getImplicitPrototype().toString());
    }
    
    @Test
    public void testDefineProperty() {
        // create a record type
        RecordType recordType = new RecordType(registry, properties);
        
        // define a new property
        boolean result = recordType.defineProperty("prop3",
                registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), true, null);
        
        // check if the new property is defined
        assertEquals(true, result);
        assertEquals(registry.getNativeType(JSTypeNative.BOOLEAN_TYPE),
                recordType.getPropertyType("prop3"));
        assertEquals(3, recordType.getOwnPropertyNames().size());
    }
    
    @Test
    public void testGetGreatestSubtypeHelper() {
        // create a record type
        RecordType recordType = new RecordType(registry, properties);
        
        // create another record type with additional property
        Map<String, RecordTypeBuilder.RecordProperty> newProperties = new HashMap<>(properties);
        newProperties.put("prop3", new RecordTypeBuilder.RecordProperty(
                registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), null));
        RecordType otherRecordType = new RecordType(registry, newProperties);
        
        // get the greatest subtype
        JSType greatestSubtype = recordType.getGreatestSubtypeHelper(otherRecordType);
        
        // check if the greatest subtype is the expected type
        assertEquals(JSTypeNative.NO_OBJECT_TYPE, greatestSubtype.toString());
    }
    
    @Test
    public void testToMaybeRecordType() {
        // create a record type
        RecordType recordType = new RecordType(registry, properties);
        
        // convert to maybe record type and check if it is the same object
        assertEquals(recordType, recordType.toMaybeRecordType());
    }
    
    @Test
    public void testIsSubtype() {
        // create a record type
        RecordType recordType = new RecordType(registry, properties);
        
        // check if the record type is a subtype of itself
        assertEquals(true, recordType.isSubtype(recordType));
        
        // create another record type with the same properties
        RecordType otherRecordType = new RecordType(registry, properties);
        
        // check if the record type is a subtype of the other record type
        assertEquals(true, recordType.isSubtype(otherRecordType));
        
        // create a record type with additional property
        Map<String, RecordTypeBuilder.RecordProperty> newProperties = new HashMap<>(properties);
        newProperties.put("prop3", new RecordTypeBuilder.RecordProperty(
                registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), null));
        RecordType additionalRecordType = new RecordType(registry, newProperties);
        
        // check if the record type is not a subtype of the additional record type
        assertEquals(false, recordType.isSubtype(additionalRecordType));
    }
    
    @Test
    public void testIsSubtypeWithObjectType() {
        // create a record type
        RecordType recordType = new RecordType(registry, properties);
        
        // create an object type
        JSType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        
        // check if the record type is a subtype of the object type
        assertEquals(true, recordType.isSubtype(objectType));
    }
    
    @Test
    public void testIsSubtypeWithNonRecordType() {
        // create a record type
        RecordType recordType = new RecordType(registry, properties);
        
        // create a non-record type
        JSType nonRecordType = registry.getNativeType(JSTypeNative.UNDEFINED_TYPE);
        
        // check if the record type is not a subtype of the non-record type
        assertEquals(false, recordType.isSubtype(nonRecordType));
    }
    
    @Test
    public void testIsSubtypeHelper() {
        // create a record type
        RecordType recordType = new RecordType(registry, properties);
        
        // check if the record type is a subtype of itself using the helper method
        assertEquals(true, RecordType.isSubtype(recordType, recordType));
        
        // create another record type with the same properties
        RecordType otherRecordType = new RecordType(registry, properties);
        
        // check if the record type is a subtype of the other record type using the helper method
        assertEquals(true, RecordType.isSubtype(recordType, otherRecordType));
        
        // create a record type with additional property
        Map<String, RecordTypeBuilder.RecordProperty> newProperties = new HashMap<>(properties);
        newProperties.put("prop3", new RecordTypeBuilder.RecordProperty(
                registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), null));
        RecordType additionalRecordType = new RecordType(registry, newProperties);
        
        // check if the record type is not a subtype of the additional record type using the helper method
        assertEquals(false, RecordType.isSubtype(recordType, additionalRecordType));
    }
}