package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;
import org.junit.Test;

public class RecordTypeBuilderTest {

    @Test
    public void testAddProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);

        // Test adding a new property
        builder.addProperty("name", new JSType(), new Node());
        assertEquals(1, builder.properties.size());

        // Test adding a duplicate property
        RecordTypeBuilder duplicateBuilder = builder.addProperty("name", new JSType(), new Node());
        assertNull(duplicateBuilder);
        assertEquals(1, builder.properties.size());
    }

    @Test
    public void testBuild() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);

        // Test building an empty record
        JSType emptyRecord = builder.build();
        assertTrue(emptyRecord instanceof RecordType);
        assertTrue(((RecordType) emptyRecord).isEmpty());

        // Test building a non-empty record
        builder.addProperty("name", new JSType(), new Node());
        JSType nonEmptyRecord = builder.build();
        assertTrue(nonEmptyRecord instanceof RecordType);
        assertFalse(((RecordType) nonEmptyRecord).isEmpty());
    }
}