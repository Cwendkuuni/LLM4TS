package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class InnerClassPropertyTest {

    private InnerClassProperty innerClassProperty;
    private SettableBeanProperty delegate;
    private Constructor<?> ctor;
    private AnnotatedConstructor annotated;
    private JsonDeserializer<?> deser;
    private PropertyName newName;
    private ObjectMapper objectMapper;
    private JsonParser jp;
    private DeserializationContext ctxt;
    private Object bean;
    private JsonToken t;
    private Object value;
    private ValueInstantiator valueDeserializer;
    private Object instance;

    @Before
    public void setUp() {
        delegate = new SettableBeanProperty() {
            private static final long serialVersionUID = 1L;

            @Override
            public final void set(Object instance, Object value) throws IOException {
            }

            @Override
            public Object setAndReturn(Object instance, Object value) throws IOException {
                return null;
            }

            @Override
            public Object deserializeSetAndReturn(JsonParser arg0, DeserializationContext arg1, Object arg2)
                    throws IOException {
                return null;
            }

            @Override
            public void deserializeAndSet(JsonParser arg0, DeserializationContext arg1, Object arg2)
                    throws IOException {
            }

            @Override
            public <A extends Annotation> A getAnnotation(Class<A> acls) {
                return null;
            }

            @Override
            public AnnotatedMember getMember() {
                return null;
            }

            @Override
            public JsonDeserializer<Object> getValueDeserializer() {
                return null;
            }

            @Override
            public void setValueDeserializer(JsonDeserializer<?> deser) {

            }
        };
        ctor = new Constructor(" + delegate + ");
        innerClassProperty = new InnerClassProperty(delegate, ctor);
        annotated = new AnnotatedConstructor(null, ctor, null, null);
        deser = new JsonDeserializer() {
            
            @Override
            public Object deserialize(JsonParser arg0, DeserializationContext arg1) throws IOException {
                return null;
            }
        };
        newName = new PropertyName("");
        objectMapper = new ObjectMapper();
        jp = objectMapper.getFactory().createParser("");
        ctxt = objectMapper.getDeserializationContext();
        bean = new Object();
        t = JsonToken.VALUE_NULL;
        value = new Object();
        valueDeserializer = new ValueInstantiator() {
            
            @Override
            public Object createUsingDefault(DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        instance = new Object();
    }

    @Test
    public void testConstructor() {
        assertNotNull(innerClassProperty);
        assertSame(delegate, innerClassProperty._delegate);
        assertSame(ctor, innerClassProperty._creator);
    }

    @Test
    public void testConstructorWithAnnotatedConstructor() {
        InnerClassProperty property = new InnerClassProperty(innerClassProperty, annotated);
        assertNotNull(property);
        assertSame(delegate, property._delegate);
        assertSame(annotated, property._annotated);
        assertSame(ctor, property._creator);
    }

    @Test
    public void testConstructorWithDeser() {
        InnerClassProperty property = new InnerClassProperty(innerClassProperty, deser);
        assertNotNull(property);
        assertSame(delegate, property._delegate);
        assertSame(ctor, property._creator);
    }

    @Test
    public void testConstructorWithNewName() {
        InnerClassProperty property = new InnerClassProperty(innerClassProperty, newName);
        assertNotNull(property);
        assertSame(delegate, property._delegate);
        assertSame(ctor, property._creator);
    }

    @Test
    public void testWithNewName() {
        InnerClassProperty property = innerClassProperty.withName(newName);
        assertNotNull(property);
        assertSame(delegate, property._delegate);
        assertSame(ctor, property._creator);
    }

    @Test
    public void testWithDeser() {
        InnerClassProperty property = innerClassProperty.withValueDeserializer(deser);
        assertNotNull(property);
        assertSame(delegate, property._delegate);
        assertSame(ctor, property._creator);
    }

    @Test
    public void testAssignIndex() {
        innerClassProperty.assignIndex(123);
    }

    @Test
    public void testGetPropertyIndex() {
        assertEquals(0, innerClassProperty.getPropertyIndex());
    }

    @Test
    public void testGetAnnotation() {
        innerClassProperty.getAnnotation(Before.class);
    }

    @Test
    public void testGetMember() {
        innerClassProperty.getMember();
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        innerClassProperty.deserializeAndSet(jp, ctxt, bean);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        innerClassProperty.deserializeSetAndReturn(jp, ctxt, instance);
    }

    @Test
    public void testSet() throws IOException {
        innerClassProperty.set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        assertNull(innerClassProperty.setAndReturn(instance, value));
    }

    @Test
    public void testReadResolve() {
        innerClassProperty.readResolve();
    }

    @Test
    public void testWriteReplace() {
        innerClassProperty.writeReplace();
    }
}