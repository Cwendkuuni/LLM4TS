package com.example.junit.test.jackson.databind.deser;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

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
import com.fasterxml.jackson.databind.util.TestBase;

public class DefaultDeserializationContextTest extends TestBase {

    @Test
    public void testFindObjectId() throws Exception {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new StandardDeserializerProvider());
        ObjectIdGenerator idGenerator = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey(id.getClass());
            }
        };
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        ReadableObjectId expectedId = context.findObjectId(new Object(), idGenerator, resolver);
        assertNotNull(expectedId);
    }

    @Test
    public void testFindObjectIdWithNullId() throws Exception {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new StandardDeserializerProvider());
        ObjectIdGenerator idGenerator = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey(id.getClass());
            }
        };
        ReadableObjectId expectedId = context.findObjectId(null, idGenerator);
        assertNull(expectedId);
    }

    @Test
    public void testFindObjectIdWithEmptyObjectIdResolverList() throws Exception {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new StandardDeserializerProvider());
        ObjectIdGenerator idGenerator = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey(id.getClass());
            }
        };
        context.setObjectIdResolvers(new ArrayList<>());
        ReadableObjectId expectedId = context.findObjectId(new Object(), idGenerator);
        assertNull(expectedId);
    }

    @Test
    public void testFindObjectIdWithEmptyMap() throws Exception {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new StandardDeserializerProvider());
        ObjectIdGenerator idGenerator = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey(id.getClass());
            }
        };
        context._objectIds = null;
        ReadableObjectId expectedId = context.findObjectId(new Object(), idGenerator);
        assertNull(expectedId);
    }

    @Test(expected = UnresolvedForwardReference.class)
    public void testCheckUnresolvedObjectIdWithReferringProperties() throws Exception {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new StandardDeserializerProvider());
        context._objectIds = new LinkedHashMap<>();
        ReadableObjectId id1 = new ReadableObjectId(new IdKey(String.class));
        ReadableObjectId id2 = new ReadableObjectId(new IdKey(List.class));
        id1.addReferringProperty(id2);
        context._objectIds.put(id1.getKey(), id1);
        context.checkUnresolvedObjectId();
    }

    @Test
    public void testDeserializerInstance() throws Exception {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new StandardDeserializerProvider());
        Annotated ann = mock(Annotated.class);
        Object deserDef = new AnnotationIntrospector().findDeserializer(ann);
        JsonDeserializer deser = context.deserializerInstance(ann, deserDef);
        assertNotNull(deser);
    }

    @Test
    public void testDeserializerInstanceWithNullDeserDef() throws Exception {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new StandardDeserializerProvider());
        Annotated ann = mock(Annotated.class);
        JsonDeserializer deser = context.deserializerInstance(ann, null);
        assertNull(deser);
    }

    @Test(expected = IllegalStateException.class)
    public void testDeserializerInstanceWithInvalidDeserDefType() throws Exception {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new StandardDeserializerProvider());
        Annotated ann = mock(Annotated.class);
        Object deserDef = new Object();
        JsonDeserializer deser = context.deserializerInstance(ann, deserDef);
        fail("Deserializer instance was created despite invalid deserializer definition type");
    }

    @Test
    public void testDeserializerInstanceWithClassDeserDef() throws Exception {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new StandardDeserializerProvider());
        Annotated ann = mock(Annotated.class);
        Class<? extends JsonDeserializer> deserClass = JsonDeserializer.class;
        JsonDeserializer deser = context.deserializerInstance(ann, deserClass);
        assertNotNull(deser);
    }

    @Test
    public void testKeyDeserializerInstance() throws Exception {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new StandardDeserializerProvider());
        Annotated ann = mock(Annotated.class);
        Object deserDef = new AnnotationIntrospector().findKeyDeserializer(ann);
        KeyDeserializer deser = context.keyDeserializerInstance(ann, deserDef);
        assertNotNull(deser);
    }

    @Test
    public void testKeyDeserializerInstanceWithNullDeserDef() throws Exception {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new StandardDeserializerProvider());
        Annotated ann = mock(Annotated.class);
        KeyDeserializer deser = context.keyDeserializerInstance(ann, null);
        assertNull(deser);
    }

    @Test(expected = IllegalStateException.class)
    public void testKeyDeserializerInstanceWithInvalidDeserDefType() throws Exception {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new StandardDeserializerProvider());
        Annotated ann = mock(Annotated.class);
        Object deserDef = new Object();
        KeyDeserializer deser = context.keyDeserializerInstance(ann, deserDef);
        fail("Key deserializer instance was created despite invalid key deserializer definition type");
    }

    @Test
    public void testKeyDeserializerInstanceWithClassDeserDef() throws Exception {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new StandardDeserializerProvider());
        Annotated ann = mock(Annotated.class);
        Class<? extends KeyDeserializer> deserClass = KeyDeserializer.class;
        KeyDeserializer deser = context.keyDeserializerInstance(ann, deserClass);
        assertNotNull(deser);
    }
}