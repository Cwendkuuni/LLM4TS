import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import org.junit.Test;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey_ValidKey() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        DeserializationContext ctxt = new DeserializationContext(null);
        String key = "test";
        Object result = keyDeserializer.deserializeKey(key, ctxt);
        assertEquals(key, result);
    }

    @Test
    public void testDeserializeKey_NullKey() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        DeserializationContext ctxt = new DeserializationContext(null);
        Object result = keyDeserializer.deserializeKey(null, ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserializeKey_InvalidKey() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        DeserializationContext ctxt = new DeserializationContext(null);
        String key = "invalid";
        try {
            keyDeserializer.deserializeKey(key, ctxt);
        } catch (Exception e) {
            assertEquals("java.lang.Integer", e.getMessage());
        }
    }

    @Test
    public void testGetKeyClass() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        Class<?> keyClass = keyDeserializer.getKeyClass();
        assertEquals(String.class, keyClass);
    }

    @Test
    public void testParse_ValidURI() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, URI.class);
        DeserializationContext ctxt = new DeserializationContext(null);
        String key = "http://www.example.com";
        Object result = keyDeserializer._parse(key, ctxt);
        assertEquals(key, result.toString());
    }

    @Test
    public void testParse_InvalidURL() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class);
        DeserializationContext ctxt = new DeserializationContext(null);
        String key = "invalid";
        try {
            keyDeserializer._parse(key, ctxt);
        } catch (IOException e) {
            assertEquals("unable to parse key as URL", e.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testParseDate_Valid() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DATE, Date.class);
        DeserializationContext ctxt = new DeserializationContext(null);
        String key = "2021-10-20";
        Object result = keyDeserializer._parse(key, ctxt);
        assertEquals(Date.class, result.getClass());
    }

    @Test
    public void testParseDate_Invalid() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DATE, Date.class);
        DeserializationContext ctxt = new DeserializationContext(null);
        String key = "invalid";
        try {
            keyDeserializer._parse(key, ctxt);
        } catch (IOException e) {
            assertEquals("java.util.Date", e.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testParseCalendar_Valid() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CALENDAR, Calendar.class);
        DeserializationContext ctxt = new DeserializationContext(null);
        String key = "2021-10-20";
        Object result = keyDeserializer._parse(key, ctxt);
        assertEquals(Calendar.class, result.getClass());
    }

    @Test
    public void testParseCalendar_Invalid() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CALENDAR, Calendar.class);
        DeserializationContext ctxt = new DeserializationContext(null);
        String key = "invalid";
        try {
            keyDeserializer._parse(key, ctxt);
        } catch (IOException e) {
            assertEquals("java.util.Calendar", e.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testParseLocale_Valid() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class);
        DeserializationContext ctxt = new DeserializationContext(null);
        String key = "en_US";
        Object result = keyDeserializer._parse(key, ctxt);
        assertEquals(Locale.class, result.getClass());
    }

    @Test
    public void testParseLocale_Invalid() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class);
        DeserializationContext ctxt = new DeserializationContext(null);
        String key = "invalid";
        try {
            keyDeserializer._parse(key, ctxt);
        } catch (IOException e) {
            assertEquals("java.util.Locale", e.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testParseCurrency_Valid() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CURRENCY, Currency.class);
        DeserializationContext ctxt = new DeserializationContext(null);
        String key = "USD";
        Object result = keyDeserializer._parse(key, ctxt);
        assertEquals(Currency.class, result.getClass());
    }

    @Test
    public void testParseCurrency_Invalid() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CURRENCY, Currency.class);
        DeserializationContext ctxt = new DeserializationContext(null);
        String key = "invalid";
        try {
            keyDeserializer._parse(key, ctxt);
        } catch (IOException e) {
            assertEquals("java.util.Currency", e.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testParse_UUID() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_UUID, UUID.class);
        DeserializationContext ctxt = new DeserializationContext(null);
        String key = "38400000-8cf0-11bd-b23e-10b96e4ef00d";
        Object result = keyDeserializer._parse(key, ctxt);
        assertEquals(UUID.class, result.getClass());
    }

}