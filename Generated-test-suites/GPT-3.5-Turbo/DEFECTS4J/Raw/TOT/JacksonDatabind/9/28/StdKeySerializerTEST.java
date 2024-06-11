package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

public class StdKeySerializerTest {
    
    private StdKeySerializer keySerializer;
    private JsonGenerator jsonGeneratorMock;
    private SerializerProvider serializerProviderMock;
    private JsonFormatVisitorWrapper jsonFormatVisitorMock;
    private Date date;
    
    @Before
    public void setUp() {
        keySerializer = new StdKeySerializer();
        jsonGeneratorMock = mock(JsonGenerator.class);
        serializerProviderMock = mock(SerializerProvider.class);
        jsonFormatVisitorMock = mock(JsonFormatVisitorWrapper.class);
        date = new Date();
    }
    
    @Test
    public void serialize_WhenValueIsDate_ShouldCallDefaultSerializeDateKey() throws IOException {
        // Arrange
        ArgumentCaptor<Date> dateCaptor = ArgumentCaptor.forClass(Date.class);
        
        // Act
        keySerializer.serialize(date, jsonGeneratorMock, serializerProviderMock);
        
        // Assert
        verify(serializerProviderMock, times(1)).defaultSerializeDateKey(dateCaptor.capture(), jsonGeneratorMock);
        assertEquals(date, dateCaptor.getValue());
    }
    
    @Test
    public void serialize_WhenValueIsNotDate_ShouldWriteFieldName() throws IOException {
        // Arrange
        String value = "test";
        
        // Act
        keySerializer.serialize(value, jsonGeneratorMock, serializerProviderMock);
        
        // Assert
        verify(jsonGeneratorMock, times(1)).writeFieldName(value);
    }
    
    @Test
    public void getSchema_ShouldReturnStringSchemaNode() throws IOException {
        // Arrange
        
        // Act
        JsonNode schemaNode = keySerializer.getSchema(serializerProviderMock, null);
        
        // Assert
        assertEquals("string", schemaNode.asText());
    }
    
    @Test
    public void acceptJsonFormatVisitor_ShouldExpectStringFormat() throws IOException {
        // Arrange
        
        // Act
        keySerializer.acceptJsonFormatVisitor(jsonFormatVisitorMock, null);
        
        // Assert
        verify(jsonFormatVisitorMock, times(1)).expectStringFormat(null);
    }
}