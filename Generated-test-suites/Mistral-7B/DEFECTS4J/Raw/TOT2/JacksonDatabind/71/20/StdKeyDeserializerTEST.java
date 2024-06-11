import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    @Test
    public void testBooleanDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
        JsonParser parser = mapper.getFactory().createParser("true");

        Object result = deserializer.deserializeKey(parser, new DeserializationContext(mapper.getFactory()));
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        parser = mapper.getFactory().createParser("false");
        result = deserializer.deserializeKey(parser, new DeserializationContext(mapper.getFactory()));
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        parser = mapper.getFactory().createParser("invalid");
        try {
            deserializer.deserializeKey(parser, new DeserializationContext(mapper.getFactory()));
            fail("Expected weirdKeyException for invalid boolean key");
        } catch (JsonMappingException e) {
            assertEquals("not a valid representation: [wrong type office.json.StdKeyDeserializer$1@5bb6f884]", e.getMessage());
        }
    }

    @Test
    public void testByteDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);

        JsonParser parser = mapper.getFactory().createParser("60");
        Object result = deserializer.deserializeKey(parser, new DeserializationContext(mapper.getFactory()));
        assertTrue(result instanceof Byte);
        assertEquals((byte) 60, result);

        parser = mapper.getFactory().createParser("128");
        try {
            deserializer.deserializeKey(parser, new DeserializationContext(mapper.getFactory()));
            fail("Expected weirdKeyException for byte key out of range");
        } catch (JsonMappingException e) {
            assertEquals("not a valid representation: overflow, value can not be represented as 8-bit value", e.getMessage());
        }
    }

    // Add similar test cases for short, char, int, long, float, double, Locale, Date, Calendar, UUID, URI, URL, and Class

    @Test
    public void testInvalidKeyDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);

        JsonParser parser = mapper.getFactory().createParser("invalidKey");
        try {
            deserializer.deserializeKey(parser, new DeserializationContext(mapper.getFactory()));
            fail("Expected weirdKeyException for invalid key");
        } catch (JsonMappingException e) {
            assertEquals("not a valid representation", e.getMessage());
        }
    }

    @Test
    public void testNullKeyDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);

        JsonParser parser = mapper.getFactory().createParser(null);
        Object result = deserializer.deserializeKey(parser, new DeserializationContext(mapper.getFactory()));
        assertNull(result);
    }
}