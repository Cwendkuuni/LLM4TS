
package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.databind.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class StdKeySerializerTest {
    @Mock
    private JsonGenerator jsonGenerator;

    @Mock
    private SerializerProvider serializerProvider;

    private StdKeySerializer keySerializer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        keySerializer = new StdKeySerializer();
    }

    @Test
    public void testSerialize_withDateValue_shouldCallDefaultSerializeDateKey() throws IOException {
        // Arrange
        Date dateValue = new Date();

        // Act
        keySerializer.serialize(dateValue, jsonGenerator, serializerProvider);

        // Assert
        verify(serializerProvider).defaultSerializeDateKey(dateValue, jsonGenerator);
    }

    @Test
    public void testSerialize_withNonDateValue_shouldWriteFieldName() throws IOException {
        // Arrange
        Object nonDateValue = "value";

        // Act
        keySerializer.serialize(nonDateValue, jsonGenerator, serializerProvider);

        // Assert
        verify(jsonGenerator).writeFieldName("value");
    }

    @Test
    public void testGetSchema_shouldReturnStringSchemaNode() throws JsonMappingException {
        // Arrange

        // Act
        JsonNode schemaNode = keySerializer.getSchema(serializerProvider, null);

        // Assert
        assertEquals("string", schemaNode.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor_shouldExpectStringFormat() throws JsonMappingException {
        // Arrange
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);

        // Act
        keySerializer.acceptJsonFormatVisitor(visitor, null);

        // Assert
        verify(visitor).expectStringFormat(any());
    }
}
