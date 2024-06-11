package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.node.TextNode;

public class FromStringDeserializerTests {

    @SuppressWarnings("unchecked")
    private static <T> T createTestInstance(Class<T> clazz) {
        try {
            return (T) FromStringDeserializer.findDeserializer(clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testFindDeserializer() {
        Arrays.stream(FromStringDeserializer.types())
                .forEach(clazz -> {
                    FromStringDeserializer<Object> deserializer = createTestInstance(clazz);
                    assertEquals(clazz, deserializer._valueClass);
                });
    }

    @Test
    public void testDeserializeFile() throws IOException, URISyntaxException {
        String json = "\"/tmp/test.txt\"";
        File fileDeserializer = createTestInstance(File.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = mapper.getFactory().createParser(new TextNode(json));
        File file = fileDeserializer.deserialize(jsonParser, new TestContext());
        assertEquals("/tmp/test.txt", file.getAbsolutePath());
    }

    @Test
    public void testDeserializeURL() throws IOException {
        String json = "\"http://localhost:8080/test\"";
        URL urlDeserializer = createTestInstance(URL.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = mapper.getFactory().createParser(new TextNode(json));
        URL url = urlDeserializer.deserialize(jsonParser, new TestContext());
        assertEquals("http://localhost:8080/test", url.toString());
    }

    @Test
    public void testDeserializeURI() throws IOException, URISyntaxException {
        String json = "\"http://localhost:8080/test?param=value\"";
        URI uriDeserializer = createTestInstance(URI.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = mapper.getFactory().createParser(new TextNode(json));
        URI uri = uriDeserializer.deserialize(jsonParser, new TestContext());
        assertEquals("http", uri.getScheme());
        assertEquals("/test", uri.getPath());
        assertEquals("param=value", uri.getQuery());
    }

    @Test
    public void testDeserializeClass() throws InstantiationException, IllegalAccessException {
        String json = "\"java.util.Date\"";
        Class<?> clazzDeserializer = createTestInstance(Class.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = mapper.getFactory().createParser(new TextNode(json));
        Class<?> clazz = (Class<?>) clazzDeserializer.deserialize(jsonParser, new TestContext());
        assertEquals(java.util.Date.class, clazz);
    }

    @Test
    public void testDeserializeJavaType() throws JsonProcessingException {
        String json = "\"java.util.List<java.lang.String>\"".replace("\"", "\\\"");
        JavaType javaTypeDeserializer = createTestInstance(JavaType.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = mapper.getFactory().createParser(new TextNode(json));
        JavaType javaType = javaTypeDeserializer.deserialize(jsonParser, new TestContext());
        assertEquals(java.util.List.class, javaType.getRawClass());
        assertEquals(java.lang.String.class, javaType.getContentType().getRawClass());
    }

    @Test
    public void testDeserializeCurrency() throws JsonProcessingException {
        String json = "\"USD\"";
        Currency currencyDeserializer = createTestInstance(Currency.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = mapper.getFactory().createParser(new TextNode(json));
        Currency currency = currencyDeserializer.deserialize(jsonParser, new TestContext());
        assertEquals(Currency.getInstance("USD"), currency);
    }

    @Test
    public void testDeserializePattern() throws JsonProcessingException {
        String json = "\"[A-Za-z0-9_]+(\\.[A-Za-z0-9_]+)*\"";
        Pattern patternDeserializer = createTestInstance(Pattern.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = mapper.getFactory().createParser(new TextNode(json));
        Pattern pattern = patternDeserializer.deserialize(jsonParser, new TestContext());
        assertEquals(Pattern.compile("[A-Za-z0-9_]+(\\.[A-Za-z0-9_]+)*"), pattern);
    }

    @Test
    public void testDeserializeLocale() throws JsonProcessingException {
        String json = "\"en_US\"";
        Locale localeDeserializer = createTestInstance(Locale.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = mapper.getFactory().createParser(new TextNode(json));
        Locale locale = localeDeserializer.deserialize(jsonParser, new TestContext());
        assertEquals(Locale.US, locale);
    }

    @Test
    public void testDeserializeCharset() throws JsonProcessingException {
        String json = "\"UTF-8\"";
        Charset charsetDeserializer = createTestInstance(Charset.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = mapper.getFactory().createParser(new TextNode(json));
        Charset charset = charsetDeserializer.deserialize(jsonParser, new TestContext());
        assertEquals(Charset.forName("UTF-8"), charset);
    }

    @Test
    public void testDeserializeTimeZone() throws JsonProcessingException {
        String json = "\"America/Los_Angeles\"";
        TimeZone timeZoneDeserializer = createTestInstance(TimeZone.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = mapper.getFactory().createParser(new TextNode(json));
        TimeZone timeZone = timeZoneDeserializer.deserialize(jsonParser, new TestContext());
        assertEquals(TimeZone.getTimeZone("America/Los_Angeles"), timeZone);
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        String jsonIpv4 = "\"192.168.1.1\"";
        String jsonIpv6 = "[\"2001:0db8:85a3:0000:0000:8a2e:0370:7334\"]";
        InetAddress inetAddressDeserializer = createTestInstance(InetAddress.class);
        ObjectMapper mapper = new ObjectMapper();

        JsonParser jsonParserIpv4 = mapper.getFactory().createParser(new TextNode(jsonIpv4));
        InetAddress ipv4Address = inetAddressDeserializer.deserialize(jsonParserIpv4, new TestContext());
        assertEquals(InetAddress.getByName("192.168.1.1"), ipv4Address);

        JsonParser jsonParserIpv6 = mapper.getFactory().createParser(new TextNode(jsonIpv6));
        InetAddress ipv6Address = inetAddressDeserializer.deserialize(jsonParserIpv6, new TestContext());
        assertEquals(InetAddress.getByName("2001:0db8:85a3:0:0:8a2e:370:7334"), ipv6Address);
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException, URISyntaxException {
        String jsonIpv4 = "\"192.168.1.1:8080\"";
        String jsonIpv6 = "[\"2001:0db8:85a3:0:0:8a2e:370:7334:8080\"]";
        InetSocketAddress inetSocketAddressDeserializer = createTestInstance(InetSocketAddress.class);

        ObjectMapper mapper = new ObjectMapper();

        JsonParser jsonParserIpv4 = mapper.getFactory().createParser(new TextNode(jsonIpv4));
        InetSocketAddress ipv4SocketAddress = inetSocketAddressDeserializer.deserialize(jsonParserIpv4, new TestContext());
        assertEquals(new InetSocketAddress("192.168.1.1", 8080), ipv4SocketAddress);

        JsonParser jsonParserIpv6 = mapper.getFactory().createParser(new TextNode(jsonIpv6));
        InetSocketAddress ipv6SocketAddress = inetSocketAddressDeserializer.deserialize(jsonParserIpv6, new TestContext());
        assertEquals(new InetSocketAddress("2001:0db8:85a3:0:0:8a2e:370:7334", 8080), ipv6SocketAddress);
    }

    @Test
    public void testDeserializeStringBuilder() throws JsonProcessingException {
        String json = "\"Hello World!\"";
        StringBuilder stringBuilderDeserializer = createTestInstance(StringBuilder.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = mapper.getFactory().createParser(new TextNode(json));
        StringBuilder stringBuilder = stringBuilderDeserializer.deserialize(jsonParser, new TestContext());
        assertEquals("Hello World!", stringBuilder.toString());
    }

    @Test
    public void testDeserializeEmptyString() throws JsonProcessingException {
        String json = "\"\"";
        FromStringDeserializer<Object> emptyStringDeserializer = createTestInstance(Object.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = mapper.getFactory().createParser(new TextNode(json));
        Object result = emptyStringDeserializer.deserialize(jsonParser, new TestContext());
        assertNull(result);
    }

    @Test
    public void testDeserializeInvalidString() throws IOException, JsonMappingException {
        String json = "\"not a valid representation\"";
        FromStringDeserializer<Object> objectDeserializer = createTestInstance(Object.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = mapper.getFactory().createParser(new TextNode(json));
        JsonMappingException mappingException = assertThrows(JsonMappingException.class, () -> {
            objectDeserializer.deserialize(jsonParser, new TestContext());
        });
        assertEquals("not a valid representation", mappingException.getMessage());
    }

    @Test
    public void testDeserializeInvalidUrl() throws IOException, URISyntaxException {
        String json = "\"invalid:/test\"";
        URL urlDeserializer = createTestInstance(URL.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = mapper.getFactory().createParser(new TextNode(json));
        assertThrows(MalformedURLException.class, () -> {
            urlDeserializer.deserialize(jsonParser, new TestContext());
        });
    }

    @Test
    public void testDeserializeInvalidLocale() throws JsonProcessingException {
        String json = "\"en_US_Invalid\"";
        Locale localeDeserializer = createTestInstance(Locale.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = mapper.getFactory().createParser(new TextNode(json));
        JsonMappingException mappingException = assertThrows(JsonMappingException.class, () -> {
            localeDeserializer.deserialize(jsonParser, new TestContext());
        });
        assertEquals("Don't know how to convert embedded Object of type java.util.Locale[id=en_US_Invalid] into java.lang.Object", mappingException.getMessage());
    }

    @Test
    public void testDeserializeInvalidCharset() throws JsonProcessingException {
        String json = "\"InvalidCharset\"";
        Charset charsetDeserializer = createTestInstance(Charset.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = mapper.getFactory().createParser(new TextNode(json));
        assertThrows(InvalidFormatException.class, () -> {
            charsetDeserializer.deserialize(jsonParser, new TestContext());
        });
    }

    @Test
    public void testDeserializeInvalidTimeZone() throws JsonProcessingException {
        String json = "\"InvalidTimeZone\"";
        TimeZone timeZoneDeserializer = createTestInstance(TimeZone.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = mapper.getFactory().createParser(new TextNode(json));
        assertThrows(InvalidFormatException.class, () -> {
            timeZoneDeserializer.deserialize(jsonParser, new TestContext());
        });
    }

    @Test
    public void testDeserializeInvalidInetAddress() throws IOException {
        String json = "\"InvalidIPv4Address\"";
        InetAddress inetAddressDeserializer = createTestInstance(InetAddress.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = mapper.getFactory().createParser(new TextNode(json));
        assertThrows(IOException.class, () -> {
            inetAddressDeserializer.deserialize(jsonParser, new TestContext());
        });
    }

    @Test
    public void testDeserializeInvalidInetSocketAddress() throws IOException, URISyntaxException {
        String json = "\"InvalidIPv6Address:8080\"";
        InetSocketAddress inetSocketAddressDeserializer = createTestInstance(InetSocketAddress.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = mapper.getFactory().createParser(new TextNode(json));
        assertThrows(IOException.class, () -> {
            inetSocketAddressDeserializer.deserialize(jsonParser, new TestContext());
        });
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public void reportMappingError(String msg) {
            throw new RuntimeException(msg);
        }

        @Override
        public void reportMappingException(JsonMappingException e) {
            throw new RuntimeException(e);
        }

        @Override
        public void reportInvalidToken(Class<?> tokenClass, int line, int col) {
            throw new RuntimeException("Unexpected token: " + tokenClass);
        }

        @Override
        public void handleUnexpectedToken(Class<?> tokenClass) throws IOException {
            throw new RuntimeException("Unexpected token: " + tokenClass);
        }

        @Override
        public void reportMalformedTree(String msg) {
            throw new RuntimeException(msg);
        }

        @Override
        public void reportMalformedTree(String msg, Throwable cause) {
            throw new RuntimeException(msg, cause);
        }

        @Override
        public void handleUnexpectedValue(Object value, Class<?> expectedType) throws IOException {
            throw new RuntimeException("Unexpected value of type: " + value.getClass());
        }

        @Override
        public void handleUnknownProperty(String name, Object bean) throws IOException {
            throw new RuntimeException("Unknown property: " + name);
        }

        @Override
        public JsonParser _parser() {
            return null;
        }
    }
}