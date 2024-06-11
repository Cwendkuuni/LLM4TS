package com.fasterxml.jackson.databind.deser.impl.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.lang.reflect.Field;

public class ObjectIdValuePropertyTest {

    private static ObjectIdValueProperty createObjectIdValueProperty(ObjectIdReader objectIdReader) {
        return new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
    }

    @org.junit.Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader("idName", ObjectIdType.INT, null, null);
        ObjectIdValueProperty objectIdValueProperty = createObjectIdValueProperty(objectIdReader);
        assertNotNull(objectIdValueProperty);
    }

    @org.junit.Test
    public void testWithName() {
        ObjectIdReader objectIdReader = new ObjectIdReader("idName", ObjectIdType.INT, null, null);
        ObjectIdValueProperty objectIdValueProperty = createObjectIdValueProperty(objectIdReader);
        ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withName(PropertyName.construct("newIdName"));
        assertNotSame(objectIdValueProperty, newObjectIdValueProperty);
    }

    @org.junit.Test
    public void testWithValueDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader("idName", ObjectIdType.INT, null, null);
        ObjectIdValueProperty objectIdValueProperty = createObjectIdValueProperty(objectIdReader);
        JsonDeserializer<ObjectIdNode> jsonDeserializer = new StdSerializer<ObjectIdNode>(ObjectIdNode.class) {
            @Override
            public ObjectIdNode deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
                return null;
            }
        };
        ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withValueDeserializer(jsonDeserializer);
        assertNotSame(objectIdValueProperty, newObjectIdValueProperty);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testGetAnnotation() {
        ObjectIdReader objectIdReader = new ObjectIdReader("idName", ObjectIdType.INT, null, null);
        ObjectIdValueProperty objectIdValueProperty = createObjectIdValueProperty(objectIdReader);
        objectIdValueProperty.getAnnotation(Annotation.class);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testGetMember() {
        ObjectIdReader objectIdReader = new ObjectIdReader("idName", ObjectIdType.INT, null, null);
        ObjectIdValueProperty objectIdValueProperty = createObjectIdValueProperty(objectIdReader);
        objectIdValueProperty.getMember();
    }

    @org.junit.Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader("idName", ObjectIdType.INT, null, null);
        ObjectIdValueProperty objectIdValueProperty = createObjectIdValueProperty(objectIdReader);
        Object testInstance = new Object();
        JsonParser jsonParser = createMockJsonParser();
        objectIdValueProperty.deserializeAndSet(jsonParser, new TestContext(), testInstance);
        // check if ObjectId was set correctly
        Field objectIdField = testInstance.getClass().getDeclaredField("id");
        objectIdField.setAccessible(true);
        Object id = objectIdField.get(testInstance);
        // testId is not mocked in the test, so it should be null by default
        assertNull(id);
    }

    @org.junit.Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader("idName", ObjectIdType.INT, null, null);
        ObjectIdValueProperty objectIdValueProperty = createObjectIdValueProperty(objectIdReader);
        Object testInstance = new Object();
        JsonParser jsonParser = createMockJsonParser();
        Object id = objectIdValueProperty.deserializeSetAndReturn(jsonParser, new TestContext(), testInstance);
        // check if ObjectId was set correctly
        Field objectIdField = testInstance.getClass().getDeclaredField("id");
        objectIdField.setAccessible(true);
        assertEquals(id, objectIdField.get(testInstance));
    }

    @org.junit.Test
    public void testSet() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader("idName", ObjectIdType.INT, null, null);
        ObjectIdValueProperty objectIdValueProperty = createObjectIdValueProperty(objectIdReader);
        Object testInstance = new Object();
        Object id = new Integer(1);
        objectIdValueProperty.set(testInstance, id);
        // check if ObjectId was set correctly
        Field objectIdField = testInstance.getClass().getDeclaredField("id");
        objectIdField.setAccessible(true);
        assertEquals(id, objectIdField.get(testInstance));
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetWithNullId() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader("idName", ObjectIdType.INT, null, null);
        ObjectIdValueProperty objectIdValueProperty = createObjectIdValueProperty(objectIdReader);
        Object testInstance = new Object();
        objectIdValueProperty.set(testInstance, null);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetWithNonObjectIdType() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader("idName", String.class, null, null);
        ObjectIdValueProperty objectIdValueProperty = createObjectIdValueProperty(objectIdReader);
        Object testInstance = new Object();
        String id = "testId";
        objectIdValueProperty.set(testInstance, id);
    }

    @org.junit.Test
    public void testSetWithNullObjectInstance() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader("idName", ObjectIdType.INT, null, null);
        ObjectIdValueProperty objectIdValueProperty = createObjectIdValueProperty(objectIdReader);
        objectIdValueProperty.set(null, new Integer(1));
    }

    @org.junit.Test
    public void testSetWithNullIdValue() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader("idName", ObjectIdType.INT, null, null);
        ObjectIdValueProperty objectIdValueProperty = createObjectIdValueProperty(objectIdReader);
        Object testInstance = new Object();
        objectIdValueProperty.set(testInstance, null);
    }

    private JsonParser createMockJsonParser() {
        return mock(JsonParser.class);
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public ObjectIdGenerator idGenerator() {
            return null;
        }

        @Override
        public ObjectResolver objectResolver() {
            return null;
        }
    }

}