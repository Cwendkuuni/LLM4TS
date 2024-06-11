import org.junit.Test;
import static org.junit.Assert.*;
import org.hamcrest.Description;
import org.mockito.internal.matchers.Same;

public class SameTest {

    @Test
    public void testMatches() {
        Same same = new Same("test");
        assertTrue(same.matches("test"));
    }

    @Test
    public void testDescribeToWithString() {
        Description description = new Description();
        Same same = new Same("string");
        same.describeTo(description);
        assertEquals("same(\"string\")", description.toString());
    }

    @Test
    public void testDescribeToWithChar() {
        Description description = new Description();
        Same same = new Same('c');
        same.describeTo(description);
        assertEquals("same('c')", description.toString());
    }

    @Test
    public void testSameObjectInstance() {
        Object obj = new Object();
        Same same = new Same(obj);
        assertTrue(same.matches(obj));
    }

    @Test
    public void testDifferentObjectInstance() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Same same = new Same(obj1);
        assertFalse(same.matches(obj2));
    }

}