package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class DefaultDeserializationContextTest {

    private DefaultDeserializationContext defaultDeserializationContext;

    @Before
    public void setUp() {
        defaultDeserializationContext = new DefaultDeserializationContext.Impl(null);
    }

    @Test
    public void testFindObjectId() {
        Object id = new Object();
        ObjectIdGenerator<?> gen = null;
        ObjectIdResolver resolverType = null;

        ReadableObjectId result = defaultDeserializationContext.findObjectId(id, gen, resolverType);

        assertNull(result);
    }

    @Test
    public void testFindObjectIdWithGenerator() {
        Object id = new Object();
        ObjectIdGenerator<?> gen = new ObjectIdGenerator<>(null, null);
        ObjectIdResolver resolverType = new SimpleObjectIdResolver();

        ReadableObjectId result = defaultDeserializationContext.findObjectId(id, gen, resolverType);

        assertNotNull(result);
    }

    @Test
    public void testFindObjectIdWithExistingEntry(){
        Object id = new Object();
        ObjectIdGenerator<?> gen = new ObjectIdGenerator<>(null, null);
        ObjectIdResolver resolverType = new SimpleObjectIdResolver();

        ReadableObjectId existingEntry = new ReadableObjectId(gen.key(id));
        defaultDeserializationContext._objectIds.put(gen.key(id), existingEntry);

        ReadableObjectId result = defaultDeserializationContext.findObjectId(id, gen, resolverType);

        assertSame(existingEntry, result);
    }

    @Test
    public void testCopy() {
        try {
            defaultDeserializationContext.copy();
            fail("Expected an IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            assertEquals("DefaultDeserializationContext sub-class not overriding copy()", e.getMessage());
        }
    }

    @Test
    public void testDeserializerInstance() {
        assertNull(defaultDeserializationContext.deserializerInstance(null, null));
    }

    @Test
    public void testKeyDeserializerInstance() {
        assertNull(defaultDeserializationContext.keyDeserializerInstance(null, null));
    }

    @Test
    public void testWith() {
        DeserializerFactory factory = null;
        DefaultDeserializationContext result = defaultDeserializationContext.with(factory);

        assertNotNull(result);
        assertNotSame(defaultDeserializationContext, result);
        assertSame(factory, result._factory);
    }

    @Test
    public void testCreateInstance() {
        DeserializationConfig config = null;
        JsonParser jp = null;
        InjectableValues values = null;
        DefaultDeserializationContext result = defaultDeserializationContext.createInstance(config, jp, values);

        assertNotNull(result);
        assertNotSame(defaultDeserializationContext, result);
        assertSame(config, result._config);
        assertSame(jp, result._parser);
        assertSame(values, result._injectableValues);
    }

    @Test
    public void testImpl() {
        DeserializerFactory df = null;
        DefaultDeserializationContext.Impl impl = new DefaultDeserializationContext.Impl(df);

        assertSame(df, impl._factory);
    }

    @Test
    public void testImplWithConfig() {
        DeserializationConfig config = null;
        JsonParser jp = null;
        InjectableValues values = null;
        DefaultDeserializationContext.Impl impl = new DefaultDeserializationContext.Impl(null, config, jp, values);

        assertSame(config, impl._config);
        assertSame(jp, impl._parser);
        assertSame(values, impl._injectableValues);
    }

    @Test
    public void testImplWithImpl() {
        DefaultDeserializationContext.Impl src = new DefaultDeserializationContext.Impl(null);
        DefaultDeserializationContext.Impl impl = new DefaultDeserializationContext.Impl(src);

        assertNotNull(impl);
        assertNotSame(src, impl);
    }

    @Test
    public void testImplWithSrcAndFactory() {
        DefaultDeserializationContext.Impl src = new DefaultDeserializationContext.Impl(null);
        DeserializerFactory factory = null;
        DefaultDeserializationContext.Impl impl = new DefaultDeserializationContext.Impl(src, factory);

        assertNotNull(impl);
        assertNotSame(src, impl);
        assertSame(factory, impl._factory);
    }
}