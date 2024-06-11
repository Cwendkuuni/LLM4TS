package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdNode;

/**
 * Test class for ObjectIdValueProperty
 */
public class ObjectIdValuePropertyTest {

    // Test constructor
    @Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return new PropertyName("objectId");
            }

            @Override
            public Class<?> getIdType() {
                return ObjectIdNode.class;
            }

            @Override
            public ObjectIdGenerator<?> generator() {
                return null;
            }

            @Override
            public ObjectIdResolver resolver() {
                return null;
            }
        };

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        assertNotNull(property);
    }

    // Test deserializeAndSet method
    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return new PropertyName("objectId");
            }

            @Override
            public Class<?> getIdType() {
                return ObjectIdNode.class;
            }

            @Override
            public ObjectIdGenerator<?> generator() {
                return null;
            }

            @Override
            public ObjectIdResolver resolver() {
                return null;
            }
        };

        JsonDeserializer<ObjectIdNode> valueDeserializer = new StdDeserializer<ObjectIdNode>(ObjectIdNode.class) {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return new ObjectIdNode(p.getText());
            }
        };

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(), valueDeserializer);

        Object id = new ObjectIdNode("123");
        Object instance = new Object();

        property.deserializeAndSet(new JsonFactory().createParser("{\"objectId\": \"123\"}"), new TestContext(), instance);

        assertNotNull(instance);
        assertEquals("123", instance.getClass().getDeclaredField("objectId").get(instance));
    }

    // Test deserializeSetAndReturn method
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return new PropertyName("objectId");
            }

            @Override
            public Class<?> getIdType() {
                return ObjectIdNode.class;
            }

            @Override
            public ObjectIdGenerator<?> generator() {
                return null;
            }

            @Override
            public ObjectIdResolver resolver() {
                return null;
            }
        };

        JsonDeserializer<ObjectIdNode> valueDeserializer = new StdDeserializer<ObjectIdNode>(ObjectIdNode.class) {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return new ObjectIdNode(p.getText());
            }
        };

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(), valueDeserializer);

        Object id = new ObjectIdNode("123");
        Object instance = new Object();

        Object result = property.deserializeSetAndReturn(new JsonFactory().createParser("{\"objectId\": \"123\"}"), new TestContext(), instance);

        assertNotNull(result);
        assertEquals(instance, result);
        assertEquals("123", instance.getClass().getDeclaredField("objectId").get(instance));
    }

    // Test set method
    @Test
    public void testSet() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return new PropertyName("objectId");
            }

            @Override
            public Class<?> getIdType() {
                return ObjectIdNode.class;
            }

            @Override
            public ObjectIdGenerator<?> generator() {
                return null;
            }

            @Override
            public ObjectIdResolver resolver() {
                return null;
            }
        };

        JsonDeserializer<ObjectIdNode> valueDeserializer = new StdDeserializer<ObjectIdNode>(ObjectIdNode.class) {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return new ObjectIdNode(p.getText());
            }
        };

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(), valueDeserializer);
        Object instance = new Object();

        property.set(instance, new ObjectIdNode("123"));

        assertNotNull(instance);
        assertEquals("123", instance.getClass().getDeclaredField("objectId").get(instance));
    }

    // Test setAndReturn method
    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return new PropertyName("objectId");
            }

            @Override
            public Class<?> getIdType() {
                return ObjectIdNode.class;
            }

            @Override
            public ObjectIdGenerator<?> generator() {
                return null;
            }

            @Override
            public ObjectIdResolver resolver() {
                return null;
            }
        };

        JsonDeserializer<ObjectIdNode> valueDeserializer = new StdDeserializer<ObjectIdNode>(ObjectIdNode.class) {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return new ObjectIdNode(p.getText());
            }
        };

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(), valueDeserializer);
        Object instance = new Object();

        Object result = property.setAndReturn(instance, new ObjectIdNode("123"));

        assertNotNull(result);
        assertEquals(instance, result);
        assertEquals("123", instance.getClass().getDeclaredField("objectId").get(instance));
    }

    // Test getAnnotation method
    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);

        assertNull(property.getAnnotation(null));
    }

    // Test getMember method
    @Test
    public void testGetMember() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);

        assertNull(property.getMember());
    }

    // Test deserialization with null id
    @Test(expected = IOException.class)
    public void testDeserializeNullId() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return new PropertyName("objectId");
            }

            @Override
            public Class<?> getIdType() {
                return ObjectIdNode.class;
            }

            @Override
            public ObjectIdGenerator<?> generator() {
                return null;
            }

            @Override
            public ObjectIdResolver resolver() {
                return null;
            }
        };

        JsonDeserializer<ObjectIdNode> valueDeserializer = new StdDeserializer<ObjectIdNode>(ObjectIdNode.class) {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return new ObjectIdNode(p.getText());
            }
        };

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(), valueDeserializer);

        Object instance = new Object();

        property.deserializeAndSet(new JsonFactory().createParser("{}"), new TestContext(), instance);

        fail("Should throw IOException");
    }

    // Test deserialization with empty id
    @Test(expected = IOException.class)
    public void testDeserializeEmptyId() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return new PropertyName("objectId");
            }

            @Override
            public Class<?> getIdType() {
                return ObjectIdNode.class;
            }

            @Override
            public ObjectIdGenerator<?> generator() {
                return null;
            }

            @Override
            public ObjectIdResolver resolver() {
                return null;
            }
        };

        JsonDeserializer<ObjectIdNode> valueDeserializer = new StdDeserializer<ObjectIdNode>(ObjectIdNode.class) {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return new ObjectIdNode(p.getText());
            }
        };

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(), valueDeserializer);

        Object instance = new Object();

        property.deserializeAndSet(new JsonFactory().createParser("{\"objectId\": \"\"}"), new TestContext(), instance);

        fail("Should throw IOException");
    }

    // Test deserialization with invalid id
    @Test(expected = IOException.class)
    public void testDeserializeInvalidId() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return new PropertyName("objectId");
            }

            @Override
            public Class<?> getIdType() {
                return ObjectIdNode.class;
            }

            @Override
            public ObjectIdGenerator<?> generator() {
                return null;
            }

            @Override
            public ObjectIdResolver resolver() {
                return null;
            }
        };

        JsonDeserializer<ObjectIdNode> valueDeserializer = new StdDeserializer<ObjectIdNode>(ObjectIdNode.class) {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return new ObjectIdNode(p.getText());
            }
        };

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(), valueDeserializer);

        Object instance = new Object();

        property.deserializeAndSet(new JsonFactory().createParser("{\"objectId\": \"invalidId\"}"), new TestContext(), instance);

        fail("Should throw IOException");
    }
}