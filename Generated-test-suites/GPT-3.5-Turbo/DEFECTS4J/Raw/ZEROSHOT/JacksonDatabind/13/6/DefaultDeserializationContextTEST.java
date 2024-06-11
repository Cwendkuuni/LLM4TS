package com.fasterxml.jackson.databind.deser;

import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.Annotated;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.Assert.*;

public class DefaultDeserializationContextTest {

    private DefaultDeserializationContext context;

    @Before
    public void setUp() {
        context = new DefaultDeserializationContext.Impl(new DeserializerFactory(), new DeserializerCache());
    }

    @Test
    public void testFindObjectId() throws Exception {
        Object id = new Object();
        ObjectIdGenerator<?> gen = new ObjectIdGenerator<>();
        ObjectIdResolver resolverType = new SimpleObjectIdResolver();

        ReadableObjectId result = context.findObjectId(id, gen, resolverType);

        assertNotNull(result);
    }

    @Test
    public void testFindObjectIdWithNull() throws Exception {
        Object id = null;
        ObjectIdGenerator<?> gen = new ObjectIdGenerator<>();
        ObjectIdResolver resolverType = new SimpleObjectIdResolver();

        ReadableObjectId result = context.findObjectId(id, gen, resolverType);

        assertNotNull(result);
    }

    @Test
    public void testCheckUnresolvedObjectId() throws Exception {
        context.checkUnresolvedObjectId();
    }

    @Test
    public void testDeserializerInstance() throws Exception {
        Annotated ann = null;
        Object deserDef = null;

        JsonDeserializer<Object> result = context.deserializerInstance(ann, deserDef);

        assertNull(result);
    }

    @Test
    public void testDeserializerInstanceWithDeserDefIsNull() throws Exception {
        Annotated ann = null;
        Object deserDef = new Object();

        JsonDeserializer<Object> result = context.deserializerInstance(ann, deserDef);

        assertNull(result);
    }

    @Test(expected = IllegalStateException.class)
    public void testDeserializerInstanceWithDeserDefIsNotAJsonDeserializer() throws Exception {
        Annotated ann = null;
        Object deserDef = new Class<Object>() {
        };

        context.deserializerInstance(ann, deserDef);
    }

    @Test
    public void testKeyDeserializerInstance() throws Exception {
        Annotated ann = null;
        Object deserDef = null;

        KeyDeserializer result = context.keyDeserializerInstance(ann, deserDef);

        assertNull(result);
    }

    @Test
    public void testKeyDeserializerInstanceWithDeserDefIsNull() throws Exception {
        Annotated ann = null;
        Object deserDef = new Object();

        KeyDeserializer result = context.keyDeserializerInstance(ann, deserDef);

        assertNull(result);
    }

    @Test(expected = IllegalStateException.class)
    public void testKeyDeserializerInstanceWithDeserDefIsNotAKeyDeserializer() throws Exception {
        Annotated ann = null;
        Object deserDef = new Class<Object>() {
        };

        context.keyDeserializerInstance(ann, deserDef);
    }

    @Test
    public void testWith() throws Exception {
        DeserializerFactory factory = new DeserializerFactory();

        DefaultDeserializationContext result = context.with(factory);

        assertNotNull(result);
        assertNotSame(context, result);
    }

    @Test
    public void testCreateInstance() throws Exception {
        DeserializationConfig config = new DeserializationConfig();
        JsonParser jp = new JsonParser();
        InjectableValues values = new InjectableValues();

        DefaultDeserializationContext result = context.createInstance(config, jp, values);

        assertNotNull(result);
        assertNotSame(context, result);
    }

    @Test
    public void testCopy() throws Exception {
        DefaultDeserializationContext result = context.copy();

        assertNotNull(result);
        assertNotSame(context, result);
    }
}