package com.fasterxml.jackson.databind.deser.impl.test;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for ObjectIdValueProperty
 */
public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReaderMock;
    private ObjectIdValueProperty objectIdValueProperty;
    private JsonDeserializer<ObjectIdNode> deserializerMock;

    @Before
    public void setup() {
        objectIdReaderMock = mock(ObjectIdReader.class);
        when(objectIdReaderMock.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(objectIdReaderMock.resolver).thenReturn(mock(ObjectIdResolver.class));
        deserializerMock = mock(JsonDeserializer.class);
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReaderMock, new PropertyMetadata());
        objectIdValueProperty.setValueDeserializer(deserializerMock);
    }

    // Test for constructor
    @Test
    public void testConstructor() {
        ObjectIdValueProperty actualObjectIdValueProperty = new ObjectIdValueProperty(objectIdReaderMock, new PropertyMetadata());
        assertEquals(objectIdReaderMock, actualObjectIdValueProperty._objectIdReader);
    }

    // Test for getter methods
    @Test
    public void testGetters() {
        assertEquals(objectIdReaderMock.propertyName, objectIdValueProperty.getName());
        assertEquals(objectIdReaderMock.getIdType(), objectIdValueProperty.getComponentType());
        assertEquals(objectIdReaderMock.getDeserializer(), objectIdValueProperty.getValueDeserializer());
        assertEquals(objectIdReaderMock._objectIdReader, objectIdValueProperty._objectIdReader);
    }

    // Test for deprecated constructors and methods
    @Test(expected = UnsupportedOperationException.class)
    public void testDeprecatedConstructorsAndMethods() {
        ObjectIdValueProperty src = objectIdValueProperty;
        ObjectIdValueProperty newObjectIdValueProperty1 = new ObjectIdValueProperty(src, PropertyName.construct("newName"));
        ObjectIdValueProperty newObjectIdValueProperty2 = new ObjectIdValueProperty(src, "newName");
        ObjectIdValueProperty newObjectIdValueProperty3 = new ObjectIdValueProperty(src, new PropertyName("newName"));

        ObjectIdValueProperty newObjectIdValueProperty4 = new ObjectIdValueProperty(src, new PropertyName("newName"), deserializerMock);
        ObjectIdValueProperty newObjectIdValueProperty5 = new ObjectIdValueProperty(src, new PropertyName("newName"), new JsonDeserializer<>());

        newObjectIdValueProperty1.withName(PropertyName.construct("newName2"));
        newObjectIdValueProperty1.withValueDeserializer(deserializerMock);

        newObjectIdValueProperty4.withName(PropertyName.construct("newName2"));
        newObjectIdValueProperty4.withValueDeserializer(deserializerMock);
    }

    // Test for getAnnotation method
    @Test
    public void testGetAnnotation() {
        assertNull(objectIdValueProperty.getAnnotation(Annotation.class));
    }

    // Test for getMember method
    @Test
    public void testGetMember() {
        assertNull(objectIdValueProperty.getMember());
    }

    // Test for deserializeAndSet method
    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdNode objectIdNode = ObjectIdNode.valueOf("56");
        doReturn(objectIdNode).when(deserializerMock).deserialize(any(JsonParser.class), eq(mock(DeserializationContext.class)));

        when(objectIdReaderMock.idProperty).thenReturn(mock(SettableBeanProperty.class));
        when(objectIdReaderMock.idProperty.setAndReturn(any(Object.class), any(Object.class))).thenReturn("idSet");

        Object id = objectIdValueProperty.deserializeAndSet(mock(JsonParser.class), mock(DeserializationContext.class), mock(Object.class));
        assertEquals("idSet", id);
    }

    // Test for deserializeSetAndReturn method
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdNode objectIdNode = ObjectIdNode.valueOf("56");
        doReturn(objectIdNode).when(deserializerMock).deserialize(any(JsonParser.class), eq(mock(DeserializationContext.class)));

        ObjectIdGenerator objectIdGeneratorMock = mock(ObjectIdGenerator.class);
        ObjectIdResolver objectIdResolverMock = mock(ObjectIdResolver.class);

        when(objectIdReaderMock.generator).thenReturn(objectIdGeneratorMock);
        when(objectIdReaderMock.resolver).thenReturn(objectIdResolverMock);

        when(objectIdGeneratorMock.generateId()).thenReturn("generatedId");
        when(objectIdResolverMock.findObjectId(any(Object.class), any(ObjectIdGenerator.class), any(ObjectIdResolver.class))).thenReturn(mock(ReadableObjectId.class));

        Object id = objectIdValueProperty.deserializeSetAndReturn(mock(JsonParser.class), mock(DeserializationContext.class), mock(Object.class));
        assertEquals("generatedId", id);
    }

    // Test for set method
    @Test
    public void testSet() throws IOException {
        when(objectIdReaderMock.idProperty).thenReturn(mock(SettableBeanProperty.class));
        doNothing().when(objectIdReaderMock.idProperty).setAndReturn(any(Object.class), any(Object.class));

        objectIdValueProperty.set(mock(Object.class), "idSet");
        verify(objectIdReaderMock.idProperty).setAndReturn(mock(Object.class), "idSet");
    }

    // Test for setAndReturn method
    @Test
    public void testSetAndReturn() throws IOException {
        when(objectIdReaderMock.idProperty).thenReturn(mock(SettableBeanProperty.class));
        when(objectIdReaderMock.idProperty.setAndReturn(any(Object.class), any(Object.class))).thenReturn("idSet");

        Object id = objectIdValueProperty.setAndReturn(mock(Object.class), "idSet");
        assertEquals("idSet", id);
    }

    // Test for setAndReturn method with null value
    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturnWithNullValue() throws IOException {
        when(objectIdReaderMock.idProperty).thenReturn(mock(SettableBeanProperty.class));
        doThrow(new UnsupportedOperationException("Should not call set() on ObjectIdProperty that has no SettableBeanProperty")).when(objectIdReaderMock.idProperty).setAndReturn(any(Object.class), null);

        objectIdValueProperty.setAndReturn(mock(Object.class), null);
    }

    // Test for deserializeAndSet method with null id
    @Test
    public void testDeserializeAndSetWithNullId() throws IOException {
        when(objectIdReaderMock.idProperty).thenReturn(mock(SettableBeanProperty.class));

        ObjectIdGenerator objectIdGeneratorMock = mock(ObjectIdGenerator.class);
        ObjectIdResolver objectIdResolverMock = mock(ObjectIdResolver.class);
        when(objectIdReaderMock.generator).thenReturn(objectIdGeneratorMock);
        when(objectIdReaderMock.resolver).thenReturn(objectIdResolverMock);

        doReturn(null).when(deserializerMock).deserialize(any(JsonParser.class), eq(mock(DeserializationContext.class)));

        objectIdValueProperty.deserializeAndSet(mock(JsonParser.class), mock(DeserializationContext.class), mock(Object.class));
        verifyNoMoreInteractions(objectIdGeneratorMock);
        verifyNoMoreInteractions(objectIdResolverMock);
    }

    // Test for deserializeSetAndReturn method with null id
    @Test
    public void testDeserializeSetAndReturnWithNullId() throws IOException {
        when(objectIdReaderMock.idProperty).thenReturn(mock(SettableBeanProperty.class));

        ObjectIdGenerator objectIdGeneratorMock = mock(ObjectIdGenerator.class);
        ObjectIdResolver objectIdResolverMock = mock(ObjectIdResolver.class);
        when(objectIdReaderMock.generator).thenReturn(objectIdGeneratorMock);
        when(objectIdReaderMock.resolver).thenReturn(objectIdResolverMock);

        doReturn(null).when(deserializerMock).deserialize(any(JsonParser.class), eq(mock(DeserializationContext.class)));

        Object id = objectIdValueProperty.deserializeSetAndReturn(mock(JsonParser.class), mock(DeserializationContext.class), mock(Object.class));
        assertEquals(null, id);
    }

    // Test for setAndReturn method with null value and null id
    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturnWithNullValueAndNullId() throws IOException {
        when(objectIdReaderMock.idProperty).thenReturn(mock(SettableBeanProperty.class));
        doThrow(new UnsupportedOperationException("Should not call set() on ObjectIdProperty that has no SettableBeanProperty")).when(objectIdReaderMock.idProperty).setAndReturn(any(Object.class), null);

        objectIdValueProperty.setAndReturn(mock(Object.class), null);
    }
}