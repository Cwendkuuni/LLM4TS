
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Date;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializer;

public class StdKeySerializerTest {
    
    @Mock
    private JsonGenerator jsonGenerator;
    
    @Mock
    private SerializerProvider serializerProvider;
    
    @Mock
    private JsonFormatVisitorWrapper jsonFormatVisitorWrapper;
    
    private StdKeySerializer stdKeySerializer;
    
    public void setup() {
        MockitoAnnotations.initMocks(this);
        stdKeySerializer = new StdKeySerializer();
    }
    
    @Test
    public void testSerialize_shouldSerializeDateAsDefaultSerializeDateKey() throws IOException {
        // Arrange
        Date date = new Date();
        
        // Act
        stdKeySerializer.serialize(date, jsonGenerator, serializerProvider);
        
        // Assert
        Mockito.verify(serializerProvider, Mockito.times(1)).defaultSerializeDateKey(date, jsonGenerator);
    }
    
    @Test
    public void testSerialize_shouldWriteFieldNameOfStringValue() throws IOException {
        // Arrange
        String value = "test";
        
        // Act
        stdKeySerializer.serialize(value, jsonGenerator, serializerProvider);
        
        // Assert
        Mockito.verify(jsonGenerator, Mockito.times(1)).writeFieldName(value);
    }
    
    @Test
    public void testGetSchema_shouldReturnSchemaNodeWithStringType() throws JsonMappingException {
        // Act
        JsonNode schemaNode = stdKeySerializer.getSchema(serializerProvider, null);
        
        // Assert
        assertEquals("string", schemaNode.getNodeType().toString());
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_shouldExpectStringFormat() throws JsonMappingException {
        // Act
        stdKeySerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, null);
        
        // Assert
        Mockito.verify(jsonFormatVisitorWrapper, Mockito.times(1)).expectStringFormat(null);
    }
    
    // Edge case test
    
    @Test
    public void testSerialize_shouldWriteEmptyStringIfValueIsNull() throws IOException {
        // Act
        stdKeySerializer.serialize(null, jsonGenerator, serializerProvider);
        
        // Assert
        Mockito.verify(jsonGenerator, Mockito.times(1)).writeFieldName("");
    }
    
    // Exception handling tests
    
    @Test
    public void testSerialize_shouldThrowIOExceptionIfSerializeDateKeyThrowsIOException() throws IOException {
        // Arrange
        Date date = new Date();
        Mockito.doThrow(new IOException()).when(serializerProvider).defaultSerializeDateKey(date, jsonGenerator);
        
        try {
            // Act
            stdKeySerializer.serialize(date, jsonGenerator, serializerProvider);
            
            // Assert
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Pass
            assertTrue(true);
        }
    }
    
    @Test
    public void testGetSchema_shouldThrowJsonMappingExceptionIfCreateSchemaNodeThrowsJsonMappingException() throws JsonMappingException {
        // Arrange
        Mockito.doThrow(new JsonMappingException("Test exception")).when(serializerProvider).createSchemaNode("string");
        
        try {
            // Act
            stdKeySerializer.getSchema(serializerProvider, null);
            
            // Assert
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            // Pass
            assertTrue(true);
        }
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_shouldThrowJsonMappingExceptionIfExpectStringFormatThrowsJsonMappingException() throws JsonMappingException {
        // Arrange
        Mockito.doThrow(new JsonMappingException("Test exception")).when(jsonFormatVisitorWrapper).expectStringFormat(null);
        
        try {
            // Act
            stdKeySerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, null);
            
            // Assert
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            // Pass
            assertTrue(true);
        }
    }
}
