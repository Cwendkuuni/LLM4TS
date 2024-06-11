package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;

public class AsWrapperTypeDeserializerTest {
    
    private AsWrapperTypeDeserializer deserializer;
    
    private JsonParser jp;
    
    private DeserializationContext ctxt;
    
    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
        jp = new JsonFactory().createParser(new StringReader("{\"type\":\"type1\",\"value\":\"value1\"}"));
        ctxt = mapper.getDeserializationContext();
        
        Object result = deserializer.deserializeTypedFromObject(jp, ctxt);
        
        assertNotNull(result);
    }
    
    @Test
    public void testDeserializeTypedFromArray() throws IOException {
        deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
        jp = new JsonFactory().createParser(new StringReader("{\"type\":\"type2\",\"value\":\"value2\"}"));
        ctxt = mapper.getDeserializationContext();
        
        Object result = deserializer.deserializeTypedFromArray(jp, ctxt);
        
        assertNotNull(result);
    }
    
    @Test
    public void testDeserializeTypedFromScalar() throws IOException {
        deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
        jp = new JsonFactory().createParser(new StringReader("{\"type\":\"type3\",\"value\":\"value3\"}"));
        ctxt = mapper.getDeserializationContext();
        
        Object result = deserializer.deserializeTypedFromScalar(jp, ctxt);
        
        assertNotNull(result);
    }
    
    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
        jp = new JsonFactory().createParser(new StringReader("{\"type\":\"type4\",\"value\":\"value4\"}"));
        ctxt = mapper.getDeserializationContext();
        
        Object result = deserializer.deserializeTypedFromAny(jp, ctxt);
        
        assertNotNull(result);
    }
    
    @Test
    public void test_deserialize_with_type_id() throws IOException {
        deserializer = new AsWrapperTypeDeserializer(null, null, "type", true, null);
        jp = new JsonFactory().createParser(new StringReader("{\"type\":\"type5\",\"value\":\"value5\"}"));
        ctxt = mapper.getDeserializationContext();
        
        Object result = deserializer.deserializeTypedFromAny(jp, ctxt);
        
        assertNotNull(result);
    }
    
    @Test(expected = JsonParseException.class)
    public void test_deserialize_error_wrong_token() throws IOException {
        deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
        jp = new JsonFactory().createParser(new StringReader("{\"value\":\"value6\"}"));
        ctxt = mapper.getDeserializationContext();
        
        deserializer.deserializeTypedFromObject(jp, ctxt);
    }
    
    @Test(expected = JsonParseException.class)
    public void test_deserialize_error_wrong_field_name() throws IOException {
        deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
        jp = new JsonFactory().createParser(new StringReader("{\"value\":\"value7\"}"));
        ctxt = mapper.getDeserializationContext();
        
        deserializer.deserializeTypedFromObject(jp, ctxt);
    }
    
    @Test(expected = JsonParseException.class)
    public void test_deserialize_error_wrong_end_object() throws IOException {
        deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
        jp = new JsonFactory().createParser(new StringReader("{\"type\":\"type8\",\"value\":\"value8\"}"));
        ctxt = mapper.getDeserializationContext();
        
        deserializer.deserializeTypedFromObject(jp, ctxt);
    }
    
    @Test
    public void test_deserialize_with_native_type_ids() throws IOException {
        deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
        StdDeserializer<Object> deser = new StdDeserializer<Object>(Object.class) {
            
            private static final long serialVersionUID = 1L;

            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
                return null;
            }
        };
        deserializer._deserializeWithNativeTypeId(jp, ctxt, "type9");
        
        TypeIdResolver resolver = deserializer._idResolver;
        resolver.init(JavaType.defaultInstance(getClass()));
        
        assertEquals("type9", resolver.idFromValueAndType(null, null));
    }
    
    @Test(expected = JsonParseException.class)
    public void test_deserialize_error_wrong_start_object() throws IOException {
        deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
        jp = new JsonFactory().createParser(new StringReader("[{\"type\":\"type10\",\"value\":\"value10\"}]"));
        ctxt = mapper.getDeserializationContext();
        
        deserializer.deserializeTypedFromObject(jp, ctxt);
    }
    
    @Test(expected = JsonParseException.class)
    public void test_deserialize_error_wrong_field_name2() throws IOException {
        deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
        jp = new JsonFactory().createParser(new StringReader("{\"typ\":\"type11\",\"value\":\"value11\"}"));
        ctxt = mapper.getDeserializationContext();
        
        deserializer.deserializeTypedFromObject(jp, ctxt);
    }
    
    @Test(expected = JsonParseException.class)
    public void test_deserialize_error_wrong_end_object2() throws IOException {
        deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
        jp = new JsonFactory().createParser(new StringReader("{\"type\":\"type12\",\"value\":\"value12\"},{\"type\":\"type13\",\"value\":\"value13\"}"));
        ctxt = mapper.getDeserializationContext();
        
        deserializer.deserializeTypedFromObject(jp, ctxt);
    }
    
    @Test(expected = JsonParseException.class)
    public void test_deserialize_error_wrong_end_object3() throws IOException {
        deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
        jp = new JsonFactory().createParser(new StringReader("{\"type\":\"type14\",\"value\":\"value14\"},"));
        ctxt = mapper.getDeserializationContext();
        
        deserializer.deserializeTypedFromObject(jp, ctxt);
    }
    
}