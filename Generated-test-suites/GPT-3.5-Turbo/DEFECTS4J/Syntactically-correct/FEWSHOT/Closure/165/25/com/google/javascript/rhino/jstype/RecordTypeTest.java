package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {
    private JSTypeRegistry registry;
    private RecordType recordType;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        recordType = new RecordType(registry, null);
    }

    @Test
    public void testIsEquivalentTo() {
        assertFalse(recordType.isEquivalentTo(null));

        recordType.defineDeclaredProperty("property1", registry.getNativeType(JSTypeNative.STRING_TYPE), false, null);
        recordType.defineDeclaredProperty("property2", registry.getNativeType(JSTypeNative.NUMBER_TYPE), false, null);

        RecordType otherRecordType = new RecordType(registry, null);
        assertTrue(recordType.isEquivalentTo(otherRecordType));

        otherRecordType.defineDeclaredProperty("property1", registry.getNativeType(JSTypeNative.STRING_TYPE), false, null);
        assertFalse(recordType.isEquivalentTo(otherRecordType));

        otherRecordType.defineDeclaredProperty("property2", registry.getNativeType(JSTypeNative.NUMBER_TYPE), false, null);
        assertTrue(recordType.isEquivalentTo(otherRecordType));

        otherRecordType.defineDeclaredProperty("property3", registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), false, null);
        assertFalse(recordType.isEquivalentTo(otherRecordType));
    }

    @Test
    public void testGetImplicitPrototype() {
        ObjectType implicitPrototype = recordType.getImplicitPrototype();
        assertEquals(JSTypeNative.OBJECT_TYPE, implicitPrototype.toString());
    }

    @Test
    public void testDefineProperty() {
        assertFalse(recordType.defineProperty("property1", registry.getNativeType(JSTypeNative.STRING_TYPE), false, null));

        recordType.freeze();

        assertFalse(recordType.defineProperty("property2", registry.getNativeType(JSTypeNative.NUMBER_TYPE), false, null));
    }

    @Test
    public void testIsSubtype() {
        assertFalse(recordType.isSubtype(null));

        recordType.defineDeclaredProperty("property1", registry.getNativeType(JSTypeNative.STRING_TYPE), false, null);
        recordType.defineDeclaredProperty("property2", registry.getNativeType(JSTypeNative.NUMBER_TYPE), false, null);

        RecordType otherRecordType = new RecordType(registry, null);
        assertFalse(recordType.isSubtype(otherRecordType));

        otherRecordType.defineDeclaredProperty("property1", registry.getNativeType(JSTypeNative.STRING_TYPE), false, null);
        assertFalse(recordType.isSubtype(otherRecordType));

        otherRecordType.defineDeclaredProperty("property2", registry.getNativeType(JSTypeNative.NUMBER_TYPE), false, null);
        assertTrue(recordType.isSubtype(otherRecordType));

        otherRecordType.defineDeclaredProperty("property3", registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), false, null);
        assertFalse(recordType.isSubtype(otherRecordType));
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        assertNull(recordType.getGreatestSubtypeHelper(null));

        recordType.defineDeclaredProperty("property1", registry.getNativeType(JSTypeNative.STRING_TYPE), false, null);
        recordType.defineDeclaredProperty("property2", registry.getNativeType(JSTypeNative.NUMBER_TYPE), false, null);

        ObjectType objType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        assertEquals(objType, recordType.getGreatestSubtypeHelper(objType));

        RecordType otherRecordType1 = new RecordType(registry, null);
        assertNull(recordType.getGreatestSubtypeHelper(otherRecordType1));

        otherRecordType1.defineDeclaredProperty("property1", registry.getNativeType(JSTypeNative.STRING_TYPE), false, null);
        assertNull(recordType.getGreatestSubtypeHelper(otherRecordType1));

        otherRecordType1.defineDeclaredProperty("property2", registry.getNativeType(JSTypeNative.NUMBER_TYPE), false, null);
        RecordType expectedRecordType1 = new RecordType(registry, null);
        expectedRecordType1.defineDeclaredProperty("property1", registry.getNativeType(JSTypeNative.STRING_TYPE), false, null);
        expectedRecordType1.defineDeclaredProperty("property2", registry.getNativeType(JSTypeNative.NUMBER_TYPE), false, null);
        assertEquals(expectedRecordType1, recordType.getGreatestSubtypeHelper(otherRecordType1));

        otherRecordType1.defineDeclaredProperty("property3", registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), false, null);
        assertNull(recordType.getGreatestSubtypeHelper(otherRecordType1));

        RecordType otherRecordType2 = new RecordType(registry, null);
        assertEquals(expectedRecordType1, recordType.getGreatestSubtypeHelper(otherRecordType2));

        otherRecordType2.defineDeclaredProperty("property1", registry.getNativeType(JSTypeNative.STRING_TYPE), false, null);
        otherRecordType2.defineDeclaredProperty("property2", registry.getNativeType(JSTypeNative.NUMBER_TYPE), false, null);
        assertEquals(expectedRecordType1, recordType.getGreatestSubtypeHelper(otherRecordType2));

        otherRecordType2.defineDeclaredProperty("property3", registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), false, null);
        assertNull(recordType.getGreatestSubtypeHelper(otherRecordType2));
    }

    @Test
    public void testResolveInternal() {
        ErrorReporter errorReporter = new ErrorReporter(null, null);
        StaticScope<JSType> scope = new StaticScope<>();
        assertEquals(recordType, recordType.resolveInternal(errorReporter, scope));
    }
}