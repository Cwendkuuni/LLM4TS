
import org.junit.Test;
import static org.junit.Assert.*;

public class FromStringDeserializerTest {

    @Test
    public void testFileDeserialization() {
        FromStringDeserializer.Std fileDeserializer = (FromStringDeserializer.Std) FromStringDeserializer.findDeserializer(File.class);
        File file = (File) fileDeserializer.deserialize(JsonToken.VALUE_STRING, DeserializationContextStub, "");
        assertEquals(file, new File(""));
    }
    
    @Test
    public void testURLDeserialization() {
        FromStringDeserializer.Std urlDeserializer = (FromStringDeserializer.Std) FromStringDeserializer.findDeserializer(URL.class);
        URL url = (URL) urlDeserializer.deserialize(JsonToken.VALUE_STRING, DeserializationContextStub, "http://www.example.com");
        assertEquals(url, new URL("http://www.example.com"));
    }
    
    @Test
    public void testURIDeserialization() {
        FromStringDeserializer.Std uriDeserializer = (FromStringDeserializer.Std) FromStringDeserializer.findDeserializer(URI.class);
        URI uri = (URI) uriDeserializer.deserialize(JsonToken.VALUE_STRING, DeserializationContextStub, "http://www.example.com");
        assertEquals(uri, URI.create("http://www.example.com"));
    }
    
    @Test
    public void testClassDeserialization() throws ClassNotFoundException {
        FromStringDeserializer.Std classDeserializer = (FromStringDeserializer.Std) FromStringDeserializer.findDeserializer(Class.class);
        Class<?> clazz = (Class<?>) classDeserializer.deserialize(JsonToken.VALUE_STRING, DeserializationContextStub, "java.lang.String");
        assertEquals(clazz, String.class);
    }
    
    @Test
    public void testJavaTypeDeserialization() {
        FromStringDeserializer.Std javaTypeDeserializer = (FromStringDeserializer.Std) FromStringDeserializer.findDeserializer(JavaType.class);
        JavaType javaType = (JavaType) javaTypeDeserializer.deserialize(JsonToken.VALUE_STRING, DeserializationContextStub, "java.util.List<java.lang.String>");
        assertEquals(javaType, new TypeReference<List<String>>() {});
    }
    
    @Test
    public void testCurrencyDeserialization() {
        FromStringDeserializer.Std currencyDeserializer = (FromStringDeserializer.Std) FromStringDeserializer.findDeserializer(Currency.class);
        Currency currency = (Currency) currencyDeserializer.deserialize(JsonToken.VALUE_STRING, DeserializationContextStub, "USD");
        assertEquals(currency, Currency.getInstance("USD"));
    }
    
    @Test
    public void testPatternDeserialization() {
        FromStringDeserializer.Std patternDeserializer = (FromStringDeserializer.Std) FromStringDeserializer.findDeserializer(Pattern.class);
        Pattern pattern = (Pattern) patternDeserializer.deserialize(JsonToken.VALUE_STRING, DeserializationContextStub, "[a-zA-Z]+");
        assertEquals(pattern, Pattern.compile("[a-zA-Z]+"));
    }
    
    @Test
    public void testLocaleDeserialization() {
        FromStringDeserializer.Std localeDeserializer = (FromStringDeserializer.Std) FromStringDeserializer.findDeserializer(Locale.class);
        Locale locale = (Locale) localeDeserializer.deserialize(JsonToken.VALUE_STRING, DeserializationContextStub, "en-US");
        assertEquals(locale, new Locale("en", "US"));
    }

    @Test
    public void testCharsetDeserialization() {
        FromStringDeserializer.Std charsetDeserializer = (FromStringDeserializer.Std) FromStringDeserializer.findDeserializer(Charset.class);
        Charset charset = (Charset) charsetDeserializer.deserialize(JsonToken.VALUE_STRING, DeserializationContextStub, "UTF-8");
        assertEquals(charset, Charset.forName("UTF-8"));
    }
    
    @Test
    public void testTimeZoneDeserialization() {
        FromStringDeserializer.Std timeZoneDeserializer = (FromStringDeserializer.Std) FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone timeZone = (TimeZone) timeZoneDeserializer.deserialize(JsonToken.VALUE_STRING, DeserializationContextStub, "GMT");
        assertEquals(timeZone, TimeZone.getTimeZone("GMT"));
    }
    
    @Test
    public void testInetAddressDeserialization() throws UnknownHostException {
        FromStringDeserializer.Std inetAddressDeserializer = (FromStringDeserializer.Std) FromStringDeserializer.findDeserializer(InetAddress.class);
        InetAddress inetAddress = (InetAddress) inetAddressDeserializer.deserialize(JsonToken.VALUE_STRING, DeserializationContextStub, "127.0.0.1");
        assertEquals(inetAddress, InetAddress.getByName("127.0.0.1"));
    }
    
    @Test
    public void testInetSocketAddressDeserialization() {
        FromStringDeserializer.Std inetSocketAddressDeserializer = (FromStringDeserializer.Std) FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        InetSocketAddress inetSocketAddress = (InetSocketAddress) inetSocketAddressDeserializer.deserialize(JsonToken.VALUE_STRING, DeserializationContextStub, "localhost:8080");
        assertEquals(inetSocketAddress, new InetSocketAddress("localhost", 8080));
    }
    
    @Test
    public void testStringBuilderDeserialization() {
        FromStringDeserializer.Std stringBuilderDeserializer = (FromStringDeserializer.Std) FromStringDeserializer.findDeserializer(StringBuilder.class);
        StringBuilder stringBuilder = (StringBuilder) stringBuilderDeserializer.deserialize(JsonToken.VALUE_STRING, DeserializationContextStub, "test");
        assertEquals(stringBuilder, new StringBuilder("test"));
    }
    
    @Test
    public void testInvalidDeserialization() {
        FromStringDeserializer.Std urlDeserializer = (FromStringDeserializer.Std) FromStringDeserializer.findDeserializer(URL.class);
        try {
            URL url = (URL) urlDeserializer.deserialize(JsonToken.VALUE_STRING, DeserializationContextStub, "invalid url");
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals(e.getMessage(), "not a valid textual representation, problem: no protocol: invalid url");
        }
    }
}

