
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);

        // Test if all the serializers are added correctly
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
    public void testShortSerializer() throws IOException {
        NumberSerializers.ShortSerializer serializer = new NumberSerializers.ShortSerializer();
        Short value = 12345;
        JsonGenerator gen = null; // initialize with correct generator
        SerializerProvider provider = null; // initialize with correct provider

        // Test if the serializer correctly serializes the value
        try {
            serializer.serialize(value, gen, provider);
            // Assert the serialization result
        } catch (IOException e) {
            // Handle exception
        }
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer(Integer.class);
        Integer value = 123456789;
        JsonGenerator gen = null; // initialize with correct generator
        SerializerProvider provider = null; // initialize with correct provider

        // Test if the serializer correctly serializes the value
        try {
            serializer.serialize(value, gen, provider);
            // Assert the serialization result
        } catch (IOException e) {
            // Handle exception
        }
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        NumberSerializers.IntLikeSerializer serializer = NumberSerializers.IntLikeSerializer.instance;
        Number value = 123;
        JsonGenerator gen = null; // initialize with correct generator
        SerializerProvider provider = null; // initialize with correct provider

        // Test if the serializer correctly serializes the value
        try {
            serializer.serialize(value, gen, provider);
            // Assert the serialization result
        } catch (IOException e) {
            // Handle exception
        }
    }

    @Test
    public void testLongSerializer() throws IOException {
        NumberSerializers.LongSerializer serializer = new NumberSerializers.LongSerializer(Long.class);
        Long value = 123456789L;
        JsonGenerator gen = null; // initialize with correct generator
        SerializerProvider provider = null; // initialize with correct provider

        // Test if the serializer correctly serializes the value
        try {
            serializer.serialize(value, gen, provider);
            // Assert the serialization result
        } catch (IOException e) {
            // Handle exception
        }
    }

    @Test
    public void testFloatSerializer() throws IOException {
        NumberSerializers.FloatSerializer serializer = NumberSerializers.FloatSerializer.instance;
        Float value = 123.456f;
        JsonGenerator gen = null; // initialize with correct generator
        SerializerProvider provider = null; // initialize with correct provider

        // Test if the serializer correctly serializes the value
        try {
            serializer.serialize(value, gen, provider);
            // Assert the serialization result
        } catch (IOException e) {
            // Handle exception
        }
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        NumberSerializers.DoubleSerializer serializer = new NumberSerializers.DoubleSerializer(Double.class);
        Double value = 123.456;
        JsonGenerator gen = null; // initialize with correct generator
        SerializerProvider provider = null; // initialize with correct provider

        // Test if the serializer correctly serializes the value
        try {
            serializer.serialize(value, gen, provider);
            // Assert the serialization result
        } catch (IOException e) {
            // Handle exception
        }
    }
}

