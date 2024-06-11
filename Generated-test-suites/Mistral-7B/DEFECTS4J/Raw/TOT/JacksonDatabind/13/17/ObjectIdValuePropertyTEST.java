package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReaderBase;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ObjectIdValuePropertyTest {

    private ObjectIdReader _objectIdReader;
    private ObjectIdValueProperty _property;

    @Before
    public void setUp() throws Exception {
        _objectIdReader = new ObjectIdReaderBase() {
            @Override
            public Class<?> getIdType() {
                return Long.class;
            }

            @Override
            public PropertyName propertyName() {
                return new PropertyName("id");
            }

            @Override
            public JsonDeserializer<ObjectId> getIdDeserializer() {
                return null;
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return new ObjectIdGenerator() {
                    private long _id = 1L;

                    @Override
                    public ObjectId generateId(Serializable idInfo) {
                        return new ObjectId(_id++, null);
                    }
                };
            }

            @Override
            public ObjectIdResolver idResolver() {
                return mock(ObjectIdResolver.class);
            }

            @Override
            public String getIdPropertyName() {
                return null;
            }
        };

        _property = new ObjectIdValueProperty(_objectIdReader, new PropertyMetadata());
    }

    @Test
    public void testDeserializeAndSet() throws IOException, JsonProcessingException {
        ObjectNode node = new ObjectMapper().createObjectNode();
        node.put("id", 1L);

        Object obj = new Object() {
            private Long id;
        };

        _property.deserializeAndSet(new JsonParser(node.traversingElement("id")), new DeserializationContext(), obj);

        assertEquals(1L, obj.id);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectNode node = new ObjectMapper().createObjectNode();
        node.put("id", 1L);

        Object obj = new Object() {
            private Long id;
        };

        Object result = _property.deserializeSetAndReturn(new JsonParser(node.traversingElement("id")), new DeserializationContext(), obj);

        assertEquals(obj, result);
        assertEquals(1L, obj.id);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object obj = new Object() {
            private Long id;
        };

        Object result = _property.setAndReturn(obj, 1L);

        assertEquals(obj, result);
        assertEquals(1L, obj.id);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetWithoutIdProperty() throws IOException {
        Object obj = new Object() {
            private Long id;
        };

        ObjectIdValueProperty property = new ObjectIdValueProperty(_objectIdReader, null);

        property.set(obj, null);
    }
}