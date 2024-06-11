package com.fasterxml.jackson.databind.ser.std;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Date;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

public class StdKeySerializerTest {

    @Mock private JsonGenerator mockJsonGenerator;
    @Mock private SerializerProvider mockSerializerProvider;
    @Mock private JsonFormatVisitorWrapper mockJsonFormatVisitorWrapper;

    private StdKeySerializer stdKeySerializer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        stdKeySerializer = new StdKeySerializer();
    }

    @Test
    public void testSerialize_WhenValueIsDate_ShouldCallDefaultSerializeDateKey() throws IOException {
        // Arrange
        Date date = new Date();

        // Act
        stdKeySerializer.serialize(date, mockJsonGenerator, mockSerializerProvider);

        // Assert
        verify(mockSerializerProvider).defaultSerializeDateKey(date, mockJsonGenerator);
    }

    @Test
    public void testSerialize_WhenValueIsNotDate_ShouldWriteFieldName() throws IOException {
        // Arrange
        String str = "testString";
        Object obj = str;

        // Act
        stdKeySerializer.serialize(obj, mockJsonGenerator, mockSerializerProvider);

        // Assert
        verify(mockJsonGenerator).writeFieldName(str);
    }

    @Test
    public void testGetSchema_ShouldReturnStringSchemaNode() throws JsonMappingException {
        // Arrange

        // Act
        JsonNode schemaNode = stdKeySerializer.getSchema(mockSerializerProvider, null);

        // Assert
        assertEquals("string", schemaNode.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor_ShouldExpectStringFormat() throws JsonMappingException {
        // Arrange

        // Act
        stdKeySerializer.acceptJsonFormatVisitor(mockJsonFormatVisitorWrapper, null);

        // Assert
        verify(mockJsonFormatVisitorWrapper).expectStringFormat(null);
    }
}