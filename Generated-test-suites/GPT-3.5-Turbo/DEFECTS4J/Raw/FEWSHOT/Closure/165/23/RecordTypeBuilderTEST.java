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
        JSType propertyType = registry.getNativeType(JSTypeNative.STRING_TYPE);
        Node propertyNode = new Node();
        recordTypeBuilder.addProperty("name", propertyType, propertyNode);

        assertEquals(propertyType, recordTypeBuilder.build().getPropertyType("name"));
        assertEquals(propertyNode, recordTypeBuilder.build().getPropertyNode("name"));

        assertNull(recordTypeBuilder.addProperty("name", propertyType, propertyNode));
    }

    @Test
    public void testBuild() {
        JSType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        assertTrue(recordTypeBuilder.build() instanceof RecordType);
        assertEquals(objectType, recordTypeBuilder.build().toObjectType());
    }

}