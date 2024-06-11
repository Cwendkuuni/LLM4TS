import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey_NullKey_ReturnsNull() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        assertNull(deserializer.deserializeKey(null, ctxt));
    }

    @Test
    public void testDeserializeKey_ValidKey_RetrievesObject() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        Object result = deserializer.deserializeKey("testKey", ctxt);
        assertEquals("testKey", result);
    }

    @Test
    public void testDeserializeKey_InvalidKey_ReturnsWeirdKey() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        Object result = deserializer.deserializeKey("123", ctxt);
        assertEquals("Integer", result.getClass().getName());
    }

    @Test
    public void testParse_InvalidKey_ReturnsWeirdKey() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        DeserializationContext ctxt = new DeserializationContext(null);
        Object result = deserializer._parse("abc", ctxt);
        assertEquals("String", result.getClass().getName());
    }

    @Test
    public void testParse_LocaleKey_ReturnsCorrectObject() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class);
        DeserializationContext ctxt = new DeserializationContext(null);
        Object result = deserializer._parse("en_US", ctxt);
        assertEquals(Locale.class, result.getClass());
    }

    @Test
    public void testParse_UnknownKeyEnumValues_ReturnsNull() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        DeserializationContext ctxt = new DeserializationContext(null);
        Object result = deserializer._parse("unknown", ctxt);
        assertNull(result);
    }

    @Test
    public void testParse_DateKey_ReturnsDateObject() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DATE, Date.class);
        DeserializationContext ctxt = new DeserializationContext(null);
        Object result = deserializer._parse("2022-12-31", ctxt);
        assertEquals(Date.class, result.getClass());
    }

    @Test(expected = MalformedURLException.class)
    public void testParse_UrlKey_ThrowsMalformedURLException() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class);
        DeserializationContext ctxt = new DeserializationContext(null);
        deserializer._parse("htp://example.com", ctxt);
    }

}