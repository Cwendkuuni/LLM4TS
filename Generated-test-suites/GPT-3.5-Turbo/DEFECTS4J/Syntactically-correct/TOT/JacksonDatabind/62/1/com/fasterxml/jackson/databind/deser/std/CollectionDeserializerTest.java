
package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollectionDeserializerTest {

    // Test case for deserialize(JsonParser p, DeserializationContext ctxt)
    @Test
    public void testDeserialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("[1, 2, 3]");
        
        CollectionDeserializer deserializer = new CollectionDeserializer(
                Collection.class, null, null, null);
        
        Collection<Object> result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        
        List<Object> expected = new ArrayList<>();
        expected.add(1);
        expected.add(2);
        expected.add(3);
        
        Assert.assertEquals(expected, result);
    }

    // Test case for deserialize(JsonParser p, DeserializationContext ctxt, Collection<Object> result)
    @Test
    public void testDeserializeWithResult() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("[4, 5, 6]");
        
        CollectionDeserializer deserializer = new CollectionDeserializer(
                Collection.class, null, null, null);
        
        Collection<Object> result = new ArrayList<>();
        deserializer.deserialize(parser, mapper.getDeserializationContext(), result);
        
        List<Object> expected = new ArrayList<>();
        expected.add(4);
        expected.add(5);
        expected.add(6);
        
        Assert.assertEquals(expected, result);
    }

    // Test case for deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
    @Test
    public void testDeserializeWithType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("[7, 8, 9]");
        
        CollectionDeserializer deserializer = new CollectionDeserializer(
                Collection.class, null, null, null);
        
        Object result = deserializer.deserializeWithType(parser, mapper.getDeserializationContext(), null);
        
        Assert.assertTrue(result instanceof Collection);
        
        List<Object> expected = new ArrayList<>();
        expected.add(7);
        expected.add(8);
        expected.add(9);
        
        Assert.assertEquals(expected, result);
    }
}
