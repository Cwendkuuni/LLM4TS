package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test class for StdKeyDeserializer.
 */
@JacksonStdImpl
public class StdKeyDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testBooleanDeserializer() throws Exception {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Boolean.class);
        Object key = serializer.deserializeKey("true", objectMapper.getDeserializationContext());
        assertEquals(Boolean.TRUE, key);

        key = serializer.deserializeKey("false", objectMapper.getDeserializationContext());
        assertEquals(Boolean.FALSE, key);

        key = serializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        assertNull(key);
    }

    @Test
    public void testByteDeserializer() throws Exception {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Byte.class);
        Object key = serializer.deserializeKey("127", objectMapper.getDeserializationContext());
        assertEquals((byte) 127, key);

        key = serializer.deserializeKey("-128", objectMapper.getDeserializationContext());
        assertEquals((byte) -128, key);

        key = serializer.deserializeKey("256", objectMapper.getDeserializationContext());
        assertNull(key);
    }

    @Test
    public void testShortDeserializer() throws Exception {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Short.class);
        Object key = serializer.deserializeKey("32767", objectMapper.getDeserializationContext());
        assertEquals((short) 32767, key);

        key = serializer.deserializeKey("-32768", objectMapper.getDeserializationContext());
        assertEquals((short) -32768, key);

        key = serializer.deserializeKey("65536", objectMapper.getDeserializationContext());
        assertNull(key);
    }

    @Test
    public void testCharDeserializer() throws Exception {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Character.class);
        Object key = serializer.deserializeKey("A", objectMapper.getDeserializationContext());
        assertEquals('A', key);

        key = serializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        assertNull(key);
    }

    @Test
    public void testIntDeserializer() throws Exception {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Integer.class);
        Object key = serializer.deserializeKey("2147483647", objectMapper.getDeserializationContext());
        assertEquals(Integer.MAX_VALUE, key);

        key = serializer.deserializeKey("-2147483648", objectMapper.getDeserializationContext());
        assertEquals(Integer.MIN_VALUE, key);

        key = serializer.deserializeKey("4294967296", objectMapper.getDeserializationContext());
        assertNull(key);
    }

    @Test
    public void testLongDeserializer() throws Exception {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Long.class);
        Object key = serializer.deserializeKey("9223372036854775807L", objectMapper.getDeserializationContext());
        assertEquals(Long.MAX_VALUE, key);

        key = serializer.deserializeKey("-9223372036854775808L", objectMapper.getDeserializationContext());
        assertEquals(Long.MIN_VALUE, key);

        key = serializer.deserializeKey("18446744073709551616L", objectMapper.getDeserializationContext());
        assertNull(key);
    }

    @Test
    public void testFloatDeserializer() throws Exception {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Float.class);
        Object key = serializer.deserializeKey("3.14159265358979323846", objectMapper.getDeserializationContext());
        assertEquals(3.14159265358979323846f, key, 0.0000001f);

        key = serializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        assertNull(key);
    }

    @Test
    public void testDoubleDeserializer() throws Exception {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Double.class);
        Object key = serializer.deserializeKey("6.28318530718", objectMapper.getDeserializationContext());
        assertEquals(6.28318530718, key, 0.00000000001);

        key = serializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        assertNull(key);
    }

    @Test
    public void testLocaleDeserializer() throws Exception {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Locale.class);
        Object key = serializer.deserializeKey("en_US", objectMapper.getDeserializationContext());
        assertEquals(Locale.US, key);

        key = serializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        assertNull(key);
    }

    @Test
    public void testDateDeserializer() throws Exception {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Date.class);
        Object key = serializer.deserializeKey("2022-12-31", objectMapper.getDeserializationContext());
        assertEquals(new Date(1671161599000L), key);

        key = serializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        assertNull(key);
    }

    @Test
    public void testCalendarDeserializer() throws Exception {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Calendar.class);
        Object key = serializer.deserializeKey("2022-12-31", objectMapper.getDeserializationContext());
        AnnotatedClass ac = objectMapper.getIntrospect().findSerializableType(GregorianCalendar.class);
        AnnotatedMethod am = ac.findMethod("getInstance", Locale.class);
        Object instance = am.invoke(null, Locale.US);
        ((GregorianCalendar) instance).setTime(new Date(1671161599000L));
        assertEquals(instance, key);

        key = serializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        assertNull(key);
    }

    @Test
    public void testUriDeserializer() throws Exception {
        KeyDeserializer serializer = StdKeyDeserializer.forType(URI.class);
        Object key = serializer.deserializeKey("http://example.com/", objectMapper.getDeserializationContext());
        assertEquals(new URI("http://example.com/"), key);

        key = serializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        assertNull(key);
    }

    @Test
    public void testUrlDeserializer() throws Exception {
        KeyDeserializer serializer = StdKeyDeserializer.forType(URL.class);
        Object key = serializer.deserializeKey("http://example.com/", objectMapper.getDeserializationContext());
        assertEquals(new URL("http://example.com/"), key);

        key = serializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        assertNull(key);
    }

    @Test
    public void testClassDeserializer() throws Exception {
        KeyDeserializer serializer = StdKeyDeserializer.forType(String.class);
        Object key = serializer.deserializeKey("java.lang.String", objectMapper.getDeserializationContext());
        assertEquals(String.class, key);

        key = serializer.deserializeKey("java.util.Date", objectMapper.getDeserializationContext());
        assertNull(key);
    }
}