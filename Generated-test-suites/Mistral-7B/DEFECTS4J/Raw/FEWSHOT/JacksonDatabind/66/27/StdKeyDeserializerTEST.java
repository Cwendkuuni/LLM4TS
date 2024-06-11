package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.StdKeyDeserializer.StringKD;
import com.fasterxml.jackson.databind.util.StdKeyDeserializer.DelegatingKD;
import com.fasterxml.jackson.databind.util.StdKeyDeserializer.EnumKD;
import com.fasterxml.jackson.databind.util.StdKeyDeserializer.StringCtorKeyDeserializer;
import com.fasterxml.jackson.databind.util.StdKeyDeserializer.StringFactoryKeyDeserializer;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.EnumSet;

/**
 * Test cases for StdKeyDeserializer class.
 */
@JacksonStdImpl
public class StdKeyDeserializerTest {

    @Test
    public void testStringKeyDeserializer() throws IOException, JsonProcessingException {
        StringKD stringKD = StdKeyDeserializer.StringKD.forType(String.class);
        assertEquals("test", stringKD.deserializeKey("test", null));
        assertNull(stringKD.deserializeKey(null, null));
    }

    @Test
    public void testObjectKeyDeserializer() throws IOException, JsonProcessingException {
        StringKD objectKD = StdKeyDeserializer.StringKD.forType(Object.class);
        assertEquals("test", objectKD.deserializeKey("test", null));
        assertNull(objectKD.deserializeKey(null, null));
    }

    @Test
    public void testBooleanDeserializer() throws IOException, JsonProcessingException {
        Class<?> booleanClass = Boolean.TYPE;
        StdKeyDeserializer booleanDeserializer = StdKeyDeserializer.forType(booleanClass);
        assertTrue(booleanDeserializer.deserializeKey("true", null));
        assertFalse(booleanDeserializer.deserializeKey("false", null));
        assertNull(booleanDeserializer.deserializeKey(null, null));
    }

    @Test
    public void testByteDeserializer() throws IOException, JsonProcessingException {
        Class<?> byteClass = Byte.TYPE;
        StdKeyDeserializer byteDeserializer = StdKeyDeserializer.forType(byteClass);
        assertEquals((byte) 42, byteDeserializer.deserializeKey("42", null));
        assertNull(byteDeserializer.deserializeKey(null, null));
    }

    @Test
    public void testShortDeserializer() throws IOException, JsonProcessingException {
        Class<?> shortClass = Short.TYPE;
        StdKeyDeserializer shortDeserializer = StdKeyDeserializer.forType(shortClass);
        assertEquals((short) 42, shortDeserializer.deserializeKey("42", null));
        assertNull(shortDeserializer.deserializeKey(null, null));
    }

    @Test
    public void testCharDeserializer() throws IOException, JsonProcessingException {
        Class<?> charClass = Character.TYPE;
        StdKeyDeserializer charDeserializer = StdKeyDeserializer.forType(charClass);
        assertEquals('A', charDeserializer.deserializeKey("A", null));
        assertNull(charDeserializer.deserializeKey(null, null));
    }

    @Test
    public void testIntDeserializer() throws IOException, JsonProcessingException {
        Class<?> intClass = Integer.TYPE;
        StdKeyDeserializer intDeserializer = StdKeyDeserializer.forType(intClass);
        assertEquals(42, intDeserializer.deserializeKey("42", null));
        assertNull(intDeserializer.deserializeKey(null, null));
    }

    @Test
    public void testLongDeserializer() throws IOException, JsonProcessingException {
        Class<?> longClass = Long.TYPE;
        StdKeyDeserializer longDeserializer = StdKeyDeserializer.forType(longClass);
        assertEquals(42L, longDeserializer.deserializeKey("42", null));
        assertNull(longDeserializer.deserializeKey(null, null));
    }

    @Test
    public void testFloatDeserializer() throws IOException, JsonProcessingException {
        Class<?> floatClass = Float.TYPE;
        StdKeyDeserializer floatDeserializer = StdKeyDeserializer.forType(floatClass);
        assertEquals(3.14f, floatDeserializer.deserializeKey("3.14", null), 0.01f);
        assertNull(floatDeserializer.deserializeKey(null, null));
    }

    @Test
    public void testDoubleDeserializer() throws IOException, JsonProcessingException {
        Class<?> doubleClass = Double.TYPE;
        StdKeyDeserializer doubleDeserializer = StdKeyDeserializer.forType(doubleClass);
        assertEquals(3.14, doubleDeserializer.deserializeKey("3.14", null), 0.01);
        assertNull(doubleDeserializer.deserializeKey(null, null));
    }

    @Test
    public void testLocaleDeserializer() throws IOException, JsonProcessingException {
        Class<?> localeClass = Locale.class;
        StdKeyDeserializer localeDeserializer = StdKeyDeserializer.forType(localeClass);
        Locale expected = new Locale("en", "US");
        assertEquals(expected, localeDeserializer.deserializeKey("en_US", null));
        assertNull(localeDeserializer.deserializeKey(null, null));
    }

    @Test
    public void testDateDeserializer() throws IOException, JsonProcessingException {
        Class<?> dateClass = Date.class;
        StdKeyDeserializer dateDeserializer = StdKeyDeserializer.forType(dateClass);
        String key = "2023-03-31";
        Date expected = new Date(1679142400000L);
        assertEquals(expected, dateDeserializer.deserializeKey(key, null));
        assertNull(dateDeserializer.deserializeKey(null, null));
    }

    @Test
    public void testCalendarDeserializer() throws IOException, JsonProcessingException {
        Class<?> calendarClass = Calendar.class;
        StdKeyDeserializer calendarDeserializer = StdKeyDeserializer.forType(calendarClass);
        String key = "2023-03-31";
        java.util.Calendar expected = java.util.GregorianCalendar.getInstance();
        expected.setTime(new Date(1679142400000L));
        assertEquals(expected, calendarDeserializer.deserializeKey(key, null));
        assertNull(calendarDeserializer.deserializeKey(null, null));
    }

    @Test
    public void testUuidDeserializer() throws IOException, JsonProcessingException {
        Class<?> uuidClass = UUID.class;
        StdKeyDeserializer uuidDeserializer = StdKeyDeserializer.forType(uuidClass);
        String key = "123e4567-e89b-12d3-a456-426614174000";
        UUID expected = new UUID(1108885205504538123L, 1231323551235L);
        assertEquals(expected, uuidDeserializer.deserializeKey(key, null));
        assertNull(uuidDeserializer.deserializeKey(null, null));
    }

    @Test
    public void testUriDeserializer() throws IOException, JsonProcessingException {
        Class<?> uriClass = URI.class;
        StdKeyDeserializer uriDeserializer = StdKeyDeserializer.forType(uriClass);
        String key = "https://www.example.com/path?query=value";
        URI expected = new URI("https", "www.example.com", "/path", null, null, null, new String[]{"query=value"});
        assertEquals(expected, uriDeserializer.deserializeKey(key, null));
        assertNull(uriDeserializer.deserializeKey(null, null));
    }

    @Test
    public void testUrlDeserializer() throws IOException, JsonProcessingException {
        Class<?> urlClass = URL.class;
        StdKeyDeserializer urlDeserializer = StdKeyDeserializer.forType(urlClass);
        String key = "https://www.example.com/path?query=value";
        URL expected = new URL("https", "www.example.com", "/path", null, null, -1, null);
        assertEquals(expected, urlDeserializer.deserializeKey(key, null));
        assertNull(urlDeserializer.deserializeKey(null, null));
    }

    @Test
    public void testClassDeserializer() throws IOException, JsonProcessingException {
        Class<?> stringClass = String.class;
        StdKeyDeserializer classDeserializer = StdKeyDeserializer.forType(stringClass);
        assertEquals(String.class, classDeserializer.deserializeKey("java.lang.String", null));
        assertNull(classDeserializer.deserializeKey(null, null));
    }

    @Test
    public void testEnumDeserializer() throws IOException, JsonProcessingException {
        EnumSet<java.time.DayOfWeek> daysOfWeek = EnumSet.allOf(java.time.DayOfWeek.class);
        Class<?> enumClass = daysOfWeek.getClass().getComponentType();
        EnumResolver enumResolver = EnumResolver.constructFromAnnotations(enumClass);
        StdKeyDeserializer enumDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_ENUM, enumClass, enumResolver);

        // test deserialization by name
        assertEquals(java.time.DayOfWeek.MONDAY, enumDeserializer.deserializeKey("MONDAY", null));
        assertEquals(java.time.DayOfWeek.FRIDAY, enumDeserializer.deserializeKey("FRIDAY", null));

        // test deserialization by toString
        if (enumClass.isEnum()) {
            Object enumResolverByName = enumResolver.findEnumByName(java.time.DayOfWeek.MONDAY.name());
            StdKeyDeserializer enumDeserializerToString = new StdKeyDeserializer(StdKeyDeserializer.TYPE_ENUM, enumClass, (EnumResolver) enumResolverByName);
            assertEquals(java.time.DayOfWeek.MONDAY, enumDeserializerToString.deserializeKey("MONDAY", null));
            assertEquals(java.time.DayOfWeek.FRIDAY, enumDeserializerToString.deserializeKey("FRIDAY", null));
        }

        assertNull(enumDeserializer.deserializeKey(null, null));
    }

    @Test
    public void testDelegatingKeyDeserializer() throws IOException, JsonProcessingException {
        Class<?> stringClass = String.class;
        JsonDeserializer<String> deserializer = new StandardDeserializer<>();
        StdKeyDeserializer delegatingKeyDeserializer = new StdKeyDeserializer.DelegatingKD(stringClass, deserializer);
        assertEquals("test", delegatingKeyDeserializer.deserializeKey("test", null));
        assertNull(delegatingKeyDeserializer.deserializeKey(null, null));
    }
}