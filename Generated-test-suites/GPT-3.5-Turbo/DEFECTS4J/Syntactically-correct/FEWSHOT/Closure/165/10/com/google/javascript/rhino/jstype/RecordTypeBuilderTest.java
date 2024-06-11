package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class RecordTypeBuilderTest {

    @Test
    public void testAddProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
        JSType type = new JSType(registry);

        Node propertyNode = new Node();
        assertNull(builder.addProperty("name", type, propertyNode));

        JSType recordType = builder.build();
        assertEquals(JSTypeNative.OBJECT_TYPE, recordType);
    }

    @Test
    public void testBuild_emptyRecord() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
        JSType recordType = builder.build();

        assertEquals(JSTypeNative.OBJECT_TYPE, recordType);
    }

    @Test
    public void testBuild_nonEmptyRecord() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
        JSType type = new JSType(registry);

        Node propertyNode1 = new Node();
        Node propertyNode2 = new Node();
        builder.addProperty("name1", type, propertyNode1);
        builder.addProperty("name2", type, propertyNode2);

        JSType recordType = builder.build();
        assertEquals(2, recordType.getPropertyCount());
        assertEquals(type, recordType.getPropertyType("name1"));
        assertEquals(type, recordType.getPropertyType("name2"));
    }
}