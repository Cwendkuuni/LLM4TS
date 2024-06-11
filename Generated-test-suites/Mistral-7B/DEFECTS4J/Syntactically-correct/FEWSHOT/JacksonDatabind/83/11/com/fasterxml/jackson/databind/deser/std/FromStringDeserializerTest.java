package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

public class FromStringDeserializerTest extends TestBase {

    @SuppressWarnings("unchecked")
    private static <T> JavaType createType(Class<T> clazz) {
        return objectMapper.getTypeFactory().constructType(clazz);
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testDeserialize_File() throws IOException, JsonProcessingException {
        ObjectMapper mapper = objectMapper.copy();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        FromStringDeserializer<File> deserializer = (FromStringDeserializer<File>) findDeserializer(File.class);

        File file = deserializer.deserialize(jsonParser, deserializationContext);
        assertEquals("test.txt", file.getName());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserialize_URL() throws IOException, JsonProcessingException {
        ObjectMapper mapper = objectMapper.copy();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        FromStringDeserializer<URL> deserializer = (FromStringDeserializer<URL>) findDeserializer(URL.class);

        URL url = deserializer.deserialize(jsonParser, deserializationContext);
        assertEquals("http://test.com", url.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserialize_URI() throws IOException, JsonProcessingException {
        ObjectMapper mapper = objectMapper.copy();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        FromStringDeserializer<URI> deserializer = (FromStringDeserializer<URI>) findDeserializer(URI.class);

        URI uri = deserializer.deserialize(jsonParser, deserializationContext);
        assertEquals("http://test.com", uri.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserialize_Class() throws IOException, JsonProcessingException {
        ObjectMapper mapper = objectMapper.copy();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        FromStringDeserializer<Class<?>> deserializer = (FromStringDeserializer<Class<?>>) findDeserializer(Class.class);

        Class<?> cls = deserializer.deserialize(jsonParser, deserializationContext);
        assertEquals(String.class, cls);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserialize_JavaType() throws IOException, JsonProcessingException {
        ObjectMapper mapper = objectMapper.copy();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        FromStringDeserializer<JavaType> deserializer = (FromStringDeserializer<JavaType>) findDeserializer(JavaType.class);

        JavaType type = deserializer.deserialize(jsonParser, deserializationContext);
        JavaType expectedType = createType(String[].class);
        assertEquals(expectedType, type);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserialize_Currency() throws IOException, JsonProcessingException {
        ObjectMapper mapper = objectMapper.copy();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        FromStringDeserializer<Currency> deserializer = (FromStringDeserializer<Currency>) findDeserializer(Currency.class);

        Currency currency = deserializer.deserialize(jsonParser, deserializationContext);
        assertEquals(Currency.USD, currency);
    }

    @Test
    public void testDeserialize_Pattern_Invalid() throws IOException, JsonProcessingException {
        ObjectMapper mapper = objectMapper.copy();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        FromStringDeserializer<Pattern> deserializer = (FromStringDeserializer<Pattern>) findDeserializer(Pattern.class);

        assertThrows(JsonMappingException.class, () -> deserializer.deserialize(jsonParser, deserializationContext));
    }

    @Test
    public void testDeserialize_Pattern() throws IOException, JsonProcessingException {
        ObjectMapper mapper = objectMapper.copy();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        FromStringDeserializer<Pattern> deserializer = (FromStringDeserializer<Pattern>) findDeserializer(Pattern.class);

        Pattern pattern = deserializer.deserialize(jsonParser, deserializationContext);
        assertEquals("[\\w-]+(\\.[-.]?[\\w-]+)*@[A-Za-z0-9]+[\\.]{1}[A-Za-z]{2,}", pattern.pattern());
    }

    @Test
    public void testDeserialize_Locale() throws IOException, JsonProcessingException {
        ObjectMapper mapper = objectMapper.copy();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        FromStringDeserializer<Locale> deserializer = (FromStringDeserializer<Locale>) findDeserializer(Locale.class);

        Locale locale = deserializer.deserialize(jsonParser, deserializationContext);
        assertEquals(Locale.US, locale);
    }

    @Test
    public void testDeserialize_Charset() throws IOException, JsonProcessingException {
        ObjectMapper mapper = objectMapper.copy();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        FromStringDeserializer<Charset> deserializer = (FromStringDeserializer<Charset>) findDeserializer(Charset.class);

        Charset charset = deserializer.deserialize(jsonParser, deserializationContext);
        assertEquals(Charset.UTF_8, charset);
    }

    @Test
    public void testDeserialize_TimeZone() throws IOException, JsonProcessingException {
        ObjectMapper mapper = objectMapper.copy();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        FromStringDeserializer<TimeZone> deserializer = (FromStringDeserializer<TimeZone>) findDeserializer(TimeZone.class);

        TimeZone timeZone = deserializer.deserialize(jsonParser, deserializationContext);
        assertEquals("America/New_York", timeZone.getID());
    }

    @Test
    public void testDeserialize_InetAddress() throws IOException, JsonProcessingException {
        ObjectMapper mapper = objectMapper.copy();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        FromStringDeserializer<InetAddress> deserializer = (FromStringDeserializer<InetAddress>) findDeserializer(InetAddress.class);

        InetAddress inetAddress = deserializer.deserialize(jsonParser, deserializationContext);
        assertNotNull(inetAddress);
    }

    @Test
    public void testDeserialize_InetSocketAddress() throws IOException, JsonProcessingException {
        ObjectMapper mapper = objectMapper.copy();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        FromStringDeserializer<InetSocketAddress> deserializer = (FromStringDeserializer<InetSocketAddress>) findDeserializer(InetSocketAddress.class);

        InetSocketAddress inetSocketAddress = deserializer.deserialize(jsonParser, deserializationContext);
        assertEquals("127.0.0.1", inetSocketAddress.getAddress().getHostAddress());
        assertEquals(12345, inetSocketAddress.getPort());
    }

    @Test
    public void testDeserialize_StringBuilder() throws IOException, JsonProcessingException {
        ObjectMapper mapper = objectMapper.copy();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        FromStringDeserializer<StringBuilder> deserializer = (FromStringDeserializer<StringBuilder>) findDeserializer(StringBuilder.class);

        StringBuilder stringBuilder = deserializer.deserialize(jsonParser, deserializationContext);
        assertEquals("Test StringBuilder", stringBuilder.toString());
    }

    @Test
    public void testDeserialize_Null() throws IOException, JsonProcessingException {
        ObjectMapper mapper = objectMapper.copy();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        FromStringDeserializer<Object> deserializer = (FromStringDeserializer<Object>) findDeserializer(Object.class);

        Object obj = deserializer.deserialize(jsonParser, deserializationContext);
        assertNull(obj);
    }

    @Test
    public void testDeserialize_EmptyString() throws IOException, JsonProcessingException {
        ObjectMapper mapper = objectMapper.copy();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        FromStringDeserializer<Object> deserializer = (FromStringDeserializer<Object>) findDeserializer(Object.class);

        Object obj = deserializer.deserialize(jsonParser, deserializationContext);
        assertNull(obj);
    }

    @Test
    public void testDeserialize_Array() throws IOException, JsonProcessingException {
        ObjectMapper mapper = objectMapper.copy();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        FromStringDeserializer<Object> deserializer = (FromStringDeserializer<Object>) findDeserializer(Object.class);

        JsonToken jsonToken = jsonParser.getCurrentToken();
        assertNotNull(jsonToken);
        assertEquals(JsonToken.START_ARRAY, jsonToken.getId());

        Object obj = deserializer.deserialize(jsonParser, deserializationContext);
        assertInstanceOf(Object[].class, obj);
    }

    @Test
    public void testDeserialize_EmbeddedObject_Null() throws IOException, JsonProcessingException {
        ObjectMapper mapper = objectMapper.copy();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        FromStringDeserializer<Object> deserializer = (FromStringDeserializer<Object>) findDeserializer(Object.class);

        JsonToken jsonToken = jsonParser.nextToken();
        assertNotNull(jsonToken);
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, jsonToken.getId());

        Object obj = deserializer.deserialize(jsonParser, deserializationContext);
        assertNull(obj);
    }

    @Test
    public void testDeserialize_EmbeddedObject_InvalidType() throws IOException, JsonProcessingException {
        ObjectMapper mapper = objectMapper.copy();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        FromStringDeserializer<Object> deserializer = (FromStringDeserializer<Object>) findDeserializer(Object.class);

        JsonToken jsonToken = jsonParser.nextToken();
        assertNotNull(jsonToken);
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, jsonToken.getId());

        Object obj = jsonParser.getEmbeddedObject();
        assertNotNull(obj);
        assertFalse(Object.class.isAssignableFrom(obj.getClass()));

        try {
            deserializer.deserializeEmbedded(obj, deserializationContext);
            fail();
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testDeserialize_EmbeddedObject_String() throws IOException, JsonProcessingException {
        ObjectMapper mapper = objectMapper.copy();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        FromStringDeserializer<Object> deserializer = (FromStringDeserializer<Object>) findDeserializer(Object.class);

        JsonToken jsonToken = jsonParser.nextToken();
        assertNotNull(jsonToken);
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, jsonToken.getId());

        Object obj = jsonParser.getEmbeddedObject();
        assertNotNull(obj);
        assertEquals("Test String", obj.toString());

        Object result = deserializer.deserializeEmbedded(obj, deserializationContext);
        assertEquals("Test String", result.toString());
    }

    @Test
    public void testDeserialize_EmbeddedObject_Number() throws IOException, JsonProcessingException {
        ObjectMapper mapper = objectMapper.copy();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        FromStringDeserializer<Object> deserializer = (FromStringDeserializer<Object>) findDeserializer(Object.class);

        JsonToken jsonToken = jsonParser.nextToken();
        assertNotNull(jsonToken);
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, jsonToken.getId());

        Object obj = jsonParser.getEmbeddedObject();
        assertNotNull(obj);
        assertEquals(123, obj.hashCode());

        Object result = deserializer.deserializeEmbedded(obj, deserializationContext);
        assertEquals(123, result.hashCode());
    }

    @Test
    public void testDeserialize_EmbeddedObject_Array() throws IOException, JsonProcessingException {
        ObjectMapper mapper = objectMapper.copy();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        FromStringDeserializer<Object> deserializer = (FromStringDeserializer<Object>) findDeserializer(Object.class);

        JsonToken jsonToken = jsonParser.nextToken();
        assertNotNull(jsonToken);
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, jsonToken.getId());

        Object obj = jsonParser.getEmbeddedObject();
        assertNotNull(obj);
        assertInstanceOf(Object[].class, obj);

        Object result = deserializer.deserializeEmbedded(obj, deserializationContext);
        assertInstanceOf(Object[].class, result);
    }

    @Test
    public void testDeserialize_EmbeddedObject_UnknownClass() throws IOException, JsonProcessingException {
        ObjectMapper mapper = objectMapper.copy();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        FromStringDeserializer<Object> deserializer = (FromStringDeserializer<Object>) findDeserializer(Object.class);

        JsonToken jsonToken = jsonParser.nextToken();
        assertNotNull(jsonToken);
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, jsonToken.getId());

        Object obj = jsonParser.getEmbeddedObject();
        assertNotNull(obj);
        assertEquals("com.test.UnknownClass", obj.getClass().getName());

        JsonMappingException ex = assertThrows(JsonMappingException.class, () -> deserializer.deserializeEmbedded(obj, deserializationContext));
        assertEquals("Don't know how to convert embedded Object of type com.test.UnknownClass into Object", ex.getMessage());
    }

    private FromStringDeserializer<?> findDeserializer(Class<?> rawType) {
        return (FromStringDeserializer<?>) Std.findDeserializer(rawType);
    }
}