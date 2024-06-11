package com.example.junit; // replace with your test package name if necessary

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FromStringDeserializerTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final JsonFactory jsonFactory = objectMapper.getFactory();

    @Before
    public void setup() {
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testDeserializeFile() throws IOException {
        String json = "\"test.txt\"";
        FromStringDeserializer<File> deserializer = FromStringDeserializer.findDeserializer(File.class);

        File result = deserializer.deserialize(jsonParserFromString(json), context());
        assertEquals(new File("test.txt"), result);
    }

    @Test
    public void testDeserializeUrl() throws IOException {
        String json = "\"http://example.com\"";
        FromStringDeserializer<URL> deserializer = FromStringDeserializer.findDeserializer(URL.class);

        URL result = deserializer.deserialize(jsonParserFromString(json), context());
        assertEquals(new URL("http://example.com"), result);
    }

    @Test
    public void testDeserializeUri() throws IOException {
        String json = "\"http://user:password@example.com:8080/path?query=param#fragment\"";
        FromStringDeserializer<URI> deserializer = FromStringDeserializer.findDeserializer(URI.class);

        URI result = deserializer.deserialize(jsonParserFromString(json), context());
        assertEquals(new URI("http://user:password@example.com:8080/path?query=param#fragment"), result);
    }

    @Test
    public void testDeserializeClass() throws IOException {
        String json = "java.lang.String";
        FromStringDeserializer<Class<?>> deserializer = FromStringDeserializer.findDeserializer(Class.class);

        Class<?> result = deserializer.deserialize(jsonParserFromString(json), context());
        assertEquals(String.class, result);
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        String json = "com.example.MyClass";
        FromStringDeserializer<JavaType> deserializer = FromStringDeserializer.findDeserializer(JavaType.class);

        JavaType result = deserializer.deserialize(jsonParserFromString(json), context());
        assertEquals(JavaType.constructType(MyClass.class), result);
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        String json = "USD";
        FromStringDeserializer<Currency> deserializer = FromStringDeserializer.findDeserializer(Currency.class);

        Currency result = deserializer.deserialize(jsonParserFromString(json), context());
        assertEquals(Currency.getInstance("USD"), result);
    }

    @Test
    public void testDeserializePattern() throws IOException {
        String json = "/^[a-z]+$/";
        FromStringDeserializer<Pattern> deserializer = FromStringDeserializer.findDeserializer(Pattern.class);

        Pattern result = deserializer.deserialize(jsonParserFromString(json), context());
        assertEquals(Pattern.compile(json), result);
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        String json = "en_US";
        FromStringDeserializer<Locale> deserializer = FromStringDeserializer.findDeserializer(Locale.class);

        Locale result = deserializer.deserialize(jsonParserFromString(json), context());
        assertEquals(Locale.forLanguageTag("en_US"), result);
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        String json = "UTF-8";
        FromStringDeserializer<Charset> deserializer = FromStringDeserializer.findDeserializer(Charset.class);

        Charset result = deserializer.deserialize(jsonParserFromString(json), context());
        assertEquals(Charset.forName(json), result);
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        String json = "America/New_York";
        FromStringDeserializer<TimeZone> deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);

        TimeZone result = deserializer.deserialize(jsonParserFromString(json), context());
        assertEquals(TimeZone.getTimeZone(json), result);
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        String json = "192.168.0.1";
        FromStringDeserializer<InetAddress> deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);

        InetAddress result = deserializer.deserialize(jsonParserFromString(json), context());
        assertEquals(InetAddress.getByName(json), result);
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        String json = "[::1]:8080";
        FromStringDeserializer<InetSocketAddress> deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);

        InetSocketAddress result = deserializer.deserialize(jsonParserFromString(json), context());
        assertEquals(new InetSocketAddress(InetAddress.getByAddress(new byte[]{0, 0, 0, 0, 0, 0, 0, 0}), 8080), result);

        json = "192.168.0.1:8080";
        result = deserializer.deserialize(jsonParserFromString(json), context());
        assertEquals(new InetSocketAddress("192.168.0.1", 8080), result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidType() throws IOException {
        String json = "123";
        FromStringDeserializer<Boolean> deserializer = FromStringDeserializer.findDeserializer(Boolean.class);

        deserializer.deserialize(jsonParserFromString(json), context());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeNull() throws IOException {
        String json = "null";
        FromStringDeserializer<String> deserializer = FromStringDeserializer.findDeserializer(String.class);

        deserializer.deserialize(jsonParserFromString(json), context());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeEmptyString() throws IOException {
        String json = "";
        FromStringDeserializer<URI> deserializer = FromStringDeserializer.findDeserializer(URI.class);

        deserializer.deserialize(jsonParserFromString(json), context());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeEmptyArray() throws IOException {
        String json = "[ ]";
        FromStringDeserializer<File> deserializer = FromStringDeserializer.findDeserializer(File.class);

        deserializer.deserialize(jsonParserFromString(json), context());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidJson() throws IOException {
        String json = "{ invalid }";
        FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(Object.class);

        deserializer.deserialize(jsonParserFromString(json), context());
    }

    private JsonParser jsonParserFromString(String json) throws IOException {
        return jsonFactory.createParser(objectMapper.writeValueAsString(new Object()).getBytes());
        // Replace this line with "jsonFactory.createParser(json.getBytes())" for testing with real JSON strings
    }

    private DeserializationContext context() {
        return objectMapper.getDeserializationContext();
    }
}