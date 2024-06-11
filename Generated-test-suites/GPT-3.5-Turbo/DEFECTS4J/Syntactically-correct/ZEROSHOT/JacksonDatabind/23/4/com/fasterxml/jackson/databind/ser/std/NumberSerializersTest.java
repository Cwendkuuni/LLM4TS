package com.fasterxml.jackson.databind.ser.std;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdScalarSerializer;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class NumberSerializersTest {

    private NumberSerializers numberSerializers;

    @Before
    public void setup() {
        numberSerializers = new NumberSerializers();
    }

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        numberSerializers.addAll(allDeserializers);

        assertEquals(8, allDeserializers.size());

        assertTrue(allDeserializers.containsKey(Integer.class.getName()));
        assertEquals(IntegerSerializer.class, allDeserializers.get(Integer.class.getName()).getClass());

        assertTrue(allDeserializers.containsKey(Integer.TYPE.getName()));
        assertEquals(IntegerSerializer.class, allDeserializers.get(Integer.TYPE.getName()).getClass());

        assertTrue(allDeserializers.containsKey(Long.class.getName()));
        assertEquals(LongSerializer.class, allDeserializers.get(Long.class.getName()).getClass());

        assertTrue(allDeserializers.containsKey(Long.TYPE.getName()));
        assertEquals(LongSerializer.class, allDeserializers.get(Long.TYPE.getName()).getClass());

        assertTrue(allDeserializers.containsKey(Byte.class.getName()));
        assertEquals(IntLikeSerializer.class, allDeserializers.get(Byte.class.getName()).getClass());

        assertTrue(allDeserializers.containsKey(Byte.TYPE.getName()));
        assertEquals(IntLikeSerializer.class, allDeserializers.get(Byte.TYPE.getName()).getClass());

        assertTrue(allDeserializers.containsKey(Short.class.getName()));
        assertEquals(ShortSerializer.class, allDeserializers.get(Short.class.getName()).getClass());

        assertTrue(allDeserializers.containsKey(Short.TYPE.getName()));
        assertEquals(ShortSerializer.class, allDeserializers.get(Short.TYPE.getName()).getClass());

        assertTrue(allDeserializers.containsKey(Float.class.getName()));
        assertEquals(FloatSerializer.class, allDeserializers.get(Float.class.getName()).getClass());

        assertTrue(allDeserializers.containsKey(Float.TYPE.getName()));
        assertEquals(FloatSerializer.class, allDeserializers.get(Float.TYPE.getName()).getClass());

        assertTrue(allDeserializers.containsKey(Double.class.getName()));
        assertEquals(DoubleSerializer.class, allDeserializers.get(Double.class.getName()).getClass());

        assertTrue(allDeserializers.containsKey(Double.TYPE.getName()));
        assertEquals(DoubleSerializer.class, allDeserializers.get(Double.TYPE.getName()).getClass());
    }

    @Test
    public void testBase() {

        Base<?> base = new Base<Object>(Integer.class, JsonParser.NumberType.INT, "number") {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {

            }
        };

        assertEquals(Integer.class, base.handledType());
        assertTrue(base.isEmpty(""));
        assertEquals(JsonParser.NumberType.INT, base._numberType);
        assertEquals("number", base._schemaType);
        assertTrue(base._isInt);

        assertNull(base.getSchema(null, null));

        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();
        base.acceptJsonFormatVisitor(visitor, null);
        assertEquals(visitor, base);

        assertNotNull(base.createContextual(null, null));
    }

    @Test
    public void testShortSerializer() {

        ShortSerializer shortSerializer = new ShortSerializer();

        assertEquals(Short.class, shortSerializer.handledType());
        assertFalse(shortSerializer.isEmpty(""));
        assertEquals(JsonParser.NumberType.INT, shortSerializer._numberType);
        assertEquals("number", shortSerializer._schemaType);

        try {
            shortSerializer.serialize((Short) null, null, null);
            fail("Should throw IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testIntegerSerializer() {

        IntegerSerializer integerSerializer = new IntegerSerializer();

        assertEquals(Integer.class, integerSerializer.handledType());
        assertFalse(integerSerializer.isEmpty(""));
        assertEquals(JsonParser.NumberType.INT, integerSerializer._numberType);
        assertEquals("integer", integerSerializer._schemaType);

        try {
            integerSerializer.serialize(null, null, null);
            fail("Should throw IOException");
        } catch (IOException e) {
            // expected
        }

        try {
            integerSerializer.serializeWithType(null, null, null, null);
            fail("Should throw IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testIntLikeSerializer() {

        IntLikeSerializer intLikeSerializer = new IntLikeSerializer();

        assertEquals(Number.class, intLikeSerializer.handledType());
        assertFalse(intLikeSerializer.isEmpty(""));
        assertEquals(JsonParser.NumberType.INT, intLikeSerializer._numberType);
        assertEquals("integer", intLikeSerializer._schemaType);

        try {
            intLikeSerializer.serialize((Number) null, null, null);
            fail("Should throw IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testLongSerializer() {

        LongSerializer longSerializer = new LongSerializer();

        assertEquals(Long.class, longSerializer.handledType());
        assertFalse(longSerializer.isEmpty(""));
        assertEquals(JsonParser.NumberType.LONG, longSerializer._numberType);
        assertEquals("number", longSerializer._schemaType);

        try {
            longSerializer.serialize((Long) null, null, null);
            fail("Should throw IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testFloatSerializer() {

        FloatSerializer floatSerializer = new FloatSerializer();

        assertEquals(Float.class, floatSerializer.handledType());
        assertFalse(floatSerializer.isEmpty(""));
        assertEquals(JsonParser.NumberType.FLOAT, floatSerializer._numberType);
        assertEquals("number", floatSerializer._schemaType);

        try {
            floatSerializer.serialize((Float) null, null, null);
            fail("Should throw IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testDoubleSerializer() {

        DoubleSerializer doubleSerializer = new DoubleSerializer();

        assertEquals(Double.class, doubleSerializer.handledType());
        assertFalse(doubleSerializer.isEmpty(""));
        assertEquals(JsonParser.NumberType.DOUBLE, doubleSerializer._numberType);
        assertEquals("number", doubleSerializer._schemaType);

        try {
            doubleSerializer.serialize((Double) null, null, null);
            fail("Should throw IOException");
        } catch (IOException e) {
            // expected
        }

        try {
            doubleSerializer.serializeWithType(null, null, null, null);
            fail("Should throw IOException");
        } catch (IOException e) {
            // expected
        }
    }
}