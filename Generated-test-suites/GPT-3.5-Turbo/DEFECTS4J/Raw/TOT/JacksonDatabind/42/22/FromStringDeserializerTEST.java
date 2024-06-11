import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class FromStringDeserializerTest {
    
    private final JsonParser jp = new JsonParser();
    private final DeserializationContext ctxt = new DeserializationContext(null, null, null);
    
    // Test cases for File.class
    @Test
    public void testDeserialize_File() throws Exception {
        FromStringDeserializer.Std fileDeserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        assertEquals(new File("test.txt"), fileDeserializer.deserialize(jp.setText("test.txt"), ctxt));
    }
    
    // Test cases for URL.class
    @Test
    public void testDeserialize_URL() throws Exception {
        FromStringDeserializer.Std urlDeserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        assertEquals(new URL("https://www.example.com"), urlDeserializer.deserialize(jp.setText("https://www.example.com"), ctxt));
    }
    
    // Test cases for URI.class
    @Test
    public void testDeserialize_URI() throws Exception {
        FromStringDeserializer.Std uriDeserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        assertEquals(URI.create("http://www.example.com"), uriDeserializer.deserialize(jp.setText("http://www.example.com"), ctxt));
    }
    
    // Test cases for Class.class
    @Test
    public void testDeserialize_Class() throws Exception {
        FromStringDeserializer.Std classDeserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        assertEquals(String.class, classDeserializer.deserialize(jp.setText("java.lang.String"), ctxt));
    }
    
    // Test cases for JavaType.class
    @Test
    public void testDeserialize_JavaType() throws Exception {
        FromStringDeserializer.Std javaTypeDeserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
        assertEquals(JavaType.class, javaTypeDeserializer.deserialize(jp.setText("com.fasterxml.jackson.databind.JavaType"), ctxt));
    }
    
    // Test cases for Currency.class
    @Test
    public void testDeserialize_Currency() throws Exception {
        FromStringDeserializer.Std currencyDeserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        assertEquals(Currency.getInstance("USD"), currencyDeserializer.deserialize(jp.setText("USD"), ctxt));
    }
    
    // Test cases for Pattern.class
    @Test
    public void testDeserialize_Pattern() throws Exception {
        FromStringDeserializer.Std patternDeserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        assertEquals(Pattern.compile("[0-9]+"), patternDeserializer.deserialize(jp.setText("[0-9]+"), ctxt));
    }
    
    // Test cases for Locale.class
    @Test
    public void testDeserialize_Locale() throws Exception {
        FromStringDeserializer.Std localeDeserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        assertEquals(new Locale("en"), localeDeserializer.deserialize(jp.setText("en"), ctxt));
        assertEquals(new Locale("en", "US"), localeDeserializer.deserialize(jp.setText("en_US"), ctxt));
        assertEquals(new Locale("en", "US", "NY"), localeDeserializer.deserialize(jp.setText("en_US_NY"), ctxt));
    }
    
    // Test cases for Charset.class
    @Test
    public void testDeserialize_Charset() throws Exception {
        FromStringDeserializer.Std charsetDeserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        assertEquals(Charset.forName("UTF-8"), charsetDeserializer.deserialize(jp.setText("UTF-8"), ctxt));
    }
    
    // Test cases for TimeZone.class
    @Test
    public void testDeserialize_TimeZone() throws Exception {
        FromStringDeserializer.Std timeZoneDeserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        assertEquals(TimeZone.getTimeZone("GMT+5:30"), timeZoneDeserializer.deserialize(jp.setText("GMT+5:30"), ctxt));
    }
    
    // Test cases for InetAddress.class
    @Test
    public void testDeserialize_InetAddress() throws Exception {
        FromStringDeserializer.Std inetAddressDeserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        assertEquals(InetAddress.getByName("127.0.0.1"), inetAddressDeserializer.deserialize(jp.setText("127.0.0.1"), ctxt));
    }
    
    // Test case for InetSocketAddress.class with IPv4 address
    @Test
    public void testDeserialize_InetSocketAddressIPv4() throws Exception {
        FromStringDeserializer.Std inetSocketAddressDeserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        assertEquals(new InetSocketAddress("127.0.0.1", 8080), inetSocketAddressDeserializer.deserialize(jp.setText("127.0.0.1:8080"), ctxt));
    }
    
    // Test case for InetSocketAddress.class with IPv6 address
    @Test
    public void testDeserialize_InetSocketAddressIPv6() throws Exception {
        FromStringDeserializer.Std inetSocketAddressDeserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        assertEquals(new InetSocketAddress("[2001:0db8:85a3:0000:0000:8a2e:0370:7334]", 8080), inetSocketAddressDeserializer.deserialize(jp.setText("[2001:0db8:85a3:0000:0000:8a2e:0370:7334]:8080"), ctxt));
    }
    
    // Test case for invalid InetSocketAddress with missing closing bracket
    @Test(expected = InvalidFormatException.class)
    public void testDeserialize_InvalidInetSocketAddress() throws Exception {
        FromStringDeserializer.Std inetSocketAddressDeserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        inetSocketAddressDeserializer.deserialize(jp.setText("[2001:0db8:85a3:0000:0000:8a2e:0370:7334:8080"), ctxt);
    }
    
    // Test case for invalid InetSocketAddress with invalid port number
    @Test(expected = NumberFormatException.class)
    public void testDeserialize_InvalidInetSocketAddressPort() throws Exception {
        FromStringDeserializer.Std inetSocketAddressDeserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        inetSocketAddressDeserializer.deserialize(jp.setText("127.0.0.1:port"), ctxt);
    }
    
    // Test case for unknown type
    @Test
    public void testDeserialize_UnknownType() throws Exception {
        FromStringDeserializer.Std unknownTypeDeserializer = new FromStringDeserializer.Std(Object.class, 100);
        assertNull(unknownTypeDeserializer.deserialize(jp.setText("test"), ctxt));
    }
    
    // Test case for empty URI
    @Test
    public void testDeserialize_EmptyURI() throws Exception {
        FromStringDeserializer.Std uriDeserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        assertEquals(URI.create(""), uriDeserializer.deserialize(jp.setText(""), ctxt));
    }
    
    // General test case for invalid text representation
    @Test(expected = JsonMappingException.class)
    public void testDeserialize_InvalidTextRepresentation() throws Exception {
        FromStringDeserializer.Std invalidTextRepresentationDeserializer = new FromStringDeserializer.Std(Object.class, 100);
        invalidTextRepresentationDeserializer.deserialize(jp.setText("invalid"), ctxt);
    }
}