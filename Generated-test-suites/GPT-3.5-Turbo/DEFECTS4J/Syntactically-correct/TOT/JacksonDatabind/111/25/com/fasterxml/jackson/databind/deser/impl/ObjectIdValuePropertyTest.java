
package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

@RunWith(MockitoJUnitRunner.class)
public class ObjectIdValuePropertyTest {

    @Mock
    private JsonParser mockJsonParser;

    @Mock
    private DeserializationContext mockDeserializationContext;

    @Test
    public void testDeserializeAndSet_NullTokenReturnsNull() throws Exception {
        // Arrange
        ObjectIdValueProperty property = createObjectIdValueProperty();
        Mockito.when(mockJsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);

        // Act
        Object result = property.deserializeSetAndReturn(mockJsonParser, mockDeserializationContext, null);

        // Assert
        assertNull(result);
    }

    @Test
    public void testDeserializeAndSet_ObjectIdExistsAndIsBoundToItem_ReturnsInstance() throws Exception {
        // Arrange
        ObjectIdValueProperty property = createObjectIdValueProperty();
        Object instance = new Object();
        Object id = new Object();
        ReadableObjectId roid = new ReadableObjectId();
        Mockito.when(mockJsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        Mockito.when(property._valueDeserializer.deserialize(mockJsonParser, mockDeserializationContext)).thenReturn(id);
        Mockito.when(mockDeserializationContext.findObjectId(id, property._objectIdReader.generator, property._objectIdReader.resolver)).thenReturn(roid);
        Mockito.when(property._objectIdReader.idProperty).thenReturn(null);

        // Act
        Object result = property.deserializeSetAndReturn(mockJsonParser, mockDeserializationContext, instance);

        // Assert
        assertSame(instance, result);
        assertSame(instance, roid.item);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn_IdPropertyIsNull_ThrowsException() throws Exception {
        // Arrange
        ObjectIdValueProperty property = createObjectIdValueProperty();
        Object instance = new Object();
        Object value = new Object();
        Mockito.when(property._objectIdReader.idProperty).thenReturn(null);

        // Act
        property.setAndReturn(instance, value);
    }

    private ObjectIdValueProperty createObjectIdValueProperty() {
        ObjectIdReader objectIdReader = null; // provide the necessary object
        PropertyMetadata metadata = null; // provide the necessary object
        return new ObjectIdValueProperty(objectIdReader, metadata);
    }
}
