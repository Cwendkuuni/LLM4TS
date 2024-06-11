import org.junit.Assert;
import org.junit.Test;

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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;

import static org.mockito.Mockito.*;

public class FromStringDeserializerTest {
    
    @Test
    public void testDeserialize_File() throws IOException {
        String filePath = "/path/to/file.txt";
        
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        
        FromStringDeserializer.Std fileDeserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        File file = (File) fileDeserializer.deserialize(jsonParser, deserializationContext);
        
        Assert.assertEquals(new File(filePath), file);
    }
    
    @Test
    public void testDeserialize_URL() throws IOException {
        String urlString = "http://www.example.com";
        
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        
        FromStringDeserializer.Std urlDeserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        URL url = (URL) urlDeserializer.deserialize(jsonParser, deserializationContext);
        
        Assert.assertEquals(new URL(urlString), url);
    }
    
    @Test
    public void testDeserialize_URI() throws IOException {
        String uriString = "http://www.example.com";
        
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        
        FromStringDeserializer.Std uriDeserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        URI uri = (URI) uriDeserializer.deserialize(jsonParser, deserializationContext);
        
        Assert.assertEquals(new URI(uriString), uri);
    }
    
    @Test
    public void testDeserialize_Class() throws IOException {
        String className = "java.lang.String";
        
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        
        FromStringDeserializer.Std classDeserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        Class<?> clazz = (Class<?>) classDeserializer.deserialize(jsonParser, deserializationContext);
        
        Assert.assertEquals(String.class, clazz);
    }
    
    @Test
    public void testDeserialize_JavaType() throws IOException {
        String typeName = "java.lang.String";
        
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        when(deserializationContext.getTypeFactory()).thenReturn(mock(com.fasterxml.jackson.databind.type.TypeFactory.class));
        
        FromStringDeserializer.Std javaTypeDeserializer = new FromStringDeserializer.Std(com.fasterxml.jackson.databind.JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
        com.fasterxml.jackson.databind.JavaType javaType = (com.fasterxml.jackson.databind.JavaType) javaTypeDeserializer.deserialize(jsonParser, deserializationContext);
        
        Assert.assertNotNull(javaType);
        verify(deserializationContext.getTypeFactory(), times(1)).constructFromCanonical(typeName);
    }
    
    @Test
    public void testDeserialize_Currency() throws IOException {
        String currencyCode = "USD";
        
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        
        FromStringDeserializer.Std currencyDeserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        Currency currency = (Currency) currencyDeserializer.deserialize(jsonParser, deserializationContext);
        
        Assert.assertEquals(Currency.getInstance(currencyCode), currency);
    }
    
    @Test
    public void testDeserialize_Pattern() throws IOException {
        String patternString = "[0-9]+";
        
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        
        FromStringDeserializer.Std patternDeserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        Pattern pattern = (Pattern) patternDeserializer.deserialize(jsonParser, deserializationContext);
        
        Assert.assertEquals(Pattern.compile(patternString), pattern);
    }
    
    @Test
    public void testDeserialize_Locale() throws IOException {
        String localeString = "en_US";
        
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        
        FromStringDeserializer.Std localeDeserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        Locale locale = (Locale) localeDeserializer.deserialize(jsonParser, deserializationContext);
        
        Assert.assertEquals(new Locale(localeString), locale);
    }
    
    @Test
    public void testDeserialize_Charset() throws IOException {
        String charsetName = "UTF-8";
        
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        
        FromStringDeserializer.Std charsetDeserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        Charset charset = (Charset) charsetDeserializer.deserialize(jsonParser, deserializationContext);
        
        Assert.assertEquals(Charset.forName(charsetName), charset);
    }
    
    @Test
    public void testDeserialize_TimeZone() throws IOException {
        String timeZoneName = "America/New_York";
        
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        
        FromStringDeserializer.Std timeZoneDeserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        TimeZone timeZone = (TimeZone) timeZoneDeserializer.deserialize(jsonParser, deserializationContext);
        
        Assert.assertEquals(TimeZone.getTimeZone(timeZoneName), timeZone);
    }
    
    @Test
    public void testDeserialize_InetAddress() throws IOException {
        String host = "localhost";
        
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        
        FromStringDeserializer.Std inetAddressDeserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        InetAddress inetAddress = (InetAddress) inetAddressDeserializer.deserialize(jsonParser, deserializationContext);
        
        Assert.assertEquals(InetAddress.getByName(host), inetAddress);
    }
    
    @Test
    public void testDeserialize_InetSocketAddress() throws IOException {
        String host = "localhost";
        int port = 8080;
        String addressString = "[" + host + "]:" + port;
        
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        
        FromStringDeserializer.Std inetSocketAddressDeserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        InetSocketAddress inetSocketAddress = (InetSocketAddress) inetSocketAddressDeserializer.deserialize(jsonParser, deserializationContext);
        
        Assert.assertEquals(new InetSocketAddress(host, port), inetSocketAddress);
        
        // Test case for IPv4 address format
        addressString = host + ":" + port;

        inetSocketAddress = (InetSocketAddress) inetSocketAddressDeserializer.deserialize(jsonParser, deserializationContext);
        
        Assert.assertEquals(new InetSocketAddress(host, port), inetSocketAddress);
    }
}