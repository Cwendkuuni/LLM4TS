package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.*;
import org.junit.Test;

public class WritableObjectIdTest {
    @Test
    public void testWriteAsId() {
        // Test case 1
        WritableObjectId obj1 = new WritableObjectId(null);
        obj1.id = "123";
        JsonGenerator gen1 = new JsonGenerator();
        SerializerProvider provider1 = new SerializerProvider();
        ObjectIdWriter w1 = new ObjectIdWriter();
        w1.alwaysAsId = false;
        assertFalse(obj1.writeAsId(gen1, provider1, w1));

        // Test case 2
        WritableObjectId obj2 = new WritableObjectId(null);
        obj2.id = "456";
        obj2.idWritten = true;
        JsonGenerator gen2 = new JsonGenerator();
        SerializerProvider provider2 = new SerializerProvider();
        ObjectIdWriter w2 = new ObjectIdWriter();
        w2.alwaysAsId = true;
        assertTrue(obj2.writeAsId(gen2, provider2, w2));
    }

    @Test
    public void testGenerateId() {
        // Test case 1
        WritableObjectId obj1 = new WritableObjectId(null);
        Object forPojo1 = new Object();
        assertNull(obj1.generateId(forPojo1));

        // Test case 2
        WritableObjectId obj2 = new WritableObjectId(null);
        obj2.generator = new ObjectIdGenerator<>();
        Object forPojo2 = new Object();
        assertNotNull(obj2.generateId(forPojo2));
    }

    @Test
    public void testWriteAsField() {
        // Test case 1
        WritableObjectId obj1 = new WritableObjectId(null);
        obj1.id = "123";
        obj1.idWritten = true;
        JsonGenerator gen1 = new JsonGenerator();
        SerializerProvider provider1 = new SerializerProvider();
        ObjectIdWriter w1 = new ObjectIdWriter();
        w1.propertyName = null;
        obj1.writeAsField(gen1, provider1, w1);

        // Test case 2
        WritableObjectId obj2 = new WritableObjectId(null);
        obj2.id = "456";
        obj2.idWritten = true;
        JsonGenerator gen2 = new JsonGenerator();
        SerializerProvider provider2 = new SerializerProvider();
        ObjectIdWriter w2 = new ObjectIdWriter();
        w2.propertyName = new SerializableString();
        obj2.writeAsField(gen2, provider2, w2);
    }
}