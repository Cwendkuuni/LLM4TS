import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectsTest {
    
    @Test
    public void testEqual() {
        assertTrue(Objects.equal(null, null));
        assertFalse(Objects.equal(null, "hello"));
        assertTrue(Objects.equal("hello", "hello"));
    }
    
    @Test
    public void testHashCode() {
        assertEquals(Objects.hashCode(1, 2, 3), Arrays.hashCode(new Object[]{1, 2, 3}));
        assertEquals(Objects.hashCode("a", "b", "c"), Arrays.hashCode(new Object[]{"a", "b", "c"}));
    }
    
    @Test
    public void testToStringHelper() {
        assertEquals("Example{}", Objects.toStringHelper("Example").toString());
        assertEquals("123{}", Objects.toStringHelper(123).toString());
    }
    
    // add more test cases for other methods in the Objects class
    
}