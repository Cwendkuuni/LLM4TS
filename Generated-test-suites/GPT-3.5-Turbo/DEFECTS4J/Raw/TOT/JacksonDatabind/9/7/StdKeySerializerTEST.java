import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.TextNode;

public class StdKeySerializerTest {
    
    private StdKeySerializer serializer;
    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;
    
    @Before
    public void setup() {
        serializer = new StdKeySerializer();
        jsonGenerator = mock(JsonGenerator.class);
        serializerProvider = mock(SerializerProvider.class);
    }

    @Test
    public void testSerializeWithStringValue() throws IOException {
        String value = "test";
        serializer.serialize(value, jsonGenerator, serializerProvider);
        
        verify(jsonGenerator).writeFieldName(value);
    }

    @Test
    public void testSerializeWithDateValue() throws IOException {
        Date value = new Date();
        serializer.serialize(value, jsonGenerator, serializerProvider);
        
        verify(serializerProvider).defaultSerializeDateKey(value, jsonGenerator);
    }

    @Test
    public void testSerializeWithNullValue() throws IOException {
        Object value = null;
        serializer.serialize(value, jsonGenerator, serializerProvider);
        
        verify(jsonGenerator).writeFieldName("null");
    }
    
    @Test
    public void testGetSchema() throws JsonProcessingException {
        TextNode expectedSchema = new TextNode("string");
        
        JsonNode schema = serializer.getSchema(serializerProvider, Object.class);
        
        assertEquals(expectedSchema, schema);
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() throws JsonProcessingException {
        JavaType typeHint = mock(JavaType.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        verify(visitor).expectStringFormat(typeHint);
    }
}