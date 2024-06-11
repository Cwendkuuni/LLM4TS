package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Test;

public class FieldPropertyTest {

    @Test
    public void testConstructor() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedField field = mock(AnnotatedField.class);

        FieldProperty property = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);

        assertNotNull(property);
        assertNotNull(property._annotated);
        assertNotNull(property._field);
        assertNull(property._skipNulls);
    }

    @Test
    public void testConstructorsWithNullValueProvider() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedField field = mock(AnnotatedField.class);
        NullValueProvider nva = mock(NullValueProvider.class);

        FieldProperty property = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field, nva);

        assertNotNull(property);
        assertNotNull(property._annotated);
        assertNotNull(property._field);
        assertTrue(property._skipNulls = NullsConstantProvider.isSkipper(nva));
    }

    @Test
    public void testSettableBeanPropertyMethods() throws IOException {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedField field = mock(AnnotatedField.class);
        Object instance = new Object();
        Object value = new Object();
        JsonParser parser = mock(JsonParser.class);

        when(parser.getCurrentName()).thenReturn("testField");
        when(parser.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(parser.nextToken()).thenReturn(JsonToken.START_OBJECT);
        when(parser.nextToken()).thenReturn(JsonToken.END_OBJECT);
        when(parser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        when(parser.getText()).thenReturn("testField");

        FieldProperty property = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);

        Object result = property.deserializeAndSet(parser, null, instance);
        assertNotNull(result);
        assertEquals(instance, result);

        property.set(instance, value);
        Object result2 = property.deserializeAndSet(parser, null, instance);
        assertEquals(value, result2);

        Object result3 = property.deserializeSetAndReturn(parser, null, instance);
        assertNotNull(result3);
        assertEquals(instance, result3);
    }

    @Test
    public void testNullValueProvider() throws IOException {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedField field = mock(AnnotatedField.class);
        NullValueProvider nva = mock(NullValueProvider.class);
        Object instance = new Object();
        Object value = new Object();
        JsonParser parser = mock(JsonParser.class);

        when(parser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);

        FieldProperty property = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field, nva);

        Object result = property.deserializeAndSet(parser, null, instance);
        assertNull(result);

        property.set(instance, value);
        Object result2 = property.deserializeAndSet(parser, null, instance);
        assertNull(result2);

        Object result3 = property.deserializeSetAndReturn(parser, null, instance);
        assertNull(result3);
    }

    @Test
    public void testFixAccess() throws IOException {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedField field = mock(AnnotatedField.class);
        Object instance = new Object();
        Field fieldAnnotation = mock(Field.class);

        when(field.getAnnotated()).thenReturn(fieldAnnotation);
        when(fieldAnnotation.getAnnotation(JsonProperty.class)).thenReturn(new JsonProperty());

        FieldProperty property = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
        property.fixAccess(null);

        assertNotNull(fieldAnnotation.getAccessible(true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFixAccessWithNullField() throws IOException {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedField field = mock(AnnotatedField.class);
        Object instance = new Object();

        FieldProperty property = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
        property.fixAccess(null);
    }
}