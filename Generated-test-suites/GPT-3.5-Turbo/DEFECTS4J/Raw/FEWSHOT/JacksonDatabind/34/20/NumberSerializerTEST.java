
package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

public class NumberSerializerTest {

    private NumberSerializer numberSerializer;

    @Test
    public void testSerialize() throws IOException {
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);

        numberSerializer = new NumberSerializer(Integer.class);
        numberSerializer.serialize(10, generator, provider);
        Mockito.verify(generator, Mockito.times(1)).writeNumber(10);

        numberSerializer = new NumberSerializer(Long.class);
        numberSerializer.serialize(10000000000L, generator, provider);
        Mockito.verify(generator, Mockito.times(1)).writeNumber(10000000000L);

        numberSerializer = new NumberSerializer(Double.class);
        numberSerializer.serialize(3.14, generator, provider);
        Mockito.verify(generator, Mockito.times(1)).writeNumber(3.14);

        numberSerializer = new NumberSerializer(Float.class);
        numberSerializer.serialize(1.234f, generator, provider);
        Mockito.verify(generator, Mockito.times(1)).writeNumber(1.234f);

        numberSerializer = new NumberSerializer(Byte.class);
        numberSerializer.serialize((byte) 1, generator, provider);
        Mockito.verify(generator, Mockito.times(1)).writeNumber(1);

        numberSerializer = new NumberSerializer(Short.class);
        numberSerializer.serialize((short) 100, generator, provider);
        Mockito.verify(generator, Mockito.times(1)).writeNumber(100);

        numberSerializer = new NumberSerializer(BigInteger.class);
        numberSerializer.serialize(new BigInteger("1234567890"), generator, provider);
        Mockito.verify(generator, Mockito.times(1)).writeNumber(new BigInteger("1234567890"));

        numberSerializer = new NumberSerializer(BigDecimal.class);
        numberSerializer.serialize(new BigDecimal("3.14159"), generator, provider);
        Mockito.verify(generator, Mockito.times(1)).writeNumber(new BigDecimal("3.14159"));

        numberSerializer = new NumberSerializer(Number.class);
        numberSerializer.serialize(100, generator, provider);
        Mockito.verify(generator, Mockito.times(1)).writeNumber("100");
    }

    @Test
    public void testGetSchema() {
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        Type typeHint = Mockito.mock(Type.class);

        numberSerializer = new NumberSerializer(BigInteger.class);
        JsonNode schemaNode = numberSerializer.getSchema(provider, typeHint);
        assertTrue(schemaNode.isNumber());
        assertEquals("integer", schemaNode.asText());

        numberSerializer = new NumberSerializer(BigDecimal.class);
        schemaNode = numberSerializer.getSchema(provider, typeHint);
        assertTrue(schemaNode.isNumber());
        assertEquals("number", schemaNode.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = Mockito.mock(JavaType.class);

        numberSerializer = new NumberSerializer(BigInteger.class);
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        Mockito.verify(visitor, Mockito.times(1)).expectNumberFormat(typeHint);

        numberSerializer = new NumberSerializer(BigDecimal.class);
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        Mockito.verify(visitor, Mockito.times(1)).visitFloatFormat(visitor, typeHint, JsonParser.NumberType.BIG_INTEGER);
    }
}
