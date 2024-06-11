import org.junit.Test;
import static org.junit.Assert.*;

public class JdkDeserializersTest {

    @Test
    public void testFind() {
        // Typical use case
        assertNull(JdkDeserializers.find(String.class, "java.lang.String"));

        // Edge case - UUID
        assertNotNull(JdkDeserializers.find(UUID.class, "java.util.UUID"));

        // Edge case - AtomicBoolean
        assertNotNull(JdkDeserializers.find(AtomicBoolean.class, "java.util.concurrent.atomic.AtomicBoolean"));

        // Error scenario
        assertNull(JdkDeserializers.find(null, "java.util.UUID"));
    }
}