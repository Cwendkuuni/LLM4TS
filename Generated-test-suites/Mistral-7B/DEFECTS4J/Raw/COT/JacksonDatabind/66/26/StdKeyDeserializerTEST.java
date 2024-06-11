package com.example.test; // replace with the actual package of your test classes

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;

import com.fasterxml.jackson.annotation.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFactory;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.BeanDeserializerFactory;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import org.junit.Before;
import org.junit.Test;

/**
 * This test file tests the functionality and edge cases of the StdKeyDeserializer class.
 */
public class StdKeyDeserializerTest {
    // Test data for different types
    static class TestEnum {
        @JsonValue
        public String toString() {
            return name();
        }

        public String name() {
            return this.getClass().getSimpleName();
        }
    }

    static class TestEnumWithFactory {
        @JsonFactory
        public static TestEnumWithFactory create() {
            return new TestEnumWithFactory();
        }

        @JsonDeserialize(using = TestEnumWithDeserializer.class)
        public static class TestEnumWithDeserializer extends StdKeyDeserializer {
            public TestEnumWithDeserializer() {
                super(-1, TestEnumWithFactory.class);
            }
        }

        private String name;

        public TestEnumWithFactory() {
            name = "TestEnumWithFactory";
        }

        public String getName() {
            return name;
        }
    }

    static class TestStringCtor {
        private final String value;

        public TestStringCtor(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    static class TestStringFactory {
        private final String name;

        public TestStringFactory() {
            name = "TestStringFactory";
        }

        public static TestStringFactory create() {
            return new TestStringFactory();
        }

        public String getName() {
            return name;
        }
    }

    static class TestObjectWithFactory {
        private String name;

        @JsonCreator
        public static TestObjectWithFactory create(String name) {
            TestObjectWithFactory instance = new TestObjectWithFactory();
            instance.name = name;
            return instance;
        }

        public String getName() {
            return name;
        }
    }

    static class TestObjectWithCtor {
        private final String name;

        public TestObjectWithCtor(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    static class TestMapEntry {
        private final String key;
        private final String value;

        public TestMapEntry(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }

    static class TestCustomDeserializer {
        private static final StdKeyDeserializer.StringKD STRING_KD = StdKeyDeserializer.StringKD.forType(String.class);

        @JsonDeserialize(using = TestCustomDeserializer.TestCustomDeserializer.class)
        public static class TestCustomDeserializer {
            public TestCustomDeserializer() {
                // Custom deserializer for testing
            }

            public Object deserialize(String key, DeserializationContext ctxt) throws IOException {
                return STRING_KD.deserializeKey(key, ctxt);
            }
        }

        private String name;

        public TestCustomDeserializer() {
            name = "TestCustomDeserializer";
        }

        public String getName() {
            return name;
        }
    }

    @Before
    public void setUp() {
        // Create mocks for DeserializationContext and JsonParser
        DeserializationContext mockContext = mock(DeserializationContext.class);
        JsonParser mockParser = mock(JsonParser.class);

        when(mockContext.getFactory()).thenReturn(mockParser);
        when(mockContext.getConfig()).thenReturn(new ObjectMapper.DefaultConfig());
        when(mockContext.getAnnotationIntrospector()).thenReturn(new JacksonAnnotationIntrospector());
        when(mockContext.handleWeirdKey(any(), anyString(), anyString())).thenAnswer(i -> i.getArguments()[0]);
    }

    @Test
    public void testStdKeyDeserializerForBasicTypes() throws Exception {
        for (int kind = StdKeyDeserializer.TYPE_BOOLEAN; kind <= StdKeyDeserializer.TYPE_URI; kind++) {
            Class<?> keyClass = StdKeyDeserializer.getPrimitiveType(kind);
            StdKeyDeserializer deserializer = StdKeyDeserializer.forType(keyClass);

            String key = keyClass.getName();
            Object deserializedKey = deserializer.deserializeKey(key, mockContext);
            assertEquals(keyClass.isInstance(deserializedKey), true);
        }
    }

    @Test
    public void testStdKeyDeserializerForEnumTypes() throws Exception {
        TestEnum[] enumValues = TestEnum.values();

        for (TestEnum enumValue : enumValues) {
            EnumResolver enumResolver = new EnumResolver(TestEnum.class);
            AnnotatedMethod factoryMethod = enumResolver.findMethod(TestEnum.class, "valueOf", String.class);
            StdKeyDeserializer enumDeserializer = new EnumKD(enumResolver, factoryMethod);

            String key = enumValue.name();
            Object deserializedKey = enumDeserializer.deserializeKey(key, mockContext);
            assertSame(enumValue, deserializedKey);
        }
    }

    @Test
    public void testStdKeyDeserializerForCustomDeserializer() throws Exception {
        TestCustomDeserializer testCustomDeserializer = new TestCustomDeserializer();
        BeanDeserializerFactory beanDeserializerFactory = mock(BeanDeserializerFactory.class);
        when(mockContext.findBeanDeserializer(TestCustomDeserializer.class, null)).thenReturn(testCustomDeserializer);
        when(mockContext.findBeanDeserializer(any(), any())).thenReturn(beanDeserializerFactory);

        String key = TestCustomDeserializer.class.getName();
        Object deserializedKey = StdKeyDeserializer.forType(TestCustomDeserializer.class).deserializeKey(key, mockContext);
        assertEquals(TestCustomDeserializer.class, deserializedKey);
    }

    @Test
    public void testStdKeyDeserializerForStringCtor() throws Exception {
        Class<?> keyClass = TestStringCtor.class;
        Constructor<?> stringCtor = keyClass.getDeclaredConstructor(String.class);
        StdKeyDeserializer stringCtorDeserializer = new StringCtorKeyDeserializer(stringCtor);

        String key = "TestStringCtor";
        Object deserializedKey = stringCtorDeserializer.deserializeKey(key, mockContext);
        TestStringCtor result = (TestStringCtor) deserializedKey;
        assertEquals(key, result.getValue());
    }

    @Test
    public void testStdKeyDeserializerForStringFactory() throws Exception {
        Class<?> keyClass = TestStringFactory.class;
        Method factoryMethod = keyClass.getDeclaredMethod("create");
        StdKeyDeserializer stringFactoryDeserializer = new StringFactoryKeyDeserializer(factoryMethod);

        Object deserializedKey = stringFactoryDeserializer.deserializeKey("TestStringFactory", mockContext);
        TestStringFactory result = (TestStringFactory) deserializedKey;
        assertEquals("TestStringFactory", result.getName());
    }

    @Test
    public void testStdKeyDeserializerForObjectWithFactory() throws Exception {
        Class<?> keyClass = TestObjectWithFactory.class;
        Method factoryMethod = keyClass.getDeclaredMethod("create");
        StdKeyDeserializer objectFactoryDeserializer = new DelegatingKD(keyClass, factoryMethod);

        String key = "TestObjectWithFactory";
        Object deserializedKey = objectFactoryDeserializer.deserializeKey(key, mockContext);
        TestObjectWithFactory result = (TestObjectWithFactory) deserializedKey;
        assertEquals("TestObjectWithFactory", result.getName());
    }

    @Test
    public void testStdKeyDeserializerForObjectWithCtor() throws Exception {
        Class<?> keyClass = TestObjectWithCtor.class;
        Constructor<?> ctor = keyClass.getDeclaredConstructor(String.class);
        StdKeyDeserializer objectCtorDeserializer = new StringCtorKeyDeserializer(ctor);

        String key = "TestObjectWithCtor";
        Object deserializedKey = objectCtorDeserializer.deserializeKey(key, mockContext);
        TestObjectWithCtor result = (TestObjectWithCtor) deserializedKey;
        assertEquals("TestObjectWithCtor", result.getName());
    }

    @Test
    public void testStdKeyDeserializerForMapEntry() throws Exception {
        Class<?> mapEntryClass = TestMapEntry.class;
        Constructor<?> mapEntryCtor = mapEntryClass.getDeclaredConstructor(String.class, String.class);
        StdKeyDeserializer mapEntryCtorDeserializer = new StringCtorKeyDeserializer(mapEntryCtor);

        String key1 = "TestMapEntry:key1";
        String value1 = "TestMapEntry:value1";
        Object deserializedKey1 = mapEntryCtorDeserializer.deserializeKey(key1, mockContext);
        TestMapEntry result1 = (TestMapEntry) deserializedKey1;
        assertEquals(key1, result1.getKey());
        assertEquals(value1, result1.getValue());

        String key2 = "TestMapEntry:key2";
        Object deserializedKey2 = mapEntryCtorDeserializer.deserializeKey(key2, mockContext);
        assertNull(deserializedKey2);

        // Test deserializing a key with a leading '#' character to check if it is treated as a map entry key or an Object ID
        String key3 = "#TestMapEntry:key3";
        Object deserializedKey3 = mapEntryCtorDeserializer.deserializeKey(key3, mockContext);
        assertNull(deserializedKey3);
    }
}