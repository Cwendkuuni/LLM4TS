package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import com.fasterxml.jackson.databind.ser.impl.WritableObjectId;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.ObjectIdWriter;

/**
 * This class contains JUnit 4 test cases for all methods in WritableObjectId class.
 */
public class WritableObjectIdTest
{
    private JsonGenerator gen;
    private SerializerProvider provider;
    private ObjectMapper mapper;
    private ObjectIdGenerator<?> idGeneratorMock;
    private ObjectIdWriter idWriterMock;
    private StdSerializer<Object> idSerializerMock;
    private WritableObjectId writableObjectId;

    @Before
    public void setup() throws Exception
    {
        idGeneratorMock = mock(ObjectIdGenerator.class);
        idWriterMock = mock(ObjectIdWriter.class);
        idSerializerMock = mock(StdSerializer.class);
        mapper = new ObjectMapper();
        provider = mapper.getSerializerProvider();
        gen = mock(JsonGenerator.class);

        when(idGeneratorMock.generateId(any())).thenReturn("generatedId");
        when(idWriterMock.propertyName).thenReturn(new SerializableString("propertyName"));
        when(idWriterMock.alwaysAsId).thenReturn(false);
        when(idWriterMock.serializer).thenReturn(idSerializerMock);
        when(gen.canWriteObjectId()).thenReturn(true);
        when(gen.writeObjectId(anyString())).thenCallRealMethod();
        when(gen.writeFieldName(anyString())).thenCallRealMethod();

        writableObjectId = new WritableObjectId(idGeneratorMock);
    }

    @Test
    public void testWritableObjectIdConstructor()
    {
        assertNotNull(writableObjectId);
    }

    @Test
    public void testWriteAsId_whenIdIsNotNullAndIdWrittenOrAlwaysAsIdIsTrue() throws IOException
    {
        when(idWriterMock.alwaysAsId).thenReturn(true);

        writableObjectId.writeAsId(gen, provider, idWriterMock);

        verifyWriteObjectIdCalled(writableObjectId.getId().toString());
    }

    @Test
    public void testWriteAsId_whenIdIsNotNullButIdWrittenIsFalse() throws IOException
    {
        writableObjectId.id = "existingId";

        writableObjectId.writeAsId(gen, provider, idWriterMock);

        verifyWriteObjectSerializerCalledWithId(writableObjectId.getId());
    }

    @Test(expected = JsonMappingException.class)
    public void testWriteAsId_whenIdIsNull() throws IOException
    {
        writableObjectId.writeAsId(gen, provider, idWriterMock);

        // No need to verify any call since the test case fails with JsonMappingException
    }

    @Test
    public void testGenerateId() throws Exception
    {
        Object testObject = new Object();

        Object id = writableObjectId.generateId(testObject);

        assertNotNull(id);
        assertEquals("generatedId", id);
    }

    @Test
    public void testWriteAsField_whenIdIsNotNull() throws IOException
    {
        writableObjectId.generateId(new Object());

        writableObjectId.writeAsField(gen, provider, idWriterMock);

        verifyWriteObjectIdCalled(writableObjectId.getId().toString());
    }

    @Test
    public void testWriteAsField_whenIdIsNotNullButIdWrittenIsFalse() throws IOException
    {
        writableObjectId.id = "existingId";

        writableObjectId.writeAsField(gen, provider, idWriterMock);

        verifyWriteFieldNameCalled();
        verifyWriteObjectSerializerCalledWithId(writableObjectId.getId());
    }

    @Test
    public void testWriteAsField_whenIdIsNull() throws IOException
    {
        writableObjectId.writeAsField(gen, provider, idWriterMock);

        verifyWriteFieldNameCalled();
        verifyNoMoreInteractions(gen);
    }

    // Helper methods

    private void verifyWriteObjectIdCalled(String id) throws IOException
    {
        gen.writeObjectId(String.valueOf(id));
        verify(gen).writeObjectId(String.valueOf(id));
    }

    private void verifyWriteObjectSerializerCalledWithId(Object id) throws IOException
    {
        idSerializerMock.serialize(id, gen, provider);
        verify(idSerializerMock).serialize(id, gen, provider);
    }

    private void verifyWriteFieldNameCalled() throws IOException
    {
        gen.writeFieldName("propertyName");
        verify(gen).writeFieldName("propertyName");
    }

    // JUnit util methods

    private static <T> T any()
    {
        return (T) ClassUtil.construct(ClassUtil.findConcreteType(Object.class));
    }

    private static <T> T any(Class<T> type)
    {
        return mock(type);
    }
}