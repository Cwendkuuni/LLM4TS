import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKeyNull() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        DeserializationContext ctxt = new DeserializationContext(null);
        assertNull(keyDeserializer.deserializeKey(null, ctxt));
    }

    @Test
    public void testDeserializeKeyInvalidKey() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);
        DeserializationContext ctxt = new DeserializationContext(null);
        try {
            keyDeserializer.deserializeKey("invalid", ctxt);
            fail("Should throw exception for invalid key");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testParseKeyDate() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DATE, Date.class);
        DeserializationContext ctxt = new DeserializationContext(null);
        assertNotNull(keyDeserializer._parse("2022-01-01", ctxt));
    }

    @Test
    public void testParseKeyURI() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, URI.class);
        DeserializationContext ctxt = new DeserializationContext(null);
        assertNotNull(keyDeserializer._parse("http://example.com", ctxt));
    }

    @Test
    public void testParseKeyURL() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class);
        DeserializationContext ctxt = new DeserializationContext(null);
        assertNotNull(keyDeserializer._parse("http://example.com", ctxt));
    }

    @Test
    public void testParseKeyCurrency() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CURRENCY, Currency.class);
        DeserializationContext ctxt = new DeserializationContext(null);
        assertNotNull(keyDeserializer._parse("USD", ctxt));
    }

    @Test
    public void testParseKeyLocale() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class);
        DeserializationContext ctxt = new DeserializationContext(null);
        assertNotNull(keyDeserializer._parse("en_US", ctxt));
    }

    @Test
    public void testParseKeyInvalidValue() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        DeserializationContext ctxt = new DeserializationContext(null);
        try {
            keyDeserializer._parse("invalid", ctxt);
            fail("Should throw exception for invalid value");
        } catch (Exception e) {
            // Expected
        }
    }
}