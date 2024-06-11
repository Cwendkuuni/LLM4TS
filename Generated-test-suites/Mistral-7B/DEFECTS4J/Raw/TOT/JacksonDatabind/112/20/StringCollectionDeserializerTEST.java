package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StringCollectionDeserializerTest {

    @Test
    public void testEmptyStringCollectionDeserialization() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);

        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser = jsonFactory.createParser("[ ]");

        List<String> result = deserializer.deserialize(parser, new TestingDeserializationContext());
        assertTrue(result.isEmpty());
    }

    @Test
    public void testStringCollectionDeserializationWithSingleValue() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);

        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser = jsonFactory.createParser("[\"hello\" ]");

        List<String> result = deserializer.deserialize(parser, new TestingDeserializationContext());
        assertEquals(1, result.size());
        assertEquals("hello", result.get(0));
    }

    @Test
    public void testStringCollectionDeserializationWithMultipleValues() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);

        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser = jsonFactory.createParser("[\"hello\", \"world\", \"!\" ]");

        List<String> result = deserializer.deserialize(parser, new TestingDeserializationContext());
        assertEquals(3, result.size());
        assertEquals("hello", result.get(0));
        assertEquals("world", result.get(1));
        assertEquals("!", result.get(2));
    }

    @Test
    public void testStringCollectionDeserializationWithNullValues() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);

        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser = jsonFactory.createParser("[null, \"world\", null ]");

        List<String> result = deserializer.deserialize(parser, new TestingDeserializationContext());
        assertEquals(3, result.size());
        assertNull(result.get(0));
        assertEquals("world", result.get(1));
        assertNull(result.get(2));
    }

    @Test
    public void testStringCollectionDeserializationWithEmptyValue() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);

        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser = jsonFactory.createParser("[\"\", \"world\" ]");

        List<String> result = deserializer.deserialize(parser, new TestingDeserializationContext());
        assertEquals(2, result.size());
        assertEquals("", result.get(0));
        assertEquals("world", result.get(1));
    }

    @Test(expected = JsonMappingException.class)
    public void testStringCollectionDeserializationWithInvalidValue() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);

        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser = jsonFactory.createParser("[1, 2]"); // expected number type instead of String type

        deserializer.deserialize(parser, new TestingDeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testStringCollectionDeserializationWithMissingStartArrayToken() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);

        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser = jsonFactory.createParser("[\"]"); // missing start array token

        deserializer.deserialize(parser, new TestingDeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testStringCollectionDeserializationWithEndArrayTokenMismatch() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);

        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser = jsonFactory.createParser("[\"hello\", \"world\" }"); // missing end array token

        deserializer.deserialize(parser, new TestingDeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testStringCollectionDeserializationWithNullDeserializer() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);

        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser = jsonFactory.createParser("[null]");

        deserializer.deserialize(parser, new TestingDeserializationContext());
        // set a null deserializer intentionally and expect JsonMappingException
        deserializer = deserializer.withResolved(null, null, null, null);
        deserializer.deserialize(parser, new TestingDeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testStringCollectionDeserializationWithInvalidDelegateDeserializer() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);

        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser = jsonFactory.createParser("[{ \"name\": \"John\" }]"); // expected Object type instead of String type

        deserializer = deserializer.withResolved(new StdSerializer<>(Object.class), null, null, null);
        deserializer.deserialize(parser, new TestingDeserializationContext());
    }

    @Test
    public void testStringCollectionDeserializationWithCustomValueDeserializer() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);

        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser = jsonFactory.createParser("[\"[1,2]\", \"[3,4]\"]");

        // custom value deserializer for handling [1,2], [3,4] format
        JsonDeserializer<String> stringListDeserializer = new StringDeserializerForList();
        deserializer = deserializer.withResolved(null, stringListDeserializer, null, null);

        List<String> result = deserializer.deserialize(parser, new TestingDeserializationContext());
        assertEquals(2, result.size());
        assertEquals("[1,2]", result.get(0));
        assertEquals("[3,4]", result.get(1));
    }

    private static class TestingDeserializationContext extends DeserializationContext {
        @Override
        public ObjectInstancifier getValueInstancifier() {
            return JacksonStdImpl.StdValueInstancifier.INSTANCE;
        }

        @Override
        public TypeDeserializer getTypeDeserializer() {
            return null;
        }

        @Override
        public NullValueProvider getNullValueProvider() {
            return null;
        }

        @Override
        public JavaType getTypeFactory() {
            return ObjectMapper.getDefaultInstance().getTypeFactory();
        }

        @Override
        public JsonFactory getFactory() {
            return new JsonFactory();
        }

        @Override
        public ObjectReader getRootValueFactory() {
            return null;
        }
    }

    private static class StringDeserializerForList extends StdDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonToken currentToken = p.getCurrentToken();
            if (currentToken.isStartArrayToken()) {
                p.nextToken(); // consume [
                String listValue = p.getValueAsString();
                p.nextToken(); // consume ]
                return listValue;
            } else {
                throw new IOException("Expected START_ARRAY_TOKEN but found " + currentToken);
            }
        }
    }
}