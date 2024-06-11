import static org.junit.Assert.*;
import org.junit.Test;
import java.util.HashMap;
import com.fasterxml.jackson.databind.JsonSerializer;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        HashMap<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);
        
        assertTrue(allDeserializers.containsKey(Integer.class.getName()));
        assertTrue(allDeserializers.containsKey(Integer.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Long.class.getName()));
        assertTrue(allDeserializers.containsKey(Long.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Byte.class.getName()));
        assertTrue(allDeserializers.containsKey(Byte.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Short.class.getName()));
        assertTrue(allDeserializers.containsKey(Short.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Float.class.getName()));
        assertTrue(allDeserializers.containsKey(Float.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Double.class.getName()));
        assertTrue(allDeserializers.containsKey(Double.TYPE.getName()));
    }

    @Test
    public void testShortSerializer() {
        NumberSerializers.ShortSerializer shortSerializer = new NumberSerializers.ShortSerializer();
        assertNotNull(shortSerializer);
    }

    @Test
    public void testIntegerSerializer() {
        NumberSerializers.IntegerSerializer integerSerializer = new NumberSerializers.IntegerSerializer();
        assertNotNull(integerSerializer);
    }

    @Test
    public void testIntLikeSerializer() {
        NumberSerializers.IntLikeSerializer intLikeSerializer = new NumberSerializers.IntLikeSerializer();
        assertNotNull(intLikeSerializer);
    }

    @Test
    public void testLongSerializer() {
        NumberSerializers.LongSerializer longSerializer = new NumberSerializers.LongSerializer();
        assertNotNull(longSerializer);
    }

    @Test
    public void testFloatSerializer() {
        NumberSerializers.FloatSerializer floatSerializer = new NumberSerializers.FloatSerializer();
        assertNotNull(floatSerializer);
    }

    @Test
    public void testDoubleSerializer() {
        NumberSerializers.DoubleSerializer doubleSerializer = new NumberSerializers.DoubleSerializer();
        assertNotNull(doubleSerializer);
    }
}