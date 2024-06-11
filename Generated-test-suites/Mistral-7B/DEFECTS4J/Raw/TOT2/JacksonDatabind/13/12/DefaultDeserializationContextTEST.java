package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext.Impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test case for DefaultDeserializationContext class
 */
public class DefaultDeserializationContextTest {

    private Impl context;
    private ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        context = mapper.getDeserializationContext().getDefaultDeserializationContext();
    }

    @After
    public void tearDown() throws Exception {
        context = null;
        mapper = null;
    }

    /**
     * Test constructor with null and non-null factory and cache
     */
    @Test
    public void testConstructor() {
        DefaultDeserializationContext dc1 = new DefaultDeserializationContext(null, null);
        DefaultDeserializationContext dc2 = new DefaultDeserializationContext(mapper.getDeserializerFactory(), null);
        DefaultDeserializationContext dc3 = new DefaultDeserializationContext(null, mapper.deserializationConfig());
        DefaultDeserializationContext dc4 = new DefaultDeserializationContext(mapper.getDeserializerFactory(), mapper.deserializationConfig());
        DefaultDeserializationContext dc5 = new DefaultDeserializationContext(context);
        DefaultDeserializationContext dc6 = new DefaultDeserializationContext(context, mapper.deserializationConfig());

        assertEquals(DefaultDeserializationContext.class, dc1.getClass());
        assertEquals(DefaultDeserializationContext.class, dc2.getClass());
        assertEquals(DefaultDeserializationContext.class, dc3.getClass());
        assertEquals(DefaultDeserializationContext.class, dc4.getClass());
        assertEquals(Impl.class, dc5.getClass());
        assertEquals(Impl.class, dc6.getClass());

        assertNull(dc1._objectIds);
        assertNull(dc1._objectIdResolvers);
        assertNull(dc2._objectIds);
        assertNull(dc2._objectIdResolvers);
        assertNull(dc3._objectIds);
        assertNull(dc3._objectIdResolvers);
        assertNotNull(dc4._objectIds);
        assertNotNull(dc4._objectIdResolvers);
    }

    /**
     * Test copy method
     * @throws IOException
     */
    @Test
    public void testCopy() throws IOException {
        context.findValue("null", null, null);

        DefaultDeserializationContext dc1 = context.copy();
        DefaultDeserializationContext dc2 = context.copy();
        DefaultDeserializationContext dc3 = context.copy();

        dc1._objectIds = new LinkedHashMap<>();
        dc1._objectIdResolvers = Arrays.asList(new SimpleObjectIdResolver(), new SimpleObjectIdResolver());

        dc2._objectIds = new LinkedHashMap<>();
        dc2._objectIdResolvers = Arrays.asList(new SimpleObjectIdResolver(), new SimpleObjectIdResolver());

        dc3._objectIds = new LinkedHashMap<>();
        dc3._objectIdResolvers = Arrays.asList(new SimpleObjectIdResolver(), null);

        ObjectIdGenerator idGen = new IdKey("test");
        ObjectIdResolver idResolver = new SimpleObjectIdResolver();

        Object id = new Object();
        ReadableObjectId idObj1 = context.findObjectId(id, idGen, idResolver);
        ReadableObjectId idObj2 = dc1.findObjectId(id, idGen, idResolver);
        ReadableObjectId idObj3 = dc2.findObjectId(id, idGen, idResolver);
        ReadableObjectId idObj4 = dc3.findObjectId(id, idGen, idResolver);

        assertEquals(idObj1, idObj2);
        assertEquals(idObj1, idObj3);
        assertNotEquals(idObj1, idObj4);
    }

    /**
     * Test findObjectId method with null and non-null id, gen and resolver
     */
    @Test
    public void testFindObjectId() {
        ObjectIdGenerator idGen = new IdKey("test");
        ObjectIdResolver idResolver = new SimpleObjectIdResolver();

        Object id = new Object();
        Object id2 = new Object();

        ReadableObjectId idObj1 = context.findObjectId(id, idGen, idResolver);
        ReadableObjectId idObj2 = context.findObjectId(id2, idGen, idResolver);

        assertNull(idObj1);
        assertNull(idObj2);

        context._objectIds = new LinkedHashMap<>();
        context._objectIdResolvers = Arrays.asList(new SimpleObjectIdResolver(), new SimpleObjectIdResolver());

        ReadableObjectId idObj3 = context.findObjectId(id, idGen, idResolver);
        ReadableObjectId idObj4 = context.findObjectId(id2, idGen, idResolver);

        assertEquals(idObj3, idObj3);
        assertNull(idObj4);

        ObjectIdResolver idResolver2 = new SimpleObjectIdResolver();
        context._objectIdResolvers = Arrays.asList(idResolver, idResolver2);

        ReadableObjectId idObj5 = context.findObjectId(id, idGen, idResolver);
        ReadableObjectId idObj6 = context.findObjectId(id, idGen, idResolver2);

        assertEquals(idObj3, idObj5);
        assertEquals(idObj3, idObj6);
    }

    /**
     * Test checkUnresolvedObjectId method with and without unresolved forward references
     */
    @Test
    public void testCheckUnresolvedObjectId() {
        ObjectIdGenerator idGen = new IdKey("test");
        ObjectIdResolver idResolver = new SimpleObjectIdResolver();

        context._objectIds = new LinkedHashMap<>();
        context._objectIdResolvers = Arrays.asList(new SimpleObjectIdResolver(), new SimpleObjectIdResolver());

        Object id = new Object();
        Object id2 = new Object();
        Object id3 = new Object();

        context.findObjectId(id, idGen, idResolver);
        context.findObjectId(id2, idGen, idResolver);
        context.findObjectId(id3, idGen, idResolver);

        try {
            context.checkUnresolvedObjectId();
            fail("Expected UnresolvedForwardReference");
        } catch (UnresolvedForwardReference e) {
            // Expected
        }

        context._objectIds.put(idGen.key(id), new ReadableObjectId(idGen.key(id)));

        try {
            context.checkUnresolvedObjectId();
            assertEquals(0, context._objectIds.size());
        } catch (UnresolvedForwardReference e) {
            // Expected
            fail("Unexpected exception: " + e.getMessage());
        }

        context._objectIds.put(idGen.key(id2), new ReadableObjectId(idGen.key(id2)));
        context._objectIds.put(idGen.key(id3), new ReadableObjectId(idGen.key(id3)));

        try {
            context.checkUnresolvedObjectId();
        } catch (UnresolvedForwardReference e) {
            // Expected
            Map<String, Class<?>> ids = new HashMap<>();
            ids.put("test.1", Object.class);
            ids.put("test.2", Object.class);
            ids.put("test.3", Object.class);
            assertEquals("Unresolved forward references for: Unresolved forward references for: [test.1, class Object]", e.getMessage());
            assertEquals(ids, e.getUnresolvedIds());
        }
    }

    /**
     * Test deserializerInstance method with null and non-null deserDef
     */
    @Test
    public void testDeserializerInstance() {
        context._objectIds = new LinkedHashMap<>();
        context._objectIdResolvers = Arrays.asList(new SimpleObjectIdResolver(), new SimpleObjectIdResolver());

        ObjectIdGenerator idGen = new IdKey("test");
        ObjectIdResolver idResolver = new SimpleObjectIdResolver();

        JsonDeserializer<Object> deser1 = new StdDeserializer<>();
        JsonDeserializer<Object> deser2 = new StdDeserializer<>();

        Annotated ann = new AnnotatedClass(Object.class);

        JsonDeserializer<Object> deserInstance1 = context.deserializerInstance(ann, deser1);
        assertEquals(deser1, deserInstance1);

        JsonDeserializer<Object> deserInstance2 = context.deserializerInstance(ann, deser2);
        assertEquals(deser2, deserInstance2);

        context._objectIds.put(idGen.key(deser1), new ReadableObjectId(idGen.key(deser1)));
        context._objectIds.put(idGen.key(deser2), new ReadableObjectId(idGen.key(deser2)));

        JsonDeserializer<Object> deserInstance3 = context.deserializerInstance(ann, deser1);
        assertEquals(deser1, deserInstance3);

        JsonDeserializer<Object> deserInstance4 = context.deserializerInstance(ann, deser2);
        assertEquals(deser2, deserInstance4);

        JsonDeserializer<Object> deserInstance5 = context.deserializerInstance(ann, null);
        assertNull(deserInstance5);
    }

    /**
     * Test keyDeserializerInstance method with null and non-null deserDef
     */
    @Test
    public void testKeyDeserializerInstance() {
        context._objectIds = new LinkedHashMap<>();
        context._objectIdResolvers = Arrays.asList(new SimpleObjectIdResolver(), new SimpleObjectIdResolver());

        ObjectIdGenerator idGen = new IdKey("test");
        ObjectIdResolver idResolver = new SimpleObjectIdResolver();

        KeyDeserializer deser1 = new StdKeyDeserializer<>();
        KeyDeserializer deser2 = new StdKeyDeserializer<>();

        Annotated ann = new AnnotatedClass(Object.class);

        KeyDeserializer deserInstance1 = context.keyDeserializerInstance(ann, deser1);
        assertEquals(deser1, deserInstance1);

        KeyDeserializer deserInstance2 = context.keyDeserializerInstance(ann, deser2);
        assertEquals(deser2, deserInstance2);

        context._objectIds.put(idGen.key(deser1), new ReadableObjectId(idGen.key(deser1)));
        context._objectIds.put(idGen.key(deser2), new ReadableObjectId(idGen.key(deser2)));

        KeyDeserializer deserInstance3 = context.keyDeserializerInstance(ann, deser1);
        assertEquals(deser1, deserInstance3);

        KeyDeserializer deserInstance4 = context.keyDeserializerInstance(ann, deser2);
        assertEquals(deser2, deserInstance4);

        KeyDeserializer deserInstance5 = context.keyDeserializerInstance(ann, null);
        assertNull(deserInstance5);
    }

    /**
     * Test with method
     */
    @Test
    public void testWith() {
        DefaultDeserializationContext dc1 = context.with(mapper.getDeserializerFactory());
        DefaultDeserializationContext dc2 = context.with(mapper.getDeserializerFactory(DeserializerFactory.StdDeserializer.class));
        DefaultDeserializationContext dc3 = context.with(mapper.getDeserializerFactory());

        assertEquals(context.getClass(), dc1.getClass());
        assertEquals(mapper.getDeserializerFactory(), dc1.getDeserializerFactory());
        assertEquals(context.getClass(), dc2.getClass());
        assertEquals(mapper.getDeserializerFactory(DeserializerFactory.StdDeserializer.class), dc2.getDeserializerFactory());
        assertEquals(context.getClass(), dc3.getClass());
        assertEquals(mapper.getDeserializerFactory(), dc3.getDeserializerFactory());
    }

    /**
     * Test createInstance method
     */
    @Test
    public void testCreateInstance() throws Exception {
        ObjectMapper mapper2 = new ObjectMapper();
        DefaultDeserializationContext dc1 = mapper2.getDeserializationContext().getDefaultDeserializationContext();

        ObjectMapper mapper3 = new ObjectMapper();
        DefaultDeserializationContext dc2 = mapper3.getDeserializationContext().getDefaultDeserializationContext();

        JsonParser jp = mapper.getFactory().createParser("{}");

        DefaultDeserializationContext dc3 = context.createInstance(mapper.deserializationConfig(), jp,
                mapper.getInjectableValues());
        DefaultDeserializationContext dc4 = dc1.createInstance(mapper.deserializationConfig(), jp,
                mapper.getInjectableValues());
        DefaultDeserializationContext dc5 = dc2.createInstance(mapper.deserializationConfig(), jp,
                mapper.getInjectableValues());

        assertEquals(context.getClass(), dc3.getClass());
        assertEquals(mapper.deserializationConfig(), dc3.getDeserializationConfig());
        assertEquals(mapper.getDeserializerFactory(), dc3.getDeserializerFactory());
        assertEquals(mapper.deserializationConfig(), dc4.getDeserializationConfig());
        assertEquals(mapper.getDeserializerFactory(), dc4.getDeserializerFactory());
        assertEquals(mapper2.deserializationConfig(), dc5.getDeserializationConfig());
        assertEquals(mapper2.getDeserializerFactory(), dc5.getDeserializerFactory());
    }
}