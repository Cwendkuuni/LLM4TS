package com.fasterxml.jackson.databind.deser;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultDeserializationContextTest {

    private DefaultDeserializationContext context;

    @Before
    public void setUp() {
        context = new DefaultDeserializationContext.Impl(null);
    }

    @Test
    public void testFindObjectId() {
        assertNull(context.findObjectId(null, null));
    }

    @Test
    public void testCheckUnresolvedObjectId() {
        try {
            context.checkUnresolvedObjectId();
        } catch (UnresolvedForwardReference e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testDeserializerInstance() {
        assertNull(context.deserializerInstance(null, null));
    }

    @Test
    public void testKeyDeserializerInstance() {
        assertNull(context.keyDeserializerInstance(null, null));
    }

    @Test
    public void testWith() {
        DefaultDeserializationContext updatedContext = context.with(null);
        assertNotEquals(context, updatedContext);
    }

    @Test
    public void testCreateInstance() {
        DefaultDeserializationContext newInstance = context.createInstance(null, null, null);
        assertNotNull(newInstance);
    }
}