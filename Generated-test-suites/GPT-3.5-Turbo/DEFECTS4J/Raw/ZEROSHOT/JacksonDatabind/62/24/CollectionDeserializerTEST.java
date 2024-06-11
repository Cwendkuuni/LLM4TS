package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.std.ContainerDeserializerBase;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class CollectionDeserializerTest {

    private CollectionDeserializer collectionDeserializer;
    
    @Before
    public void setUp() {
        JavaType collectionType = null;
        JsonDeserializer<Object> valueDeser = null;
        TypeDeserializer valueTypeDeser = null;
        ValueInstantiator valueInstantiator = null;
        JsonDeserializer<Object> delegateDeser = null;
        Boolean unwrapSingle = null;
        collectionDeserializer = new CollectionDeserializer(collectionType, valueDeser, valueTypeDeser,
                valueInstantiator, delegateDeser, unwrapSingle);
    }
    
    @Test
    public void testDeserialize() throws IOException {
        String json = "[{\"name\": \"John\"}, {\"name\": \"Jane\"}]";
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Collection<Object> result = new ArrayList<>();
        Collection<Object> deserializedCollection = collectionDeserializer.deserialize(parser, ctxt, result);
        assertEquals(2, deserializedCollection.size());
    }
    
    @Test
    public void testDeserializeWithType() throws IOException {
        String json = "[{\"name\": \"John\"}, {\"name\": \"Jane\"}]";
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        TypeDeserializer typeDeserializer = null;
        Object deserializedObject = collectionDeserializer.deserializeWithType(parser, ctxt, typeDeserializer);
        assertTrue(deserializedObject instanceof List);
    }
    
    @Test
    public void testHandleNonArray() throws IOException {
        String json = "{\"name\": \"John\"}";
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Collection<Object> result = new ArrayList<>();
        try {
            collectionDeserializer.handleNonArray(parser, ctxt, result);
        } catch (JsonMappingException e) {
            assertEquals("java.util.ArrayList", e.getMessage());
        }
    }
    
    @Test
    public void testCreateContextual() throws IOException {
        DeserializationContext ctxt = null;
        BeanProperty property = null;
        CollectionDeserializer contextualDeserializer = collectionDeserializer.createContextual(ctxt, property);
        assertNotNull(contextualDeserializer);
    }

}