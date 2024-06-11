package com.example.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

public class DefaultDeserializationContextTest extends TestBase {

    

    @org.junit.Test
    public void testDefaultConstructor() {
        DefaultDeserializationContext context = new DefaultDeserializationContext(null, null);
        assertNull(context._objectIds);
        assertNull(context._objectIdResolvers);
    }

    @org.junit.Test
    public void testConstructorWithDeserializerFactoryAndCache() {
        DefaultDeserializationContext context = new DefaultDeserializationContext(mock(DeserializerFactory.class), mock(DeserializerCache.class));
        assertNotNull(context._objectIds);
        assertNotNull(context._objectIdResolvers);
    }

    @org.junit.Test
    public void testConstructorWithSourceContext() {
        DefaultDeserializationContext srcContext = mock(DefaultDeserializationContext.class);
        DefaultDeserializationContext context = new DefaultDeserializationContext(srcContext);
        assertSame(srcContext, context._sourceContext);
        assertNull(context._objectIds);
        assertNull(context._objectIdResolvers);
    }

    @org.junit.Test
    public void testConstructorWithSourceContextAndFactory() {
        DefaultDeserializationContext srcContext = mock(DefaultDeserializationContext.class);
        DefaultDeserializationContext context = new DefaultDeserializationContext(srcContext, mock(DeserializerFactory.class));
        assertSame(srcContext, context._sourceContext);
        assertNull(context._objectIds);
        assertNull(context._objectIdResolvers);
    }

    @org.junit.Test
    public void testCopyMethod() {
        DefaultDeserializationContext context = new DefaultDeserializationContext(null, null);
        assertThrows(IllegalStateException.class, context::copy);
    }

    @org.junit.Test
    public void testFindObjectIdWithNullId() {
        DefaultDeserializationContext context = new DefaultDeserializationContext(null, null);
        ObjectIdGenerator<?> idGen = mock(ObjectIdGenerator.class);
        ObjectIdResolver resolverType = mock(ObjectIdResolver.class);
        when(idGen.key((Object) null)).thenReturn(new IdKey(null));
        ReadableObjectId objectId = context.findObjectId(null, idGen, resolverType);
        assertNull(objectId);
    }

    @org.junit.Test
    public void testFindObjectIdWithValidId() {
        DefaultDeserializationContext context = new DefaultDeserializationContext(null, null);
        ObjectIdGenerator<?> idGen = mock(ObjectIdGenerator.class);
        ObjectIdResolver resolverType = mock(ObjectIdResolver.class);
        when(idGen.key("testId")).thenReturn(new IdKey("testId"));
        ObjectIdGenerator.IdKey key = new IdKey("testId");
        ReadableObjectId objectId = context.findObjectId("testId", idGen, resolverType);
        assertNotNull(objectId);
        assertEquals(key, objectId.getKey());
    }

    @org.junit.Test
    public void testFindObjectIdWithExistingId() {
        DefaultDeserializationContext context = new DefaultDeserializationContext(null, null);
        ObjectIdGenerator<?> idGen = mock(ObjectIdGenerator.class);
        ObjectIdResolver resolverType = mock(ObjectIdResolver.class);
        LinkedHashMap<ObjectIdGenerator.IdKey, ReadableObjectId> objectIds = new LinkedHashMap<>();
        when(context._objectIds).thenReturn(objectIds);
        when(idGen.key((Object) null)).thenReturn(new IdKey(null));
        when(idGen.key("testId")).thenReturn(new IdKey("testId"));
        when(resolverType.newForDeserialization(context)).thenReturn(mock(ObjectIdResolver.class));
        ObjectIdGenerator.IdKey key = new IdKey("testId");
        ReadableObjectId objectId = new ReadableObjectId(key);
        objectId.setResolver(mock(ObjectIdResolver.class));
        objectIds.put(key, objectId);
        ReadableObjectId foundObjectId = context.findObjectId("testId", idGen, resolverType);
        assertSame(objectId, foundObjectId);
    }

    @org.junit.Test
    public void testFindObjectIdWithIdGeneratorNull() {
        DefaultDeserializationContext context = new DefaultDeserializationContext(null, null);
        ObjectIdResolver resolverType = mock(ObjectIdResolver.class);
        assertThrows(NullPointerException.class, () -> context.findObjectId(null, null, resolverType));
    }

    @org.junit.Test
    public void testFindObjectIdWithIdResolverNull() {
        DefaultDeserializationContext context = new DefaultDeserializationContext(null, null);
        ObjectIdGenerator<?> idGen = mock(ObjectIdGenerator.class);
        assertThrows(NullPointerException.class, () -> context.findObjectId(null, idGen, null));
    }

    @org.junit.Test
    public void testCheckUnresolvedObjectIdWithEmptyObjectIds() {
        DefaultDeserializationContext context = new DefaultDeserializationContext(null, null);
        context.checkUnresolvedObjectId();
    }

    @org.junit.Test
    public void testCheckUnresolvedObjectIdWithUnresolvedObjectIds() {
        DefaultDeserializationContext context = new DefaultDeserializationContext(null, null);
        LinkedHashMap<ObjectIdGenerator.IdKey, ReadableObjectId> objectIds = new LinkedHashMap<>();
        when(context._objectIds).thenReturn(objectIds);
        ObjectIdGenerator<?> idGen = mock(ObjectIdGenerator.class);
        ObjectIdResolver resolverType = mock(ObjectIdResolver.class);
        ObjectIdGenerator.IdKey key1 = new IdKey("testId1");
        ObjectIdGenerator.IdKey key2 = new IdKey("testId2");
        when(idGen.key((Object) null)).thenReturn(new IdKey(null));
        when(idGen.key("testId1")).thenReturn(key1);
        when(idGen.key("testId2")).thenReturn(key2);
        when(resolverType.newForDeserialization(context)).thenReturn(mock(ObjectIdResolver.class));
        ReadableObjectId objectId1 = new ReadableObjectId(key1);
        ReadableObjectId objectId2 = new ReadableObjectId(key2);
        objectId1.setResolver(mock(ObjectIdResolver.class));
        objectId2.setResolver(mock(ObjectIdResolver.class));
        objectIds.put(key1, objectId1);
        objectIds.put(key2, objectId2);
        UnresolvedForwardReference exception = new UnresolvedForwardReference("Unresolved forward references for: ");
        List<Referring> referringList1 = new ArrayList<>();
        List<Referring> referringList2 = new ArrayList<>();
        referringList1.add(new Referring(JsonDeserializer.class, Object.class));
        referringList2.add(new Referring(JsonDeserializer.class, String.class));
        exception.addUnresolvedId(key1.key, Object.class, referringList1);
        exception.addUnresolvedId(key2.key, String.class, referringList2);
        assertThrows(UnresolvedForwardReference.class, () -> context.checkUnresolvedObjectId());
    }

    @org.junit.Test
    public void testDeserializerInstanceWithNullAnnotated() {
        DefaultDeserializationContext context = new DefaultDeserializationContext(null, null);
        Object deserDef = null;
        assertNull(context.deserializerInstance(null, deserDef));
    }

    @org.junit.Test
    public void testDeserializerInstanceWithJsonDeserializer() {
        DefaultDeserializationContext context = new DefaultDeserializationContext(null, null);
        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        Annotated ann = mock(Annotated.class);
        when(ann.getClass()).thenReturn(JsonDeserializer.class);
        when(ann.getValue()).thenReturn(deser);
        Object deserDef = deser;
        JsonDeserializer<Object> resultDeser = context.deserializerInstance(ann, deserDef);
        assertSame(deser, resultDeser);
    }

    @org.junit.Test
    public void testDeserializerInstanceWithClassOfJsonDeserializer() {
        DefaultDeserializationContext context = new DefaultDeserializationContext(null, null);
        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        Class<JsonDeserializer<Object>> deserClass = JsonDeserializer.class;
        when(deserClass.newInstance()).thenReturn(deser);
        Annotated ann = mock(Annotated.class);
        when(ann.getType()).thenReturn(deserClass);
        Object deserDef = deserClass;
        JsonDeserializer<Object> resultDeser = context.deserializerInstance(ann, deserDef);
        assertSame(deser, resultDeser);
    }

    @org.junit.Test
    public void testDeserializerInstanceWithKeyDeserializer() {
        DefaultDeserializationContext context = new DefaultDeserializationContext(null, null);
        KeyDeserializer deser = mock(KeyDeserializer.class);
        Annotated ann = mock(Annotated.class);
        when(ann.getClass()).thenReturn(KeyDeserializer.class);
        when(ann.getValue()).thenReturn(deser);
        Object deserDef = deser;
        KeyDeserializer resultDeser = context.keyDeserializerInstance(ann, deserDef);
        assertSame(deser, resultDeser);
    }

    @org.junit.Test
    public void testDeserializerInstanceWithClassOfKeyDeserializer() {
        DefaultDeserializationContext context = new DefaultDeserializationContext(null, null);
        KeyDeserializer deser = mock(KeyDeserializer.class);
        Class<KeyDeserializer> deserClass = KeyDeserializer.class;
        when(deserClass.newInstance()).thenReturn(deser);
        Annotated ann = mock(Annotated.class);
        when(ann.getType()).thenReturn(deserClass);
        Object deserDef = deserClass;
        KeyDeserializer resultDeser = context.keyDeserializerInstance(ann, deserDef);
        assertSame(deser, resultDeser);
    }

    
}