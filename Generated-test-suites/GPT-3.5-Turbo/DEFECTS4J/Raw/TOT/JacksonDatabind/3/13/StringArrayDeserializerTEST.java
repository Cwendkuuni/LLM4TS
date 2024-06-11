import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;

public class StringArrayDeserializerTest {

    private StringArrayDeserializer deserializer;

    @Before
    public void setUp() {
        deserializer = new StringArrayDeserializer();
    }

    @Test
    public void testDeserialize_Success() throws JsonParseException, IOException {
        String json = "[\"value1\", \"value2\", \"value3\"]";
        JsonParser parser = new ObjectMapper().getFactory().createParser(json);
        DeserializationContext context = new ObjectMapper().getDeserializationContext();
        
        String[] result = deserializer.deserialize(parser, context);
        
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals("value1", result[0]);
        assertEquals("value2", result[1]);
        assertEquals("value3", result[2]);
    }

    @Test
    public void testDeserialize_NullValue() throws JsonParseException, IOException {
        String json = "[\"value1\", null, \"value3\"]";
        JsonParser parser = new ObjectMapper().getFactory().createParser(json);
        DeserializationContext context = new ObjectMapper().getDeserializationContext();
        
        String[] result = deserializer.deserialize(parser, context);
        
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals("value1", result[0]);
        assertNull(result[1]);
        assertEquals("value3", result[2]);
    }

    @Test
    public void testDeserialize_EmptyArray() throws JsonParseException, IOException {
        String json = "[]";
        JsonParser parser = new ObjectMapper().getFactory().createParser(json);
        DeserializationContext context = new ObjectMapper().getDeserializationContext();
        
        String[] result = deserializer.deserialize(parser, context);
        
        assertNotNull(result);
        assertEquals(0, result.length);
        assertEquals(0, result.length);
    }

    @Test
    public void testDeserialize_InvalidArray() throws JsonParseException, IOException {
        String json = "{\"key\": \"value\"}";
        JsonParser parser = new ObjectMapper().getFactory().createParser(json);
        DeserializationContext context = new ObjectMapper().getDeserializationContext();
        
        try {
            deserializer.deserialize(parser, context);
            fail("Expected JsonParseException");
        } catch (JsonParseException e) {
            // Exception expected
        }
    }

    @Test
    public void testDeserialize_NestedArray() throws JsonParseException, IOException {
        String json = "[\"value1\", [\"nested1\", \"nested2\"], \"value3\"]";
        JsonParser parser = new ObjectMapper().getFactory().createParser(json);
        DeserializationContext context = new ObjectMapper().getDeserializationContext();
        
        String[] result = deserializer.deserialize(parser, context);
        
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals("value1", result[0]);
        assertEquals("[\"nested1\", \"nested2\"]", result[1]);
        assertEquals("value3", result[2]);
    }

    @Test
    public void testDeserialize_InvalidJson() throws JsonParseException, IOException {
        String json = "[\"value1\", \"value2\", \"value3\"";
        JsonParser parser = new ObjectMapper().getFactory().createParser(json);
        DeserializationContext context = new ObjectMapper().getDeserializationContext();
        
        try {
            deserializer.deserialize(parser, context);
            fail("Expected JsonParseException");
        } catch (JsonParseException e) {
            // Exception expected
        }
    }

    @Test
    public void testDeserialize_UnexpectedToken() throws JsonParseException, IOException {
        String json = "{\"key\": \"value\"}";
        JsonParser parser = new ObjectMapper().getFactory().createParser(json);
        DeserializationContext context = new ObjectMapper().getDeserializationContext();
        
        try {
            deserializer.deserialize(parser, context);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Exception expected
        }
    }

    @Test
    public void testDeserializeWithType_Success() throws JsonParseException, IOException {
        String json = "[\"value1\", \"value2\", \"value3\"]";
        JsonParser parser = new ObjectMapper().getFactory().createParser(json);
        DeserializationContext context = new ObjectMapper().getDeserializationContext();
        
        Object result = deserializer.deserializeWithType(parser, context, null);
        
        assertNotNull(result);
        assertTrue(result instanceof String[]);
        String[] arrayResult = (String[]) result;
        assertEquals(3, arrayResult.length);
        assertEquals("value1", arrayResult[0]);
        assertEquals("value2", arrayResult[1]);
        assertEquals("value3", arrayResult[2]);
    }

    @Test
    public void testDeserializeWithType_InvalidJson() throws JsonParseException, IOException {
        String json = "[\"value1\", \"value2\", \"value3\"";
        JsonParser parser = new ObjectMapper().getFactory().createParser(json);
        DeserializationContext context = new ObjectMapper().getDeserializationContext();
        
        try {
            deserializer.deserializeWithType(parser, context, null);
            fail("Expected JsonParseException");
        } catch (JsonParseException e) {
            // Exception expected
        }
    }

    @Test
    public void testDeserializeWithType_UnexpectedToken() throws JsonParseException, IOException {
        String json = "{\"key\": \"value\"}";
        JsonParser parser = new ObjectMapper().getFactory().createParser(json);
        DeserializationContext context = new ObjectMapper().getDeserializationContext();
        
        try {
            deserializer.deserializeWithType(parser, context, null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Exception expected
        }
    }

}