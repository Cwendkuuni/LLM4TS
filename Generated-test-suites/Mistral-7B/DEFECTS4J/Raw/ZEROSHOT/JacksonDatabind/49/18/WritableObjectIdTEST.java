package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectIdGenerator.IdIntGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

/**
 * Test class for WritableObjectId class.
 */
public class WritableObjectIdTest {

    @Test
    public void testWritableObjectId() throws JsonProcessingException, IOException {
        ObjectIdGenerator<Object> idGenerator = new IdIntGenerator();
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);

        // Test generateId method
        Object id = writableObjectId.generateId(new Object());
        assertNotNull(id);

        // Test writeAsId method
        JsonGenerator jsonGenerator = createMockJsonGenerator();
        SerializerProvider serializerProvider = createMockSerializerProvider();
        ObjectIdWriter objectIdWriter = createMockObjectIdWriter();
        assertEquals(true, writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));

        // Test writeAsField method
        jsonGenerator = createMockJsonGenerator();
        serializerProvider = createMockSerializerProvider();
        objectIdWriter = createMockObjectIdWriter();
        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
        assertEquals(true, objectIdWriter.wasWritten);

        // Test writeAsId method with null id
        writableObjectId = new WritableObjectId(idGenerator);
        assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));

        // Test writeAsId method with null generator
        writableObjectId = new WritableObjectId(null);
        assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));

        // Test writeAsId method with null jsonGenerator
        writableObjectId = new WritableObjectId(idGenerator);
        assertFalse(writableObjectId.writeAsId(null, serializerProvider, objectIdWriter));

        // Test writeAsId method with null serializerProvider
        writableObjectId = new WritableObjectId(idGenerator);
        assertFalse(writableObjectId.writeAsId(jsonGenerator, null, objectIdWriter));

        // Test writeAsId method with null objectIdWriter
        writableObjectId = new WritableObjectId(idGenerator);
        assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, null));

        // Test generateId method with null forPojo
        writableObjectId = new WritableObjectId(idGenerator);
        try {
            writableObjectId.generateId(null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Method 'generateId' cannot be invoked with null 'forPojo' argument", e.getMessage());
        }
    }

    private JsonGenerator createMockJsonGenerator() {
        return mock(JsonGenerator.class);
    }

    private SerializerProvider createMockSerializerProvider() {
        return mock(SerializerProvider.class);
    }

    private ObjectIdWriter createMockObjectIdWriter() {
        return mock(ObjectIdWriter.class);
    }

}

class MockObjectIdWriter {
    boolean wasWritten = false;
}