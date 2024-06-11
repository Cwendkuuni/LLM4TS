package com.example.test.jackson;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;
import java.util.*;

import static org.junit.Assert.*;

public class DefaultDeserializationContextTest {

    @Test
    public void testConstructor() {
        DefaultDeserializationContext context = new DefaultDeserializationContext(null, null);
        assertNotNull(context);
    }

    @Test(expected = IllegalStateException.class)
    public void testCopyUnsupported() {
        DefaultDeserializationContext context = new DefaultDeserializationContext(null, null);
        context.copy();
    }

    @Test
    public void testFindObjectIdWithNullId() {
        DefaultDeserializationContext context = new DefaultDeserializationContext(null, null);
        ObjectIdGenerator<?> gen = mock(ObjectIdGenerator.class);
        ObjectIdResolver resolverType = mock(ObjectIdResolver.class);
        ReadableObjectId result = context.findObjectId(null, gen, resolverType);
        assertNull(result);
    }

    @Test
    public void testFindObjectId() {
        DefaultDeserializationContext context = new DefaultDeserializationContext(null, null);
        ObjectIdGenerator<?> gen = mock(ObjectIdGenerator.class);
        ObjectIdResolver resolverType = mock(ObjectIdResolver.class);
        ObjectIdGenerator.IdKey key = mock(ObjectIdGenerator.IdKey.class);
        when(gen.key(anyObject())).thenReturn(key);
        ReadableObjectId result = context.findObjectId(new Object(), gen, resolverType);
        assertNotNull(result);
        assertEquals(key, result.getKey());
    }

    @Test(expected = UnresolvedForwardReference.class)
    public void testCheckUnresolvedObjectId() {
        DefaultDeserializationContext context = new DefaultDeserializationContext(null, null);
        ObjectIdGenerator<?> gen = mock(ObjectIdGenerator.class);
        ObjectIdResolver resolverType = mock(ObjectIdResolver.class);
        ObjectIdGenerator.IdKey key = mock(ObjectIdGenerator.IdKey.class);
        when(gen.key(anyObject())).thenReturn(key);
        ReadableObjectId id = new ReadableObjectId(key);
        id.setResolver(mock(ObjectIdResolver.class));
        context._objectIds.put(key, id);
        context.checkUnresolvedObjectId();

        ObjectIdResolver resolver = mock(ObjectIdResolver.class);
        when(resolver.getObjectId(anyObject(), eq(context), any()))
                .thenThrow(new UnresolvedForwardReference("Test error message"));
        context._objectIdResolvers.add(resolver);
        context.checkUnresolvedObjectId();
    }

    @Test
    public void testDeserializerInstance() throws JsonMappingException {
        DefaultDeserializationContext context = new DefaultDeserializationContext(null, null);
        Annotated ann = mock(Annotated.class);
        Object deserDef = new Object();
        JsonDeserializer<Object> expectedResult = mock(JsonDeserializer.class);
        when(context.deserializerInstance(ann, deserDef)).thenReturn(expectedResult);
        JsonDeserializer<Object> result = context.deserializerInstance(ann, deserDef);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testKeyDeserializerInstance() throws JsonMappingException {
        DefaultDeserializationContext context = new DefaultDeserializationContext(null, null);
        Annotated ann = mock(Annotated.class);
        Object deserDef = new Object();
        KeyDeserializer expectedResult = mock(KeyDeserializer.class);
        when(context.keyDeserializerInstance(ann, deserDef)).thenReturn(expectedResult);
        KeyDeserializer result = context.keyDeserializerInstance(ann, deserDef);
        assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testDeserializerInstanceNullDeserDef() throws JsonMappingException {
        DefaultDeserializationContext context = new DefaultDeserializationContext(null, null);
        Annotated ann = mock(Annotated.class);
        when(context.deserializerInstance(ann, null)).thenThrow(new IllegalStateException());
        JsonDeserializer<Object> result = context.deserializerInstance(ann, null);
        fail("Should not reach this point");
    }

    @Test(expected = IllegalStateException.class)
    public void testDeserializerInstanceNotFound() throws JsonMappingException {
        DefaultDeserializationContext context = new DefaultDeserializationContext(null, null);
        Annotated ann = mock(Annotated.class);
        Object deserDef = mock(Object.class);
        when(context.deserializerInstance(ann, deserDef)).thenThrow(new IllegalStateException());
        JsonDeserializer<Object> result = context.deserializerInstance(ann, deserDef);
        fail("Should not reach this point");
    }

    @Test
    public void testWith() {
        DefaultDeserializationContext context = new DefaultDeserializationContext(null, null);
        DefaultDeserializationContext result = context.with(null);
        assertSame(context, result);
    }

    @Test
    public void testCreateInstance() {
        DefaultDeserializationContext context = new DefaultDeserializationContext(null, null);
        DeserializationConfig config = mock(DeserializationConfig.class);
        JsonParser jp = mock(JsonParser.class);
        InjectableValues values = mock(InjectableValues.class);
        DefaultDeserializationContext result = context.createInstance(config, jp, values);
        assertNotNull(result);
        assertEquals(context.getClass(), result.getClass());
        assertEquals(config, result.config());
        assertEquals(jp, result.jsonParser());
        assertEquals(values, result.injectableValues());
    }
}