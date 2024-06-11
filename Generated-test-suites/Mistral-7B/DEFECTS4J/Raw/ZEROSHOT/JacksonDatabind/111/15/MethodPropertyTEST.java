package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

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

import org.junit.Test;
import org.mockito.Mockito;

/**
 * Test for MethodProperty class.
 */
public class MethodPropertyTest {

    @Test
    public void testMethodPropertyConstruction() throws Exception {
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        JavaType type = Mockito.mock(JavaType.class);
        TypeDeserializer typeDeser = Mockito.mock(TypeDeserializer.class);
        Annotations contextAnnotations = Mockito.mock(Annotations.class);
        AnnotatedMethod method = Mockito.mock(AnnotatedMethod.class);

        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);

        assertEquals(propDef, methodProperty.getBeanPropertyDefinition());
        assertEquals(type, methodProperty.getType());
        assertEquals(typeDeser, methodProperty.getTypeDeserializer());
        assertEquals(contextAnnotations, methodProperty.getContextAnnotations());
        assertEquals(method, methodProperty.getAnnotated());
        assertEquals(method, methodProperty.getSetter());
        assertFalse(methodProperty.isSkipNulls());
    }

    @Test
    public void testMethodPropertyWithName() throws Exception {
        MethodProperty methodProperty = new MethodProperty(
                Mockito.mock(MethodProperty.class), Mockito.mock(PropertyName.class));

        assertEquals(methodProperty.getBeanPropertyDefinition(), methodProperty.getBeanProperty());
        assertEquals(methodProperty.getType(), methodProperty.getType());
        assertEquals(methodProperty.getTypeDeserializer(), methodProperty.getTypeDeserializer());
        assertEquals(methodProperty.getContextAnnotations(), methodProperty.getContextAnnotations());
        assertEquals(methodProperty.getAnnotated(), methodProperty.getAnnotated());
        assertEquals(methodProperty.getSetter(), methodProperty.getSetter());
        assertFalse(methodProperty.isSkipNulls());
        assertEquals("newName", methodProperty.getName());
    }

    @Test
    public void testMethodPropertyWithValueDeserializer() throws Exception {
        JsonDeserializer<?> jsonDeserializer = Mockito.mock(JsonDeserializer.class);
        MethodProperty methodProperty = new MethodProperty(
                Mockito.mock(MethodProperty.class), jsonDeserializer, Mockito.mock(NullValueProvider.class));

        assertEquals(methodProperty.getBeanPropertyDefinition(), methodProperty.getBeanProperty());
        assertEquals(methodProperty.getType(), methodProperty.getType());
        assertEquals(methodProperty.getTypeDeserializer(), jsonDeserializer);
        assertEquals(methodProperty.getContextAnnotations(), methodProperty.getContextAnnotations());
        assertEquals(methodProperty.getAnnotated(), methodProperty.getAnnotated());
        assertEquals(methodProperty.getSetter(), methodProperty.getSetter());
        assertFalse(methodProperty.isSkipNulls());
        assertNotSame(methodProperty.getValueDeserializer(), jsonDeserializer);
    }

    @Test
    public void testMethodPropertyWithNullProvider() throws Exception {
        NullValueProvider nullValueProvider = Mockito.mock(NullValueProvider.class);
        MethodProperty methodProperty = new MethodProperty(
                Mockito.mock(MethodProperty.class), Mockito.mock(JsonDeserializer.class), nullValueProvider);

        assertEquals(methodProperty.getBeanPropertyDefinition(), methodProperty.getBeanProperty());
        assertEquals(methodProperty.getType(), methodProperty.getType());
        assertEquals(methodProperty.getTypeDeserializer(), Mockito.mock(TypeDeserializer.class));
        assertEquals(methodProperty.getContextAnnotations(), methodProperty.getContextAnnotations());
        assertEquals(methodProperty.getAnnotated(), methodProperty.getAnnotated());
        assertEquals(methodProperty.getSetter(), methodProperty.getSetter());
        assertFalse(methodProperty.isSkipNulls());
        assertEquals(nullValueProvider, methodProperty.getNullProvider());
    }

    @Test
    public void testMethodPropertyFixAccess() throws Exception {
        DeserializationConfig config = Mockito.mock(DeserializationConfig.class);
        AnnotatedMethod annotatedMethod = Mockito.mock(AnnotatedMethod.class);

        MethodProperty methodProperty = new MethodProperty(
                Mockito.mock(MethodProperty.class), annotatedMethod);
        methodProperty.fixAccess(config);

        assertTrue(annotatedMethod.isAccessible());
    }

    @Test
    public void testMethodPropertyGetAnnotation() throws Exception {
        Class<Annotation> annotationType = Mockito.mock(Annotation.class);
        AnnotatedMethod annotatedMethod = Mockito.mock(AnnotatedMethod.class);
        Annotation annotation = Mockito.mock(Annotation.class);

        when(annotatedMethod.getAnnotation(annotationType)).thenReturn(annotation);

        MethodProperty methodProperty = new MethodProperty(
                Mockito.mock(BeanPropertyDefinition.class), Mockito.mock(JavaType.class),
                Mockito.mock(TypeDeserializer.class), Mockito.mock(Annotations.class), annotatedMethod);

        assertEquals(annotation, methodProperty.getAnnotation(annotationType));
    }

    @Test
    public void testMethodPropertyGetMember() throws Exception {
        AnnotatedMember annotatedMember = Mockito.mock(AnnotatedMember.class);

        MethodProperty methodProperty = new MethodProperty(
                Mockito.mock(BeanPropertyDefinition.class), Mockito.mock(JavaType.class),
                Mockito.mock(TypeDeserializer.class), Mockito.mock(Annotations.class), Mockito.mock(AnnotatedMethod.class));

        assertEquals(annotatedMember, methodProperty.getMember());
    }

    @Test
    public void testMethodPropertyDeserializeAndSet() throws Exception {
        Object instance = Mockito.mock(Object.class);
        AnnotatedMethod annotatedMethod = Mockito.mock(AnnotatedMethod.class);
        Method method = Mockito.mock(Method.class);
        Object value = Mockito.mock(Object.class);
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = Mockito.mock(TypeDeserializer.class);
        NullValueProvider nullValueProvider = Mockito.mock(NullValueProvider.class);

        when(annotatedMethod.getAnnotated()).thenReturn(method);
        when(method.getName()).thenReturn("setName");
        when(method.getParameterTypes()).thenReturn(new Class[] { String.class });
        when(method.invoke(instance, anyObject())).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);

        MethodProperty methodProperty = new MethodProperty(
                Mockito.mock(BeanPropertyDefinition.class), null, null, Mockito.mock(Annotations.class), annotatedMethod);

        methodProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        verify(jsonParser).nextToken();
        verify(jsonParser).nextValue();
        verify(jsonParser).getValueAsString();
        verify(instance).setName("value");
    }

    @Test
    public void testMethodPropertyDeserializeSetAndReturn() throws Exception {
        Object instance = Mockito.mock(Object.class);
        AnnotatedMethod annotatedMethod = Mockito.mock(AnnotatedMethod.class);
        Method method = Mockito.mock(Method.class);
        Object value = Mockito.mock(Object.class);
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = Mockito.mock(TypeDeserializer.class);
        NullValueProvider nullValueProvider = Mockito.mock(NullValueProvider.class);

        when(annotatedMethod.getAnnotated()).thenReturn(method);
        when(method.getName()).thenReturn("setName");
        when(method.getParameterTypes()).thenReturn(new Class[] { String.class });
        when(method.invoke(instance, anyObject())).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);

        MethodProperty methodProperty = new MethodProperty(
                Mockito.mock(BeanPropertyDefinition.class), null, null, Mockito.mock(Annotations.class), annotatedMethod);

        Object result = methodProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

        verify(jsonParser).nextToken();
        verify(jsonParser).nextValue();
        verify(jsonParser).getValueAsString();
        verify(instance).setName("value");
        assertEquals("value", result);
    }

    @Test
    public void testMethodPropertySet() throws Exception {
        Object instance = Mockito.mock(Object.class);
        AnnotatedMethod annotatedMethod = Mockito.mock(AnnotatedMethod.class);
        Method method = Mockito.mock(Method.class);
        Object value = Mockito.mock(Object.class);

        when(annotatedMethod.getAnnotated()).thenReturn(method);
        when(method.getName()).thenReturn("setName");
        when(method.getParameterTypes()).thenReturn(new Class[] { String.class });

        MethodProperty methodProperty = new MethodProperty(
                Mockito.mock(BeanPropertyDefinition.class), null, null, Mockito.mock(Annotations.class), annotatedMethod);

        methodProperty.set(instance, "value");

        verify(instance).setName("value");
    }

    @Test
    public void testMethodPropertySetAndReturn() throws Exception {
        Object instance = Mockito.mock(Object.class);
        AnnotatedMethod annotatedMethod = Mockito.mock(AnnotatedMethod.class);
        Method method = Mockito.mock(Method.class);
        Object value = Mockito.mock(Object.class);

        when(annotatedMethod.getAnnotated()).thenReturn(method);
        when(method.getName()).thenReturn("setName");
        when(method.getParameterTypes()).thenReturn(new Class[] { String.class });
        when(method.invoke(instance, anyObject())).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);

        MethodProperty methodProperty = new MethodProperty(
                Mockito.mock(BeanPropertyDefinition.class), null, null, Mockito.mock(Annotations.class), annotatedMethod);

        Object result = methodProperty.setAndReturn(instance, "value");

        verify(instance).setName("value");
        assertEquals("value", result);
    }

    @Test
    public void testMethodPropertyReadResolve() throws Exception {
        MethodProperty methodProperty = new MethodProperty(
                Mockito.mock(MethodProperty.class), Mockito.mock(AnnotatedMethod.class));

        Object result = methodProperty.readResolve();

        assertEquals(methodProperty.getBeanPropertyDefinition(), result.getBeanPropertyDefinition());
        assertEquals(methodProperty.getType(), result.getType());
        assertEquals(methodProperty.getTypeDeserializer(), result.getTypeDeserializer());
        assertEquals(methodProperty.getContextAnnotations(), result.getContextAnnotations());
        assertEquals(methodProperty.getAnnotated(), result.getAnnotated());
        assertEquals(methodProperty.getSetter(), result.getSetter());
        assertFalse(methodProperty.isSkipNulls());
    }
}