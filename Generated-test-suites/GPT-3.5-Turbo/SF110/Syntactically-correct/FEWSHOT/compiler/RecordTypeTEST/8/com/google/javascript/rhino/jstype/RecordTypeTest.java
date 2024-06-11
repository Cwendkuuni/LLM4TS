package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordTypeBuilder.RecordProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {

    private JSTypeRegistry registry;
    private RecordType recordType;
    private Map<String, RecordProperty> properties;
    private RecordProperty property;
    private Node propertyNode;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        properties = new HashMap<String, RecordProperty>();
        propertyNode = new Node();
        property = new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), propertyNode);
        properties.put("property", property);
        recordType = new RecordType(registry, properties);
    }

    @Test
    public void testRecordType() {
        assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), recordType.getImplicitPrototype());
        assertEquals(true, recordType.isSynthetic());
        assertEquals(true, recordType.isDefined());
        assertEquals(true, recordType.isFrozen());
        assertEquals(false, recordType.isEquivalentTo(recordType));
        assertEquals("property", recordType.getOwnPropertyNames().iterator().next());
        assertEquals(property, recordType.getPropertyType("property"));
    }

    @Test
    public void testCheckRecordEquivalenceHelper() {
        RecordType otherRecord = new RecordType(registry, properties);
        assertEquals(true, recordType.checkRecordEquivalenceHelper(otherRecord, EquivalenceMethod.STRICT));
    }

    @Test(expected = IllegalStateException.class)
    public void testDefinePropertyNotFrozen() {
        recordType.defineProperty("newProperty", registry.getNativeType(JSTypeNative.STRING_TYPE), true, propertyNode);
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        JSType that = registry.getNativeType(JSTypeNative.STRING_TYPE);
        assertEquals(registry.getNativeObjectType(JSTypeNative.NO_TYPE), recordType.getGreatestSubtypeHelper(that));
    }

    @Test
    public void testToMaybeRecordType() {
        assertEquals(recordType, recordType.toMaybeRecordType());
    }

    @Test
    public void testIsSubtype() {
        assertFalse(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.STRING_TYPE)));
        assertTrue(recordType.isSubtype(recordType));
        assertTrue(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
        RecordType that = new RecordType(registry, properties);
        assertTrue(RecordType.isSubtype(recordType, that));
    }
}