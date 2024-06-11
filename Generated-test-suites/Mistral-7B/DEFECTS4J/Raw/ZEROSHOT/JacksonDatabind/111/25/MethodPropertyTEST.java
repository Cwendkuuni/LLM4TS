package com.example.junit;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

/**
 * Test class for MethodProperty.java
 */
public class MethodPropertyTest {

    private MethodProperty methodProperty;
    private AnnotatedMethod annotatedMethod;
    private Method setter;
    private Object instance;

    @Before
    public void setUp() throws Exception {
        annotatedMethod = mock(AnnotatedMethod.class);
        setter = mock(Method.class);
        when(annotatedMethod.getAnnotated()).thenReturn(setter);

        JavaType javaType = mock(JavaType.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);

        methodProperty = new MethodProperty(null, javaType, typeDeserializer, contextAnnotations, annotatedMethod);
        instance = new Object();
    }

    @Test
    public void testGetAnnotation() throws Exception {
        when(annotatedMethod.getAnnotation(Mockito.eq(Annotation.class)))
                .thenReturn(Mockito.mock(Annotation.class));

        Annotation result = methodProperty.getAnnotation(Annotation.class);
        assertSame("Expected annotation", Mockito.mock(Annotation.class), result);
    }

    @Test
    public void testGetMember() throws Exception {
        ArgumentCaptor<AnnotatedMember> annotatedMemberCaptor = ArgumentCaptor.forClass(AnnotatedMember.class);

        methodProperty.set(instance, "testValue");
        methodProperty.fixAccess(Mockito.mock(DeserializationConfig.class));

        methodProperty.deserializeAndSet(Mockito.mock(JsonParser.class), Mockito.mock(DeserializationContext.class), instance);

        Mockito.verify(annotatedMemberCaptor.capture()).fixAccess(Mockito.eq(true));
        AnnotatedMember annotatedMember = annotatedMemberCaptor.getValue();
        assertSame("Expected annotated member", annotatedMethod, annotatedMember);
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        Object value = "testValue";
        when(setter.invoke(instance, Mockito.anyObject())).then(invocationOnMock -> value);

        JsonParser jsonParser = mock(JsonParser.class);
        when(jsonParser.getValueAsString()).thenReturn("testValue");

        methodProperty.deserializeAndSet(jsonParser, Mockito.mock(DeserializationContext.class), instance);

        assertSame("Expected instance with set value", value, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        Object value = "testValue";
        when(setter.invoke(instance, Mockito.anyObject())).then(invocationOnMock -> value);

        JsonParser jsonParser = mock(JsonParser.class);
        when(jsonParser.getValueAsString()).thenReturn("testValue");

        Object result = methodProperty.deserializeSetAndReturn(jsonParser, Mockito.mock(DeserializationContext.class), instance);

        assertSame("Expected instance with set value", value, result);
    }

    @Test
    public void testSet() throws Exception {
        Object value = "testValue";

        methodProperty.set(instance, value);

        assertSame("Expected instance with set value", value, instance);
    }

    @Test
    public void testSetAndReturn() throws Exception {
        Object value = "testValue";

        when(setter.invoke(instance, Mockito.anyObject())).then(invocationOnMock -> value);

        Object result = methodProperty.setAndReturn(instance, value);

        assertSame("Expected instance with set value", value, result);
    }

    @Test
    public void testInvokeSetterNullValue() throws Exception {
        when(setter.invoke(instance, Mockito.anyObject())).thenThrow(new IOException());

        methodProperty.set(instance, null);

        assertNull("Expected null instance", instance);
    }

    @Test
    public void testInvokeSetterNonNullValue() throws Exception {
        Object value = new Object();
        when(setter.invoke(instance, Mockito.anyObject())).thenReturn(value);

        methodProperty.set(instance, null);
        methodProperty.set(instance, value);

        assertSame("Expected instance with set value", value, instance);
    }

    @Test
    public void testInvokeSetterWithException()
            throws IOException, IllegalAccessException, InvocationTargetException {
        Object value = new Object();
        when(setter.invoke(instance, Mockito.anyObject()))
                .thenThrow(new IOException("Test IOException"));

        methodProperty.set(instance, null);
        methodProperty.set(instance, value);

        assertSame("Expected instance with set value", value, instance);
    }

    @Test
    public void testReadResolve() throws Exception {
        MethodProperty methodProperty2 = new MethodProperty(methodProperty, setter);

        Object result = methodProperty2.readResolve();

        assertSame("Expected method property", methodProperty, result);
    }
}