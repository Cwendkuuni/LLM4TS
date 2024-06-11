package com.fasterxml.jackson.databind.deser.impl;

import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.Annotations;

public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReader;
    private PropertyMetadata metadata;
    private JsonDeserializer<?> deserializer;
    private NullValueProvider nva;

    @Before
    public void setUp() {
        // Setting up the necessary components for test cases
        objectIdReader = new ObjectIdReader(new PropertyName("id"), null, null);
        metadata = PropertyMetadata.STD_REQUIRED_OR_OPTIONAL;
        deserializer = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctx) throws IOException, JsonProcessingException {
                return null;
            }
        };
        nva = NullValueProvider.forValueNulls(null);
    }

    @After
    public void tearDown() {
        // Clean up resources used in test cases
        objectIdReader = null;
        metadata = null;
        deserializer = null;
        nva = null;
    }

    @Test
    public void testConstructor_ObjectIdReader_PropertyMetadata() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        Assert.assertEquals(objectIdReader.propertyName, property._propName);
        Assert.assertEquals(objectIdReader.getIdType(), property._type);
        Assert.assertEquals(metadata, property.getMetadata());
        Assert.assertEquals(objectIdReader.getDeserializer(), property._valueDeserializer);
        Assert.assertEquals(objectIdReader, property._objectIdReader);
    }

    @Test
    public void testConstructor_ObjectIdValueProperty_JsonDeserializer_NullValueProvider() {
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, deserializer, nva);
        Assert.assertEquals(src, property);
        Assert.assertEquals(deserializer, property._valueDeserializer);
        Assert.assertEquals(nva, property._nullProvider);
    }

    @Test
    public void testConstructor_ObjectIdValueProperty_PropertyName() {
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        PropertyName newName = new PropertyName("newId");
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, newName);
        Assert.assertEquals(src, property);
        Assert.assertEquals(newName, property._propName);
    }

    @Test
    public void testWithDeser() {
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        JsonDeserializer<?> newDeserializer = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctx) throws IOException, JsonProcessingException {
                return "testValue";
            }
        };
        ObjectIdValueProperty property = (ObjectIdValueProperty) src.withValueDeserializer(newDeserializer);
        Assert.assertEquals(src, property);
        Assert.assertEquals(newDeserializer, property._valueDeserializer);
    }

    @Test
    public void testWithNullProvider() {
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        NullValueProvider newNva = NullValueProvider.forValueNulls("testValue");
        ObjectIdValueProperty property = (ObjectIdValueProperty) src.withNullProvider(newNva);
        Assert.assertEquals(src, property);
        Assert.assertEquals(newNva, property._nullProvider);
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        Assert.assertNull(property.getAnnotation(null));
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        Assert.assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSet_JsonValueDeserializer_DeserializationContext_Object()
            throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        JsonParser p = new ObjectMapper().getFactory().createParser("{\"id\": 1}");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        Object instance = new Object();
        property.deserializeAndSet(p, ctxt, instance);
        Assert.assertNotNull(instance);
    }

    @Test(expected = JsonParseException.class)
    public void testDeserializeAndSet_JsonValueDeserializer_DeserializationContext_Object_WithNull()
            throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        JsonParser p = new ObjectMapper().getFactory().createParser("{\"id\": null}");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        Object instance = new Object();
        property.deserializeAndSet(p, ctxt, instance);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet_Object_Object_WithoutIdProperty() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        Object instance = new Object();
        property.set(instance, "testId");
    }

    @Test
    public void testSet_Object_Object_WithIdProperty() throws IOException {
        ObjectIdReader idReader = new ObjectIdReader(new PropertyName("id"), null, null);
        PropertyMetadata metadata = PropertyMetadata.STD_REQUIRED_OR_OPTIONAL;
        JsonDeserializer<?> deserializer = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
                return "testId";
            }
        };
        NullValueProvider nva = NullValueProvider.forValueNulls("testNva");
        ObjectIdValueProperty property = new ObjectIdValueProperty(idReader, metadata);
        property = (ObjectIdValueProperty) property.withValueDeserializer(deserializer).withNullProvider(nva);
        Object instance = new Object();
        Object result = property.setAndReturn(instance, "testValue");
        Assert.assertEquals("testValue", result);
    }

}