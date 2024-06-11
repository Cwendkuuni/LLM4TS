import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import org.junit.Assert;
import org.junit.Test;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserializeStandard() throws Exception {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser jsonParser = createJsonParser("[\"abc\", \"def\"]");
        String[] result = deserializer.deserialize(jsonParser, new TestContext());
        Assert.assertEquals(new String[]{"abc", "def"}, result);
    }

    @Test
    public void testDeserializeCustom() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer(new StdDeserializer.StdSerializer<String>(String.class));
        JsonParser jsonParser = createJsonParser("[\"abc\", \"def\"]");
        String[] result = deserializer.deserialize(jsonParser, new TestContext());
        Assert.assertEquals(new String[]{"abc", "def"}, result);
    }

    @Test
    public void testDeserializeWithNull() throws Exception {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser jsonParser = createJsonParser("[null]");
        String[] result = deserializer.deserialize(jsonParser, new TestContext());
        Assert.assertNull(result);
    }

    @Test
    public void testDeserializeWithEmptyArray() throws Exception {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser jsonParser = createJsonParser("[]");
        String[] result = deserializer.deserialize(jsonParser, new TestContext());
        Assert.assertArrayEquals(new String[0], result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeNonArray() throws Exception {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser jsonParser = createJsonParser("{\"name\": \"John\"}");
        deserializer.deserialize(jsonParser, new TestContext());
    }

    @Test
    public void testDeserializeWithCustomDeserializer() throws Exception {
        JsonDeserializer<String> elementDeserializer = new StdDeserializer.StdSerializer<String>(String.class);
        StringArrayDeserializer deserializer = new StringArrayDeserializer(elementDeserializer);
        JsonParser jsonParser = createJsonParser("[\"abc\", \"def\"]");
        String[] result = deserializer.deserialize(jsonParser, new TestContext());
        Assert.assertEquals(new String[]{"abc", "def"}, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithInvalidJson() throws Exception {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser jsonParser = createJsonParser("{invalid_json}");
        deserializer.deserialize(jsonParser, new TestContext());
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNullElementDeserializer() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer(null);
        JsonParser jsonParser = createJsonParser("[\"abc\", \"def\"]");
        deserializer.deserialize(jsonParser, new TestContext());
    }

    private JsonParser createJsonParser(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes()));
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public ObjectBuilder createObjectBuilder() {
            return null;
        }

        @Override
        public ObjectFactory getFactory() {
            return null;
        }

        @Override
        public JavaType getTypeFactory() {
            return null;
        }

        @Override
        public ObjectReader getValueIntrinsicDeserializer(Class<?> valueType) {
            return null;
        }

        @Override
        public ObjectReader getValueDeserializer(Class<?> valueType) {
            return null;
        }

        @Override
        public ObjectReader getRootValueDeserializer() {
            return null;
        }

        @Override
        public void failContextualization(Class<?> propertyType, JsonDeserializer<?> deserializer) throws JsonMappingException {
        }

        @Override
        public void reportError(JsonParser p, String msg) throws IOException {
            throw new IOException(msg);
        }
    }
}