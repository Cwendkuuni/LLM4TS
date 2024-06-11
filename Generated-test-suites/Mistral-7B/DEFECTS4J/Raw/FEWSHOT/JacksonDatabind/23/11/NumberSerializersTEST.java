package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import org.junit.Test;

public class NumberSerializersTest {

    @Test
    public void testShortSerializer() {
        ShortSerializer shortSerializer = NumberSerializers.ShortSerializer.instance;
        assertEquals(Short.parseShort("123"), shortSerializer.deserialize(shortSerializer.serialize(Short.parseShort("123")), null));
    }

    @Test
    public void testIntegerSerializer() {
        IntegerSerializer integerSerializer = NumberSerializers.IntegerSerializer.instance;
        assertEquals(Integer.parseInt("456"), integerSerializer.deserialize(integerSerializer.serialize(Integer.parseInt("456")), null));
    }

    @Test
    public void testIntLikeSerializer() {
        IntLikeSerializer intLikeSerializer = NumberSerializers.IntLikeSerializer.instance;
        assertEquals(Integer.parseInt("789"), intLikeSerializer.deserialize(intLikeSerializer.serialize(Byte.valueOf((byte) 78)), null));
    }

    @Test
    public void testLongSerializer() {
        LongSerializer longSerializer = NumberSerializers.LongSerializer.instance;
        assertEquals(Long.parseLong("10111213"), longSerializer.deserialize(longSerializer.serialize(Long.parseLong("10111213")), null));
    }

    @Test
    public void testFloatSerializer() {
        FloatSerializer floatSerializer = NumberSerializers.FloatSerializer.instance;
        assertEquals(Float.parseFloat("123.45"), floatSerializer.deserialize(floatSerializer.serialize(Float.parseFloat("123.45")), null), 0.01f);
    }

    @Test
    public void testDoubleSerializer() {
        DoubleSerializer doubleSerializer = NumberSerializers.DoubleSerializer.instance;
        assertEquals(Double.parseDouble("123.456"), doubleSerializer.deserialize(doubleSerializer.serialize(Double.parseDouble("123.456")), null), 0.001);
    }

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new java.util.HashMap<>();
        NumberSerializers.addAll(allDeserializers);
        JsonSerializer<?> intSerializer = allDeserializers.get(Integer.class.getName());
        assertEquals(intSerializer.getClass(), IntegerSerializer.class);
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        ShortSerializer shortSerializer = NumberSerializers.ShortSerializer.instance;
        JsonFormatVisitorWrapper visitor = createMock(JsonFormatVisitorWrapper.class);
        shortSerializer.acceptJsonFormatVisitor(visitor, null);
        verify(visitor).expectIntegerFormat(null);
    }

    @Test
    public void testCreateContextual() {
        ShortSerializer shortSerializer = NumberSerializers.ShortSerializer.instance;
        assertEquals(shortSerializer, shortSerializer.createContextual(null, null));
    }

    @Test
    public void testGetSchema() {
        ShortSerializer shortSerializer = NumberSerializers.ShortSerializer.instance;
        assertEquals("number", shortSerializer.getSchema(null, null).getClass().getName());
    }

    @Test
    public void testSerialize() {
        ShortSerializer shortSerializer = NumberSerializers.ShortSerializer.instance;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JsonGenerator gen = createMock(JsonGenerator.class);
        when(gen.writeNumber(anyInt())).thenAnswer(i -> baos.write(i.intValue()));
        shortSerializer.serialize(Short.parseShort("5"), gen, null);
        assertEquals("-5\n", baos.toString());
    }

    @Test
    public void testSerializeWithType() {
        ShortSerializer shortSerializer = NumberSerializers.ShortSerializer.instance;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JsonGenerator gen = createMock(JsonGenerator.class);
        TypeSerializer typeSer = createMock(TypeSerializer.class);
        when(gen.writeNumber(anyInt())).thenAnswer(i -> baos.write(i.intValue()));
        shortSerializer.serializeWithType(Short.parseShort("5"), gen, null, typeSer);
        assertEquals("-5\n", baos.toString());
    }
}