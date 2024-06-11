package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

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
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.*;

public class NumberSerializersTest {
    
    // Test addAll method
    @SuppressWarnings("serial")
	@Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);

        assertEquals(IntegerSerializer.class, allDeserializers.get(Integer.class.getName()).getClass());
        assertEquals(IntegerSerializer.class, allDeserializers.get(Integer.TYPE.getName()).getClass());
        assertEquals(LongSerializer.class, allDeserializers.get(Long.class.getName()).getClass());
        assertEquals(LongSerializer.class, allDeserializers.get(Long.TYPE.getName()).getClass());

        assertEquals(IntLikeSerializer.instance.getClass(), allDeserializers.get(Byte.class.getName()).getClass());
        assertEquals(IntLikeSerializer.instance.getClass(), allDeserializers.get(Byte.TYPE.getName()).getClass());
        assertEquals(ShortSerializer.instance.getClass(), allDeserializers.get(Short.class.getName()).getClass());
        assertEquals(ShortSerializer.instance.getClass(), allDeserializers.get(Short.TYPE.getName()).getClass());

        assertEquals(DoubleSerializer.class, allDeserializers.get(Double.class.getName()).getClass());
        assertEquals(DoubleSerializer.class, allDeserializers.get(Double.TYPE.getName()).getClass());
        assertEquals(FloatSerializer.instance.getClass(), allDeserializers.get(Float.class.getName()).getClass());
        assertEquals(FloatSerializer.instance.getClass(), allDeserializers.get(Float.TYPE.getName()).getClass());
    }

    @SuppressWarnings("serial")
	@Test
    public void testBaseClass() {
        Base<Object> base = new Base<>(Number.class, JsonParser.NumberType.INT, "integer") {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider provider)
                    throws IOException {
            }

            @Override
            public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property)
                    throws JsonMappingException {
                return null;
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                    throws JsonMappingException {
            }

            @Override
            public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
                return null;
            }
        };

        assertEquals(JsonParser.NumberType.INT, base._numberType);
        assertEquals("integer", base._schemaType);
        assertEquals(Number.class, base.handledType());
    }

    @Test
    public void testShortSerializer() {
        ShortSerializer shortSerializer = new ShortSerializer();

        assertEquals(Short.class, shortSerializer.handledType());

        Short value = 42;
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        try {
            shortSerializer.serialize(value, gen, provider);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testIntegerSerializer() {
        IntegerSerializer integerSerializer = new IntegerSerializer(Integer.class);

        assertEquals(Integer.class, integerSerializer.handledType());

        Integer value = 42;
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        try {
            integerSerializer.serialize(value, gen, provider);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testIntLikeSerializer() {
        IntLikeSerializer intLikeSerializer = new IntLikeSerializer();

        assertEquals(Number.class, intLikeSerializer.handledType());

        Integer value = 42;
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        try {
            intLikeSerializer.serialize(value, gen, provider);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testLongSerializer() {
        LongSerializer longSerializer = new LongSerializer(Long.class);

        assertEquals(Long.class, longSerializer.handledType());

        Long value = 42L;
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        try {
            longSerializer.serialize(value, gen, provider);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testFloatSerializer() {
        FloatSerializer floatSerializer = new FloatSerializer();

        assertEquals(Float.class, floatSerializer.handledType());

        Float value = 42.0f;
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        try {
            floatSerializer.serialize(value, gen, provider);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testDoubleSerializer() {
        DoubleSerializer doubleSerializer = new DoubleSerializer(Double.class);

        assertEquals(Double.class, doubleSerializer.handledType());

        Double value = 42.0;
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        try {
            doubleSerializer.serialize(value, gen, provider);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}