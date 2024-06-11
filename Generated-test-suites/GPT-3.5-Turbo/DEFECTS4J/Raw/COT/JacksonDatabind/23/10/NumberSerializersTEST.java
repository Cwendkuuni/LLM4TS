import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;

public class NumberSerializersTest {

    private Map<String, JsonSerializer<?>> deserializers;

    @Before
    public void setUp() {
        deserializers = new HashMap<>();
        NumberSerializers.addAll(deserializers);
    }

    @Test
    public void testIntegerSerializer() throws JsonProcessingException {
        Assert.assertTrue(deserializers.containsKey(Integer.class.getName()));
        Assert.assertTrue(deserializers.containsKey(Integer.TYPE.getName()));

        JsonSerializer<?> serializer = deserializers.get(Integer.class.getName());
        Assert.assertNotNull(serializer);
        Assert.assertEquals(NumberSerializers.IntegerSerializer.class, serializer.getClass());

        serializer = deserializers.get(Integer.TYPE.getName());
        Assert.assertNotNull(serializer);
        Assert.assertEquals(NumberSerializers.IntegerSerializer.class, serializer.getClass());
    }

    @Test
    public void testLongSerializer() throws JsonProcessingException {
        Assert.assertTrue(deserializers.containsKey(Long.class.getName()));
        Assert.assertTrue(deserializers.containsKey(Long.TYPE.getName()));

        JsonSerializer<?> serializer = deserializers.get(Long.class.getName());
        Assert.assertNotNull(serializer);
        Assert.assertEquals(NumberSerializers.LongSerializer.class, serializer.getClass());

        serializer = deserializers.get(Long.TYPE.getName());
        Assert.assertNotNull(serializer);
        Assert.assertEquals(NumberSerializers.LongSerializer.class, serializer.getClass());
    }

    @Test
    public void testByteSerializer() throws JsonProcessingException {
        Assert.assertTrue(deserializers.containsKey(Byte.class.getName()));
        Assert.assertTrue(deserializers.containsKey(Byte.TYPE.getName()));

        JsonSerializer<?> serializer = deserializers.get(Byte.class.getName());
        Assert.assertNotNull(serializer);
        Assert.assertEquals(NumberSerializers.IntLikeSerializer.class, serializer.getClass());

        serializer = deserializers.get(Byte.TYPE.getName());
        Assert.assertNotNull(serializer);
        Assert.assertEquals(NumberSerializers.IntLikeSerializer.class, serializer.getClass());
    }

    @Test
    public void testShortSerializer() throws JsonProcessingException {
        Assert.assertTrue(deserializers.containsKey(Short.class.getName()));
        Assert.assertTrue(deserializers.containsKey(Short.TYPE.getName()));

        JsonSerializer<?> serializer = deserializers.get(Short.class.getName());
        Assert.assertNotNull(serializer);
        Assert.assertEquals(NumberSerializers.ShortSerializer.class, serializer.getClass());

        serializer = deserializers.get(Short.TYPE.getName());
        Assert.assertNotNull(serializer);
        Assert.assertEquals(NumberSerializers.ShortSerializer.class, serializer.getClass());
    }

    @Test
    public void testFloatSerializer() throws JsonProcessingException {
        Assert.assertTrue(deserializers.containsKey(Float.class.getName()));
        Assert.assertTrue(deserializers.containsKey(Float.TYPE.getName()));

        JsonSerializer<?> serializer = deserializers.get(Float.class.getName());
        Assert.assertNotNull(serializer);
        Assert.assertEquals(NumberSerializers.FloatSerializer.class, serializer.getClass());

        serializer = deserializers.get(Float.TYPE.getName());
        Assert.assertNotNull(serializer);
        Assert.assertEquals(NumberSerializers.FloatSerializer.class, serializer.getClass());
    }

    @Test
    public void testDoubleSerializer() throws JsonProcessingException {
        Assert.assertTrue(deserializers.containsKey(Double.class.getName()));
        Assert.assertTrue(deserializers.containsKey(Double.TYPE.getName()));

        JsonSerializer<?> serializer = deserializers.get(Double.class.getName());
        Assert.assertNotNull(serializer);
        Assert.assertEquals(NumberSerializers.DoubleSerializer.class, serializer.getClass());

        serializer = deserializers.get(Double.TYPE.getName());
        Assert.assertNotNull(serializer);
        Assert.assertEquals(NumberSerializers.DoubleSerializer.class, serializer.getClass());
    }

}