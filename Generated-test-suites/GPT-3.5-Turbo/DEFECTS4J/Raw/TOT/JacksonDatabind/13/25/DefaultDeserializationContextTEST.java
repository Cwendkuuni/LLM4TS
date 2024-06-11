package com.fasterxml.jackson.databind.deser;

import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultDeserializationContextTest {

    @Test
    public void findObjectId_NullObjectIds_EmptyObjectIdsReturned() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        Object id = new Object();
        ObjectIdGenerator<?> gen = new ObjectIdGenerator.IdKey(NullNodeIdResolver.class, id);
        ObjectIdResolver resolver = new NullObjectIdResolver();

        ReadableObjectId objectId = context.findObjectId(id, gen, resolver);

        assertNotNull(objectId);
        assertTrue(context._objectIds.isEmpty());
    }

    @Test
    public void findObjectId_AlreadySeenId_ReturnsExistingObjectId() {
        DefaultDeserializationContext.Impl context = new DefaultDeserializationContext.Impl(null);
        Object id = new Object();
        ObjectIdGenerator<?> gen = new ObjectIdGenerator.IdKey(NullNodeIdResolver.class, id);
        ObjectIdResolver resolver = new NullObjectIdResolver();
        ReadableObjectId objectId1 = context.findObjectId(id, gen, resolver);

        ReadableObjectId objectId2 = context.findObjectId(id, gen, resolver);

        assertSame(objectId1, objectId2);
        assertEquals(1, context._objectIds.size());
    }

    @Test
    public void findObjectId_NewId_ReturnsNewObjectId() {
        DefaultDeserializationContext.Impl context = new DefaultDeserializationContext.Impl(null);
        Object id1 = new Object();
        Object id2 = new Object();
        ObjectIdGenerator<?> gen = new ObjectIdGenerator.IdKey(NullNodeIdResolver.class, id1);
        ObjectIdResolver resolver = new NullObjectIdResolver();
        ReadableObjectId objectId1 = context.findObjectId(id1, gen, resolver);

        ReadableObjectId objectId2 = context.findObjectId(id2, gen, resolver);

        assertNotSame(objectId1, objectId2);
        assertEquals(2, context._objectIds.size());
    }

    @Test
    public void checkUnresolvedObjectId_NoUnresolvedIds_NoExceptionThrown() {
        DefaultDeserializationContext.Impl context = new DefaultDeserializationContext.Impl(null);

        context.checkUnresolvedObjectId();
    }

    @Test(expected = UnresolvedForwardReference.class)
    public void checkUnresolvedObjectId_UnresolvedIds_ExceptionThrown() {
        DefaultDeserializationContext.Impl context = new DefaultDeserializationContext.Impl(null);
        ReadableObjectId objectId = new ReadableObjectId(new ObjectIdGenerator.IdKey(NullNodeIdResolver.class, new Object()));
        objectId.appendReferring(new NullReferring());
        context._objectIds.put(objectId.getKey(), objectId);

        context.checkUnresolvedObjectId();
    }

    private static class NullReferring implements ReadableObjectId.Referring {
        @Override
        public void handleResolvedForwardReference(Object id, Object value) {
            // No-op
        }

        @Override
        public Object getBean() {
            return null;
        }

        @Override
        public Id getBeanType() {
            return null;
        }

        @Override
        public Object getId() {
            return null;
        }

        @Override
        public Object call() throws Exception {
            return null;
        }
    }

    private static class NullObjectIdResolver implements ObjectIdResolver {
        @Override
        public void bindItem(IdKey id, Object pojo) {
            // No-op
        }

        @Override
        public Object resolveId(IdKey id) {
            return null;
        }

        @Override
        public ObjectIdResolver newForDeserialization(Object context) {
            return null;
        }

        @Override
        public boolean canUseFor(ObjectIdResolver resolverType) {
            return resolverType.getClass() == NullObjectIdResolver.class;
        }
    }
}