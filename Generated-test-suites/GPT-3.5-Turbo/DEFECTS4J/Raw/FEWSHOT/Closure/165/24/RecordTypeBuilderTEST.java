package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class RecordTypeBuilderTest {
    private JSTypeRegistry registry;
    private RecordTypeBuilder builder;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        builder = new RecordTypeBuilder(registry);
    }

    @Test
    public void testAddProperty() {
        // Test adding a property with a valid name and type
        JSType type = new JSType();
        Node propertyNode = new Node();
        assertEquals(builder, builder.addProperty("name", type, propertyNode));
        // Test adding a property with a duplicate name
        assertNull(builder.addProperty("name", type, propertyNode));
    }

    @Test
    public void testBuild() {
        // Test building an empty record
        JSType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        assertEquals(objectType, builder.build());
        // Test building a record with properties
        JSType type = new JSType();
        Node propertyNode = new Node();
        builder.addProperty("name", type, propertyNode);
        RecordType recordType = new RecordType(registry, Map.of("name", new RecordProperty(type, propertyNode)));
        assertEquals(recordType, builder.build());
    }
}