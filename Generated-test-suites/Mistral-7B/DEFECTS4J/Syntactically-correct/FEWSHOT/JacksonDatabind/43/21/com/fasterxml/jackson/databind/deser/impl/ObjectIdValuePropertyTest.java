package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.core.util.TestUtil.createParser;
import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import java.io.IOException;
import java.lang.reflect.Field;

public class ObjectIdValuePropertyTest {

    private static class DummyObjectIdReader implements ObjectIdReader {
        public String propertyName = "id";
        public Class<?> idType = String.class;
        public ObjectIdReader generator = DummyObjectIdGenerator.instance;
        public ObjectIdReader resolver = DummyObjectIdResolver.instance;
        public SettableBeanProperty idProperty;
        public JsonDeserializer<?> idDeserializer;

        @Override
        public ObjectIdReader withIdProperty(SettableBeanProperty property) {
            idProperty = property;
            return this;
        }

        @Override
        public ObjectIdReader withIdDeserializer(JsonDeserializer<?> deserializer) {
            idDeserializer = deserializer;
            return this;
        }
    }

    private static class DummyObjectIdGenerator implements ObjectIdGenerator {
        public static final DummyObjectIdGenerator instance = new DummyObjectIdGenerator();

        @Override
        public ObjectId findId(Object id) {
            return new DummyObjectId(id);
        }
    }

    private static class DummyObjectIdResolver implements ObjectIdResolver {
        public static final DummyObjectIdResolver instance = new DummyObjectIdResolver();

        @Override
        public ObjectId idFromString(String idStr) {
            return new DummyObjectId(idStr);
        }

        @Override
        public String idToString(ObjectId id) {
            return id.toString();
        }
    }

    @SuppressWarnings("unchecked")
    private static class DummyObjectId implements ObjectId {
        private final Object id;

        public DummyObjectId(Object id) {
            this.id = id;
        }

        @Override
        public Object getIdValue() {
            return id;
        }

        @Override
        public String toString() {
            return "DummyObjectId{" + id + '}';
        }
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        ObjectIdReader reader = new DummyObjectIdReader();
        ObjectMapper mapper = new ObjectMapper();
        reader.idDeserializer = mapper.findValueDeserializer(String.class, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(reader, null);

        String json = "{\"id\": \"foo\"}";
        JsonParser parser = createParser(json);
        Object instance = new Object();

        property.deserializeAndSet(parser, null, instance);

        Object id = reader.generator.findId(instance);
        assertEquals("foo", id.getIdValue());
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        ObjectIdReader reader = new DummyObjectIdReader();
        reader.idDeserializer = (JsonDeserializer<String>) new StdDeserializer<>(String.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(reader, null);

        String json = "{\"id\": \"foo\"}";
        JsonParser parser = createParser(json);

        Object instance = new Object();
        Object id = property.deserializeSetAndReturn(parser, null, instance);

        Object idFromInstance = reader.generator.findId(instance);
        assertEquals(id, idFromInstance);
        assertEquals("foo", id.getIdValue());
    }

    @Test
    public void testSetAndReturn() throws Exception {
        ObjectIdReader reader = new DummyObjectIdReader();
        ObjectIdValueProperty property = new ObjectIdValueProperty(reader, null);

        Object instance = new Object();
        Object id = new Object();

        Object result = property.setAndReturn(instance, id);

        Object idFromInstance = reader.generator.findId(instance);
        assertEquals(id, idFromInstance);
        assertEquals(id, result);
    }

    @Test
    public void testGetAnnotation() throws Exception {
        ObjectIdReader reader = new DummyObjectIdReader();
        ObjectIdValueProperty property = new ObjectIdValueProperty(reader, null);

        assertEquals(null, property.getAnnotation(JsonId.class));
    }

    @Test
    public void testGetMember() throws Exception {
        ObjectIdReader reader = new DummyObjectIdReader();
        ObjectIdValueProperty property = new ObjectIdValueProperty(reader, null);

        assertNull(property.getMember());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCallSetOnPropertyWithoutIdProperty() throws Exception {
        ObjectIdReader reader = new DummyObjectIdReader();
        ObjectIdValueProperty property = new ObjectIdValueProperty(reader, null);

        Object instance = new Object();
        Object id = new Object();

        property.set(instance, id);
    }
}