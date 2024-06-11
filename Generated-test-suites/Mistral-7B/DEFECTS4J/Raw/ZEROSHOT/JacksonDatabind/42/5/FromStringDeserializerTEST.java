package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

public class FromStringDeserializerTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testDeserializeFile() throws IOException {
        String json = "\"/path/to/file.txt\"";
        JsonParser jsonParser = mapper.getFactory().createParser(json);
        FromStringDeserializer<File> deserializer = FromStringDeserializer.findDeserializer(File.class);
        File result = deserializer.deserialize(jsonParser, mapper.getDeserializationContext());
        assertEquals("/path/to/file.txt", result.getAbsolutePath());
    }

    @Test
    public void testDeserializeUrl() throws IOException {
        String json = "\"http://example.com\"";
        JsonParser jsonParser = mapper.getFactory().createParser(json);
        FromStringDeserializer<URL> deserializer = FromStringDeserializer.findDeserializer(URL.class);
        URL result = deserializer.deserialize(jsonParser, mapper.getDeserializationContext());
        assertEquals("http://example.com", result.toString());
    }

    @Test
    public void testDeserializeUri() throws IOException {
        String json = "\"http://example.com/path/to/resource\"";
        JsonParser jsonParser = mapper.getFactory().createParser(json);
        FromStringDeserializer<URI> deserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI result = deserializer.deserialize(jsonParser, mapper.getDeserializationContext());
        assertEquals("http://example.com/path/to/resource", result.toString());
    }

    @Test
    public void testDeserializeClass() throws IOException {
        String json = "\"java.lang.Integer.class\"";
        JsonParser jsonParser = mapper.getFactory().createParser(json);
        FromStringDeserializer<Class<?>> deserializer = FromStringDeserializer.findDeserializer(Class.class);
        Class<?> result = deserializer.deserialize(jsonParser, mapper.getDeserializationContext());
        assertEquals(Integer.class, result);
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        String json = "\"com.example.MyClass\"";
        JsonParser jsonParser = mapper.getFactory().createParser(json);
        FromStringDeserializer<JavaType> deserializer = FromStringDeserializer.findDeserializer(JavaType.class);
        JavaType result = deserializer.deserialize(jsonParser, mapper.getDeserializationContext());
        TypeFactory typeFactory = mapper.getTypeFactory();
        assertEquals(typeFactory.constructType(MyClass.class), result);
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        String json = "\"USD\"";
        JsonParser jsonParser = mapper.getFactory().createParser(json);
        FromStringDeserializer<Currency> deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        Currency result = deserializer.deserialize(jsonParser, mapper.getDeserializationContext());
        assertEquals(Currency.getInstance("USD"), result);
    }

    @Test
    public void testDeserializePattern() throws IOException {
        String json = "\"/[a-z]+\\.txt$/\"";
        JsonParser jsonParser = mapper.getFactory().createParser(json);
        FromStringDeserializer<Pattern> deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        Pattern result = deserializer.deserialize(jsonParser, mapper.getDeserializationContext());
        assertEquals(Pattern.compile("/[a-z]+\\.txt$/"), result);
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        String json = "\"en_US\"";
        JsonParser jsonParser = mapper.getFactory().createParser(json);
        FromStringDeserializer<Locale> deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        Locale result = deserializer.deserialize(jsonParser, mapper.getDeserializationContext());
        assertEquals(Locale.US, result);

        json = "\"en_US_POSIX\"";
        jsonParser = mapper.getFactory().createParser(json);
        result = deserializer.deserialize(jsonParser, mapper.getDeserializationContext());
        assertEquals(Locale.US, result);

        json = "\"en_GB_jp_JP\"";
        jsonParser = mapper.getFactory().createParser(json);
        result = deserializer.deserialize(jsonParser, mapper.getDeserializationContext());
        assertEquals(new Locale("en", "GB", "jp", "JP"), result);
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        String json = "\"UTF-8\"";
        JsonParser jsonParser = mapper.getFactory().createParser(json);
        FromStringDeserializer<Charset> deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        Charset result = deserializer.deserialize(jsonParser, mapper.getDeserializationContext());
        assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        String json = "\"America/Los_Angeles\"";
        JsonParser jsonParser = mapper.getFactory().createParser(json);
        FromStringDeserializer<TimeZone> deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone result = deserializer.deserialize(jsonParser, mapper.getDeserializationContext());
        assertEquals("America/Los_Angeles", result.getID());
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        String json = "\"192.168.0.1\"";
        JsonParser jsonParser = mapper.getFactory().createParser(json);
        FromStringDeserializer<InetAddress> deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        InetAddress result = deserializer.deserialize(jsonParser, mapper.getDeserializationContext());
        assertEquals("192.168.0.1", result.getHostAddress());

        json = "[\"2001:0db8:85a3:0000:0000:8a2e:0370:7334\"]";
        jsonParser = mapper.getFactory().createParser(json);
        result = deserializer.deserialize(jsonParser, mapper.getDeserializationContext());
        assertEquals("2001:0db8:85a3:0000:0000:8a2e:0370:7334", result.getHostAddress());
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        String json = "\"[2001:0db8:85a3:0000:0000:8a2e:0370:7334]:80\"";
        JsonParser jsonParser = mapper.getFactory().createParser(json);
        FromStringDeserializer<InetSocketAddress> deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        InetSocketAddress result = deserializer.deserialize(jsonParser, mapper.getDeserializationContext());
        assertEquals("2001:0db8:85a3:0000:0000:8a2e:0370:7334", result.getAddress().getHostAddress());
        assertEquals(80, result.getPort());

        json = "\"192.168.0.1:80\"";
        jsonParser = mapper.getFactory().createParser(json);
        result = deserializer.deserialize(jsonParser, mapper.getDeserializationContext());
        assertEquals("192.168.0.1", result.getAddress().getHostAddress());
        assertEquals(80, result.getPort());
    }

    @Test
    public void testDeserializeEmptyString() throws IOException {
        String json = "\"\"";
        JsonParser jsonParser = mapper.getFactory().createParser(json);
        FromStringDeserializer<File> deserializer = FromStringDeserializer.findDeserializer(File.class);
        File result = deserializer.deserialize(jsonParser, mapper.getDeserializationContext());
        assertNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidType() throws IOException {
        String json = "\"java.util.Date\"";
        JsonParser jsonParser = mapper.getFactory().createParser(json);
        FromStringDeserializer<String> deserializer = FromStringDeserializer.findDeserializer(String.class);
        deserializer.deserialize(jsonParser, mapper.getDeserializationContext());
        fail("Expected JsonMappingException");
    }

    @Test
    public void testDeserializeArray() throws IOException {
        String json = "[\"java.lang.Integer.class\", \"java.lang.String\"]";
        JsonParser jsonParser = mapper.getFactory().createParser(json);
        FromStringDeserializer<Object[]> deserializer = FromStringDeserializer.findDeserializer(Object[].class);
        Object[] result = deserializer.deserialize(jsonParser, mapper.getDeserializationContext());
        assertArrayEquals(new Object[] { Integer.class, String.class }, result);
    }
}