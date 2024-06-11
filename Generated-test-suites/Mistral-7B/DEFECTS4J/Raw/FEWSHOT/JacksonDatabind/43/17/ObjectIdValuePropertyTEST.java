package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.core.util.TestUtils.constructParser;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReader = new ObjectIdReader() {
        @Override
        public PropertyName propertyName() {
            return new PropertyName("objectId");
        }

        @Override
        public Class<?> idType() {
            return Long.class;
        }

        @Override
        public JsonDeserializer<ObjectIdValueProperty> idDeserializer() {
            return null;
        }

        @Override
        public ObjectIdGenerator idGenerator() {
            return new ObjectIdGenerator() {
                long nextId = 0;

                @Override
                public ObjectId generateId(SerializerProvider serializers) {
                    return new ObjectId((long) nextId++);
                }
            };
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

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        JsonParser p = constructParser("123");
        Object instance = new Object();

        property.deserializeAndSet(p, new DeserializationContext(), instance);

        assertEquals(123L, p.getValueAsLong());
        assertNull(p.nextToken());

        ObjectId id = new ObjectId(123L);
        assertEquals(id, property.get(instance));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        JsonParser p = constructParser("123");
        Object instance = new Object();

        Object result = property.deserializeSetAndReturn(p, new DeserializationContext(), instance);

        assertEquals(instance, result);
        assertEquals(123L, p.getValueAsLong());
        assertNull(p.nextToken());

        ObjectId id = new ObjectId(123L);
        assertEquals(id, property.get(instance));
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        Object instance = new Object();
        ObjectId id = new ObjectId(123L);

        Object result = property.setAndReturn(instance, id);

        assertEquals(instance, result);
        assertEquals(id, property.get(instance));
    }

    @Test
    public void testSet() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        Object instance = new Object();
        ObjectId id = new ObjectId(123L);

        property.set(instance, id);

        assertEquals(id, property.get(instance));
    }
}