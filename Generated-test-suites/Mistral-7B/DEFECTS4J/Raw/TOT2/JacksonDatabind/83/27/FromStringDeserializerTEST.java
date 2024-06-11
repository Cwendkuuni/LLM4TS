package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class FromStringDeserializerTest {

    @Test
    public void testURLDeserialization() throws IOException, URISyntaxException {
        ObjectMapper mapper = new JsonMapper();
        Module module = new SimpleModule();
        module.addSerializer(URL.class, new FromStringDeserializer<URL>(URL.class));
        mapper.registerModule(module);

        ObjectReader reader = mapper.readerForClass(URL.class);
        String jsonString = "\"http://www.example.com\"";
        URL url = reader.readValue(jsonString);
        assertEquals("http://www.example.com", url.toExternalForm());
    }

    @Test
    public void testURIDeserialization() throws IOException, URISyntaxException {
        ObjectMapper mapper = new JsonMapper();
        Module module = new SimpleModule();
        module.addSerializer(URI.class, new FromStringDeserializer<URI>(URI.class));
        mapper.registerModule(module);

        ObjectReader reader = mapper.readerForClass(URI.class);
        String jsonString = "\"http://user:pass@www.example.com:8080/path/to/file?query=param#fragment\"";
        URI uri = reader.readValue(jsonString);
        assertEquals("http://user:pass@www.example.com:8080/path/to/file?query=param#fragment",
                uri.toURL().toExternalForm());
    }

    @Test
    public void testLocaleDeserialization() throws IOException {
        ObjectMapper mapper = new JsonMapper();
        Module module = new SimpleModule();
        module.addSerializer(Locale.class, new FromStringDeserializer<Locale>(Locale.class));
        mapper.registerModule(module);

        ObjectReader reader = mapper.readerForClass(Locale.class);
        String jsonString = "\"en-US\"";
        Locale locale = reader.readValue(jsonString);
        assertEquals("en-US", locale.toString());
    }

    @Test
    public void testInvalidURLDeserialization() throws IOException {
        ObjectMapper mapper = new JsonMapper();
        Module module = new SimpleModule();
        module.addSerializer(URL.class, new FromStringDeserializer<URL>(URL.class));
        mapper.registerModule(module);

        ObjectReader reader = mapper.readerForClass(URL.class);
        String jsonString = "\"not-a-url\"";
        assertThrows(InvalidFormatException.class, () -> reader.readValue(jsonString));
    }

    @Test
    public void testInvalidURIDeserialization() throws IOException, URISyntaxException {
        ObjectMapper mapper = new JsonMapper();
        Module module = new SimpleModule();
        module.addSerializer(URI.class, new FromStringDeserializer<URI>(URI.class));
        mapper.registerModule(module);

        ObjectReader reader = mapper.readerForClass(URI.class);
        String jsonString = "\"malformed-uri\"";
        assertThrows(URISyntaxException.class, () -> reader.readValue(jsonString));
    }

    @Test
    public void testEmptyStringDeserialization() throws IOException {
        ObjectMapper mapper = new JsonMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        ObjectReader reader = mapper.readerForClass(StringBuilder.class);
        String jsonString = "\"\"";
        StringBuilder stringBuilder = reader.readValue(jsonString);
        assertNull(stringBuilder);
    }

    @Test
    public void testNullDeserialization() throws IOException {
        ObjectMapper mapper = new JsonMapper();

        ObjectReader reader = mapper.readerForClass(URL.class);
        String jsonString = "null";
        assertNull(reader.readValue(jsonString));
    }

    @Test
    public void testDeserializationFromArray() throws IOException {
        ObjectMapper mapper = new JsonMapper();

        String jsonString = "[\"Hello\", 123]";
        ObjectReader reader = mapper.readerForArrayOf(String.class);
        Object[] result = reader.readValue(jsonString);
        assertEquals(2, result.length);
        assertEquals("Hello", result[0]);
        assertEquals("123", result[1]);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializationFromInvalidType() throws IOException {
        ObjectMapper mapper = new JsonMapper();

        ObjectReader reader = mapper.readerForClass(URL.class);
        String jsonString = "123";
        reader.readValue(jsonString);
    }
}