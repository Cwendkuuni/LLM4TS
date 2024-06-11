
package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdReader;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.annotation.Annotation;

public class ObjectIdValuePropertyTest {

    private ObjectIdValueProperty objectIdValueProperty;

    @Mock
    private ObjectIdReader objectIdReader;

    @Mock
    private PropertyMetadata propertyMetadata;

    @Mock
    private JsonDeserializer jsonDeserializer;

    @Mock
    private NullValueProvider nullValueProvider;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
    }

    @Test
    public void testConstructor() {
        assertEquals(objectIdReader.propertyName, objectIdValueProperty.getName());
        assertEquals(objectIdReader.getIdType(), objectIdValueProperty.getType());
        assertEquals(propertyMetadata, objectIdValueProperty.getMetadata());
        assertEquals(objectIdReader.getDeserializer(), objectIdValueProperty.getValueDeserializer());
        assertEquals(objectIdReader, objectIdValueProperty._objectIdReader);
    }

    @Test
    public void testWithNullProvider() {
        ObjectIdValueProperty newProperty = (ObjectIdValueProperty) objectIdValueProperty.withNullProvider(nullValueProvider);
        assertNull(newProperty._valueDeserializer);
        assertEquals(objectIdValueProperty._nullProvider, nullValueProvider);
    }

    @Test
    public void testWithNullDeserializer() {
        ObjectIdValueProperty newProperty = (ObjectIdValueProperty) objectIdValueProperty.withValueDeserializer(jsonDeserializer);
        assertEquals(jsonDeserializer, newProperty.getValueDeserializer());
        assertNull(newProperty._nullProvider);
        assertEquals(objectIdValueProperty._objectIdReader, newProperty._objectIdReader);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = new Object();

        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);

        assertNull(objectIdValueProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance));

        verify(jsonParser).hasToken(JsonToken.VALUE_NULL);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = new Object();
        Object id = new Object();

        when(jsonDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(id);

        assertNull(objectIdValueProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance));

        verify(jsonDeserializer).deserialize(jsonParser, deserializationContext);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturnWithoutIdProperty() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        objectIdValueProperty.setAndReturn(instance, value);
    }
}
