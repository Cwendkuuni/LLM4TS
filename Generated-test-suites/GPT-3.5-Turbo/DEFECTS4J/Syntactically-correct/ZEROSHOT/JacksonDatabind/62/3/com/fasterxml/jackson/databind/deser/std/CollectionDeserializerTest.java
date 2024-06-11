package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.CollectionDeserializer.*;
import com.fasterxml.jackson.core.*;

public class CollectionDeserializerTest {
    
    @Test
    public void testDeserialize() throws Exception {
        CollectionDeserializer deserializer = new CollectionDeserializer(null, null, null, null);
        JsonParser parser = new JsonFactory().createParser("[]");
        DeserializationContext context = new DeserializationContext(null, null, null);
        Collection<Object> result = deserializer.deserialize(parser, context);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testDeserializeWithType() throws Exception {
        CollectionDeserializer deserializer = new CollectionDeserializer(null, null, null, null);
        JsonParser parser = new JsonFactory().createParser("[1, 2, 3]");
        DeserializationContext context = new DeserializationContext(null, null, null);
        Object result = deserializer.deserializeWithType(parser, context, null);
        assertNotNull(result);
    }
    
    @Test(expected = JsonMappingException.class)
    public void testHandleNonArray() throws Exception {
        CollectionDeserializer deserializer = new CollectionDeserializer(null, null, null, null);
        JsonParser parser = new JsonFactory().createParser("123");
        DeserializationContext context = new DeserializationContext(null, null, null);
        Collection<Object> result = deserializer.handleNonArray(parser, context, new ArrayList<>());
    }
    
    @Test
    public void testCreateContextual() throws Exception {
        CollectionDeserializer deserializer = new CollectionDeserializer(null, null, null, null);
        DeserializationContext context = new DeserializationContext(null, null, null);
        BeanProperty property = null;
        CollectionDeserializer result = deserializer.createContextual(context, property);
        assertNotNull(result);
    }
}