package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * This concrete sub-class implements property that is set
 * directly assigning to a Field.
 */
public final class FieldPropertyTest {

    private static final String FIELD_NAME = "testField";
    private static final Class<Integer> FIELD_TYPE = Integer.class;

    @Test
    public void testConstructor() {
        AnnotatedField annotatedField = createAnnotatedField();
        FieldProperty fieldProperty = new FieldProperty(
                new BeanPropertyDefinition(FIELD_NAME, FIELD_TYPE, null),
                ObjectType.Standard.INT_TYPE, null, annotatedField);
        assertNotNull(fieldProperty);
        assertEquals(FIELD_NAME, fieldProperty.getName());
        assertEquals(FIELD_TYPE, fieldProperty.getType());
        assertEquals(annotatedField, fieldProperty.getMember());
    }

    @Test
    public void testWithName() {
        FieldProperty fieldProperty = new FieldProperty(
                new BeanPropertyDefinition(FIELD_NAME, FIELD_TYPE, null),
                ObjectType.Standard.INT_TYPE, null, null);
        PropertyName newName = PropertyName.construct("newName");
        FieldProperty newFieldProperty = fieldProperty.withName(newName);
        assertEquals(newName.getSimpleName(), newFieldProperty.getName());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<Integer> deserializer = createJsonDeserializer();
        FieldProperty fieldProperty = new FieldProperty(
                new BeanPropertyDefinition(FIELD_NAME, FIELD_TYPE, null),
                ObjectType.Standard.INT_TYPE, null, null);
        NullValueProvider nullValueProvider = createNullValueProvider();
        FieldProperty newFieldProperty = fieldProperty.withValueDeserializer(deserializer)
                .withNullProvider(nullValueProvider);
        assertEquals(deserializer, newFieldProperty.getValueDeserializer());
        assertEquals(nullValueProvider, newFieldProperty.getNullProvider());
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nullValueProvider = createNullValueProvider();
        FieldProperty fieldProperty = new FieldProperty(
                new BeanPropertyDefinition(FIELD_NAME, FIELD_TYPE, null),
                ObjectType.Standard.INT_TYPE, null, null);
        FieldProperty newFieldProperty = fieldProperty.withNullProvider(nullValueProvider);
        assertEquals(nullValueProvider, newFieldProperty.getNullProvider());
    }

    @Test
    public void testFixAccess() {
        FieldProperty fieldProperty = new FieldProperty(
                new BeanPropertyDefinition(FIELD_NAME, FIELD_TYPE, null),
                ObjectType.Standard.INT_TYPE, null, null);
        DeserializationConfig config = createDeserializationConfig();
        fieldProperty.fixAccess(config);
        assertTrue(config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
    }

    @Test
    public void testGetAnnotation() {
        Annotation annotation = createAnnotation();
        AnnotatedField annotatedField = createAnnotatedField();
        AnnotatedMember member = annotatedField;
        FieldProperty fieldProperty = new FieldProperty(
                new BeanPropertyDefinition(FIELD_NAME, FIELD_TYPE, null),
                ObjectType.Standard.INT_TYPE, null, annotatedField);
        assertEquals(annotation, fieldProperty.getAnnotation(annotation.getClass()));
        assertEquals(member, fieldProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object instance = new Object();
        JsonParser parser = createParser();
        Integer expectedValue = 42;
        FieldProperty fieldProperty = new FieldProperty(
                new BeanPropertyDefinition(FIELD_NAME, FIELD_TYPE, null),
                ObjectType.Standard.INT_TYPE, null, null);
        parser.setValue(expectedValue.toString());
        fieldProperty.deserializeAndSet(parser, createContext(), instance);
        assertEquals(expectedValue, fieldProperty.getMember().getAnnotated().get(instance));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object instance = new Object();
        JsonParser parser = createParser();
        Integer expectedValue = 42;
        FieldProperty fieldProperty = new FieldProperty(
                new BeanPropertyDefinition(FIELD_NAME, FIELD_TYPE, null),
                ObjectType.Standard.INT_TYPE, null, null);
        parser.setValue(expectedValue.toString());
        Object result = fieldProperty.deserializeSetAndReturn(parser, createContext(), instance);
        assertEquals(instance, result);
        assertEquals(expectedValue, fieldProperty.getMember().getAnnotated().get(instance));
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Integer value = 42;
        FieldProperty fieldProperty = new FieldProperty(
                new BeanPropertyDefinition(FIELD_NAME, FIELD_TYPE, null),
                ObjectType.Standard.INT_TYPE, null, null);
        fieldProperty.set(instance, value);
        assertEquals(value, fieldProperty.getMember().getAnnotated().get(instance));
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Integer value = 42;
        FieldProperty fieldProperty = new FieldProperty(
                new BeanPropertyDefinition(FIELD_NAME, FIELD_TYPE, null),
                ObjectType.Standard.INT_TYPE, null, null);
        Object result = fieldProperty.setAndReturn(instance, value);
        assertEquals(instance, result);
        assertEquals(value, fieldProperty.getMember().getAnnotated().get(instance));
    }

    @Test
    public void testReadResolve() {
        FieldProperty fieldProperty = new FieldProperty(this);
        FieldProperty newFieldProperty = fieldProperty.readResolve();
        assertEquals(fieldProperty, newFieldProperty);
    }

    private JsonDeserializer<Integer> createJsonDeserializer() {
        return new JsonDeserializer<Integer>() {
            @Override
            public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getValueAsInt();
            }
        };
    }

    private NullValueProvider createNullValueProvider() {
        return new NullValueProvider() {
            @Override
            public Object getNullValue(DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
    }

    private DeserializationConfig createDeserializationConfig() {
        return new DeserializationConfig();
    }

    private Annotation createAnnotation() {
        return new MyAnnotation();
    }

    private AnnotatedField createAnnotatedField() {
        Field field = createTestField();
        return new AnnotatedField(field, new MyAnnotation());
    }

    private Object createTestObject() {
        return new Object();
    }

    private Field createTestField() {
        try {
            Field field = this.getClass().getDeclaredField("testObject");
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private JsonParser createParser() {
        return createContext().treeAsTokens(new StringReader("{\"name\":42}"));
    }

    private Object newInstance(Class<?> clazz) throws InstantiationException, IllegalAccessException {
        return clazz.newInstance();
    }

    private DeserializationContext createContext() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getDeserializationContext();
    }

    private static class MyAnnotation implements Annotation {
        @Override
        public Class<? extends Annotation> annotationType() {
            return null;
        }

        @Override
        public String toString() {
            return "MyAnnotation";
        }
    }
}