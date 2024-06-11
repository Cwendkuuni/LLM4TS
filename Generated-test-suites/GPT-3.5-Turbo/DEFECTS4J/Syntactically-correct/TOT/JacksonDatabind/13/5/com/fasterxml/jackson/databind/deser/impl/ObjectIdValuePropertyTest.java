package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.PropertyMetadata;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.PropertyName;

public class ObjectIdValuePropertyTest {

    private ObjectIdValueProperty objectIdValueProperty;

    @Before
    public void setUp() {
    	objectIdValueProperty = createObjectIdValueProperty();
    }

    @After
    public void tearDown() {
    	objectIdValueProperty = null;
    }

    private ObjectIdValueProperty createObjectIdValueProperty() {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, "test", null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null);
        return new ObjectIdValueProperty(objectIdReader, metadata);
    }

    // test constructor
    @Test
    public void testConstructor() {
    	ObjectIdReader objectIdReader = new ObjectIdReader(null, "test", null);
    	PropertyMetadata metadata = new PropertyMetadata(null, null, null);
    	assertEquals("test", objectIdValueProperty.getName());
    	assertEquals(objectIdReader.getIdType(), objectIdValueProperty.getType());
    	assertEquals(metadata, objectIdValueProperty.getMetadata());
    	assertEquals(objectIdReader.getDeserializer(), objectIdValueProperty.getValueDeserializer());
    	assertEquals(objectIdReader, objectIdValueProperty._objectIdReader);
    }

    // test withName
    @Test
    public void testWithName() {
    	String newName = "newName";
    	ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withName(new PropertyName(newName));
    	assertEquals(newName, newObjectIdValueProperty.getName());
    }

    // test withValueDeserializer
    @Test
    public void testWithValueDeserializer() {
    	JsonDeserializer<?> deser = new JsonDeserializer<Object>() {
			@Override
			public Object deserialize(JsonParser p, DeserializationContext ctxt)
					throws IOException, JsonParseException {
				return null;
			}
		};
    	ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withValueDeserializer(deser);
    	assertEquals(deser, newObjectIdValueProperty._valueDeserializer);
    }

    // test getAnnotation
    @Test
    public void testGetAnnotation() {
    	assertNull(objectIdValueProperty.getAnnotation(null));
    }

    // test getMember
    @Test
    public void testGetMember() {
    	assertNull(objectIdValueProperty.getMember());
    }

    // test deserializeAndSet
    @Test
    public void testDeserializeAndSet() throws IOException {
    	JsonParser jp = new ObjectMapper().getFactory().createParser("{}");
    	DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
    	Object instance = new Object();
    	objectIdValueProperty.deserializeAndSet(jp, ctxt, instance);
    	// assert deserialization and setting object id
    	// ...
    }

    // test deserializeSetAndReturn
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
    	JsonParser jp = new ObjectMapper().getFactory().createParser("{}");
    	DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
    	Object instance = new Object();
    	Object result = objectIdValueProperty.deserializeSetAndReturn(jp, ctxt, instance);
    	// assert deserialization and setting object id and return value
    	// ...
    }

    // test set
    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
    	objectIdValueProperty.set(new Object(), new Object());
    }

    // test setAndReturn
    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn() throws IOException {
    	objectIdValueProperty.setAndReturn(new Object(), new Object());
    }
}