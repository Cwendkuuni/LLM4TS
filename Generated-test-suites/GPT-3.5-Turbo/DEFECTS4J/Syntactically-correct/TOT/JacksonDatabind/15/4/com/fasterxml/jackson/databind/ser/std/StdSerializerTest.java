import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class StdSerializerTest {

    @Mock
    private JsonSerializer<?> serializer;
    
    private StdSerializer<Object> stdSerializer;
    private Class<Object> handledType;
    
    @Before
    public void setup() {
        handledType = Object.class;
        stdSerializer = new StdSerializer<Object>(handledType) {
            private static final long serialVersionUID = 1L;
            
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException {
                // TODO Auto-generated method stub
                
            }

            @Override
            public JsonNode getSchema(SerializerProvider provider, java.lang.reflect.Type typeHint)
                    throws com.fasterxml.jackson.databind.JsonMappingException {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public JsonNode getSchema(SerializerProvider provider, java.lang.reflect.Type typeHint,
                    boolean isOptional) throws com.fasterxml.jackson.databind.JsonMappingException {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void acceptJsonFormatVisitor(com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper visitor,
                    JavaType typeHint) throws com.fasterxml.jackson.databind.JsonMappingException {
                // TODO Auto-generated method stub
                
            }
        };
    }
    
    @Test
    public void testHandledType() {
        Class<?> result = stdSerializer.handledType();
        
        assertEquals(handledType, result);
    }
    
    @Test
    public void testSerialize() throws IOException {
        Object value = new Object();
        JsonGenerator jgen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        
        stdSerializer.serialize(value, jgen, provider);
        
        verify(serializer).serialize(value, jgen, provider);
    }
    
    @Test
    public void testGetSchema() throws com.fasterxml.jackson.databind.JsonMappingException {
        SerializerProvider provider = mock(SerializerProvider.class);
        java.lang.reflect.Type typeHint = Object.class;
        
        JsonNode result = stdSerializer.getSchema(provider, typeHint);
        
        assertEquals(null, result);
    }
    
    @Test
    public void testGetSchemaWithOptional() throws com.fasterxml.jackson.databind.JsonMappingException {
    	SerializerProvider provider = mock(SerializerProvider.class);
        java.lang.reflect.Type typeHint = Object.class;
        boolean isOptional = true;
        
        ObjectNode schema = JsonNodeFactory.instance.objectNode();
        schema.put("type", "string");
        schema.put("required", !isOptional);
        
        JsonNode result = stdSerializer.getSchema(provider, typeHint, isOptional);
        
        assertEquals(schema, result);
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() throws com.fasterxml.jackson.databind.JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        
        stdSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        verify(visitor).expectAnyFormat(typeHint);
    }
}