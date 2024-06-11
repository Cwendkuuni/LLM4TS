import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.util.Date;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

public class StdKeySerializerTest {
    
    @Test
    public void testSerialize_DateValue_Success() throws IOException {
        // Arrange
        StdKeySerializer serializer = new StdKeySerializer();
        JsonGenerator jgen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        Date date = new Date();
        
        // Act
        serializer.serialize(date, jgen, provider);
        
        // Assert
        String expectedValue = provider.defaultSerializeDateKey(date, jgen);
        assertEquals(expectedValue, jgen.getCurrentName());
    }
    
    @Test
    public void testSerialize_OtherValue_Success() throws IOException {
        // Arrange
        StdKeySerializer serializer = new StdKeySerializer();
        JsonGenerator jgen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        Object value = "test";
        
        // Act
        serializer.serialize(value, jgen, provider);
        
        // Assert
        assertEquals(value.toString(), jgen.getCurrentName());
    }
    
    @Test
    public void testGetSchema_Success() throws JsonMappingException {
        // Arrange
        StdKeySerializer serializer = new StdKeySerializer();
        SerializerProvider provider = new SerializerProvider();
        Class<?> typeHint = Object.class;
        
        // Act
        JsonNode schema = serializer.getSchema(provider, typeHint);
        
        // Assert
        assertNotNull(schema);
        assertEquals("string", schema.asText());
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_Success() throws JsonMappingException {
        // Arrange
        StdKeySerializer serializer = new StdKeySerializer();
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();
        
        // Act
        serializer.acceptJsonFormatVisitor(visitor, JavaType.typeHint(Object.class));
        
        // Assert
        assertTrue(visitor.hasStringFormat());
    }
}