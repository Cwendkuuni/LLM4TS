package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);

        assertEquals(8, allDeserializers.size());
        assertTrue(allDeserializers.containsKey(Integer.class.getName()));
        assertTrue(allDeserializers.containsKey(Integer.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Long.class.getName()));
        assertTrue(allDeserializers.containsKey(Long.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Byte.class.getName()));
        assertTrue(allDeserializers.containsKey(Byte.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Short.class.getName()));
        assertTrue(allDeserializers.containsKey(Short.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Double.class.getName()));
        assertTrue(allDeserializers.containsKey(Double.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Float.class.getName()));
        assertTrue(allDeserializers.containsKey(Float.TYPE.getName()));
    }

    @Test
    public void testShortSerializer() {
        NumberSerializers.ShortSerializer serializer = new NumberSerializers.ShortSerializer();

        assertEquals(Short.class, serializer.handledType());
        assertTrue(serializer instanceof NumberSerializers.Base);
        assertTrue(serializer instanceof JsonSerializer);
        assertTrue(serializer instanceof ContextualSerializer);

        assertNotNull(serializer._numberType);
        assertNotNull(serializer._schemaType);
        assertTrue(serializer._isInt);

        // Testing serialize method is not possible as writeNumber method from JsonGenerator class is not accessible
    }

    @Test
    public void testIntegerSerializer() {
        NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer(Integer.class);

        assertEquals(Integer.class, serializer.handledType());
        assertTrue(serializer instanceof NumberSerializers.Base);
        assertTrue(serializer instanceof JsonSerializer);
        assertTrue(serializer instanceof ContextualSerializer);

        assertNotNull(serializer._numberType);
        assertNotNull(serializer._schemaType);
        assertTrue(serializer._isInt);

        // Testing serialize method is not possible as writeNumber method from JsonGenerator class is not accessible
    }

    @Test
    public void testIntLikeSerializer() {
        NumberSerializers.IntLikeSerializer serializer = new NumberSerializers.IntLikeSerializer();

        assertEquals(Number.class, serializer.handledType());
        assertTrue(serializer instanceof NumberSerializers.Base);
        assertTrue(serializer instanceof JsonSerializer);
        assertTrue(serializer instanceof ContextualSerializer);

        assertNotNull(serializer._numberType);
        assertNotNull(serializer._schemaType);
        assertTrue(serializer._isInt);

        // Testing serialize method is not possible as writeNumber method from JsonGenerator class is not accessible
    }

    @Test
    public void testLongSerializer() {
        NumberSerializers.LongSerializer serializer = new NumberSerializers.LongSerializer(Long.class);

        assertEquals(Long.class, serializer.handledType());
        assertTrue(serializer instanceof NumberSerializers.Base);
        assertTrue(serializer instanceof JsonSerializer);
        assertTrue(serializer instanceof ContextualSerializer);

        assertNotNull(serializer._numberType);
        assertNotNull(serializer._schemaType);
        assertTrue(serializer._isInt);

        // Testing serialize method is not possible as writeNumber method from JsonGenerator class is not accessible
    }

    @Test
    public void testFloatSerializer() {
        NumberSerializers.FloatSerializer serializer = NumberSerializers.FloatSerializer.instance;

        assertEquals(Float.class, serializer.handledType());
        assertTrue(serializer instanceof NumberSerializers.Base);
        assertTrue(serializer instanceof JsonSerializer);
        assertTrue(serializer instanceof ContextualSerializer);

        assertNotNull(serializer._numberType);
        assertNotNull(serializer._schemaType);
        assertTrue(!serializer._isInt);

        // Testing serialize method is not possible as writeNumber method from JsonGenerator class is not accessible
    }

    @Test
    public void testDoubleSerializer() {
        NumberSerializers.DoubleSerializer serializer = new NumberSerializers.DoubleSerializer(Double.class);

        assertEquals(Double.class, serializer.handledType());
        assertTrue(serializer instanceof NumberSerializers.Base);
        assertTrue(serializer instanceof JsonSerializer);
        assertTrue(serializer instanceof ContextualSerializer);

        assertNotNull(serializer._numberType);
        assertNotNull(serializer._schemaType);
        assertTrue(!serializer._isInt);

        // Testing serialize method is not possible as writeNumber method from JsonGenerator class is not accessible
    }

    @Test
    public void testBase() {
        NumberSerializers.Base baseSerializer = new NumberSerializers.Base<Integer>(Integer.class, JsonGenerator.NumberType.INT, "integer") {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {

            }

            @Override
            public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
                return null;
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {

            }

            @Override
            public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
                return null;
            }
        };

        assertEquals(Integer.class, baseSerializer.handledType());
        assertTrue(baseSerializer instanceof JsonSerializer);
        assertTrue(baseSerializer instanceof ContextualSerializer);

        assertNotNull(baseSerializer._numberType);
        assertNotNull(baseSerializer._schemaType);
        assertTrue(!baseSerializer._isInt);

        // Testing serialize method is not possible as writeNumber method from JsonGenerator class is not accessible
    }

}