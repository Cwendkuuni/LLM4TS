package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

public class StdSerializerTest {
    
    @Test
    public void testHandledType() {
        StdSerializer<String> serializer = new TestSerializer();
        assertEquals(String.class, serializer.handledType());
    }
    
    @Test
    public void testSerialize() throws Exception {
        StdSerializer<String> serializer = new TestSerializer();
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        ObjectNode node = new ObjectMapper().createObjectNode();
        serializer.serialize("test", node, provider);
        assertTrue(node.has("result"));
        assertEquals("test", node.get("result").asText());
    }
    
    @Test
    public void testGetSchema() throws Exception {
        StdSerializer<String> serializer = new TestSerializer();
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        JsonNode schema = serializer.getSchema(provider, null);
        assertTrue(schema.isObject());
        assertEquals("string", schema.get("type").asText());
    }
    
    @Test
    public void testGetSchemaWithOptional() throws Exception {
        StdSerializer<String> serializer = new TestSerializer();
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        JsonNode schema = serializer.getSchema(provider, null, true);
        assertTrue(schema.isObject());
        assertEquals("string", schema.get("type").asText());
        assertTrue(schema.has("required"));
        assertFalse(schema.get("required").asBoolean());
    }
    
    @Test
    public void testCreateObjectNode() throws Exception {
        StdSerializer<String> serializer = new TestSerializer();
        ObjectNode node = serializer.createObjectNode();
        assertTrue(node.isObject());
    }
    
    @Test
    public void testCreateSchemaNode() throws Exception {
        StdSerializer<String> serializer = new TestSerializer();
        ObjectNode node = serializer.createSchemaNode("string");
        assertTrue(node.isObject());
        assertEquals("string", node.get("type").asText());
    }
    
    @Test
    public void testCreateSchemaNodeWithOptional() throws Exception {
        StdSerializer<String> serializer = new TestSerializer();
        ObjectNode node = serializer.createSchemaNode("string", true);
        assertTrue(node.isObject());
        assertEquals("string", node.get("type").asText());
        assertTrue(node.has("required"));
        assertFalse(node.get("required").asBoolean());
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        StdSerializer<String> serializer = new TestSerializer();
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        TestJsonFormatVisitor visitor = new TestJsonFormatVisitor();
        serializer.acceptJsonFormatVisitor(visitor, null);
        assertTrue(visitor.isVisited());
    }
    
    @Test
    public void testIsDefaultSerializer() {
        StdSerializer<String> serializer = new TestSerializer();
        assertFalse(serializer.isDefaultSerializer(serializer));
    }
    
    private static class TestSerializer extends StdSerializer<String> {
        
        private static final long serialVersionUID = 1L;

        protected TestSerializer() {
            super(String.class);
        }

        @Override
        public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonGenerationException {
            ObjectNode node = jgen.getObjectNode();
            node.put("result", value);
        }
    }
    
    private static class TestJsonFormatVisitor extends JsonFormatVisitorWrapper.Base {
        
        private boolean visited = false;
        
        public boolean isVisited() {
            return visited;
        }
        
        @Override
        public void expectAnyFormat(JavaType type) throws JsonMappingException {
            visited = true;
        }
    }
}