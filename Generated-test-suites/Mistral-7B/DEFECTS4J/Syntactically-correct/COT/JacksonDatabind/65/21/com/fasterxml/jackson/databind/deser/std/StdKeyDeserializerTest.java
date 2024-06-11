package com.example; // Replace with your test package name

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for StdKeyDeserializer class
 */
public class StdKeyDeserializerTest {

    @Test
    public void testForType() {
        Class<?> stringClass = String.class;
        KeyDeserializer sString = StdKeyDeserializer.forType(stringClass);
        assertEquals(String.class, sString.getKeyClass());

        Class<?> integerClass = Integer.class;
        KeyDeserializer intKeyDeserializer = StdKeyDeserializer.forType(integerClass);
        assertEquals(Integer.class, intKeyDeserializer.getKeyClass());

        Class<?> booleanClass = Boolean.class;
        KeyDeserializer booleanKeyDeserializer = StdKeyDeserializer.forType(booleanClass);
        assertEquals(Boolean.class, booleanKeyDeserializer.getKeyClass());

        Class<?> dateClass = Date.class;
        KeyDeserializer dateKeyDeserializer = StdKeyDeserializer.forType(dateClass);
        assertEquals(Date.class, dateKeyDeserializer.getKeyClass());

        Class<?> localeClass = Locale.class;
        KeyDeserializer localeKeyDeserializer = StdKeyDeserializer.forType(localeClass);
        assertEquals(Locale.class, localeKeyDeserializer.getKeyClass());

        Class<?> urlClass = URL.class;
        KeyDeserializer urlKeyDeserializer = StdKeyDeserializer.forType(urlClass);
        assertEquals(URL.class, urlKeyDeserializer.getKeyClass());

        Class<?> uriClass = URI.class;
        KeyDeserializer uriKeyDeserializer = StdKeyDeserializer.forType(uriClass);
        assertEquals(URI.class, uriKeyDeserializer.getKeyClass());

        Class<?> calendarClass = Calendar.class;
        KeyDeserializer calendarKeyDeserializer = StdKeyDeserializer.forType(calendarClass);
        assertEquals(Calendar.class, calendarKeyDeserializer.getKeyClass());

        Class<?> uuidClass = UUID.class;
        KeyDeserializer uuidKeyDeserializer = StdKeyDeserializer.forType(uuidClass);
        assertEquals(UUID.class, uuidKeyDeserializer.getKeyClass());

        Class<?> classClass = Class.class;
        KeyDeserializer classKeyDeserializer = StdKeyDeserializer.forType(classClass);
        assertEquals(Class.class, classKeyDeserializer.getKeyClass());

        Class<?> shortClass = Short.class;
        KeyDeserializer shortKeyDeserializer = StdKeyDeserializer.forType(shortClass);
        assertEquals(Short.class, shortKeyDeserializer.getKeyClass());

        Class<?> longClass = Long.class;
        KeyDeserializer longKeyDeserializer = StdKeyDeserializer.forType(longClass);
        assertEquals(Long.class, longKeyDeserializer.getKeyClass());

        Class<?> floatClass = Float.class;
        KeyDeserializer floatKeyDeserializer = StdKeyDeserializer.forType(floatClass);
        assertEquals(Float.class, floatKeyDeserializer.getKeyClass());

        Class<?> doubleClass = Double.class;
        KeyDeserializer doubleKeyDeserializer = StdKeyDeserializer.forType(doubleClass);
        assertEquals(Double.class, doubleKeyDeserializer.getKeyClass());

        Class<?> charClass = Character.class;
        KeyDeserializer charKeyDeserializer = StdKeyDeserializer.forType(charClass);
        assertEquals(Character.class, charKeyDeserializer.getKeyClass());

        Class<?> byteClass = Byte.class;
        KeyDeserializer byteKeyDeserializer = StdKeyDeserializer.forType(byteClass);
        assertEquals(Byte.class, byteKeyDeserializer.getKeyClass());

        Class<?> enumClass = MyEnum.class; // Replace with valid Enum class
        KeyDeserializer enumKeyDeserializer = StdKeyDeserializer.forType(enumClass);
        assertEquals(MyEnum.class, enumKeyDeserializer.getKeyClass());

        Class<?> customClass = MyCustomClass.class; // Replace with valid Class
        KeyDeserializer customKeyDeserializer = StdKeyDeserializer.forType(customClass);
        assertEquals(MyCustomClass.class, customKeyDeserializer.getKeyClass());
    }

    @Test
    public void testDeserializeKey() throws Exception {
        Class<?> stringClass = String.class;
        StdKeyDeserializer stringKeyDeserializer = (StdKeyDeserializer) StdKeyDeserializer.forType(stringClass);

        DeserializationContext context = null; // Replace with valid DeserializationContext
        String key = "true";
        Object result = stringKeyDeserializer.deserializeKey(key, context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        key = "false";
        result = stringKeyDeserializer.deserializeKey(key, context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        key = "java.util.Date";
        result = stringKeyDeserializer.deserializeKey(key, context);
        assertNotNull(result);
        assertInstanceOf(java.util.Date.class, result);

        key = "java.util.Locale";
        FromStringDeserializer localeDeserializer = FromStringDeserializer.findDeserializer(Locale.class);
        StdKeyDeserializer localeKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class, localeDeserializer);
        result = localeKeyDeserializer.deserializeKey(key, context);
        assertNotNull(result);
        assertInstanceOf(Locale.class, result);

        key = "java.net.URI";
        FromStringDeserializer uriDeserializer = FromStringDeserializer.findDeserializer(URI.class);
        StdKeyDeserializer uriKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, URI.class, uriDeserializer);
        result = uriKeyDeserializer.deserializeKey(key, context);
        assertNotNull(result);
        assertInstanceOf(URI.class, result);

        key = "java.net.URL";
        FromStringDeserializer urlDeserializer = FromStringDeserializer.findDeserializer(URL.class);
        StdKeyDeserializer urlKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class, urlDeserializer);
        result = urlKeyDeserializer.deserializeKey(key, context);
        assertNotNull(result);
        assertInstanceOf(URL.class, result);

        key = "java.lang.Class";
        result = stringKeyDeserializer.deserializeKey(key, context);
        assertNotNull(result);
        assertInstanceOf(Class.class, result);

        key = "java.util.UUID";
        result = stringKeyDeserializer.deserializeKey(key, context);
        assertNotNull(result);
        assertInstanceOf(UUID.class, result);

        key = "java.util.Date:Wed Jan 27 06:24:35 EST 2022";
        result = StdKeyDeserializer.forType(Date.class).deserializeKey(key, context);
        assertNotNull(result);
        assertInstanceOf(java.util.Date.class, result);

        key = "java.util.Calendar:Wed Jan 27 06:24:35 EST 2022";
        result = StdKeyDeserializer.forType(Calendar.class).deserializeKey(key, context);
        assertNotNull(result);
        assertInstanceOf(java.util.Calendar.class, result);

        key = "java.util.Locale:en_US";
        result = StdKeyDeserializer.forType(Locale.class).deserializeKey(key, context);
        assertNotNull(result);
        assertEquals("en_US", result.toString());

        key = "java.net.URI:http://www.example.com/test";
        result = StdKeyDeserializer.forType(URI.class).deserializeKey(key, context);
        assertNotNull(result);
        assertEquals("http://www.example.com/test", result.toURI().toString());

        key = "java.net.URL:http://www.example.com/test";
        result = StdKeyDeserializer.forType(URL.class).deserializeKey(key, context);
        assertNotNull(result);
        assertEquals("http://www.example.com/test", result.toString());

        key = "java.lang.Class:com.example.MyClass";
        result = StdKeyDeserializer.forType(Class.class).deserializeKey(key, context);
        assertNotNull(result);
        assertEquals("com.example.MyClass", result.getName());

        key = "java.util.UUID:123e4567-78fe-49a6-a714-56789abcdef";
        result = StdKeyDeserializer.forType(UUID.class).deserializeKey(key, context);
        assertNotNull(result);
        assertEquals("123e4567-78fe-49a6-a714-56789abcdef", result.toString());

        // Test deserialization of enum keys
        Class<?> enumClass = MyEnum.class; // Replace with valid Enum class
        StdKeyDeserializer enumKeyDeserializer = (StdKeyDeserializer) StdKeyDeserializer.forType(enumClass);

        key = "MyEnum.ONE";
        result = enumKeyDeserializer.deserializeKey(key, context);
        assertNotNull(result);
        assertEquals(MyEnum.ONE, result);

        key = "MyEnum.TWO";
        result = enumKeyDeserializer.deserializeKey(key, context);
        assertNotNull(result);
        assertEquals(MyEnum.TWO, result);

        // Test deserialization of custom classes
        Class<?> customClass = MyCustomClass.class; // Replace with valid Class
        StdKeyDeserializer customKeyDeserializer = (StdKeyDeserializer) StdKeyDeserializer.forType(customClass);

        key = "MyCustomClass:customValue";
        result = customKeyDeserializer.deserializeKey(key, context);
        assertNotNull(result);

        // Test invalid key deserialization
        key = "invalidKey:123";
        StdKeyDeserializer invalidKeyDeserializer = (StdKeyDeserializer) StdKeyDeserializer.forType(Integer.class);
        try {
            result = invalidKeyDeserializer.deserializeKey(key, context);
            fail();
        } catch (JsonMappingException expected) {
            // Pass
        }
    }

    @Test
    public void testParse() throws Exception {
        Class<?> booleanClass = Boolean.class;
        StdKeyDeserializer booleanKeyDeserializer = (StdKeyDeserializer) StdKeyDeserializer.forType(booleanClass);

        String key = "true";
        Object result = booleanKeyDeserializer._parse(key, null);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        key = "false";
        result = booleanKeyDeserializer._parse(key, null);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        // Test for integer types
        Class<?> intClass = Integer.class;
        StdKeyDeserializer intKeyDeserializer = (StdKeyDeserializer) StdKeyDeserializer.forType(intClass);

        key = "123";
        result = intKeyDeserializer._parse(key, null);
        assertEquals(123, result);

        key = "-456";
        result = intKeyDeserializer._parse(key, null);
        assertEquals(-456, result);

        // Test for long types
        Class<?> longClass = Long.class;
        StdKeyDeserializer longKeyDeserializer = (StdKeyDeserializer) StdKeyDeserializer.forType(longClass);

        key = "12345678901234567890";
        result = longKeyDeserializer._parse(key, null);
        assertEquals(12345678901234567890L, result);

        // Test for floating-point types
        Class<?> floatClass = Float.class;
        StdKeyDeserializer floatKeyDeserializer = (StdKeyDeserializer) StdKeyDeserializer.forType(floatClass);

        key = "123.45";
        result = floatKeyDeserializer._parse(key, null);
        assertEquals(123.45f, result, 0.001f); // Allow small error due to floating-point precision

        key = "-123.45";
        result = floatKeyDeserializer._parse(key, null);
        assertEquals(-123.45f, result, 0.001f);

        // Test for double types
        Class<?> doubleClass = Double.class;
        StdKeyDeserializer doubleKeyDeserializer = (StdKeyDeserializer) StdKeyDeserializer.forType(doubleClass);

        key = "123.456789";
        result = doubleKeyDeserializer._parse(key, null);
        assertEquals(123.456789, result, 0.000001); // Allow small error due to floating-point precision

        key = "-123.456789";
        result = doubleKeyDeserializer._parse(key, null);
        assertEquals(-123.456789, result, 0.000001);

        // Test for String types
        Class<?> stringClass = String.class;
        StdKeyDeserializer stringKeyDeserializer = (StdKeyDeserializer) StdKeyDeserializer.forType(stringClass);

        key = "testString";
        result = stringKeyDeserializer._parse(key, null);
        assertEquals("testString", result);

        // Test for other primitive types
        Class<?> charClass = Character.class;
        StdKeyDeserializer charKeyDeserializer = (StdKeyDeserializer) StdKeyDeserializer.forType(charClass);

        key = "A";
        result = charKeyDeserializer._parse(key, null);
        assertEquals('A', result);

        Class<?> byteClass = Byte.class;
        StdKeyDeserializer byteKeyDeserializer = (StdKeyDeserializer) StdKeyDeserializer.forType(byteClass);

        key = "127";
        result = byteKeyDeserializer._parse(key, null);
        assertEquals((byte) 127, result);

        // Test for Enum types
        Class<?> enumClass = MyEnum.class; // Replace with valid Enum class
        StdKeyDeserializer enumKeyDeserializer = (StdKeyDeserializer) StdKeyDeserializer.forType(enumClass);

        key = "MyEnum.ONE";
        result = enumKeyDeserializer._parse(key, null);
        assertEquals(MyEnum.ONE, result);

        // Test for custom classes
        Class<?> customClass = MyCustomClass.class; // Replace with valid Class
        StdKeyDeserializer customKeyDeserializer = (StdKeyDeserializer) StdKeyDeserializer.forType(customClass);

        key = "MyCustomClass:customValue";
        result = customKeyDeserializer._parse(key, null);
        assertNotNull(result);
    }

    @Test
    public void testGetStringKeyDeserializer() {
        Class<?> stringClass = String.class;
        KeyDeserializer stringKeyDeserializer = StdKeyDeserializer.forType(stringClass);

        String key = "testString";
        Object result = stringKeyDeserializer.deserializeKey(key, null);
        assertEquals(key, result);
    }
}