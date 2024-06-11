package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

public class RecordTypeTest {

    @Test
    public void testRecordTypeCreation() {
        JSTypeRegistry registry = new JSTypeRegistry(new ErrorReporter());

        Map<String, RecordProperty> recordProperties = Maps.newHashMap();
        recordProperties.put("x", new RecordProperty("x", registry.getStringType()));
        recordProperties.put("y", new RecordProperty("y", registry.getNumberType()));

        RecordType recordType = new RecordType(registry, recordProperties);

        assertEquals("{ x : java.lang.String, y : java.lang.Number}",
                recordType.toString());
    }

    @Test
    public void testIsEquivalentTo() {
        JSTypeRegistry registry = new JSTypeRegistry(new ErrorReporter());

        Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
        recordProperties1.put("x", new RecordProperty("x", registry.getStringType()));
        recordProperties1.put("y", new RecordProperty("y", registry.getNumberType()));

        RecordType recordType1 = new RecordType(registry, recordProperties1);

        Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
        recordProperties2.put("x", new RecordProperty("x", registry.getStringType()));
        recordProperties2.put("y", new RecordProperty("y", registry.getNumberType()));

        RecordType recordType2 = new RecordType(registry, recordProperties2);

        assertEquals(recordType1, recordType2);
        assertTrue(recordType1.isEquivalentTo(recordType2));
    }

    @Test
    public void testIsNotEquivalentToDifferentProperties() {
        JSTypeRegistry registry = new JSTypeRegistry(new ErrorReporter());

        Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
        recordProperties1.put("x", new RecordProperty("x", registry.getStringType()));
        recordProperties1.put("y", new RecordProperty("y", registry.getNumberType()));

        RecordType recordType1 = new RecordType(registry, recordProperties1);

        Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
        recordProperties2.put("x", new RecordProperty("x", registry.getStringType()));
        recordProperties2.put("z", new RecordProperty("z", registry.getNumberType()));

        RecordType recordType2 = new RecordType(registry, recordProperties2);

        assertFalse(recordType1.isEquivalentTo(recordType2));
    }

    @Test
    public void testIsNotEquivalentToDifferentTypes() {
        JSTypeRegistry registry = new JSTypeRegistry(new ErrorReporter());

        Map<String, RecordProperty> recordProperties = Maps.newHashMap();
        recordProperties.put("x", new RecordProperty("x", registry.getIntType()));
        recordProperties.put("y", new RecordProperty("y", registry.getBooleanType()));

        RecordType recordType = new RecordType(registry, recordProperties);

        RecordType otherRecordType = new RecordType(registry, null);

        assertFalse(recordType.isEquivalentTo(otherRecordType));
    }

    @Test
    public void testGetImplicitPrototype() {
        JSTypeRegistry registry = new JSTypeRegistry(new ErrorReporter());

        Map<String, RecordProperty> recordProperties = Maps.newHashMap();
        recordProperties.put("x", new RecordProperty("x", registry.getStringType()));

        RecordType recordType = new RecordType(registry, recordProperties);

        assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE),
                recordType.getImplicitPrototype());
    }

    @Test
    public void testDefinePropertyFrozen() {
        JSTypeRegistry registry = new JSTypeRegistry(new ErrorReporter());

        Map<String, RecordProperty> recordProperties = Maps.newHashMap();
        recordProperties.put("x", new RecordProperty("x", registry.getStringType()));

        RecordType recordType = new RecordType(registry, recordProperties);

        assertFalse(recordType.defineProperty("x", registry.getIntType(),
                false, null));
    }

    @Test
    public void testGetLeastSupertype() {
        JSTypeRegistry registry = new JSTypeRegistry(new ErrorReporter());

        Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
        recordProperties1.put("x", new RecordProperty("x", registry.getStringType()));
        recordProperties1.put("y", new RecordProperty("y", registry.getNumberType()));

        RecordType recordType1 = new RecordType(registry, recordProperties1);

        Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
        recordProperties2.put("x", new RecordProperty("x", registry.getStringType()));

        RecordType recordType2 = new RecordType(registry, recordProperties2);

        assertEquals(recordType1, recordType2.getLeastSupertype(recordType1));
        assertEquals(recordType1, recordType2.getLeastSupertype(recordType2));

        Map<String, RecordProperty> recordProperties3 = Maps.newHashMap();
        recordProperties3.put("x", new RecordProperty("x", registry.getStringType()));
        recordProperties3.put("z", new RecordProperty("z", registry.getNumberType()));

        RecordType recordType3 = new RecordType(registry, recordProperties3);

        assertEquals(recordType1, recordType1.getLeastSupertype(recordType3));
        assertEquals(recordType3, recordType3.getLeastSupertype(recordType1));
    }

    @Test
    public void testGetGreatestSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry(new ErrorReporter());

        Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
        recordProperties1.put("x", new RecordProperty("x", registry.getStringType()));
        recordProperties1.put("y", new RecordProperty("y", registry.getNumberType()));

        RecordType recordType1 = new RecordType(registry, recordProperties1);

        Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
        recordProperties2.put("x", new RecordProperty("x", registry.getStringType()));
        recordProperties2.put("z", new RecordProperty("z", registry.getNumberType()));

        RecordType recordType2 = new RecordType(registry, recordProperties2);

        RecordType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);

        assertEquals(recordType1, greatestSubtype);
    }

    @Test
    public void testGetGreatestSubtypeWithConflictingProperties() {
        JSTypeRegistry registry = new JSTypeRegistry(new ErrorReporter());

        Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
        recordProperties1.put("x", new RecordProperty("x", registry.getStringType()));
        recordProperties1.put("y", new RecordProperty("y", registry.getNumberType()));

        RecordType recordType1 = new RecordType(registry, recordProperties1);

        Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
        recordProperties2.put("x", new RecordProperty("x", registry.getIntType()));
        recordProperties2.put("y", new RecordProperty("y", registry.getNumberType()));

        RecordType recordType2 = new RecordType(registry, recordProperties2);

        assertEquals(registry.getNativeObjectType(JSTypeNative.NO_TYPE),
                recordType1.getGreatestSubtypeHelper(recordType2));
    }

    @Test
    public void testToString() {
        JSTypeRegistry registry = new JSTypeRegistry(new ErrorReporter());

        Map<String, RecordProperty> recordProperties = Maps.newHashMap();
        recordProperties.put("x", new RecordProperty("x", registry.getStringType()));
        recordProperties.put("y", new RecordProperty("y", registry.getNumberType()));

        RecordType recordType = new RecordType(registry, recordProperties);

        assertEquals("{ x : java.lang.String, y : java.lang.Number}",
                recordType.toString());
    }

    @Test
    public void testIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry(new ErrorReporter());

        Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
        recordProperties1.put("x", new RecordProperty("x", registry.getStringType()));
        recordProperties1.put("y", new RecordProperty("y", registry.getNumberType()));

        RecordType recordType1 = new RecordType(registry, recordProperties1);

        Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
        recordProperties2.put("x", new RecordProperty("x", registry.getIntType()));
        recordProperties2.put("y", new RecordProperty("y", registry.getNumberType()));

        RecordType recordType2 = new RecordType(registry, recordProperties2);

        assertTrue(recordType1.isSubtype(recordType2));

        Map<String, RecordProperty> recordProperties3 = Maps.newHashMap();
        recordProperties3.put("x", new RecordProperty("x", registry.getStringType()));

        RecordType recordType3 = new RecordType(registry, recordProperties3);

        assertTrue(recordType1.isSubtype(recordType3));

        Map<String, RecordProperty> recordProperties4 = Maps.newHashMap();
        recordProperties4.put("x", new RecordProperty("x", registry.getStringType()));
        recordProperties4.put("z", new RecordProperty("z", registry.getNumberType()));

        RecordType recordType4 = new RecordType(registry, recordProperties4);

        assertFalse(recordType1.isSubtype(recordType4));

        Map<String, RecordProperty> recordProperties5 = Maps.newHashMap();
        recordProperties5.put("y", new RecordProperty("y", registry.getNumberType()));

        RecordType recordType5 = new RecordType(registry, recordProperties5);

        assertFalse(recordType1.isSubtype(recordType5));
    }

    @Test
    public void testIsSubtypeWithObjectType() {
        JSTypeRegistry registry = new JSTypeRegistry(new ErrorReporter());

        Map<String, RecordProperty> recordProperties = Maps.newHashMap();
        recordProperties.put("x", new RecordProperty("x", registry.getStringType()));

        RecordType recordType = new RecordType(registry, recordProperties);

        assertTrue(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)
                .isSubtype(recordType));
    }

    @Test
    public void testResolveInternal() {
        JSTypeRegistry registry = new JSTypeRegistry(new ErrorReporter());

        Map<String, RecordProperty> recordProperties = Maps.newHashMap();
        recordProperties.put("x", new RecordProperty("x", registry.getStringType()));

        RecordType recordType = new RecordType(registry, recordProperties);

        ErrorReporter errorReporter = new ErrorReporter();
        StaticScope<JSType> scope = new StaticScope<>(registry, errorReporter);

        Node node = new Node_String("foo");
        JSType stringType = registry.getStringType();

        recordType.resolveInternal(errorReporter, scope);
        assertEquals(stringType, recordType.getPropertyType("x"));
    }
}