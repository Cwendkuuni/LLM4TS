import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

public class NumberSerializersTest {

    @Test
    public void testIntegerSerializer() throws JsonProcessingException {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        Integer number = 123;

        // When
        String json = mapper.writeValueAsString(number);

        // Then
        JsonParser parser = mapper.getFactory().createParser(json);
        assertEquals(number.intValue(), parser.readValueAs(Integer.class));
    }

    @Test
    public void testIntegerSerializer_BigInteger() throws JsonProcessingException {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        BigInteger bigInteger = BigInteger.valueOf(123456789012345L);

        // When
        String json = mapper.writeValueAsString(bigInteger);

        // Then
        JsonParser parser = mapper.getFactory().createParser(json);
        BigInteger deserialized = parser.readValueAs(BigInteger.class);
        assertEquals(bigInteger, deserialized);
    }

    @Test(expected = JsonMappingException.class)
    public void testIntegerSerializer_InvalidValue() throws JsonProcessingException {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        String invalidJson = "{\"invalid\": 123.5}";

        // When
        JsonProcessingException exception = assertThrows(JsonProcessingException.class, () -> mapper.readValue(invalidJson, Integer.class));

        // Then
        assertEquals("Cannot deserialize value of type [simple type, class java.lang.Integer] from String \"123.5\": not a number", exception.getMessage());
    }
}