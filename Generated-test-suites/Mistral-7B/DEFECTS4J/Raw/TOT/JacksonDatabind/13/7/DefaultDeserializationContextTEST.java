package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

public class DefaultDeserializationContextTest extends TestBase {

    public void testFindObjectId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext ctx = mapper.getDeserializationContext();

        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        ObjectIdGenerator gen = ObjectIdGenerator.ValueIdGenerator.instance;

        Object id = new Object();

        ReadableObjectId expected = ctx.findObjectId(id, gen, resolver);
        assertNotNull(expected);
        assertSame(id, expected.getId());
        assertSame(resolver, expected.getResolver());

        // Test with null resolver
        ObjectIdResolver nullResolver = null;
        ReadableObjectId nullResult = ctx.findObjectId(id, gen, nullResolver);
        assertNull(nullResult);

        // Test with different resolver
        ObjectIdGenerator customGen = new ObjectIdGenerator() {
            @Override
            public ObjectId idFromObject(Object id) {
                return new ObjectId("custom-id");
            }
        };
        ObjectIdResolver customResolver = new SimpleObjectIdResolver();
        ReadableObjectId customResult = ctx.findObjectId(id, customGen, customResolver);
        assertNotNull(customResult);
        assertNotSame(expected, customResult);
        assertSame(customGen, customResult.getGenerator());
        assertSame(customResolver, customResult.getResolver());
    }

    public void testCheckUnresolvedObjectId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext ctx = mapper.getDeserializationContext();

        ObjectIdGenerator gen = ObjectIdGenerator.ValueIdGenerator.instance;
        Object object1 = new Object();
        Object object2 = new Object();

        ctx.findObjectId(object1, gen, new SimpleObjectIdResolver());
        ctx.findObjectId(object2, gen, new SimpleObjectIdResolver());

        // No unresolved references
        ctx.checkUnresolvedObjectId();
        // Unresolved references
        ObjectIdGenerator customGen = new ObjectIdGenerator() {
            @Override
            public ObjectId idFromObject(Object id) {
                return null;
            }
        };
        ObjectIdResolver customResolver = new SimpleObjectIdResolver();
        ctx.findObjectId(object1, gen, customResolver);
        ctx.findObjectId(object2, customGen, customResolver);

        try {
            ctx.checkUnresolvedObjectId();
            fail("Expected UnresolvedForwardReference to be thrown");
        } catch (UnresolvedForwardReference e) {
            // Expected
        }
    }

    public void testDeserializerInstance() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext ctx = mapper.getDeserializationContext();

        Object id = new Object();
        JsonDeserializer<Object> deserializer = new JsonDeserializer<Object>() {};
        JsonDeserializer<Object> expected = deserializer;

        Annotated ann = mock(Annotated.class);
        Object deserDef = deserializer;

        JsonDeserializer<Object> result = ctx.deserializerInstance(ann, deserDef);
        assertSame(expected, result);

        // Test with null deserDef
        result = ctx.deserializerInstance(ann, null);
        assertNull(result);

        // Test with class
        Class<?> deserClass = JsonDeserializer.class;
        JsonDeserializer<Object> classResult = ctx.deserializerInstance(ann, deserClass);
        assertNotNull(classResult);
        assertSame(deserializer.getClass(), classResult.getClass());
    }
}