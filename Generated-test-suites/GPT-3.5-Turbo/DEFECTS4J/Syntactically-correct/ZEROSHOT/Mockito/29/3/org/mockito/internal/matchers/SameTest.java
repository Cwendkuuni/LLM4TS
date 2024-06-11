import org.junit.Test;
import org.mockito.internal.matchers.Same;
import org.hamcrest.Description;
import org.mockito.ArgumentMatcher;
import java.io.Serializable;

import static org.junit.Assert.*;

public class SameTest {

    private Same same;

    @Test
    public void testMatches_sameObject_returnTrue() {
        Object object = new Object();
        same = new Same(object);
        assertTrue(same.matches(object));
    }

    @Test
    public void testMatches_differentObject_returnFalse() {
        Object object1 = new Object();
        Object object2 = new Object();
        same = new Same(object1);
        assertFalse(same.matches(object2));
    }

    @Test
    public void testDescribeTo_stringObject_appendQuoting() {
        String str = "test";
        same = new Same(str);
        Description description = new Description();
        same.describeTo(description);
        assertEquals("same(\"test\")", description.toString());
    }

    @Test
    public void testDescribeTo_characterObject_appendQuoting() {
        Character ch = 'a';
        same = new Same(ch);
        Description description = new Description();
        same.describeTo(description);
        assertEquals("same('a')", description.toString());
    }
}