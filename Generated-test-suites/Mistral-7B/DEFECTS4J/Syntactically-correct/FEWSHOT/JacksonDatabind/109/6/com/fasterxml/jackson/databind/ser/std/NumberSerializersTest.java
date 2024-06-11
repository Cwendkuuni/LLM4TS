package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new java.util.LinkedHashMap<>();
        NumberSerializers.addAll(allDeserializers);

        assertEquals(11, allDeserializers.size());
        assertEquals(Integer.class.getName(), allDeserializers.get(Integer.class.getName()).getClass().getName());
        assertEquals(Integer.TYPE.getName(), allDeserializers.get(Integer.TYPE.getName()).getClass().getName());
        // ... add assertions for other classes as well
    }

    @Test
    public void testBaseClass() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        Class<?> cls = Integer.class;
        JsonSerializer<Object> serializer = NumberSerializers.findValueSerializer(mapper.getTypeFactory(), cls);
        assertEquals(IntegerSerializer.class, serializer.getClass());

        cls = Long.class;
        serializer = NumberSerializers.findValueSerializer(mapper.getTypeFactory(), cls);
        assertEquals(LongSerializer.class, serializer.getClass());

        // ... add test cases for other classes as well
    }

    @Test
    public void testIntegerSerializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        Integer number = 123;
        String json = mapper.writeValueAsString(number);
        assertEquals("123", json);

        // test serialization with type info
        TypeSerializer typeSer = mapper.getTypeSerializer();
        JsonSerializer<Object> serializerWithType = NumberSerializers.findValueSerializer(mapper.getTypeFactory(), Integer.class);
        ObjectWriter objectWriter = mapper.writer().withTypeSerializer(typeSer);
        json = objectWriter.writeValueAsString(number);
        assertEquals("123", json);
        assertEquals(Integer.class.getName(), json.getClass().getDelegateType().getName());
    }

    @Test
    public void testIntLikeSerializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        Byte byteValue = 1;
        String json = mapper.writeValueAsString(byteValue);
        assertEquals("1", json);

        json = mapper.writeValueAsString((Short) byteValue);
        assertEquals("1", json);

        json = mapper.writeValueAsString((Number) byteValue);
        assertEquals("1", json);

        // test serialization with type info
        TypeSerializer typeSer = mapper.getTypeSerializer();
        JsonSerializer<Object> serializerWithType = NumberSerializers.findValueSerializer(mapper.getTypeFactory(), Byte.class);
        ObjectWriter objectWriter = mapper.writer().withTypeSerializer(typeSer);
        json = objectWriter.writeValueAsString(byteValue);
        assertEquals("1", json);
        assertEquals(Byte.class.getName(), json.getClass().getDelegateType().getName());
    }

    @Test
    public void testLongSerializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        Long number = 123L;
        String json = mapper.writeValueAsString(number);
        assertEquals("123", json);

        // test serialization with type info
        TypeSerializer typeSer = mapper.getTypeSerializer();
        JsonSerializer<Object> serializerWithType = NumberSerializers.findValueSerializer(mapper.getTypeFactory(), Long.class);
        ObjectWriter objectWriter = mapper.writer().withTypeSerializer(typeSer);
        json = objectWriter.writeValueAsString(number);
        assertEquals("123", json);
        assertEquals(Long.class.getName(), json.getClass().getDelegateType().getName());
    }

    @Test
    public void testJsonFormatAnnotation() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        int number = 123;
        String json = mapper.writeValueAsString(number);
        assertEquals("123", json);

        json = mapper.writeValueAsString(NumberSerializers.findValueSerializer(mapper.getTypeFactory(), Integer.class), number, new SerializerProvider(), null);
        assertEquals("123", json);

        // test serialization with type info
        TypeSerializer typeSer = mapper.getTypeSerializer();
        ObjectWriter objectWriter = mapper.writer().withTypeSerializer(typeSer);
        json = objectWriter.writeValueAsString(number);
        assertEquals("123", json);
        assertEquals(Integer.class.getName(), json.getClass().getDelegateType().getName());
    }

    @Test
    public void testNullValues() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        Integer integer = null;
        String json = mapper.writeValueAsString(integer);
        assertNull(json);

        Long longValue = null;
        json = mapper.writeValueAsString(longValue);
        assertNull(json);

        // test serialization with type info
        TypeSerializer typeSer = mapper.getTypeSerializer();
        ObjectWriter objectWriter = mapper.writer().withTypeSerializer(typeSer);
        json = objectWriter.writeValueAsString(integer);
        assertNull(json);
        assertEquals(Integer.class.getName(), json.getClass().getDelegateType().getName());

        json = objectWriter.writeValueAsString(longValue);
        assertNull(json);
        assertEquals(Long.class.getName(), json.getClass().getDelegateType().getName());
    }
}