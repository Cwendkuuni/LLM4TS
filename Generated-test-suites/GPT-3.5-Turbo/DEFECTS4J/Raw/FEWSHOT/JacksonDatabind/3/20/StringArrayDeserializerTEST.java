package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ContextualDeserializer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

public class StringArrayDeserializerTest {
    
    private ObjectMapper objectMapper = new ObjectMapper();
    
    @Test
    public void testDeserialize() throws Exception {
        // Test string array with all non-null values
        String json = "[\"value1\",\"value2\",\"value3\"]";
        String[] expected = {"value1", "value2", "value3"};
        String[] actual = objectMapper.readValue(json, String[].class);
        assertArrayEquals(expected, actual);
        
        // Test string array with null values
        json = "[null,null,null]";
        expected = new String[3];
        actual = objectMapper.readValue(json, String[].class);
        assertArrayEquals(expected, actual);
        
        // Test empty string array
        json = "[]";
        expected = new String[0];
        actual = objectMapper.readValue(json, String[].class);
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testDeserializeWithType() throws Exception {
        // Test string array with all non-null values
        String json = "[\"value1\",\"value2\",\"value3\"]";
        JavaType javaType = objectMapper.getTypeFactory().constructArrayType(String.class);
        TypeDeserializer typeDeserializer = objectMapper.getDeserializationContext().findTypeDeserializer(javaType);
        String[] expected = {"value1", "value2", "value3"};
        Object actual = objectMapper.readValueWithType(json, typeDeserializer);
        assertTrue(actual instanceof String[]);
        assertArrayEquals(expected, (String[]) actual);
        
        // Test string array with null values
        json = "[null,null,null]";
        typeDeserializer = objectMapper.getDeserializationContext().findTypeDeserializer(javaType);
        expected = new String[3];
        actual = objectMapper.readValueWithType(json, typeDeserializer);
        assertTrue(actual instanceof String[]);
        assertArrayEquals(expected, (String[]) actual);
        
        // Test empty string array
        json = "[]";
        typeDeserializer = objectMapper.getDeserializationContext().findTypeDeserializer(javaType);
        expected = new String[0];
        actual = objectMapper.readValueWithType(json, typeDeserializer);
        assertTrue(actual instanceof String[]);
        assertArrayEquals(expected, (String[]) actual);
    }
    
    @Test
    public void testDeserializeWithContextual() throws Exception {
        // Test string array with all non-null values
        String json = "[\"value1\",\"value2\",\"value3\"]";
        String[] expected = {"value1", "value2", "value3"};
        String[] actual = objectMapper.readerFor(String[].class).readValue(json);
        assertArrayEquals(expected, actual);
        
        // Test string array with null values
        json = "[null,null,null]";
        expected = new String[3];
        actual = objectMapper.readerFor(String[].class).readValue(json);
        assertArrayEquals(expected, actual);
        
        // Test empty string array
        json = "[]";
        expected = new String[0];
        actual = objectMapper.readerFor(String[].class).readValue(json);
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testDeserializeCustom() throws Exception {
        // Test string array with all non-null values
        String json = "[\"value1\",\"value2\",\"value3\"]";
        String[] expected = {"value1", "value2", "value3"};
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        DeserializationContext deserializationContext = objectMapper.getDeserializationContext();
        String[] actual = StringArrayDeserializer.instance.deserialize(jsonParser, deserializationContext);
        assertArrayEquals(expected, actual);
        
        // Test string array with null values
        json = "[null,null,null]";
        expected = new String[3];
        jsonParser = objectMapper.getFactory().createParser(json);
        deserializationContext = objectMapper.getDeserializationContext();
        actual = StringArrayDeserializer.instance.deserialize(jsonParser, deserializationContext);
        assertArrayEquals(expected, actual);
        
        // Test empty string array
        json = "[]";
        expected = new String[0];
        jsonParser = objectMapper.getFactory().createParser(json);
        deserializationContext = objectMapper.getDeserializationContext();
        actual = StringArrayDeserializer.instance.deserialize(jsonParser, deserializationContext);
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testContextualDeserializer() throws Exception {
        // Test string array with all non-null values
        String json = "[\"value1\",\"value2\",\"value3\"]";
        String[] expected = {"value1", "value2", "value3"};
        JsonDeserializer<?> contextualDeserializer = StringArrayDeserializer.instance.createContextual(objectMapper.getDeserializationContext(), null);
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        DeserializationContext deserializationContext = objectMapper.getDeserializationContext();
        String[] actual = contextualDeserializer.deserialize(jsonParser, deserializationContext);
        assertArrayEquals(expected, actual);
        
        // Test string array with null values
        json = "[null,null,null]";
        expected = new String[3];
        contextualDeserializer = StringArrayDeserializer.instance.createContextual(objectMapper.getDeserializationContext(), null);
        jsonParser = objectMapper.getFactory().createParser(json);
        deserializationContext = objectMapper.getDeserializationContext();
        actual = contextualDeserializer.deserialize(jsonParser, deserializationContext);
        assertArrayEquals(expected, actual);
        
        // Test empty string array
        json = "[]";
        expected = new String[0];
        contextualDeserializer = StringArrayDeserializer.instance.createContextual(objectMapper.getDeserializationContext(), null);
        jsonParser = objectMapper.getFactory().createParser(json);
        deserializationContext = objectMapper.getDeserializationContext();
        actual = contextualDeserializer.deserialize(jsonParser, deserializationContext);
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testDeserializeEmptyStringAsNullObject() throws Exception {
        // Test empty string should be deserialized as null
        String json = "\"\"";
        String expected = null;
        String actual = objectMapper.readValue(json, String[].class)[0];
        assertEquals(expected, actual);
    }
}