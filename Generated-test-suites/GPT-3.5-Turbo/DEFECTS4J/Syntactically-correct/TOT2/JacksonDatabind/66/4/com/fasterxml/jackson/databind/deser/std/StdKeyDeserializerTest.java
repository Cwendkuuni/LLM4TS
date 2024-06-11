import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey_WithValidKey_String() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        DeserializationContext ctxt = new DeserializationContext(null, null);

        Object result = keyDeserializer.deserializeKey("test", ctxt);

        assertEquals("test", result);
    }

    @Test
    public void testDeserializeKey_NullKey() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        DeserializationContext ctxt = new DeserializationContext(null, null);

        Object result = keyDeserializer.deserializeKey(null, ctxt);

        assertNull(result);
    }

    @Test
    public void testDeserializeKey_EmptyStringKey() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        DeserializationContext ctxt = new DeserializationContext(null, null);

        Object result = keyDeserializer.deserializeKey("", ctxt);

        assertEquals("", result);
    }

    @Test
    public void testDeserializeKey_InvalidKey_IOException() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        DeserializationContext ctxt = new DeserializationContext(null, null);

        assertThrows(IOException.class, () -> {
            keyDeserializer.deserializeKey(null, ctxt);
        });
    }

    @Test
    public void testParse_ValidIntKey() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        DeserializationContext ctxt = new DeserializationContext(null, null);

        Object result = keyDeserializer._parse("123", ctxt);

        assertEquals(123, result);
    }

    @Test
    public void testParse_InvalidIntKey_Exception() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        DeserializationContext ctxt = new DeserializationContext(null, null);

        assertThrows(Exception.class, () -> {
            keyDeserializer._parse("abc", ctxt);
        });
    }

    @Test
    public void testParse_ValidDateKey() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DATE, Date.class);
        DeserializationContext ctxt = new DeserializationContext(null, null);
        String dateStr = "2022-12-31T23:59:59";

        Date result = (Date) keyDeserializer._parse(dateStr, ctxt);

        assertNotNull(result);
    }

    @Test
    public void testParse_InvalidURIFromString_Exception() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, URI.class);
        DeserializationContext ctxt = new DeserializationContext(null, null);

        assertThrows(Exception.class, () -> {
            keyDeserializer._parse("invalidUri", ctxt);
        });
    }

    @Test
    public void testParse_ValidURLFromUrlKey() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class);
        DeserializationContext ctxt = new DeserializationContext(null, null);
        String urlStr = "https://www.example.com";

        URL result = (URL) keyDeserializer._parse(urlStr, ctxt);

        assertNotNull(result);
    }

    @Test
    public void testParse_InvalidURLFromUrlKey_Exception() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class);
        DeserializationContext ctxt = new DeserializationContext(null, null);

        assertThrows(MalformedURLException.class, () -> {
            keyDeserializer._parse("invalidUrl", ctxt);
        });
    }
}