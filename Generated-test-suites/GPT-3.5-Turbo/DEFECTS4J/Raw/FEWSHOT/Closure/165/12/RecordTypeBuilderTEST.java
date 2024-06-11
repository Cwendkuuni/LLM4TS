package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

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
        String name = "test";
        JSType type = registry.createUnknownType();
        Node propertyNode = new Node();

        RecordTypeBuilder returnedBuilder = builder.addProperty(name, type, propertyNode);

        assertFalse(builder.isEmpty);
        assertNull(returnedBuilder);

        RecordTypeBuilder duplicateBuilder = builder.addProperty(name, type, propertyNode);

        assertNull(duplicateBuilder);
    }

    @Test
    public void testBuild() {
        JSType emptyType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

        assertEquals(emptyType, builder.build());
    }

    @Test
    public void testRecordPropertyGetType() {
        JSType type = registry.createUnknownType();
        Node propertyNode = new Node();
        RecordTypeBuilder.RecordProperty recordProperty = new RecordTypeBuilder.RecordProperty(type, propertyNode);

        assertSame(type, recordProperty.getType());
    }

    @Test
    public void testRecordPropertyGetPropertyNode() {
        JSType type = registry.createUnknownType();
        Node propertyNode = new Node();
        RecordTypeBuilder.RecordProperty recordProperty = new RecordTypeBuilder.RecordProperty(type, propertyNode);

        assertSame(propertyNode, recordProperty.getPropertyNode());
    }
}