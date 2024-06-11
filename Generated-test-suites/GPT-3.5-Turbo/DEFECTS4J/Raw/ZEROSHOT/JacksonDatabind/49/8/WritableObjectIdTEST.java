package com.fasterxml.jackson.databind.ser.impl;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class WritableObjectIdTest {

    @Test
    public void testWriteAsId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = mapper.getSerializerProvider();
        ObjectIdWriter writer = new ObjectIdWriter(null, null, null, null);
        writer.alwaysAsId = true;

        WritableObjectId objectId = new WritableObjectId(null);
        objectId.id = new Object();

        Assert.assertTrue(objectId.writeAsId(gen, provider, writer));
    }

    @Test
    public void testGenerateId() {
        ObjectIdGenerator generator = new ObjectIdGenerator() {
            @Override
            public Object generateId(Object object) {
                return new Object();
            }

            @Override
            public Class<?> getScope() {
                return null;
            }

            @Override
            public boolean canUseFor(ObjectIdGenerator generator) {
                return false;
            }

            @Override
            public ObjectIdGenerator forScope(Class<?> scope) {
                return null;
            }

            @Override
            public ObjectIdGenerator newForSerialization(Object context) {
                return null;
            }
        };

        WritableObjectId objectId = new WritableObjectId(generator);
        Object forPojo = new Object();

        Assert.assertNotNull(objectId.generateId(forPojo));
    }

    @Test
    public void testWriteAsField() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = mapper.getSerializerProvider();
        ObjectIdWriter writer = new ObjectIdWriter(null, null, null, null);

        WritableObjectId objectId = new WritableObjectId(null);
        objectId.id = new Object();
        objectId.writeAsField(gen, provider, writer);
    }

}