package com.example.junit.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;

public class DefaultDeserializationContextTest {

    @org.junit.Test
    public void testFindObjectId() throws Exception {
        DefaultDeserializationContext context = new DefaultDeserializationContext(null, null);

        ObjectIdGenerator idGenerator = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new ObjectIdGenerator.IdKey();
            }
        };

        ObjectIdResolver idResolver = new SimpleObjectIdResolver();

        ReadableObjectId objectId = context.findObjectId(new Object(), idGenerator, idResolver);
        assertNotNull(objectId);
    }

    @org.junit.Test(expected = UnresolvedForwardReference.class)
    public void testCheckUnresolvedObjectId() throws Exception {
        DefaultDeserializationContext context = new DefaultDeserializationContext(null, null);

        ReadableObjectId objectId1 = new ReadableObjectId(new ObjectIdGenerator.IdKey());
        ReadableObjectId objectId2 = new ReadableObjectId(new ObjectIdGenerator.IdKey());
        objectId1.addReferringProperty(objectId2);

        context._objectIds = new LinkedHashMap<>();
        context._objectIds.put(objectId1.getKey(), objectId1);

        context.checkUnresolvedObjectId();
    }

    @org.junit.Test
    public void testDeserializerInstanceNullAnnotated() throws Exception {
        DefaultDeserializationContext context = new DefaultDeserializationContext(null, null);
        assertNull(context.deserializerInstance(null, null));
    }

    @org.junit.Test
    public void testDeserializerInstanceJsonDeserializer() throws Exception {
        JsonDeserializer<Integer> jsonDeserializer = new StandardDeserializer<Integer>(Integer.class);
        Annotated ann = Mockito.mock(Annotated.class);

        DefaultDeserializationContext context = new DefaultDeserializationContext(null, null);
        JsonDeserializer<Object> deserializer = context.deserializerInstance(ann, jsonDeserializer);
        assertEquals(jsonDeserializer, deserializer);
    }

    @org.junit.Test
    public void testDeserializerInstanceClass() throws Exception {
        JsonDeserializer<Integer> jsonDeserializer = new StandardDeserializer<Integer>(Integer.class);
        Annotated ann = Mockito.mock(Annotated.class);

        DefaultDeserializationContext context = new DefaultDeserializationContext(null, null);
        JsonDeserializer<Object> deserializer = context.deserializerInstance(ann, JsonDeserializer.class);
        assertEquals(jsonDeserializer.getClass(), deserializer.getClass());
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testDeserializerInstanceInvalidClass() throws Exception {
        JsonDeserializer<Integer> jsonDeserializer = new StandardDeserializer<Integer>(Integer.class);
        Annotated ann = Mockito.mock(Annotated.class);

        DefaultDeserializationContext context = new DefaultDeserializationContext(null, null);
        JsonDeserializer<Object> deserializer = context.deserializerInstance(ann, String.class);
    }

    @org.junit.Test
    public void testKeyDeserializerInstanceNullAnnotated() throws Exception {
        DefaultDeserializationContext context = new DefaultDeserializationContext(null, null);
        assertNull(context.keyDeserializerInstance(null, null));
    }

    @org.junit.Test
    public void testKeyDeserializerInstanceJsonKeyDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = new StandardKeyDeserializer();
        Annotated ann = Mockito.mock(Annotated.class);

        DefaultDeserializationContext context = new DefaultDeserializationContext(null, null);
        KeyDeserializer deserializer = context.keyDeserializerInstance(ann, keyDeserializer);
        assertEquals(keyDeserializer, deserializer);
    }

    @org.junit.Test
    public void testKeyDeserializerInstanceClass() throws Exception {
        KeyDeserializer keyDeserializer = new StandardKeyDeserializer();
        Annotated ann = Mockito.mock(Annotated.class);

        DefaultDeserializationContext context = new DefaultDeserializationContext(null, null);
        KeyDeserializer deserializer = context.keyDeserializerInstance(ann, KeyDeserializer.class);
        assertEquals(keyDeserializer.getClass(), deserializer.getClass());
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testKeyDeserializerInstanceInvalidClass() throws Exception {
        KeyDeserializer keyDeserializer = new StandardKeyDeserializer();
        Annotated ann = Mockito.mock(Annotated.class);

        DefaultDeserializationContext context = new DefaultDeserializationContext(null, null);
        KeyDeserializer deserializer = context.keyDeserializerInstance(ann, String.class);
    }

    @org.junit.Test
    public void testWith() throws Exception {
        DefaultDeserializationContext context = new DefaultDeserializationContext(null, null);
        DefaultDeserializationContext newContext = context.with(new DefaultDeserializationContext.Impl.Factory());
        assertNotSame(context, newContext);
    }

    @org.junit.Test
    public void testCreateInstance() throws Exception {
        DefaultDeserializationContext context = new DefaultDeserializationContext(null, null);
        DefaultDeserializationContext newContext = context.createInstance(new DeserializationConfig(), new JsonParser(), new InjectableValues());
        assertNotSame(context, newContext);
    }
}