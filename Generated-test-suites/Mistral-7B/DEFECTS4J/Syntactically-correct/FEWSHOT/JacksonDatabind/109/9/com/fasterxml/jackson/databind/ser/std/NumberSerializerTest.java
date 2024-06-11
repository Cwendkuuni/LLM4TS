package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;

@RunWith(MockitoJUnitRunner.class)
public class NumberSerializerTest {

    @Test
    public void testSerialize_BigDecimal() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);

        BigDecimal number = new BigDecimal("123.45");
        serializer.serialize(number, generator, Mockito.mock(SerializerProvider.class));

        Mockito.verify(generator, Mockito.times(1)).writeNumber(number);
    }

    @Test
    public void testSerialize_BigInteger() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);

        BigInteger number = BigInteger.valueOf(123);
        serializer.serialize(number, generator, Mockito.mock(SerializerProvider.class));

        Mockito.verify(generator, Mockito.times(1)).writeNumber(number);
    }

    @Test
    public void testSerialize_Number() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        NumberSerializer serializer = NumberSerializer.instance;

        int number = 123;
        serializer.serialize(number, generator, Mockito.mock(SerializerProvider.class));

        Mockito.verify(generator, Mockito.times(1)).writeNumber(number);
    }

    @Test
    public void testSerialize_Long() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        NumberSerializer serializer = NumberSerializer.instance;

        long number = 123;
        serializer.serialize(number, generator, Mockito.mock(SerializerProvider.class));

        Mockito.verify(generator, Mockito.times(1)).writeNumber(number);
    }

    @Test
    public void testSerialize_Double() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        NumberSerializer serializer = NumberSerializer.instance;

        double number = 123.45;
        serializer.serialize(number, generator, Mockito.mock(SerializerProvider.class));

        Mockito.verify(generator, Mockito.times(1)).writeNumber(number);
    }

    @Test
    public void testSerialize_Float() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        NumberSerializer serializer = NumberSerializer.instance;

        float number = 123.45f;
        serializer.serialize(number, generator, Mockito.mock(SerializerProvider.class));

        Mockito.verify(generator, Mockito.times(1)).writeNumber(number);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);

        NumberSerializer serializer = NumberSerializer.instance;
        serializer.acceptJsonFormatVisitor(visitor, null);

        Mockito.verify(visitor, Mockito.times(1)).expectNumberFormat(null);
    }

    @Test
    public void testAcceptJsonFormatVisitor_BigDecimal() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);

        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        serializer.acceptJsonFormatVisitor(visitor, null);

        Mockito.verify(visitor, Mockito.times(1)).expectNumberFormat(null);
        Mockito.verify(visitor, Mockito.times(1)).visitIntFormat(null, null, JsonParser.NumberType.BIG_DECIMAL);
    }

    @Test
    public void testAcceptJsonFormatVisitor_BigInteger() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);

        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        serializer.acceptJsonFormatVisitor(visitor, null);

        Mockito.verify(visitor, Mockito.times(1)).expectNumberFormat(null);
        Mockito.verify(visitor, Mockito.times(1)).visitIntFormat(null, null, JsonParser.NumberType.BIG_INTEGER);
    }
}