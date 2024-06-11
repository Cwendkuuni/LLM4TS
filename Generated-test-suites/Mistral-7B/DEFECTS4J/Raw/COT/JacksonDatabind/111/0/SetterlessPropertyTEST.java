package com.yourpackage; // replace with your testing package

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.introspect.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SetterlessPropertyTest {

    private static final Object TEST_CLASS_INSTANCE = new Object() {};

    @org.junit.Test
    public void testConstructor() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);

        SettableBeanProperty settableBeanProperty = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        assertEquals(SetterlessProperty.class, settableBeanProperty.getClass());
        assertEquals(propDef, settableBeanProperty.getBeanPropertyDefinition());
        assertEquals(type, settableBeanProperty.getType());
        assertEquals(typeDeser, settableBeanProperty.getTypeDeserializer());
        assertEquals(contextAnnotations, settableBeanProperty.getContextAnnotations());
        assertEquals(method, settableBeanProperty.getAnnotatedMember());
    }

    @org.junit.Test
    public void testDeserializeAndSet() throws IOException {
        Object instance = TEST_CLASS_INSTANCE;
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.getValueAsString()).thenReturn("testValue");

        Method getter = mock(Method.class);
        when(getter.invoke(instance, (Object[]) null)).thenReturn(new ArrayList<>());

        SettableBeanProperty settableBeanProperty = new SetterlessProperty(null, null, null, null, getter);

        settableBeanProperty.deserializeAndSet(parser, context, instance);

        verify(instance).getClass();
        verify(getter).invoke(instance, (Object[]) null);
        verify(parser).nextToken();
        verify(context).reportBadDefinition(settableBeanProperty.getType(), isA(String.class));
    }

    @org.junit.Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object instance = TEST_CLASS_INSTANCE;
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.getValueAsString()).thenReturn("testValue");

        Method getter = mock(Method.class);
        when(getter.invoke(instance, (Object[]) null)).thenReturn(new ArrayList<>());

        SettableBeanProperty settableBeanProperty = new SetterlessProperty(null, null, null, null, getter);

        settableBeanProperty.deserializeSetAndReturn(parser, context, instance);

        verify(instance).getClass();
        verify(getter).invoke(instance, (Object[]) null);
        verify(parser).nextToken();
        verifyNoMoreInteractions(instance);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
        Object instance = TEST_CLASS_INSTANCE;
        SettableBeanProperty settableBeanProperty = new SetterlessProperty(null, null, null, null, null);
        settableBeanProperty.set(instance, new Object());
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn() throws IOException {
        Object instance = TEST_CLASS_INSTANCE;
        SettableBeanProperty settableBeanProperty = new SetterlessProperty(null, null, null, null, null);
        settableBeanProperty.setAndReturn(instance, new Object());
    }

    @org.junit.Test
    public void testGetAnnotation() {
        Class<Annotation> annotationClass = mock(Annotation.class);
        AnnotatedMethod annotatedMethod = mock(AnnotatedMethod.class);

        when(annotatedMethod.getAnnotation(annotationClass)).thenReturn(mock(annotationClass));

        SettableBeanProperty settableBeanProperty = new SetterlessProperty(null, null, null, null, annotatedMethod);

        assertEquals(mock(annotationClass), settableBeanProperty.getAnnotation(annotationClass));
    }

    @org.junit.Test
    public void testGetMember() {
        AnnotatedMember annotatedMember = mock(AnnotatedMember.class);

        when(annotatedMember.getAnnotated()).thenReturn(mock(AnnotatedMethod.class));

        SettableBeanProperty settableBeanProperty = new SetterlessProperty(null, null, null, null, annotatedMember);

        assertEquals(annotatedMember, settableBeanProperty.getMember());
    }

    @org.junit.Test
    public void testFixAccess() {
        DeserializationConfig config = mock(DeserializationConfig.class);

        SettableBeanProperty settableBeanProperty = new SetterlessProperty(null, null, null, null, null);
        settableBeanProperty.fixAccess(config);

        verify(config).isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS);
        verifyZeroInteractions(settableBeanProperty._annotated);
    }

    @org.junit.Test
    public void testWithName() {
        PropertyName newName = mock(PropertyName.class);
        SettableBeanProperty settableBeanProperty = new SetterlessProperty(null, null, null, null, null);

        SettableBeanProperty result = settableBeanProperty.withName(newName);

        assertEquals(newName, result.getName());
        assertEquals(settableBeanProperty, result.getBeanPropertyDefinition());
        assertEquals(settableBeanProperty.getType(), result.getType());
        assertEquals(settableBeanProperty.getTypeDeserializer(), result.getTypeDeserializer());
        assertEquals(settableBeanProperty.getContextAnnotations(), result.getContextAnnotations());
        assertEquals(settableBeanProperty.getAnnotatedMember(), result.getAnnotatedMember());
    }

    @org.junit.Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);
        SettableBeanProperty settableBeanProperty = new SetterlessProperty(null, null, null, null, null);

        SettableBeanProperty result = settableBeanProperty.withValueDeserializer(deserializer);

        assertEquals(deserializer, result.getValueDeserializer());
        assertEquals(settableBeanProperty, result.getBeanPropertyDefinition());
        assertEquals(settableBeanProperty.getType(), result.getType());
        assertEquals(settableBeanProperty.getTypeDeserializer(), result.getTypeDeserializer());
        assertEquals(settableBeanProperty.getContextAnnotations(), result.getContextAnnotations());
        assertEquals(settableBeanProperty.getAnnotatedMember(), result.getAnnotatedMember());
    }

    @org.junit.Test
    public void testWithNullProvider() {
        NullValueProvider nva = mock(NullValueProvider.class);
        SettableBeanProperty settableBeanProperty = new SetterlessProperty(null, null, null, null, null);

        SettableBeanProperty result = settableBeanProperty.withNullProvider(nva);

        assertEquals(nva, result.getNullProvider());
        assertEquals(settableBeanProperty, result.getBeanPropertyDefinition());
        assertEquals(settableBeanProperty.getType(), result.getType());
        assertEquals(settableBeanProperty.getTypeDeserializer(), result.getTypeDeserializer());
        assertEquals(settableBeanProperty.getContextAnnotations(), result.getContextAnnotations());
        assertEquals(settableBeanProperty.getAnnotatedMember(), result.getAnnotatedMember());
    }
}