import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey_NullKey_ReturnsNull() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        assertNull(deserializer.deserializeKey(null, null));
    }

    @Test
    public void testDeserializeKey_ValidStringKey_ReturnsKey() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        String key = "test";
        assertEquals(key, deserializer.deserializeKey(key, null));
    }

    @Test
    public void testDeserializeKey_InvalidKey_ReturnsWeirdKeyError() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        String key = "123"; // Invalid key for String
        assertNull(deserializer.deserializeKey(key, null));
    }

    @Test
    public void testParse_ValidIntKey_ReturnsInteger() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        String key = "123";
        assertEquals((Integer) 123, deserializer._parse(key, null));
    }

    @Test
    public void testParse_InvalidIntKey_ReturnsWeirdKeyError() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        String key = "abc"; // Invalid key for Integer
        assertNull(deserializer._parse(key, null));
    }

    @Test
    public void testParse_ValidLocaleKey_ReturnsLocale() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class);
        String key = "en_US";
        assertNotNull(deserializer._parse(key, null));
    }

    @Test
    public void testParse_InvalidLocaleKey_ReturnsWeirdKeyError() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class);
        String key = "abc"; // Invalid key for Locale
        assertNull(deserializer._parse(key, null));
    }

    @Test
    public void testParse_ValidUrlKey_ReturnsUrl() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class);
        String key = "http://www.example.com";
        assertNotNull(deserializer._parse(key, null));
    }

    @Test
    public void testParse_InvalidUrlKey_ReturnsMalformedUrlError() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class);
        String key = "invalidurl"; // Invalid URL format
        assertNull(deserializer._parse(key, null));
    }

}