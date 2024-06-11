import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NumberSerializersTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static byte[] serialize(Object value) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JsonGenerator generator = objectMapper.getFactory().createGenerator(out);
        DefaultSerializerProvider provider = (DefaultSerializerProvider) objectMapper.getSerializerProvider();
        JsonSerializer<Object> serializer = provider.findTypedValueSerializer(value.getClass(), true, null);
        serializer.serialize(value, generator, provider);
        generator.close();
        return out.toByteArray();
    }

    @Test
    public void testShortSerializer() throws IOException {
        Map<String, JsonSerializer<?>> allSerializers = new HashMap<>();
        NumberSerializers.addAll(allSerializers);
        JsonSerializer<?> shortSerializer = allSerializers.get(Short.TYPE.getName());
        Assert.assertNotNull(shortSerializer);
        Assert.assertEquals(NumberSerializers.ShortSerializer.class, shortSerializer.getClass());

        Short value = Short.valueOf((short) 42);
        byte[] json = serialize(value);
        Assert.assertEquals("42", new String(json));
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        Map<String, JsonSerializer<?>> allSerializers = new HashMap<>();
        NumberSerializers.addAll(allSerializers);
        JsonSerializer<?> integerSerializer = allSerializers.get(Integer.TYPE.getName());
        Assert.assertNotNull(integerSerializer);
        Assert.assertEquals(NumberSerializers.IntegerSerializer.class, integerSerializer.getClass());

        Integer value = Integer.valueOf(42);
        byte[] json = serialize(value);
        Assert.assertEquals("42", new String(json));
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        Map<String, JsonSerializer<?>> allSerializers = new HashMap<>();
        NumberSerializers.addAll(allSerializers);
        JsonSerializer<?> intLikeSerializer = allSerializers.get(Byte.TYPE.getName());
        Assert.assertNotNull(intLikeSerializer);
        Assert.assertEquals(NumberSerializers.IntLikeSerializer.class, intLikeSerializer.getClass());

        Byte value = Byte.valueOf((byte) 42);
        byte[] json = serialize(value);
        Assert.assertEquals("42", new String(json));
    }

    @Test
    public void testLongSerializer() throws IOException {
        Map<String, JsonSerializer<?>> allSerializers = new HashMap<>();
        NumberSerializers.addAll(allSerializers);
        JsonSerializer<?> longSerializer = allSerializers.get(Long.TYPE.getName());
        Assert.assertNotNull(longSerializer);
        Assert.assertEquals(NumberSerializers.LongSerializer.class, longSerializer.getClass());

        Long value = Long.valueOf(42L);
        byte[] json = serialize(value);
        Assert.assertEquals("42", new String(json));
    }

    @Test
    public void testFloatSerializer() throws IOException {
        Map<String, JsonSerializer<?>> allSerializers = new HashMap<>();
        NumberSerializers.addAll(allSerializers);
        JsonSerializer<?> floatSerializer = allSerializers.get(Float.TYPE.getName());
        Assert.assertNotNull(floatSerializer);
        Assert.assertEquals(NumberSerializers.FloatSerializer.class, floatSerializer.getClass());

        Float value = Float.valueOf(3.14f);
        byte[] json = serialize(value);
        Assert.assertEquals("3.14", new String(json));
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        Map<String, JsonSerializer<?>> allSerializers = new HashMap<>();
        NumberSerializers.addAll(allSerializers);
        JsonSerializer<?> doubleSerializer = allSerializers.get(Double.TYPE.getName());
        Assert.assertNotNull(doubleSerializer);
        Assert.assertEquals(NumberSerializers.DoubleSerializer.class, doubleSerializer.getClass());

        Double value = Double.valueOf(3.14);
        byte[] json = serialize(value);
        Assert.assertEquals("3.14", new String(json));
    }
}