package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import org.junit.Test;

public class StdKeyDeserializerTest {

    @Test
    public void testBooleanKeyDeserializer() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
        assertEquals(Boolean.TRUE, deserializer.deserializeKey("true", null));
        assertEquals(Boolean.FALSE, deserializer.deserializeKey("false", null));
        assertNull(deserializer.deserializeKey("invalid", null));
    }

    @Test
    public void testByteKeyDeserializer() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);
        assertEquals((Byte) 0, deserializer.deserializeKey("0", null));
        assertEquals((Byte) 127, deserializer.deserializeKey("127", null));
        assertEquals((Byte) 128, deserializer.deserializeKey("128", null)); // should throw an exception
        assertNull(deserializer.deserializeKey("invalid", null));
    }

    @Test
    public void testShortKeyDeserializer() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);
        assertEquals((Short) 0, deserializer.deserializeKey("0", null));
        assertEquals((Short) 32767, deserializer.deserializeKey("32767", null));
        assertEquals((Short) 32768, deserializer.deserializeKey("32768", null)); // should throw an exception
        assertNull(deserializer.deserializeKey("invalid", null));
    }

    @Test
    public void testCharKeyDeserializer() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);
        assertEquals(Character.forDigit('0', 10), deserializer.deserializeKey("0", null));
        assertEquals(Character.forDigit('A', 10), deserializer.deserializeKey("A", null));
        assertNull(deserializer.deserializeKey("invalid", null));
    }

    @Test
    public void testIntKeyDeserializer() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
        assertEquals(0, deserializer.deserializeKey("0", null));
        assertEquals(42, deserializer.deserializeKey("42", null));
        assertNull(deserializer.deserializeKey("invalid", null));
    }

    @Test
    public void testLongKeyDeserializer() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);
        assertEquals(0L, deserializer.deserializeKey("0", null));
        assertEquals(42L * 1024L * 1024L * 1024L, deserializer.deserializeKey("4294967296", null));
        assertNull(deserializer.deserializeKey("invalid", null));
    }

    @Test
    public void testFloatKeyDeserializer() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);
        assertEquals(Float.intBitsToFloat(0x40c80000), deserializer.deserializeKey("NaN", null));
        assertEquals(Float.intBitsToFloat(0x40866666), deserializer.deserializeKey("0.5f", null));
        assertNull(deserializer.deserializeKey("invalid", null));
    }

    @Test
    public void testDoubleKeyDeserializer() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);
        assertEquals(Double.longBitsToDouble(0x404a000000000000L), deserializer.deserializeKey("NaN", null));
        assertEquals(Double.longBitsToDouble(0x40090fdb54000001L), deserializer.deserializeKey("1.5", null));
        assertNull(deserializer.deserializeKey("invalid", null));
    }

    @Test
    public void testLocaleKeyDeserializer() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Locale.class);
        assertEquals(Locale.ENGLISH, deserializer.deserializeKey("en_US", null));
        assertEquals(Locale.FRENCH, deserializer.deserializeKey("fr_FR", null));
        assertNull(deserializer.deserializeKey("invalid", null));
    }

    @Test
    public void testDateKeyDeserializer() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Date.class);
        assertEquals(new Date(), deserializer.deserializeKey("2023-03-18T13:37:23.012Z", null));
        assertEquals(new Date(Long.parseLong("1611644400000")), deserializer.deserializeKey("2021-01-01", null));
        assertNull(deserializer.deserializeKey("invalid", null));
    }

    @Test
    public void testCalendarKeyDeserializer() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Calendar.class);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2023);
        calendar.set(Calendar.MONTH, Calendar.MARCH);
        calendar.set(Calendar.DAY_OF_MONTH, 18);
        assertEquals(calendar, deserializer.deserializeKey("2023-03-18", null));
        assertEquals(calendar, deserializer.deserializeKey("2023-03-18T13:37:23.012Z", null));
        assertNull(deserializer.deserializeKey("invalid", null));
    }

    @Test
    public void testUUIDKeyDeserializer() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(UUID.class);
        UUID uuid = UUID.randomUUID();
        assertEquals(uuid, deserializer.deserializeKey(uuid.toString(), null));
        assertNull(deserializer.deserializeKey("invalid", null));
    }

    @Test
    public void testURIKeyDeserializer() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);
        URI uri = new URI("https://www.example.com/path?query=test");
        assertEquals(uri, deserializer.deserializeKey(uri.toString(), null));
        assertNull(deserializer.deserializeKey("invalid", null));
    }

    @Test
    public void testURLKeyDeserializer() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);
        URL url = new URL("https://www.example.com/path?query=test");
        assertEquals(url, deserializer.deserializeKey(url.toString(), null));
        assertNull(deserializer.deserializeKey("invalid", null));
    }

    @Test
    public void testClassKeyDeserializer() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
        Class<?> clazz = String.class;
        assertEquals(clazz, deserializer.deserializeKey(clazz.getName(), null));
        assertNull(deserializer.deserializeKey("invalid", null));
    }

    @Test
    public void testByteArrayKeyDeserializer() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(byte[].class);
        byte[] byteArray = "base64encodedbytes".getBytes();
        assertEquals(byteArray, deserializer.deserializeKey(new String(deserializer.getKeyFactory().writeValueAsBytes(byteArray)), null));
        assertNull(deserializer.deserializeKey("invalid", null));
    }

    @Test
    public void testEnumKeyDeserializer() throws IOException {
        EnumResolver enumResolver = EnumResolver.construct(MyEnum.class, null);
        AnnotatedMethod factoryMethod = null;
        for (Method method : MyEnum.class.getMethods()) {
            if (method.getName().equals("valueOf")) {
                factoryMethod = method;
                break;
            }
        }

        StdKeyDeserializer deserializer = null;
        if (factoryMethod != null) {
            deserializer = new StdKeyDeserializer.StringFactoryKeyDeserializer(factoryMethod);
        } else {
            deserializer = new StdKeyDeserializer.EnumKD(enumResolver, null);
        }

        assertEquals(MyEnum.FOO, deserializer.deserializeKey("FOO", null));
        assertEquals(MyEnum.BAR, deserializer.deserializeKey("BAR", null));
        assertNull(deserializer.deserializeKey("INVALID", null));
    }

    @Test
    public void testCustomKeyDeserializer() throws IOException {
        Class<?> myClass = CustomKeyDeserializerTest.class;
        Constructor<?> constructor = myClass.getDeclaredConstructor(String.class);
        constructor.setAccessible(true);

        StdKeyDeserializer deserializer = new StdKeyDeserializer.StringCtorKeyDeserializer(constructor);

        assertEquals(new CustomKeyDeserializerTest("Hello"), deserializer.deserializeKey("Hello", null));
        assertEquals(new CustomKeyDeserializerTest("World"), deserializer.deserializeKey("World", null));
        assertNull(deserializer.deserializeKey("Invalid", null));
    }
}