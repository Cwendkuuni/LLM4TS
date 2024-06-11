import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class FromStringDeserializerTest {
    @Test
    public void testDeserialize_File() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        try {
            File file = (File) deserializer._deserialize("/path/to/file", null);
            assertEquals("/path/to/file", file.getPath());
        } catch (Exception e) {
            assertTrue(false);
        }
    }
    
    @Test
    public void testDeserialize_URL() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        try {
            URL url = (URL) deserializer._deserialize("http://www.example.com", null);
            assertEquals("http://www.example.com", url.toString());
        } catch (Exception e) {
            assertTrue(false);
        }
    }
    
    @Test
    public void testDeserialize_URI() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        try {
            URI uri = (URI) deserializer._deserialize("http://www.example.com", null);
            assertEquals("http://www.example.com", uri.toString());
        } catch (Exception e) {
            assertTrue(false);
        }
    }
    
    @Test
    public void testDeserialize_Class() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        try {
            Class<?> clazz = (Class<?>) deserializer._deserialize("java.lang.String", null);
            assertEquals(String.class, clazz);
        } catch (Exception e) {
            assertTrue(false);
        }
    }
    
    @Test
    public void testDeserialize_JavaType() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
        try {
            JavaType javaType = (JavaType) deserializer._deserialize("java.lang.String", null);
            assertEquals(String.class, javaType.getRawClass());
        } catch (Exception e) {
            assertTrue(false);
        }
    }
    
    @Test
    public void testDeserialize_Currency() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        try {
            Currency currency = (Currency) deserializer._deserialize("USD", null);
            assertEquals("USD", currency.getCurrencyCode());
        } catch (Exception e) {
            assertTrue(false);
        }
    }
    
    @Test
    public void testDeserialize_Pattern() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        try {
            Pattern pattern = (Pattern) deserializer._deserialize("[0-9]+", null);
            assertEquals("[0-9]+", pattern.pattern());
        } catch (Exception e) {
            assertTrue(false);
        }
    }
    
    @Test
    public void testDeserialize_Locale() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        try {
            Locale locale = (Locale) deserializer._deserialize("en_US", null);
            assertEquals("en", locale.getLanguage());
            assertEquals("US", locale.getCountry());
        } catch (Exception e) {
            assertTrue(false);
        }
    }
    
    @Test
    public void testDeserialize_Charset() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        try {
            Charset charset = (Charset) deserializer._deserialize("UTF-8", null);
            assertEquals("UTF-8", charset.name());
        } catch (Exception e) {
            assertTrue(false);
        }
    }
    
    @Test
    public void testDeserialize_TimeZone() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        try {
            TimeZone timeZone = (TimeZone) deserializer._deserialize("America/New_York", null);
            assertEquals("America/New_York", timeZone.getID());
        } catch (Exception e) {
            assertTrue(false);
        }
    }
    
    @Test
    public void testDeserialize_InetAddress() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        try {
            InetAddress address = (InetAddress) deserializer._deserialize("127.0.0.1", null);
            assertEquals("127.0.0.1", address.getHostAddress());
        } catch (Exception e) {
            assertTrue(false);
        }
    }
    
    @Test
    public void testDeserialize_InetSocketAddress() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        try {
            InetSocketAddress address = (InetSocketAddress) deserializer._deserialize("[::1]:8080", null);
            assertEquals("::1", address.getAddress().getHostAddress());
            assertEquals(8080, address.getPort());
        } catch (Exception e) {
            assertTrue(false);
        }
    }
}