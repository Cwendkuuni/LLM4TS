package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RecordTypeBuilderTest {

    @Test
    public void testCreateEmptyRecordType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
        JSType recordType = recordTypeBuilder.build();
        assertNotNull(recordType);
        assertTrue(recordType instanceof RecordType);
        assertTrue(((RecordType) recordType).isEmpty());
    }

    @Test
    public void testAddPropertyAndBuildRecordType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
        String propertyName = "propertyName";
        JSType propertyType = registry.getStringType();
        Node propertyNode = new Node();
        RecordTypeBuilder addedProperty = recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode);
        JSType builtRecordType = addedProperty.build();
        assertNotNull(builtRecordType);
        assertTrue(builtRecordType instanceof RecordType);
        Map<String, RecordProperty> properties = ((RecordType) builtRecordType).getProperties();
        assertTrue(properties.containsKey(propertyName));
        RecordProperty recordProperty = properties.get(propertyName);
        assertEquals(propertyType, recordProperty.getType());
        assertEquals(propertyNode, recordProperty.getPropertyNode());
    }

    @Test
    public void testAddDuplicateProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
        String propertyName = "propertyName";
        JSType propertyType = registry.getStringType();
        Node propertyNode = new Node();
        RecordTypeBuilder addedProperty = recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode);
        RecordTypeBuilder addedDuplicateProperty = recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode);
        assertNull(addedDuplicateProperty);
    }

    @Test (expected = IllegalStateException.class)
    public void testBuildRecordTypeAfterCallingBuild() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
        recordTypeBuilder.build();
        recordTypeBuilder.build();
    }
}