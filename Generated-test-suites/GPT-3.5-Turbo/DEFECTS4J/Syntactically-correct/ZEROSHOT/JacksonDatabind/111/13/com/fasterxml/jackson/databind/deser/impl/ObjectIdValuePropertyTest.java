package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

    @Mock
    ObjectIdReader objectIdReaderMock;

    @Mock
    PropertyMetadata propertyMetadataMock;

    @Mock
    JsonDeserializer<?> jsonDeserializerMock;

    @Mock
    NullValueProvider nullValueProviderMock;

    @Test
    public void testConstructor1() {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReaderMock, propertyMetadataMock);

        assertEquals(objectIdReaderMock.propertyName, objectIdValueProperty.getPropertyName());
        assertEquals(objectIdReaderMock.getIdType(), objectIdValueProperty.getType());
        assertEquals(propertyMetadataMock, objectIdValueProperty.getMetadata());
        assertEquals(objectIdReaderMock.getDeserializer(), objectIdValueProperty.getDeserializer());
        assertEquals(objectIdReaderMock, objectIdValueProperty.getObjectIdReader());
    }

    @Test
    public void testConstructor2() {
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReaderMock, propertyMetadataMock);
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(src, jsonDeserializerMock, nullValueProviderMock);

        assertEquals(src.getPropertyName(), objectIdValueProperty.getPropertyName());
        assertEquals(src.getType(), objectIdValueProperty.getType());
        assertEquals(src.getMetadata(), objectIdValueProperty.getMetadata());
        assertEquals(jsonDeserializerMock, objectIdValueProperty.getDeserializer());
        assertEquals(src.getObjectIdReader(), objectIdValueProperty.getObjectIdReader());
    }

    @Test
    public void testConstructor3() {
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReaderMock, propertyMetadataMock);
        PropertyName newName = new PropertyName("newName");
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(src, newName);

        assertEquals(newName, objectIdValueProperty.getPropertyName());
        assertEquals(src.getType(), objectIdValueProperty.getType());
        assertEquals(src.getMetadata(), objectIdValueProperty.getMetadata());
        assertEquals(src.getDeserializer(), objectIdValueProperty.getDeserializer());
        assertEquals(src.getObjectIdReader(), objectIdValueProperty.getObjectIdReader());
    }

    @Test
    public void testWithNullDeserializerReturnsSame() {
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReaderMock, propertyMetadataMock);
        ObjectIdValueProperty objectIdValueProperty = (ObjectIdValueProperty) src.withValueDeserializer(null);

        assertEquals(src, objectIdValueProperty);
    }

    @Test
    public void testWithEqualDeserializerReturnsSame() {
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReaderMock, propertyMetadataMock);
        ObjectIdValueProperty objectIdValueProperty = (ObjectIdValueProperty) src.withValueDeserializer(jsonDeserializerMock);

        assertNotSame(src, objectIdValueProperty);
        assertEquals(src.getPropertyName(), objectIdValueProperty.getPropertyName());
        assertEquals(src.getType(), objectIdValueProperty.getType());
        assertEquals(src.getMetadata(), objectIdValueProperty.getMetadata());
        assertEquals(jsonDeserializerMock, objectIdValueProperty.getDeserializer());
        assertEquals(src.getObjectIdReader(), objectIdValueProperty.getObjectIdReader());
    }

    @Test
    public void testWithDifferentDeserializerReturnsDifferent() {
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReaderMock, propertyMetadataMock);
        ObjectIdValueProperty objectIdValueProperty = (ObjectIdValueProperty) src.withValueDeserializer(Mockito.mock(JsonDeserializer.class));

        assertNotSame(src, objectIdValueProperty);
        assertEquals(src.getPropertyName(), objectIdValueProperty.getPropertyName());
        assertEquals(src.getType(), objectIdValueProperty.getType());
        assertEquals(src.getMetadata(), objectIdValueProperty.getMetadata());
        assertNotSame(src.getDeserializer(), objectIdValueProperty.getDeserializer());
        assertEquals(src.getObjectIdReader(), objectIdValueProperty.getObjectIdReader());
    }

    @Test
    public void testWithNullNullValueProviderReturnsSame() {
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReaderMock, propertyMetadataMock);
        ObjectIdValueProperty objectIdValueProperty = (ObjectIdValueProperty) src.withNullProvider(null);

        assertEquals(src, objectIdValueProperty);
    }

    @Test
    public void testWithNonNullNullValueProviderReturnsDifferent() {
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReaderMock, propertyMetadataMock);
        ObjectIdValueProperty objectIdValueProperty = (ObjectIdValueProperty) src.withNullProvider(nullValueProviderMock);

        assertNotSame(src, objectIdValueProperty);
        assertEquals(src.getPropertyName(), objectIdValueProperty.getPropertyName());
        assertEquals(src.getType(), objectIdValueProperty.getType());
        assertEquals(src.getMetadata(), objectIdValueProperty.getMetadata());
        assertEquals(src.getDeserializer(), objectIdValueProperty.getDeserializer());
        assertEquals(src.getObjectIdReader(), objectIdValueProperty.getObjectIdReader());
        assertEquals(nullValueProviderMock, objectIdValueProperty.getNullValueProvider());
    }

    @Test
    public void testGetAnnotationReturnsNull() {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReaderMock, propertyMetadataMock);

        assertNull(objectIdValueProperty.getAnnotation(Mockito.any(Class.class)));
    }

    @Test
    public void testGetMemberReturnsNull() {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReaderMock, propertyMetadataMock);

        assertNull(objectIdValueProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReaderMock, propertyMetadataMock);

        assertNull(objectIdValueProperty.deserializeSetAndReturn(Mockito.mock(JsonParser.class), Mockito.mock(DeserializationContext.class), new Object()));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetThrowsUnsupportedOperationException() throws IOException {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReaderMock, propertyMetadataMock);

        objectIdValueProperty.set(new Object(), new Object());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturnThrowsUnsupportedOperationException() throws IOException {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReaderMock, propertyMetadataMock);

        objectIdValueProperty.setAndReturn(new Object(), new Object());
    }
}