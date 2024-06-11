package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.DeserializationContext;

public class StringCollectionDeserializerTest {
  
    @Test
    public void testDeserialize() throws IOException {
        String json = "[\"foo\", \"bar\", \"baz\"]";
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser = jsonFactory.createParser(json);
        DeserializationContext context = new DeserializationContext((ObjectMapper) null, new DefaultDeserializationContext.Impl(jsonFactory));

        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        Collection<String> result = deserializer.deserialize(parser, context);

        assertNotNull(result);
        assertEquals(3, result.size());

        int index = 0;
        for (String value : result) {
            switch (index) {
                case 0:
                    assertEquals("foo", value);
                    break;
                case 1:
                    assertEquals("bar", value);
                    break;
                case 2:
                    assertEquals("baz", value);
                    break;
            }
            index++;
        }
    }
    
    @Test
    public void testDeserializeEmpty() throws IOException {
        String json = "[]";
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser = jsonFactory.createParser(json);
        DeserializationContext context = new DeserializationContext((ObjectMapper) null, new DefaultDeserializationContext.Impl(jsonFactory));

        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        Collection<String> result = deserializer.deserialize(parser, context);

        assertNotNull(result);
        assertEquals(0, result.size());
    }
    
    @Test
    public void testDeserializeNull() throws IOException {
        String json = "null";
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser = jsonFactory.createParser(json);
        DeserializationContext context = new DeserializationContext((ObjectMapper) null, new DefaultDeserializationContext.Impl(jsonFactory));

        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        Collection<String> result = deserializer.deserialize(parser, context);

        assertNull(result);
    }
    
    @Test
    public void testDeserializeWithCustomDeserializer() throws IOException {
        String json = "[\"1\", \"2\", \"3\"]";
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser = jsonFactory.createParser(json);
        DeserializationContext context = new DeserializationContext((ObjectMapper) null, new DefaultDeserializationContext.Impl(jsonFactory));

        JsonDeserializer<String> customDeserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getValueAsString() + "-custom";
            }
        };
        
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, customDeserializer, null);
        Collection<String> result = deserializer.deserialize(parser, context);

        assertNotNull(result);
        assertEquals(3, result.size());
        
        int index = 1;
        for (String value : result) {
            assertEquals(index + "-custom", value);
            index++;
        }
    }
    
    @Test
    public void testDeserializeWithNullValueProvider() throws IOException {
        String json = "[\"foo\", null, \"bar\"]";
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser = jsonFactory.createParser(json);
        DeserializationContext context = new DeserializationContext((ObjectMapper) null, new DefaultDeserializationContext.Impl(jsonFactory));

        NullValueProvider nullValueProvider = new NullValueProvider() {
            @Override
            public Object getNullValue(DeserializationContext ctxt) throws JsonMappingException {
                return "null-value";
            }
        };
        
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, nullValueProvider);
        Collection<String> result = deserializer.deserialize(parser, context);

        assertNotNull(result);
        assertEquals(3, result.size());
        
        int index = 0;
        for (String value : result) {
            switch (index) {
                case 0:
                    assertEquals("foo", value);
                    break;
                case 1:
                    assertEquals("null-value", value);
                    break;
                case 2:
                    assertEquals("bar", value);
                    break;
            }
            index++;
        }
    }
    
}