import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

    StringArrayDeserializer deserializer;

    @Before
    public void setUp() {
        deserializer = new StringArrayDeserializer();
    }

    @Test
    public void testDeserialize() throws Exception {
        String[] expected = {"value1", "value2", "value3"};
        String json = "[\"value1\",\"value2\",\"value3\"]";
        JsonParser parser = new JsonParser(json);
        DeserializationContext context = new DeserializationContext();
        String[] result = deserializer.deserialize(parser, context);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserialize_customDeserializer() throws Exception {
        String[] expected = {"value1", "value2", "value3"};
        String json = "[\"value1\",\"value2\",\"value3\"]";
        JsonParser parser = new JsonParser(json);
        DeserializationContext context = new DeserializationContext();
        StringArrayDeserializer customDeserializer = new StringArrayDeserializer();
        customDeserializer._elementDeserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getText().toUpperCase();
            }
        };
        String[] result = customDeserializer.deserialize(parser, context);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserialize_emptyArray() throws Exception {
        String[] expected = {};
        String json = "[]";
        JsonParser parser = new JsonParser(json);
        DeserializationContext context = new DeserializationContext();
        String[] result = deserializer.deserialize(parser, context);
        assertArrayEquals(expected, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_nonArray() throws Exception {
        String json = "{\"key\":\"value\"}";
        JsonParser parser = new JsonParser(json);
        DeserializationContext context = new DeserializationContext();
        deserializer.deserialize(parser, context);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_nullToken() throws Exception {
        String json = "null";
        JsonParser parser = new JsonParser(json);
        DeserializationContext context = new DeserializationContext();
        deserializer.deserialize(parser, context);
    }

    @Test
    public void testDeserialize_nullValue() throws Exception {
        String[] expected = {null};
        String json = "[null]";
        JsonParser parser = new JsonParser(json);
        DeserializationContext context = new DeserializationContext();
        String[] result = deserializer.deserialize(parser, context);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserialize_singleValue() throws Exception {
        String[] expected = {"value"};
        String json = "\"value\"";
        JsonParser parser = new JsonParser(json);
        DeserializationContext context = new DeserializationContext();
        String[] result = deserializer.deserialize(parser, context);
        assertArrayEquals(expected, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_singleValueNotAllowed() throws Exception {
        String json = "\"value\"";
        JsonParser parser = new JsonParser(json);
        DeserializationContext context = new DeserializationContext();
        context.disable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        deserializer.deserialize(parser, context);
    }

    @Test
    public void testDeserialize_emptyStringAsNull() throws Exception {
        String[] expected = {null};
        String json = "[\"\"]";
        JsonParser parser = new JsonParser(json);
        DeserializationContext context = new DeserializationContext();
        context.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        String[] result = deserializer.deserialize(parser, context);
        assertArrayEquals(expected, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_emptyStringAsNullNotAllowed() throws Exception {
        String json = "[\"\"]";
        JsonParser parser = new JsonParser(json);
        DeserializationContext context = new DeserializationContext();
        deserializer.deserialize(parser, context);
    }

    @Test
    public void testDeserializeWithTypeInfo() throws Exception {
        String[] expected = {"value1", "value2", "value3"};
        String json = "[\"value1\",\"value2\",\"value3\"]";
        JsonParser parser = new JsonParser(json);
        DeserializationContext context = new DeserializationContext();
        TypeDeserializer typeDeserializer = new TypeDeserializer();
        Object result = deserializer.deserializeWithType(parser, context, typeDeserializer);
        assertTrue(result instanceof String[]);
        String[] resultArray = (String[]) result;
        assertArrayEquals(expected, resultArray);
    }
}