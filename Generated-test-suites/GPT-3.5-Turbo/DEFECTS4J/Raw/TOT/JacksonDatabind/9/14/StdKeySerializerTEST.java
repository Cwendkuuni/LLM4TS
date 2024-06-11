
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

public class StdKeySerializerTest {
    
    private StdKeySerializer serializer;
    private JsonGenerator mockJsonGenerator;
    private SerializerProvider mockSerializerProvider;
    
    @Before
    public void setUp() {
        serializer = new StdKeySerializer();
        mockJsonGenerator = mock(JsonGenerator.class);
        mockSerializerProvider = mock(SerializerProvider.class);
    }
    
    @Test
    public void serialize_ShouldSerializeDateAsDefaultDateKey() throws IOException {
        // Arrange
        Date date = new Date();
        
        // Act
        serializer.serialize(date, mockJsonGenerator, mockSerializerProvider);
        
        // Assert
        verify(mockSerializerProvider).defaultSerializeDateKey(date, mockJsonGenerator);
    }
    
    @Test
    public void serialize_ShouldSerializeNonDateObjectAsString() throws IOException {
        // Arrange
        Object obj = new Object();
        String expectedString = obj.toString();
        
        // Act
        serializer.serialize(obj, mockJsonGenerator, mockSerializerProvider);
        
        // Assert
        verify(mockJsonGenerator).writeFieldName(expectedString);
    }
    
    @Test
    public void getSchema_ShouldReturnStringSchemaNode() throws JsonMappingException {
        // Act
        JsonNode schemaNode = serializer.getSchema(mockSerializerProvider, null);
        
        // Assert
        assertEquals("string", schemaNode.asText());
    }
    
    @Test
    public void acceptJsonFormatVisitor_ShouldExpectStringFormat() throws JsonMappingException {
        // Arrange
        JavaType typeHint = mock(JavaType.class);
        JsonFormatVisitorWrapper mockVisitor = mock(JsonFormatVisitorWrapper.class);
        
        // Act
        serializer.acceptJsonFormatVisitor(mockVisitor, typeHint);
        
        // Assert
        verify(mockVisitor).expectStringFormat(typeHint);
    }
}
