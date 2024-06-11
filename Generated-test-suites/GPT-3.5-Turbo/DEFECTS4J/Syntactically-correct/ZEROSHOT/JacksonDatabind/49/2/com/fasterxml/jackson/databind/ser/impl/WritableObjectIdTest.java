
package com.fasterxml.jackson.databind.ser.impl;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class WritableObjectIdTest {

    private WritableObjectId writableObjectId;

    @Mock
    private ObjectIdGenerator<?> generator;

    @Mock
    private JsonGenerator gen;

    @Mock
    private SerializerProvider provider;

    @Mock
    private ObjectIdWriter writer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        writableObjectId = new WritableObjectId(generator);
    }

    @Test
    public void testWriteAsId_WithIdAndIdWritten_ReturnsTrue() throws Exception {
        Object id = new Object();
        writableObjectId.idWritten = true;
        writableObjectId.id = id;

        when(gen.canWriteObjectId()).thenReturn(true);

        writableObjectId.writeAsId(gen, provider, writer);

        verify(gen, times(1)).writeObjectRef(String.valueOf(id));
    }

    @Test
    public void testWriteAsId_WithIdAndAlwaysAsId_ReturnsTrue() throws Exception {
        Object id = new Object();
        writableObjectId.id = id;
        writer.alwaysAsId = true;

        when(gen.canWriteObjectId()).thenReturn(true);

        writableObjectId.writeAsId(gen, provider, writer);

        verify(gen, times(1)).writeObjectRef(String.valueOf(id));
    }

    @Test
    public void testWriteAsId_WithIdAndCannotWriteObjectId_ReturnsTrue() throws Exception {
        Object id = new Object();
        writableObjectId.id = id;
        writer.alwaysAsId = false;

        when(gen.canWriteObjectId()).thenReturn(false);

        writableObjectId.writeAsId(gen, provider, writer);

        verify(writer.serializer, times(1)).serialize(id, gen, provider);
    }

    @Test
    public void testWriteAsId_WithIdNull_ReturnsFalse() throws Exception {
        writableObjectId.id = null;

        boolean result = writableObjectId.writeAsId(gen, provider, writer);

        verifyZeroInteractions(gen, provider, writer.serializer);
        assert !result;
    }

    @Test
    public void testWriteAsId_WithIdWrittenFalse_ReturnsFalse() throws Exception {
        Object id = new Object();
        writableObjectId.id = id;
        writableObjectId.idWritten = false;

        boolean result = writableObjectId.writeAsId(gen, provider, writer);

        verifyZeroInteractions(gen, provider, writer.serializer);
        assert !result;
    }

    @Test
    public void testGenerateId() {
        Object forPojo = new Object();
        Object generatedId = new Object();
        when(generator.generateId(forPojo)).thenReturn(generatedId);

        Object result = writableObjectId.generateId(forPojo);

        assert result == generatedId;
        assert writableObjectId.id == generatedId;
    }

    @Test
    public void testWriteAsField_WithIdWrittenTrue_CallsWriteObjectId() throws Exception {
        Object id = new Object();
        writableObjectId.id = id;
        writableObjectId.idWritten = true;

        when(gen.canWriteObjectId()).thenReturn(true);

        writableObjectId.writeAsField(gen, provider, writer);

        verify(gen, times(1)).writeObjectId(String.valueOf(id));
        verifyZeroInteractions(writer.propertyName, writer.serializer);
    }

    @Test
    public void testWriteAsField_WithIdWrittenFalseAndPropertyNameNotNull_CallsWriteFieldNameAndSerialize() throws Exception {
        Object id = new Object();
        writableObjectId.id = id;
        writableObjectId.idWritten = false;
        writer.propertyName = mock(SerializableString.class);

        when(gen.canWriteObjectId()).thenReturn(false);
        when(writer.propertyName.getValue()).thenReturn("id");

        writableObjectId.writeAsField(gen, provider, writer);

        verify(gen, times(1)).writeFieldName(writer.propertyName);
        verify(writer.serializer, times(1)).serialize(id, gen, provider);
        verifyZeroInteractions(gen, writer.propertyName);
    }

    @Test
    public void testWriteAsField_WithIdWrittenFalseAndPropertyNameNull_DoesNothing() throws Exception {
        Object id = new Object();
        writableObjectId.id = id;
        writableObjectId.idWritten = false;
        writer.propertyName = null;

        writableObjectId.writeAsField(gen, provider, writer);

        verifyZeroInteractions(gen, writer.serializer);
    }
}

