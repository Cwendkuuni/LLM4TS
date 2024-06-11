package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.node.*;

import java.io.IOException;

/**
 * Test class for {@link ObjectIdValueProperty}.
 */
public class ObjectIdValuePropertyTest {

    private static ObjectIdReader mockObjectIdReader() {
        return new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return new PropertyName("id");
            }

            @Override
            public Class<?> idType() {
                return Long.class;
            }

            @Override
            public JsonDeserializer<ObjectId> getIdDeserializer() {
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
            public SettableBeanProperty idProperty() {
                return null;
            }
        };
    }

    @SuppressWarnings("unchecked")
    @JsonSetter(name = "objectIdProperty")
    private static class ObjectIdValuePropertySetter extends ObjectIdValueProperty {
        public ObjectIdValuePropertySetter(ObjectIdReader objectIdReader, PropertyMetadata metadata) {
            super(objectIdReader, metadata);
        }
    }

    @JsonTest
    public void testCreateObjectIdValueProperty() throws IOException {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        PropertyMetadata metadata = new StandardPropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        assertNotNull(property);
        assertEquals(objectIdReader.propertyName(), property.getName());
        assertEquals(objectIdReader.getClass(), property.getMember().getAnnotated().getClass());
        assertEquals(ObjectId.class, property.getType());
        assertEquals(property.getName(), property.getName());
        assertEquals(JsonDeserializer.class, property.getValueDeserializer().getClass());
    }

    @JsonTest
    public void testCreateObjectIdValuePropertyWithName() throws IOException {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        PropertyMetadata metadata = new StandardPropertyMetadata();
        String newName = "newName";
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, newName, metadata);
        assertEquals(newName, property.getName());
    }

    @JsonTest
    public void testCreateObjectIdValuePropertyWithDeserializer() throws IOException {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        PropertyMetadata metadata = new StandardPropertyMetadata();
        JsonDeserializer<?> deserializer = new StdDeserializer<>();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata, deserializer);
        assertEquals(deserializer, property.getValueDeserializer());
    }

    @JsonTest
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        PropertyMetadata metadata = new StandardPropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdReader spyObjectIdReader = spy(objectIdReader);
        doReturn(new LongTextNode("123")).when(spyObjectIdReader).deserialize(eq(null), eq(null), anyObject());

        JsonParser parser = new JsonFactory().createParser(new StringReader("{\"id\":123}"));
        Object instance = new Object();
        property.deserializeAndSet(parser, new TestContext(), instance);
        ObjectId id = spyObjectIdReader.getObjectId(instance);
        verify(id).bindItem(instance);
        assertEquals("123", id.toString());
    }

    @JsonTest
    public void testSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        PropertyMetadata metadata = new StandardPropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdReader spyObjectIdReader = spy(objectIdReader);
        doReturn(new LongTextNode("123")).when(spyObjectIdReader).deserialize(eq(null), eq(null), anyObject());
        ObjectId id = spyObjectIdReader.getObjectId(new Object());
        doReturn(id).when(spyObjectIdReader).getObjectId(eq(new Object()));
        doReturn(new Long(123)).when(id).asLong();
        Object instance = new Object();
        Object value = new Long(123);
        Object result = property.setAndReturn(instance, value);
        assertEquals(instance, result);
        verify(id).bindItem(instance);
        assertEquals("123", id.toString());
    }

    @JsonTest
    public void testSet() throws IOException {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        PropertyMetadata metadata = new StandardPropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdReader spyObjectIdReader = spy(objectIdReader);
        doReturn(new LongTextNode("123")).when(spyObjectIdReader).deserialize(eq(null), eq(null), anyObject());
        ObjectId id = spyObjectIdReader.getObjectId(new Object());
        doReturn(id).when(spyObjectIdReader).getObjectId(eq(new Object()));
        doReturn(new Long(123)).when(id).asLong();
        Object instance = new Object();
        Object value = new Long(123);
        property.set(instance, value);
        verify(id).bindItem(instance);
        assertEquals("123", id.toString());
    }

    @JsonTest
    public void testGetAnnotation() throws IOException {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        PropertyMetadata metadata = new StandardPropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        assertNull(property.getAnnotation(ObjectIdAnnotation.class));
    }

    @JsonTest
    public void testGetMember() throws IOException {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        PropertyMetadata metadata = new StandardPropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        assertNull(property.getMember());
    }

    @JsonTest
    public void testWithName() throws IOException {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        PropertyMetadata metadata = new StandardPropertyMetadata();
        String newName = "newName";
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdValueProperty result = property.withName(new PropertyName(newName));
        assertEquals(newName, result.getName());
    }

    @JsonTest
    public void testWithValueDeserializer() throws IOException {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        PropertyMetadata metadata = new StandardPropertyMetadata();
        JsonDeserializer<?> deserializer = new StdDeserializer<>();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdValueProperty result = property.withValueDeserializer(deserializer);
        assertEquals(deserializer, result.getValueDeserializer());
    }

    @JsonTest
    public void testCreateObjectIdValuePropertySetter() throws IOException {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        PropertyMetadata metadata = new StandardPropertyMetadata();
        ObjectIdValuePropertySetter propertySetter = new ObjectIdValuePropertySetter(objectIdReader, metadata);
        assertNotNull(propertySetter);
        assertEquals(objectIdReader.propertyName(), propertySetter.getName());
        assertEquals(ObjectId.class, propertySetter.getType());
        assertEquals(propertySetter.getName(), propertySetter.getName());
        assertEquals(JsonDeserializer.class, propertySetter.getValueDeserializer().getClass());
    }
}