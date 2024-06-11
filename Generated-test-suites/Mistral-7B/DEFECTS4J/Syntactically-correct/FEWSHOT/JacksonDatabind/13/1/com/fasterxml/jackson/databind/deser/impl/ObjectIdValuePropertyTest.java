package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.core.util.TestUtils.constructTestObject;
import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public class ObjectIdValuePropertyTest {

    private static class TestBean {
        @JsonId
        private ObjectId id;
    }

    @Test
    public void testObjectIdValueProperty() throws Exception {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return new PropertyName("id");
            }

            @Override
            public Class<?> idType() {
                return ObjectId.class;
            }

            @Override
            public BeanPropertyDefinition idProperty() {
                return new BeanPropertyDefinition(new PropertyName("id"), ObjectId.class);
            }

            @Override
            public JsonDeserializer<?> getIdDeserializer() {
                return new StandardDeserializer<>(ObjectId.class) {
                    @Override
                    public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                        return new ObjectId();
                    }
                };
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return new ObjectIdGenerator() {
                    private int counter = 0;

                    @Override
                    public ObjectId generateId(SerializerProvider serializers) {
                        counter++;
                        return new ObjectId(counter);
                    }
                };
            }

            @Override
            public ObjectIdResolver idResolver() {
                return new ObjectIdResolver() {
                    @Override
                    public ObjectId idFromString(String idStr) {
                        return new ObjectId(idStr);
                    }
                };
            }
        };

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        assertEquals("id", property.getName());
        assertEquals(ObjectId.class, property.getType());
        assertNull(property.getMember());
        assertNotNull(property._objectIdReader);

        ObjectNode node = TestUtils.writeValueAsTree(new TestBean());
        JsonParser jp = TestUtils.createParser(node);

        TestBean instance = new TestBean();
        property.deserializeAndSet(jp, null, instance);

        ObjectId id = property.getIdReader().idGenerator().generateId(null);
        assertEquals(id.toString(), instance.getId().toString());
    }

    @Test
    public void testSetAndReturn() throws Exception {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return new PropertyName("id");
            }

            @Override
            public Class<?> idType() {
                return ObjectId.class;
            }

            @Override
            public BeanPropertyDefinition idProperty() {
                return new BeanPropertyDefinition(new PropertyName("id"), ObjectId.class);
            }

            @Override
            public JsonDeserializer<?> getIdDeserializer() {
                return new StandardDeserializer<>(ObjectId.class) {
                    @Override
                    public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                        return new ObjectId();
                    }
                };
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return new ObjectIdGenerator() {
                    private int counter = 0;

                    @Override
                    public ObjectId generateId(SerializerProvider serializers) {
                        counter++;
                        return new ObjectId(counter);
                    }
                };
            }

            @Override
            public ObjectIdResolver idResolver() {
                return new ObjectIdResolver() {
                    @Override
                    public ObjectId idFromString(String idStr) {
                        return new ObjectId(idStr);
                    }
                };
            }
        };

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        TestBean instance = new TestBean();

        ObjectId id = property.getIdReader().idGenerator().generateId(null);
        ObjectId setId = new ObjectId(id.getValue() + 1);

        Object value = setId;
        Object result = property.setAndReturn(instance, value);

        assertEquals(setId, result);
        assertEquals(setId, instance.getId());
    }

    @Test
    public void testSetAndReturnNull() throws Exception {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return new PropertyName("id");
            }

            @Override
            public Class<?> idType() {
                return ObjectId.class;
            }

            @Override
            public BeanPropertyDefinition idProperty() {
                return new BeanPropertyDefinition(new PropertyName("id"), ObjectId.class);
            }

            @Override
            public JsonDeserializer<?> getIdDeserializer() {
                return new StandardDeserializer<>(ObjectId.class) {
                    @Override
                    public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                        return new ObjectId();
                    }
                };
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return new ObjectIdGenerator() {
                    private int counter = 0;

                    @Override
                    public ObjectId generateId(SerializerProvider serializers) {
                        counter++;
                        return new ObjectId(counter);
                    }
                };
            }

            @Override
            public ObjectIdResolver idResolver() {
                return new ObjectIdResolver() {
                    @Override
                    public ObjectId idFromString(String idStr) {
                        return new ObjectId(idStr);
                    }
                };
            }
        };

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        TestBean instance = new TestBean();

        Object result = property.setAndReturn(instance, null);
        assertNull(result);
        assertNull(instance.getId());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws Exception {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return new PropertyName("id");
            }

            @Override
            public Class<?> idType() {
                return ObjectId.class;
            }

            @Override
            public BeanPropertyDefinition idProperty() {
                return new BeanPropertyDefinition(new PropertyName("id"), ObjectId.class);
            }

            @Override
            public JsonDeserializer<?> getIdDeserializer() {
                return new StandardDeserializer<>(ObjectId.class) {
                    @Override
                    public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                        return new ObjectId();
                    }
                };
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return new ObjectIdGenerator() {
                    private int counter = 0;

                    @Override
                    public ObjectId generateId(SerializerProvider serializers) {
                        counter++;
                        return new ObjectId(counter);
                    }
                };
            }

            @Override
            public ObjectIdResolver idResolver() {
                return new ObjectIdResolver() {
                    @Override
                    public ObjectId idFromString(String idStr) {
                        return new ObjectId(idStr);
                    }
                };
            }
        };

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        TestBean instance = new TestBean();

        Object value = new Object();
        property.set(instance, value);

        fail("Should not reach here");
    }
}