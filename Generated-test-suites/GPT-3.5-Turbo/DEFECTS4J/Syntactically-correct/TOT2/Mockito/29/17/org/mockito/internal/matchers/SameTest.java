import org.junit.Test;
import org.mockito.internal.matchers.Same;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SameTest {

    @Test
    public void testMatches_sameObject() {
        Object obj = new Object();
        Same sameMatcher = new Same(obj);
        assertTrue(sameMatcher.matches(obj));
    }

    @Test
    public void testMatches_differentObject() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Same sameMatcher = new Same(obj1);
        assertFalse(sameMatcher.matches(obj2));
    }

    @Test
    public void testDescribeTo_string() {
        String str = "test";
        Same sameMatcher = new Same(str);
        // test appending quoting for string
    }

    @Test
    public void testDescribeTo_char() {
        char c = 'c';
        Same sameMatcher = new Same(c);
        // test appending quoting for character
    }

    // Additional test cases for edge cases and exceptions can be added here

}