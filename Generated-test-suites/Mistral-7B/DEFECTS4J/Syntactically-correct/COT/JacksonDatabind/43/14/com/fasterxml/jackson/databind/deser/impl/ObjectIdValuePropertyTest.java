package com.fasterxml.jackson.databind.deser.impl.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdNode;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for ObjectIdValueProperty class
 */
public class ObjectIdValuePropertyTest {

    ObjectIdReader objectIdReader;
    ObjectIdValueProperty objectIdValueProperty;

    @Before
    public void setup() {
        objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName getPropertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public Class<?> getIdType() {
                return ObjectIdNode.class;
            }

            @Override
            public JsonDeserializer<ObjectIdNode> getIdDeserializer() {
                return null;
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
            public AnnotatedMember getMember() {
                return null;
            }
        };
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new StandardPropertyMetadata());
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull(objectIdValueProperty);
    }

    @Test
    public void testWithNameConstructor() throws IOException {
        ObjectIdValueProperty nameConstructorObjectIdValueProperty = new ObjectIdValueProperty(objectIdValueProperty, new PropertyName("name"));
        assertEquals("name", nameConstructorObjectIdValueProperty.getName());
    }

    @Test
    public void testWithValueDeserializerConstructor() throws IOException {
        JsonDeserializer<ObjectIdNode> jsonDeserializer = new StdDeserializer<ObjectIdNode>(ObjectIdNode.class);
        ObjectIdValueProperty valueDeserializerConstructorObjectIdValueProperty = new ObjectIdValueProperty(objectIdValueProperty, jsonDeserializer);
        assertSame(jsonDeserializer, valueDeserializerConstructorObjectIdValueProperty.getValueDeserializer());
    }

    @Test
    public void testGetAnnotation() {
        assertNull(objectIdValueProperty.getAnnotation(null));
    }

    @Test
    public void testGetMember() {
        assertNull(objectIdValueProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object id = new ObjectIdNode(1L);
        JsonParser jsonParser = new TestJsonParser();
        jsonParser.setValue(id.getTextValue());

        Object instance = new Object();
        objectIdValueProperty.deserializeAndSet(jsonParser, new TestContext(), instance);

        ObjectIdNode expectedId = (ObjectIdNode) id;
        ObjectIdNode actualId = (ObjectIdNode) ctxt.findObjectId(expectedId.getId(), objectIdReader.generator, objectIdReader.resolver).getId();
        assertEquals(expectedId.getId(), actualId.getId().longValue());
    }

    @Test
    public void testDeserializeSetAndReturnNullId() throws IOException {
        Object id = null;
        JsonParser jsonParser = new TestJsonParser();
        jsonParser.setValue(id == null ? null : id.getTextValue());

        Object instance = new Object();
        Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParser, new TestContext(), instance);
        assertNull(result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetWithNoIdProperty() throws IOException {
        Object id = new ObjectIdNode(1L);
        Object instance = new Object();
        objectIdValueProperty.set(instance, id);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object id = new ObjectIdNode(1L);
        Object instance = new Object();
        SettableBeanProperty idProp = new SettableBeanProperty("idProperty", null, null, null, null, null);
        Object result = idProp.setAndReturn(instance, id);

        ObjectIdNode expectedId = (ObjectIdNode) id;
        assertEquals(expectedId.getId(), result.getClass().cast(result).getId().longValue());
    }
}