package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class RecordTypeBuilderTest {

    private JSTypeRegistry registry;
    private RecordTypeBuilder recordTypeBuilder;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        recordTypeBuilder = new RecordTypeBuilder(registry);
    }

    @Test
    public void testAddProperty() {
        JSType type = registry.createType();
        Node propertyNode = new Node();
        RecordTypeBuilder.RecordProperty property = new RecordTypeBuilder.RecordProperty(type, propertyNode);

        assertNull(recordTypeBuilder.addProperty("name", type, propertyNode));
        recordTypeBuilder.addProperty("name", type, propertyNode);
        assertTrue(recordTypeBuilder.addProperty("name2", type, propertyNode) instanceof RecordTypeBuilder);
    }

    @Test
    public void testBuild() {
        JSType type = registry.createType();
        Node propertyNode = new Node();
        recordTypeBuilder.addProperty("name", type, propertyNode);
        assertEquals(RecordType.class, recordTypeBuilder.build().getClass());

        recordTypeBuilder = new RecordTypeBuilder(registry);
        assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), recordTypeBuilder.build());
    }
}