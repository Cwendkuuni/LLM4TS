
package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;

import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReader;
    private PropertyMetadata metadata;
    private ObjectIdValueProperty objectIdValueProperty;

    public ObjectIdValuePropertyTest() {
        metadata = new PropertyMetadata(null, null, null);
        objectIdReader = new ObjectIdReader(null, null);
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nvp = new NullValueProvider();
        SettableBeanProperty result = objectIdValueProperty.withNullProvider(nvp);

        assertEquals(nvp, result.nullValueProvider());
    }

    @Test
    public void testWithDifferentName() {
        PropertyName newName = new PropertyName("newName");
        SettableBeanProperty result = objectIdValueProperty.withName(newName);

        assertEquals(newName, result.getName());
    }

    @Test
    public void testWithSameName() {
        PropertyName newName = objectIdValueProperty.getFullName();
        SettableBeanProperty result = objectIdValueProperty.withName(newName);

        assertSame(objectIdValueProperty, result);
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        SettableBeanProperty result = objectIdValueProperty.withValueDeserializer(deser);

        assertSame(deser, result.getValueDeserializer());
    }

    @Test
    public void testWithSameValueDeserializer() {
        JsonDeserializer<?> deser = objectIdValueProperty.getValueDeserializer();
        SettableBeanProperty result = objectIdValueProperty.withValueDeserializer(deser);

        assertSame(objectIdValueProperty, result);
    }

    @Test
    public void testDeserializeAndSetWithNullValue() throws IOException {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        JsonToken jsonToken = JsonToken.VALUE_NULL;

        p.setToken(jsonToken);

        Object result = objectIdValueProperty.deserializeSetAndReturn(p, ctxt, instance);

        assertNull(result);
    }

    @Test
    public void testDeserializeAndSetWithNonNullValue() throws IOException {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        Object deserializedValue = new Object();

        when(objectIdValueProperty.getValueDeserializer().deserialize(any(JsonParser.class), any(DeserializationContext.class)))
                .thenReturn(deserializedValue);

        ReadableObjectId roid = ctxt.findObjectId(deserializedValue,
                objectIdReader.generator, objectIdReader.resolver);
        roid.bindItem(instance);

        Object result = objectIdValueProperty.deserializeSetAndReturn(p, ctxt, instance);

        assertSame(instance, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturnWithoutIdProperty() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        SettableBeanProperty idProp = null;
        objectIdReader.idProperty = idProp;

        objectIdValueProperty.setAndReturn(instance, value);
    }

    @Test
    public void testSetAndReturnWithIdProperty() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        SettableBeanProperty idProp = new SettableBeanProperty();
        objectIdReader.idProperty = idProp;

        Object result = objectIdValueProperty.setAndReturn(instance, value);

        assertSame(idProp.setAndReturn(instance, value), result);
    }

}
