package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    @Test
    public void testBooleanKeyDeserialization() throws JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Boolean.class);
        Object key = serializer.deserializeKey("true", new TestContext());
        assertTrue(key instanceof Boolean);
        assertEquals(Boolean.TRUE, key);

        key = serializer.deserializeKey("false", new TestContext());
        assertTrue(key instanceof Boolean);
        assertEquals(Boolean.FALSE, key);
    }

    @Test
    public void testByteKeyDeserialization() throws JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Byte.class);
        Object key = serializer.deserializeKey("10", new TestContext());
        assertTrue(key instanceof Byte);
        assertEquals((Byte) 10, key);

        key = serializer.deserializeKey("127", new TestContext());
        assertTrue(key instanceof Byte);
        assertEquals((Byte) 127, key);

        try {
            key = serializer.deserializeKey("-1", new TestContext());
            fail();
        } catch (JsonProcessingException e) {
            // expected
        }

        try {
            key = serializer.deserializeKey("256", new TestContext());
            fail();
        } catch (JsonProcessingException e) {
            // expected
        }
    }

    @Test
    public void testShortKeyDeserialization() throws JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Short.class);
        Object key = serializer.deserializeKey("100", new TestContext());
        assertTrue(key instanceof Short);
        assertEquals((Short) 100, key);

        try {
            key = serializer.deserializeKey("-32768", new TestContext());
            fail();
        } catch (JsonProcessingException e) {
            // expected
        }

        try {
            key = serializer.deserializeKey("32768", new TestContext());
            fail();
        } catch (JsonProcessingException e) {
            // expected
        }
    }

    @Test
    public void testCharKeyDeserialization() throws JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Character.class);
        Object key = serializer.deserializeKey("a", new TestContext());
        assertTrue(key instanceof Character);
        assertEquals((Character) 'a', key);

        key = serializer.deserializeKey("\u00E9", new TestContext());
        assertTrue(key instanceof Character);
        assertEquals((Character) '\u00E9', key);

        try {
            key = serializer.deserializeKey("", new TestContext());
            fail();
        } catch (JsonProcessingException e) {
            // expected
        }

        try {
            key = serializer.deserializeKey("ab", new TestContext());
            fail();
        } catch (JsonProcessingException e) {
            // expected
        }
    }

    @Test
    public void testIntKeyDeserialization() throws JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Integer.class);
        Object key = serializer.deserializeKey("100", new TestContext());
        assertTrue(key instanceof Integer);
        assertEquals((Integer) 100, key);
    }

    @Test
    public void testLongKeyDeserialization() throws JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Long.class);
        Object key = serializer.deserializeKey("1000L", new TestContext());
        assertTrue(key instanceof Long);
        assertEquals((Long) 1000L, key);
    }

    @Test
    public void testFloatKeyDeserialization() throws JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Float.class);
        Object key = serializer.deserializeKey("1.5f", new TestContext());
        assertTrue(key instanceof Float);
        assertEquals((Float) 1.5f, key);
    }

    @Test
    public void testDoubleKeyDeserialization() throws JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Double.class);
        Object key = serializer.deserializeKey("1.5", new TestContext());
        assertTrue(key instanceof Double);
        assertEquals((Double) 1.5, key);
    }

    @Test
    public void testLocaleKeyDeserialization() throws JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Locale.class);
        Object key = serializer.deserializeKey("en_US", new TestContext());
        assertTrue(key instanceof Locale);
        assertEquals(Locale.US, key);
    }

    @Test
    public void testDateKeyDeserialization() throws JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Date.class);
        Object key = serializer.deserializeKey("2023-03-12", new TestContext());
        assertTrue(key instanceof Date);
        assertEquals(new Date(1678592000000L), key);
    }

    @Test
    public void testCalendarKeyDeserialization() throws JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Calendar.class);
        Object key = serializer.deserializeKey("2023-03-12", new TestContext());
        assertTrue(key instanceof Calendar);
        // The actual testing of the Calendar object would depend on the specific implementation.
    }

    @Test
    public void testUriKeyDeserialization() throws JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URI.class);
        Object key = serializer.deserializeKey("http://example.com", new TestContext());
        assertTrue(key instanceof URI);
        assertEquals(new URI("http://example.com"), key);
    }

    @Test
    public void testUrlKeyDeserialization() throws JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URL.class);
        Object key = serializer.deserializeKey("http://example.com", new TestContext());
        assertTrue(key instanceof URL);
        assertEquals(new URL("http://example.com"), key);
    }

    @Test
    public void testClassKeyDeserialization() throws JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(String.class);
        Object key = serializer.deserializeKey("java.lang.String", new TestContext());
        assertTrue(key instanceof Class);
        assertEquals(String.class, key);
    }

    @Test
    public void testEnumKeyDeserialization() throws JsonProcessingException {
        enum MyEnum {
            VALUE1, VALUE2, VALUE3
        }

        StdKeyDeserializer serializer = StdKeyDeserializer.forType(MyEnum.class);
        Object key = serializer.deserializeKey("VALUE1", new TestContext());
        assertTrue(key instanceof MyEnum);
        assertEquals(MyEnum.VALUE1, key);

        key = serializer.deserializeKey("VALUE3", new TestContext());
        assertTrue(key instanceof MyEnum);
        assertEquals(MyEnum.VALUE3, key);

        try {
            key = serializer.deserializeKey("INVALID_VALUE", new TestContext());
            fail();
        } catch (JsonProcessingException e) {
            // expected
        }
    }

    @Test
    public void testByteArrayKeyDeserialization() throws JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(byte[].class);
        Object key = serializer.deserializeKey("Rg9jYWNoIGlzIGh1bWFuLCBidXQgdG8gZW1icmFjZTogU2FsdGg=", new TestContext());
        assertTrue(key instanceof byte[]);
        byte[] expected = {72, 71, 111, 114, 105, 112, 116, 116, 112, 101, 100, 32, 105, 110, 32, 103, 111, 111, 118, 101, 114, 116, 104, 101, 114};
        assertArrayEquals(expected, (byte[]) key);
    }

    @Test
    public void testUnknownKeyType() {
        try {
            StdKeyDeserializer serializer = StdKeyDeserializer.forType(new Object() {}.getClass());
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public ObjectMapper getConfig() {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);
            return mapper;
        }

        @Override
        public JavaType constructType(Class<?> rawType) {
            return null;
        }

        @Override
        public JavaType constructType(Class<?> rawType, PropertyName propertyName) {
            return null;
        }

        @Override
        public JavaType constructType(String name) {
            return null;
        }

        @Override
        public JavaType constructTypeFromAnnotationMembers(Class<?> rawType, AnnotatedMember member, AnnotationInstance annotations) {
            return null;
        }

        @Override
        public JavaType constructTypeFromAnnotationMembers(Class<?> rawType, AnnotatedMethod member, AnnotationInstance annotations) {
            return null;
        }

        @Override
        public JavaType constructTypeFromAnnotationMembers(Class<?> rawType, AnnotatedConstructor constructor, AnnotationInstance annotations) {
            return null;
        }

        @Override
        public JavaType constructType(JavaType baseType, Class<?> rawClass, AnnotationInstance annotations) {
            return null;
        }

        @Override
        public JavaType constructType(JavaType baseType, AnnotatedClass<?> valueType, AnnotationInstance annotations) {
            return null;
        }

        @Override
        public JavaType constructType(JavaType baseType, AnnotatedGenericType type, AnnotationInstance annotations) {
            return null;
        }

        @Override
        public JavaType constructType(JavaType baseType, AnnotatedParameterizedType type, AnnotationInstance annotations) {
            return null;
        }

        @Override
        public JavaType constructType(JavaType baseType, AnnotatedType type, AnnotationInstance annotations) {
            return null;
        }

        @Override
        public JavaType constructType(JavaType baseType, JavaType type, AnnotationInstance annotations) {
            return null;
        }
    }
}