package com.yourpackage.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Field;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonParser;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.NullValueProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.type.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class FieldPropertyTest {

    @Mock
    JsonParser mockJsonParser;
    @Mock
    DeserializationContext mockDeserializationContext;
    @Mock
    ObjectMapper mockObjectMapper;
    @Mock
    JsonFactory mockJsonFactory;
    @Mock
    TypeDeserializer mockTypeDeserializer;
    @Mock
    BeanPropertyDefinition mockBeanPropertyDefinition;
    @Mock
    NullValueProvider mockNullValueProvider;
    @Mock
    Annotations mockAnnotations;
    @Mock
    AnnotatedField mockAnnotatedField;
    @Mock
    AnnotatedMember mockAnnotatedMember;
    @Mock
    Field mockField;
    @Mock
    PropertyName mockPropertyName;
    @Mock
    JavaType mockJavaType;
    @Mock
    Converter mockConverter;
    @Mock
    JsonDeserializer mockJsonDeserializer;

    @InjectMocks
    FieldProperty fieldProperty;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConstructor() {
        fieldProperty = new FieldProperty(mockBeanPropertyDefinition, mockJavaType, mockTypeDeserializer, mockAnnotations, mockAnnotatedField);
        assertSame(mockBeanPropertyDefinition, fieldProperty.getBeanPropertyDefinition());
        assertSame(mockJavaType, fieldProperty.getType());
        assertSame(mockTypeDeserializer, fieldProperty.getTypeDeserializer());
        assertSame(mockAnnotations, fieldProperty.getContextAnnotations());
        assertSame(mockAnnotatedField, fieldProperty.getAnnotated());
        assertSame(mockAnnotatedField.getAnnotated(), fieldProperty._annotated);
        assertSame(mockAnnotatedField.getField(), fieldProperty._field);
    }

    @Test
    public void testConstructorWithNullValueProvider() {
        fieldProperty = new FieldProperty(mockBeanPropertyDefinition, mockJavaType, mockTypeDeserializer, null, mockAnnotatedField);
        assertNull(fieldProperty._nullProvider);
    }

    @Test
    public void testConstructorWithValueDeserializer() {
        fieldProperty = new FieldProperty(fieldProperty, mockJsonDeserializer, mockNullValueProvider);
        assertSame(fieldProperty, fieldProperty.getBackRef());
        assertSame(mockJsonDeserializer, fieldProperty._valueDeserializer);
    }

    @Test
    public void testConstructorWithPropertyName() {
        fieldProperty = new FieldProperty(fieldProperty, mockPropertyName);
        assertSame(fieldProperty, fieldProperty.getBackRef());
        assertSame(mockPropertyName, fieldProperty._propertyName);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        when(mockJsonParser.getValueAsBoolean()).thenReturn(true);
        when(mockJsonParser.getText()).thenReturn("Test");
        when(mockDeserializationContext.getActiveContext()).thenReturn(mockObjectMapper);
        when(mockObjectMapper.findTypeDeserializer(mockJavaType, mockDeserializationContext)).thenReturn(mockTypeDeserializer);

        Object instance = new Object();
        fieldProperty.deserializeAndSet(mockJsonParser, mockDeserializationContext, instance);

        assertEquals(true, fieldProperty.getBackRef().get(instance));
        assertEquals("Test", fieldProperty.getBackRef().getName().getShortName());
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetWithNullValue() throws IOException {
        when(mockJsonParser.getValueAsBoolean()).thenReturn(false);
        when(mockJsonParser.getValueAsBoolean(_skipNulls)).thenReturn(true);
        when(mockDeserializationContext.getActiveContext()).thenReturn(mockObjectMapper);
        when(mockObjectMapper.findTypeDeserializer(mockJavaType, mockDeserializationContext)).thenReturn(mockTypeDeserializer);

        Object instance = new Object();
        fieldProperty.deserializeAndSet(mockJsonParser, mockDeserializationContext, instance);

        assertNull(fieldProperty.getBackRef().get(instance));
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetWithNullTypeDeserializer() throws IOException {
        when(mockJsonParser.getValueAsBoolean()).thenReturn(true);
        when(mockJsonParser.getText()).thenReturn("Test");
        when(mockDeserializationContext.getActiveContext()).thenReturn(mockObjectMapper);
        when(mockObjectMapper.findTypeDeserializer(mockJavaType, mockDeserializationContext)).thenReturn(null);

        Object instance = new Object();
        fieldProperty.deserializeAndSet(mockJsonParser, mockDeserializationContext, instance);

        // Test should fail with IOException due to missing type deserializer
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        when(mockJsonParser.getValueAsBoolean()).thenReturn(true);
        when(mockDeserializationContext.getActiveContext()).thenReturn(mockObjectMapper);
        when(mockObjectMapper.findTypeDeserializer(mockJavaType, mockDeserializationContext)).thenReturn(mockTypeDeserializer);

        Object instance = new Object();
        Object result = fieldProperty.deserializeSetAndReturn(mockJsonParser, mockDeserializationContext, instance);

        assertEquals(instance, result);
        assertEquals(true, fieldProperty.getBackRef().get(instance));
        assertEquals("Test", fieldProperty.getBackRef().getName().getShortName());
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        fieldProperty.set(instance, value);

        assertEquals(value, fieldProperty.getBackRef().get(instance));
    }

    @Test
    public void testSetWithNullValue() throws IOException {
        Object instance = new Object();

        fieldProperty.set(instance, null);

        assertNull(fieldProperty.getBackRef().get(instance));
    }

    @Test
    public void testSetWithNullValueAndNullProvider() throws IOException {
        Object instance = new Object();
        fieldProperty = new FieldProperty(mockBeanPropertyDefinition, mockJavaType, mockTypeDeserializer, null, mockAnnotatedField);

        fieldProperty.set(instance, null);

        assertNull(fieldProperty.getBackRef().get(instance));
    }

    @Test
    public void testFixAccess() throws IOException {
        Class<?> testClass = ClassUtil.getRawClass(getClass());
        Field testField = testClass.getDeclaredField("fieldProperty");
        testField.setAccessible(true);

        Object instance = new Object();
        fieldProperty.fixAccess(mockDeserializationContext);

        fieldProperty.set(instance, new Object());

        assertTrue(testField.isAccessible(instance));
    }
}