import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class StdKeySerializerTest {
    
    private StdKeySerializer serializer;
    private JsonGenerator mockJsonGenerator;
    private SerializerProvider mockSerializerProvider;
    
    @Before
    public void setUp() throws Exception {
        serializer = new StdKeySerializer();
        mockJsonGenerator = mock(JsonGenerator.class);
        mockSerializerProvider = mock(SerializerProvider.class);
    }
    
    @Test
    public void testSerialize_WhenValueIsDate_ShouldCallDefaultSerializeDateKey() throws IOException {
        // Arrange
        Date value = new Date();
        
        // Act
        serializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        
        // Assert
        verify(mockSerializerProvider, times(1)).defaultSerializeDateKey(eq(value), eq(mockJsonGenerator));
        verify(mockJsonGenerator, never()).writeFieldName(anyString());
    }
    
    @Test
    public void testSerialize_WhenValueIsNotDate_ShouldWriteFieldName() throws IOException {
        // Arrange
        String value = "test";
        
        // Act
        serializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        
        // Assert
        verify(mockSerializerProvider, never()).defaultSerializeDateKey(any(Date.class), eq(mockJsonGenerator));
        verify(mockJsonGenerator, times(1)).writeFieldName(eq(value));
    }
    
    @Test
    public void testSerialize_WhenValueIsNull_ShouldWriteNullFieldName() throws IOException {
        // Arrange
        Object value = null;
        
        // Act
        serializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        
        // Assert
        verify(mockSerializerProvider, never()).defaultSerializeDateKey(any(Date.class), eq(mockJsonGenerator));
        verify(mockJsonGenerator, times(1)).writeFieldName(eq("null"));
    }
    
    @Test
    public void testGetSchema_ShouldReturnStringSchemaNode() throws JsonMappingException {
        // Arrange
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        ObjectNode expectedNode = JsonNodeFactory.instance.objectNode();
        expectedNode.put("type", "string");
        
        // Act
        JsonNode resultNode = serializer.getSchema(mockProvider, null);
        
        // Assert
        assertEquals(expectedNode, resultNode);
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_ShouldExpectStringFormat() throws JsonMappingException {
        // Arrange
        JsonFormatVisitorWrapper mockVisitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        
        // Act
        serializer.acceptJsonFormatVisitor(mockVisitor, typeHint);
        
        // Assert
        verify(mockVisitor, times(1)).expectStringFormat(eq(typeHint));
    }
}