package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class StdSerializerTest {
    
    @Test
    public void testHandledType() {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonGenerationException {
                // TODO Auto-generated method stub
            }
        };
        
        assertEquals(String.class, serializer.handledType());
    }
    
    @Test
    public void testGetSchema() throws JsonMappingException {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonGenerationException {
                // TODO Auto-generated method stub
            }
        };
        
        JsonNode expectedNode = serializer.createObjectNode();
        expectedNode.put("type", "string");
        
        JsonNode actualNode = serializer.getSchema(null, null);
        
        assertEquals(expectedNode, actualNode);
    }
    
    @Test
    public void testGetSchemaWithOptional() throws JsonMappingException {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonGenerationException {
                // TODO Auto-generated method stub
            }
        };
        
        JsonNode expectedNode = serializer.createObjectNode();
        expectedNode.put("type", "string");
        expectedNode.put("required", true);
        
        JsonNode actualNode = serializer.getSchema(null, null, false);
        
        assertEquals(expectedNode, actualNode);
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonGenerationException {
                // TODO Auto-generated method stub
            }
        };
        
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = Mockito.mock(JavaType.class);
        
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        Mockito.verify(visitor).expectAnyFormat(typeHint);
    }
    
    @Test
    public void testIsDefaultSerializer() {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonGenerationException {
                // TODO Auto-generated method stub
            }
        };
        
        JsonSerializer<String> defaultSerializer = new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonGenerationException {
                // TODO Auto-generated method stub
            }
        };
        
        assertTrue(serializer.isDefaultSerializer(defaultSerializer));
    }
    
    @Test
    public void testFindConvertingContentSerializer() throws JsonMappingException {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonGenerationException {
                // TODO Auto-generated method stub
            }
        };
        
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        BeanProperty prop = Mockito.mock(BeanProperty.class);
        JsonSerializer<String> existingSerializer = Mockito.mock(JsonSerializer.class);
        
        assertNull(serializer.findConvertingContentSerializer(provider, prop, existingSerializer));
    }
    
    @Test
    public void testFindPropertyFilter() throws JsonMappingException {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonGenerationException {
                // TODO Auto-generated method stub
            }
        };
        
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        Object filterId = Mockito.mock(Object.class);
        Object valueToFilter = Mockito.mock(Object.class);
        
        assertNull(serializer.findPropertyFilter(provider, filterId, valueToFilter));
    }
}