package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class RecordTypeBuilderTest {
    @Test
    public void testAddProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
        JSType type1 = new JSType();
        Node propertyNode1 = new Node();
        JSType type2 = new JSType();
        Node propertyNode2 = new Node();
        
        assertNull(builder.addProperty("name1", type1, propertyNode1));
        assertNull(builder.addProperty("name1", type2, propertyNode2));
        assertEquals(builder, builder.addProperty("name2", type2, propertyNode2));
    }

    @Test
    public void testBuild() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
        
        JSType expectedType1 = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        assertEquals(expectedType1, builder.build());
        
        JSType type = new JSType();
        Node propertyNode = new Node();
        builder.addProperty("name", type, propertyNode);
        
        HashMap<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        properties.put("name", new RecordTypeBuilder.RecordProperty(type, propertyNode));
        JSType expectedType2 = new RecordType(registry, Collections.unmodifiableMap(properties));
        assertEquals(expectedType2, builder.build());
    }
}