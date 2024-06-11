package com.example.junit.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.NamedPropertyIdentifier;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import org.junit.Before;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

    private ObjectMapper mapper;
    private ObjectIdReader objectIdReader;
    private ObjectIdValueProperty objectIdValueProperty;

    @Before
    public void setup() throws Exception {
        mapper = new ObjectMapper();

        objectIdReader = new StandardObjectIdReader();
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
    }

    // Test for constructor
    @Test
    public void testObjectIdValuePropertyConstruction() {
        assertNotNull(objectIdValueProperty);
    }

    // Test for getters
    @Test
    public void testGetPropertyName() {
        assertEquals(objectIdReader.propertyName, objectIdValueProperty.getName());
    }

    @Test
    public void testGetIdType() {
        assertEquals(objectIdReader.getIdType(), objectIdValueProperty.getIdType());
    }

    @Test
    public void testGetMetadata() {
        assertEquals(objectIdReader.getMetadata(), objectIdValueProperty.getMetadata());
    }

    @Test
    public void testGetValueDeserializer() {
        assertEquals(objectIdReader.getValueDeserializer(), objectIdValueProperty.getValueDeserializer());
    }

    @Test
    public void testGetObjectIdReader() {
        assertEquals(objectIdReader, objectIdValueProperty.getObjectIdReader());
    }

    // Test for deserializeAndSet method
    @Test
    public void testDeserializeAndSet() throws Exception {
        String json = "{\"id\": 123}";
        Object id = mapper.readValue(json, Long.class);

        JsonParser jp = mapper.getFactory().createParser(json);
        Object instance = new Object();

        objectIdValueProperty.deserializeAndSet(jp, mapper.getDeserializationContext(), instance);

        Object idProperty = mapper.findValueToAccess(instance, objectIdValueProperty.getName());
        assertEquals(id, idProperty);
    }

    // Test for deserializeSetAndReturn method
    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        String json = "{\"id\": 123}";
        Object id = mapper.readValue(json, Long.class);

        JsonParser jp = mapper.getFactory().createParser(json);

        Object instance = new Object();
        Object result = objectIdValueProperty.deserializeSetAndReturn(jp, mapper.getDeserializationContext(), instance);

        Object idProperty = mapper.findValueToAccess(instance, objectIdValueProperty.getName());
        assertEquals(id, idProperty);
        assertEquals(instance, result);
    }

    // Test for set method
    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws Exception {
        Object instance = new Object();
        objectIdValueProperty.set(instance, null);
    }

    // Test for setAndReturn method
    @Test
    public void testSetAndReturn() throws Exception {
        Object instance = new Object();
        Object id = new Object();

        Object result = objectIdValueProperty.setAndReturn(instance, id);

        Object idProperty = mapper.findValueToAccess(instance, objectIdValueProperty.getName());
        assertEquals(id, idProperty);
        assertSame(instance, result);
    }

    // Test for getAnnotation method
    @Test
    public void testGetAnnotation() {
        AnnotationIntrospector introspector = mapper.getAnnotatedClass(Object.class).getDefaultInstance();
        BeanPropertyDefinition propertyDefinition = introspect
                .findPropertyDefinition(new NamedPropertyIdentifier(objectIdReader.propertyName), true);
        Object idAnnotation = propertyDefinition.getAnnotation(ObjectId.class);

        assertEquals(idAnnotation, objectIdValueProperty.getAnnotation(ObjectId.class));
    }

    // Test for getMember method
    @Test
    public void testGetMember() {
        AnnotatedMember member = mapper.getAnnotatedClass(Object.class).findMember(objectIdReader.propertyName);
        assertEquals(member, objectIdValueProperty.getMember());
    }

    // Test for edge cases: empty JsonParser
    @Test(expected = IOException.class)
    public void testEmptyJsonParser() throws Exception {
        Object instance = new Object();
        objectIdValueProperty.deserializeAndSet(mapper.getFactory().createParser(""), mapper.getDeserializationContext(), instance);
    }

    // Test for edge cases: null JsonParser
    @Test(expected = IOException.class)
    public void testNullJsonParser() throws Exception {
        Object instance = new Object();
        objectIdValueProperty.deserializeAndSet(null, mapper.getDeserializationContext(), instance);
    }

    // Test for edge cases: null instance
    @Test(expected = IllegalArgumentException.class)
    public void testNullInstance() throws Exception {
        objectIdValueProperty.set(null, new Object());
    }

    // Test for edge cases: null value
    @Test(expected = IllegalArgumentException.class)
    public void testNullValue() throws Exception {
        Object instance = new Object();
        objectIdValueProperty.set(instance, null);
    }

    // Test for exceptions: UnsupportedOperationException
    @Test(expected = UnsupportedOperationException.class)
    public void testSetUnsupported() throws Exception {
        Object instance = new Object();
        objectIdValueProperty.set(instance, new Object());
    }
}