import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
    public void testDeserializeFile() {
        FromStringDeserializer.Std fileDeserializer = new FromStringDeserializer.Std(File.class, 1);
        File file = (File) fileDeserializer._deserialize("/path/to/file", null);
        assertNotNull(file);
        assertEquals("/path/to/file", file.getPath());
    }

    @Test
    public void testDeserializeURL() {
        FromStringDeserializer.Std urlDeserializer = new FromStringDeserializer.Std(URL.class, 2);
        URL url = (URL) urlDeserializer._deserialize("http://example.com", null);
        assertNotNull(url);
        assertEquals("http://example.com", url.toString());
    }

    @Test
    public void testDeserializeURI() {
        FromStringDeserializer.Std uriDeserializer = new FromStringDeserializer.Std(URI.class, 3);
        URI uri = (URI) uriDeserializer._deserialize("http://example.com", null);
        assertNotNull(uri);
        assertEquals("http://example.com", uri.toString());
    }

    @Test
    public void testDeserializeClass() {
        FromStringDeserializer.Std classDeserializer = new FromStringDeserializer.Std(Class.class, 4);
        Class<?> clazz = (Class<?>) classDeserializer._deserialize("java.lang.String", null);
        assertNotNull(clazz);
        assertEquals(String.class, clazz);
    }

    @Test
    public void testDeserializeJavaType() {
        FromStringDeserializer.Std javaTypeDeserializer = new FromStringDeserializer.Std(JavaType.class, 5);
        JavaType javaType = (JavaType) javaTypeDeserializer._deserialize("java.util.List", null);
        assertNotNull(javaType);
        assertEquals("java.util.List", javaType.toCanonical());
    }

    @Test
    public void testDeserializeCurrency() {
        FromStringDeserializer.Std currencyDeserializer = new FromStringDeserializer.Std(Currency.class, 6);
        Currency currency = (Currency) currencyDeserializer._deserialize("USD", null);
        assertNotNull(currency);
        assertEquals("USD", currency.getCurrencyCode());
    }

    @Test
    public void testDeserializePattern() {
        FromStringDeserializer.Std patternDeserializer = new FromStringDeserializer.Std(Pattern.class,
                7);
        Pattern pattern = (Pattern) patternDeserializer._deserialize("[a-z]+", null);
        assertNotNull(pattern);
        assertEquals("[a-z]+", pattern.pattern());
    }

    @Test
    public void testDeserializeLocale() {
        FromStringDeserializer.Std localeDeserializer = new FromStringDeserializer.Std(Locale.class, 8);
        Locale locale = (Locale) localeDeserializer._deserialize("en_US", null);
        assertNotNull(locale);
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());
    }

    @Test
    public void testDeserializeCharset() {
        FromStringDeserializer.Std charsetDeserializer = new FromStringDeserializer.Std(Charset.class, 9);
        Charset charset = (Charset) charsetDeserializer._deserialize("UTF-8", null);
        assertNotNull(charset);
        assertEquals("UTF-8", charset.name());
    }

    @Test
    public void testDeserializeTimeZone() {
        FromStringDeserializer.Std timeZoneDeserializer = new FromStringDeserializer.Std(TimeZone.class,
                10);
        TimeZone timeZone = (TimeZone) timeZoneDeserializer._deserialize("America/New_York", null);
        assertNotNull(timeZone);
        assertEquals("America/New_York", timeZone.getID());
    }

    @Test
    public void testDeserializeInetAddress() {
        FromStringDeserializer.Std inetAddressDeserializer = new FromStringDeserializer.Std(
                InetAddress.class, 11);
        InetAddress inetAddress = (InetAddress) inetAddressDeserializer
                ._deserialize("127.0.0.1", null);
        assertNotNull(inetAddress);
        assertEquals("127.0.0.1", inetAddress.getHostAddress());
    }

    @Test
    public void testDeserializeInetSocketAddressWithIPv4() {
        FromStringDeserializer.Std inetSocketAddressDeserializer = new FromStringDeserializer.Std(
                InetSocketAddress.class, 12);
        InetSocketAddress inetSocketAddress = (InetSocketAddress) inetSocketAddressDeserializer
                ._deserialize("127.0.0.1:8080", null);
        assertNotNull(inetSocketAddress);
        assertEquals("127.0.0.1", inetSocketAddress.getHostString());
        assertEquals(8080, inetSocketAddress.getPort());
    }

    @Test
    public void testDeserializeInetSocketAddressWithIPv6() {
        FromStringDeserializer.Std inetSocketAddressDeserializer = new FromStringDeserializer.Std(
                InetSocketAddress.class, 12);
        InetSocketAddress inetSocketAddress = (InetSocketAddress) inetSocketAddressDeserializer
                ._deserialize("[::1]:8080", null);
        assertNotNull(inetSocketAddress);
        assertEquals("::1", inetSocketAddress.getHostString());
        assertEquals(8080, inetSocketAddress.getPort());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeInvalidInput() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Integer.class, 0);
        deserializer._deserialize("invalid", null);
    }

    @Test
    public void testDeserializeEmptyStringForURI() {
        FromStringDeserializer.Std uriDeserializer = new FromStringDeserializer.Std(URI.class, 3);
        URI uri = (URI) uriDeserializer._deserialize("", null);
        assertNotNull(uri);
        assertEquals("", uri.toString());
    }

    @Test
    public void testDeserializeEmptyStringForLocale() {
        FromStringDeserializer.Std localeDeserializer = new FromStringDeserializer.Std(Locale.class, 8);
        Locale locale = (Locale) localeDeserializer._deserialize("", null);
        assertNotNull(locale);
        assertEquals("", locale.toString());
    }

    @Test
    public void testDeserializeNegativeInteger() {
        FromStringDeserializer.Std integerDeserializer = new FromStringDeserializer.Std(Integer.class, 0);
        Integer integer = (Integer) integerDeserializer._deserialize("-10", null);
        assertNotNull(integer);
        assertEquals(Integer.valueOf(-10), integer);
    }

    @Test
    public void testDeserializeFromEmptyString() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Object.class, 0);
        assertNull(deserializer._deserializeFromEmptyString());
    }

    @Test
    public void testDeserializeFromEmptyStringForURI() {
        FromStringDeserializer.Std uriDeserializer = new FromStringDeserializer.Std(URI.class, 3);
        URI uri = (URI) uriDeserializer._deserializeFromEmptyString();
        assertNotNull(uri);
        assertEquals("", uri.toString());
    }

    @Test
    public void testDeserializeFromEmptyStringForLocale() {
        FromStringDeserializer.Std localeDeserializer = new FromStringDeserializer.Std(Locale.class, 8);
        Locale locale = (Locale) localeDeserializer._deserializeFromEmptyString();
        assertNotNull(locale);
        assertEquals("", locale.toString());
    }
}