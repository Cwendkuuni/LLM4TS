package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

public class FromStringDeserializerTest {

    @Test
    public void testDeserializeFile() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("\"/path/to/file.txt\"");
        FromStringDeserializer<File> deserializer = FromStringDeserializer.findDeserializer(File.class);
        File result = deserializer.deserialize(jp, mapper.getDeserializationContext());
        assertNotNull(result);
        assertEquals("/path/to/file.txt", result.getAbsolutePath());
    }

    @Test
    public void testDeserializeUrl() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("\"http://example.com/path?query#fragment\"");
        FromStringDeserializer<URL> deserializer = FromStringDeserializer.findDeserializer(URL.class);
        URL result = deserializer.deserialize(jp, mapper.getDeserializationContext());
        assertNotNull(result);
        assertEquals("http://example.com/path?query#fragment", result.toString());
    }

    @Test
    public void testDeserializeUri() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("\" uri:/path?query#fragment scheme:http\"");
        FromStringDeserializer<URI> deserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI result = deserializer.deserialize(jp, mapper.getDeserializationContext());
        assertNotNull(result);
        assertEquals("uri:/path?query#fragment", result.toString());
    }

    @Test
    public void testDeserializeClass() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("\"java.lang.String\"");
        FromStringDeserializer<Class<?>> deserializer = FromStringDeserializer.findDeserializer(Class.class);
        Class<?> result = deserializer.deserialize(jp, mapper.getDeserializationContext());
        assertNotNull(result);
        assertEquals(String.class, result);
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("\"com.fasterxml.jackson.databind.JsonNode\"");
        FromStringDeserializer<JavaType> deserializer = FromStringDeserializer.findDeserializer(JavaType.class);
        JavaType result = deserializer.deserialize(jp, mapper.getDeserializationContext());
        assertNotNull(result);
        assertTrue(result instanceof ParametizedType);
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("\"EUR\"");
        FromStringDeserializer<Currency> deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        Currency result = deserializer.deserialize(jp, mapper.getDeserializationContext());
        assertNotNull(result);
        assertEquals(Currency.getInstance("EUR"), result);
    }

    @Test
    public void testDeserializePattern() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("\"\\w+\\.\\w+\"");
        FromStringDeserializer<Pattern> deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        Pattern result = deserializer.deserialize(jp, mapper.getDeserializationContext());
        assertNotNull(result);
        assertTrue(result.matcher("test.example.com").matches());
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("\"en_US\"");
        FromStringDeserializer<Locale> deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        Locale result = deserializer.deserialize(jp, mapper.getDeserializationContext());
        assertNotNull(result);
        assertEquals("en_US", result.toString());
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("\"UTF-8\"");
        FromStringDeserializer<Charset> deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        Charset result = deserializer.deserialize(jp, mapper.getDeserializationContext());
        assertNotNull(result);
        assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("\"America/New_York\"");
        FromStringDeserializer<TimeZone> deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone result = deserializer.deserialize(jp, mapper.getDeserializationContext());
        assertNotNull(result);
        assertEquals("America/New_York", result.getID());
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("\"127.0.0.1\"");
        FromStringDeserializer<InetAddress> deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        InetAddress result = deserializer.deserialize(jp, mapper.getDeserializationContext());
        assertNotNull(result);
        assertEquals("127.0.0.1", result.getHostAddress());
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("[\"127.0.0.1\", 80]");
        FromStringDeserializer<InetSocketAddress> deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        InetSocketAddress result = deserializer.deserialize(jp, mapper.getDeserializationContext());
        assertNotNull(result);
        assertEquals("127.0.0.1", result.getAddress().getHostAddress());
        assertEquals(80, result.getPort());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("\"java.lang.Integer\"");
        FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(Object.class);
        deserializer.deserialize(jp, mapper.getDeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeEmptyString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("\"\"");
        FromStringDeserializer<String> deserializer = FromStringDeserializer.findDeserializer(String.class);
        deserializer.deserialize(jp, mapper.getDeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeNull() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser(null);
        FromStringDeserializer<String> deserializer = FromStringDeserializer.findDeserializer(String.class);
        deserializer.deserialize(jp, mapper.getDeserializationContext());
    }
}