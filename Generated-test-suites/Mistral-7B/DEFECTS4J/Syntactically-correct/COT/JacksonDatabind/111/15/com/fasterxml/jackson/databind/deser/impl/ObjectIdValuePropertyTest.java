package com.fasterxml.jackson.databind.deser.impl.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.NameTransformer;

import org.junit.Test;

/**
 * Test class for ObjectIdValueProperty
 */
public class ObjectIdValuePropertyTest {

    @Test
    public void testConstructorWithObjectIdReader() throws Exception {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return new PropertyName("id");
            }

            @Override
            public Class<Object> getIdType() {
                return Long.class;
            }

            @Override
            public JsonDeserializer<Object> getIdDeserializer() {
                return new StdScalarDeserializer(Long.class);
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return new ObjectIdGenerator() {
                    long id = 0;

                    @Override
                    public Object generateId() {
                        id++;
                        return id;
                    }
                };
            }

            @Override
            public ObjectIdResolver idResolver() {
                return new ObjectIdResolver() {
                    @Override
                    public IdType idType(DeserializationContext ctxt, BeanProperty property) {
                        return ObjectIdGenerator.IdType.INT;
                    }
                };
            }

            @Override
            public PropertyMetadata propertyMetadata() {
                return new PropertyMetadata();
            }
        };

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        assertEquals("id", property.getName());
        assertEquals(Long.class, property.getIdType());
        assertNull(property.getDeserializer());
        assertNotNull(property._objectIdReader);
    }

    @Test
    public void testConstructorWithCopiedProperties() throws Exception {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return new PropertyName("id");
            }

            // ... other methods omitted for brevity
        };

        ObjectIdValueProperty source = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        ObjectIdValueProperty copy = new ObjectIdValueProperty(source, null, null);

        assertEquals(source.getName(), copy.getName());
        assertEquals(source.getIdType(), copy.getIdType());
        assertSame(source._objectIdReader, copy._objectIdReader);
    }

    @Test
    public void testConstructorWithCopiedName() throws Exception {
        ObjectIdValueProperty source = new ObjectIdValueProperty(new PropertyName("sourceId"), null, null);
        ObjectIdValueProperty copy = new ObjectIdValueProperty(source, new PropertyName("copyId"));

        assertEquals(source.getName(), copy.getName());
        assertNotSame(source._objectIdReader, copy._objectIdReader);
        assertEquals("copyId", copy.getName());
    }

    @Test
    public void testConstructorWithCopiedDeserializer() throws Exception {
        JsonDeserializer<String> stringDeserializer = new StdScalarDeserializer(String.class);
        ObjectIdValueProperty source = new ObjectIdValueProperty(null, null, stringDeserializer);
        ObjectIdValueProperty copy = new ObjectIdValueProperty(source, null, null);

        assertNull(copy.getDeserializer());
        assertSame(source._objectIdReader, copy._objectIdReader);
    }

    @Test
    public void testGetAnnotation() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, new PropertyMetadata());
        assertNull(property.getAnnotation(JsonProperty.class));
    }

    @Test
    public void testGetMember() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, new PropertyMetadata());
        assertNull(property.getMember());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetWithNoIdProperty() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, new PropertyMetadata());
        Object instance = new Object();
        property.set(instance, "value");
        fail("Expected UnsupportedOperationException");
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return new PropertyName("id");
            }

            @Override
            public Class<Object> getIdType() {
                return Long.class;
            }

            @Override
            public JsonDeserializer<Object> getIdDeserializer() {
                return new StdScalarDeserializer(Long.class);
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return new ObjectIdGenerator() {
                    long id = 0;

                    @Override
                    public Object generateId() {
                        id++;
                        return id;
                    }
                };
            }

            @Override
            public ObjectIdResolver idResolver() {
                return new ObjectIdResolver() {
                    @Override
                    public IdType idType(DeserializationContext ctxt, BeanProperty property) {
                        return ObjectIdGenerator.IdType.INT;
                    }
                };
            }

            @Override
            public PropertyMetadata propertyMetadata() {
                return new PropertyMetadata();
            }
        };

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object idInstance = new Object();

        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory()
                .createParser(new ByteArrayInputStream("1".getBytes()));

        property.deserializeAndSet(parser, new TestContext(), idInstance);
        assertEquals(1L, ((Number) idInstance).longValue());

        parser = mapper.getFactory().createParser(new ByteArrayInputStream("null".getBytes()));
        property.deserializeAndSet(parser, new TestContext(), idInstance);
        assertNull(idInstance);
    }

    @Test(expected = IOException.class)
    public void testDeserializeSetAndReturnWithNullIdDeserializer() throws Exception {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return new PropertyName("id");
            }

            @Override
            public Class<Object> getIdType() {
                return Long.class;
            }

            @Override
            public JsonDeserializer<Object> getIdDeserializer() {
                return null;
            }

            // ... other methods omitted for brevity
        };

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object instance = new Object();
        JsonParser parser = new TestParser();

        property.deserializeSetAndReturn(parser, new TestContext(), instance);
        fail("Expected IOException");
    }

    @Test
    public void testSetAndReturn() throws Exception {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return new PropertyName("id");
            }

            @Override
            public Class<Object> getIdType() {
                return Long.class;
            }

            @Override
            public JsonDeserializer<Object> getIdDeserializer() {
                return new StdScalarDeserializer(Long.class);
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return new ObjectIdGenerator() {
                    long id = 0;

                    @Override
                    public Object generateId() {
                        id++;
                        return id;
                    }
                };
            }

            @Override
            public ObjectIdResolver idResolver() {
                return new ObjectIdResolver() {
                    @Override
                    public IdType idType(DeserializationContext ctxt, BeanProperty property) {
                        return ObjectIdGenerator.IdType.INT;
                    }
                };
            }

            @Override
            public PropertyMetadata propertyMetadata() {
                return new PropertyMetadata();
            }
        };

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object instance = new Object();
        JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream("1".getBytes()));

        Object result = property.setAndReturn(instance, 1L);
        assertEquals(instance, result);
        assertEquals(1L, ((Number) instance).longValue());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetWithNullInstance() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, new PropertyMetadata());
        property.set(null, 1L);
        fail("Expected UnsupportedOperationException");
    }

    @Test(expected = IOException.class)
    public void testSetWithNullValue() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, new PropertyMetadata());
        Object instance = new Object();
        property.set(instance, null);
        fail("Expected IOException");
    }

    @Test(expected = IOException.class)
    public void testSetWithValueDeserializationFailure() throws Exception {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return new PropertyName("id");
            }

            @Override
            public Class<Object> getIdType() {
                return Long.class;
            }

            @Override
            public JsonDeserializer<Object> getIdDeserializer() {
                return new Base64Deserializer(); // Fails to deserialize
            }

            // ... other methods omitted for brevity
        };

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object instance = new Object();
        JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream("1".getBytes()));

        try {
            property.deserializeAndSet(parser, new TestContext(), instance);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    private static class TestParser extends JsonParser {
        @Override
        public JsonToken nextToken() throws IOException {
            return JsonToken.START_OBJECT;
        }
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public ObjectIdResolver idResolver() {
            return new ObjectIdResolver() {
                @Override
                public IdType idType(DeserializationContext ctxt, BeanProperty property) {
                    return ObjectIdGenerator.IdType.INT;
                }
            };
        }

        @Override
        public ObjectIdGenerator idGenerator() {
            return new ObjectIdGenerator() {
                long id = 0;

                @Override
                public Object generateId() {
                    id++;
                    return id;
                }
            };
        }

        @Override
        public AnnotationIntrospector introspector() {
            return new AnnotationIntrospector() {
                @Override
                public Object findAnnotation(Object valueId, Class<?> valueType) {
                    return null;
                }
            };
        }

        @Override
        public JsonDeserializer<?> findBeanDeserializer(Class<?> beanType) {
            return null;
        }

        @Override
        public BeanDescription findBeanDescription(Class<?> beanType) {
            return null;
        }

        @Override
        public TypeDeserializer findValueDeserializer(Class<?> type, JsonSerializer serializer) {
            return null;
        }

        @Override
        public JsonParser currentToken() {
            return null;
        }

        @Override
        public void findAndRegisterAlternatives(Class<?> type, JsonDeserializer<?> deserializer,
                BeanProperty property) throws JsonMappingException {
            // empty
        }

        @Override
        public JsonDeserializer<?> findValueDeserializer(Class<?> type) {
            return null;
        }

        @Override
        public void setSerializationConfig(SerializationConfig config) {
            // empty
        }

        @Override
        public String getBaseName() {
            return "test";
        }

        @Override
        public void setRootValue(Object value) {
            // empty
        }

        @Override
        public void setCurrentLocation(LocationLocator locator) {
            // empty
        }

        @Override
        public void close() throws IOException {
            // empty
        }

        @Override
        public void setNameTransformer(NameTransformer nameTransformer) {
            // empty
        }

        @Override
        public void setActiveAttributeFinder(AttributeFinder attributeFinder) {
            // empty
        }
    }
}