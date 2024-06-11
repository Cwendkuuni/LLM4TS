import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.test.BaseMapTest;
import com.fasterxml.jackson.databind.util.StdKeyDeserializer;

import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest extends BaseMapTest {

    @Test
    public void testBooleanTypeDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
        DeserializationContext context = mapper.getDeserializationContext();
        Object result = keyDeserializer.deserializeKey("true", context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        result = keyDeserializer.deserializeKey("false", context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        result = keyDeserializer.deserializeKey("invalid", context);
        assertNull(result);
    }

    @Test
    public void testByteTypeDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);
        DeserializationContext context = mapper.getDeserializationContext();

        Object result = keyDeserializer.deserializeKey("0", context);
        assertTrue(result instanceof Byte);
        assertEquals((byte) 0, result);

        result = keyDeserializer.deserializeKey("127", context);
        assertTrue(result instanceof Byte);
        assertEquals((byte) 127, result);

        result = keyDeserializer.deserializeKey("-128", context);
        assertTrue(result instanceof Byte);
        assertEquals((byte) -128, result);

        result = keyDeserializer.deserializeKey("256", context);
        assertNull(result);
    }

    // Add tests for short, char, int, long, float, double, Locale, Date, Calendar, UUID, URI, URL, Class, and byte_array types here

    @Test(expected = IllegalArgumentException.class)
    public void testExceptionThrowsForInvalidURITypeDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);
        DeserializationContext context = mapper.getDeserializationContext();

        String invalidUriString = "invalidURI";
        keyDeserializer.deserializeKey(invalidUriString, context);
    }

    @Test(expected = MalformedURLException.class)
    public void testExceptionThrowsForInvalidURLTypeDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);
        DeserializationContext context = mapper.getDeserializationContext();

        String invalidUrlString = "invalidURL";
        keyDeserializer.deserializeKey(invalidUrlString, context);
    }
}