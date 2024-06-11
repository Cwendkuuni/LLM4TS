import static org.junit.Assert.*;
import org.junit.Test;

public class FromStringDeserializerTest {

    @Test
    public void testTypesMethod() {
        Class<?>[] types = FromStringDeserializer.types();
        assertNotNull(types);
        assertEquals(12, types.length);
    }

    @Test
    public void testFindDeserializer() {
        Std deserializerFile = (Std) FromStringDeserializer.findDeserializer(File.class);
        assertNotNull(deserializerFile);
        assertEquals(Std.STD_FILE, deserializerFile._kind);
        
        Std deserializerURL = (Std) FromStringDeserializer.findDeserializer(URL.class);
        assertNotNull(deserializerURL);
        assertEquals(Std.STD_URL, deserializerURL._kind);
        
        Std deserializerURI = (Std) FromStringDeserializer.findDeserializer(URI.class);
        assertNotNull(deserializerURI);
        assertEquals(Std.STD_URI, deserializerURI._kind);
        
        Std deserializerClass = (Std) FromStringDeserializer.findDeserializer(Class.class);
        assertNotNull(deserializerClass);
        assertEquals(Std.STD_CLASS, deserializerClass._kind);
        
        Std deserializerJavaType = (Std) FromStringDeserializer.findDeserializer(JavaType.class);
        assertNotNull(deserializerJavaType);
        assertEquals(Std.STD_JAVA_TYPE, deserializerJavaType._kind);
        
        Std deserializerCurrency = (Std) FromStringDeserializer.findDeserializer(Currency.class);
        assertNotNull(deserializerCurrency);
        assertEquals(Std.STD_CURRENCY, deserializerCurrency._kind);
        
        Std deserializerPattern = (Std) FromStringDeserializer.findDeserializer(Pattern.class);
        assertNotNull(deserializerPattern);
        assertEquals(Std.STD_PATTERN, deserializerPattern._kind);
        
        Std deserializerLocale = (Std) FromStringDeserializer.findDeserializer(Locale.class);
        assertNotNull(deserializerLocale);
        assertEquals(Std.STD_LOCALE, deserializerLocale._kind);
        
        Std deserializerCharset = (Std) FromStringDeserializer.findDeserializer(Charset.class);
        assertNotNull(deserializerCharset);
        assertEquals(Std.STD_CHARSET, deserializerCharset._kind);
        
        Std deserializerTimeZone = (Std) FromStringDeserializer.findDeserializer(TimeZone.class);
        assertNotNull(deserializerTimeZone);
        assertEquals(Std.STD_TIME_ZONE, deserializerTimeZone._kind);
        
        Std deserializerInetAddress = (Std) FromStringDeserializer.findDeserializer(InetAddress.class);
        assertNotNull(deserializerInetAddress);
        assertEquals(Std.STD_INET_ADDRESS, deserializerInetAddress._kind);
        
        Std deserializerInetSocketAddress = (Std) FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        assertNotNull(deserializerInetSocketAddress);
        assertEquals(Std.STD_INET_SOCKET_ADDRESS, deserializerInetSocketAddress._kind);
        
        assertNull(FromStringDeserializer.findDeserializer(String.class));
    }
}