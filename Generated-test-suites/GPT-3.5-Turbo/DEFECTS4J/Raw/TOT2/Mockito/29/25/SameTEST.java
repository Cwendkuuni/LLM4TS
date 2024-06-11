import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.matchers.Same;
import org.mockito.ArgumentMatcher;

public class SameTest {

    @Test
    public void testMatches_sameObject() {
        Object obj = new Object();
        Same same = new Same(obj);
        assertTrue(same.matches(obj));
    }

    @Test
    public void testMatches_differentObject() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Same same = new Same(obj1);
        assertFalse(same.matches(obj2));
    }

    @Test
    public void testDescribeTo_String() {
        String str = "test";
        Same same = new Same(str);
        Description description = new StringDescription();
        same.describeTo(description);
        assertEquals("same(\"test\")", description.toString());
    }

    @Test
    public void testDescribeTo_Character() {
        char ch = 'a';
        Same same = new Same(ch);
        Description description = new StringDescription();
        same.describeTo(description);
        assertEquals("same('a')", description.toString());
    }

    @Test
    public void testDescribeTo_Boolean() {
        boolean bool = true;
        Same same = new Same(bool);
        Description description = new StringDescription();
        same.describeTo(description);
        assertEquals("same(true)", description.toString());
    }

    @Test
    public void testDescribeTo_Integer() {
        int num = 10;
        Same same = new Same(num);
        Description description = new StringDescription();
        same.describeTo(description);
        assertEquals("same(10)", description.toString());
    }
}