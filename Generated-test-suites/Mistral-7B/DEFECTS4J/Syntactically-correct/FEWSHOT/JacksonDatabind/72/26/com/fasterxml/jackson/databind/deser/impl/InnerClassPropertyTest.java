package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class InnerClassPropertyTest {

    @Test
    public void testConstructor() {
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        Constructor<?> ctor = mock(Constructor.class);
        InnerClassProperty icp = new InnerClassProperty(delegate, ctor);
        assertEquals(delegate, icp._delegate);
        assertEquals(ctor, icp._creator);
    }

    @Test
    public void testTransientConstructor() {
        SettableBeanProperty src = mock(SettableBeanProperty.class);
        AnnotatedConstructor ann = mock(AnnotatedConstructor.class);
        InnerClassProperty icp = new InnerClassProperty(src, ann);
        assertEquals(src, icp);
        assertNull(icp._delegate);
        assertNull(icp._creator);
        assertEquals(ann, icp._annotated);
    }

    @Test
    public void testValueDeserializer() {
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        InnerClassProperty icp = new InnerClassProperty(delegate, deser);
        assertEquals(delegate, icp._delegate);
        assertEquals(deser, icp.getValueDeserializer());
        assertNull(icp._creator);
    }

    @Test
    public void testName() {
        SettableBeanProperty src = mock(SettableBeanProperty.class);
        PropertyName newName = PropertyName.construct("newName");
        InnerClassProperty icp = new InnerClassProperty(src, newName);
        assertEquals(src, icp);
        assertEquals(newName, icp.getName());
        assertNull(icp._creator);
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object bean = mock(Object.class);
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        Constructor<?> creator = mock(Constructor.class);
        InnerClassProperty icp = spy(new InnerClassProperty(delegate, creator));
        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jp.readValueAs(String.class)).thenReturn("test");
        when(creator.newInstance(bean)).thenReturn(new Object());

        icp.deserializeAndSet(jp, ctxt, bean);

        verify(delegate).set(bean, "test");
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        InnerClassProperty icp = spy(new InnerClassProperty(delegate, null));
        when(jp.parseValueAs(Object.class)).thenReturn(new Object());

        Object result = icp.deserializeSetAndReturn(jp, ctxt, instance);

        verify(delegate).setAndReturn(instance, new Object());
        assertEquals(new Object(), result);
    }

    @Test
    public void testSet() throws Exception {
        Object instance = mock(Object.class);
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        InnerClassProperty icp = spy(new InnerClassProperty(delegate, null));
        icp.set(instance, "test");

        verify(delegate).set(instance, "test");
    }

    @Test
    public void testReadResolve() {
        SettableBeanProperty src = mock(SettableBeanProperty.class);
        AnnotatedConstructor ann = mock(AnnotatedConstructor.class);
        InnerClassProperty icp = new InnerClassProperty(src, ann);
        assertEquals(src, icp.readResolve());
    }

    @Test
    public void testWriteReplace() {
        SettableBeanProperty src = mock(SettableBeanProperty.class);
        AnnotatedConstructor ann = mock(AnnotatedConstructor.class);
        InnerClassProperty icp = new InnerClassProperty(src, ann);
        Object result = icp.writeReplace();
        assertEquals(icp, result);
    }
}