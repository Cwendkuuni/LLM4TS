package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DefaultDeserializationContextTest {

    @Test
    public void testFindObjectId() {
        DefaultDeserializationContext.Impl context = new DefaultDeserializationContext.Impl(null);
        assertNull(context.findObjectId(null, null, null));
    }

    @Test
    public void testCheckUnresolvedObjectId() throws Throwable {
        DefaultDeserializationContext.Impl context = new DefaultDeserializationContext.Impl(null);
        context.checkUnresolvedObjectId();
    }

    @Test
    public void testDeserializerInstance() throws Throwable {
        DefaultDeserializationContext.Impl context = new DefaultDeserializationContext.Impl(null);
        assertNull(context.deserializerInstance(null, null));
    }

    @Test
    public void testKeyDeserializerInstance() throws Throwable {
        DefaultDeserializationContext.Impl context = new DefaultDeserializationContext.Impl(null);
        assertNull(context.keyDeserializerInstance(null, null));
    }

    @Test
    public void testWith() {
        DefaultDeserializationContext.Impl context = new DefaultDeserializationContext.Impl(null);
        assertEquals(context.getClass(), context.with(null).getClass());
    }

    @Test
    public void testCreateInstance() {
        DefaultDeserializationContext.Impl context = new DefaultDeserializationContext.Impl(null);
        assertEquals(context.getClass(), context.createInstance(null, null, null).getClass());
    }

    @Test
    public void testConstructor() {
        DefaultDeserializationContext.Impl context = new DefaultDeserializationContext.Impl(null);
        assertTrue(context instanceof DefaultDeserializationContext);
    }

    @Test
    public void testCopy() {
        DefaultDeserializationContext.Impl context = new DefaultDeserializationContext.Impl(null);
        assertEquals(context.getClass(), context.copy().getClass());
    }
}