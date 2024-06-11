package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.Base;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.IntegerSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.IntLikeSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.LongSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.ShortSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.FloatSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.DoubleSerializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

public class NumberSerializersTest {

    @Test
    public void testShortSerializer() {
        ShortSerializer shortSerializer = new ShortSerializer();
        assertEquals("1", shortSerializer.serializeToString(shortValueOf((short) 1)));
    }

    @Test
    public void testIntegerSerializer() {
        IntegerSerializer integerSerializer = new IntegerSerializer();
        assertEquals("1", integerSerializer.serializeToString(integerValueOf(1)));
    }

    @Test
    public void testIntLikeSerializer() {
        IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
        assertEquals("1", intLikeSerializer.serializeToString(longValueOf(1)));
    }

    @Test
    public void testLongSerializer() {
        LongSerializer longSerializer = new LongSerializer();
        assertEquals("1", longSerializer.serializeToString(longValueOf(1)));
    }

    @Test
    public void testFloatSerializer() {
        FloatSerializer floatSerializer = new FloatSerializer();
        assertEquals("1.0", floatSerializer.serializeToString(floatValueOf(1.0f)));
    }

    @Test
    public void testDoubleSerializer() {
        DoubleSerializer doubleSerializer = new DoubleSerializer();
        assertEquals("1.0", doubleSerializer.serializeToString(doubleValueOf(1.0d)));
    }

    @Test
    public void testShortSerializerAcceptJsonFormatVisitor() {
        ShortSerializer shortSerializer = new ShortSerializer();
        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = createJsonFormatVisitorWrapper();
        shortSerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, null);
        assertTrue(jsonFormatVisitorWrapper.expectIntegerFormat(null) != null);
    }

    @Test
    public void testIntegerSerializerAcceptJsonFormatVisitor() {
        IntegerSerializer integerSerializer = new IntegerSerializer();
        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = createJsonFormatVisitorWrapper();
        integerSerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, null);
        assertTrue(jsonFormatVisitorWrapper.expectIntegerFormat(null) != null);
    }

    @Test
    public void testIntLikeSerializerAcceptJsonFormatVisitor() {
        IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = createJsonFormatVisitorWrapper();
        intLikeSerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, null);
        assertTrue(jsonFormatVisitorWrapper.expectIntegerFormat(null) != null);
    }

    @Test
    public void testLongSerializerAcceptJsonFormatVisitor() {
        LongSerializer longSerializer = new LongSerializer();
        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = createJsonFormatVisitorWrapper();
        longSerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, null);
        assertTrue(jsonFormatVisitorWrapper.expectNumberFormat(null) != null);
    }

    @Test
    public void testFloatSerializerAcceptJsonFormatVisitor() {
        FloatSerializer floatSerializer = new FloatSerializer();
        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = createJsonFormatVisitorWrapper();
        floatSerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, null);
        assertTrue(jsonFormatVisitorWrapper.expectNumberFormat(null) != null);
    }

    @Test
    public void testDoubleSerializerAcceptJsonFormatVisitor() {
        DoubleSerializer doubleSerializer = new DoubleSerializer();
        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = createJsonFormatVisitorWrapper();
        doubleSerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, null);
        assertTrue(jsonFormatVisitorWrapper.expectNumberFormat(null) != null);
    }

    private JsonFormatVisitorWrapper createJsonFormatVisitorWrapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonSerializationConfig config = objectMapper.getSerializationConfig();
        JsonFactory jsonFactory = config.getDefaultSerializers();
        return jsonFactory.getJsonFormatVisitorWrapper(config);
    }

    private Number valueOf(int value) {
        return value > Byte.MIN_VALUE && value < Byte.MAX_VALUE ? byteValueOf((byte) value) :
               value > Short.MIN_VALUE && value < Short.MAX_VALUE ? shortValueOf((short) value) :
               value > Integer.MIN_VALUE && value < Integer.MAX_VALUE ? integerValueOf(value) :
               value > Long.MIN_VALUE && value < Long.MAX_VALUE ? longValueOf((long) value) :
               value > Float.MIN_VALUE && value < Float.MAX_VALUE ? floatValueOf((float) value) :
               value > Double.MIN_VALUE && value < Double.MAX_VALUE ? doubleValueOf((double) value) : null;
    }

    private Byte byteValueOf(byte value) {
        return value;
    }

    private Short shortValueOf(short value) {
        return value;
    }

    private Integer integerValueOf(int value) {
        return value;
    }

    private Long longValueOf(long value) {
        return value;
    }

    private Float floatValueOf(float value) {
        return value;
    }

    private Double doubleValueOf(double value) {
        return value;
    }
}