package com.google.javascript.rhino.jstype;

import org.junit.Assert;
import org.junit.Test;

public class RecordTypeBuilderTest {

    @Test
    public void testAddProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
        JSType type = new JSType(registry, JSTypeNative.OBJECT_TYPE);
        Node propertyNode = new Node();
        
        RecordTypeBuilder result = builder.addProperty("name", type, propertyNode);
        
        Assert.assertEquals(builder, result);
    }

    @Test
    public void testAddDuplicateProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
        JSType type = new JSType(registry, JSTypeNative.OBJECT_TYPE);
        Node propertyNode = new Node();
        
        builder.addProperty("name", type, propertyNode);
        RecordTypeBuilder result = builder.addProperty("name", type, propertyNode);
        
        Assert.assertNull(result);
    }

    @Test
    public void testBuildEmptyRecord() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);

        JSType result = builder.build();

        Assert.assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), result);
    }

    @Test
    public void testBuildNonEmptyRecord() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
        JSType type = new JSType(registry, JSTypeNative.OBJECT_TYPE);
        Node propertyNode = new Node();

        builder.addProperty("name", type, propertyNode);
        
        JSType result = builder.build();

        RecordType recordType = new RecordType(registry, Collections.unmodifiableMap(properties));
        Assert.assertEquals(recordType, result);
    }

    @Test
    public void testRecordPropertyGetType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        JSType type = new JSType(registry, JSTypeNative.OBJECT_TYPE);
        Node propertyNode = new Node();
        RecordTypeBuilder.RecordProperty recordProperty = new RecordTypeBuilder.RecordProperty(type, propertyNode);

        JSType result = recordProperty.getType();

        Assert.assertEquals(type, result);
    }

    @Test
    public void testRecordPropertyGetPropertyNode() {
        JSTypeRegistry registry = new JSTypeRegistry();
        JSType type = new JSType(registry, JSTypeNative.OBJECT_TYPE);
        Node propertyNode = new Node();
        RecordTypeBuilder.RecordProperty recordProperty = new RecordTypeBuilder.RecordProperty(type, propertyNode);

        Node result = recordProperty.getPropertyNode();

        Assert.assertEquals(propertyNode, result);
    }
}