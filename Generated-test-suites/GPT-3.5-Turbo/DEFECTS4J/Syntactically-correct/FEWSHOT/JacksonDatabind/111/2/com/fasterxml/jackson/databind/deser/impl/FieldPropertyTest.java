package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Annotations;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class FieldPropertyTest {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testConstructor() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(null, null, null,
                TypeFactory.defaultInstance().constructType(String.class), null, null);
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);
        TypeDeserializer typeDeserializer = null;
        Annotations contextAnnotations = null;
        AnnotatedField field = new AnnotatedField(null, null, null);

        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeserializer, contextAnnotations, field);

        assertNotNull(fieldProperty);
        assertEquals(String.class, fieldProperty.getType().getRawClass());
        assertNull(fieldProperty.getName());
        assertNull(fieldProperty.getValueDeserializer());
        assertNull(fieldProperty.getNullProvider());
        assertNull(fieldProperty.getMember());
    }

    @Test
    public void testSetterAndGetters() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(null, null, null,
                TypeFactory.defaultInstance().constructType(String.class), null, null);
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);
        TypeDeserializer typeDeserializer = null;
        Annotations contextAnnotations = null;
        AnnotatedField field = new AnnotatedField(null, null, null);

        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeserializer, contextAnnotations, field);

        PropertyName newName = new PropertyName("testName");
        JsonDeserializer<?> deser = null;
        NullValueProvider nva = null;
        FieldProperty modifiedFieldProperty = fieldProperty
                .withName(newName)
                .withValueDeserializer(deser)
                .withNullProvider(nva);

        assertEquals(newName, modifiedFieldProperty.getName());
        assertNull(modifiedFieldProperty.getValueDeserializer());
        assertNull(modifiedFieldProperty.getNullProvider());
    }

    @Test
    public void testDeserializeAndSetWithNullValue() throws IOException {
        JsonParser p = MAPPER.getFactory().createParser("null");
        DeserializationContext ctxt = MAPPER.getDeserializationContext().createInstance(MAPPER.getDeserializationConfig(), p, 1);

        FieldProperty fieldProperty = getFieldProperty();
        Object instance = new Object();

        fieldProperty.deserializeAndSet(p, ctxt, instance);

        Field field = fieldProperty._field;
        field.setAccessible(true);
        Object value = field.get(instance);

        assertNull(value);
    }

    private FieldProperty getFieldProperty() throws NoSuchFieldException {
        Field annotatedField = FieldProperty.class.getDeclaredField("_annotated");
        annotatedField.setAccessible(true);
        AnnotatedField field = (AnnotatedField) annotatedField.get(null);

        Field fieldField = FieldProperty.class.getDeclaredField("_field");
        fieldField.setAccessible(true);
        Field realField = (Field) fieldField.get(null);
        field.setAnnotated(realField);

        Field fieldPropertyField = FieldProperty.class.getDeclaredField("_annotated");
        fieldPropertyField.setAccessible(true);
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(null, null, null,
                TypeFactory.defaultInstance().constructType(String.class), null, null);
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);
        TypeDeserializer typeDeserializer = null;
        Annotations contextAnnotations = null;

        return new FieldProperty(propDef, type, typeDeserializer, contextAnnotations, field);
    }
}