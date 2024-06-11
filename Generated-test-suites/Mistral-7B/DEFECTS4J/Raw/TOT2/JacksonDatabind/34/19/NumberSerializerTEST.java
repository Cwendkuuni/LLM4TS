package com.example;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializerTest {

    @Test
    public void testSerializeNumber() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(new File("output.json"), JsonEncoding.JSON);
        NumberSerializer numberSerializer = new NumberSerializer(Integer.class);

        Number number = 123;
        numberSerializer.serialize(number, jsonGenerator, mapper.getSerializerProvider());

        jsonGenerator.flush();
        jsonGenerator.close();

        ObjectMapper deserializer = new ObjectMapper();
        Number deserializedNumber = deserializer.readValue(new File("output.json"), Number.class);

        assertEquals(number, deserializedNumber);
    }

    @Test(expected = NullPointerException.class)
    public void testSerializeNullValue() {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(new File("output.json"), JsonEncoding.JSON);
        NumberSerializer numberSerializer = new NumberSerializer(Integer.class);

        numberSerializer.serialize(null, jsonGenerator, mapper.getSerializerProvider());
    }

    @Test
    public void testSerializeLargeValues() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(new File("output.json"), JsonEncoding.JSON);
        NumberSerializer numberSerializer = new NumberSerializer(Long.class);

        Long largeNumber = Long.MAX_VALUE;
        numberSerializer.serialize(largeNumber, jsonGenerator, mapper.getSerializerProvider());

        jsonGenerator.flush();
        jsonGenerator.close();

        ObjectMapper deserializer = new ObjectMapper();
        Long deserializedNumber = deserializer.readValue(new File("output.json"), Long.class);

        assertEquals(largeNumber, deserializedNumber);
    }

    @Test
    public void testSerializeFloatingPointNumbers() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(new File("output.json"), JsonEncoding.JSON);
        NumberSerializer numberSerializer = new NumberSerializer(Double.class);

        Double positiveNumber = 123.45;
        numberSerializer.serialize(positiveNumber, jsonGenerator, mapper.getSerializerProvider());

        jsonGenerator.flush();
        jsonGenerator.close();

        ObjectMapper deserializer = new ObjectMapper();
        Double deserializedNumber = deserializer.readValue(new File("output.json"), Double.class);

        assertEquals(positiveNumber, deserializedNumber);

        Double negativeNumber = -123.45;
        numberSerializer.serialize(negativeNumber, jsonGenerator, mapper.getSerializerProvider());

        jsonGenerator.flush();
        jsonGenerator.close();

        ObjectMapper deserializerNegative = new ObjectMapper();
        Double deserializedNegativeNumber = deserializerNegative.readValue(new File("output.json"), Double.class);

        assertEquals(negativeNumber, deserializedNegativeNumber);
    }

    @Test
    public void testSerializeBigValues() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(new File("output.json"), JsonEncoding.JSON);
        NumberSerializer numberSerializer = new NumberSerializer(BigDecimal.class);

        BigDecimal bigDecimal = new BigDecimal("123456789012345678901234567890.123456789012345678901234567890");
        numberSerializer.serialize(bigDecimal, jsonGenerator, mapper.getSerializerProvider());

        jsonGenerator.flush();
        jsonGenerator.close();

        ObjectMapper deserializer = new ObjectMapper();
        BigDecimal deserializedBigDecimal = deserializer.readValue(new File("output.json"), BigDecimal.class);

        assertEquals(bigDecimal, deserializedBigDecimal);

        ObjectMapper mapperBigInteger = new ObjectMapper();
        JsonGenerator jsonGeneratorBigInteger = mapperBigInteger.getFactory().createGenerator(new File("output.json"), JsonEncoding.JSON);
        NumberSerializer numberSerializerBigInteger = new NumberSerializer(BigInteger.class);

        BigInteger bigInteger = new BigInteger("340282346638528860571337403379249733260771035530088075720341155576063555221842298514411");
        numberSerializerBigInteger.serialize(bigInteger, jsonGeneratorBigInteger, mapperBigInteger.getSerializerProvider());

        jsonGeneratorBigInteger.flush();
        jsonGeneratorBigInteger.close();

        ObjectMapper deserializerBigInteger = new ObjectMapper();
        BigInteger deserializedBigInteger = deserializerBigInteger.readValue(new File("output.json"), BigInteger.class);

        assertEquals(bigInteger, deserializedBigInteger);
    }

    @Test
    public void testSerializeDifferentDataTypes() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(new File("output.json"), JsonEncoding.JSON);
        NumberSerializer numberSerializer = new NumberSerializer(Byte.TYPE);

        Byte byteValue = 127;
        numberSerializer.serialize(byteValue, jsonGenerator, mapper.getSerializerProvider());

        jsonGenerator.flush();
        jsonGenerator.close();

        ObjectMapper deserializer = new ObjectMapper();
        Byte deserializedByte = deserializer.readValue(new File("output.json"), Byte.TYPE);

        assertEquals(byteValue, deserializedByte);

        // Repeat the same test for Short, Integer, Long, Float, and Double
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeEmptyStringAndNaN() {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(new File("output.json"), JsonEncoding.JSON);
        NumberSerializer numberSerializer = new NumberSerializer(Double.class);

        numberSerializer.serialize("", jsonGenerator, mapper.getSerializerProvider());

        // Uncomment the following line if you want to test the "NaN" value
        // numberSerializer.serialize(Double.NaN, jsonGenerator, mapper.getSerializerProvider());
    }
}