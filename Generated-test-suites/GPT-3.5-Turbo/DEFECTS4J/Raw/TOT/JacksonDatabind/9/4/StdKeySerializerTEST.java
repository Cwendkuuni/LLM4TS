import static org.junit.Assert.assertEquals;
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

    private StdKeySerializer stdKeySerializer;
    
    @Mock
    private JsonGenerator mockJsonGenerator;
    
    @Mock
    private SerializerProvider mockSerializerProvider;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        stdKeySerializer = new StdKeySerializer();
    }
    
    @Test
    public void testSerialize_dateValue_successfulSerialization() throws IOException {
        // Arrange
        Date date = new Date();
        
        // Act
        stdKeySerializer.serialize(date, mockJsonGenerator, mockSerializerProvider);
        
        // Assert
        verify(mockSerializerProvider).defaultSerializeDateKey(date, mockJsonGenerator);
    }
    
    @Test
    public void testSerialize_nonDateValue_successfulSerialization() throws IOException {
        // Arrange
        Object value = "test";
        
        // Act
        stdKeySerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        
        // Assert
        verify(mockJsonGenerator).writeFieldName(value.toString());
    }
    
    @Test
    public void testGetSchema_successfulSchemaRetrieval() throws JsonMappingException {
        // Arrange
        
        // Act
        JsonNode schema = stdKeySerializer.getSchema(mockSerializerProvider, null);
        
        // Assert
        assertEquals("string", schema.asText());
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_successfulVisitorExpectation() throws JsonMappingException {
        // Arrange
        JavaType typeHint = mock(JavaType.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        
        // Act
        stdKeySerializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        // Assert
        verify(visitor).expectStringFormat(typeHint);
    }
}