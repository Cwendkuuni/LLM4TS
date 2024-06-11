package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.*;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertThrows;

public class DefaultDeserializationContextTest extends TestBase {

    private DefaultDeserializationContext context;
    private DeserializerFactory df;
    private DeserializationConfig config;

    @BeforeClass
    public void setup() {
        df = new SimpleDeserializerFactory();
        config = new DeserializationConfig(new AnnotationIntrospector());
        context = new DefaultDeserializationContext(df, config);
    }

    @AfterClass
    public void teardown() {
        context = null;
        df = null;
        config = null;
    }

    // Test for findObjectId method
    @Test
    public void testFindObjectId() throws Exception {
        Object id = new Object();
        ObjectIdGenerator<?> gen = new StdObjectIdGenerator();

        ReadableObjectId roid = context.findObjectId(id, gen, new SimpleObjectIdResolver());
        assertNotNull(roid);
        assertEquals(roid.getKey().key, gen.key(id).getId());
    }

    @Test
    public void testFindObjectIdNullId() throws Exception {
        ObjectIdGenerator<?> gen = new StdObjectIdGenerator();

        assertNull(context.findObjectId(null, gen, new SimpleObjectIdResolver()));
    }

    @Test
    public void testFindObjectIdNullGenerator() throws Exception {
        Object id = new Object();

        assertThrows(NullPointerException.class,
                () -> context.findObjectId(id, null, new SimpleObjectIdResolver()));
    }

    // Test for checkUnresolvedObjectId method
    @Test
    public void testCheckUnresolvedObjectId() throws Exception {
        Object id1 = new Object();
        Object id2 = new Object();
        ObjectIdGenerator<?> gen = new StdObjectIdGenerator();

        context.findObjectId(id1, gen, new SimpleObjectIdResolver());
        context.findObjectId(id2, gen, new SimpleObjectIdResolver());

        context.checkUnresolvedObjectId();
    }

    @Test
    public void testCheckUnresolvedObjectIdUnresolvedId() throws Exception {
        Object id = new Object();
        ObjectIdGenerator<?> gen = new StdObjectIdGenerator();

        context.findObjectId(id, gen, new SimpleObjectIdResolver());
        context.checkUnresolvedObjectId();

        assertThrows(UnresolvedForwardReference.class, () -> context.checkUnresolvedObjectId());
    }

    // Test for deserializerInstance method
    @Test
    public void testDeserializerInstance() throws Exception {
        Annotated ann = mapper.getFactory().getAnnotationsForClass(String.class);
        Object deserDef = JsonDeserializer.None.class;

        JsonDeserializer<Object> deser = context.deserializerInstance(ann, deserDef);
        assertNull(deser);

        deserDef = mapper.getFactory().findValueDeserializer(String.class, null);
        deser = context.deserializerInstance(ann, deserDef);
        assertNotNull(deser);

        deserDef = mapper.getFactory().findKeyDeserializer(String.class, null);
        deser = context.deserializerInstance(ann, deserDef);
        assertNotNull(deser);
    }

    @Test
    public void testDeserializerInstanceNullDeserDef() throws Exception {
        Annotated ann = mapper.getFactory().getAnnotationsForClass(String.class);

        assertThrows(NullPointerException.class,
                () -> context.deserializerInstance(ann, null));
    }

    @Test
    public void testDeserializerInstanceNullAnn() throws Exception {
        Object deserDef = mapper.getFactory().findValueDeserializer(String.class, null);

        assertThrows(NullPointerException.class,
                () -> context.deserializerInstance(null, deserDef));
    }

    // Test for keyDeserializerInstance method
    @Test
    public void testKeyDeserializerInstance() throws Exception {
        Annotated ann = mapper.getFactory().getAnnotationsForClass(String.class);
        Object deserDef = JsonDeserializer.None.class;

        KeyDeserializer deser = context.keyDeserializerInstance(ann, deserDef);
        assertNull(deser);

        deserDef = mapper.getFactory().findKeyDeserializer(String.class, null);
        deser = context.keyDeserializerInstance(ann, deserDef);
        assertNotNull(deser);
    }

    @Test
    public void testKeyDeserializerInstanceNullDeserDef() throws Exception {
        Annotated ann = mapper.getFactory().getAnnotationsForClass(String.class);

        assertThrows(NullPointerException.class,
                () -> context.keyDeserializerInstance(ann, null));
    }

    @Test
    public void testKeyDeserializerInstanceNullAnn() throws Exception {
        Object deserDef = mapper.getFactory().findKeyDeserializer(String.class, null);

        assertThrows(NullPointerException.class,
                () -> context.keyDeserializerInstance(null, deserDef));
    }
}