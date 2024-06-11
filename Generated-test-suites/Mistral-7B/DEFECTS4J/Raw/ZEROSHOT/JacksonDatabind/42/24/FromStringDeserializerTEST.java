package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

public class FromStringDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testFileDeserialization() throws JsonProcessingException, IOException {
        FromStringDeserializer deserializer = new FromStringDeserializer(File.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("C:/test.txt");
        DeserializationContext context = objectMapper.getDeserializationContext();
        File file = deserializer.deserialize(jsonParser, context);
        assertEquals(new File("C:/test.txt"), file);
    }

    @Test
    public void testURIDeserialization() throws JsonProcessingException, IOException {
        FromStringDeserializer deserializer = new FromStringDeserializer(URI.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("http://www.example.com");
        DeserializationContext context = objectMapper.getDeserializationContext();
        URI uri = deserializer.deserialize(jsonParser, context);
        assertEquals(new URI("http://www.example.com"), uri);
    }

    @Test
    public void testLocaleDeserialization() throws JsonProcessingException, IOException {
        FromStringDeserializer deserializer = new FromStringDeserializer(Locale.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("en_US");
        DeserializationContext context = objectMapper.getDeserializationContext();
        Locale locale = deserializer.deserialize(jsonParser, context);
        assertEquals(Locale.US, locale);

        jsonParser = objectMapper.getFactory().createParser("fr_FR");
        locale = deserializer.deserialize(jsonParser, context);
        assertEquals(Locale.FRANCE, locale);

        jsonParser = objectMapper.getFactory().createParser("it_IT_Latn");
        locale = deserializer.deserialize(jsonParser, context);
        assertEquals(new Locale("it", "IT", "Latn"), locale);
    }

    @Test
    public void testCurrencyDeserialization() throws JsonProcessingException, IOException {
        FromStringDeserializer deserializer = new FromStringDeserializer(Currency.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("EUR");
        DeserializationContext context = objectMapper.getDeserializationContext();
        Currency currency = deserializer.deserialize(jsonParser, context);
        assertEquals(Currency.EUR, currency);
    }

    @Test
    public void testPatternDeserialization() throws JsonProcessingException, IOException {
        FromStringDeserializer deserializer = new FromStringDeserializer(Pattern.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("/(.*)\\.txt/");
        DeserializationContext context = objectMapper.getDeserializationContext();
        Pattern pattern = deserializer.deserialize(jsonParser, context);
        assertEquals(Pattern.compile("/(.*)\\.txt/"), pattern);
    }

    @Test
    public void testCharsetDeserialization() throws JsonProcessingException, IOException {
        FromStringDeserializer deserializer = new FromStringDeserializer(Charset.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("UTF-8");
        DeserializationContext context = objectMapper.getDeserializationContext();
        Charset charset = deserializer.deserialize(jsonParser, context);
        assertEquals(Charset.forName("UTF-8"), charset);
    }

    @Test
    public void testTimeZoneDeserialization() throws JsonProcessingException, IOException {
        FromStringDeserializer deserializer = new FromStringDeserializer(TimeZone.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("UTC");
        DeserializationContext context = objectMapper.getDeserializationContext();
        TimeZone timeZone = deserializer.deserialize(jsonParser, context);
        assertEquals(TimeZone.getTimeZone("UTC"), timeZone);
    }

    @Test
    public void testInetAddressDeserialization() throws JsonProcessingException, IOException {
        FromStringDeserializer deserializer = new FromStringDeserializer(InetAddress.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("192.168.1.1");
        DeserializationContext context = objectMapper.getDeserializationContext();
        InetAddress inetAddress = deserializer.deserialize(jsonParser, context);
        assertEquals(InetAddress.getByName("192.168.1.1"), inetAddress);
    }

    @Test
    public void testInetSocketAddressDeserialization() throws JsonProcessingException, IOException {
        FromStringDeserializer deserializer = new FromStringDeserializer(InetSocketAddress.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("[255.255.255.255]:80");
        DeserializationContext context = objectMapper.getDeserializationContext();
        InetSocketAddress inetSocketAddress = deserializer.deserialize(jsonParser, context);
        assertEquals(new InetSocketAddress(InetAddress.getByName("255.255.255.255"), 80), inetSocketAddress);

        jsonParser = objectMapper.getFactory().createParser("127.0.0.1:8080");
        inetSocketAddress = deserializer.deserialize(jsonParser, context);
        assertEquals(new InetSocketAddress("127.0.0.1", 8080), inetSocketAddress);
    }

    @Test
    public void testClassDeserialization() throws JsonProcessingException, IOException {
        FromStringDeserializer deserializer = new FromStringDeserializer(String[].class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("[\"Hello\", \"World\"]");
        DeserializationContext context = objectMapper.getDeserializationContext();
        JavaType javaType = context.getTypeFactory().constructArrayType(String.class);
        String[] strings = deserializer.deserialize(jsonParser, context);
        assertEquals(Arrays.asList("Hello", "World").toArray(new String[0]), strings);
    }

    @Test
    public void testJavaTypeDeserialization() throws JsonProcessingException, IOException {
        FromStringDeserializer deserializer = new FromStringDeserializer(JavaType.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("class [Ljava.lang.String;");
        DeserializationContext context = objectMapper.getDeserializationContext();
        JavaType javaType = deserializer.deserialize(jsonParser, context);
        JavaType expectedJavaType = context.getTypeFactory().constructArrayType(String.class);
        assertEquals(expectedJavaType, javaType);
    }

    @Test
    public void testInvalidDeserialization() throws JsonProcessingException, IOException {
        FromStringDeserializer deserializer = new FromStringDeserializer(Integer.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("abc");
        DeserializationContext context = objectMapper.getDeserializationContext();
        try {
            deserializer.deserialize(jsonParser, context);
            assertInstanceOf(JsonMappingException.class, jsonParser.getCurrentEvent());
        } catch (JsonMappingException e) {
            assertEquals("Cannot deserialize value of type [simple type, class java.lang.Integer] from String \"abc\": not a valid number", e.getMessage());
        }
    }

    @Test
    public void testEmptyStringDeserialization() throws JsonProcessingException, IOException {
        FromStringDeserializer deserializer = new FromStringDeserializer(String.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("");
        DeserializationContext context = objectMapper.getDeserializationContext();
        String string = deserializer.deserialize(jsonParser, context);
        assertNull(string);
    }
}