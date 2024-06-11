package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SettableAnyPropertyTest {

    @SuppressWarnings("unchecked")
    private static Object createTestObject(Class<?> clazz) throws InstantiationException, IllegalAccessException {
        return clazz.getDeclaredConstructor().newInstance();
    }

    @SuppressWarnings("rawtypes")
    private static AnnotatedMember getSetter(Class<?> clazz, final String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        AnnotatedMember setter = findSetter(clazz, field);
        return setter;
    }

    private static AnnotatedMember findSetter(Class<?> clazz, Field field) throws NoSuchFieldException, IllegalAccessException {
        if (field.getType().isPrimitive()) {
            throw new UnsupportedOperationException("Cannot test setter for primitive type: " + field.getType().getName());
        }
        Method setter = findSetterMethod(clazz, field);
        if (setter != null) {
            return new AnnotatedMethod(setter);
        }
        return null;
    }

    private static Method findSetterMethod(Class<?> clazz, Field field) throws NoSuchFieldException, IllegalAccessException {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("set") && method.getParameterTypes().length == 2
                    && method.getParameterTypes()[0].equals(field.getType())) {
                return method;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private static <T> JsonDeserializer<T> createTestDeserializer(Class<T> valueType) {
        return new StubJsonDeserializer<T>(valueType);
    }

    @SuppressWarnings("unchecked")
    private static TypeDeserializer createTestTypeDeserializer(Class<?> valueType) {
        return new StubTypeDeserializer<Object>(valueType);
    }

    @SuppressWarnings("unchecked")
    private static KeyDeserializer createTestKeyDeserializer() {
        return new StubKeyDeserializer();
    }

    private static class StubJsonDeserializer<T> extends JsonDeserializer<T> {
        private final Class<T> _valueType;

        public StubJsonDeserializer(Class<T> valueType) {
            this._valueType = valueType;
        }

        @Override
        public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return (T) p.getValueAsToken().getValueAsInt();
        }

        @Override
        public Object getNullValue(DeserializationContext ctxt) {
            return null;
        }

        @Override
        public JavaType getTargetType(DeserializationContext context) {
            return context.constructType(this._valueType);
        }

        @Override
        public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer vtd)
                throws IOException {
            return deserialize(p, ctxt);
        }
    }

    private static class StubTypeDeserializer<T> implements TypeDeserializer {
        private final Class<T> _valueType;

        public StubTypeDeserializer(Class<T> valueType) {
            this._valueType = valueType;
        }

        @Override
        public Object deserializeTypedFromObject(DeserializationContext ctxt, Object value, JavaType desiredType)
                throws IOException {
            return deserializeTypedFromObject(ctxt, value);
        }

        @Override
        public Object deserializeTypedFromObject(DeserializationContext ctxt, Object value)
                throws IOException {
            return value;
        }

        @Override
        public JavaType getTypeDeserializer(DeserializationContext ctxt, BeanProperty property, TypeDeserializer valueTypeDeserializer) {
            return valueTypeDeserializer;
        }

        @Override
        public JavaType getEmptyValue(DeserializationContext ctxt) {
            return null;
        }

        @Override
        public JavaType getValueType(DeserializationContext ctxt) {
            return this._valueType;
        }

        @Override
        public void resolveValueAndType(DeserializationContext ctxt, Object value, JavaType valueType, TypeDeserializer valueTypeDeserializer)
                throws IOException {
            // no-op
        }
    }

    private static class StubKeyDeserializer implements KeyDeserializer {
        @Override
        public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
            return key;
        }
    }

    @SuppressWarnings("rawtypes")
    private static void testDeserializeAndSet(Class<?> clazz, String fieldName, Object expectedValue)
            throws InstantiationException, IllegalAccessException, IOException {
        Object testObject = createTestObject(clazz);
        AnnotatedMember setter = getSetter(clazz, fieldName);
        SettableAnyProperty anyProperty = new SettableAnyProperty(
                new BeanProperty(null, new AnnotatedField(null, fieldName, null), null),
                setter,
                Object.class.getType(),
                createTestKeyDeserializer(),
                createTestDeserializer(expectedValue.getClass()),
                createTestTypeDeserializer(expectedValue.getClass()));
        JsonParser parser = new TestJsonParser();
        parser.setValue(expectedValue);

        anyProperty.deserializeAndSet(parser, new TestDeserializationContext(), testObject, fieldName);

        Object actualValue = fieldName.isEmpty()
                ? testObject
                : getValue(testObject, fieldName);

        assertEquals(expectedValue, actualValue);
    }

    private static Object getValue(Object object, String fieldName) {
        if (object instanceof Map) {
            return ((Map<?, ?>) object).get(fieldName);
        }
        try {
            return ((AnnotatedField) findField(object.getClass(), fieldName)).getValue(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Cannot access field: " + fieldName, e);
        }
    }

    private static AnnotatedMember findField(Class<?> clazz, String fieldName) {
        Field field = null;
        try {
            field = clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Cannot find field: " + fieldName, e);
        }
        return new AnnotatedField(findSetter(clazz, field), field);
    }

    @org.junit.Test
    public void testDeserializeAndSet_Object() throws Exception {
        // Test with Object type
        testDeserializeAndSet(TestSettableAnyProperty.class, "testProperty", new Integer(42));
    }

    @org.junit.Test
    public void testDeserializeAndSet_Map() throws Exception {
        // Test with Map type
        testDeserializeAndSet(TestSettableAnyPropertyMap.class, "testProperty", new Integer(42));
    }

    public static class TestSettableAnyProperty {
        private int testProperty;
    }

    public static class TestSettableAnyPropertyMap extends HashMap<String, Object> {
    }
}