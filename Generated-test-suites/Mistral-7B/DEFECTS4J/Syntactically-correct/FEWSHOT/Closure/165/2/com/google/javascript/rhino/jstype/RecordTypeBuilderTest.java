package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
    public void testAddPropertyWithEmptyRecord() {
        JSType type = mock(JSType.class);
        Node propertyNode = mock(Node.class);
        when(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)).thenReturn(type);

        RecordTypeBuilder recordTypeBuilder = builder.addProperty("name", type, propertyNode);
        JSType recordType = recordTypeBuilder.build();

        assertNotNull(recordType);
        assertEquals(type, recordType.getProperty("name").getType());
    }

    @Test
    public void testAddDuplicateProperty() {
        JSType type = mock(JSType.class);
        Node propertyNode = mock(Node.class);

        builder.addProperty("name", type, propertyNode);
        RecordTypeBuilder recordTypeBuilder = builder.addProperty("name", type, propertyNode);

        assertNull(recordTypeBuilder);
    }

    @Test
    public void testBuildEmptyRecord() {
        JSType recordType = builder.build();

        assertNotNull(recordType);
        assertTrue(recordType instanceof JSTypeNative);
        assertEquals(JSTypeNative.OBJECT_TYPE, recordType.getID());
    }

    @Test
    public void testBuildNonEmptyRecord() {
        String propertyName1 = "name1";
        JSType propertyType1 = mock(JSType.class);
        Node propertyNode1 = mock(Node.class);

        String propertyName2 = "name2";
        JSType propertyType2 = mock(JSType.class);
        Node propertyNode2 = mock(Node.class);

        builder.addProperty(propertyName1, propertyType1, propertyNode1);
        builder.addProperty(propertyName2, propertyType2, propertyNode2);

        Map<String, RecordProperty> properties = builder.build().getProperties();

        assertNotNull(properties);
        assertEquals(2, properties.size());
        assertNotNull(properties.get(propertyName1));
        assertNotNull(properties.get(propertyName2));
        assertEquals(propertyType1, properties.get(propertyName1).getType());
        assertEquals(propertyType2, properties.get(propertyName2).getType());
    }
}