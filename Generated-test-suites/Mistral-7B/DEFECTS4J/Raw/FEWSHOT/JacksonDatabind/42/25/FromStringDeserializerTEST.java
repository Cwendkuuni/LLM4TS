package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.io.IOException;
import java.net.*;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

public class FromStringDeserializerTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeFile() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser jp = factory.createParser("\"/path/to/file.txt\"");
        FromStringDeserializer deserializer = (FromStringDeserializer) FromStringDeserializer.findDeserializer(File.class);
        File result = deserializer.deserialize(jp, ctxt);
        assertNotNull(result);
        assertEquals("/path/to/file.txt", result.getAbsolutePath());
    }

    @Test
    public void testDeserializeURL() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser jp = factory.createParser("\"http://www.example.com\"");
        FromStringDeserializer deserializer = (FromStringDeserializer) FromStringDeserializer.findDeserializer(URL.class);
        URL result = deserializer.deserialize(jp, ctxt);
        assertNotNull(result);
        assertEquals("http://www.example.com", result.toString());
    }

    @Test
    public void testDeserializeURI() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser jp = factory.createParser("\"http://user:pass@www.example.com:8080/path/to/file.txt?query=param#fragment\"");
        FromStringDeserializer deserializer = (FromStringDeserializer) FromStringDeserializer.findDeserializer(URI.class);
        URI result = deserializer.deserialize(jp, ctxt);
        assertNotNull(result);
        assertEquals("http://user:pass@www.example.com:8080/path/to/file.txt?query=param#fragment", result.toString());
    }

    @Test
    public void testDeserializeClass() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser jp = factory.createParser("\"java.lang.String\"");
        FromStringDeserializer deserializer = (FromStringDeserializer) FromStringDeserializer.findDeserializer(String.class);
        Class<?> result = deserializer.deserialize(jp, ctxt);
        assertNotNull(result);
        assertEquals(String.class, result);
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser jp = factory.createParser("\"com.example.MyClass\"");
        FromStringDeserializer deserializer = (FromStringDeserializer) FromStringDeserializer.findDeserializer(MyClass.class);
        JavaType result = deserializer.deserialize(jp, ctxt);
        assertNotNull(result);
        assertEquals(MyClass.class, result.getRawClass());
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser jp = factory.createParser("\"USD\"");
        FromStringDeserializer deserializer = (FromStringDeserializer) FromStringDeserializer.findDeserializer(Currency.class);
        Currency result = deserializer.deserialize(jp, ctxt);
        assertNotNull(result);
        assertEquals(Currency.getInstance("USD"), result);
    }

    @Test
    public void testDeserializePattern() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser jp = factory.createParser("\"\\w+(\\.\\w+)*@\\w+(\\.\\w+)*\\.\\w+\"");
        FromStringDeserializer deserializer = (FromStringDeserializer) FromStringDeserializer.findDeserializer(Pattern.class);
        Pattern result = deserializer.deserialize(jp, ctxt);
        assertNotNull(result);
        assertTrue(result.matcher("test@example.com").matches());
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser jp = factory.createParser("\"en_US\"");
        FromStringDeserializer deserializer = (FromStringDeserializer) FromStringDeserializer.findDeserializer(Locale.class);
        Locale result = deserializer.deserialize(jp, ctxt);
        assertNotNull(result);
        assertEquals(Locale.US, result);
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser jp = factory.createParser("\"UTF-8\"");
        FromStringDeserializer deserializer = (FromStringDeserializer) FromStringDeserializer.findDeserializer(Charset.class);
        Charset result = deserializer.deserialize(jp, ctxt);
        assertNotNull(result);
        assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser jp = factory.createParser("\"America/Los_Angeles\"");
        FromStringDeserializer deserializer = (FromStringDeserializer) FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone result = deserializer.deserialize(jp, ctxt);
        assertNotNull(result);
        assertEquals(TimeZone.getTimeZone("America/Los_Angeles"), result);
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser jp = factory.createParser("\"192.168.1.1\"");
        FromStringDeserializer deserializer = (FromStringDeserializer) FromStringDeserializer.findDeserializer(InetAddress.class);
        InetAddress result = deserializer.deserialize(jp, ctxt);
        assertNotNull(result);
        assertEquals("192.168.1.1", result.getHostAddress());
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser jp = factory.createParser("\"[255.255.255.255]:8080\"");
        FromStringDeserializer deserializer = (FromStringDeserializer) FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        InetSocketAddress result = deserializer.deserialize(jp, ctxt);
        assertNotNull(result);
        assertEquals("255.255.255.255", result.getAddress().getHostAddress());
        assertEquals(8080, result.getPort());
    }

    @Test
    public void testInvalidDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser jp = factory.createParser("\"invalid\"");
        FromStringDeserializer deserializer = (FromStringDeserializer) FromStringDeserializer.findDeserializer(Object.class);
        try {
            deserializer.deserialize(jp, ctxt);
            fail();
        } catch (JsonMappingException e) {
            assertEquals("Cannot construct instance of java.lang.Object from String \"invalid\": not a valid textual representation", e.getMessage());
        }
    }
}