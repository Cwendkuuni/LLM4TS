package com.example.junit.TestCases;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class FromStringDeserializerTest {

    @SuppressWarnings("unchecked")
    private static <T> T deserialize(Class<T> clazz, String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream(jsonString.getBytes()));
        DeserializationContext context = mapper.getDeserializationContext();
        StdScalarDeserializer<T> deserializer = (StdScalarDeserializer<T>) FromStringDeserializer.findDeserializer(clazz);
        return deserializer.deserialize(parser, context);
    }

    @Test
    public void testDeserializeFile() {
        String jsonString = "\"C:\\test\\file.txt\"";
        File expected = new File("C:\\test\\file.txt");
        File actual = deserialize(File.class, jsonString);
        assertEquals(expected.getAbsolutePath(), actual.getAbsolutePath());
    }

    @Test
    public void testDeserializeURL() {
        String jsonString = "\"http://www.example.com\"";
        URL expected = new URL("http://www.example.com");
        URL actual = deserialize(URL.class, jsonString);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testDeserializeURI() {
        String jsonString = "\"http://www.example.com/path/to/file\"";
        URI expected = new URI("http", "www.example.com", "/path/to/file");
        URI actual = deserialize(URI.class, jsonString);
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeClass() {
        String jsonString = "\"java.util.StringTokenizer\"";
        Class<?> expected = StringTokenizer.class;
        Class<?> actual = deserialize(Class.class, jsonString);
        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    public void testDeserializeJavaType() {
        String jsonString = "\"com.example.Person:[name:\"John\",age:30]\"";
        JavaType expected = new ObjectMapper().getTypeFactory().constructType(Person.class);
        JavaType actual = deserialize(JavaType.class, jsonString);
        assertEquals(expected.getTypeId(), actual.getTypeId());
    }

    @Test
    public void testDeserializeCurrency() {
        String jsonString = "\"USD\"";
        Currency expected = Currency.getInstance("USD");
        Currency actual = deserialize(Currency.class, jsonString);
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializePattern() {
        String jsonString = "\"\\w+@\\w+\\.\\w+\"";
        Pattern expected = Pattern.compile("\\w+@\\w+\\.\\w+");
        Pattern actual = deserialize(Pattern.class, jsonString);
        assertEquals(expected.pattern(), actual.pattern());
    }

    @Test
    public void testDeserializeLocale() {
        String jsonString = "\"en_US\"";
        Locale expected = new Locale("en", "US");
        Locale actual = deserialize(Locale.class, jsonString);
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeCharset() {
        String jsonString = "\"US-ASCII\"";
        Charset expected = Charset.forName("US-ASCII");
        Charset actual = deserialize(Charset.class, jsonString);
        assertEquals(expected.name(), actual.name());
    }

    @Test
    public void testDeserializeTimeZone() {
        String jsonString = "\"America/Los_Angeles\"";
        TimeZone expected = TimeZone.getTimeZone("America/Los_Angeles");
        TimeZone actual = deserialize(TimeZone.class, jsonString);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeInvalidString() {
        String jsonString = "\"invalidType\"";
        deserialize(String.class, jsonString);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeEmptyString() {
        String jsonString = "\"\"";
        deserialize(String.class, jsonString);
    }

    @Test
    public void testDeserializeEmbeddedObject() {
        String jsonString = "{\"class\":\"java.net.URL\",\"value\":\"http://example.com\"}";
        JsonNode jsonNode = new ObjectMapper().readTree(jsonString);
        ObjectNode objectNode = (ObjectNode) jsonNode;
        String className = objectNode.get("class").asText();
        String value = objectNode.get("value").asText();
        URL expected = new URL(value);
        URL actual = deserialize(URL.class, jsonString);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidEmbeddedObject() {
        String jsonString = "{\"class\":\"java.net.URL\",\"value\":\"invalid\"}";
        deserialize(URL.class, jsonString);
    }
}