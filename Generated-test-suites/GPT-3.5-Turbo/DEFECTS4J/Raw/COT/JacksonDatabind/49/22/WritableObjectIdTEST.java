
package com.fasterxml.jackson.databind.ser.impl;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.WritableObjectId;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WritableObjectIdTest {
    @Test
    public void testWriteAsId() {
        WritableObjectId objectId = new WritableObjectId(mock(ObjectIdGenerator.class));
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        ObjectIdWriter objectIdWriter = mock(ObjectIdWriter.class);
        
        // Test case 1 - id is null
        Assert.assertFalse(objectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));

        // Test case 2 - id is not null and idWritten is false
        objectId.id = 1;
        Assert.assertTrue(objectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
        
        // Test case 3 - id is not null, idWritten is true and alwaysAsId is false
        objectId.id = 1;
        objectId.idWritten = true;
        objectIdWriter.alwaysAsId = false;
        Assert.assertFalse(objectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));

        // Test case 4 - id is not null, idWritten is true and alwaysAsId is true
        objectId.id = 1;
        objectId.idWritten = true;
        objectIdWriter.alwaysAsId = true;
        Assert.assertTrue(objectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
    }

    @Test
    public void testGenerateId() {
        WritableObjectId objectId = new WritableObjectId(mock(ObjectIdGenerator.class));

        // Test case 1 - id is null
        Object forPojo1 = new Object();
        objectId.generateId(forPojo1);
        Assert.assertNotNull(objectId.id);

        // Test case 2 - id is not null
        Object forPojo2 = new Object();
        objectId.id = 1;
        Assert.assertEquals(objectId.generateId(forPojo2), objectId.id);
    }

    @Test
    public void testWriteAsField() {
        WritableObjectId objectId = new WritableObjectId(mock(ObjectIdGenerator.class));
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        ObjectIdWriter objectIdWriter = mock(ObjectIdWriter.class);

        // Test case 1 - idWritten is false
        Assert.assertFalse(objectId.idWritten);
        objectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
        Assert.assertTrue(objectId.idWritten);

        // Test case 2 - idWritten is true and gen.canWriteObjectId() is true
        objectId.idWritten = true;
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);
        when(objectId.generator.generateId(any())).thenReturn(1);
        objectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
        verify(jsonGenerator).writeObjectId(eq("1"));

        // Test case 3 - idWritten is true, gen.canWriteObjectId() is false and w.propertyName is null
        objectId.idWritten = true;
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);
        when(objectIdWriter.propertyName()).thenReturn(null);
        objectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
        verify(jsonGenerator, never()).writeFieldName(any(SerializableString.class));
        verify(objectIdWriter, never()).serializer().serialize(any(), eq(jsonGenerator), eq(serializerProvider));

        // Test case 4 - idWritten is true, gen.canWriteObjectId() is false, and w.propertyName is not null
        objectId.idWritten = true;
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);
        when(objectIdWriter.propertyName()).thenReturn(mock(SerializableString.class));
        when(objectIdWriter.serializer()).thenReturn(mock(ObjectIdSerializer.class));
        objectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
        verify(jsonGenerator, times(1)).writeFieldName(any(SerializableString.class));
        verify(objectIdWriter.serializer()).serialize(any(), eq(jsonGenerator), eq(serializerProvider));
    }
}
