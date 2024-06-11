package com.fasterxml.jackson.databind.deser.impl.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import org.junit.*;

public class ObjectIdValuePropertyTest {

    ObjectIdReader objectIdReader;
    ObjectIdValueProperty objectIdValueProperty;

    @Before
    public void setUp() throws Exception {
        objectIdReader = new ObjectIdReader("idProperty", ObjectIdReader.IdType.STRING, null, new StdSerializer<String>(String.class));
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
    }

    // Testing constructor method

    @Test
    public void testObjectIdValuePropertyConstructor() {
        assertNotNull(objectIdValueProperty);
    }

    // Testing deprecated constructors

    @Test(expected = AssertionError.class)
    public void testObjectIdValuePropertyDeprecatedConstructor1() {
        new ObjectIdValueProperty(objectIdValueProperty, new JsonDeserializer<ObjectIdValueProperty>() {});
    }

    @Test(expected = AssertionError.class)
    public void testObjectIdValuePropertyDeprecatedConstructor2() {
        new ObjectIdValueProperty(objectIdValueProperty, "newName");
    }

    // Testing getter methods

    @Test
    public void testGetAnnotation() {
        assertNull(objectIdValueProperty.getAnnotation(Object.class));
    }

    @Test
    public void testGetMember() {
        assertNull(objectIdValueProperty.getMember());
    }

    // Testing deserialization methods

    @Test
    public void testDeserializeAndSet() throws Exception {
        ObjectParser parser = new ObjectMapper().treeAsTokens("{\"id\":\"123\"}").getParser();
        Object id = objectIdReader.getIdType().valueInstance();
        Object result = objectIdValueProperty.deserializeAndSet(parser, null, new Object());
        assertEquals("123", result.toString());
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        ObjectParser parser = new ObjectMapper().treeAsTokens("{\"id\":\"123\"}").getParser();
        Object id = objectIdReader.getIdType().valueInstance();
        Object result = objectIdValueProperty.deserializeSetAndReturn(parser, null, new Object());
        assertEquals("123", result.toString());
    }

    @Test
    public void testSet() throws Exception {
        Object id = new ObjectIdNode("123");
        objectIdValueProperty.set(new Object(), id);
        Object result = objectIdValueProperty.get(new Object());
        assertSame(id, result);
    }

    @Test
    public void testSetAndReturn() throws Exception {
        Object id = new ObjectIdNode("123");
        Object result = objectIdValueProperty.setAndReturn(new Object(), id);
        assertSame(id, result);
    }

    // Testing exception handling

    @Test(expected = UnsupportedOperationException.class)
    public void testSetWithoutIdProperty() throws Exception {
        ObjectIdReader objectIdReaderWithoutIdProperty = new ObjectIdReader("idProperty", ObjectIdReader.IdType.STRING, null, new StdSerializer<String>(String.class));
        ObjectIdValueProperty objectIdValuePropertyWithoutIdProperty = new ObjectIdValueProperty(objectIdReaderWithoutIdProperty, new PropertyMetadata());
        Object instance = new Object();
        Object value = new ObjectIdNode("123");
        objectIdValuePropertyWithoutIdProperty.set(instance, value);
    }
}