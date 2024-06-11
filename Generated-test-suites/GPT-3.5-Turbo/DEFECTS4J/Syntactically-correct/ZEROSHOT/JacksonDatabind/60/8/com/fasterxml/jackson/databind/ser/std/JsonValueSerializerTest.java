package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.JsonValueSerializer;

public class JsonValueSerializerTest {
    
    private JsonValueSerializer jsonValueSerializer;
    
    @Before
    public void setUp() {
        jsonValueSerializer = new JsonValueSerializer();
    }
    
    @Test
    public void testCreateContextual() throws JsonMappingException {
        JsonSerializer<?> ser = null;
        BeanProperty property = null;
        
        JsonSerializer<?> result = jsonValueSerializer.createContextual(null, property);
        
        assertNull(result);
    }
    
    @Test
    public void testSerialize() throws IOException {
        Object bean = null;
        JsonGenerator gen = null;
        SerializerProvider prov = null;
        
        jsonValueSerializer.serialize(bean, gen, prov);   
    }
    
    @Test
    public void testSerializeWithType() throws IOException {
        Object bean = null;
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        TypeSerializer typeSer0 = null;
        
        jsonValueSerializer.serializeWithType(bean, gen, provider, typeSer0);
    }
    
    @Test
    public void testGetSchema() throws JsonMappingException {
        SerializerProvider provider = null;
        Type typeHint = null;
        
        JsonNode result = jsonValueSerializer.getSchema(provider, typeHint);
        
        assertNotNull(result);
        assertEquals(result, JsonSchema.getDefaultSchemaNode());
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        
        jsonValueSerializer.acceptJsonFormatVisitor(visitor, typeHint);
    }
    
    @Test
    public void testAcceptJsonFormatVisitorForEnum() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        Class<?> enumType = null;
        
        boolean result = jsonValueSerializer._acceptJsonFormatVisitorForEnum(visitor, typeHint, enumType);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsNaturalTypeWithStdHandling() {
        Class<?> rawType = null;
        JsonSerializer<?> ser = null;
        
        boolean result = jsonValueSerializer.isNaturalTypeWithStdHandling(rawType, ser);
        
        assertFalse(result);
    }
}