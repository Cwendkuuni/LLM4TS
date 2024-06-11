package com.fasterxml.jackson.databind.deser.std.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

public class FromStringDeserializerTest {

    private static ObjectMapper mapper;

    static {
        SimpleModule module = new SimpleModule();
        mapper = new ObjectMapper(new JsonFactory(), module);
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testDeserializeFile() throws JsonProcessingException {
        JsonParser jp = mapper.getFactory().createParser("\"C:/test.txt\"");
        FromStringDeserializer<File> deserializer = FromStringDeserializer.findDeserializer(File.class);
        File result = deserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals(new File("C:/test.txt"), result);
    }

    @Test
    public void testDeserializeURL() throws JsonProcessingException {
        JsonParser jp = mapper.getFactory().createParser("\"http://www.example.com\"");
        FromStringDeserializer<URL> deserializer = FromStringDeserializer.findDeserializer(URL.class);
        URL result = deserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals("http://www.example.com", result.toString());
    }

    @Test
    public void testDeserializeURI() throws JsonProcessingException {
        JsonParser jp = mapper.getFactory().createParser("\"http://user:pass@www.example.com:8080/test?query=param#fragment\"");
        FromStringDeserializer<URI> deserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI result = deserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals("http://user:pass@www.example.com:8080/test?query=param#fragment", result.toString());
    }

    @Test
    public void testDeserializeClass() throws JsonProcessingException {
        JsonParser jp = mapper.getFactory().createParser("\"java.lang.String.class\"");
        FromStringDeserializer<Class<?>> deserializer = FromStringDeserializer.findDeserializer(Class.class);
        Class<?> result = deserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals(String.class, result);
    }

    @Test
    public void testDeserializeJavaType() throws JsonProcessingException {
        JsonParser jp = mapper.getFactory().createParser("\"com.example.MyClass.java\"");
        FromStringDeserializer<JavaType> deserializer = FromStringDeserializer.findDeserializer(JavaType.class);
        JavaType result = deserializer.deserialize(jp, mapper.getDeserializationContext());
        assertInstanceOf(JavaType.ClassIdType.class, result);
    }

    @Test
    public void testDeserializeCurrency() throws JsonProcessingException {
        JsonParser jp = mapper.getFactory().createParser("\"EUR\"");
        FromStringDeserializer<Currency> deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        Currency result = deserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals(Currency.getInstance("EUR"), result);
    }

    @Test
    public void testDeserializePattern() throws JsonProcessingException {
        JsonParser jp = mapper.getFactory().createParser("\"/\\w+\\.\\w+\\b\"");
        FromStringDeserializer<Pattern> deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        Pattern result = deserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals("/\\w+\\.\\w+\\b", result.pattern().toString());
    }

    @Test
    public void testDeserializeLocale() throws JsonProcessingException {
        JsonParser jp = mapper.getFactory().createParser("\"en_US\"");
        FromStringDeserializer<Locale> deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        Locale result = deserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals(Locale.US, result);
    }

    @Test
    public void testDeserializeCharset() throws JsonProcessingException {
        JsonParser jp = mapper.getFactory().createParser("\"UTF-8\"");
        FromStringDeserializer<Charset> deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        Charset result = deserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test
    public void testDeserializeTimeZone() throws JsonProcessingException {
        JsonParser jp = mapper.getFactory().createParser("\"Europe/London\"");
        FromStringDeserializer<TimeZone> deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone result = deserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals("Europe/London", result.getID());
    }

    @Test
    public void testDeserializeInetAddress() throws JsonProcessingException {
        JsonParser jp = mapper.getFactory().createParser("\"192.168.1.1\"");
        FromStringDeserializer<InetAddress> deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        InetAddress result = deserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals("192.168.1.1", result.getHostAddress());
    }

    @Test
    public void testDeserializeInetSocketAddress() throws JsonProcessingException {
        JsonParser jp = mapper.getFactory().createParser("\"[255.255.255.255]:8080\"");
        FromStringDeserializer<InetSocketAddress> deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        InetSocketAddress result = deserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals("[255.255.255.255]:8080", result.toString());
    }

    @Test
    public void testDeserializeEmptyString() throws JsonProcessingException {
        JsonParser jp = mapper.getFactory().createParser("\"\"");
        FromStringDeserializer<Locale> deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        Locale result = (Locale) deserializer.deserialize(jp, mapper.getDeserializationContext());
        assertNull(result);
    }
}