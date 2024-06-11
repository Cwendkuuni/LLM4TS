package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.core.util.TestUtils.constructParser;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectIdReader;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectIdGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import org.junit.Before;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReader;
    private ObjectMapper mapper;
    private JsonDeserializer<Object> objectIdDeserializer;
    private ObjectIdGenerator objectIdGenerator;
    private DeserializationContext deserializationContext;

    @Before
    public void setup() {
        objectIdReader = mock(ObjectIdReader.class);
        objectIdDeserializer = mock(JsonDeserializer.class);
        objectIdGenerator = mock(ObjectIdGenerator.class);
        mapper = new ObjectMapper();
        deserializationContext = mock(DeserializationContext.class);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        String expectedId = "expectedId";
        Object id = new Object();
        when(objectIdDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class)))
                .thenReturn(id);
        when(objectIdReader.getIdType()).thenReturn(ObjectIdType.STRING_ID);
        when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        when(objectIdReader.generator).thenReturn(objectIdGenerator);
        when(objectIdGenerator.nextId(any(JsonParser.class))).thenReturn(expectedId);

        ObjectInstance instance = new ObjectInstance();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        property.deserializeAndSet(constructParser(mapper), deserializationContext, instance);
        assertEquals(expectedId, instance.getId());
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        String expectedId = "expectedId";
        Object id = new Object();
        when(objectIdDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class)))
                .thenReturn(id);
        when(objectIdReader.getIdType()).thenReturn(ObjectIdType.STRING_ID);
        when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        when(objectIdReader.generator).thenReturn(objectIdGenerator);
        when(objectIdGenerator.nextId(any(JsonParser.class))).thenReturn(expectedId);

        ObjectInstance instance = new ObjectInstance();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object result = property.deserializeSetAndReturn(constructParser(mapper), deserializationContext, instance);
        assertEquals(expectedId, result);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        String expectedId = "expectedId";
        Object id = new Object();
        ObjectInstance instance = new ObjectInstance();
        instance.setId(id);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object result = property.setAndReturn(instance, id);
        assertEquals(id, result);
    }

    @Test
    public void testSet_whenIdPropertyIsNull() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        ObjectInstance instance = new ObjectInstance();
        Object id = new Object();
        try {
            property.set(instance, id);
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        assertNull(property.getAnnotation(JsonId.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        assertNull(property.getMember());
    }

}

class ObjectInstance {
    private Object id;

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }
}