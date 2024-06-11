import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import org.junit.Test;

public class FromStringDeserializerTest {

    @Test
    public void testStdDeserialize() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        Object deserializedObject = deserializer._deserialize("test.txt", null);
        assertTrue(deserializedObject instanceof File);
        assertEquals("test.txt", ((File) deserializedObject).getName());
    }

    @Test
    public void testStdDeserialize_EmptyString() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        Object deserializedObject = deserializer._deserializeFromEmptyString();
        assertNull(deserializedObject);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStdDeserialize_InvalidValue() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        deserializer._deserialize("test", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStdDeserialize_InvalidValue_EmptyString() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        deserializer._deserialize("", null);
    }

    @Test
    public void testDeserializeFile() throws Exception {
        FromStringDeserializer<File> deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        File file = deserializer._deserialize("test.txt", null);
        assertEquals("test.txt", file.getName());
    }

    @Test
    public void testDeserializeURL() throws Exception {
        FromStringDeserializer<URL> deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        URL url = deserializer._deserialize("http://www.example.com", null);
        assertEquals("http://www.example.com", url.toString());
    }

    @Test
    public void testDeserializeURI() throws Exception {
        FromStringDeserializer<URI> deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        URI uri = deserializer._deserialize("http://www.example.com", null);
        assertEquals("http://www.example.com", uri.toString());
    }

    @Test
    public void testDeserializeClass() throws Exception {
        FromStringDeserializer<Class<?>> deserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        Class<?> clazz = deserializer._deserialize("java.lang.String", null);
        assertEquals(String.class, clazz);
    }

    @Test
    public void testDeserializeJavaType() throws Exception {
        FromStringDeserializer<JavaType> deserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
        JavaType javaType = deserializer._deserialize("java.util.List", null);
        assertEquals("java.util.List", javaType.getRawClass().getName());
    }

    @Test
    public void testDeserializeCurrency() throws Exception {
        FromStringDeserializer<Currency> deserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        Currency currency = deserializer._deserialize("USD", null);
        assertEquals("USD", currency.getCurrencyCode());
    }

    @Test
    public void testDeserializePattern() throws Exception {
        FromStringDeserializer<Pattern> deserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        Pattern pattern = deserializer._deserialize("[A-Za-z]+", null);
        assertEquals("[A-Za-z]+", pattern.pattern());
    }

    @Test
    public void testDeserializeLocale() throws Exception {
        FromStringDeserializer<Locale> deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        Locale locale = deserializer._deserialize("en_US", null);
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());
    }

    @Test
    public void testDeserializeCharset() throws Exception {
        FromStringDeserializer<Charset> deserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        Charset charset = deserializer._deserialize("UTF-8", null);
        assertEquals("UTF-8", charset.displayName());
    }

    @Test
    public void testDeserializeTimeZone() throws Exception {
        FromStringDeserializer<TimeZone> deserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        TimeZone timeZone = deserializer._deserialize("America/New_York", null);
        assertEquals("America/New_York", timeZone.getID());
    }

    @Test
    public void testDeserializeInetAddress() throws Exception {
        FromStringDeserializer<InetAddress> deserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        InetAddress inetAddress = deserializer._deserialize("127.0.0.1", null);
        assertEquals("127.0.0.1", inetAddress.getHostAddress());
    }

    @Test
    public void testDeserializeInetSocketAddress() throws Exception {
        FromStringDeserializer<InetSocketAddress> deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        InetSocketAddress inetSocketAddress = deserializer._deserialize("[::1]:8080", null);
        assertEquals("[::1]", inetSocketAddress.getAddress().toString());
        assertEquals(8080, inetSocketAddress.getPort());
    }
}