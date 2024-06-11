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

import org.apache.commons.cli.Util;
import org.junit.Test;

public class FromStringDeserializerTest {

    @Test
    public void testFileDeserializer() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        File file = (File) deserializer._deserialize("/path/to/file", null);
        assertEquals("/path/to/file", file.getPath());
    }

    @Test
    public void testURLDeserializer() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        URL url = (URL) deserializer._deserialize("https://www.example.com", null);
        assertEquals("https://www.example.com", url.toString());
    }

    @Test
    public void testURIDeserializer() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        URI uri = (URI) deserializer._deserialize("http://www.example.com", null);
        assertEquals("http://www.example.com", uri.toString());
    }

    @Test
    public void testClassDeserializer() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        Class<?> clazz = (Class<?>) deserializer._deserialize("java.lang.String", null);
        assertEquals(String.class, clazz);
    }

    @Test
    public void testCurrencyDeserializer() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        Currency currency = (Currency) deserializer._deserialize("USD", null);
        assertEquals("USD", currency.getCurrencyCode());
    }

    @Test
    public void testPatternDeserializer() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        Pattern pattern = (Pattern) deserializer._deserialize("[a-z]+", null);
        assertTrue(pattern.matcher("abcdefg").matches());
    }

    @Test
    public void testLocaleDeserializer() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        Locale locale = (Locale) deserializer._deserialize("en", null);
        assertEquals("en", locale.getLanguage());
    }

    @Test
    public void testCharsetDeserializer() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        Charset charset = (Charset) deserializer._deserialize("UTF-8", null);
        assertEquals("UTF-8", charset.name());
    }

    @Test
    public void testTimeZoneDeserializer() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        TimeZone timeZone = (TimeZone) deserializer._deserialize("GMT+09:00", null);
        assertEquals("GMT+09:00", timeZone.getID());
    }

    @Test
    public void testInetAddressDeserializer() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        InetAddress inetAddress = (InetAddress) deserializer._deserialize("127.0.0.1", null);
        assertEquals("127.0.0.1", inetAddress.getHostAddress());
    }

    @Test
    public void testInetSocketAddressDeserializer() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        InetSocketAddress inetSocketAddress = (InetSocketAddress) deserializer._deserialize("127.0.0.1:8080", null);
        assertEquals("127.0.0.1", inetSocketAddress.getAddress().getHostAddress());
        assertEquals(8080, inetSocketAddress.getPort());
    }

    @Test
    public void testStringBuilderDeserializer() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(StringBuilder.class, FromStringDeserializer.Std.STD_STRING_BUILDER);
        StringBuilder stringBuilder = (StringBuilder) deserializer._deserialize("hello", null);
        assertEquals("hello", stringBuilder.toString());
    }

}