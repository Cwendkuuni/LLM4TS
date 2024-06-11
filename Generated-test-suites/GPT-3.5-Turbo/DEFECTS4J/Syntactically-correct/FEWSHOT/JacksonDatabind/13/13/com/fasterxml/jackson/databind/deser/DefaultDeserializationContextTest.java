package com.fasterxml.jackson.databind.deser;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.*;

public class DefaultDeserializationContextTest {
    @Test
    public void testFindObjectId() {
        DefaultDeserializationContext.Impl context = new DefaultDeserializationContext.Impl(null);
        assertNull(context.findObjectId("id", null, null));
    }
    
    @Test
    public void testCheckUnresolvedObjectId() {
        DefaultDeserializationContext.Impl context = new DefaultDeserializationContext.Impl(null);
        try {
            context.checkUnresolvedObjectId();
        } catch (UnresolvedForwardReference e) {
            assertNull(e);
        }
    }
    
    @Test
    public void testDeserializerInstance() {
        DefaultDeserializationContext.Impl context = new DefaultDeserializationContext.Impl(null);
        Annotated anno = null;
        Object deser = null;
        try {
            assertNull(context.deserializerInstance(anno, deser));
        } catch (JsonMappingException e) {
            assertNull(e);
        }
    }
    
    @Test
    public void testKeyDeserializerInstance() {
        DefaultDeserializationContext.Impl context = new DefaultDeserializationContext.Impl(null);
        Annotated anno = null;
        Object deser = null;
        try {
            assertNull(context.keyDeserializerInstance(anno, deser));
        } catch (JsonMappingException e) {
            assertNull(e);
        }
    }
}