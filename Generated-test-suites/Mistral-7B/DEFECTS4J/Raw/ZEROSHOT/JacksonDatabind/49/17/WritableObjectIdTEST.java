package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.ObjectIdWriter;

/**
 * Test class for WritableObjectId class
 */
public class WritableObjectIdTest {

    @Test
    public void testWritableObjectIdConstruction() {
        ObjectIdGenerator<?> generatorMock = mock(ObjectIdGenerator.class);
        WritableObjectId writableObjectId = new WritableObjectId(generatorMock);
        assertNotNull(writableObjectId);
        assertNull(writableObjectId.id);
        assertFalse(writableObjectId.idWritten);
    }

    @Test
    public void testWriteAsIdHappyPath() throws IOException {
        ObjectIdGenerator<?> generatorMock = mock(ObjectIdGenerator.class);
        ObjectIdWriter idWriterMock = mock(ObjectIdWriter.class);
        JsonGenerator jsonGeneratorMock = mock(JsonGenerator.class);
        SerializerProvider serializerProviderMock = mock(SerializerProvider.class);
        StdSerializer serializerMock = mock(StdSerializer.class);
        when(generatorMock.generateId(any())).thenReturn("generatedId");

        WritableObjectId writableObjectId = new WritableObjectId(generatorMock);
        when(idWriterMock.serializer).thenReturn(serializerMock);
        when(idWriterMock.alwaysAsId).thenReturn(false);
        when(jsonGeneratorMock.canWriteObjectId()).thenReturn(true);
        when(jsonGeneratorMock.writeObjectRef(anyString())).thenReturn(jsonGeneratorMock);

        boolean result = writableObjectId.writeAsId(jsonGeneratorMock, serializerProviderMock, idWriterMock);
        assertNotNull(writableObjectId.id);
        assertNotNull(result);
    }

    @Test
    public void testWriteAsIdFailPath() throws IOException {
        ObjectIdGenerator<?> generatorMock = mock(ObjectIdGenerator.class);
        ObjectIdWriter idWriterMock = mock(ObjectIdWriter.class);
        JsonGenerator jsonGeneratorMock = mock(JsonGenerator.class);
        SerializerProvider serializerProviderMock = mock(SerializerProvider.class);
        StdSerializer serializerMock = mock(StdSerializer.class);
        when(generatorMock.generateId(any())).thenReturn("generatedId");

        WritableObjectId writableObjectId = new WritableObjectId(generatorMock);
        when(idWriterMock.serializer).thenReturn(serializerMock);
        when(idWriterMock.alwaysAsId).thenReturn(false);
        when(jsonGeneratorMock.canWriteObjectId()).thenReturn(false);

        boolean result = writableObjectId.writeAsId(jsonGeneratorMock, serializerProviderMock, idWriterMock);
        assertFalse(result);
    }

    @Test
    public void testGenerateIdHappyPath() {
        ObjectIdGenerator<?> generatorMock = mock(ObjectIdGenerator.class);
        WritableObjectId writableObjectId = new WritableObjectId(generatorMock);
        Object forPojo = new Object();
        String id = "generatedId";
        when(generatorMock.generateId(forPojo)).thenReturn(id);

        writableObjectId.generateId(forPojo);
        assertNotNull(writableObjectId.id);
        assertNotNull(id);
    }

    @Test(expected = JsonMappingException.class)
    public void testGenerateIdWithNullForPojo() {
        ObjectIdGenerator<?> generatorMock = mock(ObjectIdGenerator.class);
        WritableObjectId writableObjectId = new WritableObjectId(generatorMock);
        Object forPojo = null;

        writableObjectId.generateId(forPojo);
    }

    @Test
    public void testWriteAsFieldHappyPath() throws IOException {
        ObjectIdGenerator<?> generatorMock = mock(ObjectIdGenerator.class);
        ObjectIdWriter idWriterMock = mock(ObjectIdWriter.class);
        JsonGenerator jsonGeneratorMock = mock(JsonGenerator.class);
        SerializerProvider serializerProviderMock = mock(SerializerProvider.class);
        SerializableString nameMock = mock(SerializableString.class);
        when(idWriterMock.propertyName).thenReturn(nameMock);
        when(jsonGeneratorMock.canWriteObjectId()).thenReturn(false);

        WritableObjectId writableObjectId = new WritableObjectId(generatorMock);
        when(jsonGeneratorMock.writeFieldName(anyString())).thenReturn(jsonGeneratorMock);

        writableObjectId.writeAsField(jsonGeneratorMock, serializerProviderMock, idWriterMock);
        assertTrue(writableObjectId.idWritten);
    }

    @Test
    public void testWriteAsFieldFailPath() throws IOException {
        ObjectIdGenerator<?> generatorMock = mock(ObjectIdGenerator.class);
        ObjectIdWriter idWriterMock = mock(ObjectIdWriter.class);
        JsonGenerator jsonGeneratorMock = mock(JsonGenerator.class);
        SerializerProvider serializerProviderMock = mock(SerializerProvider.class);
        SerializableString nameMock = mock(SerializableString.class);
        when(jsonGeneratorMock.canWriteObjectId()).thenReturn(false);

        WritableObjectId writableObjectId = new WritableObjectId(generatorMock);

        writableObjectId.writeAsField(jsonGeneratorMock, serializerProviderMock, idWriterMock);
        assertFalse(writableObjectId.idWritten);
    }

    @Test
    public void testWriteAsFieldWithNullName() throws IOException {
        ObjectIdGenerator<?> generatorMock = mock(ObjectIdGenerator.class);
        ObjectIdWriter idWriterMock = mock(ObjectIdWriter.class);
        JsonGenerator jsonGeneratorMock = mock(JsonGenerator.class);
        SerializerProvider serializerProviderMock = mock(SerializerProvider.class);

        WritableObjectId writableObjectId = new WritableObjectId(generatorMock);
        when(jsonGeneratorMock.writeFieldName(anyString())).thenReturn(jsonGeneratorMock);
        when(idWriterMock.propertyName).thenReturn(null);

        writableObjectId.writeAsField(jsonGeneratorMock, serializerProviderMock, idWriterMock);
        assertFalse(writableObjectId.idWritten);
    }

    @Test
    public void testSerializeObjectWithWritableObjectId() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectIdGenerator<?> generatorMock = mock(ObjectIdGenerator.class);
        WritableObjectId writableObjectId = new WritableObjectId(generatorMock);
        Object forPojo = new Object();

        when(generatorMock.generateId(forPojo)).thenReturn("generatedId");

        ObjectWriter objectWriter = objectMapper.writerFor(Object.class);
        String json = objectWriter.withSerializer(WritableObjectId.class, new WritableObjectIdSerializer())
                .writeValueAsString(new WritableObjectId(generatorMock, forPojo));

        assertNotNull(json);
        assertTrue(json.contains("\"id\":\"" + "generatedId" + "\""));
    }

    static class WritableObjectIdSerializer extends StdSerializer<WritableObjectId> {
        public WritableObjectIdSerializer() {
            this(null);
        }

        public WritableObjectIdSerializer(Class<WritableObjectId> t) {
            super(t);
        }

        @Override
        public void serialize(WritableObjectId writableObjectId, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
                throws IOException {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeFieldName("id");
            jsonGenerator.writeObjectId(String.valueOf(writableObjectId.id));
            jsonGenerator.writeEndObject();
        }
    }
}