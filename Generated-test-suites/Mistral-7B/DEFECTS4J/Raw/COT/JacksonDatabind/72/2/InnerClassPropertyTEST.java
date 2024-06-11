package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.annotation.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;

import org.junit.Test;

public class InnerClassPropertyTest {

    @Test
    public void testInnerClassPropertyConstructor() throws Exception {
        PropertyName name = PropertyName.construct("name");
        SettableBeanProperty delegate = new StubSettableBeanProperty();
        Constructor<InnerClass> constructor = InnerClass.class.getDeclaredConstructor();
        InnerClassProperty property = new InnerClassProperty(delegate, constructor);
        assertNotNull(property);
        assertEquals(name, property.getName());
        assertEquals(delegate, property._delegate);
    }

    @Test
    public void testInnerClassPropertyWithValueDeserializer() throws Exception {
        PropertyName name = PropertyName.construct("name");
        SettableBeanProperty delegate = new StubSettableBeanProperty();
        Constructor<InnerClass> constructor = InnerClass.class.getDeclaredConstructor();
        AnnotatedConstructor annotatedConstructor = Mockito.mock(AnnotatedConstructor.class);
        InnerClassProperty property = new InnerClassProperty(delegate, annotatedConstructor);
        JsonDeserializer<Object> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        property = new InnerClassProperty(property, valueDeserializer);
        assertNotNull(property);
        assertEquals(name, property.getName());
        assertEquals(delegate, property._delegate);
        assertEquals(valueDeserializer, property.getValueDeserializer());
    }

    @Test
    public void testInnerClassPropertyWithAnnotation() throws Exception {
        PropertyName name = PropertyName.construct("name");
        SettableBeanProperty delegate = new StubSettableBeanProperty();
        Constructor<InnerClass> constructor = InnerClass.class.getDeclaredConstructor();
        AnnotatedConstructor annotatedConstructor = Mockito.mock(AnnotatedConstructor.class);
        JsonDeserializer<Object> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        InnerClassProperty property = new InnerClassProperty(delegate, annotatedConstructor, valueDeserializer);
        assertNotNull(property);
        assertEquals(name, property.getName());
        assertEquals(delegate, property._delegate);
        assertEquals(annotatedConstructor, property._annotated);
        assertEquals(valueDeserializer, property.getValueDeserializer());
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        PropertyName name = PropertyName.construct("name");
        SettableBeanProperty delegate = new StubSettableBeanProperty();
        Constructor<InnerClass> constructor = InnerClass.class.getDeclaredConstructor();
        AnnotatedConstructor annotatedConstructor = Mockito.mock(AnnotatedConstructor.class);
        JsonDeserializer<Object> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        InnerClassProperty property = new InnerClassProperty(delegate, annotatedConstructor, valueDeserializer);
        ObjectCarrier objectCarrier = Mockito.mock(ObjectCarrier.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Object bean = Mockito.mock(Object.class);

        when(annotatedConstructor.getAnnotated()).thenReturn(property);
        when(valueDeserializer.deserialize(Mockito.any(JsonParser.class), Mockito.any(DeserializationContext.class), Mockito.any(Object.class))).thenReturn("value");

        property.deserializeAndSet(Mockito.mock(JsonParser.class), context, bean);
        verify(bean, Mockito.times(1)).setInnerClass(eq("value"));
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        PropertyName name = PropertyName.construct("name");
        SettableBeanProperty delegate = new StubSettableBeanProperty();
        Constructor<InnerClass> constructor = InnerClass.class.getDeclaredConstructor();
        AnnotatedConstructor annotatedConstructor = Mockito.mock(AnnotatedConstructor.class);
        JsonDeserializer<Object> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        InnerClassProperty property = new InnerClassProperty(delegate, annotatedConstructor, valueDeserializer);
        ObjectCarrier objectCarrier = Mockito.mock(ObjectCarrier.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Object instance = Mockito.mock(Object.class);

        when(annotatedConstructor.getAnnotated()).thenReturn(property);
        when(valueDeserializer.deserialize(Mockito.any(JsonParser.class), Mockito.any(DeserializationContext.class))).thenReturn("value");

        assertEquals("value", property.deserializeSetAndReturn(Mockito.mock(JsonParser.class), context, instance));
    }

    @Test
    public void testSet() throws Exception {
        PropertyName name = PropertyName.construct("name");
        SettableBeanProperty delegate = new StubSettableBeanProperty();
        ObjectCarrier objectCarrier = Mockito.mock(ObjectCarrier.class);

        InnerClassProperty property = new InnerClassProperty(delegate, Mockito.mock(AnnotatedConstructor.class));
        property.set(objectCarrier, Mockito.mock(Object.class));

        verify(delegate, Mockito.times(1)).set(objectCarrier, Mockito.eq(Mockito.anyObject()));
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetNullValue() throws Exception {
        PropertyName name = PropertyName.construct("name");
        SettableBeanProperty delegate = new StubSettableBeanProperty();
        Constructor<InnerClass> constructor = InnerClass.class.getDeclaredConstructor();
        AnnotatedConstructor annotatedConstructor = Mockito.mock(AnnotatedConstructor.class);
        JsonDeserializer<Object> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        InnerClassProperty property = new InnerClassProperty(delegate, annotatedConstructor, valueDeserializer);
        ObjectCarrier objectCarrier = Mockito.mock(ObjectCarrier.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Object bean = Mockito.mock(Object.class);

        when(annotatedConstructor.getAnnotated()).thenReturn(property);
        when(valueDeserializer.getNullValue(context)).thenReturn(null);

        property.deserializeAndSet(Mockito.mock(JsonParser.class), context, bean);
        verify(bean, Mockito.never()).setInnerClass(Mockito.anyObject());
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetTypeMismatch() throws Exception {
        PropertyName name = PropertyName.construct("name");
        SettableBeanProperty delegate = new StubSettableBeanProperty();
        Constructor<InnerClass> constructor = InnerClass.class.getDeclaredConstructor();
        AnnotatedConstructor annotatedConstructor = Mockito.mock(AnnotatedConstructor.class);
        JsonDeserializer<Object> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        InnerClassProperty property = new InnerClassProperty(delegate, annotatedConstructor, valueDeserializer);
        ObjectCarrier objectCarrier = Mockito.mock(ObjectCarrier.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Object bean = Mockito.mock(Object.class);
        JsonParser parser = Mockito.mock(JsonParser.class);

        when(annotatedConstructor.getAnnotated()).thenReturn(property);
        when(valueDeserializer.deserializeWithType(parser, context, Mockito.any(Class.class))).thenReturn(new Integer(1));

        property.deserializeAndSet(parser, context, bean);
        verify(bean, Mockito.never()).setInnerClass(Mockito.anyObject());
    }

    @Test
    public void testReadResolve() throws Exception {
        PropertyName name = PropertyName.construct("name");
        SettableBeanProperty delegate = new StubSettableBeanProperty();
        Constructor<InnerClass> constructor = InnerClass.class.getDeclaredConstructor();
        AnnotatedConstructor annotatedConstructor = Mockito.mock(AnnotatedConstructor.class);
        JsonDeserializer<Object> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        InnerClassProperty property = new InnerClassProperty(delegate, annotatedConstructor, valueDeserializer);

        Object innerClassProperty = property;
        Object readResolve = innerClassProperty.readResolve();
        assertEquals(property.getClass(), readResolve.getClass());
    }

    @Test
    public void testWriteReplace() throws Exception {
        PropertyName name = PropertyName.construct("name");
        SettableBeanProperty delegate = new StubSettableBeanProperty();
        Constructor<InnerClass> constructor = InnerClass.class.getDeclaredConstructor();
        AnnotatedConstructor annotatedConstructor = Mockito.mock(AnnotatedConstructor.class);
        InnerClassProperty property = new InnerClassProperty(delegate, annotatedConstructor);

        Object writeReplace = property.writeReplace();
        assertFalse(property.equals(writeReplace));
        assertTrue(writeReplace instanceof InnerClassProperty);
    }

    private static class StubSettableBeanProperty extends SettableBeanPropertyBase {
        @Override
        public void set(Object instance, Object value) throws IOException {
            // no-op
        }

        @Override
        public Object setAndReturn(Object instance, Object value) throws IOException {
            return value;
        }
    }
}