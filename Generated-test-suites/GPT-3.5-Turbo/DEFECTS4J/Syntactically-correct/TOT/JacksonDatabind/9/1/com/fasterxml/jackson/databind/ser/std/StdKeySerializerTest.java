import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

public class StdKeySerializerTest {

    @Mock
    private JsonGenerator mockJsonGenerator;
    
    @Mock
    private SerializerProvider mockSerializerProvider;
    
    private StdKeySerializer stdKeySerializer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        stdKeySerializer = new StdKeySerializer();
    }

    @Test
    public void testSerialize_StringInput_ShouldWriteFieldName() throws IOException {
        // Arrange
        String value = "key";
        
        // Act
        stdKeySerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        
        // Assert
        verify(mockJsonGenerator).writeFieldName(value);
    }

    @Test
    public void testSerialize_DateInput_ShouldDefaultSerializeDateKey() throws IOException {
        // Arrange
        Date value = new Date();
        
        // Act
        stdKeySerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        
        // Assert
        verify(mockSerializerProvider).defaultSerializeDateKey(eq(value), eq(mockJsonGenerator));
    }

    @Test
    public void testGetSchema_ShouldReturnStringSchemaNode() throws JsonMappingException {
        // Arrange
        
        // Act
        JsonNode result = stdKeySerializer.getSchema(mockSerializerProvider, null);
        
        // Assert
        assertEquals("string", result.asText());
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_ShouldExpectStringFormat() throws JsonMappingException {
        // Arrange
        JavaType typeHint = mock(JavaType.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        
        // Act
        stdKeySerializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        // Assert
        verify(visitor).expectStringFormat(typeHint);
    }

}