package com.example.junit;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FromStringDeserializerTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testDeserializeFile() throws IOException {
        FromStringDeserializer<File> deserializer = FromStringDeserializer.findDeserializer(File.class);
        File expected = new File("test.txt");
        File actual = deserializer.deserialize(getJsonParser("\"test.txt\""), createContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeUrl() throws IOException {
        FromStringDeserializer<URL> deserializer = FromStringDeserializer.findDeserializer(URL.class);
        URL expected = new URL("http://example.com");
        URL actual = deserializer.deserialize(getJsonParser("\"http://example.com\""), createContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeUri() throws IOException {
        FromStringDeserializer<URI> deserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI expected = new URI("http://example.com");
        URI actual = deserializer.deserialize(getJsonParser("\"http://example.com\""), createContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeClass() throws IOException {
        FromStringDeserializer<StringBuilder> deserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
        StringBuilder expected = new StringBuilder("test");
        StringBuilder actual = deserializer.deserialize(getJsonParser("\"test\""), createContext());
        assertEquals(expected, actual);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidType() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Integer.class);
        deserializer.deserialize(getJsonParser("\"test\""), createContext());
    }

    @Test
    public void testDeserializeEmptyString() throws IOException {
        FromStringDeserializer<String> deserializer = FromStringDeserializer.findDeserializer(String.class);
        String actual = deserializer.deserialize(getJsonParser("\"\""), createContext()).getValueAsString();
        assertNull(actual);
    }

    @Test
    public void testDeserializeInvalidString() throws IOException {
        FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(Object.class);
        deserializer.deserialize(getJsonParser("\"invalid\""), createContext());
    }

    @Test
    public void testDeserializeArray() throws IOException {
        FromStringDeserializer<Object[]> deserializer = FromStringDeserializer.findDeserializer(Object[].class);
        Object[] expected = new Object[]{new Integer(1), new Integer(2)};
        Object[] actual = deserializer.deserialize(getJsonParser("[1, 2]"), createContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeEmbeddedObject() throws IOException {
        FromStringDeserializer<Locale> deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        Locale expected = new Locale("en_US");
        Locale actual = deserializer.deserialize(getJsonParser("{\"language\":\"en\",\"country\":\"US\"}"), createContext());
        assertEquals(expected, actual);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidEmbeddedObject() throws IOException {
        FromStringDeserializer<Locale> deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        deserializer.deserialize(getJsonParser("{\"invalid\":\"test\"}"), createContext());
    }

    private JsonParser getJsonParser(String jsonString) throws IOException {
        return mapper.getFactory().createParser(jsonString);
    }

    private DeserializationContext createContext() {
        return mapper.getDeserializationConfig().createContext();
    }
}