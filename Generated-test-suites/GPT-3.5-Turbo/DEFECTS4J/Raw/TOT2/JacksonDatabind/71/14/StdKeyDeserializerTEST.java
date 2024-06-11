import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
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
    public void testDeserializeKey_string() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CLASS, String.class);
        Object result = keyDeserializer.deserializeKey("test", new DeserializationContext(null));
        assertEquals("test", result);
    }

    @Test
    public void testDeserializeKey_nullKey() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CLASS, String.class);
        assertThrows(NullPointerException.class, () -> {
            keyDeserializer.deserializeKey(null, new DeserializationContext(null));
        });
    }

    @Test
    public void testDeserializeKey_date() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DATE, Date.class);
        Object result = keyDeserializer.deserializeKey("2022-10-20T15:30:00", new DeserializationContext(null));
        assertTrue(result instanceof Date);
    }

    @Test
    public void testDeserializeKey_currency() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CURRENCY, Currency.class);
        Object result = keyDeserializer.deserializeKey("USD", new DeserializationContext(null));
        assertTrue(result instanceof Currency);
    }

    @Test
    public void testDeserializeKey_uri() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, URI.class);
        Object result = keyDeserializer.deserializeKey("http://example.com", new DeserializationContext(null));
        assertTrue(result instanceof URI);
    }

    @Test
    public void testDeserializeKey_url() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class);
        Object result = keyDeserializer.deserializeKey("http://example.com", new DeserializationContext(null));
        assertTrue(result instanceof URL);
    }

    @Test
    public void testDeserializeKey_locale() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class);
        Object result = keyDeserializer.deserializeKey("en_US", new DeserializationContext(null));
        assertTrue(result instanceof Locale);
    }

    @Test
    public void testDeserializeKey_calendar() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CALENDAR, Calendar.class);
        Object result = keyDeserializer.deserializeKey("2022-10-20T15:30:00", new DeserializationContext(null));
        assertTrue(result instanceof Calendar);
    }

    @Test
    public void testDeserializeKey_uuid() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_UUID, UUID.class);
        Object result = keyDeserializer.deserializeKey("550e8400-e29b-41d4-a716-446655440000", new DeserializationContext(null));
        assertTrue(result instanceof UUID);
    }
}