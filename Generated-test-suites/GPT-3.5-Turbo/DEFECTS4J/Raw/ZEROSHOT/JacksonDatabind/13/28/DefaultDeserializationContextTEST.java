package com.fasterxml.jackson.databind.deser;

import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultDeserializationContextTest {

    @Test
    public void testFindObjectId() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        Object id = new Object();
        ObjectIdGenerator<?> gen = new ObjectIdGenerator<ObjectIdResolver.IdKey>() {
            public ObjectIdGenerator<ObjectIdResolver.IdKey> forScope(Class<?> scope) {
                return null;
            }

            public ObjectIdResolver.IdKey key(Object key) {
                return null;
            }

            public Object generateId(Object forPojo) {
                return null;
            }

            public boolean canUseFor(ObjectIdGenerator<?> gen) {
                return false;
            }

            public ObjectIdResolver getResolver() {
                return null;
            }
        };
        ObjectIdResolver resolver = new SimpleObjectIdResolver();

        ReadableObjectId result = context.findObjectId(id, gen, resolver);
        assertNotNull(result);
        assertEquals(id, result.getKey().key);
        assertEquals(resolver, result.getResolver());
    }

    @Test
    public void testFindObjectId_NullId() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        Object id = null;
        ObjectIdGenerator<?> gen = new ObjectIdGenerator<ObjectIdResolver.IdKey>() {
            public ObjectIdGenerator<ObjectIdResolver.IdKey> forScope(Class<?> scope) {
                return null;
            }

            public ObjectIdResolver.IdKey key(Object key) {
                return null;
            }

            public Object generateId(Object forPojo) {
                return null;
            }

            public boolean canUseFor(ObjectIdGenerator<?> gen) {
                return false;
            }

            public ObjectIdResolver getResolver() {
                return null;
            }
        };
        ObjectIdResolver resolver = new SimpleObjectIdResolver();

        ReadableObjectId result = context.findObjectId(id, gen, resolver);
        assertNotNull(result);
        assertEquals(id, result.getKey().key);
        assertEquals(resolver, result.getResolver());
    }

    @Test
    public void testCheckUnresolvedObjectId_Enabled() throws UnresolvedForwardReference {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        context.enable(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS);

        try {
            context.checkUnresolvedObjectId();
            fail("UnresolvedForwardReference should be thrown");
        } catch (UnresolvedForwardReference e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testCheckUnresolvedObjectId_Disabled() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        context.disable(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS);

        try {
            context.checkUnresolvedObjectId();
        } catch (UnresolvedForwardReference e) {
            fail("UnresolvedForwardReference should not be thrown");
        }
    }
}