package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.net.URI;
import java.util.Locale;

public class FromStringDeserializerTest {

    private ObjectMapper mapper;

    @SuppressWarnings("unchecked")
    @Before
    public void setup() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testFileDeserialization() throws IOException {
        String json = "\"/path/to/file.txt\"";
        File file = mapper.readValue(json, FromStringDeserializer.File.class);
        assertNotNull(file);
        assertTrue(file.exists());
    }

    @Test
    public void testUrlDeserialization() throws IOException {
        String json = "\"http://example.com\"";
        URL url = mapper.readValue(json, FromStringDeserializer.URL.class);
        assertNotNull(url);
        assertEquals("http://example.com", url.toString());
    }

    @Test
    public void testUriDeserialization() throws IOException {
        String json = "\"http://user:password@example.com:8080/path?query=value#fragment\"";
        URI uri = mapper.readValue(json, FromStringDeserializer.URI.class);
        assertNotNull(uri);
        assertEquals("http", uri.getScheme());
        assertEquals("user:password", uri.getUserInfo());
        assertEquals("example.com", uri.getHost());
        assertEquals(8080, uri.getPort());
        assertEquals("/path", uri.getPath());
        assertEquals("query=value", uri.getQuery());
        assertEquals("fragment", uri.getFragment());
    }

    @Test
    public void testClassDeserialization() throws IOException, ClassNotFoundException {
        String json = "\"java.util.Locale\"";
        Class<?> clazz = mapper.readValue(json, FromStringDeserializer.Class.class);
        assertNotNull(clazz);
        assertEquals(Locale.class, clazz);
    }

    @Test
    public void testJavaTypeDeserialization() throws IOException {
        String json = "\"com.example.MyClass\"";
        JavaType javaType = mapper.readValue(json, FromStringDeserializer.JavaType.class);
        assertNotNull(javaType);
        assertEquals(MyClass.class, javaType.getRawClass());
    }

    @Test
    public void testCurrencyDeserialization() throws IOException {
        String json = "\"EUR\"";
        Currency currency = mapper.readValue(json, FromStringDeserializer.Currency.class);
        assertNotNull(currency);
        assertEquals("EUR", currency.getCurrencyCode());
    }

    @Test
    public void testPatternDeserialization() throws IOException {
        String json = "\"/[a-z]+(\\.w+)*$/i\"";
        Pattern pattern = mapper.readValue(json, FromStringDeserializer.Pattern.class);
        assertNotNull(pattern);
        assertTrue(pattern.matcher("example").matches());
    }

    @Test
    public void testLocaleDeserialization() throws IOException {
        String json = "\"en_US\"";
        Locale locale = mapper.readValue(json, FromStringDeserializer.Locale.class);
        assertNotNull(locale);
        assertEquals("en_US", locale.toString());
    }

    @Test
    public void testCharsetDeserialization() throws IOException {
        String json = "\"UTF-8\"";
        Charset charset = mapper.readValue(json, FromStringDeserializer.Charset.class);
        assertNotNull(charset);
        assertEquals("UTF-8", charset.name());
    }

    @Test
    public void testTimeZoneDeserialization() throws IOException {
        String json = "\"America/Los_Angeles\"";
        TimeZone timeZone = mapper.readValue(json, FromStringDeserializer.TimeZone.class);
        assertNotNull(timeZone);
        assertEquals("America/Los_Angeles", timeZone.getID());
    }

    @Test
    public void testInetAddressDeserialization() throws IOException {
        String json = "\"192.168.1.1\"";
        InetAddress inetAddress = mapper.readValue(json, FromStringDeserializer.InetAddress.class);
        assertNotNull(inetAddress);
        assertEquals("192.168.1.1", inetAddress.getHostAddress());
    }

    @Test
    public void testInetSocketAddressDeserialization() throws IOException {
        String json = "\"[255.255.255.255]:80\"";
        InetSocketAddress inetSocketAddress = mapper.readValue(json, FromStringDeserializer.InetSocketAddress.class);
        assertNotNull(inetSocketAddress);
        assertEquals("[255.255.255.255]", inetSocketAddress.getAddress().getHostAddress());
        assertEquals(80, inetSocketAddress.getPort());

        json = "{\"host\":\"example.com\",\"port\":1234}";
        inetSocketAddress = mapper.readValue(json, FromStringDeserializer.InetSocketAddress.class);
        assertNotNull(inetSocketAddress);
        assertEquals("example.com", inetSocketAddress.getAddress().getHostName());
        assertEquals(1234, inetSocketAddress.getPort());
    }

    @Test
    public void testEmptyStringDeserialization() throws IOException {
        String json = "\"\"";
        URI uri = mapper.readValue(json, FromStringDeserializer.URI.class);
        assertEquals(URI.class, uri.getClass());
        assertTrue(uri.toString().isEmpty());
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidJsonException() throws IOException {
        String json = "\"{invalid-json\"";
        mapper.readValue(json, FromStringDeserializer.Object.class);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidTypeException() throws IOException {
        String json = "\"invalid-type\"";
        mapper.readValue(json, FromStringDeserializer.Object.class);
    }
}