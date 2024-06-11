import org.junit.Test;

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

import static org.junit.Assert.*;

public class FromStringDeserializerTest {

    @Test
    public void testDeserializeFile() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, 1);
        try {
            File file = (File) deserializer._deserialize("/path/to/file", null);
            assertNotNull(file);
            assertEquals("/path/to/file", file.getPath());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testDeserializeURL() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, 2);
        try {
            URL url = (URL) deserializer._deserialize("https://www.example.com", null);
            assertNotNull(url);
            assertEquals("https://www.example.com", url.toString());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testDeserializeURI() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, 3);
        try {
            URI uri = (URI) deserializer._deserialize("https://www.example.com", null);
            assertNotNull(uri);
            assertEquals("https://www.example.com", uri.toString());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testDeserializeClass() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, 4);
        try {
            Class<?> clazz = (Class<?>) deserializer._deserialize("java.lang.String", null);
            assertNotNull(clazz);
            assertEquals(String.class, clazz);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testDeserializeJavaType() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(JavaType.class, 5);
        try {
            JavaType javaType = (JavaType) deserializer._deserialize("java.lang.String", null);
            assertNotNull(javaType);
            assertEquals("java.lang.String", javaType.toCanonical());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testDeserializeCurrency() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, 6);
        try {
            Currency currency = (Currency) deserializer._deserialize("USD", null);
            assertNotNull(currency);
            assertEquals("USD", currency.getCurrencyCode());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testDeserializePattern() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, 7);
        try {
            Pattern pattern = (Pattern) deserializer._deserialize("[a-z]+", null);
            assertNotNull(pattern);
            assertTrue(pattern.matcher("abc").matches());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testDeserializeLocale() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, 8);
        try {
            Locale locale = (Locale) deserializer._deserialize("en_US", null);
            assertNotNull(locale);
            assertEquals("en", locale.getLanguage());
            assertEquals("US", locale.getCountry());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testDeserializeCharset() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, 9);
        try {
            Charset charset = (Charset) deserializer._deserialize("UTF-8", null);
            assertNotNull(charset);
            assertEquals("UTF-8", charset.name());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testDeserializeTimeZone() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, 10);
        try {
            TimeZone timeZone = (TimeZone) deserializer._deserialize("America/New_York", null);
            assertNotNull(timeZone);
            assertEquals("America/New_York", timeZone.getID());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testDeserializeInetAddress() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, 11);
        try {
            InetAddress inetAddress = (InetAddress) deserializer._deserialize("127.0.0.1", null);
            assertNotNull(inetAddress);
            assertEquals("127.0.0.1", inetAddress.getHostAddress());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testDeserializeInetSocketAddress() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, 12);
        try {
            InetSocketAddress inetSocketAddressIPv4 = (InetSocketAddress) deserializer._deserialize("127.0.0.1:8080", null);
            assertNotNull(inetSocketAddressIPv4);
            assertEquals("127.0.0.1", inetSocketAddressIPv4.getHostString());
            assertEquals(8080, inetSocketAddressIPv4.getPort());

            InetSocketAddress inetSocketAddressIPv6 = (InetSocketAddress) deserializer._deserialize("[::1]:80", null);
            assertNotNull(inetSocketAddressIPv6);
            assertEquals("::1", inetSocketAddressIPv6.getHostString());
            assertEquals(80, inetSocketAddressIPv6.getPort());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testDeserializeInvalidValue() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, 1);
        try {
            File file = (File) deserializer._deserialize(null, null);
            assertNull(file);

            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail("Exception of type IllegalArgumentException should be thrown");
        }
    }

    @Test
    public void testDeserializeEmptyString() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, 3);
        try {
            URI uri = (URI) deserializer._deserialize("", null);
            assertNotNull(uri);
            assertEquals("", uri.toString());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testDeserializeEmbeddedObject() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, 8);
        try {
            Locale locale = (Locale) deserializer._deserializeEmbedded("en_US", null);
            assertNotNull(locale);
            assertEquals("en", locale.getLanguage());
            assertEquals("US", locale.getCountry());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testDeserializeUnknownEmbeddedObject() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, 7);
        try {
            deserializer._deserializeEmbedded(123, null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail("Exception of type IllegalArgumentException should be thrown");
        }
    }

    @Test
    public void testDeserializeEmptyStringForURI() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, 3);
        try {
            URI uri = (URI) deserializer._deserializeFromEmptyString();
            assertNotNull(uri);
            assertEquals("", uri.toString());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

}