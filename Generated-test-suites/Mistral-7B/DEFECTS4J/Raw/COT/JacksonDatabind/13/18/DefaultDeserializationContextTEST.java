package com.example.junit.test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultDeserializationContextTest {

    @Test
    public void testFindObjectId() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new SimpleObjectMapper());
        ObjectIdGenerator idGenerator = new IdKeyGenerator();
        assertNull(context.findObjectId(null, idGenerator, null));
        Object id = new Object();
        ReadableObjectId result = context.findObjectId(id, idGenerator, new SimpleObjectIdResolver());
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(SimpleObjectIdResolver.class, result.getResolverType());
    }

    @Test(expected = UnresolvedForwardReference.class)
    public void testCheckUnresolvedObjectIdWithUnresolvedReferences() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new SimpleObjectMapper());
        ObjectIdGenerator idGenerator = new IdKeyGenerator();
        Object id1 = new Object();
        Object id2 = new Object();
        ObjectIdResolver resolver1 = new SimpleObjectIdResolver();
        ObjectIdResolver resolver2 = new SimpleObjectIdResolver();
        context._objectIds = new LinkedHashMap<>();
        context._objectIdResolvers = new ArrayList<>();
        context._objectIdResolvers.add(resolver1);
        context._objectIdResolvers.add(resolver2);
        context._objectIds.put(new ObjectIdGenerator.IdKey(id1), new ReadableObjectId(id1));
        context._objectIds.put(new ObjectIdGenerator.IdKey(id2), new ReadableObjectId(id2));
        ReadableObjectId objectId1 = context.findObjectId(id1, idGenerator, null);
        ReadableObjectId objectId2 = context.findObjectId(id2, idGenerator, null);
        assertNotNull(objectId1);
        assertNotNull(objectId2);
        ((ReadableObjectId) objectId1).getResolver().setUnresolvedPropertyName("test");
        ((ReadableObjectId) objectId2).getResolver().setUnresolvedPropertyName("test");
        context.checkUnresolvedObjectId();
    }

    @Test
    public void testCheckUnresolvedObjectIdWithNoUnresolvedReferences() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new SimpleObjectMapper());
        ObjectIdGenerator idGenerator = new IdKeyGenerator();
        Object id = new Object();
        context.findObjectId(id, idGenerator, new SimpleObjectIdResolver());
        context.checkUnresolvedObjectId();
    }

    @Test
    public void testDeserializerInstance() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new SimpleObjectMapper());
        Annotated ann = mock(Annotated.class);
        Object deserDef = new JsonDeserializerMock();
        JsonDeserializer deser = context.deserializerInstance(ann, deserDef);
        assertNotNull(deser);
        assertEquals(JsonDeserializer.class, deser.getClass());
    }

    @Test(expected = IllegalStateException.class)
    public void testDeserializerInstanceWithInvalidDeserializerDefinition() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new SimpleObjectMapper());
        Annotated ann = mock(Annotated.class);
        Object deserDef = new Object();
        context.deserializerInstance(ann, deserDef);
    }

    @Test
    public void testDeserializerInstanceWithClassDeserializerDefinition() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new SimpleObjectMapper());
        Annotated ann = mock(Annotated.class);
        Class deserDef = JsonDeserializer.class;
        JsonDeserializer deser = context.deserializerInstance(ann, deserDef);
        assertNotNull(deser);
        assertEquals(JsonDeserializer.class, deser.getClass());
    }

    @Test
    public void testFindObjectIdWithMissingIdGenerator() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new SimpleObjectMapper());
        Object id = new Object();
        try {
            context.findObjectId(id, null, new SimpleObjectIdResolver());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFindObjectIdWithMissingIdResolver() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new SimpleObjectMapper());
        ObjectIdGenerator idGenerator = new IdKeyGenerator();
        Object id = new Object();
        try {
            context.findObjectId(id, idGenerator, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFindObjectIdWithNullId() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new SimpleObjectMapper());
        ObjectIdGenerator idGenerator = new IdKeyGenerator();
        assertNull(context.findObjectId(null, idGenerator, new SimpleObjectIdResolver()));
    }

    @Test
    public void testClearObjectIds() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new SimpleObjectMapper());
        ObjectIdGenerator idGenerator = new IdKeyGenerator();
        Object id1 = new Object();
        Object id2 = new Object();
        context._objectIds = new LinkedHashMap<>();
        context._objectIds.put(new ObjectIdGenerator.IdKey(id1), new ReadableObjectId(id1));
        context._objectIds.put(new ObjectIdGenerator.IdKey(id2), new ReadableObjectId(id2));
        context.checkUnresolvedObjectId();
        context.clearObjectIds();
        context.checkUnresolvedObjectId();
    }

    @Test
    public void testClearObjectIdsAfterCheckUnresolvedObjectId() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new SimpleObjectMapper());
        ObjectIdGenerator idGenerator = new IdKeyGenerator();
        Object id1 = new Object();
        Object id2 = new Object();
        context._objectIds = new LinkedHashMap<>();
        context._objectIds.put(new ObjectIdGenerator.IdKey(id1), new ReadableObjectId(id1));
        context._objectIds.put(new ObjectIdGenerator.IdKey(id2), new ReadableObjectId(id2));
        context.checkUnresolvedObjectId();
        context.clearObjectIds();
        context.checkUnresolvedObjectId();
    }

    @Test
    public void testClearObjectIdsAfterCheckUnresolvedObjectIdWithUnresolvedReferences() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new SimpleObjectMapper());
        ObjectIdGenerator idGenerator = new IdKeyGenerator();
        Object id1 = new Object();
        Object id2 = new Object();
        context._objectIds = new LinkedHashMap<>();
        context._objectIds.put(new ObjectIdGenerator.IdKey(id1), new ReadableObjectId(id1));
        context._objectIds.put(new ObjectIdGenerator.IdKey(id2), new ReadableObjectId(id2));
        ReadableObjectId objectId1 = context.findObjectId(id1, idGenerator, null);
        ReadableObjectId objectId2 = context.findObjectId(id2, idGenerator, null);
        assertNotNull(objectId1);
        assertNotNull(objectId2);
        ((ReadableObjectId) objectId1).getResolver().setUnresolvedPropertyName("test");
        ((ReadableObjectId) objectId2).getResolver().setUnresolvedPropertyName("test");
        context.checkUnresolvedObjectId();
        context.clearObjectIds();
        context.checkUnresolvedObjectId();
    }

    @Test
    public void testClearObjectIdsAfterCheckUnresolvedObjectIdWithNoUnresolvedReferences() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new SimpleObjectMapper());
        ObjectIdGenerator idGenerator = new IdKeyGenerator();
        Object id = new Object();
        context.findObjectId(id, idGenerator, new SimpleObjectIdResolver());
        context.checkUnresolvedObjectId();
        context.clearObjectIds();
        context.checkUnresolvedObjectId();
    }

    private static class IdKeyGenerator implements ObjectIdGenerator {
        @Override
        public ObjectIdGenerator.IdKey key(Object id) {
            return new ObjectIdGenerator.IdKey(null, id.getClass());
        }
    }

    private static class JsonDeserializerMock implements JsonDeserializer<Object> {
    }
}