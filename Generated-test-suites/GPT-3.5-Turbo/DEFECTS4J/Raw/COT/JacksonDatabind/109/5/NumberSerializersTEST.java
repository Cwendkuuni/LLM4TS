
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class NumberSerializersTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);

        Assert.assertEquals(allDeserializers.get(Integer.class.getName()).getClass(), NumberSerializers.IntegerSerializer.class);
        Assert.assertEquals(allDeserializers.get(Integer.TYPE.getName()).getClass(), NumberSerializers.IntegerSerializer.class);
        Assert.assertEquals(allDeserializers.get(Long.class.getName()).getClass(), NumberSerializers.LongSerializer.class);
        Assert.assertEquals(allDeserializers.get(Long.TYPE.getName()).getClass(), NumberSerializers.LongSerializer.class);
        Assert.assertEquals(allDeserializers.get(Byte.class.getName()).getClass(), NumberSerializers.IntLikeSerializer.class);
        Assert.assertEquals(allDeserializers.get(Byte.TYPE.getName()).getClass(), NumberSerializers.IntLikeSerializer.class);
        Assert.assertEquals(allDeserializers.get(Short.class.getName()).getClass(), NumberSerializers.ShortSerializer.class);
        Assert.assertEquals(allDeserializers.get(Short.TYPE.getName()).getClass(), NumberSerializers.ShortSerializer.class);
        Assert.assertEquals(allDeserializers.get(Double.class.getName()).getClass(), NumberSerializers.DoubleSerializer.class);
        Assert.assertEquals(allDeserializers.get(Double.TYPE.getName()).getClass(), NumberSerializers.DoubleSerializer.class);
        Assert.assertEquals(allDeserializers.get(Float.class.getName()).getClass(), NumberSerializers.FloatSerializer.class);
        Assert.assertEquals(allDeserializers.get(Float.TYPE.getName()).getClass(), NumberSerializers.FloatSerializer.class);
    }

    @Test
    public void testShortSerializer() {
        NumberSerializers.ShortSerializer serializer = new NumberSerializers.ShortSerializer();

        Assert.assertEquals(serializer.getResolvedType().getRawClass(), Short.class);

        Short value = 10;
        String expectedJson = "10";

        try {
            String json = mapper.writeValueAsString(value);
            Assert.assertEquals(json, expectedJson);
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testIntegerSerializer() {
        NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer(Integer.class);

        Assert.assertEquals(serializer.getResolvedType().getRawClass(), Integer.class);

        Integer value = 10;
        String expectedJson = "10";

        try {
            String json = mapper.writeValueAsString(value);
            Assert.assertEquals(json, expectedJson);
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testIntLikeSerializer() {
        NumberSerializers.IntLikeSerializer serializer = new NumberSerializers.IntLikeSerializer();

        Assert.assertEquals(serializer.getResolvedType().getRawClass(), Number.class);

        Byte value = 10;
        String expectedJson = "10";

        try {
            String json = mapper.writeValueAsString(value);
            Assert.assertEquals(json, expectedJson);
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testLongSerializer() {
        NumberSerializers.LongSerializer serializer = new NumberSerializers.LongSerializer(Long.class);

        Assert.assertEquals(serializer.getResolvedType().getRawClass(), Long.class);

        Long value = 10000000000L;
        String expectedJson = "10000000000";

        try {
            String json = mapper.writeValueAsString(value);
            Assert.assertEquals(json, expectedJson);
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFloatSerializer() {
        NumberSerializers.FloatSerializer serializer = new NumberSerializers.FloatSerializer();

        Assert.assertEquals(serializer.getResolvedType().getRawClass(), Float.class);

        Float value = 10.5f;
        String expectedJson = "10.5";

        try {
            String json = mapper.writeValueAsString(value);
            Assert.assertEquals(json, expectedJson);
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testDoubleSerializer() {
        NumberSerializers.DoubleSerializer serializer = new NumberSerializers.DoubleSerializer(Double.class);

        Assert.assertEquals(serializer.getResolvedType().getRawClass(), Double.class);

        Double value = 10.5;
        String expectedJson = "10.5";

        try {
            String json = mapper.writeValueAsString(value);
            Assert.assertEquals(json, expectedJson);
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }
}
