
package com.fasterxml.jackson.databind.ser.impl;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Test;
import java.io.IOException;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.SerializableString;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WritableObjectIdTest {

    @Test
    public void testWriteAsId() throws IOException {
        // Mock dependencies
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter writer = mock(ObjectIdWriter.class);
        ObjectIdGenerator generator = mock(ObjectIdGenerator.class);
        SerializableString name = mock(SerializableString.class);

        // Create test case object
        WritableObjectId objectId = new WritableObjectId(generator);
        
        // Set internal id values
        objectId.id = "123";
        objectId.idWritten = true;

        // Mock writer's alwaysAsId property
        when(writer.alwaysAsId).thenReturn(true);

        // Test method
        boolean result = objectId.writeAsId(gen, provider, writer);

        // Verify method calls and assertions
        verify(gen).canWriteObjectId();
        verify(gen).writeObjectRef("123");
        assertEquals(true, result);
    }

    @Test
    public void testGenerateId() {
        // Mock dependencies
        ObjectIdGenerator generator = mock(ObjectIdGenerator.class);
        Object forPojo = mock(Object.class);
        Object generatedId = mock(Object.class);

        // Create test case object
        WritableObjectId objectId = new WritableObjectId(generator);

        // Mock generator's generateId method
        when(generator.generateId(forPojo)).thenReturn(generatedId);

        // Test method
        Object result = objectId.generateId(forPojo);

        // Verify method call and assertion
        verify(generator).generateId(forPojo);
        assertEquals(generatedId, result);
    }

    @Test
    public void testWriteAsField() throws IOException {
        // Mock dependencies
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter writer = mock(ObjectIdWriter.class);
        SerializableString name = mock(SerializableString.class);

        // Create test case object
        WritableObjectId objectId = new WritableObjectId(ObjectIdGenerator.class);

        // Set internal id value
        objectId.id = "123";
        objectId.idWritten = true;

        // Test method
        objectId.writeAsField(gen, provider, writer);

        // Verify method calls
        verify(gen).writeObjectId("123");
        verify(writer).propertyName;
        verify(writer).serializer.serialize("123", gen, provider);
    }
}
