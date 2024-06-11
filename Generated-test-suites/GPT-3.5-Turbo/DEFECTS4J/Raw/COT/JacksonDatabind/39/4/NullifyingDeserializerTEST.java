import org.junit.Test;
import static org.junit.Assert.*;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserialize() {
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        assertNull(deserializer.deserialize(null, null));
    }
    
    @Test
    public void testDeserializeWithType() {
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        assertNull(deserializer.deserializeWithType(null, null, null));
    }
}