
import static org.junit.Assert.*;
import org.junit.Test;

public class FromStringDeserializerTest {
    
    @Test
    public void testDeserialize() {
        //create instances of each supported type
        FromStringDeserializer<FromFile> fileDeserializer = new FromStringDeserializer.Std(STD_FILE);
        FromStringDeserializer<FromURL> urlDeserializer = new FromStringDeserializer.Std(STD_URL);
        FromStringDeserializer<FromURI> uriDeserializer = new FromStringDeserializer.Std(STD_URI);
        FromStringDeserializer<FromClass> classDeserializer = new FromStringDeserializer.Std(STD_CLASS);
        FromStringDeserializer<FromJavaType> javaTypeDeserializer = new FromStringDeserializer.Std(STD_JAVA_TYPE);
        FromStringDeserializer<FromCurrency> currencyDeserializer = new FromStringDeserializer.Std(STD_CURRENCY);
        FromStringDeserializer<FromPattern> patternDeserializer = new FromStringDeserializer.Std(STD_PATTERN);
        FromStringDeserializer<FromLocale> localeDeserializer = new FromStringDeserializer.Std(STD_LOCALE);
        FromStringDeserializer<FromCharset> charsetDeserializer = new FromStringDeserializer.Std(STD_CHARSET);
        FromStringDeserializer<FromTimeZone> timeZoneDeserializer = new FromStringDeserializer.Std(STD_TIME_ZONE);
        FromStringDeserializer<FromInetAddress> inetAddressDeserializer = new FromStringDeserializer.Std(STD_INET_ADDRESS);
        FromStringDeserializer<FromInetSocketAddress> inetSocketAddressDeserializer = new FromStringDeserializer.Std(STD_INET_SOCKET_ADDRESS);
        
        //create JSON strings for each supported type
        String fileString = "{\"file\" : \"/path/to/file\"}";
        String urlString = "{\"url\" : \"http://www.example.com\"}";
        String uriString = "{\"uri\" : \"http://www.example.com/path\"}";
        String classString = "{\"class\" : \"com.example.ExampleClass\"}";
        String javaTypeString = "{\"javaType\" : \"com.example.ExampleJavaType\"}";
        String currencyString = "{\"currency\" : \"USD\"}";
        String patternString = "{\"pattern\" : \"[0-9]+\"}";
        String localeString = "{\"locale\" : \"en_US\"}";
        String charsetString = "{\"charset\" : \"UTF-8\"}";
        String timeZoneString = "{\"timeZone\" : \"America/New_York\"}";
        String inetAddressString = "{\"inetAddress\" : \"127.0.0.1\"}";
        String inetSocketAddressString = "{\"inetSocketAddress\" : \"[0:0:0:0:0:0:0:1]:8080\"}";
        
        //deserialize JSON strings using deserializers
        FromFile file = fileDeserializer.deserialize(fileString);
        FromURL url = urlDeserializer.deserialize(urlString);
        FromURI uri = uriDeserializer.deserialize(uriString);
        FromClass classObj = classDeserializer.deserialize(classString);
        FromJavaType javaType = javaTypeDeserializer.deserialize(javaTypeString);
        FromCurrency currency = currencyDeserializer.deserialize(currencyString);
        FromPattern pattern = patternDeserializer.deserialize(patternString);
        FromLocale locale = localeDeserializer.deserialize(localeString);
        FromCharset charset = charsetDeserializer.deserialize(charsetString);
        FromTimeZone timeZone = timeZoneDeserializer.deserialize(timeZoneString);
        FromInetAddress inetAddress = inetAddressDeserializer.deserialize(inetAddressString);
        FromInetSocketAddress inetSocketAddress = inetSocketAddressDeserializer.deserialize(inetSocketAddressString);
        
        //assertions for each deserialized object
        assertEquals(new File("/path/to/file"), file);
        assertEquals(new URL("http://www.example.com"), url);
        assertEquals(URI.create("http://www.example.com/path"), uri);
        assertEquals(Class.forName("com.example.ExampleClass"), classObj);
        assertEquals(new JavaType("com.example.ExampleJavaType"), javaType);
        assertEquals(Currency.getInstance("USD"), currency);
        assertEquals(Pattern.compile("[0-9]+"), pattern);
        assertEquals(new Locale("en", "US"), locale);
        assertEquals(Charset.forName("UTF-8"), charset);
        assertEquals(TimeZone.getTimeZone("America/New_York"), timeZone);
        assertEquals(InetAddress.getByName("127.0.0.1"), inetAddress);
        assertEquals(new InetSocketAddress("[0:0:0:0:0:0:0:1]", 8080), inetSocketAddress);
    }
    
    @Test
    public void testInvalidFormat() {
        FromStringDeserializer<FromFile> fileDeserializer = new FromStringDeserializer.Std(STD_FILE);

        String invalidFileString = "{\"file\" : \"invalidPath\"}";
        
        try {
            FromFile file = fileDeserializer.deserialize(invalidFileString);
            fail("InvalidFormatException should have been thrown");
        } catch (InvalidFormatException e) {
            assertEquals("invalidPath is not a valid file path", e.getMessage());
        }
    }
}

