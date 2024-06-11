package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class RecordTypeBuilderTest {

    @Test
    public void testAddProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);

        // Test adding a property successfully
        String propertyName = "prop";
        JSType propertyType = registry.getNativeType(JSTypeNative.STRING_TYPE);
        Node propertyNode = new Node();
        RecordTypeBuilder returnedBuilder = builder.addProperty(propertyName, propertyType, propertyNode);

        assertNotNull(returnedBuilder);
        assertEquals(propertyType, builder.build().findPropertyType(propertyName));

        // Test adding a duplicate property
        assertNull(builder.addProperty(propertyName, propertyType, propertyNode));
    }

    @Test
    public void testBuild() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder emptyBuilder = new RecordTypeBuilder(registry);
        RecordTypeBuilder nonEmptyBuilder = new RecordTypeBuilder(registry);

        // Test building an empty record
        JSType emptyRecord = emptyBuilder.build();
        assertEquals(JSTypeNative.OBJECT_TYPE, emptyRecord);

        // Test building a non-empty record
        nonEmptyBuilder.addProperty("prop1", registry.getNativeType(JSTypeNative.STRING_TYPE), new Node());
        nonEmptyBuilder.addProperty("prop2", registry.getNativeType(JSTypeNative.NUMBER_TYPE), new Node());
        JSType nonEmptyRecord = nonEmptyBuilder.build();
        assertNotNull(nonEmptyRecord.findPropertyType("prop1"));
        assertNotNull(nonEmptyRecord.findPropertyType("prop2"));
    }
}