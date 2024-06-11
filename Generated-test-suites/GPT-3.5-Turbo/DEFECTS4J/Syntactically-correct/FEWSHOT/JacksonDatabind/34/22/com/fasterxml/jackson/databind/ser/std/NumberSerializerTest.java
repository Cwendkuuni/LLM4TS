package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import org.junit.Test;

@JacksonStdImpl
@SuppressWarnings("serial")
public class NumberSerializerTest {

    @Test
    public void testSerialize() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonGenerator g = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        BigDecimal bigDecimal = new BigDecimal("10.5");
        numberSerializer.serialize(bigDecimal, g, provider);
        verify(g, times(1)).writeNumber(bigDecimal);

        BigInteger bigInteger = new BigInteger("10");
        numberSerializer.serialize(bigInteger, g, provider);
        verify(g, times(1)).writeNumber(bigInteger);

        Integer integer = new Integer(10);
        numberSerializer.serialize(integer, g, provider);
        verify(g, times(1)).writeNumber(integer.intValue());

        Long aLong = new Long(10);
        numberSerializer.serialize(aLong, g, provider);
        verify(g, times(1)).writeNumber(aLong.longValue());

        Double aDouble = new Double(10.5);
        numberSerializer.serialize(aDouble, g, provider);
        verify(g, times(1)).writeNumber(aDouble.doubleValue());

        Float aFloat = new Float(10.5);
        numberSerializer.serialize(aFloat, g, provider);
        verify(g, times(1)).writeNumber(aFloat.floatValue());

        Byte aByte = new Byte("10");
        numberSerializer.serialize(aByte, g, provider);
        verify(g, times(1)).writeNumber(aByte.intValue());

        Short aShort = new Short("10");
        numberSerializer.serialize(aShort, g, provider);
        verify(g, times(1)).writeNumber(aShort.intValue());

        Number number = new Number() {
            @Override
            public int intValue() {
                return 10;
            }

            @Override
            public long longValue() {
                return 10;
            }

            @Override
            public float floatValue() {
                return 10.5f;
            }

            @Override
            public double doubleValue() {
                return 10.5;
            }
        };

        numberSerializer.serialize(number, g, provider);
        verify(g, times(1)).writeNumber(number.toString());
    }

    @Test
    public void testGetSchema() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);

        JsonNode schema = numberSerializer.getSchema(provider, typeHint);

        assertEquals("number", schema.get("type").asText());

        numberSerializer = new NumberSerializer(BigInteger.class);
        schema = numberSerializer.getSchema(provider, typeHint);

        assertEquals("integer", schema.get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(visitor, times(1)).expectNumberFormat(typeHint);

        numberSerializer = new NumberSerializer(BigInteger.class);
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(visitor, times(1)).visitIntFormat(typeHint, JsonParser.NumberType.BIG_INTEGER);

        numberSerializer = new NumberSerializer(BigDecimal.class);
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(visitor, times(1)).visitFloatFormat(typeHint, JsonParser.NumberType.BIG_INTEGER);
    }
}