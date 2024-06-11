package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import org.junit.Test;

public class NumberSerializersTest {
    private final JsonSerializer<?> mockSerializer = mock(JsonSerializer.class);
    private final JsonGenerator mockGenerator = mock(JsonGenerator.class);
    private final SerializerProvider mockProvider = mock(SerializerProvider.class);
    private final BeanProperty mockProperty = mock(BeanProperty.class);
    private final TypeSerializer mockTypeSerializer = mock(TypeSerializer.class);

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);

        assertEquals(IntegerSerializer.class, allDeserializers.get(Integer.class.getName()).getClass());
        assertEquals(IntegerSerializer.class, allDeserializers.get(Integer.TYPE.getName()).getClass());
        assertEquals(LongSerializer.class, allDeserializers.get(Long.class.getName()).getClass());
        assertEquals(LongSerializer.class, allDeserializers.get(Long.TYPE.getName()).getClass());
        assertEquals(IntLikeSerializer.class, allDeserializers.get(Byte.class.getName()).getClass());
        assertEquals(IntLikeSerializer.class, allDeserializers.get(Byte.TYPE.getName()).getClass());
        assertEquals(ShortSerializer.class, allDeserializers.get(Short.class.getName()).getClass());
        assertEquals(ShortSerializer.class, allDeserializers.get(Short.TYPE.getName()).getClass());
        assertEquals(DoubleSerializer.class, allDeserializers.get(Double.class.getName()).getClass());
        assertEquals(DoubleSerializer.class, allDeserializers.get(Double.TYPE.getName()).getClass());
        assertEquals(FloatSerializer.class, allDeserializers.get(Float.class.getName()).getClass());
        assertEquals(FloatSerializer.class, allDeserializers.get(Float.TYPE.getName()).getClass());
    }

    @Test
    public void testShortSerializer() throws IOException {
        NumberSerializers.ShortSerializer serializer = new NumberSerializers.ShortSerializer();

        assertEquals(Short.class, serializer.handledType());
        assertEquals(JsonParser.NumberType.INT, serializer._numberType);
        assertEquals("number", serializer._schemaType);

        serializer.serialize((short) 123, mockGenerator, mockProvider);
        verify(mockGenerator).writeNumber(123);
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer(Integer.class);

        assertEquals(Integer.class, serializer.handledType());
        assertEquals(JsonParser.NumberType.INT, serializer._numberType);
        assertEquals("integer", serializer._schemaType);

        serializer.serialize(1234, mockGenerator, mockProvider);
        verify(mockGenerator).writeNumber(1234);
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        NumberSerializers.IntLikeSerializer serializer = new NumberSerializers.IntLikeSerializer();

        assertEquals(Number.class, serializer.handledType());
        assertEquals(JsonParser.NumberType.INT, serializer._numberType);
        assertEquals("integer", serializer._schemaType);

        serializer.serialize(5678, mockGenerator, mockProvider);
        verify(mockGenerator).writeNumber(5678);
    }

    @Test
    public void testLongSerializer() throws IOException {
        NumberSerializers.LongSerializer serializer = new NumberSerializers.LongSerializer(Long.class);

        assertEquals(Long.class, serializer.handledType());
        assertEquals(JsonParser.NumberType.LONG, serializer._numberType);
        assertEquals("number", serializer._schemaType);

        serializer.serialize(1234567890L, mockGenerator, mockProvider);
        verify(mockGenerator).writeNumber(1234567890L);
    }

    @Test
    public void testFloatSerializer() throws IOException {
        NumberSerializers.FloatSerializer serializer = new NumberSerializers.FloatSerializer();

        assertEquals(Float.class, serializer.handledType());
        assertEquals(JsonParser.NumberType.FLOAT, serializer._numberType);
        assertEquals("number", serializer._schemaType);

        serializer.serialize(1.23f, mockGenerator, mockProvider);
        verify(mockGenerator).writeNumber(1.23f);
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        NumberSerializers.DoubleSerializer serializer = new NumberSerializers.DoubleSerializer(Double.class);

        assertEquals(Double.class, serializer.handledType());
        assertEquals(JsonParser.NumberType.DOUBLE, serializer._numberType);
        assertEquals("number", serializer._schemaType);

        serializer.serialize(1.234567890, mockGenerator, mockProvider);
        verify(mockGenerator).writeNumber(1.234567890);
    }

    @Test
    public void testBaseGetSchema() {
        NumberSerializers.Base<Object> serializer = new NumberSerializers.ShortSerializer();

        JsonNode schema = serializer.getSchema(mockProvider, null);
        assertNotNull(schema);
        assertEquals("number", schema.get("type").asText());
        assertEquals(true, schema.get("required").asBoolean());
    }

    @Test
    public void testBaseAcceptJsonFormatVisitor() throws JsonMappingException {
        NumberSerializers.Base<Object> serializer = new NumberSerializers.ShortSerializer();
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        if (serializer._isInt) {
            verify(visitor).expectIntFormat(typeHint, serializer._numberType);
        } else {
            verify(visitor).expectFloatFormat(typeHint, serializer._numberType);
        }
    }

    @Test
    public void testBaseContextual() throws JsonMappingException {
        NumberSerializers.Base<Object> serializer = new NumberSerializers.ShortSerializer();
        JsonFormat.Value format = JsonFormat.Value.forShape(JsonFormat.Shape.STRING);
        assertNull(serializer.createContextual(mockProvider, null));

        NumberSerializers.Base<Object> contextualSerializer = serializer.createContextual(mockProvider, mockProperty);
        assertNotNull(contextualSerializer);
        assertEquals(serializer, contextualSerializer);

        format = JsonFormat.Value.empty();
        format = format.withShape(JsonFormat.Shape.STRING);
        contextualSerializer = serializer.createContextual(mockProvider, mockProperty);
        assertNotNull(contextualSerializer);
        assertEquals(ToStringSerializer.instance, contextualSerializer);
    }
}