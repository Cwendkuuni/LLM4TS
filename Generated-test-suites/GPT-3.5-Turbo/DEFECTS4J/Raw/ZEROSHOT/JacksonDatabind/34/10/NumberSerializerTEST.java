package com.fasterxml.jackson.databind.ser.std;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

import org.junit.Test;

import static org.mockito.Mockito.*;

@JacksonStdImpl
public class NumberSerializerTest {

    private NumberSerializer numberSerializer;

    @Before
    public void setUp() {
        numberSerializer = new NumberSerializer(Number.class);
    }

    @Test
    public void testSerializeBigDecimal() throws IOException {
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        BigDecimal value = new BigDecimal(10);

        numberSerializer.serialize(value, generator, provider);

        verify(generator).writeNumber(value);
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        BigInteger value = new BigInteger("10");

        numberSerializer.serialize(value, generator, provider);

        verify(generator).writeNumber(value);
    }

    @Test
    public void testSerializeInteger() throws IOException {
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Integer value = 10;

        numberSerializer.serialize(value, generator, provider);

        verify(generator).writeNumber(value);
    }

    @Test
    public void testSerializeLong() throws IOException {
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Long value = 10L;

        numberSerializer.serialize(value, generator, provider);

        verify(generator).writeNumber(value);
    }

    @Test
    public void testSerializeDouble() throws IOException {
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Double value = 10.0;

        numberSerializer.serialize(value, generator, provider);

        verify(generator).writeNumber(value);
    }

    @Test
    public void testSerializeFloat() throws IOException {
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Float value = 10.0f;

        numberSerializer.serialize(value, generator, provider);

        verify(generator).writeNumber(value);
    }

    @Test
    public void testSerializeByte() throws IOException {
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Byte value = 10;

        numberSerializer.serialize(value, generator, provider);

        verify(generator).writeNumber(value.intValue());
    }

    @Test
    public void testSerializeShort() throws IOException {
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Short value = 10;

        numberSerializer.serialize(value, generator, provider);

        verify(generator).writeNumber(value.intValue());
    }

    @Test
    public void testSerializeOthers() throws IOException {
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Object value = "10";

        numberSerializer.serialize(value, generator, provider);

        verify(generator).writeNumber(value.toString());
    }

    @Test
    public void testGetSchema() {
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);

        JsonNode result = numberSerializer.getSchema(provider, typeHint);

        assertEquals("number", result.get("type").asText());
        assertEquals(true, result.get("required").asBoolean());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);

        if (numberSerializer._isInt) {
            verify(visitor).visitIntFormat(typeHint, JsonParser.NumberType.BIG_INTEGER);
        } else {
            Class<?> handledType = numberSerializer.handledType();
            if (handledType == BigDecimal.class) {
                verify(visitor).visitFloatFormat(typeHint, JsonParser.NumberType.BIG_INTEGER);
            } else {
                verify(visitor).expectNumberFormat(typeHint);
            }
        }
    }
}