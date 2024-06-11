package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.ObjectIdGenerator.ObjectIdGeneratorType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.ser.ObjectIdWriter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Random;

/**
 * Unit tests for WritableObjectId class.
 */
public class WritableObjectIdTest {

    private ObjectIdGenerator<Object> idGenerator;
    private JsonGenerator jsonGeneratorMock;
    private SerializerProvider serializerProviderMock;
    private ObjectIdWriter objectIdWriterMock;
    private WritableObjectId writableObjectId;

    @Before
    public void setUp() {
        idGenerator = new ObjectIdGenerator() {
            @Override
            public ObjectIdGeneratorType getIdGeneratorType() {
                return ObjectIdGeneratorType.INT;
            }

            @Override
            public Object generateId(Object obj) {
                return new Random().nextInt();
            }
        };

        jsonGeneratorMock = mock(JsonGenerator.class);
        serializerProviderMock = mock(SerializerProvider.class);
        objectIdWriterMock = mock(ObjectIdWriter.class);

        when(jsonGeneratorMock.canWriteObjectId()).thenReturn(true);
        when(jsonGeneratorMock.writeObjectId(String.valueOf(anyObject()))).thenReturn(jsonGeneratorMock);

        writableObjectId = new WritableObjectId(idGenerator);
    }

    @After
    public void tearDown() {
        idGenerator = null;
        jsonGeneratorMock = null;
        serializerProviderMock = null;
        objectIdWriterMock = null;
        writableObjectId = null;
    }

    @Test
    public void testWriteAsId_IdWritten_ReturnsTrue() throws IOException {
        SerializableString propertyName = new SerializableString("testPropertyName");
        when(objectIdWriterMock.propertyName).thenReturn(propertyName);

        Object id = new Random().nextInt();

        boolean result = writableObjectId.writeAsId(jsonGeneratorMock, serializerProviderMock, objectIdWriterMock);

        assertEquals(true, result);
    }

    @Test
    public void testWriteAsId_NoId_ReturnsFalse() throws IOException {
        boolean result = writableObjectId.writeAsId(jsonGeneratorMock, serializerProviderMock, objectIdWriterMock);

        assertEquals(false, result);
    }

    @Test
    public void testWriteAsId_NoIdWritten_ReturnsFalse() throws IOException {
        idWritten = true;

        boolean result = writableObjectId.writeAsId(jsonGeneratorMock, serializerProviderMock, objectIdWriterMock);

        assertEquals(false, result);
    }

    @Test
    public void testWriteAsId_AlwaysAsId_ReturnsTrue() throws IOException {
        ObjectIdWriter objectIdWriterMockWithAlwaysAsId = mock(ObjectIdWriter.class);
        when(objectIdWriterMockWithAlwaysAsId.alwaysAsId).thenReturn(true);

        boolean result = writableObjectId.writeAsId(jsonGeneratorMock, serializerProviderMock, objectIdWriterMockWithAlwaysAsId);

        assertEquals(true, result);
    }

    @Test
    public void testWriteAsField() throws IOException {
        SerializableString propertyName = new SerializableString("testPropertyName");
        when(objectIdWriterMock.propertyName).thenReturn(propertyName);

        Object id = new Random().nextInt();

        writableObjectId.writeAsField(jsonGeneratorMock, serializerProviderMock, objectIdWriterMock);

        verify(jsonGeneratorMock).writeFieldName(propertyName);
        verify(jsonGeneratorMock).writeObjectId(String.valueOf(id));
    }

    @Test
    public void testGenerateId() {
        Object id = writableObjectId.generateId(new Object());

        assertNotNull(id);
    }
}