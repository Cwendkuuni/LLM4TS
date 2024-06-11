import static org.junit.Assert.*;

import org.junit.Test;

public class JdkDeserializersTest {

    @Test
    public void testFind() {
        // Test case 1: rawType and clsName are both in _classNames
        Class<?> rawType1 = UUID.class;
        String clsName1 = rawType1.getName();
        JsonDeserializer<?> result1 = JdkDeserializers.find(rawType1, clsName1);
        assertTrue(result1 instanceof UUIDDeserializer);

        // Test case 2: rawType is in _classNames but clsName is not
        Class<?> rawType2 = AtomicBoolean.class;
        String clsName2 = Random.class.getName(); // Not in _classNames
        JsonDeserializer<?> result2 = JdkDeserializers.find(rawType2, clsName2);
        assertNull(result2);
    }
}