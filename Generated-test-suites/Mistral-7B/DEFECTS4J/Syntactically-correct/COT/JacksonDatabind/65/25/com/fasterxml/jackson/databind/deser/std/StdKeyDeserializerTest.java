package com.example.test; // replace with your package name

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.EnumSet;
import java.util.Locale;

public class StdKeyDeserializerTest {

    private DeserializationContext context;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        context = mock(DeserializationContext.class);
        objectMapper = new JsonMapper();
    }

    @Test
    public void testBooleanDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Boolean.class);
        Object key = serializer.deserializeKey("true", context);
        assertEquals(true, key);

        key = serializer.deserializeKey("false", context);
        assertEquals(false, key);
    }

    @Test
    public void testByteDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Byte.class);
        Object key = serializer.deserializeKey("127", context);
        assertEquals((byte) 127, key);

        key = serializer.deserializeKey("-128", context);
        assertEquals((byte) -128, key);

        key = serializer.deserializeKey("256", context); // out of range
        assertNull(key);
    }

    @Test
    public void testShortDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Short.class);
        Object key = serializer.deserializeKey("32767", context);
        assertEquals((short) 32767, key);

        key = serializer.deserializeKey("-32768", context);
        assertEquals((short) -32768, key);

        key = serializer.deserializeKey("65536", context); // out of range
        assertNull(key);
    }

    @Test
    public void testCharDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Character.class);
        Object key = serializer.deserializeKey("A", context);
        assertEquals('A', key);

        key = serializer.deserializeKey("\u0061", context); // Unicode 'a'
        assertEquals('a', key);

        key = serializer.deserializeKey("", context); // empty string
        assertNull(key);
    }

    @Test
    public void testIntDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Integer.class);
        Object key = serializer.deserializeKey("2147483647", context);
        assertEquals(2147483647, key);

        key = serializer.deserializeKey("-2147483648", context);
        assertEquals(-2147483648, key);

        key = serializer.deserializeKey("4294967297", context); // out of range
        assertNull(key);
    }

    @Test
    public void testLongDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Long.class);
        Object key = serializer.deserializeKey("9223372036854775807", context);
        assertEquals(9223372036854775807L, key);

        key = serializer.deserializeKey("-9223372036854775808", context);
        assertEquals(-9223372036854775808L, key);

        key = serializer.deserializeKey("18446744073709551616", context); // out of range
        assertNull(key);
    }

    @Test
    public void testFloatDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Float.class);
        Object key = serializer.deserializeKey("3.1415926535897932385", context);
        assertEquals(3.1415926535897932385f, key, 0.0001f);
    }

    @Test
    public void testDoubleDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Double.class);
        Object key = serializer.deserializeKey("6.28318530718041", context);
        assertEquals(6.28318530718041, key, 0.00001);
    }

    @Test
    public void testLocaleDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Locale.class);

        Object key = serializer.deserializeKey("en_US", context);
        assertEquals(Locale.US, key);

        key = serializer.deserializeKey("es_ES", context);
        assertEquals(Locale.SPAIN, key);

        key = serializer.deserializeKey("fr_FR", context);
        assertEquals(Locale.FRANCE, key);

        key = serializer.deserializeKey("de_DE@u_GER_phonebk", context);
        assertEquals(new Locale("de", "DE", ""), key);
    }

    @Test
    public void testDateDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Date.class);
        Object key = serializer.deserializeKey("2022-12-31", context);
        assertEquals(new Date(Long.parseLong("2022-12-31T00:00:00.000Z")), key);
    }

    @Test
    public void testCalendarDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Calendar.class);

        Object key = serializer.deserializeKey("2022-12-31", context);
        Calendar calendar = (Calendar) key;
        assertEquals(2022, calendar.get(Calendar.YEAR));
        assertEquals(11, calendar.get(Calendar.MONTH));
        assertEquals(30, calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Test
    public void testURIDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URI.class);
        Object key = serializer.deserializeKey("https://www.example.com", context);
        assertEquals(new URI("https://www.example.com"), key);
    }

    @Test
    public void testURLDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URL.class);
        Object key = serializer.deserializeKey("https://www.example.com", context);
        assertEquals(new URL("https://www.example.com"), key);
    }

    @Test
    public void testClassDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(String[].class);
        Object key = serializer.deserializeKey("[Ljava.lang.String;", context);
        assertEquals(new String[]{}, key);

        serializer = StdKeyDeserializer.forType(String[].class);
        key = serializer.deserializeKey("[Ljava.lang.String;[Ljava.lang.String;", context);
        assertEquals(new String[][]{new String[]{}}, key);
    }

    @Test
    public void testNullDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Void.class);
        Object key = serializer.deserializeKey("null", context);
        assertNull(key);
    }

    @Test
    public void testEnumDeserializer() throws IOException, JsonProcessingException {
        EnumSet<MyEnum> expected = EnumSet.allOf(MyEnum.class);
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(MyEnum.class);
        Object key = serializer.deserializeKey("ALL", context);
        assertEquals(expected, key);

        assertThrows(JsonProcessingException.class, () -> {
            serializer.deserializeKey("INVALID", context);
        });

        context = mock(DeserializationContext.class);
        when(context.isEnabled(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL)).thenReturn(true);
        Object nullKey = serializer.deserializeKey("null", context);
        assertEquals(null, nullKey);
    }

    @JsonDeserialize(using = StdKeyDeserializer.StringFactoryKeyDeserializer.class)
    enum MyEnum {
        ONE,
        TWO,
        THREE,
        ALL
    }
}