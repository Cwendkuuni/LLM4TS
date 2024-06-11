package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;

import java.io.IOException;
import java.net.*;
import java.util.*;

import static org.junit.Assert.*;

public class FromStringDeserializerTest {

    @Test
    public void testDeserializeFile() throws IOException {
        FromStringDeserializer<File> deserializer = new FromStringDeserializer.Std(File.class);
        JsonParser parser = createParser("\"/path/to/file.txt\"");
        File result = deserializer.deserialize(parser, new DeserializationContext(null));
        assertEquals("/path/to/file.txt", result.getAbsolutePath());
    }

    @Test
    public void testDeserializeUrl() throws IOException {
        FromStringDeserializer<URL> deserializer = new FromStringDeserializer.Std(URL.class);
        JsonParser parser = createParser("\"http://example.com\"");
        URL result = deserializer.deserialize(parser, new DeserializationContext(null));
        assertEquals("http://example.com", result.toString());
    }

    @Test
    public void testDeserializeUri() throws IOException {
        FromStringDeserializer<URI> deserializer = new FromStringDeserializer.Std(URI.class);
        JsonParser parser = createParser("\"http://example.com/path?query#fragment\"");
        URI result = deserializer.deserialize(parser, new DeserializationContext(null));
        assertEquals("http", result.getScheme());
        assertEquals("/path", result.getPath());
        assertEquals("query", result.getQuery());
        assertEquals("fragment", result.getFragment());
    }

    @Test
    public void testDeserializeClass() throws IOException {
        FromStringDeserializer<Class<?>> deserializer = new FromStringDeserializer.Std(Class.class);
        JsonParser parser = createParser("java.util.Date");
        Class<?> result = deserializer.deserialize(parser, new DeserializationContext(null));
        assertEquals(Date.class, result);
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        FromStringDeserializer<JavaType> deserializer = new FromStringDeserializer.Std(JavaType.class);
        JsonParser parser = createParser("java.util.List<java.lang.String>");
        JavaType result = deserializer.deserialize(parser, new DeserializationContext(null));
        assertTrue(result instanceof ParameterizedType);
        assertEquals(List.class, result.getRawClass());
        assertEquals(String.class, ((ParameterizedType) result).getActualTypeArguments()[0]);
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        FromStringDeserializer<Currency> deserializer = new FromStringDeserializer.Std(Currency.class);
        JsonParser parser = createParser("\"EUR\"");
        Currency result = deserializer.deserialize(parser, new DeserializationContext(null));
        assertEquals(Currency.getInstance("EUR"), result);
    }

    @Test
    public void testDeserializePattern() throws IOException {
        FromStringDeserializer<Pattern> deserializer = new FromStringDeserializer.Std(Pattern.class);
        JsonParser parser = createParser("\"\\w+\\.\\w+\"");
        Pattern result = deserializer.deserialize(parser, new DeserializationContext(null));
        assertEquals(Pattern.compile("\\w+\\.\\w+"), result);
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        FromStringDeserializer<Locale> deserializer = new FromStringDeserializer.Std(Locale.class);
        JsonParser parser = createParser("\"en_US\"");
        Locale result = deserializer.deserialize(parser, new DeserializationContext(null));
        assertEquals(Locale.US, result);

        parser = createParser("\"en_GB_POSIX\"");
        result = deserializer.deserialize(parser, new DeserializationContext(null));
        assertEquals(Locale.ROOT, result);
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        FromStringDeserializer<Charset> deserializer = new FromStringDeserializer.Std(Charset.class);
        JsonParser parser = createParser("\"UTF-8\"");
        Charset result = deserializer.deserialize(parser, new DeserializationContext(null));
        assertEquals(StandardCharsets.UTF_8, result);
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        FromStringDeserializer<TimeZone> deserializer = new FromStringDeserializer.Std(TimeZone.class);
        JsonParser parser = createParser("\"America/Los_Angeles\"");
        TimeZone result = deserializer.deserialize(parser, new DeserializationContext(null));
        assertEquals("America/Los_Angeles", result.getID());
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        FromStringDeserializer<InetAddress> deserializer = new FromStringDeserializer.Std(InetAddress.class);
        JsonParser parser = createParser("\"192.168.0.1\"");
        InetAddress result = deserializer.deserialize(parser, new DeserializationContext(null));
        assertEquals("192.168.0.1", result.getHostAddress());

        parser = createParser("\"::1\"");
        result = deserializer.deserialize(parser, new DeserializationContext(null));
        assertEquals("0:0:0:0:0:0:0:1", result.getHostAddress());
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        FromStringDeserializer<InetSocketAddress> deserializer = new FromStringDeserializer.Std(InetSocketAddress.class);
        JsonParser parser = createParser("\"192.168.0.1:80\"");
        InetSocketAddress result = deserializer.deserialize(parser, new DeserializationContext(null));
        assertEquals("192.168.0.1", result.getAddress().getHostAddress());
        assertEquals(80, result.getPort());

        parser = createParser("\"[2001:0db8:85a3:0000:0000:8a2e:0370:7334]:8080\"");
        result = deserializer.deserialize(parser, new DeserializationContext(null));
        assertEquals("2001:0db8:85a3:0:0:8a2e:370:7334", result.getAddress().getHostAddress());
        assertEquals(8080, result.getPort());
    }

    @Test
    public void testDeserializeStringBuilder() throws IOException {
        FromStringDeserializer<StringBuilder> deserializer = new FromStringDeserializer.Std(StringBuilder.class);
        JsonParser parser = createParser("\"hello world\"");
        StringBuilder result = deserializer.deserialize(parser, new DeserializationContext(null));
        assertEquals("hello world", result.toString());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeUnsupportedType() throws IOException {
        FromStringDeserializer<Object> deserializer = new FromStringDeserializer.Std(Integer.class);
        JsonParser parser = createParser("42");
        deserializer.deserialize(parser, new DeserializationContext(null));
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidString() throws IOException {
        FromStringDeserializer<StringBuilder> deserializer = new FromStringDeserializer.Std(StringBuilder.class);
        JsonParser parser = createParser("{\"name\": \"John\"}");
        deserializer.deserialize(parser, new DeserializationContext(null));
    }

    private JsonParser createParser(String content) throws IOException {
        StringReader reader = new StringReader(content);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.getFactory().createParser(reader);
    }
}