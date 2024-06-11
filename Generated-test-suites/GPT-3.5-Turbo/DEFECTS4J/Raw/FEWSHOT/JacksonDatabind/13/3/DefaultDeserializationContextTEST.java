package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext.Impl;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class DefaultDeserializationContextTest {

    private DefaultDeserializationContext ctx;

    private DeserializerFactory factory;

    @Before
    public void setUp() {
        factory = mock(DeserializerFactory.class);
        ctx = new Impl(factory);
    }

    @Test
    public void testFindObjectId() {
        ObjectIdGenerator<?> gen = mock(ObjectIdGenerator.class);
        ObjectIdResolver resolverType = mock(ObjectIdResolver.class);
        Object id = new Object();

        ReadableObjectId entry = mock(ReadableObjectId.class);
        when(entry.hasReferringProperties()).thenReturn(true);
        when(entry.getKey()).thenReturn(mock(ObjectIdGenerator.IdKey.class));

        LinkedHashMap<ObjectIdGenerator.IdKey, ReadableObjectId> objectIds = new LinkedHashMap<ObjectIdGenerator.IdKey, ReadableObjectId>();
        objectIds.put(entry.getKey(), entry);
        ctx._objectIds = objectIds;

        assertEquals(entry, ctx.findObjectId(id, gen, resolverType));
    }

    @Test
    public void testFindObjectIdWithUnresolvedForwardReferenceDisabled() throws UnresolvedForwardReference {
        ctx.configure(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS, false);
        ctx.checkUnresolvedObjectId();
        assertNull(ctx._objectIds);
    }

    @Test(expected = UnresolvedForwardReference.class)
    public void testCheckUnresolvedObjectIdWhenHasReferringProperties() throws UnresolvedForwardReference {
        final ObjectIdGenerator.IdKey idKey = mock(ObjectIdGenerator.IdKey.class);
        final Class<?> beanType = Object.class;
        final Object location = new Object();
        final ReadableObjectId referring = mock(ReadableObjectId.class);
        final Iterator<Referring> iterator = Mockito.mock(Iterator.class);

        LinkedHashMap<ObjectIdGenerator.IdKey, ReadableObjectId> objectIds = new LinkedHashMap<ObjectIdGenerator.IdKey, ReadableObjectId>();

        when(referring.getBeanType()).thenReturn(beanType);
        when(referring.getLocation()).thenReturn(location);
        when(referring.iterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true, false);

        ReadableObjectId entry = mock(ReadableObjectId.class);
        when(entry.hasReferringProperties()).thenReturn(true);
        when(entry.getKey()).thenReturn(idKey);
        when(entry.referringProperties()).thenReturn(referring);

        objectIds.put(entry.getKey(), entry);
        ctx._objectIds = objectIds;

        ctx.checkUnresolvedObjectId();
    }

    @Test
    public void testCheckUnresolvedObjectIdWithNullObjectIds() throws UnresolvedForwardReference {
        assertNull(ctx._objectIds);
        ctx.checkUnresolvedObjectId();
    }

    @Test(expected = IllegalStateException.class)
    public void testDeserializerInstanceWithIllegalStateException() throws JsonMappingException {
        Annotated ann = mock(Annotated.class);
        Object deserDef = 123;

        ctx.deserializerInstance(ann, deserDef);
    }

    @Test(expected = IllegalStateException.class)
    public void testDeserializerInstanceWithDeserDefNotAnInstanceOfClass() throws JsonMappingException {
        Annotated ann = mock(Annotated.class);
        Object deserDef = new Object();

        ctx.deserializerInstance(ann, deserDef);
    }

    @Test(expected = IllegalStateException.class)
    public void testDeserializerInstanceWithDeserClassNotAssignable() throws JsonMappingException {
        Annotated ann = mock(Annotated.class);
        Class<?> deserDef = String.class;

        ctx.deserializerInstance(ann, deserDef);
    }

    @Test(expected = IllegalStateException.class)
    public void testKeyDeserializerInstanceAnnotationClassNotAssignable() throws JsonMappingException {
        Annotated ann = mock(Annotated.class);
        Class<?> deserDef = String.class;

        ctx.keyDeserializerInstance(ann, deserDef);
    }

    @Test
    public void testWithContext() {
        DeserializationConfig config = mock(DeserializationConfig.class);
        JsonParser jp = mock(JsonParser.class);
        InjectableValues values = mock(InjectableValues.class);
        DefaultDeserializationContext result = ctx.with(factory).createInstance(config, jp, values);

        assertEquals(ctx._config, result._config);
        assertEquals(ctx._parser, result._parser);
        assertEquals(ctx._injectableValues, result._injectableValues);
        assertEquals(ctx._cache, result._cache);
        assertEquals(factory, result._factory);
    }

    @Test
    public void testCopyWithDifferentClass() {
        DefaultDeserializationContext result = ctx.copy();

        assertEquals(Impl.class, result.getClass());
        assertNotEquals(ctx, result);
    }

    @Test
    public void testCopyWithSameClass() {
        DefaultDeserializationContext.customInstance.copy();
    }
}