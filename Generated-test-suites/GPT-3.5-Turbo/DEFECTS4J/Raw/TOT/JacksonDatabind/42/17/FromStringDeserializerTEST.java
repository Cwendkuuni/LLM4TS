import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;


public class FromStringDeserializerTest {

    @Test
    public void testDeserialize_file() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        File expected = new File("testfile.txt");
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        
        assertEquals(expected, deserializer.deserialize(jp, ctxt));
    }
    
    @Test
    public void testDeserialize_URL() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        URL expected = new URL("http://example.com");
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        
        assertEquals(expected, deserializer.deserialize(jp, ctxt));
    }
    
    @Test
    public void testDeserialize_URI() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        URI expected = URI.create("http://example.com");
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        
        assertEquals(expected, deserializer.deserialize(jp, ctxt));
    }
    
    @Test
    public void testDeserialize_Class() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        Class<?> expected = String.class;
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        
        assertEquals(expected, deserializer.deserialize(jp, ctxt));
    }
    
    @Test
    public void testDeserialize_JavaType() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
        JavaType expected = new JavaType(String.class);
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        
        assertEquals(expected, deserializer.deserialize(jp, ctxt));
    }
    
    @Test
    public void testDeserialize_Currency() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        Currency expected = Currency.getInstance("USD");
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        
        assertEquals(expected, deserializer.deserialize(jp, ctxt));
    }
    
    @Test
    public void testDeserialize_Pattern() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        Pattern expected = Pattern.compile("\\d+");
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        
        assertEquals(expected, deserializer.deserialize(jp, ctxt));
    }
    
    @Test
    public void testDeserialize_Locale() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        Locale expected = new Locale("en", "US");
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        
        assertEquals(expected, deserializer.deserialize(jp, ctxt));
    }
    
    @Test
    public void testDeserialize_Charset() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        Charset expected = Charset.forName("UTF-8");
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        
        assertEquals(expected, deserializer.deserialize(jp, ctxt));
    }
    
    @Test
    public void testDeserialize_TimeZone() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        TimeZone expected = TimeZone.getTimeZone("UTC");
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        
        assertEquals(expected, deserializer.deserialize(jp, ctxt));
    }
    
    @Test
    public void testDeserialize_InetAddress() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        InetAddress expected = InetAddress.getByName("localhost");
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        
        assertEquals(expected, deserializer.deserialize(jp, ctxt));
    }
    
    @Test
    public void testDeserialize_InetSocketAddress() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        InetSocketAddress expected = new InetSocketAddress("localhost", 8080);
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        
        assertEquals(expected, deserializer.deserialize(jp, ctxt));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testDeserialize_invalidType() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Boolean.class, 13);
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        
        deserializer.deserialize(jp, ctxt);
    }
    
    @Test(expected = JsonMappingException.class)
    public void testDeserialize_invalidValue() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        
        deserializer.deserialize(jp, ctxt);
    }
    
    @Test
    public void testDeserialize_emptyString_URI() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        URI expected = URI.create("");
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        
        assertEquals(expected, deserializer.deserialize(jp, ctxt));
    }

    @Test
    public void testDeserialize_emptyString_Locale() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        Locale expected = null;
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        
        assertEquals(expected, deserializer.deserialize(jp, ctxt));
    }

    @Test
    public void testDeserialize_emptyString_default() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(String.class, 13);
        String expected = "";
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        
        assertEquals(expected, deserializer.deserialize(jp, ctxt));
    }
}