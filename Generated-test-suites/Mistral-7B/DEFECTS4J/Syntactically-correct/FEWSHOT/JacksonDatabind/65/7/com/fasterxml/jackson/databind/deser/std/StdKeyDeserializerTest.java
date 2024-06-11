package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;

import java.io.IOException;
import java.lang.reflect.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;

public class StdKeyDeserializerTest {

    @Test
    public void testStringKD() throws Exception {
        StdKeyDeserializer serializableStringKD = StdKeyDeserializer.StringKD.forType(String.class);
        Object deserializedKey = serializableStringKD.deserializeKey("test", new TestContext());
        assertEquals("test", deserializedKey);
    }

    @Test
    public void testObjectKD() throws Exception {
        StdKeyDeserializer serializableObjectKD = StdKeyDeserializer.StringKD.forType(Object.class);
        Object deserializedKey = serializableObjectKD.deserializeKey("test", new TestContext());
        assertEquals("test", deserializedKey);
    }

    @Test
    public void testBooleanKD() throws Exception {
        StdKeyDeserializer booleanKD = StdKeyDeserializer.forType(Boolean.class);
        Object deserializedKey = booleanKD.deserializeKey("true", new TestContext());
        assertTrue((Boolean) deserializedKey);

        deserializedKey = booleanKD.deserializeKey("false", new TestContext());
        assertFalse((Boolean) deserializedKey);
    }

    @Test
    public void testByteKD() throws Exception {
        StdKeyDeserializer byteKD = StdKeyDeserializer.forType(Byte.class);
        Object deserializedKey = byteKD.deserializeKey("10", new TestContext());
        assertEquals((byte) 10, deserializedKey);
    }

    @Test
    public void testShortKD() throws Exception {
        StdKeyDeserializer shortKD = StdKeyDeserializer.forType(Short.class);
        Object deserializedKey = shortKD.deserializeKey("100", new TestContext());
        assertEquals((short) 100, deserializedKey);
    }

    @Test
    public void testCharKD() throws Exception {
        StdKeyDeserializer charKD = StdKeyDeserializer.forType(Character.class);
        Object deserializedKey = charKD.deserializeKey("A", new TestContext());
        assertEquals('A', deserializedKey);
    }

    @Test
    public void testIntKD() throws Exception {
        StdKeyDeserializer intKD = StdKeyDeserializer.forType(Integer.class);
        Object deserializedKey = intKD.deserializeKey("1000", new TestContext());
        assertEquals(1000, deserializedKey);
    }

    @Test
    public void testLongKD() throws Exception {
        StdKeyDeserializer longKD = StdKeyDeserializer.forType(Long.class);
        Object deserializedKey = longKD.deserializeKey("1000000", new TestContext());
        assertEquals(1000000L, deserializedKey);
    }

    @Test
    public void testFloatKD() throws Exception {
        StdKeyDeserializer floatKD = StdKeyDeserializer.forType(Float.class);
        Object deserializedKey = floatKD.deserializeKey("3.14", new TestContext());
        assertEquals(3.14f, deserializedKey);
    }

    @Test
    public void testDoubleKD() throws Exception {
        StdKeyDeserializer doubleKD = StdKeyDeserializer.forType(Double.class);
        Object deserializedKey = doubleKD.deserializeKey("3.14159265358979323846", new TestContext());
        assertEquals(3.14159265358979323846, deserializedKey);
    }

    @Test
    public void testLocaleKD() throws Exception {
        StdKeyDeserializer localeKD = StdKeyDeserializer.forType(Locale.class);
        Object deserializedKey = localeKD.deserializeKey("en_US", new TestContext());
        assertEquals(Locale.US, deserializedKey);
    }

    @Test
    public void testDateKD() throws Exception {
        StdKeyDeserializer dateKD = StdKeyDeserializer.forType(Date.class);
        Object deserializedKey = dateKD.deserializeKey("2023-03-01", new TestContext());
        assertNotNull(deserializedKey);
        assertTrue(deserializedKey instanceof Date);
    }

    @Test
    public void testCalendarKD() throws Exception {
        StdKeyDeserializer calendarKD = StdKeyDeserializer.forType(Calendar.class);
        Object deserializedKey = calendarKD.deserializeKey("2023-03-01", new TestContext());
        assertNotNull(deserializedKey);
        assertTrue(deserializedKey instanceof Calendar);
    }

    @Test
    public void testUUIDKD() throws Exception {
        StdKeyDeserializer uuidKD = StdKeyDeserializer.forType(UUID.class);
        Object deserializedKey = uuidKD.deserializeKey("4a6c1b9d-2e2e-4f3a-8a9b-60d1fc46e0a0", new TestContext());
        assertEquals(UUID.fromString("4a6c1b9d-2e2e-4f3a-8a9b-60d1fc46e0a0"), deserializedKey);
    }

    @Test
    public void testURikD() throws Exception {
        StdKeyDeserializer uriKD = StdKeyDeserializer.forType(URI.class);
        Object deserializedKey = uriKD.deserializeKey("http://example.com/test", new TestContext());
        assertEquals(new URI("http://example.com/test"), deserializedKey);
    }

    @Test
    public void testURLkD() throws Exception {
        StdKeyDeserializer urlKD = StdKeyDeserializer.forType(URL.class);
        Object deserializedKey = urlKD.deserializeKey("http://example.com/test", new TestContext());
        assertEquals(new URL("http://example.com/test"), deserializedKey);
    }

    @Test
    public void testClassKD() throws Exception {
        Class<?> testClass = String[].class;
        StdKeyDeserializer classKD = StdKeyDeserializer.forType(testClass);
        Object deserializedKey = classKD.deserializeKey("class [Ljava.lang.String;", new TestContext());
        assertEquals(testClass, deserializedKey);
    }

    @Test
    public void testEnumKD() throws Exception {
        Enum testEnum = Enum.valueOf(TestEnum.class, "TEST_VALUE");
        AnnotatedClass<TestEnum> annotatedClass = new TestContext()._annotationIntrospector().findAnnotation(TestEnum.class);
        AnnotatedField<TestEnum, TestEnum> annotatedField = annotatedClass.findMember(TestEnum.TEST_VALUE.getName());
        StdKeyDeserializer enumKD = new StdKeyDeserializer(new EnumResolver(TestEnum.class, annotatedField), null);
        Object deserializedKey = enumKD.deserializeKey("TEST_VALUE", new TestContext());
        assertEquals(testEnum, deserializedKey);
    }

    @Test
    public void testEnumToStringKD() throws Exception {
        Enum testEnum = Enum.valueOf(TestEnum.class, "TEST_VALUE");
        AnnotatedClass<TestEnum> annotatedClass = new TestContext()._annotationIntrospector().findAnnotation(TestEnum.class);
        StdKeyDeserializer enumKD = new StdKeyDeserializer(null, null);
        Object deserializedKey = enumKD.deserializeKey(testEnum.name(), new TestContext());
        assertEquals(testEnum, deserializedKey);
    }

    @Test(expected = IllegalStateException.class)
    public void testUnknownKeyType() throws Exception {
        StdKeyDeserializer unknownKeyType = new StdKeyDeserializer(-1, new Object(){}.getClass());
        unknownKeyType.deserializeKey("test", new TestContext());
    }

    private class TestContext extends DeserializationContext {
        private final AnnotationIntrospector _annotationIntrospector = new TestAnnotationIntrospector();

        TestContext() {
            super(new TestObjectMapper(), null, null, new TestObjectFactory(), null, new TestObjectFactory(), null);
        }

        @Override
        public AnnotationIntrospector getAnnotationIntrospector() {
            return _annotationIntrospector;
        }
    }

    private static class TestAnnotationIntrospector extends StdAnnotationIntrospector {
        @Override
        public AnnotatedMethod findMethod(Class<?> clazz, String methodName, Method method) {
            return super.findMethod(clazz, methodName, method);
        }
    }
}

class TestObjectMapper extends ObjectMapper {
    TestObjectMapper() {
        configure(MapperFeature.AUTO_DETECT_FIELDS, true);
        configure(MapperFeature.AUTO_DETECT_GETTERS, true);
    }
}

class TestObjectFactory implements ObjectFactory {
    @Override
    public SimpleValueFactory findValueFactory(Class<?> valueType) {
        return null;
    }

    @Override
    public ValueFactory findValueFactory(String valueTypeName) {
        return null;
    }

    @Override
    public Object createInstance(Class<?> beanType) throws InstantiationException, IllegalAccessException {
        return null;
    }

    @Override
    public Object createValueInstance(Class<?> valueType) throws InstantiationException, IllegalAccessException {
        return null;
    }

    @Override
    public AnnotatedClass<?> findClassAnnotations(Class<?> clazz) {
        return null;
    }

    @Override
    public AnnotatedMethod findMethodAnnotations(Class<?> clazz, Method method) {
        return null;
    }

    @Override
    public AnnotatedField<?> findFieldAnnotations(Class<?> clazz, String fieldName) {
        return null;
    }
}

class TestEnum {
    public static final TestEnum TEST_VALUE = new TestEnum("TEST_VALUE");

    private final String name;

    TestEnum(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}