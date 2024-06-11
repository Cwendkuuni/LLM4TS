package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdIdGeneratorType;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.JsonTestUtils;

import com.fasterxml.jackson.databind.annotation.JsonSerialize.Using;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Test class for WritableObjectId.
 */
public class WritableObjectIdTest
{
    private ObjectMapper objectMapper;
    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;
    private ObjectIdWriter objectIdWriter;
    private WritableObjectId writableObjectId;

    @Before
    public void setup() {
        objectMapper = new JsonMapper();
        jsonGenerator = mock(JsonGenerator.class);
        serializerProvider = mock(SerializerProvider.class);
        objectIdWriter = new ObjectIdWriter() {{
            propertyName = new SerializableString("propertyName");
            alwaysAsId = true;
        }};
        writableObjectId = new WritableObjectId(mockObjectIdGenerator());
    }

    @Test
    public void testConstructor() {
        assertNull(writableObjectId.id);
        assertFalse(writableObjectId.idWritten);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullGenerator() {
        new WritableObjectId(null);
    }

    @Test
    public void testWriteAsIdWithNullGenerator() throws IOException {
        writableObjectId = new WritableObjectId(null);
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);
        when(jsonGenerator.writeObjectRef(String.valueOf(writableObjectId.id))).thenThrow(IOException.class);
        when(objectIdWriter.serializer).thenReturn(new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                gen.writeString("value");
            }
        });

        assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
    }

    @Test
    public void testWriteAsIdWithNullId() throws IOException {
        writableObjectId.id = null;
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);

        assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
    }

    @Test
    public void testWriteAsIdWithWrittenId() throws IOException {
        writableObjectId.id = new Object();
        writableObjectId.idWritten = true;

        when(jsonGenerator.canWriteObjectId()).thenReturn(true);

        assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
    }

    @Test
    public void testWriteAsIdWithAlwaysAsId() throws IOException {
        writableObjectId.id = new Object();

        when(jsonGenerator.canWriteObjectId()).thenReturn(false);
        when(jsonGenerator.writeFieldName(objectIdWriter.propertyName)).thenThrow(IOException.class);
        when(objectIdWriter.serializer.serialize(writableObjectId.id, jsonGenerator, serializerProvider)).thenReturn(null);

        assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
    }

    @Test
    public void testWriteAsId() throws IOException {
        writableObjectId.id = new Object();

        when(jsonGenerator.canWriteObjectId()).thenReturn(true);

        assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, null));

        when(jsonGenerator.canWriteObjectId()).thenReturn(true);
        when(jsonGenerator.writeObjectRef(String.valueOf(writableObjectId.id))).thenReturn(null);
        when(objectIdWriter.serializer.serialize(writableObjectId.id, jsonGenerator, serializerProvider)).thenReturn(null);

        assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));

        when(jsonGenerator.writeObjectId(String.valueOf(writableObjectId.id))).thenReturn(null);

        assertTrue(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
    }

    @Test
    public void testGenerateIdWithNullForPojo() {
        assertNull(writableObjectId.generateId(null));
    }

    @Test
    public void testGenerateIdWithEmptyForPojo() {
        assertNull(writableObjectId.generateId(new Object()));
    }

    @Test
    public void testGenerateId() {
        assertSame(new Object(), writableObjectId.generateId(new Object()));
    }

    @Test
    public void testWriteAsFieldWithNullId() throws IOException {
        writableObjectId.id = null;

        when(jsonGenerator.canWriteObjectId()).thenReturn(true);
        when(jsonGenerator.writeObjectId(String.valueOf(writableObjectId.id))).thenThrow(IOException.class);
        when(objectIdWriter.serializer.serialize(writableObjectId.id, jsonGenerator, serializerProvider)).thenReturn(null);

        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);

        verify(jsonGenerator).writeFieldName(objectIdWriter.propertyName);
        verify(objectIdWriter.serializer).serialize(writableObjectId.id, jsonGenerator, serializerProvider);
    }

    @Test
    public void testWriteAsField() throws IOException {
        Object forPojo = new Object();
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);

        writableObjectId.id = new Object();

        when(objectIdWriter.serializer.serialize(writableObjectId.id, jsonGenerator, serializerProvider)).thenReturn(null);

        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);

        verify(jsonGenerator).writeFieldName(objectIdWriter.propertyName);
        verify(objectIdWriter.serializer).serialize(writableObjectId.id, jsonGenerator, serializerProvider);
    }

    @Test
    public void testWriteAsFieldWithIdWritten() throws IOException {
        Object forPojo = new Object();
        writableObjectId.id = new Object();
        writableObjectId.idWritten = true;

        when(jsonGenerator.canWriteObjectId()).thenReturn(false);

        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);

        verify(jsonGenerator, never()).writeObjectId(String.valueOf(writableObjectId.id));
        verify(jsonGenerator).writeFieldName(objectIdWriter.propertyName);
        verify(objectIdWriter.serializer).serialize(writableObjectId.id, jsonGenerator, serializerProvider);
    }

    @Test
    public void testWriteAsFieldWithIdPresent() throws IOException {
        Object forPojo = new Object();
        writableObjectId.id = new Object();

        when(jsonGenerator.canWriteObjectId()).thenReturn(true);

        when(jsonGenerator.writeObjectId(String.valueOf(writableObjectId.id))).thenReturn(null);

        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);

        verify(jsonGenerator, never()).writeObjectId(String.valueOf(writableObjectId.id));
        verify(jsonGenerator).writeFieldName(objectIdWriter.propertyName);
        verify(objectIdWriter.serializer).serialize(writableObjectId.id, jsonGenerator, serializerProvider);
    }

    @Test
    public void testWriteAsFieldWithNullPropertyName() {
        Object forPojo = new Object();
        writableObjectId.id = new Object();

        WritableObjectId writableObjectIdWithNullPropertyName = new WritableObjectId(mockObjectIdGenerator()) {{
            propertyName = null;
        }};

        writableObjectIdWithNullPropertyName.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);

        verify(jsonGenerator).writeFieldName(null);
        verify(objectIdWriter.serializer, never()).serialize(writableObjectIdWithNullPropertyName.id, jsonGenerator, serializerProvider);
    }

    @Test
    public void testToString() {
        assertSame("WritableObjectId(generator=null, id=null, idWritten=false)", writableObjectId.toString());

        writableObjectId = new WritableObjectId(mockObjectIdGenerator()) {{
            id = new Object();
            idWritten = true;
        }};

        assertSame("WritableObjectId(generator=" + mockObjectIdGenerator().getClass().getName() + ", id=" + writableObjectId.id + ", idWritten=true)", writableObjectId.toString());
    }

    private ObjectIdGenerator<?> mockObjectIdGenerator() {
        ObjectIdGenerator<Object> objectIdGenerator = mock(ObjectIdGenerator.class);
        when(objectIdGenerator.getTypeId()).thenReturn(IdIdGeneratorType.OBJECT_ID_GENERATOR_STRING);
        return objectIdGenerator;
    }

    @Test
    public void testFromJson() throws IOException {
        ObjectReader objectReader = objectMapper.readerForUpdating(WritableObjectId.class);

        String json = "{\"generator\":{\"className\":\"com.fasterxml.jackson.annotation.JsonIdentityInfo$IntIdGenerator\",\"idProperty\":\"id\"},\"id\":\"idValue\",\"idWritten\":false}";
        WritableObjectId writableObjectId = objectReader.readValue(new ObjectMapper().readTree(json));

        assertSame(JsonMappingException.ClassIdMismatch.class, writableObjectId.getClass());

        json = "{\"generator\":{\"className\":\"com.fasterxml.jackson.annotation.JsonIdentityInfo$IntIdGenerator\",\"idProperty\":\"id\"},\"id\":\"idValue\",\"idWritten\":true}";
        WritableObjectId writableObjectIdWritten = objectReader.readValue(new ObjectMapper().readTree(json));

        assertSame(WritableObjectId.class, writableObjectIdWritten.getClass());
        assertSame("idValue", writableObjectIdWritten.id);
        assertTrue(writableObjectIdWritten.idWritten);
    }
}