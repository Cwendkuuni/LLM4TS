package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReader;
    private PropertyMetadata metadata;
    private JsonDeserializer deserializer;
    private NullValueProvider nullValueProvider;

    @Before
    public void setUp() {
        objectIdReader = mock(ObjectIdReader.class);
        metadata = mock(PropertyMetadata.class);
        deserializer = mock(JsonDeserializer.class);
        nullValueProvider = mock(NullValueProvider.class);
    }

    @Test
    public void testConstructor() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        assertSame(objectIdReader, property._objectIdReader);
        assertNull(property._valueDeserializer);
        assertNull(property._nullProvider);
    }

    @Test
    public void testCopyConstructorWithDeserializerAndNullValueProvider() {
        ObjectIdValueProperty original = new ObjectIdValueProperty(objectIdReader, metadata);
        original._valueDeserializer = deserializer;
        original._nullProvider = nullValueProvider;

        ObjectIdValueProperty copy = new ObjectIdValueProperty(original, deserializer, nullValueProvider);

        assertSame(objectIdReader, copy._objectIdReader);
        assertSame(deserializer, copy._valueDeserializer);
        assertSame(nullValueProvider, copy._nullProvider);
    }

    @Test
    public void testCopyConstructorWithPropertyName() {
        ObjectIdValueProperty original = new ObjectIdValueProperty(objectIdReader, metadata);
        PropertyName newName = mock(PropertyName.class);

        ObjectIdValueProperty copy = new ObjectIdValueProperty(original, newName);

        assertSame(objectIdReader, copy._objectIdReader);
        assertEquals(newName, copy.getFullName());
        assertNull(copy._valueDeserializer);
        assertNull(copy._nullProvider);
    }

    @Test
    public void testWithName() {
        ObjectIdValueProperty original = new ObjectIdValueProperty(objectIdReader, metadata);
        PropertyName newName = mock(PropertyName.class);

        ObjectIdValueProperty copy = original.withName(newName);

        assertSame(objectIdReader, copy._objectIdReader);
        assertEquals(newName, copy.getFullName());
        assertNull(copy._valueDeserializer);
        assertNull(copy._nullProvider);
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdValueProperty original = new ObjectIdValueProperty(objectIdReader, metadata);

        ObjectIdValueProperty copy = original.withValueDeserializer(deserializer);

        assertSame(objectIdReader, copy._objectIdReader);
        assertSame(deserializer, copy._valueDeserializer);
        assertNull(copy._nullProvider);
    }

    @Test
    public void testWithNullProvider() {
        ObjectIdValueProperty original = new ObjectIdValueProperty(objectIdReader, metadata);

        ObjectIdValueProperty copy = original.withNullProvider(nullValueProvider);

        assertSame(objectIdReader, copy._objectIdReader);
        assertNull(copy._valueDeserializer);
        assertSame(nullValueProvider, copy._nullProvider);
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        assertNull(property.getAnnotation(null));
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object instance = new Object();

        property.deserializeAndSet(parser, context, instance);

        verify(property).deserializeSetAndReturn(parser, context, instance);
    }

    @Test
    public void testDeserializeSetAndReturnWithNullValue() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object instance = new Object();

        when(parser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);

        assertNull(property.deserializeSetAndReturn(parser, context, instance));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object instance = new Object();
        Object id = new Object();
        ReadableObjectId roid = mock(ReadableObjectId.class);
        SettableBeanProperty idProp = mock(SettableBeanProperty.class);

        property._objectIdReader = objectIdReader;
        when(objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        when(parser.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(property._valueDeserializer.deserialize(parser, context)).thenReturn(id);
        when(context.findObjectId(id, objectIdReader.generator, objectIdReader.resolver)).thenReturn(roid);
        when(roid.bindItem(instance)).thenReturn(instance);
        property._objectIdReader.idProperty = idProp;
        when(idProp.setAndReturn(instance, id)).thenReturn(instance);

        assertSame(instance, property.deserializeSetAndReturn(parser, context, instance));

        verify(property).deserializeSetAndReturn(parser, context, instance);
        verify(idProp).setAndReturn(instance, id);
    }

    @Test
    public void testSetWithoutIdProperty() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        try {
            property.set(new Object(), new Object());
        } catch (UnsupportedOperationException e) {
            assertEquals("Should not call set() on ObjectIdProperty that has no SettableBeanProperty", e.getMessage());
        }
    }

    @Test
    public void testSetAndReturnWithoutIdProperty() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        try {
            property.setAndReturn(new Object(), new Object());
        } catch (UnsupportedOperationException e) {
            assertEquals("Should not call set() on ObjectIdProperty that has no SettableBeanProperty", e.getMessage());
        }
    }
}