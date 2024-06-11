package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;

import org.junit.Before;
import org.junit.Test;

public class WritableObjectIdTest {
    private ObjectIdGenerator<?> idGenerator;
    private ObjectIdWriter idWriter;
    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;
    private TypeSerializer typeSerializer;
    private WritableObjectId writableObjectId;

    @Before
    public void setUp() throws Exception {
        idGenerator = mock(ObjectIdGenerator.class);
        idWriter = mock(ObjectIdWriter.class);
        jsonGenerator = mock(JsonGenerator.class);
        serializerProvider = mock(SerializerProvider.class);
        typeSerializer = mock(TypeSerializer.class);

        when(idGenerator.generateId(null)).thenReturn("generatedId");

        writableObjectId = new WritableObjectId(idGenerator);
    }

    @Test
    public void testWriteAsIdWithNullGenerator() {
        // Given
        WritableObjectId writableObjectId = new WritableObjectId(null);

        // When
        boolean result = writableObjectId.writeAsId(jsonGenerator, serializerProvider, idWriter);

        // Then
        assertFalse(result);
    }

    @Test
    public void testWriteAsIdWithIdAndGenerator() throws IOException {
        // Given
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);
        when(jsonGenerator.writeObjectId(String.valueOf("id"))).thenReturnVoid();

        // When
        boolean result = writableObjectId.writeAsId(jsonGenerator, serializerProvider, idWriter);

        // Then
        assertTrue(result);
    }

    @Test
    public void testWriteAsIdWithoutIdAndGenerator() throws IOException {
        // Given
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);

        // When
        boolean result = writableObjectId.writeAsId(jsonGenerator, serializerProvider, idWriter);

        // Then
        assertFalse(result);
    }

    @Test
    public void testWriteAsIdWithIdAndGeneratorAndAlwaysAsIdFalse() throws IOException {
        // Given
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);
        when(idWriter.alwaysAsId).thenReturn(false);

        // When
        boolean result = writableObjectId.writeAsId(jsonGenerator, serializerProvider, idWriter);

        // Then
        assertTrue(result);
    }

    @Test
    public void testWriteAsIdWithIdAndGeneratorAndAlwaysAsIdTrue() throws IOException {
        // Given
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);
        when(idWriter.alwaysAsId).thenReturn(true);

        // When
        boolean result = writableObjectId.writeAsId(jsonGenerator, serializerProvider, idWriter);

        // Then
        assertTrue(result);
    }

    @Test
    public void testGenerateId() throws JsonMappingException, IOException {
        // Given
        Object pojo = new Object();

        // When
        Object result = writableObjectId.generateId(pojo);

        // Then
        assertNotNull(result);
        assertEquals("generatedId", result.toString());
    }

    @Test
    public void testWriteAsFieldWithNullGenerator() {
        // Given
        WritableObjectId writableObjectId = new WritableObjectId(null);

        // When
        writableObjectId.writeAsField(jsonGenerator, serializerProvider, idWriter);

        // Then
        // Not executed as writeAsField requires a valid JsonGenerator
    }

    @Test
    public void testWriteAsFieldWithIdWrittenFalse() throws IOException {
        // Given
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);

        // When
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        writableObjectId.writeAsField(jsonGenerator, serializerProvider, idWriter);

        // Then
        assertFalse(writableObjectId.idWritten);
    }

    @Test
    public void testWriteAsFieldWithIdWrittenTrue() throws IOException {
        // Given
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);

        // When
        writableObjectId.writeAsField(jsonGenerator, serializerProvider, idWriter);

        // Then
        assertTrue(writableObjectId.idWritten);
    }

    @Test
    public void testWriteAsFieldWithNullPropertyName() throws IOException {
        // Given
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);
        SerializableString propertyName = null;

        // When
        writableObjectId.writeAsField(jsonGenerator, serializerProvider, new ObjectIdWriter() {{
            this.propertyName = propertyName;
        }});

        // Then
        assertNull(writableObjectId.id);
    }

    @Test
    public void testWriteAsFieldWithNonNullPropertyName() throws IOException {
        // Given
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);
        SerializableString propertyName = new SerializableString("propertyName");

        // When
        writableObjectId.writeAsField(jsonGenerator, serializerProvider, new ObjectIdWriter() {{
            this.propertyName = propertyName;
        }});

        // Then
        assertNotNull(writableObjectId.id);
        assertEquals("generatedId", writableObjectId.id.toString());
    }

    @Test
    public void testWriteAsFieldWithIdWrittenAndNullPropertyName() throws IOException {
        // Given
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);
        when(idWriter.propertyName).thenReturn(null);

        // When
        writableObjectId.writeAsField(jsonGenerator, serializerProvider, idWriter);

        // Then
        assertTrue(writableObjectId.idWritten);
        assertNull(writableObjectId.id);
    }

    @Test
    public void testWriteAsFieldWithIdWrittenAndNonNullPropertyName() throws IOException {
        // Given
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);
        when(idWriter.propertyName).thenReturn(new SerializableString("propertyName"));

        // When
        writableObjectId.writeAsField(jsonGenerator, serializerProvider, idWriter);

        // Then
        assertTrue(writableObjectId.idWritten);
        assertNotNull(writableObjectId.id);
        assertEquals("generatedId", writableObjectId.id.toString());
    }
}