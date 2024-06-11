package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

public class StdKeySerializerTest {
    
    private StdKeySerializer serializer;
    
    @Mock
    private JsonGenerator mockGenerator;
    
    @Mock
    private SerializerProvider mockProvider;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        serializer = new StdKeySerializer();
    }
    
    @Test
    public void testSerialize_DateValue_ShouldCallDefaultSerializeDateKey() throws IOException {
        // Arrange
        Date date = new Date();
        
        // Act
        serializer.serialize(date, mockGenerator, mockProvider);
        
        // Assert
        verify(mockProvider).defaultSerializeDateKey(date, mockGenerator);
    }
    
    @Test
    public void testSerialize_NonDateValue_ShouldCallWriteFieldName() throws IOException {
        // Arrange
        Object value = "test";
        
        // Act
        serializer.serialize(value, mockGenerator, mockProvider);
        
        // Assert
        verify(mockGenerator).writeFieldName((String) value);
    }
    
    @Test
    public void testGetSchema_ShouldReturnStringSchemaNode() throws JsonMappingException {
        // Act
        JsonNode schemaNode = serializer.getSchema(mockProvider, null);
        
        // Assert
        assertNotNull(schemaNode);
        assertEquals("string", schemaNode.asText());
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_ShouldExpectStringFormat() throws JsonMappingException {
        // Arrange
        JavaType typeHint = mock(JavaType.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        
        // Act
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        // Assert
        verify(visitor).expectStringFormat(typeHint);
    }
}