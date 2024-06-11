package com.fasterxml.jackson.databind.deser.test;

import static com.fasterxml.jackson.databind.DeserializationFeature.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext.Impl;

public class DefaultDeserializationContextTest {

    @Test
    public void testDefaultDeserializationContext() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new JacksonAnnotationIntrospector(), null);
        assertNotNull(context);
    }

    @Test
    public void testFindObjectId() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new JacksonAnnotationIntrospector(), null);

        ObjectIdGenerator idGenerator = new ObjectIdGenerator() {
            @Override
            public IdKey key(Object id) {
                return new IdKey() {
                    @Override
                    public String id() {
                        return "testKey";
                    }
                };
            }
        };

        Object id = new Object();
        ReadableObjectId readableObjectId = context.findObjectId(id, idGenerator, null);
        assertNotNull(readableObjectId);
        assertNull(readableObjectId.getResolver());

        context = new DefaultDeserializationContext.Impl(new JacksonAnnotationIntrospector(), null, Collections.emptyList());
        readableObjectId = context.findObjectId(id, idGenerator, null);
        assertNotNull(readableObjectId);
        assertNull(readableObjectId.getResolver());

        ObjectIdResolver resolver = new ObjectIdResolver() {
            @Override
            public ObjectId idResolution(DeserializationContext ctxt, Object id) {
                return new ReadableObjectId(idGenerator.key(id));
            }
        };

        context = new DefaultDeserializationContext.Impl(new JacksonAnnotationIntrospector(), null, Collections.singletonList(resolver));
        readableObjectId = context.findObjectId(id, idGenerator, null);
        assertNotNull(readableObjectId);
        assertNotNull(readableObjectId.getResolver());
    }

    @Test
    public void testCheckUnresolvedObjectId() throws UnresolvedForwardReference {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new JacksonAnnotationIntrospector(), null, Collections.emptyList());
        ObjectIdGenerator idGenerator = new ObjectIdGenerator() {
            @Override
            public IdKey key(Object id) {
                return new IdKey() {
                    @Override
                    public String id() {
                        return "testKey";
                    }
                };
            }
        };
        context.findObjectId(new Object(), idGenerator, null);
        context.checkUnresolvedObjectId();

        context = new DefaultDeserializationContext.Impl(new JacksonAnnotationIntrospector(), null, Collections.singletonList(new SimpleObjectIdResolver()));
        Object id = new Object();
        context.findObjectId(id, idGenerator, null);
        context.checkUnresolvedObjectId();

        context = new DefaultDeserializationContext.Impl(new JacksonAnnotationIntrospector(), null, null);
        context.checkUnresolvedObjectId();

        context = new DefaultDeserializationContext.Impl(new JacksonAnnotationIntrospector(), null, null);
        IdKey idKey = new IdKey() {
            @Override
            public String id() {
                return "testKey";
            }
        };
        ReadableObjectId readableObjectId1 = new ReadableObjectId(idKey);
        context._objectIds = new LinkedHashMap<>();
        context._objectIds.put(idKey, readableObjectId1);
        context.checkUnresolvedObjectId();

        context = new DefaultDeserializationContext.Impl(new JacksonAnnotationIntrospector(), null, null);
        readableObjectId1.setReferring(new Referring() {
            @Override
            public Annotated getBeanType() {
                return new Annotated() {
                    @Override
                    public JsonTypeDescriptor getTypeDescriptor() {
                        return null;
                    }
                };
            }

            @Override
            public JsonParser getLocation() {
                return null;
            }
        });
        context._objectIds = new LinkedHashMap<>();
        context._objectIds.put(idKey, readableObjectId1);
        try {
            context.checkUnresolvedObjectId();
            fail("Expected UnresolvedForwardReference");
        } catch (UnresolvedForwardReference e) {
            // Expected
        }
    }

    @Test
    public void testDeserializerInstance() throws JsonMappingException {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new JacksonAnnotationIntrospector(), null, Collections.emptyList());

        JsonDeserializer deserializer = new JsonDeserializer() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };

        Annotated ann = new Annotated() {
            @Override
            public JsonTypeDescriptor getTypeDescriptor() {
                return null;
            }
        };

        Object deserDef = deserializer;
        JsonDeserializer<?> deser = context.deserializerInstance(ann, deserDef);
        assertNotNull(deser);
        assertEquals(deserializer.getClass(), deser.getClass());

        deserializer = null;
        deserDef = deserializer.getClass();
        deser = context.deserializerInstance(ann, deserDef);
        assertNull(deser);

        deserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        deserDef = deserializer;
        deser = context.deserializerInstance(ann, deserDef);
        assertNotNull(deser);
        assertEquals(deserializer.getClass(), deser.getClass());

        deserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        deserDef = deserializer.getClass();
        deser = context.deserializerInstance(ann, deserDef);
        assertNotNull(deser);
        assertEquals(deserializer.getClass(), deser.getClass());

        deserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        deserDef = deserializer.getClass().getName();
        deser = context.deserializerInstance(ann, deserDef);
        assertNotNull(deser);
        assertEquals(deserializer.getClass(), deser.getClass());

        deserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        deserDef = deserializer;
        HandlerInstantiator handlerInstantiator = new HandlerInstantiator() {
            @Override
            public JsonDeserializer<?> deserializerInstance(DeserializationConfig config, Annotated ann, Class<?> deserClass)
                    throws JsonMappingException {
                return null;
            }
        };
        deser = context.deserializerInstance(ann, deserDef, handlerInstantiator);
        assertNull(deser);

        deserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        deserDef = deserializer;
        deser = context.deserializerInstance(ann, deserDef, null);
        assertNotNull(deser);
        assertEquals(deserializer.getClass(), deser.getClass());

        deserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        deserDef = deserializer.getClass();
        deser = context.deserializerInstance(ann, deserDef, null);
        assertNotNull(deser);
        assertEquals(deserializer.getClass(), deser.getClass());

        deserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        deserDef = deserializer.getClass();
        deser = context.deserializerInstance(ann, deserDef, null);
        assertNotNull(deser);
        assertEquals(deserializer.getClass(), deser.getClass());
    }

    @Test
    public void testKeyDeserializerInstance() throws JsonMappingException {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new JacksonAnnotationIntrospector(), null, Collections.emptyList());

        KeyDeserializer keyDeserializer = new KeyDeserializer() {
            @Override
            public Object deserializeKey(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };

        Annotated ann = new Annotated() {
            @Override
            public JsonTypeDescriptor getTypeDescriptor() {
                return null;
            }
        };

        Object deserDef = keyDeserializer;
        KeyDeserializer deser = context.keyDeserializerInstance(ann, deserDef);
        assertNotNull(deser);
        assertEquals(keyDeserializer.getClass(), deser.getClass());

        keyDeserializer = null;
        deserDef = keyDeserializer.getClass();
        deser = context.keyDeserializerInstance(ann, deserDef);
        assertNull(deser);

        keyDeserializer = new KeyDeserializer() {
            @Override
            public Object deserializeKey(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        deserDef = keyDeserializer.getClass();
        deser = context.keyDeserializerInstance(ann, deserDef);
        assertNotNull(deser);
        assertEquals(keyDeserializer.getClass(), deser.getClass());

        keyDeserializer = new KeyDeserializer() {
            @Override
            public Object deserializeKey(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        deserDef = keyDeserializer.getClass().getName();
        deser = context.keyDeserializerInstance(ann, deserDef);
        assertNotNull(deser);
        assertEquals(keyDeserializer.getClass(), deser.getClass());

        keyDeserializer = new KeyDeserializer() {
            @Override
            public Object deserializeKey(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        deserDef = keyDeserializer;
        HandlerInstantiator handlerInstantiator = new HandlerInstantiator() {
            @Override
            public KeyDeserializer keyDeserializerInstance(DeserializationConfig config, Annotated ann, Class<?> deserClass)
                    throws JsonMappingException {
                return null;
            }
        };
        deser = context.keyDeserializerInstance(ann, deserDef, handlerInstantiator);
        assertNull(deser);

        keyDeserializer = new KeyDeserializer() {
            @Override
            public Object deserializeKey(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        deserDef = keyDeserializer;
        deser = context.keyDeserializerInstance(ann, deserDef, null);
        assertNotNull(deser);
        assertEquals(keyDeserializer.getClass(), deser.getClass());

        keyDeserializer = new KeyDeserializer() {
            @Override
            public Object deserializeKey(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        deserDef = keyDeserializer.getClass();
        deser = context.keyDeserializerInstance(ann, deserDef, null);
        assertNotNull(deser);
        assertEquals(keyDeserializer.getClass(), deser.getClass());

        keyDeserializer = new KeyDeserializer() {
            @Override
            public Object deserializeKey(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        deserDef = keyDeserializer.getClass();
        deser = context.keyDeserializerInstance(ann, deserDef, null);
        assertNotNull(deser);
        assertEquals(keyDeserializer.getClass(), deser.getClass());
    }

    @Test
    public void testWith() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new JacksonAnnotationIntrospector(), null);
        DefaultDeserializationContext newContext = context.with(new JacksonAnnotationIntrospector());
        assertNotNull(newContext);
        assertNotSame(context, newContext);
    }

    @Test
    public void testCreateInstance() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new JacksonAnnotationIntrospector(), null);
        DefaultDeserializationContext newContext = context.createInstance(new DeserializationConfig(), null, null);
        assertNotNull(newContext);
        assertNotSame(context, newContext);
    }
}