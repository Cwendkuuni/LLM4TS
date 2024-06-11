import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StringArrayDeserializerTest {

    private StringArrayDeserializer stringArrayDeserializer;
    private JsonFactory jsonFactory;
    private DeserializationContext deserializationContext;
    private JsonDeserializer<?> elementDeserializer;
    private ObjectMapper objectMapper;
    
    @Before
    public void setUp() {
        stringArrayDeserializer = StringArrayDeserializer.instance;
        jsonFactory = new JsonFactory();
        elementDeserializer = null;
        objectMapper = new ObjectMapper();
    }
    
    // Test for deserialize method
    // Test case: Typical use case with array of strings
    @Test
    public void testDeserialize_Typical() throws JsonParseException, Exception {
        // Input
        String inputJson = "[\"hello\", \"world\", \"test\"]";
        
        // Expected output
        String[] expectedOutput = {"hello", "world", "test"};
        
        // Actual output
        String[] actualOutput = stringArrayDeserializer.deserialize(jsonFactory.createParser(inputJson), deserializationContext);
        
        // Assertion
        assertArrayEquals(expectedOutput, actualOutput);
    }
    
    // Test for deserialize method
    // Test case: Array with null values
    @Test
    public void testDeserialize_NullValues() throws JsonParseException, Exception {
        // Input
        String inputJson = "[null, null, null]";
        
        // Expected output
        String[] expectedOutput = {null, null, null};
        
        // Actual output
        String[] actualOutput = stringArrayDeserializer.deserialize(jsonFactory.createParser(inputJson), deserializationContext);
        
        // Assertion
        assertArrayEquals(expectedOutput, actualOutput);
    }
    
    // Test for deserialize method
    // Test case: Array with empty elements and null elements
    @Test
    public void testDeserialize_EmptyAndNullElements() throws JsonParseException, Exception {
        // Input
        String inputJson = "[\"\", null, \"test\"]";
        
        // Expected output
        String[] expectedOutput = {"", null, "test"};
        
        // Actual output
        String[] actualOutput = stringArrayDeserializer.deserialize(jsonFactory.createParser(inputJson), deserializationContext);
        
        // Assertion
        assertArrayEquals(expectedOutput, actualOutput);
    }
    
    // Test for deserialize method
    // Test case: Empty array
    @Test
    public void testDeserialize_EmptyArray() throws JsonParseException, Exception {
        // Input
        String inputJson = "[]";
        
        // Expected output
        String[] expectedOutput = {};
        
        // Actual output
        String[] actualOutput = stringArrayDeserializer.deserialize(jsonFactory.createParser(inputJson), deserializationContext);
        
        // Assertion
        assertArrayEquals(expectedOutput, actualOutput);
    }
    
    // Test for deserialize method
    // Test case: Array with invalid input
    @Test(expected = JsonParseException.class)
    public void testDeserialize_InvalidInput() throws JsonParseException, Exception {
        // Input
        String inputJson = "[\"hello\", 123, \"test\"]";
        
        // Actual output
        stringArrayDeserializer.deserialize(jsonFactory.createParser(inputJson), deserializationContext);
    }
    
    // Test for _deserializeCustom method
    // Test case: Typical use case with array of strings
    @Test
    public void testDeserializeCustom_Typical() throws JsonParseException, Exception {
        // Input
        String inputJson = "[\"hello\", \"world\", \"test\"]";
        
        // Expected output
        String[] expectedOutput = {"hello", "world", "test"};
        
        // Actual output
        String[] actualOutput = stringArrayDeserializer.deserialize(jsonFactory.createParser(inputJson), deserializationContext);
        
        // Assertion
        assertArrayEquals(expectedOutput, actualOutput);
    }
    
    // Test for _deserializeCustom method
    // Test case: Array with null values
    @Test
    public void testDeserializeCustom_NullValues() throws JsonParseException, Exception {
        // Input
        String inputJson = "[null, null, null]";
        
        // Expected output
        String[] expectedOutput = {null, null, null};
        
        // Actual output
        String[] actualOutput = stringArrayDeserializer.deserialize(jsonFactory.createParser(inputJson), deserializationContext);
        
        // Assertion
        assertArrayEquals(expectedOutput, actualOutput);
    }
    
    // Test for _deserializeCustom method
    // Test case: Array with empty elements and null elements
    @Test
    public void testDeserializeCustom_EmptyAndNullElements() throws JsonParseException, Exception {
        // Input
        String inputJson = "[\"\", null, \"test\"]";
        
        // Expected output
        String[] expectedOutput = {"", null, "test"};
        
        // Actual output
        String[] actualOutput = stringArrayDeserializer.deserialize(jsonFactory.createParser(inputJson), deserializationContext);
        
        // Assertion
        assertArrayEquals(expectedOutput, actualOutput);
    }
    
    // Test for _deserializeCustom method
    // Test case: Empty array
    @Test
    public void testDeserializeCustom_EmptyArray() throws JsonParseException, Exception {
        // Input
        String inputJson = "[]";
        
        // Expected output
        String[] expectedOutput = {};
        
        // Actual output
        String[] actualOutput = stringArrayDeserializer.deserialize(jsonFactory.createParser(inputJson), deserializationContext);
        
        // Assertion
        assertArrayEquals(expectedOutput, actualOutput);
    }
    
    // Test for _deserializeCustom method
    // Test case: Array with invalid input
    @Test(expected = JsonParseException.class)
    public void testDeserializeCustom_InvalidInput() throws JsonParseException, Exception {
        // Input
        String inputJson = "[\"hello\", 123, \"test\"]";
        
        // Actual output
        stringArrayDeserializer.deserialize(jsonFactory.createParser(inputJson), deserializationContext);
    }
    
    // Test for deserializeWithType method
    // Test case: Typical use case with array of strings with type deserializer
    @Test
    public void testDeserializeWithType_Typical() throws JsonParseException, Exception {
        // Input
        String inputJson = "[\"hello\", \"world\", \"test\"]";
        
        // Expected output
        String[] expectedOutput = {"hello", "world", "test"};
        
        // Actual output
        String[] actualOutput = (String[]) stringArrayDeserializer.deserializeWithType(jsonFactory.createParser(inputJson), deserializationContext, null);
        
        // Assertion
        assertArrayEquals(expectedOutput, actualOutput);
    }
    
    // Test for handleNonArray method
    // Test case: Non-array input with ACCEPT_SINGLE_VALUE_AS_ARRAY disabled
    @Test(expected = com.fasterxml.jackson.databind.JsonMappingException.class)
    public void testHandleNonArray_SingleValueNotAllowed() throws JsonParseException, Exception {
        // Input
        String inputJson = "\"hello\"";
        
        // Actual output
        stringArrayDeserializer.handleNonArray(jsonFactory.createParser(inputJson), deserializationContext);
    }
    
    // Test for handleNonArray method
    // Test case: Non-array input with ACCEPT_SINGLE_VALUE_AS_ARRAY enabled
    @Test
    public void testHandleNonArray_SingleValueAllowed() throws JsonParseException, Exception {
        // Input
        String inputJson = "\"hello\"";
        
        // Expected output
        String[] expectedOutput = {"hello"};
        
        // Actual output
        String[] actualOutput = stringArrayDeserializer.handleNonArray(jsonFactory.createParser(inputJson), deserializationContext);
        
        // Assertion
        assertArrayEquals(expectedOutput, actualOutput);
    }

    // Test for handleNonArray method
    // Test case: Non-array input with empty string as null object
    @Test
    public void testHandleNonArray_EmptyStringAsNullObject() throws JsonParseException, Exception {
        // Input
        String inputJson = "\"\"";
        
        // Expected output
        String[] expectedOutput = null;
        
        // Actual output
        String[] actualOutput = stringArrayDeserializer.handleNonArray(jsonFactory.createParser(inputJson), deserializationContext);
        
        // Assertion
        assertArrayEquals(expectedOutput, actualOutput);
    }

    // Test for handleNonArray method
    // Test case: Non-array input with empty string as not null object
    @Test
    public void testHandleNonArray_EmptyStringAsNotNullObject() throws JsonParseException, Exception {
        // Input
        String inputJson = "\"\"";
        
        // Expected output
        String[] expectedOutput = {""};
        
        // Set ACCEPT_EMPTY_STRING_AS_NULL_OBJECT feature as false
        objectMapper.disable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        
        // Actual output
        String[] actualOutput = stringArrayDeserializer.handleNonArray(jsonFactory.createParser(inputJson), deserializationContext);
        
        // Assertion
        assertArrayEquals(expectedOutput, actualOutput);
    }
    
    // Test for createContextual method
    // Test case: Deserializer is already the default String deserializer
    @Test
    public void testCreateContextual_DefaultDeserializer() throws JsonParseException, Exception {
        // Input
        String elementDeserializerJson = "[\"hello\", \"world\", \"test\"]";
        elementDeserializer = objectMapper.reader().forType(String.class).readValue(elementDeserializerJson);
        
        // Expected output
        StringArrayDeserializer expectedOutput = StringArrayDeserializer.instance;
        
        // Actual output
        JsonDeserializer<?> actualOutput = stringArrayDeserializer.createContextual(deserializationContext, null);
        
        // Assertion
        assertEquals(expectedOutput, actualOutput);
    }
    
    // Test for createContextual method
    // Test case: Deserializer is not the default String deserializer
    @Test
    public void testCreateContextual_NonDefaultDeserializer() throws JsonParseException, Exception {
        // Input
        String elementDeserializerJson = "[\"hello\", \"world\", \"test\"]";
        elementDeserializer = objectMapper.reader().forType(Integer.class).readValue(elementDeserializerJson);
        
        // Expected output
        StringArrayDeserializer expectedOutput = new StringArrayDeserializer(elementDeserializer);
        
        // Actual output
        JsonDeserializer<?> actualOutput = stringArrayDeserializer.createContextual(deserializationContext, null);
        
        // Assertion
        assertEquals(expectedOutput, actualOutput);
    }
    
    // Test for createContextual method
    // Test case: Deserializer is null
    @Test
    public void testCreateContextual_NullDeserializer() throws JsonParseException, Exception {
        // Expected output
        StringArrayDeserializer expectedOutput = StringArrayDeserializer.instance;
        
        // Actual output
        JsonDeserializer<?> actualOutput = stringArrayDeserializer.createContextual(deserializationContext, null);
        
        // Assertion
        assertEquals(expectedOutput, actualOutput);
    }
    
    // Test for createContextual method
    // Test case: Contextual value deserializer is null
    @Test
    public void testCreateContextual_ContextualValueDeserializerNull() throws JsonParseException, Exception {
        // Expected output
        StringArrayDeserializer expectedOutput = StringArrayDeserializer.instance;
        
        // Actual output
        JsonDeserializer<?> actualOutput = stringArrayDeserializer.createContextual(deserializationContext, null);
        
        // Assertion
        assertEquals(expectedOutput, actualOutput);
    }
    
    // Test for createContextual method
    // Test case: Contextual value deserializer is not null
    @Test
    public void testCreateContextual_ContextualValueDeserializerNotNull() throws JsonParseException, Exception {
        // Input
        elementDeserializer = objectMapper.reader().forType(String.class).readValue("[\"hello\", \"world\", \"test\"]");
        
        // Expected output
        StringArrayDeserializer expectedOutput = new StringArrayDeserializer(elementDeserializer);
        
        // Actual output
        JsonDeserializer<?> actualOutput = stringArrayDeserializer.createContextual(deserializationContext, null);
        
        // Assertion
        assertEquals(expectedOutput, actualOutput);
    }
}