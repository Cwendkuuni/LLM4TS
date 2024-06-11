package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import java.io.IOException;
import java.lang.reflect.Type;

public class StdDelegatingSerializerTest {

    @Test
    public void testConvertValue() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new Converter<Object, Object>() {
            @Override
            public Object convert(Object value) {
                return value;
            }
        });
        
        Object value = "test";
        Object convertedValue = serializer.convertValue(value);
        
        assertEquals(value, convertedValue);
    }

    @Test
    public void testSerializeEmptyValue() throws IOException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new Converter<Object, Object>() {
            @Override
            public Object convert(Object value) {
                return null;
            }
        });
        JsonGenerator mockGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider mockProvider = Mockito.mock(SerializerProvider.class);
        
        serializer.serialize(null, mockGenerator, mockProvider);
        Mockito.verify(mockProvider).defaultSerializeNull(mockGenerator);
    }

    @Test(expected = IllegalStateException.class)
    public void testSubClassWithDelegate() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new Converter<Object, Object>() {
            @Override
            public Object convert(Object value) {
                return null;
            }
        });
        serializer.withDelegate(null, null, null);
    }

    @Test
    public void testEmptySerialization() throws IOException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new Converter<Object, Object>() {
            @Override
            public Object convert(Object value) {
                return value;
            }
        });
        JsonGenerator mockGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider mockProvider = Mockito.mock(SerializerProvider.class);
        
        assertTrue(serializer.isEmpty(mockProvider, null));
    }

}