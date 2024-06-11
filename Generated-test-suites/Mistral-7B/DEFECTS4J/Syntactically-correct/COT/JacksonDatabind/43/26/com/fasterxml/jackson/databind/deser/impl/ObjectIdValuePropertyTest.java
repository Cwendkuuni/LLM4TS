package com.fasterxml.jackson.databind.deser.impl.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdNode;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.io.IOException;
import java.lang.annotation.Annotation;

public class ObjectIdValuePropertyTest {

    private static ObjectIdReader createObjectIdReaderMock() {
        return new ObjectIdReader() {
            PropertyName propertyName = PropertyName.construct("id");
            Class idType = ObjectIdNode.class;
            ObjectIdGenerator generator = null;
            ObjectIdResolver resolver = null;
            JsonDeserializer<?> idDeserializer = new StandardDeserializer<>("ObjectIdValuePropertyTest.IdReader") {
                @Override
                public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                    return ObjectIdNode.valueOf("testId");
                }
            };

            @Override
            public PropertyName getPropertyName() {
                return propertyName;
            }

            @Override
            public Class<ObjectIdNode> getIdType() {
                return idType;
            }

            @Override
            public ObjectIdGenerator getObjectIdGenerator() {
                return generator;
            }

            @Override
            public ObjectIdResolver getObjectIdResolver() {
                return resolver;
            }

            @Override
            public JsonDeserializer<?> getIdDeserializer() {
                return idDeserializer;
            }
        };
    }

    @org.junit.Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = createObjectIdReaderMock();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
        assertNotNull(property);
    }

    @org.junit.Test
    public void testWithName() {
        ObjectIdReader objectIdReader = createObjectIdReaderMock();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
        PropertyName newName = PropertyName.construct("newName");
        ObjectIdValueProperty newProperty = property.withName(newName);
        assertEquals(newName, newProperty.getName());
    }

    @org.junit.Test
    public void testWithValueDeserializer() {
        ObjectIdReader objectIdReader = createObjectIdReaderMock();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
        JsonDeserializer<?> deserializer = new StandardDeserializer<>("ObjectIdValuePropertyTest.Deserializer") {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return "testDeserializedValue";
            }
        };
        ObjectIdValueProperty newProperty = property.withValueDeserializer(deserializer);
        assertSame(deserializer, newProperty.getValueDeserializer());
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetPropertyWithNullIdProperty() throws IOException {
        ObjectIdReader objectIdReader = createObjectIdReaderMock();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
        Object instance = new Object();
        Object id = ObjectIdNode.valueOf("testId");
        property.set(instance, id);
        fail("Should throw UnsupportedOperationException");
    }

    @org.junit.Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReader objectIdReader = createObjectIdReaderMock();
        ObjectMapper mapper = new ObjectMapper();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
        Object instance = new Object();
        String json = "{\"id\":\"testId\"}";
        JsonParser p = mapper.getFactory().createParser(json);
        property.deserializeAndSet(p, mapper.getDeserializationContext(), instance);
        ObjectIdNode id = (ObjectIdNode) instance.getClass().getMethod("getId").invoke(instance);
        assertEquals("testId", id.getValue().asText());
    }

    @org.junit.Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = createObjectIdReaderMock();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
        Object instance = new Object();
        Object id = ObjectIdNode.valueOf("testId");
        Object result = property.deserializeSetAndReturn(JsonParserFactory.getEmptyParser(),
                ObjectMapperTestUtil.createContext(), instance);
        assertEquals(instance, result);
        ObjectIdNode idResult = (ObjectIdNode) instance.getClass().getMethod("getId").invoke(instance);
        assertEquals("testId", idResult.getValue().asText());
    }

    @org.junit.Test
    public void testSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = createObjectIdReaderMock();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
        Object instance = new Object();
        Object id = ObjectIdNode.valueOf("testId");
        Object result = property.setAndReturn(instance, id);
        assertEquals(instance, result);
        ObjectIdNode idResult = (ObjectIdNode) instance.getClass().getMethod("getId").invoke(instance);
        assertEquals("testId", idResult.getValue().asText());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testDeserializeAndSetNullInstance() throws IOException {
        ObjectIdReader objectIdReader = createObjectIdReaderMock();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
        Object instance = null;
        String json = "{\"id\":\"testId\"}";
        JsonParser p = new JsonFactory().createParser(json);
        property.deserializeAndSet(p, ObjectMapperTestUtil.createContext(), instance);
        fail("Should throw IllegalArgumentException");
    }

    @org.junit.Test(expected = IOException.class)
    public void testDeserializeAndSetEmptyParser() throws IOException {
        ObjectIdReader objectIdReader = createObjectIdReaderMock();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
        Object instance = new Object();
        JsonParser p = JsonParserFactory.getEmptyParser();
        property.deserializeAndSet(p, ObjectMapperTestUtil.createContext(), instance);
        fail("Should throw IOException");
    }

    @org.junit.Test(expected = IOException.class)
    public void testDeserializeSetAndReturnNullParser() throws IOException {
        ObjectIdReader objectIdReader = createObjectIdReaderMock();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
        Object instance = new Object();
        JsonParser p = JsonParserFactory.getEmptyParser();
        Object result = property.deserializeSetAndReturn(p, ObjectMapperTestUtil.createContext(), instance);
        fail("Should throw IOException");
    }

    @org.junit.Test(expected = IOException.class)
    public void testSetAndReturnNullInstance() throws IOException {
        ObjectIdReader objectIdReader = createObjectIdReaderMock();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
        Object instance = null;
        Object id = ObjectIdNode.valueOf("testId");
        Object result = property.setAndReturn(instance, id);
        fail("Should throw IOException");
    }

    @org.junit.Test(expected = IOException.class)
    public void testSetAndReturnNullId() throws IOException {
        ObjectIdReader objectIdReader = createObjectIdReaderMock();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
        Object instance = new Object();
        Object result = property.setAndReturn(instance, null);
        fail("Should throw IOException");
    }

    @org.junit.Test
    public void testGetAnnotation() {
        ObjectIdReader objectIdReader = createObjectIdReaderMock();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
        assertNull(property.getAnnotation(Annotation.class));
    }

    @org.junit.Test
    public void testGetMember() {
        ObjectIdReader objectIdReader = createObjectIdReaderMock();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
        assertNull(property.getMember());
    }
}