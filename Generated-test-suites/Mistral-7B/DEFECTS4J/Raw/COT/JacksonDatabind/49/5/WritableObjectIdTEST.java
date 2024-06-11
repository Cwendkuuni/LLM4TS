package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Field;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.ObjectIdGenerator.IdIntGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ObjectIdWriter;

/**
 * Test class for WritableObjectId class
 */
public class WritableObjectIdTest {

    private ObjectIdGenerator<Object> idGenerator;
    private ObjectMapper objectMapper;
    private ObjectIdWriter idWriter;

    @Before
    public void setup() throws Exception {
        idGenerator = mock(IdIntGenerator.class);
        ObjectMapper mapper = new ObjectMapper();
        objectMapper = new ObjectMapper();
        objectMapper.setDefaultPropertyInclusion(null);
        idWriter = new ObjectIdWriter(mapper, null, null);
    }

    @Test
    public void testWritableObjectIdConstructor() {
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        assertNull(writableObjectId.id);
        assertFalse(writableObjectId.idWritten);
        assertSame(idGenerator, writableObjectId.generator);
    }

    @Test
    public void testWriteAsId_idWrittenAndIdPresent_writesIdToJsonGenerator() throws IOException {
        String testId = "testId";
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        writableObjectId.id = testId;
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);

        when(jsonGenerator.canWriteObjectId()).thenReturn(true);
        when(jsonGenerator.writeObjectRef(String.valueOf(testId))).thenReturn(jsonGenerator);

        assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, idWriter));
        assertSame(testId, idWriter.getId());
    }

    @Test
    public void testWriteAsId_idWrittenAndIdAbsent_writesIdToJsonGeneratorUsingSerializer() throws IOException {
        String testId = "testId";
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);

        when(jsonGenerator.canWriteObjectId()).thenReturn(false);
        SerializableString propertyName = mock(SerializableString.class);
        when(idWriter.propertyName).thenReturn(propertyName);

        when(propertyName.toString()).thenReturn("testPropertyName");
        when(serializerProvider.findValueSerializer(Object.class)).thenReturn(mock(SerializerProvider.SerializerProvider.ValueSerializer.class));

        Object testObject = new Object();
        when(serializerProvider.serializeValue(testObject, jsonGenerator, serializerProvider)).thenReturn("testSerializedValue");

        assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, idWriter));
        assertSame("testSerializedValue", idWriter.getSerializedValue());
        assertSame("testPropertyName", idWriter.getPropertyName());
    }

    @Test
    public void testWriteAsId_idAbsentAndIdWrittenFalse_returnsFalse() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);

        assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, idWriter));
    }

    @Test
    public void testGenerateId_nullForPojo_returnsNullObjectId() {
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        assertNull(writableObjectId.generateId(null));
    }

    @Test
    public void testGenerateId_nonNullForPojo_generatesObjectId() {
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        Object testPojo = new Object();
        Object generatedId = writableObjectId.generateId(testPojo);
        assertNotNull(generatedId);
    }

    @Test(expected = IOException.class)
    public void testGenerateId_throwsIOException() throws IOException {
        WritableObjectId writableObjectId = spy(WritableObjectId.class);
        doThrow(new IOException("TestException")).when(writableObjectId).generateId(any());

        WritableObjectId writableObjectIdSpy = writableObjectId;
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);

        writableObjectIdSpy.writeAsId(jsonGenerator, serializerProvider, idWriter);
    }

    @Test
    public void testWriteAsField() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        ObjectIdWriter idWriter = new ObjectIdWriter(objectMapper, jsonGenerator, null);

        idWriter.propertyName = new SerializableString("testPropertyName");

        writableObjectId.writeAsField(jsonGenerator, serializerProvider, idWriter);
        assertSame("testPropertyName", idWriter.getPropertyName());
        assertSame("testObjectId", idWriter.getId());
    }

    @After
    public void tearDown() throws Exception {
        idGenerator = null;
        objectMapper = null;
        idWriter = null;
    }
}