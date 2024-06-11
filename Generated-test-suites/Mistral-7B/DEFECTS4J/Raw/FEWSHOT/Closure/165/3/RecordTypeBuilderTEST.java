package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

public class RecordTypeBuilderTest {

    @Test
    public void testEmptyRecord() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
        JSType recordType = builder.build();
        assertTrue(recordType instanceof JSTypeNative);
        assertTrue(recordType.getClassName().equals("Object"));
    }

    @Test
    public void testRecordWithOneProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
        JSType stringType = registry.getStringType();
        Node propertyNode = new Node(); // Dummy node
        builder.addProperty("name", stringType, propertyNode);
        JSType recordType = builder.build();
        RecordType expectedRecordType = new RecordType(registry, Maps.newHashMap());
        expectedRecordType.putProperty("name", stringType);
        assertTrue(recordType.getClass().equals(RecordType.class));
        assertEquals(expectedRecordType.getPropertyNames().size(), recordType.getPropertyNames().size());
        for (String name : expectedRecordType.getPropertyNames()) {
            JSType expectedPropertyType = expectedRecordType.getProperty(name);
            JSType actualPropertyType = recordType.getProperty(name);
            assertEquals(expectedPropertyType, actualPropertyType);
        }
    }

    @Test
    public void testRecordWithDuplicateProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
        JSType stringType = registry.getStringType();
        Node propertyNode = new Node(); // Dummy node
        builder.addProperty("name", stringType, propertyNode);
        builder.addProperty("name", stringType, propertyNode); // Duplicate
        assertNull(builder.build());
    }

    @Test
    public void testRecordWithMultipleProperties() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
        JSType stringType = registry.getStringType();
        JSType numberType = registry.getNumberType();
        Node propertyNode1 = new Node(); // Dummy node for string property
        Node propertyNode2 = new Node(); // Dummy node for number property

        builder.addProperty("name", stringType, propertyNode1);
        builder.addProperty("age", numberType, propertyNode2);

        JSType recordType = builder.build();
        RecordType expectedRecordType = new RecordType(registry, Maps.newHashMap());
        expectedRecordType.putProperty("name", stringType);
        expectedRecordType.putProperty("age", numberType);

        assertTrue(recordType.getClass().equals(RecordType.class));
        assertEquals(expectedRecordType.getPropertyNames().size(), recordType.getPropertyNames().size());
        for (String name : expectedRecordType.getPropertyNames()) {
            JSType expectedPropertyType = expectedRecordType.getProperty(name);
            JSType actualPropertyType = recordType.getProperty(name);
            assertEquals(expectedPropertyType, actualPropertyType);
        }
    }
}