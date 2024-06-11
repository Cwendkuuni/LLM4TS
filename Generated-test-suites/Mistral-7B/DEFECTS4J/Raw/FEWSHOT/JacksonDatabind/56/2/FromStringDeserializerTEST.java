package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;

public class FromStringDeserializerTest {

    @SuppressWarnings("unchecked")
    private static <T> T deserialize(Class<T> valueType, String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        return mapper.readValue(new JsonFactory(), valueType).getValue();
    }

    @Test
    public void testFileDeserialization() throws IOException {
        String json = "\"/test/file.txt\"";
        File file = (File) deserialize(File.class, json);
        assertEquals("/test/file.txt", file.getAbsolutePath());
    }

    @Test
    public void testUrlDeserialization() throws IOException {
        String json = "\"http://www.example.com\"";
        URL url = (URL) deserialize(URL.class, json);
        assertEquals("http://www.example.com", url.toString());
    }

    @Test
    public void testUriDeserialization() throws IOException {
        String json = "\"http://www.example.com/path/to/file.txt\"";
        URI uri = (URI) deserialize(URI.class, json);
        assertEquals("http://www.example.com/path/to/file.txt", uri.toURI().toString());
    }

    @Test
    public void testClassDeserialization() throws IOException {
        String json = "\"java.lang.String\"";
        Class<?> clazz = (Class<?>) deserialize(Class.class, json);
        assertEquals(String.class, clazz);
    }

    @Test
    public void testJavaTypeDeserialization() throws IOException {
        String json = "\"class java.lang.String\"";
        JavaType javaType = (JavaType) deserialize(JavaType.class, json);
        assertEquals(String.class, javaType.getRawClass());
    }

    @Test
    public void testCurrencyDeserialization() throws IOException {
        String json = "\"USD\"";
        Currency currency = (Currency) deserialize(Currency.class, json);
        assertEquals("USD", currency.getCurrencyCode());
    }

    @Test
    public void testPatternDeserialization() throws IOException {
        String json = "\"/^[A-Za-z]+\\d{3,}$/\"";
        Pattern pattern = (Pattern) deserialize(Pattern.class, json);
        // Patterns cannot be directly compared
        assertInstanceOf(Pattern.class, deserialize(Pattern.class, pattern.pattern()));
    }

    @Test
    public void testLocaleDeserialization() throws IOException {
        String json = "\"en_US\"";
        Locale locale = (Locale) deserialize(Locale.class, json);
        assertEquals("en_US", locale.toString());
    }

    @Test
    public void testCharsetDeserialization() throws IOException {
        String json = "\"UTF-8\"";
        Charset charset = (Charset) deserialize(Charset.class, json);
        assertEquals("UTF-8", charset.name());
    }

    @Test
    public void testTimeZoneDeserialization() throws IOException {
        String json = "\"America/New_York\"";
        TimeZone timeZone = (TimeZone) deserialize(TimeZone.class, json);
        assertEquals("America/New_York", timeZone.getID());
    }

    @Test
    public void testInetAddressDeserialization() throws IOException {
        String json = "\"127.0.0.1\"";
        InetAddress inetAddress = (InetAddress) deserialize(InetAddress.class, json);
        assertEquals("127.0.0.1", inetAddress.getHostAddress());
    }

    @Test
    public void testInetSocketAddressDeserialization() throws IOException {
        String json = "[\"127.0.0.1\", 8080]";
        InetSocketAddress inetSocketAddress = (InetSocketAddress) deserialize(InetSocketAddress.class, json);
        assertEquals("127.0.0.1", inetSocketAddress.getAddress().getHostAddress());
        assertEquals(8080, inetSocketAddress.getPort());
    }

    @Test
    public void testEmptyStringDeserialization() throws IOException {
        String json = "";

        // File
        assertNull(deserialize(File.class, json));

        // URL
        assertNull(deserialize(URL.class, json));

        // URI
        assertNull(deserialize(URI.class, json));

        // Class
        assertNull(deserialize(Class.class, json));

        // JavaType
        assertNull(deserialize(JavaType.class, json));

        // Currency
        assertNull(deserialize(Currency.class, json));

        // Pattern
        assertNull(deserialize(Pattern.class, json));

        // Locale
        assertNull(deserialize(Locale.class, json));

        // Charset
        assertNull(deserialize(Charset.class, json));

        // TimeZone
        assertNull(deserialize(TimeZone.class, json));

        // InetAddress
        assertNull(deserialize(InetAddress.class, json));

        // InetSocketAddress
        assertNull(deserialize(InetSocketAddress.class, json));
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidDeserialization() throws IOException {
        String json = "\"InvalidType\"";
        deserialize(Object.class, json);
    }
}