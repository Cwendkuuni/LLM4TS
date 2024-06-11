package com.example.junit; // replace with your test package name if needed

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.junit.Assert;
import org.junit.Test;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserializeStandard() throws JsonProcessingException {
        JsonParser jp = createParser("[\"abc\", \"def\", \"ghi\"]");
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(jp, new TestContext());
        Assert.assertArrayEquals(new String[]{"abc", "def", "ghi"}, result);
    }

    @Test
    public void testDeserializeCustom() throws JsonProcessingException {
        JsonParser jp = createParser("[\"abc\", null, \"def\"]");
        StringArrayDeserializer deserializer = new StringArrayDeserializer(new StringDeserializer()); // replace with your custom deserializer
        String[] result = deserializer.deserialize(jp, new TestContext());
        Assert.assertArrayEquals(new String[]{"abc", null, "def"}, result);
    }

    @Test
    public void testDeserializeEmptyArray() throws JsonProcessingException {
        JsonParser jp = createParser("[]");
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(jp, new TestContext());
        Assert.assertNull(result);
    }

    @Test
    public void testDeserializeSingleValue() throws JsonProcessingException {
        JsonParser jp = createParser("\"abc\"");
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(jp, new TestContext());
        Assert.assertEquals("abc", result[0]);
    }

    @Test(expected = IOException.class)
    public void testDeserializeInvalidJson() throws JsonProcessingException {
        JsonParser jp = createParser("\"invalid json\"");
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        deserializer.deserialize(jp, new TestContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeNullElementDeserializer() throws JsonProcessingException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer(null);
        JsonParser jp = createParser("[\"abc\", \"def\", \"ghi\"]");
        deserializer.deserialize(jp, new TestContext());
    }

    @Test
    public void testContextualization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"name\":\"John\", \"hobbies\":[\"reading\", null, \"swimming\"]}");
        JsonParser jp = node.asParser();
        StringArrayDeserializer deserializer = new StringArrayDeserializer(new JsonDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getText().toUpperCase();
            }
        });
        String[] result = deserializer.createContextual(new TestContext(mapper), null).deserialize(jp, new TestContext());
        Assert.assertArrayEquals(new String[]{"READING", null, "SWIMMING"}, result);
    }

    private JsonParser createParser(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getFactory().createParser(json);
    }

    private static class TestContext extends DeserializationContext {
        public TestContext() {
            super(new ObjectMapper(), new TestAnnotations());
        }

        public TestContext(ObjectMapper mapper) {
            super(mapper, new TestAnnotations());
        }
    }

    private static class TestAnnotations extends Annotations {
        @Override
        public boolean hasBeanProperties() {
            return false;
        }

        @Override
        public boolean hasDeserializer() {
            return false;
        }

        @Override
        public boolean hasJsonSerializer() {
            return false;
        }

        @Override
        public boolean hasSingleValueDeserializer() {
            return false;
        }
    }
}