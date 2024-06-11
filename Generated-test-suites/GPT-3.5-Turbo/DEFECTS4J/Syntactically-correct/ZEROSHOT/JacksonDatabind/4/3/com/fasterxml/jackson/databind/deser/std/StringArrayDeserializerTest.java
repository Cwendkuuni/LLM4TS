package com.fasterxml.jackson.databind.deser.std;

import java.io.IOException;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

import org.junit.Test;
import static org.junit.Assert.*;

@JacksonStdImpl
public class StringArrayDeserializerTest {
  
    @Test
    public void testDeserialize() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jp = new JsonFactory().createParser("[\"test\", \"example\"]");
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), null);
        String[] result = deserializer.deserialize(jp, ctxt);
        String[] expected = new String[]{"test", "example"};
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testDeserializeCustom() throws IOException {
        JsonDeserializer<String> elementDeserializer = new StdDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return jp.getText().toUpperCase();
            }
        };
        StringArrayDeserializer deserializer = new StringArrayDeserializer(elementDeserializer);
        JsonParser jp = new JsonFactory().createParser("[\"test\", \"example\"]");
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), null);
        String[] result = deserializer.deserialize(jp, ctxt);
        String[] expected = new String[]{"TEST", "EXAMPLE"};
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testHandleNonArray() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jp = new JsonFactory().createParser("12");
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), null);
        String[] result = deserializer.handleNonArray(jp, ctxt);
        String[] expected = new String[]{"12"};
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testDeserializeWithType() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jp = new JsonFactory().createParser("[\"test\", \"example\"]");
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), null);
        TypeDeserializer typeDeserializer = new TypeDeserializer() {
            public Object deserializeTypedFromArray(JsonParser p, DeserializationContext ctxt) throws IOException {
                return deserializer.deserialize(p, ctxt);
            }
        };
        Object result = deserializer.deserializeWithType(jp, ctxt, typeDeserializer);
        String[] expected = new String[]{"test", "example"};
        assertArrayEquals(expected, (String[])result);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), null);
        BeanProperty property = null;
        JsonDeserializer<?> result = deserializer.createContextual(ctxt, property);
        assertNull(result);
    }
}