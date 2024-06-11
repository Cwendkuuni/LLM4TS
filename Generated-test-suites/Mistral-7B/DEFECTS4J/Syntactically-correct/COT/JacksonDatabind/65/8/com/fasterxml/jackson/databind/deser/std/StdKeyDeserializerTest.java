package com.example.test;

import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EnumSet;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class StdKeyDeserializerTest {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testDeserializeKey_String() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
        String key = "test";
        String expected = key;
        Object result = keyDeserializer.deserializeKey(key, new DeserializationContext(objectMapper, new AnnotatedClass(String.class), null));
        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeKey_Boolean() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
        String keyTrue = "true";
        Boolean expectedTrue = Boolean.TRUE;
        Object resultTrue = keyDeserializer.deserializeKey(keyTrue, new DeserializationContext(objectMapper, new AnnotatedClass(Boolean.class), null));
        assertNotNull(resultTrue);
        assertEquals(expectedTrue, resultTrue);

        String keyFalse = "false";
        Boolean expectedFalse = Boolean.FALSE;
        Object resultFalse = keyDeserializer.deserializeKey(keyFalse, new DeserializationContext(objectMapper, new AnnotatedClass(Boolean.class), null));
        assertNotNull(resultFalse);
        assertEquals(expectedFalse, resultFalse);
    }

    @Test
    public void testDeserializeKey_Byte() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);
        String keyByteMin = "0";
        Byte expectedByteMin = Byte.MIN_VALUE;
        Object resultByteMin = keyDeserializer.deserializeKey(keyByteMin, new DeserializationContext(objectMapper, new AnnotatedClass(Byte.class), null));
        assertNotNull(resultByteMin);
        assertEquals(expectedByteMin, resultByteMin);

        String keyByteMax = "127";
        Byte expectedByteMax = Byte.MAX_VALUE;
        Object resultByteMax = keyDeserializer.deserializeKey(keyByteMax, new DeserializationContext(objectMapper, new AnnotatedClass(Byte.class), null));
        assertNotNull(resultByteMax);
        assertEquals(expectedByteMax, resultByteMax);

        String keyByteOutOfRange = "256";
        Object resultByteOutOfRange = keyDeserializer.deserializeKey(keyByteOutOfRange, new DeserializationContext(objectMapper, new AnnotatedClass(Byte.class), null));
        assertNull(resultByteOutOfRange);
    }

    // Test other data types, such as Short, Char, Int, Long, Float, Double, Locale, Date, Calendar, UUID, URI, URL, Class, and custom types here

    @Test
    public void testDeserializeKey_Enum() throws Exception {
        KeyDeserializer keyDeserializer = new EnumKD(objectMapper.getFactory().getEnumResolver(EnumSet.of(MyEnum.VAL1, MyEnum.VAL2)), new AnnotatedMethod(MyEnum.class.getDeclaredMethod("valueOf", String.class)));
        String keyEnumVal1 = "VAL1";
        MyEnum expectedEnumVal1 = MyEnum.VAL1;
        Object resultEnumVal1 = keyDeserializer.deserializeKey(keyEnumVal1, new DeserializationContext(objectMapper, new AnnotatedClass(MyEnum.class), null));
        assertNotNull(resultEnumVal1);
        assertEquals(expectedEnumVal1, resultEnumVal1);

        String keyEnumVal2 = "VAL2";
        MyEnum expectedEnumVal2 = MyEnum.VAL2;
        Object resultEnumVal2 = keyDeserializer.deserializeKey(keyEnumVal2, new DeserializationContext(objectMapper, new AnnotatedClass(MyEnum.class), null));
        assertNotNull(resultEnumVal2);
        assertEquals(expectedEnumVal2, resultEnumVal2);
    }

    @Test
    public void testDeserializeKey_StringConstructor() throws Exception {
        Class<?> clazz = MyStringConstructorClass.class;
        Constructor<?> constructor = clazz.getDeclaredConstructor(String.class);
        KeyDeserializer keyDeserializer = new StringCtorKeyDeserializer(constructor);
        String key = "test";
        MyStringConstructorClass expected = new MyStringConstructorClass("test");
        Object result = keyDeserializer.deserializeKey(key, new DeserializationContext(objectMapper, new AnnotatedClass(clazz), null));
        assertNotNull(result);
        assertEquals(expected, result);
    }

    // Test other classes with single-string-arg constructors here

    @Test
    public void testDeserializeKey_StaticFactoryMethod() throws Exception {
        Class<?> clazz = MyStaticFactoryClass.class;
        Method method = clazz.getDeclaredMethod("create");
        KeyDeserializer keyDeserializer = new StringFactoryKeyDeserializer(method);
        String key = "test";
        MyStaticFactoryClass expected = MyStaticFactoryClass.create();
        Object result = keyDeserializer.deserializeKey(key, new DeserializationContext(objectMapper, new AnnotatedClass(clazz), null));
        assertNotNull(result);
        assertEquals(expected, result);
    }

    // Test other classes with static no-args factory methods here

    @Test
    public void testDeserializeKey_EmptyString() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
        String keyEmpty = "";
        Object resultEmpty = keyDeserializer.deserializeKey(keyEmpty, new DeserializationContext(objectMapper, new AnnotatedClass(String.class), null));
        assertNotNull(resultEmpty);
        assertEquals(keyEmpty, resultEmpty);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeKey_NullKey() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
        String keyNull = null;
        keyDeserializer.deserializeKey(keyNull, new DeserializationContext(objectMapper, new AnnotatedClass(String.class), null));
    }

    @Test(expected = IllegalStateException.class)
    public void testDeserializeKey_UnknownKeyType() throws Exception {
        KeyDeserializer keyDeserializer = new StdKeyDeserializer(-1, Object.class);
        String key = "test";
        keyDeserializer.deserializeKey(key, new DeserializationContext(objectMapper, new AnnotatedClass(Object.class), null));
    }
}

class MyEnum {
    public static final MyEnum VAL1 = new MyEnum("VAL1");
    public static final MyEnum VAL2 = new MyEnum("VAL2");

    private final String name;

    MyEnum(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }

    // Add getters, setters, and other methods here
}

class MyStringConstructorClass {
    private final String value;

    MyStringConstructorClass(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    // Add other methods here
}

class MyStaticFactoryClass {
    private static MyStaticFactoryClass instance;

    private MyStaticFactoryClass() {
        // Empty constructor
    }

    public static MyStaticFactoryClass create() {
        if (instance == null) {
            instance = new MyStaticFactoryClass();
        }
        return instance;
    }

    // Add other methods here
}