package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class RecordTypeBuilderTest {

    private JSTypeRegistry registry;
    private RecordTypeBuilder builder;

    @Before
    public void setUp() {
        registry = mock(JSTypeRegistry.class);
        builder = new RecordTypeBuilder(registry);
    }

    @Test
    public void testEmptyRecord() {
        JSType record = builder.build();
        assertNotNull(record);
        assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), record);
    }

    @Test
    public void testAddProperty() {
        String name = "name";
        JSType type = mock(JSType.class);
        Node propertyNode = mock(Node.class);

        builder.addProperty(name, type, propertyNode);

        Map<String, RecordProperty> properties = builder.properties;
        assertNotNull(properties);
        assertEquals(1, properties.size());
        RecordProperty property = properties.get(name);
        assertNotNull(property);
        assertEquals(type, property.type);
        assertEquals(propertyNode, property.propertyNode);
    }

    @Test
    public void testAddDuplicateProperty() {
        String name = "name";
        JSType type = mock(JSType.class);
        Node propertyNode = mock(Node.class);

        builder.addProperty(name, type, propertyNode);

        when(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)
            .getDeclaredProperty(name)).thenReturn(mock(Property.class));

        RecordTypeBuilder builder2 = new RecordTypeBuilder(registry);
        builder2.addProperty(name, type, propertyNode);

        assertNull(builder2.build());
    }
}