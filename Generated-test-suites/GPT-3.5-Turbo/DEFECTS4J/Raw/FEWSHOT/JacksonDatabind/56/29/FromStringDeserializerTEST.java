import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import org.junit.Test;

public class FromStringDeserializerTest {
    
    @Test
    public void testDeserializeWithValidValue() throws Exception {
        // Test with valid values to check if deserialization is successful
        
        // File
        FromStringDeserializer.Std fileDeserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        assertEquals(new File("test.txt"), fileDeserializer._deserialize("test.txt", null));
        
        // URL
        FromStringDeserializer.Std urlDeserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        assertEquals(new URL("http://www.example.com"), urlDeserializer._deserialize("http://www.example.com", null));
        
        // URI
        FromStringDeserializer.Std uriDeserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        assertEquals(new URI("https://www.google.com"), uriDeserializer._deserialize("https://www.google.com", null));
        
        // Class
        FromStringDeserializer.Std classDeserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        assertEquals(String.class, classDeserializer._deserialize("java.lang.String", null));
        
        // JavaType
        FromStringDeserializer.Std javaTypeDeserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
        // TODO: Provide a valid value for JavaType
        
        // Currency
        FromStringDeserializer.Std currencyDeserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        assertEquals(Currency.getInstance("USD"), currencyDeserializer._deserialize("USD", null));
        
        // Pattern
        FromStringDeserializer.Std patternDeserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        assertEquals(Pattern.compile("\\d+"), patternDeserializer._deserialize("\\d+", null));
        
        // Locale
        FromStringDeserializer.Std localeDeserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        assertEquals(Locale.US, localeDeserializer._deserialize("en-US", null));
        
        // Charset
        FromStringDeserializer.Std charsetDeserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        assertEquals(Charset.forName("UTF-8"), charsetDeserializer._deserialize("UTF-8", null));
        
        // TimeZone
        FromStringDeserializer.Std timeZoneDeserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        assertEquals(TimeZone.getTimeZone("America/Los_Angeles"), timeZoneDeserializer._deserialize("America/Los_Angeles", null));
        
        // InetAddress
        FromStringDeserializer.Std inetAddressDeserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        assertEquals(InetAddress.getByName("127.0.0.1"), inetAddressDeserializer._deserialize("127.0.0.1", null));
        
        // InetSocketAddress
        FromStringDeserializer.Std inetSocketAddressDeserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        assertEquals(new InetSocketAddress("localhost", 8080), inetSocketAddressDeserializer._deserialize("localhost:8080", null));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeWithInvalidValue() throws Exception {
        // Test with invalid values to check if IllegalArgumentException is thrown
        
        // File
        FromStringDeserializer.Std fileDeserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        fileDeserializer._deserialize("invalid-file.txt", null);
    }
    
    @Test
    public void testDeserializeFromEmptyString() throws Exception {
        // Test with empty string values to check if the appropriate default value is returned
        
        // URI
        FromStringDeserializer.Std uriDeserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        assertEquals(URI.create(""), uriDeserializer._deserialize("", null));
        
        // Locale
        FromStringDeserializer.Std localeDeserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        assertEquals(Locale.ROOT, localeDeserializer._deserialize("", null));
        
        // Default implementation
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(Object.class, 0);
        assertNull(stdDeserializer._deserializeFromEmptyString());
    }

}