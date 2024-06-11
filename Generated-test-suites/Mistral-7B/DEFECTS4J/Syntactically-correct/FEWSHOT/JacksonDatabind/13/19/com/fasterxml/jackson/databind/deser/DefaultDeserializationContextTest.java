package com.fasterxml.jackson.databind.deser.test;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.FieldVisibility;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

/**
 * Test cases for DefaultDeserializationContext class.
 */
public class DefaultDeserializationContextTest extends TestBase {

    @SuppressWarnings("unchecked")
    @Test
    public void testFindObjectId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext context = mapper.getDeserializationContext();

        // Test with null id and simple generator
        ObjectIdGenerator idGen = mapper.getPolymorphicTypeIdResolver();
        ObjectIdGenerator.IdKey key = idGen.key(null);
        ReadableObjectId id = context.findObjectId(null, idGen);
        assertNull(id);

        // Test with empty list of resolvers
        context = new DefaultDeserializationContext(context, null, Collections.emptyList());
        id = context.findObjectId(new Object(), idGen);
        assertNotNull(id);

        // Test with SimpleObjectIdResolver as resolver
        List<ObjectIdResolver> resolvers = Arrays.asList(new SimpleObjectIdResolver());
        context = new DefaultDeserializationContext(context, null, null, null);
        context._objectIdResolvers = resolvers;
        id = context.findObjectId(new Object(), idGen);
        assertNotNull(id);

        // Test with custom ObjectIdResolver
        ObjectIdResolver customResolver = new MyObjectIdResolver();
        resolvers = Arrays.asList(customResolver);
        context = new DefaultDeserializationContext(context, null, null, null);
        context._objectIdResolvers = resolvers;
        id = context.findObjectId(new Object(), idGen);
        assertNotNull(id);
        assertEquals(customResolver.getClass(), id.getResolver().getClass());

        // Test with multiple resolvers
        context = new DefaultDeserializationContext(context, null, null, null);
        context._objectIdResolvers = Arrays.asList(new SimpleObjectIdResolver(), customResolver);
        id = context.findObjectId(new Object(), idGen);
        assertNotNull(id);
        assertEquals(customResolver.getClass(), id.getResolver().getClass());

        // Test with unresolved object id
        context._objectIds = new LinkedHashMap<>();
        id = context.findObjectId(new Object(), idGen);
        assertNotNull(id);
        try {
            context.checkUnresolvedObjectId();
            fail("Expected UnresolvedForwardReference exception");
        } catch (UnresolvedForwardReference e) {
            // expected
        }

        context._objectIds.clear();
        context.checkUnresolvedObjectId();
        assertNull(id);
    }

    @Test
    public void testDeserializerInstance() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext context = mapper.getDeserializationContext();

        // Test with null annotated and deserDef
        assertNull(context.deserializerInstance(null, null));

        // Test with null annotated and non-null deserDef
        JsonDeserializer deser = new StdDeserializer<>();
        assertSame(deser, context.deserializerInstance(null, deser.getClass()));

        // Test with non-null annotated and null deserDef
        assertNull(context.deserializerInstance(createAnnotated(Foo.class), null));

        // Test with non-null annotated and non-null deserDef
        JsonDeserializer deser2 = new MyDeserializer<>();
        HandlerInstantiator hi = mapper.getConfig().getHandlerInstantiator();
        assertSame(deser2, context.deserializerInstance(createAnnotated(Foo.class), deser2.getClass()));
        assertSame(deser2, context.deserializerInstance(createAnnotated(Foo.class), hi.deserializerInstance(mapper.getConfig(), createAnnotated(Foo.class), deser2.getClass())));
    }

    @Test
    public void testKeyDeserializerInstance() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext context = mapper.getDeserializationContext();

        // Test with null annotated and deserDef
        assertNull(context.keyDeserializerInstance(null, null));

        // Test with null annotated and non-null deserDef
        KeyDeserializer deser = new StdKeyDeserializer<>();
        assertSame(deser, context.keyDeserializerInstance(null, deser.getClass()));

        // Test with non-null annotated and null deserDef
        assertNull(context.keyDeserializerInstance(createAnnotated(Foo.class), null));

        // Test with non-null annotated and non-null deserDef
        KeyDeserializer deser2 = new MyKeyDeserializer<>();
        HandlerInstantiator hi = mapper.getConfig().getHandlerInstantiator();
        assertSame(deser2, context.keyDeserializerInstance(createAnnotated(Foo.class), deser2.getClass()));
        assertSame(deser2, context.keyDeserializerInstance(createAnnotated(Foo.class), hi.keyDeserializerInstance(mapper.getConfig(), createAnnotated(Foo.class), deser2.getClass())));
    }

    @Test
    public void testFailOnUnresolvedObjectIds() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(FAIL_ON_UNRESOLVED_OBJECT_IDS);
        DefaultDeserializationContext context = mapper.getDeserializationContext();

        // Test with no unresolved object ids
        context.checkUnresolvedObjectId();

        // Test with unresolved object ids
        context._objectIds = new LinkedHashMap<>();
        context.addObjectId(new ObjectIdGenerator.IdKey(Foo.class), new ReadableObjectId(Foo.class));
        try {
            context.checkUnresolvedObjectId();
            fail("Expected UnresolvedForwardReference exception");
        } catch (UnresolvedForwardReference e) {
            // expected
        }

        // Test with resolved object ids
        context._objectIds.clear();
        context.addObjectId(new ObjectIdGenerator.IdKey(Foo.class), new ReadableObjectId(Foo.class));
        context.checkUnresolvedObjectId();
    }

    private Annotated createAnnotated(Class<?> clazz) {
        return new AnnotatedClass(clazz);
    }

    static class Foo {
        @JsonAutoDetect(fieldVisibility = FieldVisibility.ANY)
        int id;
    }

    static class StdDeserializer<T> extends JsonDeserializer<T> {
        // empty
    }

    static class MyDeserializer extends StdDeserializer<Foo> {
        // empty
    }

    static class StdKeyDeserializer extends KeyDeserializer {
        // empty
    }

    static class MyKeyDeserializer extends StdKeyDeserializer {
        // empty
    }

    static class MyObjectIdResolver implements ObjectIdResolver {
        @Override
        public ObjectIdResolver newForDeserialization(DefaultDeserializationContext ctxt) {
            return this;
        }

        @Override
        public ReadableObjectId idResolution(DeserializationContext ctxt, Object id) throws JsonProcessingException {
            return new ReadableObjectId(Foo.class, 1L);
        }
    }
}