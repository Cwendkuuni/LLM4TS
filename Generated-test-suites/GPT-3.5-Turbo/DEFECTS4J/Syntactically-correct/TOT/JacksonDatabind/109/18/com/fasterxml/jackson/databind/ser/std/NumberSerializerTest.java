
package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

public class NumberSerializerTest {

    @Test
    public void testSerialize_BigDecimal() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(BigDecimal.class);
        BigDecimal value = new BigDecimal("1234567890.1234567890");
        JsonGenerator generator = new ObjectMapper().getFactory().createGenerator(System.out);
        numberSerializer.serialize(value, generator, SerializerProvider.nonEmptyInstance());
    }

    @Test
    public void testSerialize_BigInteger() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(BigInteger.class);
        BigInteger value = new BigInteger("123456789012345678901234567890");
        JsonGenerator generator = new ObjectMapper().getFactory().createGenerator(System.out);
        numberSerializer.serialize(value, generator, SerializerProvider.nonEmptyInstance());
    }
  
    @Test
    public void testSerialize_Long() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Long.class);
        Long value = Long.MAX_VALUE;
        JsonGenerator generator = new ObjectMapper().getFactory().createGenerator(System.out);
        numberSerializer.serialize(value, generator, SerializerProvider.nonEmptyInstance());
    }

    @Test
    public void testSerialize_Double() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Double.class);
        Double value = 3.14159;
        JsonGenerator generator = new ObjectMapper().getFactory().createGenerator(System.out);
        numberSerializer.serialize(value, generator, SerializerProvider.nonEmptyInstance());
    }

    @Test
    public void testSerialize_Float() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Float.class);
        Float value = 6.28318530718f;
        JsonGenerator generator = new ObjectMapper().getFactory().createGenerator(System.out);
        numberSerializer.serialize(value, generator, SerializerProvider.nonEmptyInstance());
    }

    @Test
    public void testSerialize_Integer() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Integer.class);
        Integer value = 42;
        JsonGenerator generator = new ObjectMapper().getFactory().createGenerator(System.out);
        numberSerializer.serialize(value, generator, SerializerProvider.nonEmptyInstance());
    }

    @Test
    public void testSerialize_Byte() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Byte.class);
        Byte value = 127;
        JsonGenerator generator = new ObjectMapper().getFactory().createGenerator(System.out);
        numberSerializer.serialize(value, generator, SerializerProvider.nonEmptyInstance());
    }

    @Test
    public void testSerialize_Short() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Short.class);
        Short value = 32767;
        JsonGenerator generator = new ObjectMapper().getFactory().createGenerator(System.out);
        numberSerializer.serialize(value, generator, SerializerProvider.nonEmptyInstance());
    }

    @Test
    public void testSerialize_UnsupportedType() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        Number value = (Number) new Object();
        JsonGenerator generator = new ObjectMapper().getFactory().createGenerator(System.out);
        numberSerializer.serialize(value, generator, SerializerProvider.nonEmptyInstance());
    }
}

