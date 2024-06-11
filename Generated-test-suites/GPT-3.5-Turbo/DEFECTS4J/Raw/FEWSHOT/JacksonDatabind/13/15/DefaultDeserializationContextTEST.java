
package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class DefaultDeserializationContextTest {

    private DefaultDeserializationContext context;
    private DeserializationConfig config;
    private JsonParser jp;
    private InjectableValues values;
    private DeserializerFactory factory;

    @org.junit.Before
    public void setup() {
        config = mock(DeserializationConfig.class);
        jp = mock(JsonParser.class);
        values = mock(InjectableValues.class);
        factory = mock(DeserializerFactory.class);
        context = new DefaultDeserializationContext.Impl(factory);
    }

    @org.junit.Test
    public void testDefaultDeserializationContextConstructor() {
        assertEquals(factory, context.getFactory());
        assertNull(context.getInjectableInstance());
        assertNull(context.getParser());
    }

    @org.junit.Test
    public void testCreateInstance() {
        DefaultDeserializationContext newInstance = context.createInstance(config, jp, values);
        assertEquals(DefaultDeserializationContext.Impl.class, newInstance.getClass());
    }

    @org.junit.Test
    public void testWith() {
        DefaultDeserializationContext withInstance = context.with(factory);
        assertEquals(DefaultDeserializationContext.Impl.class, withInstance.getClass());
    }

    @org.junit.Test
    public void testCopy() {
        DefaultDeserializationContext copiedInstance = context.copy();
        assertEquals(DefaultDeserializationContext.Impl.class, copiedInstance.getClass());
    }

    @org.junit.Test
    public void testDeserializerInstance() throws JsonMappingException {
        Annotated annotated = mock(Annotated.class);
        Object deserDef = mock(JsonDeserializer.class);

        JsonDeserializer<Object> deserializer = context.deserializerInstance(annotated, deserDef);
        assertEquals(deserDef, deserializer);
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testDeserializerInstanceNotJsonDeserializer() throws JsonMappingException {
        Annotated annotated = mock(Annotated.class);
        Object deserDef = String.class;

        context.deserializerInstance(annotated, deserDef);
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testDeserializerInstanceNotClass() throws JsonMappingException {
        Annotated annotated = mock(Annotated.class);
        Object deserDef = mock(JsonDeserializer.class);
        when(deserDef.getClass()).thenReturn(String.class);

        context.deserializerInstance(annotated, deserDef);
    }

    @org.junit.Test
    public void testKeyDeserializerInstance() throws JsonMappingException {
        Annotated annotated = mock(Annotated.class);
        Object deserDef = mock(KeyDeserializer.class);

        KeyDeserializer keyDeserializer = context.keyDeserializerInstance(annotated, deserDef);
        assertEquals(deserDef, keyDeserializer);
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testKeyDeserializerInstanceNotJsonDeserializer() throws JsonMappingException {
        Annotated annotated = mock(Annotated.class);
        Object deserDef = String.class;

        context.keyDeserializerInstance(annotated, deserDef);
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testKeyDeserializerInstanceNotClass() throws JsonMappingException {
        Annotated annotated = mock(Annotated.class);
        Object deserDef = mock(KeyDeserializer.class);
        when(deserDef.getClass()).thenReturn(String.class);

        context.keyDeserializerInstance(annotated, deserDef);
    }
}
