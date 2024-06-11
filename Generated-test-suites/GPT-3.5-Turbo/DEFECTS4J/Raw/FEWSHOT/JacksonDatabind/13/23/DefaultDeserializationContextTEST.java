package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class DefaultDeserializationContextTest {

    @Test
    public void testFindObjectId() {
        DefaultDeserializationContext.Impl context = new DefaultDeserializationContext.Impl(null);
        assertNotNull(context.findObjectId("id", null));
    }

    @Test
    public void testCheckUnresolvedObjectId() {
        DefaultDeserializationContext.Impl context = new DefaultDeserializationContext.Impl(null);
        context.checkUnresolvedObjectId();
        // Test with enabled DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS
        context.configure(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS, true);
        try {
            context.checkUnresolvedObjectId();
        } catch (UnresolvedForwardReference e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testDeserializerInstance() {
        DefaultDeserializationContext.Impl context = new DefaultDeserializationContext.Impl(null);
        assertNull(context.deserializerInstance(null, null));
    }

    @Test
    public void testKeyDeserializerInstance() {
        DefaultDeserializationContext.Impl context = new DefaultDeserializationContext.Impl(null);
        assertNull(context.keyDeserializerInstance(null, null));
    }

    @Test
    public void testCopy() {
        DefaultDeserializationContext.Impl context = new DefaultDeserializationContext.Impl(null);
        DefaultDeserializationContext.Impl copiedContext = context.copy();
        assertNotNull(copiedContext);
        assertEquals(context.getClass(), copiedContext.getClass());
    }

    @Test
    public void testCreateInstance() {
        DefaultDeserializationContext.Impl context = new DefaultDeserializationContext.Impl(null);
        DefaultDeserializationContext.Impl newInstance = context.createInstance(null, null, null);
        assertNotNull(newInstance);
        assertEquals(context.getClass(), newInstance.getClass());
    }

    @Test
    public void testWith() {
        DefaultDeserializationContext.Impl context = new DefaultDeserializationContext.Impl(null);
        DefaultDeserializationContext.Impl withContext = context.with(null);
        assertNotNull(withContext);
        assertEquals(context.getClass(), withContext.getClass());
    }
}