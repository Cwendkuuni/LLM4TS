import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize() throws JsonProcessingException {
        // Test case 1: Basic usage
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "[\"test1\",\"test2\",\"test3\"]";
        String[] expectedResult = {"test1", "test2", "test3"};
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;

        String[] result = mapper.readerForValueType(String[].class)
                .readValue(new JsonFactory(), deserializer, jsonString);
        assertArrayEquals(expectedResult, result);
    }

    @Test
    public void testDeserializeNullJsonParser() throws JsonProcessingException {
        // Test case 2: Null input JsonParser
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser nullJsonParser = null;
        JsonFactory factory = new JsonFactory();
        try {
            deserializer.deserialize(nullJsonParser, new TestContext());
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            //Expected
        }
    }

    @Test
    public void testDeserializeEmptyJsonArray() throws JsonProcessingException {
        // Test case 3: Empty JSON array
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "[]";
        String[] expectedResult = {};
        String[] result = mapper.readerForValueType(String[].class)
                .readValue(new JsonFactory(), deserializer, jsonString);
        assertArrayEquals(expectedResult, result);
    }

    @Test
    public void testDeserializeNullValuesJsonArray() throws JsonProcessingException {
        // Test case 4: JSON array containing null values
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "[null]";
        String[] expectedResult = {null};
        String[] result = mapper.readerForValueType(String[].class)
                .readValue(new JsonFactory(), deserializer, jsonString);
        assertArrayEquals(expectedResult, result);
    }

    @Test
    public void testDeserializeNonStringValuesJsonArray() throws JsonProcessingException {
        // Test case 5: JSON array containing non-String values
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "[1, \"test\", true]";
        try {
            String[] result = mapper.readerForValueType(String[].class)
                    .readValue(new JsonFactory(), deserializer, jsonString);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            //Expected
        }
    }

    @Test
    public void testDeserializeUnexpectedEndTokenJsonArray() throws JsonProcessingException {
        // Test case 6: JSON array with an unexpected end token
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "[\"test1\",\"test2\",";
        try {
            String[] result = mapper.readerForValueType(String[].class)
                    .readValue(new JsonFactory(), deserializer, jsonString);
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            //Expected
        }
    }
}
//