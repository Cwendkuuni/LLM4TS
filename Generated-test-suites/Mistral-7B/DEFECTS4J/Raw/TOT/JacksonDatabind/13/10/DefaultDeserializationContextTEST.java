package com.fasterxml.jackson.databind.deser.test;

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
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext.Impl;

import java.io.IOException;
import java.util.*;

public class DefaultDeserializationContextTest {

    @org.junit.Test
    public void testFindObjectId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        JsonParser jp = mapper.getFactory().createParser("[{\"id\":1,\"name\":\"foo\"},{\"id\":2,\"name\":\"bar\"}]");
        DefaultDeserializationContext context = new Impl(mapper.getDeserializationConfig(), jp, null);

        ObjectIdGenerator idGenerator = context.injectables().getInstance(ObjectIdGenerator.class);
        ObjectIdResolver idResolver = new SimpleObjectIdResolver();

        Object id1 = new Object();
        Object id2 = new Object();

        ReadableObjectId objId1 = context.findObjectId(id1, idGenerator, idResolver);
        ReadableObjectId objId2 = context.findObjectId(id2, idGenerator, idResolver);

        Assert.assertNotNull(objId1);
        Assert.assertNotNull(objId2);

        Assert.assertNotSame(objId1.getKey().getId(), objId2.getKey().getId());

        // Test with null id
        ObjectIdGenerator nullIdGenerator = null;
        ObjectIdResolver nullResolver = new SimpleObjectIdResolver();
        ReadableObjectId nullObjectId = context.findObjectId(null, nullIdGenerator, nullResolver);
        Assert.assertNull(nullObjectId);
    }

    @org.junit.Test
    public void testCheckUnresolvedObjectId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        JsonParser jp = mapper.getFactory().createParser("[{\"id\":1,\"name\":\"foo\"},{\"id\":2,\"name\":\"bar\"}]");
        DefaultDeserializationContext context = new Impl(mapper.getDeserializationConfig(), jp, null);

        ObjectIdGenerator idGenerator = context.injectables().getInstance(ObjectIdGenerator.class);
        ObjectIdResolver idResolver = new SimpleObjectIdResolver();

        Object id1 = new Object();
        Object id2 = new Object();
        Object id3 = new Object();

        // Test with no object ids
        context.findObjectId(id1, idGenerator, idResolver);
        context.findObjectId(id2, idGenerator, idResolver);

        List<ObjectIdGenerator.IdKey> keys = new ArrayList<>();
        keys.add(new IdKey("test1"));
        keys.add(new IdKey("test2"));

        context._objectIds = new LinkedHashMap<>();

        // Test with unresolved object id
        context.findObjectId(id3, idGenerator, idResolver);
        context.checkUnresolvedObjectId();

        // Test with resolved object ids
        ReadableObjectId objId1 = context.findObjectId(id1, idGenerator, idResolver);
        ReadableObjectId objId2 = context.findObjectId(id2, idGenerator, idResolver);
        context._objectIds.put(objId1.getKey(), objId1);
        context._objectIds.put(objId2.getKey(), objId2);

        context.checkUnresolvedObjectId();
    }

    @org.junit.Test
    public void testDeserializerInstance() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        JsonParser jp = mapper.getFactory().createParser("[{\"@class\":\"com.foo.Foo\"}]");
        DefaultDeserializationContext context = new Impl(mapper.getDeserializationConfig(), jp, null);

        Class<JsonDeserializer<String>> stringDeserializer = StringDeserializer.class;
        JsonDeserializer<String> stringDeserializerInstance = context.deserializerInstance(null, stringDeserializer);

        Assert.assertNotNull(stringDeserializerInstance);
        Assert.assertEquals(StringDeserializer.class, stringDeserializerInstance.getClass());

        // Test with null deserializer definition
        Class<JsonDeserializer<String>> nullDeserializer = null;
        JsonDeserializer<String> nullDeserializerInstance = context.deserializerInstance(null, nullDeserializer);
        Assert.assertNull(nullDeserializerInstance);

        // Test with class deserializer definition
        Class<JsonDeserializer<Foo>> fooDeserializer = FooDeserializer.class;
        JsonDeserializer<Foo> fooDeserializerInstance = context.deserializerInstance(null, fooDeserializer);

        Assert.assertNotNull(fooDeserializerInstance);
        Assert.assertEquals(FooDeserializer.class, fooDeserializerInstance.getClass());
    }

    @org.junit.Test
    public void testKeyDeserializerInstance() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        JsonParser jp = mapper.getFactory().createParser("[{\"key\":\"foo\"}]");
        DefaultDeserializationContext context = new Impl(mapper.getDeserializationConfig(), jp, null);

        Class<KeyDeserializer> keyDeserializer = KeyDeserializer.class;
        KeyDeserializer keyDeserializerInstance = context.keyDeserializerInstance(null, keyDeserializer);

        Assert.assertNotNull(keyDeserializerInstance);
        Assert.assertEquals(KeyDeserializer.class, keyDeserializerInstance.getClass());

        // Test with null key deserializer definition
        Class<KeyDeserializer> nullKeyDeserializer = null;
        KeyDeserializer nullKeyDeserializerInstance = context.keyDeserializerInstance(null, nullKeyDeserializer);
        Assert.assertNull(nullKeyDeserializerInstance);

        // Test with class key deserializer definition
        Class<KeyDeserializer<Integer>> integerKeyDeserializer = IntegerKeyDeserializer.class;
        KeyDeserializer<Integer> integerKeyDeserializerInstance = context.keyDeserializerInstance(null, integerKeyDeserializer);

        Assert.assertNotNull(integerKeyDeserializerInstance);
        Assert.assertEquals(IntegerKeyDeserializer.class, integerKeyDeserializerInstance.getClass());
    }

    static class Foo {}
    static class FooDeserializer extends JsonDeserializer<Foo> {}
    static class IntegerKeyDeserializer extends KeyDeserializer<Integer> {}
    static class StringDeserializer extends JsonDeserializer<String> {}
}