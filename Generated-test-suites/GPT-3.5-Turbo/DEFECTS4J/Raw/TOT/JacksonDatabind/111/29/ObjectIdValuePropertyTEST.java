
package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

@RunWith(MockitoJUnitRunner.class)
public class ObjectIdValuePropertyTest {

    @Mock
    private ObjectIdReader objectIdReader;

    @Mock
    private JsonDeserializer<?> deserializer;

    @Mock
    private NullValueProvider nullValueProvider;

    @Mock
    private JsonParser jsonParser;

    @Mock
    private DeserializationContext deserializationContext;

    @Captor
    private ArgumentCaptor<Object> instanceCaptor;

    private final PropertyMetadata metadata = PropertyMetadata.STD_REQUIRED;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testConstructor() {
        // Arrange
        String propertyName = "objectIdProperty";
        Class<?> idType = Long.class;

        // Act
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        // Assert
        assertEquals(propertyName, property.getName());
        assertEquals(idType, property.getType());
        assertEquals(metadata, property.getMetadata());
    }

    @Test
    public void testCopyConstructorWithDeserializerAndNullValueProvider() {
        // Arrange
        ObjectIdValueProperty srcProperty = new ObjectIdValueProperty(objectIdReader, metadata);

        // Act
        ObjectIdValueProperty property = new ObjectIdValueProperty(srcProperty, deserializer,
                nullValueProvider);

        // Assert
        assertSame(srcProperty._objectIdReader, property._objectIdReader);
        assertSame(deserializer, property._valueDeserializer);
        assertSame(nullValueProvider, property._nullProvider);
    }

    @Test
    public void testCopyConstructorWithNewName() {
        // Arrange
        ObjectIdValueProperty srcProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        PropertyName newName = new PropertyName("newProperty");

        // Act
        ObjectIdValueProperty property = new ObjectIdValueProperty(srcProperty, newName);

        // Assert
        assertEquals(newName, property._propName);
        assertEquals(srcProperty._objectIdReader, property._objectIdReader);
        assertEquals(srcProperty._valueDeserializer, property._valueDeserializer);
        assertEquals(srcProperty._nullProvider, property._nullProvider);
    }

    @Test
    public void testWithNullProvider() {
        // Arrange
        ObjectIdValueProperty srcProperty = new ObjectIdValueProperty(objectIdReader, metadata);

        // Act
        ObjectIdValueProperty property = (ObjectIdValueProperty) srcProperty
                .withNullProvider(nullValueProvider);

        // Assert
        assertSame(srcProperty._objectIdReader, property._objectIdReader);
        assertSame(srcProperty._valueDeserializer, property._valueDeserializer);
        assertSame(nullValueProvider, property._nullProvider);
    }

    @Test
    public void testWithNullValueDeserializerSameDeserializer() {
        // Arrange
        ObjectIdValueProperty srcProperty = new ObjectIdValueProperty(objectIdReader, metadata);

        // Act
        ObjectIdValueProperty property = (ObjectIdValueProperty) srcProperty
                .withValueDeserializer(deserializer);

        // Assert
        assertSame(srcProperty, property);
    }

    @Test
    public void testWithNullValueDeserializerDifferentDeserializer() {
        // Arrange
        ObjectIdValueProperty srcProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        JsonDeserializer<?> newDeserializer = mock(JsonDeserializer.class);

        // Act
        ObjectIdValueProperty property = (ObjectIdValueProperty) srcProperty
                .withValueDeserializer(newDeserializer);

        // Assert
        assertSame(newDeserializer, property._valueDeserializer);
        assertSame(srcProperty._objectIdReader, property._objectIdReader);
        assertSame(srcProperty._nullProvider, property._nullProvider);
    }

    @Test
    public void testGetAnnotation() {
        // Arrange
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        // Act
        Object result = property.getAnnotation(Override.class);

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetMember() {
        // Arrange
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        // Act
        AnnotatedMember result = property.getMember();

        // Assert
        assertNull(result);
    }

    @Test
    public void testDeserializeAndSetWithNullValue() throws IOException {
        // Arrange
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        // Act
        Object result = property.deserializeSetAndReturn(jsonParser, deserializationContext, new Object());

        // Assert
        assertNull(result);
    }

    @Test
    public void testDeserializeAndSetWithValue() throws IOException {
        // Arrange
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        Object value = new Object();

        // Act
        Object result = property.deserializeSetAndReturn(jsonParser, deserializationContext, value);

        // Assert
        assertSame(value, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturnWithNullIdPropertyShouldThrowException() throws IOException {
        // Arrange
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        // Act
        property.setAndReturn(new Object(), new Object());
    }
}

