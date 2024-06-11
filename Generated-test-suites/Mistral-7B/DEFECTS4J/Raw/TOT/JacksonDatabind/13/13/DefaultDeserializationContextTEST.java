package com.fasterxml.jackson.databind.deserialization.test;

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
import com.fasterxml.jackson.test.BaseMapTest;

import java.util.*;

public class DefaultDeserializationContextTest extends BaseMapTest {

    private static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
    }

    @org.junit.Test
    public void testFindObjectId() throws Exception {
        ObjectIdGenerator idGen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey() {
                    @Override
                    public String id() {
                        return "test";
                    }
                };
            }
        };

        DefaultDeserializationContext ctx = mapper.getDeserializationContext().with(new DeserializerFactory() {
            @Override
            public JsonDeserializer<?> createValueDeserializer(DeserializationConfig config, Annotated param, Class<?> paramType) throws JsonMappingException {
                return null;
            }
        });

        Object id = new Object();

        ReadableObjectId idFromCtx = ctx.findObjectId(id, idGen, new SimpleObjectIdResolver());
        assertNotNull(idFromCtx);
        assertEquals("test", idFromCtx.getKey().id());
    }

    @org.junit.Test
    public void testFindObjectIdWithMultipleResolvers() throws Exception {
        ObjectIdGenerator idGen1 = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey() {
                    @Override
                    public String id() {
                        return "test1";
                    }
                };
            }
        };

        ObjectIdGenerator idGen2 = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey() {
                    @Override
                    public String id() {
                        return "test2";
                    }
                };
            }
        };

        DefaultDeserializationContext ctx = mapper.getDeserializationContext().with(new DeserializerFactory() {
            @Override
            public JsonDeserializer<?> createValueDeserializer(DeserializationConfig config, Annotated param, Class<?> paramType) throws JsonMappingException {
                return null;
            }
        }).with(new ObjectIdResolver[] {
                new ObjectIdResolver() {
                    @Override
                    public ObjectIdResolver newForDeserialization(DefaultDeserializationContext ctxt) {
                        return idGen1;
                    }
                },
                new ObjectIdResolver() {
                    @Override
                    public ObjectIdResolver newForDeserialization(DefaultDeserializationContext ctxt) {
                        return idGen2;
                    }
                }
        });

        Object id = new Object();

        ReadableObjectId idFromCtx1 = ctx.findObjectId(id, idGen1, new SimpleObjectIdResolver());
        assertNotNull(idFromCtx1);
        assertEquals("test1", idFromCtx1.getKey().id());

        ReadableObjectId idFromCtx2 = ctx.findObjectId(id, idGen2, new SimpleObjectIdResolver());
        assertNotNull(idFromCtx2);
        assertEquals("test2", idFromCtx2.getKey().id());
    }

    @org.junit.Test
    public void testFindObjectIdWithCachedObject() throws Exception {
        ObjectIdGenerator idGen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey() {
                    @Override
                    public String id() {
                        return "test";
                    }
                };
            }
        };

        DefaultDeserializationContext ctx = mapper.getDeserializationContext().with(new DeserializerFactory() {
            @Override
            public JsonDeserializer<?> createValueDeserializer(DeserializationConfig config, Annotated param, Class<?> paramType) throws JsonMappingException {
                return null;
            }
        });

        Object id = new Object();

        ctx.findObjectId(id, idGen, new SimpleObjectIdResolver());
        ctx.findObjectId(id, idGen, new SimpleObjectIdResolver());

        ReadableObjectId idFromCtx = ctx.findObjectId(id, idGen, new SimpleObjectIdResolver());
        assertNotNull(idFromCtx);
        assertEquals("test", idFromCtx.getKey().id());
    }

    @org.junit.Test(expected = UnresolvedForwardReference.class)
    public void testCheckUnresolvedObjectId() throws Exception {
        ObjectIdGenerator idGen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey() {
                    @Override
                    public String id() {
                        return "test";
                    }
                };
            }
        };

        DefaultDeserializationContext ctx = mapper.getDeserializationContext().with(new DeserializerFactory() {
            @Override
            public JsonDeserializer<?> createValueDeserializer(DeserializationConfig config, Annotated param, Class<?> paramType) throws JsonMappingException {
                return null;
            }
        });

        Object id = new Object();

        ctx.findObjectId(id, idGen, new SimpleObjectIdResolver());
        ctx.findObjectId(new Object(), idGen, new SimpleObjectIdResolver());

        ctx.checkUnresolvedObjectId();
    }

    @org.junit.Test
    public void testDeserializerInstance() throws Exception {
        JsonDeserializer<String> stringDeserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return "test";
            }
        };

        ObjectMapper mapper = new ObjectMapper();
        DeserializerFactory factory = new DeserializerFactory() {
            @Override
            public JsonDeserializer<?> createValueDeserializer(DeserializationConfig config, Annotated param, Class<?> paramType) throws JsonMappingException {
                if (paramType.equals(String.class)) {
                    return stringDeserializer;
                }
                return null;
            }
        };

        DefaultDeserializationContext ctx = mapper.getDeserializationContext().with(factory);

        JsonDeserializer<String> deser = ctx.deserializerInstance(new AnnotatedClass(), stringDeserializer.getClass());
        assertEquals(stringDeserializer, deser);
    }

    @org.junit.Test
    public void testDeserializerInstanceWithClass() throws Exception {
        JsonDeserializer<String> stringDeserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return "test";
            }
        };

        ObjectMapper mapper = new ObjectMapper();

        DefaultDeserializationContext ctx = mapper.getDeserializationContext().with(new DeserializerFactory() {
            @Override
            public JsonDeserializer<?> createValueDeserializer(DeserializationConfig config, Annotated param, Class<?> paramType) throws JsonMappingException {
                if (paramType.equals(String.class)) {
                    return stringDeserializer;
                }
                return null;
            }
        });

        JsonDeserializer<String> deser = ctx.deserializerInstance(new AnnotatedClass(), String.class);
        assertEquals(stringDeserializer, deser);
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void testDeserializerInstanceWithUnassignedType() throws Exception {
        JsonDeserializer<String> stringDeserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return "test";
            }
        };

        ObjectMapper mapper = new ObjectMapper();

        DefaultDeserializationContext ctx = mapper.getDeserializationContext().with(new DeserializerFactory() {
            @Override
            public JsonDeserializer<?> createValueDeserializer(DeserializationConfig config, Annotated param, Class<?> paramType) throws JsonMappingException {
                if (paramType.equals(String.class)) {
                    return stringDeserializer;
                }
                return null;
            }
        });

        JsonDeserializer<Number> deser = ctx.deserializerInstance(new AnnotatedClass(), String.class);
    }

    @org.junit.Test
    public void testKeyDeserializerInstance() throws Exception {
        KeyDeserializer keyDeserializer = new KeyDeserializer() {
            @Override
            public Object deserializeKey(JsonParser p, DeserializationContext ctxt) throws IOException {
                return "test";
            }
        };

        ObjectMapper mapper = new ObjectMapper();

        DefaultDeserializationContext ctx = mapper.getDeserializationContext().with(new DeserializerFactory() {
            @Override
            public JsonDeserializer<?> createValueDeserializer(DeserializationConfig config, Annotated param, Class<?> paramType) throws JsonMappingException {
                return null;
            }

            @Override
            public KeyDeserializer keyDeserializerInstance(Annotated ann, Object deserDef) throws JsonMappingException {
                if (deserDef instanceof Class) {
                    return keyDeserializer;
                }
                return null;
            }
        });

        KeyDeserializer deser = ctx.keyDeserializerInstance(new AnnotatedClass(), keyDeserializer.getClass());
        assertEquals(keyDeserializer, deser);
    }
}