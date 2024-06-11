package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdReader.IdType;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

    private ObjectIdReader mockObjectIdReader;
    private ObjectMapper mockObjectMapper;
    private JsonDeserializer<ObjectIdValueProperty> mockValueDeserializer;
    private IdType mockIdType;
    private ObjectIdValueProperty objectIdValueProperty;
    private SettableBeanProperty mockIdProperty;

    @Before
    public void setup() {
        mockObjectIdReader = mock(ObjectIdReader.class);
        mockObjectMapper = mock(ObjectMapper.class);
        mockValueDeserializer = mock(JsonDeserializer.class);
        mockIdType = mock(IdType.class);
        mockIdProperty = mock(SettableBeanProperty.class);

        when(mockObjectIdReader.propertyName).thenReturn(PropertyName.construct("id"));
        when(mockObjectIdReader.getIdType()).thenReturn(mockIdType);
        when(mockObjectIdReader.generator).thenReturn(mockObjectMapper);
        when(mockObjectIdReader.resolver).thenReturn(mockObjectMapper);

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(mockObjectIdReader, null);
        this.objectIdValueProperty = objectIdValueProperty;
    }

    @Test
    public void testInstanceCreation() {
        assertEquals("id", objectIdValueProperty.getName());
        assertEquals(mockIdType, objectIdValueProperty.getIdType());
        assertEquals(null, objectIdValueProperty.getMetadata());
        assertEquals(null, objectIdValueProperty.getDeserializer());
        assertEquals(mockObjectIdReader, objectIdValueProperty._objectIdReader);
    }

    @Test
    public void testCopyConstructor() {
        ObjectIdValueProperty source = new ObjectIdValueProperty(mockObjectIdReader, mockValueDeserializer);
        ObjectIdValueProperty dest = new ObjectIdValueProperty(source, null);

        assertEquals(source.getName(), dest.getName());
        assertEquals(source.getIdType(), dest.getIdType());
        assertEquals(source.getMetadata(), dest.getMetadata());
        assertEquals(source.getDeserializer(), dest.getDeserializer());
        assertEquals(source._objectIdReader, dest._objectIdReader);
    }

    @Test
    public void testNameSetter() {
        PropertyName newName = PropertyName.construct("newName");
        ObjectIdValueProperty dest = new ObjectIdValueProperty(objectIdValueProperty, newName);

        assertEquals(newName, dest.getName());
        assertEquals(objectIdValueProperty.getName(), objectIdValueProperty.getName());
    }

    @Test
    public void testValueDeserializerSetter() {
        JsonDeserializer<String> mockJsonDeserializer = mock(JsonDeserializer.class);
        ObjectIdValueProperty dest = new ObjectIdValueProperty(objectIdValueProperty, mockJsonDeserializer);

        assertEquals(mockValueDeserializer, objectIdValueProperty.getDeserializer());
        assertEquals(mockJsonDeserializer, dest.getDeserializer());
    }

    @Test
    public void testGetAnnotation() {
        assertNull(objectIdValueProperty.getAnnotation(JsonDeserialize.class));
    }

    @Test
    public void testGetMember() {
        assertNull(objectIdValueProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        when(mockValueDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn("123");

        Object id = objectIdValueProperty.deserializeAndSet(mockParser(), mockContext(), null);

        verify(mockObjectMapper).findObjectId(eq("123"), eq(mockObjectIdReader), any());
        assertNotNull(id);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        when(mockValueDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn("123");

        Object result = objectIdValueProperty.deserializeSetAndReturn(mockParser(), mockContext(), null);

        if (mockIdProperty != null) {
            verify(mockIdProperty).setAndReturn(eq(null), eq("123"));
        }
        assertNotNull(result);
    }

    @Test
    public void testSet() throws IOException {
        when(mockIdProperty.setAndReturn(any(), any())).thenReturn("456");

        Object result = objectIdValueProperty.setAndReturn(null, "123");

        assertEquals("456", result);
    }
}