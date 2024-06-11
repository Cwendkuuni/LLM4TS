package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSetter;

public class InnerClassPropertyTest {

    @Test
    public void testInnerClassPropertyConstruction() {
        AnnotatedConstructor constructor = Mockito.mock(AnnotatedConstructor.class);
        SettableBeanProperty delegate = Mockito.mock(SettableBeanProperty.class);

        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, constructor);
        InnerClassProperty innerClassPropertyWithValueDeserializer = new InnerClassProperty(innerClassProperty, Mockito.mock(JsonDeserializer.class));
        InnerClassProperty innerClassPropertyWithName = new InnerClassProperty(innerClassProperty, Mockito.mock(PropertyName.class));
        InnerClassProperty innerClassPropertyWithJdkSerialization = new InnerClassProperty(innerClassProperty, Mockito.mock(AnnotatedConstructor.class));

        assertEquals(delegate, innerClassProperty._delegate);
        assertEquals(constructor, innerClassProperty._creator);
        assertNull(innerClassProperty._annotated);
        assertEquals(innerClassProperty, innerClassPropertyWithJdkSerialization.source);

        assertEquals(delegate, innerClassPropertyWithValueDeserializer._delegate);
        assertEquals(constructor, innerClassPropertyWithValueDeserializer._creator);
        assertNotNull(innerClassPropertyWithValueDeserializer._valueDeserializer);
        assertEquals(innerClassProperty, innerClassPropertyWithValueDeserializer.source);

        assertEquals(delegate, innerClassPropertyWithName._delegate);
        assertEquals(constructor, innerClassPropertyWithName._creator);
        assertNotNull(innerClassPropertyWithName.getName());
        assertEquals(innerClassProperty, innerClassPropertyWithName.source);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
        Object bean = Mockito.mock(Object.class);

        SettableBeanProperty delegate = Mockito.mock(SettableBeanProperty.class);
        Constructor<?> creator = Mockito.mock(Constructor.class);

        AnnotatedConstructor annotatedConstructor = Mockito.mock(AnnotatedConstructor.class);
        JsonDeserializer<?> valueDeserializer = Mockito.mock(JsonDeserializer.class);

        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, creator);
        InnerClassProperty innerClassPropertyWithValueDeserializer = new InnerClassProperty(innerClassProperty, valueDeserializer);

        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getValueAsString()).thenReturn("value");

        when(creator.newInstance(bean)).thenCallRealMethod();

        innerClassPropertyWithValueDeserializer.deserializeAndSet(jsonParser, deserializationContext, bean);

        verify(delegate).set(bean, "value");
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
        Object instance = Mockito.mock(Object.class);

        SettableBeanProperty delegate = Mockito.mock(SettableBeanProperty.class);
        Constructor<?> creator = Mockito.mock(Constructor.class);

        JsonDeserializer<?> valueDeserializer = Mockito.mock(JsonDeserializer.class);

        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, creator);
        InnerClassProperty innerClassPropertyWithValueDeserializer = new InnerClassProperty(innerClassProperty, valueDeserializer);

        when(jsonParser.getValueAsString()).thenReturn("value");

        assertEquals("value", innerClassPropertyWithValueDeserializer.deserializeSetAndReturn(jsonParser, deserializationContext, instance));

        verify(delegate).setAndReturn(instance, "value");
    }

    @Test
    public void testSet() throws IOException {
        Object instance = Mockito.mock(Object.class);
        Object value = Mockito.mock(Object.class);

        SettableBeanProperty delegate = Mockito.mock(SettableBeanProperty.class);

        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, null);

        innerClassProperty.set(instance, value);

        verify(delegate).set(instance, value);
    }

    @Test
    public void testReadResolve() {
        AnnotatedConstructor annotatedConstructor = Mockito.mock(AnnotatedConstructor.class);
        SettableBeanProperty delegate = Mockito.mock(SettableBeanProperty.class);

        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, annotatedConstructor);

        InnerClassProperty readResolve = (InnerClassProperty) innerClassProperty.readResolve();

        assertEquals(delegate, readResolve._delegate);
        assertEquals(annotatedConstructor, readResolve._annotated);
        assertNull(readResolve._creator);
    }

    @Test
    public void testWriteReplace() {
        AnnotatedConstructor annotatedConstructor = Mockito.mock(AnnotatedConstructor.class);
        SettableBeanProperty delegate = Mockito.mock(SettableBeanProperty.class);

        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, annotatedConstructor);

        Object writeReplace = innerClassProperty.writeReplace();

        assertEquals(InnerClassProperty.class, writeReplace.getClass());
        assertEquals(delegate, ((InnerClassProperty) writeReplace).source._delegate);
        assertEquals(annotatedConstructor, ((InnerClassProperty) writeReplace).source._creator);
    }

    @JsonDeserialize(using = MyJsonDeserializer.class)
    static class MyInnerClass {
        private String name;

        @JsonSetter("name")
        public void setName(String name) {
            this.name = name;
        }
    }

    @Test
    public void testDeserializeMyInnerClass() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
        Object bean = Mockito.mock(Object.class);

        SettableBeanProperty delegate = Mockito.mock(SettableBeanProperty.class);
        Constructor<MyInnerClass> creator = Mockito.mock(Constructor.class);

        AnnotatedConstructor annotatedConstructor = Mockito.mock(AnnotatedConstructor.class);
        JsonDeserializer<MyInnerClass> valueDeserializer = Mockito.mock(JsonDeserializer.class);

        MyInnerClass expectedInstance = new MyInnerClass();

        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(jsonParser.nextValue()).thenReturn("name");
        when(jsonParser.nextValue()).thenReturn("MyValue");

        when(creator.newInstance(bean)).thenCallRealMethod();

        when(valueDeserializer.deserialize(jsonParser, deserializationContext, null)).thenReturn(expectedInstance);

        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, creator);
        InnerClassProperty innerClassPropertyWithValueDeserializer = new InnerClassProperty(innerClassProperty, valueDeserializer);

        innerClassPropertyWithValueDeserializer.deserializeAndSet(jsonParser, deserializationContext, bean);

        MyInnerClass actualInstance = (MyInnerClass) bean;
        assertEquals(expectedInstance, actualInstance);

        verify(delegate).set(bean, expectedInstance);
    }
}