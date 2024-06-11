import org.junit.Test;
import org.mockito.internal.matchers.Same;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;

public class SameTest {

    @Test
    public void testMatches() {
        Object obj = new Object();
        Same sameMatcher = new Same(obj);
        
        assertTrue(sameMatcher.matches(obj));
        assertFalse(sameMatcher.matches(new Object()));
    }

    @Test
    public void testDescribeTo() {
        Object stringObj = "test";
        Same sameMatcher = new Same(stringObj);
        
        // Test for String
        // The expected result should contains "same("test")"
        // The sample result could be here: same("test")
        assertEquals("same(\"test\")", sameMatcher.describeTo(null).toString());
        
        // Test for Character
        Object charObj = 'c';
        sameMatcher = new Same(charObj);
        // The expected result should contains "same('c')"
        // The sample result could be here: same('c')
        assertEquals("same('c')", sameMatcher.describeTo(null).toString());
    }

    @Test
    public void testAppendQuoting() {
        Object stringObj = "test";
        Object charObj = 'c';
        Same sameMatcher = new Same(stringObj);
        
        // Test for String
        Description description = new StringDescription();
        sameMatcher.appendQuoting(description);
        // The expected result should be "
        assertEquals("\"", description.toString());
        
        // Test for Character
        description = new StringDescription();
        sameMatcher = new Same(charObj);
        sameMatcher.appendQuoting(description);
        // The expected result should be '
        assertEquals("'", description.toString());
    }
}