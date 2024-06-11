package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

public class FieldPropertyTest {

    @SuppressWarnings("unchecked")
    private static <T> AnnotatedField<T> createTestAnnotatedField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return new AnnotatedField<>(new AnnotatedMember(field), Annotations.empty());
    }

    @SuppressWarnings("unchecked")
    private static <T> AnnotatedField<T> createTestAnnotatedFieldWithAnnotation(Class<?> clazz, String fieldName, Class<? extends Annotation> annotationType) throws NoSuchFieldException {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        Annotation annotation = mock(annotationType);
        when(annotation.annotationType()).thenReturn(annotationType);
        return new AnnotatedField<>(new AnnotatedMember(field), new Annotation[]{annotation});
    }

    @Test
    public void testConstructor() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedField<FieldPropertyTestBean> field = createTestAnnotatedField(FieldPropertyTestBean.class, "testField");

        FieldProperty property = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);

        assertSame(propDef, property.getBeanPropertyDefinition());
        assertSame(type, property.getType());
        assertSame(typeDeser, property.getTypeDeserializer());
        assertSame(contextAnnotations, property.getContextAnnotations());
        assertSame(field, property.getAnnotated());
        assertSame(field.getAnnotated(), property._annotated);
        assertSame(field.getAnnotated().getMember(), property._field);
    }

    @Test
    public void testConstructorWithValueDeserializer() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);
        JsonDeserializer<?> valueDeserializerWithType = mock(JsonDeserializer.class);
        NullValueProvider nva = mock(NullValueProvider.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedField<FieldPropertyTestBean> field = createTestAnnotatedField(FieldPropertyTestBean.class, "testField");

        FieldProperty property = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field, valueDeserializer);

        assertSame(propDef, property.getBeanPropertyDefinition());
        assertSame(type, property.getType());
        assertSame(typeDeser, property.getTypeDeserializer());
        assertSame(contextAnnotations, property.getContextAnnotations());
        assertSame(field, property.getAnnotated());
        assertSame(field.getAnnotated(), property._annotated);
        assertSame(field.getAnnotated().getMember(), property._field);
        assertSame(valueDeserializer, property._valueDeserializer);
    }

    @Test
    public void testConstructorWithNullValueProvider() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedField<FieldPropertyTestBean> field = createTestAnnotatedField(FieldPropertyTestBean.class, "testField");

        FieldProperty property = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field, nullValueProvider);

        assertSame(propDef, property.getBeanPropertyDefinition());
        assertSame(type, property.getType());
        assertSame(typeDeser, property.getTypeDeserializer());
        assertSame(contextAnnotations, property.getContextAnnotations());
        assertSame(field, property.getAnnotated());
        assertSame(field.getAnnotated(), property._annotated);
        assertSame(field.getAnnotated().getMember(), property._field);
        assertSame(nullValueProvider, property._nullProvider);
    }

    @Test
    public void testConstructorWithJsonDeserializerAndNullValueProvider() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);
        NullValueProvider nva = mock(NullValueProvider.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedField<FieldPropertyTestBean> field = createTestAnnotatedField(FieldPropertyTestBean.class, "testField");

        FieldProperty property = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field, valueDeserializer, nva);

        assertSame(propDef, property.getBeanPropertyDefinition());
        assertSame(type, property.getType());
        assertSame(typeDeser, property.getTypeDeserializer());
        assertSame(contextAnnotations, property.getContextAnnotations());
        assertSame(field, property.getAnnotated());
        assertSame(field.getAnnotated(), property._annotated);
        assertSame(field.getAnnotated().getMember(), property._field);
        assertSame(valueDeserializer, property._valueDeserializer);
        assertSame(nva, property._nullProvider);
    }

    @Test
    public void testConstructorWithPropertyName() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        PropertyName newName = mock(PropertyName.class);
        AnnotatedField<FieldPropertyTestBean> field = createTestAnnotatedField(FieldPropertyTestBean.class, "testField");

        FieldProperty property = new FieldProperty(this, propDef, type, typeDeser, contextAnnotations, newName, field);

        assertSame(propDef, property.getBeanPropertyDefinition());
        assertSame(type, property.getType());
        assertSame(typeDeser, property.getTypeDeserializer());
        assertSame(contextAnnotations, property.getContextAnnotations());
        assertSame(newName, property.getName());
        assertSame(field, property.getAnnotated());
        assertSame(field.getAnnotated(), property._annotated);
        assertSame(field.getAnnotated().getMember(), property._field);
    }

    @Test
    public void testConstructorWithJsonDeserializerAndPropertyName() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        PropertyName newName = mock(PropertyName.class);
        JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);
        AnnotatedField<FieldPropertyTestBean> field = createTestAnnotatedField(FieldPropertyTestBean.class, "testField");

        FieldProperty property = new FieldProperty(this, propDef, type, typeDeser, contextAnnotations, newName, field, valueDeserializer);

        assertSame(propDef, property.getBeanPropertyDefinition());
        assertSame(type, property.getType());
        assertSame(typeDeser, property.getTypeDeserializer());
        assertSame(contextAnnotations, property.getContextAnnotations());
        assertSame(newName, property.getName());
        assertSame(field, property.getAnnotated());
        assertSame(field.getAnnotated(), property._annotated);
        assertSame(field.getAnnotated().getMember(), property._field);
        assertSame(valueDeserializer, property._valueDeserializer);
    }

    @Test
    public void testConstructorWithJdkSerialization() throws IOException {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);
        JsonParser jsonParser = mock(JsonParser.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedField<FieldPropertyTestBean> field = createTestAnnotatedField(FieldPropertyTestBean.class, "testField");

        FieldProperty property = new FieldProperty(this, propDef, type, typeDeser, contextAnnotations, field);

        Object obj = new FieldProperty(property);

        assertSame(propDef, obj.getBeanPropertyDefinition());
        assertSame(type, obj.getType());
        assertSame(typeDeser, obj.getTypeDeserializer());
        assertSame(contextAnnotations, obj.getContextAnnotations());
        assertSame(field, obj.getAnnotated());
        assertSame(field.getAnnotated(), obj._annotated);
        Field fieldInstance = obj._field;
        assertSame(fieldInstance.getClass(), Field.class);
        assertSame(fieldInstance.getName(), "testField");
    }

    @Test
    public void testGetAnnotation() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        Class<? extends Annotation> annotationType = JsonProperty.class;
        AnnotatedField<FieldPropertyTestBean> field = createTestAnnotatedFieldWithAnnotation(FieldPropertyTestBean.class, "testField", annotationType);

        FieldProperty property = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);

        assertSame(annotationType, property.getAnnotation(annotationType));
    }

    @Test
    public void testGetAnnotationReturnsNull() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedField<FieldPropertyTestBean> field = createTestAnnotatedField(FieldPropertyTestBean.class, "testField");

        FieldProperty property = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);

        assertNull(property.getAnnotation(JsonProperty.class));
    }

    @Test
    public void testGetMember() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedField<FieldPropertyTestBean> field = createTestAnnotatedField(FieldPropertyTestBean.class, "testField");

        FieldProperty property = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);

        assertSame(field, property.getMember());
    }

    private static class FieldPropertyTestBean {
        @JsonProperty("testField")
        private String testField;
    }

}