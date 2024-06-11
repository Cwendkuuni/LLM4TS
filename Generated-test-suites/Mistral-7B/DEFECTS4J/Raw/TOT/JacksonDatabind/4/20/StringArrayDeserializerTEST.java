package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test cases for {@link StringArrayDeserializer}.
 */
public class StringArrayDeserializerTest {

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testDeserializeEmptyArray() throws IOException {
        String[] result = new StringArrayDeserializer().deserialize(mapper.getFactory().createParser("[]"), mapper.getDeserializationContext());
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testDeserializeArrayWithElements() throws IOException {
        String[] expected = new String[]{"element1", "element2"};
        String[] result = new StringArrayDeserializer().deserialize(mapper.getFactory().createParser("[\n" +
                "  \"element1\",\n" +
                "  \"element2\"\n" +
                "]"), mapper.getDeserializationContext());
        assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserializeArrayWithNullElement() throws IOException {
        String[] result = new StringArrayDeserializer().deserialize(mapper.getFactory().createParser("[\n" +
                "  null,\n" +
                "  \"element2\"\n" +
                "]"), mapper.getDeserializationContext());
        assertArrayEquals(new String[]{null, "element2"}, result);
    }

    @Test
    public void testDeserializeArrayWithEmptyStringElement() throws IOException {
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        String[] result = new StringArrayDeserializer().deserialize(mapper.getFactory().createParser("[\n" +
                "  \"\",\n" +
                "  \"element2\"\n" +
                "]"), mapper.getDeserializationContext());
        assertArrayEquals(new String[]{null, "element2"}, result);

        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, false);
        String[] emptyStringArray = new String[0];
        String[] result2 = new StringArrayDeserializer().deserialize(mapper.getFactory().createParser("[\n" +
                "  \"\",\n" +
                "  \"element2\"\n" +
                "]"), mapper.getDeserializationContext());
        assertArrayEquals(emptyStringArray, result2);
    }

    @Test
    public void testDeserializeArrayWithNullArray() throws IOException {
        String[] result = new StringArrayDeserializer().deserialize(mapper.getFactory().createParser("null"), mapper.getDeserializationContext());
        assertNull(result);
    }

    @Test
    public void testDeserializeArrayWithInvalidJson() throws IOException {
        try {
            new StringArrayDeserializer().deserialize(mapper.getFactory().createParser("{\"array\":[]}"), mapper.getDeserializationContext());
            fail();
        } catch (JsonMappingException expected) {
            // expected
        }
    }

    @Test
    public void testDeserializeArrayWithCustomDeserializer() throws IOException {
        String[] expected = new String[]{"customElement1", "customElement2"};
        JsonDeserializer<String> customDeserializer = new StdDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return jp.getValueAsString();
            }
        };
        StringArrayDeserializer deserializerWithCustomDeserializer = new StringArrayDeserializer(customDeserializer);
        String[] result = deserializerWithCustomDeserializer.deserialize(mapper.getFactory().createParser("[\n" +
                "  \"customElement1\",\n" +
                "  \"customElement2\"\n" +
                "]"), mapper.getDeserializationContext());
        assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserializeArrayWithTypeDeserializer() throws IOException {
        TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructType(String[].class);
        String[] result = new StringArrayDeserializer().deserializeWithType(mapper.getFactory().createParser("[\n" +
                "  \"element1\",\n" +
                "  \"element2\"\n" +
                "]"), mapper.getDeserializationContext(), typeDeserializer);
        assertArrayEquals(new String[]{"element1", "element2"}, result);
    }

    @Test
    public void testCreateContextual() throws IOException {
        String[] result = new StringArrayDeserializer().createContextual(mapper.getDeserializationContext(), new StdFieldProperty(String[].class, "array"))
                .deserialize(mapper.getFactory().createParser("[\n" +
                "  \"element1\",\n" +
                "  \"element2\"\n" +
                "]"), mapper.getDeserializationContext());
        assertArrayEquals(new String[]{"element1", "element2"}, result);
    }

    @Test
    public void testHandleNonArray() throws IOException {
        String[] result = new StringArrayDeserializer().handleNonArray(mapper.getFactory().createParser("{\"array\":[\"element1\"]}"), mapper.getDeserializationContext());
        assertArrayEquals(new String[]{"element1"}, result);
    }

    /**
     * Test that contextualization works correctly.
     */
    @Test
    public void testContextualization() throws IOException, JsonMappingException {
        String[] expected = new String[]{"customElement1", "customElement2"};
        JsonDeserializer<String> customDeserializer = new StdDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return jp.getValueAsString();
            }
        };
        ObjectMapper mapperWithCustomDeserializer = new ObjectMapper();
        mapperWithCustomDeserializer.registerDeserializer(String[].class, new StringArrayDeserializer(customDeserializer));
        String[] result = mapperWithCustomDeserializer.readValue(mapperWithCustomDeserializer.getFactory().createParser("[\n" +
                "  \"customElement1\",\n" +
                "  \"customElement2\"\n" +
                "]"), String[].class);
        assertArrayEquals(expected, result);
    }
}