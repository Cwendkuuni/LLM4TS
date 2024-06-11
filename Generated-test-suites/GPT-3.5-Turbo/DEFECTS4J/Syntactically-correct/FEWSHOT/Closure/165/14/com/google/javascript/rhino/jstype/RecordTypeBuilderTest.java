package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class RecordTypeBuilderTest {
    private RecordTypeBuilder builder;

    @Before
    public void setUp() {
        builder = new RecordTypeBuilder(new JSTypeRegistry());
    }

    @Test
    public void testAddProperty() {
        JSType type = new JSType();
        Node propertyNode = new Node();
        builder.addProperty("name", type, propertyNode);

        assertEquals(type, builder.properties.get("name").getType());
        assertEquals(propertyNode, builder.properties.get("name").getPropertyNode());

        assertNull(builder.addProperty("name", type, propertyNode));
    }

    @Test
    public void testBuild() {
        JSType type = new JSType();
        Node propertyNode = new Node();
        builder.addProperty("name", type, propertyNode);

        assertEquals(type, builder.build().getType());
        assertEquals(Collections.unmodifiableMap(builder.properties), builder.build().getProperties());
    }
}