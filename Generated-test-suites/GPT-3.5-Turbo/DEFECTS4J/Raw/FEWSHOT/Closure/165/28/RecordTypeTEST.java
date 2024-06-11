package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class RecordTypeTest {
    @Test
    public void testIsEquivalentTo() {
        RecordType recordType = new RecordType(null, null);
        assertEquals(true, recordType.isEquivalentTo(recordType));
        assertEquals(false, recordType.isEquivalentTo(null));
        assertEquals(false, recordType.isEquivalentTo(new ObjectType(null, null, null)));
    }

    @Test
    public void testIsSubtype() {
        RecordType recordType = new RecordType(null, null);
        assertEquals(true, recordType.isSubtype(recordType));
        assertEquals(false, recordType.isSubtype(null));
        assertEquals(false, recordType.isSubtype(new ObjectType(null, null, null)));
    }

    @Test
    public void testDefineProperty() {
        RecordType recordType = new RecordType(null, null);
        assertEquals(true, recordType.defineProperty("prop1", new JSType(null, null), true, null));
        assertEquals(false, recordType.defineProperty("prop1", new JSType(null, null), true, null));
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        RecordType recordType = new RecordType(null, null);
        assertEquals(recordType, recordType.getGreatestSubtypeHelper(recordType));
        assertNull(recordType.getGreatestSubtypeHelper(null));
    }

    @Test
    public void testResolveInternal() {
        RecordType recordType = new RecordType(null, null);
        assertEquals(recordType, recordType.resolveInternal(null, null));
    }
}