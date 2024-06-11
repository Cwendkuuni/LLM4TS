package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class SetterlessPropertyTest {

    private SetterlessProperty property;

    @Mock
    private BeanPropertyDefinition propDef;

    @Mock
    private JavaType type;

    @Mock
    private TypeDeserializer typeDeser;

    @Mock
    private Annotations contextAnnotations;

    @Mock
    private AnnotatedMethod method;

    @Mock
    private SetterlessProperty src;

    @Mock
    private JsonDeserializer<?> deser;

    @Mock
    private NullValueProvider nva;

    @Mock
    private PropertyName newName;

    @Mock
    private DeserializationConfig config;

    @Mock
    private Class<Annotation> acls;

    @Mock
    private AnnotatedMember annotatedMember;

    @Mock
    private JsonParser p;

    @Mock
    private DeserializationContext ctxt;

    @Mock
    private Object instance;

    @Mock
    private Object toModify;

    @Before
    public void setUp() {
        property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
    }

    @Test
    public void testWithNullProvider() {
        SettableBeanProperty beanProp = property.withNullProvider(nva);
        assertEquals(property._valueDeserializer, beanProp._valueDeserializer);
        assertEquals(nva, beanProp._nullProvider);
    }

    @Test
    public void testWithNewName() {
        SettableBeanProperty beanProp = property.withName(newName);
        assertEquals(newName, beanProp.getFullName());
        assertEquals(property._valueDeserializer, beanProp._valueDeserializer);
    }

    @Test
    public void testFixAccess() {
        when(config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS)).thenReturn(true);
        property.fixAccess(config);
        verify(method).fixAccess(true);
    }

    @Test
    public void testGetAnnotation() {
        property._annotated = annotatedMember;
        when(annotatedMember.getAnnotation(acls)).thenReturn(mock(acls));
        Annotation annotation = property.getAnnotation(acls);
        assertEquals(acls, annotation.getClass());
    }

    @Test
    public void testGetMember() {
        property._annotated = annotatedMember;
        AnnotatedMember member = property.getMember();
        assertEquals(annotatedMember, member);
    }

    @Test
    public void testDeserializeAndSetWithNullToken() throws IOException {
        when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
        property.deserializeAndSet(p, ctxt, instance);
        verify(p, never()).nextToken();
    }

    @Test
    public void testDeserializeAndSetWithTypeDeserializer() throws IOException {
        when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
        when(property._valueTypeDeserializer).thenReturn(typeDeser);

        property.deserializeAndSet(p, ctxt, instance);

        verify(ctxt).reportBadDefinition(property.getType(),
                String.format("Problem deserializing 'setterless' property (\"%s\"): no way to handle typed deser with setterless yet",
                        property.getName()));
    }

    @Test
    public void testDeserializeAndSetWithNullToModify() throws IOException {
        when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(instance.getClass()).thenReturn(Object.class);
        when(_getter.invoke(instance, (Object[]) null)).thenReturn(null);

        property.deserializeAndSet(p, ctxt, instance);

        verify(ctxt).reportBadDefinition(property.getType(),
                String.format("Problem deserializing 'setterless' property '%s': get method returned null",
                        property.getName()));
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(instance.getClass()).thenReturn(Object.class);
        when(_getter.invoke(instance, (Object[]) null)).thenReturn(toModify);

        property.deserializeAndSet(p, ctxt, instance);

        verify(property._valueDeserializer).deserialize(p, ctxt, toModify);
    }

    @Test
    public void testDeserializeAndReturn() throws IOException {
        when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(instance.getClass()).thenReturn(Object.class);
        when(_getter.invoke(instance, (Object[]) null)).thenReturn(toModify);

        Object result = property.deserializeSetAndReturn(p, ctxt, instance);

        verify(property._valueDeserializer).deserialize(p, ctxt, toModify);
        assertEquals(instance, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
        property.set(instance, null);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        property.setAndReturn(instance, null);
        verify(property).set(instance, null);
        verify(property, never()).deserializeAndSet(p, ctxt, instance);
    }
}