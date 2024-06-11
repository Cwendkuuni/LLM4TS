package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectIdGenerator;
import com.fasterxml.jackson.databind.ObjectIdWriter;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleValueSerializer;
import com.fasterxml.jackson.databind.ser.std.StdObjectIdGenerator;

/**
 * Test class for WritableObjectId.java
 *
 * @author John Doe
 */
public class WritableObjectIdTest {

    private JsonGenerator jsonGeneratorMock;
    private SerializerProvider serializerProviderMock;
    private ObjectIdGenerator<Object> objectIdGeneratorMock;
    private ObjectIdWriter objectIdWriterMock;

    @Before
    public void setup() {
        jsonGeneratorMock = Mockito.mock(JsonGenerator.class);
        serializerProviderMock = Mockito.mock(SerializerProvider.class);
        objectIdGeneratorMock = Mockito.spy(new StdObjectIdGenerator());
        objectIdWriterMock = Mockito.mock(ObjectIdWriter.class);
    }

    @Test
    public void testWriteAsId() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(objectIdGeneratorMock);
        Object id = "testId";

        Mockito.when(jsonGeneratorMock.canWriteObjectId()).thenReturn(true);
        Mockito.when(jsonGeneratorMock.writeObjectRef(Mockito.anyString())).thenReturn(writableObjectId);
        Mockito.when(objectIdGeneratorMock.generateId(Mockito.any())).thenReturn(id);

        assertTrue(writableObjectId.writeAsId(jsonGeneratorMock, serializerProviderMock, objectIdWriterMock));
        Mockito.verify(jsonGeneratorMock).writeObjectRef(Mockito.eq(String.valueOf(id)));
    }

    @Test
    public void testWriteAsId_CannotWriteObjectId() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(objectIdGeneratorMock);

        Mockito.when(jsonGeneratorMock.canWriteObjectId()).thenReturn(false);
        Mockito.when(objectIdGeneratorMock.generateId(Mockito.any())).thenReturn(null);

        assertFalse(writableObjectId.writeAsId(jsonGeneratorMock, serializerProviderMock, objectIdWriterMock));

        Mockito.verifyNoMoreInteractions(jsonGeneratorMock, objectIdGeneratorMock);
    }

    @Test(expected = JsonMappingException.class)
    public void testWriteAsId_NullIdGenerator() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(null);

        Mockito.when(jsonGeneratorMock.canWriteObjectId()).thenReturn(true);

        writableObjectId.writeAsId(jsonGeneratorMock, serializerProviderMock, objectIdWriterMock);

        fail("Expected JsonMappingException but got no exception");
    }

    @Test
    public void testGenerateId() {
        WritableObjectId writableObjectId = new WritableObjectId(objectIdGeneratorMock);
        Object testObject = new Object();

        Mockito.when(objectIdGeneratorMock.generateId(Mockito.eq(testObject))).thenReturn("testId");

        assertEquals("testId", writableObjectId.generateId(testObject));

        Mockito.verify(objectIdGeneratorMock).generateId(Mockito.eq(testObject));
    }

    @Test
    public void testWriteAsField() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(objectIdGeneratorMock);

        Mockito.when(jsonGeneratorMock.canWriteObjectId()).thenReturn(false);

        SerializableString propertyName = Mockito.mock(SerializableString.class);
        Mockito.when(objectIdWriterMock.propertyName).thenReturn(propertyName);

        writableObjectId.writeAsField(jsonGeneratorMock, serializerProviderMock, objectIdWriterMock);

        Mockito.verify(jsonGeneratorMock).writeFieldName(Mockito.eq(propertyName));
        Mockito.verifyNoMoreInteractions(jsonGeneratorMock, objectIdGeneratorMock);
    }
}