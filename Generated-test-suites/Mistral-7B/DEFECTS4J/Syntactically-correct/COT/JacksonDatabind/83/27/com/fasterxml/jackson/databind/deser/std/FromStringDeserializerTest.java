package com.example.tests;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonParseException;
import com.fasterxml.jackson.databind.util.StdSerializer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

public class FromStringDeserializerTest {

    @SuppressWarnings("unchecked")
    private static <T> StdSerializer<T> createDeserializer(Class<T> type) {
        return (StdSerializer<T>) FromStringDeserializer.findDeserializer(type);
    }

    @org.junit.Test
    public void testDeserializeString() throws IOException, JsonMappingException {
        String jsonString = "\"Hello World\"";
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser parser = mapper.getFactory().createParser(jsonString);

        StdSerializer<StringBuilder> stringBuilderSerializer = createDeserializer(StringBuilder.class);
        StringBuilder expected = new StringBuilder("Hello World");
        assertEquals(expected, stringBuilderSerializer.deserialize(parser, ctxt));
    }

    @org.junit.Test
    public void testDeserializeEmptyString() throws IOException, JsonMappingException {
        String jsonString = "\"\"";
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser parser = mapper.getFactory().createParser(jsonString);

        StdSerializer<StringBuilder> stringBuilderSerializer = createDeserializer(StringBuilder.class);
        StringBuilder expected = new StringBuilder();
        assertEquals(expected, stringBuilderSerializer.deserialize(parser, ctxt));
    }

    @org.junit.Test
    public void testDeserializeInvalidString() throws IOException, JsonMappingException {
        String jsonString = "\"InvalidString\"";
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser parser = mapper.getFactory().createParser(jsonString);

        StdSerializer<StringBuilder> stringBuilderSerializer = createDeserializer(StringBuilder.class);
        assertNull(stringBuilderSerializer.deserialize(parser, ctxt));
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidFormatForFile() throws IOException {
        String jsonString = "\"invalidFileFormat\"";
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser parser = mapper.getFactory().createParser(jsonString);

        StdSerializer<File> fileSerializer = createDeserializer(File.class);
        fileSerializer.deserialize(parser, ctxt);
    }

    @org.junit.Test
    public void testDeserializeURL() throws IOException, JsonMappingException {
        String jsonString = "\"http://example.com\"";
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser parser = mapper.getFactory().createParser(jsonString);

        StdSerializer<URL> urlSerializer = createDeserializer(URL.class);
        URL expected = new URL("http://example.com");
        assertEquals(expected, urlSerializer.deserialize(parser, ctxt));
    }

    @org.junit.Test
    public void testDeserializeURI() throws IOException, JsonMappingException {
        String jsonString = "\"http://example.com/path/to/resource\"";
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser parser = mapper.getFactory().createParser(jsonString);

        StdSerializer<URI> uriSerializer = createDeserializer(URI.class);
        URI expected = new URI("http://example.com/path/to/resource");
        assertEquals(expected, uriSerializer.deserialize(parser, ctxt));
    }

    @org.junit.Test
    public void testDeserializeClass() throws IOException, JsonMappingException {
        String jsonString = "\"java.lang.String\"";
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser parser = mapper.getFactory().createParser(jsonString);

        StdSerializer<Class<?>> classSerializer = createDeserializer(Class.class);
        Class<?> expected = Class.forName("java.lang.String");
        assertEquals(expected, classSerializer.deserialize(parser, ctxt));
    }

    @org.junit.Test
    public void testDeserializeJavaType() throws IOException, JsonMappingException {
        String jsonString = "\"com.example.Person:[name:java.lang.String, age:java.lang.Integer]\"";
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser parser = mapper.getFactory().createParser(jsonString);

        StdSerializer<JavaType> javaTypeSerializer = createDeserializer(JavaType.class);
        JavaType expected = mapper.getTypeFactory().constructType(JavaType.class, "com.example.Person", null);
        assertEquals(expected, javaTypeSerializer.deserialize(parser, ctxt));
    }

    @org.junit.Test
    public void testDeserializeCurrency() throws IOException, JsonMappingException {
        String jsonString = "\"USD\"";
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser parser = mapper.getFactory().createParser(jsonString);

        StdSerializer<Currency> currencySerializer = createDeserializer(Currency.class);
        Currency expected = Currency.getInstance("USD");
        assertEquals(expected, currencySerializer.deserialize(parser, ctxt));
    }

    @org.junit.Test
    public void testDeserializePattern() throws IOException, JsonMappingException {
        String jsonString = "/[a-zA-Z]+/";
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser parser = mapper.getFactory().createParser(jsonString);

        StdSerializer<Pattern> patternSerializer = createDeserializer(Pattern.class);
        Pattern expected = Pattern.compile("/[a-zA-Z]+/");
        assertEquals(expected, patternSerializer.deserialize(parser, ctxt));
    }

    @org.junit.Test
    public void testDeserializeLocale() throws IOException, JsonMappingException {
        String jsonString = "\"en_US\"";
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser parser = mapper.getFactory().createParser(jsonString);

        StdSerializer<Locale> localeSerializer = createDeserializer(Locale.class);
        Locale expected = new Locale("en", "US");
        assertEquals(expected, localeSerializer.deserialize(parser, ctxt));
    }

    @org.junit.Test
    public void testDeserializeCharset() throws IOException, JsonMappingException {
        String jsonString = "\"UTF-8\"";
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser parser = mapper.getFactory().createParser(jsonString);

        StdSerializer<Charset> charsetSerializer = createDeserializer(Charset.class);
        Charset expected = Charset.forName("UTF-8");
        assertEquals(expected, charsetSerializer.deserialize(parser, ctxt));
    }

    @org.junit.Test
    public void testDeserializeTimeZone() throws IOException, JsonMappingException {
        String jsonString = "\"America/Los_Angeles\"";
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser parser = mapper.getFactory().createParser(jsonString);

        StdSerializer<TimeZone> timeZoneSerializer = createDeserializer(TimeZone.class);
        TimeZone expected = TimeZone.getTimeZone("America/Los_Angeles");
        assertEquals(expected, timeZoneSerializer.deserialize(parser, ctxt));
    }

    @org.junit.Test
    public void testDeserializeInetAddress() throws IOException, JsonMappingException, UnknownHostException {
        String jsonString = "\"127.0.0.1\"";
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser parser = mapper.getFactory().createParser(jsonString);

        StdSerializer<InetAddress> inetAddressSerializer = createDeserializer(InetAddress.class);
        InetAddress expected = InetAddress.getByName("127.0.0.1");
        assertEquals(expected, inetAddressSerializer.deserialize(parser, ctxt));
    }

    @org.junit.Test
    public void testDeserializeInetSocketAddress() throws IOException, JsonMappingException {
        String jsonString = "[\"127.0.0.1\", 80]";
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser parser = mapper.getFactory().createParser(jsonString);

        StdSerializer<InetSocketAddress> inetSocketAddressSerializer = createDeserializer(InetSocketAddress.class);
        InetSocketAddress expected = new InetSocketAddress("127.0.0.1", 80);
        assertEquals(expected, inetSocketAddressSerializer.deserialize(parser, ctxt));
    }

    @org.junit.Test
    public void testDeserializeEmptyArray() throws IOException, JsonMappingException {
        String jsonString = "[]";
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser parser = mapper.getFactory().createParser(jsonString);

        StdSerializer<Object[]> arraySerializer = createDeserializer(Object[].class);
        Object[] expected = new Object[0];
        assertEquals(expected.length, arraySerializer.deserialize(parser, ctxt).length);
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void testDeserializeNull() throws IOException, JsonMappingException {
        String jsonString = "null";
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser parser = mapper.getFactory().createParser(jsonString);

        StdSerializer<StringBuilder> stringBuilderSerializer = createDeserializer(StringBuilder.class);
        stringBuilderSerializer.deserialize(parser, ctxt);
    }
}