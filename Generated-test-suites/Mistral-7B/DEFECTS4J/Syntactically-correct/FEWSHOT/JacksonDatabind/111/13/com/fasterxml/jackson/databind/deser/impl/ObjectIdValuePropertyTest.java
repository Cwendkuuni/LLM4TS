package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public class ObjectIdValuePropertyTest {

    @Test
    public void testDeserializeAndSet() throws Exception {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public Class<Object> idType() {
                return Object.class;
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return new ObjectIdGenerator() {
                    Object idCounter = 0;

                    @Override
                    public Object idFromString(String idStr) {
                        return idCounter++;
                    }

                    @Override
                    public String idToString(Object idObj) {
                        return String.valueOf(idObj);
                    }
                };
            }

            @Override
            public ObjectIdResolver idResolver() {
                return new ObjectIdResolver() {
                    @Override
                    public Object findId(String idStr, ObjectIdGenerator idGenerator) {
                        return idGenerator.idFromString(idStr);
                    }
                };
            }

            @Override
            public JsonDeserializer<Object> idDeserializer() {
                return new JsonDeserializer<Object>() {
                    @Override
                    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                        Object id = p.getValueAsString();
                        return ctxt.findValueSerializer(ctxt.getConfig().findValueSerializer(Object.class), id);
                    }
                };
            }

            @Override
            public SettableBeanProperty idProperty() {
                return new SettableBeanProperty("name", Object.class, null, new ToStringSerializer(Object.class));
            }
        };

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());

        JsonParser parser = new TestJsonParser();
        parser.setValue(JsonToken.VALUE_STRING, "123");

        Object instance = new Object();

        objectIdValueProperty.deserializeAndSet(parser, null, instance);

        assertEquals(123, instance.getClass().getDeclaredField("id").get(instance));
    }

    @Test
    public void testDeserializeAndSetNull() throws Exception {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public Class<Object> idType() {
                return Object.class;
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return null;
            }

            @Override
            public ObjectIdResolver idResolver() {
                return null;
            }

            @Override
            public JsonDeserializer<Object> idDeserializer() {
                return null;
            }

            @Override
            public SettableBeanProperty idProperty() {
                return null;
            }
        };

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());

        JsonParser parser = new TestJsonParser();
        parser.setValue(JsonToken.VALUE_NULL);

        Object instance = new Object();

        Object result = objectIdValueProperty.deserializeAndSet(parser, null, instance);

        assertNull(instance.getClass().getDeclaredField("id").get(instance));
        assertEquals(null, result);
    }

    @Test
    public void testSetAndReturn() throws Exception {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public Class<Object> idType() {
                return Object.class;
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return new ObjectIdGenerator() {
                    Object idCounter = 0;

                    @Override
                    public Object idFromString(String idStr) {
                        return idCounter++;
                    }

                    @Override
                    public String idToString(Object idObj) {
                        return String.valueOf(idObj);
                    }
                };
            }

            @Override
            public ObjectIdResolver idResolver() {
                return new ObjectIdResolver() {
                    @Override
                    public Object findId(String idStr, ObjectIdGenerator idGenerator) {
                        return idGenerator.idFromString(idStr);
                    }
                };
            }

            @Override
            public JsonDeserializer<Object> idDeserializer() {
                return null;
            }

            @Override
            public SettableBeanProperty idProperty() {
                return new SettableBeanProperty("name", Object.class, null, new ToStringSerializer(Object.class));
            }
        };

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());

        Object instance = new Object();
        Object id = 123;

        Object result = objectIdValueProperty.setAndReturn(instance, id);

        assertEquals(id, instance.getClass().getDeclaredField("id").get(instance));
        assertEquals(id, result);
    }

    @Test
    public void testSetAndReturnNull() throws Exception {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public Class<Object> idType() {
                return Object.class;
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return null;
            }

            @Override
            public ObjectIdResolver idResolver() {
                return null;
            }

            @Override
            public JsonDeserializer<Object> idDeserializer() {
                return null;
            }

            @Override
            public SettableBeanProperty idProperty() {
                return null;
            }
        };

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());

        Object instance = new Object();

        Object result = objectIdValueProperty.setAndReturn(instance, null);

        assertNull(instance.getClass().getDeclaredField("id").get(instance));
        assertEquals(null, result);
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public Class<Object> idType() {
                return Object.class;
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return null;
            }

            @Override
            public ObjectIdResolver idResolver() {
                return null;
            }

            @Override
            public JsonDeserializer<Object> idDeserializer() {
                return null;
            }

            @Override
            public SettableBeanProperty idProperty() {
                return null;
            }
        };

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());

        assertNull(objectIdValueProperty.getAnnotation(Annotation.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public Class<Object> idType() {
                return Object.class;
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return null;
            }

            @Override
            public ObjectIdResolver idResolver() {
                return null;
            }

            @Override
            public JsonDeserializer<Object> idDeserializer() {
                return null;
            }

            @Override
            public SettableBeanProperty idProperty() {
                return null;
            }
        };

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());

        assertNull(objectIdValueProperty.getMember());
    }
}