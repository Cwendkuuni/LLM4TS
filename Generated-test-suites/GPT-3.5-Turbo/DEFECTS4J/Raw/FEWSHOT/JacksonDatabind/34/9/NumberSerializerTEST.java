package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberSerializerTest {
    @Test
    public void testSerialize() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonGenerator g = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);

        BigDecimal bigDecimal = new BigDecimal("123.45");
        numberSerializer.serialize(bigDecimal, g, provider);
        Mockito.verify(g).writeNumber(bigDecimal);

        BigInteger bigInteger = new BigInteger("12345");
        numberSerializer.serialize(bigInteger, g, provider);
        Mockito.verify(g).writeNumber(bigInteger);

        int intValue = 123;
        numberSerializer.serialize(intValue, g, provider);
        Mockito.verify(g).writeNumber(intValue);

        long longValue = 12345L;
        numberSerializer.serialize(longValue, g, provider);
        Mockito.verify(g).writeNumber(longValue);

        double doubleValue = 123.45;
        numberSerializer.serialize(doubleValue, g, provider);
        Mockito.verify(g).writeNumber(doubleValue);

        float floatValue = 123.45f;
        numberSerializer.serialize(floatValue, g, provider);
        Mockito.verify(g).writeNumber(floatValue);

        byte byteValue = 123;
        numberSerializer.serialize(byteValue, g, provider);
        Mockito.verify(g).writeNumber(byteValue);

        short shortValue = 1234;
        numberSerializer.serialize(shortValue, g, provider);
        Mockito.verify(g).writeNumber(shortValue);

        Number otherNumber = 123.45;
        numberSerializer.serialize(otherNumber, g, provider);
        Mockito.verify(g).writeNumber(otherNumber.toString());
    }

    @Test
    public void testGetSchema() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);

        JsonNode schema = numberSerializer.getSchema(provider, null);
        Assert.assertNotNull(schema);
        Assert.assertEquals("number", schema.get("type").asText());
        Assert.assertTrue(schema.get("nullable").asBoolean());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JavaType type = Mockito.mock(JavaType.class);
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);

        numberSerializer.acceptJsonFormatVisitor(visitor, type);

        if (numberSerializer._isInt) {
            Mockito.verify(visitor).visitIntFormat(numberSerializer, type, JsonParser.NumberType.BIG_INTEGER);
        } else {
            Class<?> handledType = numberSerializer.handledType();
            if (handledType == BigDecimal.class) {
                Mockito.verify(visitor).visitFloatFormat(numberSerializer, type, JsonParser.NumberType.BIG_INTEGER);
            } else {
                Mockito.verify(visitor).expectNumberFormat(type);
            }
        }
    }
}