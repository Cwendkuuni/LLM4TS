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
import com.fasterxml.jackson.databind.json.JsonMapperFactory;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.ser.impl.WritableObjectId;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.ObjectIdWriter;

/**
 * Test class for WritableObjectId class
 */
public class WritableObjectIdTest {

    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;
    private ObjectIdGenerator<Object> idGenerator;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        idGenerator = mock(ObjectIdGenerator.class);
        serializerProvider = mock(SerializerProvider.class);
        jsonGenerator = mock(JsonGenerator.class);
        objectMapper = new ObjectMapper(JsonMapperFactory.getMapperInstance());
        objectMapper.setSerializerProvider(serializerProvider);
    }

    @Test
    public void testWritableObjectIdCreation() {
        ObjectIdGenerator<Object> generator = new ObjectIdGeneratorBase() {
            @Override
            public Object generateId(Object idWritable) {
                return new Object() {
                };
            }
        };
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        assertNotNull(writableObjectId);
        assertNotNull(writableObjectId.generator);
        assertNull(writableObjectId.id);
        assertFalse(writableObjectId.idWritten);
    }

    @Test
    public void testWriteAsIdTrue() throws IOException {
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);
        ObjectIdGenerator<Object> generator = mock(ObjectIdGenerator.class);
        when(generator.generateId(anyObject())).thenReturn(123L);
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        writableObjectId.id = 123L;
        when(serializerProvider.getNullValueSerializer()).thenReturn(mock(StdSerializer.class));

        boolean result = writableObjectId.writeAsId(jsonGenerator, serializerProvider, new ObjectIdWriter());
        assertTrue(result);

        verify(jsonGenerator).writeObjectRef("123");
    }

    @Test
    public void testWriteAsIdFalse() throws IOException {
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);
        ObjectIdGenerator<Object> generator = mock(ObjectIdGenerator.class);
        when(generator.generateId(anyObject())).thenReturn("testId");
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        writableObjectId.id = "testId";

        boolean result = writableObjectId.writeAsId(jsonGenerator, serializerProvider, new ObjectIdWriter());
        assertFalse(result);

        verify(serializerProvider, times(1)).findValueSerializer(String.class);
    }

    @Test(expected = JsonMappingException.class)
    public void testWriteAsIdNullGenerator() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(null);
        writableObjectId.id = "testId";
        writableObjectId.writeAsId(jsonGenerator, serializerProvider, new ObjectIdWriter());
    }

    @Test
    public void testGenerateId() {
        when(idGenerator.generateId(anyObject())).thenReturn(123L);
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        Object id = writableObjectId.generateId(new Object());
        assertEquals(123L, id);
    }

    @Test
    public void testWriteAsField() throws IOException {
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);
        ObjectIdGenerator<Object> generator = mock(ObjectIdGenerator.class);
        when(generator.generateId(anyObject())).thenReturn("testId");
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        writableObjectId.id = "testId";

        SerializableString propertyName = new SerializableString("propertyName");
        ObjectIdWriter objectIdWriter = new ObjectIdWriter() {
            @Override
            public String getPropertyName() {
                return propertyName.toString();
            }
        };
        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);

        verify(jsonGenerator).writeFieldName("propertyName");
        verify(serializerProvider).findValueSerializer(String.class);
    }

    @Test
    public void testWriteAsFieldIdWritten() throws IOException {
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);
        ObjectIdGenerator<Object> generator = mock(ObjectIdGenerator.class);
        when(generator.generateId(anyObject())).thenReturn("testId");
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        writableObjectId.id = "testId";
        writableObjectId.idWritten = true;

        SerializableString propertyName = new SerializableString("propertyName");
        ObjectIdWriter objectIdWriter = new ObjectIdWriter() {
            @Override
            public String getPropertyName() {
                return propertyName.toString();
            }
        };
        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);

        verify(jsonGenerator, times(1)).writeFieldName("propertyName");
        verify(serializerProvider, times(1)).findValueSerializer(String.class);
        verify(jsonGenerator, times(1)).writeString("testId");
    }

    @Test
    public void testWriteAsFieldIdNotWritten() throws IOException {
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);
        ObjectIdGenerator<Object> generator = mock(ObjectIdGenerator.class);
        when(generator.generateId(anyObject())).thenReturn("testId");
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        writableObjectId.id = "testId";

        SerializableString propertyName = new SerializableString("propertyName");
        ObjectIdWriter objectIdWriter = new ObjectIdWriter() {
            @Override
            public String getPropertyName() {
                return propertyName.toString();
            }
        };
        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);

        verify(jsonGenerator, times(1)).writeFieldName("propertyName");
        verify(serializerProvider, times(1)).findValueSerializer(String.class);
        verifyNoMoreInteractions(jsonGenerator);
    }
}