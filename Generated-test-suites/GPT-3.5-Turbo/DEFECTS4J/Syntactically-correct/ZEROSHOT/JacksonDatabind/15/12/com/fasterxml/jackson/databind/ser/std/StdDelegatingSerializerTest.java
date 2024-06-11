package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.util.Converter;

import java.io.IOException;
import java.lang.reflect.Type;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

@SuppressWarnings("serial")
public class StdDelegatingSerializerTest {
    
    private StdDelegatingSerializer serializer;
    
    @Before
    public void setUp() {
        Converter<?,?> converter = new Converter<Object,Object>() {
            @Override
            public Object convert(Object value) {
                return value;
            }
        };
        serializer = new StdDelegatingSerializer(converter);
    }
    
    @Test
    public void testSerialize() {
        // TODO: add test case for serialize() method
    }
    
    @Test
    public void testSerializeWithType() {
        // TODO: add test case for serializeWithType() method
    }
    
    @Test
    public void testIsEmpty() {
        // TODO: add test case for isEmpty() method
    }
    
    @Test
    public void testIsEmptyWithProvider() {
        // TODO: add test case for isEmpty(SerializerProvider, Object) method
    }
    
    @Test
    public void testGetSchema() {
        // TODO: add test case for getSchema(SerializerProvider, Type) method
    }
    
    @Test
    public void testGetSchemaWithOptional() {
        // TODO: add test case for getSchema(SerializerProvider, Type, boolean) method
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() {
        // TODO: add test case for acceptJsonFormatVisitor(JsonFormatVisitorWrapper, JavaType) method
    }
}