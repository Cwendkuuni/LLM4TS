package com.example.junit;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Field;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.NullValueProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FieldPropertyTest {

    private ObjectMapper objectMapper;
    private Field testField;
    private JsonDeserializer<String> testDeserializer;
    private NullValueProvider testNullValueProvider;
    private TypeDeserializer testTypeDeserializer;

    @Before
    public void setup() throws Exception {
        testField = this.getClass().getDeclaredField("testField");
        testField.setAccessible(true);

        objectMapper = new ObjectMapper();
        testDeserializer = mock(JsonDeserializer.class);
        testNullValueProvider = mock(NullValueProvider.class);
        testTypeDeserializer = mock(TypeDeserializer.class);
    }

    @After
    public void teardown() throws Exception {
        testField.setAccessible(false);
    }

    @Test
    public void testConstructor() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedField annotatedField = mock(AnnotatedField.class);
        Field field = mock(Field.class);

        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, annotatedField);
        assertSame(propDef, fieldProperty.getPropertyDefinition());
        assertSame(type, fieldProperty.getType());
        assertSame(typeDeser, fieldProperty.getTypeDeserializer());
        assertSame(contextAnnotations, fieldProperty.getContextAnnotationInstances());
        assertSame(annotatedField, fieldProperty.getMember());
        assertSame(field, fieldProperty.getField());
    }

    @Test
    public void testWithName() {
        PropertyName newName = mock(PropertyName.class);
        FieldProperty fieldProperty = new FieldProperty(this, newName);
        assertSame(newName, fieldProperty.getName());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<String> deser = mock(JsonDeserializer.class);
        NullValueProvider nva = mock(NullValueProvider.class);

        FieldProperty fieldProperty = new FieldProperty(this, deser, nva);
        assertSame(deser, fieldProperty.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nva = mock(NullValueProvider.class);

        FieldProperty fieldProperty = new FieldProperty(this, nva);
        assertSame(nva, fieldProperty.getNullProvider());
    }

    @Test
    public void testFixAccess() throws Exception {
        ClassUtil.checkAndFixAccess(testField, true);
        assertSame(testField.getModifiers(), testField.getModifiers() & ~Modifier.FINAL);
    }

    @Test
    public void testGetAnnotation() {
        JsonProperty jsonProperty = mock(JsonProperty.class);
        when(testField.getAnnotated()).thenReturn(jsonProperty);

        FieldProperty fieldProperty = new FieldProperty(this, null, null, null, testField);
        assertSame(jsonProperty, fieldProperty.getAnnotation(JsonProperty.class));
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        Object instance = new Object();
        String expectedValue = "testValue";

        when(testDeserializer.deserialize(mock(JsonParser.class), mock(DeserializationContext.class)))
                .thenReturn(expectedValue);

        FieldProperty fieldProperty = new FieldProperty(null, null, testDeserializer, null, testField);
        fieldProperty.deserializeAndSet(mock(JsonParser.class), mock(DeserializationContext.class), instance);
        assertSame(expectedValue, testField.get(instance));
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        Object instance = new Object();
        String expectedValue = "testValue";

        when(testDeserializer.deserialize(mock(JsonParser.class), mock(DeserializationContext.class)))
                .thenReturn(expectedValue);

        FieldProperty fieldProperty = new FieldProperty(null, null, testDeserializer, null, testField);
        Object result = fieldProperty.deserializeSetAndReturn(mock(JsonParser.class), mock(DeserializationContext.class), instance);
        assertSame(instance, result);
        assertSame(expectedValue, testField.get(instance));
    }

    @Test
    public void testSet() throws Exception {
        Object instance = new Object();
        String value = "testValue";

        FieldProperty fieldProperty = new FieldProperty(this, null, null, null, testField);
        fieldProperty.set(instance, value);
        assertSame(value, testField.get(instance));
    }

    @Test
    public void testSetAndReturn() throws Exception {
        Object instance = new Object();
        String value = "testValue";

        FieldProperty fieldProperty = new FieldProperty(this, null, null, null, testField);
        Object result = fieldProperty.setAndReturn(instance, value);
        assertSame(instance, result);
        assertSame(value, testField.get(instance));
    }

    @Test
    public void testReadResolve() {
        FieldProperty fieldProperty = new FieldProperty(this);
        FieldProperty result = (FieldProperty) fieldProperty.readResolve();
        assertSame(fieldProperty, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetWithNullValue() throws Exception {
        Object instance = new Object();

        JsonParser parser = mock(JsonParser.class);
        when(parser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);

        FieldProperty fieldProperty = new FieldProperty(null, null, null, null, testField);
        fieldProperty.deserializeAndSet(parser, mock(DeserializationContext.class), instance);

        assertNull(testField.get(instance));
    }

    @Test(expected = IOException.class)
    public void testDeserializeSetAndReturnWithNullValue() throws Exception {
        Object instance = new Object();

        JsonParser parser = mock(JsonParser.class);
        when(parser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);

        FieldProperty fieldProperty = new FieldProperty(null, null, null, null, testField);
        fieldProperty.deserializeSetAndReturn(parser, mock(DeserializationContext.class), instance);

        assertSame(instance, testField.get(instance));
        assertNull(testField.get(instance));
    }

    @Test(expected = IOException.class)
    public void testSetWithInvalidValue() throws Exception {
        Object instance = new Object();
        Integer value = 123;

        FieldProperty fieldProperty = new FieldProperty(this, null, null, null, testField);
        fieldProperty.set(instance, value);

        // This will throw an exception because the field is of type String and we're trying to set an Integer value
    }

    @Test(expected = IOException.class)
    public void testSetAndReturnWithInvalidValue() throws Exception {
        Object instance = new Object();
        Integer value = 123;

        FieldProperty fieldProperty = new FieldProperty(this, null, null, null, testField);
        fieldProperty.setAndReturn(instance, value);

        // This will throw an exception because the field is of type String and we're trying to set an Integer value
    }
}