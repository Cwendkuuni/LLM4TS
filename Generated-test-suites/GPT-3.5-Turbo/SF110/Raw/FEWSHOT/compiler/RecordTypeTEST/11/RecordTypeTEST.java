package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordTypeBuilder.RecordProperty;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {
    private JSTypeRegistry registry;
    private Map<String, RecordProperty> properties;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        properties = new HashMap<>();
        properties.put("foo", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), new Node(Token.STRING)));
        properties.put("bar", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), new Node(Token.NUMBER)));
    }

    @Test
    public void testRecordType_constructor() {
        RecordType recordType = new RecordType(registry, properties);
        assertFalse(recordType.isSynthetic());
        assertTrue(recordType.isFrozen());

        assertTrue(recordType.hasProperty("foo"));
        assertTrue(recordType.hasProperty("bar"));
    }

    @Test
    public void testRecordType_getImplicitPrototype() {
        RecordType recordType = new RecordType(registry, properties);
        ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        assertEquals(objectType, recordType.getImplicitPrototype());
    }

    @Test
    public void testRecordType_defineProperty() {
        RecordType recordType = new RecordType(registry, properties);
        assertFalse(recordType.defineProperty("baz", registry.getNativeType(JSTypeNative.STRING_TYPE), true, new Node(Token.STRING)));
        assertFalse(recordType.defineProperty("foo", registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), true, new Node(Token.STRING)));
    }

    @Test
    public void testRecordType_getGreatestSubtypeHelper() {
        RecordType recordType = new RecordType(registry, properties);
        JSType recordTypeNoType = registry.getNativeObjectType(JSTypeNative.NO_TYPE);
        JSType recordTypeString = registry.getNativeType(JSTypeNative.STRING_TYPE);
        JSType recordTypeNumber = registry.getNativeType(JSTypeNative.NUMBER_TYPE);

        assertEquals(recordTypeNoType, recordType.getGreatestSubtypeHelper(recordTypeString));
        assertEquals(recordTypeNoType, recordType.getGreatestSubtypeHelper(recordTypeNumber));
    }

    @Test
    public void testRecordType_isSubtype() {
        RecordType recordType = new RecordType(registry, properties);
        RecordType recordTypeEmpty = new RecordType(registry, new HashMap<>());

        assertTrue(recordType.isSubtype(recordTypeEmpty));
    }

    @Test
    public void testRecordType_isSubtype_withInvalidInput() {
        RecordType recordType = new RecordType(registry, properties);
        ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

        assertFalse(recordType.isSubtype(objectType));
    }

    @Test
    public void testRecordType_isSubtypeHelper() {
        assertTrue(RecordType.isSubtype(recordType, recordTypeEmpty));
    }

    @Test
    public void testRecordType_checkRecordEquivalenceHelper() {
        assertTrue(recordType.checkRecordEquivalenceHelper(recordTypeEmpty, null));
    }
}