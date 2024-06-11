import static org.junit.Assert.*;

import org.junit.Test;

public class SameTest {

    @Test
    public void testMatches() {
        Same sameInstance = new Same("test");
        assertTrue(sameInstance.matches("test"));
        assertFalse(sameInstance.matches("different"));
    }

    @Test
    public void testDescribeTo() {
        Same sameInstance = new Same("test");
        // Create a Description for testing
        // Call describeTo method
        // Assert the description generated is as expected
    }

    @Test
    public void testAppendQuoting() {
        Same sameInstance = new Same("test");
        // Call appendQuoting with different instances (String and Character)
        // Assert the output is correct based on the type
    }
}