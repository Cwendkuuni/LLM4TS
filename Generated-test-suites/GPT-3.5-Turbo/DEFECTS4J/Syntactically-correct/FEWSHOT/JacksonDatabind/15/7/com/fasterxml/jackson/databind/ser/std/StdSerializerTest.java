package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.Type;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

public class StdSerializerTest {
    @Test
    public void testStdSerializer() {
        StdSerializer<?> serializer = new StdSerializer<String>(String.class) {
            private static final long serialVersionUID = 1L;

            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
            }

            @Override
            public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
                return null;
            }

            @Override
            public JsonNode getSchema(SerializerProvider provider, Type typeHint, boolean isOptional)
                    throws JsonMappingException {
                return null;
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                    throws JsonMappingException {
            }
        };

        assertNotNull(serializer.handledType());

        serializer.serialize("", null, null);

        try {
            serializer.getSchema(null, null);
        } catch (Exception e) {
            assertTrue(e instanceof JsonMappingException);
        }

        try {
            serializer.getSchema(null, null, false);
        } catch (Exception e) {
            assertTrue(e instanceof JsonMappingException);
        }

        serializer.acceptJsonFormatVisitor(null, null);

        PropertyFilter propertyFilter = serializer.findPropertyFilter(null, null, null);
        assertNotNull(propertyFilter);

        assertTrue(serializer.isDefaultSerializer(serializer));
        
        JsonSerializer<?> existingSerializer = new StdSerializer<String>(String.class) {
            private static final long serialVersionUID = 1L;

            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
            }

            @Override
            public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
                return null;
            }

            @Override
            public JsonNode getSchema(SerializerProvider provider, Type typeHint, boolean isOptional)
                    throws JsonMappingException {
                return null;
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                    throws JsonMappingException {
            }
        };
        
        existingSerializer = serializer.findConvertingContentSerializer(null, null, existingSerializer);
        assertNotNull(existingSerializer);

        JsonMappingException exception = null;
        try {
            serializer.wrapAndThrow(null, new Exception(), null, "test");
        } catch (JsonMappingException e) {
            exception = e;
        }
        assertNotNull(exception);

        exception = null;
        try {
            serializer.wrapAndThrow(null, new Exception(), null, 0);
        } catch (JsonMappingException e) {
            exception = e;
        }
        assertNotNull(exception);
    }
    
    @Test
    public void testStdSerializerWithObjectNode() {
        StdSerializer<?> serializer = new StdSerializer<String>(String.class) {
            private static final long serialVersionUID = 1L;

            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
            }

            @Override
            public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
                return null;
            }

            @Override
            public JsonNode getSchema(SerializerProvider provider, Type typeHint, boolean isOptional)
                    throws JsonMappingException {
                return null;
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                    throws JsonMappingException {
            }
            
            @Override
            protected ObjectNode createObjectNode() {
            	return null;
            }
        };
        
        PropertyFilter propertyFilter = serializer.findPropertyFilter(null, null, null);
        assertNotNull(propertyFilter);
    }
    
    @Test
    public void testStdSerializerWithSchemaNode() {
        StdSerializer<?> serializer = new StdSerializer<String>(String.class) {
            private static final long serialVersionUID = 1L;

            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
            }

            @Override
            public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
                return null;
            }

            @Override
            public JsonNode getSchema(SerializerProvider provider, Type typeHint, boolean isOptional)
                    throws JsonMappingException {
                return null;
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                    throws JsonMappingException {
            }
            
            @Override
            protected ObjectNode createSchemaNode(String type)
            {
                return null;
            }
        };
        
        PropertyFilter propertyFilter = serializer.findPropertyFilter(null, null, null);
        assertNotNull(propertyFilter);
    }
}