import org.junit.Assert;
import org.junit.Test;

public class FromStringDeserializerTest {

    @Test
    public void testFileDeserialize() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        try {
            File file = (File) deserializer.deserialize(null, null);
            Assert.assertNull(file);
        } catch (Exception e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testURLDeserialize() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        try {
            URL url = (URL) deserializer.deserialize(null, null);
            Assert.assertNull(url);
        } catch (Exception e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testURIDeserialize() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(java.net.URI.class, FromStringDeserializer.Std.STD_URI);
        try {
            URI uri = (URI) deserializer.deserialize(null, null);
            Assert.assertNull(uri);
        } catch (Exception e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testClassDeserialize() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        try {
            Class clazz = (Class) deserializer.deserialize(null, null);
            Assert.assertNull(clazz);
        } catch (Exception e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testJavaTypeDeserialize() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
        try {
            JavaType javaType = (JavaType) deserializer.deserialize(null, null);
            Assert.assertNull(javaType);
        } catch (Exception e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testCurrencyDeserialize() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        try {
            Currency currency = (Currency) deserializer.deserialize(null, null);
            Assert.assertNull(currency);
        } catch (Exception e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testPatternDeserialize() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        try {
            Pattern pattern = (Pattern) deserializer.deserialize(null, null);
            Assert.assertNull(pattern);
        } catch (Exception e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testLocaleDeserialize() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        try {
            Locale locale = (Locale) deserializer.deserialize(null, null);
            Assert.assertNull(locale);
        } catch (Exception e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testCharsetDeserialize() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        try {
            Charset charset = (Charset) deserializer.deserialize(null, null);
            Assert.assertNull(charset);
        } catch (Exception e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testTimeZoneDeserialize() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        try {
            TimeZone timeZone = (TimeZone) deserializer.deserialize(null, null);
            Assert.assertNull(timeZone);
        } catch (Exception e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testInetAddressDeserialize() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        try {
            InetAddress inetAddress = (InetAddress) deserializer.deserialize(null, null);
            Assert.assertNull(inetAddress);
        } catch (Exception e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testInetSocketAddressDeserialize() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        try {
            InetSocketAddress inetSocketAddress = (InetSocketAddress) deserializer.deserialize(null, null);
            Assert.assertNull(inetSocketAddress);
        } catch (Exception e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }

}