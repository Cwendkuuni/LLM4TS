package com.example.tests.jackson;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;
import java.util.Currency;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.type.SimpleType;

import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.deser.std.Std;

@RunWith(Parameterized.class)
public class FromStringDeserializerTest {

    private static final JsonFactory JSON_FACTORY = new ObjectMapper().getFactory();

    private static final Class<?>[] supportedTypes = FromStringDeserializer.types();
    private Class<?> type;

    public FromStringDeserializerTest(Class<?> type) {
        this.type = type;
    }

    @Parameterized.Parameters(name = "Test type: {0}")
    public static Object[] data() {
        return new Object[] {
                File.class,
                URL.class,
                URI.class,
                Class.class,
                JavaType.class,
                Currency.class,
                Pattern.class,
                Locale.class,
                Charset.class,
                TimeZone.class,
                InetAddress.class,
                InetSocketAddress.class
        };
    }

    @Test
    public void testDeserializeSuccess() throws IOException {
        JsonParser parser = JSON_FACTORY.createParser("{\"type\":\"" + type.getName() + "\", \"value\":\"some_valid_string\"}");
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        StdDeserializer deserializer = (StdDeserializer) FromStringDeserializer.findDeserializer(type);
        Object result = deserializer.deserialize(parser, mapper.getDeserializationContext());

        assertEquals(type, result.getClass());
        parser.close();
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidFormatUrl() throws IOException {
        JsonParser parser = JSON_FACTORY.createParser("{\"type\":\"" + URL.class.getName() + "\", \"value\":\"invalid_url\"}");
        ObjectMapper mapper = new ObjectMapper();
        StdDeserializer deserializer = (StdDeserializer) FromStringDeserializer.findDeserializer(URL.class);
        deserializer.deserialize(parser, mapper.getDeserializationContext());
        fail("Should have thrown JsonMappingException");
        parser.close();
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidFormatCurrency() throws IOException {
        JsonParser parser = JSON_FACTORY.createParser("{\"type\":\"" + Currency.class.getName() + "\", \"value\":\"EUR100.50\"}");
        ObjectMapper mapper = new ObjectMapper();
        StdDeserializer deserializer = (StdDeserializer) FromStringDeserializer.findDeserializer(Currency.class);
        deserializer.deserialize(parser, mapper.getDeserializationContext());
        fail("Should have thrown JsonMappingException");
        parser.close();
    }

    @Test
    public void testDeserializeNull() throws IOException {
        JsonParser parser = JSON_FACTORY.createParser("{\"type\":\"" + String.class.getName() + "\", \"value\":null}");
        ObjectMapper mapper = new ObjectMapper();
        StdDeserializer deserializer = (StdDeserializer) FromStringDeserializer.findDeserializer(String.class);
        Object result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertNull(result);
        parser.close();
    }

    @Test
    public void testDeserializeEmptyString() throws IOException {
        JsonParser parser = JSON_FACTORY.createParser("{\"type\":\"" + String.class.getName() + "\", \"value\":\"\"}");
        ObjectMapper mapper = new ObjectMapper();
        StdDeserializer deserializer = (StdDeserializer) FromStringDeserializer.findDeserializer(String.class);
        Object result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertNull(result);
        parser.close();
    }

    @Test
    public void testDeserializeEmbeddedObject() throws IOException {
        JsonParser parser = JSON_FACTORY.createParser("{\"type\":\"java.net.URI\", \"value\":{\"scheme\":\"http\", \"authority\":\"example.com\"}}");
        ObjectMapper mapper = new ObjectMapper();
        StdDeserializer deserializer = (StdDeserializer) FromStringDeserializer.findDeserializer(URI.class);
        URI result = (URI) deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals("http", result.getScheme());
        assertEquals("example.com", result.getHost());
        parser.close();
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidType() throws IOException {
        JsonParser parser = JSON_FACTORY.createParser("{\"type\":\"java.lang.Integer\", \"value\":\"some_valid_string\"}");
        ObjectMapper mapper = new ObjectMapper();
        StdDeserializer deserializer = (StdDeserializer) FromStringDeserializer.findDeserializer(String.class);
        deserializer.deserialize(parser, mapper.getDeserializationContext());
        fail("Should have thrown JsonMappingException");
        parser.close();
    }

    static class StdDeserializer extends FromStringDeserializer<Object> {
        @Override
        protected Object _deserialize(String value, DeserializationContext ctxt) throws IOException {
            try {
                return super._deserialize(value, ctxt);
            } catch (InvalidFormatException e) {
                throw new JsonMappingException(e.getMessage(), e);
            }
        }
    }
}