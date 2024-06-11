package com.example.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactoryBuilder;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.StdConverter;
import com.fasterxml.jackson.databind.util.TestBase;
import com.fasterxml.jackson.databind.util.TypeUtil;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class StringCollectionDeserializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();
    static {
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
    }

    

    @org.junit.Test
    public void testDefaultStringCollectionDeserializer() throws IOException {
        String json = "[\"foo\", \"bar\"]";
        JsonParser parser = mapper.getFactory().createParser(json);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                TypeUtil.constructCollectionType(List.class, String.class), null, null, null, null, null);
        List<String> result = deserializer.deserialize(parser, null);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("foo", result.get(0));
        assertEquals("bar", result.get(1));
    }

    @org.junit.Test
    public void testStringCollectionDeserializerWithValueDeserializer() throws IOException {
        JsonFactory jsonFactory = new JsonFactoryBuilder().build();
        ObjectMapper mapper = new ObjectMapper(jsonFactory);
        String json = "[\"foo\", \"bar\"]";
        JsonParser parser = mapper.getFactory().createParser(json);
        StdConverter.StdConverterValueFactory<String, UpperCaseString> stringToUpperConverter =
                StdConverter.stdConverter(String.class, UpperCaseString.class);
        JsonDeserializer<String> valueDeserializer = new StdConverterDeserializer<>(stringToUpperConverter);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                TypeUtil.constructCollectionType(List.class, String.class), null, null, valueDeserializer, null, null);
        List<UpperCaseString> result = deserializer.deserialize(parser, null);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("FOO", result.get(0));
        assertEquals("BAR", result.get(1));
    }

    @org.junit.Test
    public void testStringCollectionDeserializerWithNullValueProvider() throws IOException {
        String json = "[null, null]";
        JsonParser parser = mapper.getFactory().createParser(json);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                TypeUtil.constructCollectionType(List.class, String.class), null, null, null, null, null);
        deserializer.setNullProvider(new NullValueProvider() {
            @Override
            public Object getNullValue(DeserializationContext context) {
                return null;
            }
        });
        List<String> result = deserializer.deserialize(parser, null);
        assertNull(result);
    }

    @org.junit.Test
    public void testStringCollectionDeserializerWithDelegateDeserializer() throws IOException {
        String json = "[{\"name\": \"foo\"}, {\"name\": \"bar\"}]";
        JsonParser parser = mapper.getFactory().createParser(json);
        JsonDeserializer<Object> delegateDeserializer = new StandardDeserializer<>();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                TypeUtil.constructCollectionType(List.class, String.class), null, delegateDeserializer, null, null, null);
        List<String> result = deserializer.deserialize(parser, null);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("foo", result.get(0));
        assertEquals("bar", result.get(1));
    }

    @org.junit.Test
    public void testStringCollectionDeserializerWithEmptyJson() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("[]");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                TypeUtil.constructCollectionType(List.class, String.class), null, null, null, null, null);
        List<String> result = deserializer.deserialize(parser, null);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @org.junit.Test
    public void testStringCollectionDeserializerWithNullJson() throws IOException {
        JsonParser parser = mapper.getFactory().createParser(null);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                TypeUtil.constructCollectionType(List.class, String.class), null, null, null, null, null);
        List<String> result = deserializer.deserialize(parser, null);
        assertNull(result);
    }

    @org.junit.Test
    public void testStringCollectionDeserializerWithInvalidJson() throws IOException {
        String json = "\"foo\"";
        JsonParser parser = mapper.getFactory().createParser(json);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                TypeUtil.constructCollectionType(List.class, String.class), null, null, null, null, null);
        try {
            deserializer.deserialize(parser, null);
            fail("Expected MappingException");
        } catch (MappingException e) {
            // Expected
        }
    }

    
}