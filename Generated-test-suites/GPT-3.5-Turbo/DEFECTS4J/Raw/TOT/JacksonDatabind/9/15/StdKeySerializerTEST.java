package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeySerializerTest {

    @Test
    public void testSerialize_DateInput_SerializesDateKey() throws IOException {
        // Arrange
        StdKeySerializer serializer = new StdKeySerializer();
        Date inputDate = new Date();
        JsonGenerator jgen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        
        // Act
        serializer.serialize(inputDate, jgen, provider);
        
        // Assert
        Mockito.verify(provider).defaultSerializeDateKey(inputDate, jgen);
        Mockito.verify(jgen, Mockito.never()).writeFieldName(anyString());
    }
    
    @Test
    public void testSerialize_NonDateInput_SerializesStringKey() throws IOException {
        // Arrange
        StdKeySerializer serializer = new StdKeySerializer();
        Object nonDateInput = "Test";
        JsonGenerator jgen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        
        // Act
        serializer.serialize(nonDateInput, jgen, provider);
        
        // Assert
        Mockito.verify(provider, Mockito.never()).defaultSerializeDateKey(anyObject(), anyObject());
        Mockito.verify(jgen).writeFieldName(nonDateInput.toString());
    }
    
    @Test
    public void testSerialize_NullInput_ThrowsIOException() throws IOException {
        // Arrange
        StdKeySerializer serializer = new StdKeySerializer();
        Object nullInput = null;
        JsonGenerator jgen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        
        // Act & Assert
        assertThrows(IOException.class, () -> serializer.serialize(nullInput, jgen, provider));
    }
    
    @Test
    public void testSerialize_InvalidJsonGenerator_ThrowsIOException() throws IOException {
        // Arrange
        StdKeySerializer serializer = new StdKeySerializer();
        Object input = "Test";
        JsonGenerator jgen = null;
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        
        // Act & Assert
        assertThrows(IOException.class, () -> serializer.serialize(input, jgen, provider));
    }
    
    @Test
    public void testGetSchema_ReturnsStringSchemaNode() throws JsonMappingException {
        // Arrange
        StdKeySerializer serializer = new StdKeySerializer();
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        Type typeHint = Mockito.mock(Type.class);
        
        // Act
        JsonNode result = serializer.getSchema(provider, typeHint);
        
        // Assert
        assertEquals("string", result.asText());
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_VisitorExpectsStringFormat() throws JsonMappingException {
        // Arrange
        StdKeySerializer serializer = new StdKeySerializer();
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = Mockito.mock(JavaType.class);
        
        // Act
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        // Assert
        Mockito.verify(visitor).expectStringFormat(typeHint);
    }
}