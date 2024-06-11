package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class RecordTypeTest {
    @Test
    public void testIsSynthetic() {
        RecordType recordType1 = new RecordType(null, null, true);
        assertFalse(recordType1.isSynthetic());
    }

    @Test
    public void testCheckRecordEquivalenceHelper() {
        RecordType recordType1 = new RecordType(null, null, true);
        RecordType recordType2 = new RecordType(null, null, true);
        boolean result = recordType1.checkRecordEquivalenceHelper(recordType2, true);
        assertFalse(result);
    }

    @Test
    public void testDefineProperty() {
        RecordType recordType1 = new RecordType(null, null, true);
        boolean result = recordType1.defineProperty("testProp", null, true, null);
        assertFalse(result);
    }

    @Test
    public void testGetImplicitPrototype() {
        RecordType recordType1 = new RecordType(null, null, true);
        ObjectType objectType = recordType1.getImplicitPrototype();
        assertEquals(objectType, null);
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        RecordType recordType1 = new RecordType(null, null, true);
        JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(null);
        assertEquals(greatestSubtype, null);
    }

    @Test
    public void testIsSubtype() {
        RecordType recordType1 = new RecordType(null, null, true);
        boolean isSubtype = recordType1.isSubtype(null);
        assertFalse(isSubtype);
    }

    @Test
    public void testResolveInternal() {
        RecordType recordType1 = new RecordType(null, null, true);
        JSType resolvedType = recordType1.resolveInternal(null, null);
        assertEquals(resolvedType, null);
    }
}