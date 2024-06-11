package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class DefaultDeserializationContextTest {
    
    @Test
    public void testFindObjectId() {
        DefaultDeserializationContext.Impl context = new DefaultDeserializationContext.Impl(null);
        ObjectIdGenerator.ObjectIdGenerator.IdKey key = new ObjectIdGenerator.ObjectIdGenerator.IdKey("", null, null);
        assertNull(context.findObjectId(null, null));
        ReadableObjectId entry = new ReadableObjectId(key);
        entry.setResolver(null);
        context._objectIds = new LinkedHashMap<ObjectIdGenerator.IdKey, ReadableObjectId>();
        context._objectIds.put(key, entry);
        assertEquals(entry, context.findObjectId(null, null));
        
        ObjectIdResolver resolverType = new SimpleObjectIdResolver();
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        context._objectIdResolvers = new ArrayList<ObjectIdResolver>();
        context._objectIdResolvers.add(resolver);
        assertNotNull(context.findObjectId(null, null, resolverType));
    }
    
    @Test
    public void testCheckUnresolvedObjectId() {
        DefaultDeserializationContext.Impl context = new DefaultDeserializationContext.Impl(null);
        assertFalse(context.isEnabled(DeserializationConfig.FAIL_ON_UNRESOLVED_OBJECT_IDS));
        context.checkUnresolvedObjectId();
        context.enable(DeserializationConfig.FAIL_ON_UNRESOLVED_OBJECT_IDS);
        context._objectIds = new LinkedHashMap<ObjectIdGenerator.ObjectIdGenerator.IdKey,ReadableObjectId>();
        context._objectIds.put(new ObjectIdGenerator.ObjectIdGenerator.IdKey("", null, null), new ReadableObjectId(null));
        UnresolvedForwardReference exception = null;
        try {
            context.checkUnresolvedObjectId();
        } catch (UnresolvedForwardReference e) {
            exception = e;
        }
        assertNotNull(exception);
        assertTrue(exception instanceof UnresolvedForwardReference);
    }
    
    @Test
    public void testDeserializerInstance() {
        DefaultDeserializationContext.Impl context = new DefaultDeserializationContext.Impl(null);
        assertNull(context.deserializerInstance(null, null));
        Annotated ann = new Annotated() {
            public <A extends java.lang.annotation.Annotation> A getAnnotation(java.lang.Class<A> arg0) {return null;}
            public java.util.Map<java.lang.String,java.lang.Object> getAnnotationMap() {return null;}
            public int getModifiers() {return 0;}
            public java.lang.String getName() {return null;}
            public <A extends java.lang.annotation.Annotation> A[] getAnnotations(java.lang.Class<A> arg0) {return null;}
            public <A extends java.lang.annotation.Annotation> boolean hasAnnotation(java.lang.Class<A> arg0) {return false;}
            public boolean isPublic() {return false;}
            public boolean isStatic() {return false;}
            public boolean isTransient() {return false;}
        };
        assertNotNull(context.deserializerInstance(ann, new Object()));
        Exception thrown = null;
        try {
            context.deserializerInstance(ann, new Class());
        } catch (IllegalStateException e) {
            thrown = e;
        }
        assertNotNull(thrown);
        thrown = null;
        try {
            context.deserializerInstance(ann, new SimpleObjectIdResolver());
        } catch (IllegalStateException e) {
            thrown = e;
        }
        assertNotNull(thrown);
        thrown = null;
        try {
            context.deserializerInstance(ann, new Class());
        } catch (IllegalStateException e) {
            thrown = e;
        }
        assertNotNull(thrown);
    }
    
    @Test
    public void testKeyDeserializerInstance() {
        DefaultDeserializationContext.Impl context = new DefaultDeserializationContext.Impl(null);
        assertNull(context.keyDeserializerInstance(null, null));
        Annotated ann = new Annotated() {
            public <A extends java.lang.annotation.Annotation> A getAnnotation(java.lang.Class<A> arg0) {return null;}
            public java.util.Map<java.lang.String,java.lang.Object> getAnnotationMap() {return null;}
            public int getModifiers() {return 0;}
            public java.lang.String getName() {return null;}
            public <A extends java.lang.annotation.Annotation> A[] getAnnotations(java.lang.Class<A> arg0) {return null;}
            public <A extends java.lang.annotation.Annotation> boolean hasAnnotation(java.lang.Class<A> arg0) {return false;}
            public boolean isPublic() {return false;}
            public boolean isStatic() {return false;}
            public boolean isTransient() {return false;}
        };
        assertNotNull(context.keyDeserializerInstance(ann, new Object()));
        Exception thrown = null;
        try {
            context.keyDeserializerInstance(ann, new Class());
        } catch (IllegalStateException e) {
            thrown = e;
        }
        assertNotNull(thrown);
        thrown = null;
        try {
            context.keyDeserializerInstance(ann, new KeyDeserializer.None());
        } catch (IllegalStateException e) {
            thrown = e;
        }
        assertNotNull(thrown);
    }
    
    @Test
    public void testWith() {
        DefaultDeserializationContext.Impl context = new DefaultDeserializationContext.Impl(null);
        assertNotNull(context.with(null));
    }
    
    @Test
    public void testCreateInstance() {
        DefaultDeserializationContext.Impl context = new DefaultDeserializationContext.Impl(null);
        assertNotNull(context.createInstance(null, null, null));
    }
    
    @Test
    public void testCopy() {
        DefaultDeserializationContext.Impl context = new DefaultDeserializationContext.Impl(null);
        assertNotNull(context.copy());
    }

}