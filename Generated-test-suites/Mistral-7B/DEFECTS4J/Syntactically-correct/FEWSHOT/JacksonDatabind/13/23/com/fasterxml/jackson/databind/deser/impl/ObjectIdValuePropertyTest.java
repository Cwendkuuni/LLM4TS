package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.core.util.TestUtils.constructParser;
import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.util.TestBase;

public class ObjectIdValuePropertyTest extends TestBase {

    @Test
    public void testDeserializeAndSet() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, new PropertyName("id"), new SimpleObjectIdReader(Object.class), new SimpleDeserializer());
        JsonParser jp = constructParser(getClass().getResourceAsStream("/ObjectIdValuePropertyTest.json"));
        Object instance = new Object();

        property.deserializeAndSet(jp, new TestContext(), instance);

        Object id = jp.getValueAsToken().getValueAsInt();
        Object readId = property.getPropertyValue(instance);
        assertEquals(id, readId);
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, new PropertyName("id"), new SimpleObjectIdReader(Object.class), new SimpleDeserializer());
        JsonParser jp = constructParser(getClass().getResourceAsStream("/ObjectIdValuePropertyTest.json"));
        Object instance = new Object();

        Object result = property.deserializeSetAndReturn(jp, new TestContext(), instance);
        assertEquals(instance, result);

        Object id = jp.getValueAsToken().getValueAsInt();
        Object readId = property.getPropertyValue(instance);
        assertEquals(id, readId);
    }

    @Test
    public void testSetAndReturn() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, new PropertyName("id"), new SimpleObjectIdReader(Object.class), new SimpleDeserializer());
        Object instance = new Object();
        int id = 1;

        Object result = property.setAndReturn(instance, id);
        assertEquals(instance, result);
        assertEquals(id, property.getPropertyValue(instance));
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, new PropertyName("id"), new SimpleObjectIdReader(Object.class), new SimpleDeserializer());
        assertNull(property.getAnnotation(JsonId.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, new PropertyName("id"), new SimpleObjectIdReader(Object.class), new SimpleDeserializer());
        assertNull(property.getMember());
    }

    static class SimpleDeserializer extends JsonDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return jp.getValueAsInt();
        }
    }

    static class SimpleObjectIdReader implements ObjectIdReader {
        @Override
        public PropertyName propertyName() {
            return new PropertyName("id");
        }

        @Override
        public Class<? extends ObjectIdGenerator> idType() {
            return ObjectIdGenerator.class;
        }

        @Override
        public ObjectIdGenerator generator() {
            return new ObjectIdGenerator();
        }

        @Override
        public ObjectIdResolver resolver() {
            return new ObjectIdResolver();
        }
    }

    static class ObjectIdGenerator implements ObjectIdGenerator {
        @Override
        public ObjectId generateId(SerializerProvider serializers, JsonGenerator gen, Object idInfo) throws IOException {
            return null;
        }
    }

    static class ObjectIdResolver implements ObjectIdResolver {
        @Override
        public ObjectId idFromString(String idStr, ObjectIdGenerator generator) throws IOException {
            return new ObjectId(Integer.parseInt(idStr));
        }
    }

    static class ObjectId implements ObjectId {
        private final int id;

        public ObjectId(int id) {
            this.id = id;
        }

        @Override
        public int idValue() {
            return id;
        }

        @Override
        public String toString() {
            return String.valueOf(id);
        }
    }

    static class TestContext extends DeserializationContext {
        @Override
        public ObjectIdResolver getObjectIdResolver() {
            return new ObjectIdResolver();
        }

        @Override
        public ObjectIdGenerator getObjectIdGenerator(DeserializationConfig config) {
            return new ObjectIdGenerator();
        }
    }
}