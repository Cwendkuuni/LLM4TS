
package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Method;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.Annotation;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

public class MethodPropertyTest {

    // Mock objects
    private ObjectMapper objectMapper = mock(ObjectMapper.class);
    private BeanPropertyDefinition beanPropertyDefinition = mock(BeanPropertyDefinition.class);
    private JavaType javaType = mock(JavaType.class);
    private TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
    private Annotations contextAnnotations = mock(Annotations.class);
    private AnnotatedMethod method = mock(AnnotatedMethod.class);
    private Method setter = mock(Method.class);
    private NullValueProvider nullValueProvider = mock(NullValueProvider.class);
    private JsonDeserializer<?> jsonDeserializer = mock(JsonDeserializer.class);
    private PropertyName propertyName = mock(PropertyName.class);
    private DeserializationConfig deserializationConfig = mock(DeserializationConfig.class);
    private JsonParser jsonParser = mock(JsonParser.class);
    private DeserializationContext deserializationContext = mock(DeserializationContext.class);
    private Object instance = new Object();

    @Test
    public void testConstructor() {
        MethodProperty methodProperty = new MethodProperty(beanPropertyDefinition, javaType, typeDeserializer, contextAnnotations, method);

        assertEquals(beanPropertyDefinition, methodProperty.getMember());
        assertEquals(javaType, methodProperty.getType());
        assertEquals(typeDeserializer, methodProperty.getValueTypeDeserializer());
        assertEquals(contextAnnotations, methodProperty.getContextAnnotations());
        assertEquals(method, methodProperty.getAnnotated());
        assertFalse(methodProperty.isNonNull());
    }

    @Test
    public void testConstructorWithDeserializerAndNullValueProvider() {
        MethodProperty srcMethodProperty = new MethodProperty(beanPropertyDefinition, javaType, typeDeserializer, contextAnnotations, method);

        MethodProperty methodProperty = new MethodProperty(srcMethodProperty, jsonDeserializer, nullValueProvider);

        assertEquals(beanPropertyDefinition, methodProperty.getMember());
        assertEquals(javaType, methodProperty.getType());
        assertEquals(typeDeserializer, methodProperty.getValueTypeDeserializer());
        assertEquals(contextAnnotations, methodProperty.getContextAnnotations());
        assertEquals(method, methodProperty.getAnnotated());
        assertFalse(methodProperty.isNonNull());
    }

    @Test
    public void testConstructorWithPropertyName() {
        MethodProperty srcMethodProperty = new MethodProperty(beanPropertyDefinition, javaType, typeDeserializer, contextAnnotations, method);

        MethodProperty methodProperty = new MethodProperty(srcMethodProperty, propertyName);

        assertEquals(propertyName, methodProperty.getFullName());
        assertEquals(javaType, methodProperty.getType());
        assertEquals(typeDeserializer, methodProperty.getValueTypeDeserializer());
        assertEquals(contextAnnotations, methodProperty.getContextAnnotations());
        assertEquals(method, methodProperty.getAnnotated());
        assertFalse(methodProperty.isNonNull());
    }

    @Test
    public void testConstructorWithMethod() {
        MethodProperty srcMethodProperty = new MethodProperty(beanPropertyDefinition, javaType, typeDeserializer, contextAnnotations, method);

        MethodProperty methodProperty = new MethodProperty(srcMethodProperty, setter);

        assertEquals(beanPropertyDefinition, methodProperty.getMember());
        assertEquals(javaType, methodProperty.getType());
        assertEquals(typeDeserializer, methodProperty.getValueTypeDeserializer());
        assertEquals(contextAnnotations, methodProperty.getContextAnnotations());
        assertEquals(setter, methodProperty.getAnnotated());
        assertFalse(methodProperty.isNonNull());
    }

    @Test
    public void testWithPropertyName() {
        MethodProperty srcMethodProperty = new MethodProperty(beanPropertyDefinition, javaType, typeDeserializer, contextAnnotations, method);

        MethodProperty methodProperty = (MethodProperty) srcMethodProperty.withName(propertyName);

        assertEquals(propertyName, methodProperty.getFullName());
        assertEquals(javaType, methodProperty.getType());
        assertEquals(typeDeserializer, methodProperty.getValueTypeDeserializer());
        assertEquals(contextAnnotations, methodProperty.getContextAnnotations());
        assertEquals(method, methodProperty.getAnnotated());
        assertFalse(methodProperty.isNonNull());
    }

    @Test
    public void testWithValueDeserializerSameDeserializer() {
        MethodProperty srcMethodProperty = new MethodProperty(beanPropertyDefinition, javaType, typeDeserializer, contextAnnotations, method);

        MethodProperty methodProperty = (MethodProperty) srcMethodProperty.withValueDeserializer(jsonDeserializer);

        assertSame(srcMethodProperty, methodProperty);
    }

    @Test
    public void testWithValueDeserializerDifferentDeserializer() {
        MethodProperty srcMethodProperty = new MethodProperty(beanPropertyDefinition, javaType, typeDeserializer, contextAnnotations, method);

        MethodProperty methodProperty = (MethodProperty) srcMethodProperty.withValueDeserializer(jsonDeserializer);

        assertEquals(beanPropertyDefinition, methodProperty.getMember());
        assertEquals(javaType, methodProperty.getType());
        assertEquals(typeDeserializer, methodProperty.getValueTypeDeserializer());
        assertEquals(contextAnnotations, methodProperty.getContextAnnotations());
        assertEquals(method, methodProperty.getAnnotated());
        assertSame(jsonDeserializer, methodProperty.getValueDeserializer());
        assertNotNull(methodProperty.getNullProvider());
        assertFalse(methodProperty.isNonNull());
    }

    @Test
    public void testWithNullProvider() {
        MethodProperty srcMethodProperty = new MethodProperty(beanPropertyDefinition, javaType, typeDeserializer, contextAnnotations, method);

        MethodProperty methodProperty = (MethodProperty) srcMethodProperty.withNullProvider(nullValueProvider);

        assertEquals(beanPropertyDefinition, methodProperty.getMember());
        assertEquals(javaType, methodProperty.getType());
        assertEquals(typeDeserializer, methodProperty.getValueTypeDeserializer());
        assertEquals(contextAnnotations, methodProperty.getContextAnnotations());
        assertEquals(method, methodProperty.getAnnotated());
        assertNull(methodProperty.getValueDeserializer());
        assertSame(nullValueProvider, methodProperty.getNullProvider());
        assertFalse(methodProperty.isNonNull());
    }

    @Test
    public void testFixAccess() {
        MethodProperty methodProperty = new MethodProperty(beanPropertyDefinition, javaType, typeDeserializer, contextAnnotations, method);

        methodProperty.fixAccess(deserializationConfig);

        verify(method).fixAccess(true);
    }

    @Test
    public void testGetAnnotation() {
        Class<? extends Annotation> acls = Annotation.class;
        when(method.getAnnotation(acls)).thenReturn(null);

        MethodProperty methodProperty = new MethodProperty(beanPropertyDefinition, javaType, typeDeserializer, contextAnnotations, method);

        assertNull(methodProperty.getAnnotation(acls));

        verify(method).getAnnotation(acls);
    }

    @Test
    public void testSet() throws Exception {
        MethodProperty methodProperty = new MethodProperty(beanPropertyDefinition, javaType, typeDeserializer, contextAnnotations, method);

        methodProperty.set(instance, instance);

        verify(setter).invoke(instance, instance);
    }

    @Test(expected = JsonParseException.class)
    public void testSetWithException() throws Exception {
        MethodProperty methodProperty = new MethodProperty(beanPropertyDefinition, javaType, typeDeserializer, contextAnnotations, method);

        doThrow(Exception.class).when(setter).invoke(instance, null);

        methodProperty.set(instance, null);
    }

    @Test
    public void testDeserializeSetAndReturnWithNullValue() throws IOException {
        MethodProperty methodProperty = new MethodProperty(beanPropertyDefinition, javaType, typeDeserializer, contextAnnotations, method);

        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        when(methodProperty._skipNulls).thenReturn(false);
        when(methodProperty._nullProvider.getNullValue(deserializationContext)).thenReturn(nullValueProvider);

        Object result = methodProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

        assertNull(result);
        verify(jsonParser).hasToken(JsonToken.VALUE_NULL);
        verify(methodProperty._nullProvider.getNullValue(deserializationContext));
    }

    @Test
    public void testDeserializeSetAndReturnWithNonNullValue() throws IOException {
        MethodProperty methodProperty = new MethodProperty(beanPropertyDefinition, javaType, typeDeserializer, contextAnnotations, method);

        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(jsonParser.readValueAs(javaType.getRawClass())).thenReturn(instance);

        Object result = methodProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

        assertSame(result, instance);
        verify(jsonParser).hasToken(JsonToken.VALUE_NULL);
        verify(jsonParser).readValueAs(javaType.getRawClass());
    }

    @Test
    public void testDeserializeSetAndReturnWithNullValueAndSkipNullsTrue() throws IOException {
        MethodProperty methodProperty = new MethodProperty(beanPropertyDefinition, javaType, typeDeserializer, contextAnnotations, method);

        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        when(methodProperty._skipNulls).thenReturn(true);
        when(methodProperty._nullProvider.getNullValue(deserializationContext)).thenReturn(nullValueProvider);

        Object result = methodProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

        assertSame(result, instance);
        verify(jsonParser).hasToken(JsonToken.VALUE_NULL);
        verify(methodProperty._nullProvider, never()).getNullValue(deserializationContext);
    }

    @Test(expected = IOException.class)
    public void testDeserializeSetAndReturnWithException() throws IOException {
        MethodProperty methodProperty = new MethodProperty(beanPropertyDefinition, javaType, typeDeserializer, contextAnnotations, method);

        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        when(methodProperty._skipNulls).thenReturn(false);
        when(methodProperty._nullProvider.getNullValue(deserializationContext)).thenReturn(nullValueProvider);

        doThrow(Exception.class).when(setter).invoke(instance, null);

        methodProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);
    }
}
