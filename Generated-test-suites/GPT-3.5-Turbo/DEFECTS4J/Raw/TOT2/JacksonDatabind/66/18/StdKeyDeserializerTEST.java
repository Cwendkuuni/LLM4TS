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

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey_NullKey_ReturnsNull() throws IOException {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        assertNull(deserializer.deserializeKey(null, null));
    }

    @Test
    public void testDeserializeKey_InvalidKey_ThrowsException() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        assertThrows(IOException.class, () -> {
            deserializer.deserializeKey("invalid", null);
        });
    }

    @Test
    public void testDeserializeKey_BooleanType_True() throws IOException {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        assertEquals(true, deserializer.deserializeKey("true", null));
    }

    @Test
    public void testDeserializeKey_ByteType() throws IOException {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class);
        assertEquals((byte) 127, deserializer.deserializeKey("127", null));
    }

    @Test
    public void testDeserializeKey_DateType() throws IOException {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DATE, Date.class);
        assertNotNull(deserializer.deserializeKey("2021-10-20", null));
    }

    @Test
    public void testDeserializeKey_UuidType() throws IOException {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_UUID, UUID.class);
        UUID expected = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");
        assertEquals(expected, deserializer.deserializeKey("38400000-8cf0-11bd-b23e-10b96e4ef00d", null));
    }

    @Test
    public void testDeserializeKey_UriType() throws IOException {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, URI.class);
        URI expected = URI.create("http://www.example.com");
        assertEquals(expected, deserializer.deserializeKey("http://www.example.com", null));
    }

    @Test
    public void testDeserializeKey_UrlType() throws IOException, MalformedURLException {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class);
        URL expected = new URL("http://www.example.com");
        assertEquals(expected, deserializer.deserializeKey("http://www.example.com", null));
    }

    @Test
    public void testGetKeyClass_StringType_ReturnsStringClass() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        assertEquals(String.class, deserializer.getKeyClass());
    }

    @Test
    public void testGetKeyClass_IntegerType_ReturnsIntegerClass() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        assertEquals(Integer.class, deserializer.getKeyClass());
    }

    @Test
    public void testGetKeyClass_UriType_ReturnsUriClass() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, URI.class);
        assertEquals(URI.class, deserializer.getKeyClass());
    }

    @Test
    public void testGetKeyClass_LocaleType_ReturnsLocaleClass() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class);
        assertEquals(Locale.class, deserializer.getKeyClass());
    }

    @Test
    public void testGetKeyClass_CurrencyType_ReturnsCurrencyClass() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CURRENCY, Currency.class);
        assertEquals(Currency.class, deserializer.getKeyClass());
    }

    @Test
    public void testGetKeyClass_UnknownType_ThrowsException() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(-1, Date.class);
        assertThrows(IllegalStateException.class, () -> {
            deserializer.getKeyClass();
        });
    }

    @Test
    public void testGetKeyClass_NullType_ThrowsException() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(-1, null);
        assertThrows(IllegalStateException.class, () -> {
            deserializer.getKeyClass();
        });
    }
}