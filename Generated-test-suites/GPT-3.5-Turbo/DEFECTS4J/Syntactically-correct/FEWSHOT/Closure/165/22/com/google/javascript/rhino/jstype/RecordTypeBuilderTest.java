package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class RecordTypeBuilderTest {

    @Test
    public void testAddProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);

        JSType stringType = new JSType();
        Node propertyNode = new Node();

        assertNull(builder.addProperty("name", stringType, propertyNode));
        assertNull(builder.addProperty("age", stringType, propertyNode));

        // Testing case where there is a duplicate property
        assertNull(builder.addProperty("name", stringType, propertyNode));

        builder.addProperty("gender", stringType, propertyNode);

        JSType recordType = builder.build();
        assertEquals(recordType.getPropertyType("name"), stringType);
        assertEquals(recordType.getPropertyType("age"), stringType);
        assertEquals(recordType.getPropertyType("gender"), stringType);
    }

    @Test
    public void testBuild() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
        
        JSType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        JSType recordType = builder.build();

        // Testing case where the record is empty
        assertEquals(recordType, objectType);
    }

    @Test
    public void testRecordProperty() {
        JSType stringType = new JSType();
        Node propertyNode = new Node();

        RecordTypeBuilder.RecordProperty recordProperty = new RecordTypeBuilder.RecordProperty(stringType, propertyNode);
        assertEquals(recordProperty.getType(), stringType);
        assertEquals(recordProperty.getPropertyNode(), propertyNode);
    }
}