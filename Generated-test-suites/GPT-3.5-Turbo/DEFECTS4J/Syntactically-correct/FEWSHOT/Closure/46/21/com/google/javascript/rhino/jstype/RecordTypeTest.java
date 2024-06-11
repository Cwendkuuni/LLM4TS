
package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {

    private RecordType recordType;
    private JSTypeRegistry registry;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        properties.put("a", new RecordTypeBuilder.RecordProperty(registry.getObjectType()));
        properties.put("b", new RecordTypeBuilder.RecordProperty(registry.getNumberType()));
        recordType = new RecordType(registry, properties);
    }

    @Test
    public void testDefineProperty_AddNewProperty_ReturnsTrue() {
        assertTrue(recordType.defineProperty("c", registry.getStringType(), false, null));
    }

    @Test
    public void testDefineProperty_AddExistingProperty_ReturnsFalse() {
        assertFalse(recordType.defineProperty("a", registry.getObjectType(), false, null));
    }

    @Test
    public void testIsEquivalentTo_SameRecordType_ReturnsTrue() {
        assertTrue(recordType.isEquivalentTo(recordType));
    }

    @Test
    public void testIsEquivalentTo_RecordTypeWithDifferentProperties_ReturnsFalse() {
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        properties.put("c", new RecordTypeBuilder.RecordProperty(registry.getObjectType()));
        RecordType otherRecordType = new RecordType(registry, properties);
        assertFalse(recordType.isEquivalentTo(otherRecordType));
    }

    @Test
    public void testIsEquivalentTo_RecordTypeWithSamePropertiesButDifferentOrder_ReturnsTrue() {
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        properties.put("b", new RecordTypeBuilder.RecordProperty(registry.getNumberType()));
        properties.put("a", new RecordTypeBuilder.RecordProperty(registry.getObjectType()));
        RecordType otherRecordType = new RecordType(registry, properties);
        assertTrue(recordType.isEquivalentTo(otherRecordType));
    }

    @Test
    public void testIsSubtype_SubtypeOfRecordTypeWithSameProperties_ReturnsTrue() {
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        properties.put("a", new RecordTypeBuilder.RecordProperty(registry.getObjectType()));
        properties.put("b", new RecordTypeBuilder.RecordProperty(registry.getNumberType()));
        RecordType otherRecordType = new RecordType(registry, properties);
        assertTrue(otherRecordType.isSubtype(recordType));
    }

    @Test
    public void testIsSubtype_SubtypeOfRecordTypeWithExtraProperties_ReturnsTrue() {
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        properties.put("a", new RecordTypeBuilder.RecordProperty(registry.getObjectType()));
        properties.put("b", new RecordTypeBuilder.RecordProperty(registry.getNumberType()));
        properties.put("c", new RecordTypeBuilder.RecordProperty(registry.getStringType()));
        RecordType otherRecordType = new RecordType(registry, properties);
        assertTrue(otherRecordType.isSubtype(recordType));
    }

    @Test
    public void testIsSubtype_NotSubtypeOfDifferentRecordType_ReturnsFalse() {
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        properties.put("c", new RecordTypeBuilder.RecordProperty(registry.getObjectType()));
        RecordType otherRecordType = new RecordType(registry, properties);
        assertFalse(otherRecordType.isSubtype(recordType));
    }

    @Test
    public void testIsSubtype_NotSubtypeOfRecordTypeWithDifferentProperties_ReturnsFalse() {
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        properties.put("c", new RecordTypeBuilder.RecordProperty(registry.getNumberType()));
        properties.put("d", new RecordTypeBuilder.RecordProperty(registry.getStringType()));
        RecordType otherRecordType = new RecordType(registry, properties);
        assertFalse(otherRecordType.isSubtype(recordType));
    }
}
