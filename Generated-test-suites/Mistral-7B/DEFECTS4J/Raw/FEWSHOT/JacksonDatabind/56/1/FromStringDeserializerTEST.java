package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FromStringDeserializerTest {

    @SuppressWarnings("unchecked")
    private static <T> T deserialize(Class<T> clazz, String value, DeserializationContext ctxt) throws JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        return (T) mapper.readValue(mapper.getTextFactory().writeValueAsString(new Object() {
            String value = value;
        }).getBytes(), clazz, ctxt);
    }

    @Test
    public void testDeserialize_File() throws IOException, JsonProcessingException {
        File file = deserialize(File.class, "/path/to/file.txt", null);
        assertTrue(file.exists());
    }

    @Test
    public void testDeserialize_URL() throws IOException, JsonProcessingException {
        URL url = deserialize(URL.class, "http://example.com", null);
        assertEquals("http://example.com", url.toString());
    }

    @Test
    public void testDeserialize_URI() throws IOException, JsonProcessingException {
        URI uri = deserialize(URI.class, "http://example.com", null);
        assertEquals("http", uri.getScheme());
        assertEquals("/example.com", uri.getPath());
    }

    @Test
    public void testDeserialize_Class() throws IOException, JsonProcessingException {
        Class<?> clazz = String.class;
        assertEquals(clazz, deserialize(clazz, "java.lang.String", null));
    }

    @Test
    public void testDeserialize_JavaType() throws IOException, JsonProcessingException {
        JavaType javaType = null;
        assertEquals(JavaType.class, deserialize(JavaType.class, "com.fasterxml.jackson.databind.JavaType", null));
        assertEquals(Integer.class, deserialize(JavaType.class, "[I", null).getType());
        javaType = new JavaType(Integer.class);
        assertEquals(javaType, deserialize(JavaType.class, javaType.toString(), null));
    }

    @Test
    public void testDeserialize_Currency() throws IOException, JsonProcessingException {
        Currency currency = deserialize(Currency.class, "USD", null);
        assertEquals("USD", currency.getCurrencyCode());
    }

    @Test
    public void testDeserialize_Pattern() throws IOException, JsonProcessingException {
        Pattern pattern = deserialize(Pattern.class, "\\d+", null);
        assertEquals("\\d+", pattern.pattern());
    }

    @Test
    public void testDeserialize_Locale() throws IOException, JsonProcessingException {
        Locale locale = deserialize(Locale.class, "en_US", null);
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());

        locale = deserialize(Locale.class, "en_US_POSIX", null);
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());
        assertEquals("POSIX", locale.getVariant());
    }

    @Test
    public void testDeserialize_Charset() throws IOException, JsonProcessingException {
        Charset charset = deserialize(Charset.class, "UTF-8", null);
        assertEquals("UTF-8", charset.name());
    }

    @Test
    public void testDeserialize_TimeZone() throws IOException, JsonProcessingException {
        TimeZone timeZone = deserialize(TimeZone.class, "UTC", null);
        assertEquals("UTC", timeZone.getID());
    }

    @Test
    public void testDeserialize_InetAddress() throws IOException, JsonProcessingException {
        InetAddress inetAddress = deserialize(InetAddress.class, "127.0.0.1", null);
        assertNotNull(inetAddress);
    }

    @Test
    public void testDeserialize_InetSocketAddress() throws IOException, JsonProcessingException {
        InetSocketAddress inetSocketAddress = deserialize(InetSocketAddress.class, "[::1]:8080", null);
        assertEquals("[::1]", inetSocketAddress.getAddress().getHostAddress());
        assertEquals(8080, inetSocketAddress.getPort());

        inetSocketAddress = deserialize(InetSocketAddress.class, "127.0.0.1:8080", null);
        assertEquals("127.0.0.1", inetSocketAddress.getAddress().getHostAddress());
        assertEquals(8080, inetSocketAddress.getPort());
    }
}