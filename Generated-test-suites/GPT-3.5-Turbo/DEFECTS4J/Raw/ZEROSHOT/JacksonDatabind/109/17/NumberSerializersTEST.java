import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class NumberSerializersTest {

    private Map<String, JsonSerializer<?>> allDeserializers;

    @Before
    public void setUp() {
        allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);
    }

    @Test
    public void testIntegerSerializers() throws IOException {
        JsonSerializer<?> serializer = allDeserializers.get(Integer.class.getName());
        Assert.assertEquals(NumberSerializers.IntegerSerializer.class, serializer.getClass());

        JsonGenerator gen = null; // Dummy JsonGenerator
        SerializerProvider provider = null; // Dummy SerializerProvider

        Integer value = 10;
        serializer.serialize(value, gen, provider);
        // TODO: Validate the output of the serialize method
    }

    @Test
    public void testLongSerializers() throws IOException {
        JsonSerializer<?> serializer = allDeserializers.get(Long.class.getName());
        Assert.assertEquals(NumberSerializers.LongSerializer.class, serializer.getClass());

        JsonGenerator gen = null; // Dummy JsonGenerator
        SerializerProvider provider = null; // Dummy SerializerProvider

        Long value = 10000000000L;
        serializer.serialize(value, gen, provider);
        // TODO: Validate the output of the serialize method
    }

    @Test
    public void testShortSerializers() throws IOException {
        JsonSerializer<?> serializer = allDeserializers.get(Short.class.getName());
        Assert.assertEquals(NumberSerializers.ShortSerializer.class, serializer.getClass());

        JsonGenerator gen = null; // Dummy JsonGenerator
        SerializerProvider provider = null; // Dummy SerializerProvider

        Short value = 100;
        serializer.serialize(value, gen, provider);
        // TODO: Validate the output of the serialize method
    }

    @Test
    public void testByteSerializers() throws IOException {
        JsonSerializer<?> serializer = allDeserializers.get(Byte.class.getName());
        Assert.assertEquals(NumberSerializers.IntLikeSerializer.class, serializer.getClass());

        JsonGenerator gen = null; // Dummy JsonGenerator
        SerializerProvider provider = null; // Dummy SerializerProvider

        Byte value = 127;
        serializer.serialize(value, gen, provider);
        // TODO: Validate the output of the serialize method
    }

    @Test
    public void testDoubleSerializers() throws IOException {
        JsonSerializer<?> serializer = allDeserializers.get(Double.class.getName());
        Assert.assertEquals(NumberSerializers.DoubleSerializer.class, serializer.getClass());

        JsonGenerator gen = null; // Dummy JsonGenerator
        SerializerProvider provider = null; // Dummy SerializerProvider

        Double value = 10.5;
        serializer.serialize(value, gen, provider);
        // TODO: Validate the output of the serialize method
    }

    @Test
    public void testFloatSerializers() throws IOException {
        JsonSerializer<?> serializer = allDeserializers.get(Float.class.getName());
        Assert.assertEquals(NumberSerializers.FloatSerializer.class, serializer.getClass());

        JsonGenerator gen = null; // Dummy JsonGenerator
        SerializerProvider provider = null; // Dummy SerializerProvider

        Float value = 5.5f;
        serializer.serialize(value, gen, provider);
        // TODO: Validate the output of the serialize method
    }

    @Test
    public void testIntegerPrimitiveSerializers() throws IOException {
        JsonSerializer<?> serializer = allDeserializers.get(Integer.TYPE.getName());
        Assert.assertEquals(NumberSerializers.IntegerSerializer.class, serializer.getClass());

        JsonGenerator gen = null; // Dummy JsonGenerator
        SerializerProvider provider = null; // Dummy SerializerProvider

        int value = 10;
        serializer.serialize(value, gen, provider);
        // TODO: Validate the output of the serialize method
    }

    @Test
    public void testLongPrimitiveSerializers() throws IOException {
        JsonSerializer<?> serializer = allDeserializers.get(Long.TYPE.getName());
        Assert.assertEquals(NumberSerializers.LongSerializer.class, serializer.getClass());

        JsonGenerator gen = null; // Dummy JsonGenerator
        SerializerProvider provider = null; // Dummy SerializerProvider

        long value = 10000000000L;
        serializer.serialize(value, gen, provider);
        // TODO: Validate the output of the serialize method
    }
}