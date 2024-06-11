package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.test.BaseTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.junit.Assert.*;

public class StdSerializerTest {

    private ObjectMapper objectMapper;

    @BeforeMethod
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testDefaultSerializer() {
        StdSerializer<String> stringSerializer = objectMapper.findValueSerializer(String.class);
        assertTrue(StdSerializer.class.isAssignableFrom(stringSerializer.getClass()));
        assertTrue(StdSerializer.class.isInstance(stringSerializer));
    }

    @Test
    public void testHandledType() {
        StdSerializer<Integer> intSerializer = new StdSerializer<Integer>(Integer.class);
        assertEquals(Integer.class, intSerializer.handledType());

        JavaType javaType = objectMapper.getTypeFactory().constructType(Integer.class);
        StdSerializer<Integer> intSerializer2 = new StdSerializer<Integer>(javaType);
        assertEquals(Integer.class, intSerializer2.handledType());

        StdSerializer<Object> objectSerializer = new StdSerializer<Object>(Object.class, false);
        assertEquals(Object.class, objectSerializer.handledType());
    }

    @Test
    public void testSerialize() throws JsonMappingException, IOException {
        StdSerializer<String> stringSerializer = objectMapper.findValueSerializer(String.class);
        String value = "test";
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), objectMapper.getSerializationConfig());
        stringSerializer.serialize(value, jsonGenerator, null);
        assertEquals("\"test\"", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        StdSerializer<String> stringSerializer = objectMapper.findValueSerializer(String.class);
        JsonNode schema = stringSerializer.getSchema(objectMapper.getSerializationConfig(), String.class);
        assertEquals("string", schema.get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        StdSerializer<String> stringSerializer = objectMapper.findValueSerializer(String.class);
        stringSerializer.acceptJsonFormatVisitor(null, String.class);
    }

    @Test
    public void testWrapAndThrow() throws IOException {
        SerializerProvider provider = null;
        StdSerializer<Object> objectSerializer = new StdSerializer<Object>(Object.class, false);
        try {
            objectSerializer.wrapAndThrow(provider, new RuntimeException("Test"), null, 0);
            fail();
        } catch (JsonMappingException e) {
            // expected
        }

        try {
            objectSerializer.wrapAndThrow(provider, new IOException("Test"), null, 0);
            fail();
        } catch (IOException e) {
            // expected
        }

        try {
            objectSerializer.wrapAndThrow(provider, new Exception("Test"), null, 0);
            fail();
        } catch (JsonMappingException e) {
            // expected
        }

        try {
            objectSerializer.wrapAndThrow(provider, null, null, 0);
            fail();
        } catch (JsonMappingException e) {
            // expected
        }

        try {
            objectSerializer.wrapAndThrow(provider, new RuntimeException("Test"), null, 0);
            fail();
        } catch (IOException e) {
            // expected
        }

        try {
            objectSerializer.wrapAndThrow(provider, new IOException("Test"), null, 0);
            fail();
        } catch (JsonMappingException e) {
            // expected
        }

        try {
            objectSerializer.wrapAndThrow(provider, new Exception("Test"), null, 0);
            fail();
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testFindConvertingContentSerializer() throws JsonMappingException {
        StdSerializer<Object> objectSerializer = new StdSerializer<Object>(Object.class, false);
        SerializerProvider provider = objectMapper.getSerializationConfig().getDefaultSerializerProvider();

        JsonSerializer<Object> existingSerializer = objectSerializer.findConvertingContentSerializer(provider, null, null);
        assertNull(existingSerializer);

        JsonSerializer<List<Integer>> listSerializer = objectMapper.findValueSerializer(List.class);
        existingSerializer = objectSerializer.findConvertingContentSerializer(provider, null, new ArrayList<>());
        assertNotNull(existingSerializer);
        assertTrue(JsonSerializer.class.isAssignableFrom(existingSerializer.getClass()));
        assertTrue(listSerializer.getClass().isInstance(existingSerializer));
    }

    @Test
    public void testFindPropertyFilter() throws JsonMappingException {
        SerializerProvider provider = objectMapper.getSerializationConfig().getDefaultSerializerProvider();

        PropertyFilter filter = StdSerializer.class.newInstance().findPropertyFilter(provider, "testFilterId", null);
        assertNull(filter);

        filter = StdSerializer.class.newInstance().findPropertyFilter(provider, "testFilterId", new Object());
        assertNull(filter);
    }
}