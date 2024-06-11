package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
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

import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DefaultDeserializationContextTest extends TestBase {

    private DefaultDeserializationContext context;
    private DeserializerFactory factory;
    private DeserializationConfig config;
    private JsonParser parser;
    private InjectableValues values;

    @Before
    public void setup() throws Exception {
        factory = new DefaultDeserializerFactory();
        config = new DeserializationConfig(new AnnotationIntrospector(), new StandardSerializerFactory(null), factory);
        parser = mapper().getFactory().createParser(getResourceAsString("/TestObject.json"));
        values = new InjectableValues();
        context = new DefaultDeserializationContext.Impl(factory);
    }

    @Test
    public void testFindObjectId() throws Exception {
        ObjectIdGenerator idGen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey() {
                    @Override
                    public String id() {
                        return "testKey";
                    }
                };
            }

            @Override
            public ObjectIdResolver resolverInstance(DefaultDeserializationContext ctxt) {
                return new ObjectIdResolver() {
                    @Override
                    public ObjectId idFromString(String idStr, DeserializationContext ctxt) {
                        return new ReadableObjectId(new IdKey() {
                            @Override
                            public String id() {
                                return "testId";
                            }
                        });
                    }

                    @Override
                    public String idToString(ObjectId id) {
                        return null;
                    }
                };
            }
        };

        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        context._objectIdResolvers = new ArrayList<>();
        context._objectIdResolvers.add(resolver);

        ReadableObjectId id1 = context.findObjectId(null, idGen, null);
        assertNull(id1);

        ReadableObjectId id2 = context.findObjectId(null, idGen, idGen);
        assertNull(id2);

        ReadableObjectId id3 = context.findObjectId("testId", idGen, idGen);
        assertNotNull(id3);

        assertEquals(IdKey.class, id3.getId().getClass());
        assertEquals("testKey", id3.getId().id());
        assertEquals("testId", id3.getIdString());
        assertNull(id3.getResolver());

        resolver = context.findObjectIdResolverFor(IdKey.class);
        assertEquals(resolver, resolverType(SimpleObjectIdResolver.class));

        context.addObjectIdResolver(null);
        context.addObjectIdResolver(idGen);
        context.addObjectIdResolver(idGen);

        ReadableObjectId id4 = context.findObjectId(null, idGen, null);
        assertEquals(IdKey.class, id4.getId().getClass());
        assertEquals("testKey", id4.getId().id());
        assertEquals("testId", id4.getIdString());
        assertNotNull(id4.getResolver());
        assertEquals(idGen, id4.getResolver());

        context._objectIdResolvers.remove(2);

        ReadableObjectId id5 = context.findObjectId(null, idGen, null);
        assertNull(id5);
    }

    @Test
    public void testCheckUnresolvedObjectId() throws Exception {
        ObjectIdGenerator idGen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey() {
                    @Override
                    public String id() {
                        return "testKey";
                    }
                };
            }

            @Override
            public ObjectIdResolver resolverInstance(DefaultDeserializationContext ctxt) {
                return new ObjectIdResolver() {
                    @Override
                    public ObjectId idFromString(String idStr, DeserializationContext ctxt) {
                        return new ReadableObjectId(new IdKey() {
                            @Override
                            public String id() {
                                return "testId1";
                            }
                        });
                    }

                    @Override
                    public String idToString(ObjectId id) {
                        return null;
                    }
                };
            }
        };

        ObjectIdResolver resolver = new ObjectIdResolver() {
            @Override
            public ObjectId idFromString(String idStr, DeserializationContext ctxt) {
                return new ReadableObjectId(new IdKey() {
                    @Override
                    public String id() {
                        return "testId2";
                    }
                });
            }

            @Override
            public String idToString(ObjectId id) {
                return null;
            }
        };

        context._objectIdResolvers = new ArrayList<>();
        context._objectIdResolvers.add(resolver);
        context._objectIds = new HashMap<>();

        context.checkUnresolvedObjectId();

        context.findObjectId("testId1", idGen, idGen);
        context.findObjectId("testId2", idGen, idGen);

        context._objectIdResolvers.add(idGen);

        context.checkUnresolvedObjectId();

        context._objectIdResolvers.remove(0);

        try {
            context.checkUnresolvedObjectId();
            fail();
        } catch (UnresolvedForwardReference e) {
            assertEquals("Unresolved forward references for: testKey", e.getMessage());
            assertEquals(2, e.size());
            assertEquals("testId1", e.getUnresolvedId(0).getId().id());
            assertEquals(String.class, e.getUnresolvedId(0).getId().getClass());
            assertEquals(String.class, e.getUnresolvedId(0).getBeanType());
            assertEquals(0, e.getUnresolvedId(0).getLocation().lineNumber());
            assertEquals(0, e.getUnresolvedId(0).getLocation().columnNumber());

            assertEquals("testId2", e.getUnresolvedId(1).getId().id());
            assertEquals(Integer.class, e.getUnresolvedId(1).getId().getClass());
            assertEquals(Integer.class, e.getUnresolvedId(1).getBeanType());
            assertEquals(1, e.getUnresolvedId(1).getLocation().lineNumber());
            assertEquals(1, e.getUnresolvedId(1).getLocation().columnNumber());
        }
    }

    @Test
    public void testDeserializerInstance() throws Exception {
        JsonDeserializer deserializer = new JsonDeserializer() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return "test";
            }
        };

        ObjectIdGenerator idGen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey() {
                    @Override
                    public String id() {
                        return "testKey";
                    }
                };
            }

            @Override
            public ObjectIdResolver resolverInstance(DefaultDeserializationContext ctxt) {
                return new ObjectIdResolver() {
                    @Override
                    public ObjectId idFromString(String idStr, DeserializationContext ctxt) {
                        return new ReadableObjectId(new IdKey() {
                            @Override
                            public String id() {
                                return "testId";
                            }
                        });
                    }

                    @Override
                    public String idToString(ObjectId id) {
                        return null;
                    }
                };
            }
        };

        context.findObjectId("testId", idGen, idGen);

        JsonDeserializer deser = context.deserializerInstance(new AnnotatedClassTest(), deserializer);
        assertEquals("test", deser.deserialize(parser, context));
    }

    private static Class<? extends ObjectIdResolver> resolverType(Class<? extends ObjectIdResolver> clazz) {
        return clazz;
    }

    static class AnnotatedClassTest {
        @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
        static class TestObject {
            private int id;

            @JsonProperty
            public int getId() {
                return id;
            }

            @JsonProperty
            public void setId(int id) {
                this.id = id;
            }
        }
    }
}