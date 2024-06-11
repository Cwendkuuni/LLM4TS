import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class NumberSerializersTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testIntegerSerializer() throws JsonProcessingException {
        Integer integer = 123;
        String json = mapper.writeValueAsString(integer);
        JsonParser parser = mapper.getFactory().createParser(json);
        assertEquals(123, parser.readValueAs(Integer.class));
        parser.nextToken(); // consume the parsed integer
        assertNull(parser.readValueAs(String.class));
    }

    @Test
    public void testIntegerSerializerNegative() throws JsonProcessingException {
        Integer integer = -123;
        String json = mapper.writeValueAsString(integer);
        JsonParser parser = mapper.getFactory().createParser(json);
        assertEquals(-123, parser.readValueAs(Integer.class));
        parser.nextToken(); // consume the parsed integer
        assertNull(parser.readValueAs(String.class));
    }

    @Test
    public void testIntegerSerializerZero() throws JsonProcessingException {
        Integer integer = 0;
        String json = mapper.writeValueAsString(integer);
        JsonParser parser = mapper.getFactory().createParser(json);
        assertEquals(0, parser.readValueAs(Integer.class));
        parser.nextToken(); // consume the parsed integer
        assertNull(parser.readValueAs(String.class));
    }

    @Test(expected = JsonProcessingException.class)
    public void testIntegerSerializerNaN() throws JsonProcessingException {
        Float floatValue = Float.NaN;
        Integer integer = (int) floatValue; // force int casting
        String json = mapper.writeValueAsString(integer);
        JsonParser parser = mapper.getFactory().createParser(json);
        parser.nextToken(); // consume the parsed integer
        parser.nextToken(); // consume the exception
        fail("Expected JsonProcessingException but got: " + parser.getCurrentToken());
    }

    @Test
    public void testIntegerSerializerNull() throws JsonProcessingException {
        Integer integer = null;
        String json = mapper.writeValueAsString(integer);
        JsonParser parser = mapper.getFactory().createParser(json);
        assertNull(parser.readValueAs(Integer.class));
        parser.nextToken(); // consume the null value
        assertNull(parser.readValueAs(String.class));
    }

    // Add tests for other serializers in the class (ShortSerializer, LongSerializer, etc.) in a similar manner.
}