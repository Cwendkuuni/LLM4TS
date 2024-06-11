package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext.Impl;

import java.util.*;
import java.util.Map.Entry;

/**
 * Test cases for {@link DefaultDeserializationContext} class.
 */
public class DefaultDeserializationContextTest {

    @Test
    public void testDefaultDeserializationContextCreation() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        assertNotNull(context);
    }

    @Test
    public void testFindObjectId() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);

        ObjectIdGenerator<?> idGen = mock(ObjectIdGenerator.class);
        ObjectIdResolver idResolver = mock(ObjectIdResolver.class);

        when(idGen.key((Object) null)).thenReturn(new IdKey(1));

        ObjectIdResolver resolver = new ObjectIdGenerator.IdImpl(idGen);

        ReadableObjectId result = context.findObjectId(null, idGen, resolver);
        assertNull(result);

        Object id = new Object();
        ReadableObjectId expected = new ReadableObjectId(new IdKey(1));
        ReadableObjectId result1 = context.findObjectId(id, idGen, resolver);
        assertEquals(expected, result1);
    }

    @Test
    public void testFindObjectIdWithSimpleObjectIdResolver() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);

        ObjectIdGenerator<?> idGen = mock(ObjectIdGenerator.class);
        ObjectIdResolver idResolver = new SimpleObjectIdResolver();

        when(idGen.key((Object) null)).thenReturn(new IdKey(1));

        ObjectIdResolver resolver = new ObjectIdResolverBase() {
            @Override
            public ObjectIdResolver newForDeserialization(DefaultDeserializationContext ctxt) {
                return idResolver;
            }
        };

        ObjectIdResolver resolverInstance = context.findObjectIdResolver(resolver);
        assertEquals(idResolver, resolverInstance);

        Object id = new Object();
        ReadableObjectId expected = new ReadableObjectId(new IdKey(1));
        ReadableObjectId result = context.findObjectId(id, idGen);
        assertEquals(expected, result);
    }

    @Test
    public void testFindObjectIdWithMultipleObjectIdResolvers() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);

        ObjectIdGenerator<?> idGen = mock(ObjectIdGenerator.class);
        ObjectIdResolver idResolver1 = mock(ObjectIdResolver.class);
        ObjectIdResolver idResolver2 = mock(ObjectIdResolver.class);

        when(idGen.key((Object) null)).thenReturn(new IdKey(1));

        ObjectIdResolver resolver1 = new ObjectIdGenerator.IdImpl(idGen);
        ObjectIdResolver resolver2 = new ObjectIdResolverBase() {
            @Override
            public ObjectIdResolver newForDeserialization(DefaultDeserializationContext ctxt) {
                return idResolver2;
            }
        };

        context.addObjectIdResolver(idResolver1);
        context.addObjectIdResolver(idResolver2);

        Object id = new Object();
        ReadableObjectId expected = new ReadableObjectId(new IdKey(1));
        ReadableObjectId result = context.findObjectId(id, idGen);
        assertEquals(expected, result);

        verify(idResolver1, times(0)).newForDeserialization(context);
        verify(idResolver2, times(1)).newForDeserialization(context);
    }

    @Test(expected = UnresolvedForwardReference.class)
    public void testThrowUnresolvedForwardReferenceException() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);

        ObjectIdGenerator<?> idGen = mock(ObjectIdGenerator.class);
        ObjectIdResolver idResolver = mock(ObjectIdResolver.class);

        when(idGen.key((Object) null)).thenReturn(new IdKey(1));

        ObjectIdResolver resolver = new ObjectIdGenerator.IdImpl(idGen);

        ReadableObjectId id1 = new ReadableObjectId(new IdKey(1));
        id1.setResolver(resolver);
        context._objectIds.put(id1.getKey(), id1);

        Object id2 = new Object();
        ReadableObjectId expected = new ReadableObjectId(new IdKey(2));
        ReadableObjectId result = context.findObjectId(id2, idGen);
        assertEquals(expected, result);

        // This should trigger UnresolvedForwardReferenceException.
        context.checkUnresolvedObjectId();
    }

    @Test
    public void testDeserializerInstance() throws JsonMappingException {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);

        Annotated ann = mock(Annotated.class);
        Object deserDef = mock(Object.class);

        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);

        when(deserDef.getClass()).thenReturn(JsonDeserializer.class);
        when(deserDef).thenReturn(deserializer);

        JsonDeserializer<Object> result = context.deserializerInstance(ann, deserDef);
        assertEquals(deserializer, result);

        verify(deserializer, times(1)).resolve(context);
    }

    @Test
    public void testKeyDeserializerInstance() throws JsonMappingException {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);

        Annotated ann = mock(Annotated.class);
        Object deserDef = mock(Object.class);

        KeyDeserializer deserializer = mock(KeyDeserializer.class);

        when(deserDef.getClass()).thenReturn(KeyDeserializer.class);
        when(deserDef).thenReturn(deserializer);

        KeyDeserializer result = context.keyDeserializerInstance(ann, deserDef);
        assertEquals(deserializer, result);

        verify(deserializer, times(1)).resolve(context);
    }

    @Test(expected = IllegalStateException.class)
    public void testDeserializerInstanceWithInvalidDefinition() throws JsonMappingException {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);

        Annotated ann = mock(Annotated.class);
        Object deserDef = new Object();

        JsonDeserializer<Object> result = context.deserializerInstance(ann, deserDef);
        fail("Expected IllegalStateException but got " + result);
    }

    @Test(expected = IllegalStateException.class)
    public void testKeyDeserializerInstanceWithInvalidDefinition() throws JsonMappingException {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);

        Annotated ann = mock(Annotated.class);
        Object deserDef = new Object();

        KeyDeserializer result = context.keyDeserializerInstance(ann, deserDef);
        fail("Expected IllegalStateException but got " + result);
    }
}