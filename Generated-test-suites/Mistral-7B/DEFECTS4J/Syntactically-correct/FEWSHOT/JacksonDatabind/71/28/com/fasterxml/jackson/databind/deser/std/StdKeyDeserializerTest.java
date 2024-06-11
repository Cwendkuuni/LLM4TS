package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.io.NumberInput;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.StdKeyDeserializer.StringFactoryKeyDeserializer;
import com.fasterxml.jackson.databind.util.StdKeyDeserializer.StringKD;

/**
 * Test class for StdKeyDeserializer.
 */
public class StdKeyDeserializerTest {

    @Test
    public void testIntKeyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
        Object result = deserializer.deserializeKey("1", new TestContext());
        assertEquals(Integer.valueOf(1), result);
    }

    @Test
    public void testLongKeyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);
        Object result = deserializer.deserializeKey("123", new TestContext());
        assertEquals(Long.valueOf(123), result);
    }

    @Test
    public void testBooleanKeyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
        Object result = deserializer.deserializeKey("true", new TestContext());
        assertEquals(Boolean.TRUE, result);

        result = deserializer.deserializeKey("false", new TestContext());
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testStringKeyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StringKD.forType(String.class);
        Object result = deserializer.deserializeKey("test", new TestContext());
        assertEquals("test", result);
    }

    @Test
    public void testObjectKeyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StringKD.forType(Object.class);
        Object result = deserializer.deserializeKey("test", new TestContext());
        assertEquals("test", result.toString());
    }

    @Test
    public void testDateKeyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Date.class);
        Object result = deserializer.deserializeKey("2019-12-31", new TestContext());
        assertInstanceOf(Date.class, result);
    }

    @Test
    public void testCalendarKeyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Calendar.class);
        Object result = deserializer.deserializeKey("2019-12-31", new TestContext());
        assertInstanceOf(Calendar.class, result);
    }

    @Test
    public void testUUIDKeyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(UUID.class);
        Object result = deserializer.deserializeKey("123e4567-e89b-12d3-a456-426655440000", new TestContext());
        assertEquals(new UUID("123e4567-e89b-12d3-a456-426655440000"), result);
    }

    @Test
    public void testURIKeyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);
        Object result = deserializer.deserializeKey("http://example.com", new TestContext());
        assertEquals(new URI("http://example.com"), result);
    }

    @Test
    public void testURLKeyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);
        Object result = deserializer.deserializeKey("http://example.com", new TestContext());
        assertEquals(new URL("http://example.com"), result);
    }

    @Test
    public void testClassKeyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String[].class);
        Object result = deserializer.deserializeKey("[Ljava.lang.String;", new TestContext());
        assertInstanceOf(String[].class, result.getClass());
    }

    @Test
    public void testLocaleKeyDeserializer() throws Exception {
        Locale locale = new Locale("en", "US");
        AnnotatedClass ac = ObjectMapper.findAnnotatedClass(Locale.class, new TestAnnotationIntrospector());
        AnnotatedMethod factoryMethod = ac.findMethod("forLanguageTag");
        StdKeyDeserializer deserializer = new StringFactoryKeyDeserializer(factoryMethod);
        Object result = deserializer.deserializeKey("en-US", new TestContext());
        assertEquals(locale, result);
    }

    @Test
    public void testCurrencyKeyDeserializer() throws Exception {
        Currency currency = Currency.getInstance("USD");
        AnnotatedClass ac = ObjectMapper.findAnnotatedClass(Currency.class, new TestAnnotationIntrospector());
        AnnotatedMethod factoryMethod = ac.findMethod("getInstance", Locale.class, String.class);
        StdKeyDeserializer deserializer = new StringFactoryKeyDeserializer(factoryMethod);
        Object result = deserializer.deserializeKey("USD", new TestContext());
        assertEquals(currency, result);
    }

    @Test
    public void testInvalidKeyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
        try {
            deserializer.deserializeKey("invalid", new TestContext());
            fail();
        } catch (JsonMappingException expected) {
        }
    }

    @Test
    public void testNullKeyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
        Object result = deserializer.deserializeKey(null, new TestContext());
        assertNull(result);
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public ObjectMapper _getConfig() {
            return new ObjectMapper();
        }

        @Override
        public DeserializationConfig _config() {
            return new DeserializationConfig();
        }

        @Override
        public JsonParser _parser() {
            return new TestJsonParser();
        }

        @Override
        public JsonDeserializer<Object> _valueDeserializer() {
            return new StandardDeserializer<>();
        }

        @Override
        public Object _readValue(Class<?> clazz, JsonParser p) throws IOException {
            return p.getValueAsString();
        }

        @Override
        public Object _readValueAsArray(JsonParser p) throws IOException {
            return p.readValueAs(Object[].class);
        }

        @Override
        public Object _readValueAsTree(JsonParser p) throws IOException {
            return p.readValueAsTree();
        }

        @Override
        public Object _handleUnknownProperty(Object bean, String name, Object valueSource, String propertyName, JsonParser p)
                throws IOException {
            return valueSource;
        }

        @Override
        public Object _handleMissingProperty(Object bean, String propertyName) {
            return null;
        }

        @Override
        public void _reportError(String msg) throws JsonProcessingException {
            throw new JsonProcessingException(msg);
        }
    }

    private static class TestJsonParser extends JsonParserBase {
        @Override
        public void nextToken() throws IOException {
        }

        @Override
        public String getText() {
            return "";
        }

        @Override
        public int nextVal() throws IOException {
            return 0;
        }

        @Override
        public int nextIntValue() throws IOException {
            return 0;
        }

        @Override
        public long nextLongValue() throws IOException {
            return 0;
        }

        @Override
        public boolean nextBoolean() throws IOException {
            return false;
        }

        @Override
        public float nextFloatValue() throws IOException {
            return 0;
        }

        @Override
        public double nextDoubleValue() throws IOException {
            return 0;
        }

        @Override
        public void setCodec(JsonCodec<?> c) {
        }

        @Override
        public void setCodec(JsonCodec<?> c, String name) {
        }

        @Override
        public void close() throws IOException {
        }

        @Override
        public void close() {
        }
    }
}