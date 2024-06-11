package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordTypeBuilder.RecordProperty;

import java.util.Map;
import java.util.SortedMap;

public class RecordTypeTest {
    private JSTypeRegistry registry;
    private RecordType recordType;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        recordType = new RecordType(registry, Maps.<String, RecordProperty>newHashMap());
    }

    @Test
    public void testIsEquivalentTo_sameRecordType_returnsTrue() {
        RecordType other = recordType;
        assertTrue(recordType.isEquivalentTo(other));
    }

    @Test
    public void testIsEquivalentTo_differentRecordType_returnsFalse() {
        RecordType other = new RecordType(registry, Maps.<String, RecordProperty>newHashMap());
        assertFalse(recordType.isEquivalentTo(other));
    }

    @Test
    public void testIsEquivalentTo_differentObjectType_returnsFalse() {
        ObjectType other = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        assertFalse(recordType.isEquivalentTo(other));
    }

    @Test
    public void testGetImplicitPrototype_returnsObjectType() {
        ObjectType expected = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        assertEquals(expected, recordType.getImplicitPrototype());
    }

    @Test
    public void testDefineProperty_callWhenFrozen_returnsFalse() {
        recordType.defineProperty("prop", registry.getNativeType(JSTypeNative.NUMBER_TYPE), false, null);
        SortedMap<String, JSType> expectedProperties = Maps.newTreeMap();
        assertFalse(recordType.defineProperty("newProp", registry.getNativeType(JSTypeNative.STRING_TYPE), false, null));
        assertFalse(recordType.getPropertyType("newProp").isStringValueType());
    }

    @Test
    public void testIsSubtype_sameRecordType_returnsTrue() {
        assertTrue(recordType.isSubtype(recordType));
    }

    @Test
    public void testIsSubtype_differentRecordType_returnsFalse() {
        RecordType other = new RecordType(registry, Maps.<String, RecordProperty>newHashMap());
        assertFalse(recordType.isSubtype(other));
    }

    @Test
    public void testIsSubtype_objectType_returnsTrue() {
        ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        assertTrue(recordType.isSubtype(objectType));
    }

    @Test
    public void testIsSubtype_otherType_returnsFalse() {
        JSType otherType = registry.getNativeType(JSTypeNative.BOOLEAN_TYPE);
        assertFalse(recordType.isSubtype(otherType));
    }

    @Test
    public void testIsSubtype_otherRecordType_returnsFalse() {
        RecordType other = new RecordType(registry, Maps.<String, RecordProperty>newHashMap());
        assertFalse(recordType.isSubtype(other));
    }

    @Test
    public void testIsSubtype_samePropertiesDifferentTypes_returnsFalse() {
        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("prop", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
        RecordType other = new RecordType(registry, properties);
        assertFalse(recordType.isSubtype(other));
    }

    @Test
    public void testResolveInternal_allPropertiesResolvable_returnsResolvedType() {
        SortedMap<String, JSType> properties = Maps.newTreeMap();
        properties.put("prop1", registry.getNativeType(JSTypeNative.STRING_TYPE));
        properties.put("prop2", registry.getNativeType(JSTypeNative.NUMBER_TYPE));
        recordType = new RecordType(registry, properties);
        JSType resolvedType = recordType.resolveInternal(null, null);
        assertTrue(resolvedType.isRecordType());
        assertEquals(registry.getNativeType(JSTypeNative.STRING_TYPE),
                     resolvedType.toMaybeRecordType().getPropertyType("prop1"));
        assertEquals(registry.getNativeType(JSTypeNative.NUMBER_TYPE),
                     resolvedType.toMaybeRecordType().getPropertyType("prop2"));
    }

    @Test
    public void testResolveInternal_somePropertiesResolvable_returnsResolvedType() {
        SortedMap<String, JSType> properties = Maps.newTreeMap();
        properties.put("prop1", registry.getNativeType(JSTypeNative.STRING_TYPE));
        properties.put("prop2", registry.createUnknownType());
        recordType = new RecordType(registry, properties);
        JSType resolvedType = recordType.resolveInternal(null, null);
        assertTrue(resolvedType.isRecordType());
        assertEquals(registry.getNativeType(JSTypeNative.STRING_TYPE),
                     resolvedType.toMaybeRecordType().getPropertyType("prop1"));
        assertTrue(resolvedType.toMaybeRecordType().getPropertyType("prop2").isUnknownType());
    }

    @Test
    public void testResolveInternal_noPropertiesResolvable_returnsResolvedType() {
        JSType resolvedType = recordType.resolveInternal(null, null);
        assertTrue(resolvedType.isRecordType());
        assertTrue(resolvedType.toMaybeRecordType().getProperties().isEmpty());
    }
}