package com.example.tests;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class FromStringDeserializerTest {

    private ObjectMapper mapper = new ObjectMapper();
    private JsonFactory jsonFactory = mapper.getFactory();

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserialize_StringToFile() throws IOException {
        String json = "\"/path/to/my/file.txt\"";
        JsonParser jp = jsonFactory.createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<File> deserializer = FromStringDeserializer.findDeserializer(File.class);
        File expected = new File("/path/to/my/file.txt");
        File actual = deserializer.deserialize(jp, ctxt);
        assertEquals(expected, actual);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserialize_StringToURL() throws IOException {
        String json = "\"http://example.com\"";
        JsonParser jp = jsonFactory.createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<URL> deserializer = FromStringDeserializer.findDeserializer(URL.class);
        URL expected = new URL("http://example.com");
        URL actual = deserializer.deserialize(jp, ctxt);
        assertEquals(expected, actual);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserialize_StringToURI() throws IOException {
        String json = "\"http://example.com/path/to/resource\"";
        JsonParser jp = jsonFactory.createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<URI> deserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI expected = new URI("http://example.com/path/to/resource");
        URI actual = deserializer.deserialize(jp, ctxt);
        assertEquals(expected, actual);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserialize_StringToClass() throws IOException, ClassNotFoundException {
        String json = "\"java.lang.String\"";
        JsonParser jp = jsonFactory.createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<Class<?>> deserializer = FromStringDeserializer.findDeserializer(Class.class);
        Class<?> expected = String.class;
        Class<?> actual = deserializer.deserialize(jp, ctxt);
        assertEquals(expected, actual);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserialize_StringToJavaType() throws IOException {
        String json = "\"com.example.MyClass\"";
        JsonParser jp = jsonFactory.createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<JavaType> deserializer = FromStringDeserializer.findDeserializer(JavaType.class);
        JavaType<MyClass> expected = mapper.getTypeFactory().constructType(MyClass.class);
        JavaType<MyClass> actual = deserializer.deserialize(jp, ctxt);
        assertEquals(expected, actual);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserialize_StringToCurrency() throws IOException {
        String json = "\"USD\"";
        JsonParser jp = jsonFactory.createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<Currency> deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        Currency expected = Currency.getInstance("USD");
        Currency actual = deserializer.deserialize(jp, ctxt);
        assertEquals(expected, actual);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserialize_StringToPattern() throws IOException {
        String json = "/[a-z]+/i";
        JsonParser jp = jsonFactory.createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<Pattern> deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        Pattern expected = Pattern.compile(json);
        Pattern actual = deserializer.deserialize(jp, ctxt);
        assertEquals(expected, actual);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserialize_StringToLocale() throws IOException {
        String json = "\"en_US\"";
        JsonParser jp = jsonFactory.createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<Locale> deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        Locale expected = Locale.US;
        Locale actual = deserializer.deserialize(jp, ctxt);
        assertEquals(expected, actual);

        json = "\"en_US_POSIX\"";
        jp = jsonFactory.createParser(json);
        expected = new Locale("en", "US", "");
        actual = deserializer.deserialize(jp, ctxt);
        assertEquals(expected, actual);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserialize_StringToCharset() throws IOException {
        String json = "\"UTF-8\"";
        JsonParser jp = jsonFactory.createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<Charset> deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        Charset expected = Charset.forName(json);
        Charset actual = deserializer.deserialize(jp, ctxt);
        assertEquals(expected, actual);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserialize_StringToTimeZone() throws IOException {
        String json = "\"America/Los_Angeles\"";
        JsonParser jp = jsonFactory.createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<TimeZone> deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone expected = TimeZone.getTimeZone(json);
        TimeZone actual = deserializer.deserialize(jp, ctxt);
        assertEquals(expected, actual);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserialize_StringToInetAddress() throws IOException {
        String json = "\"192.168.1.1\"";
        JsonParser jp = jsonFactory.createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<InetAddress> deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        InetAddress expected = InetAddress.getByName(json);
        InetAddress actual = deserializer.deserialize(jp, ctxt);
        assertEquals(expected, actual);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserialize_StringToInetSocketAddress() throws IOException {
        String json = "[\"192.168.1.1\", 80]";
        JsonParser jp = jsonFactory.createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<InetSocketAddress> deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        InetSocketAddress expected = new InetSocketAddress("192.168.1.1", 80);
        InetSocketAddress actual = deserializer.deserialize(jp, ctxt);
        assertEquals(expected, actual);

        json = "[\"[2001:0db8:85a3:0000:0000:8a2e:0370:7334]\", 80]";
        jp = jsonFactory.createParser(json);
        expected = new InetSocketAddress("[2001:0db8:85a3:0000:0000:8a2e:0370:7334]", 80);
        actual = deserializer.deserialize(jp, ctxt);
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserialize_NullString() throws IOException {
        String json = "null";
        JsonParser jp = jsonFactory.createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<File> deserializer = FromStringDeserializer.findDeserializer(File.class);
        File expected = null;
        File actual = deserializer.deserialize(jp, ctxt);
        assertNull(actual);
    }

    @Test
    public void testDeserialize_EmptyString() throws IOException {
        String json = "";
        JsonParser jp = jsonFactory.createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<File> deserializer = FromStringDeserializer.findDeserializer(File.class);
        File expected = null;
        File actual = deserializer.deserialize(jp, ctxt);
        assertNull(actual);

        // Handle specific cases for URI and Locale
        ctxt.disable(DeserializationFeature.FAIL_ON_EMPTY_STRING_AS_NULL_OBJECT_ID);
        expected = URI.create("");
        actual = deserializer.deserialize(jp, ctxt);
        assertEquals(expected, actual);

        ctxt.disable(DeserializationFeature.FAIL_ON_EMPTY_STRING_AS_NULL_OBJECT_ID);
        expected = new Locale("");
        actual = deserializer.deserialize(jp, ctxt);
        assertEquals(expected, actual);

        ctxt.disable(DeserializationFeature.FAIL_ON_EMPTY_STRING_AS_NULL_OBJECT_ID);
        expected = new Locale("", "", "");
        actual = deserializer.deserialize(jp, ctxt);
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserialize_EmbeddedObject() throws IOException {
        String json = "{\"class\": \"java.net.URI\", \"value\": \"http://example.com\"}";
        JsonParser jp = mapper.readTree(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<URI> deserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI expected = new URI("http://example.com");
        URI actual = deserializer.deserializeEmbedded(jp.getEmbeddedObject(), ctxt);
        assertEquals(expected, actual);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_InvalidString() throws IOException {
        String json = "\"invalid_value\"";
        JsonParser jp = jsonFactory.createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<File> deserializer = FromStringDeserializer.findDeserializer(File.class);
        deserializer.deserialize(jp, ctxt); // Should throw an exception
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_StringToUnsupportedType() throws IOException {
        String json = "123";
        JsonParser jp = jsonFactory.createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<Double> deserializer = FromStringDeserializer.findDeserializer(Integer.class);
        deserializer.deserialize(jp, ctxt); // Should throw an exception
    }

    @Test
    public void testTypes() {
        Class<?>[] types = FromStringDeserializer.types();
        for (Class<?> type : types) {
            System.out.println(type.getName());
        }
    }

    static class MyClass {}
}