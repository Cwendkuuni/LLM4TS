import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {
    // Test for IntegerSerializer
    @Test
    public void testIntegerSerializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        IntegerSerializer integerSerializer = mapper.findValue("123", IntegerSerializer.class);
        JsonParser parser = mapper.getFactory().getParser();
        parser.setCodec(mapper);

        // Test serialization and deserialization of Integer
        Integer number = 123;
        String jsonString = mapper.writerWithTypeValue(number.getClass()).writeValueAsString(number);
        JsonNode jsonNode = parser.readValueAs(JsonNode.class, jsonString);
        JsonNode deserializedNumber = jsonNode.get(0);
        assertEquals(number.intValue(), deserializedNumber.asInt());

        // Test edge cases and exception handling
        assertTrue(integerSerializer.isValidBiggerNumber(Integer.MAX_VALUE));
        assertFalse(integerSerializer.isValidBiggerNumber(Integer.MAX_VALUE + 1));
        assertThrows(IllegalArgumentException.class, () -> integerSerializer.serialize(null, null, null));
        assertThrows(JsonProcessingException.class, () -> mapper.readValue("\"non-number\"", IntegerSerializer.class));
    }

    // Test for the other serializers follows a similar pattern
}