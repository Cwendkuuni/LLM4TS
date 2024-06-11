package com.google.javascript.rhino.jstype;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class RecordTypeTest {

    private JSTypeRegistry registry;
    private RecordType recordType;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("a", new RecordProperty("a", JSTypeNative.OBJECT_TYPE, null));
        properties.put("b", new RecordProperty("b", JSTypeNative.NUMBER_TYPE, null));
        
        recordType = new RecordType(registry, properties);
    }

    @Test
    public void testIsEquivalentTo() {
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("a", new RecordProperty("a", JSTypeNative.OBJECT_TYPE, null));
        properties.put("b", new RecordProperty("b", JSTypeNative.NUMBER_TYPE, null));
        
        RecordType otherRecordType = new RecordType(registry, properties);
        
        assertTrue(recordType.isEquivalentTo(otherRecordType));
    }

    @Test
    public void testGetImplicitPrototype() {
        ObjectType prototype = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        assertEquals(prototype, recordType.getImplicitPrototype());
    }

    @Test
    public void testDefineProperty() {
        assertFalse(recordType.defineProperty("c", JSTypeNative.STRING_TYPE, false, null));
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        JSType greatestSubtype = objectType.getGreatestSubtype(recordType);
        assertTrue(greatestSubtype.isEquivalentTo(recordType));
    }

    @Test
    public void testToMaybeRecordType() {
        assertEquals(recordType, recordType.toMaybeRecordType());
    }

    @Test
    public void testIsSubtype() {
        ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        assertTrue(recordType.isSubtype(objectType));
    }

    @Test
    public void testResolveInternal() {
        ErrorReporter errorReporter = new ErrorReporter();
        StaticScope<JSType> scope = new StaticScope<>();
        JSType resolvedType = recordType.resolveInternal(errorReporter, scope);
        assertEquals(recordType, resolvedType);
    }

}