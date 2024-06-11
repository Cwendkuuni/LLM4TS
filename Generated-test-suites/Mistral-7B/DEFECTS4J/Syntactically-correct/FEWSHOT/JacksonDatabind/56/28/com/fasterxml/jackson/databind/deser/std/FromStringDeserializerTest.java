package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class FromStringDeserializerTest {

    @Test
    public void testDeserializeFile() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = mapper.getFactory().createParser("\"/path/to/file.txt\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<File> deserializer = FromStringDeserializer.findDeserializer(File.class);
        File result = deserializer.deserialize(jsonParser, ctxt);
        assertEquals("/path/to/file.txt", result.getAbsolutePath());
    }

    @Test
    public void testDeserializeURL() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = mapper.getFactory().createParser("\"http://example.com\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<URL> deserializer = FromStringDeserializer.findDeserializer(URL.class);
        URL result = deserializer.deserialize(jsonParser, ctxt);
        assertEquals("http://example.com", result.toString());
    }

    @Test
    public void testDeserializeURI() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = mapper.getFactory().createParser("\"uri://example.com/path/to\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<URI> deserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI result = deserializer.deserialize(jsonParser, ctxt);
        assertEquals("uri://example.com/path/to", result.toString());
    }

    @Test
    public void testDeserializeClass() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = mapper.getFactory().createParser("\"java.lang.String\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<Class<?>> deserializer = FromStringDeserializer.findDeserializer(Class.class);
        Class<?> result = deserializer.deserialize(jsonParser, ctxt);
        assertEquals(String.class, result);
    }

    @Test
    public void testDeserializeJavaType() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = mapper.getFactory().createParser("\"com.example.MyClass\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JavaType javaType = mapper.getTypeFactory().constructType("java.util.List<java.lang.String>");
        FromStringDeserializer<JavaType> deserializer = FromStringDeserializer.findDeserializer(JavaType.class);
        JavaType result = deserializer.deserialize(jsonParser, ctxt);
        assertEquals(javaType.getType(), result.getType());
    }

    @Test
    public void testDeserializeCurrency() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = mapper.getFactory().createParser("\"EUR\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<Currency> deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        Currency result = deserializer.deserialize(jsonParser, ctxt);
        assertEquals(Currency.EUR, result);
    }

    @Test
    public void testDeserializePattern() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = mapper.getFactory().createParser("\"/^[A-Za-z]+$/\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<Pattern> deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        Pattern result = deserializer.deserialize(jsonParser, ctxt);
        assertTrue(result.matcher("abc").matches());
    }

    @Test
    public void testDeserializeLocale() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = mapper.getFactory().createParser("\"en_US\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<Locale> deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        Locale result = deserializer.deserialize(jsonParser, ctxt);
        assertEquals("en_US", result.toString());
    }

    @Test
    public void testDeserializeCharset() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = mapper.getFactory().createParser("\"UTF-8\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<Charset> deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        Charset result = deserializer.deserialize(jsonParser, ctxt);
        assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test
    public void testDeserializeTimeZone() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = mapper.getFactory().createParser("\"America/Los_Angeles\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<TimeZone> deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone result = deserializer.deserialize(jsonParser, ctxt);
        assertEquals("America/Los_Angeles", result.getID());
    }

    @Test
    public void testDeserializeInetAddress() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = mapper.getFactory().createParser("\"192.168.1.1\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<InetAddress> deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        InetAddress result = deserializer.deserialize(jsonParser, ctxt);
        assertEquals("192.168.1.1", result.getHostAddress());
    }

    @Test
    public void testDeserializeInetSocketAddress() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = mapper.getFactory().createParser("[\"192.168.1.1\", 80]");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<InetSocketAddress> deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        InetSocketAddress result = deserializer.deserialize(jsonParser, ctxt);
        assertEquals("192.168.1.1", result.getAddress().getHostAddress());
        assertEquals(80, result.getPort());
    }

    @Test
    public void testEmptyStringDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = mapper.getFactory().createParser("\"\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(Object.class);
        Object result = deserializer.deserialize(jsonParser, ctxt);
        assertNull(result);
    }
}