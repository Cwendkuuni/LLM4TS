package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;

/**
 * Unit tests for {@link StdKeyDeserializer} class.
 */
@JacksonStdImpl
public class StdKeyDeserializerTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @After
    public void teardown() throws Exception {
        mapper = null;
    }

    @Test
    public void testForTypeBoolean() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
        Assert.assertEquals(Boolean.TRUE, deserializer.deserializeKey("true", mapper.getDeserializationContext()));
        Assert.assertEquals(Boolean.FALSE, deserializer.deserializeKey("false", mapper.getDeserializationContext()));
    }

    @Test
    public void testForTypeByte() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);
        Assert.assertEquals((byte) 1, deserializer.deserializeKey("1", mapper.getDeserializationContext()));
        Assert.assertEquals((byte) 127, deserializer.deserializeKey("127", mapper.getDeserializationContext()));
        Assert.assertEquals((byte) 128, deserializer.deserializeKey("128", mapper.getDeserializationContext()));
    }

    @Test
    public void testForTypeShort() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);
        Assert.assertEquals((short) 1, deserializer.deserializeKey("1", mapper.getDeserializationContext()));
        Assert.assertEquals((short) Short.MIN_VALUE, deserializer.deserializeKey("-32768", mapper.getDeserializationContext()));
        Assert.assertEquals((short) Short.MAX_VALUE, deserializer.deserializeKey("32767", mapper.getDeserializationContext()));
    }

    @Test
    public void testForTypeChar() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);
        Assert.assertEquals((char) 'a', deserializer.deserializeKey("a", mapper.getDeserializationContext()));
        Assert.assertEquals((char) '\u0001', deserializer.deserializeKey("\u0001", mapper.getDeserializationContext()));
    }

    @Test
    public void testForTypeInt() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
        Assert.assertEquals(1, deserializer.deserializeKey("1", mapper.getDeserializationContext()));
        Assert.assertEquals(-32768, deserializer.deserializeKey("-32768", mapper.getDeserializationContext()));
        Assert.assertEquals(Integer.MAX_VALUE, deserializer.deserializeKey("2147483647", mapper.getDeserializationContext()));
    }

    @Test
    public void testForTypeLong() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);
        Assert.assertEquals(1L, deserializer.deserializeKey("1", mapper.getDeserializationContext()));
        Assert.assertEquals(-9223372036854775808L, deserializer.deserializeKey("-9223372036854775808", mapper.getDeserializationContext()));
        Assert.assertEquals(Long.MAX_VALUE, deserializer.deserializeKey("9223372036854775807", mapper.getDeserializationContext()));
    }

    @Test
    public void testForTypeFloat() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);
        Assert.assertEquals(1.0f, deserializer.deserializeKey("1.0", mapper.getDeserializationContext()), 0.001f);
        Assert.assertEquals(Float.NaN, deserializer.deserializeKey("NaN", mapper.getDeserializationContext()));
    }

    @Test
    public void testForTypeDouble() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);
        Assert.assertEquals(1.0, deserializer.deserializeKey("1.0", mapper.getDeserializationContext()), 0.001);
        Assert.assertEquals(Double.NaN, deserializer.deserializeKey("NaN", mapper.getDeserializationContext()));
    }

    @Test
    public void testForTypeLocale() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Locale.class);
        Assert.assertEquals(Locale.ENGLISH, deserializer.deserializeKey("en_US", mapper.getDeserializationContext()));
    }

    @Test
    public void testForTypeDate() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Date.class);
        String dateString = "2022-03-01";
        Assert.assertEquals(mapper.parseDate(dateString), deserializer.deserializeKey(dateString, mapper.getDeserializationContext()));
    }

    @Test
    public void testForTypeCalendar() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Calendar.class);
        String dateString = "2022-03-01";
        JavaTimeZone timeZone = JavaTimeZone.systemDefault();
        Assert.assertEquals(mapper.constructCalendar(mapper.parseDate(dateString)), deserializer.deserializeKey(dateString, mapper.getDeserializationContext(new JavaTimeZoneParam(timeZone))));
    }

    @Test
    public void testForTypeUri() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);
        Assert.assertEquals(new URI("http://example.com"), deserializer.deserializeKey("http://example.com", mapper.getDeserializationContext()));
    }

    @Test
    public void testForTypeUrl() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);
        Assert.assertEquals(new URL("http://example.com"), deserializer.deserializeKey("http://example.com", mapper.getDeserializationContext()));
    }

    @Test
    public void testForTypeClass() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
        Assert.assertEquals(String.class, deserializer.deserializeKey("String.class", mapper.getDeserializationContext()));
    }

    @Test
    public void testForTypeEnum() throws Exception {
        enum TestEnum {
            ONE, TWO, THREE, FOUR
        }

        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(TestEnum.class);

        Assert.assertEquals(TestEnum.ONE, deserializer.deserializeKey("ONE", mapper.getDeserializationContext()));
        Assert.assertEquals(TestEnum.TWO, deserializer.deserializeKey("TWO", mapper.getDeserializationContext()));
        Assert.assertEquals(TestEnum.THREE, deserializer.deserializeKey("THREE", mapper.getDeserializationContext()));
        Assert.assertEquals(TestEnum.FOUR, deserializer.deserializeKey("FOUR", mapper.getDeserializationContext()));

        Assert.assertNull(deserializer.deserializeKey("FIVE", mapper.getDeserializationContext()));
        Assert.assertNull(deserializer.deserializeKey("SIX", mapper.getDeserializationContext()));
    }

    @Test
    public void testForTypeEnumWithToString() throws Exception {
        enum TestEnum {
            ONE, TWO, THREE, FOUR
        }

        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(TestEnum.class);
        deserializer.getAnnotationIntrospector().findProperty("readEnumUsingToString", TestEnum.class).set(true);

        Assert.assertEquals(TestEnum.ONE, deserializer.deserializeKey("ONE", mapper.getDeserializationContext()));
        Assert.assertEquals(TestEnum.TWO, deserializer.deserializeKey("TWO", mapper.getDeserializationContext()));
        Assert.assertEquals(TestEnum.THREE, deserializer.deserializeKey("THREE", mapper.getDeserializationContext()));
        Assert.assertEquals(TestEnum.FOUR, deserializer.deserializeKey("FOUR", mapper.getDeserializationContext()));

        Assert.assertEquals(TestEnum.ONE, deserializer.deserializeKey("ONE_ONE", mapper.getDeserializationContext()));
        Assert.assertEquals(TestEnum.TWO, deserializer.deserializeKey("TWO_TWO", mapper.getDeserializationContext()));
        Assert.assertEquals(TestEnum.THREE, deserializer.deserializeKey("THREE_THREE", mapper.getDeserializationContext()));
        Assert.assertEquals(TestEnum.FOUR, deserializer.deserializeKey("FOUR_FOUR", mapper.getDeserializationContext()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testForInvalidType() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(new Object(){}.getClass());
    }
}