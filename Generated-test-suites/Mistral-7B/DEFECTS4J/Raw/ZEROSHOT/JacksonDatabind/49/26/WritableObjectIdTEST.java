package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectIdGenerator.IdIntGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * Test class for WritableObjectId class.
 */
public class WritableObjectIdTest {

    @Test
    public void testWritableObjectId() throws IOException, JsonMappingException {
        ObjectIdGenerator<Object> idGenerator = mock(ObjectIdGenerator.class);
        when(idGenerator.generateId(null)).thenReturn("mockId1");
        when(idGenerator.generateId(new Object())).thenReturn("mockId2");

        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);

        // Test generateId method
        Object id = writableObjectId.generateId(null);
        assertEquals("mockId1", id);

        id = writableObjectId.generateId(new Object());
        assertEquals("mockId2", id);

        // Test writeAsId method
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);
        when(jsonGenerator.writeObjectRef(String.valueOf("mockId1"))).thenReturn(jsonGenerator);

        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        when(serializerProvider.isEnabled(JsonSerializer.Feature.WRITE_OBJECT_ID_KEYS_UNQUOTED)).thenReturn(true);
        when(serializerProvider.isEnabled(JsonSerializer.Feature.WRITE_NULL_SERIALIZERS)).thenReturn(true);

        ObjectIdWriter objectIdWriter = new ObjectIdWriter(null, null, null, null, null, null);
        objectIdWriter.propertyName = new SerializableString("testPropertyName");

        boolean result = writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);
        assertEquals(true, result);

        // Test writeAsId method when canWriteObjectId is false
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);
        result = writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);
        assertEquals(false, result);

        // Test writeAsField method
        JsonGenerator jsonGenerator2 = mock(JsonGenerator.class);
        when(jsonGenerator2.canWriteObjectId()).thenReturn(false);
        when(jsonGenerator2.writeFieldName(new SerializableString("testPropertyName"))).thenReturn(jsonGenerator2);

        SerializerProvider serializerProvider2 = mock(SerializerProvider.class);

        ObjectIdWriter objectIdWriter2 = new ObjectIdWriter(null, null, null, null, null, null);
        objectIdWriter2.propertyName = new SerializableString("testPropertyName");

        writableObjectId.writeAsField(jsonGenerator2, serializerProvider2, objectIdWriter2);
    }

    @Test
    public void testJsonSchemaCompatibility() throws IOException {
        ObjectIdGenerator<Object> idGenerator = mock(ObjectIdGenerator.class);
        when(idGenerator.generateId(null)).thenReturn("mockId1");
        when(idGenerator.generateId(new Object())).thenReturn("mockId2");

        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);

        JsonMapper jsonMapper = new JsonMapper();
        JsonSchema jsonSchema = jsonMapper.getSchema(WritableObjectId.class);

        TypeFactory typeFactory = jsonMapper.getTypeFactory();
        assertEquals(typeFactory.constructType(WritableObjectId.class), jsonSchema.getSchemaName());
    }

    @Test
    public void testIdWrittenFlag() throws IOException, JsonMappingException {
        ObjectIdGenerator<Object> idGenerator = mock(ObjectIdGenerator.class);
        when(idGenerator.generateId(null)).thenReturn("mockId1");
        when(idGenerator.generateId(new Object())).thenReturn("mockId2");

        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);

        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);

        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        when(serializerProvider.isEnabled(JsonSerializer.Feature.WRITE_OBJECT_ID_KEYS_UNQUOTED))
                .thenReturn(true);
        when(serializerProvider.isEnabled(JsonSerializer.Feature.WRITE_NULL_SERIALIZERS)).thenReturn(true);

        ObjectIdWriter objectIdWriter = new ObjectIdWriter(null, null, null, null, null, null);
        objectIdWriter.propertyName = new SerializableString("testPropertyName");

        writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);
        assertEquals(true, writableObjectId.idWritten);

        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
        assertEquals(true, writableObjectId.idWritten);
    }

    @Test
    public void testIdWrittenFlagAfterGenerateId() throws IOException, JsonMappingException {
        ObjectIdGenerator<Object> idGenerator = mock(ObjectIdGenerator.class);
        when(idGenerator.generateId(null)).thenReturn("mockId1");
        when(idGenerator.generateId(new Object())).thenReturn("mockId2");

        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);

        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);

        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        when(serializerProvider.isEnabled(JsonSerializer.Feature.WRITE_OBJECT_ID_KEYS_UNQUOTED))
                .thenReturn(true);
        when(serializerProvider.isEnabled(JsonSerializer.Feature.WRITE_NULL_SERIALIZERS)).thenReturn(true);

        ObjectIdWriter objectIdWriter = new ObjectIdWriter(null, null, null, null, null, null);
        objectIdWriter.propertyName = new SerializableString("testPropertyName");

        writableObjectId.generateId(null);
        writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);
        assertEquals(true, writableObjectId.idWritten);

        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
        assertEquals(true, writableObjectId.idWritten);
    }

    @Test
    public void testIdWrittenFlagWhenIdIsNull() throws IOException, JsonMappingException {
        ObjectIdGenerator<Object> idGenerator = mock(ObjectIdGenerator.class);
        when(idGenerator.generateId(null)).thenReturn(null);
        when(idGenerator.generateId(new Object())).thenReturn("mockId2");

        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);

        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);

        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        when(serializerProvider.isEnabled(JsonSerializer.Feature.WRITE_OBJECT_ID_KEYS_UNQUOTED))
                .thenReturn(true);
        when(serializerProvider.isEnabled(JsonSerializer.Feature.WRITE_NULL_SERIALIZERS)).thenReturn(true);

        ObjectIdWriter objectIdWriter = new ObjectIdWriter(null, null, null, null, null, null);
        objectIdWriter.propertyName = new SerializableString("testPropertyName");

        writableObjectId.generateId(null);
        writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);
        assertEquals(false, writableObjectId.idWritten);

        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
        assertEquals(false, writableObjectId.idWritten);

        IdIntGenerator intIdGenerator = mock(IdIntGenerator.class);
        when(intIdGenerator.generateId(null)).thenReturn(1);
        when(idGenerator.generateId(null)).thenReturn(intIdGenerator);

        WritableObjectId writableObjectIdInt = new WritableObjectId(idGenerator);

        writableObjectIdInt.generateId(null);
        writableObjectIdInt.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);
        assertEquals(false, writableObjectIdInt.idWritten);

        writableObjectIdInt.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
        assertEquals(false, writableObjectIdInt.idWritten);
    }

    @Test(expected = JsonMappingException.class)
    public void testWriteAsIdWithNullJsonGenerator() throws IOException, JsonMappingException {
        ObjectIdGenerator<Object> idGenerator = mock(ObjectIdGenerator.class);
        when(idGenerator.generateId(null)).thenReturn("mockId1");
        when(idGenerator.generateId(new Object())).thenReturn("mockId2");

        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);

        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        when(serializerProvider.isEnabled(JsonSerializer.Feature.WRITE_OBJECT_ID_KEYS_UNQUOTED))
                .thenReturn(true);
        when(serializerProvider.isEnabled(JsonSerializer.Feature.WRITE_NULL_SERIALIZERS)).thenReturn(true);

        ObjectIdWriter objectIdWriter = new ObjectIdWriter(null, null, null, null, null, null);
        objectIdWriter.propertyName = new SerializableString("testPropertyName");

        JsonGenerator jsonGenerator = null;
        writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);
    }

    @Test(expected = JsonMappingException.class)
    public void testWriteAsIdWithNullObjectIdGenerator() throws IOException, JsonMappingException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);

        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        when(serializerProvider.isEnabled(JsonSerializer.Feature.WRITE_OBJECT_ID_KEYS_UNQUOTED))
                .thenReturn(true);
        when(serializerProvider.isEnabled(JsonSerializer.Feature.WRITE_NULL_SERIALIZERS)).thenReturn(true);

        ObjectIdWriter objectIdWriter = new ObjectIdWriter(null, null, null, null, null, null);
        objectIdWriter.propertyName = new SerializableString("testPropertyName");

        WritableObjectId writableObjectId = new WritableObjectId(null);
        writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);
    }
}