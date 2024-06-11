package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdIntrospect;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ObjectIdValuePropertyTest {

    @Test
    public void testObjectIdValueProperty() {
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
            public ObjectIdGenerator<?> idGenerator() {
                return new ObjectIdGenerator() {
                    @Override
                    public IdIntrospect introspect(AnnotatedMember member) {
                        return new IdIntrospect() {
                            @Override
                            public ObjectIdGenerator.IdResolver getIdResolver() {
                                return null;
                            }
                        };
                    }

                    @Override
                    public ObjectIdGenerator.IdGenerator idGeneratorInstance() {
                        return null;
                    }
                };
            }

            @Override
            public JsonDeserializer<Object> idDeserializer() {
                return null;
            }
        };

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        assertEquals("id", objectIdValueProperty.getName());
        assertEquals(Long.class, objectIdValueProperty.getType());
        assertNull(objectIdValueProperty.getAnnotation(JsonIdentityInfo.class));
        assertNull(objectIdValueProperty.getMember());

        assertNull(objectIdValueProperty.withName(null));

        JsonDeserializer<String> valueDeserializer = new StdDeserializer<String>(String.class) {};
        ObjectIdValueProperty objectIdValuePropertyWithValueDeserializer = objectIdValueProperty.withValueDeserializer(valueDeserializer);
        assertEquals(valueDeserializer, objectIdValuePropertyWithValueDeserializer.getValueDeserializer());

        NullValueProvider nullValueProvider = new NullValueProvider() {
            @Override
            public Object nullValue() {
                return null;
            }
        };
        ObjectIdValueProperty objectIdValuePropertyWithNullProvider = objectIdValuePropertyWithValueDeserializer.withNullProvider(nullValueProvider);
        assertEquals(nullValueProvider, objectIdValuePropertyWithNullProvider.getNullProvider());

        BeanPropertyDefinition idPropertyDefinition = new BeanPropertyDefinition("idProperty", Long.class);
        ObjectIdValueProperty objectIdValuePropertyWithIdProperty = objectIdValuePropertyWithNullProvider.withName("idProperty");
        assertEquals("idProperty", objectIdValuePropertyWithIdProperty.getName());
        assertEquals(Long.class, objectIdValuePropertyWithIdProperty.getType());
        assertNull(objectIdValuePropertyWithIdProperty.getValueDeserializer());
        assertNull(objectIdValuePropertyWithIdProperty.getNullProvider());
        assertNull(objectIdValuePropertyWithIdProperty.getIdProperty());

        ObjectNode objectNode = ObjectNode.parser().readValueAsTree("{}");
        ObjectMapper objectMapper = new ObjectMapper();
        Object idInstance = objectMapper.findAndRegisterValues(null, null, objectNode);
        ObjectIdValueProperty objectIdValuePropertyAnotherInstance = new ObjectIdValueProperty(objectIdReader, null);
        objectIdValuePropertyAnotherInstance.setValue(idInstance, 1L);
        Object result = objectIdValuePropertyAnotherInstance.deserializeAndSet(new JsonFactory(), null, idInstance);
        assertEquals(1L, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetNullValueOnObjectIdValueProperty() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {};
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        Object instance = new Object() {};
        objectIdValueProperty.set(instance, null);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
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
            public ObjectIdGenerator<?> idGenerator() {
                return new ObjectIdGenerator() {};
            }

            @Override
            public JsonDeserializer<Object> idDeserializer() {
                return new StdDeserializer<Long>(Long.class) {
                    @Override
                    public Long deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                        return 1L;
                    }
                };
            }
        };

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        Object instance = new Object() {};
        ObjectIdReader objectIdReaderForIdProperty = new ObjectIdReader() {};
        SettableBeanProperty idProperty = new ObjectIdValueProperty(objectIdReaderForIdProperty, null);
        ObjectIdValueProperty objectIdValuePropertyWithIdProperty = new ObjectIdValueProperty(objectIdValueProperty, idProperty);
        assertEquals(idProperty, objectIdValuePropertyWithIdProperty.getIdProperty());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser("{\"id\":1}");
        Object result = objectIdValuePropertyWithIdProperty.deserializeAndSet(jsonParser, null, instance);
        assertEquals(1L, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testdeserializeAndSetOnObjectIdValuePropertyWithNoIdProperty() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {};
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        Object instance = new Object() {};
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser("{\"id\":1}");
        objectIdValueProperty.deserializeAndSet(jsonParser, null, instance);
    }
}