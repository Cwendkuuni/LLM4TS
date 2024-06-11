package com.fasterxml.jackson.databind.deser.test;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext.Impl;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

public class DefaultDeserializationContextTest extends TestBase {

    @Test
    public void testFindObjectId() {
        DefaultDeserializationContext context = createContext();

        ObjectIdGenerator gen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey() {
                    @Override
                    public Object id() {
                        return id;
                    }
                };
            }
        };

        Object id = new Object();
        ReadableObjectId resolvedId = context.findObjectId(id, gen, SimpleObjectIdResolver.instance);
        assertSame(id, resolvedId.getId());

        context.checkUnresolvedObjectId();

        id = null;
        resolvedId = context.findObjectId(id, gen, SimpleObjectIdResolver.instance);
        assertNull(resolvedId);

        context.checkUnresolvedObjectId();

        context.clearUnresolvedObjectIds();

        ObjectIdResolver resolver = new ObjectIdResolver() {
            @Override
            public ObjectId resolveObjectId(ObjectId id) {
                return id;
            }

            @Override
            public ObjectId getId(Object id) {
                return id;
            }

            @Override
            public ObjectIdResolver newForDeserialization(DefaultDeserializationContext ctxt) {
                return this;
            }

            @Override
            public boolean canUseFor(ObjectIdResolver resolverType) {
                return resolverType == SimpleObjectIdResolver.class;
            }
        };

        context.addObjectIdResolver(resolver);

        id = new Object();
        resolvedId = context.findObjectId(id, gen, SimpleObjectIdResolver.instance);
        assertSame(id, resolvedId.getId());

        context.checkUnresolvedObjectId();

        Map<ObjectIdGenerator.IdKey, ReadableObjectId> objectIds = context._objectIds;
        assertEquals(1, objectIds.size());
        ReadableObjectId entry = objectIds.values().iterator().next();
        assertSame(id, entry.getId().getId());
        assertSame(resolver, entry.getResolver());

        context.clearUnresolvedObjectIds();

        id = null;
        resolvedId = context.findObjectId(id, gen, SimpleObjectIdResolver.instance);
        assertNull(resolvedId);

        context.checkUnresolvedObjectId();

        context.clearUnresolvedObjectIds();

        context.addObjectIdResolver(resolver);

        ObjectIdGenerator gen2 = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey() {
                    @Override
                    public Object id() {
                        return id;
                    }
                };
            }
        };

        id = new Object();
        resolvedId = context.findObjectId(id, gen2, SimpleObjectIdResolver.instance);
        assertNull(resolvedId);

        context.checkUnresolvedObjectId();

        context.clearUnresolvedObjectIds();

        context.addObjectIdResolver(resolver);

        id = new Object();
        resolvedId = context.findObjectId(id, gen2, resolver);
        assertSame(id, resolvedId.getId().getId());

        context.checkUnresolvedObjectId();

        ObjectIdGenerator gen3 = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey() {
                    @Override
                    public Object id() {
                        return id;
                    }
                };
            }
        };

        id = new Object();
        resolvedId = context.findObjectId(id, gen3, resolver);
        assertSame(id, resolvedId.getId().getId());

        context.checkUnresolvedObjectId();

        context.clearUnresolvedObjectIds();

        context.addObjectIdResolver(resolver);

        List<ObjectIdResolver> objectIdResolvers = context._objectIdResolvers;
        assertEquals(1, objectIdResolvers.size());
        assertSame(resolver, objectIdResolvers.get(0));

        context.clearUnresolvedObjectIds();

        context.addObjectIdResolver(new ObjectIdResolver() {
            @Override
            public ObjectId resolveObjectId(ObjectId id) {
                throw new RuntimeException("Should not be called");
            }

            @Override
            public ObjectId getId(Object id) {
                return id;
            }

            @Override
            public ObjectIdResolver newForDeserialization(DefaultDeserializationContext ctxt) {
                return this;
            }

            @Override
            public boolean canUseFor(ObjectIdResolver resolverType) {
                return false;
            }
        });

        id = new Object();
        resolvedId = context.findObjectId(id, gen, SimpleObjectIdResolver.instance);
        assertNull(resolvedId);

        context.checkUnresolvedObjectId();
    }

    @Test
    public void testFindObjectIdWithNullContext() {
        ObjectIdGenerator gen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey() {
                    @Override
                    public Object id() {
                        return id;
                    }
                };
            }
        };

        ObjectId id = new ObjectIdGenerator.IdKey() {
            @Override
            public Object id() {
                return new Object();
            }
        };

        try {
            DefaultDeserializationContext.findObjectId(id, gen, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testFindObjectIdWithNullId() {
        ObjectIdGenerator gen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey() {
                    @Override
                    public Object id() {
                        return id;
                    }
                };
            }
        };

        try {
            DefaultDeserializationContext.findObjectId(null, gen, SimpleObjectIdResolver.instance);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testFindObjectIdWithNullGen() {
        try {
            DefaultDeserializationContext.findObjectId(new Object(), null, SimpleObjectIdResolver.instance);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testFindObjectIdWithNullResolver() {
        ObjectIdGenerator gen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey() {
                    @Override
                    public Object id() {
                        return id;
                    }
                };
            }
        };

        try {
            DefaultDeserializationContext.findObjectId(new Object(), gen, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testCheckUnresolvedObjectId() {
        DefaultDeserializationContext context = createContext();
        context.addObjectIdResolver(new ObjectIdResolver() {
            @Override
            public ObjectId resolveObjectId(ObjectId id) {
                return id;
            }

            @Override
            public ObjectId getId(Object id) {
                return id;
            }

            @Override
            public ObjectIdResolver newForDeserialization(DefaultDeserializationContext ctxt) {
                return this;
            }

            @Override
            public boolean canUseFor(ObjectIdResolver resolverType) {
                return false;
            }
        });

        ObjectId id = new ObjectIdGenerator.IdKey() {
            @Override
            public Object id() {
                return new Object();
            }
        };
        context.findObjectId(id, new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey() {
                    @Override
                    public Object id() {
                        return id;
                    }
                };
            };
        }, this);

        try {
            context.checkUnresolvedObjectId();
            fail("Expected UnresolvedForwardReference");
        } catch (UnresolvedForwardReference e) {
            // expected
        }

        context.clearUnresolvedObjectIds();

        assertEquals(0, context._objectIds.size());
        assertEquals(0, context._objectIdResolvers.size());
    }

    @Test
    public void testCheckUnresolvedObjectIdWithEnabledFeature() {
        DefaultDeserializationContext context = createContext();
        context.addObjectIdResolver(new ObjectIdResolver() {
            @Override
            public ObjectId resolveObjectId(ObjectId id) {
                return id;
            }

            @Override
            public ObjectId getId(Object id) {
                return id;
            }

            @Override
            public ObjectIdResolver newForDeserialization(DefaultDeserializationContext ctxt) {
                return this;
            }

            @Override
            public boolean canUseFor(ObjectIdResolver resolverType) {
                return false;
            }
        });

        ObjectId id = new ObjectIdGenerator.IdKey() {
            @Override
            public Object id() {
                return new Object();
            }
        };
        context.findObjectId(id, new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey() {
                    @Override
                    public Object id() {
                        return id;
                    }
                };
            };
        }, this);

        try {
            context.checkUnresolvedObjectId();
            fail("Expected UnresolvedForwardReference");
        } catch (UnresolvedForwardReference e) {
            // expected
        }

        context.clearUnresolvedObjectIds();

        context.configure(FAIL_ON_UNRESOLVED_OBJECT_IDS, true);

        try {
            context.checkUnresolvedObjectId();
            fail("Expected UnresolvedForwardReference");
        } catch (UnresolvedForwardReference e) {
            // expected
        }

        context.clearUnresolvedObjectIds();

        context.configure(FAIL_ON_UNRESOLVED_OBJECT_IDS, false);

        context.clearUnresolvedObjectIds();

        assertEquals(0, context._objectIds.size());
        assertEquals(0, context._objectIdResolvers.size());
    }

    @Test
    public void testDeserializerInstance() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<Object> deserializer = mapper.deserializerForClass(Object.class);
        DefaultDeserializationContext context = createContext();

        ObjectIdGenerator gen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey() {
                    @Override
                    public Object id() {
                        return id;
                    }
                };
            }
        };

        ReadableObjectId id = context.findObjectId(new Object(), gen, SimpleObjectIdResolver.instance);

        assertEquals(deserializer, context.deserializerInstance(new AnnotatedClassTest(), id));

        context.clearUnresolvedObjectIds();

        assertEquals(0, context._objectIds.size());
        assertEquals(0, context._objectIdResolvers.size());
    }

    @Test
    public void testKeyDeserializerInstance() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        KeyDeserializer keyDeserializer = mapper.keyDeserializerForClass(Object.class);
        DefaultDeserializationContext context = createContext();

        ObjectIdGenerator gen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey() {
                    @Override
                    public Object id() {
                        return id;
                    }
                };
            }
        };

        ReadableObjectId id = context.findObjectId(new Object(), gen, SimpleObjectIdResolver.instance);

        assertEquals(keyDeserializer, context.keyDeserializerInstance(new AnnotatedClassTest(), id));

        context.clearUnresolvedObjectIds();

        assertEquals(0, context._objectIds.size());
        assertEquals(0, context._objectIdResolvers.size());
    }

    private DefaultDeserializationContext createContext() {
        return new DefaultDeserializationContext.Impl(new DefaultDeserializationContext.Impl(),
                new DeserializationConfig(), null, null) {
            @Override
            public DefaultDeserializationContext copy() {
                return new DefaultDeserializationContext.Impl(this);
            }

            @Override
            public DefaultDeserializationContext createInstance(DeserializationConfig config, JsonParser jp,
                    InjectableValues values) {
                return new DefaultDeserializationContext.Impl(this, config, jp, values);
            }

            @Override
            public DefaultDeserializationContext with(DeserializerFactory factory) {
                return new DefaultDeserializationContext.Impl(this, factory);
            }
        };
    }

}