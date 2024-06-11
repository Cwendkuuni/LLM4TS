package com.example.jackson.test;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class StdKeyDeserializerTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testBooleanKeyDeserialization() throws IOException {
        StdKeyDeserializer booleanKD = StdKeyDeserializer.forType(Boolean.class);

        String key = "true";
        Object result = booleanKD.deserializeKey(key, mapper.getDeserializationContext());
        Assert.assertEquals(true, result);

        key = "false";
        result = booleanKD.deserializeKey(key, mapper.getDeserializationContext());
        Assert.assertEquals(false, result);

        key = "invalid";
        try {
            booleanKD.deserializeKey(key, mapper.getDeserializationContext());
            Assert.fail();
        } catch (JsonMappingException e) {
            Assert.assertEquals("not a valid representation: not a valid Boolean value", e.getMessage());
        }
    }

    @Test
    public void testByteKeyDeserialization() throws IOException {
        StdKeyDeserializer byteKD = StdKeyDeserializer.forType(Byte.class);

        String key = "10";
        Object result = byteKD.deserializeKey(key, mapper.getDeserializationContext());
        Assert.assertEquals((byte) 10, result);

        key = "128";
        try {
            byteKD.deserializeKey(key, mapper.getDeserializationContext());
            Assert.fail();
        } catch (JsonMappingException e) {
            Assert.assertEquals("not a valid representation: overflow, value can not be represented as 8-bit value", e.getMessage());
        }

        key = "256";
        try {
            byteKD.deserializeKey(key, mapper.getDeserializationContext());
            Assert.fail();
        } catch (JsonMappingException e) {
            Assert.assertEquals("not a valid representation: overflow, value can not be represented as 8-bit value", e.getMessage());
        }
    }

    @Test
    public void testShortKeyDeserialization() throws IOException {
        StdKeyDeserializer shortKD = StdKeyDeserializer.forType(Short.class);

        String key = "100";
        Object result = shortKD.deserializeKey(key, mapper.getDeserializationContext());
        Assert.assertEquals((short) 100, result);

        key = "32767";
        Object result1 = shortKD.deserializeKey(key, mapper.getDeserializationContext());
        Assert.assertEquals((short) 32767, result1);

        key = "32768";
        try {
            shortKD.deserializeKey(key, mapper.getDeserializationContext());
            Assert.fail();
        } catch (JsonMappingException e) {
            Assert.assertEquals("not a valid representation: overflow, value can not be represented as 16-bit value", e.getMessage());
        }
    }

    @Test
    public void testCharKeyDeserialization() throws IOException {
        StdKeyDeserializer charKD = StdKeyDeserializer.forType(Character.class);

        String key = "A";
        Object result = charKD.deserializeKey(key, mapper.getDeserializationContext());
        Assert.assertEquals('A', result);

        key = "AB";
        try {
            charKD.deserializeKey(key, mapper.getDeserializationContext());
            Assert.fail();
        } catch (JsonMappingException e) {
            Assert.assertEquals("can only convert 1-character Strings", e.getMessage());
        }
    }

    @Test
    public void testIntKeyDeserialization() throws IOException {
        StdKeyDeserializer intKD = StdKeyDeserializer.forType(Integer.class);

        String key = "100";
        Object result = intKD.deserializeKey(key, mapper.getDeserializationContext());
        Assert.assertEquals(100, result);

        key = "Integer.MIN_VALUE";
        Object result1 = intKD.deserializeKey(key, mapper.getDeserializationContext());
        Assert.assertEquals(Integer.MIN_VALUE, result1);

        key = "Integer.MAX_VALUE";
        try {
            intKD.deserializeKey(key, mapper.getDeserializationContext());
            Assert.fail();
        } catch (JsonMappingException e) {
            Assert.assertEquals("not a valid representation: overflow, value can not be represented as 32-bit value", e.getMessage());
        }
    }

    @Test
    public void testLongKeyDeserialization() throws IOException {
        StdKeyDeserializer longKD = StdKeyDeserializer.forType(Long.class);

        String key = "1000L";
        Object result = longKD.deserializeKey(key, mapper.getDeserializationContext());
        Assert.assertEquals(1000L, result);

        key = "Long.MIN_VALUE";
        Object result1 = longKD.deserializeKey(key, mapper.getDeserializationContext());
        Assert.assertEquals(Long.MIN_VALUE, result1);

        key = "Long.MAX_VALUE";
        try {
            longKD.deserializeKey(key, mapper.getDeserializationContext());
            Assert.fail();
        } catch (JsonMappingException e) {
            Assert.assertEquals("not a valid representation: overflow, value can not be represented as 64-bit value", e.getMessage());
        }
    }

    @Test
    public void testLocaleKeyDeserialization() throws IOException, ParseException {
        StdKeyDeserializer localeKD = StdKeyDeserializer.forType(Locale.class);

        String key = "en_US";
        Object result = localeKD.deserializeKey(key, mapper.getDeserializationContext());
        Assert.assertEquals(Locale.US, result);

        key = "fr_FR";
        Object result1 = localeKD.deserializeKey(key, mapper.getDeserializationContext());
        Assert.assertEquals(Locale.FRENCH, result1);

        key = "InvalidLocale";
        try {
            localeKD.deserializeKey(key, mapper.getDeserializationContext());
            Assert.fail();
        } catch (JsonMappingException e) {
            Assert.assertEquals("unable to parse key as locale", e.getMessage());
        }
    }

    @Test
    public void testCurrencyKeyDeserialization() throws IOException {
        StdKeyDeserializer currencyKD = StdKeyDeserializer.forType(Currency.class);

        String key = "USD";
        Object result = currencyKD.deserializeKey(key, mapper.getDeserializationContext());
        Assert.assertEquals(Currency.getInstance("USD"), result);

        key = "EUR";
        Object result1 = currencyKD.deserializeKey(key, mapper.getDeserializationContext());
        Assert.assertEquals(Currency.getInstance("EUR"), result1);

        key = "InvalidCurrency";
        try {
            currencyKD.deserializeKey(key, mapper.getDeserializationContext());
            Assert.fail();
        } catch (JsonMappingException e) {
            Assert.assertEquals("unable to parse key as currency", e.getMessage());
        }
    }

    @Test
    public void testDateKeyDeserialization() throws IOException {
        StdKeyDeserializer dateKD = StdKeyDeserializer.forType(Date.class);

        String key = "2022-12-31";
        Object result = dateKD.deserializeKey(key, mapper.getDeserializationContext());
        Assert.assertEquals(new Date(2022, 11, 31), result);

        key = "InvalidDate";
        try {
            dateKD.deserializeKey(key, mapper.getDeserializationContext());
            Assert.fail();
        } catch (JsonMappingException e) {
            Assert.assertEquals("not a valid representation", e.getMessage());
        }
    }

    @Test
    public void testCalendarKeyDeserialization() throws IOException, ParseException {
        StdKeyDeserializer calendarKD = StdKeyDeserializer.forType(Calendar.class);

        String key = "2022-12-31";
        Object result = calendarKD.deserializeKey(key, mapper.getDeserializationContext());
        Calendar calendar = (Calendar) result;
        Assert.assertEquals(2022, calendar.get(Calendar.YEAR));
        Assert.assertEquals(11, calendar.get(Calendar.MONTH));
        Assert.assertEquals(31, calendar.get(Calendar.DAY_OF_MONTH));

        key = "InvalidDate";
        try {
            calendarKD.deserializeKey(key, mapper.getDeserializationContext());
            Assert.fail();
        } catch (JsonMappingException e) {
            Assert.assertEquals("not a valid representation", e.getMessage());
        }
    }

    @Test
    public void testUUIDKeyDeserialization() throws IOException {
        StdKeyDeserializer uuidKD = StdKeyDeserializer.forType(UUID.class);

        String key = "123e4567-e89b-12d3-a456-426614174000";
        Object result = uuidKD.deserializeKey(key, mapper.getDeserializationContext());
        Assert.assertEquals(UUID.fromString(key), result);

        key = "InvalidUUID";
        try {
            uuidKD.deserializeKey(key, mapper.getDeserializationContext());
            Assert.fail();
        } catch (JsonMappingException e) {
            Assert.assertEquals("not a valid representation: UUID must be in the format: xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx", e.getMessage());
        }
    }

    @Test
    public void testURIKeyDeserialization() throws IOException {
        StdKeyDeserializer uriKD = StdKeyDeserializer.forType(URI.class);

        String key = "http://example.com";
        Object result = uriKD.deserializeKey(key, mapper.getDeserializationContext());
        Assert.assertEquals(new URI(key), result);

        key = "InvalidURI";
        try {
            uriKD.deserializeKey(key, mapper.getDeserializationContext());
            Assert.fail();
        } catch (JsonMappingException e) {
            Assert.assertEquals("not a valid representation: URI must be a well-formed URI string", e.getMessage());
        }
    }

    @Test
    public void testURLKeyDeserialization() throws IOException {
        StdKeyDeserializer urlKD = StdKeyDeserializer.forType(URL.class);

        String key = "http://example.com";
        Object result = urlKD.deserializeKey(key, mapper.getDeserializationContext());
        Assert.assertEquals(new URL(key), result);

        key = "InvalidURL";
        try {
            urlKD.deserializeKey(key, mapper.getDeserializationContext());
            Assert.fail();
        } catch (JsonMappingException e) {
            Assert.assertEquals("not a valid representation: URL must be a well-formed URL string", e.getMessage());
        }
    }

    @Test
    public void testClassKeyDeserialization() throws IOException {
        StdKeyDeserializer classKD = StdKeyDeserializer.forType(String[].class);

        String key = "[Ljava.lang.String;";
        Object result = classKD.deserializeKey(key, mapper.getDeserializationContext());
        Assert.assertArrayEquals(new String[]{}, (String[]) result);

        key = "[Ljava.lang.String;@12345678";
        Object result1 = classKD.deserializeKey(key, mapper.getDeserializationContext());
        Assert.assertArrayEquals(new String[]{"12345678"}, (String[]) result1);

        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        key = "[]";
        Object result2 = classKD.deserializeKey(key, mapper.getDeserializationContext());
        Assert.assertArrayEquals(new String[]{}, (String[]) result2);

        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, true);
        try {
            classKD.deserializeKey(key, mapper.getDeserializationContext());
            Assert.fail();
        } catch (JsonMappingException e) {
            Assert.assertEquals("not a valid representation: Empty Array", e.getMessage());
        }
    }
}