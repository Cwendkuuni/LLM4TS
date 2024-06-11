package com.fasterxml.jackson.databind.deser;

import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultDeserializationContextTest {

    @Test
    public void testFindObjectId() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        Object id = new Object();
        ObjectIdGenerator<?> gen = null;
        ObjectIdResolver resolver = null;

        ReadableObjectId result = context.findObjectId(id, gen, resolver);
        assertNotNull(result);
    }

    @Test
    public void testFindObjectId_DEPRECATED() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        Object id = new Object();
        ObjectIdGenerator<?> gen = null;

        ReadableObjectId result = context.findObjectId(id, gen);
        assertNotNull(result);
    }

    @Test
    public void testCheckUnresolvedObjectId() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        context.enable(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS);

        try {
            context.checkUnresolvedObjectId();
            fail("UnresolvedForwardReference exception not thrown");
        } catch (UnresolvedForwardReference e) {
            // expected exception
        }
    }

    @Test
    public void testDeserializerInstance() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        Annotated ann = null;
        Object deserDef = null;

        JsonDeserializer<Object> result = context.deserializerInstance(ann, deserDef);
        assertNull(result);
    }

    @Test
    public void testKeyDeserializerInstance() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        Annotated ann = null;
        Object deserDef = null;

        KeyDeserializer result = context.keyDeserializerInstance(ann, deserDef);
        assertNull(result);
    }
}