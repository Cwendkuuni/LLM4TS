
import static org.junit.Assert.*;
import org.junit.Test;

public class JdkDeserializersTest {
    
    @Test
    public void testFind_withValidClassAndClassName_shouldReturnDeserializer() {
        Class<?> rawType = UUID.class;
        String clsName = UUID.class.getName();
        
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        
        assertEquals(UUIDDeserializer.class, result.getClass());
    }
    
    @Test
    public void testFind_withInvalidClassAndClassName_shouldReturnNull() {
        Class<?> rawType = String.class;
        String clsName = String.class.getName();
        
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        
        assertNull(result);
    }
    
    @Test
    public void testFind_withNullClassAndValidClassName_shouldReturnNull() {
        Class<?> rawType = null;
        String clsName = UUID.class.getName();
        
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        
        assertNull(result);
    }
}
