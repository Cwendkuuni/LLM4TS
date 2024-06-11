
import static org.junit.Assert.*;
import org.junit.Test;

public class NullifyingDeserializerTest {
    @Test
    public void testDeserialize() {
        // Test basic functionality
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        assertNull(deserializer.deserialize(null, null));
    }

    @Test
    public void testDeserializeWithType() {
        // Test basic functionality
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        assertNull(deserializer.deserializeWithType(null, null, null));
    }
}
