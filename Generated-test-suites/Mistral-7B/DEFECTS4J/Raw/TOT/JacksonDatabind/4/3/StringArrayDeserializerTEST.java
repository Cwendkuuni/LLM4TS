import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeDefinition;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class StringArrayDeserializerTest {

    private ObjectMapper objectMapper;

    @SuppressWarnings("unchecked")
    public StringArrayDeserializerTest() {
        this.objectMapper = new ObjectMapper(new JsonFactory());
    }

    @Test
    public void testDeserializeEmptyArray() throws IOException {
        String json = "[]";
        String[] expected = new String[0];
        String[] result = this.objectMapper.readValue(json, String[].class);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserializeSingleValueArray() throws IOException {
        String json = "[\"Hello\"]";
        String[] expected = new String[]{"Hello"};
        String[] result = this.objectMapper.readValue(json, String[].class);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserializeCustomElementDeserializer() throws IOException {
        JsonDeserializer<String> customDeserializer = new CustomStringDeserializer();
        StringArrayDeserializer serializer = new StringArrayDeserializer(customDeserializer);
        String json = "[\"Hello\", 1, null]";
        String[] expected = new String[]{"Hello", null};
        String[] result = this.objectMapper.readValue(json, String[].class);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserializeEmptyStringAsNull() throws IOException {
        String json = "[\"\"]";
        String[] expected = new String[0];
        String[] result = this.objectMapper.readValue(json, String[].class);
        Assert.assertArrayEquals(expected, result);

        json = "[\"\"]";
        expected = new String[]{null};
        result = this.objectMapper.readValue(json, String[].class);
        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeInvalidJson() throws IOException {
        String json = "{\"key\":\"value\"}";
        this.objectMapper.readValue(json, String[].class);
    }

    @Test(expected = IOException.class)
    public void testDeserializeNullInput() throws IOException {
        String json = null;
        this.objectMapper.readValue(json, String[].class);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        TypeDefinition typeDef = new TypeDefinition().withSimpleName("TestClass")
                .withClass(TestClass.class)
                .withProperty("stringArray", String[].class);

        String json = "[\"Hello\", \"World\"]";
        TestClass testClass = this.objectMapper.readValue(json, typeDef);
        String[] result = testClass.getStringArray();
        String[] expected = new String[]{"Hello", "World"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testCreateContextual() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper(new JsonFactory());
        JsonDeserializer<String> customDeserializer = new CustomStringDeserializer();

        StringArrayDeserializer serializer = objectMapper.findValueDeserializer(String[].class);
        JsonDeserializer<?> deser = serializer.createContextual(objectMapper.getDeserializationContext(), new StdName("name"));

        Assert.assertNotNull(deser);
        Assert.assertTrue(deser instanceof StringArrayDeserializer);

        serializer = new StringArrayDeserializer(customDeserializer);
        deser = serializer.createContextual(objectMapper.getDeserializationContext(), new StdName("name"));

        Assert.assertNotNull(deser);
        Assert.assertTrue(deser instanceof StringArrayDeserializer);
        Assert.assertEquals(customDeserializer, ((StringArrayDeserializer) deser)._elementDeserializer);
    }

    static class CustomStringDeserializer extends StdDeserializer<String> {
        @Override
        public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return jp.getValueAsString();
        }
    }

    static class TestClass {
        private String[] stringArray;

        public String[] getStringArray() {
            return stringArray;
        }
    }
}