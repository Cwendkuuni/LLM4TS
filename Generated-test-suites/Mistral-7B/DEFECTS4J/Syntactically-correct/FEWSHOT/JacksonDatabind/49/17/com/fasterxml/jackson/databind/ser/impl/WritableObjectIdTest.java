package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.ObjectIdWriter;
import com.fasterxml.jackson.databind.util.ClassUtil;

import com.fasterxml.jackson.databind.ser.impl.WritableObjectId;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for WritableObjectId class.
 */
public class WritableObjectIdTest {

    private ObjectMapper objectMapper;
    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;
    private ObjectIdWriter objectIdWriter;
    private ObjectIdGenerator<?> objectIdGenerator;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        jsonGenerator = mock(JsonGenerator.class);
        serializerProvider = mock(SerializerProvider.class);
        objectIdWriter = mock(ObjectIdWriter.class);
        objectIdGenerator = mock(ObjectIdGenerator.class);

        when(objectMapper.getSerializerProvider()).thenReturn(serializerProvider);
        when(objectMapper.writerWithView(anyClass())).thenReturn(jsonGenerator);
        when(objectIdWriter.propertyName).thenReturn(new SerializableString("testPropertyName"));
        when(objectIdGenerator.generateId(any())).thenReturn("testId");
    }

    @After
    public void tearDown() throws Exception {
        // no specific cleanup needed
    }

    @Test
    public void testWriteAsId() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(objectIdGenerator);

        when(jsonGenerator.canWriteObjectId()).thenReturn(true);
        when(jsonGenerator.writeObjectRef(anyString())).thenReturnVoid();

        assertTrue(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));

        when(jsonGenerator.canWriteObjectId()).thenReturn(false);
        when(objectIdWriter.serializer).thenReturn(mock(ObjectSerializer.class));

        ObjectSerializer objectSerializer = mock(ObjectSerializer.class);
        when(objectIdWriter.serializer).thenReturn(objectSerializer);

        assertTrue(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
    }

    @Test
    public void testGenerateId() {
        WritableObjectId writableObjectId = new WritableObjectId(objectIdGenerator);

        Object forPojo = new Object() {};

        Object id = writableObjectId.generateId(forPojo);

        assertEquals("testId", id);
    }

    @Test
    public void testWriteAsField() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(objectIdGenerator);

        when(jsonGenerator.canWriteObjectId()).thenReturn(false);
        when(objectIdWriter.propertyName).thenReturn(new SerializableString("testPropertyName"));

        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);

        verify(jsonGenerator, times(1)).writeFieldName("testPropertyName");
    }
}

class ObjectSerializer {
    public void serialize(Object obj, JsonGenerator gen, SerializerProvider provider) throws IOException {
        // no implementation needed for this test
    }
}