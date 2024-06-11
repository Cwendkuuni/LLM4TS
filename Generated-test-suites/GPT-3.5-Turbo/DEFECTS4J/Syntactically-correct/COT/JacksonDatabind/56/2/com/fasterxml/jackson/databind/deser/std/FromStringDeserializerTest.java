import org.junit.Test;
import static org.junit.Assert.*;

public class FromStringDeserializerTest {

    @Test
    public void testDeserialize_File() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        File file = (File) deserializer.deserialize(null, null);
        assertNull(file);
        
        file = (File) deserializer.deserialize(null, null);
        assertNull(file);
        
        file = (File) deserializer.deserialize(null, null);
        assertNull(file);
    }
    
    @Test
    public void testDeserialize_URL() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        URL url = (URL) deserializer.deserialize(null, null);
        assertNull(url);
        
        url = (URL) deserializer.deserialize(null, null);
        assertNull(url);
        
        url = (URL) deserializer.deserialize(null, null);
        assertNull(url);
    }
    
    @Test
    public void testDeserialize_URI() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        URI uri = (URI) deserializer.deserialize(null, null);
        assertNull(uri);
        
        uri = (URI) deserializer.deserialize(null, null);
        assertNull(uri);
        
        uri = (URI) deserializer.deserialize(null, null);
        assertNull(uri);
    }
    
    @Test
    public void testDeserialize_Class() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        Class<?> clazz = (Class<?>) deserializer.deserialize(null, null);
        assertNull(clazz);
        
        clazz = (Class<?>) deserializer.deserialize(null, null);
        assertNull(clazz);
        
        clazz = (Class<?>) deserializer.deserialize(null, null);
        assertNull(clazz);
    }
    
    @Test
    public void testDeserialize_JavaType() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
        JavaType javaType = (JavaType) deserializer.deserialize(null, null);
        assertNull(javaType);
        
        javaType = (JavaType) deserializer.deserialize(null, null);
        assertNull(javaType);
        
        javaType = (JavaType) deserializer.deserialize(null, null);
        assertNull(javaType);
    }
    
    @Test
    public void testDeserialize_Currency() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        Currency currency = (Currency) deserializer.deserialize(null, null);
        assertNull(currency);
        
        currency = (Currency) deserializer.deserialize(null, null);
        assertNull(currency);
        
        currency = (Currency) deserializer.deserialize(null, null);
        assertNull(currency);
    }
    
    @Test
    public void testDeserialize_Pattern() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        Pattern pattern = (Pattern) deserializer.deserialize(null, null);
        assertNull(pattern);
        
        pattern = (Pattern) deserializer.deserialize(null, null);
        assertNull(pattern);
        
        pattern = (Pattern) deserializer.deserialize(null, null);
        assertNull(pattern);
    }
    
    @Test
    public void testDeserialize_Locale() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        Locale locale = (Locale) deserializer.deserialize(null, null);
        assertNull(locale);
        
        locale = (Locale) deserializer.deserialize(null, null);
        assertNull(locale);
        
        locale = (Locale) deserializer.deserialize(null, null);
        assertNull(locale);
    }
    
    @Test
    public void testDeserialize_Charset() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        Charset charset = (Charset) deserializer.deserialize(null, null);
        assertNull(charset);
        
        charset = (Charset) deserializer.deserialize(null, null);
        assertNull(charset);
        
        charset = (Charset) deserializer.deserialize(null, null);
        assertNull(charset);
    }
    
    @Test
    public void testDeserialize_TimeZone() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        TimeZone timeZone = (TimeZone) deserializer.deserialize(null, null);
        assertNull(timeZone);
        
        timeZone = (TimeZone) deserializer.deserialize(null, null);
        assertNull(timeZone);
        
        timeZone = (TimeZone) deserializer.deserialize(null, null);
        assertNull(timeZone);
    }
    
    @Test
    public void testDeserialize_InetAddress() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        InetAddress inetAddress = (InetAddress) deserializer.deserialize(null, null);
        assertNull(inetAddress);
        
        inetAddress = (InetAddress) deserializer.deserialize(null, null);
        assertNull(inetAddress);
        
        inetAddress = (InetAddress) deserializer.deserialize(null, null);
        assertNull(inetAddress);
    }
    
    @Test
    public void testDeserialize_InetSocketAddress() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        InetSocketAddress inetSocketAddress = (InetSocketAddress) deserializer.deserialize(null, null);
        assertNull(inetSocketAddress);
        
        inetSocketAddress = (InetSocketAddress) deserializer.deserialize(null, null);
        assertNull(inetSocketAddress);
        
        inetSocketAddress = (InetSocketAddress) deserializer.deserialize(null, null);
        assertNull(inetSocketAddress);
    }
}