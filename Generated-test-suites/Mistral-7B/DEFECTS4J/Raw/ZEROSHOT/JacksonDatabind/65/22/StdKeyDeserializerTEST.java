package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;

/**
 * Unit tests for {@link StdKeyDeserializer}.
 */
public class StdKeyDeserializerTest {

    @Test
    public void testBooleanKeyDeserializer() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Boolean.class);
        Object deserializedKey = serializer.deserializeKey("true", new TestContext());
        assertTrue((Boolean) deserializedKey);

        deserializedKey = serializer.deserializeKey("false", new TestContext());
        assertFalse((Boolean) deserializedKey);

        deserializedKey = serializer.deserializeKey("invalid", new TestContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testByteKeyDeserializer() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Byte.class);
        Object deserializedKey = serializer.deserializeKey("0", new TestContext());
        assertEquals((Byte) 0, deserializedKey);

        deserializedKey = serializer.deserializeKey("127", new TestContext());
        assertEquals((Byte) 127, deserializedKey);

        deserializedKey = serializer.deserializeKey("128", new TestContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testShortKeyDeserializer() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Short.class);
        Object deserializedKey = serializer.deserializeKey("0", new TestContext());
        assertEquals((Short) 0, deserializedKey);

        deserializedKey = serializer.deserializeKey("32767", new TestContext());
        assertEquals((Short) 32767, deserializedKey);

        deserializedKey = serializer.deserializeKey("32768", new TestContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testCharKeyDeserializer() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Character.class);
        Object deserializedKey = serializer.deserializeKey("a", new TestContext());
        assertEquals((Character) 'a', deserializedKey);

        deserializedKey = serializer.deserializeKey("z", new TestContext());
        assertEquals((Character) 'z', deserializedKey);

        deserializedKey = serializer.deserializeKey(" invalid", new TestContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testIntKeyDeserializer() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Integer.class);
        Object deserializedKey = serializer.deserializeKey("0", new TestContext());
        assertEquals((Integer) 0, deserializedKey);

        deserializedKey = serializer.deserializeKey("2147483647", new TestContext());
        assertEquals((Integer) 2147483647, deserializedKey);

        deserializedKey = serializer.deserializeKey("2147483648", new TestContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testLongKeyDeserializer() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Long.class);
        Object deserializedKey = serializer.deserializeKey("0", new TestContext());
        assertEquals((Long) 0L, deserializedKey);

        deserializedKey = serializer.deserializeKey("9223372036854775807", new TestContext());
        assertEquals((Long) 9223372036854775807L, deserializedKey);

        deserializedKey = serializer.deserializeKey("9223372036854775808", new TestContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testFloatKeyDeserializer() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Float.class);
        Object deserializedKey = serializer.deserializeKey("0.0", new TestContext());
        assertEquals((Float) 0.0f, deserializedKey);

        deserializedKey = serializer.deserializeKey("3.14159", new TestContext());
        assertEquals((Float) 3.14159f, deserializedKey);

        deserializedKey = serializer.deserializeKey("invalid", new TestContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testDoubleKeyDeserializer() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Double.class);
        Object deserializedKey = serializer.deserializeKey("0.0", new TestContext());
        assertEquals((Double) 0.0, deserializedKey);

        deserializedKey = serializer.deserializeKey("6.28318530718", new TestContext());
        assertEquals((Double) 6.28318530718, deserializedKey);

        deserializedKey = serializer.deserializeKey("invalid", new TestContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testLocaleKeyDeserializer() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Locale.class);
        Object deserializedKey = serializer.deserializeKey("en_US", new TestContext());
        assertEquals(Locale.US, deserializedKey);

        deserializedKey = serializer.deserializeKey("fr_FR", new TestContext());
        assertEquals(Locale.FRANCE, deserializedKey);

        deserializedKey = serializer.deserializeKey("invalid", new TestContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testDateKeyDeserializer() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Date.class);
        Object deserializedKey = serializer.deserializeKey("2018-12-12", new TestContext());
        assertNotNull(deserializedKey);

        Date date = (Date) deserializedKey;
        assertEquals(2018, date.getYear());
        assertEquals(12, date.getMonth());
        assertEquals(12, date.getDate());

        deserializedKey = serializer.deserializeKey("invalid", new TestContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testCalendarKeyDeserializer() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Calendar.class);
        Object deserializedKey = serializer.deserializeKey("2018-12-12", new TestContext());
        assertNotNull(deserializedKey);

        Calendar calendar = (Calendar) deserializedKey;
        assertEquals(2018, calendar.get(Calendar.YEAR));
        assertEquals(12, calendar.get(Calendar.MONTH));
        assertEquals(12, calendar.get(Calendar.DATE));

        deserializedKey = serializer.deserializeKey("invalid", new TestContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testUUIDKeyDeserializer() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(UUID.class);
        Object deserializedKey = serializer.deserializeKey("123e4567-e89b-12d3-a456-426614174000", new TestContext());
        assertEquals(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), deserializedKey);

        deserializedKey = serializer.deserializeKey("invalid", new TestContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testURIKeyDeserializer() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URI.class);
        Object deserializedKey = serializer.deserializeKey("http://example.com", new TestContext());
        assertEquals(new URI("http://example.com"), deserializedKey);

        deserializedKey = serializer.deserializeKey("invalid", new TestContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testURLKeyDeserializer() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URL.class);
        Object deserializedKey = serializer.deserializeKey("http://example.com", new TestContext());
        assertEquals(new URL("http://example.com"), deserializedKey);

        deserializedKey = serializer.deserializeKey("invalid", new TestContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testStringKeyDeserializer() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(String.class);
        Object deserializedKey = serializer.deserializeKey("key", new TestContext());
        assertEquals("key", deserializedKey);
    }

    @Test
    public void testClassKeyDeserializer() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(String.class);
        Class<?> clazz = Class.forName("java.lang.String");
        Object deserializedKey = serializer.deserializeKey(clazz.getName(), new TestContext());
        assertEquals(clazz, deserializedKey);

        deserializedKey = serializer.deserializeKey("invalid", new TestContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testEnumKeyDeserializer() throws Exception {
        EnumResolver enumResolver = new EnumResolver() {
            @Override
            public Enum<?> findEnum(Object key) {
                if (key instanceof String) {
                    String name = (String) key;
                    if (name.equals("ONE")) {
                        return EnumOne.ONE;
                    } else if (name.equals("TWO")) {
                        return EnumTwo.TWO;
                    }
                }
                return null;
            }
        };

        StdKeyDeserializer serializer = new EnumKD(enumResolver, null);
        Object deserializedKey = serializer.deserializeKey("ONE", new TestContext());
        assertEquals(EnumOne.ONE, deserializedKey);

        deserializedKey = serializer.deserializeKey("TWO", new TestContext());
        assertEquals(EnumTwo.TWO, deserializedKey);

        deserializedKey = serializer.deserializeKey("THREE", new TestContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testStringFactoryKeyDeserializer() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(String.class);
        AnnotatedClass annotatedClass = new AnnotatedClass(StringFactory.class);
        AnnotatedMethod annotatedMethod = annotatedClass.findMethod(StringFactory.class.getMethod("fromString"));
        Object deserializedKey = serializer.deserializeKey(annotatedMethod, new TestContext());
        assertEquals(StringFactory.class, deserializedKey);
    }

    @Test
    public void testStringCtorKeyDeserializer() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(String.class);
        AnnotatedClass annotatedClass = new AnnotatedClass(StringWithCtor.class);
        AnnotatedConstructor annotatedConstructor = annotatedClass.findConstructor(StringWithCtor.class.getConstructor(String.class));
        Object deserializedKey = serializer.deserializeKey("test", new TestContext());
        assertEquals("test", deserializedKey);
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public JavaType getInputType() {
            return null;
        }

        @Override
        public ObjectMapper getConfig() {
            return null;
        }

        @Override
        public JsonDeserializer<?> getDeserializerFactory() {
            return null;
        }

        @Override
        public AnnotationIntrospector getAnnotationIntrospector() {
            return null;
        }

        @Override
        public Object getContextualData() {
            return null;
        }

        @Override
        public DeserializationFeature getConfig(DeserializationFeature feature) {
            return null;
        }

        @Override
        public void findAndRegisterAlternatives(AnnotatedClass ac, AnnotationIntrospector ai) throws JsonMappingException {
        }

        @Override
        public void findAndRegisterAlternatives(AnnotatedMethod am, AnnotationIntrospector ai) throws JsonMappingException {
        }

        @Override
        public void findAndRegisterAlternatives(AnnotatedField af, AnnotationIntrospector ai) throws JsonMappingException {
        }

        @Override
        public void findAndRegisterAlternatives(AnnotatedParameter ap, AnnotationIntrospector ai) throws JsonMappingException {
        }

        @Override
        public void reportError(DeserializationContext ctxt, JsonProcessingException e) throws JsonProcessingException {
        }

        @Override
        public void reportError(Throwable t) throws JsonProcessingException {
        }
    }

    @JsonValue
    static class EnumOne {
        public static final EnumOne ONE = new EnumOne();
    }

    static class EnumTwo {
        public static final EnumTwo TWO = new EnumTwo();
    }

    static class StringFactory {
        public static String fromString(String s) {
            return s;
        }
    }

    static class StringWithCtor {
        @JsonCreator
        public StringWithCtor(String s) {
            this.s = s;
        }

        private String s;
    }
}