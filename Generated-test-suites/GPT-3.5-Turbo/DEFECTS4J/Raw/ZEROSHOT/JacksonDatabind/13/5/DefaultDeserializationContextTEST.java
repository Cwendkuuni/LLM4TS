package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.*;
import org.junit.*;

public class DefaultDeserializationContextTest {

    private DefaultDeserializationContext context;

    @Before
    public void setup() {
        context = new DefaultDeserializationContext.Impl(null);
    }

    @Test
    public void testFindObjectId() {
        Object id = new Object();
        ObjectIdGenerator<?> gen = new ObjectIdGenerator<>();
        ObjectIdResolver resolverType = new SimpleObjectIdResolver();

        ReadableObjectId result = context.findObjectId(id, gen, resolverType);

        assertNotNull(result);
    }

    @Test
    public void testCopy() {
        DefaultDeserializationContext contextCopy = context.copy();

        assertNotNull(contextCopy);
    }

    @Test
    public void testDeserializerInstance() {
        Annotated ann = null;
        Object deserDef = null;

        JsonDeserializer<Object> result = context.deserializerInstance(ann, deserDef);

        assertNull(result);
    }

    @Test
    public void testKeyDeserializerInstance() {
        Annotated ann = null;
        Object deserDef = null;

        KeyDeserializer result = context.keyDeserializerInstance(ann, deserDef);

        assertNull(result);
    }

    @Test
    public void testWith() {
        DeserializerFactory factory = null;

        DefaultDeserializationContext result = context.with(factory);

        assertNotNull(result);
    }

    @Test
    public void testCreateInstance() {
        DeserializationConfig config = null;
        JsonParser jp = null;
        InjectableValues values = null;

        DefaultDeserializationContext result = context.createInstance(config, jp, values);

        assertNotNull(result);
    }

    @After
    public void teardown() {
        context = null;
    }
}