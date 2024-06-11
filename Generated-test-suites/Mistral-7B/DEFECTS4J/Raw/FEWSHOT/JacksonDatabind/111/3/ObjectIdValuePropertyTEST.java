package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.databind.DeserializationTest.createContext;
import static com.fasterxml.jackson.databind.DeserializationTest.createParser;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Test;

public class ObjectIdValuePropertyTest {

    @Test
    public void testDeserializeAndSet() throws Exception {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public Class<?> idType() {
                return Long.class;
            }

            @Override
            public JsonDeserializer<?> idDeserializer() {
                return new JsonDeserializer<Object>() {
                    @Override
                    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                        return 1L;
                    }
                };
            }

            @Override
            public SettableBeanProperty idProperty() {
                return null;
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return null;
            }

            @Override
            public ObjectIdResolver idResolver() {
                return null;
            }
        };
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);

        ObjectNode node = createParser().readValueAsTree("{}");
        Object ctx = createContext();
        Object instance = new Object();

        property.deserializeAndSet(createParser(node), ctx, instance);
        assertEquals(1L, instance.getClass().getField("id").get(instance));
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public Class<?> idType() {
                return Long.class;
            }

            @Override
            public JsonDeserializer<?> idDeserializer() {
                return new JsonDeserializer<Object>() {
                    @Override
                    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                        return 1L;
                    }
                };
            }

            @Override
            public SettableBeanProperty idProperty() {
                return new SettableBeanProperty("id", Long.class, null, null);
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return null;
            }

            @Override
            public ObjectIdResolver idResolver() {
                return null;
            }
        };
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);

        ObjectNode node = createParser().readValueAsTree("{}");
        Object ctx = createContext();
        Object instance = new Object();

        Object result = property.deserializeSetAndReturn(createParser(node), ctx, instance);
        assertEquals(1L, instance.getClass().getField("id").get(instance));
        assertEquals(instance, result);
    }

    @Test
    public void testSetAndReturn() throws Exception {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public Class<?> idType() {
                return Long.class;
            }

            @Override
            public JsonDeserializer<?> idDeserializer() {
                return new JsonDeserializer<Object>() {
                    @Override
                    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                        return 1L;
                    }
                };
            }

            @Override
            public SettableBeanProperty idProperty() {
                return new SettableBeanProperty("id", Long.class, null, null);
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return null;
            }

            @Override
            public ObjectIdResolver idResolver() {
                return null;
            }
        };
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);

        Object instance = new Object();
        Object value = 1L;

        Object result = property.setAndReturn(instance, value);
        assertEquals(value, instance.getClass().getField("id").get(instance));
        assertEquals(instance, result);
    }

    @Test
    public void testSet() throws Exception {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public Class<?> idType() {
                return Long.class;
            }

            @Override
            public JsonDeserializer<?> idDeserializer() {
                return new JsonDeserializer<Object>() {
                    @Override
                    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                        return 1L;
                    }
                };
            }

            @Override
            public SettableBeanProperty idProperty() {
                return new SettableBeanProperty("id", Long.class, null, null);
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return null;
            }

            @Override
            public ObjectIdResolver idResolver() {
                return null;
            }
        };
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);

        Object instance = new Object();
        Object value = 1L;

        property.set(instance, value);
        assertEquals(value, instance.getClass().getField("id").get(instance));
    }

    @Test
    public void testNullValue() throws Exception {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public Class<?> idType() {
                return Long.class;
            }

            @Override
            public JsonDeserializer<?> idDeserializer() {
                return new JsonDeserializer<Object>() {
                    @Override
                    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                        return null;
                    }
                };
            }

            @Override
            public SettableBeanProperty idProperty() {
                return new SettableBeanProperty("id", Long.class, null, null);
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return null;
            }

            @Override
            public ObjectIdResolver idResolver() {
                return null;
            }
        };
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);

        Object instance = new Object();

        property.deserializeAndSet(createParser(null), createContext(), instance);
        assertNull(instance.getClass().getField("id").get(instance));
    }

    @Test
    public void testAnnotation() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public Class<?> idType() {
                return Long.class;
            }

            @Override
            public JsonDeserializer<?> idDeserializer() {
                return new JsonDeserializer<Object>() {};
            }

            @Override
            public SettableBeanProperty idProperty() {
                ObjectIdValueProperty property = new ObjectIdValueProperty(this, null);
                return AnnotationIntrospector.findPropertyAnnotation(property, ObjectId.class);
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return null;
            }

            @Override
            public ObjectIdResolver idResolver() {
                return null;
            }
        };
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        assertEquals(ObjectId.class, property.getClassAnnotations()[0].annotationType());
    }

    @Test
    public void testWithName() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("oldId");
            }

            @Override
            public Class<?> idType() {
                return Long.class;
            }

            @Override
            public JsonDeserializer<?> idDeserializer() {
                return new JsonDeserializer<Object>() {};
            }

            @Override
            public SettableBeanProperty idProperty() {
                return null;
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return null;
            }

            @Override
            public ObjectIdResolver idResolver() {
                return null;
            }
        };
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        SettableBeanProperty newProperty = property.withName(PropertyName.construct("newId"));
        assertEquals(PropertyName.construct("newId"), newProperty.getName());
        assertEquals(property._objectIdReader, newProperty._objectIdReader);
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public Class<?> idType() {
                return Long.class;
            }

            @Override
            public JsonDeserializer<?> idDeserializer() {
                return new JsonDeserializer<Object>() {};
            }

            @Override
            public SettableBeanProperty idProperty() {
                return null;
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return null;
            }

            @Override
            public ObjectIdResolver idResolver() {
                return null;
            }
        };
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        JsonDeserializer<Object> deserializer = new JsonDeserializer<Object>() {};
        SettableBeanProperty newProperty = property.withValueDeserializer(deserializer);
        assertEquals(deserializer, newProperty._valueDeserializer);
        assertEquals(property._objectIdReader, newProperty._objectIdReader);
    }

    @Test
    public void testWithNullProvider() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public Class<?> idType() {
                return Long.class;
            }

            @Override
            public JsonDeserializer<?> idDeserializer() {
                return new JsonDeserializer<Object>() {};
            }

            @Override
            public SettableBeanProperty idProperty() {
                return null;
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return null;
            }

            @Override
            public ObjectIdResolver idResolver() {
                return null;
            }
        };
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        NullValueProvider nva = new NullValueProvider() {};
        SettableBeanProperty newProperty = property.withNullProvider(nva);
        assertEquals(nva, newProperty._nullProvider);
        assertEquals(property._objectIdReader, newProperty._objectIdReader);
    }
}