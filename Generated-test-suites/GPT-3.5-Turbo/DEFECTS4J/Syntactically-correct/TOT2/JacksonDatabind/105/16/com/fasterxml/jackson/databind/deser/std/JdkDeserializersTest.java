import static org.junit.Assert.*;
import org.junit.Test;

public class JdkDeserializersTest {

    @Test
    public void testFind() {
        // Typical use case
        Class<?> rawType = UUID.class;
        String clsName = UUID.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNotNull(deserializer);

        // Edge case: Class not in predefined list
        rawType = HashMap.class;
        clsName = HashMap.class.getName();
        deserializer = JdkDeserializers.find(rawType, clsName);
        assertNull(deserializer);

        // Edge case: Null rawType
        rawType = null;
        clsName = UUID.class.getName();
        deserializer = JdkDeserializers.find(rawType, clsName);
        assertNull(deserializer);

        // Exception handling: rawType is not handled
        rawType = ArrayList.class;
        clsName = ArrayList.class.getName();
        deserializer = JdkDeserializers.find(rawType, clsName);
        assertNull(deserializer);
    }
}