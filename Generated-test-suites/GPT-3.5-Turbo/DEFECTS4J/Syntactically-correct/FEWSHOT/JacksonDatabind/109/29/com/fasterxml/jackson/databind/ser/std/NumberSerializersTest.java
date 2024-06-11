package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.fasterxml.jackson.core.JsonGenerator;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;


public class NumberSerializersTest {

    private Map<String, JsonSerializer<?>> allDeserializers = new HashMap<String, JsonSerializer<?>>();

    @Before
    public void setUp() {
        NumberSerializers.addAll(allDeserializers);
    }

    @Test
    public void testAddAll() {
        Set<String> keys = allDeserializers.keySet();
        assertEquals(10, keys.size());
        assertNotNull(allDeserializers.get(Integer.class.getName()));
        assertNotNull(allDeserializers.get(Integer.TYPE.getName()));
        assertNotNull(allDeserializers.get(Long.class.getName()));
        assertNotNull(allDeserializers.get(Long.TYPE.getName()));
        assertNotNull(allDeserializers.get(Byte.class.getName()));
        assertNotNull(allDeserializers.get(Byte.TYPE.getName()));
        assertNotNull(allDeserializers.get(Short.class.getName()));
        assertNotNull(allDeserializers.get(Short.TYPE.getName()));
        assertNotNull(allDeserializers.get(Double.class.getName()));
        assertNotNull(allDeserializers.get(Double.TYPE.getName()));
        assertNotNull(allDeserializers.get(Float.class.getName()));
        assertNotNull(allDeserializers.get(Float.TYPE.getName()));
    }

    @Test
    public void testShortSerializer() throws IOException {
        NumberSerializers.ShortSerializer serializer = new NumberSerializers.ShortSerializer();
        Short value = Short.valueOf((short) 10);
        JsonGenerator generator = new ObjectMapper().getFactory().createGenerator(System.out);
        serializer.serialize(value, generator, null);
        generator.close();
    }

    @Test
    public void testShortSerializerSchema() {
        NumberSerializers.ShortSerializer serializer = new NumberSerializers.ShortSerializer();
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        JavaType typeHint = null;
        JsonNode schema = serializer.getSchema(provider, typeHint);
        assertNotNull(schema);
        assertEquals("number", schema.get("type").asText());
    }

    @Test
    public void testShortSerializerFormatVisitor() throws JsonMappingException {
        NumberSerializers.ShortSerializer serializer = new NumberSerializers.ShortSerializer();
        JsonFormatVisitorWrapper visitor = new ObjectMapper().getSerializerProvider().getProviderInstance().getFactory().getVisitorWrapper();
        JavaType typeHint = null;
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer(Integer.class);
        Integer value = Integer.valueOf(10);
        JsonGenerator generator = new ObjectMapper().getFactory().createGenerator(System.out);
        serializer.serialize(value, generator, null);
        generator.close();
    }

    @Test
    public void testIntegerSerializerWithType() throws IOException {
        NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer(Integer.class);
        Integer value = Integer.valueOf(10);
        JsonGenerator generator = new ObjectMapper().getFactory().createGenerator(System.out);
        TypeSerializer typeSer = null;
        serializer.serializeWithType(value, generator, null, typeSer);
        generator.close();
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        NumberSerializers.IntLikeSerializer serializer = new NumberSerializers.IntLikeSerializer();
        Number value = Integer.valueOf(10);
        JsonGenerator generator = new ObjectMapper().getFactory().createGenerator(System.out);
        serializer.serialize(value, generator, null);
        generator.close();
    }

    @Test
    public void testLongSerializer() throws IOException {
        NumberSerializers.LongSerializer serializer = new NumberSerializers.LongSerializer(Long.class);
        Long value = Long.valueOf(10L);
        JsonGenerator generator = new ObjectMapper().getFactory().createGenerator(System.out);
        serializer.serialize(value, generator, null);
        generator.close();
    }

    @Test
    public void testFloatSerializer() throws IOException {
        NumberSerializers.FloatSerializer serializer = new NumberSerializers.FloatSerializer();
        Float value = Float.valueOf(10.0f);
        JsonGenerator generator = new ObjectMapper().getFactory().createGenerator(System.out);
        serializer.serialize(value, generator, null);
        generator.close();
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        NumberSerializers.DoubleSerializer serializer = new NumberSerializers.DoubleSerializer(Double.class);
        Double value = Double.valueOf(10.0d);
        JsonGenerator generator = new ObjectMapper().getFactory().createGenerator(System.out);
        serializer.serialize(value, generator, null);
        generator.close();
    }

    @Test
    public void testDoubleSerializerWithType() throws IOException {
        NumberSerializers.DoubleSerializer serializer = new NumberSerializers.DoubleSerializer(Double.class);
        Double value = Double.valueOf(10.0d);
        JsonGenerator generator = new ObjectMapper().getFactory().createGenerator(System.out);
        TypeSerializer typeSer = null;
        serializer.serializeWithType(value, generator, null, typeSer);
        generator.close();
    }
}