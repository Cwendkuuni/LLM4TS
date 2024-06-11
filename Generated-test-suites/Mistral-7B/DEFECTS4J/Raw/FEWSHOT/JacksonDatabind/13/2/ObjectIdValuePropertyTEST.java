package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdReader;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class ObjectIdValuePropertyTest {

    @Test
    public void testObjectIdValueProperty() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return new PropertyName("id");
            }

            @Override
            public Class<?> idType() {
                return Long.class;
            }

            @Override
            public JsonDeserializer<Object> idDeserializer() {
                return new StdDeserializer<Long>(Long.class) {
                    @Override
                    public Long deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                        return jp.getValueAsLong();
                    }
                };
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
            public SettableBeanProperty idProperty() {
                return null;
            }
        };

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        assertEquals("id", objectIdValueProperty.getName());
        assertEquals(Long.class, objectIdValueProperty.getType());
        assertNull(objectIdValueProperty.getAnnotation(ObjectId.class));
        assertNull(objectIdValueProperty.getMember());

        ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withName(new PropertyName("newName"));
        assertEquals("newName", newObjectIdValueProperty.getName());

        ObjectIdValueProperty newObjectIdValuePropertyWithDeserializer = objectIdValueProperty.withValueDeserializer(new StdDeserializer<String>(String.class));
        assertNotNull(newObjectIdValuePropertyWithDeserializer.getValueDeserializer());

        Object id = new ObjectIdValuePropertyTest().testDeserializeSetAndReturn(objectIdValueProperty, new JsonParserFactory().getParser("{\"id\":1}"));
        assertEquals(1L, id);
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
                return Long.class;
            }

            @Override
            public JsonDeserializer<Object> idDeserializer() {
                return new StdDeserializer<Long>(Long.class) {
                    @Override
                    public Long deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                        return jp.getValueAsLong();
                    }
                };
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
            public SettableBeanProperty idProperty() {
                return null;
            }
        };

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        Object id = new ObjectIdValuePropertyTest().testDeserializeSetAndReturn(objectIdValueProperty, new JsonParserFactory().getParser("{\"id\":1}"));
        assertEquals(1L, id);
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
                return Long.class;
            }

            @Override
            public JsonDeserializer<Object> idDeserializer() {
                return new StdDeserializer<Long>(Long.class) {
                    @Override
                    public Long deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                        return jp.getValueAsLong();
                    }
                };
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
            public SettableBeanProperty idProperty() {
                return new SettableBeanProperty("id", Long.class, null, null) {
                    @Override
                    public Object setAndReturn(Object instance, Object value) throws IOException {
                        assertEquals(Long.class, instance.getClass());
                        assertEquals(1L, value);
                        return value;
                    }
                };
            }
        };

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        Object id = new Object();
        Object idValue = 1L;
        Object instance = new Object();

        Object newId = objectIdValueProperty.setAndReturn(instance, idValue);
        assertEquals(idValue, newId);
        assertEquals(idValue, instance.getClass().getField("id").get(instance));
    }

    private Object testDeserializeSetAndReturn(ObjectIdValueProperty objectIdValueProperty, JsonParser jp) throws IOException {
        Object id = objectIdValueProperty.deserializeSetAndReturn(jp, null, null);
        return id;
    }
}