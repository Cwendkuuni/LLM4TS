package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RecordTypeTest {
    @Test
    public void testIsEquivalentTo() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordType record1 = new RecordType(registry, null);
        RecordType record2 = new RecordType(registry, null);
        assertFalse(record1.isEquivalentTo(null));
        assertTrue(record1.isEquivalentTo(record1));
        assertFalse(record1.isEquivalentTo(record2));
    }

    @Test
    public void testIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordType record1 = new RecordType(registry, null);
        RecordType record2 = new RecordType(registry, null);
        assertFalse(record1.isSubtype(null));
        assertTrue(record1.isSubtype(record1));
        assertFalse(record1.isSubtype(record2));
    }
}