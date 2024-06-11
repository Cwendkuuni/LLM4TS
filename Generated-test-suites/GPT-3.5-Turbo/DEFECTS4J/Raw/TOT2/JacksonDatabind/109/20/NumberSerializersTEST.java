import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);

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
    public void testShortSerializer() {
        NumberSerializers.ShortSerializer shortSerializer = new NumberSerializers.ShortSerializer();
        assertNotNull(shortSerializer);
    }

    @Test
    public void testIntegerSerializer() {
        NumberSerializers.IntegerSerializer integerSerializer = new NumberSerializers.IntegerSerializer(Integer.class);
        assertNotNull(integerSerializer);
    }

    @Test
    public void testIntLikeSerializer() {
        NumberSerializers.IntLikeSerializer intLikeSerializer = new NumberSerializers.IntLikeSerializer();
        assertNotNull(intLikeSerializer);
    }

    @Test
    public void testLongSerializer() {
        NumberSerializers.LongSerializer longSerializer = new NumberSerializers.LongSerializer(Long.class);
        assertNotNull(longSerializer);
    }

    @Test
    public void testFloatSerializer() {
        NumberSerializers.FloatSerializer floatSerializer = new NumberSerializers.FloatSerializer();
        assertNotNull(floatSerializer);
    }

    @Test
    public void testDoubleSerializer() {
        NumberSerializers.DoubleSerializer doubleSerializer = new NumberSerializers.DoubleSerializer(Double.class);
        assertNotNull(doubleSerializer);
    }
}