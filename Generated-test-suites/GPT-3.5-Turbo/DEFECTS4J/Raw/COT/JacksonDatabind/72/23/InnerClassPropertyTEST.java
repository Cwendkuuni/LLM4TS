
package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class InnerClassPropertyTest {

    @Test
    public void testDeserializeAndSet_withNullValue() throws IOException {
        InnerClassProperty innerClassProperty = createInnerClassProperty();
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object bean = new Object();

        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
        when(innerClassProperty._valueDeserializer.getNullValue(ctxt)).thenReturn(null);

        innerClassProperty.deserializeAndSet(jp, ctxt, bean);

        assertEquals(null, innerClassProperty.get(bean));
    }

    @Test
    public void testDeserializeAndSet_withValueTypeDeserializer() throws IOException {
        InnerClassProperty innerClassProperty = createInnerClassProperty();
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object bean = new Object();
        Object value = new Object();

        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(innerClassProperty._valueTypeDeserializer.deserializeWithType(jp, ctxt, innerClassProperty._valueTypeDeserializer)).thenReturn(value);

        innerClassProperty.deserializeAndSet(jp, ctxt, bean);

        assertEquals(value, innerClassProperty.get(bean));
    }

    @Test
    public void testDeserializeAndSet_withCreator() throws IOException {
        InnerClassProperty innerClassProperty = createInnerClassProperty();
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object bean = new Object();
        Object value = new Object();

        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        try {
            when(innerClassProperty._creator.newInstance(bean)).thenReturn(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        innerClassProperty.deserializeAndSet(jp, ctxt, bean);

        assertEquals(value, innerClassProperty.get(bean));
    }

    private InnerClassProperty createInnerClassProperty() {
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        Constructor<?> ctor = mock(Constructor.class);
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, ctor);
        innerClassProperty._valueTypeDeserializer = mock(TypeDeserializer.class);
        innerClassProperty._valueDeserializer = mock(JsonDeserializer.class);

        return innerClassProperty;
    }
}
