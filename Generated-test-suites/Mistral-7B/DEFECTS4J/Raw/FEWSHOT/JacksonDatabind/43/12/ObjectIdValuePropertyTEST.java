package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.annotation.JsonTest.assertNull;
import static com.fasterxml.jackson.annotation.JsonTest.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdReader;

/**
 * Test class for ObjectIdValueProperty
 */
public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
    private JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
    private PropertyName propertyName = mock(PropertyName.class);

    @JsonProperty
    private static class TestClass {
        private Object id;
    }

    @Test
    public void testCreateObjectIdValueProperty() {
        when(objectIdReader.propertyName).thenReturn(propertyName);
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        assertEquals(propertyName, objectIdValueProperty.getName());
        assertEquals(objectIdReader.getClass(), objectIdValueProperty._objectIdReader.getClass());
        assertEquals(valueDeserializer, objectIdValueProperty.getValueDeserializer());
        assertEquals(null, objectIdValueProperty.getAnnotation(JsonProperty.class));
        assertEquals(null, objectIdValueProperty.getMember());
    }

    @Test
    public void testGetAnnotation() {
        assertEquals(null, objectIdValueProperty.getAnnotation(JsonProperty.class));
    }

    @Test
    public void testGetMember() {
        assertEquals(null, objectIdValueProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata()) {
            @Override
            public ObjectIdValueProperty withName(PropertyName newName) {
                return new ObjectIdValueProperty(this, newName);
            }

            @Override
            public ObjectIdValueProperty withValueDeserializer(JsonDeserializer<?> deser) {
                return new ObjectIdValueProperty(this, deser);
            }
        };

        TestClass testInstance = new TestClass();
        when(objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));

        when(valueDeserializer.deserialize(mock(JsonParser.class), mock(DeserializationContext.class)))
                .thenReturn("testIdValue");

        objectIdValueProperty.deserializeAndSet(mock(JsonParser.class), mock(DeserializationContext.class), testInstance);
        assertNotNull(testInstance.getId());
        assertEquals("testIdValue", testInstance.getId());
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        TestClass testInstance = new TestClass();
        ObjectId id = mock(ObjectId.class);
        when(objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        when(id.getIdValueAsText()).thenReturn("testIdValue");
        when(id.bindItem(testInstance)).thenReturn(testInstance);

        when(valueDeserializer.deserialize(mock(JsonParser.class), mock(DeserializationContext.class)))
                .thenReturn(id);

        Object result = objectIdValueProperty.deserializeSetAndReturn(mock(JsonParser.class), mock(DeserializationContext.class), testInstance);
        assertNotNull(result);
        assertEquals(testInstance, result);
    }

    @Test
    public void testSet() throws IOException {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata()) {
            @Override
            public ObjectIdValueProperty withName(PropertyName newName) {
                return new ObjectIdValueProperty(this, newName);
            }

            @Override
            public ObjectIdValueProperty withValueDeserializer(JsonDeserializer<?> deser) {
                return new ObjectIdValueProperty(this, deser);
            }
        };

        TestClass testInstance = new TestClass();
        ObjectId id = mock(ObjectId.class);
        when(id.getIdValueAsText()).thenReturn("testIdValue");
        when(objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        when(objectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));

        objectIdValueProperty.set(testInstance, id);
        assertNotNull(testInstance.getId());
        assertEquals("testIdValue", testInstance.getId().getIdValueAsText());
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata()) {
            @Override
            public ObjectIdValueProperty withName(PropertyName newName) {
                return new ObjectIdValueProperty(this, newName);
            }

            @Override
            public ObjectIdValueProperty withValueDeserializer(JsonDeserializer<?> deser) {
                return new ObjectIdValueProperty(this, deser);
            }
        };

        TestClass testInstance = new TestClass();
        ObjectId id = mock(ObjectId.class);
        when(id.getIdValueAsText()).thenReturn("testIdValue");
        when(objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        when(objectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));

        Object result = objectIdValueProperty.setAndReturn(testInstance, id);
        assertNotNull(result);
        assertEquals(testInstance, result);
    }
}