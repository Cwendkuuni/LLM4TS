package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.StdSerializer;

import org.junit.Test;

public class StdSerializerTest {

    @Test
    public void testIsDefaultSerializer() {
        StdSerializer<Object> serializer = new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonProcessingException {
                // empty implementation
            }
        };
        
        assertTrue(StdSerializer.class.isInstance(serializer));
        assertTrue(StdSerializer.class.isAssignableFrom(serializer.getClass()));
        assertTrue(StdSerializer.class.isInstance(new StdSerializer<Object>(Object.class)));
        assertTrue(StdSerializer.class.isAssignableFrom(new StdSerializer<Object>(Object.class).getClass()));
        assertNotNull(serializer.handledType());
        assertTrue(StdSerializer.class.isInstance(serializer.handledType()));
        assertTrue(StdSerializer.class.isAssignableFrom(serializer.handledType().getClass()));
        
        JsonSerializer<Object> nonStdSerializer = new ObjectMapper().findValueSerializer(Object.class);
        assertFalse(StdSerializer.class.isInstance(nonStdSerializer));
        assertFalse(StdSerializer.class.isAssignableFrom(nonStdSerializer.getClass()));
        
        assertTrue(StdSerializer.class.isInstance(serializer));
        assertTrue(StdSerializer.class.isAssignableFrom(serializer.getClass()));
        assertTrue(StdSerializer.class.isInstance(serializer.handledType()));
        assertTrue(StdSerializer.class.isAssignableFrom(serializer.handledType().getClass()));
        assertFalse(StdSerializer.class.isInstance(nonStdSerializer));
        assertFalse(StdSerializer.class.isAssignableFrom(nonStdSerializer.getClass()));
        
        assertTrue(StdSerializer.class.isInstance(serializer));
        assertTrue(StdSerializer.class.isAssignableFrom(serializer.getClass()));
        assertTrue(StdSerializer.class.isInstance(serializer.handledType()));
        assertTrue(StdSerializer.class.isAssignableFrom(serializer.handledType().getClass()));
        assertFalse(StdSerializer.class.isInstance(nonStdSerializer));
        assertFalse(StdSerializer.class.isAssignableFrom(nonStdSerializer.getClass()));
        
        JsonSerializer<String> stringSerializer = new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonProcessingException {
                jgen.writeString(value);
            }
        };
        
        assertTrue(StdSerializer.class.isInstance(stringSerializer));
        assertTrue(StdSerializer.class.isAssignableFrom(stringSerializer.getClass()));
        assertTrue(StdSerializer.class.isInstance(stringSerializer.handledType()));
        assertTrue(StdSerializer.class.isAssignableFrom(stringSerializer.handledType().getClass()));
        
        JsonSerializer<Integer> intSerializer = new StdSerializer<Integer>(Integer.class) {
            @Override
            public void serialize(Integer value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonProcessingException {
                jgen.writeNumber(value);
            }
        };
        
        assertTrue(StdSerializer.class.isInstance(intSerializer));
        assertTrue(StdSerializer.class.isAssignableFrom(intSerializer.getClass()));
        assertTrue(StdSerializer.class.isInstance(intSerializer.handledType()));
        assertTrue(StdSerializer.class.isAssignableFrom(intSerializer.handledType().getClass()));
        
        JsonSerializer<ObjectNode> objectNodeSerializer = new StdSerializer<ObjectNode>(ObjectNode.class) {
            @Override
            public void serialize(ObjectNode value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonProcessingException {
                jgen.writeObject(value);
            }
        };
        
        assertTrue(StdSerializer.class.isInstance(objectNodeSerializer));
        assertTrue(StdSerializer.class.isAssignableFrom(objectNodeSerializer.getClass()));
        assertTrue(StdSerializer.class.isInstance(objectNodeSerializer.handledType()));
        assertTrue(StdSerializer.class.isAssignableFrom(objectNodeSerializer.handledType().getClass()));
    }

    @Test
    public void testAbstractMethods() {
        StdSerializer<Object> serializer = new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonProcessingException {
                // empty implementation
            }
        };
        
        assertNotNull(serializer.handledType());
        
        try {
            serializer.serialize(null, null, null);
            fail();
        } catch (IllegalArgumenException e) {
            // expected
        }
        
        try {
            serializer.getSchema(null, null);
            fail();
        } catch (JsonMappingException e) {
            // expected
        }

        try {
            serializer.acceptJsonFormatVisitor(null, null);
            fail();
        } catch (JsonMappingException e) {
            // expected
        }
        
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonProcessingException {
                jgen.writeString(value);
            }
        };

        ObjectNode schema = stringSerializer.getSchema(null, null);
        ObjectNode expectedSchema = createSchemaNode("string");
        assertEquals(expectedSchema, schema);
    }

    private ObjectNode createSchemaNode(String type) {
        ObjectNode schema = JsonNodeFactory.instance.objectNode();
        schema.put("type", type);
        return schema;
    }
}