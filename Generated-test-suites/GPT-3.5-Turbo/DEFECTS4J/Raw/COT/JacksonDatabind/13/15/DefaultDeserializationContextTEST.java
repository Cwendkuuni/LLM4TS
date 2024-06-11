
package com.fasterxml.jackson.databind.deser;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializerFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;

import static org.junit.Assert.*;

public class DefaultDeserializationContextTest {

    private DefaultDeserializationContext context;

    @Before
    public void setUp() {
        context = new DefaultDeserializationContext.Impl(new DeserializerFactory(), null);
    }

    @After
    public void tearDown() {
        context = null;
    }

    @Test
    public void testFindObjectId() {
        ObjectIdGenerator<?> gen = new ObjectIdGenerator<IdKey>() {
            @Override
            public IdKey key(Object key) {
                return null;
            }

            @Override
            public boolean canUseFor(ObjectIdGenerator<?> gen) {
                return false;
            }

            @Override
            public ObjectIdGenerator<?> forScope(Class<?> scope) {
                return null;
            }

            @Override
            public ObjectIdGenerator<?> newForSerialization(Object context) {
                return null;
            }

            @Override
            public JsonDeserializer<Object> getDeserializer() {
                return null;
            }
        };

        ObjectIdResolver resolverType = new SimpleObjectIdResolver();

        ReadableObjectId result = context.findObjectId(null, gen, resolverType);

        assertNotNull(result);
    }

    @Test
    public void testCheckUnresolvedObjectId() {
        ReadableObjectId readableObjectId = new ReadableObjectId(null);
        context.checkUnresolvedObjectId();
    }

    @Test
    public void testDeserializerInstanceDeserializer() throws Exception {
        Annotated ann = null;
        Object deserDef = null;

        JsonDeserializer<Object> result = context.deserializerInstance(ann, deserDef);

        assertNull(result);
    }

    @Test
    public void testDeserializerInstanceClass() throws Exception {
        Annotated ann = null;
        Object deserDef = DefaultDeserializationContext.class;

        JsonDeserializer<Object> result = context.deserializerInstance(ann, deserDef);

        assertNotNull(result);
    }

    @Test
    public void testKeyDeserializerInstanceDeserializer() throws Exception {
        Annotated ann = null;
        Object deserDef = null;

        KeyDeserializer result = context.keyDeserializerInstance(ann, deserDef);

        assertNull(result);
    }

    @Test
    public void testKeyDeserializerInstanceClass() throws Exception {
        Annotated ann = null;
        Object deserDef = DefaultDeserializationContext.class;

        KeyDeserializer result = context.keyDeserializerInstance(ann, deserDef);

        assertNull(result);
    }

    @Test
    public void testWith() throws Exception {
        DeserializerFactory factory = new DeserializerFactory();

        DefaultDeserializationContext result = context.with(factory);

        assertNotNull(result);
    }

    @Test
    public void testCreateInstance() throws Exception {
        DeserializationConfig config = new DeserializationConfig(null, null, null, null);
        JsonParser jp = null;
        InjectableValues values = null;

        DefaultDeserializationContext result = context.createInstance(config, jp, values);

        assertNotNull(result);
    }
}
