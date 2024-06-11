package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

/**
 * JUnit test file for StringArrayDeserializer class.
 */
public class StringArrayDeserializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @org.junit.Test
    public void testDeserialize() throws Exception {
        String[] expected = new String[]{"test1", "test2", "test3"};
        JsonNode jsonNode = mapper.readTree("[\"" + "test1" + "\",\"" + "test2" + "\",\"" + "test3" + "\"]");
        String[] actual = new StringArrayDeserializer().deserialize(mapper.treeToJsonParser(jsonNode), new DeserializationContext(mapper.getConfiguration()));
        assertArrayEquals(expected, actual);
    }

    @org.junit.Test
    public void testDeserializeWithCustomElementDeserializer() throws Exception {
        String[] expected = new String[]{"customTest1", "customTest2", "customTest3"};
        JsonDeserializer<String> customDeserializer = new StdDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
                return jsonParser.getText() + "_custom";
            }
        };
        StringArrayDeserializer serializerWithCustomDeserializer = new StringArrayDeserializer(customDeserializer);
        JsonNode jsonNode = mapper.readTree("[\"" + "test1" + "\",\"" + "test2" + "\",\"" + "test3" + "\"]");
        String[] actual = serializerWithCustomDeserializer.deserialize(mapper.treeToJsonParser(jsonNode), new DeserializationContext(mapper.getConfiguration()));
        assertArrayEquals(expected, actual);
    }

    @org.junit.Test
    public void testDeserializeEmptyArray() throws Exception {
        String[] expected = new String[0];
        JsonNode jsonNode = mapper.readTree("[]");
        String[] actual = new StringArrayDeserializer().deserialize(mapper.treeToJsonParser(jsonNode), new DeserializationContext(mapper.getConfiguration()));
        assertArrayEquals(expected, actual);
    }

    @org.junit.Test
    public void testDeserializeNullArray() throws Exception {
        String[] expected = null;
        JsonNode jsonNode = mapper.readTree("null");
        String[] actual = new StringArrayDeserializer().deserialize(mapper.treeToJsonParser(jsonNode), new DeserializationContext(mapper.getConfiguration()));
        assertArrayEquals(expected, actual);
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidJson() throws Exception {
        String json = "{\"invalid\": \"Json\"}";
        JsonNode jsonNode = mapper.readTree(json);
        new StringArrayDeserializer().deserialize(mapper.treeToJsonParser(jsonNode), new DeserializationContext(mapper.getConfiguration()));
    }

    @org.junit.Test
    public void testDeserializeWithType() throws Exception {
        String[] expected = new String[]{"test1", "test2", "test3"};
        JsonNode jsonNode = mapper.readTree("[\"" + "test1" + "\",\"" + "test2" + "\",\"" + "test3" + "\"]");
        TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructType(String[].class);
        String[] actual = new StringArrayDeserializer().deserializeWithType(mapper.treeToJsonParser(jsonNode), new DeserializationContext(mapper.getConfiguration()), typeDeserializer);
        assertArrayEquals(expected, actual);
    }

    @org.junit.Test(expected = IOException.class)
    public void testHandleNonArray() throws Exception {
        JsonParser jsonParser = mapper.getFactory().createParser("{\"test\": \"data\"}");
        new StringArrayDeserializer().handleNonArray(jsonParser, new DeserializationContext(mapper.getConfiguration()));
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void testCreateContextualWithNullDeserializer() throws Exception {
        DeserializationContext deserializationContext = new DeserializationContext(mapper.getConfiguration());
        assertNull(new StringArrayDeserializer().createContextual(deserializationContext, null));
    }

    @org.junit.Test
    public void testCreateContextualWithStringDeserializer() throws Exception {
        String[] expected = new String[]{"test1", "test2", "test3"};
        DeserializationContext deserializationContext = new DeserializationContext(mapper.getConfiguration());
        JsonDeserializer<String> stringDeserializer = new StdDeserializer<String>(String.class) {};
        JsonDeserializer<String[]> actual = new StringArrayDeserializer().createContextual(deserializationContext, null);
        assertEquals(StringArrayDeserializer.class, actual.getClass());
        assertEquals(String[].class, actual.getValueType());
        assertEquals(stringDeserializer, actual.getContentDeserializer());
    }
}