package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class FromStringDeserializerTests {

    @Test
    public void testDeserializeFile() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"/path/to/file.txt\"");
        FromStringDeserializer<File> deserializer = FromStringDeserializer.findDeserializer(File.class);
        File result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals("/path/to/file.txt", result.getAbsolutePath());
    }

    @Test
    public void testDeserializeURL() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"http://example.com\"");
        FromStringDeserializer<URL> deserializer = FromStringDeserializer.findDeserializer(URL.class);
        URL result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals("http://example.com", result.toString());
    }

    @Test
    public void testDeserializeURI() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"uri://example.com\"");
        FromStringDeserializer<URI> deserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals("uri://example.com", result.toString());
    }

    @Test
    public void testDeserializeClass() throws JsonProcessingException, InstantiationException, IllegalAccessException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"java.lang.String\"");
        FromStringDeserializer<Class<?>> deserializer = FromStringDeserializer.findDeserializer(Class.class);
        Class<?> result = deserializer.deserialize(parser, mapper.getDeseralizationContext());
        assertEquals(String.class, result);
    }

    @Test
    public void testDeserializeJavaType() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"com.example.MyClass\"");
        JavaType javaType = mapper.getTypeFactory().constructType("com.example.MyClass");
        FromStringDeserializer<JavaType> deserializer = FromStringDeserializer.findDeserializer(JavaType.class);
        JavaType result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(javaType, result);
    }

    @Test
    public void testDeserializeCurrency() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"USD\"");
        FromStringDeserializer<Currency> deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        Currency result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(Currency.getInstance("USD"), result);
    }

    @Test
    public void testDeserializePattern() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"/^[a-z]+$/i\"");
        FromStringDeserializer<Pattern> deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        Pattern result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(Pattern.compile("/^[a-z]+$/i"), result);
    }

    @Test
    public void testDeserializeLocale() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"en_US\"");
        FromStringDeserializer<Locale> deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        Locale result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(Locale.US, result);

        parser = mapper.getFactory().createParser("\"en-US\"");
        result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(Locale.US, result);

        parser = mapper.getFactory().createParser("\"en_US_POSIX\"");
        result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(Locale.US, result);

        parser = mapper.getFactory().createParser("\"en_US_POSIX.le_ML\"");
        result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(Locale.US, result);

        parser = mapper.getFactory().createParser("\"en_US_POSIX.le_ML.Vendor\"");
        result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(Locale.US, result);

        parser = mapper.getFactory().createParser("\"fr_FR\"");
        result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(Locale.FRANCE, result);

        parser = mapper.getFactory().createParser("\"fr_FR_FR\"");
        result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(Locale.FRANCE, result);

        parser = mapper.getFactory().createParser("\"fr_FR_FR_FR\"");
        result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(Locale.FRANCE, result);
    }

    @Test
    public void testDeserializeCharset() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"UTF-8\"");
        FromStringDeserializer<Charset> deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        Charset result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test
    public void testDeserializeTimeZone() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"America/Los_Angeles\"");
        FromStringDeserializer<TimeZone> deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(TimeZone.getTimeZone("America/Los_Angeles"), result);
    }

    @Test
    public void testDeserializeInetAddress() throws JsonProcessingException, UnknownHostException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"192.168.1.1\"");
        FromStringDeserializer<InetAddress> deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        InetAddress result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(InetAddress.getByName("192.168.1.1"), result);
    }

    @Test
    public void testDeserializeInetSocketAddress() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"[255.255.255.255]:80\"");
        FromStringDeserializer<InetSocketAddress> deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        InetSocketAddress result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(new InetSocketAddress(InetAddress.getByName("255.255.255.255"), 80), result);

        parser = mapper.getFactory().createParser("\"localhost:8080\"");
        result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(new InetSocketAddress(InetAddress.getByName("localhost"), 8080), result);
    }

    @Test
    public void testDeserializeStringBuilder() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"Hello World\"");
        FromStringDeserializer<StringBuilder> deserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
        StringBuilder result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals("Hello World", result.toString());
    }

    @Test
    public void testDeserializeEmptyString() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"\"");
        FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(Object.class);
        Object result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertNull(result);
    }

    @Test
    public void testDeserializeEmbeddedObject() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"value\": \"foo\"}");
        FromStringDeserializer<Std> deserializer = FromStringDeserializer.findDeserializer(Std.class);
        Std result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals("foo", result.getValue());
    }

    @Test
    public void testDeserializeArray() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[\"foo\", \"bar\"]");
        FromStringDeserializer<Object[]> deserializer = FromStringDeserializer.findDeserializer(Object[].class);
        Object[] result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertInstanceOf(Object[].class, result);
        assertEquals(2, result.length);
        assertEquals("foo", result[0]);
        assertEquals("bar", result[1]);
    }

    @Test
    public void testUnexpectedToken() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[1, 2, 3]");
        FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(Object.class);
        try {
            deserializer.deserialize(parser, mapper.getDeserializationContext());
            fail();
        } catch (JsonMappingException e) {
            assertEquals("Cannot construct instance of `java.lang.Object`", e.getMessage());
        }
    }
}