package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.PropertyMetadata;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;

import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

    private ObjectIdValueProperty property;
    private ObjectIdReader objectIdReader;

    @Before
    public void setUp() {
        objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        property = new ObjectIdValueProperty(objectIdReader, metadata);
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nva = new NullValueProvider();
        SettableBeanProperty result = property.withNullProvider(nva);
        assertNotNull(result);
        assertEquals(nva, result._nullProvider);
    }

    @Test
    public void testWithName() {
        PropertyName newName = new PropertyName("newName");
        SettableBeanProperty result = property.withName(newName);
        assertNotNull(result);
        assertEquals(newName, result._propertyName);
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<?> deser = mapper.getDeserializerProvider().findDefaultDeserializer(property.getType(), property);
        SettableBeanProperty result = property.withValueDeserializer(deser);
        assertNotNull(result);
        assertEquals(deser, result._valueDeserializer);
    }

    @Test
    public void testDeserializeSetAndReturn() {
        // TODO: implement test
    }

    @Test
    public void testSetAndReturn() {
        // TODO: implement test
    }

    @Test
    public void testDeserializeAndSet() {
        // TODO: implement test
    }

    @Test
    public void testGetMember() {
        // TODO: implement test
    }

    @Test
    public void testGetAnnotation() {
        // TODO: implement test
    }
}