package com.example.jackson.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
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

    private DefaultDeserializationContext context;
    private DeserializerFactory deserializerFactory;
    private DeserializerCache deserializerCache;

    @Before
    public void setup() {
        deserializerFactory = new StandardDeserializerFactory();
        deserializerCache = new DeserializerCacheImpl();
        context = new DefaultDeserializationContext.Impl(deserializerFactory, deserializerCache);
    }

    /**
     * Test method for {@link DefaultDeserializationContext#DefaultDeserializationContext(DeserializerFactory, DeserializerCache)}
     */
    @Test
    public void testDefaultConstructor() {
        assertNotNull(context);
        assertNull(context._objectIds);
        assertNull(context._objectIdResolvers);
    }

    /**
     * Test method for {@link DefaultDeserializationContext#DefaultDeserializationContext(DefaultDeserializationContext, DeserializationConfig, JsonParser, InjectableValues)}
     */
    @Test
    public void testCopyConstructor() {
        DefaultDeserializationContext src = new DefaultDeserializationContext.Impl(context);
        DefaultDeserializationContext copied = context.copy();
        assertNotSame(src, copied);
        assertEquals(context, copied);
    }

    /**
     * Test method for {@link DefaultDeserializationContext#copy()}
     */
    @Test(expected = IllegalStateException.class)
    public void testCopyIllegalStateException() {
        context.copy();
    }

    /**
     * Test method for {@link DefaultDeserializationContext#findObjectId(Object, ObjectIdGenerator, ObjectIdResolver)}
     */
    @Test
    public void testFindObjectId() {
        ObjectIdGenerator idGenerator = new StdObjectIdGenerator();
        ObjectIdResolver idResolver = new StdObjectIdResolver();

        Object key = new Object();
        ObjectIdGenerator.IdKey idKey = idGenerator.key(key);

        context._objectIds = new LinkedHashMap<>();
        ReadableObjectId id = context.findObjectId(key, idGenerator, idResolver);
        assertNotNull(id);
        assertNull(id._id);
        assertEquals(idKey, id.getIdKey());

        id = context.findObjectId(key, idGenerator);
        assertEquals(id, context.findObjectId(key, idGenerator, idResolver));

        context._objectIds = null;
        id = context.findObjectId(key, idGenerator, idResolver);
        assertNull(id);
    }

    /**
     * Test method for {@link DefaultDeserializationContext#findObjectId(Object, ObjectIdGenerator)}
     */
    @Test
    public void testFindObjectIdWithDefaultResolver() {
        ObjectIdGenerator idGenerator = new StdObjectIdGenerator();

        Object key = new Object();
        ObjectIdGenerator.IdKey idKey = idGenerator.key(key);

        context._objectIds = new LinkedHashMap<>();
        ReadableObjectId id = context.findObjectId(key, idGenerator);
        assertNotNull(id);
        assertNull(id._id);
        assertEquals(idKey, id.getIdKey());

        context._objectIds = null;
        id = context.findObjectId(key, idGenerator);
        assertNull(id);
    }

    /**
     * Test method for {@link DefaultDeserializationContext#checkUnresolvedObjectId()}
     */
    @Test(expected = UnresolvedForwardReference.class)
    public void testCheckUnresolvedObjectIdWithUnresolvedIds() {
        ObjectIdGenerator idGenerator = new StdObjectIdGenerator();
        ObjectIdResolver idResolver = new StdObjectIdResolver();

        Object idKey1 = new Object();
        Object idKey2 = new Object();

        context._objectIds = new LinkedHashMap<>();
        context.findObjectId(idKey1, idGenerator, idResolver);
        context.findObjectId(idKey2, idGenerator, idResolver);

        try {
            context.checkUnresolvedObjectId();
            fail("Expected UnresolvedForwardReference exception");
        } catch (UnresolvedForwardReference e) {
            List<Entry<Object, Object>> entries = e.getUnresolvedIds();
            assertEquals(2, entries.size());
            assertEquals(idKey1.toString(), entries.get(0).getKey().toString());
            assertEquals(idKey2.toString(), entries.get(1).getKey().toString());
        }
    }

    /**
     * Test method for {@link DefaultDeserializationContext#deserializerInstance(Annotated, Object)}
     */
    @Test(expected = IllegalStateException.class)
    public void testDeserializerInstanceWithInvalidDefinition() {
        Object id = new Object();
        Annotated ann = mock(Annotated.class);
        when(ann.getClass()).thenReturn(String.class);

        context.deserializerInstance(ann, id);
    }

    /**
     * Test method for {@link DefaultDeserializationContext#deserializerInstance(Annotated, Object)}
     */
    @Test
    public void testDeserializerInstanceWithJsonDeserializer() {
        ObjectIdGenerator idGenerator = new StdObjectIdGenerator();
        ObjectIdResolver idResolver = new StdObjectIdResolver();

        Object id = new Object();
        ObjectIdGenerator.IdKey idKey = idGenerator.key(id);

        Annotated ann = mock(Annotated.class);
        when(ann.getAnnotatedClass()).thenReturn(String.class);
        when(ann.findValue("@JsonDeserialize")).thenReturn(new StdJsonDeserializer());

        JsonDeserializer<Object> deserializer = context.deserializerInstance(ann, id);
        assertNotNull(deserializer);
        assertNotNull(deserializer.getClass());
        assertEquals(StdJsonDeserializer.class, deserializer.getClass());
    }

    /**
     * Test method for {@link DefaultDeserializationContext#deserializerInstance(Annotated, Object)}
     */
    @Test
    public void testDeserializerInstanceWithClass() {
        ObjectIdGenerator idGenerator = new StdObjectIdGenerator();
        ObjectIdResolver idResolver = new StdObjectIdResolver();

        Object id = new Object();
        ObjectIdGenerator.IdKey idKey = idGenerator.key(id);

        Annotated ann = mock(Annotated.class);
        when(ann.getAnnotatedClass()).thenReturn(String.class);
        when(ann.findValue("@JsonDeserialize")).thenReturn(null);

        HandlerInstantiator handlerInstantiator = mock(HandlerInstantiator.class);
        when(handlerInstantiator.deserializerInstance(any(DeserializationConfig.class), any(Annotated.class), eq(String.class)))
                .thenReturn(new StdJsonDeserializer());
        doReturn(handlerInstantiator).when(context)._config;

        JsonDeserializer<Object> deserializer = context.deserializerInstance(ann, id);
        assertNotNull(deserializer);
        assertNotNull(deserializer.getClass());
        assertEquals(StdJsonDeserializer.class, deserializer.getClass());
    }

    /**
     * Test method for {@link DefaultDeserializationContext#keyDeserializerInstance(Annotated, Object)}
     */
    @Test(expected = IllegalStateException.class)
    public void testKeyDeserializerInstanceWithInvalidDefinition() {
        Object id = new Object();
        Annotated ann = mock(Annotated.class);
        when(ann.getClass()).thenReturn(String.class);

        context.keyDeserializerInstance(ann, id);
    }

    /**
     * Test method for {@link DefaultDeserializationContext#keyDeserializerInstance(Annotated, Object)}
     */
    @Test
    public void testKeyDeserializerInstanceWithJsonKeyDeserializer() {
        ObjectIdGenerator idGenerator = new StdObjectIdGenerator();
        ObjectIdResolver idResolver = new StdObjectIdResolver();

        Object id = new Object();
        ObjectIdGenerator.IdKey idKey = idGenerator.key(id);

        Annotated ann = mock(Annotated.class);
        when(ann.getAnnotatedClass()).thenReturn(String.class);
        when(ann.findValue("@JsonDeserialize")).thenReturn(new StdJsonKeyDeserializer());

        KeyDeserializer deserializer = context.keyDeserializerInstance(ann, id);
        assertNotNull(deserializer);
        assertNotNull(deserializer.getClass());
        assertEquals(StdJsonKeyDeserializer.class, deserializer.getClass());
    }

    /**
     * Test method for {@link DefaultDeserializationContext#keyDeserializerInstance(Annotated, Object)}
     */
    @Test
    public void testKeyDeserializerInstanceWithClass() {
        ObjectIdGenerator idGenerator = new StdObjectIdGenerator();
        ObjectIdResolver idResolver = new StdObjectIdResolver();

        Object id = new Object();
        ObjectIdGenerator.IdKey idKey = idGenerator.key(id);

        Annotated ann = mock(Annotated.class);
        when(ann.getAnnotatedClass()).thenReturn(String.class);
        when(ann.findValue("@JsonDeserialize")).thenReturn(null);

        HandlerInstantiator handlerInstantiator = mock(HandlerInstantiator.class);
        when(handlerInstantiator.keyDeserializerInstance(any(DeserializationConfig.class), any(Annotated.class), eq(String.class)))
                .thenReturn(new StdJsonKeyDeserializer());
        doReturn(handlerInstantiator).when(context)._config;

        KeyDeserializer deserializer = context.keyDeserializerInstance(ann, id);
        assertNotNull(deserializer);
        assertNotNull(deserializer.getClass());
        assertEquals(StdJsonKeyDeserializer.class, deserializer.getClass());
    }

    private static class StdJsonDeserializer extends JsonDeserializer<Object> {
        // Empty implementation
    }

    private static class StdJsonKeyDeserializer extends KeyDeserializer {
        // Empty implementation
    }

    private static class StdObjectIdGenerator implements ObjectIdGenerator<Object> {
        public ObjectIdGenerator.IdKey key(Object id) {
            return new IdKey() {
                @Override
                public Object getId() {
                    return id;
                }

                @Override
                public ObjectIdGenerator.IdKey key(Object id2) {
                    return new IdKey() {
                        // Empty implementation
                    };
                }
            };
        }
    }

    private static class StdObjectIdResolver implements ObjectIdResolver {
        public ReadableObjectId idFor(Object id, DeserializationContext ctxt) {
            return new ReadableObjectId(new IdKey() {
                @Override
                public Object getId() {
                    return id;
                }

                @Override
                public ObjectIdGenerator.IdKey key(Object id2) {
                    return new IdKey() {
                        // Empty implementation
                    };
                }
            });
        }

        public ObjectIdResolver newForDeserialization(DeserializationContext ctxt) {
            return this;
        }
    }
}