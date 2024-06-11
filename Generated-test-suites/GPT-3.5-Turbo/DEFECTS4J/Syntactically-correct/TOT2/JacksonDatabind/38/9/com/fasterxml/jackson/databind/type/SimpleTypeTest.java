import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
    }
    
    @Test
    public void testConstruct() {
        assertThrows(IllegalArgumentException.class, () -> {
            SimpleType.construct(Map.class);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            SimpleType.construct(Collection.class);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            SimpleType.construct(String[].class);
        });
        
        SimpleType simpleType = SimpleType.construct(String.class);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
    }
    
    // Add more test cases for other methods as needed
    

}