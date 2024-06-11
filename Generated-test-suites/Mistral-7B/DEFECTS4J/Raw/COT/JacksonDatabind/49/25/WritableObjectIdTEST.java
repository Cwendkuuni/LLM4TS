package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.ObjectIdGenerator.IdIdGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.junit.SerializerTestBase;
import com.fasterxml.jackson.databind.ser.std.ObjectIdWriter;

/**
 * Test class for WritableObjectId
 */
public class WritableObjectIdTest extends SerializerTestBase {

    private WritableObjectId writableObjectId;
    private ObjectIdGenerator<?> idGenerator;
    private IdIdGenerator idGeneratorMock;
    private SerializerProvider provider;
    private ObjectIdWriter objectIdWriter;
    private JsonGenerator jsonGenerator;

    @Before
    public void setUp() throws Exception {
        idGenerator = mock(IdIdGenerator.class);
        idGeneratorMock = mock(IdIdGenerator.class);
        when(idGenerator.generateId(any())).thenCallRealMethod();
        when(idGeneratorMock.generateId(any())).thenReturn(1L);
        provider = new SerializerProvider();
        objectIdWriter = new ObjectIdWriter(idGenerator, null, null, null, null);
        jsonGenerator = createJsonGenerator();
        writableObjectId = new WritableObjectId(idGenerator);
    }

    @Test
    public void testConstructor() {
        // Test constructor
        assertNotNull(writableObjectId);
        assertNotNull(writableObjectId.generator);
        assertFalse(writableObjectId.idWritten);
    }

    @Test
    public void testWriteAsId_ShouldWriteObjectIdWhenCanWriteObjectId() throws IOException {
        // Test writeAsId when canWriteObjectId is true
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);
        when(objectIdWriter.alwaysAsId).thenReturn(false);
        when(objectIdWriter.propertyName).thenReturn(new SerializableString("testPropertyName"));
        assertFalse(writableObjectId.writeAsId(jsonGenerator, provider, objectIdWriter));
        // The ObjectId should not be written when id is null or idWritten is already true
        assertFalse(writableObjectId.idWritten);
        assertNull(writableObjectId.id);

        // Test writeAsId when id is not null and idWritten is false
        Object testId = new Object();
        writableObjectId.id = testId;
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);
        when(objectIdWriter.alwaysAsId).thenReturn(false);
        when(objectIdWriter.propertyName).thenReturn(new SerializableString("testPropertyName"));
        writableObjectId.writeAsId(jsonGenerator, provider, objectIdWriter);
        // The ObjectId should be written
        assertTrue(writableObjectId.idWritten);
        assertNotNull(writableObjectId.id);

        // Test writeAsId when canWriteObjectId is false and alwaysAsId is false
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);
        when(objectIdWriter.alwaysAsId).thenReturn(false);
        when(objectIdWriter.propertyName).thenReturn(null);
        assertFalse(writableObjectId.writeAsId(jsonGenerator, provider, objectIdWriter));
        // The ObjectId should not be written
        assertFalse(writableObjectId.idWritten);
        assertNull(writableObjectId.id);

        // Test writeAsId when canWriteObjectId is false and alwaysAsId is true
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);
        when(objectIdWriter.alwaysAsId).thenReturn(true);
        when(objectIdWriter.propertyName).thenReturn(null);
        assertFalse(writableObjectId.writeAsId(jsonGenerator, provider, objectIdWriter));
        // The ObjectId should not be written
        assertFalse(writableObjectId.idWritten);
        assertNull(writableObjectId.id);

        // Test writeAsId when canWriteObjectId is false and propertyName is not null
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);
        when(objectIdWriter.alwaysAsId).thenReturn(false);
        when(objectIdWriter.propertyName).thenReturn(new SerializableString("testPropertyName"));
        assertFalse(writableObjectId.writeAsId(jsonGenerator, provider, objectIdWriter));
        // The ObjectId should not be written
        assertFalse(writableObjectId.idWritten);
        assertNull(writableObjectId.id);

        // Test writeAsId when canWriteObjectId is false and propertyName is null
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);
        when(objectIdWriter.alwaysAsId).thenReturn(false);
        when(objectIdWriter.propertyName).thenReturn(null);
        assertFalse(writableObjectId.writeAsId(jsonGenerator, provider, objectIdWriter));
        // The ObjectId should not be written
        assertFalse(writableObjectId.idWritten);
        assertNull(writableObjectId.id);
    }

    @Test
    public void testWriteAsId_ShouldWriteSerializerToJsonGeneratorWhenCannotWriteObjectId() throws IOException {
        // Test writeAsId when canWriteObjectId is false and id is not null and idWritten is false
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);
        when(objectIdWriter.alwaysAsId).thenReturn(false);
        when(objectIdWriter.propertyName).thenReturn(new SerializableString("testPropertyName"));
        Object testId = new Object();
        writableObjectId.id = testId;
        writableObjectId.writeAsId(jsonGenerator, provider, objectIdWriter);
        // The Serializer should be written to JsonGenerator
        verify(objectIdWriter.serializer, times(1)).serialize(testId, jsonGenerator, provider);
        // The ObjectId should not be written
        assertFalse(writableObjectId.idWritten);
        assertNull(writableObjectId.id);
    }

    @Test
    public void testGenerateId_ShouldGenerateIdWhenGeneratorIsNotNull() {
        // Test generateId when generator is not null
        when(idGeneratorMock.generateId(any())).thenReturn(1L);
        Object testPojo = new Object();
        writableObjectId.generateId(testPojo);
        // The id should be generated
        assertEquals(1L, writableObjectId.id);
        // The idWritten flag should be false
        assertFalse(writableObjectId.idWritten);
    }

    @Test(expected = AssertionError.class)
    public void testGenerateId_ShouldThrowAssertionErrorWhenGeneratorIsNull() {
        // Test generateId when generator is null
        writableObjectId.generator = null;
        Object testPojo = new Object();
        writableObjectId.generateId(testPojo);
        // This test should fail with AssertionError
    }

    @Test
    public void testWriteAsField_ShouldWriteObjectIdAsFieldWhenCanWriteObjectId() throws IOException {
        // Test writeAsField when canWriteObjectId is true
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);
        when(objectIdWriter.propertyName).thenReturn(new SerializableString("testPropertyName"));
        assertFalse(writableObjectId.idWritten);
        assertNull(writableObjectId.id);
        writableObjectId.writeAsField(jsonGenerator, provider, objectIdWriter);
        // The ObjectId should be written as field
        verify(jsonGenerator, times(1)).writeObjectId(eq("testPropertyName"));
        // The idWritten flag should be true
        assertTrue(writableObjectId.idWritten);
        assertNotNull(writableObjectId.id);
    }

    @Test
    public void testWriteAsField_ShouldWriteSerializerToJsonGeneratorWhenCannotWriteObjectId() throws IOException {
        // Test writeAsField when canWriteObjectId is false and propertyName is not null
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);
        when(objectIdWriter.propertyName).thenReturn(new SerializableString("testPropertyName"));
        assertFalse(writableObjectId.idWritten);
        assertNull(writableObjectId.id);
        writableObjectId.writeAsField(jsonGenerator, provider, objectIdWriter);
        // The Serializer should be written to JsonGenerator
        verify(objectIdWriter.serializer, times(1)).serialize(any(), eq(jsonGenerator), eq(provider));
        // The idWritten flag should be false
        assertFalse(writableObjectId.idWritten);
        assertNull(writableObjectId.id);
    }

    @Test
    public void testWriteAsField_ShouldWriteObjectIdAsFieldWithNameWhenCanWriteObjectId() throws IOException {
        // Test writeAsField when canWriteObjectId is true and propertyName is not null
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);
        when(objectIdWriter.propertyName).thenReturn(new SerializableString("testPropertyName"));
        assertFalse(writableObjectId.idWritten);
        assertNull(writableObjectId.id);
        writableObjectId.writeAsField(jsonGenerator, provider, objectIdWriter);
        // The ObjectId should be written as field with name "testPropertyName"
        verify(jsonGenerator, times(1)).writeFieldName(eq("testPropertyName"));
        verify(jsonGenerator, times(1)).writeObjectId(eq("testPropertyName"));
        // The idWritten flag should be true
        assertTrue(writableObjectId.idWritten);
        assertNotNull(writableObjectId.id);
    }

    @Test
    public void testWriteAsField_ShouldWriteSerializerToJsonGeneratorWithNameWhenCannotWriteObjectId() throws IOException {
        // Test writeAsField when canWriteObjectId is false and propertyName is not null
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);
        when(objectIdWriter.propertyName).thenReturn(new SerializableString("testPropertyName"));
        assertFalse(writableObjectId.idWritten);
        assertNull(writableObjectId.id);
        writableObjectId.writeAsField(jsonGenerator, provider, objectIdWriter);
        // The Serializer should be written to JsonGenerator
        verify(objectIdWriter.serializer, times(1)).serialize(any(), eq(jsonGenerator), eq(provider));
        // The idWritten flag should be false
        assertFalse(writableObjectId.idWritten);
        assertNull(writableObjectId.id);
    }

    @Test
    public void testWriteAsField_ShouldWriteObjectIdAsFieldWithNullNameWhenCanWriteObjectId() throws IOException {
        // Test writeAsField when canWriteObjectId is true and propertyName is null
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);
        when(objectIdWriter.propertyName).thenReturn(null);
        assertFalse(writableObjectId.idWritten);
        assertNull(writableObjectId.id);
        writableObjectId.writeAsField(jsonGenerator, provider, objectIdWriter);
        // The ObjectId should be written as field without name
        verify(jsonGenerator, times(1)).writeObjectId(anyString());
        // The idWritten flag should be true
        assertTrue(writableObjectId.idWritten);
        assertNotNull(writableObjectId.id);
    }

    @Test
    public void testWriteAsField_ShouldWriteSerializerToJsonGeneratorWithNullNameWhenCannotWriteObjectId() throws IOException {
        // Test writeAsField when canWriteObjectId is false and propertyName is null
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);
        when(objectIdWriter.propertyName).thenReturn(null);
        assertFalse(writableObjectId.idWritten);
        assertNull(writableObjectId.id);
        writableObjectId.writeAsField(jsonGenerator, provider, objectIdWriter);
        // The Serializer should be written to JsonGenerator
        verify(objectIdWriter.serializer, times(1)).serialize(any(), eq(jsonGenerator), eq(provider));
        // The idWritten flag should be false
        assertFalse(writableObjectId.idWritten);
        assertNull(writableObjectId.id);
    }
}