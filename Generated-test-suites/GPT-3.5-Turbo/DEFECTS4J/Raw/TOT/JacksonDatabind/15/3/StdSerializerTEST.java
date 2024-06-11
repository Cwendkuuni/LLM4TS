import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Type;

import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class StdSerializerTest {

    @Test
    public void testConstructor_withClassParameter_createsSerializerWithHandledTypeSet() {
        Class<SomeClass> clazz = SomeClass.class;
        StdSerializer<SomeClass> serializer = new StdSerializer<SomeClass>(clazz) {
            @Override
            public void serialize(SomeClass value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
                
            }
        };
        
        assertEquals(clazz, serializer.handledType());
    }
    
    @Test
    public void testConstructor_withJavaTypeParameter_createsSerializerWithHandledTypeSet() {
        JavaType javaType = Mockito.mock(JavaType.class);
        Class<SomeClass> clazz = SomeClass.class;
        Mockito.when(javaType.getRawClass()).thenReturn(clazz);
        
        StdSerializer<SomeClass> serializer = new StdSerializer<SomeClass>(javaType) {
            @Override
            public void serialize(SomeClass value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
                
            }
        };
        
        assertEquals(clazz, serializer.handledType());
    }
    
    @Test
    public void testConstructor_withClassAndBooleanParameter_createsSerializerWithHandledTypeSet() {
        Class<SomeClass> clazz = SomeClass.class;
        StdSerializer<SomeClass> serializer = new StdSerializer<SomeClass>(clazz, true) {
            @Override
            public void serialize(SomeClass value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
                
            }
        };
        
        assertEquals(clazz, serializer.handledType());
    }
    
    @Test
    public void testSerialize_shouldSerializeObjectToJson() throws IOException, JsonGenerationException {
        JsonGenerator jgen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        
        SomeClass value = new SomeClass();
        StdSerializer<SomeClass> serializer = new StdSerializer<SomeClass>(SomeClass.class) {
            @Override
            public void serialize(SomeClass value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
                jgen.writeString("serialized");
            }
        };
        
        serializer.serialize(value, jgen, provider);
        
        Mockito.verify(jgen).writeString("serialized");
    }
    
    @Test
    public void testGetSchema_withOptionalFalse_shouldReturnSchemaNodeWithRequiredFieldSetToTrue()
            throws JsonMappingException {
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        Type typeHint = Mockito.mock(Type.class);
        
        SomeClass value = new SomeClass();
        StdSerializer<SomeClass> serializer = new StdSerializer<SomeClass>(SomeClass.class) {
            @Override
            public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
                ObjectNode schema = createSchemaNode("string");
                schema.put("property", "serialized");
                return schema;
            }
        };
        
        JsonNode schema = serializer.getSchema(provider, typeHint, false);
        
        assertEquals("string", schema.get("type").asText());
        assertTrue(schema.get("required").asBoolean());
        assertEquals("serialized", schema.get("property").asText());
    }
}