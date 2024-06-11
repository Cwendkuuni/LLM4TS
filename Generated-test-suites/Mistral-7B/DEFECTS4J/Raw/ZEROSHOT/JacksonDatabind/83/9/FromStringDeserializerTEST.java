package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectReader;

public class FromStringDeserializerTest {

    @Test
    public void testDeserializeStringToFile() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerForClass(File.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        File expected = new File("path/to/file.txt");
        String input = "path/to/file.txt";
        File result = reader.readValue(mapper.createParser(input));
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeStringToUrl() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerForClass(URL.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        URL expected = new URL("http://example.com:8080/test");
        String input = "http://example.com:8080/test";
        URL result = reader.readValue(mapper.createParser(input));
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeStringToUri() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerForClass(URI.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        URI expected = new URI("http://example.com:8080/test");
        String input = "http://example.com:8080/test";
        URI result = reader.readValue(mapper.createParser(input));
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeStringToClass() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerForClass(String.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        String expected = "Hello, World!";
        String input = "java.lang.String";
        String result = reader.readValue(mapper.createParser(input));
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeStringToJavaType() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerForClass(String.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        String input = "com.fasterxml.jackson.databind.JavaType";
        String result = reader.readValue(mapper.createParser(input), javaType);
        assertEquals(expectedStringValue(input), result);
    }

    @Test
    public void testDeserializeStringToCurrency() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerForClass(Currency.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        String input = "USD";
        Currency expected = Currency.getInstance("USD");
        Currency result = reader.readValue(mapper.createParser(input));
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeStringToPattern() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerForClass(Pattern.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        String input = "([\\w]+)@([\\w]+)[.]([\\w]+|[a-z]+)";
        Pattern expected = Pattern.compile(input);
        Pattern result = reader.readValue(mapper.createParser(input));
        assertEquals(expected.pattern(), result.pattern());
    }

    @Test
    public void testDeserializeStringToLocale() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerForClass(Locale.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        String input = "en_US";
        Locale expected = new Locale("en", "US");
        Locale result = reader.readValue(mapper.createParser(input));
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeStringToCharset() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerForClass(Charset.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        String input = "UTF-8";
        Charset expected = Charset.forName(input);
        Charset result = reader.readValue(mapper.createParser(input));
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeStringToTimeZone() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerForClass(TimeZone.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        String input = "America/Los_Angeles";
        TimeZone expected = TimeZone.getTimeZone(input);
        TimeZone result = reader.readValue(mapper.createParser(input));
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeStringToInetAddress() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerForClass(InetAddress.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        String input = "127.0.0.1";
        InetAddress expected = InetAddress.getByName(input);
        InetAddress result = reader.readValue(mapper.createParser(input));
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeStringToInetSocketAddress() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerForClass(InetSocketAddress.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        String input = "[255.255.255.255]:8080";
        InetSocketAddress expected = new InetSocketAddress("255.255.255.255", 8080);
        InetSocketAddress result = reader.readValue(mapper.createParser(input));
        assertEquals(expected, result);

        input = "127.0.0.1:8080";
        expected = new InetSocketAddress("127.0.0.1", 8080);
        result = reader.readValue(mapper.createParser(input));
        assertEquals(expected, result);

        input = "http://localhost:8080";
        try {
            reader.readValue(mapper.createParser(input));
            fail();
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testDeserializeStringToStringBuilder() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerForClass(StringBuilder.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        String input = "sample";
        StringBuilder expected = new StringBuilder("sample");
        StringBuilder result = reader.readValue(mapper.createParser(input));
        assertEquals(expected, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidString() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerForClass(String.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        String input = "invalid";
        reader.readValue(mapper.createParser(input));
        fail();
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeEmptyString() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerForClass(String.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        String input = "";
        reader.readValue(mapper.createParser(input));
    }

    @Test
    public void testDeserializeEmbeddedObject() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerForClass(FromStringDeserializer.Std.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        Object obj = mapper.readValue(new ByteArrayInputStream("{\"kind\":1,\"valueClass\":\"java.net.URL\"}".getBytes()), FromStringDeserializer.Std.class);
        FromStringDeserializer.Std expected = new FromStringDeserializer.Std(URL.class, 1);
        assertEquals(expected, obj);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidEmbeddedObject() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerForClass(FromStringDeserializer.Std.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        Object obj = reader.readValue(mapper.createParser("{\"kind\":-1,\"valueClass\":\"java.net.URL\"}"));
        fail();
    }

    private String expectedStringValue(String value) {
        try {
            return ClassUtil.canonicalName(Class.forName(value));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}