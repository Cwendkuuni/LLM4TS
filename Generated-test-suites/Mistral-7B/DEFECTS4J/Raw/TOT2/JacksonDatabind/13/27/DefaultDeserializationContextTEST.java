package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
import com.fasterxml.jackson.test.BaseTest;

import org.junit.Before;
import org.junit.Test;

public class DefaultDeserializationContextTest extends BaseTest {

    private DefaultDeserializationContext context;
    private DefaultDeserializationContext context2;
    private HandlerInstantiator handlerInstantiator;

    @Before
    public void setup() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        context = mapper.getDeserializationContext();
        context2 = context.with(mapper.getDeserializerFactory());
        handlerInstantiator = mapper.getConfig().getHandlerInstantiator();
    }

    @Test
    public void testFindObjectId() throws Exception {
        Object id = new Object();
        ObjectIdGenerator<?> gen = new StubObjectIdGenerator();
        ObjectIdResolver resolverType = new StubObjectIdResolver();

        // Test finding an existing object id
        ObjectIdGenerator.IdKey key = gen.key(id);
        ReadableObjectId objId = context.findObjectId(id, gen, resolverType);
        assertNotNull(objId);
        assertEquals(id, objId.getId());

        // Test finding a new object id
        objId = context.findObjectId(id, gen, resolverType);
        assertNotNull(objId);
        assertNull(objId.getResolver());

        // Test finding a previously registered object id
        context._objectIds = Collections.singletonMap(key, objId);
        objId = context.findObjectId(id, gen, resolverType);
        assertNotNull(objId);
        assertSame(objId, context._objectIds.get(key));

        // Test finding a previously registered object id with a resolver
        context._objectIds = Collections.singletonMap(key, new ReadableObjectId(key));
        ObjectIdResolver resolver = new StubObjectIdResolver();
        context._objectIdResolvers = Collections.singletonList(resolver);
        objId = context.findObjectId(id, gen, resolverType);
        assertNotNull(objId);
        assertSame(objId, context._objectIds.get(key));
        assertSame(resolver, objId.getResolver());

        // Test finding a null object id
        objId = context.findObjectId(null, gen, resolverType);
        assertNull(objId);

        // Test finding a null generator
        ObjectIdGenerator<?> nullGen = null;
        objId = context.findObjectId(id, nullGen, resolverType);
        assertNull(objId);

        // Test finding a null resolver type
        ObjectIdResolver nullResolverType = null;
        objId = context.findObjectId(id, gen, nullResolverType);
        assertNull(objId);
    }

    @Test
    public void testCheckUnresolvedObjectId() throws Exception {
        Object id1 = new Object();
        Object id2 = new Object();
        ObjectIdGenerator<?> gen = new StubObjectIdGenerator();

        context._objectIds = new LinkedHashMap<ObjectIdGenerator.IdKey, ReadableObjectId>() {{
            put(new IdKey(id1.getClass()), new ReadableObjectId(new IdKey(id1.getClass())));
        }};

        try {
            context.checkUnresolvedObjectId();
            fail("Expected UnresolvedForwardReference exception");
        } catch (UnresolvedForwardReference e) {
            // Expected
        }

        context._objectIds.remove(new IdKey(id1.getClass()));

        try {
            context.checkUnresolvedObjectId();
            // No exception expected, as unresolved refs are skipped if not enabled
        } catch (UnresolvedForwardReference e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        context.disable(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS);

        try {
            context.checkUnresolvedObjectId();
            // No exception expected
        } catch (UnresolvedForwardReference e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        context._objectIds = new LinkedHashMap<ObjectIdGenerator.IdKey, ReadableObjectId>() {{
            put(new IdKey(id1.getClass()), new ReadableObjectId(new IdKey(id1.getClass()), Arrays.asList(new Referring(id2.getClass(), new ObjectLocation("test")))));
            put(new IdKey(id2.getClass()), new ReadableObjectId(new IdKey(id2.getClass())));
        }};

        try {
            context.checkUnresolvedObjectId();
            fail("Expected UnresolvedForwardReference exception");
        } catch (UnresolvedForwardReference e) {
            List<UnresolvedId> unresolvedIds = e.getUnresolvedIds();
            assertEquals(1, unresolvedIds.size());
            assertEquals(id1.getClass(), unresolvedIds.get(0).getDesiredType());
            assertSame(id1, unresolvedIds.get(0).getIdValue());
        }
    }

    @Test
    public void testDeserializerInstance() throws Exception {
        Annotated ann = mock(Annotated.class);
        Object deserDef = new JsonDeserializer<String>(String.class) {};

        JsonDeserializer<String> deser = context.deserializerInstance(ann, deserDef);
        assertNotNull(deser);
        assertEquals(deserDef.getClass(), deser.getClass());

        deser = context.deserializerInstance(ann, deserDef.getClass());
        assertNotNull(deser);
        assertEquals(deserDef.getClass(), deser.getClass());

        deser = context.deserializerInstance(ann, null);
        assertNull(deser);

        deser = context.deserializerInstance(ann, new Class<JsonDeserializer<String>>(){});
        assertNotNull(deser);
        assertEquals(deserDef.getClass(), deser.getClass());

        deser = context.deserializerInstance(ann, new Class<JsonDeserializer<String>>(){}, handlerInstantiator);
        assertNotNull(deser);
        assertEquals(deserDef.getClass(), deser.getClass());
    }

    @Test
    public void testKeyDeserializerInstance() throws Exception {
        Annotated ann = mock(Annotated.class);
        Object deserDef = new KeyDeserializer.StringKeyDeserializer() {};

        KeyDeserializer deser = context.keyDeserializerInstance(ann, deserDef);
        assertNotNull(deser);
        assertEquals(deserDef.getClass(), deser.getClass());

        deser = context.keyDeserializerInstance(ann, deserDef.getClass());
        assertNotNull(deser);
        assertEquals(deserDef.getClass(), deser.getClass());

        deser = context.keyDeserializerInstance(ann, null);
        assertNull(deser);

        deser = context.keyDeserializerInstance(ann, new Class<KeyDeserializer>() {});
        assertNotNull(deser);
        assertEquals(deserDef.getClass(), deser.getClass());

        deser = context.keyDeserializerInstance(ann, new Class<KeyDeserializer>() {}, handlerInstantiator);
        assertNotNull(deser);
        assertEquals(deserDef.getClass(), deser.getClass());
    }

    private static class StubObjectIdGenerator implements ObjectIdGenerator<Object> {
        @Override
        public ObjectIdGenerator.IdKey key(Object id) {
            return new IdKey(id.getClass());
        }
    }

    private static class StubObjectIdResolver implements ObjectIdResolver {
        @Override
        public ObjectIdResolver newForDeserialization(DefaultDeserializationContext ctxt) {
            return this;
        }

        @Override
        public ReadableObjectId idResolved(DefaultDeserializationContext ctxt, ObjectIdGenerator.IdKey key) {
            return null;
        }

        @Override
        public ReadableObjectId idResolved(DefaultDeserializationContext ctxt, ObjectIdGenerator.IdKey key, Object id) {
            return null;
        }
    }

    private static class ObjectLocation {
        private String location;

        public ObjectLocation(String location) {
            this.location = location;
        }

        @Override
        public String toString() {
            return location;
        }
    }
}