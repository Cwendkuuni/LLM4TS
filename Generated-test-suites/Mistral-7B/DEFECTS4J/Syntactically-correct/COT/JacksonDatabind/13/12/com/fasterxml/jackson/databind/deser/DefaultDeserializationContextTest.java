package com.example.junit.test.jackson;

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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DefaultDeserializationContextTest {

    @Test
    public void testFindObjectId() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DefaultDeserializerFactory()) {
            @Override
            public JsonDeserializer<?> deserializerInstance(Annotated ann, Object deserDef) throws JsonMappingException {
                return null;
            }
        };

        ObjectIdGenerator generator = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey() {};
            }
        };

        ObjectIdResolver resolver = new ObjectIdResolver() {
            @Override
            public ObjectIdResolver newForDeserialization(DefaultDeserializationContext ctxt) {
                return null;
            }
        };

        ReadableObjectId result = context.findObjectId(new Object(), generator, resolver);
        assertNull(result);

        result = context.findObjectId(new Object(), generator);
        assertNull(result);

        result = context.findObjectId(new Object(), generator, resolver);
        assertNotNull(result);
    }

    @Test(expected = UnresolvedForwardReference.class)
    public void testCheckUnresolvedObjectId() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DefaultDeserializerFactory()) {
            @Override
            protected LinkedHashMap<ObjectIdGenerator.IdKey, ReadableObjectId> _objectIds() {
                return new LinkedHashMap<ObjectIdGenerator.IdKey, ReadableObjectId>() {{
                    put(new ObjectIdGenerator.IdKey() {}, new ReadableObjectId(new ObjectIdGenerator.IdKey() {}));
                }};
            }
        };
        context.checkUnresolvedObjectId();
    }

    @Test
    public void testDeserializerInstance() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DefaultDeserializerFactory()) {
            @Override
            public JsonDeserializer<?> deserializerInstance(Annotated ann, Object deserDef) throws JsonMappingException {
                return null;
            }
        };

        Annotated ann = null;
        Object deserDef = null;
        JsonDeserializer<?> result = context.deserializerInstance(ann, deserDef);
        assertNull(result);

        result = context.deserializerInstance(ann, new Object());
        assertNull(result);

        result = context.deserializerInstance(ann, new JsonDeserializer<Object>() {});
        assertNotNull(result);

        result = context.deserializerInstance(ann, JsonDeserializer.None.class);
        assertNull(result);
    }

    @Test
    public void testKeyDeserializerInstance() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DefaultDeserializerFactory()) {
            @Override
            public KeyDeserializer keyDeserializerInstance(Annotated ann, Object deserDef) throws JsonMappingException {
                return null;
            }
        };

        Annotated ann = null;
        Object deserDef = null;
        KeyDeserializer result = context.keyDeserializerInstance(ann, deserDef);
        assertNull(result);

        result = context.keyDeserializerInstance(ann, new Object());
        assertNull(result);

        result = context.keyDeserializerInstance(ann, new KeyDeserializer() {});
        assertNotNull(result);

        result = context.keyDeserializerInstance(ann, KeyDeserializer.None.class);
        assertNull(result);
    }

    @Test
    public void testCopy() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DefaultDeserializerFactory());
        DefaultDeserializationContext copy = context.copy();
        assert (context != copy);
        assertNotNull(copy._objectIds);
        assertNotNull(copy._objectIdResolvers);
    }

    @Test
    public void testWith() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DefaultDeserializerFactory());
        DefaultDeserializationContext with = context.with(new DefaultDeserializerFactory());
        assert (context != with);
        assertNotNull(with._objectIds);
        assertNotNull(with._objectIdResolvers);
    }

    @Test
    public void testCreateInstance() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DefaultDeserializerFactory());
        DefaultDeserializationContext result = context.createInstance(new DeserializationConfig(), null, null);
        assert (context != result);
        assertNotNull(result._objectIds);
        assertNotNull(result._objectIdResolvers);
    }
}