package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;
import org.junit.Test;

public class RecordTypeTest {

    @Test
    public void testIsEquivalentTo() {
        // Test cases for isEquivalentTo method

        // Test record type equivalent to itself
        RecordType recordType1 = new RecordType();
        assertTrue(recordType1.isEquivalentTo(recordType1));

        // Test equivalent record types with same properties and types
        RecordType recordType2 = new RecordType();
        recordType1.addProperty("prop1", "type1");
        recordType1.addProperty("prop2", "type2");
        recordType2.addProperty("prop1", "type1");
        recordType2.addProperty("prop2", "type2");
        assertTrue(recordType1.isEquivalentTo(recordType2));

        // Test equivalent record types with different properties
        RecordType recordType3 = new RecordType();
        recordType3.addProperty("prop3", "type3");
        assertFalse(recordType1.isEquivalentTo(recordType3));

        // Test equivalent record types with different property types
        RecordType recordType4 = new RecordType();
        recordType4.addProperty("prop1", "type4");
        recordType4.addProperty("prop2", "type5");
        assertFalse(recordType1.isEquivalentTo(recordType4));

        // Test record type not equivalent to null
        assertFalse(recordType1.isEquivalentTo(null));
    }

    @Test
    public void testIsSubtype() {
        // Test cases for isSubtype method

        // Test record type is subtype of itself
        RecordType recordType1 = new RecordType();
        assertTrue(recordType1.isSubtype(recordType1));

        // Test record type is subtype of OBJECT_TYPE
        ObjectType objectType = new ObjectType();
        assertTrue(recordType1.isSubtype(objectType));

        // Test record type is not subtype of null
        assertFalse(recordType1.isSubtype(null));

        // Test record type is subtype of another record type with same properties and types
        RecordType recordType2 = new RecordType();
        recordType1.addProperty("prop1", "type1");
        recordType1.addProperty("prop2", "type2");
        recordType2.addProperty("prop1", "type1");
        recordType2.addProperty("prop2", "type2");
        assertTrue(recordType1.isSubtype(recordType2));

        // Test record type is not subtype of another record type with different properties
        RecordType recordType3 = new RecordType();
        recordType3.addProperty("prop3", "type3");
        assertFalse(recordType1.isSubtype(recordType3));

        // Test record type is not subtype of another record type with different property types
        RecordType recordType4 = new RecordType();
        recordType4.addProperty("prop1", "type4");
        recordType4.addProperty("prop2", "type5");
        assertFalse(recordType1.isSubtype(recordType4));
    }
}