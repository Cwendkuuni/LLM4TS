
package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class StdKeySerializerTest {

    @Mock
    private JsonGenerator jsonGenerator;

    @Mock
    private SerializerProvider serializerProvider;

    private StdKeySerializer stdKeySerializer;

    public StdKeySerializerTest() {
        MockitoAnnotations.initMocks(this);
        stdKeySerializer = new StdKeySerializer();
    }

    @Test
    public void testSerialize_DateObject_ShouldCallDefaultSerializeDateKeyMethod() throws IOException {
        // Arrange
        Date input = new Date();

        // Act
        stdKeySerializer.serialize(input, jsonGenerator, serializerProvider);

        // Assert
        verify(serializerProvider, times(1)).defaultSerializeDateKey(input, jsonGenerator);
    }

    @Test
    public void testSerialize_NotDateObject_ShouldCallWriteFieldNameMethod() throws IOException {
        // Arrange
        String input = "test";

        // Act
        stdKeySerializer.serialize(input, jsonGenerator, serializerProvider);

        // Assert
        verify(jsonGenerator, times(1)).writeFieldName(input);
    }

    @Test
    public void testGetSchema_ShouldReturnStringSchema() throws IOException {
        // Arrange
        String expectedSchema = "string";

        // Act
        JsonNode schema = stdKeySerializer.getSchema(serializerProvider, null);

        // Assert
        assertEquals(expectedSchema, schema.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor_ShouldExpectStringFormat() throws IOException {
        // Arrange
        JavaType javaType = mock(JavaType.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);

        // Act
        stdKeySerializer.acceptJsonFormatVisitor(visitor, javaType);

        // Assert
        verify(visitor, times(1)).expectStringFormat(javaType);
    }
}
