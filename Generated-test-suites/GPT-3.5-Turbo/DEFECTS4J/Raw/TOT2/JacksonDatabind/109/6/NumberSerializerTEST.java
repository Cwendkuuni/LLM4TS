package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

public class NumberSerializerTest {

    NumberSerializer numberSerializer;

    @Before
    public void setUp() {
        numberSerializer = new NumberSerializer(Number.class);
    }

    @Test
    public void testSerializeBigDecimal() throws IOException {
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        
        BigDecimal testValue = new BigDecimal("10.5");
        
        numberSerializer.serialize(testValue, generator, provider);
        
        Mockito.verify(generator).writeNumber(testValue);
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        
        BigInteger testValue = new BigInteger("1234");
        
        numberSerializer.serialize(testValue, generator, provider);
        
        Mockito.verify(generator).writeNumber(testValue);
    }

    @Test
    public void testSerializeLong() throws IOException {
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        
        Long testValue = 100L;
        
        numberSerializer.serialize(testValue, generator, provider);
        
        Mockito.verify(generator).writeNumber(testValue);
    }

    @Test
    public void testSerializeDouble() throws IOException {
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        
        Double testValue = 10.5;
        
        numberSerializer.serialize(testValue, generator, provider);
        
        Mockito.verify(generator).writeNumber(testValue);
    }

    @Test
    public void testSerializeFloat() throws IOException {
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        
        Float testValue = 5.4f;
        
        numberSerializer.serialize(testValue, generator, provider);
        
        Mockito.verify(generator).writeNumber(testValue);
    }

    @Test
    public void testSerializeInteger() throws IOException {
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        
        Integer testValue = 15;
        
        numberSerializer.serialize(testValue, generator, provider);
        
        Mockito.verify(generator).writeNumber(testValue);
    }

    @Test
    public void testSerializeFallback() throws IOException {
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        
        Short testValue = 7;
        
        numberSerializer.serialize(testValue, generator, provider);
        
        Mockito.verify(generator).writeNumber(testValue.intValue());
    }

    @Test
    public void testGetSchema() {
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        Type typeHint = Mockito.mock(Type.class);
        
        JsonNode schema = numberSerializer.getSchema(provider, typeHint);
        
        assertNotNull(schema);
        assertEquals("number", schema.get("type").asText());
        assertTrue(schema.get("required").asBoolean());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = Mockito.mock(JavaType.class);
        
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        if (numberSerializer._isInt) {
            Mockito.verify(visitor).expectIntegerFormat(typeHint);
        } else {
            Mockito.verify(visitor).expectNumberFormat(typeHint);
        }
    }
}