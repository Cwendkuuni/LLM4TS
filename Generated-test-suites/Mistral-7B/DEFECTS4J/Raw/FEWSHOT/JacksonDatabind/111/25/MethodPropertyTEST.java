package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.core.JsonToken.VALUE_NULL;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Method;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

public class MethodPropertyTest {

    @SuppressWarnings("unchecked")
    private static final MethodProperty<Object, Object> TEST_PROPERTY = new MethodProperty<Object, Object>(
            new BeanPropertyDefinition(null, null, null, null, null),
            Object.class, null, Annotations.empty(),
            mock(AnnotatedMethod.class)) {
        {
            _skipNulls = false;
        }

        @Override
        public void set(Object instance, Object value) throws IOException {
            // No-op
        }

        @Override
        public Object setAndReturn(Object instance, Object value) throws IOException {
            // No-op
            return null;
        }
    };

    @Test
    public void testGetAnnotation() {
        AnnotatedMethod m = mock(AnnotatedMethod.class);
        when(m.getAnnotations()).thenReturn(new JsonSetter[0]);

        Annotation annotation = TEST_PROPERTY.getAnnotation(JsonSetter.class);
        assertNull(annotation);

        Annotation annotatedMethodAnnotation = TEST_PROPERTY.getAnnotation(_annotated.getAnnotations().getClass());
        assertEquals(m, annotatedMethodAnnotation);
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        Object instance = mock(Object.class);
        Object value = new Object();
        Method method = mock(Method.class);
        when(method.invoke(instance, anyObject())).thenReturn(value);

        JsonParser p = mock(JsonParser.class);
        when(p.getValueAsToken()).thenReturn(VALUE_NULL);
        when(p.nextToken()).thenReturn(VALUE_NULL);

        Object nullValue = TEST_PROPERTY._nullProvider.getNullValue(null);

        TEST_PROPERTY.deserializeAndSet(p, null, instance);
        verifyNoMoreInteractions(p);
        assertEquals(nullValue, instance);

        when(p.getValueAsToken()).thenReturn(JsonToken.START_OBJECT);
        when(p.nextValue()).thenReturn("foo");
        when(p.nextToken()).thenReturn(JsonToken.END_OBJECT);

        TEST_PROPERTY.deserializeAndSet(p, null, instance);
        verify(method).invoke(instance, value);
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        Object instance = mock(Object.class);
        Object value = new Object();
        Method method = mock(Method.class);
        when(method.invoke(instance, anyObject())).thenReturn(value);

        JsonParser p = mock(JsonParser.class);
        when(p.getValueAsToken()).thenReturn(VALUE_NULL);
        when(p.nextToken()).thenReturn(VALUE_NULL);

        Object nullValue = TEST_PROPERTY._nullProvider.getNullValue(null);

        Object result = TEST_PROPERTY.deserializeSetAndReturn(p, null, instance);
        verifyNoMoreInteractions(p);
        assertEquals(instance, result);

        when(p.getValueAsToken()).thenReturn(JsonToken.START_OBJECT);
        when(p.nextValue()).thenReturn("foo");
        when(p.nextToken()).thenReturn(JsonToken.END_OBJECT);

        Object result2 = TEST_PROPERTY.deserializeSetAndReturn(p, null, instance);
        verify(method).invoke(instance, value);
        assertEquals(value, result2);
    }

    @Test
    public void testSet() throws Exception {
        Object instance = mock(Object.class);
        Object value = new Object();
        Method method = mock(Method.class);
        when(method.invoke(instance, anyObject())).thenReturn(value);

        TEST_PROPERTY.set(instance, value);
        verify(method).invoke(instance, value);
    }

    @Test
    public void testSetAndReturn() throws Exception {
        Object instance = mock(Object.class);
        Object value = new Object();
        Method method = mock(Method.class);
        when(method.invoke(instance, anyObject())).thenReturn(value);

        Object result = TEST_PROPERTY.setAndReturn(instance, value);
        verify(method).invoke(instance, value);
        assertEquals(value, result);
    }

    @Test
    public void testReadResolve() throws Exception {
        Object instance = mock(Object.class);
        AnnotatedMethod annotated = mock(AnnotatedMethod.class);
        when(annotated.getAnnotated()).thenReturn(instance);
        when(annotated.getDeclaringClass()).thenReturn(Object.class);

        MethodProperty<Object, Object> result = (MethodProperty<Object, Object>) TEST_PROPERTY.readResolve();
        assertEquals(TEST_PROPERTY, result);
        assertEquals(instance, result._annotated.getAnnotated());
    }

    @Test
    public void testDeserializeWithType() throws Exception {
        Object instance = mock(Object.class);
        Object value = new Object();
        Method method = mock(Method.class);
        when(method.invoke(instance, anyObject())).thenReturn(value);

        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(typeDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class), any(Class.class)))
                .thenReturn(value);

        JsonParser p = mock(JsonParser.class);
        when(p.getValueAsToken()).thenReturn(VALUE_NULL);
        when(p.nextToken()).thenReturn(VALUE_NULL);

        Object nullValue = TEST_PROPERTY._nullProvider.getNullValue(null);

        TEST_PROPERTY.deserializeAndSet(p, null, instance);
        verifyNoMoreInteractions(p);
        assertEquals(nullValue, instance);

        when(p.getValueAsToken()).thenReturn(JsonToken.START_OBJECT);
        when(p.nextValue()).thenReturn("foo");
        when(p.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        when(p.nextValue()).thenReturn("bar");
        when(p.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(p.nextValue()).thenReturn("baz");
        when(p.nextToken()).thenReturn(JsonToken.END_OBJECT);

        TYPE:
        TEST_PROPERTY.deserializeAndSet(p, null, instance);

        verify(method).invoke(instance, value);
        verify(p, times(2)).nextToken();
        verify(typeDeserializer).deserialize(p, null, String.class);
        assertEquals("baz", value);

        when(p.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(p.nextValue()).thenReturn(JsonToken.VALUE_NULL);
        when(p.nextToken()).thenReturn(JsonToken.END_ARRAY);

        TEST_PROPERTY.deserializeAndSet(p, null, instance);

        verify(method, times(2)).invoke(instance, value);
        verify(p, times(5)).nextToken();
        verify(typeDeserializer, times(1)).deserialize(p, null, ClassUtil.handleRawType(List.class));
    }

    @Test
    public void testDeserializeWithTypeWithConverter() throws Exception {
        Object instance = mock(Object.class);
        Object value = new Object();
        Method method = mock(Method.class);
        when(method.invoke(instance, anyObject())).thenReturn(value);

        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(typeDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class), any(Class.class)))
                .thenReturn(value);

        Converter<String, Object> converter = mock(Converter.class);

        JsonParser p = mock(JsonParser.class);
        when(p.getValueAsToken()).thenReturn(VALUE_NULL);
        when(p.nextToken()).thenReturn(VALUE_NULL);

        Object nullValue = TEST_PROPERTY._nullProvider.getNullValue(null);

        TEST_PROPERTY.deserializeAndSet(p, null, instance);
        verifyNoMoreInteractions(p);
        assertEquals(nullValue, instance);

        when(p.getValueAsToken()).thenReturn(JsonToken.START_OBJECT);
        when(p.nextValue()).thenReturn("foo");
        when(p.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        when(p.nextValue()).thenReturn("bar");
        when(p.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(p.nextValue()).thenReturn("baz");
        when(p.nextToken()).thenReturn(JsonToken.END_OBJECT);

        TEST_PROPERTY.deserializeAndSet(p, null, instance);

        verify(method).invoke(instance, value);
        verify(p, times(2)).nextToken();
        verify(typeDeserializer).deserialize(p, null, String.class);
        verify(converter).convertAndTie(eq("baz"), any(MapperContext.class));
        assertEquals(value, TEST_PROPERTY._valueDeserializer.deserialize(p, null));

        when(p.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(p.nextValue()).thenReturn(JsonToken.VALUE_NULL);
        when(p.nextToken()).thenReturn(JsonToken.END_ARRAY);

        TEST_PROPERTY.deserializeAndSet(p, null, instance);

        verify(method, times(2)).invoke(instance, value);
        verify(p, times(5)).nextToken();
        verify(typeDeserializer, times(1)).deserialize(p, null, ClassUtil.handleRawType(List.class));
        verify(converter, times(2)).convertAndTie(eq(null), any(MapperContext.class));
    }
}