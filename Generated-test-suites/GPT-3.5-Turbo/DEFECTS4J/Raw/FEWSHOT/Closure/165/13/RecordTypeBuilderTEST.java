package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class RecordTypeBuilderTest {
    @Test
    public void testAddProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
        
        String name = "age";
        JSType type = registry.createNumberType();
        Node propertyNode = new Node();
        
        assertNull(builder.addProperty(name, type, propertyNode));
        
        name = "name";
        type = registry.createStringType();
        propertyNode = new Node();
        
        assertEquals(builder, builder.addProperty(name, type, propertyNode));
    }

    @Test
    public void testBuild() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
        
        JSType expectedType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        JSType actualType = builder.build();
        
        assertEquals(expectedType, actualType);
        
        String name = "age";
        JSType type = registry.createNumberType();
        Node propertyNode = new Node();
        
        builder.addProperty(name, type, propertyNode);
        expectedType = new RecordType(registry, Collections.singletonMap(name, new RecordProperty(type, propertyNode)));
        actualType = builder.build();
        
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testRecordPropertyGetters() {
        JSTypeRegistry registry = new JSTypeRegistry();
        
        String name = "age";
        JSType type = registry.createNumberType();
        Node propertyNode = new Node();
        
        RecordTypeBuilder.RecordProperty recordProperty = new RecordTypeBuilder.RecordProperty(type, propertyNode);
        
        assertEquals(type, recordProperty.getType());
        assertEquals(propertyNode, recordProperty.getPropertyNode());
    }
}