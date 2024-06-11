import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class FromStringDeserializerTest {

    @Test
    public void testDeserializeFile() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        File expected = new File("test.txt");
        DeserializationContext ctxt = null; // provide actual context for testing
        
        File result = (File) deserializer.deserialize(mock(JsonParser.class), ctxt);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testDeserializeURL() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        URL expected = new URL("https://www.example.com");
        DeserializationContext ctxt = null; // provide actual context for testing
        
        URL result = (URL) deserializer.deserialize(mock(JsonParser.class), ctxt);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testDeserializeURI() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        URI expected = URI.create("https://www.example.com");
        DeserializationContext ctxt = null; // provide actual context for testing
        
        URI result = (URI) deserializer.deserialize(mock(JsonParser.class), ctxt);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testDeserializeClass() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        Class<?> expected = String.class;
        DeserializationContext ctxt = null; // provide actual context for testing
        
        Class<?> result = (Class<?>) deserializer.deserialize(mock(JsonParser.class), ctxt);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testDeserializeJavaType() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
        JavaType expected = new JavaType(String.class);
        DeserializationContext ctxt = null; // provide actual context for testing
        
        JavaType result = (JavaType) deserializer.deserialize(mock(JsonParser.class), ctxt);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testDeserializeCurrency() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        Currency expected = Currency.getInstance("USD");
        DeserializationContext ctxt = null; // provide actual context for testing
        
        Currency result = (Currency) deserializer.deserialize(mock(JsonParser.class), ctxt);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testDeserializePattern() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        Pattern expected = Pattern.compile("[0-9]");
        DeserializationContext ctxt = null; // provide actual context for testing
        
        Pattern result = (Pattern) deserializer.deserialize(mock(JsonParser.class), ctxt);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testDeserializeLocale() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        Locale expected = new Locale("en", "US");
        DeserializationContext ctxt = null; // provide actual context for testing
        
        Locale result = (Locale) deserializer.deserialize(mock(JsonParser.class), ctxt);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testDeserializeCharset() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        Charset expected = Charset.defaultCharset();
        DeserializationContext ctxt = null; // provide actual context for testing
        
        Charset result = (Charset) deserializer.deserialize(mock(JsonParser.class), ctxt);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testDeserializeTimeZone() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        TimeZone expected = TimeZone.getDefault();
        DeserializationContext ctxt = null; // provide actual context for testing
        
        TimeZone result = (TimeZone) deserializer.deserialize(mock(JsonParser.class), ctxt);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testDeserializeInetAddress() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        InetAddress expected = InetAddress.getLocalHost();
        DeserializationContext ctxt = null; // provide actual context for testing
        
        InetAddress result = (InetAddress) deserializer.deserialize(mock(JsonParser.class), ctxt);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        InetSocketAddress expected = new InetSocketAddress("localhost", 8080);
        DeserializationContext ctxt = null; // provide actual context for testing
        
        InetSocketAddress result = (InetSocketAddress) deserializer.deserialize(mock(JsonParser.class), ctxt);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testDeserializeStringBuilder() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(StringBuilder.class, FromStringDeserializer.Std.STD_STRING_BUILDER);
        StringBuilder expected = new StringBuilder("test");
        DeserializationContext ctxt = null; // provide actual context for testing
        
        StringBuilder result = (StringBuilder) deserializer.deserialize(mock(JsonParser.class), ctxt);
        
        assertEquals(expected, result);
    }
}