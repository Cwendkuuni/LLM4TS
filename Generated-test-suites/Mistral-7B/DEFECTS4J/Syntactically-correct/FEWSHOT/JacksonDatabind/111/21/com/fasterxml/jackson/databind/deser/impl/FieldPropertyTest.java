package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Field;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.ObjectIdGenerator;

import org.junit.Before;
import org.junit.Test;

public class FieldPropertyTest {

    private ObjectIdGenerator idGenerator;
    private AnnotatedField annotatedField;
    private Field field;
    private ObjectInstanceFactory instanceFactory;
    private DeserializationConfig config;
    private JsonParser jsonParser;

    @Before
    public void setup() {
        idGenerator = mock(ObjectIdGenerator.class);
        annotatedField = mock(AnnotatedField.class);
        when(annotatedField.getAnnotated()).thenReturn(field);
        field = mock(Field.class);
        instanceFactory = mock(ObjectInstanceFactory.class);
        config = mock(DeserializationConfig.class);
        jsonParser = mock(JsonParser.class);
    }

    @Test
    public void testFieldPropertyConstruction() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);

        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, annotatedField);

        assertEquals(propDef, fieldProperty.getBeanPropertyDefinition());
        assertEquals(type, fieldProperty.getType());
        assertEquals(typeDeser, fieldProperty.getTypeDeserializer());
        assertEquals(contextAnnotations, fieldProperty.getContextAnnotations());
        assertEquals(annotatedField, fieldProperty.getAnnotatedField());
        assertEquals(field, fieldProperty.getField());
        assertFalse(fieldProperty.isTransient());
    }

    @Test
    public void testFieldPropertyWithName() {
        PropertyName newName = mock(PropertyName.class);
        FieldProperty fieldProperty = new FieldProperty(this.fieldProperty, newName);

        assertEquals(newName, fieldProperty.getName());
    }

    @Test
    public void testFieldPropertyWithValueDeserializer() {
        JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);

        FieldProperty fieldProperty = new FieldProperty(this.fieldProperty, valueDeserializer, nullValueProvider);

        assertEquals(valueDeserializer, fieldProperty.getValueDeserializer());
        assertEquals(nullValueProvider, fieldProperty.getNullValueProvider());
    }

    @Test
    public void testFieldPropertyWithNullProvider() {
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);

        FieldProperty fieldProperty = new FieldProperty(this.fieldProperty, null, nullValueProvider);

        assertEquals(nullValueProvider, fieldProperty.getNullValueProvider());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object instance = mock(Object.class);
        JsonToken jsonToken = JsonToken.VALUE_STRING;
        String value = "testValue";

        when(jsonParser.getCurrentName()).thenReturn("testProperty");
        when(jsonParser.hasToken(JsonToken.FIELD_NAME)).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(jsonToken);
        when(jsonParser.getValueAsString()).thenReturn(value);

        Object result = fieldProperty.deserializeAndSet(jsonParser, config, instance);

        verify(field).set(instance, value);
        assertEquals(instance, result);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object instance = mock(Object.class);
        JsonToken jsonToken = JsonToken.VALUE_STRING;
        String value = "testValue";

        when(jsonParser.getCurrentName()).thenReturn("testProperty");
        when(jsonParser.hasToken(JsonToken.FIELD_NAME)).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(jsonToken);
        when(jsonParser.getValueAsString()).thenReturn(value);

        Object result = fieldProperty.deserializeSetAndReturn(jsonParser, config, instance);

        verify(field).set(instance, value);
        assertEquals(instance, result);
    }

    @Test
    public void testSet() throws IOException {
        Object instance = mock(Object.class);
        Object value = "testValue";

        fieldProperty.set(instance, value);

        verify(field).set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = mock(Object.class);
        Object value = "testValue";

        Object result = fieldProperty.setAndReturn(instance, value);

        verify(field).set(instance, value);
        assertEquals(instance, result);
    }

    @Test
    public void testFixAccess() throws IOException {
        fieldProperty.fixAccess(config);

        verify(field).setAccessible(true);
    }

    @Test
    public void testGetAnnotation() {
        Class<Annotation> annotationType = mock(Annotation.class);
        Annotation annotation = mock(Annotation.class);

        when(annotatedField.getAnnotation(annotationType)).thenReturn(annotation);

        assertEquals(annotation, fieldProperty.getAnnotation(annotationType));
    }

    @Test
    public void testGetMember() {
        assertEquals(annotatedField, fieldProperty.getMember());
    }

    @Test
    public void testReadResolve() throws IOException {
        Object result = fieldProperty.readResolve();

        assertEquals(fieldProperty, result);
    }
}