package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordTypeBuilder.RecordProperty;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

public class RecordTypeTest {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType;
    
    // Helper method to initialize the recordType object
    private void initializeRecordType(Map<String, RecordProperty> properties, boolean declared) {
        recordType = new RecordType(registry, properties, declared);
    }
    
    @Test
    public void testIsSynthetic() {
        initializeRecordType(Maps.newHashMap(), true);
        assertFalse(recordType.isSynthetic());
    }
    
    @Test
    public void testCheckRecordEquivalenceHelper() {
        initializeRecordType(Maps.newHashMap(), true);
        RecordType otherRecord = new RecordType(registry, Maps.newHashMap(), true);
        assertTrue(recordType.checkRecordEquivalenceHelper(otherRecord, true));
    }
    
    @Test
    public void testGetPropertyType() {
        initializeRecordType(Maps.newHashMap(), true);
        String property = "testProperty";
        JSType type = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        recordType.defineProperty(property, type, false, null);
        assertEquals(type, recordType.getPropertyType(property));
    }
    
    @Test
    public void testIsSubtype() {
        initializeRecordType(Maps.newHashMap(), true);
        JSType that = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        assertFalse(recordType.isSubtype(that));
    }
    
    @Test
    public void testResolveInternal() {
        initializeRecordType(Maps.newHashMap(), true);
        ErrorReporter errorReporter = new ErrorReporter(registry);
        StaticScope<JSType> scope = new StaticScope<>();
        assertEquals(recordType, recordType.resolveInternal(errorReporter, scope));
    }
    
    @Test
    public void testIsSubtypeForObject() {
        initializeRecordType(Maps.newHashMap(), true);
        JSType that = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        assertTrue(recordType.isSubtype(that));
    }
    
    @Test
    public void testIsSubtypeForRecordType() {
        initializeRecordType(Maps.newHashMap(), true);
        Map<String, RecordProperty> properties = Maps.newHashMap();
        JSType type = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        properties.put("testProperty", new RecordProperty(type, null));
        RecordType thatRecordType = new RecordType(registry, properties, true);
        assertFalse(recordType.isSubtype(thatRecordType));
    }
    
    @Test
    public void testGetGreatestSubtypeHelper() {
        initializeRecordType(Maps.newHashMap(), true);
        JSType that = registry.getNativeType(JSTypeNative.STRING_TYPE);
        assertEquals(registry.getNativeObjectType(JSTypeNative.NO_TYPE),
                     recordType.getGreatestSubtypeHelper(that));
    }
    
    @Test
    public void testDefineProperty() {
        initializeRecordType(Maps.newHashMap(), true);
        String property = "testProperty";
        JSType type = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        assertTrue(recordType.defineProperty(property, type, false, null));
    }
    
    @Test
    public void testToMaybeRecordType() {
        initializeRecordType(Maps.newHashMap(), true);
        assertEquals(recordType, recordType.toMaybeRecordType());
    }
    
    @Test
    public void testIsFrozen() {
        initializeRecordType(Maps.newHashMap(), true);
        assertTrue(recordType.isFrozen());
    }
    
    @Test
    public void testIsDeclaredProperty() {
        initializeRecordType(Maps.newHashMap(), true);
        String property = "testProperty";
        JSType type = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        recordType.defineProperty(property, type, false, null);
        assertTrue(recordType.isDeclaredProperty(property));
    }
    
    @Test
    public void testIsInferredProperty() {
        initializeRecordType(Maps.newHashMap(), true);
        String property = "testProperty";
        JSType type = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        recordType.defineProperty(property, type, true, null);
        assertTrue(recordType.isInferredProperty(property));
    }
}