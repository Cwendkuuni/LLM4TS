package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.ClassUtil;

import static org.junit.Assert.*;

public class InnerClassPropertyTest {

    private InnerClassProperty innerClassProperty;

    @Before
    public void setUp() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = new Constructor<>();
        innerClassProperty = new InnerClassProperty(delegate, ctor);
    }

    @Test
    public void testConstructor() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = new Constructor<>();
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, ctor);

        assertSame(delegate, innerClassProperty._delegate);
        assertSame(ctor, innerClassProperty._creator);
    }

    @Test
    public void testConstructorWithAnnotated() {
        InnerClassProperty src = new InnerClassProperty(innerClassProperty, new AnnotatedConstructor(null, null, null, null));
        AnnotatedConstructor ann = new AnnotatedConstructor(null, null, null, null);
        InnerClassProperty innerClassProperty = new InnerClassProperty(src, ann);

        assertSame(src._delegate, innerClassProperty._delegate);
        assertSame(ann, innerClassProperty._annotated);
        assertSame(ann.getAnnotated(), innerClassProperty._creator);
    }

    @Test
    public void testConstructorWithDeserializer() {
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        InnerClassProperty src = new InnerClassProperty(innerClassProperty, deser);
        InnerClassProperty innerClassProperty = new InnerClassProperty(src, deser);

        assertSame(src._delegate.withValueDeserializer(deser), innerClassProperty._delegate);
        assertSame(src._creator, innerClassProperty._creator);
    }

    @Test
    public void testConstructorWithNewName() {
        PropertyName newName = new PropertyName("");
        InnerClassProperty src = new InnerClassProperty(innerClassProperty, newName);
        InnerClassProperty innerClassProperty = new InnerClassProperty(src, newName);

        assertSame(src._delegate.withName(newName), innerClassProperty._delegate);
        assertSame(src._creator, innerClassProperty._creator);
    }

    @Test
    public void testWithName() {
        PropertyName newName = new PropertyName("");
        InnerClassProperty innerClassProperty = this.innerClassProperty.withName(newName);

        assertSame(newName, innerClassProperty.getFullName());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        InnerClassProperty innerClassProperty = this.innerClassProperty.withValueDeserializer(deser);

        assertSame(deser, innerClassProperty.getValueDeserializer());
    }

    @Test
    public void testAssignIndex() {
        int index = 0;
        innerClassProperty.assignIndex(index);

        assertEquals(index, innerClassProperty.getPropertyIndex());
    }

    @Test
    public void testGetPropertyIndex() {
        int index = 0;
        innerClassProperty.assignIndex(index);

        assertEquals(index, innerClassProperty.getPropertyIndex());
    }

    @Test
    public void testGetAnnotation() {
        Class<A> acls = A.class;
        Annotation annotation = innerClassProperty.getAnnotation(acls);

        assertTrue(annotation instanceof Annotation);
    }

    @Test
    public void testGetMember() {
        AnnotatedMember member = innerClassProperty.getMember();

        assertTrue(member instanceof AnnotatedMember);
    }

    @Test
    public void testDeserializeAndSetWithNullValue() throws IOException {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object bean = new Object();
        innerClassProperty.deserializeAndSet(jp, ctxt, bean);

        assertNull(innerClassProperty.get(bean));
    }

    @Test
    public void testDeserializeAndSetWithTypeDeserializer() throws IOException {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object bean = new Object();
        innerClassProperty.deserializeAndSet(jp, ctxt, bean);

        assertNull(innerClassProperty.get(bean));
    }
    
    @Test
    public void testSetAndReturn() throws IOException {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        Object value = new Object();
        Object result = innerClassProperty.setAndReturn(instance, value);
        
        assertSame(instance, result);
        assertSame(value, innerClassProperty.get(instance));
    }

    @Test
    public void testReadResolve() {
        Object result = innerClassProperty.readResolve();

        assertTrue(result instanceof InnerClassProperty);
    }

    @Test
    public void testWriteReplaceWithAnnotated() {
        Object result = innerClassProperty.writeReplace();

        assertTrue(result instanceof InnerClassProperty);
    }

    @Test
    public void testWriteReplaceWithoutAnnotated() {
        innerClassProperty = new InnerClassProperty(innerClassProperty, null);
        Object result = innerClassProperty.writeReplace();

        assertTrue(result instanceof InnerClassProperty);
    }

    private static class SettableBeanProperty extends BeanProperty {

        @Override
        public <A extends Annotation> A getAnnotation(Class<A> acls) {
            return null;
        }

        @Override
        public AnnotatedMember getMember() {
            return null;
        }

        @Override
        public void deserializeAndSet(JsonParser jp, DeserializationContext ctxt, Object bean) throws IOException {

        }

        @Override
        public Object deserializeSetAndReturn(JsonParser jp, DeserializationContext ctxt, Object instance) throws IOException {
            return null;
        }

        @Override
        public void set(Object instance, Object value) throws IOException {

        }

        @Override
        public Object setAndReturn(Object instance, Object value) throws IOException {
            return null;
        }
    }

    private static class Annotation {

    }

    private static class A extends Annotation {

    }

    private static class JsonParser {

        public JsonToken getCurrentToken() {
            return null;
        }
    }

    private static class DeserializationContext {

    }

    private static class JsonToken {

        public static final JsonToken VALUE_NULL = new JsonToken();

    }

    private static class JsonDeserializer<T> {

        public T getNullValue(DeserializationContext ctxt) {
            return null;
        }

        public T deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
            return null;
        }

        public void deserialize(JsonParser jp, DeserializationContext ctxt, T value) throws IOException {

        }
    }

    private static class PropertyName {

        public PropertyName(String name) {

        }
    }

    private static class BeanProperty {

        public PropertyName getFullName() {
            return null;
        }

        public <A extends Annotation> A getAnnotation(Class<A> acls) {
            return null;
        }

        public AnnotatedMember getMember() {
            return null;
        }

        public void deserializeAndSet(JsonParser jp, DeserializationContext ctxt, Object bean) throws IOException {

        }

        public Object deserializeSetAndReturn(JsonParser jp, DeserializationContext ctxt, Object instance) throws IOException {
            return null;
        }

        public void set(Object instance, Object value) throws IOException {

        }

        public Object setAndReturn(Object instance, Object value) throws IOException {
            return null;
        }
    }

    private static class TypeDeserializer {

    }

    private static class Constructor<T> {

        public T newInstance(Object bean) throws Exception {
            return null;
        }

        public Class<?> getDeclaringClass() {
            return null;
        }
    }

    private static class AnnotatedConstructor {

        public AnnotatedConstructor(Constructor<?> ctor, AnnotatedConstructor ann) {
        
        }

        public Constructor<?> getAnnotated() {
            return null;
        }
    }

    private static class Object {

    }


}