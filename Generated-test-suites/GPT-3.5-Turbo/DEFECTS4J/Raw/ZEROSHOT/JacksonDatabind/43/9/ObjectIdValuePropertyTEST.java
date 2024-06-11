package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;
import java.lang.annotation.Annotation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

public class ObjectIdValuePropertyTest {

    @Test
    public void testConstructorWithObjectIdReaderAndMetadata() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        Assert.assertEquals(objectIdReader.propertyName, property.getPropertyName());
        Assert.assertEquals(objectIdReader.getIdType(), property.getType());
        Assert.assertEquals(metadata, property.getMetadata());
        Assert.assertEquals(objectIdReader.getDeserializer(), property.getValueDeserializer());
        Assert.assertEquals(objectIdReader, property._objectIdReader);
    }

    @Test
    public void testConstructorWithSrcAndDeserializer() {
        ObjectIdValueProperty src = new ObjectIdValueProperty();
        JsonDeserializer<?> deser = new JsonDeserializer();
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, deser);
        Assert.assertEquals(src.getPropertyName(), property.getPropertyName());
        Assert.assertEquals(src.getType(), property.getType());
        Assert.assertEquals(src.getMetadata(), property.getMetadata());
        Assert.assertEquals(deser, property.getValueDeserializer());
        Assert.assertEquals(src._objectIdReader, property._objectIdReader);
    }

    @Test
    public void testConstructorWithSrcAndNewName() {
        ObjectIdValueProperty src = new ObjectIdValueProperty();
        PropertyName newName = new PropertyName();
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, newName);
        Assert.assertEquals(newName, property.getPropertyName());
        Assert.assertEquals(src.getType(), property.getType());
        Assert.assertEquals(src.getMetadata(), property.getMetadata());
        Assert.assertEquals(src.getValueDeserializer(), property.getValueDeserializer());
        Assert.assertEquals(src._objectIdReader, property._objectIdReader);
    }

    @Test
    public void testWithName() {
        ObjectIdValueProperty src = new ObjectIdValueProperty();
        PropertyName newName = new PropertyName();
        ObjectIdValueProperty property = src.withName(newName);
        Assert.assertEquals(newName, property.getPropertyName());
        Assert.assertEquals(src.getType(), property.getType());
        Assert.assertEquals(src.getMetadata(), property.getMetadata());
        Assert.assertEquals(src.getValueDeserializer(), property.getValueDeserializer());
        Assert.assertEquals(src._objectIdReader, property._objectIdReader);
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdValueProperty src = new ObjectIdValueProperty();
        JsonDeserializer<?> deser = new JsonDeserializer();
        ObjectIdValueProperty property = src.withValueDeserializer(deser);
        Assert.assertEquals(src.getPropertyName(), property.getPropertyName());
        Assert.assertEquals(src.getType(), property.getType());
        Assert.assertEquals(src.getMetadata(), property.getMetadata());
        Assert.assertEquals(deser, property.getValueDeserializer());
        Assert.assertEquals(src._objectIdReader, property._objectIdReader);
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty property = new ObjectIdValueProperty();
        Assert.assertNull(property.getAnnotation(Annotation.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty property = new ObjectIdValueProperty();
        Assert.assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSet() {
        ObjectIdValueProperty property = new ObjectIdValueProperty();
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        try {
            property.deserializeAndSet(p, ctxt, instance);
        } catch (IOException e){
            Assert.fail();
        }
        // Add assertions here
    }

    @Test
    public void testDeserializeSetAndReturn() {
        ObjectIdValueProperty property = new ObjectIdValueProperty();
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        try {
            Assert.assertNotNull(property.deserializeSetAndReturn(p, ctxt, instance));
        } catch (IOException e){
            Assert.fail();
        }
    }

    @Test
    public void testSet() {
        ObjectIdValueProperty property = new ObjectIdValueProperty();
        Object instance = new Object();
        Object value = new Object();
        try {
            property.set(instance, value);
        } catch (IOException e){
            Assert.fail();
        }
        // Add assertions here
    }

    @Test
    public void testSetAndReturn() {
        ObjectIdValueProperty property = new ObjectIdValueProperty();
        Object instance = new Object();
        Object value = new Object();
        try {
            Assert.assertNotNull(property.setAndReturn(instance, value));
        } catch (IOException e){
            Assert.fail();
        }
    }
}