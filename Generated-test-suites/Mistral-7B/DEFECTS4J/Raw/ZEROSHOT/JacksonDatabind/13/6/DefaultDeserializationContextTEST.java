package com.example.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

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
import com.fasterxml.jackson.databind.util.TestBase;

public class DefaultDeserializationContextTest extends TestBase {

    @Test
    public void testFindObjectId() throws Exception {
        ObjectIdGenerator generator = new ObjectIdGenerator() {
            @Override
            public ObjectGenerator.IdKey key(Object id) {
                return new ObjectIdGenerator.IdKey(id.getClass(), "id");
            }
        };

        DefaultDeserializationContext context = new DefaultDeserializationContext(new DeserializerFactory(null), null);

        Object id = new Object();
        ReadableObjectId id1 = context.findObjectId(id, generator, null);
        assertEquals(id, id1.getId());

        Object id2 = id;
        ReadableObjectId id3 = context.findObjectId(id2, generator, null);
        assertEquals(id1, id3);

        context._objectIds = new LinkedHashMap<>();
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        context._objectIdResolvers = new ArrayList<>();
        context._objectIdResolvers.add(resolver);

        ObjectIdGenerator generator2 = new ObjectIdGenerator() {
            @Override
            public ObjectGenerator.IdKey key(Object id) {
                return new ObjectIdGenerator.IdKey(id.getClass(), "id2");
            }
        };
        ReadableObjectId id4 = context.findObjectId(id, generator2, null);
        assertNotNull(id4);
        assertNotSame(id1, id4);
        assertEquals(id, id4.getId());
        assertEquals(generator2.getClass(), id4.getGenerator().getClass());
        assertEquals(resolver, id4.getResolver());
    }

    @Test(expected = UnresolvedForwardReference.class)
    public void testCheckUnresolvedObjectId() throws Exception {
        ObjectIdGenerator generator = new ObjectIdGenerator() {
            @Override
            public ObjectGenerator.IdKey key(Object id) {
                return new ObjectIdGenerator.IdKey(id.getClass(), "id");
            }
        };

        DefaultDeserializationContext context = new DefaultDeserializationContext(new DeserializerFactory(null), null) {
            @Override
            public ReadableObjectId findObjectId(Object id, ObjectIdGenerator<?> gen) {
                return new ReadableObjectId(gen.key(id));
            }
        };

        context._objectIds = new LinkedHashMap<>();
        Object id1 = new Object();
        context.findObjectId(id1, generator, null);

        Object id2 = new Object();
        context.checkUnresolvedObjectId();
    }

    @Test
    public void testDeserializerInstance() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext context = mapper.getDeserializationContext();

        Annotated ann = mock(Annotated.class);
        JsonDeserializer<Object> deserializer = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };

        Object deserDef = deserializer;
        JsonDeserializer<Object> deser = context.deserializerInstance(ann, deserDef);
        assertEquals(deserializer, deser);

        ann = mock(Annotated.class);
        deserializer = null;
        deserDef = deserializer.getClass();
        deser = context.deserializerInstance(ann, deserDef);
        assertNull(deser);

        deserializer = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        deserDef = new Class<JsonDeserializer<Object>>() {};
        deser = context.deserializerInstance(ann, deserDef);
        assertEquals(deserializer, deser);

        context = new DefaultDeserializationContext(context, null, null, null) {
            @Override
            public JsonDeserializer<Object> deserializerInstance(Annotated ann, Object deserDef)
                    throws JsonMappingException {
                return null;
            }
        };
        ann = mock(Annotated.class);
        deserializer = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        deserDef = deserializer;
        deser = context.deserializerInstance(ann, deserDef);
        assertNull(deser);

        deserializer = null;
        deserDef = deserializer.getClass();
        deser = context.deserializerInstance(ann, deserDef);
        assertNull(deser);

        deserializer = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        deserDef = new Class<JsonDeserializer<Object>>() {};
        deser = context.deserializerInstance(ann, deserDef);
        assertNull(deser);

        deserializer = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        deserDef = new Class<JsonDeserializer<Object>>() {};
        deserializer.setConfig(mapper.getConfig());
        deser = context.deserializerInstance(ann, deserDef);
        assertNotNull(deser);
        assertEquals(deserializer, deser);
    }

    @Test
    public void testKeyDeserializerInstance() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext context = mapper.getDeserializationContext();

        Annotated ann = mock(Annotated.class);
        KeyDeserializer deserializer = new KeyDeserializer() {
            @Override
            public Object deserializeKey(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };

        Object deserDef = deserializer;
        KeyDeserializer deser = context.keyDeserializerInstance(ann, deserDef);
        assertEquals(deserializer, deser);

        ann = mock(Annotated.class);
        deserializer = null;
        deserDef = deserializer.getClass();
        deser = context.keyDeserializerInstance(ann, deserDef);
        assertNull(deser);

        deserializer = new KeyDeserializer() {
            @Override
            public Object deserializeKey(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        deserDef = new Class<KeyDeserializer>() {};
        deser = context.keyDeserializerInstance(ann, deserDef);
        assertEquals(deserializer, deser);

        context = new DefaultDeserializationContext(context, null, null, null) {
            @Override
            public KeyDeserializer keyDeserializerInstance(Annotated ann, Object deserDef)
                    throws JsonMappingException {
                return null;
            }
        };
        ann = mock(Annotated.class);
        deserializer = new KeyDeserializer() {
            @Override
            public Object deserializeKey(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        deserDef = deserializer;
        deser = context.keyDeserializerInstance(ann, deserDef);
        assertNull(deser);

        deserializer = null;
        deserDef = deserializer.getClass();
        deser = context.keyDeserializerInstance(ann, deserDef);
        assertNull(deser);

        deserializer = new KeyDeserializer() {
            @Override
            public Object deserializeKey(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        deserDef = new Class<KeyDeserializer>() {};
        deserializer.setConfig(mapper.getConfig());
        deser = context.keyDeserializerInstance(ann, deserDef);
        assertNotNull(deser);
        assertEquals(deserializer, deser);
    }

    @Test
    public void testWith() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext originalContext = mapper.getDeserializationContext();

        DefaultDeserializationContext newContext = originalContext.with(null);
        assertSame(originalContext, newContext);

        DeserializerFactory factory = new DeserializerFactory(null);
        newContext = originalContext.with(factory);
        assertNotSame(originalContext, newContext);
        assertEquals(factory, newContext.getFactory());

        HandlerInstantiator handlerInstantiator = mapper.getConfig().getHandlerInstantiator();
        newContext = originalContext.with(handlerInstantiator);
        assertNotSame(originalContext, newContext);
        assertEquals(handlerInstantiator, newContext.getHandlerInstantiator());
    }

    @Test
    public void testCreateInstance() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext originalContext = mapper.getDeserializationContext();

        DeserializerFactory factory = new DeserializerFactory(null);
        JsonParser jp = createParser();
        InjectableValues values = createInjectableValues();
        DefaultDeserializationContext newContext = originalContext.createInstance(mapper.getConfig(), jp, values);
        assertNotSame(originalContext, newContext);
        assertEquals(factory, newContext.getFactory());

        factory = new DeserializerFactory(null) {
            @Override
            public JsonDeserializer<?> createValueDeserializer(DeserializationConfig config, Annotated ann, Class<?> valueType)
                    throws JsonMappingException {
                return null;
            }
        };
        newContext = originalContext.createInstance(mapper.getConfig(), jp, values, factory);
        assertNotSame(originalContext, newContext);
        assertEquals(factory, newContext.getFactory());
    }
}