
import org.junit.Test;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import static org.junit.Assert.*;

public class StringArrayDeserializerTest {
    @Test
    public void testDeserialize() throws JsonParseException, JsonMappingException, IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] expected = {"test1", "test2"};
        String[] result = deserializer.deserialize(createJsonParser(expected), createDeserializationContext());

        assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserializeWithNulls() throws JsonParseException, JsonMappingException, IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] expected = {"test1", null, "test2"};
        String[] result = deserializer.deserialize(createJsonParser(expected), createDeserializationContext());

        assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserializeEmptyArray() throws JsonParseException, JsonMappingException, IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] expected = {};
        String[] result = deserializer.deserialize(createJsonParser(expected), createDeserializationContext());

        assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserializeWithInvalidJson() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        try {
            deserializer.deserialize(createInvalidJsonParser(), createDeserializationContext());
            fail("Expected JsonParseException to be thrown");
        } catch (JsonParseException e) {
            // expected
        } catch (Exception e) {
            fail("Expected JsonParseException to be thrown");
        }
    }

    // Helper method to create a mock JsonParser with given values
    private JsonParser createJsonParser(String[] values) {
        return new MockJsonParser(values);
    }

    // Helper method to create a mock JsonParser with an invalid JSON
    private JsonParser createInvalidJsonParser() {
        return new MockJsonParser("invalid json");
    }

    // Helper method to create a mock DeserializationContext
    private DeserializationContext createDeserializationContext() {
        return new MockDeserializationContext();
    }

    // Mock implementation of JsonParser
    private static class MockJsonParser extends JsonParser {
        private String[] values;
        private int currentIndex;

        public MockJsonParser(String[] values) {
            this.values = values;
            this.currentIndex = 0;
        }

        @Override
        public JsonToken nextToken() throws IOException {
            if (currentIndex >= values.length) {
                return JsonToken.END_ARRAY;
            }

            return JsonToken.VALUE_STRING;
        }

        @Override
        public String getText() throws IOException {
            return values[currentIndex++];
        }

        // Other methods of JsonParser can be implemented as needed for the tests
    }

    // Mock implementation of DeserializationContext
    private static class MockDeserializationContext extends DeserializationContext {
        public MockDeserializationContext() {
            super(null, null);
        }

        // Other methods of DeserializationContext can be implemented as needed for the tests
    }
}
