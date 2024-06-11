package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class ObjectIdValuePropertyTest {

    @Test
    public void testDeserializeAndSet() throws IOException, JsonProcessingException {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());

        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = new Object() {};

        Object id = new Object() {};
        ReadableObjectId readableObjectId = mock(ReadableObjectId.class);
        when(readableObjectId.bindItem(instance)).thenReturn(instance);

        Object idProperty = mock(SettableBeanProperty.class);

        when(objectIdReader.idProperty).thenReturn(idProperty);

        when(jsonParser.getValueAsToken()).thenReturn(new ObjectIdToken("123"));
        Object result = property.deserializeAndSet(jsonParser, deserializationContext, instance);
        verify(deserializationContext).findObjectId(eq("123"), any(), any());
        verify(idProperty).setAndReturn(instance, id);
        Assert.assertEquals(instance, result);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException, JsonProcessingException {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());

        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = new Object() {};

        Object id = new Object() {};
        ReadableObjectId readableObjectId = mock(ReadableObjectId.class);
        when(readableObjectId.bindItem(instance)).thenReturn(instance);

        Object idProperty = mock(SettableBeanProperty.class);

        when(objectIdReader.idProperty).thenReturn(idProperty);

        when(jsonParser.getValueAsToken()).thenReturn(new ObjectIdToken("123"));
        Object result = property.deserializeSetAndReturn(jsonParser, deserializationContext, instance);
        verify(deserializationContext).findObjectId(eq("123"), any(), any());
        verify(idProperty).setAndReturn(instance, id);
        Assert.assertEquals(instance, result);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());

        Object instance = new Object() {};
        Object value = new Object() {};

        SettableBeanProperty idProperty = mock(SettableBeanProperty.class);

        when(objectIdReader.idProperty).thenReturn(idProperty);

        Object result = property.setAndReturn(instance, value);
        verify(idProperty).setAndReturn(instance, value);
        Assert.assertEquals(instance, result);
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, PropertyMetadata.empty());
        Assert.assertNull(property.getAnnotation(Object.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, PropertyMetadata.empty());
        Assert.assertNull(property.getMember());
    }
}