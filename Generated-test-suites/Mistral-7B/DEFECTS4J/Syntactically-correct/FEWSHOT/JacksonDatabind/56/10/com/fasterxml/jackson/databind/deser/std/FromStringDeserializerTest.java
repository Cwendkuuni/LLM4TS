package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;
import java.util.Locale;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

public class FromStringDeserializerTest {

    @Test
    public void testDeserializeFile() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<File> fileDeserializer = FromStringDeserializer.findDeserializer(File.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonNode node = mapper.readTree("\"/path/to/file.txt\"");
        File file = fileDeserializer.deserialize(mapper.treeToValue(node, File.class), ctxt);
        assertEquals("/path/to/file.txt", file.getAbsolutePath());
    }

    @Test
    public void testDeserializeURL() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<URL> urlDeserializer = FromStringDeserializer.findDeserializer(URL.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonNode node = mapper.readTree("\"http://example.com\"");
        URL url = urlDeserializer.deserialize(mapper.treeToValue(node, URL.class), ctxt);
        assertEquals("http://example.com", url.toString());
    }

    @Test
    public void testDeserializeURI() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<URI> uriDeserializer = FromStringDeserializer.findDeserializer(URI.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonNode node = mapper.readTree("\"uri://example.com\"");
        URI uri = uriDeserializer.deserialize(mapper.treeToValue(node, URI.class), ctxt);
        assertEquals("uri://example.com", uri.toString());
    }

    @Test
    public void testDeserializeClass() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Class<?>> classDeserializer = FromStringDeserializer.findDeserializer(Class.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonNode node = mapper.readTree("\"java.lang.String\"");
        Class<?> clazz = classDeserializer.deserialize(mapper.treeToValue(node, Class.class), ctxt);
        assertEquals(String.class, clazz);
    }

    @Test
    public void testDeserializeJavaType() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<JavaType> javaTypeDeserializer = FromStringDeserializer.findDeserializer(JavaType.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonNode node = mapper.readTree("\"com.example:MyClass\"");
        JavaType javaType = javaTypeDeserializer.deserialize(mapper.treeToValue(node, JavaType.class), ctxt);
        assertTrue(javaType.isContainerType());
        assertEquals("com.example.MyClass", javaType.getTypeName().toString());
    }

    @Test
    public void testDeserializeCurrency() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Currency> currencyDeserializer = FromStringDeserializer.findDeserializer(Currency.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonNode node = mapper.readTree("\"USD\"");
        Currency currency = currencyDeserializer.deserialize(mapper.treeToValue(node, Currency.class), ctxt);
        assertEquals("USD", currency.getCurrencyCode());
    }

    @Test
    public void testDeserializePattern() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Pattern> patternDeserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonNode node = mapper.readTree("\"/[a-z]+\\.txt$/g\"");
        Pattern pattern = patternDeserializer.deserialize(mapper.treeToValue(node, Pattern.class), ctxt);
        assertEquals("/[a-z]+\\.txt$/g", pattern.pattern());
    }

    @Test
    public void testDeserializeLocale() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Locale> localeDeserializer = FromStringDeserializer.findDeserializer(Locale.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonNode node = mapper.readTree("\"en_US\"");
        Locale locale = localeDeserializer.deserialize(mapper.treeToValue(node, Locale.class), ctxt);
        assertEquals("en_US", locale.toString());
    }

    @Test
    public void testDeserializeCharset() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Charset> charsetDeserializer = FromStringDeserializer.findDeserializer(Charset.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonNode node = mapper.readTree("\"UTF-8\"");
        Charset charset = charsetDeserializer.deserialize(mapper.treeToValue(node, Charset.class), ctxt);
        assertEquals("UTF-8", charset.name());
    }

    @Test
    public void testDeserializeTimeZone() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<TimeZone> timeZoneDeserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonNode node = mapper.readTree("\"America/New_York\"");
        TimeZone timeZone = timeZoneDeserializer.deserialize(mapper.treeToValue(node, TimeZone.class), ctxt);
        assertEquals("America/New_York", timeZone.getID());
    }

    @Test
    public void testDeserializeInetAddress() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<InetAddress> inetAddressDeserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonNode node = mapper.readTree("\"127.0.0.1\"");
        InetAddress inetAddress = inetAddressDeserializer.deserialize(mapper.treeToValue(node, InetAddress.class), ctxt);
        assertEquals("127.0.0.1", inetAddress.getHostAddress());
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<InetSocketAddress> inetSocketAddressDeserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonNode node = mapper.readTree("\"[127.0.0.1]:8080\"");
        InetSocketAddress inetSocketAddress = inetSocketAddressDeserializer.deserialize(mapper.treeToValue(node, InetSocketAddress.class), ctxt);
        assertEquals("127.0.0.1", inetSocketAddress.getAddress().getHostAddress());
        assertEquals(8080, inetSocketAddress.getPort());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeUnknownType() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Object> objectDeserializer = FromStringDeserializer.findDeserializer(Object.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonNode node = mapper.readTree("\"someString\"");
        objectDeserializer.deserialize(mapper.treeToValue(node, Object.class), ctxt);
    }
}