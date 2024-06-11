package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Locale;

public class FromStringDeserializerTest {

    @Test
    public void testDeserializeFile() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<File> fileDeserializer = FromStringDeserializer.findDeserializer(File.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        File result = fileDeserializer.deserialize(mapper.getFactory().createParser("path/to/file.txt"), ctxt);
        assertTrue(result.exists());
    }

    @Test
    public void testDeserializeUrl() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<URL> urlDeserializer = FromStringDeserializer.findDeserializer(URL.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        URL result = urlDeserializer.deserialize(mapper.getFactory().createParser("http://example.com"), ctxt);
        assertNotNull(result);
        assertEquals("http", result.getProtocol());
    }

    @Test
    public void testDeserializeUri() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<URI> uriDeserializer = FromStringDeserializer.findDeserializer(URI.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        URI result = uriDeserializer.deserialize(mapper.getFactory().createParser("http://example.com/path/to/file.txt"), ctxt);
        assertNotNull(result);
        assertEquals("http", result.getScheme());
        assertEquals("/path/to/file.txt", result.getPath());
    }

    @Test
    public void testDeserializeClass() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Class<?>> classDeserializer = FromStringDeserializer.findDeserializer(String.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Class<?> result = classDeserializer.deserialize(mapper.getFactory().createParser("java.lang.String"), ctxt);
        assertEquals(String.class, result);
    }

    @Test
    public void testDeserializeJavaType() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<JavaType> javaTypeDeserializer = FromStringDeserializer.findDeserializer(JavaType.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JavaType result = javaTypeDeserializer.deserialize(mapper.getFactory().createParser("java.util.List<java.lang.String>"), ctxt);
        assertEquals(JavaType.constructType(List.class, String.class), result);
    }

    @Test
    public void testDeserializeCurrency() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Currency> currencyDeserializer = FromStringDeserializer.findDeserializer(Currency.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Currency result = currencyDeserializer.deserialize(mapper.getFactory().createParser("USD"), ctxt);
        assertEquals(Currency.getInstance("USD"), result);
    }

    @Test
    public void testDeserializePattern() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Pattern> patternDeserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Pattern result = patternDeserializer.deserialize(mapper.getFactory().createParser(".*"), ctxt);
        assertEquals(Pattern.compile(".*"), result);
    }

    @Test
    public void testDeserializeLocale() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Locale> localeDeserializer = FromStringDeserializer.findDeserializer(Locale.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Locale result = localeDeserializer.deserialize(mapper.getFactory().createParser("en_US"), ctxt);
        assertEquals(Locale.US, result);
    }

    @Test
    public void testDeserializeCharset() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Charset> charsetDeserializer = FromStringDeserializer.findDeserializer(Charset.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Charset result = charsetDeserializer.deserialize(mapper.getFactory().createParser("UTF-8"), ctxt);
        assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test
    public void testDeserializeTimeZone() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<TimeZone> timeZoneDeserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        TimeZone result = timeZoneDeserializer.deserialize(mapper.getFactory().createParser("UTC"), ctxt);
        assertEquals("UTC", result.getID());
    }

    @Test
    public void testDeserializeInetAddress() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<InetAddress> inetAddressDeserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        InetAddress result = inetAddressDeserializer.deserialize(mapper.getFactory().createParser("192.168.0.1"), ctxt);
        assertNotNull(result);
        assertEquals("192.168.0.1", result.getHostAddress());
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<InetSocketAddress> inetSocketAddressDeserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        InetSocketAddress result = inetSocketAddressDeserializer.deserialize(mapper.getFactory().createParser("[2001:db8:85a3:0:0:8a2e:370:7334]:12345"), ctxt);
        assertNotNull(result);
        assertEquals("2001:db8:85a3:0:0:8a2e:370:7334", result.getAddress().getHostAddress());
        assertEquals(12345, result.getPort());

        result = inetSocketAddressDeserializer.deserialize(mapper.getFactory().createParser("192.168.0.1:8080"), ctxt);
        assertNotNull(result);
        assertEquals("192.168.0.1", result.getAddress().getHostAddress());
        assertEquals(8080, result.getPort());
    }

    @Test
    public void testEmptyStringDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<File> fileDeserializer = FromStringDeserializer.findDeserializer(File.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        File result = fileDeserializer.deserialize(mapper.getFactory().createParser(""), ctxt);
        assertNull(result);

        fileDeserializer = FromStringDeserializer.findDeserializer(URI.class);
        result = fileDeserializer.deserialize(mapper.getFactory().createParser(""), ctxt);
        assertNotNull(result);
        assertEquals("", result.toString());

        fileDeserializer = FromStringDeserializer.findDeserializer(Locale.class);
        result = fileDeserializer.deserialize(mapper.getFactory().createParser(""), ctxt);
        assertEquals(Locale.ROOT, result);
    }
}