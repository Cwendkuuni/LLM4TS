package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.core.JsonToken.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.NullValueProvider.NullProvider;

/**
 * Test cases for {@link MethodProperty} class.
 */
public class MethodPropertyTest {

    private static final BasicDeserializer.ValueDeserializerTest DESerializer = mock(BasicDeserializer.ValueDeserializerTest.class);
    private static final Object INSTANCE = new Object();

    @Test
    public void testBasic() throws Exception {
        Method method = mock(Method.class);
        MethodProperty property = new MethodProperty(null, method);

        assertEquals(property.getClass(), MethodProperty.class);
        assertNull(property._valueDeserializer);
        assertSame(method, property._setter);
        assertSame(property._annotated, property._annotated);
    }

    @Test
    public void testProperties() throws Exception {
        String name = "name";
        Annotation annotation = mock(Annotation.class);
        Method method = mock(Method.class);
        AnnotatedMethod annotatedMethod = mock(AnnotatedMethod.class);
        when(annotatedMethod.getAnnotated()).thenReturn(method);

        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);

        MethodProperty property = new MethodProperty(null, type, typeDeserializer, contextAnnotations, annotatedMethod);

        assertEquals(property.getName(), name);
        assertEquals(property.getType(), type);
        assertEquals(property.getTypeHandler(), typeDeserializer);
        assertEquals(property.getMember(), annotatedMethod);
        assertEquals(property._annotated, annotatedMethod);
        assertEquals(property._setter, method);
    }

    @Test
    public void testSkeleton() throws Exception {
        String name = "name";
        Annotation annotation = mock(Annotation.class);
        Method method = mock(Method.class);
        AnnotatedMethod annotatedMethod = mock(AnnotatedMethod.class);
        when(annotatedMethod.getAnnotated()).thenReturn(method);

        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        NullProvider nullProvider = mock(NullProvider.class);

        MethodProperty property = new MethodProperty(null, type, typeDeserializer, contextAnnotations, annotatedMethod);

        assertEquals(property.getName(), name);
        assertEquals(property.getType(), type);
        assertEquals(property.getTypeHandler(), typeDeserializer);
        assertEquals(property.getMember(), annotatedMethod);
        assertEquals(property._annotated, annotatedMethod);
        assertEquals(property._setter, method);
        assertEquals(property._skipNulls, false);
        assertNull(property._valueDeserializer);
        assertNull(property._nullProvider);
    }

    @Test
    public void testWithValueDeserializer() throws Exception {
        String name = "name";
        Annotation annotation = mock(Annotation.class);
        Method method = mock(Method.class);
        AnnotatedMethod annotatedMethod = mock(AnnotatedMethod.class);
        when(annotatedMethod.getAnnotated()).thenReturn(method);

        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        NullProvider nullProvider = mock(NullProvider.class);
        BasicDeserializer.ValueDeserializer deserializer = mock(BasicDeserializer.ValueDeserializer.class);

        MethodProperty property = new MethodProperty(null, type, typeDeserializer, contextAnnotations, annotatedMethod);
        MethodProperty propertyWithDeserializer = new MethodProperty(property, deserializer, nullProvider);

        assertEquals(propertyWithDeserializer.getName(), name);
        assertEquals(propertyWithDeserializer.getType(), type);
        assertEquals(propertyWithDeserializer.getTypeHandler(), typeDeserializer);
        assertEquals(propertyWithDeserializer.getMember(), annotatedMethod);
        assertEquals(propertyWithDeserializer._annotated, annotatedMethod);
        assertEquals(propertyWithDeserializer._setter, method);
        assertEquals(propertyWithDeserializer._skipNulls, false);
        assertEquals(propertyWithDeserializer._valueDeserializer, deserializer);
        assertEquals(propertyWithDeserializer._nullProvider, nullProvider);
    }

    @Test
    public void testWithNullProvider() throws Exception {
        String name = "name";
        Annotation annotation = mock(Annotation.class);
        Method method = mock(Method.class);
        AnnotatedMethod annotatedMethod = mock(AnnotatedMethod.class);
        when(annotatedMethod.getAnnotated()).thenReturn(method);

        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        NullProvider nullProvider = mock(NullProvider.class);

        MethodProperty property = new MethodProperty(null, type, typeDeserializer, contextAnnotations, annotatedMethod);
        MethodProperty propertyWithNullProvider = new MethodProperty(property, nullDeserializer, nullProvider);

        assertEquals(propertyWithNullProvider.getName(), name);
        assertEquals(propertyWithNullProvider.getType(), type);
        assertEquals(propertyWithNullProvider.getTypeHandler(), typeDeserializer);
        assertEquals(propertyWithNullProvider.getMember(), annotatedMethod);
        assertEquals(propertyWithNullProvider._annotated, annotatedMethod);
        assertEquals(propertyWithNullProvider._setter, method);
        assertEquals(propertyWithNullProvider._skipNulls, false);
        assertNull(propertyWithNullProvider._valueDeserializer);
        assertEquals(propertyWithNullProvider._nullProvider, nullProvider);
    }

    @Test
    public void testSetter() throws Exception {
        String name = "name";
        Annotation annotation = mock(Annotation.class);
        Method method = mock(Method.class);
        Object instance = INSTANCE;
        Object value = new Object();

        AnnotatedMethod annotatedMethod = mock(AnnotatedMethod.class);
        when(annotatedMethod.getAnnotated()).thenReturn(method);

        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);

        MethodProperty property = new MethodProperty(null, type, typeDeserializer, contextAnnotations, annotatedMethod);

        property.set(instance, value);

        verify(method).invoke(instance, value);
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        String name = "name";
        Annotation annotation = mock(Annotation.class);
        Method method = mock(Method.class);
        Object instance = INSTANCE;
        Object value = new Object();

        JsonParser parser = mock(JsonParser.class);
        ObjectContext context = mock(ObjectContext.class);

        AnnotatedMethod annotatedMethod = mock(AnnotatedMethod.class);
        when(annotatedMethod.getAnnotated()).thenReturn(method);

        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);

        MethodProperty property = new MethodProperty(null, type, typeDeserializer, contextAnnotations, annotatedMethod);

        when(parser.nextToken()).thenReturn(VALUE_STRING);
        when(parser.getValueAsString()).thenReturn("value");

        property.deserializeAndSet(parser, context, instance);

        verify(method).invoke(instance, "value");
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        String name = "name";
        Annotation annotation = mock(Annotation.class);
        Method method = mock(Method.class);
        Object instance = INSTANCE;
        Object value = new Object();

        JsonParser parser = mock(JsonParser.class);
        ObjectContext context = mock(ObjectContext.class);

        AnnotatedMethod annotatedMethod = mock(AnnotatedMethod.class);
        when(annotatedMethod.getAnnotated()).thenReturn(method);

        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);

        MethodProperty property = new MethodProperty(null, type, typeDeserializer, contextAnnotations, annotatedMethod);

        when(parser.nextToken()).thenReturn(VALUE_STRING);
        when(parser.getValueAsString()).thenReturn("value");

        Object result = property.deserializeSetAndReturn(parser, context, instance);

        verify(method).invoke(instance, "value");
        assertEquals(instance, result);
    }

    @Test
    public void testFixAccess() throws Exception {
        String name = "name";
        Annotation annotation = mock(Annotation.class);
        Method method = mock(Method.class);
        Object instance = INSTANCE;

        AnnotatedMethod annotatedMethod = mock(AnnotatedMethod.class);
        when(annotatedMethod.getAnnotated()).thenReturn(method);

        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);

        MethodProperty property = new MethodProperty(null, type, typeDeserializer, contextAnnotations, annotatedMethod);

        property.fixAccess(null);

        verify(method).fixAccess(true);
    }

    @Test
    public void testReadResolve() throws Exception {
        String name = "name";
        Annotation annotation = mock(Annotation.class);
        Method method = mock(Method.class);
        AnnotatedMethod annotatedMethod = mock(AnnotatedMethod.class);
        when(annotatedMethod.getAnnotated()).thenReturn(method);

        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);

        MethodProperty property = new MethodProperty(null, type, typeDeserializer, contextAnnotations, annotatedMethod);

        MethodProperty resolved = property.readResolve();

        assertEquals(property.getClass(), MethodProperty.class);
        assertEquals(resolved.getClass(), MethodProperty.class);
        assertEquals(resolved._annotated, property._annotated);
        assertEquals(resolved._setter, property._setter);
    }
}