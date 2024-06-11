package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.core.JsonToken.*;
import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.TypeDeserializers;
import com.fasterxml.jackson.databind.util.TypeUtil;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

public class FieldPropertyTest {

    @Test
    public void testConstructorWithValidArguments() throws IOException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        AnnotatedField annotatedField = createAnnotatedField("field1", new JsonProperty("field1"));
        FieldProperty fieldProperty = new FieldProperty(createBeanPropertyDefinition(), createJavaType(), null, null, annotatedField);
        assertEquals(fieldProperty, fieldProperty);
    }

    @Test
    public void testWithName() throws IOException {
        // Arrange
        AnnotatedField annotatedField = createAnnotatedField("field1", new JsonProperty("field1"));
        FieldProperty fieldProperty = new FieldProperty(createBeanPropertyDefinition(), createJavaType(), null, null, annotatedField);

        // Act
        PropertyName newName = PropertyName.construct("newName");
        FieldProperty newFieldProperty = fieldProperty.withName(newName);

        // Assert
        assertEquals(newName, newFieldProperty.getName());
        assertEquals(fieldProperty, newFieldProperty);
    }

    @Test
    public void testWithValueDeserializer() throws IOException {
        // Arrange
        AnnotatedField annotatedField = createAnnotatedField("field1", new JsonProperty("field1"));
        FieldProperty fieldProperty = new FieldProperty(createBeanPropertyDefinition(), createJavaType(), null, null, annotatedField);
        JsonDeserializer<Object> valueDeserializer = createJsonDeserializer();

        // Act
        FieldProperty newFieldProperty = fieldProperty.withValueDeserializer(valueDeserializer);

        // Assert
        assertEquals(valueDeserializer, newFieldProperty.getValueDeserializer());
        assertEquals(fieldProperty, newFieldProperty);
    }

    @Test
    public void testWithNullProvider() throws IOException {
        // Arrange
        AnnotatedField annotatedField = createAnnotatedField("field1", new JsonProperty("field1"));
        FieldProperty fieldProperty = new FieldProperty(createBeanPropertyDefinition(), createJavaType(), null, null, annotatedField);
        NullValueProvider nullValueProvider = createNullValueProvider();

        // Act
        FieldProperty newFieldProperty = fieldProperty.withNullProvider(nullValueProvider);

        // Assert
        assertEquals(nullValueProvider, newFieldProperty.getNullProvider());
        assertEquals(fieldProperty, newFieldProperty);
    }

    @Test
    public void testFixAccess() throws IOException {
        // Arrange
        Object instance = new Object() {};
        FieldProperty fieldProperty = new FieldProperty(createBeanPropertyDefinition(), createJavaType(), null, null, createAnnotatedField("field1", new JsonProperty("field1")));

        // Act
        DeserializationConfig config = createDeserializationConfig();
        fieldProperty.fixAccess(config);

        // Assert
        Field field = fieldProperty.getField();
        assertTrue(config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
        field.setAccessible(true);
        field.set(instance, "value");
        assertEquals("value", fieldProperty.getValue(instance));
    }

    @Test
    public void testGetAnnotation() throws IOException {
        // Arrange
        AnnotatedField annotatedField = createAnnotatedField("field1", new JsonProperty("field1"));
        FieldProperty fieldProperty = new FieldProperty(createBeanPropertyDefinition(), createJavaType(), null, null, annotatedField);

        // Act
        JsonProperty jsonProperty = fieldProperty.getAnnotation(JsonProperty.class);

        // Assert
        assertEquals("field1", jsonProperty.value());
        assertEquals(fieldProperty, jsonProperty.forField(fieldProperty));
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        // Arrange
        Object instance = new Object() {};
        FieldProperty fieldProperty = new FieldProperty(createBeanPropertyDefinition(), createJavaType(), null, null, createAnnotatedField("field1", new JsonProperty("field1")));

        // Act
        JsonParser p = createParser("\"value\"");
        DeserializationContext ctxt = createDeserializationContext();
        fieldProperty.deserializeAndSet(p, ctxt, instance);

        // Assert
        assertEquals("value", fieldProperty.getValue(instance));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        // Arrange
        Object instance = new Object() {};
        FieldProperty fieldProperty = new FieldProperty(createBeanPropertyDefinition(), createJavaType(), null, null, createAnnotatedField("field1", new JsonProperty("field1")));

        // Act
        JsonParser p = createParser("\"value\"");
        DeserializationContext ctxt = createDeserializationContext();
        Object result = fieldProperty.deserializeSetAndReturn(p, ctxt, instance);

        // Assert
        assertEquals(instance, result);
        assertEquals("value", fieldProperty.getValue(instance));
    }

    @Test
    public void testSet() throws IOException, InstantiationException, IllegalAccessException {
        // Arrange
        Object instance = new Object() {};
        FieldProperty fieldProperty = new FieldProperty(createBeanPropertyDefinition(), createJavaType(), null, null, createAnnotatedField("field1", new JsonProperty("field1")));

        // Act
        fieldProperty.set(instance, "value");

        // Assert
        assertEquals("value", fieldProperty.getValue(instance));
    }

    @Test
    public void testSetAndReturn() throws IOException, InstantiationException, IllegalAccessException {
        // Arrange
        Object instance = new Object() {};
        FieldProperty fieldProperty = new FieldProperty(createBeanPropertyDefinition(), createJavaType(), null, null, createAnnotatedField("field1", new JsonProperty("field1")));

        // Act
        Object result = fieldProperty.setAndReturn(instance, "value");

        // Assert
        assertEquals(instance, result);
        assertEquals("value", fieldProperty.getValue(instance));
    }

    @Test
    public void testReadResolve() throws IOException {
        // Arrange
        Object instance = new Object() {};
        FieldProperty fieldProperty = new FieldProperty(createBeanPropertyDefinition(), createJavaType(), null, null, createAnnotatedField("field1", new JsonProperty("field1")));

        // Act
        Object result = fieldProperty.readResolve();

        // Assert
        assertEquals(fieldProperty, result);
    }

    private AnnotatedField createAnnotatedField(String name, Annotation annotation) {
        Class<?> clazz = SimpleBean.class;
        AccessibleObject field = null;
        try {
            field = clazz.getDeclaredField(name);
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Should not fail", e);
        }
        return new AnnotatedField(field, Annotations.asMap(annotation));
    }

    private BeanPropertyDefinition createBeanPropertyDefinition() {
        return new BeanPropertyDefinition(createPropertyName(), createJavaType(), createBeanPropertyTypeDeserializer(), null, null);
    }

    private JavaType createJavaType() {
        return TypeFactory.defaultInstance().constructType(Integer.TYPE);
    }

    private JsonDeserializer<Object> createJsonDeserializer() {
        return new StdSerializer<Object>(Object.class) {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return "value";
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
        return new DeserializationConfig(new ObjectMapper());
    }

    private PropertyName createPropertyName() {
        return PropertyName.construct("field1");
    }

    private JsonParser createParser(String contentInStringForm) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getFactory().createParser(new StringReader(contentInStringForm));
    }

    private DeserializationContext createDeserializationContext() throws IOException {
        return new DeserializationContext(new ObjectMapper());
    }

    private BeanPropertyTypeDeserializer createBeanPropertyTypeDeserializer() {
        return new BeanPropertyTypeDeserializer(null, null, null);
    }
}