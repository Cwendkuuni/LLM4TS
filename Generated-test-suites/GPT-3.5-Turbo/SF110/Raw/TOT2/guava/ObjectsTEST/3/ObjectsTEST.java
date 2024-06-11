import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectsTest {
    
    @Test
    public void testEqual() {
        assertTrue(Objects.equal(null, null));
        assertFalse(Objects.equal(null, "Test"));
        assertTrue(Objects.equal("Test", "Test"));
    }
    
    @Test
    public void testHashCode() {
        assertEquals(Objects.hashCode("Test1", "Test2"), 360607283);
    }
    
    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper helper = Objects.toStringHelper("TestClassName");
        helper.addValue("Value1");
        helper.addValue(123);
        assertEquals(helper.toString(), "TestClassName{Value1, 123}");
    }
    
    @Test
    public void testFirstNonNull() {
        assertEquals(Objects.firstNonNull("Test", "Default"), "Test");
    }
    
    @Test
    public void testToStringHelper_add() {
        Objects.ToStringHelper helper = Objects.toStringHelper("TestClassName");
        helper.add("Name1", "Value1");
        assertEquals(helper.toString(), "TestClassName{Name1=Value1}");
    }
    
    // Add more test cases for other methods and edge cases
    
}