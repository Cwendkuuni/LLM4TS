package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

public class ObjectIdValuePropertyTest {
    @Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        PropertyMetadata metadata = Mockito.mock(PropertyMetadata.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        assertSame(objectIdReader.propertyName, property._objectIdReader.propertyName);
        assertSame(objectIdReader.getIdType(), property._objectIdReader.getIdType());
        assertSame(objectIdReader.getDeserializer(), property._objectIdReader.getDeserializer());
    }

    @Test
    public void testCopyConstructor() {
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        PropertyMetadata metadata = Mockito.mock(PropertyMetadata.class);
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        JsonDeserializer<?> deser = Mockito.mock(JsonDeserializer.class);
        NullValueProvider nva = Mockito.mock(NullValueProvider.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, deser, nva);

        assertSame(src._objectIdReader, property._objectIdReader);
        assertSame(deser, property._valueDeserializer);
        assertSame(nva, property._nullProvider);
    }

    @Test
    public void testCopyConstructorWithPropertyName() {
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        PropertyMetadata metadata = Mockito.mock(PropertyMetadata.class);
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        PropertyName newName = Mockito.mock(PropertyName.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, newName);

        assertSame(src._objectIdReader, property._objectIdReader);
        assertSame(newName, property._propertyName);
    }

    @Test
    public void testWithName() {
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        PropertyMetadata metadata = Mockito.mock(PropertyMetadata.class);
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        PropertyName newName = Mockito.mock(PropertyName.class);
        ObjectIdValueProperty property = (ObjectIdValueProperty) src.withName(newName);

        assertSame(src._objectIdReader, property._objectIdReader);
        assertSame(newName, property._propertyName);
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        PropertyMetadata metadata = Mockito.mock(PropertyMetadata.class);
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        JsonDeserializer<?> deser = Mockito.mock(JsonDeserializer.class);
        ObjectIdValueProperty property = (ObjectIdValueProperty) src.withValueDeserializer(deser);

        assertSame(src._objectIdReader, property._objectIdReader);
        assertSame(deser, property._valueDeserializer);
        assertNull(property._nullProvider);
    }

    @Test
    public void testWithNullProvider() {
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        PropertyMetadata metadata = Mockito.mock(PropertyMetadata.class);
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        NullValueProvider nva = Mockito.mock(NullValueProvider.class);
        ObjectIdValueProperty property = (ObjectIdValueProperty) src.withNullProvider(nva);

        assertSame(src._objectIdReader, property._objectIdReader);
        assertSame(nva, property._nullProvider);
        assertNull(property._valueDeserializer);
    }

    @Test
    public void testGetAnnotation() {
        PropertyMetadata metadata = Mockito.mock(PropertyMetadata.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, metadata);

        assertNull(property.getAnnotation(null));
    }

    @Test
    public void testGetMember() {
        PropertyMetadata metadata = Mockito.mock(PropertyMetadata.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, metadata);

        assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSetWithValueNull() throws Exception {
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        PropertyMetadata metadata = Mockito.mock(PropertyMetadata.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        JsonParser p = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object instance = new Object();

        Mockito.when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);

        Object result = property.deserializeAndSet(p, ctxt, instance);

        assertNull(result);
        assertEquals(instance, result);
    }

    @Test
    public void testDeserializeAndSetWithNoIdProperty() throws Exception {
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        PropertyMetadata metadata = Mockito.mock(PropertyMetadata.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        JsonParser p = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object instance = new Object();
        Object id = new Object();
        ReadableObjectId roid = Mockito.mock(ReadableObjectId.class);

        Mockito.when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        Mockito.when(property._valueDeserializer.deserialize(p, ctxt)).thenReturn(id);
        Mockito.when(ctxt.findObjectId(id, objectIdReader.generator, objectIdReader.resolver)).thenReturn(roid);

        Object result = property.deserializeAndSet(p, ctxt, instance);

        assertSame(instance, result);
        Mockito.verify(roid).bindItem(instance);
        assertNull(property._objectIdReader.idProperty);
    }

    @Test
    public void testDeserializeAndSetWithIdProperty() throws Exception {
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        PropertyMetadata metadata = Mockito.mock(PropertyMetadata.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        JsonParser p = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object instance = new Object();
        Object id = new Object();
        ReadableObjectId roid = Mockito.mock(ReadableObjectId.class);
        SettableBeanProperty idProp = Mockito.mock(SettableBeanProperty.class);

        property._objectIdReader.idProperty = idProp;
        Mockito.when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        Mockito.when(property._valueDeserializer.deserialize(p, ctxt)).thenReturn(id);
        Mockito.when(ctxt.findObjectId(id, objectIdReader.generator, objectIdReader.resolver)).thenReturn(roid);
        Mockito.when(idProp.setAndReturn(instance, id)).thenReturn(instance);

        Object result = property.deserializeAndSet(p, ctxt, instance);

        assertSame(instance, result);
        Mockito.verify(roid).bindItem(instance);
        assertSame(instance, result);
        Mockito.verify(roid).bindItem(instance);
        Mockito.verify(idProp).setAndReturn(instance, id);
    }

    @Test
    public void testSetWithNoIdProperty() throws Exception {
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        PropertyMetadata metadata = Mockito.mock(PropertyMetadata.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        Object instance = new Object();
        Object value = new Object();

        property._objectIdReader.idProperty = null;

        try {
            property.set(instance, value);
        } catch (UnsupportedOperationException e) {
            assertEquals("Should not call set() on ObjectIdProperty that has no SettableBeanProperty", e.getMessage());
        }
    }

    @Test
    public void testSetWithIdProperty() throws Exception {
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        PropertyMetadata metadata = Mockito.mock(PropertyMetadata.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        Object instance = new Object();
        Object value = new Object();
        SettableBeanProperty idProp = Mockito.mock(SettableBeanProperty.class);

        property._objectIdReader.idProperty = idProp;
        Mockito.when(idProp.setAndReturn(instance, value)).thenReturn(instance);

        Object result = property.setAndReturn(instance, value);

        assertSame(instance, result);
        Mockito.verify(idProp).setAndReturn(instance, value);
    }
}