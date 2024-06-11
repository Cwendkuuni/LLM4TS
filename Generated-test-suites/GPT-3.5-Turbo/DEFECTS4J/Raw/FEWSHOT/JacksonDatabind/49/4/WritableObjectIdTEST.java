package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.*;
import org.junit.Test;

public class WritableObjectIdTest {

    @Test
    public void testConstructor() {
        ObjectIdGenerator<?> generator = new ObjectIdGenerator<?>();
        WritableObjectId obj = new WritableObjectId(generator);
        assertEquals(generator, obj.generator);
        assertNull(obj.id);
        assertFalse(obj.idWritten);
    }

    @Test
    public void testWriteAsIdWithIdNotNullAndIdWrittenTrue() {
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        ObjectIdWriter w = new ObjectIdWriter();
        
        WritableObjectId obj = new WritableObjectId(new ObjectIdGenerator<?>());
        obj.id = "test";
        obj.idWritten = true;
        
        assertTrue(obj.writeAsId(gen, provider, w));
    }

    @Test
    public void testWriteAsIdWithIdNotNullAndIdWrittenFalseAndAlwaysAsIdTrue() {
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        ObjectIdWriter w = new ObjectIdWriter();
        w.alwaysAsId = true;
        
        WritableObjectId obj = new WritableObjectId(new ObjectIdGenerator<?>());
        obj.id = "test";
        obj.idWritten = false;
        
        assertTrue(obj.writeAsId(gen, provider, w));
    }
    
    @Test
    public void testWriteAsIdWithIdNotNullAndIdWrittenFalseAndAlwaysAsIdFalse() {
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        ObjectIdWriter w = new ObjectIdWriter();
        
        WritableObjectId obj = new WritableObjectId(new ObjectIdGenerator<?>());
        obj.id = "test";
        obj.idWritten = false;
        
        assertFalse(obj.writeAsId(gen, provider, w));
    }

    @Test
    public void testWriteAsIdWithIdNull() {
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        ObjectIdWriter w = new ObjectIdWriter();
        
        WritableObjectId obj = new WritableObjectId(new ObjectIdGenerator<?>());
        obj.id = null;
        
        assertFalse(obj.writeAsId(gen, provider, w));
    }

    @Test
    public void testGenerateId() {
        ObjectIdGenerator<?> generator = new ObjectIdGenerator<?>();
        WritableObjectId obj = new WritableObjectId(generator);
        Object forPojo = new Object();
        
        Object id = obj.generateId(forPojo);
        assertEquals(id, obj.id);
    }

    @Test
    public void testWriteAsFieldWithIdWrittenTrue() {
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        ObjectIdWriter w = new ObjectIdWriter();
        
        WritableObjectId obj = new WritableObjectId(new ObjectIdGenerator<?>());
        obj.id = "test";
        obj.idWritten = true;
        
        obj.writeAsField(gen, provider, w);
        assertEquals(obj.idWritten, true);
    }

    @Test
    public void testWriteAsFieldWithIdWrittenFalseAndCanWriteObjectIdTrue() {
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        ObjectIdWriter w = new ObjectIdWriter();
        
        WritableObjectId obj = new WritableObjectId(new ObjectIdGenerator<?>());
        obj.id = "test";
        obj.idWritten = false;
        
        gen.setCanWriteObjectId(true);
        
        obj.writeAsField(gen, provider, w);
        assertEquals(obj.idWritten, true);
    }

    @Test
    public void testWriteAsFieldWithIdWrittenFalseAndCanWriteObjectIdFalse() {
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        ObjectIdWriter w = new ObjectIdWriter();
        
        WritableObjectId obj = new WritableObjectId(new ObjectIdGenerator<?>());
        obj.id = "test";
        obj.idWritten = false;
        
        gen.setCanWriteObjectId(false);
        
        obj.writeAsField(gen, provider, w);
        assertEquals(obj.idWritten, true);
    }

    @Test
    public void testWriteAsFieldWithIdWrittenFalseAndPropertyNameNotNull() {
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        ObjectIdWriter w = new ObjectIdWriter();
        
        WritableObjectId obj = new WritableObjectId(new ObjectIdGenerator<?>());
        obj.id = "test";
        obj.idWritten = false;
        w.propertyName = "id";
        
        obj.writeAsField(gen, provider, w);
        assertEquals(obj.idWritten, true);
    }

    @Test
    public void testWriteAsFieldWithIdWrittenFalseAndPropertyNameNull() {
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        ObjectIdWriter w = new ObjectIdWriter();
        
        WritableObjectId obj = new WritableObjectId(new ObjectIdGenerator<?>());
        obj.id = "test";
        obj.idWritten = false;
        w.propertyName = null;
        
        obj.writeAsField(gen, provider, w);
        assertEquals(obj.idWritten, true);
    }

}