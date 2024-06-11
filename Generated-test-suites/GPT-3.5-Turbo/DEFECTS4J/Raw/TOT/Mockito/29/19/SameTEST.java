import static org.junit.Assert.*;
import org.junit.Test;

public class SameTest {

    @Test
    public void testMatches() {
        // Test when actual and wanted object reference is same
        Object obj1 = new Object();
        Same same = new Same(obj1);
        boolean result = same.matches(obj1);
        assertTrue(result);

        // Test when actual and wanted object reference is different
        Object obj2 = new Object();
        result = same.matches(obj2);
        assertFalse(result);
    }

    @Test
    public void testDescribeTo() {
        // Test when wanted object is a String
        Same same = new Same("test string");
        Description description = new StringDescription();
        same.describeTo(description);
        assertEquals("same(\"test string\")", description.toString());

        // Test when wanted object is a Character
        same = new Same('c');
        description = new StringDescription();
        same.describeTo(description);
        assertEquals("same('c')", description.toString());

        // Test when wanted object is not a String or Character
        same = new Same(10);
        description = new StringDescription();
        same.describeTo(description);
        assertEquals("same(10)", description.toString());
    }

}