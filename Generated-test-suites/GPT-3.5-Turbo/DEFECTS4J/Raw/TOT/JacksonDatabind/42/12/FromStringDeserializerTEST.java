import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

public class FromStringDeserializerTest {

    @Test
    public void testDeserializeFile() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        
        File expected = new File("test");
        File result = (File) deserializer.deserialize(mockJsonParser(expected.getPath()), mockDeserializationContext());
        
        assertEquals(expected.getPath(), result.getPath());
    }
    
    @Test
    public void testDeserializeURL() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        
        URL expected = new URL("http://www.example.com");
        URL result = (URL) deserializer.deserialize(mockJsonParser(expected.toString()), mockDeserializationContext());
        
        assertEquals(expected.toString(), result.toString());
    }
    
    @Test
    public void testDeserializeURI() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        
        URI expected = new URI("http://www.example.com");
        URI result = (URI) deserializer.deserialize(mockJsonParser(expected.toString()), mockDeserializationContext());
        
        assertEquals(expected.toString(), result.toString());
    }
    
    @Test
    public void testDeserializeClass() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        
        Class<?> expected = String.class;
        Class<?> result = (Class<?>) deserializer.deserialize(mockJsonParser(expected.getName()), mockDeserializationContext());
        
        assertEquals(expected.getName(), result.getName());
    }
    
    @Test
    public void testDeserializeCurrency() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        
        Currency expected = Currency.getInstance("USD");
        Currency result = (Currency) deserializer.deserialize(mockJsonParser(expected.getCurrencyCode()), mockDeserializationContext());
        
        assertEquals(expected.getCurrencyCode(), result.getCurrencyCode());
    }
    
    @Test
    public void testDeserializePattern() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        
        Pattern expected = Pattern.compile("\\d+");
        Pattern result = (Pattern) deserializer.deserialize(mockJsonParser(expected.pattern()), mockDeserializationContext());
        
        assertEquals(expected.pattern(), result.pattern());
    }
    
    @Test
    public void testDeserializeLocale() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        
        Locale expected = Locale.JAPAN;
        Locale result = (Locale) deserializer.deserialize(mockJsonParser(expected.toString()), mockDeserializationContext());
        
        assertEquals(expected.toString(), result.toString());
    }
    
    @Test
    public void testDeserializeCharset() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        
        Charset expected = Charset.forName("UTF-8");
        Charset result = (Charset) deserializer.deserialize(mockJsonParser(expected.toString()), mockDeserializationContext());
        
        assertEquals(expected.toString(), result.toString());
    }
    
    @Test
    public void testDeserializeTimeZone() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        
        TimeZone expected = TimeZone.getTimeZone("GMT");
        TimeZone result = (TimeZone) deserializer.deserialize(mockJsonParser(expected.getID()), mockDeserializationContext());
        
        assertEquals(expected.getID(), result.getID());
    }
    
    @Test
    public void testDeserializeInetAddress() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        
        InetAddress expected = InetAddress.getByName("localhost");
        InetAddress result = (InetAddress) deserializer.deserialize(mockJsonParser(expected.getHostAddress()), mockDeserializationContext());
        
        assertEquals(expected.getHostAddress(), result.getHostAddress());
    }
    
    @Test
    public void testDeserializeInetSocketAddress() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        
        InetSocketAddress expected = new InetSocketAddress("localhost", 8080);
        InetSocketAddress result = (InetSocketAddress) deserializer.deserialize(mockJsonParser(expected.toString()), mockDeserializationContext());
        
        assertEquals(expected.getHostString(), result.getHostString());
        assertEquals(expected.getPort(), result.getPort());
    }
    
    @Test
    public void testDeserializeEmptyStringForURI() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        
        URI expected = new URI("");
        URI result = (URI) deserializer.deserialize(mockJsonParser(expected.toString()), mockDeserializationContext());
        
        assertEquals(expected.toString(), result.toString());
    }
    
    @Test(expected = InvalidFormatException.class)
    public void testDeserializeInvalidFormatForInetSocketAddress() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        
        InetSocketAddress expected = new InetSocketAddress("localhost", 8080);
        deserializer.deserialize(mockJsonParser("localhost:8080:8080"), mockDeserializationContext());
    }
    
    private JsonParser mockJsonParser(String value) {
        // Mocked JsonParser implementation for testing
    }
    
    private DeserializationContext mockDeserializationContext() {
        // Mocked DeserializationContext implementation for testing
    }
}