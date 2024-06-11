
package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

    @Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        assertSame(objectIdReader, property._objectIdReader);
        assertEquals(objectIdReader.propertyName, property.getName());
        assertEquals(objectIdReader.getIdType(), property.getType());
        assertSame(metadata, property.getMetadata());
        assertSame(objectIdReader.getDeserializer(), property.getValueDeserializer());
        assertNull(property.getNullValueProvider());
    }

    @Test
    public void testCopyConstructorWithDeserializerAndNullValueProvider() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        NullValueProvider nva = new NullValueProvider();

        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, deser, nva);

        assertSame(objectIdReader, property._objectIdReader);
        assertEquals(objectIdReader.propertyName, property.getName());
        assertEquals(objectIdReader.getIdType(), property.getType());
        assertSame(metadata, property.getMetadata());
        assertSame(deser, property.getValueDeserializer());
        assertSame(nva, property.getNullValueProvider());
    }

    @Test
    public void testCopyConstructorWithNewName() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        PropertyName newName = new PropertyName();

        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, newName);

        assertSame(objectIdReader, property._objectIdReader);
        assertEquals(newName, property.getName());
        assertEquals(objectIdReader.getIdType(), property.getType());
        assertSame(metadata, property.getMetadata());
        assertSame(objectIdReader.getDeserializer(), property.getValueDeserializer());
        assertNull(property.getNullValueProvider());
    }

    @Test
    public void testWithName() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        PropertyName newName = new PropertyName();

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        SettableBeanProperty result = property.withName(newName);

        assertSame(objectIdReader, result._objectIdReader);
        assertEquals(newName, result.getName());
        assertEquals(objectIdReader.getIdType(), result.getType());
        assertSame(metadata, result.getMetadata());
        assertSame(objectIdReader.getDeserializer(), result.getValueDeserializer());
        assertNull(result.getNullValueProvider());
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        JsonDeserializer<?> deser = new JsonDeserializer<>();

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        SettableBeanProperty result = property.withValueDeserializer(deser);

        assertSame(objectIdReader, result._objectIdReader);
        assertEquals(objectIdReader.propertyName, result.getName());
        assertEquals(objectIdReader.getIdType(), result.getType());
        assertSame(metadata, result.getMetadata());
        assertSame(deser, result.getValueDeserializer());
        assertNull(result.getNullValueProvider());
    }

    @Test
    public void testWithNullProvider() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        NullValueProvider nva = new NullValueProvider();

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        SettableBeanProperty result = property.withNullProvider(nva);

        assertSame(objectIdReader, result._objectIdReader);
        assertEquals(objectIdReader.propertyName, result.getName());
        assertEquals(objectIdReader.getIdType(), result.getType());
        assertSame(metadata, result.getMetadata());
        assertSame(objectIdReader.getDeserializer(), result.getValueDeserializer());
        assertSame(nva, result.getNullValueProvider());
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        assertNull(property.getAnnotation(null));
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSetNull() throws IOException {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        Object instance = new Object();

        property.deserializeAndSet(p, ctxt, instance);

        // Here you can add assertions to validate the behavior of the method
    }

    @Test
    public void testDeserializeAndSetNotNull() throws IOException {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        Object instance = new Object();

        Object result = property.deserializeSetAndReturn(p, ctxt, instance);

        // Here you can add assertions to validate the behavior of the method
        assertSame(instance, result);
    }

    @Test
    public void testSetNull() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        Object instance = new Object();

        property.set(instance, null);

        // Here you can add assertions to validate the behavior of the method
    }

    @Test
    public void testSetNotNull() throws IOException {
        ObjectIdValueProperty idProp = new ObjectIdValueProperty(null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(idProp, null);
        Object instance = new Object();

        Object result = property.setAndReturn(instance, null);

        // Here you can add assertions to validate the behavior of the method
        assertSame(instance, result);
    }
}
