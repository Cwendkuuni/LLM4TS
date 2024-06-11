package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class RecordTypeBuilderTest {
    @Test
    public void testRecordTypeBuilder() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);

        assertNull(recordTypeBuilder.build());
    }

    @Test
    public void testAddProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);

        String propertyName = "property1";
        JSType propertyType = new IntType();
        Node propertyNode = new Node(); // Assuming a valid Node representation

        RecordTypeBuilder builder = recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode);
        assertNotNull(builder);

        JSType recordType = builder.build();
        Map<String, RecordTypeBuilder.RecordProperty> properties =
                ((RecordType) recordType).getProperties();
        assertNotNull(properties);
        assertEquals(1, properties.size());
        RecordTypeBuilder.RecordProperty recordProperty = properties.get(propertyName);
        assertNotNull(recordProperty);
        assertEquals(propertyType, recordProperty.getType());
        assertEquals(propertyNode, recordProperty.getPropertyNode());
    }

    @Test
    public void testAddDuplicateProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);

        String propertyName = "property1";
        JSType propertyType = new IntType();
        Node propertyNode = new Node(); // Assuming a valid Node representation

        RecordTypeBuilder builder1 = recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode);
        assertNotNull(builder1);

        RecordTypeBuilder builder2 = recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode);
        assertNull(builder2);

        JSType recordType = recordTypeBuilder.build();
        Map<String, RecordTypeBuilder.RecordProperty> properties =
                ((RecordType) recordType).getProperties();
        assertEquals(1, properties.size());
    }
}