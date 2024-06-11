package com.fasterxml.jackson.databind.deser.impl.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.NameTransformer;

import org.junit.Test;

/**
 * Test cases for ObjectIdValueProperty class
 */
public class ObjectIdValuePropertyTest {

    @Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        assertEquals("propertyName", property.getName());
        assertEquals(ObjectIdReader.class, property.getMember().getType());
        assertEquals(objectIdReader, property._objectIdReader);
    }

    @Test
    public void testWithName() {
        ObjectIdValueProperty src = mock(ObjectIdValueProperty.class);
        PropertyName newName = mock(PropertyName.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, newName);
        assertEquals(newName, property.getName());
        assertEquals(src, property.getBackingBean());
        assertEquals(src._objectIdReader, property._objectIdReader);
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdValueProperty src = mock(ObjectIdValueProperty.class);
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, deser, null);
        assertEquals(deser, property._valueDeserializer);
        assertEquals(src, property.getBackingBean());
        assertEquals(src._objectIdReader, property._objectIdReader);
    }

    @Test
    public void testWithNullProvider() {
        ObjectIdValueProperty src = mock(ObjectIdValueProperty.class);
        NullValueProvider nva = mock(NullValueProvider.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, null, nva);
        assertEquals(nva, property._nullProvider);
        assertEquals(src, property.getBackingBean());
        assertEquals(src._objectIdReader, property._objectIdReader);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        Object instance = new Object();
        JsonParser parser = mock(JsonParser.class);
        when(parser.getCurrentName()).thenReturn("idPropertyName");
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.getValueAsString()).thenReturn("123");
        DeserializationContext context = mock(DeserializationContext.class);
        when(context.getAnnotationInstance(ObjectIdReader.class)).thenReturn(objectIdReader);
        when(context.findBeanProperty("idPropertyName")).thenReturn(property);
        Object id = property.deserializeAndSet(parser, context, instance);
        assertEquals("123", id);
    }

    @Test
    public void testDeserializeSetAndReturnNull() throws IOException {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        Object instance = new Object();
        JsonParser parser = mock(JsonParser.class);
        when(parser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        DeserializationContext context = mock(DeserializationContext.class);
        Object id = property.deserializeSetAndReturn(parser, context, instance);
        assertNull(id);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetWithoutIdProperty() throws IOException {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        Object instance = new Object();
        property.set(instance, "id");
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        Object instance = new Object();
        Object id = new Object();
        SettableBeanProperty idProp = mock(SettableBeanProperty.class);
        when(idProp.getName()).thenReturn("idPropertyName");
        when(idProp.setAndReturn(instance, id)).thenReturn(instance);
        when(property._objectIdReader.idProperty).thenReturn(idProp);
        Object result = property.setAndReturn(instance, id);
        assertEquals(instance, result);
    }
}