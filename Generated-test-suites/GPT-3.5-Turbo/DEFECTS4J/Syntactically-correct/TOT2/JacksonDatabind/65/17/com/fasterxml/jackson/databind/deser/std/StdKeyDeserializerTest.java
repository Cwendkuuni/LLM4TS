import org.junit.Test;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey_NullKey_ReturnsNull() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        DeserializationContext ctxt = new DeserializationContext();
        assertNull(deserializer.deserializeKey(null, ctxt));
    }

    @Test
    public void testDeserializeKey_EmptyString_ReturnsEmptyString() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        DeserializationContext ctxt = new DeserializationContext();
        assertEquals("", deserializer.deserializeKey("", ctxt));
    }

    @Test
    public void testDeserializeKey_IntegerKey_ReturnsInteger() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        DeserializationContext ctxt = new DeserializationContext();
        assertEquals(Integer.valueOf(123), deserializer.deserializeKey("123", ctxt));
    }

    @Test
    public void testDeserializeKey_BooleanKey_ReturnsBoolean() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        DeserializationContext ctxt = new DeserializationContext();
        assertEquals(Boolean.TRUE, deserializer.deserializeKey("true", ctxt));
    }

    @Test
    public void testDeserializeKey_DoubleKey_ReturnsDouble() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);
        DeserializationContext ctxt = new DeserializationContext();
        assertEquals(Double.valueOf(3.14), deserializer.deserializeKey("3.14", ctxt));
    }

    @Test
    public void testDeserializeKey_UrlKey_ReturnsUrlObject() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class);
        DeserializationContext ctxt = new DeserializationContext();
        assertEquals(new URL("https://www.example.com"), deserializer.deserializeKey("https://www.example.com", ctxt));
    }

    @Test(expected = IOException.class)
    public void testDeserializeKey_InvalidUrlKey_ThrowsIOException() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class);
        DeserializationContext ctxt = new DeserializationContext();
        deserializer.deserializeKey("invalid-url", ctxt);
    }

}