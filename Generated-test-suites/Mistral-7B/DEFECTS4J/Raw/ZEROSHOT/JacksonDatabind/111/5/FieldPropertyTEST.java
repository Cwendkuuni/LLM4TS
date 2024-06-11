package com.example.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Field;

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

/**
 * Test class for {@link FieldProperty}
 */
public class FieldPropertyTest {

    private static final String FIELD_NAME = "testField";
    private static final Class<?> FIELD_CLASS = String.class;

    @org.junit.Test
    public void testConstructor() throws Exception {
        Field field = new Field("testField", String.class, null);
        AnnotatedField annotatedField = mock(AnnotatedField.class);
        when(annotatedField.getAnnotated()).thenReturn(field);

        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        ObjectType type = mock(ObjectType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);

        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, annotatedField);

        assertEquals(propDef, fieldProperty.getPropertyDefinition());
        assertEquals(type, fieldProperty.getType());
        assertNull(fieldProperty.getValueDeserializer());
        assertNull(fieldProperty.getNullProvider());
        assertEquals(field, fieldProperty.getField());
        assertEquals(annotatedField, fieldProperty.getAnnotated());
        assertEquals(false, fieldProperty.isSkipNulls());
    }

    @org.junit.Test
    public void testWithName() throws Exception {
        FieldProperty fieldProperty = new FieldProperty(null, null);
        PropertyName newName = mock(PropertyName.class);

        FieldProperty newFieldProperty = fieldProperty.withName(newName);

        assertEquals(fieldProperty, newFieldProperty);
        assertEquals(newName, newFieldProperty.getName());
    }

    @org.junit.Test
    public void testWithValueDeserializer() throws Exception {
        FieldProperty fieldProperty = new FieldProperty(null, null);
        JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);

        FieldProperty newFieldProperty = fieldProperty.withValueDeserializer(deserializer)
                .withNullProvider(nullValueProvider);

        assertEquals(fieldProperty, newFieldProperty);
        assertEquals(deserializer, newFieldProperty.getValueDeserializer());
        assertEquals(nullValueProvider, newFieldProperty.getNullProvider());
    }

    @org.junit.Test
    public void testWithNullProvider() throws Exception {
        FieldProperty fieldProperty = new FieldProperty(null, null);
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);

        FieldProperty newFieldProperty = fieldProperty.withNullProvider(nullValueProvider);

        assertEquals(fieldProperty, newFieldProperty);
        assertEquals(nullValueProvider, newFieldProperty.getNullProvider());
    }

    @org.junit.Test
    public void testFixAccess() throws Exception {
        Field field = new Field("testField", String.class, null);
        AnnotatedField annotatedField = mock(AnnotatedField.class);
        when(annotatedField.getAnnotated()).thenReturn(field);

        DeserializationConfig config = mock(DeserializationConfig.class);
        when(config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS)).thenReturn(true);

        FieldProperty fieldProperty = new FieldProperty(null, null, null, null, annotatedField);
        fieldProperty.fixAccess(config);

        // Access should be public after fixAccess
        field.setAccessible(true);
    }

    @org.junit.Test
    public void testDeserializeAndSet() throws Exception {
        Field field = new Field("testField", String.class, null);
        AnnotatedField annotatedField = mock(AnnotatedField.class);
        when(annotatedField.getAnnotated()).thenReturn(field);

        Object instance = new Object();
        String expectedValue = "expectedValue";

        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        ObjectType type = mock(ObjectType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        JsonParser parser = mock(JsonParser.class);
        when(parser.getCurrentName()).thenReturn(FIELD_NAME);
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.getValueAsString()).thenReturn(expectedValue);

        NullValueProvider nullValueProvider = mock(NullValueProvider.class);

        when(propDef.getBeanClass()).thenReturn(instance.getClass());
        when(propDef.getName()).thenReturn(FIELD_NAME);
        when(propDef.getType()).thenReturn(FIELD_CLASS);

        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, annotatedField);

        fieldProperty.deserializeAndSet(parser, null, instance);

        assertEquals(expectedValue, field.get(instance));
    }

    @org.junit.Test
    public void testDeserializeSetAndReturn() throws Exception {
        Field field = new Field("testField", String.class, null);
        AnnotatedField annotatedField = mock(AnnotatedField.class);
        when(annotatedField.getAnnotated()).thenReturn(field);

        Object instance = new Object();
        String expectedValue = "expectedValue";

        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        ObjectType type = mock(ObjectType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        JsonParser parser = mock(JsonParser.class);
        when(parser.getCurrentName()).thenReturn(FIELD_NAME);
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.getValueAsString()).thenReturn(expectedValue);

        NullValueProvider nullValueProvider = mock(NullValueProvider.class);

        when(propDef.getBeanClass()).thenReturn(instance.getClass());
        when(propDef.getName()).thenReturn(FIELD_NAME);
        when(propDef.getType()).thenReturn(FIELD_CLASS);

        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, annotatedField);

        Object result = fieldProperty.deserializeSetAndReturn(parser, null, instance);

        assertEquals(instance, result);
        assertEquals(expectedValue, field.get(instance));
    }

    @org.junit.Test
    public void testSet() throws Exception {
        Field field = new Field("testField", String.class, null);
        Object instance = new Object();
        String value = "value";

        FieldProperty fieldProperty = new FieldProperty(null, null);

        fieldProperty.set(instance, value);

        assertEquals(value, field.get(instance));
    }

    @org.junit.Test
    public void testSetAndReturn() throws Exception {
        Field field = new Field("testField", String.class, null);
        Object instance = new Object();
        String value = "value";

        FieldProperty fieldProperty = new FieldProperty(null, null);

        Object result = fieldProperty.setAndReturn(instance, value);

        assertEquals(instance, result);
        assertEquals(value, field.get(instance));
    }

    @org.junit.Test
    public void testReadResolve() throws Exception {
        Field field = new Field("testField", String.class, null);
        FieldProperty fieldProperty = new FieldProperty(null);

        FieldProperty newFieldProperty = (FieldProperty) fieldProperty.readResolve();

        assertEquals(fieldProperty, newFieldProperty);
        assertEquals(field, newFieldProperty.getField());
    }
}