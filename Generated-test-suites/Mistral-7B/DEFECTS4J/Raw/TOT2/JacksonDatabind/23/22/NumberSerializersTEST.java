import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JsonNode;

public class NumberSerializersTest {

    @Test
    public void testIntegerSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        IntegerSerializer integerSerializer = new IntegerSerializer();
        JsonSerializer<Object> serializer = integerSerializer;

        Integer number = 123;
        String jsonString = mapper.writeValueAsString(number);
        JsonNode jsonNode = mapper.readTree(jsonString);

        JsonNode expectedJsonNode = mapper.valueToTree(123);
        assertEquals(expectedJsonNode, jsonNode);

        int deserializedNumber = mapper.treeToValue(jsonNode, int.class);
        assertEquals(number.intValue(), deserializedNumber);

        // Test exception handling when passing a non-integer value
        JsonNode invalidJsonNode = mapper.readTree("{\"value\":\"abc\"}");
        try {
            deserializedNumber = mapper.treeToValue(invalidJsonNode, int.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Repeat similar tests for other serializers: ShortSerializer, LongSerializer, FloatSerializer, DoubleSerializer
}