import static org.junit.Assert.*;

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
    public void testDeserialize_file() {
        String value = "test/file.txt";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        File file = (File) deserializer._deserialize(value, null);
        assertEquals(new File(value), file);
    }

    @Test
    public void testDeserialize_url() {
        String value = "http://example.com";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        URL url = (URL) deserializer._deserialize(value, null);
        assertEquals(new URL(value), url);
    }

    @Test
    public void testDeserialize_uri() {
        String value = "http://example.com";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        URI uri = (URI) deserializer._deserialize(value, null);
        assertEquals(URI.create(value), uri);
    }

    @Test
    public void testDeserialize_class() {
        String value = "java.lang.String";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        Class<?> clazz = (Class<?>) deserializer._deserialize(value, null);
        assertEquals(String.class, clazz);
    }

    @Test
    public void testDeserialize_javaType() {
        String value = "java.lang.String";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
        JavaType javaType = (JavaType) deserializer._deserialize(value, null);
        assertEquals(JavaType.class, javaType.getClass());
        assertEquals(value, javaType.toCanonical());
    }

    @Test
    public void testDeserialize_currency() {
        String value = "USD";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        Currency currency = (Currency) deserializer._deserialize(value, null);
        assertEquals(Currency.getInstance(value), currency);
    }

    @Test
    public void testDeserialize_pattern() {
        String value = "\\d{3}-\\d{3}-\\d{4}";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        Pattern pattern = (Pattern) deserializer._deserialize(value, null);
        assertEquals(Pattern.compile(value), pattern);
    }

    @Test
    public void testDeserialize_locale() {
        String value = "en_US";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        Locale locale = (Locale) deserializer._deserialize(value, null);
        assertEquals(new Locale(value), locale);
    }

    @Test
    public void testDeserialize_charset() {
        String value = "UTF-8";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        Charset charset = (Charset) deserializer._deserialize(value, null);
        assertEquals(Charset.forName(value), charset);
    }

    @Test
    public void testDeserialize_timeZone() {
        String value = "America/New_York";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        TimeZone timeZone = (TimeZone) deserializer._deserialize(value, null);
        assertEquals(TimeZone.getTimeZone(value), timeZone);
    }

    @Test
    public void testDeserialize_inetAddress() {
        String value = "localhost";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        InetAddress inetAddress = (InetAddress) deserializer._deserialize(value, null);
        assertEquals(InetAddress.getByName(value), inetAddress);
    }

    @Test
    public void testDeserialize_inetSocketAddress_ipv4() {
        String value = "127.0.0.1:8080";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        InetSocketAddress inetSocketAddress = (InetSocketAddress) deserializer._deserialize(value, null);
        assertEquals(new InetSocketAddress("127.0.0.1", 8080), inetSocketAddress);
    }

    @Test
    public void testDeserialize_inetSocketAddress_ipv6() {
        String value = "[::1]:8080";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        InetSocketAddress inetSocketAddress = (InetSocketAddress) deserializer._deserialize(value, null);
        assertEquals(new InetSocketAddress("::1", 8080), inetSocketAddress);
    }

    @Test
    public void testDeserialize_inetSocketAddress_hostname() {
        String value = "localhost:8080";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        InetSocketAddress inetSocketAddress = (InetSocketAddress) deserializer._deserialize(value, null);
        assertEquals(new InetSocketAddress("localhost", 8080), inetSocketAddress);
    }

}