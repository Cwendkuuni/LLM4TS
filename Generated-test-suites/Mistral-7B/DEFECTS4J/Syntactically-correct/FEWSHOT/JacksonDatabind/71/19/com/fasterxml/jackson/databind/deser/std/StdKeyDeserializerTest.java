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
import java.util.Currency;
import java.util.Date;
import java.util.EnumSet;
import java.util.Locale;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;

/**
 * Test class for StdKeyDeserializer.
 */
@JacksonStdImpl
public class StdKeyDeserializerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testForType() {
        Class<?> rawType = String.class;
        StdKeyDeserializer expected = StringKD.forType(rawType);
        StdKeyDeserializer actual = StdKeyDeserializer.forType(rawType);
        assertEquals(expected, actual);

        rawType = UUID.class;
        expected = StdKeyDeserializer.forType(rawType);
        actual = StdKeyDeserializer.forType(rawType);
        assertEquals(expected, actual);

        rawType = Integer.class;
        expected = StdKeyDeserializer.forType(rawType);
        actual = StdKeyDeserializer.forType(rawType);
        assertEquals(expected, actual);

        rawType = Boolean.class;
        expected = null;
        actual = StdKeyDeserializer.forType(rawType);
        assertEquals(expected, actual);

        rawType = Locale.class;
        expected = new StdKeyDeserializer(TYPE_LOCALE, rawType, FromStringDeserializer.findDeserializer(Locale.class));
        actual = StdKeyDeserializer.forType(rawType);
        assertEquals(expected, actual);

        rawType = Currency.class;
        expected = new StdKeyDeserializer(TYPE_CURRENCY, rawType, FromStringDeserializer.findDeserializer(Currency.class));
        actual = StdKeyDeserializer.forType(rawType);
        assertEquals(expected, actual);

        rawType = EnumSet.class;
        assertNull(StdKeyDeserializer.forType(rawType));
    }

    @Test
    public void testDeserializeKey() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);

        Object key = "123";
        Integer result = deserializer.deserializeKey(key.toString(), MAPPER.getDeserializationContext());
        assertEquals(123, result.intValue());

        key = "true";
        Boolean resultBoolean = deserializer.deserializeKey(key.toString(), MAPPER.getDeserializationContext());
        assertTrue(resultBoolean);

        key = "-123";
        Long resultLong = deserializer.deserializeKey(key.toString(), MAPPER.getDeserializationContext());
        assertEquals(-123L, resultLong);

        key = "foo";
        assertNull(deserializer.deserializeKey(key.toString(), MAPPER.getDeserializationContext()));

        key = "en_US";
        Locale resultLocale = deserializer.deserializeKey(key.toString(), MAPPER.getDeserializationContext());
        assertEquals(Locale.US, resultLocale);

        key = "EUR";
        Currency resultCurrency = deserializer.deserializeKey(key.toString(), MAPPER.getDeserializationContext());
        assertEquals(Currency.EUR, resultCurrency);

        key = "2022-01-01";
        Date resultDate = deserializer.deserializeKey(key.toString(), MAPPER.getDeserializationContext());
        assertNotNull(resultDate);

        key = "{1, 2, 3}";
        EnumSet<Integer> resultEnumSet = deserializer.deserializeKey(key.toString(), MAPPER.getDeserializationContext());
        assertEquals(EnumSet.of(1, 2, 3), resultEnumSet);

        key = "foo.Bar";
        assertNull(deserializer.deserializeKey(key.toString(), MAPPER.getDeserializationContext()));
    }

    @Test
    public void testParse() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);

        String key = "123";
        Object result = deserializer._parse(key, MAPPER.getDeserializationContext());
        assertEquals(123, result);

        key = "true";
        try {
            deserializer._parse(key, MAPPER.getDeserializationContext());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }

        key = "-123";
        Long resultLong = (Long) deserializer._parse(key, MAPPER.getDeserializationContext());
        assertEquals(-123L, resultLong);

        key = "foo";
        assertNull(deserializer._parse(key, MAPPER.getDeserializationContext()));

        key = "en_US";
        EnumResolver enumResolver = FromStringDeserializer.findDeserializer(Locale.class);
        Object resultLocale = deserializer._parse(key, MAPPER.getDeserializationContext());
        assertEquals(enumResolver.deserialize(key, MAPPER.getDeserializationContext()), resultLocale);

        key = "EUR";
        FromStringDeserializer currencyDeserializer = FromStringDeserializer.findDeserializer(Currency.class);
        Object resultCurrency = deserializer._parse(key, MAPPER.getDeserializationContext());
        assertEquals(currencyDeserializer.deserialize(key, MAPPER.getDeserializationContext()), resultCurrency);

        key = "2022-01-01";
        Date resultDate = ctxt.parseDate(key);
        assertEquals(resultDate, deserializer._parse(key, MAPPER.getDeserializationContext()));

        key = "{1, 2, 3}";
        EnumSet<Integer> resultEnumSet = EnumSet.of(1, 2, 3);
        assertEquals(resultEnumSet, deserializer._parse(key, MAPPER.getDeserializationContext()));

        key = "foo.Bar";
        assertNull(deserializer._parse(key, MAPPER.getDeserializationContext()));
    }

    @Test
    public void testParseLong() throws IOException, NumberFormatException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);

        String key = "12345678901234567890";
        Long result = deserializer._parseLong(key);
        assertEquals(12345678901234567890L, result);

        key = "12345678901234567890a";
        try {
            deserializer._parseLong(key);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // expected
        }
    }

    @Test
    public void testParseDouble() throws IOException, NumberFormatException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);

        String key = "123.456";
        Double result = deserializer._parseDouble(key);
        assertEquals(123.456, result, 0.001);

        key = "123.456e1";
        Double resultExponent = deserializer._parseDouble(key);
        assertEquals(123456.0, resultExponent, 0.001);

        key = "123.456e-1";
        Double resultExponentNegative = deserializer._parseDouble(key);
        assertEquals(0.0123456, resultExponentNegative, 0.00001);

        key = "123.456e+10";
        try {
            deserializer._parseDouble(key);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // expected
        }
    }
}