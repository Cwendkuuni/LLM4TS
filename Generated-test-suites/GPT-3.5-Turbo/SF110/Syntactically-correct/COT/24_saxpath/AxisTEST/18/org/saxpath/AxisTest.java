import org.junit.Test;
import static org.junit.Assert.*;

public class AxisTest {
    // Basic test cases for the lookup(int) method
    @Test
    public void testLookupIntChild() {
        assertEquals("child", Axis.lookup(1));
    }

    @Test
    public void testLookupIntDescendant() {
        assertEquals("descendant", Axis.lookup(2));
    }

    // Add more basic test cases for the remaining lookup(int) values

    // Edge case test cases for the lookup(int) method
    @Test
    public void testLookupIntInvalidAxis() {
        assertNull(Axis.lookup(0));
    }

    // Basic test cases for the lookup(String) method
    @Test
    public void testLookupStringChild() {
        assertEquals(1, Axis.lookup("child"));
    }

    @Test
    public void testLookupStringDescendant() {
        assertEquals(2, Axis.lookup("descendant"));
    }

    // Add more basic test cases for the remaining lookup(String) values

    // Exception handling test cases for the lookup(String) method
    @Test(expected = IllegalArgumentException.class)
    public void testLookupStringInvalidAxis() {
        Axis.lookup("invalid");
    }

    // Complete JUnit 4 test file starting and ending markers
    // 
    // ...
    // 
}