import org.junit.Test;
import static org.junit.Assert.*;

public class JdkDeserializersTest {

    @Test
    public void testFind() {
        Class<?> rawType = UUID.class;
        String clsName = UUID.class.getName();

        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);

        assertNotNull(result);
        assertTrue(result instanceof UUIDDeserializer);
    }

    @Test
    public void testFind_NotInClassNames() {
        Class<?> rawType = String.class;
        String clsName = String.class.getName();

        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);

        assertNull(result);
    }
    
    // Test cases for other public methods

    // TODO: Add test cases for the remaining public methods

}