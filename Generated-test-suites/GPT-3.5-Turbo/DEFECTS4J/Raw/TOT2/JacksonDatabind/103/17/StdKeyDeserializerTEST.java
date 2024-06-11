import static org.junit.Assert.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonProcessingException;
import com.fasterxml.jackson.core.JsonParser;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        DeserializationContext ctxt = null;

        // Typical case
        Object result = keyDeserializer.deserializeKey("test", ctxt);
        assertEquals("test", result);

        // Edge case - null key
        result = keyDeserializer.deserializeKey(null, ctxt);
        assertNull(result);

        // Error scenario
        try {
            keyDeserializer.deserializeKey(null, ctxt);
            fail("Exception not thrown for null key");
        } catch (IOException e) {
            assertEquals("Null key", e.getMessage());
        }
    }

    @Test
    public void testForType() {
        Class<?> raw = String.class;
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(raw);

        // Typical case
        assertEquals(String.class, keyDeserializer.getKeyClass());

        // Error scenario
        raw = StdKeyDeserializer.class;
        keyDeserializer = StdKeyDeserializer.forType(raw);
        assertNull(keyDeserializer);
    }

    @Test
    public void testParse() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        DeserializationContext ctxt = null;

        // Typical case
        Object result = keyDeserializer._parse("42", ctxt);
        assertEquals(42, result);

        // Edge case - overflow
        try {
            keyDeserializer._parse("99999999999", ctxt);
            fail("Exception not thrown for overflow");
        } catch (IOException e) {
            assertEquals("Value overflow", e.getMessage());
        }

        // Error scenario
        try {
            keyDeserializer._parse("not an int", ctxt);
            fail("Exception not thrown for invalid int");
        } catch (IOException e) {
            assertEquals("Invalid int", e.getMessage());
        }
    }

    @Test
    public void testWeirdKey() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, URI.class);
        DeserializationContext ctxt = null;

        // Typical case
        Object result = keyDeserializer._weirdKey(ctxt, "test", new Exception("Custom error message"));
        assertEquals("Custom error message", result);

        // Error scenario
        try {
            keyDeserializer._weirdKey(ctxt, "test", new Exception("Another error message"));
            fail("Exception not thrown for weird key");
        } catch (IOException e) {
            assertEquals("Weird key", e.getMessage());
        }
    }
}