import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FromStringDeserializerTest {

    @Mock
    DeserializationContext ctxt;
    
    @InjectMocks
    FromStringDeserializer.Std fromStringDeserializer;

    @Test
    public void testDeserialize_File_Success() throws IOException {
        String value = "/path/to/file.txt";
        File expected = new File(value);
        Object result = fromStringDeserializer._deserialize(value, ctxt);
        assertEquals(expected, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testDeserialize_File_Invalid() throws IOException {
        String value = "";
        fromStringDeserializer._deserialize(value, ctxt);
    }
    
    @Test
    public void testDeserialize_URL_Success() throws IOException {
        String value = "http://www.example.com";
        URL expected = new URL(value);
        Object result = fromStringDeserializer._deserialize(value, ctxt);
        assertEquals(expected, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testDeserialize_URL_Invalid() throws IOException {
        String value = "";
        fromStringDeserializer._deserialize(value, ctxt);
    }
    
    @Test
    public void testDeserialize_URI_Success() throws IOException {
        String value = "http://www.example.com";
        URI expected = URI.create(value);
        Object result = fromStringDeserializer._deserialize(value, ctxt);
        assertEquals(expected, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testDeserialize_URI_Invalid() throws IOException {
        String value = "";
        fromStringDeserializer._deserialize(value, ctxt);
    }
    
    @Test
    public void testDeserialize_Class_Success() throws IOException {
        String value = "com.example.SomeClass";
        Class<?> expected = ctxt.findClass(value);
        Object result = fromStringDeserializer._deserialize(value, ctxt);
        assertEquals(expected, result);
    }
    
    @Test(expected = Exception.class)
    public void testDeserialize_Class_Invalid() throws IOException {
        String value = "com.example.SomeClass";
        fromStringDeserializer._deserialize(value, ctxt);
    }
    
    @Test
    public void testDeserialize_JavaType_Success() throws IOException {
        String value = "java.util.List<java.lang.String>";
        JavaType expected = ctxt.getTypeFactory().constructFromCanonical(value);
        Object result = fromStringDeserializer._deserialize(value, ctxt);
        assertEquals(expected, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testDeserialize_JavaType_Invalid() throws IOException {
        String value = "";
        fromStringDeserializer._deserialize(value, ctxt);
    }
    
    @Test
    public void testDeserialize_Currency_Success() throws IOException {
        String value = "USD";
        Currency expected = Currency.getInstance(value);
        Object result = fromStringDeserializer._deserialize(value, ctxt);
        assertEquals(expected, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testDeserialize_Currency_Invalid() throws IOException {
        String value = "";
        fromStringDeserializer._deserialize(value, ctxt);
    }
    
    @Test
    public void testDeserialize_Pattern_Success() throws IOException {
        String value = "\\d{3}-\\d{3}-\\d{4}";
        Pattern expected = Pattern.compile(value);
        Object result = fromStringDeserializer._deserialize(value, ctxt);
        assertEquals(expected, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testDeserialize_Pattern_Invalid() throws IOException {
        String value = "";
        fromStringDeserializer._deserialize(value, ctxt);
    }
    
    @Test
    public void testDeserialize_Locale_Success() throws IOException {
        String value = "en_US";
        Locale expected = new Locale(value);
        Object result = fromStringDeserializer._deserialize(value, ctxt);
        assertEquals(expected, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testDeserialize_Locale_Invalid() throws IOException {
        String value = "";
        fromStringDeserializer._deserialize(value, ctxt);
    }
    
    @Test
    public void testDeserialize_Charset_Success() throws IOException {
        String value = "UTF-8";
        Charset expected = Charset.forName(value);
        Object result = fromStringDeserializer._deserialize(value, ctxt);
        assertEquals(expected, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testDeserialize_Charset_Invalid() throws IOException {
        String value = "";
        fromStringDeserializer._deserialize(value, ctxt);
    }
    
    @Test
    public void testDeserialize_TimeZone_Success() throws IOException {
        String value = "America/New_York";
        TimeZone expected = TimeZone.getTimeZone(value);
        Object result = fromStringDeserializer._deserialize(value, ctxt);
        assertEquals(expected, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testDeserialize_TimeZone_Invalid() throws IOException {
        String value = "";
        fromStringDeserializer._deserialize(value, ctxt);
    }
    
    @Test
    public void testDeserialize_InetAddress_Success() throws IOException {
        String value = "127.0.0.1";
        InetAddress expected = InetAddress.getByName(value);
        Object result = fromStringDeserializer._deserialize(value, ctxt);
        assertEquals(expected, result);
    }
    
    @Test(expected = Exception.class)
    public void testDeserialize_InetAddress_Invalid() throws IOException {
        String value = "";
        fromStringDeserializer._deserialize(value, ctxt);
    }
    
    @Test
    public void testDeserialize_InetSocketAddress_Success() throws IOException {
        String value = "localhost:8080";
        InetSocketAddress expected = new InetSocketAddress(value, 8080);
        Object result = fromStringDeserializer._deserialize(value, ctxt);
        assertEquals(expected, result);
    }
    
    @Test
    public void testDeserialize_InetSocketAddress_IPv6Bracketed_Success() throws IOException {
        String value = "[::1]:8080";
        InetSocketAddress expected = new InetSocketAddress("::1", 8080);
        Object result = fromStringDeserializer._deserialize(value, ctxt);
        assertEquals(expected, result);
    }
    
    @Test
    public void testDeserialize_InetSocketAddress_IPv6Unbracketed_Success() throws IOException {
        String value = "::1:8080";
        InetSocketAddress expected = new InetSocketAddress("::1:8080", 0);
        Object result = fromStringDeserializer._deserialize(value, ctxt);
        assertEquals(expected, result);
    }
    
    @Test(expected = InvalidFormatException.class)
    public void testDeserialize_InetSocketAddress_IPv6Bracketed_Invalid() throws IOException {
        String value = "[::1";
        fromStringDeserializer._deserialize(value, ctxt);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testDeserialize_InetSocketAddress_Invalid() throws IOException {
        String value = "";
        fromStringDeserializer._deserialize(value, ctxt);
    }
}