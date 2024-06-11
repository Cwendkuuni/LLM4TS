package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.StdKeyDeserializer.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import org.junit.Test;

public class StdKeyDeserializerTest {

    // Test for StdKeyDeserializer constructor and forType method
    @Test
    public void testStdKeyDeserializerConstructionAndForType() throws Exception {
        StdKeyDeserializer keyDeserializerForInt = StdKeyDeserializer.forType(Integer.class);
        assertNotNull(keyDeserializerForInt);
        assertEquals(Integer.class, keyDeserializerForInt.getKeyClass());
        assertEquals(StdKeyDeserializer.TYPE_INT, keyDeserializerForInt._kind);

        StdKeyDeserializer keyDeserializerForString = StdKeyDeserializer.forType(String.class);
        assertNotNull(keyDeserializerForString);
        assertEquals(String.class, keyDeserializerForString.getKeyClass());
        assertEquals(StdKeyDeserializer.TYPE_STRING, keyDeserializerForString._kind);
    }

    // Test for deserializeKey method with different types
    @Test
    public void testDeserializeKeyForDifferentTypes() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer keyDeserializerForBoolean = StdKeyDeserializer.forType(Boolean.class);
        StdKeyDeserializer keyDeserializerForInteger = StdKeyDeserializer.forType(Integer.class);
        StdKeyDeserializer keyDeserializerForString = StdKeyDeserializer.forType(String.class);

        DeserializationContext ctxt = mapper.getDeserializationContext();

        // Test with boolean
        String booleanKey = "true";
        Object booleanResult = keyDeserializerForBoolean.deserializeKey(booleanKey, ctxt);
        assertTrue(booleanResult instanceof Boolean);
        assertEquals(Boolean.TRUE, booleanResult);

        String falseKey = "false";
        Object falseResult = keyDeserializerForBoolean.deserializeKey(falseKey, ctxt);
        assertTrue(falseResult instanceof Boolean);
        assertEquals(Boolean.FALSE, falseResult);

        // Test with integer
        String integerKey = "123";
        Object integerResult = keyDeserializerForInteger.deserializeKey(integerKey, ctxt);
        assertTrue(integerResult instanceof Number);
        assertEquals(123, ((Number) integerResult).intValue());

        // Test with string
        String stringKey = "test";
        Object stringResult = keyDeserializerForString.deserializeKey(stringKey, ctxt);
        assertTrue(stringResult instanceof String);
        assertEquals(stringKey, stringResult);
    }

    // Test for _parse method with different types
    @Test
    public void testParseMethodForDifferentTypes() throws Exception {
        StdKeyDeserializer keyDeserializerForBoolean = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        StdKeyDeserializer keyDeserializerForInteger = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        StdKeyDeserializer keyDeserializerForString = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);

        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper());

        // Test with boolean
        String booleanKey = "true";
        Object booleanResult = keyDeserializerForBoolean._parse(booleanKey, ctxt);
        assertTrue(booleanResult instanceof Boolean);
        assertEquals(Boolean.TRUE, booleanResult);

        String falseKey = "false";
        Object falseResult = keyDeserializerForBoolean._parse(falseKey, ctxt);
        assertTrue(falseResult instanceof Boolean);
        assertEquals(Boolean.FALSE, falseResult);

        // Test with integer
        String integerKey = "123";
        Object integerResult = keyDeserializerForInteger._parse(integerKey, ctxt);
        assertTrue(integerResult instanceof Number);
        assertEquals(123, ((Number) integerResult).intValue());

        // Test with string
        String stringKey = "test";
        Object stringResult = keyDeserializerForString._parse(stringKey, ctxt);
        assertTrue(stringResult instanceof String);
        assertEquals(stringKey, stringResult);
    }

    // Test for _parseInt, _parseLong, _parseDouble methods
    @Test
    public void testParseNumberMethods() throws Exception {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);

        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper());

        // Test with int
        String intKey = "123";
        Object intResult = keyDeserializer._parse(intKey, ctxt);
        assertTrue(intResult instanceof Number);
        assertEquals(123, ((Number) intResult).intValue());

        // Test with long
        String longKey = "123456789123456789";
        Object longResult = keyDeserializer._parseLong(longKey);
        assertTrue(longResult instanceof Number);
        assertEquals(123456789123456789L, ((Number) longResult).longValue());

        // Test with double
        String doubleKey = "123.45";
        Object doubleResult = keyDeserializer._parseDouble(doubleKey);
        assertTrue(doubleResult instanceof Number);
        assertEquals(123.45, ((Number) doubleResult).doubleValue(), 0.001);
    }

    // Test for deserializing Locale, Currency, Date, Calendar, UUID, URI, URL, and Class
    @Test
    public void testDeserializingOtherTypes() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer keyDeserializerForLocale = StdKeyDeserializer.forType(Locale.class);
        StdKeyDeserializer keyDeserializerForCurrency = StdKeyDeserializer.forType(Currency.class);
        StdKeyDeserializer keyDeserializerForDate = StdKeyDeserializer.forType(Date.class);
        StdKeyDeserializer keyDeserializerForCalendar = StdKeyDeserializer.forType(Calendar.class);
        StdKeyDeserializer keyDeserializerForUUID = StdKeyDeserializer.forType(UUID.class);
        StdKeyDeserializer keyDeserializerForURI = StdKeyDeserializer.forType(URI.class);
        StdKeyDeserializer keyDeserializerForURL = StdKeyDeserializer.forType(URL.class);
        StdKeyDeserializer keyDeserializerForClass = StdKeyDeserializer.forType(Class.class);

        DeserializationContext ctxt = mapper.getDeserializationContext();

        // Test with Locale
        String localeKey = "en_US";
        Object localeResult = keyDeserializerForLocale.deserializeKey(localeKey, ctxt);
        assertTrue(localeResult instanceof Locale);
        assertEquals(Locale.US, localeResult);

        // Test with Currency
        String currencyKey = "USD";
        Object currencyResult = keyDeserializerForCurrency.deserializeKey(currencyKey, ctxt);
        assertTrue(currencyResult instanceof Currency);
        assertEquals(Currency.getInstance("USD"), currencyResult);

        // Test with Date
        String dateKey = "2022-12-31";
        Object dateResult = keyDeserializerForDate.deserializeKey(dateKey, ctxt);
        assertTrue(dateResult instanceof Date);
        assertEquals(new SimpleDateFormat("yyyy-MM-dd").parse(dateKey), dateResult);

        // Test with Calendar
        String calendarKey = "2022-12-31";
        Object calendarResult = keyDeserializerForCalendar.deserializeKey(calendarKey, ctxt);
        assertTrue(calendarResult instanceof Calendar);
        assertEquals(new SimpleDateFormat("yyyy-MM-dd").parse(calendarKey), calendarResult.getTime());

        // Test with UUID
        String uuidKey = "123e4567-e89b-12d3-a456-426614174000";
        Object uuidResult = keyDeserializerForUUID.deserializeKey(uuidKey, ctxt);
        assertTrue(uuidResult instanceof UUID);
        assertEquals(UUID.fromString(uuidKey), uuidResult);

        // Test with URI
        String uriKey = "https://www.example.com/test";
        Object uriResult = keyDeserializerForURI.deserializeKey(uriKey, ctxt);
        assertTrue(uriResult instanceof URI);
        assertEquals(new URI(uriKey), uriResult);

        // Test with URL
        String urlKey = "https://www.example.com/test";
        Object urlResult = keyDeserializerForURL.deserializeKey(urlKey, ctxt);
        assertTrue(urlResult instanceof URL);
        assertEquals(new URL(urlKey), urlResult);

        // Test with Class
        String classNameKey = "java.lang.String";
        Object classNameResult = keyDeserializerForClass.deserializeKey(classNameKey, ctxt);
        assertTrue(classNameResult instanceof Class);
        assertEquals(Class.forName(classNameKey), classNameResult);
    }
}