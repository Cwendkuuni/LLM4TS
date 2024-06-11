import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;

public class StringArrayDeserializerTest {

    // Test deserialize method
    @Test
    public void testDeserialize() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();

        // Basic test case
        String[] expectedResult = {"value1", "value2"};
        String json = "[\"value1\",\"value2\"]";
        JsonParser parser = new JsonFactory().createParser(json);
        DeserializationContext context = new DeserializationContext(null, null);
        String[] result = deserializer.deserialize(parser, context);
        assertArrayEquals(expectedResult, result);

        // Test case with empty array
        expectedResult = new String[]{};
        json = "[]";
        parser = new JsonFactory().createParser(json);
        result = deserializer.deserialize(parser, context);
        assertArrayEquals(expectedResult, result);

        // Test case with null value
        expectedResult = new String[]{null};
        json = "[null]";
        parser = new JsonFactory().createParser(json);
        result = deserializer.deserialize(parser, context);
        assertArrayEquals(expectedResult, result);
    }

    // Test _deserializeCustom method
    @Test
    public void testDeserializeCustom() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] expectedResult = {"value1", "value2"};
        String json = "[\"value1\",\"value2\"]";
        JsonParser parser = new JsonFactory().createParser(json);
        DeserializationContext context = new DeserializationContext(null, null);
        JsonDeserializer<String> elementDeserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
                if (jsonParser.getCurrentToken() == JsonToken.VALUE_STRING) {
                    return jsonParser.getText();
                } else {
                    return null;
                }
            }
        };
        deserializer._elementDeserializer = elementDeserializer;
        String[] result = deserializer._deserializeCustom(parser, context);
        assertArrayEquals(expectedResult, result);
    }

    // Test handleNonArray method
    @Test
    public void testHandleNonArray() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] expectedResult = {"value"};
        String json = "\"value\"";
        JsonParser parser = new JsonFactory().createParser(json);
        DeserializationContext context = new DeserializationContext(null, null);
        String[] result = deserializer.handleNonArray(parser, context);
        assertArrayEquals(expectedResult, result);

        // Test case with empty string as null object
        expectedResult = null;
        json = "\"\"";
        parser = new JsonFactory().createParser(json);
        result = deserializer.handleNonArray(parser, context);
        assertArrayEquals(expectedResult, result);
    }

    // Test createContextual method
    @Test
    public void testCreateContextual() throws JsonMappingException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();

        // Test case with default String deserializer
        deserializer._elementDeserializer = null;
        DeserializationContext context = new DeserializationContext(null, null);
        BeanProperty property = null;
        JsonDeserializer<?> result = deserializer.createContextual(context, property);
        assertNull(result);

        // Test case with custom String deserializer
        JsonDeserializer<String> elementDeserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
                if (jsonParser.getCurrentToken() == JsonToken.VALUE_STRING) {
                    return jsonParser.getText();
                } else {
                    return null;
                }
            }
        };
        deserializer._elementDeserializer = elementDeserializer;
        result = deserializer.createContextual(context, property);
        assertTrue(result instanceof StringArrayDeserializer);
    }

    // Test deserializeWithType method
    @Test
    public void testDeserializeWithType() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] expectedResult = {"value1", "value2"};
        String json = "[\"value1\",\"value2\"]";
        JsonParser parser = new JsonFactory().createParser(json);
        DeserializationContext context = new DeserializationContext(null, null);
        TypeDeserializer typeDeserializer = null;
        Object result = deserializer.deserializeWithType(parser, context, typeDeserializer);
        assertEquals(expectedResult, result);
    }
}