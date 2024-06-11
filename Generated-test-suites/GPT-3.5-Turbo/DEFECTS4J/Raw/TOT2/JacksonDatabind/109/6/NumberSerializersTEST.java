import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mock;
import java.util.Map;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;

public class NumberSerializersTest {

    private Map<String, JsonSerializer<?>> allDeserializers;

    @Mock
    private NumberSerializers.Base baseSerializer;

    @Test
    public void testAddAll() {
        NumberSerializers.addAll(allDeserializers);

        assertFalse(allDeserializers.isEmpty());
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
    public void testShortSerializer() throws Exception {
        ShortSerializer shortSerializer = new ShortSerializer();
        assertNotNull(shortSerializer);
        assertEquals(Short.class, shortSerializer.handledType());
    }

    @Test
    public void testIntegerSerializer() throws Exception {
        IntegerSerializer intSerializer = new IntegerSerializer(Integer.class);
        assertNotNull(intSerializer);
        assertEquals(Integer.class, intSerializer.handledType());
    }

    @Test
    public void testIntLikeSerializer() throws Exception {
        IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
        assertNotNull(intLikeSerializer);
        assertEquals(Number.class, intLikeSerializer.handledType());
    }

    @Test
    public void testLongSerializer() throws Exception {
        LongSerializer longSerializer = new LongSerializer(Long.class);
        assertNotNull(longSerializer);
        assertEquals(Long.class, longSerializer.handledType());
    }

    @Test
    public void testDoubleSerializer() throws Exception {
        DoubleSerializer doubleSerializer = new DoubleSerializer(Double.class);
        assertNotNull(doubleSerializer);
        assertEquals(Double.class, doubleSerializer.handledType());
    }
}