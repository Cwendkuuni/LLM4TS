package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.core.util.TestUtils.constructParser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader.Generator;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader.Resolver;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.ValuePropertyAnnotation;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ObjectIdValuePropertyTest {

    private static ObjectIdValueProperty createProperty(ObjectIdReader objectIdReader) {
        return new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
    }

    @Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return new PropertyName("id");
            }

            @Override
            public Class<?> idType() {
                return Integer.class;
            }

            @Override
            public ObjectIdReader.Generator generator() {
                return new Generator() {
                    @Override
                    public ObjectId newIdInstance() {
                        return new ObjectId();
                    }
                };
            }

            @Override
            public ObjectIdReader.Resolver resolver() {
                return new Resolver() {
                    @Override
                    public Object getIdValue(ObjectIdReader reader, Object idInstance) {
                        return idInstance;
                    }

                    @Override
                    public ObjectId readObjectId(JsonParser jp) throws IOException {
                        return jp.readValueAs(ObjectId.class);
                    }
                };
            }

            @Override
            public JsonDeserializer<?> idDeserializer() {
                return null;
            }
        };

        ObjectIdValueProperty property = createProperty(objectIdReader);
        assertEquals("id", property.getName());
        assertEquals(Integer.class, property.getIdType());
        assertNull(property.getDeserializer());
        assertNull(property.getMetadata());
        assertEquals(objectIdReader, property._objectIdReader);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return new PropertyName("id");
            }

            @Override
            public Class<?> idType() {
                return Integer.class;
            }

            @Override
            public ObjectIdReader.Generator generator() {
                return new Generator() {
                    @Override
                    public ObjectId newIdInstance() {
                        return new ObjectId();
                    }
                };
            }

            @Override
            public ObjectIdReader.Resolver resolver() {
                return new Resolver() {
                    @Override
                    public Object getIdValue(ObjectIdReader reader, Object idInstance) {
                        return idInstance;
                    }

                    @Override
                    public ObjectId readObjectId(JsonParser jp) throws IOException {
                        return jp.readValueAs(ObjectId.class);
                    }
                };
            }

            @Override
            public JsonDeserializer<?> idDeserializer() {
                return new StdDeserializer<ObjectId>(ObjectId.class) {};
            }
        };

        ObjectIdValueProperty property = createProperty(objectIdReader);

        JsonParser jp = constructParser("{\"id\": 1}");
        Object id = property.deserializeAndSet(jp, null, new Object());
        assertEquals(1, id);

        jp = constructParser("{\"id\": null}");
        id = property.deserializeAndSet(jp, null, new Object());
        assertNull(id);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return new PropertyName("id");
            }

            @Override
            public Class<?> idType() {
                return Integer.class;
            }

            @Override
            public ObjectIdReader.Generator generator() {
                return new Generator() {
                    @Override
                    public ObjectId newIdInstance() {
                        return new ObjectId();
                    }
                };
            }

            @Override
            public ObjectIdReader.Resolver resolver() {
                return new Resolver() {
                    @Override
                    public Object getIdValue(ObjectIdReader reader, Object idInstance) {
                        return idInstance;
                    }

                    @Override
                    public ObjectId readObjectId(JsonParser jp) throws IOException {
                        return jp.readValueAs(ObjectId.class);
                    }
                };
            }

            @Override
            public JsonDeserializer<?> idDeserializer() {
                return new StdDeserializer<ObjectId>(ObjectId.class) {};
            }
        };

        ObjectIdValueProperty property = createProperty(objectIdReader);

        JsonParser jp = constructParser("{\"id\": 1}");
        Object id = property.deserializeSetAndReturn(jp, null, new Object());
        assertEquals(1, id);

        jp = constructParser("{\"id\": null}");
        id = property.deserializeSetAndReturn(jp, null, new Object());
        assertNull(id);
    }

    @Test
    public void testSet() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return new PropertyName("id");
            }

            @Override
            public Class<?> idType() {
                return Integer.class;
            }

            @Override
            public ObjectIdReader.Generator generator() {
                return new Generator() {
                    @Override
                    public ObjectId newIdInstance() {
                        return new ObjectId();
                    }
                };
            }

            @Override
            public ObjectIdReader.Resolver resolver() {
                return new Resolver() {
                    @Override
                    public Object getIdValue(ObjectIdReader reader, Object idInstance) {
                        return idInstance;
                    }

                    @Override
                    public ObjectId readObjectId(JsonParser jp) throws IOException {
                        return jp.readValueAs(ObjectId.class);
                    }
                };
            }

            @Override
            public JsonDeserializer<?> idDeserializer() {
                return new StdDeserializer<ObjectId>(ObjectId.class) {};
            }
        };

        ObjectIdValueProperty property = createProperty(objectIdReader);

        Object id = new ObjectId();
        property.set(new Object(), id);
        ObjectId readId = property.getObjectIdReader().resolver().readObjectId(constructParser("{\"id\": " + id + "}"));
        assertEquals(id, readId);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return new PropertyName("id");
            }

            @Override
            public Class<?> idType() {
                return Integer.class;
            }

            @Override
            public ObjectIdReader.Generator generator() {
                return new Generator() {
                    @Override
                    public ObjectId newIdInstance() {
                        return new ObjectId();
                    }
                };
            }

            @Override
            public ObjectIdReader.Resolver resolver() {
                return new Resolver() {
                    @Override
                    public Object getIdValue(ObjectIdReader reader, Object idInstance) {
                        return idInstance;
                    }

                    @Override
                    public ObjectId readObjectId(JsonParser jp) throws IOException {
                        return jp.readValueAs(ObjectId.class);
                    }
                };
            }

            @Override
            public JsonDeserializer<?> idDeserializer() {
                return new StdDeserializer<ObjectId>(ObjectId.class) {};
            }
        };

        ObjectIdValueProperty property = createProperty(objectIdReader);

        Object id = new ObjectId();
        Object instance = new Object();
        Object returnValue = property.setAndReturn(instance, id);
        assertEquals(instance, returnValue);

        ObjectId readId = property.getObjectIdReader().resolver().readObjectId(constructParser("{\"id\": " + id + "}"));
        assertEquals(id, readId);
    }
}