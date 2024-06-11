package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.NameTransformer;

public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReaderMock;
    private JsonDeserializer<?> valueDeserializerMock;
    private JsonDeserializer<?> idDeserializerMock;
    private NameTransformer nameTransformerMock;
    private ObjectIdValueProperty objectIdValueProperty;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        objectIdReaderMock = mock(ObjectIdReader.class);
        valueDeserializerMock = mock(JsonDeserializer.class);
        idDeserializerMock = mock(JsonDeserializer.class);
        nameTransformerMock = mock(NameTransformer.class);

        when(objectIdReaderMock.propertyName).thenReturn("id");
        when(objectIdReaderMock.getIdType()).thenReturn(Integer.class);
        when(objectIdReaderMock.getDeserializer()).thenReturn(idDeserializerMock);
        when(objectIdReaderMock.generator).thenReturn(new StdSerializer<>((Class) null));
        when(objectIdReaderMock.resolver).thenReturn(new StdSerializer<>((Class) null));
        when(objectIdReaderMock.idProperty).thenReturn(new SettableBeanProperty(nameTransformerMock, null, null, null));

        objectIdValueProperty = new ObjectIdValueProperty(objectIdReaderMock, valueDeserializerMock);
    }

    @Test
    public void testConstructor() {
        assertSame(ObjectIdReader.class, objectIdValueProperty.getMember());
        assertSame(valueDeserializerMock, objectIdValueProperty.getValueDeserializer());
    }

    @Test
    public void testWithName() {
        PropertyName newName = mock(PropertyName.class);
        ObjectIdValueProperty propertyWithNewName = objectIdValueProperty.withName(newName);
        assertSame(newName, propertyWithNewName.getName());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> newValueDeserializer = mock(JsonDeserializer.class);
        ObjectIdValueProperty propertyWithNewValueDeserializer = objectIdValueProperty.withValueDeserializer(newValueDeserializer);
        assertSame(newValueDeserializer, propertyWithNewValueDeserializer.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nullValueProviderMock = mock(NullValueProvider.class);
        ObjectIdValueProperty propertyWithNullProvider = objectIdValueProperty.withNullProvider(nullValueProviderMock);
        assertSame(nullValueProviderMock, propertyWithNullProvider._nullProvider);
    }

    @Test
    public void testGetAnnotation() {
        assertNull(objectIdValueProperty.getAnnotation(Object.class));
    }

    @Test
    public void testGetMember() {
        assertSame(objectIdReaderMock, objectIdValueProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object instance = new Object();

        when(idDeserializerMock.deserialize(any(JsonParser.class), any(DeserializationContext.class)))
                .thenReturn(123);

        objectIdValueProperty.deserializeAndSet(mock(JsonParser.class), mock(DeserializationContext.class), instance);

        assertEquals(123, _objectIdReaderMock.getIdType().cast(instance.getClass().getDeclaredField("id").get(instance)));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object instance = new Object();

        when(valueDeserializerMock.deserialize(any(JsonParser.class), any(DeserializationContext.class)))
                .thenReturn("value");

        when(idDeserializerMock.deserialize(any(JsonParser.class), any(DeserializationContext.class)))
                .thenReturn(123);

        when(nameTransformerMock.transformFromPropertyName("id")).thenReturn("newName");
        when(objectIdReaderMock.getIdType()).thenReturn(String.class);

        Object result = objectIdValueProperty.deserializeSetAndReturn(mock(JsonParser.class), mock(DeserializationContext.class), instance);

        assertEquals("newName", instance.getClass().getDeclaredField("id").get(instance));
        assertEquals("value", result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetNull() throws IOException {
        Object instance = new Object();
        objectIdValueProperty.set(instance, null);
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        SettableBeanProperty idPropMock = mock(SettableBeanProperty.class);

        when(idPropMock.setAndReturn(instance, value)).thenReturn(instance);

        when(objectIdReaderMock.idProperty).thenReturn(idPropMock);

        Object result = objectIdValueProperty.setAndReturn(instance, value);

        assertEquals(instance, result);
    }

}