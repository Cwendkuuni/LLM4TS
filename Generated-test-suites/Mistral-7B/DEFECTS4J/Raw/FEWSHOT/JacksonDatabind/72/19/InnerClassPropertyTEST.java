package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.*;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class InnerClassPropertyTest {

    private SettableBeanProperty delegate;
    private AnnotatedConstructor annotatedConstructor;
    private Constructor<?> constructor;
    private InnerClassProperty property;
    private JsonDeserializer<?> deserializer;
    private PropertyName newName;
    private Object instance;
    private JsonParser parser;
    private DeserializationContext context;
    private AnnotationIntrospector introspector;

    @SuppressWarnings("unchecked")
    @Before
    public void setup() throws Exception {
        initMocks(this);

        delegate = mock(SettableBeanProperty.class);
        when(delegate.getMember()).thenReturn(mock(AnnotatedMember.class));
        when(delegate.withName(any(PropertyName.class))).then(i -> i.getArgument(0));
        when(delegate.withValueDeserializer(any(JsonDeserializer.class))).then(i -> i.getArgument(0));

        annotatedConstructor = mock(AnnotatedConstructor.class);
        constructor = mock(Constructor.class);
        when(constructor.getDeclaringClass()).thenReturn(mock(Class.class));
        when(constructor.newInstance(any(Object.class))).thenAnswer(i -> i.callRealMethod());
        doReturn(constructor).when(annotatedConstructor).getDelegate();
        doReturn(annotatedConstructor).when(property)._annotated;

        property = new InnerClassProperty(delegate, constructor);

        deserializer = mock(JsonDeserializer.class);
        doReturn(deserializer).when(property).getPropertyConfig(any(AnnotatedMethod.class)).getValueDeserializer();

        newName = mock(PropertyName.class);
        property = new InnerClassProperty(property, newName);

        instance = mock(Object.class);
        parser = mock(JsonParser.class);
        context = mock(DeserializationContext.class);
        context.getDefaultAnnotationIntrospector().assignAnnotations(instance, new Class[0], null);
        introspector = mock(AnnotationIntrospector.class);
        doReturn(introspector).when(context).findPropertyAnnotation(instance, SettableBeanProperty.class);
    }

    @Test
    public void testInnerClassPropertyConstruction() {
        assertEquals(delegate, property._delegate);
        assertEquals(constructor, property._creator);
        assertNull(property._annotated);
    }

    @Test
    public void testInnerClassPropertyWithAnnotatedConstructor() {
        when(constructor.isAnnotationPresent(any(Annotation.class))).thenReturn(true);
        when(constructor.getAnnotation(any(Annotation.class))).thenReturn(mock(Annotation.class));

        property = new InnerClassProperty(delegate, constructor);
        property = new InnerClassProperty(property, annotatedConstructor);

        verify(annotatedConstructor, times(1)).getAnnotated();
        verify(property, times(1))._creator = annotatedConstructor.getAnnotated();
    }

    @Test
    public void testInnerClassPropertyWithJsonDeserializer() {
        when(deserializer.getClass()).thenReturn(JsonDeserializer.class);
        when(property.getPropertyConfig(any(AnnotatedMethod.class))).thenReturn(mock(BeanProperty.BasePropertyConfig.class));

        property = new InnerClassProperty(property, deserializer);

        verify(deserializer, times(1)).getValueDeserializer();
        verify(property, times(1)).getPropertyConfig(any(AnnotatedMethod.class)).setValueDeserializer(deserializer);
    }

    @Test
    public void testInnerClassPropertyWithPropertyName() {
        property = new InnerClassProperty(property, newName);

        verify(property, times(1)).withName(newName);
    }

    @Test
    public void testAssignIndex() throws IOException {
        property.assignIndex(1);
        verify(delegate, times(1)).assignIndex(1);
    }

    @Test
    public void testGetPropertyIndex() throws IOException {
        when(delegate.getPropertyIndex()).thenReturn(1);

        assertEquals(1, property.getPropertyIndex());
    }

    @Test
    public void testGetAnnotation() {
        when(delegate.getAnnotation(any(Annotation.class))).thenReturn(mock(Annotation.class));

        assertEquals(mock(Annotation.class), property.getAnnotation(any(Annotation.class)));
    }

    @Test
    public void testGetMember() {
        AnnotatedMember member = mock(AnnotatedMember.class);

        when(delegate.getMember()).thenReturn(member);

        assertEquals(member, property.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object value = new Object();

        when(parser.readValueAs(any(Class.class))).thenReturn(value);

        property.deserializeAndSet(parser, context, instance);

        verify(delegate, times(1)).set(instance, value);
    }

    @Test
    public void testDeserializeAndSetWithValueTypeDeserializer() throws IOException {
        Object valueType = new Object();
        JsonDeserializer<Object> valueTypeDeserializer = new StdSerializer<Object>(Object.class) {};

        when(parser.readValueAs(valueType.getClass())).thenReturn(valueType);
        when(parser.getValueAsToken()).thenReturn(mock(JsonToken.class));
        when(property._valueTypeDeserializer).thenReturn(valueTypeDeserializer);

        property.deserializeAndSet(parser, context, instance);

        verify(valueTypeDeserializer, times(1)).deserialize(parser, context, valueType);
        verify(delegate, times(1)).set(instance, valueType);
    }

    @Test
    public void testDeserializeAndSetWithValueDeserializer() throws IOException {
        Object value = new Object();
        JsonDeserializer<Object> valueDeserializer = new StdSerializer<Object>(Object.class) {};

        when(parser.readValueAs(value.getClass())).thenReturn(value);

        property.deserializeAndSet(parser, context, instance);

        verify(valueDeserializer, times(1)).deserialize(parser, context, value);
        verify(delegate, times(1)).set(instance, value);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object value = new Object();

        when(parser.readValueAs(any(Class.class))).thenReturn(value);
        when(parser.getValueAsToken()).thenReturn(mock(JsonToken.class));

        assertEquals(value, property.deserializeSetAndReturn(parser, context, instance));
    }

    @Test
    public void testSet() throws IOException {
        Object value = new Object();

        property.set(instance, value);

        verify(delegate, times(1)).set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object value = new Object();
        Object result = new Object();

        when(delegate.setAndReturn(instance, value)).thenReturn(result);

        assertEquals(result, property.setAndReturn(instance, value));
    }

    @Test
    public void testReadResolve() throws IOException {
        Object resolved = new Object();

        when(property._annotated).thenReturn(annotatedConstructor);

        assertEquals(resolved, property.readResolve());

        verify(property, times(1)).<InnerClassProperty>constructor(property, annotatedConstructor);
    }

    @Test
    public void testWriteReplace() throws IOException {
        Object wrapper = new Object();

        when(property._annotated).thenReturn(null);

        assertEquals(wrapper, property.writeReplace());

        verify(property, times(1)).<InnerClassProperty>constructor(property, new AnnotatedConstructor(null, constructor, null, null));
    }
}