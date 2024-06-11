package com.fasterxml.jackson.databind.ser.std;

import static com.fasterxml.jackson.core.Version.VERSION_2_13;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapperForTest;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.TestBase;

/**
 * Test cases for {@link NumberSerializer}.
 */
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, fieldVisibility = JsonAutoDetect.Visibility.NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeName("NumberSerializerTest")
public class NumberSerializerTest extends TestBase {

    private static final NumberSerializer NUMBER_SERIALIZER = new NumberSerializer(Number.class);

    @Test
    public void testSerializeNumber() throws IOException {
        ObjectMapper mapper = new JsonMapperForTest();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        JsonSerializer<Number> serializer = mapper.getSerializerProvider().findValueSerializer(Number.class);
        assertSame(NUMBER_SERIALIZER, serializer);

        BigDecimal numberAsBigDecimal = new BigDecimal("123");
        BigInteger numberAsBigInteger = new BigInteger("456");
        Integer integerNumber = 789;
        Long longNumber = 1011;
        Double doubleNumber = 3.14;
        Float floatNumber = 2.718f;
        Byte byteNumber = 1;
        Short shortNumber = 2;
        Object testValues[] = { numberAsBigDecimal, numberAsBigInteger, integerNumber, longNumber, doubleNumber,
                floatNumber, byteNumber, shortNumber };

        for (Object value : testValues) {
            String json = mapper.writeValueAsString(value);
            Number deserializedValue = mapper.readValue(json, Number.class);
            assertEquals(value, deserializedValue);
        }
    }

    @Test
    public void testSerializeBigDecimal() throws IOException {
        ObjectMapper mapper = new JsonMapperForTest();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        JsonSerializer<Number> serializer = mapper.getSerializerProvider().findValueSerializer(BigDecimal.class);
        assertSame(NUMBER_SERIALIZER, serializer);

        BigDecimal bigDecimalNumber = new BigDecimal("123456789012345678901234567890.123456789012345678901234567890");
        String json = mapper.writeValueAsString(bigDecimalNumber);
        BigDecimal deserializedValue = mapper.readValue(json, BigDecimal.class);
        assertEquals(bigDecimalNumber, deserializedValue);
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        ObjectMapper mapper = new JsonMapperForTest();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        JsonSerializer<Number> serializer = mapper.getSerializerProvider().findValueSerializer(BigInteger.class);
        assertSame(NUMBER_SERIALIZER, serializer);

        BigInteger bigIntegerNumber = new BigInteger("123456789012345678901234567890");
        String json = mapper.writeValueAsString(bigIntegerNumber);
        BigInteger deserializedValue = mapper.readValue(json, BigInteger.class);
        assertEquals(bigIntegerNumber, deserializedValue);
    }

    @Test
    public void testSerializeInt() throws IOException {
        ObjectMapper mapper = new JsonMapperForTest();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        JsonSerializer<Number> serializer = mapper.getSerializerProvider().findValueSerializer(Integer.class);
        assertSame(NUMBER_SERIALIZER, serializer);

        Integer integerNumber = 789;
        String json = mapper.writeValueAsString(integerNumber);
        Integer deserializedValue = mapper.readValue(json, Integer.class);
        assertEquals(integerNumber, deserializedValue);
    }

    @Test
    public void testSerializeLong() throws IOException {
        ObjectMapper mapper = new JsonMapperForTest();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        JsonSerializer<Number> serializer = mapper.getSerializerProvider().findValueSerializer(Long.class);
        assertSame(NUMBER_SERIALIZER, serializer);

        Long longNumber = 1011;
        String json = mapper.writeValueAsString(longNumber);
        Long deserializedValue = mapper.readValue(json, Long.class);
        assertEquals(longNumber, deserializedValue);
    }

    @Test
    public void testSerializeDouble() throws IOException {
        ObjectMapper mapper = new JsonMapperForTest();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        JsonSerializer<Number> serializer = mapper.getSerializerProvider().findValueSerializer(Double.class);
        assertSame(NUMBER_SERIALIZER, serializer);

        Double doubleNumber = 3.14;
        String json = mapper.writeValueAsString(doubleNumber);
        Double deserializedValue = mapper.readValue(json, Double.class);
        assertEquals(doubleNumber, deserializedValue);
    }

    @Test
    public void testSerializeFloat() throws IOException {
        ObjectMapper mapper = new JsonMapperForTest();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        JsonSerializer<Number> serializer = mapper.getSerializerProvider().findValueSerializer(Float.class);
        assertSame(NUMBER_SERIALIZER, serializer);

        Float floatNumber = 2.718f;
        String json = mapper.writeValueAsString(floatNumber);
        Float deserializedValue = mapper.readValue(json, Float.class);
        assertEquals(floatNumber, deserializedValue);
    }

    @Test
    public void testSerializeByte() throws IOException {
        ObjectMapper mapper = new JsonMapperForTest();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        JsonSerializer<Number> serializer = mapper.getSerializerProvider().findValueSerializer(Byte.class);
        assertSame(NUMBER_SERIALIZER, serializer);

        Byte byteNumber = 1;
        String json = mapper.writeValueAsString(byteNumber);
        Byte deserializedValue = mapper.readValue(json, Byte.class);
        assertEquals(byteNumber, deserializedValue);
    }

    @Test
    public void testSerializeShort() throws IOException {
        ObjectMapper mapper = new JsonMapperForTest();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        JsonSerializer<Number> serializer = mapper.getSerializerProvider().findValueSerializer(Short.class);
        assertSame(NUMBER_SERIALIZER, serializer);

        Short shortNumber = 2;
        String json = mapper.writeValueAsString(shortNumber);
        Short deserializedValue = mapper.readValue(json, Short.class);
        assertEquals(shortNumber, deserializedValue);
    }
}