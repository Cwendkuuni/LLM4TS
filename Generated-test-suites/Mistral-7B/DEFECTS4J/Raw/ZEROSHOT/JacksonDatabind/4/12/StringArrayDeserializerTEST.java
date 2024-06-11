package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link StringArrayDeserializer}
 */
@JacksonStdImpl
public class StringArrayDeserializerTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @After
    public void tearDown() {
        mapper = null;
    }

    @Test
    public void testDefaultDeserialization() throws Exception {
        String json = "[\"Hello\", \"World\" ]";
        String[] result = mapper.readValue(json, String[].class);
        assertArrayEquals(new String[]{"Hello", "World"}, result);
    }

    @Test
    public void testCustomDeserialization() throws Exception {
        String json = "[null, null]";
        JsonDeserializer<String> stringDeserializer = mapper.getFactory().createValueDeserializer(String.class, null);
        StringArrayDeserializer deserializer = new StringArrayDeserializer(stringDeserializer);
        String[] result = mapper.readerForArrayOf(String.class).with(deserializer).readValue(json);
        assertArrayEquals(new String[]{null, null}, result);
    }

    @Test
    public void testEmptyArray() throws Exception {
        String json = "[]";
        String[] result = mapper.readValue(json, String[].class);
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testSingleValue() throws Exception {
        String json = "\"Hello\"";
        String[] result = mapper.readValue(json, String[].class);
        assertEquals(new String[]{"Hello"}, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidJson() throws Exception {
        String json = "{ \"key\": [1, 2] }";
        mapper.readValue(json, String[].class);
    }

    @Test
    public void testContextualization() throws Exception {
        String json = "[null, \"Custom Value\"]";
        ObjectMapper contextualMapper = new ObjectMapper();
        JsonDeserializer<String> stringDeserializer = contextualMapper.getFactory().createValueDeserializer(String.class, null);
        StringArrayDeserializer deserializer = new StringArrayDeserializer(stringDeserializer);
        JsonDeserializer<?> contextualDeserializer = contextualMapper.readerForArrayOf(String.class)
                .with(deserializer)
                .andReturning(mapper.getFactory().createContextualValueDeserializer(String[].class, null))
                .readValue(json);
        String[] result = contextualDeserializer.getValue();
        assertArrayEquals(new String[]{null, "Custom Value"}, result);
    }
}