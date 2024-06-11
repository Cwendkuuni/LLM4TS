package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

@JacksonStdImpl
@SuppressWarnings("serial")
public class NumberSerializerTest {

    private final NumberSerializer numberSerializer = new NumberSerializer(Number.class);

    @Test
    public void testSerialize() throws IOException {
        JsonGenerator g = null;
        SerializerProvider provider = null;

        BigDecimal bd = new BigDecimal("100");
        numberSerializer.serialize(bd, g, provider);
        // assert expected behavior

        BigInteger bi = new BigInteger("100");
        numberSerializer.serialize(bi, g, provider);
        // assert expected behavior

        Integer i = Integer.valueOf(100);
        numberSerializer.serialize(i, g, provider);
        // assert expected behavior

        Long l = Long.valueOf(100);
        numberSerializer.serialize(l, g, provider);
        // assert expected behavior

        Double d = Double.valueOf(100);
        numberSerializer.serialize(d, g, provider);
        // assert expected behavior

        Float f = Float.valueOf(100);
        numberSerializer.serialize(f, g, provider);
        // assert expected behavior

        Byte b = Byte.valueOf("100");
        numberSerializer.serialize(b, g, provider);
        // assert expected behavior

        Short s = Short.valueOf("100");
        numberSerializer.serialize(s, g, provider);
        // assert expected behavior

        Number n = Double.valueOf(100);
        numberSerializer.serialize(n, g, provider);
        // assert expected behavior
    }

    @Test
    public void testGetSchema() {
        SerializerProvider provider = null;
        Type typeHint = null;

        JsonNode schema = numberSerializer.getSchema(provider, typeHint);
        // assert expected behavior
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;

        if (numberSerializer._isInt) {
            numberSerializer.visitIntFormat(visitor, typeHint, JsonParser.NumberType.BIG_INTEGER);
            // assert expected behavior
        } else {
            Class<?> h = numberSerializer.handledType();
            if (h == BigDecimal.class) {
                numberSerializer.visitFloatFormat(visitor, typeHint, JsonParser.NumberType.BIG_INTEGER);
                // assert expected behavior
            } else {
                numberSerializer.visitor.expectNumberFormat(typeHint);
                // assert expected behavior
            }
        }
    }
}