package com.fasterxml.jackson.databind.ser.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.SerializerProvider;

public class WritableObjectIdTest {
    
    private ObjectIdGenerator<Object> generator;
    private WritableObjectId writableObjectId;
    private Object id;
    
    @Before
    public void setUp() {
        generator = Mockito.mock(ObjectIdGenerator.class);
        writableObjectId = new WritableObjectId(generator);
        id = new Object();
    }
    
    @Test
    public void testWriteAsIdWhenIdIsNullAndIdWrittenIsFalseAndAlwaysAsIdIsFalse() throws IOException {
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        ObjectIdWriter w = Mockito.mock(ObjectIdWriter.class);
        
        boolean result = writableObjectId.writeAsId(gen, provider, w);
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testWriteAsIdWhenIdIsNotNullAndIdWrittenIsFalseAndAlwaysAsIdIsFalseAndCanWriteObjectIdIsTrue() throws IOException {
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        ObjectIdWriter w = mockObjectIdWriter(false);
        
        Mockito.when(gen.canWriteObjectId()).thenReturn(true);
        Mockito.doNothing().when(gen).writeObjectRef(Mockito.anyString());
        
        boolean result = writableObjectId.writeAsId(gen, provider, w);
        
        Assert.assertTrue(result);
        Mockito.verify(gen).writeObjectRef(Mockito.anyString());
    }
    
    @Test
    public void testWriteAsIdWhenIdIsNotNullAndIdWrittenIsFalseAndAlwaysAsIdIsFalseAndCanWriteObjectIdIsFalse() throws IOException {
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        ObjectIdWriter w = mockObjectIdWriter(false);
        
        SerializableString name = Mockito.mock(SerializableString.class);
        Mockito.when(w.propertyName).thenReturn(name);
        Mockito.doNothing().when(gen).writeFieldName(Mockito.any(SerializableString.class));
        Mockito.doNothing().when(w.serializer).serialize(Mockito.any(), Mockito.any(JsonGenerator.class), Mockito.any(SerializerProvider.class));
        
        boolean result = writableObjectId.writeAsId(gen, provider, w);
        
        Assert.assertTrue(result);
        Mockito.verify(gen).writeFieldName(Mockito.any(SerializableString.class));
        Mockito.verify(w.serializer).serialize(Mockito.any(), Mockito.any(JsonGenerator.class), Mockito.any(SerializerProvider.class));
    }
    
    @Test
    public void testGenerateId() {
        Object forPojo = new Object();
        
        Mockito.when(generator.generateId(Mockito.any())).thenReturn(id);
        
        Object result = writableObjectId.generateId(forPojo);
        
        Assert.assertEquals(id, result);
        Assert.assertEquals(id, writableObjectId.id);
    }
    
    @Test
    public void testWriteAsField() throws IOException {
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        ObjectIdWriter w = mockObjectIdWriter(true);
        
        Mockito.doNothing().when(w.serializer).serialize(Mockito.any(), Mockito.any(JsonGenerator.class), Mockito.any(SerializerProvider.class));
        
        writableObjectId.writeAsField(gen, provider, w);
        
        Assert.assertTrue(writableObjectId.idWritten);
        Mockito.verify(w.serializer).serialize(Mockito.any(), Mockito.any(JsonGenerator.class), Mockito.any(SerializerProvider.class));
    }
    
    private ObjectIdWriter mockObjectIdWriter(boolean hasPropertyName) {
        ObjectIdWriter w = Mockito.mock(ObjectIdWriter.class);
        SerializableString name = hasPropertyName ? Mockito.mock(SerializableString.class) : null;
        
        Mockito.when(w.alwaysAsId).thenReturn(false);
        Mockito.when(w.propertyName).thenReturn(name);
        Mockito.when(w.serializer).thenReturn(Mockito.mock(Object.class));
        
        return w;
    }
}