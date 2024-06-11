package com.fasterxml.jackson.databind.ser.impl;

import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class WritableObjectIdTest {

    @Test
    public void testWriteAsId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = new JsonFactory(mapper);
        JsonGenerator generator = jsonFactory.createGenerator(System.out);
        SerializerProvider provider = mapper.getSerializerProvider();

        // Test with id not being null
        WritableObjectId objectId = new WritableObjectId(new ObjectIdGenerator<Object>() {
            @Override
            public Object generateId(Object forPojo) {
                return 123L;
            }
        });
        objectId.id = 456L;
        ObjectIdWriter objectWriter = new ObjectIdWriter();

        boolean result = objectId.writeAsId(generator, provider, objectWriter);

        assertTrue(result);
    }

    @Test
    public void testGenerateId() throws Exception {
        // Test with generator returning id
        WritableObjectId objectId = new WritableObjectId(new ObjectIdGenerator<Object>() {
            @Override
            public Object generateId(Object forPojo) {
                return 123L;
            }
        });

        Object result = objectId.generateId(new Object());

        assertNotNull(result);
    }

    @Test
    public void testWriteAsField() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = new JsonFactory(mapper);
        JsonGenerator generator = jsonFactory.createGenerator(System.out);
        SerializerProvider provider = mapper.getSerializerProvider();

        // Test with id not being null
        WritableObjectId objectId = new WritableObjectId(new ObjectIdGenerator<Object>() {
            @Override
            public Object generateId(Object forPojo) {
                return 123L;
            }
        });
        objectId.id = 456L;
        ObjectIdWriter objectWriter = new ObjectIdWriter();

        objectId.writeAsField(generator, provider, objectWriter);
    }

}