package com.fasterxml.jackson.databind.deser.test;

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
import org.junit.Test;

import static org.junit.Assert.*;

public class DefaultDeserializationContextTest {

    @Test
    public void testFindObjectId() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl.Builder()
                .withObjectIdGenerator(new ObjectIdGenerator() {
                    @Override
                    public ObjectIdGenerator.IdKey key(Object id) {
                        return new IdKey() {
                            @Override
                            public String id() {
                                return id.toString();
                            }
                        };
                    }
                })
                .build();

        Object id = new Object();
        ObjectIdResolver resolver = new SimpleObjectIdResolver();

        ReadableObjectId objectId = context.findObjectId(id, new ObjectIdGenerator<Object>() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return context.getObjectIdGenerator().key(id);
            }
        }, resolver);

        assertEquals(id.toString(), objectId.getId());
        assertEquals(resolver, objectId.getResolver());
    }

    @Test(expected = UnresolvedForwardReference.class)
    public void testCheckUnresolvedObjectId() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl.Builder().build();

        Object id1 = new Object();
        Object id2 = new Object();
        context.findObjectId(id1, null, null);
        context.findObjectId(id2, null, null);

        context.checkUnresolvedObjectId();
    }

    @Test
    public void testDeserializerInstance() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl.Builder()
                .withObjectMapper(new ObjectMapper())
                .build();

        Annotated ann = mock(Annotated.class);
        Object deserDef = mock(JsonDeserializer.class);

        JsonDeserializer<Object> deserializer = context.deserializerInstance(ann, deserDef);
        assertTrue(deserializer instanceof JsonDeserializer);

        ann = mock(Annotated.class);
        deserDef = mock(Class.class);
        assertEquals(JsonDeserializer.class, deserializerInstance(context, ann, deserDef).getClass());

        ann = mock(Annotated.class);
        deserDef = null;
        assertNull(deserializerInstance(context, ann, deserDef));
    }

    @Test
    public void testKeyDeserializerInstance() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl.Builder()
                .withObjectMapper(new ObjectMapper())
                .build();

        Annotated ann = mock(Annotated.class);
        Object deserDef = mock(JsonDeserializer.class);

        KeyDeserializer keyDeserializer = context.keyDeserializerInstance(ann, deserDef);
        assertTrue(keyDeserializer instanceof KeyDeserializer);

        ann = mock(Annotated.class);
        deserDef = mock(Class.class);
        assertEquals(KeyDeserializer.class, keyDeserializerInstance(context, ann, deserDef).getClass());

        ann = mock(Annotated.class);
        deserDef = null;
        assertNull(keyDeserializerInstance(context, ann, deserDef));
    }

    private static JsonDeserializer<?> deserializerInstance(DefaultDeserializationContext context, Annotated ann, Object deserDef) throws JsonMappingException {
        if (deserDef == null) {
            return null;
        }

        JsonDeserializer<?> deser;

        if (deserDef instanceof JsonDeserializer) {
            deser = (JsonDeserializer<?>) deserDef;
        } else {
            Class<?> deserClass = (Class<?>)deserDef;
            HandlerInstantiator hi = context.getConfig().getHandlerInstantiator();
            deser = (hi == null) ? null : hi.deserializerInstance(context.getConfig(), ann, deserClass);
            if (deser == null) {
                deser = (JsonDeserializer<?>) ClassUtil.createInstance(deserClass, context.getConfig().canOverrideAccessModifiers());
            }
        }

        if (deser instanceof ResolvableDeserializer) {
            ((ResolvableDeserializer) deser).resolve(context);
        }

        return (JsonDeserializer<Object>) deser;
    }

    private static KeyDeserializer keyDeserializerInstance(DefaultDeserializationContext context, Annotated ann, Object deserDef) throws JsonMappingException {
        if (deserDef == null) {
            return null;
        }

        KeyDeserializer deser;

        if (deserDef instanceof KeyDeserializer) {
            deser = (KeyDeserializer) deserDef;
        } else {
            Class<?> deserClass = (Class<?>)deserDef;
            HandlerInstantiator hi = context.getConfig().getHandlerInstantiator();
            deser = (hi == null) ? null : hi.keyDeserializerInstance(context.getConfig(), ann, deserClass);
            if (deser == null) {
                deser = (KeyDeserializer) ClassUtil.createInstance(deserClass, context.getConfig().canOverrideAccessModifiers());
            }
        }

        if (deser instanceof ResolvableDeserializer) {
            ((ResolvableDeserializer) deser).resolve(context);
        }

        return deser;
    }
}