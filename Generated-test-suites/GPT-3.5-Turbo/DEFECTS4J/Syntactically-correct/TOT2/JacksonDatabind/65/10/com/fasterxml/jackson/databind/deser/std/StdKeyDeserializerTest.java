import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey_NullKey_ReturnNull() throws IOException {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        DeserializationContext ctxt = null; // context is mocked here for simplicity
        Object result = deserializer.deserializeKey(null, ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserializeKey_InvalidKey_ReturnError() throws IOException {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        DeserializationContext ctxt = null; // context is mocked here for simplicity
        try {
            deserializer.deserializeKey("", ctxt);
            fail("Expected IOException");
        } catch (IOException e) {
            assertEquals("not a valid representation", e.getMessage());
        }
    }

    @Test
    public void testDeserializeKey_ValidKey_ReturnValue() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(UUID.class);
        DeserializationContext ctxt = null; // context is mocked here for simplicity
        Object result = deserializer.deserializeKey("550e8400-e29b-41d4-a716-446655440000", ctxt);
        assertTrue(result instanceof UUID);
    }

    @Test
    public void testParseKey_IntegerOutOfBounds_ReturnError() throws IOException {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        DeserializationContext ctxt = null; // context is mocked here for simplicity
        try {
            deserializer.deserializeKey("2147483648", ctxt);
            fail("Expected IOException");
        } catch (IOException e) {
            assertEquals("overflow, value can not be represented as 32-bit value", e.getMessage());
        }
    }

    @Test
    public void testParseKey_NullUUID_ReturnNull() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(UUID.class);
        DeserializationContext ctxt = null; // context is mocked here for simplicity
        Object result = deserializer.deserializeKey(null, ctxt);
        assertNull(result);
    }

    @Test
    public void testParseKey_InvalidURL_ReturnError() throws IOException {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class);
        DeserializationContext ctxt = null; // context is mocked here for simplicity
        try {
            deserializer.deserializeKey("invalid-url", ctxt);
            fail("Expected IOException");
        } catch (IOException e) {
            assertEquals("problem: no protocol: invalid-url", e.getMessage());
        }
    }

}