package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class RecordTypeBuilderTest {
    private RecordTypeBuilder recordTypeBuilder;

    @Before
    public void setUp() {
        recordTypeBuilder = new RecordTypeBuilder(new JSTypeRegistry());
    }

    @Test
    public void testAddProperty() {
        JSType type = new JSType();
        Node propertyNode = new Node();

        assertEquals(recordTypeBuilder, recordTypeBuilder.addProperty("name", type, propertyNode));
        assertNull(recordTypeBuilder.addProperty("name", type, propertyNode));
    }

    @Test
    public void testBuild() {
        JSType type = new JSType();
        Node propertyNode = new Node();

        recordTypeBuilder.addProperty("name", type, propertyNode);

        assertEquals(type, recordTypeBuilder.build());
    }
}