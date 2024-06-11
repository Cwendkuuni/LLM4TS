package com.example.test; // Replace the package name with the actual one

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Introspector;
import com.fasterxml.jackson.databind.JsonMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

public class StdKeyDeserializerTest {

    private DeserializationContext context;
    private Introspector introspector;
    private JsonMapper mapper;

    @Before
    public void setup() throws Exception {
        context = mock(DeserializationContext.class);
        introspector = mock(Introspector.class);
        mapper = new JsonMapper();
        mapper.setAnnotationIntrospector(introspector);
    }

    @Test
    public void testDeserializeKey_StringType() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(String.class);
        String key = "TestString";
        assertEquals(key, serializer.deserializeKey(key, context));
    }

    @Test
    public void testDeserializeKey_BooleanType() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Boolean.class);
        String trueKey = "true";
        Boolean result = (Boolean) serializer.deserializeKey(trueKey, context);
        assertEquals(true, result);

        String falseKey = "false";
        result = (Boolean) serializer.deserializeKey(falseKey, context);
        assertEquals(false, result);
    }

    @Test
    public void testDeserializeKey_ByteType() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Byte.class);
        byte expectedValue = 10;
        String key = String.valueOf((int) expectedValue);
        Byte result = (Byte) serializer.deserializeKey(key, context);
        assertEquals(expectedValue, result.byteValue());
    }

    @Test
    public void testDeserializeKey_ShortType() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Short.class);
        short expectedValue = 20;
        String key = String.valueOf((int) expectedValue);
        Short result = (Short) serializer.deserializeKey(key, context);
        assertEquals(expectedValue, result.shortValue());
    }

    @Test
    public void testDeserializeKey_CharType() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Character.class);
        char expectedValue = 'A';
        String key = String.valueOf(expectedValue);
        Character result = (Character) serializer.deserializeKey(key, context);
        assertEquals(expectedValue, result.charValue());
    }

    @Test
    public void testDeserializeKey_IntType() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Integer.class);
        int expectedValue = 30;
        String key = String.valueOf(expectedValue);
        Integer result = (Integer) serializer.deserializeKey(key, context);
        assertEquals(expectedValue, result.intValue());
    }

    @Test
    public void testDeserializeKey_LongType() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Long.class);
        long expectedValue = 40L;
        String key = String.valueOf(expectedValue);
        Long result = (Long) serializer.deserializeKey(key, context);
        assertEquals(expectedValue, result.longValue());
    }

    @Test
    public void testDeserializeKey_FloatType() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Float.class);
        float expectedValue = 5.0F;
        String key = String.valueOf(expectedValue);
        Float result = (Float) serializer.deserializeKey(key, context);
        assertEquals(expectedValue, result.floatValue(), 0.001f);
    }

    @Test
    public void testDeserializeKey_DoubleType() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Double.class);
        double expectedValue = 6.0;
        String key = String.valueOf(expectedValue);
        Double result = (Double) serializer.deserializeKey(key, context);
        assertEquals(expectedValue, result.doubleValue(), 0.001);
    }

    @Test
    public void testDeserializeKey_LocaleType() throws Exception {
        Locale locale = Locale.US;
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Locale.class);
        String key = locale.toString();
        Locale result = (Locale) serializer.deserializeKey(key, context);
        assertEquals(locale, result);
    }

    @Test
    public void testDeserializeKey_DateType() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Date.class);
        String key = "2022-12-31";
        Date result = (Date) serializer.deserializeKey(key, context);
        assertEquals(key, mapper.writerWithRootValue(true).writeValueAsString(result));
    }

    @Test
    public void testDeserializeKey_CalendarType() throws Exception {
        Calendar calendar = Calendar.getInstance();
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Calendar.class);
        String key = mapper.writerWithRootValue(true).writeValueAsString(calendar);
        Calendar result = (Calendar) serializer.deserializeKey(key, context);
        assertEquals(calendar, result);
    }

    @Test
    public void testDeserializeKey_URIType() throws Exception {
        URI uri = new URI("http://example.com");
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URI.class);
        String key = uri.toString();
        URI result = (URI) serializer.deserializeKey(key, context);
        assertEquals(uri, result);
    }

    @Test
    public void testDeserializeKey_URLType() throws Exception {
        URL url = new URL("http://example.com");
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URL.class);
        String key = url.toString();
        URL result = (URL) serializer.deserializeKey(key, context);
        assertEquals(url, result);
    }

    @Test
    public void testDeserializeKey_ClassType() throws Exception {
        Class<?> clazz = String[].class;
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(clazz);
        String key = clazz.getName();
        Class<?> result = (Class<?>) serializer.deserializeKey(key, context);
        assertEquals(clazz, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeKey_InvalidBooleanType() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Boolean.class);
        String key = "invalid";
        serializer.deserializeKey(key, context);
    }

    @Test(expected = NumberFormatException.class)
    public void testDeserializeKey_InvalidByteType() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Byte.class);
        String key = "invalidByte";
        serializer.deserializeKey(key, context);
    }

    @Test(expected = NumberFormatException.class)
    public void testDeserializeKey_InvalidShortType() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Short.class);
        String key = "invalidShort";
        serializer.deserializeKey(key, context);
    }

    @Test(expected = NumberFormatException.class)
    public void testDeserializeKey_InvalidIntType() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Integer.class);
        String key = "invalidInt";
        serializer.deserializeKey(key, context);
    }

    @Test(expected = NumberFormatException.class)
    public void testDeserializeKey_InvalidLongType() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Long.class);
        String key = "invalidLong";
        serializer.deserializeKey(key, context);
    }

    @Test(expected = JsonProcessingException.class)
    public void testDeserializeKey_InvalidDateType() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Date.class);
        String key = "invalidDate";
        serializer.deserializeKey(key, context);
    }

    @Test(expected = MalformedURLException.class)
    public void testDeserializeKey_InvalidURLType() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URL.class);
        String key = "invalidURL";
        serializer.deserializeKey(key, context);
    }

    @Test
    public void testDeserializeKey_NullType() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Boolean.class);
        assertNull(serializer.deserializeKey(null, context));
    }

    @Test
    public void testGetKeyClass() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(String.class);
        assertEquals(String.class, serializer.getKeyClass());
    }
}