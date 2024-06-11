package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.TestBase;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.EnumSet;
import java.util.Locale;
import java.util.UUID;

public class StdKeyDeserializerTest extends TestBase {

    // Test cases for various types

    @Test
    public void testBooleanKeyDeserializer() throws IOException {
        ObjectNode rootNode = mapper().createObjectNode();
        rootNode.put("key", "true");
        Object key = mapper().treeToValue(rootNode, StdKeyDeserializer.forType(Boolean.class));
        assertTrue((Boolean) key);

        rootNode.put("key", "false");
        key = mapper().treeToValue(rootNode, StdKeyDeserializer.forType(Boolean.class));
        assertFalse((Boolean) key);
    }

    @Test
    public void testByteKeyDeserializer() throws IOException {
        ObjectNode rootNode = mapper().createObjectNode();
        rootNode.put("key", "12");
        Object key = mapper().treeToValue(rootNode, StdKeyDeserializer.forType(Byte.class));
        assertEquals((Byte) 12, key);
    }

    @Test
    public void testShortKeyDeserializer() throws IOException {
        ObjectNode rootNode = mapper().createObjectNode();
        rootNode.put("key", "256");
        Object key = mapper().treeToValue(rootNode, StdKeyDeserializer.forType(Short.class));
        assertEquals((Short) 256, key);

        rootNode.put("key", "-128");
        key = mapper().treeToValue(rootNode, StdKeyDeserializer.forType(Short.class));
        assertEquals((Short) -128, key);
    }

    @Test
    public void testCharKeyDeserializer() throws IOException {
        ObjectNode rootNode = mapper().createObjectNode();
        rootNode.put("key", "A");
        Object key = mapper().treeToValue(rootNode, StdKeyDeserializer.forType(Character.class));
        assertEquals((Character) 'A', key);
    }

    @Test
    public void testIntKeyDeserializer() throws IOException {
        ObjectNode rootNode = mapper().createObjectNode();
        rootNode.put("key", "100");
        Object key = mapper().treeToValue(rootNode, StdKeyDeserializer.forType(Integer.class));
        assertEquals((Integer) 100, key);
    }

    @Test
    public void testLongKeyDeserializer() throws IOException {
        ObjectNode rootNode = mapper().createObjectNode();
        rootNode.put("key", "9223372036854775807");
        Object key = mapper().treeToValue(rootNode, StdKeyDeserializer.forType(Long.class));
        assertEquals((Long) 9223372036854775807L, key);
    }

    @Test
    public void testFloatKeyDeserializer() throws IOException {
        ObjectNode rootNode = mapper().createObjectNode();
        rootNode.put("key", "3.14");
        Object key = mapper().treeToValue(rootNode, StdKeyDeserializer.forType(Float.class));
        assertEquals((Float) 3.14F, key);
    }

    @Test
    public void testDoubleKeyDeserializer() throws IOException {
        ObjectNode rootNode = mapper().createObjectNode();
        rootNode.put("key", "3.14159265358979323846");
        Object key = mapper().treeToValue(rootNode, StdKeyDeserializer.forType(Double.class));
        assertEquals((Double) 3.14159265358979323846, key);
    }

    @Test
    public void testLocaleKeyDeserializer() throws IOException {
        ObjectNode rootNode = mapper().createObjectNode();
        rootNode.put("key", "en_US");
        Object key = mapper().treeToValue(rootNode, StdKeyDeserializer.forType(Locale.class));
        assertEquals(Locale.US, key);
    }

    @Test
    public void testDateKeyDeserializer() throws IOException {
        ObjectNode rootNode = mapper().createObjectNode();
        rootNode.put("key", "2023-03-22");
        Object key = mapper().treeToValue(rootNode, StdKeyDeserializer.forType(Date.class));
        assertNotNull(key);
        assertTrue(key instanceof Date);
    }

    @Test
    public void testCalendarKeyDeserializer() throws IOException {
        ObjectNode rootNode = mapper().createObjectNode();
        rootNode.put("key", "2023-03-22");
        Object key = mapper().treeToValue(rootNode, StdKeyDeserializer.forType(Calendar.class));
        assertNotNull(key);
        assertTrue(key instanceof Calendar);
    }

    @Test
    public void testUUIDKeyDeserializer() throws IOException {
        ObjectNode rootNode = mapper().createObjectNode();
        rootNode.put("key", "123e4567-e89b-12d3-a456-426614174000");
        Object key = mapper().treeToValue(rootNode, StdKeyDeserializer.forType(UUID.class));
        assertEquals(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), key);
    }

    @Test
    public void testURIKeyDeserializer() throws IOException {
        ObjectNode rootNode = mapper().createObjectNode();
        rootNode.put("key", "https://www.example.com/");
        Object key = mapper().treeToValue(rootNode, StdKeyDeserializer.forType(URI.class));
        assertEquals(new URI("https://www.example.com/"), key);
    }

    @Test
    public void testURLKeyDeserializer() throws IOException {
        ObjectNode rootNode = mapper().createObjectNode();
        rootNode.put("key", "https://www.example.com/");
        Object key = mapper().treeToValue(rootNode, StdKeyDeserializer.forType(URL.class));
        assertEquals(new URL("https://www.example.com/"), key);
    }

    @Test
    public void testClassKeyDeserializer() throws IOException {
        ObjectNode rootNode = mapper().createObjectNode();
        rootNode.put("key", "java.lang.String");
        Object key = mapper().treeToValue(rootNode, StdKeyDeserializer.forType(String.class));
        assertEquals(String.class, key);

        rootNode.put("key", "java.util.Locale");
        key = mapper().treeToValue(rootNode, StdKeyDeserializer.forType(Locale.class));
        assertEquals(Locale.class, key);
    }

    @Test
    public void testEnumKeyDeserializer() throws IOException {
        ObjectNode rootNode = mapper().createObjectNode();
        rootNode.put("key", "WEDNESDAY");

        EnumSet<java.time.DayOfWeek> days = EnumSet.allOf(java.time.DayOfWeek.class);
        Object key = mapper().treeToValue(rootNode, StdKeyDeserializer.forType(java.time.DayOfWeek.class));
        assertEquals(java.time.DayOfWeek.WEDNESDAY, key);

        rootNode.put("key", "THURSDAY");
        key = mapper().treeToValue(rootNode, StdKeyDeserializer.forType(java.time.DayOfWeek.class));
        assertEquals(java.time.DayOfWeek.THURSDAY, key);
    }

    // Test case for StringKeyDeserializer
    @Test
    public void testStringKeyDeserializer() throws IOException {
        ObjectNode rootNode = mapper().createObjectNode();
        rootNode.put("key", "value");
        Object key = mapper().treeToValue(rootNode, StdKeyDeserializer.forType(String.class));
        assertEquals("value", key);
    }

    // Test case for DelegatingKD
    @Test
    public void testDelegatingKD() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final JsonDeserializer<MyBean> beanDeserializer = new SimpleBeanDeserializer();
        final Class<MyBean> beanClass = MyBean.class;

        final KeyDeserializer keyDeserializer = new DelegatingKD(beanClass, beanDeserializer);
        final ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("key", "{\"name\":\"John\", \"age\":45}");
        final Object key = mapper.treeToValue(rootNode, keyDeserializer);
        final MyBean deserializedObject = mapper.readValue(rootNode.toString(), MyBean.class);

        assertEquals(deserializedObject, key);
    }

    // Test case for EnumKD
    @Test
    public void testEnumKD() throws IOException {
        ObjectNode rootNode = mapper().createObjectNode();
        rootNode.put("key", "MONDAY");

        EnumResolver resolver = new EnumResolver(java.time.DayOfWeek.class);
        Object key = mapper().treeToValue(rootNode, new EnumKD(resolver, null));
        assertEquals(java.time.DayOfWeek.MONDAY, key);

        rootNode.put("key", "FRIDAY");
        key = mapper().treeToValue(rootNode, new EnumKD(resolver, null));
        assertEquals(java.time.DayOfWeek.FRIDAY, key);

        // Test case for EnumKD with getEnumValueByName
        ObjectNode rootNode2 = mapper().createObjectNode();
        rootNode2.put("key", "THURSDAY");

        EnumResolver resolver2 = new EnumResolver(java.time.DayOfWeek.class);
        Object key2 = mapper().treeToValue(rootNode2, new EnumKD(resolver2, null));
        assertEquals(java.time.DayOfWeek.THURSDAY, key2);
    }

    // Test case for StringCtorKeyDeserializer
    @Test
    public void testStringCtorKeyDeserializer() throws IOException, InstantiationException, IllegalAccessException {
        ObjectNode rootNode = mapper().createObjectNode();
        rootNode.put("key", "HelloWorld");

        Constructor<MyStringConstructorClass> constructor = MyStringConstructorClass.class.getConstructor(String.class);
        Object key = mapper().treeToValue(rootNode, new StringCtorKeyDeserializer(constructor));
        assertEquals("HelloWorld", key);
    }

    // Test case for StringFactoryKeyDeserializer
    @Test
    public void testStringFactoryKeyDeserializer() throws IOException, InstantiationException, IllegalAccessException {
        ObjectNode rootNode = mapper().createObjectNode();
        rootNode.put("key", "HelloWorld");

        Method factoryMethod = MyStringFactoryClass.class.getMethod("createString", String.class);
        Object key = mapper().treeToValue(rootNode, new StringFactoryKeyDeserializer(factoryMethod));
        assertEquals("HelloWorld", key);
    }
}