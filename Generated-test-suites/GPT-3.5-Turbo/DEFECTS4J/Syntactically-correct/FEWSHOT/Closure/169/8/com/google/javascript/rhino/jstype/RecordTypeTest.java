package com.google.javascript.rhino.jstype;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordTypeBuilder.RecordProperty;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import static org.junit.Assert.*;
import org.junit.Test;

public class RecordTypeTest {
    RecordTypeBuilder builder;
    JSTypeRegistry registry;
    Map<String, RecordProperty> properties;
    RecordType recordType;

    // Helper method to initialize the RecordType object for testing
    private void initialize() {
        registry = new JSTypeRegistry();
        builder = new RecordTypeBuilder(registry);
        properties = Maps.newHashMap();
        recordType = new RecordType(registry, properties);
    }

    @Test
    public void testIsSynthetic() {
        initialize();
        assertFalse(recordType.isSynthetic());
    }

    @Test
    public void testCheckRecordEquivalenceHelper() {
        initialize();
        RecordType otherRecord = new RecordType(registry, properties);
        assertTrue(recordType.checkRecordEquivalenceHelper(otherRecord, false));
    }

    @Test
    public void testDefineProperty() {
        initialize();
        assertFalse(recordType.defineProperty("property", registry.getNativeType(JSTypeNative.NO_TYPE), true, null));
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        initialize();
        JSType that = registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE);
        assertEquals(registry.getNativeObjectType(JSTypeNative.NO_TYPE), recordType.getGreatestSubtypeHelper(that));
    }

    @Test
    public void testIsSubtype() {
        initialize();
        JSType that = registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE);
        assertTrue(recordType.isSubtype(that));
    }

    @Test
    public void testResolveInternal() {
        initialize();
        ErrorReporter t = null;
        StaticScope<JSType> scope = null;
        assertEquals(recordType, recordType.resolveInternal(t, scope));
    }
}