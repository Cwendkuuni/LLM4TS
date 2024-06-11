package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Type;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

public class StdSerializerTest {

    @Test
    public void testSerialize() {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonGenerationException {
                // TODO Auto-generated method stub
                
            }
        };
        
        fail("Not yet implemented");
    }

    @Test
    public void testGetSchema() {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonGenerationException {
                // TODO Auto-generated method stub
                
            }
        };
        
        fail("Not yet implemented");
    }

    @Test
    public void testGetSchemaOptional() {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonGenerationException {
                // TODO Auto-generated method stub
                
            }
        };
        
        fail("Not yet implemented");
    }

    @Test
    public void testCreateObjectNode() {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonGenerationException {
                // TODO Auto-generated method stub
                
            }
        };
        
        fail("Not yet implemented");
    }

    @Test
    public void testCreateSchemaNode() {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonGenerationException {
                // TODO Auto-generated method stub
                
            }
        };
        
        fail("Not yet implemented");
    }

    @Test
    public void testCreateSchemaNodeOptional() {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonGenerationException {
                // TODO Auto-generated method stub
                
            }
        };
        
        fail("Not yet implemented");
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonGenerationException {
                // TODO Auto-generated method stub
                
            }
        };
        
        fail("Not yet implemented");
    }

    @Test
    public void testWrapAndThrow() {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonGenerationException {
                // TODO Auto-generated method stub
                
            }
        };
        
        fail("Not yet implemented");
    }

    @Test
    public void testWrapAndThrowInt() {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonGenerationException {
                // TODO Auto-generated method stub
                
            }
        };
        
        fail("Not yet implemented");
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
        
        fail("Not yet implemented");
    }

    @Test
    public void testFindConvertingContentSerializer() {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonGenerationException {
                // TODO Auto-generated method stub
                
            }
        };
        
        fail("Not yet implemented");
    }

    @Test
    public void testFindPropertyFilter() {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonGenerationException {
                // TODO Auto-generated method stub
                
            }
        };
        
        fail("Not yet implemented");
    }

}