package com.example.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

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
import com.fasterxml.jackson.databind.util.ObjectIdGenerator$AlwaysIdKey;

@RunWith(MockitoJUnitRunner.class)
public class DefaultDeserializationContextTest {
    @InjectMocks
    private DefaultDeserializationContext context;

    @Mock
    private DeserializerFactory factory;

    @Mock
    private DeserializerCache cache;

    @Mock
    private JsonParser parser;

    @Mock
    private InjectableValues values;

    @Mock
    private HandlerInstantiator handlerInstantiator;

    @Mock
    private ObjectIdGenerator<?> idGenerator;

    @Mock
    private SimpleObjectIdResolver resolverType;

    @Mock
    private ObjectIdResolver resolver;

    @Test
    public void testFindObjectId_NullId_ShouldReturnNull() {
        when(idGenerator.key(any())).thenReturn(new ObjectIdGenerator.AlwaysIdKey("testKey"));
        when(resolverType.newForDeserialization(context)).thenReturn(resolver);

        assertNull(context.findObjectId(null, idGenerator, resolverType));
    }

    @Test
    public void testFindObjectId_ValidId_ShouldReturnObjectId() {
        Object id = new Object();
        when(idGenerator.key(id)).thenReturn(new ObjectIdGenerator.AlwaysIdKey("testKey"));
        when(resolverType.newForDeserialization(context)).thenReturn(resolver);

        ObjectIdGenerator.IdKey key = new ObjectIdGenerator.AlwaysIdKey("testKey");
        ReadableObjectId objectId = new ReadableObjectId(key);

        when(resolver.canUseFor(resolverType)).thenReturn(true);

        assertNotNull(context.findObjectId(id, idGenerator, resolverType));
    }

    @Test(expected = UnresolvedForwardReference.class)
    public void testCheckUnresolvedObjectId_UnresolvedObjectIds_ShouldThrowException() {
        LinkedHashMap<ObjectIdGenerator.IdKey, ReadableObjectId> objectIds = new LinkedHashMap<>();
        objectIds.put(new ObjectIdGenerator.AlwaysIdKey("testKey1"), new ReadableObjectId(new ObjectIdGenerator.AlwaysIdKey("testKey1")));
        objectIds.put(new ObjectIdGenerator.AlwaysIdKey("testKey2"), new ReadableObjectId(new ObjectIdGenerator.AlwaysIdKey("testKey2")));

        when(context._objectIds).thenReturn(objectIds);

        context.checkUnresolvedObjectId();
    }

    @Test
    public void testDeserializerInstance_NullAnnotated_ShouldReturnNull() {
        assertNull(context.deserializerInstance(null, null));
    }

    @Test
    public void testDeserializerInstance_JsonDeserializer_ShouldReturnJsonDeserializer() {
        JsonDeserializer<String> deserializer = mock(JsonDeserializer.class);
        Annotated annotated = mock(Annotated.class);

        when(annotated.getAnnotation(JsonDeserializer.class)).thenReturn(deserializer);

        assertSame(deserializer, context.deserializerInstance(annotated, deserializer));
    }

    @Test
    public void testDeserializerInstance_Class_ShouldReturnJsonDeserializerInstance() {
        JsonDeserializer<String> deserializer = mock(JsonDeserializer.class);
        Annotated annotated = mock(Annotated.class);
        Class<JsonDeserializer<String>> deserializerClass = JsonDeserializer.class;

        when(annotated.getAnnotation(deserializerClass)).thenReturn(annotated);
        when(context.handlerInstantiator.deserializerInstance(context, annotated, deserializerClass)).thenReturn(deserializer);

        assertSame(deserializer, context.deserializerInstance(annotated, deserializerClass));
    }

    @Test(expected = IllegalStateException.class)
    public void testDeserializerInstance_InvalidAnnotated_ShouldThrowException() {
        Annotated annotated = mock(Annotated.class);
        Object deserDef = new Object();

        assertNull(context.deserializerInstance(annotated, deserDef));
    }

    @Test
    public void testKeyDeserializerInstance_NullAnnotated_ShouldReturnNull() {
        assertNull(context.keyDeserializerInstance(null, null));
    }

    @Test
    public void testKeyDeserializerInstance_JsonKeyDeserializer_ShouldReturnJsonKeyDeserializer() {
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        Annotated annotated = mock(Annotated.class);

        when(annotated.getAnnotation(KeyDeserializer.class)).thenReturn(keyDeserializer);

        assertSame(keyDeserializer, context.keyDeserializerInstance(annotated, keyDeserializer));
    }

    @Test
    public void testKeyDeserializerInstance_Class_ShouldReturnKeyDeserializerInstance() {
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        Annotated annotated = mock(Annotated.class);
        Class<KeyDeserializer> keyDeserializerClass = KeyDeserializer.class;

        when(annotated.getAnnotation(keyDeserializerClass)).thenReturn(annotated);
        when(context.handlerInstantiator.keyDeserializerInstance(context, annotated, keyDeserializerClass)).thenReturn(keyDeserializer);

        assertSame(keyDeserializer, context.keyDeserializerInstance(annotated, keyDeserializerClass));
    }

    @Test(expected = IllegalStateException.class)
    public void testKeyDeserializerInstance_InvalidAnnotated_ShouldThrowException() {
        Annotated annotated = mock(Annotated.class);
        Object deserDef = new Object();

        assertNull(context.keyDeserializerInstance(annotated, deserDef));
    }

    @Test
    public void testWith_DeserializerFactory_ShouldReturnDefaultDeserializationContextInstance() {
        DefaultDeserializationContext context = context.with(factory);

        assertNotNull(context);
        assertSame(context.getClass(), DefaultDeserializationContext.class);
    }

    @Test
    public void testCreateInstance_DeserializationConfigJsonParserInjectableValues_ShouldReturnDefaultDeserializationContextInstance() {
        assertNotNull(context.createInstance(mock(DeserializationConfig.class), parser, values));
        assertSame(context.getClass(), DefaultDeserializationContext.class);
    }
}