import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class StdKeySerializerTest {

    @Test
    public void testSerialize_DateValue_Success() throws IOException {
        // Arrange
        Object value = new Date();
        JsonGenerator jgen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        
        StdKeySerializer serializer = new StdKeySerializer();
        
        // Act
        serializer.serialize(value, jgen, provider);
        
        // Assert
        verify(provider, times(1)).defaultSerializeDateKey((Date) value, jgen);
        verify(jgen, never()).writeFieldName(anyString());
    }

    @Test
    public void testSerialize_NonDateValue_Success() throws IOException {
        // Arrange
        Object value = "test";
        JsonGenerator jgen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        
        StdKeySerializer serializer = new StdKeySerializer();
        
        // Act
        serializer.serialize(value, jgen, provider);
        
        // Assert
        verify(provider, never()).defaultSerializeDateKey(any(Date.class), any(JsonGenerator.class));
        verify(jgen, times(1)).writeFieldName(value.toString());
    }

    @Test
    public void testSerialize_NullValue_Success() throws IOException {
        // Arrange
        Object value = null;
        JsonGenerator jgen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        
        StdKeySerializer serializer = new StdKeySerializer();
        
        // Act
        serializer.serialize(value, jgen, provider);
        
        // Assert
        verify(provider, never()).defaultSerializeDateKey(any(Date.class), any(JsonGenerator.class));
        verify(jgen, times(1)).writeFieldName(null);
    }

    @Test
    public void testGetSchema_Success() throws JsonMappingException {
        // Arrange
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);
        
        StdKeySerializer serializer = new StdKeySerializer();
        
        // Act
        JsonNode schema = serializer.getSchema(provider, typeHint);
        
        // Assert
        assertNotNull(schema);
        assertEquals("string", schema.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor_Success() throws JsonMappingException {
        // Arrange
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        
        StdKeySerializer serializer = new StdKeySerializer();
        
        // Act
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        // Assert
        verify(visitor, times(1)).expectStringFormat(typeHint);
    }
}