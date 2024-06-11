package com.fasterxml.jackson.databind.ser.std;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.*;

public class NumberSerializersTest {

    @Test
    public void testAllSerializersRegistered() {
        Map<String, JsonSerializer<?>> allSerializers = new LinkedHashMap<>();
        NumberSerializers.addAll(allSerializers);

        assertEquals(11, allSerializers.size());
        assertTrue(allSerializers.containsKey(Integer.class.getName()));
        assertTrue(allSerializers.containsKey(Integer.TYPE.getName()));
        assertTrue(allSerializers.containsKey(Long.class.getName()));
        assertTrue(allSerializers.containsKey(Long.TYPE.getName()));
        assertTrue(allSerializers.containsKey(Byte.class.getName()));
        assertTrue(allSerializers.containsKey(Byte.TYPE.getName()));
        assertTrue(allSerializers.containsKey(Short.class.getName()));
        assertTrue(allSerializers.containsKey(Short.TYPE.getName()));
        assertTrue(allSerializers.containsKey(Double.class.getName()));
        assertTrue(allSerializers.containsKey(Double.TYPE.getName()));
        assertTrue(allSerializers.containsKey(Float.class.getName()));
        assertTrue(allSerializers.containsKey(Float.TYPE.getName()));
    }

    @Test
    public void testShortSerializer() throws JsonProcessingException {
        ShortSerializer serializer = new ShortSerializer();
        ObjectWriter writer = new ObjectMapper().writerWithRootValueSerializer(serializer);

        JsonNode node = writer.writeValueAsTree(new Short("123"));
        assertEquals(123, node.numberValue().intValue());

        Short deserialized = writer.readValueAs(JsonNodeClass.class, Short.class);
        assertEquals(123, deserialized.shortValue());
    }

    @Test
    public void testIntegerSerializer() throws JsonProcessingException {
        IntegerSerializer serializer = new IntegerSerializer(Integer.class);
        ObjectWriter writer = new ObjectMapper().writerWithRootValueSerializer(serializer);

        JsonNode node = writer.writeValueAsTree(new Integer(123));
        assertEquals(123, node.numberValue().intValue());

        Integer deserialized = writer.readValueAs(JsonNodeClass.class, Integer.class);
        assertEquals(123, deserialized.intValue());
    }

    @Test
    public void testIntLikeSerializer() throws JsonProcessingException {
        IntLikeSerializer serializer = new IntLikeSerializer();
        ObjectWriter writer = new ObjectMapper().writerWithRootValueSerializer(serializer);

        JsonNode node = writer.writeValueAsTree(new Byte((byte) 123));
        assertEquals(123, node.numberValue().intValue());

        Byte deserialized = writer.readValueAs(JsonNodeClass.class, Byte.class);
        assertEquals((byte) 123, deserialized.byteValue());
    }

    @Test
    public void testLongSerializer() throws JsonProcessingException {
        LongSerializer serializer = new LongSerializer(Long.class);
        ObjectWriter writer = new ObjectMapper().writerWithRootValueSerializer(serializer);

        JsonNode node = writer.writeValueAsTree(new Long(1234567890123L));
        assertEquals(1234567890123L, node.numberValue().longValue());

        Long deserialized = writer.readValueAs(JsonNodeClass.class, Long.class);
        assertEquals(1234567890123L, deserialized.longValue());
    }

    @Test
    public void testFloatSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Float> floatSerializer = mapper.findValueSerializer(Float.class);

        JsonNode node = mapper.valueToTree(new Float(3.14f));
        assertEquals(3.14f, node.numberValue().floatValue(), 0.001f);

        Float deserialized = mapper.readValue(JsonNodeClass.class, Float.class);
        assertEquals(3.14f, deserialized.floatValue(), 0.001f);
    }

    @Test
    public void testDoubleSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Double> doubleSerializer = mapper.findValueSerializer(Double.class);

        JsonNode node = mapper.valueToTree(new Double(3.14159265358979323846));
        assertEquals(3.14159265358979323846, node.numberValue().doubleValue(), 0.000000000001);

        Double deserialized = mapper.readValue(JsonNodeClass.class, Double.class);
        assertEquals(3.14159265358979323846, deserialized.doubleValue(), 0.000000000001);
    }

    @Test
    public void testJsonFormatAnnotation() throws JsonProcessingException {
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        Integer serializeAsString = 123;

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writerWithRootValueSerializer(new ToStringSerializer(Integer.class));

        JsonNode node = writer.writeValueAsTree(serializeAsString);
        assertEquals("123", node.textValue());

        Integer deserialized = mapper.readValueAs(JsonNodeClass.class, Integer.class);
        assertEquals(123, deserialized.intValue());
    }
}