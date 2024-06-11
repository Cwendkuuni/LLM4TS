import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.*;

public class FromStringDeserializerTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testDeserializeFile() throws IOException {
        String json = "{\"@type\":\"File\",\"path\":\"test.txt\"}";
        FromStringDeserializer<File> deserializer = new FromStringDeserializer<File>(File.class);
        File file = deserializer.deserialize(mapper.readTree(json), new DeserializationContext(mapper.getConfiguration()));
        assertEquals("test.txt", file.getAbsolutePath());
    }

    @Test
    public void testDeserializeUrl() throws IOException {
        String json = "{\"@type\":\"URL\",\"string\":\"http://example.com\"}";
        FromStringDeserializer<URL> deserializer = new FromStringDeserializer<URL>(URL.class);
        URL url = deserializer.deserialize(mapper.readTree(json), new DeserializationContext(mapper.getConfiguration()));
        assertEquals("http://example.com", url.toString());
    }

    @Test
    public void testDeserializeUri() throws IOException {
        String json = "{\"@type\":\"URI\",\"string\":\"http://user:pass@example.com:8080/path?query#fragment\"}";
        FromStringDeserializer<URI> deserializer = new FromStringDeserializer<URI>(URI.class);
        URI uri = deserializer.deserialize(mapper.readTree(json), new DeserializationContext(mapper.getConfiguration()));
        assertEquals("http", uri.getScheme());
        assertEquals("user:pass", uri.getUserInfo());
        assertEquals("example.com", uri.getHost());
        assertEquals(8080, uri.getPort());
        assertEquals("/path", uri.getPath());
        assertEquals("query", uri.getQuery());
        assertEquals("fragment", uri.getFragment());
    }

    @Test
    public void testDeserializeClass() throws IOException {
        String json = "{\"@type\":\"Class\",\"name\":\"java.lang.String\"}";
        FromStringDeserializer<Class<?>> deserializer = new FromStringDeserializer<Class<?>>(Class.class);
        Class<?> clazz = deserializer.deserialize(mapper.readTree(json), new DeserializationContext(mapper.getConfiguration()));
        assertEquals(String.class, clazz);
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        String json = "{\"@type\":\"JavaType\",\"typeName\":\"java.util.List<java.lang.String>\",\"rawClass\":\"[Ljava.lang.String;\"}";
        FromStringDeserializer<JavaType> deserializer = new FromStringDeserializer<JavaType>(JavaType.class);
        JavaType javaType = deserializer.deserialize(mapper.readTree(json), new DeserializationContext(mapper.getConfiguration()));
        assertEquals("java.util.List<java.lang.String>", javaType.toString());
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        String json = "{\"@type\":\"Currency\",\"currencyCode\":\"USD\"}";
        FromStringDeserializer<Currency> deserializer = new FromStringDeserializer<Currency>(Currency.class);
        Currency currency = deserializer.deserialize(mapper.readTree(json), new DeserializationContext(mapper.getConfiguration()));
        assertEquals(Currency.getInstance("USD"), currency);
    }

    @Test
    public void testDeserializePattern() throws IOException {
        String json = "{\"@type\":\"Pattern\",\"pattern\":\"\\w+\\.\\w+\\.\\w+\"}";
        FromStringDeserializer<Pattern> deserializer = new FromStringDeserializer<Pattern>(Pattern.class);
        Pattern pattern = deserializer.deserialize(mapper.readTree(json), new DeserializationContext(mapper.getConfiguration()));
        String regex = pattern.toString();
        assertTrue(Pattern.matches("\\w+\\.\\w+\\.\\w+", "test.example.com"));
        assertFalse(Pattern.matches(regex, "test-example.com"));
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        String json = "{\"@type\":\"Locale\",\"language\":\"en\",\"country\":\"US\"}";
        FromStringDeserializer<Locale> deserializer = new FromStringDeserializer<Locale>(Locale.class);
        Locale locale = deserializer.deserialize(mapper.readTree(json), new DeserializationContext(mapper.getConfiguration()));
        assertEquals("en_US", locale.getLanguage());
        assertEquals("US", locale.getCountry());
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        String json = "{\"@type\":\"Charset\",\"charsetName\":\"UTF-8\"}";
        FromStringDeserializer<Charset> deserializer = new FromStringDeserializer<Charset>(Charset.class);
        Charset charset = deserializer.deserialize(mapper.readTree(json), new DeserializationContext(mapper.getConfiguration()));
        assertEquals("UTF-8", charset.name());
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        String json = "{\"@type\":\"TimeZone\",\"id\":\"America/Los_Angeles\"}";
        FromStringDeserializer<TimeZone> deserializer = new FromStringDeserializer<TimeZone>(TimeZone.class);
        TimeZone timeZone = deserializer.deserialize(mapper.readTree(json), new DeserializationContext(mapper.getConfiguration()));
        assertEquals("America/Los_Angeles", timeZone.getID());
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        String jsonIpv4 = "{\"@type\":\"InetAddress\",\"address\":\"192.168.1.1\"}";
        String jsonIpv6 = "{\"@type\":\"InetAddress\",\"address\":\"2001:0db8:85a3:0000:0000:8a2e:0370:7334\"}";

        FromStringDeserializer<InetAddress> deserializer = new FromStringDeserializer<InetAddress>(InetAddress.class);

        InetAddress ipv4Address = deserializer.deserialize(mapper.readTree(jsonIpv4), new DeserializationContext(mapper.getConfiguration()));
        InetAddress ipv6Address = deserializer.deserialize(mapper.readTree(jsonIpv6), new DeserializationContext(mapper.getConfiguration()));

        assertEquals("192.168.1.1", ipv4Address.getHostAddress());
        assertEquals("2001:0db8:85a3:0:0:8a2e:370:7334", ipv6Address.getHostAddress());
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        String jsonIpv4 = "{\"@type\":\"InetSocketAddress\",\"address\":\"192.168.1.1\",\"port\":80}";
        String jsonIpv6 = "{\"@type\":\"InetSocketAddress\",\"address\":\"2001:0db8:85a3:0:0:8a2e:370:7334\",\"port\":8080}";

        FromStringDeserializer<InetSocketAddress> deserializer = new FromStringDeserializer<InetSocketAddress>(InetSocketAddress.class);

        InetSocketAddress ipv4SocketAddress = deserializer.deserialize(mapper.readTree(jsonIpv4), new DeserializationContext(mapper.getConfiguration()));
        InetSocketAddress ipv6SocketAddress = deserializer.deserialize(mapper.readTree(jsonIpv6), new DeserializationContext(mapper.getConfiguration()));

        assertEquals("192.168.1.1", ipv4SocketAddress.getAddress().getHostAddress());
        assertEquals("2001:0db8:85a3:0:0:8a2e:370:7334", ipv6SocketAddress.getAddress().getHostAddress());
        assertEquals(80, ipv4SocketAddress.getPort());
        assertEquals(8080, ipv6SocketAddress.getPort());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidJson() throws IOException {
        String json = "{\"@type\":\"InvalidType\"}";
        FromStringDeserializer<Object> deserializer = new FromStringDeserializer<Object>(Object.class);
        deserializer.deserialize(mapper.readTree(json), new DeserializationContext(mapper.getConfiguration()));
        fail("Expected JsonMappingException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeInvalidType() throws IOException {
        String json = "{\"@type\":\"JSONParser\"}";
        FromStringDeserializer<Object> deserializer = new FromStringDeserializer<Object>(Object.class);
        deserializer.deserialize(mapper.readTree(json), new DeserializationContext(mapper.getConfiguration()));
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeNullValue() throws IOException {
        String json = "{\"@type\":\"File\",\"path\":null}";
        FromStringDeserializer<File> deserializer = new FromStringDeserializer<File>(File.class);
        deserializer.deserialize(mapper.readTree(json), new DeserializationContext(mapper.getConfiguration()));
        fail("Expected JsonMappingException");
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeEmptyString() throws IOException {
        String json = "{\"@type\":\"URL\",\"string\":\"\"}";
        FromStringDeserializer<URL> deserializer = new FromStringDeserializer<URL>(URL.class);
        deserializer.deserialize(mapper.readTree(json), new DeserializationContext(mapper.getConfiguration()));
        fail("Expected JsonMappingException");
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeMalformedUrl() throws IOException {
        String json = "{\"@type\":\"URL\",\"string\":\"http://user:pass@example.com:80/path?query#fragment::/other/path\"}";
        FromStringDeserializer<URL> deserializer = new FromStringDeserializer<URL>(URL.class);
        deserializer.deserialize(mapper.readTree(json), new DeserializationContext(mapper.getConfiguration()));
        fail("Expected JsonMappingException");
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeMalformedCurrencyCode() throws IOException {
        String json = "{\"@type\":\"Currency\",\"currencyCode\":\"EUR$\"}";
        FromStringDeserializer<Currency> deserializer = new FromStringDeserializer<Currency>(Currency.class);
        deserializer.deserialize(mapper.readTree(json), new DeserializationContext(mapper.getConfiguration()));
        fail("Expected JsonMappingException");
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeMalformedPattern() throws IOException {
        String json = "{\"@type\":\"Pattern\",\"pattern\":\"\\w+\\.\\w+\\.\\w+\\.\\w+\"}";
        FromStringDeserializer<Pattern> deserializer = new FromStringDeserializer<Pattern>(Pattern.class);
        deserializer.deserialize(mapper.readTree(json), new DeserializationContext(mapper.getConfiguration()));
        fail("Expected JsonMappingException");
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeMalformedLocale() throws IOException {
        String json = "{\"@type\":\"Locale\",\"language\":\"en_US_Country\"}";
        FromStringDeserializer<Locale> deserializer = new FromStringDeserializer<Locale>(Locale.class);
        deserializer.deserialize(mapper.readTree(json), new DeserializationContext(mapper.getConfiguration()));
        fail("Expected JsonMappingException");
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeMalformedCharsetName() throws IOException {
        String json = "{\"@type\":\"Charset\",\"charsetName\":\"UTF-8_Invalid\"}";
        FromStringDeserializer<Charset> deserializer = new FromStringDeserializer<Charset>(Charset.class);
        deserializer.deserialize(mapper.readTree(json), new DeserializationContext(mapper.getConfiguration()));
        fail("Expected JsonMappingException");
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeMalformedTimeZoneId() throws IOException {
        String json = "{\"@type\":\"TimeZone\",\"id\":\"America/Los_Angeles_Invalid\"}";
        FromStringDeserializer<TimeZone> deserializer = new FromStringDeserializer<TimeZone>(TimeZone.class);
        deserializer.deserialize(mapper.readTree(json), new DeserializationContext(mapper.getConfiguration()));
        fail("Expected JsonMappingException");
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeMalformedInetAddress() throws IOException {
        String jsonIpv4 = "{\"@type\":\"InetAddress\",\"address\":\"192.168.1.1.1\"}";
        String jsonIpv6 = "{\"@type\":\"InetAddress\",\"address\":\"2001:0db8:85a3:0:0:8a2e:370:7334:1\"}";

        FromStringDeserializer<InetAddress> deserializer = new FromStringDeserializer<InetAddress>(InetAddress.class);

        deserializer.deserialize(mapper.readTree(jsonIpv4), new DeserializationContext(mapper.getConfiguration()));
        deserializer.deserialize(mapper.readTree(jsonIpv6), new DeserializationContext(mapper.getConfiguration()));
        fail("Expected JsonMappingException");
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeMalformedInetSocketAddress() throws IOException {
        String jsonIpv4 = "{\"@type\":\"InetSocketAddress\",\"address\":\"192.168.1.1\",\"port\":80,\"invalid\":\"1\"}";
        String jsonIpv6 = "{\"@type\":\"InetSocketAddress\",\"address\":\"2001:0db8:85a3:0:0:8a2e:370:7334\",\"port\":8080,\"invalid\":\"1\"}";

        FromStringDeserializer<InetSocketAddress> deserializer = new FromStringDeserializer<InetSocketAddress>(InetSocketAddress.class);

        deserializer.deserialize(mapper.readTree(jsonIpv4), new DeserializationContext(mapper.getConfiguration()));
        deserializer.deserialize(mapper.readTree(jsonIpv6), new DeserializationContext(mapper.getConfiguration()));
        fail("Expected JsonMappingException");
    }
}