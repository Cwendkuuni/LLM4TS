import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NumberSerializersTest {

    private static ObjectMapper mapper = new ObjectMapper();

    // Test for ShortSerializer
    @Test
    public void testShortSerializer() throws JsonProcessingException {
        Short number = Short.MIN_VALUE;
        String json = "{\"number\": " + number + "}";
        Short deserializedNumber = mapper.readValue(json, Short.class);
        assertEquals(number, deserializedNumber);
    }

    // Test for IntegerSerializer
    @Test
    public void testIntegerSerializer() throws JsonProcessingException {
        Integer number = Integer.MAX_VALUE;
        String json = "{\"integer\": " + number + "}";
        Integer deserializedNumber = mapper.readValue(json, Integer.class);
        assertEquals(number, deserializedNumber);

        number = -12345;
        json = "{\"integer\": " + number + "}";
        deserializedNumber = mapper.readValue(json, Integer.class);
        assertEquals(number, deserializedNumber);
    }

    // Test for IntLikeSerializer
    @Test
    public void testIntLikeSerializer() throws JsonProcessingException {
        Long longNumber = 123L;
        String json = "{\"integer\": " + longNumber + "}";
        Number deserializedNumber = mapper.readValue(json, Number.class);
        assertEquals(longNumber, deserializedNumber.longValue());
    }

    // Test for LongSerializer
    @Test
    public void testLongSerializer() throws JsonProcessingException {
        Long number = Long.MIN_VALUE;
        String json = "{\"number\": " + number + "}";
        Long deserializedNumber = mapper.readValue(json, Long.class);
        assertEquals(number, deserializedNumber);
    }

    // Test for FloatSerializer
    @Test
    public void testFloatSerializer() throws JsonProcessingException {
        Float number = Float.NaN;
        String json = "{\"number\": " + number + "}";
        Float deserializedNumber = mapper.readValue(json, Float.class);
        assertEquals(number, deserializedNumber);

        number = 3.14F;
        json = "{\"number\": " + number + "}";
        deserializedNumber = mapper.readValue(json, Float.class);
        assertEquals(number, deserializedNumber, 0.001f);
    }

    // Test for DoubleSerializer
    @Test
    public void testDoubleSerializer() throws JsonProcessingException {
        Double number = Double.NaN;
        String json = "{\"number\": " + number + "}";
        Double deserializedNumber = mapper.readValue(json, Double.class);
        assertEquals(number, deserializedNumber);

        number = 1.23456789;
        json = "{\"number\": " + number + "}";
        deserializedNumber = mapper.readValue(json, Double.class);
        assertEquals(number, deserializedNumber, 0.0001);
    }
}