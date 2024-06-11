import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.databind.JsonSerializer;

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
        assertNotNull(allDeserializers.get(Float.class.getName()));
        assertNotNull(allDeserializers.get(Float.TYPE.getName()));
        assertNotNull(allDeserializers.get(Double.class.getName()));
        assertNotNull(allDeserializers.get(Double.TYPE.getName()));
    }

    @Test
    public void testShortSerializer() {
        // Test typical case
        NumberSerializers.ShortSerializer shortSerializer = new NumberSerializers.ShortSerializer();
        assertEquals("Short", shortSerializer.getSchema(null, null).get("type").asText());

        // Test edge case
        try {
            shortSerializer.serialize(null, null, null);
            fail("NullPointerException should have been thrown");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    // Additional test methods for IntegerSerializer, IntLikeSerializer, LongSerializer, FloatSerializer, DoubleSerializer can be added here

}