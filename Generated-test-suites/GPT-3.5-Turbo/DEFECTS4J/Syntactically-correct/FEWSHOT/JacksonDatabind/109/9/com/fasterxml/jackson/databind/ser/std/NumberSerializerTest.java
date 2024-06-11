
package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class NumberSerializerTest {

    private NumberSerializer numberSerializer;

    @Mock
    private JsonGenerator jsonGenerator;

    @Mock
    private SerializerProvider serializerProvider;

    @Mock
    private JsonSerializer jsonSerializer;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        numberSerializer = new NumberSerializer(Number.class);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        SerializerProvider prov = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        JsonSerializer<?> result = numberSerializer.createContextual(prov, property);
        assertEquals(result, numberSerializer);
    }

    @Test
    public void testSerializeBigDecimal() throws IOException {
        BigDecimal value = new BigDecimal("10.5");
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        ArgumentCaptor<BigDecimal> argument = ArgumentCaptor.forClass(BigDecimal.class);
        verify(jsonGenerator, times(1)).writeNumber(argument.capture());
        assertEquals(value, argument.getValue());
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        BigInteger value = new BigInteger("10");
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        ArgumentCaptor<BigInteger> argument = ArgumentCaptor.forClass(BigInteger.class);
        verify(jsonGenerator, times(1)).writeNumber(argument.capture());
        assertEquals(value, argument.getValue());
    }

    @Test
    public void testSerializeLong() throws IOException {
        Long value = 10L;
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        ArgumentCaptor<Long> argument = ArgumentCaptor.forClass(Long.class);
        verify(jsonGenerator, times(1)).writeNumber(argument.capture());
        assertEquals(value, argument.getValue());
    }

    @Test
    public void testSerializeDouble() throws IOException {
        Double value = 10.5;
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        ArgumentCaptor<Double> argument = ArgumentCaptor.forClass(Double.class);
        verify(jsonGenerator, times(1)).writeNumber(argument.capture());
        assertEquals(value, argument.getValue());
    }

    @Test
    public void testSerializeFloat() throws IOException {
        Float value = 10.5F;
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        ArgumentCaptor<Float> argument = ArgumentCaptor.forClass(Float.class);
        verify(jsonGenerator, times(1)).writeNumber(argument.capture());
        assertEquals(value, argument.getValue());
    }

    @Test
    public void testSerializeInteger() throws IOException {
        Integer value = 10;
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        ArgumentCaptor<Integer> argument = ArgumentCaptor.forClass(Integer.class);
        verify(jsonGenerator, times(1)).writeNumber(argument.capture());
        assertEquals(value, argument.getValue());
    }

    @Test
    public void testSerializeByte() throws IOException {
        Byte value = 10;
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        ArgumentCaptor<Byte> argument = ArgumentCaptor.forClass(Byte.class);
        verify(jsonGenerator, times(1)).writeNumber(argument.capture());
        assertEquals(value, argument.getValue());
    }

    @Test
    public void testSerializeShort() throws IOException {
        Short value = 10;
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        ArgumentCaptor<Short> argument = ArgumentCaptor.forClass(Short.class);
        verify(jsonGenerator, times(1)).writeNumber(argument.capture());
        assertEquals(value, argument.getValue());
    }

    @Test
    public void testSerializeFallback() throws IOException {
        Integer value = 10;
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        verify(jsonGenerator, times(1)).writeNumber(argument.capture());
        assertEquals(value.toString(), argument.getValue());
    }

    @Test
    public void testGetSchema() {
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);
        JsonNode result = numberSerializer.getSchema(provider, typeHint);
        assertEquals(result.get("type").asText(), "number");
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        verify(visitor, times(1)).expectNumberFormat(typeHint);
    }
}
