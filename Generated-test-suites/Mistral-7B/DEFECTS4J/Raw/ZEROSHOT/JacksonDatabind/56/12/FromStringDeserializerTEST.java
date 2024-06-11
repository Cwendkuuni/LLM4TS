package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

public class FromStringDeserializerTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testDeserializeFile() throws IOException {
        String json = "\"/path/to/file.txt\"";
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(File.class);
        File result = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertEquals("/path/to/file.txt", result.getAbsolutePath());
    }

    @Test
    public void testDeserializeUrl() throws IOException {
        String json = "\"http://example.com\"";
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URL.class);
        URL result = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertEquals("http://example.com", result.toString());
    }

    @Test
    public void testDeserializeUri() throws IOException {
        String json = "\"http://example.com/path/to/resource\"";
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI result = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertEquals("http://example.com/path/to/resource", result.toString());
    }

    @Test
    public void testDeserializeClass() throws IOException {
        String json = "\"java.lang.String\"";
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
        String result = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext()).toString();
        assertEquals("java.lang.String", result);
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        String json = "\"com.example.MyClass\"";
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(MyClass.class);
        MyClass result = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertNotNull(result);
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        String json = "\"USD\"";
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        Currency result = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertEquals(Currency.USD, result);
    }

    @Test
    public void testDeserializePattern() throws IOException {
        String json = "\"/^[a-zA-Z]+$/\"";
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        Pattern result = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertNotNull(result);
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        String json = "\"en_US\"";
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        Locale result = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertEquals(Locale.US, result);
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        String json = "\"UTF-8\"";
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        Charset result = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        String json = "\"America/Los_Angeles\"";
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone result = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertEquals("America/Los_Angeles", result.getID());
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        String json = "\"192.168.1.1\"";
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        InetAddress result = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertNotNull(result);
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        String json = "[\"192.168.1.1\", 80]";
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        InetSocketAddress result = deserializer.deserialize(mapper.class.getResource("/DeserializationTest-inetsocketaddress.json").openStream(), mapper.getDeserializationContext());
        assertNotNull(result);
        assertEquals("192.168.1.1", result.getAddress().getHostAddress());
        assertEquals(80, result.getPort());
    }

    @Test
    public void testEmptyStringDeserialization() throws IOException {
        String json = "\"\"";
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI result = (URI) deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertEquals(URI.create(""), result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidString() throws IOException {
        String json = "\"InvalidType\"";
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
        deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        fail("Should throw JsonMappingException for unknown type");
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeEmptyEmbeddedObject() throws IOException {
        String json = "{}";
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(MyClass.class);
        deserializer.deserializeEmbedded(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        fail("Should throw JsonMappingException for empty embedded object");
    }

    @Test
    public void testDeserializeEmbeddedObject() throws IOException {
        String json = "{\"myField\": \"MyValue\"}";
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(MyClass.class);
        MyClass result = deserializer.deserializeEmbedded(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertNotNull(result);
        assertEquals("MyValue", result.getMyField());
    }

    static class MyClass {
        private String myField;

        public String getMyField() {
            return myField;
        }

        public void setMyField(String myField) {
            this.myField = myField;
        }
    }
}