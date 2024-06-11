import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectsTest {
    
    @Test
    public void testEqual() {
        assertTrue(Objects.equal("test", "test"));
        assertFalse(Objects.equal("test", "Test"));
    }
    
    @Test
    public void testHashCode() {
        Object[] objects = {1, "hello", 3.14};
        assertEquals(Objects.hashCode(objects), 63765707);
    }
    
    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper toStringHelper = Objects.toStringHelper("TestClass");
        assertNotNull(toStringHelper);
        assertEquals(toStringHelper.toString(), "TestClass{}");
    }
    
    @Test
    public void testFirstNonNull() {
        Integer first = null;
        Integer second = 5;
        assertEquals(Objects.firstNonNull(first, second), Integer.valueOf(5));
    }
    
    // Add more test cases for other methods here
    
}