package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.RootNameLookup;

public class NumberSerializerTest {
    
    private NumberSerializer numberSerializer;
    
    @Before
    public void setUp() {
        numberSerializer = new NumberSerializer(Number.class);
    }
    
    @Test
    public void testCreateContextualWithNullFormat() throws Exception {
        numberSerializer.createContextual(null, null);
        assertEquals(numberSerializer, numberSerializer.createContextual(null, null));
    }
    
    @Test
    public void testSerializeBigDecimal() throws IOException {
        BigDecimal value = new BigDecimal(10);
        JsonSerializer<Object> serializer = new ToStringSerializer();
        JsonGenerator jsonGenerator = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new SerializerProvider().createInstance(new ObjectMapper().getFactory(), new RootNameLookup());
        numberSerializer.serialize(value, jsonGenerator, provider);
        assertTrue(ClassUtil.hasClass(null));
    }
    
    @Test
    public void testSerializeBigInteger() throws IOException {
        BigInteger value = new BigInteger("10");
        JsonSerializer<Object> serializer = new ToStringSerializer();
        JsonGenerator jsonGenerator = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new SerializerProvider().createInstance(new ObjectMapper().getFactory(), new RootNameLookup());
        numberSerializer.serialize(value, jsonGenerator, provider);
        assertTrue(ClassUtil.hasClass(null));
    }
    
    @Test
    public void testSerializeLong() throws IOException {
        Long value = 10L;
        JsonGenerator jsonGenerator = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new SerializerProvider().createInstance(new ObjectMapper().getFactory(), new RootNameLookup());
        numberSerializer.serialize(value, jsonGenerator, provider);
        assertTrue(ClassUtil.hasClass(null));
    }
    
    @Test
    public void testSerializeDouble() throws IOException {
        Double value = 10.0;
        JsonGenerator jsonGenerator = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new SerializerProvider().createInstance(new ObjectMapper().getFactory(), new RootNameLookup());
        numberSerializer.serialize(value, jsonGenerator, provider);
        assertTrue(ClassUtil.hasClass(null));
    }
    
    @Test
    public void testSerializeFloat() throws IOException {
        Float value = 10.0f;
        JsonGenerator jsonGenerator = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new SerializerProvider().createInstance(new ObjectMapper().getFactory(), new RootNameLookup());
        numberSerializer.serialize(value, jsonGenerator, provider);
        assertTrue(ClassUtil.hasClass(null));
    }
    
    @Test
    public void testSerializeInteger() throws IOException {
        Integer value = 10;
        JsonGenerator jsonGenerator = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new SerializerProvider().createInstance(new ObjectMapper().getFactory(), new RootNameLookup());
        numberSerializer.serialize(value, jsonGenerator, provider);
        assertTrue(ClassUtil.hasClass(null));
    }
    
    @Test
    public void testSerializeByte() throws IOException {
        Byte value = 10;
        JsonGenerator jsonGenerator = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new SerializerProvider().createInstance(new ObjectMapper().getFactory(), new RootNameLookup());
        numberSerializer.serialize(value, jsonGenerator, provider);
        assertTrue(ClassUtil.hasClass(null));
    }
    
    @Test
    public void testSerializeShort() throws IOException {
        Short value = 10;
        JsonGenerator jsonGenerator = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new SerializerProvider().createInstance(new ObjectMapper().getFactory(), new RootNameLookup());
        numberSerializer.serialize(value, jsonGenerator, provider);
        assertTrue(ClassUtil.hasClass(null));
    }
    
    @Test
    public void testSerialize()
    {
        Number value = new Number() {
            @Override
            public long longValue() {
                return 123;
            }

            @Override
            public int intValue() {
                return 123;
            }

            @Override
            public float floatValue() {
                return 123.0f;
            }

            @Override
            public double doubleValue() {
                return 123.0;
            }
        };
        JsonGenerator jsonGenerator = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new SerializerProvider().createInstance(new ObjectMapper().getFactory(), new RootNameLookup());
        numberSerializer.serialize(value, jsonGenerator, provider);
        assertTrue(ClassUtil.hasClass(null));
    }
    
    
    @Test
    public void testGetSchema() throws IOException {
        SerializerProvider provider = new SerializerProvider().createInstance(new ObjectMapper().getFactory(), new RootNameLookup());
        numberSerializer.getSchema(provider, null);
        assertTrue(ClassUtil.hasClass(null));
    }
}