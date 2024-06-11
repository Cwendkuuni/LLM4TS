package com.example; // replace with your test package

import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class StdKeyDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testBooleanTypeDeserialization() throws IOException {
        StdKeyDeserializer booleanDeserializer = StdKeyDeserializer.forType(Boolean.class);
        DeserializationContext context = mockDeserializationContext();
        String key = "true";
        Object result = booleanDeserializer.deserializeKey(key, context);
        assertEquals(Boolean.TRUE, result);

        key = "false";
        result = booleanDeserializer.deserializeKey(key, context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testByteTypeDeserialization() throws IOException {
        StdKeyDeserializer byteDeserializer = StdKeyDeserializer.forType(Byte.class);
        DeserializationContext context = mockDeserializationContext();
        String key = "127";
        Object result = byteDeserializer.deserializeKey(key, context);
        assertEquals((byte) 127, result);

        key = "-128";
        assertThrows(JsonMappingException.class, () -> byteDeserializer.deserializeKey(key, context));
    }

    @Test
    public void testShortTypeDeserialization() throws IOException {
        StdKeyDeserializer shortDeserializer = StdKeyDeserializer.forType(Short.class);
        DeserializationContext context = mockDeserializationContext();
        String key = "32767";
        Object result = shortDeserializer.deserializeKey(key, context);
        assertEquals((short) 32767, result);

        key = "-32768";
        assertThrows(JsonMappingException.class, () -> shortDeserializer.deserializeKey(key, context));
    }

    @Test
    public void testCharTypeDeserialization() throws IOException {
        StdKeyDeserializer charDeserializer = StdKeyDeserializer.forType(Character.class);
        DeserializationContext context = mockDeserializationContext();
        String key = "A";
        Object result = charDeserializer.deserializeKey(key, context);
        assertEquals('A', result);

        key = "z";
        Object result2 = charDeserializer.deserializeKey(key, context);
        assertEquals('z', result2);

        key = "!";
        assertThrows(JsonMappingException.class, () -> charDeserializer.deserializeKey(key, context));
    }

    @Test
    public void testIntTypeDeserialization() throws IOException {
        StdKeyDeserializer intDeserializer = StdKeyDeserializer.forType(Integer.class);
        DeserializationContext context = mockDeserializationContext();
        String key = "100";
        Object result = intDeserializer.deserializeKey(key, context);
        assertEquals(100, result);
    }

    @Test
    public void testLongTypeDeserialization() throws IOException {
        StdKeyDeserializer longDeserializer = StdKeyDeserializer.forType(Long.class);
        DeserializationContext context = mockDeserializationContext();
        String key = "1000000";
        Object result = longDeserializer.deserializeKey(key, context);
        assertEquals(1000000L, result);
    }

    @Test
    public void testFloatTypeDeserialization() throws IOException {
        StdKeyDeserializer floatDeserializer = StdKeyDeserializer.forType(Float.class);
        DeserializationContext context = mockDeserializationContext();
        String key = "10.5";
        Object result = floatDeserializer.deserializeKey(key, context);
        assertEquals(10.5f, result);
    }

    @Test
    public void testDoubleTypeDeserialization() throws IOException {
        StdKeyDeserializer doubleDeserializer = StdKeyDeserializer.forType(Double.class);
        DeserializationContext context = mockDeserializationContext();
        String key = "10.5";
        Object result = doubleDeserializer.deserializeKey(key, context);
        assertEquals(10.5, result);
    }

    @Test
    public void testLocaleTypeDeserialization() throws IOException {
        StdKeyDeserializer localeDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class, null);
        DeserializationContext context = mockDeserializationContext();
        String key = "en_US";
        Object result = localeDeserializer.deserializeKey(key, context);
        assertEquals(Locale.US, result);

        key = "es_ES";
        Object result2 = localeDeserializer.deserializeKey(key, context);
        assertEquals(Locale.SPAIN, result2);

        key = "non_existent_locale";
        assertThrows(JsonMappingException.class, () -> localeDeserializer.deserializeKey(key, context));
    }

    @Test
    public void testDateTypeDeserialization() throws IOException {
        StdKeyDeserializer dateDeserializer = StdKeyDeserializer.forType(Date.class);
        DeserializationContext context = mockDeserializationContext();
        String key = "2022-01-01";
        Object result = dateDeserializer.deserializeKey(key, context);
        assertEquals(new Date(1641344000000L), result);
    }

    @Test
    public void testCalendarTypeDeserialization() throws IOException {
        StdKeyDeserializer calendarDeserializer = StdKeyDeserializer.forType(Calendar.class);
        DeserializationContext context = mockDeserializationContext();
        String key = "2022-01-01";
        Object result = calendarDeserializer.deserializeKey(key, context);
        Calendar calendar = (Calendar) result;
        assertEquals(2022, calendar.get(Calendar.YEAR));
        assertEquals(1, calendar.get(Calendar.MONTH) + 1);
        assertEquals(1, calendar.get(Calendar.DAY_OF_MONTH));

        key = "invalid_date";
        assertThrows(JsonMappingException.class, () -> calendarDeserializer.deserializeKey(key, context));
    }

    @Test
    public void testUuidTypeDeserialization() throws IOException {
        StdKeyDeserializer uuidDeserializer = StdKeyDeserializer.forType(UUID.class);
        DeserializationContext context = mockDeserializationContext();
        String key = "123e4567-e89b-12d3-a456-426614174000";
        Object result = uuidDeserializer.deserializeKey(key, context);
        assertEquals(new UUID("123e4567-e89b-12d3-a456-426614174000"), result);
    }

    @Test
    public void testUriTypeDeserialization() throws IOException {
        StdKeyDeserializer uriDeserializer = StdKeyDeserializer.forType(URI.class);
        DeserializationContext context = mockDeserializationContext();
        String key = "http://example.com";
        Object result = uriDeserializer.deserializeKey(key, context);
        assertEquals(new URI("http://example.com"), result);
    }

    @Test
    public void testUrlTypeDeserialization() throws IOException {
        StdKeyDeserializer urlDeserializer = StdKeyDeserializer.forType(URL.class);
        DeserializationContext context = mockDeserializationContext();
        String key = "http://example.com";
        Object result = urlDeserializer.deserializeKey(key, context);
        assertEquals(new URL("http://example.com"), result);
    }

    @Test
    public void testClassTypeDeserialization() throws IOException {
        StdKeyDeserializer classDeserializer = StdKeyDeserializer.forType(String.class);
        DeserializationContext context = mockDeserializationContext();
        String key = "java.util.Date";
        Object result = classDeserializer.deserializeKey(key, context);
        assertEquals(Date.class, result);
    }

    @Test
    public void testCurrencyTypeDeserialization() throws IOException {
        StdKeyDeserializer currencyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CURRENCY, Currency.class, null);
        DeserializationContext context = mockDeserializationContext();
        String key = "EUR";
        Object result = currencyDeserializer.deserializeKey(key, context);
        assertEquals(Currency.getInstance("EUR"), result);

        key = "invalid_currency";
        assertThrows(JsonMappingException.class, () -> currencyDeserializer.deserializeKey(key, context));
    }

    private DeserializationContext mockDeserializationContext() {
        return new TestDeserializationContext();
    }

    private static class TestDeserializationContext implements DeserializationContext {
        @Override
        public ObjectFactory getFactory() {
            return null;
        }

        @Override
        public JsonFactory getJsonFactory() {
            return null;
        }

        @Override
        public JsonParser _parser() {
            return null;
        }

        @Override
        public ObjectMapper _defaultObjectMapper() {
            return objectMapper;
        }

        @Override
        public void reportError(MessageBase msg) throws JsonMappingException {
            throw new RuntimeException(msg.toString());
        }

        @Override
        public void reportWarning(MessageBase msg) {
            // do nothing
        }

        @Override
        public void findClass(Class<?> rawClass) throws IOException, JsonMappingException {
            // do nothing
        }

        @Override
        public void findClass(String name) throws IOException, JsonMappingException {
            // do nothing
        }

        @Override
        public <T> T findValueHandler(Class<T> valueType) {
            return null;
        }

        @Override
        public void handleUnknownProperty(PropertyName propertyName, Object beanOrSource, Object propertyValue1, Object propertyValue2)
                throws JsonMappingException {
            // do nothing
        }

        @Override
        public Object instantiateValue(Class<?> valueType, AnnotatedConstructor constructor, ObjectCreator creatorContext) throws IOException {
            return null;
        }

        @Override
        public Object instantiateValue(Class<?> valueType, ObjectCreator creatorContext) throws IOException {
            return null;
        }
    }
}