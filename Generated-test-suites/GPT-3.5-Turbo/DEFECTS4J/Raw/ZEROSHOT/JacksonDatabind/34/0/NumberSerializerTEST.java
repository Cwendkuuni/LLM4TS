package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@JacksonStdImpl
@SuppressWarnings("serial")
public class NumberSerializerTest {

    @Mock
    private JsonGenerator mockJsonGenerator;

    @Mock
    private SerializerProvider mockSerializerProvider;

    private NumberSerializer numberSerializer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        numberSerializer = new NumberSerializer(Number.class);
    }

    @Test
    public void testSerializeBigDecimal() throws IOException {
        BigDecimal value = new BigDecimal("123.45");
        
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        
        verify(mockJsonGenerator).writeNumber(value);
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        BigInteger value = new BigInteger("1234567890");
        
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        
        verify(mockJsonGenerator).writeNumber(value);
    }

    @Test
    public void testSerializeInteger() throws IOException {
        Integer value = 123;
        
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        
        verify(mockJsonGenerator).writeNumber(value);
    }

    @Test
    public void testSerializeLong() throws IOException {
        Long value = 1234567890L;
        
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        
        verify(mockJsonGenerator).writeNumber(value);
    }

    @Test
    public void testSerializeDouble() throws IOException {
        Double value = 123.45;
        
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        
        verify(mockJsonGenerator).writeNumber(value);
    }

    @Test
    public void testSerializeFloat() throws IOException {
        Float value = 123.45f;
        
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        
        verify(mockJsonGenerator).writeNumber(value);
    }

    @Test
    public void testSerializeByte() throws IOException {
        Byte value = 123;
        
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        
        verify(mockJsonGenerator).writeNumber(value.intValue());
    }

    @Test
    public void testSerializeShort() throws IOException {
        Short value = 123;
        
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        
        verify(mockJsonGenerator).writeNumber(value.intValue());
    }

    @Test
    public void testSerializeUnknownNumberType() throws IOException {
        Number value = new Number() {
            @Override
            public long longValue() {
                return 0;
            }

            @Override
            public int intValue() {
                return 0;
            }

            @Override
            public float floatValue() {
                return 0.0f;
            }

            @Override
            public double doubleValue() {
                return 0.0;
            }
        };
        
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        
        verify(mockJsonGenerator).writeNumber(value.toString());
    }

    @Test
    public void testGetSchemaForInt() {
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = BigInteger.class;
        
        JsonNode schema = numberSerializer.getSchema(provider, typeHint);
        
        assertEquals(schema.get("type").asText(), "integer");
    }

    @Test
    public void testGetSchemaForNonInt() {
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = BigDecimal.class;
        
        JsonNode schema = numberSerializer.getSchema(provider, typeHint);
        
        assertEquals(schema.get("type").asText(), "number");
    }

    @Test
    public void testAcceptJsonFormatVisitorForInt() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        JsonParser.NumberType numberType = JsonParser.NumberType.BIG_INTEGER;
        
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        verify(visitor).expectNumberFormat(typeHint);
        // How to verify visitIntFormat??
    }
    
    @Test
    public void testAcceptJsonFormatVisitorForNonInt() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        JsonParser.NumberType numberType = JsonParser.NumberType.DOUBLE;
        
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        verify(visitor).expectNumberFormat(typeHint);
        // How to verify visitFloatFormat??
    }
}