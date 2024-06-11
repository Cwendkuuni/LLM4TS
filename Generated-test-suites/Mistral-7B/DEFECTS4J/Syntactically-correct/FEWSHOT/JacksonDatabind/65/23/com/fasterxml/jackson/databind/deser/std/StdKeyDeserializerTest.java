package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;

/**
 * Test cases for {@link StdKeyDeserializer} class.
 */
@JacksonStdImpl
public class StdKeyDeserializerTest {

    @Test
    public void testStringKD() throws IOException, JsonProcessingException {
        StdKeyDeserializer stringKD = StdKeyDeserializer.StringKD.forType(String.class);
        assertEquals("test", stringKD.deserializeKey("test", null));
    }

    @Test
    public void testStringKD_Object() throws IOException, JsonProcessingException {
        StdKeyDeserializer stringKD = StdKeyDeserializer.StringKD.forType(Object.class);
        assertEquals("test", stringKD.deserializeKey("test", null));
    }

    @Test
    public void testBooleanKD() throws IOException, JsonProcessingException {
        StdKeyDeserializer booleanKD = StdKeyDeserializer.forType(Boolean.class);
        assertTrue(booleanKD.deserializeKey("true", null));
        assertFalse(booleanKD.deserializeKey("false", null));
    }

    @Test
    public void testByteKD() throws IOException, JsonProcessingException {
        StdKeyDeserializer byteKD = StdKeyDeserializer.forType(Byte.class);
        assertEquals((byte) 0, byteKD.deserializeKey("0", null));
        assertEquals((byte) 127, byteKD.deserializeKey("127", null));
        assertThrows(IllegalArgumentException.class, () -> byteKD.deserializeKey("-128", null));
    }

    @Test
    public void testShortKD() throws IOException, JsonProcessingException {
        StdKeyDeserializer shortKD = StdKeyDeserializer.forType(Short.class);
        assertEquals((short) 0, shortKD.deserializeKey("0", null));
        assertEquals((short) 32767, shortKD.deserializeKey("32767", null));
        assertThrows(IllegalArgumentException.class, () -> shortKD.deserializeKey("-32768", null));
    }

    @Test
    public void testCharKD() throws IOException, JsonProcessingException {
        StdKeyDeserializer charKD = StdKeyDeserializer.forType(Character.class);
        assertEquals('\u0000', charKD.deserializeKey("\u0000", null));
        assertEquals('\u0061', charKD.deserializeKey("a", null));
        assertThrows(IllegalArgumentException.class, () -> charKD.deserializeKey("123", null));
    }

    @Test
    public void testIntKD() throws IOException, JsonProcessingException {
        StdKeyDeserializer intKD = StdKeyDeserializer.forType(Integer.class);
        assertEquals(0, intKD.deserializeKey("0", null));
        assertEquals(123, intKD.deserializeKey("123", null));
        assertThrows(NumberFormatException.class, () -> intKD.deserializeKey("-128", null));
    }

    @Test
    public void testLongKD() throws IOException, JsonProcessingException {
        StdKeyDeserializer longKD = StdKeyDeserializer.forType(Long.class);
        assertEquals(0L, longKD.deserializeKey("0", null));
        assertEquals(1234567890123L, longKD.deserializeKey("1234567890123", null));
        assertThrows(NumberFormatException.class, () -> longKD.deserializeKey("-128", null));
    }

    @Test
    public void testFloatKD() throws IOException, JsonProcessingException {
        StdKeyDeserializer floatKD = StdKeyDeserializer.forType(Float.class);
        assertEquals(0.0f, floatKD.deserializeKey("0", null), 0.0f);
        assertEquals(123.456f, floatKD.deserializeKey("123.456", null), 123.456f);
        assertThrows(NumberFormatException.class, () -> floatKD.deserializeKey("-128.0", null));
    }

    @Test
    public void testDoubleKD() throws IOException, JsonProcessingException {
        StdKeyDeserializer doubleKD = StdKeyDeserializer.forType(Double.class);
        assertEquals(0.0, doubleKD.deserializeKey("0", null), 0.0);
        assertEquals(123.456, doubleKD.deserializeKey("123.456", null), 123.456);
        assertThrows(NumberFormatException.class, () -> doubleKD.deserializeKey("-128.0", null));
    }

    @Test
    public void testLocaleKD() throws IOException, JsonProcessingException {
        StdKeyDeserializer localeKD = StdKeyDeserializer.forType(Locale.class);
        assertEquals(Locale.ENGLISH, localeKD.deserializeKey("en_US", null));
        assertThrows(IllegalArgumentException.class, () -> localeKD.deserializeKey("en_US_invalid", null));
    }

    @Test
    public void testDateKD() throws IOException, JsonProcessingException {
        StdKeyDeserializer dateKD = StdKeyDeserializer.forType(Date.class);
        assertEquals(new Date(), dateKD.deserializeKey("", null));
        assertNull(dateKD.deserializeKey("invalid", null));
    }

    @Test
    public void testCalendarKD() throws IOException, JsonProcessingException {
        StdKeyDeserializer calendarKD = StdKeyDeserializer.forType(Calendar.class);
        GregorianCalendar gc = new GregorianCalendar();
        assertEquals(gc, calendarKD.deserializeKey("", gc.getTimeZone()));
        assertNull(calendarKD.deserializeKey("invalid", null));
    }

    @Test
    public void testUUIDKD() throws IOException, JsonProcessingException {
        StdKeyDeserializer uuidKD = StdKeyDeserializer.forType(UUID.class);
        UUID uuid = UUID.randomUUID();
        assertEquals(uuid, uuidKD.deserializeKey(uuid.toString(), null));
        assertThrows(IllegalArgumentException.class, () -> uuidKD.deserializeKey("InvalidUUID", null));
    }

    @Test
    public void testURICD() throws IOException, JsonProcessingException {
        StdKeyDeserializer uriCD = StdKeyDeserializer.forType(URI.class);
        URI uri = new URI("http://www.example.com");
        assertEquals(uri, uriCD.deserializeKey(uri.toString(), null));
        assertThrows(IllegalArgumentException.class, () -> uriCD.deserializeKey("InvalidURI", null));
    }

    @Test
    public void testURLEndPointCD() throws IOException, JsonProcessingException {
        StdKeyDeserializer urlCD = StdKeyDeserializer.forType(URL.class);
        URL url = new URL("http://www.example.com");
        assertEquals(url, urlCD.deserializeKey(url.toString(), null));
        assertThrows(MalformedURLException.class, () -> urlCD.deserializeKey("InvalidURL", null));
    }

    @Test
    public void testClassCD() throws IOException, JsonProcessingException {
        StdKeyDeserializer classCD = StdKeyDeserializer.forType(String.class);
        Class<?> clazz = String.class;
        assertEquals(clazz, classCD.deserializeKey(clazz.getName(), null));
        assertThrows(IllegalArgumentException.class, () -> classCD.deserializeKey("InvalidClass", null));
    }

    // Test delegating key deserializer

    @Test
    public void testDelegatingKD() throws IOException, JsonProcessingException {
        StdKeyDeserializer classCD = StdKeyDeserializer.forType(String.class);
        JsonDeserializer<String> stringDeserializer = new StandardDeserializer<>(String.class);
        KeyDeserializer delegatingKD = new StdKeyDeserializer.DelegatingKD(String.class, stringDeserializer);
        assertEquals("test", delegatingKD.deserializeKey("test", null));
    }

    // Test EnumKD

    @Test
    public void testEnumKD() throws IOException, JsonProcessingException {
        Enum testEnum = EnumValue.FOO;
        EnumResolver enumResolver = new EnumResolver(testEnum.getDeclaringClass());
        StdKeyDeserializer enumKD = new StdKeyDeserializer.EnumKD(enumResolver, null);
        assertEquals(testEnum, enumKD.deserializeKey("FOO", null));
        assertNull(enumKD.deserializeKey("INVALID", null));
    }

    // Test StringCtorKeyDeserializer

    @Test
    public void testStringCtorKeyDeserializer() throws IOException, JsonProcessingException {
        String key = "test";
        Constructor<String> constructor = String.class.getConstructor(String.class);
        StdKeyDeserializer stringCtorKeyDeserializer = new StdKeyDeserializer.StringCtorKeyDeserializer(constructor);
        assertEquals(key, stringCtorKeyDeserializer.deserializeKey(key, null));
    }

    // Test StringFactoryKeyDeserializer

    @Test
    public void testStringFactoryKeyDeserializer() throws IOException, JsonProcessingException {
        String key = "test";
        Method factoryMethod = String.class.getDeclaredMethod("valueOf", String.class);
        StdKeyDeserializer stringFactoryKeyDeserializer = new StdKeyDeserializer.StringFactoryKeyDeserializer(factoryMethod);
        assertEquals(key, stringFactoryKeyDeserializer.deserializeKey(key, null));
    }
}