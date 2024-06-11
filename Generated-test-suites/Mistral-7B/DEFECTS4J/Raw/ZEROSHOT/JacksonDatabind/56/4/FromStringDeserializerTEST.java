package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;

@RunWith(MockitoJUnitRunner.class)
public class FromStringDeserializerTest {

    @Test
    public void testFileDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        String json = "\"C:\\test.txt\"";
        FromStringDeserializer<File> deserializer = FromStringDeserializer.findDeserializer(File.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        File result = deserializer.deserialize(Mockito.mock(JsonParser.class), context);
        assertEquals(new File("C:\\test.txt"), result);
    }

    @Test
    public void testUrlDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        String json = "\"http://example.com\"";
        FromStringDeserializer<URL> deserializer = FromStringDeserializer.findDeserializer(URL.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        URL result = deserializer.deserialize(Mockito.mock(JsonParser.class), context);
        assertEquals(new URL("http://example.com"), result);
    }

    @Test
    public void testUriDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        String json = "\"http://example.com/path?query=value#fragment\"";
        FromStringDeserializer<URI> deserializer = FromStringDeserializer.findDeserializer(URI.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        URI result = deserializer.deserialize(Mockito.mock(JsonParser.class), context);
        assertEquals(new URI("http://example.com/path?query=value#fragment"), result);
    }

    @Test
    public void testClassDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        String json = "java.util.Locale";
        FromStringDeserializer<Class<?>> deserializer = FromStringDeserializer.findDeserializer(Class.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Class<?> result = deserializer.deserialize(Mockito.mock(JsonParser.class), context);
        assertEquals(Locale.class, result);
    }

    @Test
    public void testJavaTypeDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        String json = "com.example.MyType";
        FromStringDeserializer<JavaType> deserializer = FromStringDeserializer.findDeserializer(JavaType.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        JavaType result = deserializer.deserialize(Mockito.mock(JsonParser.class), context);
        assertEquals(new TypeFactory().constructType(ClassUtil.getCanonicalName(MyType.class)), result);
    }

    @Test
    public void testCurrencyDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        String json = "\"USD\"";
        FromStringDeserializer<Currency> deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Currency result = deserializer.deserialize(Mockito.mock(JsonParser.class), context);
        assertEquals(Currency.getInstance("USD"), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnknownDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        String json = "java.util.List";
        FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(Object.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        deserializer.deserialize(Mockito.mock(JsonParser.class), context);
    }

    @Test
    public void testEmbeddedObjectDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        String json = "{\"className\":\"java.util.Locale\",\"country\":\"US\",\"language\":\"en\"}";
        FromStringDeserializer<Locale> deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Locale result = (Locale) deserializer.deserializeEmbedded(Mockito.mock(Object.class), context);
        assertEquals(Locale.US, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testEmptyStringDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        String json = "\"\"";
        FromStringDeserializer<String> deserializer = FromStringDeserializer.findDeserializer(String.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        deserializer.deserialize(Mockito.mock(JsonParser.class), context);
    }

    @Test
    public void testEmptyStringDeserializationLocale() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        String json = "\"\"";
        FromStringDeserializer<Locale> deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Locale result = deserializer.deserializeEmbedded(Mockito.mock(Object.class), context);
        assertEquals(Locale.ROOT, result);
    }

    @Test
    public void testEmptyStringDeserializationUrl() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        String json = "\"\"";
        FromStringDeserializer<URL> deserializer = FromStringDeserializer.findDeserializer(URL.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        URL result = deserializer.deserializeEmbedded(Mockito.mock(Object.class), context);
        assertNull(result);
    }

    @Test
    public void testInvalidFormatException() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        String json = "\"invalidDate\"";
        FromStringDeserializer<Date> deserializer = FromStringDeserializer.findDeserializer(Date.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        try {
            deserializer.deserializeEmbedded(Mockito.mock(Object.class), context);
            fail();
        } catch (JsonMappingException e) {
            assertEquals("Failed to parse Date value [\"invalidDate\"]: Cannot parse value of type java.util.Date from String \"invalidDate\": Unparseable date: \"invalidDate\"", e.getMessage());
        }
    }

    private static class MyType {
    }
}