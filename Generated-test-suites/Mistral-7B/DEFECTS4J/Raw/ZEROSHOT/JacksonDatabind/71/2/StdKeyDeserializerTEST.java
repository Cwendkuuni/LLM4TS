package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer.None;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.NodeType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.StdKeyDeserializer;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.EnumSet;
import java.util.Locale;
import java.util.UUID;

public class StdKeyDeserializerTest {

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testBooleanDeserializer() throws Exception {
        StdKeyDeserializer booleanDeserializer = StdKeyDeserializer.forType(Boolean.class);

        DeserializationContext ctxt = mapper.getDeserializationContext();

        Object key = booleanDeserializer.deserializeKey("true", ctxt);
        Assert.assertEquals(Boolean.TRUE, key);

        key = booleanDeserializer.deserializeKey("false", ctxt);
        Assert.assertEquals(Boolean.FALSE, key);

        key = booleanDeserializer.deserializeKey("invalid", ctxt);
        Assert.fail("Expected JsonMappingException but got: " + key);
    }

    @Test
    public void testByteDeserializer() throws Exception {
        StdKeyDeserializer byteDeserializer = StdKeyDeserializer.forType(Byte.class);

        DeserializationContext ctxt = mapper.getDeserializationContext();

        Object key = byteDeserializer.deserializeKey("10", ctxt);
        Assert.assertEquals((byte) 10, key);

        key = byteDeserializer.deserializeKey("127", ctxt);
        Assert.assertEquals((byte) 127, key);

        key = byteDeserializer.deserializeKey("128", ctxt);
        Assert.fail("Expected JsonMappingException but got: " + key);
    }

    @Test
    public void testShortDeserializer() throws Exception {
        StdKeyDeserializer shortDeserializer = StdKeyDeserializer.forType(Short.class);

        DeserializationContext ctxt = mapper.getDeserializationContext();

        Object key = shortDeserializer.deserializeKey("100", ctxt);
        Assert.assertEquals((short) 100, key);

        key = shortDeserializer.deserializeKey("32767", ctxt);
        Assert.assertEquals((short) 32767, key);

        key = shortDeserializer.deserializeKey("32768", ctxt);
        Assert.fail("Expected JsonMappingException but got: " + key);
    }

    @Test
    public void testCharDeserializer() throws Exception {
        StdKeyDeserializer charDeserializer = StdKeyDeserializer.forType(Character.class);

        DeserializationContext ctxt = mapper.getDeserializationContext();

        Object key = charDeserializer.deserializeKey("a", ctxt);
        Assert.assertEquals('a', key);

        key = charDeserializer.deserializeKey("z", ctxt);
        Assert.assertEquals('z', key);

        key = charDeserializer.deserializeKey("invalid", ctxt);
        Assert.fail("Expected JsonMappingException but got: " + key);
    }

    @Test
    public void testIntDeserializer() throws Exception {
        StdKeyDeserializer intDeserializer = StdKeyDeserializer.forType(Integer.class);

        DeserializationContext ctxt = mapper.getDeserializationContext();

        Object key = intDeserializer.deserializeKey("1000", ctxt);
        Assert.assertEquals(1000, key);

        key = intDeserializer.deserializeKey("Integer.MIN_VALUE", ctxt);
        Assert.assertEquals(Integer.MIN_VALUE, key);

        key = intDeserializer.deserializeKey("Integer.MAX_VALUE", ctxt);
        Assert.assertEquals(Integer.MAX_VALUE, key);

        key = intDeserializer.deserializeKey("invalid", ctxt);
        Assert.fail("Expected JsonMappingException but got: " + key);
    }

    @Test
    public void testLongDeserializer() throws Exception {
        StdKeyDeserializer longDeserializer = StdKeyDeserializer.forType(Long.class);

        DeserializationContext ctxt = mapper.getDeserializationContext();

        Object key = longDeserializer.deserializeKey("10000000000", ctxt);
        Assert.assertEquals(10000000000L, key);

        key = longDeserializer.deserializeKey("Long.MIN_VALUE", ctxt);
        Assert.assertEquals(Long.MIN_VALUE, key);

        key = longDeserializer.deserializeKey("Long.MAX_VALUE", ctxt);
        Assert.assertEquals(Long.MAX_VALUE, key);

        key = longDeserializer.deserializeKey("invalid", ctxt);
        Assert.fail("Expected JsonMappingException but got: " + key);
    }

    @Test
    public void testFloatDeserializer() throws Exception {
        StdKeyDeserializer floatDeserializer = StdKeyDeserializer.forType(Float.class);

        DeserializationContext ctxt = mapper.getDeserializationContext();

        Object key = floatDeserializer.deserializeKey("1.0", ctxt);
        Assert.assertEquals(1.0f, key);

        key = floatDeserializer.deserializeKey("3.14", ctxt);
        Assert.assertEquals(3.14f, key);

        key = floatDeserializer.deserializeKey("invalid", ctxt);
        Assert.fail("Expected JsonMappingException but got: " + key);
    }

    @Test
    public void testDoubleDeserializer() throws Exception {
        StdKeyDeserializer doubleDeserializer = StdKeyDeserializer.forType(Double.class);

        DeserializationContext ctxt = mapper.getDeserializationContext();

        Object key = doubleDeserializer.deserializeKey("1.0", ctxt);
        Assert.assertEquals(1.0, key);

        key = doubleDeserializer.deserializeKey("3.14", ctxt);
        Assert.assertEquals(3.14, key);

        key = doubleDeserializer.deserializeKey("invalid", ctxt);
        Assert.fail("Expected JsonMappingException but got: " + key);
    }

    @Test
    public void testLocaleDeserializer() throws Exception {
        StdKeyDeserializer localeDeserializer = StdKeyDeserializer.forType(Locale.class);

        DeserializationContext ctxt = mapper.getDeserializationContext();

        Object key = localeDeserializer.deserializeKey("en_US", ctxt);
        Assert.assertEquals(Locale.US, key);

        key = localeDeserializer.deserializeKey("es_ES", ctxt);
        Assert.assertEquals(Locale.SPAIN, key);

        key = localeDeserializer.deserializeKey("invalid", ctxt);
        Assert.fail("Expected JsonMappingException but got: " + key);
    }

    @Test
    public void testDateDeserializer() throws Exception {
        StdKeyDeserializer dateDeserializer = StdKeyDeserializer.forType(Date.class);

        DeserializationContext ctxt = mapper.getDeserializationContext();

        Object key = dateDeserializer.deserializeKey("2023-03-07", ctxt);
        Assert.assertNotNull(key);
        Assert.assertTrue(((Date) key).after(new Date()));

        key = dateDeserializer.deserializeKey("invalid", ctxt);
        Assert.fail("Expected JsonMappingException but got: " + key);
    }

    @Test
    public void testCalendarDeserializer() throws Exception {
        StdKeyDeserializer calendarDeserializer = StdKeyDeserializer.forType(Calendar.class);

        DeserializationContext ctxt = mapper.getDeserializationContext();

        Object key = calendarDeserializer.deserializeKey("2023-03-07", ctxt);
        Assert.assertNotNull(key);
        Assert.assertTrue(((Calendar) key).after(Calendar.getInstance()));

        key = calendarDeserializer.deserializeKey("invalid", ctxt);
        Assert.fail("Expected JsonMappingException but got: " + key);
    }

    @Test
    public void testUUIDDeserializer() throws Exception {
        StdKeyDeserializer uuidDeserializer = StdKeyDeserializer.forType(UUID.class);

        DeserializationContext ctxt = mapper.getDeserializationContext();

        Object key = uuidDeserializer.deserializeKey("6ba7b810-9dad-11d1-80b4-00c04fd430c8", ctxt);
        Assert.assertEquals(UUID.fromString("6ba7b810-9dad-11d1-80b4-00c04fd430c8"), key);

        key = uuidDeserializer.deserializeKey("invalid", ctxt);
        Assert.fail("Expected JsonMappingException but got: " + key);
    }

    @Test
    public void testURIDeserializer() throws Exception {
        StdKeyDeserializer uriDeserializer = StdKeyDeserializer.forType(URI.class);

        DeserializationContext ctxt = mapper.getDeserializationContext();

        Object key = uriDeserializer.deserializeKey("http://www.example.com", ctxt);
        Assert.assertEquals(new URI("http://www.example.com"), key);

        key = uriDeserializer.deserializeKey("invalid", ctxt);
        Assert.fail("Expected JsonMappingException but got: " + key);
    }

    @Test
    public void testURLDeserializer() throws Exception {
        StdKeyDeserializer urlDeserializer = StdKeyDeserializer.forType(URL.class);

        DeserializationContext ctxt = mapper.getDeserializationContext();

        Object key = urlDeserializer.deserializeKey("http://www.example.com", ctxt);
        Assert.assertEquals(new URL("http://www.example.com"), key);

        key = urlDeserializer.deserializeKey("invalid", ctxt);
        Assert.fail("Expected JsonMappingException but got: " + key);
    }

    @Test
    public void testEnumDeserializer() throws Exception {
        EnumSet<DaysOfWeek> daysOfWeek = EnumSet.allOf(DaysOfWeek.class);

        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer enumDeserializer = StdKeyDeserializer.forType(DaysOfWeek.class);

        DeserializationContext ctxt = mapper.getDeserializationContext();

        Object key = enumDeserializer.deserializeKey("MONDAY", ctxt);
        Assert.assertEquals(DaysOfWeek.MONDAY, key);

        key = enumDeserializer.deserializeKey("FRIDAY", ctxt);
        Assert.assertEquals(DaysOfWeek.FRIDAY, key);

        key = enumDeserializer.deserializeKey("invalid", ctxt);
        Assert.fail("Expected JsonMappingException but got: " + key);
    }

    @Test
    public void testCustomClassDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        StdKeyDeserializer customClassDeserializer = new StdKeyDeserializer(7, MyCustomClass.class);

        DeserializationContext ctxt = mapper.getDeserializationContext();

        Object key = customClassDeserializer.deserializeKey("MyCustomClassKey", ctxt);
        Assert.assertNotNull(key);
        Assert.assertTrue(key instanceof MyCustomClass);

        key = customClassDeserializer.deserializeKey("invalid", ctxt);
        Assert.fail("Expected JsonMappingException but got: " + key);
    }
}

class MyCustomClass {
    private String name;

    public MyCustomClass(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "MyCustomClass{" +
                "name='" + name + '\'' +
                '}';
    }
}

enum DaysOfWeek {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}