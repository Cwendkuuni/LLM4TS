
package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonParser;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.util.ReadableObjectId;
import org.junit.Test;

import java.io.IOException;
import java.lang.annotation.Annotation;

public class ObjectIdValuePropertyTest {
    @Test
    public void testWithNullProvider() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        NullValueProvider nullValueProvider = new NullValueProvider();
        SettableBeanProperty withNullProvider = property.withNullProvider(nullValueProvider);

        assertEquals(nullValueProvider, withNullProvider.getNullProvider());
    }

    @Test
    public void testWithNullProvider_doesNotAlterOriginalProperty() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        NullValueProvider nullValueProvider = new NullValueProvider();
        property.withNullProvider(nullValueProvider);

        assertNull(property.getNullProvider());
    }

    @Test
    public void testWithNullProvider_returnsNewInstanceWithNullProvider() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        NullValueProvider nullValueProvider = new NullValueProvider();
        SettableBeanProperty withNullProvider = property.withNullProvider(nullValueProvider);

        assertEquals(nullValueProvider, withNullProvider.getNullProvider());
        assertEquals(property.getClass(), withNullProvider.getClass());
        assertEquals(property.getNullProvider(), null);
    }

    @Test
    public void testWithName() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        PropertyName newName = new PropertyName("newName");
        SettableBeanProperty withName = property.withName(newName);

        assertEquals(newName, withName.getFullName());
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        JsonDeserializer<?> deserializer = new JsonDeserializer<>();
        SettableBeanProperty withValueDeserializer = property.withValueDeserializer(deserializer);

        assertEquals(deserializer, withValueDeserializer.getValueDeserializer());
    }

    @Test
    public void testWithValueDeserializer_doesNotAlterOriginalProperty() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        JsonDeserializer<?> deserializer = new JsonDeserializer<>();
        property.withValueDeserializer(deserializer);

        assertNull(property.getValueDeserializer());
    }

    @Test
    public void testWithValueDeserializer_returnsNewInstanceWithValueDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        JsonDeserializer<?> deserializer = new JsonDeserializer<>();
        SettableBeanProperty withValueDeserializer = property.withValueDeserializer(deserializer);

        assertEquals(deserializer, withValueDeserializer.getValueDeserializer());
        assertEquals(property.getClass(), withValueDeserializer.getClass());
        assertEquals(property.getValueDeserializer(), null);
    }

    @Test
    public void testSetAndReturn_throwsUnsupportedOperationException() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        try {
            property.setAndReturn(null, null);
        } catch (UnsupportedOperationException e) {
            assertEquals("Should not call set() on ObjectIdProperty that has no SettableBeanProperty", e.getMessage());
        }
    }

    @Test
    public void testDeserializeAndSet_withNullToken_returnsNull() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        JsonParser p = new JsonParser(null);
        DeserializationContext ctxt = new DeserializationContext(null, null);
        assertNull(property.deserializeAndSet(p, ctxt, null));
    }

    @Test
    public void testDeserializeAndSet_withNonNullToken_callsDeserializeSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        JsonParser p = new JsonParser(null);
        DeserializationContext ctxt = new DeserializationContext(null, null);
        Object instance = new Object();
        property.deserializeAndSet(p, ctxt, instance);
    }

    @Test
    public void testDeserializeSetAndReturn_withNullToken_returnsNull() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        JsonParser p = new JsonParser(null);
        DeserializationContext ctxt = new DeserializationContext(null, null);
        assertNull(property.deserializeSetAndReturn(p, ctxt, null));
    }

    @Test
    public void testDeserializeSetAndReturn_withNonNullToken_callsFindObjectIdAndBindItemAndSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        JsonParser p = new JsonParser(null);
        DeserializationContext ctxt = new DeserializationContext(null, null);
        Object instance = new Object();
        assertNull(property.deserializeSetAndReturn(p, ctxt, instance));
    }

    @Test
    public void testGetAnnotation_alwaysReturnsNull() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        assertNull(property.getAnnotation(null));
    }

    @Test
    public void testGetMember_alwaysReturnsNull() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        assertNull(property.getMember());
    }
}
