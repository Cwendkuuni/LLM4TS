package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;
import com.fasterxml.jackson.databind.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import static org.mockito.Mockito.*;

public class StdKeySerializerTest {
    
    @Test
    public void testSerialize_WhenValueIsDate_ShouldCallDefaultSerializeDateKeyMethod() throws IOException {
        // Arrange
        JsonGenerator jgen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Date value = new Date();
        StdKeySerializer serializer = new StdKeySerializer();
        
        // Act
        serializer.serialize(value, jgen, provider);
        
        // Assert
        verify(provider).defaultSerializeDateKey(value, jgen);
    }
    
    @Test
    public void testSerialize_WhenValueIsNotDate_ShouldWriteFieldName() throws IOException {
        // Arrange
        JsonGenerator jgen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Object value = new Object();
        StdKeySerializer serializer = new StdKeySerializer();
        
        // Act
        serializer.serialize(value, jgen, provider);
        
        // Assert
        verify(jgen).writeFieldName(value.toString());
    }
    
    @Test
    public void testGetSchema_ShouldCreateSchemaNodeWithStringType() throws JsonMappingException {
        // Arrange
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);
        StdKeySerializer serializer = new StdKeySerializer();
        
        // Act
        ObjectNode result = serializer.getSchema(provider, typeHint);
        
        // Assert
        assertEquals("string", result.get("type").asText());
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_ShouldExpectStringFormat() throws JsonMappingException {
        // Arrange
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        StdKeySerializer serializer = new StdKeySerializer();
        
        // Act
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        // Assert
        verify(visitor).expectStringFormat(typeHint);
    }
}