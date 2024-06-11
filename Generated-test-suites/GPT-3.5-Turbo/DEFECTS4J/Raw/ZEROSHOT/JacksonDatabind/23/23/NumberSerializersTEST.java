
package com.fasterxml.jackson.databind.ser.std;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.*;
import com.fasterxml.jackson.databind.util.StdConverter;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonIntegerFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNumberFormatVisitor;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NumberSerializersTest {

    @Test
    public void testShortSerializer() throws IOException {
        ShortSerializer serializer = ShortSerializer.instance;
        Short value = 42;
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        
        serializer.serialize(value, gen, provider);
        gen.flush();
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        IntegerSerializer serializer = new IntegerSerializer();
        Integer value = 42;
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        
        serializer.serialize(value, gen, provider);
        gen.flush();
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        IntLikeSerializer serializer = IntLikeSerializer.instance;
        Number value = 42;
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        
        serializer.serialize(value, gen, provider);
        gen.flush();
    }

    @Test
    public void testLongSerializer() throws IOException {
        LongSerializer serializer = LongSerializer.instance;
        Long value = 42L;
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        
        serializer.serialize(value, gen, provider);
        gen.flush();
    }

    @Test
    public void testFloatSerializer() throws IOException {
        FloatSerializer serializer = FloatSerializer.instance;
        Float value = 42.0f;
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        
        serializer.serialize(value, gen, provider);
        gen.flush();
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        DoubleSerializer serializer = DoubleSerializer.instance;
        Double value = 42.0;
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        
        serializer.serialize(value, gen, provider);
        gen.flush();
    }

}
