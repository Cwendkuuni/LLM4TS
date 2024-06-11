
package com.fasterxml.jackson.databind.ser.impl;

import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

public class WritableObjectIdTest {

    private WritableObjectId objectId;
    private ObjectIdGenerator<?> generator;
    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;
    private ObjectIdWriter objectIdWriter;

    @Before
    public void setUp() {
        generator = mock(ObjectIdGenerator.class);
        jsonGenerator = mock(JsonGenerator.class);
        serializerProvider = mock(SerializerProvider.class);
        objectIdWriter = mock(ObjectIdWriter.class);
        objectId = new WritableObjectId(generator);
    }

    @Test
    public void testWriteAsId_IdNotNullAndIdWritten_ExpectTrue() throws IOException {
        objectId.id = "1";
        objectId.idWritten = true;
        boolean result = objectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);

        verify(jsonGenerator, times(1)).writeObjectRef("1");
        verify(objectIdWriter, never()).getSerializer();
        Assert.assertTrue(result);
    }
    
    @Test
    public void testWriteAsId_IdNotNullAndIdNotWrittenButAlwaysAsId_ExpectTrue() throws IOException {
        objectId.id = "2";
        objectId.idWritten = false;
        when(objectIdWriter.alwaysAsId()).thenReturn(true);
        boolean result = objectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);

        verify(jsonGenerator, times(1)).writeObjectRef("2");
        verify(objectIdWriter, never()).getSerializer();
        Assert.assertTrue(result);
    }
    
    @Test
    public void testWriteAsId_IdNotNullAndIdNotWrittenAndNotAlwaysAsId_ExpectFalse() throws IOException {
        objectId.id = "3";
        objectId.idWritten = false;
        when(objectIdWriter.alwaysAsId()).thenReturn(false);
        boolean result = objectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);

        verify(jsonGenerator, never()).writeObjectRef(any());
        verify(objectIdWriter, never()).getSerializer();
        Assert.assertFalse(result);
    }
    
    @Test
    public void testWriteAsId_IdNull_ExpectFalse() throws IOException {
        objectId.id = null;
        objectId.idWritten = true;
        boolean result = objectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);

        verify(jsonGenerator, never()).writeObjectRef(any());
        verify(objectIdWriter, never()).getSerializer();
        Assert.assertFalse(result);
    }
    
    @Test
    public void testGenerateId_ExpectIdToBeGenerated() {
        when(generator.generateId(any())).thenReturn("4");
        
        Object generatedId = objectId.generateId(new Object());
        
        Assert.assertEquals("4", generatedId);
        Assert.assertEquals("4", objectId.id);
    }
    
    @Test
    public void testWriteAsField_CanWriteObjectId_ExpectObjectIdToBeWritten() throws IOException {
        objectId.id = "5";
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);
        
        objectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
        
        verify(jsonGenerator, times(1)).writeObjectId(any());
        verify(objectIdWriter, never()).getSerializer();
    }
    
    @Test
    public void testWriteAsField_CannotWriteObjectId_ExpectFieldNameAndValueToBeWritten() throws IOException {
        objectId.id = "6";
        when(objectIdWriter.propertyName()).thenReturn(null);
        
        objectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
        
        verify(jsonGenerator, never()).writeObjectId(any());
        verify(objectIdWriter, never()).getSerializer();
        verify(jsonGenerator, never()).writeFieldName(any());
    }

}
