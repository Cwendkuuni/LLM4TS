import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKeyWithValidString() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
        DeserializationContext ctxt = null;
        Object result = keyDeserializer.deserializeKey("testString", ctxt);
        assertEquals("testString", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseIntWithInvalidValue() {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);
        keyDeserializer._parseInt("invalid");
    }

    @Test
    public void testParseLongWithValidValue() {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Long.class);
        long result = keyDeserializer._parseLong("123");
        assertEquals(123L, result);
    }

    @Test
    public void testParseDoubleWithValidValue() {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Double.class);
        double result = keyDeserializer._parseDouble("123.45");
        assertEquals(123.45, result, 0.0001);
    }

    @Test
    public void testDeserializeKeyWithNullString() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
        DeserializationContext ctxt = null;
        Object result = keyDeserializer.deserializeKey(null, ctxt);
        assertNull(result);
    }

    @Test(expected = MalformedURLException.class)
    public void testParseURLWithInvalidValue() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);
        keyDeserializer._parse("invalid-url", null);
    }

    @Test
    public void testDeserializeKeyWithValidUUID() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(UUID.class);
        DeserializationContext ctxt = null;
        Object result = keyDeserializer.deserializeKey("b7cf5e02-183e-45e5-a071-468adca92559", ctxt);
        assertEquals(UUID.fromString("b7cf5e02-183e-45e5-a071-468adca92559"), result);
    }

    // Add more test cases as needed

}