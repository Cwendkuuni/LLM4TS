package com.yourpackage.test;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapperBuilder;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

/**
 * Test file for NumberSerializer class.
 */
public class NumberSerializerTest {

    private ObjectMapper mapper;

    /**
     * Initializes the test environment.
     */
    @SuppressWarnings("unused")
    @BeforeClass
    public static void setup() {
        mapper = new JsonMapperBuilder().build();
    }

    /**
     * Serialization tests.
     *
     * @throws JsonGenerationException if there is an error during serialization.
     */
    @Test
    public void testSerialization() throws JsonGenerationException {
        NumberSerializer numberSerializer = NumberSerializer.instance;

        // Test serialization of primitive types.
        assertEquals("0", numberSerializer.serializeValue(0, mapper.getSerializerProvider(), null));
        assertEquals("1.0", numberSerializer.serializeValue(1.0F, mapper.getSerializerProvider(), null));
        assertEquals("1.0", numberSerializer.serializeValue(1.0D, mapper.getSerializerProvider(), null));
        assertEquals("1", numberSerializer.serializeValue(1, mapper.getSerializerProvider(), null));

        // Test serialization of custom Number types.
        BigDecimal bigDecimal = new BigDecimal("123.45");
        assertEquals("123.45", numberSerializer.serializeValue(bigDecimal, mapper.getSerializerProvider(), null));

        BigInteger bigInteger = new BigInteger("98765432109876543210");
        assertEquals("98765432109876543210", numberSerializer.serializeValue(bigInteger, mapper.getSerializerProvider(), null));

        // Test serialization of other Number types.
        Long longValue = 123L;
        assertEquals("123", numberSerializer.serializeValue(longValue, mapper.getSerializerProvider(), null));

        Double doubleValue = 3.14159265358979323846;
        assertEquals("3.14159265358979323846", numberSerializer.serializeValue(doubleValue, mapper.getSerializerProvider(), null));

        Float floatValue = 1.5f;
        assertEquals("1.5", numberSerializer.serializeValue(floatValue, mapper.getSerializerProvider(), null));

        Integer intValue = 255;
        assertEquals("255", numberSerializer.serializeValue(intValue, mapper.getSerializerProvider(), null));

        Short shortValue = 127;
        assertEquals("127", numberSerializer.serializeValue(shortValue, mapper.getSerializerProvider(), null));

        Byte byteValue = 128;
        assertEquals("128", numberSerializer.serializeValue(byteValue, mapper.getSerializerProvider(), null));
    }

    /**
     * Integration tests.
     *
     * @throws IOException if there is an error during deserialization.
     */
    @Test
    public void testIntegration() throws IOException {
        // Test serialization and deserialization of primitive types.
        String jsonString = mapper.writeValueAsString(0);
        Number number = mapper.readValue(jsonString, Number.class);
        assertEquals(0, number.intValue());

        jsonString = mapper.writeValueAsString(1.0F);
        number = mapper.readValue(jsonString, Number.class);
        assertEquals(1.0F, number, 0.0);

        jsonString = mapper.writeValueAsString(1.0D);
        number = mapper.readValue(jsonString, Number.class);
        assertEquals(1.0D, number, 0.0);

        jsonString = mapper.writeValueAsString(1);
        number = mapper.readValue(jsonString, Number.class);
        assertEquals(1, number.intValue());

        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        jsonString = mapper.writeValueAsString(null);
        number = mapper.readValue(jsonString, Number.class);
        assertEquals(0, number);

        // Test serialization and deserialization of custom Number types.
        BigDecimal bigDecimal = new BigDecimal("123.45");
        jsonString = mapper.writeValueAsString(bigDecimal);
        Number deserializedNumber = mapper.readValue(jsonString, Number.class);
        assertEquals(bigDecimal, deserializedNumber);

        BigInteger bigInteger = new BigInteger("98765432109876543210");
        jsonString = mapper.writeValueAsString(bigInteger);
        deserializedNumber = mapper.readValue(jsonString, Number.class);
        assertEquals(bigInteger, deserializedNumber);

        // Test serialization and deserialization of other Number types.
        Long longValue = 123L;
        jsonString = mapper.writeValueAsString(longValue);
        number = mapper.readValue(jsonString, Number.class);
        assertEquals(longValue, number.longValue());

        Double doubleValue = 3.14159265358979323846;
        jsonString = mapper.writeValueAsString(doubleValue);
        number = mapper.readValue(jsonString, Number.class);
        assertEquals(doubleValue, number, 0.0);

        Float floatValue = 1.5f;
        jsonString = mapper.writeValueAsString(floatValue);
        number = mapper.readValue(jsonString, Number.class);
        assertEquals(floatValue, number, 0.0);

        Integer intValue = 255;
        jsonString = mapper.writeValueAsString(intValue);
        number = mapper.readValue(jsonString, Number.class);
        assertEquals(intValue, number.intValue());

        Short shortValue = 127;
        jsonString = mapper.writeValueAsString(shortValue);
        number = mapper.readValue(jsonString, Number.class);
        assertEquals(shortValue, number.shortValue());

        Byte byteValue = 128;
        jsonString = mapper.writeValueAsString(byteValue);
        number = mapper.readValue(jsonString, Number.class);
        assertEquals(byteValue, number.byteValue());
    }
}