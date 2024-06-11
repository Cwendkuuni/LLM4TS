package com.example.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class FromStringDeserializerTest {

    @Test
    public void testValidDeserialization() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = objectMapper.getFactory();

        String jsonString = "{\"file\":\"testFile.txt\",\"url\":\"http://example.com\",\"uri\":\"file:/test/dir/testFile.txt\",\"class\":\"java.lang.String\",\"javaType\":\"com.example.MyClass\",\"currency\":\"USD\",\"pattern\":\"\\d+\\.\\d+\",\"locale\":\"en_US\",\"charset\":\"UTF-8\",\"timeZone\":\"America/Los_Angeles\",\"inetAddress\":\"127.0.0.1\",\"inetSocketAddress\":{\"address\":\"127.0.0.1\",\"port\":8069}}";
        JsonParser jsonParser = jsonFactory.getParser(jsonString);
        DeserializationContext context = objectMapper.getDeserializationContext();

        FromStringDeserializer deserializer = new FromStringDeserializer.Std(Locale.class);
        Locale locale = deserializer.deserialize(jsonParser, context);
        assertEquals(Locale.US, locale);

        // Test other supported types
        File file = deserializer.findDeserializer(File.class).deserialize(jsonParser, context);
        URI uri = deserializer.findDeserializer(URI.class).deserialize(jsonParser, context);
        Class<?> clazz = deserializer.findDeserializer(String.class).deserialize(jsonParser, context);
        JavaType javaType = deserializer.findDeserializer(JavaType.class).deserialize(jsonParser, context);
        Currency currency = deserializer.findDeserializer(Currency.class).deserialize(jsonParser, context);
        Pattern pattern = deserializer.findDeserializer(Pattern.class).deserialize(jsonParser, context);
        Charset charset = deserializer.findDeserializer(Charset.class).deserialize(jsonParser, context);
        TimeZone timeZone = deserializer.findDeserializer(TimeZone.class).deserialize(jsonParser, context);
        InetAddress inetAddress = deserializer.findDeserializer(InetAddress.class).deserialize(jsonParser, context);
        InetSocketAddress inetSocketAddress = deserializer.findDeserializer(InetSocketAddress.class).deserialize(jsonParser, context);

        assertNotNull(file);
        assertNotNull(uri);
        assertEquals(Class.forName("java.lang.String"), clazz);
        assertEquals(JavaType.constructType(int.class), javaType);
        assertNotNull(currency);
        assertNotNull(pattern);
        assertEquals(Charset.forName("UTF-8"), charset);
        assertNotNull(timeZone);
        assertNotNull(inetAddress);
        assertNotNull(inetSocketAddress);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidDeserializationEmptyString() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = objectMapper.getFactory();

        String jsonString = "\"\"";
        JsonParser jsonParser = jsonFactory.getParser(jsonString);
        DeserializationContext context = objectMapper.getDeserializationContext();

        FromStringDeserializer deserializer = new FromStringDeserializer.Std(Locale.class);
        deserializer.deserialize(jsonParser, context);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidDeserializationInvalidFormat() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = objectMapper.getFactory();

        String jsonString = "\"invalidFormat\"";
        JsonParser jsonParser = jsonFactory.getParser(jsonString);
        DeserializationContext context = objectMapper.getDeserializationContext();

        FromStringDeserializer deserializer = new FromStringDeserializer.Std(Locale.class);
        deserializer.deserialize(jsonParser, context);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidDeserializationUnsupportedType() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = objectMapper.getFactory();

        String jsonString = "{\"unsupportedType\":\"test\"}";
        JsonParser jsonParser = jsonFactory.getParser(jsonString);
        DeserializationContext context = objectMapper.getDeserializationContext();

        FromStringDeserializer deserializer = new FromStringDeserializer.Std(Locale.class);
        deserializer.deserialize(jsonParser, context);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidDeserializationMalformedURI() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = objectMapper.getFactory();

        String jsonString = "{" +
                "\"uri\":\"invalid:/test/dir/testFile.txt\"}";
        JsonParser jsonParser = jsonFactory.getParser(jsonString);
        DeserializationContext context = objectMapper.getDeserializationContext();

        FromStringDeserializer deserializer = new FromStringDeserializer.Std(URI.class);
        deserializer.deserialize(jsonParser, context);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidDeserializationMalformedLocale() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = objectMapper.getFactory();

        String jsonString = "{" +
                "\"locale\":\"en_US_InvalidCountry\"}";
        JsonParser jsonParser = jsonFactory.getParser(jsonString);
        DeserializationContext context = objectMapper.getDeserializationContext();

        FromStringDeserializer deserializer = new FromStringDeserializer.Std(Locale.class);
        deserializer.deserialize(jsonParser, context);
    }
}