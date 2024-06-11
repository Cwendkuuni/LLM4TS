package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {

    private RecordType recordType;

    @Before
    public void setUp() {
        recordType = new RecordType();
    }

    @Test
    public void testIsEquivalentTo() {
        RecordType other = new RecordType();

        // Compare two empty record types
        assertTrue(recordType.isEquivalentTo(other));

        // Add a property to one of the record types
        recordType.addProperty("name", new StringType());
        assertFalse(recordType.isEquivalentTo(other));

        // Add the same property to the other record type
        other.addProperty("name", new StringType());
        assertTrue(recordType.isEquivalentTo(other));

        // Add a different property to one of the record types
        recordType.addProperty("age", new NumberType());
        assertFalse(recordType.isEquivalentTo(other));
    }

    @Test
    public void testGetPropertyType() {
        recordType.addProperty("name", new StringType());
        recordType.addProperty("age", new NumberType());

        assertEquals(new StringType(), recordType.getPropertyType("name"));
        assertEquals(new NumberType(), recordType.getPropertyType("age"));
    }

    @Test
    public void testHasProperty() {
        recordType.addProperty("name", new StringType());
        recordType.addProperty("age", new NumberType());

        assertTrue(recordType.hasProperty("name"));
        assertTrue(recordType.hasProperty("age"));
        assertFalse(recordType.hasProperty("height"));
    }

    @Test
    public void testDefineProperty() {
        // Add a property with a non-inferred type
        assertTrue(recordType.defineProperty("name", new StringType(), false));
        assertTrue(recordType.hasProperty("name"));

        // Add a property with an inferred type
        assertTrue(recordType.defineProperty("age", new NumberType(), true));
        assertTrue(recordType.hasProperty("age"));

        // Try to redefine a property
        assertFalse(recordType.defineProperty("name", new StringType(), false));
    }

    @Test
    public void testGetLeastSupertype() {
        RecordType other = new RecordType();

        // Both empty record types, should return a new empty record type
        assertTrue(recordType.getLeastSupertype(other).isEmpty());

        // Add a common property to both record types
        recordType.addProperty("name", new StringType());
        other.addProperty("name", new StringType());
        assertTrue(recordType.getLeastSupertype(other).isEquivalentTo(recordType));
        assertTrue(recordType.getLeastSupertype(other).isEquivalentTo(other));
        assertTrue(other.getLeastSupertype(recordType).isEquivalentTo(recordType));
        assertTrue(other.getLeastSupertype(recordType).isEquivalentTo(other));

        // Add a property to one of the record types
        recordType.addProperty("age", new NumberType());
        assertEquals(recordType, recordType.getLeastSupertype(other));
        assertEquals(recordType, other.getLeastSupertype(recordType));

        // Add a property to the other record type
        other.addProperty("height", new NumberType());
        assertTrue(recordType.getLeastSupertype(other).isEmpty());
        assertTrue(other.getLeastSupertype(recordType).isEmpty());
    }

    @Test
    public void testIsSubtype() {
        RecordType other = new RecordType();

        // Empty record type should be a subtype of another empty record type
        assertTrue(recordType.isSubtype(other));

        // Add a common property to both record types
        recordType.addProperty("name", new StringType());
        other.addProperty("name", new StringType());
        assertTrue(recordType.isSubtype(other));
        assertTrue(other.isSubtype(recordType));

        // Add a property to one of the record types
        recordType.addProperty("age", new NumberType());
        assertFalse(recordType.isSubtype(other));
        assertTrue(other.isSubtype(recordType));

        // Add a property to the other record type
        other.addProperty("height", new NumberType());
        assertFalse(recordType.isSubtype(other));
        assertFalse(other.isSubtype(recordType));
    }

    @Test
    public void testResolveInternal() {
        StaticScope scope = new StaticScope();

        // Add a property with an unresolved type
        recordType.addProperty("name", new NameType());
        assertEquals(new UnknownType(), recordType.getPropertyType("name"));

        // Define a property with a resolved type
        // and check if it is resolved correctly
        recordType.resolveInternal(scope);
        assertEquals(new StringType(), recordType.getPropertyType("name"));
    }
}