import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.net.URL;
import java.net.URI;
import java.util.Currency;
import java.util.regex.Pattern;
import java.util.Locale;
import java.nio.charset.Charset;
import java.util.TimeZone;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;

public class FromStringDeserializerTest {

    @Test
    public void testDeserialize() {
        FromStringDeserializer<String> deserializer = new FromStringDeserializer<String>(String.class) {
            @Override
            protected String _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return value;
            }
        };
        
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        try {
            String result = deserializer.deserialize(parser, ctxt);
            assertNull(result);
        } catch (IOException e) {
            fail("IOException thrown");
        }        
    }
    
    @Test
    public void testStdFile() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, 1);
        
        String path = "test/path.txt";
        File file = (File)deserializer._deserialize(path, null);
        
        assertEquals(path, file.getPath());
    }
    
    @Test
    public void testStdURL() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, 2);
        
        String urlString = "http://www.example.com";
        URL url = (URL)deserializer._deserialize(urlString, null);
        
        assertEquals(urlString, url.toString());
    }
    
    @Test
    public void testStdURI() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, 3);
        
        String uriString = "http://www.example.com";
        URI uri = (URI)deserializer._deserialize(uriString, null);
        
        assertEquals(uriString, uri.toString());
    }
    
    @Test
    public void testStdClass() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, 4);
        
        String className = "java.lang.String";
        Class<?> clazz = (Class<?>)deserializer._deserialize(className, null);
        
        assertEquals(className, clazz.getName());
    }
    
    @Test
    public void testStdCurrency() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, 6);
        
        String currencyCode = "USD";
        Currency currency = (Currency)deserializer._deserialize(currencyCode, null);
        
        assertEquals(currencyCode, currency.getCurrencyCode());
    }
    
    @Test
    public void testStdPattern() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, 7);
        
        String patternString = "\\d+";
        Pattern pattern = (Pattern)deserializer._deserialize(patternString, null);
        assertTrue(pattern.matcher("123").matches());
    }
    
    @Test
    public void testStdLocale() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, 8);
        
        String localeString = "en_US";
        Locale locale = (Locale)deserializer._deserialize(localeString, null);
        
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());
    }
    
    @Test
    public void testStdCharset() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, 9);
        
        String charsetName = "UTF-8";
        Charset charset = (Charset)deserializer._deserialize(charsetName, null);
        
        assertEquals(charsetName, charset.name());
    }
    
    @Test
    public void testStdTimeZone() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, 10);
        
        String timeZoneID = "America/New_York";
        TimeZone timeZone = (TimeZone)deserializer._deserialize(timeZoneID, null);
        
        assertEquals(timeZoneID, timeZone.getID());
    }
    
    @Test
    public void testStdInetAddress() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, 11);
        
        String hostAddress = "127.0.0.1";
        InetAddress inetAddress = (InetAddress)deserializer._deserialize(hostAddress, null);
        
        assertEquals(hostAddress, inetAddress.getHostAddress());
    }
    
    @Test
    public void testStdInetSocketAddress() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, 12);
        
        String address = "localhost:8080";
        InetSocketAddress inetSocketAddress = (InetSocketAddress)deserializer._deserialize(address, null);
        
        assertEquals("localhost", inetSocketAddress.getHostString());
        assertEquals(8080, inetSocketAddress.getPort());
    }
}