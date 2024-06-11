import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NumberSerializersTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testIntegerSerialization() throws JsonProcessingException {
        Integer number = 123;
        String json = mapper.writeValueAsString(number);
        assertEquals("123", json);

        // Test JSON parsing
        Integer deserializedNumber = mapper.readValue(json, Integer.class);
        assertEquals(number, deserializedNumber);

        // Test negative numbers
        number = -123;
        json = mapper.writeValueAsString(number);
        assertEquals("-123", json);

        deserializedNumber = mapper.readValue(json, Integer.class);
        assertEquals(number, deserializedNumber);
    }

    @Test
    public void testLongSerialization() throws JsonProcessingException {
        Long number = 123L;
        String json = mapper.writeValueAsString(number);
        assertEquals("123", json);

        // Test JSON parsing
        Long deserializedNumber = mapper.readValue(json, Long.class);
        assertEquals(number, deserializedNumber);

        // Test large numbers
        number = Long.MAX_VALUE;
        json = mapper.writeValueAsString(number);
        assertEquals(String.valueOf(number), json);

        deserializedNumber = mapper.readValue(json, Long.class);
        assertEquals(number, deserializedNumber);
    }

    @Test
    public void testFloatSerialization() throws JsonProcessingException {
        Float number = 123.45f;
        String json = mapper.writeValueAsString(number);
        assertEquals("123.45", json);

        // Test JSON parsing
        Float deserializedNumber = mapper.readValue(json, Float.class);
        assertEquals(number, deserializedNumber, 0.01f);

        // Test negative numbers
        number = -123.45f;
        json = mapper.writeValueAsString(number);
        assertEquals("-123.45", json);

        deserializedNumber = mapper.readValue(json, Float.class);
        assertEquals(number, deserializedNumber, 0.01f);
    }

    @Test
    public void testDoubleSerialization() throws JsonProcessingException {
        Double number = 123.45;
        String json = mapper.writeValueAsString(number);
        assertEquals("123.45", json);

        // Test JSON parsing
        Double deserializedNumber = mapper.readValue(json, Double.class);
        assertEquals(number, deserializedNumber, 0.001);

        // Test large numbers
        number = Double.MAX_VALUE;
        json = mapper.writeValueAsString(number);
        assertEquals(String.valueOf(number), json);

        deserializedNumber = mapper.readValue(json, Double.class);
        assertEquals(number, deserializedNumber, 0.001);
    }

    @Test
    public void testStringSerialization() {
        String json = "\"123\"";
        ObjectMapper mapper = new ObjectMapper();

        try {
            Integer number = mapper.readValue(json, Integer.class);
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            // Expected
        }
    }
}