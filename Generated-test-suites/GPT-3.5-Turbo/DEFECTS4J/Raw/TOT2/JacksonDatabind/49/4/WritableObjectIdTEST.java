package com.fasterxml.jackson.databind.ser.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class WritableObjectIdTest {

    @Test
    public void testWriteAsId() {
        ObjectIdGenerator generator = new ObjectIdGenerator();
        WritableObjectId writableId = new WritableObjectId(generator);
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        ObjectIdWriter w = new ObjectIdWriter();

        assertFalse(writableId.writeAsId(gen, provider, w));
    }

    @Test
    public void testGenerateId() {
        ObjectIdGenerator generator = new ObjectIdGenerator();
        WritableObjectId writableId = new WritableObjectId(generator);
        Object forPojo = new Object();

        assertNotNull(writableId.generateId(forPojo));
    }

    @Test
    public void testWriteAsField() {
        ObjectIdGenerator generator = new ObjectIdGenerator();
        WritableObjectId writableId = new WritableObjectId(generator);
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        ObjectIdWriter w = new ObjectIdWriter();

        try {
            writableId.writeAsField(gen, provider, w);
        } catch (IOException e) {
            fail("IOException should not have been thrown");
        }
    }
}