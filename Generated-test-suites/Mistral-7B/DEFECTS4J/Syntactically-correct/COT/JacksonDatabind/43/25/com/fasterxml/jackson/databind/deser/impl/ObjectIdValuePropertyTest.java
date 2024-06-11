package com.example.junit.test.jackson;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.lang.annotation.Annotation;

public class ObjectIdValuePropertyTest {

    private static final ObjectIdReader DUMMY_OBJECT_ID_READER = new ObjectIdReader() {
        @Override
        public PropertyName propertyName() {
            return PropertyName.constructor(new Class[]{Object.class}, "id");
        }

        @Override
        public Class<?> idType() {
            return Object.class;
        }

        @Override
        public JsonDeserializer<?> idDeserializer() {
            return new StdDeserializer<ObjectIdNode>(ObjectIdNode.class) {
            };
        }

        @Override
        public ObjectIdGenerator idGenerator() {
            return null;
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

    @org.junit.Test
    public void testObjectIdValuePropertyConstruction() {
        ObjectIdValueProperty prop = new ObjectIdValueProperty(DUMMY_OBJECT_ID_Reader, null);
        assertNotNull(prop);
    }

    @org.junit.Test
    public void testObjectIdValuePropertyWithName() {
        PropertyName newName = PropertyName.constructor(new Class[]{Object.class}, "newName");
        ObjectIdValueProperty prop = new ObjectIdValueProperty(DUMMY_OBJECT_ID_Reader, newName);
        assertEquals(newName, prop.getName());
    }

    @org.junit.Test
    public void testObjectIdValuePropertyWithValueDeserializer() {
        JsonDeserializer<ObjectIdNode> deserializer = new StdDeserializer<ObjectIdNode>(ObjectIdNode.class) {};
        ObjectIdValueProperty prop = new ObjectIdValueProperty(DUMMY_OBJECT_ID_Reader, deserializer);
        assertEquals(deserializer, prop.getValueDeserializer());
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testGetAnnotationThrowsException() {
        ObjectIdValueProperty prop = new ObjectIdValueProperty(DUMMY_OBJECT_ID_Reader, null);
        prop.getAnnotation(Annotation.class);
    }

    @org.junit.Test
    public void testGetMemberReturnsNull() {
        ObjectIdValueProperty prop = new ObjectIdValueProperty(DUMMY_OBJECT_ID_Reader, null);
        assertNull(prop.getMember());
    }

    @org.junit.Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdValueProperty prop = new ObjectIdValueProperty(DUMMY_OBJECT_ID_Reader, null);
        Object id = new ObjectIdNode(1L).asObject();
        JsonParser p = new JsonFactory().createParser("{\"id\": 1}");
        Object instance = new Object();

        prop.deserializeAndSet(p, new DeserializationContext(new ObjectMapper()), instance);

        assertEquals(id, prop.getValue(instance));
    }

    @org.junit.Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdValueProperty prop = new ObjectIdValueProperty(DUMMY_OBJECT_ID_Reader, null);
        Object id = new ObjectIdNode(1L).asObject();
        JsonParser p = new JsonFactory().createParser("{\"id\": 1}");
        Object instance = new Object();

        Object result = prop.deserializeSetAndReturn(p, new DeserializationContext(new ObjectMapper()), instance);

        assertEquals(instance, result);
        assertEquals(id, prop.getValue(instance));
    }

    @org.junit.Test
    public void testSetAndReturn() throws IOException {
        ObjectIdValueProperty prop = new ObjectIdValueProperty(DUMMY_OBJECT_ID_Reader, null);
        Object id = new ObjectIdNode(1L).asObject();
        Object instance = new Object();

        Object result = prop.setAndReturn(instance, id);

        assertEquals(instance, result);
        assertEquals(id, prop.getValue(instance));
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetThrowsException() throws IOException {
        ObjectIdValueProperty prop = new ObjectIdValueProperty(DUMMY_OBJECT_ID_Reader, null);
        Object instance = new Object();
        prop.set(instance, new Object());
    }
}