import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class StdKeySerializerTest {
    
    @Test
    public void testSerializeWithDate() {
        StdKeySerializer serializer = new StdKeySerializer();
        Date date = new Date();
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        
        try {
            serializer.serialize(date, mockGenerator, mockProvider);
            verify(mockProvider).defaultSerializeDateKey(date, mockGenerator);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }
    
    @Test
    public void testSerializeWithString() {
        StdKeySerializer serializer = new StdKeySerializer();
        String str = "test";
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        
        try {
            serializer.serialize(str, mockGenerator, mockProvider);
            verify(mockGenerator).writeFieldName(str);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }
    
    @Test
    public void testGetSchema() {
        StdKeySerializer serializer = new StdKeySerializer();
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        Type typeHint = String.class;
        
        try {
            JsonNode schemaNode = serializer.getSchema(mockProvider, typeHint);
            assertEquals("string", schemaNode.asText());
        } catch (JsonMappingException e) {
            fail("Unexpected JsonMappingException");
        }
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() {
        StdKeySerializer serializer = new StdKeySerializer();
        JsonFormatVisitorWrapper mockVisitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        
        try {
            serializer.acceptJsonFormatVisitor(mockVisitor, typeHint);
            verify(mockVisitor).expectStringFormat(typeHint);
        } catch (JsonMappingException e) {
            fail("Unexpected JsonMappingException");
        }
    }
}