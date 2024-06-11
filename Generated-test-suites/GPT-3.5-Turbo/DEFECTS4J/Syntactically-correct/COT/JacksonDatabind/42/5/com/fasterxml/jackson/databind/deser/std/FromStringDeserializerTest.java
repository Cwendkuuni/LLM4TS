
import org.junit.Test;
import static org.junit.Assert.*;

public class FromStringDeserializerTest {
    @Test
    public void testFile() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        assertNull(deserializer.deserialize(null, null));
        assertNull(deserializer.deserialize(JsonToken.VALUE_EMBEDDED_OBJECT, null));
        assertNull(deserializer._deserialize("", null));
        assertNull(deserializer._deserializeFromEmptyString());
    }

    @Test
    public void testURL() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        assertNull(deserializer.deserialize(null, null));
        assertNull(deserializer.deserialize(JsonToken.VALUE_EMBEDDED_OBJECT, null));
        assertNull(deserializer._deserialize("", null));
        assertNull(deserializer._deserializeFromEmptyString());
    }

    @Test
    public void testURI() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        assertNull(deserializer.deserialize(null, null));
        assertNull(deserializer.deserialize(JsonToken.VALUE_EMBEDDED_OBJECT, null));
        assertNull(deserializer._deserialize("", null));
        assertEquals(deserializer._deserializeFromEmptyString(), URI.create(""));
    }

    @Test
    public void testClass() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        assertNull(deserializer.deserialize(null, null));
        assertNull(deserializer.deserialize(JsonToken.VALUE_EMBEDDED_OBJECT, null));
        assertNull(deserializer._deserialize("", null));
        assertNull(deserializer._deserializeFromEmptyString());
    }

    @Test
    public void testJavaType() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
        assertNull(deserializer.deserialize(null, null));
        assertNull(deserializer.deserialize(JsonToken.VALUE_EMBEDDED_OBJECT, null));
        assertNull(deserializer._deserialize("", null));
        assertNull(deserializer._deserializeFromEmptyString());
    }

    @Test
    public void testCurrency() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        assertNull(deserializer.deserialize(null, null));
        assertNull(deserializer.deserialize(JsonToken.VALUE_EMBEDDED_OBJECT, null));
        assertNull(deserializer._deserialize("", null));
        assertNull(deserializer._deserializeFromEmptyString());
    }

    @Test
    public void testPattern() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        assertNull(deserializer.deserialize(null, null));
        assertNull(deserializer.deserialize(JsonToken.VALUE_EMBEDDED_OBJECT, null));
        assertNull(deserializer._deserialize("", null));
        assertNull(deserializer._deserializeFromEmptyString());
    }

    @Test
    public void testLocale() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        assertNull(deserializer.deserialize(null, null));
        assertNull(deserializer.deserialize(JsonToken.VALUE_EMBEDDED_OBJECT, null));
        assertNull(deserializer._deserialize("", null));
        assertEquals(deserializer._deserializeFromEmptyString(), new Locale(""));
    }

    @Test
    public void testCharset() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        assertNull(deserializer.deserialize(null, null));
        assertNull(deserializer.deserialize(JsonToken.VALUE_EMBEDDED_OBJECT, null));
        assertNull(deserializer._deserialize("", null));
        assertNull(deserializer._deserializeFromEmptyString());
    }

    @Test
    public void testTimeZone() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        assertNull(deserializer.deserialize(null, null));
        assertNull(deserializer.deserialize(JsonToken.VALUE_EMBEDDED_OBJECT, null));
        assertNull(deserializer._deserialize("", null));
        assertNull(deserializer._deserializeFromEmptyString());
    }

    @Test
    public void testInetAddress() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        assertNull(deserializer.deserialize(null, null));
        assertNull(deserializer.deserialize(JsonToken.VALUE_EMBEDDED_OBJECT, null));
        assertNull(deserializer._deserialize("", null));
        assertNull(deserializer._deserializeFromEmptyString());
    }

    @Test
    public void testInetSocketAddress() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        assertNull(deserializer.deserialize(null, null));
        assertNull(deserializer.deserialize(JsonToken.VALUE_EMBEDDED_OBJECT, null));
        assertNull(deserializer._deserialize("", null));
        assertNull(deserializer._deserializeFromEmptyString());
    }
}
