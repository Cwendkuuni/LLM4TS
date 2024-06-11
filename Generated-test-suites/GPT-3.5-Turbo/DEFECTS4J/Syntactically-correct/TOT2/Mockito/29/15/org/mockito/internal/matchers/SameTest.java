import static org.junit.Assert.*;

import org.hamcrest.Description;
import org.mockito.internal.matchers.Same;
import org.junit.Test;

public class SameTest {

    @Test
    public void testMatches() {
        Object obj = new Object();
        Same same = new Same(obj);
        assertTrue(same.matches(obj));
    }

    @Test
    public void testDescribeTo_String() {
        String str = "test";
        Same same = new Same(str);
        Description description = new Description();
        same.describeTo(description);
        assertEquals("same(\"test\")", description.toString());
    }

    @Test
    public void testDescribeTo_Character() {
        Character ch = 'a';
        Same same = new Same(ch);
        Description description = new Description();
        same.describeTo(description);
        assertEquals("same('a')", description.toString());
    }
    
    @Test
    public void testDescribeTo_Other() {
        Integer num = 123;
        Same same = new Same(num);
        Description description = new Description();
        same.describeTo(description);
        assertEquals("same(123)", description.toString());
    }
}