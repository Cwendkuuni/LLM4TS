import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.DoubleSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.FloatSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.IntLikeSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.IntegerSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.LongSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.ShortSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.Base;

public class NumberSerializersTest {

    @Test
    public void testShortSerializer() throws IOException {
        ShortSerializer serializer = new ShortSerializer();
        Short value = Short.valueOf("1");
        Map<String, JsonSerializer<?>> deserializers = new HashMap<>();
        NumberSerializers.addAll(deserializers);
        JsonSerializer<?> actualSerializer = deserializers.get(Short.class.getName());
        assertEquals(serializer, actualSerializer);
        JsonGenerator gen = null; // initialize a JsonGenerator object
        SerializerProvider provider = null; // initialize a SerializerProvider object
        serializer.serialize(value, gen, provider);
        // Assert the output of the serialization
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        IntegerSerializer serializer = new IntegerSerializer(Integer.class);
        Integer value = Integer.valueOf("1");
        Map<String, JsonSerializer<?>> deserializers = new HashMap<>();
        NumberSerializers.addAll(deserializers);
        JsonSerializer<?> actualSerializer = deserializers.get(Integer.class.getName());
        assertEquals(serializer, actualSerializer);
        JsonGenerator gen = null; // initialize a JsonGenerator object
        SerializerProvider provider = null; // initialize a SerializerProvider object
        serializer.serialize(value, gen, provider);
        // Assert the output of the serialization
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        IntLikeSerializer serializer = new IntLikeSerializer();
        Number value = Double.valueOf("1.0");
        Map<String, JsonSerializer<?>> deserializers = new HashMap<>();
        NumberSerializers.addAll(deserializers);
        JsonSerializer<?> actualSerializer = deserializers.get(Number.class.getName());
        assertEquals(serializer, actualSerializer);
        JsonGenerator gen = null; // initialize a JsonGenerator object
        SerializerProvider provider = null; // initialize a SerializerProvider object
        serializer.serialize(value, gen, provider);
        // Assert the output of the serialization
    }

    @Test
    public void testLongSerializer() throws IOException {
        LongSerializer serializer = new LongSerializer(Long.class);
        Long value = Long.valueOf("1");
        Map<String, JsonSerializer<?>> deserializers = new HashMap<>();
        NumberSerializers.addAll(deserializers);
        JsonSerializer<?> actualSerializer = deserializers.get(Long.class.getName());
        assertEquals(serializer, actualSerializer);
        JsonGenerator gen = null; // initialize a JsonGenerator object
        SerializerProvider provider = null; // initialize a SerializerProvider object
        serializer.serialize(value, gen, provider);
        // Assert the output of the serialization
    }
    
    @Test
    public void testFloatSerializer() throws IOException {
        FloatSerializer serializer = new FloatSerializer();
        Float value = Float.valueOf("1.0");
        Map<String, JsonSerializer<?>> deserializers = new HashMap<>();
        NumberSerializers.addAll(deserializers);
        JsonSerializer<?> actualSerializer = deserializers.get(Float.class.getName());
        assertEquals(serializer, actualSerializer);
        JsonGenerator gen = null; // initialize a JsonGenerator object
        SerializerProvider provider = null; // initialize a SerializerProvider object
        serializer.serialize(value, gen, provider);
        // Assert the output of the serialization
    }
    
    @Test
    public void testDoubleSerializer() throws IOException {
        DoubleSerializer serializer = new DoubleSerializer(Double.class);
        Double value = Double.valueOf("1.0");
        Map<String, JsonSerializer<?>> deserializers = new HashMap<>();
        NumberSerializers.addAll(deserializers);
        JsonSerializer<?> actualSerializer = deserializers.get(Double.class.getName());
        assertEquals(serializer, actualSerializer);
        JsonGenerator gen = null; // initialize a JsonGenerator object
        SerializerProvider provider = null; // initialize a SerializerProvider object
        serializer.serialize(value, gen, provider);
        // Assert the output of the serialization
    }
}