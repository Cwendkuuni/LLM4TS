package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.core.util.TestUtils.constructParser;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import java.io.IOException;
import java.lang.annotation.Annotation;

public class ObjectIdValuePropertyTest {

    @Test
    public void testObjectIdValueProperty() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public BeanPropertyDefinition propertyDefinition() {
                return new BeanPropertyDefinition() {
                    @Override
                    public PropertyName getName() {
                        return PropertyName.construct("id");
                    }

                    @Override
                    public JsonTypeDescriptor getTypeDescriptor() {
                        return null;
                    }

                    @Override
                    public Annotation[] getAnnnotations() {
                        return new JsonId();
                    }

                    @Override
                    public String getBeanPropertyName() {
                        return "id";
                    }

                    @Override
                    public String getFullName() {
                        return "id";
                    }

                    @Override
                    public String getNameAsString() {
                        return "id";
                    }
                };
            }

            @Override
            public PropertyMetadata getMetadata() {
                return new PropertyMetadata() {
                    @Override
                    public JsonSerializer<?> getSerializer() {
                        return null;
                    }

                    @Override
                    public Annotation[] getAnnnotations() {
                        return new JsonId();
                    }
                };
            }

            @Override
            public JsonDeserializer<Object> getIdDeserializer() {
                return null;
            }

            @Override
            public ObjectIdGenerator getGenerator() {
                return null;
            }

            @Override
            public ObjectIdResolver getResolver() {
                return null;
            }

            @Override
            public String getIdName() {
                return "id";
            }

            @Override
            public String getIdTypeName() {
                return "java.lang.String";
            }
        };

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        assertEquals("id", property.getName());
        assertSame(objectIdReader, property._objectIdReader);
        assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public BeanPropertyDefinition propertyDefinition() {
                return new BeanPropertyDefinition() {
                    @Override
                    public PropertyName getName() {
                        return PropertyName.construct("id");
                    }

                    @Override
                    public JsonTypeDescriptor getTypeDescriptor() {
                        return null;
                    }

                    @Override
                    public Annotation[] getAnnnotations() {
                        return new JsonId();
                    }

                    @Override
                    public String getBeanPropertyName() {
                        return "id";
                    }

                    @Override
                    public String getFullName() {
                        return "id";
                    }

                    @Override
                    public String getNameAsString() {
                        return "id";
                    }
                };
            }

            @Override
            public PropertyMetadata getMetadata() {
                return new PropertyMetadata() {
                    @Override
                    public JsonSerializer<?> getSerializer() {
                        return null;
                    }

                    @Override
                    public Annotation[] getAnnnotations() {
                        return new JsonId();
                    }
                };
            }

            @Override
            public JsonDeserializer<Object> getIdDeserializer() {
                return new StdJsonDeserializer<String>() {
                    @Override
                    public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                        return "idValue";
                    }
                };
            }

            @Override
            public ObjectIdGenerator getGenerator() {
                return new ObjectIdGenerator() {
                    @Override
                    public ObjectId generateId(SerializerProvider serializers, Object idValue) throws IOException {
                        return new ObjectId();
                    }
                };
            }

            @Override
            public ObjectIdResolver getResolver() {
                return new ObjectIdResolver() {
                    @Override
                    public ObjectId resolveId(Serializable id, ObjectIdGenerator generator) throws IOException {
                        return id;
                    }
                };
            }

            @Override
            public String getIdName() {
                return "id";
            }

            @Override
            public String getIdTypeName() {
                return "java.lang.String";
            }
        };

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        Object idValue = new Object();
        JsonParser jp = constructParser("{\"id\": \"idValue\"}");
        Object instance = new Object();

        property.deserializeAndSet(jp, new TestContext(), instance);

        assertEquals("idValue", property.getIdValue(instance));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public BeanPropertyDefinition propertyDefinition() {
                return new BeanPropertyDefinition() {
                    @Override
                    public PropertyName getName() {
                        return PropertyName.construct("id");
                    }

                    @Override
                    public JsonTypeDescriptor getTypeDescriptor() {
                        return null;
                    }

                    @Override
                    public Annotation[] getAnnnotations() {
                        return new JsonId();
                    }

                    @Override
                    public String getBeanPropertyName() {
                        return "id";
                    }

                    @Override
                    public String getFullName() {
                        return "id";
                    }

                    @Override
                    public String getNameAsString() {
                        return "id";
                    }
                };
            }

            @Override
            public PropertyMetadata getMetadata() {
                return new PropertyMetadata() {
                    @Override
                    public JsonSerializer<?> getSerializer() {
                        return null;
                    }

                    @Override
                    public Annotation[] getAnnnotations() {
                        return new JsonId();
                    }
                };
            }

            @Override
            public JsonDeserializer<Object> getIdDeserializer() {
                return new StdJsonDeserializer<String>() {
                    @Override
                    public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                        return "idValue";
                    }
                };
            }

            @Override
            public ObjectIdGenerator getGenerator() {
                return new ObjectIdGenerator() {
                    @Override
                    public ObjectId generateId(SerializerProvider serializers, Object idValue) throws IOException {
                        return new ObjectId();
                    }
                };
            }

            @Override
            public ObjectIdResolver getResolver() {
                return new ObjectIdResolver() {
                    @Override
                    public ObjectId resolveId(Serializable id, ObjectIdGenerator generator) throws IOException {
                        return id;
                    }
                };
            }

            @Override
            public String getIdName() {
                return "id";
            }

            @Override
            public String getIdTypeName() {
                return "java.lang.String";
            }
        };

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        Object idValue = new Object();
        Object instance = new Object();

        Object result = property.deserializeSetAndReturn(constructParser("{\"id\": \"idValue\"}"), new TestContext(), instance);

        assertEquals(instance, result);
        assertEquals("idValue", property.getIdValue(instance));
    }

    @Test
    public void testSet() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public BeanPropertyDefinition propertyDefinition() {
                return new BeanPropertyDefinition() {
                    @Override
                    public PropertyName getName() {
                        return PropertyName.construct("id");
                    }

                    @Override
                    public JsonTypeDescriptor getTypeDescriptor() {
                        return null;
                    }

                    @Override
                    public Annotation[] getAnnnotations() {
                        return new JsonId();
                    }

                    @Override
                    public String getBeanPropertyName() {
                        return "id";
                    }

                    @Override
                    public String getFullName() {
                        return "id";
                    }

                    @Override
                    public String getNameAsString() {
                        return "id";
                    }
                };
            }

            @Override
            public PropertyMetadata getMetadata() {
                return new PropertyMetadata() {
                    @Override
                    public JsonSerializer<?> getSerializer() {
                        return null;
                    }

                    @Override
                    public Annotation[] getAnnnotations() {
                        return new JsonId();
                    }
                };
            }

            @Override
            public JsonDeserializer<Object> getIdDeserializer() {
                return null;
            }

            @Override
            public ObjectIdGenerator getGenerator() {
                return null;
            }

            @Override
            public ObjectIdResolver getResolver() {
                return null;
            }

            @Override
            public String getIdName() {
                return "id";
            }

            @Override
            public String getIdTypeName() {
                return "java.lang.String";
            }
        };

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object instance = new Object();

        property.set(instance, "idValue");

        assertEquals("idValue", property.getIdValue(instance));
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public BeanPropertyDefinition propertyDefinition() {
                return new BeanPropertyDefinition() {
                    @Override
                    public PropertyName getName() {
                        return PropertyName.construct("id");
                    }

                    @Override
                    public JsonTypeDescriptor getTypeDescriptor() {
                        return null;
                    }

                    @Override
                    public Annotation[] getAnnnotations() {
                        return new JsonId();
                    }

                    @Override
                    public String getBeanPropertyName() {
                        return "id";
                    }

                    @Override
                    public String getFullName() {
                        return "id";
                    }

                    @Override
                    public String getNameAsString() {
                        return "id";
                    }
                };
            }

            @Override
            public PropertyMetadata getMetadata() {
                return new PropertyMetadata() {
                    @Override
                    public JsonSerializer<?> getSerializer() {
                        return null;
                    }

                    @Override
                    public Annotation[] getAnnnotations() {
                        return new JsonId();
                    }
                };
            }

            @Override
            public JsonDeserializer<Object> getIdDeserializer() {
                return null;
            }

            @Override
            public ObjectIdGenerator getGenerator() {
                return null;
            }

            @Override
            public ObjectIdResolver getResolver() {
                return null;
            }

            @Override
            public String getIdName() {
                return "id";
            }

            @Override
            public String getIdTypeName() {
                return "java.lang.String";
            }
        };

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object instance = new Object();

        Object result = property.setAndReturn(instance, "idValue");

        assertEquals(instance, result);
        assertEquals("idValue", property.getIdValue(instance));
    }
}