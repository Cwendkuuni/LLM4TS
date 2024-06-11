
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.DoubleSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.FloatSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.IntegerSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.LongSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.ShortSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.IntLikeSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.Base;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NumberSerializersTest {

    private Map<String, JsonSerializer<?>> deserializers;

    @Before
    public void setUp() {
        deserializers = new HashMap<>();
    }

    @Test
    public void testAddAll() {
        NumberSerializers.addAll(deserializers);
        assertFalse(deserializers.isEmpty());
        assertEquals(8, deserializers.size());
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        JsonSerializer<?> serializer = deserializers.get(Integer.class.getName());
        assertNotNull(serializer);
        assertTrue(serializer instanceof IntegerSerializer);
        IntegerSerializer integerSerializer = (IntegerSerializer) serializer;
        assertNotNull(integerSerializer.getSchema(null, null));
        ObjectMapper mapper = new ObjectMapper();
        TypeSerializer typeSerializer = mapper.getSerializerFactory().createTypeSerializer(mapper.getSerializationConfig(), null);
        integerSerializer.serializeWithType(10, null, null, typeSerializer);
        // Add more test cases
    }

    @Test
    public void testLongSerializer() throws IOException {
        JsonSerializer<?> serializer = deserializers.get(Long.class.getName());
        assertNotNull(serializer);
        assertTrue(serializer instanceof LongSerializer);
        LongSerializer longSerializer = (LongSerializer) serializer;
        assertNotNull(longSerializer.getSchema(null, null));
        ObjectMapper mapper = new ObjectMapper();
        TypeSerializer typeSerializer = mapper.getSerializerFactory().createTypeSerializer(mapper.getSerializationConfig(), null);
        longSerializer.serializeWithType(1000L, null, null, typeSerializer);
        // Add more test cases
    }

    @Test
    public void testShortSerializer() throws IOException {
        JsonSerializer<?> serializer = deserializers.get(Short.class.getName());
        assertNotNull(serializer);
        assertTrue(serializer instanceof ShortSerializer);
        ShortSerializer shortSerializer = (ShortSerializer) serializer;
        assertNotNull(shortSerializer.getSchema(null, null));
        ObjectMapper mapper = new ObjectMapper();
        TypeSerializer typeSerializer = mapper.getSerializerFactory().createTypeSerializer(mapper.getSerializationConfig(), null);
        shortSerializer.serializeWithType((short) 5, null, null, typeSerializer);
        // Add more test cases
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        JsonSerializer<?> serializer = deserializers.get(Number.class.getName());
        assertNotNull(serializer);
        assertTrue(serializer instanceof IntLikeSerializer);
        IntLikeSerializer intLikeSerializer = (IntLikeSerializer) serializer;
        assertNotNull(intLikeSerializer.getSchema(null, null));
        ObjectMapper mapper = new ObjectMapper();
        TypeSerializer typeSerializer = mapper.getSerializerFactory().createTypeSerializer(mapper.getSerializationConfig(), null);
        intLikeSerializer.serializeWithType(1000, null, null, typeSerializer);
        // Add more test cases
    }

    @Test
    public void testFloatSerializer() throws IOException {
        JsonSerializer<?> serializer = deserializers.get(Float.class.getName());
        assertNotNull(serializer);
        assertTrue(serializer instanceof FloatSerializer);
        FloatSerializer floatSerializer = (FloatSerializer) serializer;
        assertNotNull(floatSerializer.getSchema(null, null));
        ObjectMapper mapper = new ObjectMapper();
        TypeSerializer typeSerializer = mapper.getSerializerFactory().createTypeSerializer(mapper.getSerializationConfig(), null);
        floatSerializer.serializeWithType(10.5f, null, null, typeSerializer);
        // Add more test cases
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        JsonSerializer<?> serializer = deserializers.get(Double.class.getName());
        assertNotNull(serializer);
        assertTrue(serializer instanceof DoubleSerializer);
        DoubleSerializer doubleSerializer = (DoubleSerializer) serializer;
        assertNotNull(doubleSerializer.getSchema(null, null));
        ObjectMapper mapper = new ObjectMapper();
        TypeSerializer typeSerializer = mapper.getSerializerFactory().createTypeSerializer(mapper.getSerializationConfig(), null);
        doubleSerializer.serializeWithType(3.14, null, null, typeSerializer);
        // Add more test cases
    }

    // Add more test methods for other serializers

}

