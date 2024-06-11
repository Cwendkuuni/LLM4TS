package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;
import com.fasterxml.jackson.databind.ser.impl.WritableObjectId;

import org.junit.Before;
import org.junit.Test;

public class WritableObjectIdTest {

    private ObjectIdGenerator<Object> objectIdGenerator;
    private ObjectMapper objectMapper;
    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;
    private ObjectIdWriter objectIdWriter;

    @Before
    public void setup() {
        objectIdGenerator = mock(ObjectIdGenerator.class);
        objectMapper = new ObjectMapper();
        jsonGenerator = mock(JsonGenerator.class);
        serializerProvider = mock(SerializerProvider.class);
        objectIdWriter = new ObjectIdWriter(null, null, null, null, null, null);
    }

    @Test
    public void testWriteAsId_NullGenerator() {
        WritableObjectId writableObjectId = new WritableObjectId(null);
        assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
    }

    @Test
    public void testWriteAsId_IdWritten() throws IOException {
        String idValue = "testId";
        Object id = new Object() {
            @Override
            public String toString() {
                return idValue;
            }
        };
        when(objectIdGenerator.generateId(any())).thenReturn(id);

        WritableObjectId writableObjectId = new WritableObjectId(objectIdGenerator);
        writableObjectId.id = id;

        when(jsonGenerator.canWriteObjectId()).thenReturn(true);
        when(jsonGenerator.writeObjectRef(anyString())).thenReturn(writableObjectId);

        assertTrue(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
    }

    @Test
    public void testWriteAsId_NoId() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(objectIdGenerator);

        when(jsonGenerator.canWriteObjectId()).thenReturn(true);

        assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
    }

    @Test
    public void testWriteAsId_WriteObjectIdFailed() throws IOException {
        String idValue = "testId";
        Object id = new Object() {
            @Override
            public String toString() {
                return idValue;
            }
        };
        when(objectIdGenerator.generateId(any())).thenReturn(id);

        WritableObjectId writableObjectId = new WritableObjectId(objectIdGenerator);

        StdSerializer<Object> serializer = mock(StdSerializer.class);
        when(serializerProvider.findValueSerializer(any(), any())).thenReturn(serializer);

        when(jsonGenerator.writeObjectId(anyString())).thenThrow(JsonMappingException.class);

        assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
    }

    @Test
    public void testWriteAsId_WriteField() throws IOException {
        String idValue = "testId";
        Object id = new Object() {
            @Override
            public String toString() {
                return idValue;
            }
        };
        when(objectIdGenerator.generateId(any())).thenReturn(id);

        WritableObjectId writableObjectId = new WritableObjectId(objectIdGenerator);

        SerializableString name = new SerializableString("testName");
        when(objectIdWriter.propertyName).thenReturn(name);

        when(jsonGenerator.canWriteObjectId()).thenReturn(false);

        when(jsonGenerator.writeFieldName(name)).thenReturn(writableObjectId);
        when(serializer.serialize(id, jsonGenerator, serializerProvider)).thenReturn(writableObjectId);

        assertTrue(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
    }

    @Test
    public void testGenerateId() throws IOException {
        String idValue = "testId";
        Object id = new Object() {
            @Override
            public String toString() {
                return idValue;
            }
        };
        when(objectIdGenerator.generateId(any())).thenReturn(id);

        WritableObjectId writableObjectId = new WritableObjectId(objectIdGenerator);

        Object forPojo = new Object();
        Object generatedId = writableObjectId.generateId(forPojo);

        assertEquals(idValue, generatedId.toString());
    }

    @Test
    public void testWriteAsField_IdWritten() throws IOException {
        String idValue = "testId";
        Object id = new Object() {
            @Override
            public String toString() {
                return idValue;
            }
        };

        WritableObjectId writableObjectId = new WritableObjectId(null);
        writableObjectId.id = id;

        when(jsonGenerator.canWriteObjectId()).thenReturn(false);

        writeAsField_WriteField("testName", id);
    }

    @Test
    public void testWriteAsField_NoId() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(null);

        writeAsField_WriteField("testName", null);
    }

    @Test(expected = JsonMappingException.class)
    public void testWriteAsField_FailedToWrite() throws IOException {
        String idValue = "testId";
        Object id = new Object() {
            @Override
            public String toString() {
                return idValue;
            }
        };

        WritableObjectId writableObjectId = new WritableObjectId(null);

        SerializableString name = new SerializableString("testName");
        when(jsonGenerator.writeFieldName(name)).thenReturn(writableObjectId);
        when(serializerProvider.findValueSerializer(any(), any())).thenThrow(JsonMappingException.class);

        writeAsField_WriteField(name, id);
    }

    @Test
    public void testWriteAsField_WriteObjectId() throws IOException {
        String idValue = "testId";
        Object id = new Object() {
            @Override
            public String toString() {
                return idValue;
            }
        };

        WritableObjectId writableObjectId = new WritableObjectId(null);

        SerializableString name = new SerializableString("testName");
        when(jsonGenerator.writeFieldName(name)).thenReturn(writableObjectId);
        when(jsonGenerator.writeObjectId(anyString())).thenReturn(writableObjectId);

        writeAsField_WriteField(name, id);
    }

    private void writeAsField_WriteField(SerializableString name, Object id) throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(null);
        writableObjectId.id = id;

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = new ObjectIdWriter(name, null, null, null, null, null);

        writableObjectId.writeAsField(gen, provider, w);

        verify(gen).writeFieldName(name);
        verify(provider).findValueSerializer(any(), any());
    }
}