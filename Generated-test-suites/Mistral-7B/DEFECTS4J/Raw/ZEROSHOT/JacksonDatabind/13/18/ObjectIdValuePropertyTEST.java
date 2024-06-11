package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer.None;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.PojoBootstrap;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.ser.PropertyWriter;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * Test class for {@link ObjectIdValueProperty}
 */
public class ObjectIdValuePropertyTest {

    private static class TestBean {
        @JsonDeserialize(using = ObjectIdValueProperty.class)
        private String id;

        @JsonDeserialize(using = None.class)
        private String name;
    }

    @SuppressWarnings("unchecked")
    private ObjectIdReader createObjectIdReaderMock() {
        return new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return new PropertyName("id");
            }

            @Override
            public Class<?> idType() {
                return String.class;
            }

            @Override
            public JsonDeserializer<?> idDeserializer() {
                return null;
            }

            @Override
            public ObjectIdGenerator generator() {
                return new ObjectIdGenerator() {
                    private long idCounter = 0;

                    @Override
                    public Object generateId(DeserializationContext ctxt) {
                        return Long.toString(idCounter++);
                    }
                };
            }

            @Override
            public ObjectIdResolver resolver() {
                return new ObjectIdResolver() {
                    @Override
                    public ObjectId idFromString(String idStr) {
                        return new ReadableObjectId() {
                            @Override
                            public String toString() {
                                return idStr;
                            }

                            @Override
                            public void bindItem(Object item) {
                                try {
                                    Field idField = TestBean.class.getDeclaredField("id");
                                    idField.set(item, idStr);
                                } catch (NoSuchFieldException | IllegalAccessException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        };
                    }
                };
            }

            @Override
            public SettableBeanProperty idProperty() {
                return new StubSettableBeanProperty();
            }
        };
    }

    private ObjectMapper createObjectMapper() throws InstantiationException, IllegalAccessException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        BeanDescription beanDesc = mapper.findBeanDescription(TestBean.class);
        PojoBootstrap bootstrap = new PojoBootstrap(beanDesc, mapper.getTypeFactory());
        ObjectIdReader objectIdReader = createObjectIdReaderMock();
        Field idReaderField = beanDesc.findProperty("id").getMember().getAnnotated();
        idReaderField.set(beanDesc, objectIdReader);
        return mapper;
    }

    @org.junit.Test
    public void testDeserializeSetAndReturn() throws JsonProcessingException, IOException {
        ObjectMapper mapper = createObjectMapper();
        TestBean testBean = mapper.readValue("{\"id\":\"1\"}", TestBean.class);
        assertEquals("1", testBean.getId());
    }

    @org.junit.Test
    public void testSetAndReturn() throws JsonProcessingException, IOException {
        ObjectMapper mapper = createObjectMapper();
        TestBean testBean = mapper.readValue("{\"name\":\"John\"}", TestBean.class);
        Object newId = "2";
        testBean = mapper.readerForUpdating(testBean)
                .readValue(new ObjectMapper().writeValueAsString(newId));
        assertEquals("2", testBean.getId());
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetWithoutIdProperty() throws JsonProcessingException, IOException {
        ObjectMapper mapper = createObjectMapper();
        TestBean testBean = mapper.readValue("{\"name\":\"John\"}", TestBean.class);
        testBean.setId("3"); // should not be allowed
    }

    private static class StubSettableBeanProperty extends SettableBeanProperty {
        @Override
        public ObjectIdValueProperty withName(PropertyName newName) {
            return this;
        }

        @Override
        public ObjectIdValueProperty withValueDeserializer(JsonDeserializer<?> deser) {
            return this;
        }

        @Override
        public <A extends Annotation> A getAnnotation(Class<A> acls) {
            return null;
        }

        @Override
        public AnnotatedMember getMember() {
            return null;
        }

        @Override
        public void deserializeAndSet(JsonParser jp, DeserializationContext ctxt, Object instance) throws IOException {
        }

        @Override
        public Object deserializeSetAndReturn(JsonParser jp, DeserializationContext ctxt, Object instance) throws IOException {
            return null;
        }

        @Override
        public void set(Object instance, Object value) throws IOException {
        }

        @Override
        public Object setAndReturn(Object instance, Object value) throws IOException {
            return null;
        }

        @Override
        public BeanPropertyDefinition getBeanPropertyDefinition(PojoBootstrap beanContext) {
            return null;
        }

        @Override
        public PropertyWriter getWriter(TypeDeserializer typeDeserializer, BeanPropertyDefinition propertyDefinition, JsonSerializer serializer) {
            return null;
        }

        @Override
        public String getName() {
            return "";
        }
    }

    @org.junit.Test
    public void testGetAnnotation() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        assertNull(property.getAnnotation(JsonDeserialize.class));
    }

    @org.junit.Test
    public void testGetMember() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        assertNull(property.getMember());
    }
}