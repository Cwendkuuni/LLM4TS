package com.example; // Replace this with your package name

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.NumberInput;

public class StdKeyDeserializerTest {

    

    @Test
    public void testForType() throws Exception {
        Class<?> stringClass = String.class;
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(stringClass);
        assertEquals(StdKeyDeserializer.class, serializer.getClass());
        assertEquals(String.class, serializer.getKeyClass());
        assertNull(serializer._deser);
    }

    @Test
    public void testDeserializeKey() throws Exception {
        String key = "true";
        StdKeyDeserializer booleanSerializer = StdKeyDeserializer.forType(Boolean.class);
        assertTrue(booleanSerializer.deserializeKey(key, new TestContext()).equals(Boolean.TRUE));

        key = "false";
        assertTrue(booleanSerializer.deserializeKey(key, new TestContext()).equals(Boolean.FALSE));

        key = "invalid";
        assertNull(booleanSerializer.deserializeKey(key, new TestContext()));
    }

    @Test
    public void testParse() throws Exception {
        StdKeyDeserializer booleanSerializer = StdKeyDeserializer.forType(Boolean.class);

        assertTrue(booleanSerializer._parse("true", new TestContext()).equals(Boolean.TRUE));
        assertTrue(booleanSerializer._parse("false", new TestContext()).equals(Boolean.FALSE));

        try {
            booleanSerializer._parse("invalid", new TestContext());
            fail();
        } catch (Exception e) {
            // Expected
        }
    }

    @Test
    public void testParseInt() throws Exception {
        StdKeyDeserializer intSerializer = StdKeyDeserializer.forType(Integer.class);
        int value = intSerializer._parseInt("123");
        assertEquals(123, value);

        try {
            int value2 = intSerializer._parseInt("invalid");
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            int value3 = intSerializer._parseInt("-123");
            fail();
        } catch (NumberFormatException e) {
            // Expected
        }
    }

    @Test
    public void testParseLong() throws Exception {
        StdKeyDeserializer longSerializer = StdKeyDeserializer.forType(Long.class);
        long value = longSerializer._parseLong("12345678901234567890");
        assertEquals(12345678901234567890L, value);

        try {
            long value2 = longSerializer._parseLong("invalid");
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            long value3 = longSerializer._parseLong("-12345678901234567890");
            fail();
        } catch (NumberFormatException e) {
            // Expected
        }
    }

    @Test
    public void testParseDouble() throws Exception {
        StdKeyDeserializer doubleSerializer = StdKeyDeserializer.forType(Double.class);
        double value = doubleSerializer._parseDouble("123.456");
        assertEquals(123.456, value, 0.001);

        try {
            double value2 = doubleSerializer._parseDouble("invalid");
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseDate() throws Exception {
        StdKeyDeserializer dateSerializer = StdKeyDeserializer.forType(Date.class);
        Date date = dateSerializer._parse("2022-01-01", new TestContext());
        assertNotNull(date);

        try {
            dateSerializer._parse("invalid", new TestContext());
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseCalendar() throws Exception {
        StdKeyDeserializer calendarSerializer = StdKeyDeserializer.forType(Calendar.class);
        Date date = new Date();
        Calendar calendar = calendarSerializer._parse(date.toString(), new TestContext());
        assertNotNull(calendar);

        try {
            calendarSerializer._parse("invalid", new TestContext());
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseEnum() throws Exception {
        EnumSet<Weekday> weekdays = EnumSet.allOf(Weekday.class);
        StdKeyDeserializer enumSerializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Weekday.class, null);

        for (Weekday weekday : weekdays) {
            Object result = enumSerializer._parse(weekday.name(), new TestContext());
            assertEquals(weekday, result);
        }

        try {
            enumSerializer._parse("invalid", new TestContext());
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseUUID() throws Exception {
        StdKeyDeserializer uuidSerializer = StdKeyDeserializer.forType(UUID.class);
        UUID uuid = uuidSerializer._parse("123e4567-e89b-12d3-a456-426614174000");
        assertNotNull(uuid);

        try {
            uuidSerializer._parse("invalid", new TestContext());
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseURI() throws Exception {
        StdKeyDeserializer uriSerializer = StdKeyDeserializer.forType(URI.class);
        URI uri = uriSerializer._parse("http://example.com");
        assertNotNull(uri);

        try {
            uriSerializer._parse("invalid", new TestContext());
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseURL() throws Exception {
        StdKeyDeserializer urlSerializer = StdKeyDeserializer.forType(URL.class);
        URL url = urlSerializer._parse("http://example.com");
        assertNotNull(url);

        try {
            urlSerializer._parse("invalid", new TestContext());
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseClass() throws Exception {
        StdKeyDeserializer classSerializer = StdKeyDeserializer.forType(String.class);
        Class<?> clazz = classSerializer._parse("java.lang.String", new TestContext());
        assertEquals(String.class, clazz);

        try {
            classSerializer._parse("invalid", new TestContext());
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseEnumUsingToString() throws Exception {
        EnumSet<Weekday> weekdays = EnumSet.allOf(Weekday.class);
        StdKeyDeserializer enumSerializer = StdKeyDeserializer.forType(Weekday.class);

        for (Weekday weekday : weekdays) {
            Object result = enumSerializer._parse(weekday.toString(), new TestContext());
            assertEquals(weekday, result);
        }

        try {
            enumSerializer._parse("invalid", new TestContext());
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    

    static class TestContext extends DeserializationContext {
        @Override
        public DeserializationConfig config() {
            return new DeserializationConfig(new AnnotationIntrospector(), new StandardSerializerFinder(), new BeanDescriptionFactory());
        }

        @Override
        public ObjectMapper _getFactory() {
            return new ObjectMapper();
        }
    }

    enum Weekday {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }
}